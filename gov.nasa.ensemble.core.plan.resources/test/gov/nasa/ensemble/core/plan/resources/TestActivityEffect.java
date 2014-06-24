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

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TestActivityEffect extends AbstractResourceTest {
	
	private static final Date OFFSET_TEST_ACTIVITY_START_TIME = DateUtils.add(PLAN_START, ONE_HOUR.times(2));
	private static final Date OFFSET_TEST_ACTIVITY_END_TIME = DateUtils.add(OFFSET_TEST_ACTIVITY_START_TIME, ONE_HOUR);
	
	public void testOffsetEffectsOfNominalActivity() {
		load(URI.createPlatformPluginURI("gov.nasa.ensemble.core.plan.resources/datafiles/test/TestActivityEffect__offsetEffect.dictionary", true));
		
		EActivityDef noOffestDef = getActivityDef("NoOffset");
		final EActivity noOffset = PLAN_FACTORY.createActivity(noOffestDef);
		final EPlan plan = createPlan(noOffset);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				noOffset.getMember(TemporalMember.class).setStartTime(OFFSET_TEST_ACTIVITY_START_TIME);
				noOffset.getMember(TemporalMember.class).setDuration(ONE_HOUR);
			}
		});
		recomputePlan(plan);
		
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Profile<?> profile = ResourceUtils.getProfile(plan, "Control");
				assertNotNull(profile);
				assertEquals(null, profile.getValue(DateUtils.subtract(OFFSET_TEST_ACTIVITY_START_TIME, ONE_HOUR)));
				assertEquals(Boolean.TRUE, profile.getValue(OFFSET_TEST_ACTIVITY_START_TIME));
				assertEquals(Boolean.FALSE, profile.getValue(OFFSET_TEST_ACTIVITY_END_TIME));
				assertEquals(Boolean.FALSE, profile.getValue(DateUtils.add(OFFSET_TEST_ACTIVITY_END_TIME, ONE_HOUR)));
			}
		});
	}
	
	public void testOffsetEffectsOfOffsetActivity() {
		load(URI.createPlatformPluginURI("gov.nasa.ensemble.core.plan.resources/datafiles/test/TestActivityEffect__offsetEffect.dictionary", true));
			
		EActivityDef oneHourOffestDef = getActivityDef("OneHourOffset");
		final EActivity oneHourOffest = PLAN_FACTORY.createActivity(oneHourOffestDef);
		final EPlan plan = createPlan(oneHourOffest);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				oneHourOffest.getMember(TemporalMember.class).setStartTime(DateUtils.add(PLAN_START, ONE_HOUR.times(2)));
				oneHourOffest.getMember(TemporalMember.class).setDuration(ONE_HOUR);
			}
		});
		recomputePlan(plan);
		
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Profile<?> profile = ResourceUtils.getProfile(plan, "Control");
				assertNotNull(profile);
				assertEquals(null, profile.getValue(DateUtils.subtract(OFFSET_TEST_ACTIVITY_START_TIME, ONE_HOUR.times(2))));
				assertEquals(Boolean.TRUE, profile.getValue(DateUtils.subtract(OFFSET_TEST_ACTIVITY_START_TIME, ONE_HOUR)));
				assertEquals(Boolean.TRUE, profile.getValue(OFFSET_TEST_ACTIVITY_START_TIME));
				assertEquals(Boolean.TRUE, profile.getValue(OFFSET_TEST_ACTIVITY_END_TIME));
				assertEquals(Boolean.FALSE, profile.getValue(DateUtils.add(OFFSET_TEST_ACTIVITY_END_TIME, ONE_HOUR)));
			}
		});
	}

	public void testStateResourceEffectFromParameter() throws Exception {
		load(URI.createURI("platform:/plugin/gov.nasa.ensemble.core.plan.resources/datafiles/test/SPF_4545.dictionary"));
		
		final EEnum issControlModeEnum = (EEnum) AD.getEClassifier("ISS_CONTROL_MODE");
		assertNotNull(issControlModeEnum);
		final EEnumLiteral ustEnumLiteral = issControlModeEnum.getEEnumLiteralByLiteral("UST");
		
		EActivityDef handoverDef = getActivityDef("Handover");
		EStructuralFeature toControlFeature = handoverDef.getEStructuralFeature("toControl");
		assertNotNull(toControlFeature);
		assertTrue(toControlFeature instanceof EAttribute);
		
		final EActivity handover = PLAN_FACTORY.createActivity(handoverDef);
		handover.setName("handover");
		handover.getData().eSet(toControlFeature, ustEnumLiteral);
		
		final EPlan plan = createPlan(handover);

		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				handover.getMember(TemporalMember.class).setStartTime(PLAN_START);
				handover.getMember(TemporalMember.class).setDuration(ONE_HOUR);
			}
		});

		recomputePlan(plan);
		
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				Profile<?> profile = ResourceUtils.getProfile(plan, "Control");
				assertNotNull(profile);
				assertEquals(ustEnumLiteral, profile.getValue(DateUtils.add(PLAN_START, ONE_HOUR)));
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void debugProfiles(EPlan plan) {
		for (Profile<?> p : WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles()) {
			ProfileUtil.debugProfile(p);
		}
	}

}
