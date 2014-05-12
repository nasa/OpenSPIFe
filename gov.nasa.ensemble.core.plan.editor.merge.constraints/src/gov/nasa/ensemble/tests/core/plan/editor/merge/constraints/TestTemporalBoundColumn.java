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
package gov.nasa.ensemble.tests.core.plan.editor.merge.constraints;

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.core.jscience.TimeOfDayFormat;
import gov.nasa.ensemble.core.jscience.TimeOfDayUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.NamedMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.constraints.TemporalBoundColumn;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.jscience.physics.amount.Amount;

public class TestTemporalBoundColumn extends TestCase {

	private static final PeriodicTemporalConstraint START_ANYTIME_AFTER = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint START_EXACTLY_AT = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint START_ANYTIME_BETWEEN = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint END_ANYTIME_BEFORE = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint END_ANYTIME_BEFORE_ZERO_MIN = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint END_ANYTIME_AFTER = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint START_ANYTIME_BEFORE = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint START_ANYTIME_BEFORE_ZERO_MIN = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint END_ANYTIME_BETWEEN = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	private static final PeriodicTemporalConstraint END_EXACTLY_AT = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
	
	private static final long TIME = MissionConstants.getInstance().getMissionStartTime().getTime();
	private static final long THOUSAND = (long)Math.ceil(1000*MissionConstants.getInstance().getEarthSecondsPerLocalSeconds());

	private static final Amount<Duration> TIME_ZERO = Amount.valueOf(0L, SI.SECOND);
	private static final Amount<Duration> TIME_A = Amount.valueOf(THOUSAND, SI.SECOND);
	private static final Amount<Duration> TIME_B = Amount.valueOf(2*THOUSAND, SI.SECOND);
	
	private static final IMergeColumnProvider TEST_PROVIDER = new NamedMergeColumnProvider("Test");
	
	private IUndoContext undoContext = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		undoContext = new ObjectUndoContext(this);
		
		START_ANYTIME_AFTER.getPoint().setEndpoint(Timepoint.START);
		START_ANYTIME_AFTER.setEarliest(TIME_A);
		START_ANYTIME_AFTER.setRationale("START_ANYTIME_AFTER");
		
		START_ANYTIME_BEFORE.getPoint().setEndpoint(Timepoint.START);
		START_ANYTIME_BEFORE.setLatest(TIME_A);
		START_ANYTIME_BEFORE.setRationale("START_ANYTIME_BEFORE");
		
		START_ANYTIME_BEFORE_ZERO_MIN.getPoint().setEndpoint(Timepoint.START);
		START_ANYTIME_BEFORE_ZERO_MIN.setEarliest(TIME_ZERO);
		START_ANYTIME_BEFORE_ZERO_MIN.setLatest(TIME_B);
		START_ANYTIME_BEFORE_ZERO_MIN.setRationale("START_ANYTIME_BEFORE_ZERO_MIN");
		
		START_ANYTIME_BETWEEN.getPoint().setEndpoint(Timepoint.START);
		START_ANYTIME_BETWEEN.setEarliest(TIME_A);
		START_ANYTIME_BETWEEN.setLatest(TIME_B);
		START_ANYTIME_BETWEEN.setRationale("START_ANYTIME_BETWEEN");
		
		START_EXACTLY_AT.getPoint().setEndpoint(Timepoint.START);
		START_EXACTLY_AT.setEarliest(TIME_A);
		START_EXACTLY_AT.setLatest(TIME_A);
		START_EXACTLY_AT.setRationale("START_EXACTLY_AT");
		
		END_ANYTIME_AFTER.getPoint().setEndpoint(Timepoint.END);
		END_ANYTIME_AFTER.setEarliest(TIME_B);
		END_ANYTIME_AFTER.setRationale("END_ANYTIME_AFTER");
		
		END_ANYTIME_BEFORE.getPoint().setEndpoint(Timepoint.END);
		END_ANYTIME_BEFORE.setLatest(TIME_B);
		END_ANYTIME_BEFORE.setRationale("END_ANYTIME_BEFORE");
		
		END_ANYTIME_BEFORE_ZERO_MIN.getPoint().setEndpoint(Timepoint.END);
		END_ANYTIME_BEFORE_ZERO_MIN.setEarliest(TIME_ZERO);
		END_ANYTIME_BEFORE_ZERO_MIN.setLatest(TIME_B);
		END_ANYTIME_BEFORE_ZERO_MIN.setRationale("END_ANYTIME_BEFORE_ZERO_MIN");
		
		END_EXACTLY_AT.getPoint().setEndpoint(Timepoint.END);
		END_EXACTLY_AT.setEarliest(TIME_A);
		END_EXACTLY_AT.setLatest(TIME_A);
		END_EXACTLY_AT.setRationale("END_EXACTLY_AT");
		
		END_ANYTIME_BETWEEN.getPoint().setEndpoint(Timepoint.END);
		END_ANYTIME_BETWEEN.setEarliest(TIME_A);
		END_ANYTIME_BETWEEN.setLatest(TIME_B);
		END_ANYTIME_BETWEEN.setRationale("END_ANYTIME_BETWEEN");
	}
	
	public void testEditing() throws ExecutionException {
		assertEditingResult(START_ANYTIME_AFTER, Timepoint.START);
		assertEditingResult(START_EXACTLY_AT, Timepoint.START); // PIN
		assertEditingResult(START_ANYTIME_BETWEEN, Timepoint.START);
		assertEditingResult(START_ANYTIME_BEFORE, Timepoint.START);
		assertEditingResult(START_ANYTIME_BEFORE_ZERO_MIN, Timepoint.START);
		assertEditingResult(END_ANYTIME_BEFORE, Timepoint.END);
		assertEditingResult(END_ANYTIME_BEFORE_ZERO_MIN, Timepoint.END);
		assertEditingResult(END_ANYTIME_AFTER, Timepoint.END);
		assertEditingResult(END_ANYTIME_BETWEEN, Timepoint.END);
		assertEditingResult(END_EXACTLY_AT, Timepoint.END);
	}
	
	public void assertEditingResult(PeriodicTemporalConstraint constraint, Timepoint timepoint) throws ExecutionException {
		TemporalBoundColumn column = new TemporalBoundColumn(TEST_PROVIDER, timepoint);
		EActivity a = constructBoundedActivity(constraint);
		ConstraintsMember originalFacet = column.getFacet(a);
		String originalText = column.getText(originalFacet);

		Amount<Duration> offset = ConstraintUtils.getPeriodicConstraintOffset(new Date(TIME));
		Amount<Duration> oneHour = TimeOfDayUtils.getOffset(1, 0, 0);
		offset = offset.plus(oneHour);
		String laterDateString = new TimeOfDayFormat().format(offset);
		
		column.modify(originalFacet, laterDateString, undoContext);
		
		ConstraintsMember constraintsMember = a.getMember(ConstraintsMember.class, true);
		Set<PeriodicTemporalConstraint> bounds = column.getTimepointPeriodicTemporalConstraints(constraintsMember);
		assertEquals("Number mismatch in bounds count after editing", 1, bounds.size());
		ConstraintsMember changedFacet = column.getFacet(a);
		assertEquals("Column text not equal", laterDateString, column.getText(changedFacet));
		
		IUndoableOperation op = OperationHistoryFactory.getOperationHistory().getUndoOperation(undoContext);
		assertNotNull("Null operation found for "+constraint.getRationale(), op);
		
		OperationHistoryFactory.getOperationHistory().undo(undoContext, null, null);
		
		ConstraintsMember finalFacet = column.getFacet(a);
		assertEquals("Error in undo operation", originalText, column.getText(finalFacet));
	}

	private EActivity constructBoundedActivity(PeriodicTemporalConstraint constraint) {
		EActivity activity = PlanFactory.getInstance().createActivityInstance();
		constraint.getPoint().setElement(activity);
		ConstraintsMember constraintsMember = activity.getMember(ConstraintsMember.class, true);
		constraintsMember.getPeriodicTemporalConstraints().add(constraint);
		return activity;
	}
	
	public void assertDeleteConstraint() {
		assertDeleteConstraint(START_ANYTIME_AFTER, Timepoint.START);
		assertDeleteConstraint(START_EXACTLY_AT, Timepoint.START);
		assertDeleteConstraint(START_ANYTIME_BETWEEN, Timepoint.START);
		assertDeleteConstraint(START_ANYTIME_BEFORE, Timepoint.START);
		assertDeleteConstraint(START_ANYTIME_BEFORE_ZERO_MIN, Timepoint.START);
		assertDeleteConstraint(END_ANYTIME_BEFORE, Timepoint.END);
		assertDeleteConstraint(END_ANYTIME_BEFORE_ZERO_MIN, Timepoint.END);
		assertDeleteConstraint(END_ANYTIME_AFTER, Timepoint.END);
		assertDeleteConstraint(END_ANYTIME_BETWEEN, Timepoint.END);
		assertDeleteConstraint(END_EXACTLY_AT, Timepoint.END);
	}
	
	public void assertDeleteConstraint(PeriodicTemporalConstraint constraint, Timepoint timepoint) {
		TemporalBoundColumn column = new TemporalBoundColumn(TEST_PROVIDER, timepoint);
		EActivity a = constructBoundedActivity(constraint);
		ConstraintsMember originalFacet = column.getFacet(a);
		String originalText = column.getText(originalFacet);
		column.modify(originalFacet, "", undoContext);
		
		ConstraintsMember constraintsMember = a.getMember(ConstraintsMember.class, true);
		List<PeriodicTemporalConstraint> bounds = constraintsMember.getPeriodicTemporalConstraints();
		assertEquals("Number mismatch in bounds count after editing", 0, bounds.size());
		ConstraintsMember changedFacet = column.getFacet(a);
		assertEquals("Column text not equal", column.getText(changedFacet), "");
		
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.undo(undoContext, null, null);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		ConstraintsMember finalFacet = column.getFacet(a);
		assertEquals("Error in undo operation", originalText, column.getText(finalFacet));
	}
	
}
