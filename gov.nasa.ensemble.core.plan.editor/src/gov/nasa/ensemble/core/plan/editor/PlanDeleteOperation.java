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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.operation.OperationJob.IJobOperation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

public class PlanDeleteOperation extends DeleteOperation implements IJobOperation {

	private EPlan context = null;

	public PlanDeleteOperation(ITransferable transferable, IStructureModifier modifier) {
		super(transferable, modifier);
	}

	public PlanDeleteOperation(String label, ITransferable transferable, IStructureModifier modifier) {
		super(label, transferable, modifier);
	}

	@Override
	public boolean isExecutable() {
		boolean executable = true;
		ITransferable transferable = getObjects();
		if (transferable instanceof PlanTransferable) {
			PlanTransferable planTransferable = (PlanTransferable) transferable;
			for (EPlanElement pe : planTransferable.getPlanElements()) {
				if (!PlanEditApproverRegistry.getInstance().canModify(pe)) {
					executable = false;
					break;
				}
				if (context == null) {
					context = EPlanUtils.getPlan(pe);
				}
			}
		}
		return super.isExecutable() && executable;
	}

	@Override
	protected void doit() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanDeleteOperation.super.doit();
			}
		});
	}
	
	@Override
	protected void undo() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanDeleteOperation.super.undo();
			}
		});
	}

}
