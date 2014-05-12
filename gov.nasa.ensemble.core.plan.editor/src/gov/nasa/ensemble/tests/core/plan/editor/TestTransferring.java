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

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.SimpleByteArrayTransferProvider;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestTransferring extends TestCase {

	private static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	private static final PlanStructureModifier PLAN_STRUCTURE_MODIFIER = PlanStructureModifier.INSTANCE;
	public static final EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef("transferring", "test");

	public void testTransferringBasicPlan() throws IOException {
		EPlan plan = createBasicTestPlan();
		try {
			testTransferringPlan(plan);
		} finally {
			WrapperUtils.dispose(plan);
		}
	}
	
	public void testTransferringPlan(EPlan plan) throws IOException {
		ISelection planSelection = new StructuredSelection(plan);
		PlanTransferable outputTransferable = PLAN_STRUCTURE_MODIFIER.getTransferable(planSelection);
		PlanContainerTransferProvider transferProvider = new PlanContainerTransferProvider();
		assertTrue("PlanContainerTransferProvider should be able to pack a transferable that is a Plan.", transferProvider.canPack(outputTransferable));
		File tempFile = FileUtilities.createTempFile("plan_transfer",".bin");
		ITransferable inputTransferable;
		try {
	        writeTransferableToFile(tempFile, outputTransferable, transferProvider);
	        inputTransferable = readTransferableFromFile(tempFile, transferProvider);
        } finally {
	        tempFile.delete();
        }
		assertTrue("the input transferable should be a PlanTransferable", inputTransferable instanceof PlanTransferable);
		PlanTransferable planTransferable = (PlanTransferable)inputTransferable;
		List<? extends EPlanElement> planElements = planTransferable.getPlanElements();
		try {
			assertTrue("there should be exactly one element", planElements.size() == 1);
			EPlanElement planElement = planElements.get(0);
			assertTrue("the plan element should be a Plan", planElement instanceof EPlan);
			WrapperUtils.assertPlanDeepEquals(plan, (EPlan)planElement);
		} finally {
			for (EPlanElement element : planElements) {
				if (element instanceof EPlan) {
					EPlan plan2 = (EPlan) element;
					WrapperUtils.dispose(plan2);
				}
			}
		}
	}

	public static EPlan createBasicTestPlan() {
		EPlan plan = PLAN_FACTORY.createPlan("TransferTestPlan");
		EActivityGroup group1 = PLAN_FACTORY.createActivityGroup(plan);
		group1.setName("Group 1");
		EActivity activity1_1 = PLAN_FACTORY.createActivity(def, group1);
		activity1_1.setName("activity1_1");
		group1.getChildren().add(activity1_1);
		plan.getChildren().add(group1);
		group1.getChildren().add(PLAN_FACTORY.createActivity(def, group1));
		EActivityGroup group2 = PLAN_FACTORY.createActivityGroup(plan);
		group2.setName("Group 2");
		plan.getChildren().add(group2);
		EActivity activity2_1 = PLAN_FACTORY.createActivityInstance();
		activity2_1.setName("activity2_1");
		group2.getChildren().add(activity2_1);
		return plan;
	}
	
	private static void writeTransferableToFile(File file, PlanTransferable transferable, SimpleByteArrayTransferProvider transferProvider)
			throws FileNotFoundException, IOException {
		byte[] outputObject = transferProvider.packTransferObject(transferable);
		FileOutputStream output = new FileOutputStream(file);
		output.write(outputObject);
		output.flush();
		output.close();
	}

	private static ITransferable readTransferableFromFile(File file, SimpleByteArrayTransferProvider transferProvider)
			throws FileNotFoundException, IOException {
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Length of file: " + file.getName() + " longer than Integer.MAX_VALUE: " + length);
		}
		byte[] inputObject = new byte[(int)length];
		FileInputStream input = new FileInputStream(file);
		assertTrue("all the bytes should be available", input.available() == length);
		assertTrue("all the bytes should be read", input.read(inputObject) == length);
		ITransferable inputTransferable = transferProvider.unpackTransferObject(inputObject);
		return inputTransferable;
	}
	
}
