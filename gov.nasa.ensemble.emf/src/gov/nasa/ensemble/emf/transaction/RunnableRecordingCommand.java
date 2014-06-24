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

import org.eclipse.emf.common.command.AbstractCommand;

public final class RunnableRecordingCommand extends AbstractCommand {
	private Runnable runnable;

	public RunnableRecordingCommand(String label, Runnable runnable) {
		super(label);
		this.runnable = runnable;
	}

	@Override
	public boolean canExecute() {
		return runnable != null;
	}
	
	@Override
	public void execute() {
		runnable.run();
		
		// MSLICE-262 - release the reference to the runnable after running so that any references it may be holding
		// can be GC'ed... since canUndo() is always false, and we only ever execute once, this is reasonable. Frequently
		// when executing transactional blocks we use temporary runnables (closures) which capture instance and final local
		// variables in the environment which may refer to very large objects, e.g. 70MB command dictionaries. If the
		// command stack then keeps references to all these runnables, we have a rather bad memory leak, so the solution
		// is to break the chain here.
		runnable = null;
	}

	@Override
	public final boolean canUndo() {
		return false;
	}
	
	@Override
	public void redo() {
		throw new IllegalStateException("shouldn't be able to reach here");
	}
	
}
