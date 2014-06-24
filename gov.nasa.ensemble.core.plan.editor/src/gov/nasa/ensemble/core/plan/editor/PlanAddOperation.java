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
import gov.nasa.ensemble.common.ui.WorkbenchUtils;
import gov.nasa.ensemble.common.ui.operations.AddOperation;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;

public class PlanAddOperation extends AddOperation implements IDisplayOperation {

	private EPlan context;

	public PlanAddOperation(ITransferable transferable, IStructureModifier modifier, IStructureLocation location) {
		super(transferable, modifier, location);
	}

	@Override
	public boolean isExecutable() {
		boolean executable = true;
		IStructureLocation location = getStructureLocation();
		if (location instanceof PlanInsertionLocation) {
			EPlanElement element = ((PlanInsertionLocation)location).getTarget();
			EPlan plan = EPlanUtils.getPlan(element);
			context = plan;
			executable = PlanEditApproverRegistry.getInstance().canModify(element)
				&& plan != null && PlanEditApproverRegistry.getInstance().canModifyStructure(plan);
		}
		return super.isExecutable() && executable;
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		if (site instanceof IWorkbenchPartSite) {
			IWorkbenchPartSite partSite = (IWorkbenchPartSite) site;
			WorkbenchUtils.activate(partSite);
		}
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		List<? extends EPlanElement> toSelect = getElementsToSelect(getPlanElements());
		IStructuredSelection selection = new StructuredSelection(toSelect);
		selectionProvider.setSelection(selection);
	}

	/**
	 * Subclasses may override to filter what elements get selected after a
	 * drop.
	 */
	protected List<? extends EPlanElement> getElementsToSelect(List<? extends EPlanElement> elements) {
		return elements;
	}
	
	@Override
	protected void execute() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanAddOperation.super.execute();
			}
		});
	}
	
	@Override
	protected void undo() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanAddOperation.super.undo();
			}
		});
	}
	
	protected final List<? extends EPlanElement> getPlanElements() {
		ITransferable transferable = getObjects();
        if (transferable instanceof IPlanElementTransferable) { 
        	IPlanElementTransferable planTransferable = (IPlanElementTransferable) transferable;
			return planTransferable.getPlanElements();
        }
		return Collections.emptyList();
	}

}
