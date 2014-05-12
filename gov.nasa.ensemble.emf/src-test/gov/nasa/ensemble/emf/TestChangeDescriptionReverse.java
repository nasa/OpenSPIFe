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
package gov.nasa.ensemble.emf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * There is a new feature in the model Eclipse Modeling Tools for 3.7.x that improves the ability to record a set of changes being made to
 * a model and from that efficiently produce a change description that can be sent elsewhere and applied to replicate those changes.
 * Although it's possible to call applyAndReverse in 3.6.2 to produce a forward delta, it modifies the state of the model (i.e. like undo).
 * Of course, it's also possible to call applyAndReverse as second time to redo the changes, but clearly that's not very efficient nor desirable
 * when there's a user interface updating in response to all the changes. To address that problem, EMF has implemented a copyAndReverse method so
 * that we can produce a serializeable forward delta without changing the state of the model itself. Pretty cool, isn't?
 * To run this test, you need to use eclipse 3.7.x and remove the comments in the TestHelper::doit() method. 
 * @since 3.7.x
 */
public class TestChangeDescriptionReverse extends TestCase {
	public TestChangeDescriptionReverse(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite ts = new TestSuite("ChangeDescription copyAndReverse Test");
		ts.addTest(new TestChangeDescriptionReverse("testOne"));
		ts.addTest(new TestChangeDescriptionReverse("testTwo"));
		ts.addTest(new TestChangeDescriptionReverse("testThree"));
		ts.addTest(new TestChangeDescriptionReverse("testFour"));
		ts.addTest(new TestChangeDescriptionReverse("testFive"));
		ts.addTest(new TestChangeDescriptionReverse("testSix"));
		ts.addTest(new TestChangeDescriptionReverse("testSeven"));
		return ts;
	}

	abstract class TestHelper {
		void doit() throws Exception {
			ResourceSet originalResourceSet = new ResourceSetImpl();

			loadResources(originalResourceSet);

			@SuppressWarnings("unused")
			Map<EObject, URI> eObjectToProxyURIMap = new HashMap<EObject, URI>();
			ChangeRecorder changeRecorder = new ChangeRecorder();
			
			/* Uncomment this in 3.7.x for the test to work/pass
			changeRecorder.setRecordingTransientFeatures(false);
			changeRecorder.setEObjectToProxyURIMap(eObjectToProxyURIMap);
			*/
			
			changeRecorder.beginRecording(Collections.singleton(originalResourceSet));

			makeChanges();

			ChangeDescription changeDescription = changeRecorder.endRecording();
			/* Uncomment this in 3.7.x for the test to work/pass
			changeDescription.copyAndReverse(eObjectToProxyURIMap);
			*/

			ResourceSet finalResourceSet = new ResourceSetImpl();
			Resource changeDescriptionResource = finalResourceSet.createResource(URI.createURI("changes.change"));
			changeDescriptionResource.getContents().add(changeDescription);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			changeDescriptionResource.save(System.out, null);
			changeDescriptionResource.save(out, null);
			changeDescriptionResource.getContents().clear();
			changeDescriptionResource.unload();
			changeDescriptionResource.load(new ByteArrayInputStream(out.toByteArray()), null);

			ChangeDescription finalChangeDescription = (ChangeDescription) changeDescriptionResource.getContents().get(0);
			finalChangeDescription.apply();

			finalResourceSet.getResources().remove(0);

			assertEquals(originalResourceSet, finalResourceSet);
		}

		abstract void loadResources(ResourceSet resourceSet);

		abstract void makeChanges();
	}

	public void testOne() throws Exception {
		new TestHelper() {
			Resource ecoreResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
				ePackage.getEClassifiers().remove(1);
			}
		}.doit();
	}

	public void testTwo() throws Exception {
		new TestHelper() {
			Resource ecoreResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
				EClass eClass = EcoreFactory.eINSTANCE.createEClass();
				ePackage.getEClassifiers().add(5, eClass);
				eClass.setName("NewClass");
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eClass.getEAnnotations().add(eAnnotation);
				eAnnotation.getContents().add(0, ePackage.getEClassifiers().get(2));
			}
		}.doit();
	}

	public void testThree() throws Exception {
		new TestHelper() {
			Resource ecoreResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ePackage = (EPackage) ecoreResource.getContents().get(0);
				ePackage.getEClassifiers().move(10, 5);
			}
		}.doit();
	}

	public void testFour() throws Exception {
		new TestHelper() {
			Resource ecoreResource;
			Resource xmlTypeResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
				xmlTypeResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/XMLType.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ecorePackage = (EPackage) ecoreResource.getContents().get(0);
				EPackage xmlTypePackage = (EPackage) xmlTypeResource.getContents().get(0);
				xmlTypePackage.getEClassifiers().add(2, ecorePackage.getEClassifiers().get(2));
			}
		}.doit();
	}

	public void testFive() throws Exception {
		new TestHelper() {
			Resource ecoreResource;
			Resource xmlTypeResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
				xmlTypeResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/XMLType.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ecorePackage = (EPackage) ecoreResource.getContents().get(0);
				EPackage xmlTypePackage = (EPackage) xmlTypeResource.getContents().get(0);
				xmlTypeResource.getContents().add(ecorePackage.getEClassifiers().get(0));
				ecoreResource.getContents().add(xmlTypePackage.getEClassifiers().get(0));
			}
		}.doit();
	}

	public void testSix() throws Exception {
		new TestHelper() {
			Resource ecoreResource;
			Resource xmlTypeResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
				xmlTypeResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/XMLType.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ecorePackage = (EPackage) ecoreResource.getContents().get(0);
				EPackage xmlTypePackage = (EPackage) xmlTypeResource.getContents().get(0);
				EClassifier eClassifier = ecorePackage.getEClassifiers().get(10);
				eClassifier.setName(eClassifier.getName() + "Suffix");
				xmlTypePackage.getEClassifiers().get(0).getEAnnotations().get(0).getContents().add(eClassifier);
				eClassifier.setName(eClassifier.getName() + "Suffix");
				xmlTypePackage.getEClassifiers().get(0).setName("BadName");
				xmlTypePackage.getEClassifiers().remove(12);
				xmlTypePackage.getEClassifiers().move(10, 20);
			}
		}.doit();
	}

	public void testSeven() throws Exception {
		new TestHelper() {
			Resource ecoreResource;
			Resource xmlTypeResource;

			@Override
			void loadResources(ResourceSet resourceSet) {
				ecoreResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/Ecore.ecore"), true);
				xmlTypeResource = resourceSet.getResource(URI.createURI("platform:/plugin/org.eclipse.emf.ecore/model/XMLType.ecore"), true);
			}

			@Override
			void makeChanges() {
				EPackage ecorePackage = (EPackage) ecoreResource.getContents().get(0);
				EPackage xmlTypePackage = (EPackage) xmlTypeResource.getContents().get(0);
				EClassifier eClassifier = ecorePackage.getEClassifiers().get(10);
				eClassifier.setName(eClassifier.getName() + "Suffix");
				xmlTypePackage.getEClassifiers().get(0).getEAnnotations().get(0).getContents().add(eClassifier);
				eClassifier.setName(eClassifier.getName() + "Suffix");
				xmlTypePackage.getEClassifiers().get(0).setName("BadName");
				xmlTypePackage.getEClassifiers().remove(12);
				EDataType myEDataType = EcoreFactory.eINSTANCE.createEDataType();
				myEDataType.setName("My");
				xmlTypePackage.getEClassifiers().add(myEDataType);
				myEDataType.setInstanceTypeName("java.util.List<?>");
				xmlTypePackage.getEClassifiers().move(10, 20);
				xmlTypePackage.getEClassifiers().get(12).setName(xmlTypePackage.getEClassifiers().get(10).getName() + "Extension");
				xmlTypePackage.getEClassifiers().remove(12);
			}
		}.doit();
	}

	void assertEquals(ResourceSet resourceSet1, ResourceSet resourceSet2) {
		EcoreUtil.resolveAll(resourceSet1);
		EcoreUtil.resolveAll(resourceSet2);
		EList<Resource> resources2 = resourceSet2.getResources();
		for (Resource resource2 : resources2) {
			assertEquals(resourceSet1.getResource(resource2.getURI(), false), resource2);
		}
	}

	void assertEquals(Resource resource1, Resource resource2) {
		EList<EObject> eObjects1 = resource1.getContents();
		EList<EObject> eObjects2 = resource2.getContents();
		assertEquals(eObjects1.size(), eObjects2.size());
		for (int i = 0, size = eObjects1.size(); i < size; ++i) {
			assertTrue(EcoreUtil.equals(eObjects1.get(i), eObjects2.get(i)));
		}
	}
}
