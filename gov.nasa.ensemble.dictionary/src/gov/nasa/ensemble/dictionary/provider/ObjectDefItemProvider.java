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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.dictionary.provider;


import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EClassItemProvider;

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.dictionary.ObjectDef} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ObjectDefItemProvider
	extends EClassItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ObjectDefItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This returns ObjectDef.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ObjectDef"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((ObjectDef)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ObjectDef_type") : label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
//		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createEResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createENumericResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createEExtendedNumericResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createEClaimableResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createEStateResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createESharableResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createESummaryResourceDef()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DictionaryEditPlugin.INSTANCE;
	}

}
