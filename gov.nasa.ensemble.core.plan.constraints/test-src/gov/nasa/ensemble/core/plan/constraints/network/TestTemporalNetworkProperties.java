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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalPropertiesTest;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.HashSet;
import java.util.Set;

public class TestTemporalNetworkProperties extends TemporalPropertiesTest {

	private TemporalNetworkMember network;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		network = TemporalNetworkMember.get(plan);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		network = null;
	}
	
	@Override
	protected ConsistencyProperties getProperties(EPlanElement element) {
		return network.getProperties(element);
	}

	@Override
	protected ConsistencyBounds getBounds(EPlanElement element) {
		return network.getBounds(element);
	}

	public void testAllBoundsVsProperties() {
		for (EPlanElement element : allElements) {
			ConsistencyBounds bounds = getBounds(element);
			ConsistencyProperties properties = getProperties(element);
			assertNotNull(bounds);
			assertNotNull(properties);
			assertEquals(bounds, properties.getBounds());
			for (ConsistencyConstraint constraint : properties.getConstraints()) {
				assertEquals(constraint.sourceElement, element);
			}
		}
	}

	public void testPlanProperties() {
		ConsistencyProperties properties = getProperties(plan);
		Set<ConsistencyConstraint> constraints = properties.getConstraints();
		int count = allElements.size() - 1; // everything except ourselves (the plan)
		Set<ConsistencyConstraint> startConstraints = new HashSet<ConsistencyConstraint>();
		Set<ConsistencyConstraint> endConstraints = new HashSet<ConsistencyConstraint>();
		for (ConsistencyConstraint constraint : constraints) {
			if (constraint.affectedTimepoint == Timepoint.START) {
				startConstraints.add(constraint);
			} else {
				assertEquals(Timepoint.END, constraint.affectedTimepoint);
				endConstraints.add(constraint);
			}
		}			
		assertEquals("the plan affects all other start timepoints in it", count * 2, startConstraints.size());
		assertEquals("the plan affects all other end timepoints in it", count * 2, endConstraints.size());
		for (ConsistencyConstraint constraint : startConstraints) {
			// any start - plan start >= 0
			// any start - plan start <= +infinity
			long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
			long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
			if (constraint.sourceTimepoint == Timepoint.START) {
				assertTrue("everything must start after the plan start", 0 <= earliestDistance);
				assertTrue("everything is allowed to start arbitrarily later than the plan start", latestDistance >= PLUS_INFINITY);
			}
		}
		for (ConsistencyConstraint constraint : endConstraints) {
			// any end - plan start >= any duration
			// any end - plan start <= +infinity
			long duration = getEffectiveDuration(constraint.affectedElement);
			long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
			long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
			if (constraint.sourceTimepoint == Timepoint.START) {
				assertTrue("everything must end at least 'duration' after the plan start", duration <= earliestDistance);
				assertTrue("everything is allowed to end arbitrarily later than the plan start", latestDistance >= PLUS_INFINITY);
			}
		}
	}

	public void testGroupProperties() {
		for (EPlanChild group : plan.getChildren()) {
			ConsistencyProperties properties = getProperties(group);
			Set<ConsistencyConstraint> constraints = properties.getConstraints();
			Set<EPlanElement> affectedChildren = EPlanUtils.computeContainedElements(EPlanUtils.getChildren(group));
			int count = affectedChildren.size() + 1; // 1 more for the plan
			Set<ConsistencyConstraint> startConstraints = new HashSet<ConsistencyConstraint>();
			Set<ConsistencyConstraint> endConstraints = new HashSet<ConsistencyConstraint>();
			for (ConsistencyConstraint constraint : constraints) {
				if (constraint.affectedTimepoint == Timepoint.START) {
					startConstraints.add(constraint);
				} else {
					assertEquals(Timepoint.END, constraint.affectedTimepoint);
					endConstraints.add(constraint);
				}
			}			
			assertEquals("the group affects start timepoints in it and the plan", 2 * count, startConstraints.size());
			assertEquals("the group affects end timepoints in it and the plan", 2 * count, endConstraints.size());
			for (ConsistencyConstraint constraint : startConstraints) {
				long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
				long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
				if (constraint.affectedElement instanceof EPlan) {
					// plan start - group start >= -infinity
					// plan start - group start <= 0
					assertTrue("the plan is allowed to occur arbitrarily earlier than the group", earliestDistance <= MINUS_INFINITY);
					if (constraint.sourceTimepoint == Timepoint.START) {
						assertEquals("the plan must start before the start of the group", 0, latestDistance);
					}
				} else {
					assertTrue("the group should affect only plans and activities", constraint.affectedElement instanceof EActivity);
					assertTrue("the group should only affect activities that belong to it", affectedChildren.contains(constraint.affectedElement));
					// activity start - group start >= 0
					// activity start - group start <= +infinity
					if (constraint.sourceTimepoint == Timepoint.START) {
						assertTrue("the activities must occur after the group start", 0 <= earliestDistance);
	//					assertTrue("the activities are allowed to start arbitrarily late after the group", latestDistance >= PLUS_INFINITY); // SPF-7700: freezing
					}
				}
			}
			for (ConsistencyConstraint constraint : endConstraints) {
				long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
				long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
				if (constraint.affectedElement instanceof EPlan) {
					// plan end - group start >= group duration
					// plan end - group start <= +infinity
					long duration = getEffectiveDuration(group);
					if (constraint.sourceTimepoint == Timepoint.START) {
						assertTrue("the plan must end at least 'duration' after the group starts", duration <= earliestDistance);
					}
					assertTrue("the plan is allowed to end arbitrarily later than the group starts", latestDistance >= PLUS_INFINITY);
				} else {
					assertTrue("the group should affect only plans and activities", constraint.affectedElement instanceof EActivity);
					assertTrue("the group should only affect activities that belong to it", affectedChildren.contains(constraint.affectedElement));
					// activity end - group start >= activity duration
					// activity end - group start <= +infinity
					long duration = getEffectiveDuration(constraint.affectedElement);
					if (constraint.sourceTimepoint == Timepoint.START) {
						assertTrue("activities must end at least 'duration' after the group start", duration <= earliestDistance);
					}
//					assertTrue("activities are allowed to end arbitrarily later than the group start", latestDistance >= PLUS_INFINITY); // SPF-7700: freezing
				}
			}
		}
	}

	public void testActivityProperties() {
		for (EActivity activity : EPlanUtils.getActivities(plan)) {
			ConsistencyProperties properties = getProperties(activity);
			Set<ConsistencyConstraint> constraints = properties.getConstraints();
//			Set<EPlanElement> affectedChildren = EPlanUtils.computeContainedElements(EPlanUtils.getChildren(activity));
//			int count = affectedChildren.size() + 2; // 1 for the plan, 1 for the group
			Set<ConsistencyConstraint> startConstraints = new HashSet<ConsistencyConstraint>();
			Set<ConsistencyConstraint> endConstraints = new HashSet<ConsistencyConstraint>();
			for (ConsistencyConstraint constraint : constraints) {
				if (constraint.affectedTimepoint == Timepoint.START) {
					startConstraints.add(constraint);
				} else {
					assertEquals(Timepoint.END, constraint.affectedTimepoint);
					endConstraints.add(constraint);
				}
			}			
//			assertEquals("the activity affects its group and the plan starts", count, startConstraints.size()); // SPF-7700: freezing
//			assertEquals("the activity affects its group and the plan ends", count, endConstraints.size()); // SPF-7700: freezing
			for (ConsistencyConstraint constraint : startConstraints) {
				long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
				long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
				if ((constraint.affectedElement instanceof EPlan) || (constraint.affectedElement instanceof EActivityGroup)) {
					// group/plan start - activity start >= -infinity
					// group/plan start - activity start <= 0
//					assertTrue("the group/plan is allowed to occur arbitrarily earlier than the activity", earliestDistance <= MINUS_INFINITY); // SPF-7700: freezing
					assertTrue("the group/plan must start before the start of the activity", 0 >= latestDistance);
				} else if (constraint.affectedElement.eContainer().equals(activity)) { 
					assertTrue("the activity should affect only plans, groups, and subactivities", constraint.affectedElement instanceof EActivity);
//					assertTrue("the activity should only affect activities that belong to it", affectedChildren.contains(constraint.affectedElement)); // SPF-7700: freezing
					// activity start - group start >= 0
					// activity start - group start <= +infinity
					assertTrue("the subactivities must occur after the activity start", 0 <= earliestDistance);
				}
			}
			for (ConsistencyConstraint constraint : endConstraints) {
				long earliestDistance = constraint.minimumDistance.longValue(DateUtils.MILLISECONDS);
//				long latestDistance = constraint.maximumDistance.longValue(DateUtils.MILLISECONDS);
				if ((constraint.affectedElement instanceof EPlan) || (constraint.affectedElement instanceof EActivityGroup)) {
					// group/plan end - activity start >= group duration
					// group/plan end - activity start <= +infinity
					long duration = getEffectiveDuration(activity);
					if (constraint.sourceTimepoint == Timepoint.START) {
						assertTrue("the group/plan must end at least 'duration' after the activity starts", duration <= earliestDistance);
					}
//					assertTrue("the group/plan is allowed to end arbitrarily later than the activity starts", latestDistance >= PLUS_INFINITY); // SPF-7700: freezing
				} else if (constraint.affectedElement.eContainer().equals(activity)) { 
					assertTrue("the activity should affect only plans, groups, and subactivities", constraint.affectedElement instanceof EActivity);
//					assertTrue("the activity should only affect activities that belong to it", affectedChildren.contains(constraint.affectedElement)); // SPF-7700: freezing
					// activity end - group start >= activity duration
					// activity end - group start <= +infinity
					long duration = getEffectiveDuration(constraint.affectedElement);
					assertTrue("activities must end at least 'duration' after the group start", duration <= earliestDistance);
				}
			}
		}
	}
	
	
}
