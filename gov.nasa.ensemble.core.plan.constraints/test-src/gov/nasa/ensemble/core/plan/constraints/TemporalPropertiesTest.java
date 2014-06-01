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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyBounds;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyProperties;
import gov.nasa.ensemble.core.plan.constraints.network.DistanceGraph;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetworkModel;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.tests.core.plan.ActivityDefGenerator;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.jscience.physics.amount.Amount;

public abstract class TemporalPropertiesTest extends TestCase {

	protected static final long PLUS_INFINITY = TemporalNetworkModel.convertNetworkToTimeDistance(DistanceGraph.POS_INFINITY);
	protected static final long MINUS_INFINITY = TemporalNetworkModel.convertNetworkToTimeDistance(DistanceGraph.NEG_INFINITY);
	
	protected EPlan plan;
	protected EActivityGroup group1;
	protected EActivity activity1_1;
	protected EActivity activity1_2;
	protected EActivity activity1_3;
	protected EActivityGroup group2;
	protected EActivity activity2_1;
	protected EActivity activity2_2;
	protected EActivity activity2_3;
	protected EActivityGroup group3;
	protected EActivity activity3_1;
	protected EActivity activity3_2;
	protected EActivity activity3_3;
	protected Set<EPlanElement> allElements;

	public TemporalPropertiesTest() {
		super();
	}

	public TemporalPropertiesTest(String name) {
		super(name);
	}

	protected abstract ConsistencyBounds getBounds(EPlanElement element);

	protected abstract ConsistencyProperties getProperties(EPlanElement element);

	@Override
	protected void setUp() throws Exception {
		ActivityDefGenerator generator = new ActivityDefGenerator() {
			@Override
			public EActivityDef next() {
				EActivityDef def = super.next();
				// Activities with subactivities won't allow us 
				// to set their durations directly like we want to.
				// So, only return the def is it has no subactivities.
				// Otherwise, skip to the next.
				if (def.getChildren().isEmpty()) {
					return def;
				}
				return next();
			}
		};
		plan = PlanFactory.getInstance().createPlan("TemporalPropertiesTestPlan");
		group1 = PlanFactory.getInstance().createActivityGroup(plan);
		activity1_1 = PlanFactory.getInstance().createActivity(generator.next(), group1);
		activity1_2 = PlanFactory.getInstance().createActivity(generator.next(), group1);
		activity1_3 = PlanFactory.getInstance().createActivity(generator.next(), group1);
		group2 = PlanFactory.getInstance().createActivityGroup(plan);
		activity2_1 = PlanFactory.getInstance().createActivity(generator.next(), group2);
		activity2_2 = PlanFactory.getInstance().createActivity(generator.next(), group2);
		activity2_3 = PlanFactory.getInstance().createActivity(generator.next(), group2);
		group3 = PlanFactory.getInstance().createActivityGroup(plan);
		activity3_1 = PlanFactory.getInstance().createActivity(generator.next(), group3);
		activity3_2 = PlanFactory.getInstance().createActivity(generator.next(), group3);
		activity3_3 = PlanFactory.getInstance().createActivity(generator.next(), group3);
		group1.setName("group 1");
		group2.setName("group 2");
		group3.setName("group 3");
		activity1_1.setName("activity 1.1");
		activity1_2.setName("activity 1.2");
		activity1_3.setName("activity 1.3");
		activity2_1.setName("activity 2.1");
		activity2_2.setName("activity 2.2");
		activity2_3.setName("activity 2.3");
		activity3_1.setName("activity 3.1");
		activity3_2.setName("activity 3.2");
		activity3_3.setName("activity 3.3");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				constructPlan(plan);
			}
		});
		allElements = EPlanUtils.computeContainedElements(Collections.singletonList(plan));
	}

	private void constructPlan(EPlan ePlan) {
		plan.getChildren().add(group1);
		group1.getChildren().add(activity1_1);
		group1.getChildren().add(activity1_2);
		group1.getChildren().add(activity1_3);
		plan.getChildren().add(group2);
		group2.getChildren().add(activity2_1);
		group2.getChildren().add(activity2_2);
		group2.getChildren().add(activity2_3);
		plan.getChildren().add(group3);
		group3.getChildren().add(activity3_1);
		group3.getChildren().add(activity3_2);
		group3.getChildren().add(activity3_3);
		Date planDate = MissionCalendarUtils.getMissionDate(0);
		Date date1_1 = MissionCalendarUtils.getMissionDate(1);
		Date date1_2 = DateUtils.add(date1_1, 3600*1000L);
		Date date1_3 = DateUtils.add(date1_2, 3600*1000L);
		Date date2_1 = MissionCalendarUtils.getMissionDate(2);
		Date date2_2 = DateUtils.add(date2_1, 2400*1000L);
		Date date2_3 = DateUtils.add(date2_2, 4600*1000L);
		Date date3_1 = MissionCalendarUtils.getMissionDate(3);
		Date date3_2 = DateUtils.add(date3_1, 1600*1000L);
		Date date3_3 = DateUtils.add(date3_2, 5000*1000L);
		ePlan.getMember(PlanTemporalMember.class).setStartBoundary(planDate);
		plan.getMember(TemporalMember.class).setStartTime(planDate);
		activity1_1.getMember(TemporalMember.class).setStartTime(date1_1);
		activity1_1.getMember(TemporalMember.class).setDuration(Amount.valueOf(800, SI.SECOND));
		activity1_2.getMember(TemporalMember.class).setStartTime(date1_2);
		activity1_2.getMember(TemporalMember.class).setDuration(Amount.valueOf(400, SI.SECOND));
		activity1_3.getMember(TemporalMember.class).setStartTime(date1_3);
		activity1_3.getMember(TemporalMember.class).setDuration(Amount.valueOf(250, SI.SECOND));
		activity2_1.getMember(TemporalMember.class).setStartTime(date2_1);
		activity2_1.getMember(TemporalMember.class).setDuration(Amount.valueOf(600, SI.SECOND));
		activity2_2.getMember(TemporalMember.class).setStartTime(date2_2);
		activity2_2.getMember(TemporalMember.class).setDuration(Amount.valueOf(500, SI.SECOND));
		activity2_3.getMember(TemporalMember.class).setStartTime(date2_3);
		activity2_3.getMember(TemporalMember.class).setDuration(Amount.valueOf(330, SI.SECOND));
		activity3_1.getMember(TemporalMember.class).setStartTime(date3_1);
		activity3_1.getMember(TemporalMember.class).setDuration(Amount.valueOf(100, SI.SECOND));
		activity3_2.getMember(TemporalMember.class).setStartTime(date3_2);
		activity3_2.getMember(TemporalMember.class).setDuration(Amount.valueOf(200, SI.SECOND));
		activity3_3.getMember(TemporalMember.class).setStartTime(date3_3);
		activity3_3.getMember(TemporalMember.class).setDuration(Amount.valueOf(300, SI.SECOND));
		createConstraint(group1, Timepoint.START, activity1_1, Timepoint.START, "activity 1.1 starts after group 1 starts");
		createConstraint(group1, Timepoint.START, activity1_2, Timepoint.START, "activity 1.2 starts after group 1 starts");
		createConstraint(group1, Timepoint.START, activity1_3, Timepoint.START, "activity 1.3 starts after group 1 starts");
		createConstraint(activity1_1, Timepoint.END, group1, Timepoint.END, "group 1 ends after activity 1.1 ends");
		createConstraint(activity1_2, Timepoint.END, group1, Timepoint.END, "group 1 ends after activity 1.2 ends");
		createConstraint(activity1_3, Timepoint.END, group1, Timepoint.END, "group 1 ends after activity 1.3 ends");
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				for (PeriodicTemporalConstraint constraint : ConstraintUtils.getPeriodicConstraints(element, false)) {
					ConstraintUtils.detachConstraint(constraint);
				}
			}
		}.visitAll(plan);
	}

	private void createConstraint(EPlanElement planElementA, Timepoint endpointA, EPlanElement planElementB, Timepoint endpointB, String rationale) {
		BinaryTemporalConstraint constraint = ConstraintUtils.createConstraint(planElementA, endpointA, planElementB, endpointA, Amount.valueOf(0, SI.SECOND), null, rationale);
		ConstraintUtils.attachConstraint(constraint);
	}
	
	@Override
	protected void tearDown() throws Exception {
		WrapperUtils.dispose(plan);
		plan = null;
		group1 = null;
		activity1_1 = null;
		activity1_2 = null;
		activity1_3 = null;
		group2 = null;
		activity2_1 = null;
		activity2_2 = null;
		activity2_3 = null;
		group3 = null;
		activity3_1 = null;
		activity3_2 = null;
		activity3_3 = null;
		allElements = null;
	}

	protected static long getEffectiveDuration(EPlanElement affectedElement) {
		TemporalExtent extent = affectedElement.getMember(TemporalMember.class).getExtent();
		long duration = (extent != null ? extent.getDurationMillis() : 0L);
		if (affectedElement instanceof EActivityGroup) {
			duration = 0L;
			// use the shortest possible duration for the group
			EActivityGroup group = (EActivityGroup) affectedElement;
			for (EPlanChild activity : group.getChildren()) {
				extent = activity.getMember(TemporalMember.class).getExtent();
				if (extent.getDurationMillis() > duration) {
					duration = extent.getDurationMillis();
				}
			}
		}
		return duration;
	}

}
