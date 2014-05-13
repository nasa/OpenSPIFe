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
package gov.nasa.ensemble.core.plan.resources.ui.profile.table;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

public class ProfileColumn extends AbstractMergeColumn {

	private final Profile profile;
	
	public ProfileColumn(PlanProfileColumnProvider provider, Profile profile) {
		super(provider, profile.getName(), 50);
		this.profile = profile;
	}

	@Override
	public boolean needsUpdate(Object feature) {
		return true;
	}

	@Override
	public Object getFacet(Object element) {
		if (element instanceof EObject) {
			Date date = getDate((EObject) element);
			if (date != null) {
				DataPoint dataPoint = profile.getDataPoint(date);
				if (dataPoint != null) {
					return dataPoint.getValue();
				}
			}
		}
		return null;
	}
	
	private static Date getDate(EObject object) {
		if (object instanceof EPlanElement) {
			return ((EPlanElement) object).getMember(TemporalMember.class).getEndTime();
		} else {
			Object value = MergeTreePlanContributor.getInstance().getValueForFeature(object, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
			if (value instanceof Date) {
				return (Date) value;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object facet) {
		if (facet == null) {
			return "";
		}
		IStringifier stringifier = StringifierRegistry.getStringifier(facet.getClass());
		if (stringifier != null) {
			return stringifier.getDisplayString(facet);
		}
		return facet.toString();
	}
	
	@Override
	public boolean updateAll() {
		return true;
	}

}
