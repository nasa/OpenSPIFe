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
import gov.nasa.ensemble.common.ui.operations.ClipboardPasteOperation;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class PlanClipboardPasteOperation extends ClipboardPasteOperation implements IDisplayOperation {

	protected final IStructureModifier modifier;
	private final ISelection targetSelection;
	private Long targetPlanUniqueId;
	private EPlanElement targetElement;
	private EPlan context;
	
	public PlanClipboardPasteOperation(ISelection targetSelection, IStructureModifier modifier) {
		super(targetSelection, modifier);
		this.targetSelection = targetSelection;
		this.modifier = modifier;
	}		

	@Override
	protected final boolean somethingAvailable() {
		if ((targetSelection == null) || !(targetSelection instanceof IStructuredSelection)) {
			return false;
		}
		IStructuredSelection selection = (IStructuredSelection) targetSelection;
		Set<EPlanElement> elements = PlanEditorUtil.emfFromSelection(selection);
		if (elements.isEmpty()) {
			return false;
		}
		targetElement = elements.iterator().next();
		EPlan plan = EPlanUtils.getPlan(targetElement);
		if (plan == null) {
			return false; // can't paste onto a selection that isn't in a plan
		}
		context = plan;
		targetPlanUniqueId = plan.getRuntimeId();
		return super.somethingAvailable();
	}

	@Override
	protected void getTransferableFromClipboard() {
		super.getTransferableFromClipboard();
		if (transferable instanceof PlanTransferable) {
			PlanTransferable planTransferable = (PlanTransferable) transferable;
			boolean useCopy = false;
			Long clipboardPlanUniqueId = planTransferable.getPlanUniqueId();
			if ((clipboardPlanUniqueId != null) && !clipboardPlanUniqueId.equals(targetPlanUniqueId)) {
				useCopy = true;
			}
			if (useCopy) {
				transferable = getTransferableAfterMovingBetweenPlans(planTransferable);
			}
		}
	}
	
	@Override
	protected void doit() {
		TransactionUtils.writing(context, new Runnable() {
 			@Override
			public void run() {
 				PlanClipboardPasteOperation.super.doit();
 			}
 		});
	}

	@Override
	protected void undo() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				PlanClipboardPasteOperation.super.undo();
			}
		});
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		if (transferable instanceof PlanTransferable) {
			PlanTransferable planTransferable = (PlanTransferable) transferable;
    		List<? extends EPlanElement> elements = planTransferable.getPlanElements();
    		ISelectionProvider selectionProvider = site.getSelectionProvider();
    		IStructuredSelection selection = new StructuredSelection(elements);
    		selectionProvider.setSelection(selection);
    	}
	}

	private PlanTransferable getTransferableAfterMovingBetweenPlans(PlanTransferable transferable) {
		return (PlanTransferable)modifier.copy(transferable);
	}

	/**
	 * Assert that two elements are identical for all parameters.
	 * Used for testing purposes.
	 *
	 * @param expectedElement
	 * @param actualElement
	 * @param moveBetweenPlans if true, compare using getPlanElementsAfterMovingBetweenPlans
	 */
	public void assertMatches(EPlanElement expectedElement, EPlanElement actualElement, boolean moveBetweenPlans) {
		if (moveBetweenPlans) {
			PlanTransferable transferable = new PlanTransferable();
			transferable.setPlanElements(Collections.singletonList(expectedElement));
			transferable = getTransferableAfterMovingBetweenPlans(transferable);
			expectedElement = transferable.getPlanElements().iterator().next(); 
		}
		Assert.assertFalse(expectedElement == actualElement);
		String expectedString = PlanElementXMLUtils.getString(expectedElement);
		String actualString = PlanElementXMLUtils.getString(actualElement);
		Assert.assertEquals("didn't match" + (moveBetweenPlans ? " when move between plans" : ""), expectedString, actualString);
	}

}
