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
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;

import java.util.List;
import java.util.Set;

import org.junit.Assert;

public class PasteOutsideOfChainPostconditionRunnable extends ChainRepeatabilityPostconditionRunnable {
	private final List<List<EPlanElement>> expectedNewChainsMembers;
	protected final Set<TemporalChain> initialChains;

	public PasteOutsideOfChainPostconditionRunnable(ChainTestPlan plan, EPlanElement[] selectedElements, List<List<EPlanElement>> expectedNewChainsMembers) {
		super("paste", plan, selectedElements);
		this.expectedNewChainsMembers = expectedNewChainsMembers;
		this.initialChains = TemporalChainUtils.getChains(plan.plan);
	}

	@Override
	public void run() {
		super.run();
		Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
		Assert.assertEquals(selectedElements + " : wrong number of expected chains after paste", initialChains.size() + expectedNewChainsMembers.size(), chains.size());
		for (List<EPlanElement> expectedNewChainMembers : expectedNewChainsMembers) {
			TemporalChain chain = expectedNewChainMembers.iterator().next().getMember(ConstraintsMember.class, true).getChain();
			Assert.assertNotNull(selectedElements + " : expected a pasted element to be in a chain", chain);
			for (int i = 0 ; i < expectedNewChainMembers.size() ; i++) {
				EPlanElement expectedPlanElement = expectedNewChainMembers.get(i);
				EPlanElement planElement = chain.getElements().get(i);
				Assert.assertEquals(selectedElements + " : a pasted element should be in its place in the resulting chain", expectedPlanElement, planElement);
			}
		}
	}
	
}
