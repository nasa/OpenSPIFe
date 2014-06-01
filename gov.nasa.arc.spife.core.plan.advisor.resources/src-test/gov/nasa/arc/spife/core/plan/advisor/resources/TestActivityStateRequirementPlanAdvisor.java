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
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.unit.NonSI;

import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public class TestActivityStateRequirementPlanAdvisor extends AbstractResourcePlanAdvisorTest {

	private EActivity requires90PercentCoverage;
	private EActivity notGeneratesCoverage;
	private EActivity generatesCoverage;
	private EActivity requiresBeforeAndGenerate;
	private EActivity turnOnResource;
	private EActivity turnOffResource;
	private EActivity requiresThroughoutAndGenerate;
	private EActivity disallowsThroughout;
	private EActivity allowsMultipleThroughout;
	private EActivity generatesOffsetCoverage;
	private EActivity requiresOffsetCoverage;
	private PlanEditorModel model;

	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.arc.spife.core.plan.advisor.resources/data-test/TestStateRequirement.dictionary", true));
		requires90PercentCoverage = createActivity("Requires90PercentCoverage");
		notGeneratesCoverage = createActivity("NotGeneratesCoverage");
		generatesCoverage = createActivity("GeneratesCoverage");
		requiresBeforeAndGenerate = createActivity("RequiresBeforeAndGenerate");
		requiresThroughoutAndGenerate = createActivity("RequiresThroughoutAndGenerate");
		turnOnResource = createActivity("TurnOnResource");
		turnOffResource = createActivity("TurnOffResource");
		disallowsThroughout = createActivity("DisallowsThroughout");
		allowsMultipleThroughout = createActivity("AllowsMultipleThroughout");
		generatesOffsetCoverage = createActivity("GeneratesOffsetCoverage");
		requiresOffsetCoverage = createActivity("RequiresOffsetCoverage");
		model = createPlanEditorModel();
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (model != null) {
			model.dispose();
		}
	}

	public void test90PercentCoveragePasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, requires90PercentCoverage, PLAN_START);
				addActivityAtTime(plan, generatesCoverage, PLAN_START);
			}
		});
		assertViolationCount(plan, 0);
	}
	
	public void test90PercentCoverageFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, requires90PercentCoverage, PLAN_START);
				addActivityAtTime(plan, notGeneratesCoverage, PLAN_START);
			}
		});
		assertViolationCount(plan, 1);
	}
	
	/**
	 * Test for SPF-5742 in which an activity that effects the same resource it requires to
	 * be a certain state before it's start
	 */
	public void testRequiresBeforeAndGeneratePasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOnResource, PLAN_START);
				addActivityAtTime(plan, requiresBeforeAndGenerate, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, 0);
	}
	
	/**
	 * Test for SPF-5669 in which an activity that effects the same resource it requires to
	 * be a certain state throughout
	 */
	public void testRequiresThroughoutAndGeneratePasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOnResource, PLAN_START);
				addActivityAtTime(plan, requiresThroughoutAndGenerate, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, 0);
	}
	
	public void testDisallowsThroughoutFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOnResource, PLAN_START);
				addActivityAtTime(plan, disallowsThroughout, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, 1);
	}
	
	public void testDisallowsThroughoutPasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOffResource, PLAN_START);
				addActivityAtTime(plan, disallowsThroughout, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, 0);
	}
	
	public void testAllowsMultipleThroughoutFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOffResource, PLAN_START);
				addActivityAtTime(plan, allowsMultipleThroughout, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}
		});
		assertViolationCount(plan, 1);
	}
	
	public void testAllowsMultipleThroughoutPasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, turnOnResource, PLAN_START);
				addActivityAtTime(plan, allowsMultipleThroughout, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}

		});
		assertViolationCount(plan, 0);
	}
	
	public void testRequiresOffsetCoverageFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, generatesCoverage, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
				addActivityAtTime(plan, requiresOffsetCoverage, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}

		});
		assertViolationCount(plan, 1);
	}
	
	public void testRequiresOffsetCoveragePasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				addActivityAtTime(plan, generatesOffsetCoverage, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
				addActivityAtTime(plan, requiresOffsetCoverage, DateUtils.add(PLAN_START, Amount.valueOf(1, NonSI.HOUR)));
			}

		});
		assertViolationCount(plan, 0);
	}
	
	private void addActivityAtTime(final EPlan plan, EActivity activity, Date date) {
		activity.getMember(TemporalMember.class).setStartTime(date);
		plan.getChildren().add(activity);
	}
	
	private void assertViolationCount(EPlan plan, int violationCount) {
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), violationCount);
	}
	
}
