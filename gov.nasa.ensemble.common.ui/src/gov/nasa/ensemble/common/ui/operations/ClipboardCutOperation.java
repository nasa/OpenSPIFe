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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;

import org.apache.log4j.Logger;

public class ClipboardCutOperation extends DeleteOperation {
	
	private final Logger trace = Logger.getLogger(getClass()); 
	private final ITransferable transferable;
	private final IStructureModifier modifier;

	public ClipboardCutOperation(ITransferable transferable, IStructureModifier modifier) {
		super("cut", transferable, modifier);
		this.transferable = transferable;
		this.modifier = modifier;
	}
	
	@Override
	protected void undo() {
		putNewCopyOnClipboard(); // this is ordinary undo semantics, the contents remain
		super.undo();
	}

	@Override
	protected void doit() {
		TransferRegistry.getInstance().putOnClipboard(transferable);
		super.doit();
	}

	private void putNewCopyOnClipboard() {
		try {
			ITransferable copiedStuff = modifier.copy(transferable);
			TransferRegistry.getInstance().putOnClipboard(copiedStuff);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			trace.warn("failed to clone clipboard on undo cut, clearing instead");
			TransferRegistry.clearClipboardContents();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(ClipboardCutOperation.class.getSimpleName());
		builder.append(":");
		builder.append(super.toString());
		return builder.toString();
	}
	
}
