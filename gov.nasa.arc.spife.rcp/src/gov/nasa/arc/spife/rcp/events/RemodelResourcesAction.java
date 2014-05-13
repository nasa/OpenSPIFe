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
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferences;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class RemodelResourcesAction implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		if (!PlanEditorPreferences.isAutomaticResourceModeling()) {
			for (EPlan plan : PlanEditorUtil.getOpenPlans()) {
				ResourceUpdater updater = WrapperUtils.getMember(plan, ResourceUpdater.class);
				if (updater != null && updater.isQueueEnabled()) {
					try {
						updater.setQueueEnabled(false);
					} finally {
						updater.setQueueEnabled(true);
					}
				}
				PlanAdvisorMember planAdvisorMember = PlanAdvisorMember.get(plan);
				if (planAdvisorMember != null && !planAdvisorMember.isListening()) {
					try {
						planAdvisorMember.setListening(true);
					} finally {
						planAdvisorMember.setListening(false);
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// Nothing to do.
	}

	@Override
	public void dispose() {
		// Nothing to do.
	}

	@Override
	public void init(IWorkbenchWindow window) {
		// Nothing to do.
	}

}
