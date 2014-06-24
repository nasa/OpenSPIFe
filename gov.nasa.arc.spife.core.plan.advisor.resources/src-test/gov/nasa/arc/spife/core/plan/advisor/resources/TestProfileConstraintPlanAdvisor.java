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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import javax.measure.unit.NonSI;

import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public class TestProfileConstraintPlanAdvisor extends AbstractResourcePlanAdvisorTest {
	
	private static final URI URI_TEST_AD = URI.createPlatformPluginURI("/gov.nasa.arc.spife.core.plan.advisor.resources/data-test/TestProfileConstraintPlanAdvisor.dictionary", true);
	
	private static final String ACTIVITY_DEF_VIOLATES_DERIVED_RESOURCE 	= "ViolatesDerivedResource";
	private static final String ACTIVITY_DEF_GENERATES_RESOURCE 		= "GeneratesResource";
	
	private static final String INTEGER_RESOURCE 						= "Integer_Resource";
	private static final String ON_OFF_RESOUCE 							= "ON_OFF_Resouce";
	
	private EActivity generatesResource;
	private PlanEditorModel model;

	@Override
	protected void setUp() throws Exception {
		load(URI_TEST_AD);
		generatesResource = createActivity(ACTIVITY_DEF_GENERATES_RESOURCE);
		model = createPlanEditorModel();
	}
	
	/**
	 * Test that the profile key being null fails
	 */
	public void testProfileConstraintNullProfileKeyFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, null, null, "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}

	/**
	 * Test that the profile key does not reference and actual profile fails
	 */
	public void testProfileConstraintMissingProfileFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, "SomeMissingProfile", null, "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	/**
	 * Create a constraint that should fail if not for the max gap attribute
	 */
	public void testDerivedProfileConstraintFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = createActivity(ACTIVITY_DEF_VIOLATES_DERIVED_RESOURCE);
				activity.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getChildren().add(activity);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testDerivedProfileConstraintPasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				generatesResource.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getChildren().add(generatesResource);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 0);
	}
	
	public void testProfileEqualityConstraintPasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				addEqualityConstraint(addActivity(plan), ON_OFF_RESOUCE, "ON");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 0);
	}
	
	public void testProfileEqualityConstraintFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEqualityConstraint(activity, ON_OFF_RESOUCE, "OFF");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEqualityConstraintFailsOnNullValueForOn() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEqualityConstraint(activity, ON_OFF_RESOUCE, "OFF");
				
				activity.getMember(TemporalMember.class).setStartTime(DateUtils.subtract(PLAN_START, Amount.valueOf(4, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEqualityConstraintFailsOnNullValueForOff() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEqualityConstraint(activity, ON_OFF_RESOUCE, "OFF");
				
				activity.getMember(TemporalMember.class).setStartTime(DateUtils.subtract(PLAN_START, Amount.valueOf(4, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	/** 
	 * Create a constraint that should fail if not for the max gap attribute
	 */
	public void testProfileEqualityConstraintPassesMaximumGap() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				//
				// Two generates resource activities spread apart
				EActivity a1 = createActivity(ACTIVITY_DEF_GENERATES_RESOURCE);
				a1.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getChildren().add(a1);
				EActivity a2 = createActivity(ACTIVITY_DEF_GENERATES_RESOURCE);
				a2.getMember(TemporalMember.class).setStartTime(DateUtils.add(PLAN_START, Amount.valueOf(2, NonSI.HOUR)));
				plan.getChildren().add(a2);
				//
				// Add a constraint on the plan
				ProfileEqualityConstraint constraint = addEqualityConstraint(plan, ON_OFF_RESOUCE, "ON");
				constraint.setMaximumGap(Amount.valueOf(2, NonSI.HOUR));
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 0);
	}
	
	/**
	 * Constraint fails despite specification of max gap
	 */
	public void testProfileEqualityConstraintFailsMaximumGap() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				//
				// Two generates resource activities spread apart
				EActivity a1 = createActivity(ACTIVITY_DEF_GENERATES_RESOURCE);
				a1.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getChildren().add(a1);
				EActivity a2 = createActivity(ACTIVITY_DEF_GENERATES_RESOURCE);
				a2.getMember(TemporalMember.class).setStartTime(DateUtils.add(PLAN_START, Amount.valueOf(2, NonSI.HOUR)));
				plan.getChildren().add(a2);
				//
				// Add a constraint on the plan
				ProfileEqualityConstraint constraint = addEqualityConstraint(plan, ON_OFF_RESOUCE, "ON");
				constraint.setMaximumGap(Amount.valueOf(10, NonSI.MINUTE));
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEqualityConstraintWithNullValueFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEqualityConstraint(activity, INTEGER_RESOURCE, null);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEqualityConstraintUnparsableFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEqualityConstraint(activity, INTEGER_RESOURCE, "99A");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEnvelopeConstraintFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, INTEGER_RESOURCE, "0", "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEnvelopeConstraintPassesMinOnly() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, INTEGER_RESOURCE, "0", null);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 0);
	}
	
	public void testProfileEnvelopeConstraintFailsMaxOnly() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, INTEGER_RESOURCE, null, "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEnvelopeConstraintWithNullMinAndMaxFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, INTEGER_RESOURCE, null, null);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEnvelopeConstraintUnparsableFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEnvelopeConstraint(activity, "SomeMissingProfile", "0A", "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEffectConstraintWithNullStartAndEndFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEffect(activity, INTEGER_RESOURCE, null, null);
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	public void testProfileEffectConstraintUnparsableFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivity activity = addActivity(plan);
				addEffect(activity, ON_OFF_RESOUCE, "0A", "99");
			}
		});
		assertViolationCount(plan, new ProfileConstraintPlanAdvisorFactory(), 1);
	}
	
	@Override
	protected PlanEditorModel createPlanEditorModel() {
		PlanEditorModel model = super.createPlanEditorModel();
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
			}
		});
		return model;
	}

	private EActivity addActivity(final EPlan plan) {
		generatesResource.getMember(TemporalMember.class).setStartTime(PLAN_START);
		plan.getChildren().add(generatesResource);
		
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		activity.getMember(TemporalMember.class).setStartTime(PLAN_START);
		activity.getMember(TemporalMember.class).setDuration(generatesResource.getMember(TemporalMember.class).getDuration());
		ProfileMember member = activity.getMember(ProfileMember.class);
		assertNotNull(member);
		plan.getChildren().add(activity);
		return activity;
	}

	private ProfileEffect addEffect(EActivity activity, String profileKey, String startLiteral, String endLiteral) {
		ProfileMember member = activity.getMember(ProfileMember.class);
		assertNotNull(member);
		ProfileEffect effect = ProfileFactory.eINSTANCE.createProfileEffect();
		effect.setProfileKey(profileKey);
		effect.setStartValueLiteral(startLiteral);
		effect.setEndValueLiteral(endLiteral);
		member.getEffects().add(effect);
		return effect;
	}

	private ProfileEnvelopeConstraint addEnvelopeConstraint(EActivity activity, String profileKey, String minLiteral, String maxLiteral) {
		ProfileMember member = activity.getMember(ProfileMember.class);
		assertNotNull(member);
		ProfileEnvelopeConstraint constraint = ProfileFactory.eINSTANCE.createProfileEnvelopeConstraint();
		constraint.setProfileKey(profileKey);
		constraint.setMinLiteral(minLiteral);
		constraint.setMaxLiteral(maxLiteral);
		member.getConstraints().add(constraint);
		return constraint;
	}

	private ProfileEqualityConstraint addEqualityConstraint(EPlanElement pe, String resourceName, String valueLiteral) {
		ProfileMember member = pe.getMember(ProfileMember.class);
		assertNotNull(member);
		ProfileEqualityConstraint constraint = ProfileFactory.eINSTANCE.createProfileEqualityConstraint();
		constraint.setProfileKey(resourceName);
		constraint.setValueLiteral(valueLiteral);
		member.getConstraints().add(constraint);
		return constraint;
	}
	
}
