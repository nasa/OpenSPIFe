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

import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestTemporalTransferableExtension extends UndoableOperationTestCase {

	private static final PlanFactory 			PLAN_FACTORY = PlanFactory.getInstance();
	private static final PlanStructureModifier 	PLAN_STRUCTURE_MODIFIER = PlanStructureModifier.INSTANCE;
	
	private static final Date 					SRC_ACTIVITY_1_DATE = MissionConstants.getInstance().getMissionStartTime(); // Mission birthday
	private static final long 					SRC_ACTIVITY_2_OFFSET = (long) (60*1000*MissionConstants.getInstance().getEarthSecondsPerLocalSeconds());
	
	private static final Date 					TARGET_DATE = MissionCalendarUtils.offset(SRC_ACTIVITY_1_DATE, Calendar.DAY_OF_YEAR, 1);
	
	private static final IStringifier<Date> 	DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	private EPlan srcPlan = null;
	private EPlan dstPlan = null;
	private EActivityGroup dstGroup = null;
	
	private List<EActivity> activitiesToCopy = null;
	
	@Override
	protected void setUp() throws Exception {
		activitiesToCopy = new ArrayList<EActivity>();
		srcPlan = PLAN_FACTORY.createPlan("SRC_PLAN");
		TransactionUtils.writing(srcPlan, new Runnable() {
			@Override
			public void run() {
				constructSrcPlan();
			}
		});
		dstPlan = PLAN_FACTORY.createPlan("DST_PLAN");
		TransactionUtils.writing(dstPlan, new Runnable() {
			@Override
			public void run() {
				constructDstPlan();
			}
		});
		PlanElementXMLUtils.setUpMap();
	}

	private void constructSrcPlan() {
		srcPlan.getMember(TemporalMember.class).setStartTime(SRC_ACTIVITY_1_DATE);
		EActivityGroup group = PLAN_FACTORY.createActivityGroup(srcPlan);
		srcPlan.getChildren().add(group);
		EActivity activityToCopy = PLAN_FACTORY.createActivity(DictionaryFactory.eINSTANCE.createEActivityDef("operation", "test"), group);
		group.getChildren().add(activityToCopy);
		activityToCopy.getMember(TemporalMember.class).setStartTime(SRC_ACTIVITY_1_DATE);
		activitiesToCopy.add(activityToCopy);
		activityToCopy = PLAN_FACTORY.createActivity(DictionaryFactory.eINSTANCE.createEActivityDef("operation", "test"), group);
		group.getChildren().add(activityToCopy);
		activityToCopy.getMember(TemporalMember.class).setStartTime(DateUtils.add(SRC_ACTIVITY_1_DATE, SRC_ACTIVITY_2_OFFSET));
		activitiesToCopy.add(activityToCopy);
	}

	private void constructDstPlan() {
		dstPlan.getMember(TemporalMember.class).setStartTime(TARGET_DATE);
		dstGroup= PLAN_FACTORY.createActivityGroup(dstPlan);
		dstGroup.getMember(TemporalMember.class).setStartTime(TARGET_DATE);
		dstPlan.getChildren().add(dstGroup);
	}
	
	@Override
	protected void tearDown() throws Exception {
		WrapperUtils.dispose(srcPlan);
		WrapperUtils.dispose(dstPlan);
		PlanElementXMLUtils.tearDownMap();
	}

	public void testCopyPaste() {
		copy();
		paste();
		if (TemporalTransferableExtension.isAutoshift()) {
			assertDestinationActivityDate(TARGET_DATE);
		} else {
			assertDestinationActivityDate(SRC_ACTIVITY_1_DATE);
		}
		
	}

	public void testCutPaste() {
		cut();
		paste();
		if (TemporalTransferableExtension.isAutoshift()) {
			assertDestinationActivityDate(TARGET_DATE);
		} else {
			assertDestinationActivityDate(SRC_ACTIVITY_1_DATE);
		}
	}

	/*
	 * Utility functions
	 */

	private void cut() {
		IStructuredSelection selection = new StructuredSelection(activitiesToCopy);
		ITransferable transferable = PLAN_STRUCTURE_MODIFIER.getTransferable(selection);
		IUndoableOperation cut = new PlanClipboardCutOperation(transferable, PLAN_STRUCTURE_MODIFIER);
		execute(cut, srcPlan);
	}

	private void copy() {
		IStructuredSelection selection = new StructuredSelection(activitiesToCopy);
		ITransferable transferable = PLAN_STRUCTURE_MODIFIER.getTransferable(selection);
		IUndoableOperation copy = new ClipboardCopyOperation(transferable, PLAN_STRUCTURE_MODIFIER);
		execute(copy, srcPlan);
	}

	private void paste() {
		IStructuredSelection targetSelection = new StructuredSelection(dstGroup);
		IUndoableOperation paste = new PlanClipboardPasteOperation(targetSelection, PLAN_STRUCTURE_MODIFIER);
		execute(paste, dstPlan);
	}

	private void assertDestinationActivityDate(Date expectedDate) {
		assertEquals(1, dstPlan.getChildren().size());
		List<? extends EPlanChild> activities = dstPlan.getChildren().get(0).getChildren();
		assertEquals(2, activities.size());
		assertActivityDate(activities.get(0), expectedDate);
		assertActivityDate(activities.get(1), DateUtils.add(expectedDate, SRC_ACTIVITY_2_OFFSET));
	}

	private void assertActivityDate(EPlanChild activity, Date expectedDate) {
		Date actualDate = activity.getMember(TemporalMember.class).getStartTime();
		int threshold = 250;
		String expectedDateString = DATE_STRINGIFIER.getDisplayString(expectedDate);
		assertNotNull("Expected <"+expectedDateString+">, received null.", actualDate);
		String actualDateString = DATE_STRINGIFIER.getDisplayString(actualDate);
		boolean condition = threshold > Math.abs(DateUtils.subtract(expectedDate, actualDate));
		assertTrue("Expected <"+expectedDateString+">, received <"+actualDateString+">", condition);
	}
	
	private void execute(IUndoableOperation operation, EPlan plan) {
		operation.addContext(TransactionUtils.getUndoContext(plan));
		// perform the operation
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(operation, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}
	}
	
}
