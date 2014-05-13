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

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityDefTransferable;
import gov.nasa.ensemble.tests.core.plan.BasicTestPlan;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestChainActivityDefAdd extends TestChainOperation {

	@Override
	protected void setUp() throws Exception {
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testChainActivityDefDrop() {
		ChainTestPlan plan = new ChainTestPlan();
		List<? extends EActivity> initialActivities = plan.getActivities();
		Set<TemporalChain> initialChains = TemporalChainUtils.getChains(plan.plan);
		for (EActivity activity : plan.getActivities()) {
			EPlanElement[] targetElements = new EPlanElement[] { activity };
			testActivityDefDrop(plan, targetElements);
			assertPlanUnmodified(plan, initialActivities, initialChains);
		}
		plan.dispose();
	}
	
	private void testActivityDefDrop(ChainTestPlan plan, EPlanElement[] targetElements) {
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = new ActivityDefTransferable(Collections.singletonList(BasicTestPlan.def));
		IStructureLocation location = modifier.getInsertionLocation(transferable, targetSelection, InsertionSemantics.ON);
		IUndoableOperation add = new PlanAddOperation(transferable, modifier, location);
		testUndoableOperation(plan.plan, add, new ChainRepeatabilityPostconditionRunnable("activity def drop", plan, targetElements));
		plan.clearHistory();
	}

}
