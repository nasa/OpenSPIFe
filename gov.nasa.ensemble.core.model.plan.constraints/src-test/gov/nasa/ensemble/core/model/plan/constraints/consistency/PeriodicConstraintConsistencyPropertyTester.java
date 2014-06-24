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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.consistency.IConsistencyPropertyTester;

import java.util.Date;

import org.junit.Assert;

public class PeriodicConstraintConsistencyPropertyTester implements IConsistencyPropertyTester {

	@Override
	public void test(EPlanElement element) {
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		Boolean scheduled = temporalMember.getScheduled();
		if (scheduled == null || scheduled) {
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
			if (constraintsMember != null) {
				for (PeriodicTemporalConstraint constraint : constraintsMember.getPeriodicTemporalConstraints()) {
					ConstraintPoint point = constraint.getPoint();
					Assert.assertSame(element, point.getElement());
					if (constraint.getWaiverRationale() != null) {
						continue; // skip waived constraints
					}
					Date date = point.getDate();
					Date earliest = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
					if (earliest != null) {
						Assert.assertFalse(earliest.after(date));
					}
					Date latest = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
					if (latest != null) {
						Assert.assertFalse(latest.before(date));
					}
				}
			}
		}
	}

}
