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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public class TestConditions extends AbstractResourceTest {
	
	private static final Date ACTIVITY_START = DateUtils.add(PLAN_START, ONE_HOUR);
	private static final Date INCON_DATE = PLAN_START;
	private static final Date INTERCON_DATE = DateUtils.add(PLAN_START, ONE_HOUR.times(4));

	private static final float NUMERIC_RESOURCE_00_CONDITION_INIT_VALUE = 100f;
	private static final float NUMERIC_RESOURCE_01_CONDITION_INIT_VALUE = 50f;
	private static final float SUMMARY_RESOURCE_CONDITION_INIT_VALUE = 150f;
	private static final String STATE_RESOURCE_CONDITION_INIT_VALUE = "XYZ";
	private static final String UNDEFINED_RESOURCE_CONDITION_INIT_VALUE = "50";
	private static final Amount<Dimensionless> UNDEFINED_RESOURCE_CONDITION_INIT_AMOUNT = Amount.valueOf(Integer.parseInt(UNDEFINED_RESOURCE_CONDITION_INIT_VALUE), Unit.ONE);

	private static final float NUMERIC_RESOURCE_00_CONDITION_INTER_VALUE = 200f;
	private static final String STATE_RESOURCE_CONDITION_INTER_VALUE = "OMG";
	private static final String UNDEFINED_RESOURCE_CONDITION_INTER_VALUE = "150";
	private static final Object UNDEFINED_RESOURCE_CONDITION_INTER_AMOUNT = Amount.valueOf(Integer.parseInt(UNDEFINED_RESOURCE_CONDITION_INTER_VALUE), Unit.ONE);
	
	private static final String NUMERIC_RESOURCE_00 = "NumericResource00";
	private static final String NUMERIC_RESOURCE_01 = "NumericResource01";
	private static final String SUMMARY_RESOURCE = "SummaryResource";
	private static final String STATE_RESOURCE = "StateResource";
	private static final String UNDEFINED_RESOURCE = "UndefinedResource";
	
	private static final MemberFactory CONDITIONS_FACTORY = MemberFactory.eINSTANCE;

	private EPlan plan;
	private EActivityDef activityDef;
	
	private Conditions initialConditions;
	private Conditions intermediateConditions;
	
	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestConditions.dictionary", true));
		activityDef = getActivityDef("Activity");
		//
		// Create the conditions
		initialConditions = createConditions(INCON_DATE);
		setNumericResource(initialConditions, NUMERIC_RESOURCE_00, NUMERIC_RESOURCE_00_CONDITION_INIT_VALUE);
		setNumericResource(initialConditions, NUMERIC_RESOURCE_01, NUMERIC_RESOURCE_01_CONDITION_INIT_VALUE);
		setSumaryesource(initialConditions, SUMMARY_RESOURCE, -100f); // should ignore this summary resource value
		setStateResource(initialConditions, STATE_RESOURCE_CONDITION_INIT_VALUE);
		setUndefinedResource(initialConditions, UNDEFINED_RESOURCE_CONDITION_INIT_VALUE);

		intermediateConditions = createConditions(INTERCON_DATE);
		setNumericResource(intermediateConditions, NUMERIC_RESOURCE_00, NUMERIC_RESOURCE_00_CONDITION_INTER_VALUE);
		setStateResource(intermediateConditions, STATE_RESOURCE_CONDITION_INTER_VALUE);
		setUndefinedResource(intermediateConditions, UNDEFINED_RESOURCE_CONDITION_INTER_VALUE);
		
		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		EPlanUtils.contributeProductResources(plan);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getMember(TemporalMember.class).setDuration(ONE_DAY);
				plan.getMember(ResourceConditionsMember.class).getConditions().add(initialConditions);
			}
		});
		super.setUp();
	}
	
	public void testEmptyPlanResources() {
		recomputePlan(plan);
		assertProfileValue(plan, NUMERIC_RESOURCE_00, INCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_00_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, NUMERIC_RESOURCE_01, INCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_01_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, SUMMARY_RESOURCE, INCON_DATE, Amount.valueOf(SUMMARY_RESOURCE_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, STATE_RESOURCE, INCON_DATE, STATE_RESOURCE_CONDITION_INIT_VALUE);
	}
	
	public void testEmptyPlanUndefinedResource() {
		addUndefinedProfile();
		recomputePlan(plan);
		assertProfileValue(plan, UNDEFINED_RESOURCE, INCON_DATE, UNDEFINED_RESOURCE_CONDITION_INIT_AMOUNT);
	}
	
	public void testEffectedPlanResources() {
		createSingleActivityPlan();
		recomputePlan(plan);
		assertProfileValue(plan, NUMERIC_RESOURCE_00, INCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_00_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, NUMERIC_RESOURCE_01, INCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_01_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, SUMMARY_RESOURCE, INCON_DATE, Amount.valueOf(SUMMARY_RESOURCE_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, NUMERIC_RESOURCE_00, ACTIVITY_START, Amount.valueOf(90f, Unit.ONE));
		assertProfileValue(plan, SUMMARY_RESOURCE, ACTIVITY_START, Amount.valueOf(140f, Unit.ONE));
		assertProfileValue(plan, STATE_RESOURCE, INCON_DATE, STATE_RESOURCE_CONDITION_INIT_VALUE);
		assertProfileValue(plan, STATE_RESOURCE, ACTIVITY_START, "ABC");
	}
	
	public void testEffectedPlanUndefinedResource() {
		createSingleActivityProfileEffectPlan();
		recomputePlan(plan);
		assertProfileValue(plan, UNDEFINED_RESOURCE, INCON_DATE, UNDEFINED_RESOURCE_CONDITION_INIT_AMOUNT);
		assertProfileValue(plan, UNDEFINED_RESOURCE, ACTIVITY_START, Amount.valueOf(40, Unit.ONE));
	}
	
	public void testIntermediateConditions() {
		createSingleActivityPlan();
		addintermediateConditions();
		recomputePlan(plan);
		assertProfileValue(plan, NUMERIC_RESOURCE_00, INCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_00_CONDITION_INIT_VALUE, Unit.ONE));
		assertProfileValue(plan, NUMERIC_RESOURCE_00, ACTIVITY_START, Amount.valueOf(90f, Unit.ONE));
		assertProfileValue(plan, NUMERIC_RESOURCE_00, INTERCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_00_CONDITION_INTER_VALUE, Unit.ONE));
		assertProfileValue(plan, SUMMARY_RESOURCE, INTERCON_DATE, Amount.valueOf(NUMERIC_RESOURCE_00_CONDITION_INTER_VALUE, Unit.ONE));
		assertProfileValue(plan, STATE_RESOURCE, INCON_DATE, STATE_RESOURCE_CONDITION_INIT_VALUE);
		assertProfileValue(plan, STATE_RESOURCE, ACTIVITY_START, "ABC");
		assertProfileValue(plan, STATE_RESOURCE, INTERCON_DATE, STATE_RESOURCE_CONDITION_INTER_VALUE);
	}

	public void testIntermediateUndefinedConditions() {
		createSingleActivityProfileEffectPlan();
		addintermediateConditions();
		recomputePlan(plan);
		assertProfileValue(plan, UNDEFINED_RESOURCE, INCON_DATE, UNDEFINED_RESOURCE_CONDITION_INIT_AMOUNT);
		assertProfileValue(plan, UNDEFINED_RESOURCE, ACTIVITY_START, Amount.valueOf(40, Unit.ONE));
		assertProfileValue(plan, UNDEFINED_RESOURCE, INTERCON_DATE, UNDEFINED_RESOURCE_CONDITION_INTER_AMOUNT);
	}

	private void createSingleActivityProfileEffectPlan() {
		final EActivity activity = createSingleActivityPlan();
		final ProfileEffect effect = ProfileFactory.eINSTANCE.createProfileEffect();
		effect.setProfileKey(UNDEFINED_RESOURCE);
		effect.setStartOffset(new TemporalOffset(Timepoint.START, DateUtils.ZERO_DURATION));
		effect.setEndOffset(new TemporalOffset(Timepoint.END, DateUtils.ZERO_DURATION));
		effect.setStartValueLiteral("-10");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				activity.getMember(ProfileMember.class).getEffects().add(effect);
			}
		});
		addUndefinedProfile();
	}

	private void addintermediateConditions() {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(ResourceConditionsMember.class).getConditions().add(intermediateConditions);
			}
		});
	}
	
	private void addUndefinedProfile() {
		final Profile profile = JScienceFactory.eINSTANCE.createProfile();
		profile.setId(UNDEFINED_RESOURCE);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles().add(profile);
			}
		});
	}
	
	private EActivity createSingleActivityPlan() {
		final EActivity activity = PLAN_FACTORY.createActivity(activityDef);
		activity.getMember(TemporalMember.class).setStartTime(ACTIVITY_START);
		activity.getMember(TemporalMember.class).setDuration(DateUtils.ZERO_DURATION);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
				// Set the plan start time 'back' to original start time since
				// it's bounds change as a result of the activity addition
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
			}
		});
		return activity;
	}
	
	private <T extends INamedDefinition> T assertDefinition(Class<T> klass, String name) {
		T definition = AD.getDefinition(klass, name);
		assertNotNull(definition);
		return definition;
	}

	private Conditions createConditions(Date date) {
		Conditions conditions = CONDITIONS_FACTORY.createConditions();
		conditions.setActive(true);
		conditions.setTime(date);
		return conditions;
	}

	//
	// Set up the numeric resource def
	private void setNumericResource(Conditions conditions, String resourceName, Float numericValue) {
		if (numericValue != null) {
			assertDefinition(ENumericResourceDef.class, resourceName); // assert
			NumericResource numericCondition = CONDITIONS_FACTORY.createNumericResource();
			numericCondition.setName(resourceName);
			numericCondition.setFloat(numericValue);
			conditions.getNumericResources().add(numericCondition);
		}
	}
	
	//
	// Set up the numeric resource def
	private void setSumaryesource(Conditions conditions, String resourceName, Float numericValue) {
		if (numericValue != null) {
			assertDefinition(ESummaryResourceDef.class, resourceName); // assert
			NumericResource numericCondition = CONDITIONS_FACTORY.createNumericResource();
			numericCondition.setName(resourceName);
			numericCondition.setFloat(numericValue);
			conditions.getNumericResources().add(numericCondition);
		}
	}
	
	private void setStateResource(Conditions conditions, String stateValue) {
		if (stateValue != null) {
			assertDefinition(EStateResourceDef.class, STATE_RESOURCE); // assert
			StateResource stateCondition = CONDITIONS_FACTORY.createStateResource();
			stateCondition.setName(STATE_RESOURCE);
			stateCondition.setState(stateValue);
			conditions.getStateResources().add(stateCondition);
		}
	}
	
	//
	// Set up undefined resource
	private void setUndefinedResource(Conditions conditions, String undefinedValue) {
		if (undefinedValue != null) {
			UndefinedResource undefinedResource = CONDITIONS_FACTORY.createUndefinedResource();
			undefinedResource.setName(UNDEFINED_RESOURCE);
			undefinedResource.setValueLiteral(undefinedValue);
			conditions.getUndefinedResources().add(undefinedResource);
		}
	}

}
