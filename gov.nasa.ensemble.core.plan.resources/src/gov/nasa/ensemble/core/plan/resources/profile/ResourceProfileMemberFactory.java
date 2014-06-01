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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.IMemberFactory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

public class ResourceProfileMemberFactory implements IMemberFactory<ResourceProfileMember> {

	@Override
	@SuppressWarnings("unchecked")
	public ResourceProfileMember getMember(EPlan plan) {
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plan);
		if (domain == null) {
			return null;
		}
		ResourceSet resourceSet = domain.getResourceSet();
		URI profilesURI = PlanResourcesProfileResourceSetContributor.getProfilesURI(plan);
		try {
			Resource resource = resourceSet.getResource(profilesURI, false);
			if (resource == null) {
				return null;
			}
			EList<EObject> contents = resource.getContents();
			return (ResourceProfileMember)contents.get(0);
		} catch (NullPointerException e) {
			return null;
		}
	}

	@Override
	public Class<ResourceProfileMember> getMemberClass() {
		return ResourceProfileMember.class;
	}

}
