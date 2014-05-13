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

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;

public class MoveOperation extends AbstractEnsembleUndoableOperation {

	private final ITransferable transferable;
	private final IStructureModifier modifier;
	private final IStructureLocation origin;
	private final IStructureLocation destination;
	
	public MoveOperation(ITransferable transferable, IStructureModifier modifier, IStructureLocation destination) {
		super("move");
		this.modifier = modifier;
		this.transferable = transferable;
		this.origin = modifier.getLocation(transferable);
		this.destination = destination;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		switch (state) {
		case DONE:
		case UNEXECUTED:
		case UNDONE:
			// the transferable is in the document
			break;
		case FAILED:
			// don't know how to recover here 
		}
	}
	
	@Override
	public boolean isExecutable() {
		return (transferable != null) && (origin != null) && (destination != null);
	}
	
	@Override
	protected void execute() {
		move(origin, destination);
	}

	@Override
	protected void undo() {
		move(destination, origin);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(MoveOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(transferable));
		builder.append(" from ");
		builder.append(String.valueOf(origin));
		builder.append(" to ");
		builder.append(String.valueOf(destination));
		return builder.toString();
	}
	
	private void move(IStructureLocation from, IStructureLocation to) {
		modifier.remove(transferable, from);
		try {
			modifier.add(transferable, to);
		} catch (RuntimeException e) {
			// don't duplicate elements
			// this handler assumes that we failed to add any part of the transferable
			modifier.add(transferable, from);
			throw e;
		}
	}

}
