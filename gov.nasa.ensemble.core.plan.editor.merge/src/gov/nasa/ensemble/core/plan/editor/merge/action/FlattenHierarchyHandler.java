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
package gov.nasa.ensemble.core.plan.editor.merge.action;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

public class FlattenHierarchyHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 0;
	}
	
	@Override
	protected void selectionChanged(ISelection selection) {
		//dont care about selection
	}
	
	@Override
	protected void partActivated(IWorkbenchPart part) {
		if (part instanceof MultiPagePlanEditor) {
			part = ((MultiPagePlanEditor) part).getCurrentEditor();
		}
		boolean isFlattenable = (part instanceof IToggleFlattenEditor);
		setBaseEnabled(isFlattenable);
	}

	private void updateHandler(IWorkbenchPart part) {
		boolean isFlattenable = (part instanceof IToggleFlattenEditor);
		boolean isChecked = (isFlattenable ? ((IToggleFlattenEditor) part).isFlattened() : false);
		setBaseEnabled(isFlattenable);
		try {
			setCommandState(getCommand(), isChecked);
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		IToggleFlattenEditor editor = getFlattenableEditor();
		if (editor != null) {
			editor.toggleFlatten();
			try {
				setCommandState(event.getCommand(), editor.isFlattened());
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
		return null;
	}
	
	private IToggleFlattenEditor getFlattenableEditor() {
		MultiPagePlanEditor editor = getActiveEditor();
		if (editor != null) {
    		IEditorPart currentEditor = editor.getCurrentEditor();
    		if (currentEditor instanceof IToggleFlattenEditor) {
    			return (IToggleFlattenEditor)currentEditor;
    		}
    	}
	    return null;
    }
	

	@Override
	public String getCommandId() {
		return FLATTEN_HIERARCHY_COMMAND_ID;
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		Object page = event.getSelectedPage();
		if (page instanceof IWorkbenchPart) {
			updateHandler((IWorkbenchPart) page);
		}
	}

}
