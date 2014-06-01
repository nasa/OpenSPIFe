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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.IMember;

public class PlanModifierMember implements IMember {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7510912026123748090L;
	private IPlanModifier modifier;
	
	public static PlanModifierMember get(EPlan plan) {
		return WrapperUtils.getMember(plan, PlanModifierMember.class);
	}

	/* package */ PlanModifierMember(EPlan plan) {
		PlanModifierFactory factory = PlanModifierRegistry.getInstance().getDefaultPlanModifierFactory();
		this.modifier = factory.instantiateModifier();
		modifier.initialize(plan);
	}
	
	public IPlanModifier getModifier() {
		return modifier;
	}
	
	public void setModifier(IPlanModifier modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public void dispose() {
		modifier = null;
	}

}
