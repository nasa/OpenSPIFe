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

import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.tests.core.plan.BasicTestPlan;

import java.util.Arrays;
import java.util.List;

public class ChainTestPlan extends BasicTestPlan {

	public final EActivity[] chain1activities = new EActivity[] {
		activity1_1, activity1_2, activity1_3, activity1_4, activity1_5
	};
	public final TemporalChain chain1 = TemporalChainUtils.createChain(Arrays.asList(chain1activities));
	
	public final EActivity[] chain2activities = new EActivity[] {
			activity2_2, activity2_3, activity2_4
		};
	public final TemporalChain chain2 = TemporalChainUtils.createChain(Arrays.asList(chain2activities));
		
	public ChainTestPlan() {
		super("ChainTestPlan");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				TemporalChainUtils.attachChain(chain1);
				TemporalChainUtils.attachChain(chain2);
			}
		});
	}

	public List<? extends EActivity> getActivities() {
		return EPlanUtils.getActivities(plan);
	}

}
