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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.operations.ClipboardPasteOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.constraints.PinUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.jscience.physics.amount.Amount;

public class TestTimepointConstraintTransferExtension extends TestCase {

	private static final int NUMBER_OF_BOUNDS = 1;
	private static final Date DATE = MissionConstants.getInstance().getMissionStartTime();
	private static final Date OFFSET_DATE = MissionCalendarUtils.offset(DATE, Calendar.DAY_OF_YEAR, 1);
	private static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	private static final PlanStructureModifier PLAN_STRUCTURE_MODIFIER = PlanStructureModifier.INSTANCE;
	
	private EPlan plan = null;
	private EActivityGroup group = null;
	private EActivityGroup group2 = null;
	private EActivity activity = null; 
	
	private PeriodicTemporalConstraint pinBound = null;
//	private HMSTemporalBound hmsBound = null;

	@Override
	protected void setUp() throws Exception {
		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		
		group = PLAN_FACTORY.createActivityGroup(plan);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				constructPlan();
			}
		});
	}

	private void constructPlan() {
		plan.getChildren().add(group);
		group.getMember(TemporalMember.class).setStartTime(DATE);
		
		activity = PLAN_FACTORY.createActivity(DictionaryFactory.eINSTANCE.createEActivityDef("operation", "test"), group);
		activity.getMember(TemporalMember.class).setStartTime(DATE);
		
		TransactionUtils.writing(activity, new Runnable() {
			@Override
			public void run() {
				Amount<Duration> offset = ConstraintUtils.getPeriodicConstraintOffset(DATE);
				pinBound = PinUtils.createPinConstraint(activity, offset);
				ConstraintUtils.attachConstraint(pinBound);
			}
		});
		
		group.getChildren().add(activity);
		
		assertEquals(1, group.getChildren().size());
		EPlanChild activity2 = group.getChildren().get(0);
		assertEquals(NUMBER_OF_BOUNDS, activity2.getMember(ConstraintsMember.class, true).getPeriodicTemporalConstraints().size());
	
		// make group2 that is one day later than group
		group2 = PLAN_FACTORY.createActivityGroup(plan);
		plan.getChildren().add(group2);
		group2.getMember(TemporalMember.class).setStartTime(OFFSET_DATE);
	}
	
	@Override
	protected void tearDown() throws Exception {
		WrapperUtils.dispose(plan);
		plan = null;
		group = null;
		group2 = null;
		activity = null; 
	}
	
	public void testCopyPaste() {
		copy();
		paste();
		assertDestinationTemporalBoundOffset(0);
	}
	
	public void testCutPaste() {
		cut();
		paste();
		assertDestinationTemporalBoundOffset(0);
	}
	
	/*
	 * Assert helpers
	 */
	private void assertDestinationTemporalBoundOffset(long offset) {
		assertEquals(1, group2.getChildren().size());
		EPlanChild activity = group2.getChildren().get(0);
		List<PeriodicTemporalConstraint> bounds = ConstraintUtils.getPeriodicConstraints(activity, true);
		assertEquals(NUMBER_OF_BOUNDS, bounds.size());
//		long day = (long) (24*60*60*1000 * MissionConstants.getInstance().getEarthSecondsPerLocalSeconds());
		for (PeriodicTemporalConstraint bound : bounds) {
			if (PinUtils.isPinConstraint(bound)) {
				assertEquals(pinBound.getEarliest(), bound.getEarliest());
				assertEquals(pinBound.getLatest(), bound.getLatest());
			}
			assertEquals(pinBound.getPoint().getEndpoint(), bound.getPoint().getEndpoint());
		}
	}

	/*
	 * Utilitity methods
	 */
	
	private void cut() {
		IStructuredSelection selection = new StructuredSelection(activity);
		ITransferable transferable = PLAN_STRUCTURE_MODIFIER.getTransferable(selection);
		execute(new PlanClipboardCutOperation(transferable, PLAN_STRUCTURE_MODIFIER), activity);
		assertEquals(0, group.getChildren().size());
	}
	
	private void copy() {
		IStructuredSelection selection = new StructuredSelection(activity);
		ITransferable transferable = PLAN_STRUCTURE_MODIFIER.getTransferable(selection);
		execute(new ClipboardCopyOperation(transferable, PLAN_STRUCTURE_MODIFIER), activity);
		assertEquals(1, group.getChildren().size());
		EPlanChild activity = group.getChildren().get(0);
		ConstraintsMember constraintsMember = activity.getMember(ConstraintsMember.class, true);
		assertEquals(NUMBER_OF_BOUNDS, constraintsMember.getPeriodicTemporalConstraints().size());
	}
	
	private void paste() {
		IStructuredSelection targetSelection = new StructuredSelection(group2);
		execute(new ClipboardPasteOperation(targetSelection, PLAN_STRUCTURE_MODIFIER), group2);
	}
	
	private void execute(IUndoableOperation operation, EPlanElement element) {
		operation.addContext(TransactionUtils.getUndoContext(element));
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(operation, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}
	}
	
}
