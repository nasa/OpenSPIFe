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

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.DiffIdGenerator;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;

public class TemplatePlanDuplicateOperation extends TemplatePlanOperation {
	
	/** Passed into the constructor. A collection of plan elements to be duplicated. */
	private Collection<EPlanElement> elementsToDuplicate;
	
	/** The duplicate of the collection passed into the constructor. Initially empty, and filled by execute(). */
	private Collection<EPlanElement> duplicates;
	
	/**
	 * Decorate by storing the list of elements to duplicate.
	 * @param label an immutable string label that can be fetched; may be null
	 * @param templatePlan stored and gettable, but otherwise ignored
	 * @param elementsToDuplicate a list of plan elements to duplicate; may be empty but must not be null
	 */
	public TemplatePlanDuplicateOperation(String label, EPlan templatePlan, Collection<EPlanElement> elementsToDuplicate) {
		super(label, templatePlan);
		this.elementsToDuplicate = elementsToDuplicate;
		duplicates = new ArrayList<EPlanElement>();
	}

	/**
	 * Execute the duplicate operation. For each plan element in the list passed into the constructor, make a
	 * copy and give prefix "copy of" to the original's name. Change the DiffId of the common member. Add
	 * the plan element to the list of duplicates. If successful, the template plan is flagged as dirty.
	 * Note: there is no check that elementsToDuplicate is not null.
	 */
	@Override
	public void execute() throws Throwable {
		for(EPlanElement element: elementsToDuplicate) {
			final EPlanElement copy = TemplatePlanUtils.getCopy(element, false);
			final Object currentName = copy.eGet(PlanPackage.Literals.EPLAN_ELEMENT__NAME);				
			TransactionUtils.writing(getTemplatePlan(), new Runnable() {
				@Override
				public void run() {
					copy.eSet(PlanPackage.Literals.EPLAN_ELEMENT__NAME, "copy of " + currentName);
					CommonMember commonMember = copy.getMember(CommonMember.class);
					// give the new activity a unique template id
					DiffIdGenerator instance = DiffIdGenerator.getInstance();
					String generatedDiffId = instance.generateDiffId(copy.eClass());
					commonMember.setDiffID(generatedDiffId);
					maybeAddToPlan(copy);
					duplicates.add(copy);
				}

			});
		}
	}
	
	protected void maybeAddToPlan(final EPlanElement copy) {
		getTemplatePlan().getChildren().add((EPlanChild)copy);
		needsSave = true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<EPlanElement> getDuplicates() {
		return this.duplicates;
	}
}
