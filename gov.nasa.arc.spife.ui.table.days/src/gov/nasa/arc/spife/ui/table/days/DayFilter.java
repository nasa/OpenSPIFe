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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerFilter;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.Viewer;

public class DayFilter extends TreeTableViewerFilter {

	private Day day;
	
	@Override
	public boolean select(Viewer v, Object parent, Object child) {
		if (day == null) {
			// Show nothing until a day is set,
			// to avoid creating Items for everything in the plan
			// when the viewer is first created.
			return false;
		}
		if (child instanceof EPlanElement) {
			EPlanElement element = (EPlanElement)child;
			return day.has(element);
		}
		if (child instanceof EObject) {
			return MergeTreePlanContributor.getInstance().intersectsExtent((EObject) child, day.getExtent());
		}
		return true;
		
	}

	public void setDay(Day day) {
		this.day = day;
	}

	@Override
	public boolean isFilterProperty(Object feature) {
		return (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME);
	}

}
