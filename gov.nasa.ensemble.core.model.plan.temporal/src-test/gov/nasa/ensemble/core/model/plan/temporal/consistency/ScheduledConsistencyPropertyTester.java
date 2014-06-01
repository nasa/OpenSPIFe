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

import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.List;

import org.junit.Assert;

public class ScheduledConsistencyPropertyTester implements IConsistencyPropertyTester {

	@Override
	public void test(EPlanElement parent) {
		List<? extends EPlanChild> children = parent.getChildren();
		if (!children.isEmpty()) {
			boolean anyScheduled = false;
			boolean anyUnscheduled = false;
			for (EPlanChild child : children) {
				TemporalMember member = child.getMember(TemporalMember.class);
				Boolean scheduled = member.getScheduled();
				if (scheduled == null) {
					anyScheduled = true;
					anyUnscheduled = true;
				} else if (scheduled) {
					anyScheduled = true;
				} else {
					anyUnscheduled = true;
				}
				if (anyScheduled && anyUnscheduled) {
					break;
				}
			}
			TemporalMember parentMember = parent.getMember(TemporalMember.class);
			Boolean parentScheduled = parentMember.getScheduled();
			if (parentScheduled == null) {
				Assert.assertTrue(anyScheduled);
				Assert.assertTrue(anyUnscheduled);
			} else if (parentScheduled) {
				Assert.assertTrue(anyScheduled);
				Assert.assertFalse(anyUnscheduled);
			} else {
				Assert.assertFalse(anyScheduled);
				Assert.assertTrue(anyUnscheduled);
			}
		}
	}

}
