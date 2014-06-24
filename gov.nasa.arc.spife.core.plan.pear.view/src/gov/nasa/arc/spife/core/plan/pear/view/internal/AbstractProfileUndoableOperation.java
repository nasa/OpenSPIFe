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
package gov.nasa.arc.spife.core.plan.pear.view.internal;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;


public abstract class AbstractProfileUndoableOperation extends AbstractTransactionUndoableOperation {

	protected EPlan plan;
	protected ResourceSet resourceSet;
	
	public AbstractProfileUndoableOperation(EPlan plan, String label) {
		super(label);
		this.plan = plan;
		if(plan != null) {
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(plan);
			this.resourceSet = domain.getResourceSet();
		}
	}
	
	protected void addProfile(Profile profile) {
		//add the profile to the resource
		Resource resource = getProfileResource(profile);
		resource.getContents().add(profile);
		
		//add the profile to the plan.
		WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles().add(profile);
	}
	
	protected void removeProfile(Profile profile) {
		//remove the profile from the plan
		WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles().remove(profile);
		
		//remove the profile from the resource
		Resource resource = getProfileResource(profile);
		resource.getContents().remove(profile);
		
	}
	
	protected Resource getProfileResource(Profile profile) {
		Resource resource = profile.eResource();
		if (resource != null) {
			return resource;
		}
		URI baseURI = plan.eResource().getURI();
		URI uri = null;
		if (profile.isExternalCondition()) {
			uri = URI.createURI("Conditions/").appendSegment(URI.encodeSegment(profile.getCategory()+".condition", false)).resolve(baseURI); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			uri = URI.createURI("Resources/").appendSegment(URI.encodeSegment(profile.getCategory()+".condition", false)).resolve(baseURI); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (resourceSet.getResource(uri, false) != null) {
			resource = resourceSet.getResource(uri, true);
		} else {
			resource = resourceSet.createResource(uri);
		}
		return resource;
	}
	
	
}
