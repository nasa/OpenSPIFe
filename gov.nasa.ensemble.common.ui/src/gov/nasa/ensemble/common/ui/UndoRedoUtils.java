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
package gov.nasa.ensemble.common.ui;


import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.operations.OperationHistoryActionHandler;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;

public class UndoRedoUtils {
	
	public static void setupUndoRedo(IActionBars bars, IWorkbenchPartSite site, IUndoContext context) {
		if (context == null) {
			throw new NullPointerException("context must not be null");
		}
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		//
		// undo
		OperationHistoryActionHandler undoHandler;
		undoHandler = new UndoActionHandler(site, context);
		undoHandler.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
		bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoHandler);
		//
		// redo
		OperationHistoryActionHandler redoHandler;
		redoHandler = new RedoActionHandler(site, context);
		redoHandler.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
		bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoHandler);
		//
		// refresh the workbench
		bars.updateActionBars();
	}

	public static void disposeUndoRedo(IActionBars bars) {
		disposeAction(bars, ActionFactory.UNDO.getId());
		disposeAction(bars, ActionFactory.REDO.getId());
	}

	private static void disposeAction(IActionBars bars, String id) {
		IAction handler = bars.getGlobalActionHandler(id);
		if (handler instanceof ActionFactory.IWorkbenchAction) {
			ActionFactory.IWorkbenchAction action = (ActionFactory.IWorkbenchAction) handler;
			action.dispose();
		}
	}

}
