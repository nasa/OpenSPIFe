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
package gov.nasa.ensemble.core.model.plan.temporal.util;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalFactory;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class TestTemporalMemberPlanSharingNotificationFilter extends TestCase {

	private static final Date TIME = new Date(0);
	private static final Amount<Duration> ONE_HOUR = Amount.valueOf(1, NonSI.HOUR);
	private static final TemporalMemberPlanSharingNotificationFilter FILTER = new TemporalMemberPlanSharingNotificationFilter();

	public void testStartFiltering() {
		testFiltering(CalculatedVariable.START, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME);
	}

	public void testEndFiltering() {
		testFiltering(CalculatedVariable.END, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
	}

	public void testDurationFiltering() {
		testFiltering(CalculatedVariable.DURATION, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
	}
	
	private void testFiltering(CalculatedVariable calculatedVariable, final EStructuralFeature derivedfeature, EStructuralFeature settableFeature) {
		TemporalMember member = TemporalFactory.eINSTANCE.createTemporalMember();
		member.setStartTime(TIME);
		member.setDuration(ONE_HOUR);
		member.setCalculatedVariable(calculatedVariable);
		member.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				if (FILTER.matches(msg)) {
					assertTrue("derived temporal feature not filtered out", CommonUtils.equals(msg.getFeature(), derivedfeature));
				}
				super.notifyChanged(msg);
			}
			
		});
		
		try {
			member.eSet(derivedfeature, new Date(System.currentTimeMillis()));
			fail("expected failure state");
		} catch (Exception e) {
			// we expected this
		}
		
		member.eSet(settableFeature, DateUtils.add(TIME, ONE_HOUR));
	}
	
}
