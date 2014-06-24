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
package gov.nasa.ensemble.emf.patch;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.ecore.EObject;

public class PatchOperation extends AbstractTransactionUndoableOperation {
	
	private Patch patch;
	private EObject target;
	
	public PatchOperation(EObject target, Patch patch) {
		this(target, patch, "patch operation");	
	}
	
	public PatchOperation(EObject target, Patch patch, String label) {
		super(label);
		this.target = target;
		this.patch = patch;		
	}
	
	@Override
	protected void dispose(UndoableState state) {
		this.target = null;
		this.patch = null;
	}
	
	private void doit() {
		TransactionUtils.writeIfNecessary(target, new Runnable() {
			@Override
			public void run() {
				if (patch != null) {
					try {
						patch.applyAndReverse();
					} catch (PatchRollbackException e) {
						LogUtil.error(e);
					}
				}
			}
		});
	}
	
	@Override
	protected void execute() throws Throwable {
		doit();
	}

	@Override
	protected void undo() throws Throwable {
		doit();
	}

	@Override
	protected void redo() throws Throwable {
		doit();
	}

	@Override
	public String toString() {
		return null;
	}
	
	public EObject getTarget() {
		return target;
	}
	
	public Patch getPatch() {
		return patch;
	}

}
