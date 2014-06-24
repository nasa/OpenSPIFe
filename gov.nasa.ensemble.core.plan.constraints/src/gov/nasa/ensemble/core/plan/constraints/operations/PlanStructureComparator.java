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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Comparator;

public final class PlanStructureComparator implements Comparator<EPlanChild> {
	
	public static final PlanStructureComparator INSTANCE = new PlanStructureComparator(); 
	
	@Override
	public int compare(EPlanChild element1, EPlanChild element2) {
		if (element1 == element2) {
			return 0;
		}
		if (element1 == null) {
			return -1;
		}
		if (element2 == null) {
			return 1;
		}
		EPlanElement parent1 = element1.getParent();
		EPlanElement parent2 = element2.getParent();
		if (parent1 == parent2) {
			int index1 = element1.getListPosition();
			int index2 = element2.getListPosition();
			return CommonUtils.compare(index1, index2);
		}
		if (parent1 == null) {
			return -1;
		}
		if (parent2 == null) {
			return 1;
		}
		return 0;
	}
	
}
