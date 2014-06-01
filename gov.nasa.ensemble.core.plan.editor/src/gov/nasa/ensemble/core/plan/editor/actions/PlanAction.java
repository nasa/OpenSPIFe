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
package gov.nasa.ensemble.core.plan.editor.actions;

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public abstract class PlanAction extends Action implements IWorkbenchWindowActionDelegate
{
	protected IWorkbenchWindow window;
	protected IAction action;
	
	protected boolean canMutate(){
		return true;
	}
	
	/**
	 *
	 */
	@Override
	public void run(IAction action)
	{
		// cache the action
		this.action = action;
		
		// check if a plan is selected
		IEditorPart editor = EditorPartUtils.getCurrent(window);
		PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editor.getEditorInput());
		if (model == null || !(editor instanceof MultiPagePlanEditor))
		{
			MessageDialog.openInformation(window.getShell(), "Invalid Selection", "No plan is currently selected.");
			return;
		}
		if( ! canMutate(     )){
			return;
		}
		// get the plan from the part
		EPlan plan                  = model.getEPlan();
		
		editor.getTitleImage();
		
		runImpl((MultiPagePlanEditor) editor, plan);
	}

	public abstract void runImpl(MultiPagePlanEditor editor, EPlan plan);
	
	
	@Override
	public void init(IWorkbenchWindow window)                          { this.window = window; }
	@Override
	public void dispose()                                              { /* do nothing */ } 
	@Override
	public void selectionChanged(IAction action, ISelection selection) { /* do nothing */ }
}
