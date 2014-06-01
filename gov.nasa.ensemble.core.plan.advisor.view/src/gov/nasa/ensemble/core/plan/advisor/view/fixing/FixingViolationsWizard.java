/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.core.plan.advisor.view.fixing;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.ThreadedCancellableRunnableWithProgress;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestedStartTime;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.advisor.preferences.PlanAdvisorPreferences;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class FixingViolationsWizard extends Wizard {

	private final FixingViolationsWizardSelectionPage selectionPage;
	private final FixingViolationsWizardFixesPage fixesPage;
	
	private ISelection selection;

	private boolean ranFixViolations = false;
	private ViolationFixes violationFixes = null;
	private ViolationFixes acceptedViolationFixes = null;

	private PlanAdvisor advisor;

	public FixingViolationsWizard(EPlan plan, ISelection selection) {
		PlanAdvisorMember advisorMember = WrapperUtils.getMember(plan, PlanAdvisorMember.class);
		List<PlanAdvisor> advisors = advisorMember.getAdvisors();
		for (PlanAdvisor advisor : advisors) {
			this.advisor = advisor;
			if (advisor.getName().contains("Europa")) {
				break;
			}
		}
		this.selection = selection;
		this.selectionPage = new FixingViolationsWizardSelectionPage(selection, advisorMember);
		this.fixesPage = new FixingViolationsWizardFixesPage(selection);
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		advisor = null;
		selection = null;
	}
	
	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		super.setContainer(wizardContainer);
		if (wizardContainer instanceof WizardDialog) {
			WizardDialog wizardDialog = (WizardDialog) wizardContainer;
			wizardDialog.addPageChangingListener(new ChangingListener());
			wizardDialog.addPageChangedListener(new ChangedListener(wizardDialog));
	        wizardDialog.setHelpAvailable(true);
		}
	}
	
	@Override
	public void addPages() {
		addPage(selectionPage);
		addPage(fixesPage);
	}

	@Override
	public boolean performFinish() {
		boolean result = runFixViolations();
		if (result) {
			acceptedViolationFixes = fixesPage.getAcceptedViolationFixes();
		}
		return result;
	}
	
	public ViolationFixes getAcceptedFixes() {
		return acceptedViolationFixes;
	}

	/**
	 * Create a runnable with progress for fix violations and run it in this wizard.
	 * Returns true if fix violations has been completed successfully
	 * @return true if fix violations has been completed successfully
	 */
	private synchronized boolean runFixViolations() {
		if (!ranFixViolations) {
			IRunnableWithProgress op = new ThreadedCancellableRunnableWithProgress() {
				@Override
				protected void doRun(IProgressMonitor monitor) throws Exception {
					doFixViolations(monitor);
				}
			};
			try {
				getContainer().run(true, true, op);
			} catch (InterruptedException e) {
				// cancelled
				return false;
			} catch (InvocationTargetException e) {
				return false;
			}
			fixesPage.setViolationFixes(violationFixes);
			ranFixViolations = true;
		}
		return true;
	}

	/**
	 * Do the actual work of contacting the advisor, updating the progress bar,
	 * and capturing the violation fixes.
	 * 
	 * @param advisor
	 * @param selection 
	 * @param monitor
	 * @throws InterruptedException 
	 */
	private void doFixViolations(IProgressMonitor monitor) throws InterruptedException {
		violationFixes = null;
		try {
			monitor.beginTask("Finding violation fixes...", 1);
			Object result = advisor.fixViolations(selection);
			if (result instanceof ViolationFixes) {
				violationFixes = filterNonchanges((ViolationFixes)result);
			}
		} finally {
			monitor.done();
		}
	}

	private ViolationFixes filterNonchanges(ViolationFixes fixes) {
		List<SuggestedStartTime> startTimes = fixes.getStartTimes();
		if (startTimes == null) {
			startTimes = Collections.emptyList();
		}
		int count = 0;
		List<SuggestedStartTime> filteredTimes = new ArrayList<SuggestedStartTime>(startTimes.size());
		for (SuggestedStartTime time : startTimes) {
			Date start = time.node.getMember(TemporalMember.class).getStartTime();
			if ((start == null) || start.before(time.earliest) || start.after(time.latest)) {
				filteredTimes.add(time);
			} else {
				count++;
			}
		}
		if (count != 0) {
			LogUtil.debug("plan advisor suggested moving " + count + " activities to the place they were already at.");
		}
		return new ViolationFixes(fixes.getAdvisor(), filteredTimes, fixes.getUnsatisfiedNodes(), fixes.getOpposingNodes());
	}

	/*
	 * Listener classes
	 */
	
	private final class ChangingListener implements IPageChangingListener {
		@Override
		public void handlePageChanging(PageChangingEvent event) {
			if (event.getTargetPage() == fixesPage) {
				if (!runFixViolations()) {
					event.doit = false;
				}
			}
		}
	}

	private final class ChangedListener implements IPageChangedListener {
		private boolean alreadyTried = false;
		private final WizardDialog wizardDialog;
		public ChangedListener(WizardDialog wizardDialog) {
			this.wizardDialog = wizardDialog;
		}
		@Override
		public void pageChanged(org.eclipse.jface.dialogs.PageChangedEvent event) {
			Shell shell = wizardDialog.getShell();
			shell.setText("Fixing violations");
			if (PlanAdvisorPreferences.isAutomaticallyStartFixing() && !alreadyTried) {
				alreadyTried = true;
				WidgetUtils.runLaterInDisplayThread(shell, new Runnable() {
					@Override
					public void run() {
						if (runFixViolations()) {
							wizardDialog.showPage(fixesPage);
						}
					}
				});
			}
		}
	}

}
