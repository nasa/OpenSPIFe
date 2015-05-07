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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

public class RemovingChainActivitiesPostconditionRunnable extends ChainRepeatabilityPostconditionRunnable {

	private final String actionName;
	private final Set<TemporalChain> completelyDeletedChains = new HashSet<TemporalChain>();
	private final Set<TemporalChain> allButOneDeletedChains = new HashSet<TemporalChain>();
	private final Set<TemporalChain> unaffectedChains = new HashSet<TemporalChain>();
	private final Map<TemporalChain, Integer> shortenedChainsToExpectedLength = new HashMap<TemporalChain, Integer>();

	public RemovingChainActivitiesPostconditionRunnable(String actionName, ChainTestPlan plan, EPlanElement[] selectedElements) {
		super(actionName, plan, selectedElements);
		this.actionName = actionName;
		List<EPlanElement> planElementsToRemoveList = Arrays.asList(selectedElements);
		for (TemporalChain chain : TemporalChainUtils.getChains(plan.plan)) {
			List<EPlanElement> chainElements = chain.getElements();
			int leftovers = chainElements.size();
			for (EPlanElement chainElement : chainElements) {
				if (planElementsToRemoveList.contains(chainElement)) {
					leftovers--;
				}
			}
			if (leftovers == 0) {
				completelyDeletedChains.add(chain);
			} else if (leftovers == 1) {
				allButOneDeletedChains.add(chain);
			} else if (leftovers == chainElements.size()) {
				unaffectedChains.add(chain);
			} else {
				shortenedChainsToExpectedLength.put(chain, leftovers);
			}
		}
	}

	@Override
	public void run() {
		super.run();
		Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
		Assert.assertTrue("chains that have all of their activities " + actionName + " should be removed. ", 
			              Collections.disjoint(completelyDeletedChains, chains));
		Assert.assertTrue("chains that have all but one of their activities " + actionName + " should be removed. ", 
			              Collections.disjoint(allButOneDeletedChains, chains));
		Assert.assertTrue("chains that had no " + actionName + " activities should remain. ", 
			              chains.containsAll(unaffectedChains));
		chains.removeAll(unaffectedChains); // leaving only the shortened chains
		Assert.assertEquals("chains that had two activities left after " + actionName + " should remain. ", 
			                shortenedChainsToExpectedLength.size(), chains.size());
		Assert.assertTrue(shortenedChainsToExpectedLength.size() <= 1); // TODO: handle more than 1 affected chain (see below)
		for (Map.Entry<TemporalChain, Integer> entry : shortenedChainsToExpectedLength.entrySet()) {
			TemporalChain shortenedChain = entry.getKey();
			Integer expectedLength = entry.getValue();
			TemporalChain affectedChain = chains.iterator().next(); // TODO: should somehow map new chains to old chains here (part of handle more than 1 affected chain)
			List<EPlanElement> shortenedChainElements = shortenedChain.getElements();
			Assert.assertEquals(actionName + " activities from a chain of " + shortenedChainElements.size()
				                           + " should leave " + expectedLength + ".", 
				                expectedLength, Integer.valueOf(affectedChain.getElements().size()));
			Iterator<EPlanElement> initialChainElementIterator = shortenedChainElements.iterator();
			for (EPlanElement chainElement : affectedChain.getElements()) {
				while (initialChainElementIterator.next() != chainElement) {
					Assert.assertTrue("all elements in the resulting chain should be present in the same order in the initial chain. ", 
						       initialChainElementIterator.hasNext());
				}
				for (EPlanElement element : selectedElements) {
					Assert.assertFalse("a " + actionName + " element shouldn't be in the chain. ", chainElement == element);
				}
			}
		}
	}
}
