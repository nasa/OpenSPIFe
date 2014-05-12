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

import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.tests.core.plan.AbstractTestPlanIORoundTrip;

import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class TestProfileReferenceIO extends AbstractTestPlanIORoundTrip {

	protected static final ProfileEffect PROFILE_EFFECT = ProfileFactory.eINSTANCE.createProfileEffect();
	
	protected static final ProfileEqualityConstraint PROFILE_EQUALITY_CONSTRAINT = ProfileFactory.eINSTANCE.createProfileEqualityConstraint();
	protected static final ProfileEnvelopeConstraint PROFILE_ENVELOPE_CONSTRAINT = ProfileFactory.eINSTANCE.createProfileEnvelopeConstraint();
	
	private static final TemporalOffset START_OFFSET = new TemporalOffset(Timepoint.START, Amount.valueOf(5, SI.SECOND));
	private static final TemporalOffset END_OFFSET = new TemporalOffset(Timepoint.END, Amount.valueOf(-5, SI.SECOND));
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PROFILE_EFFECT.setProfileKey("EFFECT_KEY");
		PROFILE_EFFECT.setStartValueLiteral("true");
		PROFILE_EFFECT.setStartOffset(START_OFFSET);
		PROFILE_EFFECT.setEndValueLiteral("false");
		PROFILE_EFFECT.setEndOffset(END_OFFSET);
		
		PROFILE_EQUALITY_CONSTRAINT.setProfileKey("EFFECT_KEY");
		PROFILE_EQUALITY_CONSTRAINT.setValueLiteral("true");
		PROFILE_EQUALITY_CONSTRAINT.setStartOffset(START_OFFSET);
		PROFILE_EQUALITY_CONSTRAINT.setEndOffset(END_OFFSET);

		PROFILE_ENVELOPE_CONSTRAINT.setProfileKey("EFFECT_KEY");
		PROFILE_ENVELOPE_CONSTRAINT.setMinLiteral("1.0");
		PROFILE_ENVELOPE_CONSTRAINT.setMaxLiteral("2.0");
		PROFILE_ENVELOPE_CONSTRAINT.setStartOffset(START_OFFSET);
		PROFILE_ENVELOPE_CONSTRAINT.setEndOffset(END_OFFSET);
	}

	public void testPlanIO() throws Exception {
		EPlan planOut = null;
		EPlan planIn = null;
		try {
			planOut = createTestPlan();
			planIn = performRoundTrip(planOut);
			assertPlanEquality(planOut, planIn);
		} finally {
			WrapperUtils.dispose(planOut);
			WrapperUtils.dispose(planIn);
		}
	}
	
	private void assertPlanEquality(EPlan planOut, EPlan planIn) {
		EPlanChild activity = planIn.getChildren().get(0);
		ProfileMember profileMember = activity.getMember(ProfileMember.class);
		assertProfileEffectEquality(PROFILE_EFFECT, profileMember.getEffects().get(0));
		assertProfileEqualityConstraintEquality(PROFILE_EQUALITY_CONSTRAINT, (ProfileEqualityConstraint) profileMember.getConstraints().get(0));
		assertProfileEnvelopeConstraintEquality(PROFILE_ENVELOPE_CONSTRAINT, (ProfileEnvelopeConstraint) profileMember.getConstraints().get(1));
	}
	
	protected void assertProfileEffectEquality(ProfileEffect expected, ProfileEffect actual) {
		assertEquals(expected.getProfileKey(), actual.getProfileKey());
		assertTemporalOffsetEquality(expected.getStartOffset(), actual.getStartOffset());
		assertTemporalOffsetEquality(expected.getEndOffset(), actual.getEndOffset());
		assertEquals(expected.getStartValueLiteral(), actual.getStartValueLiteral());
		assertEquals(expected.getEndValueLiteral(), actual.getEndValueLiteral());
	}
	
	protected void assertProfileEqualityConstraintEquality(ProfileEqualityConstraint expected, ProfileEqualityConstraint actual) {
		assertEquals(expected.getProfileKey(), actual.getProfileKey());
		assertEquals(expected.getValueLiteral(), actual.getValueLiteral());
		assertTemporalOffsetEquality(expected.getStartOffset(), actual.getStartOffset());
		assertTemporalOffsetEquality(expected.getEndOffset(), actual.getEndOffset());
	}
	
	protected void assertProfileEnvelopeConstraintEquality(ProfileEnvelopeConstraint expected, ProfileEnvelopeConstraint actual) {
		assertEquals(expected.getProfileKey(), actual.getProfileKey());
		assertEquals(expected.getMinLiteral(), actual.getMinLiteral());
		assertEquals(expected.getMaxLiteral(), actual.getMaxLiteral());
		assertTemporalOffsetEquality(expected.getStartOffset(), actual.getStartOffset());
		assertTemporalOffsetEquality(expected.getEndOffset(), actual.getEndOffset());
	}		
	
	protected EPlan createTestPlan() {
		final EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		activity.getMember(ProfileMember.class).getEffects().add(EMFUtils.copy(PROFILE_EFFECT));
		activity.getMember(ProfileMember.class).getConstraints().add(EMFUtils.copy(PROFILE_EQUALITY_CONSTRAINT));
		activity.getMember(ProfileMember.class).getConstraints().add(EMFUtils.copy(PROFILE_ENVELOPE_CONSTRAINT));
		plan.getChildren().add(activity);
		
		return plan;
	}
	
	private void assertTemporalOffsetEquality(TemporalOffset expected, TemporalOffset actual) {
		assertEquals(expected.getTimepoint(), actual.getTimepoint());
		assertTrue("expected "+expected.getOffset()+" but was "+actual.getOffset(), expected.getOffset().approximates(actual.getOffset()));
	}
	
}
