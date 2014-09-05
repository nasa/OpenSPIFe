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

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.plan.advisor.AdvisorListener;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class RemoveSelectedFixedViolationsAction extends Action {

	private final PlanAdvisorMember planAdvisorMember;
	private final PlanAdvisorTreeViewer planAdvisorTreeViewer;

	public RemoveSelectedFixedViolationsAction(PlanAdvisorTreeViewer viewer, PlanAdvisorMember planAdvisorMember) {
		this.planAdvisorTreeViewer = viewer;
		this.planAdvisorMember = planAdvisorMember;
		planAdvisorMember.addViolationsListener(new ViolationsUpdatedListener());
		planAdvisorTreeViewer.addSelectionChangedListener(new SelectionChangedListener());
		setText("Remove selected fixed violations");
		setToolTipText("Remove selected fixed violations");
		setImageDescriptor(Activator.getImageDescriptor("icons/rem_co_003.gif"));
		setDisabledImageDescriptor(Activator.getImageDescriptor("icons/rem_co.gif"));
		updateEnablement(planAdvisorTreeViewer.getSelection());
	}

	@Override
	public void run() {
		ISelection selection = planAdvisorTreeViewer.getSelection();
		List<ViolationTracker> fixedViolationTrackers = getFixedViolationTrackers(selection);
		planAdvisorMember.removeViolationTrackers(fixedViolationTrackers);
	}

	private List<ViolationTracker> getFixedViolationTrackers(ISelection selection) {
		List<ViolationTracker> fixedViolationTrackers = new ArrayList<ViolationTracker>();
		if (selection instanceof IStructuredSelection) {
			List<?> objects = ((IStructuredSelection) selection).toList();
			for (Object object : objects) {
				if (object instanceof ViolationTracker) {
					ViolationTracker violationTracker = (ViolationTracker) object;
					if (!violationTracker.getViolation().isCurrentlyViolated()) {
						fixedViolationTrackers.add(violationTracker);
					}
				}
			}
		}
		return fixedViolationTrackers;
	}

	private void updateEnablement(ISelection selection) {
		List<ViolationTracker> fixedViolations = getFixedViolationTrackers(selection);
		setEnabled(!fixedViolations.isEmpty());
	}

	private final class ViolationsUpdatedListener extends AdvisorListener {
		@Override
		public void advisorsUpdated() {
			WidgetUtils.runInDisplayThread(planAdvisorTreeViewer.getControl(), new Runnable() {
				@Override
				public void run() {
					ISelection selection = planAdvisorTreeViewer.getSelection();
					updateEnablement(selection);
				}
			});
		}
	}

	private class SelectionChangedListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(final SelectionChangedEvent event) {
			WidgetUtils.runInDisplayThread(planAdvisorTreeViewer.getControl(), new Runnable() {
				@Override
				public void run() {
					ISelection selection = event.getSelection();
					updateEnablement(selection);
				}
			});
		}

	}

}
