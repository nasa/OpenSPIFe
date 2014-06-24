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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.util.CompatibleItemPropertyDescriptor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

public class EMemberItemPropertyDescriptor extends CompatibleItemPropertyDescriptor {

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable, String category) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences, boolean isSettable) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences, isSettable);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EReference[] parentReferences) {
		super(adapterFactory, resourceLocator, displayName, description, parentReferences);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
			String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage, String category) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, Object staticImage) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, staticImage);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable, String category) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature, boolean isSettable) {
		super(adapterFactory, resourceLocator, displayName, description, feature, isSettable);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
			EStructuralFeature feature) {
		super(adapterFactory, resourceLocator, displayName, description, feature);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, parentReferences, isSettable, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable, String category) {
		super(adapterFactory, displayName, description, parentReferences, isSettable, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences,
			boolean isSettable) {
		super(adapterFactory, displayName, description, parentReferences, isSettable);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EReference[] parentReferences) {
		super(adapterFactory, displayName, description, parentReferences);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage, String category) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, Object staticImage) {
		super(adapterFactory, displayName, description, feature, isSettable, staticImage);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, String category, String[] filterFlags) {
		super(adapterFactory, displayName, description, feature, isSettable, category, filterFlags);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable, String category) {
		super(adapterFactory, displayName, description, feature, isSettable, category);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature,
			boolean isSettable) {
		super(adapterFactory, displayName, description, feature, isSettable);
	}

	public EMemberItemPropertyDescriptor(AdapterFactory adapterFactory, String displayName, String description, EStructuralFeature feature) {
		super(adapterFactory, displayName, description, feature);
	}

	@Override
	public String getCategory(Object object) {
		String category = super.getCategory(object);
		if (category == null) {
			IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
			if (labeler != null) {
				return labeler.getText(object);
			}
		}
		return category;
	}
	
	@Override
	public boolean canSetProperty(Object object) {
		if (object==null) {
			return false;
		} else if (object instanceof EMember) {
			return EPlanUtils.canEdit((EMember) object) && super.canSetProperty(object);
		}
		return super.canSetProperty(object);
	}

}
