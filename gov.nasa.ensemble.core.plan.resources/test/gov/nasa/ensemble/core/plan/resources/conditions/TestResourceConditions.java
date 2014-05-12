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
package gov.nasa.ensemble.core.plan.resources.conditions;

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.core.plan.resources.ResourcesPlugin;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.PowerLoad;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.util.MemberResourceFactoryImpl;
import gov.nasa.ensemble.core.plan.resources.member.util.MemberXMLProcessor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.framework.Bundle;

public class TestResourceConditions extends TestCase {

	private Conditions sampleConditions = null;
	
	@Override
	protected void setUp() throws Exception {
		sampleConditions = MemberFactory.eINSTANCE.createConditions();
		Claim claim = MemberFactory.eINSTANCE.createClaim();
		claim.setName("Motor_Controller_A_Control");
		claim.setUsed(true);
		NumericResource numericResource = MemberFactory.eINSTANCE.createNumericResource();
		numericResource.setName("APXS_data");
		numericResource.setFloat(3.3f);
		PowerLoad powerLoad = MemberFactory.eINSTANCE.createPowerLoad();
		powerLoad.setName("APXS_Main_Electronics");
		powerLoad.setState("Integration_Cooler_off");
		SharableResource sharableResource = MemberFactory.eINSTANCE.createSharableResource();
		sharableResource.setName("gurneys");
		sharableResource.setUsed(1);
		StateResource stateResource = MemberFactory.eINSTANCE.createStateResource();
		stateResource.setName("Arm_Sample_Delivery");
		stateResource.setState("Arm_Ready");
		sampleConditions.getClaims().add(claim);
		sampleConditions.getNumericResources().add(numericResource);
		sampleConditions.getPowerLoads().add(powerLoad);
		sampleConditions.getSharableResources().add(sharableResource);
		sampleConditions.getStateResources().add(stateResource);
	}
	
	public void testLoadingSampleConditions() throws IOException {
		MemberXMLProcessor xmlProcessor = new MemberXMLProcessor();
		Bundle bundle = ResourcesPlugin.getDefault().getBundle();
		InputStream stream = FileLocator.openStream(bundle, new Path("datafiles/sample_conditions.xml"), false);
		try {
			Resource resource = xmlProcessor.load(stream, null);
			EList<EObject> contents = resource.getContents();
			assertEquals(1, contents.size());
			EObject object = contents.get(0);
			assertTrue(object instanceof Conditions);
			Conditions conditions = (Conditions)object;
			assertSameConditions(sampleConditions, conditions);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}
	
	public void testWritingSampleConditions() throws IOException {
		File temp = FileUtilities.createTempFile("test_conditions", ".xml");
		try {
			ResourceSet resourceSet = EMFUtils.createResourceSet();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new MemberResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(MemberPackage.eNS_URI, MemberPackage.eINSTANCE);
			Resource resource = resourceSet.createResource(URI.createFileURI(temp.getAbsolutePath()));
			resource.getContents().add(sampleConditions);
			MemberXMLProcessor xmlProcessor = new MemberXMLProcessor();
			String string = xmlProcessor.saveToString(resource, null);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(string.getBytes());
			Resource resource2 = new MemberXMLProcessor().load(inputStream, null);
			EList<EObject> contents = resource2.getContents();
			assertEquals(1, contents.size());
			EObject object = contents.get(0);
			assertTrue(object instanceof Conditions);
			Conditions readConditions = (Conditions)object;
			assertSameConditions(sampleConditions, readConditions);
		} finally {
			temp.delete();
		}
	}

	private void assertSameConditions(Conditions expectedConditions, Conditions conditions) {
		EcoreUtil.equals(expectedConditions, conditions);
	}

/*
	// check Claim
	List<Claim> claims = conditions.getClaims();
	assertEquals(1, claims.size());
	Claim claim = claims.get(0);
	assertEquals("Motor_Controller_A_Control", claim.getName());
	assertEquals(true, claim.isUsed());
	// check NumericResource
	List<NumericResource> numericResources = conditions.getNumericResources();
	assertEquals(1, numericResources.size());
	NumericResource numericResource = numericResources.get(0);
	assertEquals("APXS_data", numericResource.getName());
	assertEquals(3.3f, numericResource.getFloat());
*/

}
