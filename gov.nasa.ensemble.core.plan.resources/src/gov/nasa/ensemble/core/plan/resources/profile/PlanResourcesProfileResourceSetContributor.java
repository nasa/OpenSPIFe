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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils.PlanResourceSetContributor;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.resource.IgnorableResource;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class PlanResourcesProfileResourceSetContributor extends PlanResourceSetContributor {

	private static final String PROFILES_PATHNAME = ".profiles";

	public static URI getProfilesURI(EPlan plan) {
		return ProjectURIConverter.createProjectURI(PROFILES_PATHNAME);
	}
	
	@Override
	public void contributePlanResources(EPlan plan, ResourceSet resourceSet) {
		EList<Resource> resources = resourceSet.getResources();
		Map<String, Profile> profileById = gatherProfilesById(plan, resources);
		URI profilesURI = getProfilesURI(plan);
		Resource profilesResource = new IgnorableResource.Stub(profilesURI);
		ResourceProfileMember member = ProfileFactory.eINSTANCE.createResourceProfileMember();
		profilesResource.getContents().add(member);
		for (Profile profile : profileById.values()) {
			member.getResourceProfiles().add(profile);
			if (profile.eResource() == null) {
				profilesResource.getContents().add(profile);
			}
		}
		resources.add(profilesResource);
	}

	private Map<String, Profile> gatherProfilesById(EPlan plan, EList<Resource> resources) {
		ActivityDictionary AD = ActivityDictionary.getInstance(); // avoid static which must be loaded
		Map<String, Profile> profileById = new LinkedHashMap<String, Profile>();
		for (Resource resource : resources) {
			for (EObject o : resource.getContents()) {
				if (o instanceof Profile) {
					Profile profile = (Profile) o;
					profileById.put(profile.getId(), profile);
				}
			}
		}
		for (EResourceDef def : AD.getDefinitions(EResourceDef.class)) {
			if (def instanceof ENumericResourceDef || def instanceof EStateResourceDef || def instanceof ESummaryResourceDef) {
				Profile profile = ResourceProfileFactory.getInstance().createResourceProfile(def);
				profile.setInterpolation(INTERPOLATION.STEP);
				String id = profile.getId();
				if (!profileById.containsKey(id)) {
					profileById.put(id, profile);
				} else {
					LogUtil.info("ignoring an AD-based profile with already existing id: " + id);
				}
			}
		}
		for (ObjectDef objectDef : AD.getDefinitions(ObjectDef.class)) {
			Collection<EObject> reachableObjectsOfType = null;
			for (EStructuralFeature f : objectDef.getEStructuralFeatures()) {
				if (f instanceof EResourceDef) {
					if (reachableObjectsOfType == null) {
						reachableObjectsOfType = EMFUtils.getReachableObjectsOfType(plan, objectDef);
					}
					for (EObject object : reachableObjectsOfType) {
						Profile profile = ProfileFactory.eINSTANCE.createStructuralFeatureProfile(object, f);
						profile.setInterpolation(INTERPOLATION.STEP);
						String id = profile.getId();
						if (!profileById.containsKey(id)) {
							profileById.put(id, profile);
						} else {
							LogUtil.info("ignoring an AD-based profile with already existing id: " + id);
						}
					}
				}
			}
		}
		return profileById;
	}

	@Override
	public boolean dependsOn(PlanResourceSetContributor o) {
		return (o instanceof IProfileContributor);
	}
	
}
