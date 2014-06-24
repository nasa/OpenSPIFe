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
package gov.nasa.arc.spife.rcp.table;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.ParameterStringifierUtils;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class SPIFeTablePlanContributor extends MergeTreePlanContributor {

	private static final String INCON_PREFIX = "Incon ";
	private static final IStringifier DATE_STRINGIFIER = ParameterStringifierUtils.getStringifier(TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);

	@Override
	public List<Object> getContributions(EPlan plan) {
		return new ArrayList(getIncons(plan));
	}
	
	private static List<Conditions> getIncons(EPlan plan) {
		return plan.getMember(ResourceConditionsMember.class).getConditions();
	}
	
	@Override
	public Object getValueForFeature(EObject object, EStructuralFeature feature) {
		if (object instanceof Conditions) {
			Conditions condition = (Conditions) object;
			if (feature == PlanPackage.Literals.EPLAN_ELEMENT__NAME) {
				return INCON_PREFIX + DATE_STRINGIFIER.getDisplayString(condition.getTime());
			} else if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME) {
				return condition.getTime();
			}
		}
		return super.getValueForFeature(object, feature);
	}
	
	@Override
	public boolean intersectsExtent(EObject object, TemporalExtent extent) {
		if (object instanceof Conditions) {
			Conditions condition = (Conditions) object;
			Date time = condition.getTime();
			if (time.before(extent.getStart())) {
				return false;
			}
			if (time.after(extent.getEnd())) {
				return false;
			}
			return true;
		}
		return super.intersectsExtent(object, extent);
	}
	
}
