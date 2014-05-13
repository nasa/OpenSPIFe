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
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferences;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
/**
 * 
 *
 */
public class ToggleResourceModelingAction implements IWorkbenchWindowActionDelegate
{
	public static final String ID = ToggleResourceModelingAction.class.getName();
	
	@Override
	public void run(IAction action) {
		boolean oldValue = PlanEditorPreferences.isAutomaticResourceModeling();
		boolean newValue = !oldValue;
		PlanEditorPreferences.setAutomaticResourceModeling(newValue);
		updateChecked(action);
		for (EPlan plan : PlanEditorUtil.getOpenPlans()) {
			PlanEditorModel.RESOURCE_MODELING_CONTROLLER.enableAutomaticResourceModeling(plan, newValue);
		}
	}

	private void updateChecked(IAction action) {
		action.setChecked(PlanEditorPreferences.isAutomaticResourceModeling());
	}
	
	@Override
	public void init(IWorkbenchWindow window) {
		// Nothing to do.
	}

	@Override
	public void dispose() {
		// Nothing to do.		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		updateChecked(action);
	}

}
