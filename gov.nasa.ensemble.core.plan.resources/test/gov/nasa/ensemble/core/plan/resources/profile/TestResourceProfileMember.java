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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class TestResourceProfileMember extends TestCase {

	private static final String ID0 = "id0";

	
	public void testProfileId() {
		ResourceProfileMember member = createResourceProfileMember();
		Profile<?> fetchedProfile = member.getProfile(ID0);
		assertNotNull(fetchedProfile);
		assertEquals(1, member.getResourceProfiles().size());
	}
	
	/**
	 * Tests the case by which the old profile is replaced by a new profile...
	 */
	public void testProfileSwitching() {
		ResourceProfileMember member = createResourceProfileMember();
		Profile profile1 = createProfileInResource("http://whatever.junit.org/profile1.xmi", ID0);
		member.getResourceProfiles().add(profile1);
		Profile<?> fetchedProfile = member.getProfile(ID0);
		assertNotNull(fetchedProfile);
		assertEquals(1, member.getResourceProfiles().size());
	}
	
	/**
	 * Tests that the old profile is derersolved
	 */
	public void testProfileDeresolution() {
		ResourceProfileMember member = createResourceProfileMember();
		Profile<?> profile0 = member.getProfile(ID0);
		Profile profile1 = createProfileInResource("http://whatever.junit.org/profile1.xmi", ID0);
		member.getResourceProfiles().add(profile1);
		assertTrue(profile0.eIsProxy());
	}
	
	private ResourceProfileMember createResourceProfileMember() {
		Profile profile0 = createProfileInResource("http://whatever.junit.org/profile0.xmi", ID0);
		Resource resource = new XMIResourceImpl(URI.createURI("http://whatever.junit.org/profileMember.xmi"));
		ResourceProfileMember member = ProfileFactory.eINSTANCE.createResourceProfileMember();
		resource.getContents().add(member);
		member.getResourceProfiles().add(profile0);
		return member;
	}

	private Profile createProfileInResource(String resourceUriString, String profileIdString) {
		Resource resource = new XMIResourceImpl(URI.createURI(resourceUriString));
		Profile profile = createProfile(profileIdString);
		resource.getContents().add(profile);
		return profile;
	}

	private Profile createProfile(String profileIdString) {
		Profile profile = JScienceFactory.eINSTANCE.createProfile();
		profile.setId(profileIdString);
		return profile;
	}
	
}
