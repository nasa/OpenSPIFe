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
package gov.nasa.ensemble.emf.patch;

import static org.junit.Assert.*;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.model.test.PatchTestFactory;
import gov.nasa.ensemble.emf.model.test.PatchTestModel;
import gov.nasa.ensemble.emf.model.test.PatchTestPackage;
import gov.nasa.ensemble.emf.model.test.SomeTestClass;
import gov.nasa.ensemble.emf.model.test.SomeTestClassWithID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public abstract class AbstractPatchTestWithAllFeatureTypes {

	protected static final Random RANDOM_ID_GENERATOR = new Random();
	private PatchTestModel model;
	private ResourceSetImpl resourceSet;
	
	protected boolean showDebuggingOutput() {
		return false;
	}
	
	/**
	 * The Patch can be used locally in memory and also after being serialized and deserialized.
	 * @return
	 */
	protected abstract boolean serializePatch();
	
	 @Rule public TestName name = new TestName();
	
	@Before
	public void setup() {
		resourceSet = new ResourceSetImpl();
		model = PatchTestFactory.eINSTANCE.createPatchTestModel();
		model.setId("ID");
		ResourceImpl testModelResource = new XMLResourceImpl(URI.createURI("http://model"));
		testModelResource.getContents().add(model);
		resourceSet.getResources().add(testModelResource);
		if (showDebuggingOutput()) {
			System.out.println("TEST: " + name.getMethodName());
		}
	}
	
	@After
	public void teardown() {
		String serialize = serialize(model);
		if (showDebuggingOutput()) {
			System.out.println("---------------------------------\nMODEL:\n"  + serialize 
					+ "===================================================================================================");
		}
	}
	
	protected void assertPatchOperationExecuteUndoRedo(Patch patch, EStructuralFeature feature, Object original, Object newValue) throws Throwable {
		PatchOperation operation = new PatchOperation(model, patch);

		//execute
		operation.execute();
		assertEquality("Error on execute.", feature, newValue, model.eGet(feature));
		
		//undo
		operation.undo();
		assertEquality("Error on undo.", feature, original, model.eGet(feature));
		
		//redo
		operation.redo();
		assertEquality("Error on redo.", feature, newValue, model.eGet(feature));
	}
	
	protected void assertEquality(String message, EStructuralFeature feature, Object expected, Object actual) {
		if (feature instanceof EReference && ((EReference) feature).isContainment()) {
			if (feature.isMany()) {
				List<SomeTestClass> listActual = (List<SomeTestClass>) actual;
				List<SomeTestClass> listExpected = (List<SomeTestClass>) expected;
				assertEquals(message, listExpected.size(), listActual.size());
				for(int i = 0; i < listExpected.size(); i++) {
					assertEquals(message, listExpected.get(i).getAttribute(), listActual.get(i).getAttribute());
				}
			} else {
				if (expected == null) {
					assertNull(message, actual);
				} else {
					assertEquals(message, ((SomeTestClass) expected).getAttribute(), ((SomeTestClass) actual).getAttribute());
				}
			}
		} else {
			assertEquals(message, expected, actual);
		}
	}
	
	@Test
	public void test_oneAttribute_modify() throws Throwable {
		String newValue = "NEW ATTRIBUTE VALUE!";
		PatchBuilder builder = new PatchBuilder();
		EStructuralFeature feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__ONE_ATTRIBUTE;
		builder.modify(model, feature, newValue);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		assertPatchOperationExecuteUndoRedo(patch, feature, null, newValue);
	}
	
	@Test
	public void test_multiAttribute_modify() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		List<Integer> newValue = Arrays.asList(10, 9, 8, 7, 6);
		PatchBuilder builder = new PatchBuilder();
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.modify(model, feature, newValue);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_multiAttribute_add() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		Integer objectToAdd = 99;
		PatchBuilder builder = new PatchBuilder();
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.add(model, feature, objectToAdd);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.add(objectToAdd);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_multiAttribute_addWithIndex() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		int index = 1;
		Integer objectToAdd = 99;
		PatchBuilder builder = new PatchBuilder();
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.add(model, feature, objectToAdd, index);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.add(index, objectToAdd);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_multiAttribute_addAll() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		PatchBuilder builder = new PatchBuilder();
		List<Integer> toAdd = Arrays.asList(50, 51, 52);
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.addAll(model, feature, toAdd);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.addAll(toAdd);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}

	@Test
	public void test_multiAttribute_addAllWithIndex() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		PatchBuilder builder = new PatchBuilder();
		int index = 1;
		List<Integer> toAdd = Arrays.asList(50, 51, 52);
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.addAll(model, feature, toAdd, index);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.addAll(index, toAdd);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}

	@Test
	public void test_multiAttribute_remove() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		PatchBuilder builder = new PatchBuilder();
		int index = 1;
		Integer toRemove = original.get(index);
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.remove(model, feature, toRemove);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.remove(index);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_multiAttribute_removeByIndex() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		PatchBuilder builder = new PatchBuilder();
		int index = 1;
		Integer toRemove = original.get(index);
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.remove(model, feature, toRemove, index);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		//assert results
		List<Integer> newValue = new ArrayList(original);
		newValue.remove(index);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_multiAttribute_removeAll() throws Throwable {
		List<Integer> original = setDefaultMultiAttribute();
		PatchBuilder builder = new PatchBuilder();
		List<Integer> toRemove = original.subList(1, original.size());
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.removeAll(model, feature, toRemove);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<Integer> newValue = new ArrayList(original);
		newValue.removeAll(toRemove);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_oneNonContainmentReference_modify() throws Throwable {
		SomeTestClass newValue = createSomeTestClass();
		model.setOneContainmentReference(newValue);
		PatchBuilder builder = new PatchBuilder();
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE;
		builder.modify(model, feature, newValue);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		assertPatchOperationExecuteUndoRedo(patch, feature, null, newValue);
	}
	
	@Test
	public void test_multiNonContainmentRefernce_modify() throws Throwable {
		List<SomeTestClass> newValue = new ArrayList(setDefaultMultiContainedReference());
		PatchBuilder builder = new PatchBuilder();
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES;
		builder.modify(model, feature, newValue);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		assertPatchOperationExecuteUndoRedo(patch, feature, Collections.EMPTY_LIST, newValue);
		
		//assert results
		assertEquals(newValue.size(), model.getMultiNonContainmentReferences().size());
		for (int i = 0; i < model.getMultiNonContainmentReferences().size(); i++) {
			assertEquals(newValue.get(i).getAttribute(), model.getMultiNonContainmentReferences().get(i).getAttribute());
		}
	}
	
	@Test 
	public void test_multiNonContainmentReference_add() throws Throwable {
		setDefaultMultiContainedReference();
		PatchBuilder builder = new PatchBuilder();
		SomeTestClass someTestClass = model.getMultiContainmentReference().get(0);
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES;
		builder.add(model, feature, someTestClass);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		Object original = model.getMultiNonContainmentReferences();
		List<EObject> newValue = new ArrayList();
		newValue.add(someTestClass);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
		assertEquals(1, model.getMultiNonContainmentReferences().size());
		assertEquals(someTestClass.getAttribute(), model.getMultiNonContainmentReferences().get(0).getAttribute());
	}
	
	@Test 
	public void test_multiNonContainmentReference_addAll() throws Throwable {
		setDefaultMultiContainedReference();
		PatchBuilder builder = new PatchBuilder();
		List<SomeTestClass> newValue = new ArrayList(model.getMultiContainmentReference().subList(0, 3));
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES;
		builder.addAll(model, feature, newValue);
		Patch patch = assertSerializeDeserializePatch(builder);
		assertPatchOperationExecuteUndoRedo(patch, feature, Collections.EMPTY_LIST, newValue);
	}
	
	@Test 
	public void test_multiNonContainmentReference_remove() throws Throwable {
		test_multiNonContainmentReference_add();
		PatchBuilder builder = new PatchBuilder();
		SomeTestClass someTestClass = model.getMultiContainmentReference().get(0);
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES;
		builder.remove(model, feature, someTestClass);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		EList<SomeTestClass> original = model.getMultiNonContainmentReferences();
		List<EObject> newValue = new ArrayList(original);
		newValue.remove(someTestClass);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
		assertEquals(0, model.getMultiNonContainmentReferences().size());
	}
	
	@Test 
	public void test_multiNonContainmentReference_removeAll() throws Throwable {
		test_multiNonContainmentReference_addAll();
		List<SomeTestClass> original = new ArrayList(model.getMultiNonContainmentReferences());
		PatchBuilder builder = new PatchBuilder();
		List<SomeTestClass> toRemove = new ArrayList(model.getMultiNonContainmentReferences().subList(0, 2));
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES;
		builder.removeAll(model, feature, toRemove);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List newValue = new ArrayList(original);
		newValue.removeAll(toRemove);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
	}
	
	@Test
	public void test_oneContainment_modify() throws Throwable {
		PatchBuilder builder = new PatchBuilder();
		SomeTestClass someTestClass = createSomeTestClass();
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE;
		builder.modify(model, feature, someTestClass);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		assertPatchOperationExecuteUndoRedo(patch, feature, null, someTestClass);
		assertNotNull(model.getOneContainmentReference());
		assertEquals(someTestClass.getAttribute(), model.getOneContainmentReference().getAttribute());
	}
	
	@Test
	public void test_multiContainment_add() throws Throwable {
		List<SomeTestClass> original = new ArrayList(model.getMultiContainmentReference());
		PatchBuilder builder = new PatchBuilder();
		SomeTestClass someTestClass = createSomeTestClass();
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE;
		builder.add(model, feature, someTestClass);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<SomeTestClass> newValue = new ArrayList();
		newValue.add(someTestClass);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
		assertEquals(1, model.getMultiContainmentReference().size());
		assertEquals(someTestClass.getAttribute(), model.getMultiContainmentReference().get(0).getAttribute());
	}
	
	@Test
	public void test_multiContainment_addAll() throws Throwable {
		PatchBuilder builder = new PatchBuilder();
		List<SomeTestClass> toAdd = new ArrayList();
		toAdd.add(createSomeTestClass());
		toAdd.add(createSomeTestClass());
		
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE;
		builder.addAll(model, feature, toAdd);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		List<SomeTestClass> newValue = new ArrayList();
		newValue.addAll(toAdd);
		assertPatchOperationExecuteUndoRedo(patch, feature, Collections.EMPTY_LIST, newValue);
	}
	
	@Test
	public void test_multiContainment_remove() throws Throwable {
		test_multiContainment_add();
		List<SomeTestClass> original = new ArrayList(model.getMultiContainmentReference());
		PatchBuilder builder = new PatchBuilder();
		SomeTestClass someTestClass = model.getMultiContainmentReference().get(0);
		EReference feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE;
		builder.remove(model, feature, someTestClass);
		Patch patch = assertSerializeDeserializePatch(builder);
		
		ArrayList newValue = new ArrayList(original);
		newValue.remove(someTestClass);
		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
		assertEquals(0, model.getMultiContainmentReference().size());
	}
	
//	@Test
//	public void test_multiNonContainmentRefernce_addAllRemoveAdd() {
//		setDefaultMultiContainedReference();
//		
//		//add them first
//		PatchBuilder builder = new PatchBuilder();
//		List<SomeTestClass> newValue = model.getMultiContainmentReference();
//		builder.addAll(model, PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES, newValue);
//		Patch patch = assertSerializeDeserializePatch(builder);
//		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
//		
//		//move one
//		builder = new PatchBuilder();
//		SomeTestClass objectMoving = model.getMultiNonContainmentReferences().get(0);
//		builder.remove(model, PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES, objectMoving);
//		int index = howManyReferences-1;
//		builder.add(model, PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES, objectMoving, index);
//		patch = assertSerializeDeserializePatch(builder);
//		assertPatchOperationExecuteUndoRedo(patch, feature, original, newValue);
//
//		assertEquals(howManyReferences, model.getMultiNonContainmentReferences().size());
//		assertEquals(objectMoving.getAttribute(), model.getMultiNonContainmentReferences().get(index).getAttribute());
//	}
	
	protected abstract SomeTestClass createSomeTestClass();
	
	private Patch assertSerializeDeserializePatch(PatchBuilder builder) {
		String serialized = serialize(builder.getPatch());
		assertNotNull("Failed to serialize patch. ", serialized);
		if (showDebuggingOutput()) {
			System.out.println("PATCH:\n" + serialized);
		}
		if (!serializePatch()) {
			return builder.getPatch();
		}
		Patch patch = deserializePatch(serialized);
		assertNotNull("Failed to deserialize patch: " + serialized, patch);
		return patch;
	}
	
	private Patch deserializePatch(String string) {
		EObject eObject = deserialize(string);
		if (eObject instanceof Patch) {
			return (Patch) eObject;
		}
		return null;
		
	}
	
	private EObject deserialize(String string) {
		ByteArrayInputStream bais = new ByteArrayInputStream(string.getBytes());
		XMLResourceImpl resource = new XMLResourceImpl(URI.createURI("http://patch"));
		resourceSet.getResources().add(resource);
		try {
			resource.load(bais, null);
			return resource.getContents().get(0);
		} catch (IOException e) {
			LogUtil.error(e);
		}
		return null;
	}
	
	private String serialize(EObject eObject) {
		ResourceImpl resource = new XMLResourceImpl();
		resource.getContents().add(eObject);
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
		options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
		try {
			resource.save(baos, options);
			return baos.toString();
		} catch (IOException e) {
			LogUtil.error(e);
		}
		return null;
	}
	
	private List<Integer> setDefaultMultiAttribute() {
		List<Integer> original = Arrays.asList(0, 1, 2, 3, 4, 5);
		PatchBuilder builder = new PatchBuilder();
		EAttribute feature = PatchTestPackage.Literals.PATCH_TEST_MODEL__MULTI_ATTRIBUTE;
		builder.modify(model, feature, original);
		CommonUtils.execute(new PatchOperation(model, builder.getPatch()), null);
		return original;
	}
	
	private EList<SomeTestClass> setDefaultMultiContainedReference() {
		int howManyReferences = 5;
		for (int i = 0; i < howManyReferences; i++) {
			SomeTestClass someTestObject = createSomeTestClass();
			if (someTestObject instanceof SomeTestClassWithID) {
				((SomeTestClassWithID) someTestObject).setId(Integer.toString(i));
			}
			model.getMultiContainmentReference().add(someTestObject);
		}
		return model.getMultiContainmentReference();
	}
	
}
