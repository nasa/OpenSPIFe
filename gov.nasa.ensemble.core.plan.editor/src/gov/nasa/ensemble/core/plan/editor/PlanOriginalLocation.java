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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanElementState;

import java.util.HashMap;

public class PlanOriginalLocation extends PlanStructureLocation {

	private String PLAN_ELEMENT_STATE_KEY = "gov.nasa.ensemble.core.plan.editor.PlanElementState"; 
	
	public PlanOriginalLocation() {
		map.put(PLAN_ELEMENT_STATE_KEY, new HashMap<EPlanElement, PlanElementState>());
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<EPlanElement, PlanElementState> getPlanElementStateMap() {
		return (HashMap<EPlanElement, PlanElementState>)map.get(PLAN_ELEMENT_STATE_KEY);
	}
	
	public void setPlanElementState(EPlanElement element, PlanElementState state) {
		getPlanElementStateMap().put(element, state);
	}
	
	public PlanElementState getPlanElementState(EPlanElement element) {
		return getPlanElementStateMap().get(element);
	}

	@Override
	public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(map.toString());
		return builder.toString();
    }

}
