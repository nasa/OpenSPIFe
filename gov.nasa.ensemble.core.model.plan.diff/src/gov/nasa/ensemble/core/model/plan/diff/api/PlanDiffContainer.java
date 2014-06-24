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
package gov.nasa.ensemble.core.model.plan.diff.api;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.impl.AbstractPlanDiffContainer;

/** 
 * A container for applications that need a plan diff list without computing diffs,
 * e.g. to divide an existing list of diffs into accepted and rejected changes.
 * 
 */
public class PlanDiffContainer extends AbstractPlanDiffContainer {
	
	@Override
	public EPlanElement findNewCopy(EPlanElement x) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EPlanElement findOldCopy(EPlanElement x) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EPlanElement findOtherCopy(EPlanElement x) {
		throw new UnsupportedOperationException();
	}
	
}
