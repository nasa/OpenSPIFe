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
package gov.nasa.arc.spife.core.plan.pear.view.ui;

import gov.nasa.arc.spife.core.plan.pear.view.internal.RemoveProfileOperation;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.profile.tree.ProfileTreeView;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.resources.profile.impl.StructuralFeatureProfileImpl;
import gov.nasa.ensemble.core.plan.resources.ui.view.PlanProfileTreePage;

import java.util.List;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class DeleteProfileMemberAction implements IViewActionDelegate {

	private IViewPart view;
	
	@Override
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(workbench);
		EPlan plan = null;
		if (model != null) {
			plan = model.getEPlan();
		}
		if (model == null || plan == null) {
			LogUtil.error("Could not retrieve the current plan.");
			return;
		}
		// Delete the selected profiles
		ProfileTreeView profileTreeView = (ProfileTreeView)view;
		PlanProfileTreePage planProfileTreePage = (PlanProfileTreePage) profileTreeView.getCurrentPage();
		if (planProfileTreePage == null) {
			LogUtil.error("Could not retrieve current PlanProfileTreePage.");
			return;
		}
		// SPF-10328 -- Changed the call from removeRows to getSelectedProfiles as .condition files
		// should not be affected until the plan editor is saved in order for undo to work properly
		List<Profile> removedProfileList = planProfileTreePage.getSelectedProfiles();
		// Set the operation for undo/redo functionality
		final IUndoableOperation op = new RemoveProfileOperation(plan, removedProfileList);
		CommonUtils.execute(op, model.getUndoContext());
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

		boolean isDeletable = false;
		IStructuredSelection select = (IStructuredSelection) selection;
		for(Object item : select.toList()) {
			// Able to delete if it is a Profile and not defined in the Activity Dictionary
			if (item instanceof Profile && 
					!(item instanceof StructuralFeatureProfileImpl)) {
				isDeletable = true;
			}
			else {
				isDeletable = false;
				break;
			}
		}
		action.setEnabled(isDeletable);
	}

	@Override
	public void init(IViewPart view) {
		this.view = view;
	}
}
