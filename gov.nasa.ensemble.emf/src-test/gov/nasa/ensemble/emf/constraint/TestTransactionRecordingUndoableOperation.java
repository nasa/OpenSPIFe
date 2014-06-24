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
package gov.nasa.ensemble.emf.constraint;

import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.emf.util.TransactionRecordingUndoableOperation;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.junit.Assert;
import org.junit.Test;


public class TestTransactionRecordingUndoableOperation extends Assert {

	private static String TEST_CLASS_NAME = "TestClass";
	private static String TEST_CLASS_NAME2 = "TestClass2";
	private static String TEST_CLASS_NAME3 = "TestClass3";
	private static IOperationHistory OPERATION_HISTORY = OperationHistoryFactory.getOperationHistory();

	@Test
	public void test() throws ExecutionException {
		final Resource resource = new XMIResourceImpl();
		final EClass object = EcoreFactory.eINSTANCE.createEClass();
		resource.getContents().add(object);
		final EClass object2 = EcoreFactory.eINSTANCE.createEClass();
		final EClass object3 = EcoreFactory.eINSTANCE.createEClass();
		
		EditingDomain domain = TransactionUtils.createTransactionEditingDomain();
		domain.getResourceSet().getResources().add(resource);
		
		final EAttribute feature1 = EcoreFactory.eINSTANCE.createEAttribute();
		final EAttribute feature2 = EcoreFactory.eINSTANCE.createEAttribute();
		final EAttribute feature3 = EcoreFactory.eINSTANCE.createEAttribute();
		final EAttribute feature4 = EcoreFactory.eINSTANCE.createEAttribute();
		
		object2.getEStructuralFeatures().add(feature4);
		resource.getContents().add(object2);
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				object.setName(TEST_CLASS_NAME);
				object2.setName(TEST_CLASS_NAME2);
				resource.getContents().add(object3);
				object3.setName(TEST_CLASS_NAME3);
				object.getEStructuralFeatures().add(feature2);
				object.getEStructuralFeatures().add(0, feature1);
				object3.getEStructuralFeatures().add(feature3);
				object2.getEStructuralFeatures().remove(feature4);
				assertDone("in runnable", resource, object, object2, object3, feature1, feature2, feature3);
			}
			
		};
		TransactionRecordingUndoableOperation op = new TransactionRecordingUndoableOperation("Test operation", domain, runnable);
		IUndoContext undoContext = EMFUtils.getUndoContext(object);
		op.addContext(undoContext);
		assertUndone("before execute", resource, object, object2, object3, feature4);
		OPERATION_HISTORY.execute(op, null, null);
		assertDone("after execute", resource, object, object2, object3, feature1, feature2, feature3);
		
		for (int i = 0 ; i < 5 ; i++) {
			OPERATION_HISTORY.undo(undoContext, null, null);
			assertUndone("undo i=" + 1, resource, object, object2, object3, feature4);
			
			OPERATION_HISTORY.redo(undoContext, null, null);
			assertDone("redo i=" + i, resource, object, object2, object3, feature1, feature2, feature3);
		}
	}

	private void assertUndone(String when, Resource resource, EClass object, EClass object2, EClass object3, EAttribute feature4) {
		assertEquals(when, null, object.getName());
		assertEquals(when, null, object2.getName());
		assertEquals(when, null, object3.getName());
		assertTrue(when, resource.getContents().contains(object2));
		assertFalse(when, resource.getContents().contains(object3));
		assertTrue(when, object.getEAttributes().isEmpty());
		assertTrue(when, object3.getEAttributes().isEmpty());
		assertEquals(when, object2.getEAttributes().get(0), feature4);
	}

	private void assertDone(String when, Resource resource, EClass object, EClass object2,
						    EClass object3, EAttribute feature1, EAttribute feature2, EAttribute feature3) {
		assertEquals(when, TEST_CLASS_NAME, object.getName());
		assertEquals(when, TEST_CLASS_NAME2, object2.getName());
		assertEquals(when, TEST_CLASS_NAME3, object3.getName());
		assertTrue(when, resource.getContents().contains(object2));
		assertTrue(when, resource.getContents().contains(object3));
		assertEquals(when, object.getEAttributes().get(0), feature1);
		assertEquals(when, object.getEAttributes().get(1), feature2);
		assertEquals(when, object3.getEAttributes().get(0), feature3);
		assertTrue(when, object2.getEStructuralFeatures().isEmpty());
	}
	
}
