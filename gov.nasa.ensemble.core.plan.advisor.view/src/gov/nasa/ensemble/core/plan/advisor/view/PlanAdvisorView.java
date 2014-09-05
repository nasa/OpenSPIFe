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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.AdvisorListener;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanPageBookView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.Page;

public class PlanAdvisorView extends PlanPageBookView {

	private static final String DEFAULT_MESSAGE = "Plan Advisors show possible problems with your plan.";
	public static final String ID = "gov.nasa.ensemble.core.plan.advisor.view.PlanAdvisorView";

	private final ViewIconListener viewIconListener = new ViewIconListener();

	public PlanAdvisorView() {
		super(DEFAULT_MESSAGE);
		setTitleImage(PlanAdvisorViewImage.unknown);
	}

	@Override
	protected Page createPage(IEditorPart editor, PlanEditorModel model) {
		return new PlanAdvisorPage(editor, model);
	}

	@Override
	protected void pageActivated(IPage page) {
		if (page instanceof PlanAdvisorPage) {
			PlanAdvisorPage planAdvisorPage = (PlanAdvisorPage) page;
			EPlan plan = planAdvisorPage.getPlan();
			PlanAdvisorMember planAdvisorMember = PlanAdvisorMember.get(plan);
			viewIconListener.setPlanAdvisorMember(planAdvisorMember);
		} else {
			viewIconListener.setPlanAdvisorMember(null);
		}
	}

	@Override
	public void dispose() {
		viewIconListener.setPlanAdvisorMember(null);
		super.dispose();
	}

	private void setTitleImage(PlanAdvisorViewImage image) {
		setTitleImage(image.getImage());
	}

	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return new ContextProvider(ID);
		}

		if (key.equals(TreeViewer.class)) {
			IPage page = this.getCurrentPage();
			if (page instanceof PlanAdvisorPage) {
				PlanAdvisorPage planAdvisorPage = (PlanAdvisorPage) page;
				return planAdvisorPage.getPlanAdvisorTreeViewer();
			}

			else {
				return null;
			}
		}
		return super.getAdapter(key);
	}

	private void computeAndDisplayStatusSummary(PlanAdvisorMember planAdvisorMember) {
		if (planAdvisorMember == null) {
			WidgetUtils.runInDisplayThread(getPageBook(), new Runnable() {
				@Override
				public void run() {
					updateImageDescription(PlanAdvisorViewImage.unknown, " ");
				}
			});
			return;
		}
		int unwaived = 0, waived = 0, fixed = 0;
		List<ViolationTracker> violationTrackers = planAdvisorMember.getViolationTrackers();
		for (ViolationTracker violationTracker : violationTrackers) {
			Violation violation = violationTracker.getViolation();
			if (violation.isOutOfDate()) {
				waived++;
			} else if (violation.isCurrentlyViolated()) {
				if (violation.isWaivedByRule()) {
					waived++;
				} else if (violation.isWaivedByInstance()) {
					waived++;
				} else {
					unwaived++;
				}
			} else {
				fixed++;
			}
		}
		displayStatusSummary(unwaived, waived, fixed, planAdvisorMember.getUpdatingAdvisors());
	}

	private void displayStatusSummary(final int unwaived, final int waived, final int fixed, final List<PlanAdvisor> updatingAdvisors) {
		WidgetUtils.runInDisplayThread(getPageBook(), new Runnable() {
			@Override
			public void run() {
				displayStatusSummaryW(unwaived, waived, fixed, updatingAdvisors);
			}
		});
	}

	private String oldDescription = "";
	private PlanAdvisorViewImage oldImage = null;

	private void displayStatusSummaryW(final int unwaived, final int waived, final int fixed, final List<PlanAdvisor> updatingAdvisors) {
		String contentDescription = "";
		PlanAdvisorViewImage image;
		if (unwaived != 0) {
			image = PlanAdvisorViewImage.inconsistent;
			contentDescription += unwaived + " unfixed";
		} else {
			image = PlanAdvisorViewImage.consistent;
		}
		if (waived != 0) {
			if (contentDescription.length() != 0) {
				contentDescription += ", ";
			}
			contentDescription += waived + " waived";
		}
		if (fixed != 0) {
			if (contentDescription.length() != 0) {
				contentDescription += ", ";
			}
			contentDescription += fixed + " fixed";
		}
		if (!updatingAdvisors.isEmpty()) {
			image = PlanAdvisorViewImage.thinking;
			List<String> advisorNames = new ArrayList<String>(updatingAdvisors.size());
			for (PlanAdvisor advisor : updatingAdvisors) {
				advisorNames.add(advisor.getName());
			}
			if (contentDescription.length() != 0) {
				contentDescription += ", ";
			}
			contentDescription += "pending";
			// contentDescription += ": " + CommonUtils.getListText(advisorNames);
		}
		if (contentDescription == "") {
			contentDescription = "no violations";
		}
		updateImageDescription(image, contentDescription);
	}

	private void updateImageDescription(PlanAdvisorViewImage image, String contentDescription) {
		synchronized (this) {
			if (oldImage != image) {
				setTitleImage(image);
				oldImage = image;
			}
			if (!oldDescription.equals(contentDescription)) {
				setContentDescription(contentDescription);
				oldDescription = contentDescription;
			}
		}
	}

	private class ViewIconListener extends AdvisorListener {
		private PlanAdvisorMember planAdvisorMember;

		public void setPlanAdvisorMember(PlanAdvisorMember planAdvisorMember) {
			if (this.planAdvisorMember != null) {
				this.planAdvisorMember.removeViolationsListener(this);
			}
			this.planAdvisorMember = planAdvisorMember;
			computeAndDisplayStatusSummary(this.planAdvisorMember);
			if (this.planAdvisorMember != null) {
				this.planAdvisorMember.addViolationsListener(this);
			}
		}

		@Override
		public void advisorsUpdating() {
			computeAndDisplayStatusSummary(planAdvisorMember);
		}

		@Override
		public void advisorsUpdated() {
			computeAndDisplayStatusSummary(planAdvisorMember);
		}

		@Override
		public void violationsAdded(Set<ViolationTracker> violations) {
			computeAndDisplayStatusSummary(planAdvisorMember);
		}

		@Override
		public void violationsRemoved(Set<ViolationTracker> violations) {
			computeAndDisplayStatusSummary(planAdvisorMember);
		}

	}
}
