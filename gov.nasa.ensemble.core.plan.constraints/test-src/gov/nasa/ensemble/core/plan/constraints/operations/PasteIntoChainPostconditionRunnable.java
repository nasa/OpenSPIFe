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
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;

import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;

public class PasteIntoChainPostconditionRunnable extends ChainRepeatabilityPostconditionRunnable {

	private final EPlanElement targetPlanElement;
	protected final Set<TemporalChain> initialChains;
	protected final TemporalChain targetChain;

	public PasteIntoChainPostconditionRunnable(ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement[] targetElements) {
		super("paste", plan, selectedElements);
		this.initialChains = TemporalChainUtils.getChains(plan.plan);
		this.targetPlanElement = targetElements[targetElements.length - 1];
		this.targetChain = targetPlanElement.getMember(ConstraintsMember.class, true).getChain();
	}
	
	@Override
	public void run() {
		super.run();
		Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
		Assert.assertFalse("the target chain should be replaced by another chain", chains.contains(targetChain));
		TemporalChain newChain = targetPlanElement.getMember(ConstraintsMember.class, true).getChain();
		Iterator<EPlanElement> oldIterator = targetChain.getElements().iterator();
		Iterator<EPlanElement> newIterator = newChain.getElements().iterator();
		while (oldIterator.hasNext() && newIterator.hasNext()) {
			EPlanElement oldPlanElement = oldIterator.next();
			EPlanElement newPlanElement = newIterator.next();
			Assert.assertSame("each old plan element should be present in the same order in the new chain. ", 
					          oldPlanElement, newPlanElement);
			if (oldPlanElement == targetPlanElement) {
				for (EPlanElement planElement : selectedElements) {
					EPlanElement newInsertedPlanElement = newIterator.next();
					Assert.assertSame("each inserted element should be present in the same order in the new chain, at the point of insertion. ",
							          planElement, newInsertedPlanElement);
				}
			}
		}
		Assert.assertFalse("both old and new chains should end on the same element",
			               oldIterator.hasNext() || newIterator.hasNext());
	}

}
