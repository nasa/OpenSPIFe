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
//package gov.nasa.ensemble.core.plan.temporal;
//
//import gov.nasa.ensemble.core.activityDictionary.AttributeDef;
//import gov.nasa.ensemble.common.CommonUtils;
//import gov.nasa.ensemble.common.time.DateUtils;
//import gov.nasa.ensemble.core.plan.Parameter;
//import gov.nasa.ensemble.core.plan.PlanElement;
//import gov.nasa.ensemble.core.plan.tree.PlanTreeModel;
//import gov.nasa.ensemble.tests.core.plan.OperationTestPlan;
//import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;
//
//import java.util.Date;
//
//import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.core.commands.operations.IOperationHistory;
//import org.eclipse.core.commands.operations.IUndoableOperation;
//import org.eclipse.core.commands.operations.OperationHistoryFactory;
//
//public class TestPreferredTimesOperation extends UndoableOperationTestCase {
//
//	/*
//	 * Convenience class ensures that the test plan has all activities
//	 * at their preferred placements.
//	 */
//	public class PreferredTimesOperationTestPlan extends OperationTestPlan {
//		public PreferredTimesOperationTestPlan() {
//			if (!isAtPreferredTimes(this)) {
//				IUndoableOperation operation = new PreferredTimesOperation("initialization-operation", this); 
//				try {
//					IOperationHistory history = OperationHistoryFactory.getOperationHistory();
//					history.execute(operation, null, null);
//				} catch (ExecutionException ee) {
//					fail("failed to execute");
//				}
//			}			
//		}
//	}
//	
//	public void testInitialPlan() {
//		final OperationTestPlan plan = new OperationTestPlan();
//		if (!isAtPreferredTimes(plan)) {
//			IUndoableOperation operation = new PreferredTimesOperation("operation", plan); 
//			testUndoableOperation(plan, operation, new Runnable() {
//				public void run() {
//					assertAtPreferredTimes(plan);
//				}
//			});
//		}
//	}
//	
//	public void testMoveActivity() {
//		final OperationTestPlan plan = new PreferredTimesOperationTestPlan();
//		
//		Date movedStart = DateUtils.add(TemporalMember.get(plan.activity1_2).getCurrentStart(), 90000L);
//		plan.activity1_2.setNamedParameterObject(AttributeDef.ATTRIBUTE_START_TIME, "DATE", movedStart);
//		final Date date = TemporalMember.get(plan.activity1_2).getCurrentStart();
//		
//		IUndoableOperation operation = new PreferredTimesOperation("operation", plan); 
//		testUndoableOperation(plan, operation, new Runnable() {
//			public void run() {
//				assertAtPreferredTimes(plan);
//				assertEquals(date, TemporalMember.get(plan.activity1_2).getUserStart());
//			}
//		});
//		
//	}
//
//	public void testMoveActivityGroup() {
//		final OperationTestPlan plan = new PreferredTimesOperationTestPlan();
//		
//		Date movedStart = DateUtils.add(TemporalMember.get(plan.group2).getCurrentStart(), 90000L);
//		plan.group2.setNamedParameterObject(AttributeDef.ATTRIBUTE_START_TIME, "DATE", movedStart);
//		final Date date = TemporalMember.get(plan.group2).getCurrentStart();
//		
//		IUndoableOperation operation = new PreferredTimesOperation("operation", plan); 
//		testUndoableOperation(plan, operation, new Runnable() {
//			public void run() {
//				assertAtPreferredTimes(plan);
//				assertEquals(date, TemporalMember.get(plan.group2).getUserStart());
//			}
//		});
//		
//	}
//	
//	@Override
//	protected String getParameterStateString(Parameter parameter) {
//		if (CommonUtils.equals(parameter.getName(), AttributeDef.ATTRIBUTE_START_TIME)) {
//			return "\t" + parameter.getName() + "\n"; // don't include the start time in the state
//		}
//		return "\t" + parameter.getName() + "=" + parameter.getValueString() + "\n";
//	}
//
//	/*
//	 * Utility functions
//	 */
//	
//	/**
//	 * Iterates over all plan elements and ensures that they are at their
//	 * preferred times (if they have them specified).  This is equivalent
//	 * to assertTrue(isAtPreferredTimes(element)) but will be more
//	 * informative if the assertion fails.
//	 * 
//	 * @param element
//	 */
//	private void assertAtPreferredTimes(PlanElement element) {
//		TemporalMember member = TemporalMember.get(element);
//		Date userStart = member.getUserStart();
//		Date currentStart = member.getCurrentStart();
//		if (userStart != null) {
//			assertEquals(userStart, currentStart);
//		}
//		for (PlanElement child : PlanTreeModel.getInstance().getChildren(element)) {
//			assertAtPreferredTimes(child);
//		}		
//	}
//	
//	/**
//	 * Iterates over all plan elements and returns true if they are at
//	 * their preferred times (if they have them specified).
//	 * 
//	 * @param element
//	 * @return
//	 */
//	private boolean isAtPreferredTimes(PlanElement element) {
//		TemporalMember member = TemporalMember.get(element);
//		Date userStart = member.getUserStart();
//		Date currentStart = member.getCurrentStart();
//		Date suggestedStart = member.getSuggestedStart();
//		if (userStart != null && !userStart.equals(currentStart)) {
//			return false;
//		}
//		if (!CommonUtils.equals(userStart, suggestedStart)) {
//			return false;		}
//		
//		for (PlanElement child : PlanTreeModel.getInstance().getChildren(element)) {
//			if (!isAtPreferredTimes(child)) {
//				return false;
//			}
//		}
//		return true;
//	}
//	
//}
