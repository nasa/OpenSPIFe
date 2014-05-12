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

import gov.nasa.ensemble.common.operation.AbstractEnsembleDoableOperation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;

public class ClipboardCopyOperation extends AbstractEnsembleDoableOperation {
	
	private final ITransferable transferable;
	private final IStructureModifier modifier;
	private ITransferable copiedTransferable;
	
	public ClipboardCopyOperation(ITransferable transferable, IStructureModifier modifier) {
		super("copy");
		this.modifier = modifier;
		this.transferable = transferable;
	}
	
	@Override
	public boolean isExecutable() {
		return (transferable != null);
	}
	
	@Override
	protected void execute() {
		copiedTransferable = modifier.copy(transferable);
		TransferRegistry.getInstance().putOnClipboard(copiedTransferable);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(ClipboardCopyOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(transferable));
		return builder.toString();
	}
	
}
