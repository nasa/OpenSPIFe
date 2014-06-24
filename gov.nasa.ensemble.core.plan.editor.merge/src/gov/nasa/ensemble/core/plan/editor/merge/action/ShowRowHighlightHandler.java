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
import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.WorkbenchPart;

/**
 * @author ideliz
 */
public class ShowRowHighlightHandler extends AbstractPlanEditorHandler implements IPageChangedListener {

	@Override
	protected void selectionChanged(ISelection selection) {
		//independent of selection
	}
	
	@Override
	protected void partActivated(IWorkbenchPart part) {
		if (part instanceof MultiPagePlanEditor) {
			part = ((MultiPagePlanEditor) part).getCurrentEditor();
		}
		Object adapter = ((WorkbenchPart) part).getAdapter(MergeRowHighlightDecorator.class);
		MergeRowHighlightDecorator decorator = (MergeRowHighlightDecorator) adapter;
		updateState(decorator);
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws  ExecutionException {
		MultiPagePlanEditor editor = getActiveEditor();
		if (editor != null) {
    		IEditorPart currentEditor = editor.getCurrentEditor();
    		MergeRowHighlightDecorator decorator = (MergeRowHighlightDecorator) currentEditor.getAdapter(MergeRowHighlightDecorator.class);
			if (decorator != null) {
				decorator.toggleRowHighlightVisibility();
				setCommandState(event.getCommand(), decorator.isRowColorHighlightingVisible());
				if (currentEditor instanceof AbstractPlanEditorPart) {
					((AbstractPlanEditorPart) currentEditor).refresh();
				}
			}
		}
		return null;
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		Object page = event.getSelectedPage();
		if (page instanceof IWorkbenchPart) {
			Object adapter = ((WorkbenchPart) page).getAdapter(MergeRowHighlightDecorator.class);
			MergeRowHighlightDecorator decorator = (MergeRowHighlightDecorator) adapter;
			updateState(decorator);
		}
	}

	private void updateState(MergeRowHighlightDecorator decorator) {
		boolean hasDecorator = decorator != null && decorator.isRowColorHighlightingEnabled();
		boolean isChecked = (hasDecorator ? decorator != null && decorator.isRowColorHighlightingVisible() : false);
		setBaseEnabled(hasDecorator);
		try {
			setCommandState(getCommand(), isChecked);
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public String getCommandId() {
		return SHOW_ROW_HIGHLIGHT_COMMAND_ID;
	}

}
