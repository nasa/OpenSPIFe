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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.CommandRefreshingActiveEditorPartListener;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;

public abstract class AbstractPlanEditorHandler extends AbstractHandler implements ISelectionChangedListener, PlanEditorCommandConstants {

	private static final int DEFAULT_LOWER_BOUND_SELECTION_COUNT = 2;
	private static final int DEFAULT_UPPER_BOUND_SELECTION_COUNT = -1; //means no upper bound
	
	private final IOperationHistoryListener operationListener = new OperationHistoryListener();
	private final PlanEditorHandlerRefresher updateHandlerListener = new PlanEditorHandlerRefresher(getCommandId());

	protected MultiPagePlanEditor activeEditor = null;
	protected ISelectionProvider activeSelectionProvider = null;

	public AbstractPlanEditorHandler() {
		//add command refreshing part changed listener
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window != null) {
			window.getPartService().addPartListener(updateHandlerListener);
			IWorkbenchPartReference part = window.getPartService().getActivePartReference();
			updateHandlerListener.partActivated(part);
		}
		//add operation listener
		OperationHistoryFactory.getOperationHistory().addOperationHistoryListener(operationListener);
	}

	public abstract String getCommandId();

	/**
	 * Handlers should override this to specify the minimum of elements selected
	 * @return
	 */
	protected int getLowerBoundSelectionCount() {
		return DEFAULT_LOWER_BOUND_SELECTION_COUNT;
	}
	
	protected int getUpperBoundSelectionCount() {
		return DEFAULT_UPPER_BOUND_SELECTION_COUNT;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		selectionChanged(selection);
	}
	
	protected void selectionChanged(ISelection selection) {
		boolean enabled = isEnabledForSelection(selection);
		setBaseEnabled(enabled);
		Command command = getCommand();
		State state = command.getState(RegistryToggleState.STATE_ID);
		if (state != null) {
			boolean checked = isCheckedForSelection(selection);
			try {
				setCommandState(command, checked);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
	}

	protected void partActivated(IWorkbenchPart part) {
		if (!(part instanceof MultiPagePlanEditor)) {
			setBaseEnabled(false);
		}
	}

	protected void pageChanged(PageChangedEvent event) {
		//do nothing by default; override if necessary
	}
	
	protected boolean isEnabledForSelection(ISelection selection) {
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		//respects lower bound
		if (elements.size() < getLowerBoundSelectionCount()) {
			return false;
		}
		//respects upper bound
		if (getUpperBoundSelectionCount() > -1 && elements.size() > getUpperBoundSelectionCount()) {
			return false;
		}
		//elements are editable
		for (EPlanElement element : elements) {
			if (!PlanEditApproverRegistry.getInstance().canModify(element)) {
				return false;
			}
		}
		return true;
	}
	
	protected  boolean isEnabledForEditorPart(IEditorPart part) {
		return true;
	}
	
	protected boolean isCheckedForSelection(ISelection selection) {
		return false;
	}
	
	protected ISelection getSelection() {
		if (activeSelectionProvider != null) {
			ISelection selection = activeSelectionProvider.getSelection();
			if (selection != null) {
				return selection;
			}
		}
		return StructuredSelection.EMPTY; 
	}

	protected EList<EPlanElement> getSelectedTemporalElements(ISelection selection) {
		EList<EPlanElement> elements = new BasicEList<EPlanElement>();
		if (selection instanceof IStructuredSelection) {
			Set<EPlanChild> fromSelection = WidgetUtils.typeFromSelection(selection, EPlanChild.class);
			elements.addAll(fromSelection);
		}
		return elements;
	}
	
	protected MultiPagePlanEditor getActiveEditor() {
		return activeEditor; 
	}
	
	protected IUndoContext getUndoContext() {
		MultiPagePlanEditor editor = getActiveEditor();
		return (editor != null ? editor.getUndoContext() : null); 
	}
	
	protected Command getCommand() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		ICommandService service = (ICommandService) workbench.getService(ICommandService.class);
		return service.getCommand(getCommandId());
	}
	
	protected static boolean getCommandState(Command command) {
		State toggleState = command.getState(RegistryToggleState.STATE_ID);
		if (toggleState != null) {
			Object value = toggleState.getValue();
			if (CommonUtils.equals(Boolean.TRUE, value)) {
				return true;
			}
		}
		return false;
	}

	protected static void setCommandState(Command command, boolean newValue) throws ExecutionException {
		State state = command.getState(RegistryToggleState.STATE_ID);
		if(state == null)
			throw new ExecutionException("The command does not have a toggle state"); //$NON-NLS-1$
		 if(!(state.getValue() instanceof Boolean))
			throw new ExecutionException("The command's toggle state doesn't contain a boolean value"); //$NON-NLS-1$
		boolean oldValue = ((Boolean) state.getValue()).booleanValue();
		if (oldValue != newValue) {
			HandlerUtil.toggleCommandState(command);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window != null) {
			window.getPartService().removePartListener(updateHandlerListener);
		}
		activeEditor = null;
		if (activeSelectionProvider != null) {
			activeSelectionProvider.removeSelectionChangedListener(this);
			activeSelectionProvider = null;
		}
		OperationHistoryFactory.getOperationHistory().removeOperationHistoryListener(operationListener);
	}

	private class PlanEditorHandlerRefresher extends CommandRefreshingActiveEditorPartListener {
		
		public PlanEditorHandlerRefresher(String commandId) {
			super(commandId);
		}

		@Override
		public void partActivated(IWorkbenchPartReference partReference) {
			super.partActivated(partReference);
			IWorkbenchPart part = partReference.getPart(false);
			if (part instanceof MultiPagePlanEditor) {
				if (activeEditor == null || part != activeEditor) {
					activeEditor = (MultiPagePlanEditor) part;
					ISelectionProvider provider = part.getSite().getSelectionProvider();
					if (activeSelectionProvider != null) {
						activeSelectionProvider.removeSelectionChangedListener(AbstractPlanEditorHandler.this);
					}
					activeSelectionProvider = provider;
					activeSelectionProvider.addSelectionChangedListener(AbstractPlanEditorHandler.this);
				}
				ISelection selection = activeSelectionProvider.getSelection();
				selectionChanged(selection);
			} 
			AbstractPlanEditorHandler.this.partActivated(part);
		}
		
		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			super.partClosed(partRef);
			IWorkbenchPart part = partRef.getPart(false);
			if (part instanceof MultiPagePlanEditor) {
				if (activeEditor == part) {
					activeEditor = null;
					if (activeSelectionProvider != null) {
						activeSelectionProvider.removeSelectionChangedListener(AbstractPlanEditorHandler.this);
						activeSelectionProvider = null;
					}
				}
			} 
		}

		@Override
		public void pageChanged(org.eclipse.jface.dialogs.PageChangedEvent event) {
			AbstractPlanEditorHandler.this.pageChanged(event);
		}
		
	}
	
	private class OperationHistoryListener implements IOperationHistoryListener {

		@Override
		public void historyNotification(OperationHistoryEvent event) {
			int eventType = event.getEventType();
			if (eventType == OperationHistoryEvent.DONE ||
					eventType == OperationHistoryEvent.UNDONE ||
					eventType == OperationHistoryEvent.REDONE) {
				IEditorPart activeEditor = getActiveEditor();
				if (activeEditor != null) {
					WidgetUtils.runInDisplayThread(activeEditor.getSite().getShell(), new Runnable() {
						@Override
						public void run() {
							selectionChanged(activeSelectionProvider.getSelection());
						}
					});
				}
			}
		}

	}

}
