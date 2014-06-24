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

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanElementState;

import org.eclipse.swt.dnd.TransferData;

public abstract class AbstractPlanTransferableExtension implements IPlanTransferableExtension {

	@Override
	public boolean vetoInsertHook(TransferData type, PlanElementState state) {
		return false;
	}
	
	@Override
	public void postGetLocation(PlanTransferable transferable, PlanOriginalLocation location) {
		// ignore
	}
	
	@Override
	public void postGetInsertionHook(ITransferable transferable, PlanInsertionLocation location) {
		// ignore
	}
	
	@Override
	public void postGetHook(PlanTransferable transferable) {
		// ignore
	}

	@Override
	public void postCopyHook(PlanTransferable original, PlanTransferable copy) {
		// ignore
	}
	
	@Override
	public void postInstantiationHook(PlanTransferable original, PlanTransferable copy) {
		// ignore
	}

	@Override
	public void mergeHook(PlanTransferable copy, PlanTransferable mergedCopy) {
		// ignore
	}

	@Override
	public void postUnpackHook(PlanTransferable planTransferable) {
		// ignore
	}

	@Override
	public void preAddHook(IPlanElementTransferable transferable, IStructureLocation location) {
		// ignore
	}

	@Override
	public void postAddHook(IPlanElementTransferable transferable, IStructureLocation location) {
		// ignore
	}

	@Override
	public void preRemoveHook(IPlanElementTransferable transferable, IStructureLocation location) {
		// ignore
	}

	@Override
	public void postRemoveHook(IPlanElementTransferable transferable, IStructureLocation location) {
		// ignore
	}

	public void preparePasteExpectations(EPlanElement expectedElement) {
		// ignore
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (IPlanTransferableExtension.class.isAssignableFrom(adapter)) {
			return this;
		}
		return null;
	}

	
}
