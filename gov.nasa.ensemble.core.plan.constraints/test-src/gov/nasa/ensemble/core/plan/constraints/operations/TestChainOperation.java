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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class TestChainOperation extends UndoableOperationTestCase {

	public static final PlanStructureModifier PLAN_STRUCTURE_MODIFIER = PlanStructureModifier.INSTANCE;

	public TestChainOperation() {
		super();
	}

	protected final List<List<EPlanElement>> constructTransferableChainsMembers(EPlan plan, EPlanElement[] selectedElements) {
		List<EPlanElement> selectedElementsList = Arrays.asList(selectedElements);
		List<List<EPlanElement>> newChainsMembers = new ArrayList<List<EPlanElement>>();
		for (TemporalChain chain : TemporalChainUtils.getChains(plan)) {
			List<EPlanElement> selectedChainMembers = new ArrayList<EPlanElement>();
			for (EPlanElement chainElement : chain.getElements()) {
				if (selectedElementsList.contains(chainElement)) {
					selectedChainMembers.add(chainElement);
				}
			}
			if (selectedChainMembers.size() >= 2) {
				newChainsMembers.add(selectedChainMembers);
			}
		}
		return newChainsMembers;
	}
	
	protected final List<List<EPlanElement>> constructRemainingElementChainsMembers(final EPlanElement[] selectedElements) {
		List<List<EPlanElement>> newChainsMembers = new ArrayList<List<EPlanElement>>();
		TemporalChain currentChain = null;
		List<EPlanElement> currentChainMembers = new ArrayList<EPlanElement>();
		for (EPlanElement selectedElement : selectedElements) {
			ConstraintsMember constraintsMember = selectedElement.getMember(ConstraintsMember.class, true);
			TemporalChain chain = constraintsMember.getChain();
			if ((chain != null) && (chain == currentChain)) {
				currentChainMembers.add(selectedElement);
				continue;
			}
			if (currentChainMembers.size() >= 2) {
				newChainsMembers.add(currentChainMembers);
				currentChainMembers = new ArrayList<EPlanElement>();
			}
			if (chain != null) {
				currentChain = chain;
				currentChainMembers.add(selectedElement);
			} else {
				currentChain = null;
			}
		}
		if (currentChainMembers.size() >= 2) {
			newChainsMembers.add(currentChainMembers);
		}
		return newChainsMembers;
	}

	protected static void assertPlanUnmodified(ChainTestPlan plan, List<? extends EActivity> initialActivities, Set<TemporalChain> initialChains) {
		List<? extends EActivity> finalActivities = plan.getActivities();
		Set<TemporalChain> finalChains = TemporalChainUtils.getChains(plan.plan);
		assertEquals("the same number of chains should be in the plan at the end", initialChains.size(), finalChains.size());
		assertTrue("each initial chain should be in the plan at the end", finalChains.containsAll(initialChains));
		assertEquals("the same number of activities should be in the plan at the end", initialActivities.size(), finalActivities.size());
		assertTrue("each initial activity should be in the plan at the end", finalActivities.containsAll(initialActivities));
	}

	public static String getPlanElementStateString(EPlanElement planElement, String string) {
		StringBuilder sb = new StringBuilder(string);
		if (planElement instanceof EPlan) {
			for (TemporalChain chain : TemporalChainUtils.getChains((EPlan)planElement)) {
				sb.append("chain:");
				for (EPlanElement chainElement : chain.getElements()) {
					sb.append(" ").append(chainElement);
				}
				sb.append("\n");
			}
		} else {
			ConstraintsMember constraintsMember = planElement.getMember(ConstraintsMember.class, true);
			TemporalChain chain = constraintsMember.getChain();
			if (chain != null) {
				sb.append("chainMember: ").append(chain);
			}
		}
		return sb.toString();
	}

}
