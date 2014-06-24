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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanPlugin;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.constraints.ApprovableAction;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Move a generic orbit event and its associated activities to a different planned orbit event.
 * TODO:  Consider making this a subclass of DrudgerySavingAction.
 * @since SPF-7982.
 * @author kanef
 *
 */
public class MoveToOrbitAction extends ApprovableAction {

	private static MoveToOrbitAction moveToOrbitAction;
	public static final String ID = MoveToOrbitAction.class.getName();
	
	@Override
	public String getId()
	{
		return ID;
	}
	
	public static MoveToOrbitAction getInstance()
	{
		if(moveToOrbitAction == null)
			moveToOrbitAction = new MoveToOrbitAction();
		
		return moveToOrbitAction;
	}
	
	private IWorkbenchWindow window;
	private String dialogTitle = "Move to " + OrbitEventUtil.ORBIT_NAME;
	private String dialogText = "Enter " + OrbitEventUtil.ORBIT_NAME + "  id";
	IInputValidator validator = null; // design does not call for validation
	
	
	public MoveToOrbitAction() {
		super();
		setText("Move to " + OrbitEventUtil.ORBIT_NAME);
		setToolTipText("Move activities to a different " + OrbitEventUtil.ORBIT_NAME);
	}

	@Override
	public void runWithEvent(IAction action, Event event) {
		updateEnablement(action);
		if (action.isEnabled()) {
			ISelection currentSelection = getSelection();
			Set<EPlanElement> selected = PlanEditorUtil.emfFromSelection(currentSelection);
			final MoveToOrbitOperation op = new MoveToOrbitOperation(selected);
			String errorMsg = op.analyzeMistakenSelection();
			if (errorMsg != null) {
				LogUtil.warn(errorMsg);
				ErrorDialog.openError(WidgetUtils.getShell(), "Nothing to Move To " + OrbitEventUtil.ORBIT_NAME,
						"No orbit-related plan elements selected.",
						new ExceptionStatus(EditorPlugin.ID, errorMsg, new ExecutionException(selected.toString())));
				return;
			}
			try {
				String orbitId = promptForValue();
				if (orbitId != null) {
					op.setOrbitId(orbitId);
					IUndoContext undoContext = getUndoContext();
					CommonUtils.execute(op, undoContext);
					String errorMsg2 = op.analyzeMissingOrbitDestination();
					if (errorMsg2 != null) {
						LogUtil.warn(errorMsg2);
						//TODO:  Use the new WidgetUtils.showErrorToUser method.
						ErrorDialog.openError(WidgetUtils.getShell(), "Move To " + OrbitEventUtil.ORBIT_NAME,
								"Move To " + OrbitEventUtil.ORBIT_NAME +  " may not have done what you expected.",
								new ExceptionStatus(EditorPlugin.ID, errorMsg2, new ExecutionException(selected.toString())));
						return;
					}
				}
			}
			catch (Exception e) {
					IStatus error = new ExceptionStatus(EditorPlugin.ID, e.getMessage(), e);
					ErrorDialog.openError(WidgetUtils.getShell(), "An error occurred", "That didn't work.", error);
				}
			}
		}

	@Override
	public void runWithEvent(Event event) {
		runWithEvent(this, event);
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void dispose() {
		this.window = null;
	}
	
	private String promptForValue() {
		if (window == null) {
			window = PlanPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		}
		if (window != null) {
			InputDialog d = new InputDialog(window.getShell(), dialogTitle, dialogText, "", validator);
			int code = d.open();
			if (code == Window.OK) {
				return d.getValue();
			}
		}
		return null;
	}

	@Override
	protected void updateEnablement(IAction action) {
		// TODO Auto-generated method stub
		
	}
	
	

}
