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

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.WorkbenchUtils;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;

public class AddGroupOperation extends AbstractTransactionUndoableOperation implements IDisplayOperation {

	private final IStructureModifier modifier;
	private final EPlan plan;
	private final PlanElementState state;
	private final PlanInsertionLocation location;

	// these are set in the first execute()
	private PlanTransferable transferable;
	
	public AddGroupOperation(IStructureModifier modifier, EPlanElement target) {
		super("add " + EPlanUtils.getActivityGroupDisplayName());
		this.modifier = modifier;
		this.plan = EPlanUtils.getPlan(target);
		this.state = PlanUtils.getAddLocationForActivityGroups(target, InsertionSemantics.AFTER);
		this.location = new PlanInsertionLocation(target, state);
	}

	@Override
	protected void dispose(UndoableState state) {
		switch (state) {
		case DONE:
			// transferable in document
			break;
		case UNEXECUTED:
		case UNDONE:
			if (transferable != null) { // should not be null here but check anyway
				transferable.dispose();
			}
			break;
		case FAILED:
			// don't know how to recover here 
		}
	}
	
	@Override
	public boolean isExecutable() {
		if ((state != null) && (plan != null)) {
			EPlanParent parent = state.getParent();
			if (parent != null && PlanEditApproverRegistry.getInstance().canModify(parent)
				&& PlanEditApproverRegistry.getInstance().canModifyStructure(plan)
				&& (!(modifier instanceof PlanStructureModifier)) || ((PlanStructureModifier)modifier).notInsertionVetoed(null, state)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void execute() {
		transferable = new PlanTransferable();
		EActivityGroup activityGroup = PlanFactory.getInstance().createActivityGroup(state.getParent());
		transferable.setPlanElements(Collections.singletonList(activityGroup));
		doit();
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		if (site instanceof IWorkbenchPartSite) {
			IWorkbenchPartSite partSite = (IWorkbenchPartSite) site;
			WorkbenchUtils.activate(partSite);
		}
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		List<? extends EPlanElement> elements = transferable.getPlanElements();
		IStructuredSelection selection = new StructuredSelection(elements);
		selectionProvider.setSelection(selection);
	}

	@Override
	protected void undo() {
		TransactionUtils.writeIfNecessary(state.getParent(), new Runnable() {
			@Override
			public void run() {
				modifier.remove(transferable, location);
			}
		});
	}
	
	@Override
	protected void redo() throws Throwable {
		doit();
	}
	
	private void doit() {
		TransactionUtils.writeIfNecessary(state.getParent(), new Runnable() {
			@Override
			public void run() {
				modifier.add(transferable, location);
			}
		});
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(AddGroupOperation.class.getSimpleName());
		builder.append(": ");
		builder.append(String.valueOf(state.getParent()));
		builder.append(" @ ");
		builder.append(String.valueOf(state.getIndex()));
		return builder.toString();
	}

}
