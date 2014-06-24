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
package gov.nasa.ensemble.core.model.plan.patch;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.patch.PatchRollbackException;

import org.eclipse.emf.common.util.BasicEList;
import org.junit.Test;

/**
 * SPF-8674 regression test.
 */
public class TestPatchBuilderListReplacement extends TestPatchBuilderBaseSetup {

	@Test
	public void testAdding1() throws PatchRollbackException {		
		EPlan existingPlan = makePlanWithActivityNames("horse", "buggy", "trolley");
		assertNamesEqual("Test is not written correctly", existingPlan, "horse", "buggy", "trolley");		
		BasicEList<EPlanChild> replacementActivities = (BasicEList<EPlanChild>)makePlanWithActivityNames("bike", "car", "monorail").getChildren();
		assertNamesEqual("Test is not written correctly", replacementActivities, "bike", "car", "monorail");		
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.modify(existingPlan, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, replacementActivities.clone());
		builder.getPatch().applyAndReverse();
		assertNamesEqual("Did not replace with new names", existingPlan, "bike", "car", "monorail");		
	}
	
	
}

