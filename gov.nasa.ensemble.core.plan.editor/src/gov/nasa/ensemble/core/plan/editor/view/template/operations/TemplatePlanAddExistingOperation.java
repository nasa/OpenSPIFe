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
package gov.nasa.ensemble.core.plan.editor.view.template.operations;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * An operation for adding a copy of a structured selection as a child of a template plan.
 */
public class TemplatePlanAddExistingOperation extends
TemplatePlanOperation {

	/** Passed into the constructor. May be null. */
	private IStructuredSelection structuredSelection;

	/**
	 * Decorate by storing the structuredSelection.
	 * @param structuredSelection the selection
	 * @param plan the template plan into which the structuredSelection is to be added
	 */
	public TemplatePlanAddExistingOperation(IStructuredSelection structuredSelection, EPlan plan) {
		super("add to template plan", plan);
		this.structuredSelection = structuredSelection;
	}
	
	/**
	 * Execute the add-existing operation. Convert the structuredSelection into a PlanTransferable and copy it.
	 * Then add that copy into the template plan at the insertion location. If successful, the template plan is
	 * flagged as dirty.
	 */
	@Override
	public void execute() throws Throwable {
		final PlanTransferable planTransferable = createCopy(structuredSelection);
		TransactionUtils.writing(getTemplatePlan(), new Runnable() {
			@Override
			public void run() {
				PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
				IStructureLocation location = modifier.getInsertionLocation(planTransferable, new StructuredSelection(getTemplatePlan()), InsertionSemantics.ON);
				modifier.add(planTransferable, location);
				needsSave = true;
			}						
		});		
	}

	/** Return a null string reference. */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Create and return a copy of a new PlanTransferable from the StructuredSelection
	 * @param selection
	 * @return
	 */
	private PlanTransferable createCopy(IStructuredSelection selection) {
		PlanStructureModifier planStructureModifier = PlanStructureModifier.INSTANCE;
		PlanTransferable source = planStructureModifier.getTransferable(selection);
		PlanTransferable result = (PlanTransferable) planStructureModifier.copy(source);
		return result;
	}		
}
