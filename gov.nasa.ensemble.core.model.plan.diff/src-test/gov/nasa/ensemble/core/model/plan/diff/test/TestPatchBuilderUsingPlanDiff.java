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
package gov.nasa.ensemble.core.model.plan.diff.test;

import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Perform this same set of tests that was designed to test PlanDiff,
 * but with one difference, intended to test ChangeDescriptionOperation:
 * when applying the known changes to the plan, use a more roundabout
 * implementation that makes the changes using ChangeDescriptionOperation
 * instead of the existing test code that applies them directly.
 * Thus, if TestPlanDiffSyntheticThorough passes and this test fails,
 * that indicates a bug in ChangeDescriptionOperation rather than PlanDiff,
 * since otherwise the test code and diff code are the same in both tests.
 * @see TestPlanDiffSyntheticThorough
 * @see PatchBuilder
 * @see ChangeDescription
 */
public class TestPatchBuilderUsingPlanDiff extends TestPlanDiffSyntheticThorough {

	@Override
	protected PlanForPlanDiffTest newPlan() {
		return new PlanImplementedWithPatchBuilder();
	}

	@Override
	protected PlanForPlanDiffTest newPlan(ResourceSet resourcerSet) {
		return new PlanImplementedWithPatchBuilder(resourcerSet);
	}

	@Override
	protected void sanityCheck(PlanForPlanDiffTest plan) {
		super.sanityCheck(plan);
		assertEquals("Wrong class of plan -- something wrong with test.",
				PlanImplementedWithPatchBuilder.class, plan.getClass());
		}
}
