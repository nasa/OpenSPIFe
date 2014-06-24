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
package gov.nasa.ensemble.core.plan.temporal.columns;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterFacet;

import java.util.Comparator;

/**
 * Compare first by text, ties broken by checking actual value (if they are strings),
 * ties broken by using plan element ordering 
 * 
 * @author Andrew
 */
public class BooleanParameterComparator implements Comparator<ParameterFacet<Boolean>> {
	
	public static final BooleanParameterComparator INSTANCE = new BooleanParameterComparator();
	
	@Override
	public int compare(ParameterFacet<Boolean> facet1, ParameterFacet<Boolean> facet2) {
		if (facet1 == null && facet2 == null) {
			return 0;
		}
		if (facet1 == null && facet2 != null) {
			return -1;
		}
		if (facet1 != null && facet2 == null) {
			return 1;
		}
		if (facet1 != null && facet2 != null) {
			Boolean o1 = facet1.getValue();
			Boolean o2 = facet2.getValue();
			if ((o1 == null) && (o2 != null)) {
				return -1;
			} else if ((o1 != null) && (o2 == null)) {
				return 1;
			} else if (o1 != null) {
				return o1.compareTo(o2);
			}
			EPlanElement pe1 = facet1.getElement();
			EPlanElement pe2 = facet2.getElement();
			return PlanUtils.INHERENT_ORDER.compare(pe1, pe2);
		}
		return 0;
	}
}
