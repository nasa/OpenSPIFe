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
package gov.nasa.ensemble.core.plan.advisor.provider;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils.PlanElementSeverityProvider;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;

import java.util.List;

import org.eclipse.core.resources.IMarker;

public class PlanAdvisorSeverityProvider extends PlanElementSeverityProvider {

	@Override
	public Integer getSeverity(EPlanElement element, EPlan plan, boolean includeChildren) {
		if (plan.isTemplate() || plan.eResource() == null) {
			// avoid creating a PlanAdvisorMember for a template plan
			// or a plan that does not have an associated resource
			return null;
		}
		PlanAdvisorMember advisorMember = PlanAdvisorMember.get(plan);
		if (advisorMember != null) {
			return getHighestSeverity(element, advisorMember, includeChildren);
		}
		return null;
	}
	
	
	private static int getHighestSeverity(EPlanElement element, PlanAdvisorMember advisorMember, boolean includeChildren) {
		int severity = Integer.MIN_VALUE;
		for (IMarker marker : advisorMember.getPlanElementMarkers(element)) {
			int markerSeverity = marker.getAttribute(IMarker.SEVERITY, Integer.MIN_VALUE);
			if (markerSeverity >= IMarker.SEVERITY_ERROR) {
				return markerSeverity;
			}
			if (severity < markerSeverity) {
				severity = markerSeverity;
			}
		}
		if (includeChildren && element instanceof EPlanChild) {
			List<? extends EPlanChild> children = ((EPlanChild)element).getChildren();
			for (EPlanChild child : children) {
				int highestSeverity = getHighestSeverity(child, advisorMember, true);
				if (highestSeverity == IMarker.SEVERITY_ERROR) {
					return highestSeverity;
				}
				if (severity < highestSeverity) {
					severity = highestSeverity;
				}
			}
		}
		return severity;
	}


}
