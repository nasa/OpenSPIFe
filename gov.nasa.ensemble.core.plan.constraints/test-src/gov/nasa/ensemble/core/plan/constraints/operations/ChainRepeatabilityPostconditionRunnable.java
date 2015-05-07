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

import java.util.Set;

import org.junit.Assert;

public class ChainRepeatabilityPostconditionRunnable implements Runnable {

	protected final String actionName;
	protected final ChainTestPlan plan;
	protected final EPlanElement[] selectedElements;
	private Set<TemporalChain> chainsAfterExecute = null;
	
	public ChainRepeatabilityPostconditionRunnable(String actionName, ChainTestPlan plan, EPlanElement[] selectedElements) {
		this.actionName = actionName;
		this.plan = plan;
		this.selectedElements = selectedElements;
	}
	
	@Override
	public void run() {
		if (chainsAfterExecute == null) { // EXECUTE
			chainsAfterExecute = TemporalChainUtils.getChains(plan.plan);
		} else { // REDO
			Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
			Assert.assertEquals("wrong number of chains after redo of " + actionName, 
				                chains.size(), chainsAfterExecute.size());
			Assert.assertTrue("expected the same chains after redo of " + actionName,
				              chains.containsAll(chainsAfterExecute));
		}
	}

}
