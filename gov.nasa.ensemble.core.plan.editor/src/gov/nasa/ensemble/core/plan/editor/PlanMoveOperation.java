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
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.common.ui.operations.MoveOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.List;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class PlanMoveOperation extends MoveOperation implements IDisplayOperation {

	private EPlan context = null;
	private List<? extends EPlanElement> elements = null;

	public PlanMoveOperation(ITransferable transferable, IStructureModifier modifier, IStructureLocation destination) {
		super(transferable, modifier, destination);
		elements = ((IPlanElementTransferable)transferable).getPlanElements();
		EPlanElement element = elements.get(0);
		context = EPlanUtils.getPlan(element);
	}

	@Override
	protected void dispose(UndoableState state) {
		context = null;
		elements = null;
	}
	
	@Override
	protected void execute() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanMoveOperation.super.execute();
			}
		});
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
//		WorkbenchUtils.activate(site);
//		ISelectionProvider selectionProvider = site.getSelectionProvider();
//		IStructuredSelection selection = new StructuredSelection(elements);
//		selectionProvider.setSelection(selection);
	}
	
	@Override
	protected void undo() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanMoveOperation.super.undo();
			}
		});
	}

}
