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
package gov.nasa.ensemble.emf.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestEMFUtils extends Assert {
	
	private static EcoreFactory FACTORY = EcoreFactory.eINSTANCE;
	private EPackage ePackage;
	private EClass eClass0;
	private EClass eClass1;
	private EReference multiRef;
	private EReference singleRef;

	@Before
	public void before() {
		ePackage = FACTORY.createEPackage();
		ePackage.setName("Package");
		
		eClass0 = FACTORY.createEClass();
		eClass0.setName("EClass0");
		ePackage.getEClassifiers().add(eClass0);

		eClass1 = FACTORY.createEClass();
		eClass1.setName("EClass1");
		ePackage.getEClassifiers().add(eClass1);
		
		singleRef = FACTORY.createEReference();
		singleRef.setName("singleRef");
		singleRef.setUpperBound(1);
		singleRef.setEType(eClass1);
		singleRef.setContainment(true);
		eClass0.getEStructuralFeatures().add(singleRef);
		
		multiRef = FACTORY.createEReference();
		multiRef.setName("multiRef");
		multiRef.setUpperBound(-1);
		multiRef.setEType(eClass0);
		multiRef.setContainment(true);
		eClass1.getEStructuralFeatures().add(multiRef);
	}
	
	@Test
	public void roundtripEDataType() {
		EDataType eDataType = EcorePackage.Literals.EDOUBLE_OBJECT;
		String string = EMFUtils.convertToString(eDataType);
		assertNotNull(string);
		EDataType eDataType2 = EMFUtils.createEDataTypeFromString(string);
		assertEquals(eDataType, eDataType2);
	}
	
	@Test
	public void inAndOutOfString() {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("Package");
		
		EClass eClass0 = EcoreFactory.eINSTANCE.createEClass();
		eClass0.setName("EClass0");
		ePackage.getEClassifiers().add(eClass0);
		
		EClass eClass1 = EcoreFactory.eINSTANCE.createEClass();
		eClass1.setName("EClass1");
		ePackage.getEClassifiers().add(eClass1);
		
		String stringified = EMFUtils.convertToXML(ePackage);
		Logger logger = Logger.getLogger(TestEMFUtils.class);
		logger.debug("STRING: " + stringified);
		
		EObject object = EMFUtils.createFromXML(stringified);
		assertTrue(object instanceof EPackage);
		ePackage = (EPackage)object;
		assertEquals(2, ePackage.getEClassifiers().size());
		assertEquals("EClass0", ePackage.getEClassifiers().get(0).getName());
		assertEquals("EClass1", ePackage.getEClassifiers().get(1).getName());
	}
	
	@Test
	public void filter() {
		final List<EObject> list = new ArrayList<EObject>();
		final EObject o1 = EcoreFactory.eINSTANCE.createEObject();
		final EObject o2 = EcoreFactory.eINSTANCE.createEObject();
		list.add(o1);
		list.add(o2);
		final List<EObject> filtered = EMFUtils.filter(list, new Object() {
			@SuppressWarnings("unused")
			public boolean doSwitch(EObject obj) {
				if (obj == o1)
					return true;
				return false;
			}
		});
		assertEquals(Collections.singletonList(o1), filtered);
	}
	
	@Test
	public void map() {
		final List<EObject> list = new ArrayList<EObject>();
		final EObject o1 = EcoreFactory.eINSTANCE.createEObject();
		final EObject o2 = EcoreFactory.eINSTANCE.createEObject();
		list.add(o1);
		list.add(o2);
		final List<String> mapped = EMFUtils.map(list, new Object() {
			@SuppressWarnings("unused")
			public String doSwitch(EObject obj) {
				return obj.toString();
			}
		});
		final List<String> stringified = new ArrayList<String>();
		stringified.add(o1.toString());
		stringified.add(o2.toString());
		assertEquals(stringified, mapped);
	}
	
	@Test
	public void filterMap() {
		final List<EObject> list = new ArrayList<EObject>();
		final EObject o1 = EcoreFactory.eINSTANCE.createEObject();
		final EObject o2 = EcoreFactory.eINSTANCE.createEObject();
		list.add(o1);
		list.add(o2);
		final List<String> mapped = EMFUtils.filterMap(list, new Object() {
			@SuppressWarnings("unused")
			public String doSwitch(EObject obj) {
				if (obj == o1)
					return obj.toString();
				return null;
			}
		});
		assertEquals(Collections.singletonList(o1.toString()), mapped);
	}
	
	@Test
	public void set() {
		EObject obj0 = EcoreUtil.create(eClass0);
		EObject obj1 = EcoreUtil.create(eClass1);
		EMFUtils.setOrAdd(obj0, singleRef, obj1);
		assertEquals(obj1, obj0.eGet(singleRef));
	}
	
	@Test
	public void containingListAndIndex() {
		EObject obj0 = EcoreUtil.create(eClass0);
		assertNull(EMFUtils.getContainingList(obj0));
		assertEquals(-1, EMFUtils.getIndexWithinParent(obj0));
		EObject obj1 = EcoreUtil.create(eClass1);
		assertNull(EMFUtils.getContainingList(obj1));
		assertEquals(-1, EMFUtils.getIndexWithinParent(obj1));
		EMFUtils.setOrAdd(obj1, multiRef, obj0);
		assertTrue(obj1.eGet(multiRef) == EMFUtils.getContainingList(obj0));
		assertEquals(0, EMFUtils.getIndexWithinParent(obj0));
	}
	
	@Test
	public void getAddedAndRemoved() {
		final EObject obj00 = EcoreUtil.create(eClass0);
		final EObject obj1 = EcoreUtil.create(eClass1);
		final EObject obj01 = EcoreUtil.create(eClass0);
		final EObject obj02 = EcoreUtil.create(eClass0);
		final List<Notification> messages = new ArrayList<Notification>();
		obj1.eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				messages.add(msg);
			}
		});
		
		// Add
		obj1.eSet(multiRef, Arrays.asList(new EObject[] {obj00, obj01}));
		EMFUtils.setOrAdd(obj1, multiRef, obj02);
		
		// Remove
		final List list = (List)obj1.eGet(multiRef);
		list.remove(obj00);
		list.removeAll(Arrays.asList(new EObject[] {obj01, obj02}));
		
		final List<EObject> addedObjects = new ArrayList<EObject>();
		final List<EObject> removedObjects = new ArrayList<EObject>();
		for (Notification msg : messages) {
			addedObjects.addAll(EMFUtils.getAddedObjects(msg, obj00.getClass()));
			removedObjects.addAll(EMFUtils.getRemovedObjects(msg, obj00.getClass()));
		}
		
		assertEquals(Arrays.asList(new EObject[] {obj00, obj01, obj02}), addedObjects);
		assertEquals(Arrays.asList(new EObject[] {obj00, obj01, obj02}), removedObjects);
	}
	
	@Test
	public void annotation() {
		final String source = "source";
		final String key = "key";
		final String value = "value";
		assertNull(EMFUtils.getAnnotation(eClass0, source, key));
		EMFUtils.addAnnotation(eClass0, source, new String[] {key, value});
		assertEquals(value, EMFUtils.getAnnotation(eClass0, source, key));
		
		// boolean annotations
		EMFUtils.setBooleanAnnotation(multiRef, source, key, true);
		assertTrue(EMFUtils.testBooleanAnnotation(multiRef, source, key));
		EMFUtils.setBooleanAnnotation(multiRef, source, key, false);
		assertFalse(EMFUtils.testBooleanAnnotation(multiRef, source, key));
	}
	
	@Test
	public void cast() {
		final EClassifier[] types = new EClassifier[] {
				EcorePackage.eINSTANCE.getEInt(), 
				EcorePackage.eINSTANCE.getEFloat(), 
				EcorePackage.eINSTANCE.getEDouble()
		};
		final Object[] values = new Object[] {1, 1F, 1D};
		for (int i = 0; i < types.length; i++) {
			EClassifier classifier = types[i];
			EAttribute attribute = FACTORY.createEAttribute();
			attribute.setEType(classifier);
			eClass0.getEStructuralFeatures().add(attribute);
			assertEquals(values[i], EMFUtils.cast(values[(i+1)%values.length], attribute));
		}
	}
}
