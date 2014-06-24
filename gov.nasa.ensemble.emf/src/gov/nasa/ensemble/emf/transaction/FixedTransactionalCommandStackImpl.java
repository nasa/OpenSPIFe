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
/**
 * 
 */
package gov.nasa.ensemble.emf.transaction;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.TransactionalCommandStackImpl;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.util.ConditionalRedoCommand;

public class FixedTransactionalCommandStackImpl extends TransactionalCommandStackImpl {
	
	private static final Map<String, Boolean> DEFAULT_TRANSACTION_OPTIONS = new HashMap<String, Boolean>();
	static {
		DEFAULT_TRANSACTION_OPTIONS.put(Transaction.OPTION_NO_UNDO, Boolean.TRUE);
		DEFAULT_TRANSACTION_OPTIONS.put(Transaction.OPTION_NO_VALIDATION, Boolean.TRUE);
	}
	
	@Override
	public boolean canUndo() {
		return false;
	}
	
	@Override
	public void undo() {
		throw new UnsupportedOperationException("undo not supported");
	}

	@Override
	protected void doExecute(Command command, Map<?, ?> options) throws InterruptedException, RollbackException {
		// default the transaction options to not perform EMF validation or allow undo 
		if (options == null) {
			options = DEFAULT_TRANSACTION_OPTIONS;
		}
		InternalTransaction tx = createTransaction(command, options);
		boolean completed = false;
		
		try {
			basicExecute(command);
			
			// new in EMF 2.4:  AbortExecutionException can cause the
			// command not to be added to the undo stack
			completed = mostRecentCommand == command;
			
			// commit the transaction now
			TransactionalEditingDomainImpl domain = (TransactionalEditingDomainImpl)tx.getEditingDomain();
			if (domain != null && domain.getChangeRecorder() != null) {
				tx.commit();
			} else {
				tx = null;
			}
		} catch (OperationCanceledException e) {
			// snuff the exception, because this is expected (user asked to
			//    cancel the model change).  We will rollback, below
		} finally {
			if (tx != null) {
				if (tx.isActive()) {
					// roll back (some exception, possibly being thrown now or
					//    an operation cancel, has occurred)
					rollback(tx);
	                handleRollback(command, null);
				} else {
					// the transaction has already incorporated the triggers
					//    into its change description, so the recording command
					//    doesn't need them again
					if (!(command instanceof RecordingCommand) && completed) {
						Command triggerCommand = tx.getTriggers();
						if (triggerCommand != null) {
							// replace the executed command by a compound of the
							//    original and the trigger commands
							CompoundCommand compound = new ConditionalRedoCommand.Compound();
							compound.append(mostRecentCommand);
							compound.append(triggerCommand);
							mostRecentCommand = compound;
						}
					}
				}
			}
		}
        clearStack(command);
	}

	private void clearStack(Command command) {
		while (top >= 0) {
			Command c;
			try {
				c = commandList.remove(top--);
			} catch (IndexOutOfBoundsException e) {
				continue;
			}
			c.dispose();
		}
		mostRecentCommand = null;
    }
	
}
