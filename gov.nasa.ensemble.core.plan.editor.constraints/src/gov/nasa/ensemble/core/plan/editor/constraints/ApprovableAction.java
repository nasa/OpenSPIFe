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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.GlobalAction;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorPart;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public abstract class ApprovableAction extends GlobalAction implements IWorkbenchWindowActionDelegate, IActionDelegate2, IHandler {

	private EPlan currentPlan;
	private IAction currentAction = null;
	private IWorkbenchWindow window = null;
	private ISelection currentSelection = null;

	private IPartListener2 activationListener = new PlanActivationListener();
	private Listener listener = new Listener();
	private AbstractEnsembleEditorPart targetEditor;
	private ActionHandler proxy;

	public ApprovableAction() {
		super();
	}

	public ApprovableAction(String text, int style) {
		super(text, style);
	}
	
	protected final IEditorPart getTargetEditor() {
		ensureTargetEditor();
		return targetEditor;
	}
	
	@Override
	public void init(IAction action) {
		currentAction = action;
	}
	
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				page.addPartListener(activationListener);
			}
		}
	}
	
	@Override
	public void dispose() {
		if (currentPlan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(currentPlan);
			domain.removeResourceSetListener(listener);
			currentPlan = null;
		}
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				page.removePartListener(activationListener);
			}
		}
		this.currentAction = null;
		this.window = null;
	}
	
	@Override
	public final void run(IAction action) {
		runWithEvent(action, null);
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		currentAction = action;
		currentSelection = selection;
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof MultiPagePlanEditor) {
			targetEditor = ((MultiPagePlanEditor)targetEditor).getCurrentEditor();
		}
		if (targetEditor instanceof AbstractPlanEditorPart) {
			this.targetEditor = (AbstractPlanEditorPart)targetEditor;
		} else {
			this.targetEditor = null;
		}
		updateEnablement(action);
	}

	/**
	 * Provide the current undo context to subclasses.
	 * @return the undo context, or null if none is current.
	 */
	protected final IUndoContext getUndoContext() {
		ensureTargetEditor();
		if (targetEditor != null) {
			return targetEditor.getUndoContext();
		}		
		return null;
	}

	private void ensureTargetEditor() {
		if(targetEditor == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			if(workbench != null) {
				IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
				if(workbenchWindow != null) {
					IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
					if(workbenchPage != null) {
						IEditorPart editorPart = workbenchPage.getActiveEditor();
						if(editorPart instanceof MultiPagePlanEditor) {
							MultiPagePlanEditor multiPagePlanEditor = (MultiPagePlanEditor)editorPart;
							editorPart = multiPagePlanEditor.getCurrentEditor();
						}
						if(editorPart instanceof AbstractEnsembleEditorPart) {
							targetEditor = (AbstractEnsembleEditorPart)editorPart;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Provider the current selection provider to subclasses.
	 * @return the selection provider, or null if none is current.
	 */
	protected final ISelectionProvider getSelectionProvider() {
		ensureTargetEditor();
		if (targetEditor != null) {
			return targetEditor.getSite().getSelectionProvider();
		}
		return null;
	}

	/**
	 * Provide the current selection to subclasses.
	 * @return the selection, or null if none is current.
	 */
	protected final ISelection getSelection() {
		if (currentSelection != null) {
			return currentSelection;
		}
		ensureTargetEditor();
		if (targetEditor != null) {
			ISelectionProvider selectionProvider = getSelectionProvider();
			if (selectionProvider != null) {
				return selectionProvider.getSelection();
			}
		}
		return null;
	}

	/**
	 * Provide the current window to subclasses. 
	 * @return the window, or null if none is current.
	 */
    protected final IWorkbenchWindow getWindow() {
	    return window;
    }

    /**
     * Implement in your subclass to set the action
     * to be enabled or disabled, set the tooltip, etc.
     * 
     * @param action
     */
    protected abstract void updateEnablement(IAction action);

    /*
     * Utility methods and classes
     */
    
    private void updateEnablement() {
		if (currentAction != null) {
			updateEnablement(currentAction);
		}
	}
	
	/**
     * Whether or not we should reorder by time
     * @return
     */
    private ActionHandler getProxy() {
    	if (proxy == null) {
    		proxy = new ActionHandler(this);
    	}
    	return proxy;
    }

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
    	getProxy().addHandlerListener(handlerListener);
    }

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
    	getProxy().execute(event);
    	return null;
    }

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
    	getProxy().removeHandlerListener(handlerListener);
    }

	private synchronized void setCurrentPlan(EPlan ePlan) {
		if (ePlan != currentPlan) {
			if (currentPlan != null) {
				TransactionalEditingDomain domain = TransactionUtils.getDomain(currentPlan);
				if(domain != null) {
					domain.removeResourceSetListener(listener);
				}
			}
			currentPlan = ePlan;
			if (currentPlan != null) {
				TransactionalEditingDomain domain = TransactionUtils.getDomain(currentPlan);
				if(domain != null) {
					domain.addResourceSetListener(listener);
				} else {
					LogUtil.warn("No transaction domain for current plan.");
				}
			}
		}
	}

	private class Listener extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			if (PlanEditApproverRegistry.getInstance().needsUpdate(event)) {
				updateEnablement();
			}
		}
		
	}
	
	private class PlanActivationListener implements IPartListener2 {
		
		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			IWorkbenchPage page = partRef.getPage();
			IEditorPart editor = page.getActiveEditor();
			if (editor == null) {
				targetEditor = null;
				return;
			}
			EPlan ePlan = EditorPartUtils.getAdapter(editor, EPlan.class);
			if (ePlan == null) {
				// nothing to do
				return;
			}
			setCurrentPlan(ePlan);
			setActiveEditor(currentAction, editor);
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			setCurrentPlan(null);
			setActiveEditor(currentAction, null);
		}
		
		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) 	{ /* no-operation */}
		@Override
		public void partDeactivated(IWorkbenchPartReference partRef) 	{ /* no-operation */}
		@Override
		public void partOpened(IWorkbenchPartReference partRef) 		{ /* no-operation */}
		@Override
		public void partHidden(IWorkbenchPartReference partRef) 		{ /* no-operation */}
		@Override
		public void partVisible(IWorkbenchPartReference partRef) 		{ /* no-operation */}
		@Override
		public void partInputChanged(IWorkbenchPartReference partRef) 	{ /* no-operation */}
	}

}
