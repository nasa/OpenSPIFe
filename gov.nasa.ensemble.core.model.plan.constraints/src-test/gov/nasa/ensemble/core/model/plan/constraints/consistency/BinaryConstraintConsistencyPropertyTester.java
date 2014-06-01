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
package gov.nasa.ensemble.core.model.plan.constraints.consistency;

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.consistency.IConsistencyPropertyTester;

import java.util.Date;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;
import org.junit.Assert;

public class BinaryConstraintConsistencyPropertyTester implements IConsistencyPropertyTester {

	@Override
	public void test(EPlanElement element) {
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		Boolean scheduled = temporalMember.getScheduled();
		if (scheduled == null || scheduled) {
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
			if (constraintsMember != null) {
				for (BinaryTemporalConstraint constraint : constraintsMember.getBinaryTemporalConstraints()) {
					ConstraintPoint pointA = constraint.getPointA();
					ConstraintPoint pointB = constraint.getPointB();
					Assert.assertTrue(pointA.getElement() == element || pointB.getElement() == element);
					if (constraint.getWaiverRationale() != null) {
						continue; // skip waived constraints
					}
					Amount<Duration> maximumBminusA = constraint.getMaximumBminusA();
					Amount<Duration> minimumBminusA = constraint.getMinimumBminusA();
					if (maximumBminusA != null && minimumBminusA != null) {
						Assert.assertFalse(maximumBminusA.isLessThan(minimumBminusA));
					}
					testConstraint(pointA, pointB, maximumBminusA, minimumBminusA);
				}
			}
		}
	}

	private void testConstraint(ConstraintPoint pointA, ConstraintPoint pointB,
			Amount<Duration> maximumBminusA, Amount<Duration> minimumBminusA) {
		TemporalMember temporalMemberA = pointA.getElement().getMember(TemporalMember.class);
		TemporalMember temporalMemberB = pointB.getElement().getMember(TemporalMember.class);
		Boolean scheduledA = temporalMemberA.getScheduled();
		Boolean scheduledB = temporalMemberB.getScheduled();
		if (scheduledA != null && !scheduledA) {
			return; // skip this constraint because element A is unscheduled
		}
		if (scheduledB != null && !scheduledB) {
			return; // skip this constraint because element B is unscheduled
		}
		Date dateA = pointA.getDate();
		Date dateB = pointB.getDate();
		Amount<Duration> duration = DateUtils.subtract(dateB, dateA);
		if (maximumBminusA != null) {
			Assert.assertFalse(maximumBminusA.isLessThan(duration));
		}
		if (minimumBminusA != null) {
			Assert.assertFalse(minimumBminusA.isGreaterThan(duration));
		}
	}

}
