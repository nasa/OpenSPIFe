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

import gov.nasa.ensemble.common.CommonUtils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;

public class CompatibleItemPropertyDescriptor extends ItemPropertyDescriptor {

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable, String category) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
			String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage, String category) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, String category) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature) {
		super(adapterFactory, resourceLocator, displayName, description, feature);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, parentReferences, isSettable, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable, String category) {
		super(adapterFactory, displayName, description, parentReferences, isSettable, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable) {
		super(adapterFactory, displayName, description, parentReferences, isSettable);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences) {
		super(adapterFactory, displayName, description, parentReferences);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage, String category) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, feature, isSettable, category, filterFlags);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, String category) {
		super(adapterFactory, displayName, description, feature, isSettable, category);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable) {
		super(adapterFactory, displayName, description, feature, isSettable);
	}

	public CompatibleItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature) {
		super(adapterFactory, displayName, description, feature);
	}

	@Override
	public boolean isCompatibleWith(Object object, Object anotherObject, IItemPropertyDescriptor otherPD) {
		if (otherPD == this) {
			return true;
	    } else {
	    	EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(anotherObject);
			if (domain instanceof AdapterFactoryEditingDomain) {
				AdapterFactoryEditingDomain thisDomain = (AdapterFactoryEditingDomain) domain;
				AdapterFactory thisAdapterFactory = thisDomain.getAdapterFactory();
				AdapterFactoryEditingDomain otherDomain = (AdapterFactoryEditingDomain) AdapterFactoryEditingDomain.getEditingDomainFor(object);
				AdapterFactory otherAdapterFactory = otherDomain == null ? null : otherDomain.getAdapterFactory();
				if (otherAdapterFactory == thisAdapterFactory 
						&& CommonUtils.equals(this.getDisplayName(object), otherPD.getDisplayName(anotherObject)) 
			            		&& CommonUtils.equals(this.getCategory(object), otherPD.getCategory(anotherObject)))
				{
					return true;
				}
			}
	    }
		return false;
	}

}
