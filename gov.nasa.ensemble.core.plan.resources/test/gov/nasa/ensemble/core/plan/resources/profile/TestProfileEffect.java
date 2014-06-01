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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.resources.AbstractResourceTest;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.jscience.physics.amount.Amount;

public class TestProfileEffect extends AbstractResourceTest {
	
	private static final String KEY_TEST_PROFILE = "TEST_PROFILE";
	
	private static final Date ACTIVITY_START = DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR));
	private static final Date ACTIVITY_END = DateUtils.add(ACTIVITY_START, Amount.valueOf(1, NonSI.HOUR));
	
	private EPlan plan;
	private EActivity activity;
	private Profile profile;
	
	@Override
	protected void setUp() throws Exception {
		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		activity = PLAN_FACTORY.createActivityInstance();
		activity.setName("TEST_ACTIVITY");
		activity.getMember(TemporalMember.class).setStartTime(ACTIVITY_START);
		activity.getMember(TemporalMember.class).setDuration(DateUtils.subtract(ACTIVITY_END, ACTIVITY_START));
		EPlanUtils.contributeProductResources(plan);
		final ResourceProfileMember resourceProfileMember = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		assertNotNull(resourceProfileMember);
		profile = JScienceFactory.eINSTANCE.createProfile();
		profile.setId(KEY_TEST_PROFILE);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(ACTIVITY_START);
				plan.getMember(TemporalMember.class).setDuration(Amount.valueOf(1, NonSI.DAY));
				plan.getChildren().add(activity);
				resourceProfileMember.getResourceProfiles().add(profile);
			}
		});
	}
	
	public void testSimpleNumericProfileEffect() throws Exception {
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(DateUtils.ZERO_DURATION, "10", DateUtils.ZERO_DURATION, "-10");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		recomputePlan(plan);
		
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_START, Amount.valueOf(10, Unit.ONE));
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_END  , Amount.valueOf( 0, Unit.ONE));
	}
	
	public void testDelayedNumericProfileEffect() throws Exception {
		Amount<Duration> startOffset = Amount.valueOf(30, NonSI.MINUTE);
		
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(startOffset, "10", DateUtils.ZERO_DURATION, "-10");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		recomputePlan(plan);
		
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_START, null);
		assertProfileValue(plan, KEY_TEST_PROFILE, DateUtils.add(ACTIVITY_START, startOffset), Amount.valueOf(10, Unit.ONE));
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_END  , Amount.valueOf( 0, Unit.ONE));
	}
	
	public void testDelayedStateProfileEffect() throws Exception {
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(DateUtils.ZERO_DURATION, "\"XYZ\"", DateUtils.ZERO_DURATION, "\"ABC\"");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		recomputePlan(plan);

		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_START, "XYZ");
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_END  , "ABC");
	}
	
	public void testSingleNumericProfileEffect() throws Exception {
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(DateUtils.ZERO_DURATION, "10", DateUtils.ZERO_DURATION, ProfileEffect.NO_EFFECT);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		recomputePlan(plan);
		
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_START, Amount.valueOf(10, Unit.ONE));
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_END  , Amount.valueOf(10, Unit.ONE));
	}
	
	public void testNullValueStateProfileEffect() throws Exception {
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(DateUtils.ZERO_DURATION, "\"XYZ\"", DateUtils.ZERO_DURATION, ProfileEffect.NULL_VALUE);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		recomputePlan(plan);

		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_START, "XYZ");
		assertProfileValue(plan, KEY_TEST_PROFILE, ACTIVITY_END  , null);
	}

	public void testIO() throws IOException {
		final ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertNotNull(profileMember);
		assertNotNull(ResourceUtils.getProfile(plan, KEY_TEST_PROFILE));
		final ProfileEffect effect = createProfileEffect(DateUtils.ZERO_DURATION, "\"XYZ\"", DateUtils.ZERO_DURATION, "\"ABC\"");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				profileMember.getEffects().add(effect);
			}
		});
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Resource outResource = new PlanResourceImpl(createTempFileURI());
		outResource.getContents().add(plan);
		outResource.save(baos, null);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		Resource inResource = new PlanResourceImpl(createTempFileURI());
		inResource.load(bais, null);
		assertEquals(1, inResource.getContents().size());
		assertTrue(inResource.getContents().get(0) instanceof EPlan);
		EPlan plan = (EPlan) inResource.getContents().get(0);
		assertEquals(1, plan.getChildren().size());
		assertTrue(plan.getChildren().get(0) instanceof EActivity);
		EActivity activity = (EActivity) plan.getChildren().get(0);
		ProfileMember member = activity.getMember(ProfileMember.class);
		assertNotNull(member);
		assertEquals(1, member.getEffects().size());
		ProfileEffect inEffect = member.getEffects().get(0);
		assertEquals(KEY_TEST_PROFILE, inEffect.getProfileKey());
		assertEquals(Timepoint.START, inEffect.getStartOffset().getTimepoint());
		assertTrue(DateUtils.ZERO_DURATION.approximates(inEffect.getStartOffset().getOffset()));
		assertEquals("\"XYZ\"", inEffect.getStartValueLiteral());
		assertEquals(Timepoint.END, inEffect.getEndOffset().getTimepoint());
		assertEquals(DateUtils.ZERO_DURATION, inEffect.getEndOffset().getOffset());
		assertEquals("\"ABC\"", inEffect.getEndValueLiteral());
	}

	private URI createTempFileURI() throws IOException {
		File file = File.createTempFile("plan", "plan");
		file.deleteOnExit();
		return URI.createURI(file.toURI().toString());
	}
	
	protected ProfileEffect createProfileEffect(Amount<Duration> startOffset, String startValueLiteral, Amount<Duration> endOffset, String endValueLiteral) {
		ProfileEffect effect = ProfileFactory.eINSTANCE.createProfileEffect();
		effect.setProfileKey(KEY_TEST_PROFILE);
		effect.setStartValueLiteral(startValueLiteral);
		effect.setStartOffset(new TemporalOffset(Timepoint.START, startOffset));
		effect.setEndValueLiteral(endValueLiteral);
		effect.setEndOffset(new TemporalOffset(Timepoint.END, endOffset));
		return effect;
	}
	
}
