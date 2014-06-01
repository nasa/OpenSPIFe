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
package gov.nasa.ensemble.core.model.plan.temporal.consistency;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

public class UseChildTimesConsistencyPropertyTester implements IConsistencyPropertyTester {
	
	private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
	private static final Date MIN_DATE = new Date(Long.MIN_VALUE);

	@Override
	public void test(EPlanElement element) {
		List<? extends EPlanChild> children = element.getChildren();
		if (!children.isEmpty()) {
			TemporalMember parentMember = element.getMember(TemporalMember.class);
			if (parentMember.isUseChildTimes()) {
				List<TemporalMember> childMembers = new ArrayList<TemporalMember>();
				for (EPlanChild child : children) {
					TemporalMember childMember = child.getMember(TemporalMember.class);
					if (!childMember.equals("isUsingParentTimes()")) {
						childMembers.add(childMember);
					}
				}
				if (childMembers.isEmpty()) {
					return; // nothing to check in this case
				}
				Date earliestStartForScheduledElement = MAX_DATE;
				Date latestEndForScheduledElement = MIN_DATE;
				Date earliestStartForAnyElement = MAX_DATE;
				Date latestEndForAnyElement = MIN_DATE;
				for (TemporalMember childMember : childMembers) {
					Date childStart = childMember.getStartTime();
					Date childEnd = childMember.getEndTime();
					Boolean childScheduled = childMember.getScheduled();
					if (childScheduled == null || childScheduled.booleanValue()) {
						earliestStartForScheduledElement = DateUtils.earliest(childStart, earliestStartForScheduledElement);
						latestEndForScheduledElement = DateUtils.latest(childEnd, latestEndForScheduledElement);
					} else {
						earliestStartForAnyElement = DateUtils.earliest(childStart, earliestStartForAnyElement);
						latestEndForAnyElement = DateUtils.latest(childEnd, latestEndForAnyElement);
					}
				}
				Date parentStart = parentMember.getStartTime();
				Date parentEnd = parentMember.getEndTime();
				if (earliestStartForScheduledElement != MAX_DATE) {
					Assert.assertEquals(parentStart, earliestStartForScheduledElement);
					Assert.assertEquals(parentEnd, latestEndForScheduledElement);
					Assert.assertFalse(earliestStartForAnyElement.after(earliestStartForScheduledElement));
					Assert.assertFalse(latestEndForAnyElement.before(latestEndForScheduledElement));
				} else {
					Assert.assertEquals(parentStart, earliestStartForAnyElement);
					Assert.assertEquals(parentEnd, latestEndForAnyElement);
					Assert.assertSame(latestEndForScheduledElement, MIN_DATE);
				}
			}
		}
	}

}
