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
import gov.nasa.ensemble.dictionary.EReferenceParameter;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.dictionary.EReferenceParameter} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EReferenceParameterItemProvider
	extends ItemProviderAdapter
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
	public EReferenceParameterItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addOrderedPropertyDescriptor(object);
			addUniquePropertyDescriptor(object);
			addLowerBoundPropertyDescriptor(object);
			addUpperBoundPropertyDescriptor(object);
			addManyPropertyDescriptor(object);
			addRequiredPropertyDescriptor(object);
			addETypePropertyDescriptor(object);
			addChangeablePropertyDescriptor(object);
			addVolatilePropertyDescriptor(object);
			addTransientPropertyDescriptor(object);
			addDefaultValueLiteralPropertyDescriptor(object);
			addDefaultValuePropertyDescriptor(object);
			addUnsettablePropertyDescriptor(object);
			addDerivedPropertyDescriptor(object);
			addContainmentPropertyDescriptor(object);
			addContainerPropertyDescriptor(object);
			addResolveProxiesPropertyDescriptor(object);
			addEOppositePropertyDescriptor(object);
			addEReferenceTypePropertyDescriptor(object);
			addEKeysPropertyDescriptor(object);
			addDefaultLengthPropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ENamedElement_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ENamedElement_name_feature", "_UI_ENamedElement_type"),
				 EcorePackage.Literals.ENAMED_ELEMENT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ordered feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOrderedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_ordered_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_ordered_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__ORDERED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unique feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUniquePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_unique_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_unique_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__UNIQUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lower Bound feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLowerBoundPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_lowerBound_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_lowerBound_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__LOWER_BOUND,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Upper Bound feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUpperBoundPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_upperBound_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_upperBound_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__UPPER_BOUND,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Many feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addManyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_many_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_many_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__MANY,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Required feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRequiredPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_required_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_required_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__REQUIRED,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the EType feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addETypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ETypedElement_eType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ETypedElement_eType_feature", "_UI_ETypedElement_type"),
				 EcorePackage.Literals.ETYPED_ELEMENT__ETYPE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Changeable feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addChangeablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_changeable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_changeable_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__CHANGEABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Volatile feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVolatilePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_volatile_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_volatile_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__VOLATILE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Transient feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTransientPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_transient_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_transient_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__TRANSIENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Default Value Literal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDefaultValueLiteralPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_defaultValueLiteral_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_defaultValueLiteral_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__DEFAULT_VALUE_LITERAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Default Value feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDefaultValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_defaultValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_defaultValue_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__DEFAULT_VALUE,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unsettable feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUnsettablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_unsettable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_unsettable_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__UNSETTABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Derived feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDerivedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EStructuralFeature_derived_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EStructuralFeature_derived_feature", "_UI_EStructuralFeature_type"),
				 EcorePackage.Literals.ESTRUCTURAL_FEATURE__DERIVED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Containment feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContainmentPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_containment_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_containment_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__CONTAINMENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Container feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContainerPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_container_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_container_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__CONTAINER,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Resolve Proxies feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addResolveProxiesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_resolveProxies_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_resolveProxies_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__RESOLVE_PROXIES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the EOpposite feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEOppositePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_eOpposite_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_eOpposite_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__EOPPOSITE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the EReference Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEReferenceTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_eReferenceType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_eReferenceType_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__EREFERENCE_TYPE,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the EKeys feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEKeysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EReference_eKeys_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EReference_eKeys_feature", "_UI_EReference_type"),
				 EcorePackage.Literals.EREFERENCE__EKEYS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Default Length feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDefaultLengthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EParameterDef_defaultLength_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EParameterDef_defaultLength_feature", "_UI_EParameterDef_type"),
				 DictionaryPackage.Literals.EPARAMETER_DEF__DEFAULT_LENGTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
				 getString("_UI_EParameterDef_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EParameterDef_description_feature", "_UI_EParameterDef_type"),
				 DictionaryPackage.Literals.EPARAMETER_DEF__DESCRIPTION,
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
			childrenFeatures.add(EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS);
			childrenFeatures.add(EcorePackage.Literals.ETYPED_ELEMENT__EGENERIC_TYPE);
			childrenFeatures.add(DictionaryPackage.Literals.EREFERENCE_PARAMETER__EFFECTS);
			childrenFeatures.add(DictionaryPackage.Literals.EREFERENCE_PARAMETER__REQUIREMENTS);
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
	 * This returns EReferenceParameter.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/EReferenceParameter"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		EReferenceParameter eReference = (EReferenceParameter)object;
	    StringBuffer result = new StringBuffer();
	    result.append(eReference.getName());
	    if (eReference.getEType() != null)
	    {
	      result.append(" : ");
	      result.append(eReference.getEType().getName());
	    }
	    return result.toString();
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

		switch (notification.getFeatureID(EReferenceParameter.class)) {
			case DictionaryPackage.EREFERENCE_PARAMETER__NAME:
			case DictionaryPackage.EREFERENCE_PARAMETER__ORDERED:
			case DictionaryPackage.EREFERENCE_PARAMETER__UNIQUE:
			case DictionaryPackage.EREFERENCE_PARAMETER__LOWER_BOUND:
			case DictionaryPackage.EREFERENCE_PARAMETER__UPPER_BOUND:
			case DictionaryPackage.EREFERENCE_PARAMETER__MANY:
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIRED:
			case DictionaryPackage.EREFERENCE_PARAMETER__CHANGEABLE:
			case DictionaryPackage.EREFERENCE_PARAMETER__VOLATILE:
			case DictionaryPackage.EREFERENCE_PARAMETER__TRANSIENT:
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_VALUE_LITERAL:
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_VALUE:
			case DictionaryPackage.EREFERENCE_PARAMETER__UNSETTABLE:
			case DictionaryPackage.EREFERENCE_PARAMETER__DERIVED:
			case DictionaryPackage.EREFERENCE_PARAMETER__CONTAINMENT:
			case DictionaryPackage.EREFERENCE_PARAMETER__CONTAINER:
			case DictionaryPackage.EREFERENCE_PARAMETER__RESOLVE_PROXIES:
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH:
			case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__EANNOTATIONS:
			case DictionaryPackage.EREFERENCE_PARAMETER__EGENERIC_TYPE:
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
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
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__EFFECTS,
				 DictionaryFactory.eINSTANCE.createEClaimableEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__EFFECTS,
				 DictionaryFactory.eINSTANCE.createENumericResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__EFFECTS,
				 DictionaryFactory.eINSTANCE.createEStateResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__EFFECTS,
				 DictionaryFactory.eINSTANCE.createESharableResourceEffect()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__REQUIREMENTS,
				 DictionaryFactory.eINSTANCE.createEActivityRequirement()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__REQUIREMENTS,
				 DictionaryFactory.eINSTANCE.createENumericRequirement()));

		newChildDescriptors.add
			(createChildParameter
				(DictionaryPackage.Literals.EREFERENCE_PARAMETER__REQUIREMENTS,
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
