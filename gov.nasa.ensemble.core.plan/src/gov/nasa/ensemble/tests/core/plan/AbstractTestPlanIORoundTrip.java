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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class AbstractTestPlanIORoundTrip extends TestCase {

	/**
	 * Perform the round trip
	 * @param planOut
	 * @return
	 */
	protected EPlan performRoundTrip(EPlan planOut) throws Exception {
		Resource resource = new PlanResourceImpl(URI.createURI("http://ensemble.nasa.gov/junit/out"));
		resource.getContents().add(planOut);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		resource.save(baos, null);
		baos.close();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		resource = new PlanResourceImpl(URI.createURI("http://ensemble.nasa.gov/junit/in"));
		try {
			resource.load(bais, null);
		} catch (Exception e) {
			resource.load(bais, null);
		}
		assertEquals(1, resource.getContents().size());
		assertTrue(resource.getContents().get(0) instanceof EPlan);
		return (EPlan) resource.getContents().get(0);
	}

	protected void assertObjectAttributeEquality(EObject objectOut, EObject objectIn) {
		if (objectOut == null) {
			assertEquals(objectOut, objectIn);
			return;
		}
		EClass eClass = objectOut.eClass();
		for (EAttribute a : eClass.getEAllAttributes()) {
			assertFeatureEquality(eClass, a, objectOut, objectIn);
		}
	}

	protected void assertFeatureEquality(EClass eClass, EStructuralFeature f, EObject objectOut, EObject objectIn) {
		if (f.isTransient()) {
			return;
		}
		Object outValue = objectOut.eGet(f);
		Object inValue = objectIn.eGet(f);
		assertValueEquality(eClass, f, outValue, inValue);
	}
	
	protected void assertValueEquality(EClass eObject, EStructuralFeature feature, Object outObject, Object inObject) {
		if (!CommonUtils.equals(outObject, inObject)) {
			String message = eObject.getName() + "." + feature.getName() + "(" + feature.getEType().getName() + ")";
			fail(message + ": Expected '" + outObject + "' but was '" + inObject + "'");
		}
	}
	
}
