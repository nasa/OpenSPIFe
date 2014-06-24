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
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDictionary;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EPackageItemProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.dictionary.EActivityDictionary} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EActivityDictionaryItemProvider
	extends EPackageItemProvider
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
	public EActivityDictionaryItemProvider(AdapterFactory adapterFactory) {
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

			addAuthorPropertyDescriptor(object);
			addDatePropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			addVersionPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Author feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAuthorPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDictionary_author_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDictionary_author_feature", "_UI_EActivityDictionary_type"),
				 DictionaryPackage.Literals.EACTIVITY_DICTIONARY__AUTHOR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDictionary_date_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDictionary_date_feature", "_UI_EActivityDictionary_type"),
				 DictionaryPackage.Literals.EACTIVITY_DICTIONARY__DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDictionary_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDictionary_description_feature", "_UI_EActivityDictionary_type"),
				 DictionaryPackage.Literals.EACTIVITY_DICTIONARY__DESCRIPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Version feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDictionary_version_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDictionary_version_feature", "_UI_EActivityDictionary_type"),
				 DictionaryPackage.Literals.EACTIVITY_DICTIONARY__VERSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns EActivityDictionary.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/EActivityDictionary"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((EActivityDictionary)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_EActivityDictionary_type") : label;
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

		switch (notification.getFeatureID(EActivityDictionary.class)) {
			case DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR:
			case DictionaryPackage.EACTIVITY_DICTIONARY__DATE:
			case DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION:
			case DictionaryPackage.EACTIVITY_DICTIONARY__VERSION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
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
				(EcorePackage.Literals.EPACKAGE__ECLASSIFIERS,
				 DictionaryFactory.eINSTANCE.createEActivityDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.EPACKAGE__ECLASSIFIERS,
				 DictionaryFactory.eINSTANCE.createObjectDef()));

		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.EPACKAGE__ESUBPACKAGES,
				 DictionaryFactory.eINSTANCE.createEActivityDictionary()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS,
				 DictionaryFactory.eINSTANCE.createEAttributeParameter()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS,
				 DictionaryFactory.eINSTANCE.createEReferenceParameter()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEActivityDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createENumericResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEExtendedNumericResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEClaimableResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEClaimableEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createENumericResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEStateResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEStateResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createERule()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createESharableResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createESharableResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createEStateRequirement()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createESummaryResourceDef()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS,
				 DictionaryFactory.eINSTANCE.createObjectDef()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == EcorePackage.Literals.EPACKAGE__ECLASSIFIERS ||
			childFeature == DictionaryPackage.Literals.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
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
