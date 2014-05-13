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
package gov.nasa.ensemble.emf.provider;

import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor.OverrideableCommandOwner;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

public class RecursiveItemProviderAdapter extends ItemProviderAdapter {

	private static final String DELEGATING_WRAPPER_ITEM_PROPERTY_DESCRIPTOR_NAME = 
		"org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider$DelegatingWrapperItemPropertyDescriptor";
	
	public RecursiveItemProviderAdapter(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	protected void addChildDescriptors(Object object) {
		addChildDescriptors(object, false);
	}
	
	protected void addChildDescriptors(Object object, boolean readOnly) {
		if (object instanceof EObject)
			itemPropertyDescriptors.addAll(getAllChildDescriptors((EObject)object, readOnly));
	}

	protected void addChildDescriptors(Object object, EReference reference) {
		addChildDescriptors(object, reference, false);
	}
	
	protected void addChildDescriptors(Object object, EReference reference, boolean readOnly) {
		if (object instanceof EObject)
			itemPropertyDescriptors.addAll(getChildDescriptors((EObject)object, reference, readOnly));
	}
	
	protected List<IItemPropertyDescriptor> getAllChildDescriptors(EObject eObject) {
		return getAllChildDescriptors(eObject, false);
	}
	
	protected List<IItemPropertyDescriptor> getAllChildDescriptors(EObject eObject, boolean readOnly) {
		return getAllChildDescriptors(eObject, eObject.eClass().getEAllContainments(), readOnly);
	}

	protected List<IItemPropertyDescriptor> getAllChildDescriptors(EObject owner, List<EReference> references) {
		return getAllChildDescriptors(owner, references, false);
	}
	
	protected List<IItemPropertyDescriptor> getAllChildDescriptors(EObject owner, List<EReference> references, boolean readOnly) {
		List<IItemPropertyDescriptor> childDescriptors = new ArrayList<IItemPropertyDescriptor>();
		for (EReference ref : references)
			childDescriptors.addAll(getChildDescriptors(owner, ref, readOnly));
		return childDescriptors;
	}
	
	protected List<IItemPropertyDescriptor> getChildDescriptors(EObject owner, EReference reference) {
		return getChildDescriptors(owner, reference, false);
	}
	
	protected List<IItemPropertyDescriptor> getChildDescriptors(EObject owner, EReference reference, boolean readOnly) {
		Object value = owner.eGet(reference);
		if (reference.isMany()) {
			final List<IItemPropertyDescriptor> childDescriptors = new ArrayList<IItemPropertyDescriptor>();
			int index = 0;
			for (Object child : (List<EObject>)value) {
				final List<IItemPropertyDescriptor> childrenForThisItem = 
					getChildDescriptors(owner, (EObject)child, reference, index++, readOnly);
				childDescriptors.addAll(childrenForThisItem);
			}
			return childDescriptors;
		} else {
			return getChildDescriptors(owner, (EObject)value, reference, -1, readOnly);
		}
	}
	
	protected List<IItemPropertyDescriptor> getChildDescriptors(EObject parent, EObject child, EStructuralFeature feature, int index) {
		return getChildDescriptors(parent, child, feature, index, false);
	}
	
	protected List<IItemPropertyDescriptor> getChildDescriptors(EObject parent, EObject child, EStructuralFeature feature, int index, final boolean readOnly) {
		List<IItemPropertyDescriptor> childDescriptors = new ArrayList<IItemPropertyDescriptor>();
		IItemPropertySource source = EMFUtils.adapt(child, IItemPropertySource.class);
		if (source != null) {
			List<IItemPropertyDescriptor> pds;
			{
				final DelegatingWrapperItemProvider ip = new DelegatingWrapperItemProvider(child, parent, feature, index, getAdapterFactory());
				pds = ip.getPropertyDescriptors(parent);
				ip.dispose();
			}
			for (IItemPropertyDescriptor pd : pds) {
				((OverrideableCommandOwner)pd).setCommandOwner(child);
				childDescriptors.add(pd);
				Object realDescriptor = ReflectionUtils.get(pd, "itemPropertyDescriptor");
				if (realDescriptor.getClass().getName().equals(DELEGATING_WRAPPER_ITEM_PROPERTY_DESCRIPTOR_NAME))
					realDescriptor = ReflectionUtils.get(realDescriptor, "itemPropertyDescriptor");
				ReflectionUtils.set(realDescriptor, "category", pd.getLabelProvider(child).getText(child));
				if (readOnly)
					ReflectionUtils.set(realDescriptor, "isSettable", false);
			}
		}
		
//		// recurse?
//		if (child != null)
//			childDescriptors.addAll(getAllChildDescriptors(child));
		
		return childDescriptors;
	}
	
	protected String beautifyFeatureName(String name) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (i == 0)
				result.append(Character.toUpperCase(c));
			else if (Character.isUpperCase(c))
				result.append(" ").append(c);
			else
				result.append(c);
		}
		return result.toString();
	}
}
