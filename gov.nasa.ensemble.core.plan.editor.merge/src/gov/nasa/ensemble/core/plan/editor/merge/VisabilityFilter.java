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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerFilter;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.jface.viewers.Viewer;

public class VisabilityFilter extends TreeTableViewerFilter {

	@Override
	public boolean isFilterProperty(Object feature) {
		return (feature == PlanPackage.Literals.COMMON_MEMBER__VISIBLE);
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement planElement = (EPlanElement) element;
			return planElement.getMember(CommonMember.class).isVisible();
		}
		return true;
	}

}
