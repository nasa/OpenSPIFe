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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardCutOperation;
import gov.nasa.ensemble.tests.core.plan.Combinations;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestChainCuts extends TestChainOperation {

	@Override
	protected void setUp() throws Exception {
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testChainCuts() {
		ChainTestPlan plan = new ChainTestPlan();
		List<? extends EActivity> initialActivities = plan.getActivities();
		Set<TemporalChain> initialChains = TemporalChainUtils.getChains(plan.plan);
		Combinations combinations = new Combinations(plan.group1activities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] elements = combinations.nextElement();
			testCuttingActivities(plan, elements);
			assertPlanUnmodified(plan, initialActivities, initialChains);
		}
		plan.dispose();
	}
	
	private void testCuttingActivities(final ChainTestPlan plan, final EPlanElement[] planElementsToCut) {
		ISelection selection = new StructuredSelection(planElementsToCut);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new PlanClipboardCutOperation(transferable, modifier);
		final Runnable postcondition1 = new RemovingChainActivitiesPostconditionRunnable("cut", plan, planElementsToCut);
		final Runnable postcondition2 = new ClipboardContentsPostconditionRunnable(planElementsToCut, false);
		testUndoableOperation(plan.plan, operation, new Runnable() {
			@Override
			public void run() {
				postcondition1.run();
				postcondition2.run();
			}
		});
		plan.clearHistory();
	}

}
