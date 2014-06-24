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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalModifier;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.temporal.modification.DirectPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EAttribute;
import org.jscience.physics.amount.Amount;

public class TestTemporalModifier extends TestCase {

	private static final TemporalModifier TEMPORAL_MODIFIER = TemporalModifier.getInstance();
	private static final Date START = new Date();
	private static final TemporalExtent EXTENT = new TemporalExtent(START, AmountUtils.toAmount(3600, SI.SECOND));
	
	private EPlan plan;
	private EActivity activity;

	@Override
	protected void setUp() throws Exception {
		PlanFactory factory = PlanFactory.getInstance();
		plan = factory.createPlan(TestTemporalModifier.class.getCanonicalName());
		activity = factory.createActivityInstance();
		activity.getMember(TemporalMember.class).setExtent(EXTENT);
		TransactionUtils.checkedWriting(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
			}
		});
	}
	
	public void testSetStartTimeCalculatingDurationWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.DURATION, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME, START);
	}

	public void testSetStartTimeCalculatingEndTimeWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.END, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME, START);
	}
	
	public void testSetEndTimeCalculatingDurationWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.DURATION, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME, START);
	}
	
	public void testSetEndTimeCalculatingStartTimeWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.START, TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME, START);
	}
	
	public void testSetDurationCalculatingStartTimeWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.START, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION, AmountUtils.toAmount(300, DateUtils.MILLISECONDS));
	}
	
	public void testSetDurationCalculatingEndTimeWithDirectPlanModifier() throws Exception {
		check(new DirectPlanModifier(), CalculatedVariable.END, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION, AmountUtils.toAmount(345, DateUtils.MILLISECONDS));
	}
	
	private void check(DirectPlanModifier modifier, final CalculatedVariable calculatedVariable, final EAttribute feature, final Object value) throws Exception {
		PlanModifierMember.get(plan).setModifier(modifier);
		TransactionUtils.checkedWriting(plan, new Runnable() {
			@Override
			public void run() {
				TemporalMember member = activity.getMember(TemporalMember.class);
				member.setCalculatedVariable(calculatedVariable);
				IUndoableOperation operation = TEMPORAL_MODIFIER.set(member, feature, value);
				CommonUtils.execute(operation, TransactionUtils.getUndoContext(member));
				Object newValue = member.eGet(feature);
				if (newValue instanceof Amount) {
					assertTrue(AmountUtils.equals((Amount)value, (Amount)newValue));
				} else if (newValue instanceof Date) {
					assertEquals(((Date)value).getTime(), ((Date)newValue).getTime());
				} else {
					fail("unexpected data type");
				}
				operation = TEMPORAL_MODIFIER.set(member, feature, null);
				CommonUtils.execute(operation, TransactionUtils.getUndoContext(member));
				if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME) {
					operation = TEMPORAL_MODIFIER.set(member, feature, EXTENT.getStart());
					CommonUtils.execute(operation, TransactionUtils.getUndoContext(member));
				}
				if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION) {
					operation = TEMPORAL_MODIFIER.set(member, feature, EXTENT.getDuration());
					CommonUtils.execute(operation, TransactionUtils.getUndoContext(member));
				}
				if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
					operation = TEMPORAL_MODIFIER.set(member, feature, EXTENT.getEnd());
					CommonUtils.execute(operation, TransactionUtils.getUndoContext(member));
				}
			}
		});
	}
	
}
