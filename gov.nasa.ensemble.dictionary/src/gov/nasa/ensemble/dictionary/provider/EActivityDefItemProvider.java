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
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EClassItemProvider;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.dictionary.EActivityDef} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EActivityDefItemProvider
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
	public EActivityDefItemProvider(AdapterFactory adapterFactory) {
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

			addCategoryPropertyDescriptor(object);
			addChildrenPropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			addDurationPropertyDescriptor(object);
			addNumericEffectsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Category feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCategoryPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDef_category_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDef_category_feature", "_UI_EActivityDef_type"),
				 DictionaryPackage.Literals.EACTIVITY_DEF__CATEGORY,
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
				 getString("_UI_EActivityDef_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDef_description_feature", "_UI_EActivityDef_type"),
				 DictionaryPackage.Literals.EACTIVITY_DEF__DESCRIPTION,
				 true,
				 true,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDef_duration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDef_duration_feature", "_UI_EActivityDef_type"),
				 DictionaryPackage.Literals.EACTIVITY_DEF__DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Numeric Effects feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNumericEffectsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDef_numericEffects_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDef_numericEffects_feature", "_UI_EActivityDef_type"),
				 DictionaryPackage.Literals.EACTIVITY_DEF__NUMERIC_EFFECTS,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Children feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addChildrenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivityDef_children_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivityDef_children_feature", "_UI_EActivityDef_type"),
				 DictionaryPackage.Literals.EACTIVITY_DEF__CHILDREN,
				 false,
				 false,
				 false,
				 null,
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
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__CHILDREN);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__CLAIMABLE_EFFECTS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__NUMERIC_EFFECTS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__NUMERIC_REQUIREMENTS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__SHARED_EFFECTS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__STATE_EFFECTS);
			childrenFeatures.add(DictionaryPackage.Literals.EACTIVITY_DEF__STATE_REQUIREMENTS);
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
	 * This returns EActivityDef.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/EActivityDef"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		return super.getText(object);
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

		switch (notification.getFeatureID(EActivityDef.class)) {
			case DictionaryPackage.EACTIVITY_DEF__CATEGORY:
			case DictionaryPackage.EACTIVITY_DEF__DESCRIPTION:
			case DictionaryPackage.EACTIVITY_DEF__DURATION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * Changed to allow only ensemble reference/attributes. No sub activity definition
	 * and better ordering. Removed all super collected descriptors except annotations.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
//		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
	      (createChildParameter
	        (EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS,
	         EcoreFactory.eINSTANCE.createEAnnotation()));
		
		newChildDescriptors.add
			(createChildParameter
				(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
				 DictionaryFactory.eINSTANCE.createEReferenceParameter()));

		newChildDescriptors.add
		(createChildParameter
			(EcorePackage.Literals.ECLASS__ESTRUCTURAL_FEATURES,
			 DictionaryFactory.eINSTANCE.createEAttributeParameter()));
		
		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DEF__CLAIMABLE_EFFECTS,
				 DictionaryFactory.eINSTANCE.createEClaimableEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DEF__NUMERIC_EFFECTS,
				 DictionaryFactory.eINSTANCE.createENumericResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DEF__SHARED_EFFECTS,
				 DictionaryFactory.eINSTANCE.createESharableResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DEF__STATE_EFFECTS,
				 DictionaryFactory.eINSTANCE.createEStateResourceEffect()));

		newChildDescriptors.add
		(createChildParameter
			(DictionaryPackage.Literals.EACTIVITY_DEF__NUMERIC_REQUIREMENTS,
			 DictionaryFactory.eINSTANCE.createENumericRequirement()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EACTIVITY_DEF__STATE_REQUIREMENTS,
				 DictionaryFactory.eINSTANCE.createEStateRequirement()));
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
