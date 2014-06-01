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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public abstract class AbstractUndoableOperationAction extends Action implements IWorkbenchAction {

	private IWorkbenchPartSite site;
	private IUndoContext undoContext;
	private IOperationHistoryListener historyListener = new IOperationHistoryListener() {
		@Override
		public void historyNotification(OperationHistoryEvent event) {
			int type = event.getEventType();
			if ((type == OperationHistoryEvent.DONE)
				|| (type == OperationHistoryEvent.REDONE)
				|| (type == OperationHistoryEvent.UNDONE)) {
				IUndoableOperation operation = event.getOperation();
				if ((undoContext == null) || operation.hasContext(undoContext) || operation.getContexts().length == 0) {
					Display display = WidgetUtils.getDisplay();
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							updateEnablement();
						}
					});
				}
			}
		}
	};

	public AbstractUndoableOperationAction() {
		this(null);
	}
	
	public AbstractUndoableOperationAction(IUndoContext undoContext) {
		this.undoContext = undoContext;
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		history.addOperationHistoryListener(historyListener);
	}

	@Override
	public void dispose() {
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		history.removeOperationHistoryListener(historyListener);
	}
	
	/**
	 * subclasses must override
	 * 
	 * @param event
	 * @return IUndoableOperation
	 */
	public abstract IUndoableOperation getOperation(Event event);
	
	public void init(IWorkbenchPartSite site, IUndoContext undoContext) {
		this.site = site;
		this.undoContext = undoContext;
	}

	public void updateEnablement() {
		IUndoableOperation operation = getOperation(null);
		if (operation != null) {
			boolean canExecute = operation.canExecute();
			setEnabled(canExecute);
			operation.dispose();
		} else {
			setEnabled(false);
		}
	}
	
	@Override
	public void run() {
		runWithEvent(null);
	}

	@Override
	public void runWithEvent(Event event) {
		final IUndoableOperation operation = getOperation(event);
		WidgetUtils.execute(operation, undoContext, event.widget, site);
	}

}
