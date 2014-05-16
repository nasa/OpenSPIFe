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
package gov.nasa.ensemble.core.plan.resources.ui.wizard;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.ConditionsProvider;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jscience.physics.amount.Amount;

public class ResourceProfileConditionsProvider extends ConditionsProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Conditions getConditions(EPlan plan, Date date) throws Exception {
		MemberFactory factory = MemberFactory.eINSTANCE;
		Conditions conditions = factory.createConditions();
		Map<String, DataPoint> conditionValues = new HashMap<String, DataPoint>();
		for (Profile profile : WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles()) {
			conditionValues.put(profile.getId(), profile.getDataPoint(date));
		}
		ActivityDictionary AD = ActivityDictionary.getInstance();
		for (ENumericResourceDef nrd : AD.getDefinitions(ENumericResourceDef.class)) {
			try {
				DataPoint dataPoint = conditionValues.get(nrd.getName());
				Number value = (dataPoint != null ? value = dataPoint.getNumericValue() : null);
				NumericResource numericResource = factory.createNumericResource();
				numericResource.setName(nrd.getName());
				if (value != null) {
					numericResource.setFloat(value.floatValue());
				}
				conditions.getNumericResources().add(numericResource);
			} catch (Exception e) {
				LogUtil.error("exporting resource "+nrd.getName(), e);
			}
		}
		for (EStateResourceDef srd : AD.getDefinitions(EStateResourceDef.class)) {
			DataPoint dataPoint = conditionValues.get(srd.getName());
			Object value = (dataPoint != null ? value = dataPoint.getValue() : null);
			StateResource stateResource = factory.createStateResource();
			stateResource.setName(srd.getName());
			if (value != null) {
				stateResource.setState(value.toString());
			}
			conditions.getStateResources().add(stateResource);
		}
		for (EClaimableResourceDef crd : AD.getDefinitions(EClaimableResourceDef.class)) {
			DataPoint dataPoint = conditionValues.get(crd.getName());
			Object value = (dataPoint != null ? value = dataPoint.getValue() : null);
			Claim claim = factory.createClaim();
			claim.setName(crd.getName());
			if (value instanceof Boolean) {
				claim.setUsed((Boolean) value);
			} else if (value instanceof Amount) {
				Amount amount = (Amount) value;
				Amount ONE = Amount.valueOf(1, amount.getUnit());
				claim.setUsed(amount.isGreaterThan(ONE) || amount.approximates(ONE));
			}
			conditions.getClaims().add(claim);
		}
		return conditions;
	}
	
}
