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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

public class MoveOutsideOfChainPostconditionRunnable extends ChainRepeatabilityPostconditionRunnable {

	private final Set<TemporalChain> completelyMovedChains = new HashSet<TemporalChain>();
	private final Set<TemporalChain> allButOneMovedChains = new HashSet<TemporalChain>();
	private final Set<TemporalChain> unaffectedChains = new HashSet<TemporalChain>();
	private final List<List<EPlanElement>> unselectedChainsMembers = new ArrayList<List<EPlanElement>>();
	private final List<List<EPlanElement>> selectedChainsMembers = new ArrayList<List<EPlanElement>>();

	public MoveOutsideOfChainPostconditionRunnable(ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement targetElement) {
		super("move", plan, selectedElements);
		List<EPlanElement> selectedElementsList = Arrays.asList(selectedElements);
		for (TemporalChain chain : TemporalChainUtils.getChains(plan.plan)) {
			List<EPlanElement> unselectedSubset = new ArrayList<EPlanElement>();
			List<EPlanElement> selectedSubset = new ArrayList<EPlanElement>();
			for (EPlanElement chainElement : chain.getElements()) {
				if (selectedElementsList.contains(chainElement)) {
					selectedSubset.add(chainElement);
				} else {
					unselectedSubset.add(chainElement);
				}
			}
			if (selectedSubset.isEmpty()) {
				unaffectedChains.add(chain);
			} else if (unselectedSubset.isEmpty()) {
				completelyMovedChains.add(chain);
			} else if (unselectedSubset.size() == 1) {
				allButOneMovedChains.add(chain);
			}
			if (unselectedSubset.size() >= 2 && !selectedSubset.isEmpty()) {
				unselectedChainsMembers.add(unselectedSubset);
			}
			if (selectedSubset.size() >= 2 && !unselectedSubset.isEmpty()) {
				selectedChainsMembers.add(selectedSubset);
			}
		}
	}

	@Override
	public void run() {
		Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
		Assert.assertTrue("chains that have all of their activities " + actionName  + " should still exist. ", 
			              chains.containsAll(completelyMovedChains));
		Assert.assertTrue("chains that have all but one of their activities " + actionName + " should be removed. ", 
			              Collections.disjoint(allButOneMovedChains, chains));
		Assert.assertTrue("chains that had no " + actionName + " activities should remain. ", 
			              chains.containsAll(unaffectedChains));
		chains.removeAll(completelyMovedChains); 
		chains.removeAll(unaffectedChains); // leaving only the new chains
		Assert.assertEquals("new chains should number the chains leftover from unselected elements plus the new chains from the selected elements. ",
			                unselectedChainsMembers.size() + selectedChainsMembers.size(), chains.size());
		for (TemporalChain chain : chains) {
			List<EPlanElement> chainElements = chain.getElements();
			EPlanElement firstPlanElement = chainElements.iterator().next();
			List<EPlanElement> expectedChainMembers = null;
			for (List<EPlanElement> unselectedChainMembers : unselectedChainsMembers) {
				if (unselectedChainMembers.iterator().next() == firstPlanElement) {
					Assert.assertNull("each plan element should only be in one chainMembers list", expectedChainMembers);
					expectedChainMembers = unselectedChainMembers;
				}
			}
			for (List<EPlanElement> selectedChainMembers : selectedChainsMembers) {
				if (selectedChainMembers.iterator().next() == firstPlanElement) {
					Assert.assertNull("each plan element should only be in one chainMembers list", expectedChainMembers);
					expectedChainMembers = selectedChainMembers;
				}
			}
			Assert.assertNotNull("a new chain should correspond to one of the expected chainMembers", expectedChainMembers);
			if (expectedChainMembers != null) {
				Assert.assertEquals("a new chain should be the same size as expected",
						expectedChainMembers.size(), chainElements.size());
				Iterator<EPlanElement> chainElementIterator = chainElements.iterator();
				Iterator<EPlanElement> expectedChainMembersIterator = expectedChainMembers.iterator();
				while (chainElementIterator.hasNext() && expectedChainMembersIterator.hasNext()) {
					EPlanElement chainPlanElement = chainElementIterator.next();
					EPlanElement memberPlanElement = expectedChainMembersIterator.next();
					Assert.assertEquals("all elements in the resulting chain should exist in the order expected", memberPlanElement, chainPlanElement);
				}
			}
		}
	}
	
}
