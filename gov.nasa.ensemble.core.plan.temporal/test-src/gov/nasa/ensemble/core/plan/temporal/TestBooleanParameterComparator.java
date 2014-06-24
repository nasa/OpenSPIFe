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
package gov.nasa.ensemble.core.plan.temporal;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterFacet;
import gov.nasa.ensemble.core.plan.temporal.columns.BooleanParameterComparator;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.junit.Assert;
import org.junit.Test;

public class TestBooleanParameterComparator extends Assert {

	private static final ParameterFacet<Boolean> NULL_FACET = null;
	
	@Test
	public void testBooleanParameterComparator() {
		final EPlan plan = PlanFactory.getInstance().createPlan("TEST_PLAN");
		final EActivity activity0 = PlanFactory.getInstance().createActivityInstance();
		final EActivity activity1 = PlanFactory.getInstance().createActivityInstance();
		final TemporalMember tm0 = activity0.getMember(TemporalMember.class);
		final TemporalMember tm1 = activity1.getMember(TemporalMember.class);
		TransactionUtils.writing(plan, new Runnable() {

			@Override
			public void run() {
				tm0.setScheduled(Boolean.TRUE);
				plan.getChildren().add(activity0);
				tm1.setScheduled(Boolean.FALSE);
				plan.getChildren().add(activity1);
			}
			
		});
		
		ParameterFacet<Boolean> f0 = new ParameterFacet<Boolean>(tm0, TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED, Boolean.TRUE);
		ParameterFacet<Boolean> f1 = new ParameterFacet<Boolean>(tm1, TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED, Boolean.FALSE);
		assertEquals(0, BooleanParameterComparator.INSTANCE.compare(NULL_FACET, NULL_FACET));
		assertEquals(-1, BooleanParameterComparator.INSTANCE.compare(NULL_FACET, f0));
		assertEquals(-1, BooleanParameterComparator.INSTANCE.compare(NULL_FACET, f1));
		assertEquals(1, BooleanParameterComparator.INSTANCE.compare(f0, NULL_FACET));
		assertEquals(1, BooleanParameterComparator.INSTANCE.compare(f1, NULL_FACET));
		assertEquals(1, BooleanParameterComparator.INSTANCE.compare(f0, f1));
		assertEquals(-1, BooleanParameterComparator.INSTANCE.compare(f1, f0));
	}

}
