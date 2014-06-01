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

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.AbstractUndoableOperationAction;
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.operations.ClipboardCutOperation;
import gov.nasa.ensemble.common.ui.operations.ClipboardPasteOperation;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.operations.SelectAllOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.EditorPart;

public abstract class AbstractEnsembleEditorPart extends EditorPart implements IEnsembleEditorPart {

	protected final AbstractUndoableOperationAction cutHandler = new EditorUndoableOperationAction(ActionFactory.CUT);
	protected final AbstractUndoableOperationAction copyHandler = new EditorUndoableOperationAction(ActionFactory.COPY);
	protected final AbstractUndoableOperationAction pasteHandler = new EditorUndoableOperationAction(ActionFactory.PASTE);
//	protected final AbstractUndoableOperationAction pasteSpecialHandler = new EditorUndoableOperationAction(EnsembleActionFactory.PASTE_SPECIAL);
	protected final AbstractUndoableOperationAction deleteHandler = new EditorUndoableOperationAction(ActionFactory.DELETE);
	protected final AbstractUndoableOperationAction selectAllHandler = new EditorUndoableOperationAction(ActionFactory.SELECT_ALL);
	
	private UndoActionHandler undoHandler;
	private RedoActionHandler redoHandler;
	private ISelectionChangedListener selectionChangedListener = new EditorSelectionChangedListener();
	private ISelectionProvider selectionProvider;

    /**
	 * @throws PartInitException -- removing this causes a compiler error in AbstractPlanEditorPart.init(); leaving it causes a compiler warning here unless documented. 
	 */
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException  {
		setSite(site);
		setInput(input);
	}

    @Override
    public void dispose() {
    	super.dispose();
    	cutHandler.dispose();
    	copyHandler.dispose();
    	pasteHandler.dispose();
//        pasteSpecialHandler.dispose();
    	deleteHandler.dispose();
    	selectAllHandler.dispose();
    	if (undoHandler != null) {
    		undoHandler.dispose();
    	}
    	if (redoHandler != null) {
    		redoHandler.dispose();
    	}
    	if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(selectionChangedListener);
			selectionProvider = null;
		}
    }
    
    public ISelectionProvider getSelectionProvider() {
	    return selectionProvider;
    }
    
    public abstract IUndoContext getUndoContext();
    
	public abstract IStructureModifier getStructureModifier();

	@Override
	protected void setSite(IWorkbenchPartSite newSite) {
		if (selectionProvider != null) {
			selectionProvider.removeSelectionChangedListener(selectionChangedListener);
			selectionProvider = null;
		}
	    super.setSite(newSite);
	    if (newSite != null) {
	    	selectionProvider = newSite.getSelectionProvider();
			if (selectionProvider != null) {
				selectionProvider.addSelectionChangedListener(selectionChangedListener);
			}
	    }
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		IWorkbenchPartSite site = getSite();
		IEditorInput oldInput = getEditorInput();
		if (oldInput != null) {
			detachModel(input, site);
		}
		super.setInput(input);
		if ((input != null) && (site != null)) {
			attachModel(input, site);
		}
	}
	
	@SuppressWarnings("deprecation")
    protected void attachModel(IEditorInput input, IWorkbenchPartSite site) {
		copyAttributes(cutHandler, ActionFactory.CUT);
		copyAttributes(copyHandler, ActionFactory.COPY);
		copyAttributes(pasteHandler, ActionFactory.PASTE);
//		copyAttributes(pasteSpecialHandler, EnsembleActionFactory.PASTE_SPECIAL);
		copyAttributes(deleteHandler, ActionFactory.DELETE);
		copyAttributes(selectAllHandler, ActionFactory.SELECT_ALL);
		getEditorSite().getKeyBindingService(); // Editors need to bind keyboard actions 
		
		IUndoContext undoContext = getUndoContext();
		undoHandler = new UndoActionHandler(site, undoContext);
		redoHandler = new RedoActionHandler(site, undoContext);
		cutHandler.init(site, undoContext);
		copyHandler.init(site, undoContext);
		pasteHandler.init(site, undoContext);
//		pasteSpecialHandler.setUndoContext(undoContext);
		deleteHandler.init(site, undoContext);
		
		updateHandlerEnablements();
    }
	
	protected void detachModel(IEditorInput input, IWorkbenchPartSite site) {
	    // nothing to do here
    }

	private void copyAttributes(IAction actionHandler, ActionFactory actionFactory) {
		IWorkbenchWindow workbenchWindow = getSite().getWorkbenchWindow();
		IWorkbenchAction factoryAction = actionFactory.create(workbenchWindow);
		// the following two lines are commented out to suppress shortcuts on the context menu
//		actionHandler.setAccelerator(factoryAction.getAccelerator());		
//		actionHandler.setActionDefinitionId(factoryAction.getActionDefinitionId());
		actionHandler.setDescription(factoryAction.getDescription());
		actionHandler.setDisabledImageDescriptor(factoryAction.getDisabledImageDescriptor());
		actionHandler.setHoverImageDescriptor(factoryAction.getHoverImageDescriptor());
		actionHandler.setId(factoryAction.getId());
		actionHandler.setImageDescriptor(factoryAction.getImageDescriptor());
		actionHandler.setText(factoryAction.getText());
		actionHandler.setToolTipText(factoryAction.getToolTipText());
	}

	/**
	 * Override if necessary, be sure to call super
	 * @param bars
	 */
	@Override
	public void updateActionBars(IActionBars bars) {
		if (bars != null) {
			updateHandlerEnablements();
			bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoHandler);
			bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoHandler);
			if (useUndoableOperations()) {
				bars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutHandler);
				bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyHandler);
				bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteHandler);
				//			bars.setGlobalActionHandler(EnsembleActionFactory.PASTE_SPECIAL.getId(), pasteSpecialHandler);
				bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteHandler);
				bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllHandler);
			}
			bars.updateActionBars();
		}
	}

	protected boolean useUndoableOperations() {
		return true;
	}

	/**
	 * Override if necessary
	 * 
	 * @param modifier
	 * @param selection
	 * @param event
	 *            TODO
	 * @return IUndoableOperation
	 */
	protected IUndoableOperation getCutOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new ClipboardCutOperation(transferable, modifier);
	}

	/**
	 * Override if necessary
	 * 
	 * @param modifier
	 * @param selection
	 * @param event
	 *            TODO
	 * @return IUndoableOperation
	 */
	protected IUndoableOperation getCopyOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new ClipboardCopyOperation(transferable, modifier);
	}

	/**
	 * Override if necessary
	 * 
	 * @param modifier
	 * @param selection
	 * @return IUndoableOperation
	 */
	protected IUndoableOperation getPasteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		return new ClipboardPasteOperation(selection, modifier);
	}
	
	/**
	 * Override if necessary
	 * 
	 * @param modifier
	 * @param selection
	 * @param event
	 *            TODO
	 * @return IUndoableOperation
	 */
	protected IUndoableOperation getDeleteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		return new DeleteOperation(transferable, modifier);
	}

	/**
	 * Override if necessary
	 * 
	 * @param modifier
	 * @param selection
	 * @return IUndoableOperation
	 */
	protected IUndoableOperation getSelectAllOperation(IStructureModifier modifier, ISelection selection) {
		return new SelectAllOperation();
	}

	/*
	 * Utility functions/classes
	 */
	
	/**
	 * Create the context menu for an ensemble editor part.
	 * @see EnsembleActionBarAdvisor.createEditMenu
	 * @param menu
	 */
	public void fillContextMenu(IMenuManager menu) {
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
		menu.add(new Separator());
		menu.add(undoHandler);
		menu.add(redoHandler);
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());
		menu.add(cutHandler);
		menu.add(copyHandler);
		menu.add(pasteHandler);
//		menu.add(pasteSpecialHandler);
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());
		menu.add(deleteHandler);
		menu.add(selectAllHandler);
		menu.add(new Separator());
				
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		updateActionBars(getEditorSite().getActionBars());
	}
	
	/**
	 * Update the handlers
	 */
	private void updateHandlerEnablements() {
		cutHandler.updateEnablement();
		copyHandler.updateEnablement();
		pasteHandler.updateEnablement();
//		pasteSpecialHandler.updateEnablement();
		deleteHandler.updateEnablement();
		selectAllHandler.updateEnablement();
	}

	/**
	 * Update all the handlers whenever the selection changes 
	 * @author Andrew
	 */
	private class EditorSelectionChangedListener implements ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateHandlerEnablements();
		}
	}
	
	private final class EditorUndoableOperationAction extends AbstractUndoableOperationAction {
		private final ActionFactory actionFactory;
		
		/**
		 * Create an editor undoable operation action using the given id
		 * The id should be only of the ids from the ActionFactory.  See
		 * getOperation() below for recognized ids.
		 * @param actionFactory
		 */
		public EditorUndoableOperationAction(ActionFactory actionFactory) {
			this.actionFactory = actionFactory;
		}

		@Override
		public IUndoableOperation getOperation(Event event) {
			IStructureModifier modifier = getStructureModifier();
			IUndoableOperation operation = null;
			if (modifier != null) {
				ISelection selection = getCurrentSelection();
				if (ActionFactory.CUT == actionFactory) {
					operation = getCutOperation(modifier, selection, event);
				} else if (ActionFactory.COPY == actionFactory) {
					operation = getCopyOperation(modifier, selection, event);
				} else if (ActionFactory.PASTE == actionFactory) {
					operation = getPasteOperation(modifier, selection, event);
				} else if (ActionFactory.DELETE == actionFactory) {
					operation = getDeleteOperation(modifier, selection, event);
				} else if (ActionFactory.SELECT_ALL == actionFactory) {
					operation = getSelectAllOperation(modifier, selection);
				}
			}
			return operation;
		}
		
		
	}

	/**
	 * Get the current selection from the site for this editor
	 * @return the current selection (guaranteed to be non-null)
	 */
	protected final ISelection getCurrentSelection() {
		ISelectionProvider provider = selectionProvider;
		if (provider != null) {
			ISelection selection = provider.getSelection();
			if (selection != null) {
				return selection;
			}
		}
		return StructuredSelection.EMPTY;
	}
	
}
