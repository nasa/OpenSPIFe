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
package gov.nasa.ensemble.common.ui.gef;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * A GEF {@link Command} wrapper for {@link IUndoableOperation}s.
 */
public class OperationCommand extends Command {
	
	private final IUndoableOperation operation;
	private final IUndoContext context;
	private final Widget widget;
	private final IWorkbenchPartSite site;
	
	public OperationCommand(IUndoContext context, IUndoableOperation operation, Widget widget, IWorkbenchPartSite site) {
		this.operation = operation;
		this.context = context;
		this.widget = widget;
		this.site = site;
	}
	
	public IUndoableOperation getOperation() {
		return operation;
	}

	@Override
	public boolean canExecute() {
		return operation.canExecute();
	}
	
	@Override
	public boolean canUndo() {
		return operation.canUndo();
	}
	
	@Override
	public void execute() {
		WidgetUtils.execute(operation, context, widget, site);
	}
	
	@Override
	public void undo() {
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.undo(context, null, null);
		} catch (ExecutionException e) {
			Logger.getLogger(OperationCommand.class).error("failed to undo: " + operation.toString(), e);
		}
	}
	
}
