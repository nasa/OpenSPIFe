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
package gov.nasa.ensemble.common.ui.editor;
/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;

/**
 * This CellEditorActionHandler is somewhat based on the eclipse
 * CellEditorActionHandler.  However, it will automatically
 * switch the global action handler when a registered cell editor
 * is focused, and restore the previous action handler when the
 * cell editor loses focus.  So, it doesn't require registering
 * the editor actions or delegating to them.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p><p>
 * Example usage:
 * <pre>
 * actionHandler = new CellEditorActionHandler(this.getViewSite().getActionBars());
 * actionHandler.addCellEditor(textCellEditor1);
 * actionHandler.addCellEditor(textCellEditor2);
 * </pre>
 * </p>
 */
@SuppressWarnings("restriction")
public class CellEditorActionHandler {
    private final CutActionHandler cellCutAction = new CutActionHandler();
    private final CopyActionHandler cellCopyAction = new CopyActionHandler();
    private final PasteActionHandler cellPasteAction = new PasteActionHandler();
    private final DeleteActionHandler cellDeleteAction = new DeleteActionHandler();
    private final SelectAllActionHandler cellSelectAllAction = new SelectAllActionHandler();
    private final FindActionHandler cellFindAction = new FindActionHandler();
    private final UndoActionHandler cellUndoAction = new UndoActionHandler();
    private final RedoActionHandler cellRedoAction = new RedoActionHandler();

    private CellEditor activeEditor;

    private IPropertyChangeListener cellListener = new CellChangeListener();

    private final Listener controlListener = new ControlListener();

    private HashMap<Control, CellEditor> controlToEditor = new HashMap<Control, CellEditor>();

	private final IActionBars actionBars;
	
	private IAction oldCutAction;
	private IAction oldCopyAction;
	private IAction oldPasteAction;
	private IAction oldDeleteAction;
	private IAction oldSelectAllAction;
	private IAction oldFindAction;
	private IAction oldUndoAction;
	private IAction oldRedoAction;

    private class ControlListener implements Listener {
        @Override
		public void handleEvent(Event event) {
            switch (event.type) {
            case SWT.Activate:
                activeEditor = controlToEditor.get(event.widget);
                if (activeEditor != null) {
					activeEditor.addPropertyChangeListener(cellListener);
				}
                attachToActionBars();
                updateActionsEnableState();
                break;
            case SWT.Deactivate:
            case SWT.Dispose:
                if (activeEditor != null) {
					activeEditor.removePropertyChangeListener(cellListener);
				}
                activeEditor = null;
                restoreActionBars();
                updateActionsEnableState();
                break;
            default:
                break;
            }
        }
    }
    
    private class CellChangeListener implements IPropertyChangeListener {
        @Override
		public void propertyChange(PropertyChangeEvent event) {
            if (activeEditor == null) {
				return;
			}
            if (event.getProperty().equals(CellEditor.CUT)) {
                cellCutAction.setEnabled(activeEditor.isCutEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.COPY)) {
                cellCopyAction.setEnabled(activeEditor.isCopyEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.PASTE)) {
                cellPasteAction.setEnabled(activeEditor.isPasteEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.DELETE)) {
                cellDeleteAction.setEnabled(activeEditor.isDeleteEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.SELECT_ALL)) {
                cellSelectAllAction.setEnabled(activeEditor
                        .isSelectAllEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.FIND)) {
                cellFindAction.setEnabled(activeEditor.isFindEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.UNDO)) {
                cellUndoAction.setEnabled(activeEditor.isUndoEnabled());
                return;
            }
            if (event.getProperty().equals(CellEditor.REDO)) {
                cellRedoAction.setEnabled(activeEditor.isRedoEnabled());
                return;
            }
        }
    }

    private class CutActionHandler extends Action {
        protected CutActionHandler() {
            setId("CellEditorCutActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_CUT_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performCut();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isCutEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class CopyActionHandler extends Action {
        protected CopyActionHandler() {
            setId("CellEditorCopyActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_COPY_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performCopy();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isCopyEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class PasteActionHandler extends Action {
        protected PasteActionHandler() {
            setId("CellEditorPasteActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_PASTE_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performPaste();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isPasteEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class DeleteActionHandler extends Action {
        protected DeleteActionHandler() {
            setId("CellEditorDeleteActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_DELETE_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performDelete();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isDeleteEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class SelectAllActionHandler extends Action {
        protected SelectAllActionHandler() {
            setId("CellEditorSelectAllActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_SELECT_ALL_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performSelectAll();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isSelectAllEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class FindActionHandler extends Action {
        protected FindActionHandler() {
            setId("CellEditorFindActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_FIND_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performFind();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isFindEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class UndoActionHandler extends Action {
        protected UndoActionHandler() {
            setId("CellEditorUndoActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_UNDO_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performUndo();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isUndoEnabled());
                return;
            }
            setEnabled(false);
        }
    }

    private class RedoActionHandler extends Action {
		protected RedoActionHandler() {
            setId("CellEditorRedoActionHandler");//$NON-NLS-1$
            setEnabled(false);
            PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CELL_REDO_ACTION);
        }

        @Override
		public void runWithEvent(Event event) {
            if (activeEditor != null) {
                activeEditor.performRedo();
                return;
            }
        }

        public void updateEnabledState() {
            if (activeEditor != null) {
                setEnabled(activeEditor.isRedoEnabled());
                return;
            }
            setEnabled(false);
        }
    }

	/**
	 * Creates a <code>CellEditor</code> action handler for the global Cut, Copy, Paste, Delete, Select All, Find, Undo, and Redo of
	 * the action bar.
	 * 
	 * @param actionBar
	 *            the action bar to register global action handlers.
	 */
	public CellEditorActionHandler(IActionBars actionBar) {
		super();
		if (actionBar == null) {
			throw new NullPointerException("No action bar provided");
		}
		this.actionBars = actionBar;
	}

    /**
     * Adds a <code>CellEditor</code> to the handler so that the
     * Cut, Copy, Paste, Delete, Select All, Find, Undo, and Redo
     * actions are redirected to it when active.
     *
     * @param editor the <code>CellEditor</code>
     */
    public void addCellEditor(CellEditor editor) {
        if (editor == null) {
			return;
		}

        Control control = editor.getControl();
        if (control != null) {
	        controlToEditor.put(control, editor);
	        control.addListener(SWT.Activate, controlListener);
	        control.addListener(SWT.Dispose, controlListener);
	        control.addListener(SWT.Deactivate, controlListener);
	
	        if (control.isFocusControl()) {
	            activeEditor = editor;
	            editor.addPropertyChangeListener(cellListener);
	            updateActionsEnableState();
	        }
        }
    }

    /**
     * Disposes of this action handler
     */
    public void dispose() {

        Iterator<Control> itr = controlToEditor.keySet().iterator();
        while (itr.hasNext()) {
            Control control = itr.next();
            if (!control.isDisposed()) {
                control.removeListener(SWT.Activate, controlListener);
                control.removeListener(SWT.Dispose, controlListener);
                control.removeListener(SWT.Deactivate, controlListener);
            }
        }
        controlToEditor.clear();

        if (activeEditor != null) {
			activeEditor.removePropertyChangeListener(cellListener);
		}
        activeEditor = null;

    }

    /**
     * Removes a <code>CellEditor</code> from the handler
     * so that the Cut, Copy, Paste, Delete, Select All, Find
     * Undo, and Redo actions are no longer redirected to it.
     *
     * @param editor the <code>CellEditor</code>
     */
    public void removeCellEditor(CellEditor editor) {
        if (editor == null) {
			return;
		}

        if (activeEditor == editor) {
            activeEditor.removePropertyChangeListener(cellListener);
            activeEditor = null;
        }

        Control control = editor.getControl();
        if (control != null) {
            controlToEditor.remove(control);
            if (!control.isDisposed()) {
                control.removeListener(SWT.Activate, controlListener);
                control.removeListener(SWT.Deactivate, controlListener);
            }
        }
    }

    /**
     * Make the cell editor actions the target for the global action handler.
     */
    public void attachToActionBars() {
    	oldCutAction = actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
    	oldCopyAction = actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
    	oldPasteAction = actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
    	oldDeleteAction = actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
    	oldSelectAllAction = actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
    	oldFindAction = actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
    	oldUndoAction = actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
    	oldRedoAction = actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
	    
	    actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cellCutAction);
	    actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), cellCopyAction);
	    actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), cellPasteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), cellDeleteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), cellSelectAllAction);
	    actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), cellFindAction);
	    actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), cellUndoAction);
	    actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), cellRedoAction);
	    actionBars.updateActionBars();
    }

    public void restoreActionBars() {
	    actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), oldCutAction);
	    actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), oldCopyAction);
	    actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), oldPasteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), oldDeleteAction);
	    actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), oldSelectAllAction);
	    actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), oldFindAction);
	    actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), oldUndoAction);
	    actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), oldRedoAction);
	    actionBars.updateActionBars();
    }
    
    /**
     * Updates the enable state of the Cut, Copy,
     * Paste, Delete, Select All, Find, Undo, and
     * Redo action handlers
     */
    private void updateActionsEnableState() {
        cellCutAction.updateEnabledState();
        cellCopyAction.updateEnabledState();
        cellPasteAction.updateEnabledState();
        cellDeleteAction.updateEnabledState();
        cellSelectAllAction.updateEnabledState();
        cellFindAction.updateEnabledState();
        cellUndoAction.updateEnabledState();
        cellRedoAction.updateEnabledState();
    }
}
