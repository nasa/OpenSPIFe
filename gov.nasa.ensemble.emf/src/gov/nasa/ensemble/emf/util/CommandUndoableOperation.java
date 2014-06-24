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
package gov.nasa.ensemble.emf.util;

import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.EditingDomain;

public class CommandUndoableOperation extends AbstractTransactionUndoableOperation {

	private final EditingDomain domain;
	private final Command command;

	public CommandUndoableOperation(EditingDomain domain, Command command) {
		this(command.getLabel(), domain, command);
	}
	
	public CommandUndoableOperation(String label, EditingDomain domain, Command command) {
		super(label);
		this.domain = domain;
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}

	@Override
	protected void dispose(UndoableState state) {
		command.dispose();
	}

	@Override
	protected void execute() throws Throwable {
		domain.getCommandStack().execute(command);
	}

	@Override
	public String toString() {
		return command.toString();
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				command.undo();
			}
		});
	}

	@Override
	protected boolean isExecutable() {
		return command.canExecute();
	}

	@Override
	protected boolean isUndoable() {
		return command.canUndo();
	}

}
