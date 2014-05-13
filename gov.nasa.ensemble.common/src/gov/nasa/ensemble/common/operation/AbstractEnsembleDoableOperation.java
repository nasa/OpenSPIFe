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
package gov.nasa.ensemble.common.operation;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;

/**
 * This class is used as a convenience to capture a moment's state in the plan,
 * use it to determine the enablement of an operation, and then later execute
 * that same operation.
 * 
 * Subclass implementers should probably override:
 * isExecutable, execute, and toString.
 * 
 * @author Andrew
 *
 */
public abstract class AbstractEnsembleDoableOperation extends AbstractEnsembleUndoableOperation {

	public AbstractEnsembleDoableOperation(String label) {
		super(label);
	}

	@Override
	public final void addContext(IUndoContext context) {
		// block this operation from being added to any context
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// ignorable?
	}
	
	@Override
	protected boolean isRedoable() {
		Logger.getLogger(AbstractEnsembleDoableOperation.class).warn("shouldn't be here");
		return false;
	}
	
	@Override
	protected final void redo() {
		Logger.getLogger(AbstractEnsembleDoableOperation.class).warn("shouldn't be here");
	}

	@Override
	protected boolean isUndoable() {
		Logger.getLogger(AbstractEnsembleDoableOperation.class).warn("shouldn't be here");
		return false;
	}
	
	@Override
	protected final void undo() {
		Logger.getLogger(AbstractEnsembleDoableOperation.class).warn("shouldn't be here");
	}

}
