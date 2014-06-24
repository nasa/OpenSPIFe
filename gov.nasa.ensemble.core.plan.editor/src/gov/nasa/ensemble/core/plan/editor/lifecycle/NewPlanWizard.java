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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.IPlanElementMemberFactory;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * The NewPlanWizard creates and opens a new plan file
 * that can then be edited by the Ensemble Plan Editor.
 */
public class NewPlanWizard extends Wizard implements INewWizard, MissionExtendable {
	
	/** The page that solicits information about the plan to be created. */
	protected NewPlanWizardPage page;
	
	/** Saved from the call to init(). */
	protected ISelection selection;
	
	/** The logger for the NewPlanWizard. */
	protected static final Logger trace = Logger.getLogger(NewPlanWizard.class);
	
	
	/**
	 * Constructor for NewPlanWizard. Give it a title and progress monitor, but not previous and
	 * next buttons.
	 */
	public NewPlanWizard() {
		super();
		setNeedsProgressMonitor(true);
		setWindowTitle("New Plan");
		setForcePreviousAndNextButtons(false);		
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		page = createNewPlanWizardPage();
		addPage(page);
	}
	
	/**
	 * Invoke the factory to create a new instance of the NewPlanWizardPage.
	 * @return a new instance of the NewPlanWizardPage
	 */
	protected NewPlanWizardPage createNewPlanWizardPage() {
		return NewPlanWizardPage.createInstance();
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 * @return whether a new plan was created
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			return false;
		}
		return true;
	}

	/**
	 * This is what is run in the Container as a consequence of the user clicking the "Finish"
	 * button. It presents a page to solicit new-plan data with a progress monitor and
	 * runs it in its own thread. Log success and throw an exception on failure. On success,
	 * open a plan editor with the newly-created plan.
	 * As a side effect, it causes the IPlanElementMemberFactory instances to be created.
	 * @param monitor
	 */
	private void doFinish(final IProgressMonitor monitor) {
		monitor.beginTask("Opening a new plan...", 5);
		@SuppressWarnings("unused") // eagerly create the factories
        List<IPlanElementMemberFactory> factories = IPlanElementMemberFactory.FACTORIES;
		monitor.worked(2);
		Display display = getShell().getDisplay();
		final EPlan[] plans = new EPlan[] { null };
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				plans[0] = page.createNewPlan(monitor);
			}
		});
		final EPlan plan = plans[0];
		if (plan == null) {
			throw new NullPointerException("The plan wizard page failed to create a plan");
		}
		EnsembleUsageLogger.logUsage("PlanLifecycle.createPlan", plan.getName());
		monitor.worked(3);
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MultiPagePlanEditor.openEditor(plan);
			}
		});
		monitor.done();
	}
	
	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
}
