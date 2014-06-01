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

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;
import org.junit.Assert;

public class BasicConsistencyPropertyTester implements IConsistencyPropertyTester {

	@Override
	public void test(EPlanElement element) {
		TemporalMember member = element.getMember(TemporalMember.class);
		Date startTime = member.getStartTime();
		Amount<Duration> duration = member.getDuration();
		Date endTime = member.getEndTime();
		TemporalExtent extent = member.getExtent();
		Date timepointStart = member.getTimepointDate(Timepoint.START);
		Date timepointEnd = member.getTimepointDate(Timepoint.END);
		Assert.assertEquals(startTime, timepointStart);
		Assert.assertEquals(startTime, extent.getStart());
		Assert.assertEquals(endTime, timepointEnd);
		Assert.assertEquals(endTime, extent.getEnd());
		Assert.assertEquals(duration, DateUtils.subtract(endTime, startTime));
	}

}
