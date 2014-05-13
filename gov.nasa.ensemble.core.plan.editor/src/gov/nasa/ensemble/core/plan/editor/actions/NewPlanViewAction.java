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

import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

/**
 * Simple action which creates a new EDITOR for the selected plan. NOTE: this is functionally equivelent to
 * the NewEditorAction code, except that it is using the standard workbench Action delegates (IE: via the plugin.xml) instead of
 * having to be instantiated through code. 
 * 
 * Enables/disables based on a plan tab being selected.
 * 
 * Looked into getting the new editor to show up in a different editor group, but it is not 
 * easily doable. The code to do that exsists in the PartStack.java from eclipse.
 * 
 * @author alexeiser
 *
 */
public class NewPlanViewAction implements IWorkbenchWindowActionDelegate {

	/** The workbench window in which it will work. Stored from init(). */
	protected IWorkbenchWindow window;

	/** There is nothing to dispose. */
	@Override
	public void dispose() {
		// Nothing to dispose.
	}
	
	/**
	 * Store the workbench window in which it will work.
	 * @param window the workbench window in which it will work
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;	
	}

	/**
	 * Open the plan editor if there is one. There must be an active page in the workbench and
	 * an active editor in the window's active page; furthermore, that editor's site must have
	 * a non-null ID.
	 * @param action ignored
	 */
	@Override
	public void run(IAction action) {
		if (window!=null) {
			IWorkbenchPage page = window.getActivePage();
			
			IWorkbenchPage workbenchPage = window.getActivePage();
			IEditorPart editor = workbenchPage.getActiveEditor();
			
			if (page == null || editor == null) {
				return;
			}
			String editorId = editor.getSite().getId();
			if (editorId == null) {
				return;
			}
			try {
				page.openEditor(editor.getEditorInput(), editorId, true, IWorkbenchPage.MATCH_NONE);
	        } catch (PartInitException e) {
	        	Logger.getLogger(NewPlanViewAction.class).error("can't open plan view", e);
			}
		}
        
	}

	/**
	 * If init() has been called and the active part is a plan editor, enable the action;
	 * otherwise disable it.
	 * @param action this action
	 * @param selection ignored
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (window != null) {
			IWorkbenchPage workbenchPage = window.getActivePage();
			IEditorPart activeEditor = workbenchPage.getActiveEditor();
			
			// Check if editor is a Plan Editor
			if ((activeEditor instanceof MultiPagePlanEditor)
				&& (workbenchPage.getActivePart() == activeEditor)) 
			{
				action.setEnabled(true);
			} else {
				action.setEnabled(false);
			}

		}
	}

}
