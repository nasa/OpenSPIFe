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
package gov.nasa.ensemble.core.model.plan.diff.top;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffFeatureFilter;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.impl.OldAndNewCopyOfSameThingImpl;
import gov.nasa.ensemble.core.model.plan.diff.impl.PlanDiffListImpl;
import gov.nasa.ensemble.core.model.plan.diff.impl.PlanMatcher;

public class PlanDiffEngine {

	public static PlanDiffList findChanges(EPlan plan1, EPlan plan2) {
		return new PlanDiffListImpl(plan1, plan2, null, null);
	}
	
	public static PlanDiffList findChanges(EPlan plan1, EPlan plan2, PlanMatcher matcher, PlanDiffFeatureFilter featureFilter) {
		return new PlanDiffListImpl(plan1, plan2, matcher, featureFilter);
	}
	
	public static PlanDiffList findChangesSpecificallyTo(EPlanElement oldCopy, EPlanElement newCopy) {
		return new PlanDiffListImpl(new OldAndNewCopyOfSameThingImpl(oldCopy, newCopy), null, null);
	}
	
	public static PlanDiffList findChangesSpecificallyTo(EPlanElement oldCopy, EPlanElement newCopy,
			PlanMatcher matcher, PlanDiffFeatureFilter featureFilter) {
		return new PlanDiffListImpl(new OldAndNewCopyOfSameThingImpl(oldCopy, newCopy), matcher, featureFilter);
	}
	
}
