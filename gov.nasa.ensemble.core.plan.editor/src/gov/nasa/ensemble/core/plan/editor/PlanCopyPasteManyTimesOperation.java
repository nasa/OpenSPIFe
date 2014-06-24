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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class PlanCopyPasteManyTimesOperation extends AbstractTransactionUndoableOperation implements IDisplayOperation {

	private PlanTransferable mergedTransferable;
	private ISelection targetSelection;
	private IStructureModifier modifier;
	private int copies;
	private IStructureLocation insertionLocation;
	private EPlan plan;
	private ISelectionProvider selectionProvider;
	
	public PlanCopyPasteManyTimesOperation(ISelection targetSelection, IStructureModifier modifier, int copies, ISelectionProvider selectionProvider) {
		super("copy many times");
		this.targetSelection = targetSelection;
		this.modifier = modifier;
		this.copies = copies;
		this.selectionProvider = selectionProvider;
	}

	@Override
	protected void dispose(UndoableState state) {
		switch (state) {
		case DONE:
			// transferable is in the document
			break;
		case UNEXECUTED:
		case UNDONE:
			if (mergedTransferable != null) {
				mergedTransferable.dispose();
			}
			break;
		case FAILED:
			// don't know how to recover here 
		}
	}

	@Override
	public boolean isExecutable() {
		if (targetSelection == null || targetSelection.isEmpty()) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void execute(IProgressMonitor monitor) throws Throwable {
		// TODO Auto-generated method stub
		super.execute(monitor);
	}

	@Override
	protected void execute() throws Throwable {
		// create a plan transferable that will be used as a source for the copies
		final PlanTransferable sourcePlanTransferable = (PlanTransferable) modifier.getTransferable(targetSelection);				
		
		// the number of copies to make is "copies"
		List<EPlanElement> copiedElements = new ArrayList<EPlanElement>();
		// create a new transferable that will contain all of the elements to paste (the copies)
		mergedTransferable = new PlanTransferable();
		for(int i = 0; i < copies; i++) {
			PlanTransferable copyPlanTransferable = (PlanTransferable) modifier.copy(sourcePlanTransferable);
			for (IPlanTransferableExtension extension : PlanTransferableExtensionRegistry.getInstance().getExtensions()) {
				extension.mergeHook(copyPlanTransferable, mergedTransferable);
			}			
			copiedElements.addAll(copyPlanTransferable.getPlanElements());
		}
		
		mergedTransferable.setPlanElements(copiedElements);
		insertionLocation = modifier.getInsertionLocation(mergedTransferable, targetSelection, InsertionSemantics.ON);
		plan = EPlanUtils.getPlan(sourcePlanTransferable.getPlanElements().get(0));
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				modifier.add(mergedTransferable, insertionLocation);
			}
		});
		
		putNewCopyOnClipboard();
	}

	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		StructuredSelection selection = new StructuredSelection(mergedTransferable.getPlanElements());
		selectionProvider.setSelection(selection);
	}
	
	protected void putNewCopyOnClipboard() {
		try {
			ITransferable copiedStuff = modifier.copy(mergedTransferable);
			TransferRegistry.getInstance().putOnClipboard(copiedStuff);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			LogUtil.warn("failed to clone clipboard on paste, clearing instead");
			TransferRegistry.clearClipboardContents();
		}
	}	

	@Override
	protected void redo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				modifier.add(mergedTransferable, insertionLocation);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {		
				modifier.remove(mergedTransferable, insertionLocation);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(PlanCopyPasteManyTimesOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(mergedTransferable));
		builder.append(" onto ");
		builder.append(String.valueOf(targetSelection));
		builder.append(" landing at ");
		builder.append(String.valueOf(insertionLocation));
		return builder.toString();
	}
}
