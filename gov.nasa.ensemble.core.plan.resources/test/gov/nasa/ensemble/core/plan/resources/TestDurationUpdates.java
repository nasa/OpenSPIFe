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

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public class TestDurationUpdates extends AbstractResourceUpdaterTest {

	private static final String BOOLEAN_RESOURCE = "BooleanResource";
	private static final Amount<Duration> FIVE_HOURS = Amount.valueOf(5, NonSI.HOUR);
	private EStateResourceDef booleanResourceDef;

	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestDuration.dictionary", true));
		super.setUp();
	
		final Conditions conditions = MemberFactory.eINSTANCE.createConditions();
		conditions.setActive(true);
		conditions.setTime(PLAN_START);
		StateResource stateCondition = MemberFactory.eINSTANCE.createStateResource();
		stateCondition.setName(BOOLEAN_RESOURCE);
		stateCondition.setState("false");
		conditions.getStateResources().add(stateCondition);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(ResourceConditionsMember.class).getConditions().add(conditions);
			}
		});
		
		booleanResourceDef = AD.getDefinition(EStateResourceDef.class, BOOLEAN_RESOURCE);
	}

	public void testDerivedDurationUpdates() throws InterruptedException {
		final EActivityDef activityDef = getActivityDef("DerivedDurationActivity");
		final EActivity activity = PLAN_FACTORY.createActivity(activityDef);
		final Date activityStart = DateUtils.add(PLAN_START, ONE_HOUR);
		activity.getMember(TemporalMember.class).setStartTime(activityStart);
		executeTest(new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertAmountProximity(ONE_HOUR, activity.getMember(TemporalMember.class).getDuration());
				assertProfileValue(plan, booleanResourceDef, activityStart, false);
				assertProfileValue(plan, booleanResourceDef, DateUtils.add(activityStart, ONE_HOUR), true);
			}
		});
		
		executeTest(new Runnable() {
			@Override
			public void run() {
				activity.getData().eSet(activityDef.getEStructuralFeature("explicit_duration"), FIVE_HOURS);
			}
		},  new Runnable() {
			@Override
			public void run() {
				assertAmountProximity(FIVE_HOURS, activity.getMember(TemporalMember.class).getDuration());
				assertProfileValue(plan, booleanResourceDef, activityStart, false);
				assertProfileValue(plan, booleanResourceDef, DateUtils.add(activityStart, FIVE_HOURS), true);
			}
		});
		assertActivityRemovalEffects(activity);
	}

	public void testOverrideDurationUpdates() throws InterruptedException {
		final EActivityDef activityDef = getActivityDef("OverrideDurationActivity");
		final EActivity activity = PLAN_FACTORY.createActivity(activityDef);
		final Date activityStart = DateUtils.add(PLAN_START, ONE_HOUR);
		activity.getMember(TemporalMember.class).setStartTime(activityStart);
		executeTest(new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertAmountProximity(ONE_HOUR, (Amount) eGet(activity.getData(), "derived_duration"));
				assertAmountProximity(ONE_HOUR, activity.getMember(TemporalMember.class).getDuration());
				assertProfileValue(plan, booleanResourceDef, activityStart, false);
				assertProfileValue(plan, booleanResourceDef, DateUtils.add(activityStart, ONE_HOUR), true);
			}
		});
		
		executeTest(new Runnable() {
			@Override
			public void run() {
				eSet(activity.getData(), "override_duration", FIVE_HOURS);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertAmountProximity(FIVE_HOURS, (Amount) eGet(activity.getData(), "derived_duration"));
				assertAmountProximity(FIVE_HOURS, activity.getMember(TemporalMember.class).getDuration());
				assertProfileValue(plan, booleanResourceDef, activityStart, false);
				assertProfileValue(plan, booleanResourceDef, DateUtils.add(activityStart, FIVE_HOURS), true);
			}
		});
		assertActivityRemovalEffects(activity);
	}

	private void assertActivityRemovalEffects(final EActivity activity) throws InterruptedException {
		executeTest(new Runnable() {
			@Override
			public void run() {
				plan.getChildren().remove(activity);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertProfileValue(plan, booleanResourceDef, activity.getMember(TemporalMember.class).getStartTime(), false);
				assertProfileValue(plan, booleanResourceDef, activity.getMember(TemporalMember.class).getEndTime(), false);
			}
		});
	}
	
}
