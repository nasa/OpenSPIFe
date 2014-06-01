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
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.consistency.IConsistencyPropertyTester;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.junit.Assert;

public class ChainConstraintConsistencyPropertyTester implements
		IConsistencyPropertyTester {

	@Override
	public void test(EPlanElement element) {
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		Boolean scheduled = temporalMember.getScheduled();
		if (scheduled == null || scheduled.booleanValue()) {
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class);
			TemporalChain chain = constraintsMember.getChain();
			if (chain != null) {
				if (chain.getWaiverRationale() != null) {
					return; // skip waived chains
				}
				List<EPlanElement> chainElements = chain.getElements();
				int index = chainElements.indexOf(element);
				Assert.assertNotSame(-1, index);
				EPlanElement predecessor = getScheduledPredecessor(chainElements, index);
				if (predecessor != null) {
					Date endTime = predecessor.getMember(TemporalMember.class).getEndTime();
					Date startTime = temporalMember.getStartTime();
					Assert.assertFalse(endTime.after(startTime));
				}
				EPlanElement successor = getScheduledSuccessor(chainElements, index);
				if (successor != null) {
					Date endTime = temporalMember.getEndTime();
					Date startTime = successor.getMember(TemporalMember.class).getStartTime();
					Assert.assertFalse(endTime.after(startTime));
				}
				// TODO: need to handle the preference for meets chaining
			}
		}
	}

	private EPlanElement getScheduledPredecessor(List<EPlanElement> chainElements, int index) {
		ListIterator<EPlanElement> iterator = chainElements.listIterator(index);
		while (iterator.hasPrevious()) {
			EPlanElement previous = iterator.previous();
			TemporalMember member = previous.getMember(TemporalMember.class);
			Boolean scheduled = member.getScheduled();
			if (scheduled == null || scheduled) {
				return previous;
			}
		}
		return null;
	}

	private EPlanElement getScheduledSuccessor(List<EPlanElement> chainElements, int index) {
		ListIterator<EPlanElement> iterator = chainElements.listIterator(index + 1);
		while (iterator.hasNext()) {
			EPlanElement next = iterator.next();
			TemporalMember member = next.getMember(TemporalMember.class);
			Boolean scheduled = member.getScheduled();
			if (scheduled == null || scheduled) {
				return next;
			}
		}
		return null;
	}

}
