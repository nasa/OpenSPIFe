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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.LinkedHashSet;

public class PlanAdvisorGroup {

	private final ViolationKey key;
	private final String value;
	private LinkedHashSet<ViolationTracker> violationTrackers;

	public PlanAdvisorGroup(ViolationKey key, String value) {
		this.key = key;
		this.value = value;
	}

	public ViolationKey getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}

	public void setViolationTrackers(LinkedHashSet<ViolationTracker> violationTrackers) {
		this.violationTrackers = violationTrackers;
	}
	
	public LinkedHashSet<ViolationTracker> getViolationTrackers() {
		return violationTrackers;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(PlanAdvisorGroup.class.getSimpleName());
		builder.append("[" + key + " = " + value + "]");
		return builder.toString();
	}

	public String getPrintString(ViolationKey column) {
		if (isInheritedFromOnlyChld(column) && violationTrackers.size()>=1) {
			return violationTrackers.iterator().next().getViolation().getPrintStringForParent(column);
		} else {
			return "";
		}
	}

	private boolean isInheritedFromOnlyChld(ViolationKey column) {
		switch (column) {
		case PPCR_STATUS: // the only one to appear at the group level, by design (SPF-11943)
			return (ViolationKey.ELEMENTS == this.getKey());
		default:
			return false;
		}
	}

}
