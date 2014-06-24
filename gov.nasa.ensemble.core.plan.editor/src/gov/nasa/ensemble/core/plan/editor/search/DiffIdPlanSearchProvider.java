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
package gov.nasa.ensemble.core.plan.editor.search;

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

public class DiffIdPlanSearchProvider implements IPlanSearchProvider {

	@Override
	public String getFeatureName() {
		return PlanPackage.Literals.COMMON_MEMBER__DIFF_ID.getName();
	}

	@Override
	public String getDisplayName() {
		return "ID";
	}

	@Override
	public String getFacet(Object object) {
		if (object instanceof EPlanElement) {
			CommonMember member = ((EPlanElement) object).getMember(CommonMember.class);
			if (member != null) {
				return member.getDiffID();
			}
		}
		return null;
	}

}
