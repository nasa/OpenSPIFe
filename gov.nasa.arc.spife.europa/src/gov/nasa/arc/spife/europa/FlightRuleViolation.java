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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FlightRuleViolation implements IFlightRuleViolation {

	private final Date startTime;
	private final String type;
	private final double level;
	private final List<EActivity> culprits;
	
	/**
	 * Constructor.
	 * 
	 * @param rawTime
	 * @param type
	 * @param level
	 * @param culprits
	 */
	public FlightRuleViolation(Date startTime, String type, double level, List<EActivity> culprits) {
		this.startTime = startTime;		
		this.type = type;
		this.level = level;
		this.culprits = Collections.unmodifiableList(culprits);
	}

	@Override
	public int hashCode() {
		int code = (type == null ? 0 : type.hashCode());
		for (EPlanElement element : culprits) {
			code += element.hashCode();
		}
		return code;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FlightRuleViolation) {
			FlightRuleViolation violation = (FlightRuleViolation) obj;
			return (CommonUtils.equals(type, violation.type)
					&& CommonUtils.equals(culprits, violation.culprits));
		}
		return super.equals(obj);
	}
	
	@Override
	public Date getStartTime() {
		return startTime;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public double getLevel() {
		return level;
	}
	
	@Override
	public List<EActivity> getCulprits() {
		return culprits;
	}
	
	@Override
	public boolean isWaived () {
		for (EActivity culprit : culprits) {
			ActivityAdvisorMember advisorMember = culprit.getMember(ActivityAdvisorMember.class);
			Boolean waived = advisorMember.getWaivingAllFlightRules();
			if (waived != null && waived.booleanValue()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "startTime= " + startTime + " type= " + type + 
		       " level= " + level + " culprits= " + culprits;
	}
}
