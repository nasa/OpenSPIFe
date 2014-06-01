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

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Toggle the editor page when run. Ignore actions and events.
 */
public class NextEditorPageAction extends Action implements IWorkbenchWindowActionDelegate, IActionDelegate2 {

	/**
	 * Toggle the editor page.
	 * @param e ignored
	 */
	@Override
	public void runWithEvent(Event e) {
		final IEditorPart editorPart = EditorPartUtils.getCurrent();
		if (editorPart instanceof MultiPagePlanEditor) {
			WidgetUtils.runLaterInDisplayThread(editorPart.getSite().getShell(), new Runnable() {
				@Override
				public void run() {
					((MultiPagePlanEditor) editorPart).prevPage();
				}
			});
		}
	}

	/**
	 * Toggle the editor page.
	 */
	@Override
	public void run() {
		runWithEvent(null);
	}
	
	// ---- IWorkbenchWindowActionDelegate
	// ------------------------------------------------

	/**
	 * Because this class implements interface IActionDelegate2, runWithEvent(IAction, Event)
	 * will be called instead of this.
	 * @param IAction ignored
	 */
	@Override
	public void run(IAction action) {
		run();
	}

	/** There is nothing to dispose. */
	@Override
	public void dispose() {
		// do nothing.
	}
	
	/**
	 *  There is nothing to initialize. 
	 *  @param window ignored
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		// do nothing.
	}

	/**
	 * Nothing need be done when the selection changes.
	 * @param action ignored
	 * @param selection ignored
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing. Action doesn't depend on selection.
	}
	
	// ---- IActionDelegate2
	// ------------------------------------------------

	/**
	 * Toggle the editor page.
	 * @param action ignored
	 * @param event ignored
	 */
	@Override
	public void runWithEvent(IAction action, Event event) {
		runWithEvent(event);
	}
	
	/**
	 * There is nothing to initialize.
	 * @param action ignored
	 */
	@Override
	public void init(IAction action) {
		// do nothing.
	}
	
}
