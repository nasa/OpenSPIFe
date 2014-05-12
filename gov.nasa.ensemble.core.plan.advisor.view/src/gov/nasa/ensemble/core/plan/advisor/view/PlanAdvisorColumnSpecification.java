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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;

public enum PlanAdvisorColumnSpecification {
	
	NAME("Violations", ViolationKey.NAME, 150),
	DESCRIPTION("Description", ViolationKey.DESCRIPTION, 180),
	ELEMENT("Participants", ViolationKey.ELEMENTS, 120),
	CONTAINER(EnsembleProperties.getStringPropertyValue("ensemble.plan.activityGroupName", "Context"), ViolationKey.CONTAINERS, 80),
	TIME("Time", ViolationKey.TIME, 110),
	TYPE("Type", ViolationKey.TYPE, 80),
	STATUS("Status", ViolationKey.STATUS, 40),
	FIXABLE("Fixable", ViolationKey.FIXABLE, 40),
	ADVISOR("Advisor", ViolationKey.ADVISOR, 80),
	/** APEX-specific.  Other products can use advisor.hideColumns=PPCR Status in ensemble.properties.
	 * TODO:  If anyone wants to do this right, rewrite this to be a MissionExtendable class instead of an enum.
	 */
	PPCR_STATUS("PPCR Status", ViolationKey.PPCR_STATUS, 150),
	;
	
	private final String headerText;
	private final ViolationKey key;
	private final int defaultWidth;

	private PlanAdvisorColumnSpecification(String headerText, ViolationKey key, int defaultWidth) {
		this.headerText = headerText;
		this.key = key;
		this.defaultWidth = defaultWidth;
	}

	public String getHeaderText() {
		return headerText;
	}
	
	public ViolationKey getViolationKey() {
		return key;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public boolean isGroupable() {
		return true;
	}
	
}
