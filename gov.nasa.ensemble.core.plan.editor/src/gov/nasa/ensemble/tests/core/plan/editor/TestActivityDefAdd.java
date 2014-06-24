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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityDefTransferable;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestActivityDefAdd extends UndoableOperationTestCase {

	public void testActivityDefDrop() {
		OperationTestPlanRecord plan = new OperationTestPlanRecord();
		String initialState = EMFUtils.convertToXML(plan.plan);
		List<? extends EActivity> allActivities = EPlanUtils.getActivities(plan.plan);
		for (EActivity activity : allActivities) {
			EPlanElement[] targetElements = new EPlanElement[] { activity };
			testActivityDefDrop(plan, targetElements);
			assertEquals(initialState, EMFUtils.convertToXML(plan.plan));
		}
		WrapperUtils.dispose(plan.plan);
	}
	
	private void testActivityDefDrop(OperationTestPlanRecord plan, EPlanElement[] targetElements) {
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = new ActivityDefTransferable(Collections.singletonList(OperationTestPlanRecord.def));
		IStructureLocation location = modifier.getInsertionLocation(transferable, targetSelection, InsertionSemantics.ON);
		IUndoableOperation add = new PlanAddOperation(transferable, modifier, location);
		final EActivity target = (EActivity)targetElements[targetElements.length - 1];
		final int position = target.getListPosition();
		final EPlanElement group = target.getParent();
		final EPlanChild next; 
		if (group.getChildren().size() > position + 1) {
			next = group.getChildren().get(position + 1);
		} else {
			next = null;
		}
		testUndoableOperation(plan.plan, add, new Runnable() {
			@Override
			public void run() {
				assertEquals(group, target.getParent());
				assertEquals(position, target.getListPosition());
				assertTrue(group.getChildren().size() > position);
				EPlanChild activity = group.getChildren().get(position + 1);
				assertTrue(activity instanceof EActivity);
				assertEquals(OperationTestPlanRecord.def.getName(), ((EActivity)activity).getType());
				if (next != null) {
					assertEquals(group, next.getParent());
					assertEquals(position + 2, next.getListPosition());
				}
			}
		});
	}

}
