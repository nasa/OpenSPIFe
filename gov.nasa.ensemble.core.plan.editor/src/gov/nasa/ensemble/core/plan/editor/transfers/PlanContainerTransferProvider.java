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
package gov.nasa.ensemble.core.plan.editor.transfers;

import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.SimpleByteArrayTransferProvider;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.IPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanTransferableExtensionRegistry;

import java.util.List;

import org.eclipse.swt.dnd.Transfer;

/**
 * @author Andrew
 *
 */
public class PlanContainerTransferProvider extends SimpleByteArrayTransferProvider {

	public static final Transfer transfer = createSimpleByteArrayTransfer(PlanContainerTransferProvider.class.getCanonicalName()); 
	
	@Override
	public Transfer getTransfer() {
		return transfer;
	}

	@Override
	public boolean canPack(ITransferable transferable) {
		if (!(transferable instanceof PlanTransferable)) {
			return false;
		}
		PlanTransferable planTransferable = (PlanTransferable) transferable;
		List<? extends EPlanElement> elements = planTransferable.getPlanElements();
		if ((elements == null) || elements.isEmpty()) {
			return false;
		}
		for (EPlanElement element : elements) {
			if (!(element instanceof EActivity)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ITransferable unpackTransferObject(byte[] byteArray) {
		ITransferable transferable = super.unpackTransferObject(byteArray);
		if (transferable instanceof PlanTransferable) {
			PlanTransferable planTransferable = (PlanTransferable) transferable;
			for (IPlanTransferableExtension extension : PlanTransferableExtensionRegistry.getInstance().getExtensions()) {
				extension.postUnpackHook(planTransferable);
			}
		}
		return transferable;
	}

}
