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
package gov.nasa.ensemble.core.model.plan.temporal;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.measure.quantity.Duration;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.jscience.physics.amount.Amount;

public class TestTemporalMemberSerialization extends TestCase {

	private URI testURI = URI.createFileURI(TestTemporalMemberSerialization.class.getCanonicalName() + "-test.plan");
	
	/**
	 * Check various calculated variables
	 * @throws IOException 
	 */
	public void testCalculatedVariables() throws IOException {
		Resource resource = new PlanResourceImpl(testURI);
		for (EPlanElement element : Util.createTestElements()) {
			resource.getContents().add(element);
			TemporalMember member = element.getMember(TemporalMember.class);
			for (CalculatedVariable cv : CalculatedVariable.values()) {
				member.setCalculatedVariable(cv);
				TemporalMember member2 = roundTrip(element);
				checkValues(member, member2);
			}
		}
	}

	/**
	 * Check various calculated variables
	 * @throws IOException 
	 */
	public void testCalculatedVariablesWithStartAndDuration() throws IOException {
		Resource resource = new PlanResourceImpl(testURI);
		for (EPlanElement element : Util.createDurativeElements()) {
			resource.getContents().add(element);
			TemporalMember member = element.getMember(TemporalMember.class);
			for (CalculatedVariable cv : CalculatedVariable.values()) {
				member.setCalculatedVariable(cv);
				TemporalMember member2 = roundTrip(element);
				checkValues(member, member2);
			}
		}
	}

	/**
	 * Check various calculated variables and plan temporal member serialization
	 * @throws IOException 
	 */
	public void testCalculatedVariablesWithPlans() throws IOException {
		Resource resource = new PlanResourceImpl(testURI);
		for (EPlanElement element : Util.createPlansElements()) {
			resource.getContents().add(element);
			PlanTemporalMember member = (PlanTemporalMember)element.getMember(TemporalMember.class);
			Date startBoundary = new Date();
			Amount<Duration> duration = AmountUtils.toAmount(5040, DateUtils.MILLISECONDS);
			Date endBoundary = DateUtils.add(startBoundary, duration);
			Date start = DateUtils.subtract(startBoundary, duration);
			Date end = DateUtils.add(endBoundary, duration);
			member.setStartBoundary(startBoundary);
			member.setEndBoundary(endBoundary);
			member.setExtent(new TemporalExtent(start, end));
			for (CalculatedVariable cv : CalculatedVariable.values()) {
				member.setCalculatedVariable(cv);
				PlanTemporalMember member2 = (PlanTemporalMember)roundTrip(element);
				checkValues(member, member2);
				Date start1 = member.getStartBoundary();
				Date start2 = member2.getStartBoundary();
				Date end1 = member.getEndBoundary();
				Date end2 = member2.getEndBoundary();
				assertEquals("unexpected start boundary", start1, start2);
				assertEquals("unexpected end boundary", end1, end2);
			}
		}
	}

	/**
	 * Check various calculated variables
	 * @throws IOException 
	 */
	public void testPlanCalculatedVariablesWithStartAndDuration() throws IOException {
		Resource resource = new PlanResourceImpl(testURI);
		for (EPlanElement element : Util.createDurativeElements()) {
			resource.getContents().add(element);
			TemporalMember member = element.getMember(TemporalMember.class);
			for (CalculatedVariable cv : CalculatedVariable.values()) {
				member.setCalculatedVariable(cv);
				TemporalMember member2 = roundTrip(element);
				checkValues(member, member2);
			}
		}
	}

	/*
	 * Utility methods
	 */
	
	private TemporalMember roundTrip(EPlanElement element) throws IOException {
		String xml = EMFUtils.convertToXML(element);
		InputStream stream = new ByteArrayInputStream(xml.getBytes());
		Resource resource2 = new PlanResourceImpl(testURI);
		resource2.load(stream, null);
		EPlanElement element2 = (EPlanElement)resource2.getContents().get(0);
		TemporalMember member2 = element2.getMember(TemporalMember.class);
		return member2;
	}

	private void checkValues(TemporalMember member, TemporalMember member2) {
		Date start = member.getStartTime();
		Date start2 = member2.getStartTime();
		Amount<Duration> duration = member.getDuration();
		Amount<Duration> duration2 = member2.getDuration();
		Date endTime = member.getEndTime();
		Date endTime2 = member2.getEndTime();
		CalculatedVariable variable = member.getCalculatedVariable();
		CalculatedVariable variable2 = member2.getCalculatedVariable();
		TemporalExtent extent = member.getExtent();
		TemporalExtent extent2 = member2.getExtent();
		assertEquals("unexpected calculated variable", variable, variable2);
		assertEquals("unexpected start time", start, start2);
		assertEquals("unexpected duration", duration, duration2);
		assertEquals("unexpected end time", endTime, endTime2);
		if (extent == null) {
			assertNull("other extent should be null", extent2);
		} else {
			assertNotNull("other extent should not be null", extent2);
			Date extentStart = extent.getStart();
			Date extentStart2 = extent2.getStart();
			Amount<Duration> extentDuration = extent.getDuration();
			Amount<Duration> extentDuration2 = extent2.getDuration();
			Date extentEndTime = extent.getEnd();
			Date extentEndTime2 = extent2.getEnd();
			assertEquals("unexpected extent start time", extentStart, extentStart2);
			assertEquals("unexpected extent duration", extentDuration, extentDuration2);
			assertEquals("unexpected extent end time", extentEndTime, extentEndTime2);
		}
	}

}
