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
package gov.nasa.ensemble.core.plan.resources.member.provider;


import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.ResourcesPlugin;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.plan.resources.member.Conditions} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConditionsItemProvider
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
	public ConditionsItemProvider(AdapterFactory adapterFactory) {
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

			addTimePropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			addEditablePropertyDescriptor(object);
			addActivePropertyDescriptor(object);
			addClaimsPropertyDescriptor(object);
			addPowerLoadsPropertyDescriptor(object);
			addNumericResourcesPropertyDescriptor(object);
			addStateResourcesPropertyDescriptor(object);
			addSharableResourcesPropertyDescriptor(object);
			addUndefinedResourcesPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_time_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_time_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__TIME,
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
				 getString("_UI_Conditions_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_description_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__DESCRIPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Editable feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEditablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_editable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_editable_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__EDITABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Active feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addActivePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_active_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_active_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__ACTIVE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Claims feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addClaimsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_claims_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_claims_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__CLAIMS,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Power Loads feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPowerLoadsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_powerLoads_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_powerLoads_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__POWER_LOADS,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Numeric Resources feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNumericResourcesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_numericResources_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_numericResources_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__NUMERIC_RESOURCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the State Resources feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStateResourcesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_stateResources_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_stateResources_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__STATE_RESOURCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sharable Resources feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSharableResourcesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_sharableResources_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_sharableResources_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__SHARABLE_RESOURCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Undefined Resources feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUndefinedResourcesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Conditions_undefinedResources_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Conditions_undefinedResources_feature", "_UI_Conditions_type"),
				 MemberPackage.Literals.CONDITIONS__UNDEFINED_RESOURCES,
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
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__CLAIMS);
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__POWER_LOADS);
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__NUMERIC_RESOURCES);
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__STATE_RESOURCES);
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__SHARABLE_RESOURCES);
			childrenFeatures.add(MemberPackage.Literals.CONDITIONS__UNDEFINED_RESOURCES);
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
	 * This returns Conditions.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return IconLoader.getIcon(ResourcesPlugin.getDefault().getBundle(), "icons/resource_conditions_marker.png");
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		Date labelValue = ((Conditions)object).getTime();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_Conditions_type") :
			getString("_UI_Conditions_type") + " " + label;
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

		switch (notification.getFeatureID(Conditions.class)) {
			case MemberPackage.CONDITIONS__TIME:
			case MemberPackage.CONDITIONS__DESCRIPTION:
			case MemberPackage.CONDITIONS__EDITABLE:
			case MemberPackage.CONDITIONS__ACTIVE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case MemberPackage.CONDITIONS__CLAIMS:
			case MemberPackage.CONDITIONS__POWER_LOADS:
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
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
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__CLAIMS,
				 MemberFactory.eINSTANCE.createClaim()));

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__POWER_LOADS,
				 MemberFactory.eINSTANCE.createPowerLoad()));

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__NUMERIC_RESOURCES,
				 MemberFactory.eINSTANCE.createNumericResource()));

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__STATE_RESOURCES,
				 MemberFactory.eINSTANCE.createStateResource()));

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__SHARABLE_RESOURCES,
				 MemberFactory.eINSTANCE.createSharableResource()));

		newChildDescriptors.add
			(createChildParameter
				(MemberPackage.Literals.CONDITIONS__UNDEFINED_RESOURCES,
				 MemberFactory.eINSTANCE.createUndefinedResource()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ResourceMembersEditPlugin.INSTANCE;
	}
	
	@Override
	// MSLICE-2844
	protected ItemPropertyDescriptor createItemPropertyDescriptor(
	    AdapterFactory adapterFactory,
	    ResourceLocator resourceLocator,
	    String displayName,
	    String description,
	    EStructuralFeature feature,
	    boolean isSettable,
	    boolean multiLine,
	    boolean sortChoices,
	    Object staticImage,
	    String category,
	    String[] filterFlags)
	  {
	    return new ItemPropertyDescriptor(
	      adapterFactory,
	      resourceLocator,
	      displayName,
	      description,
	      feature,
	      isSettable,
	      multiLine,
	      sortChoices,
	      staticImage,
	      category,
	      filterFlags) {
	    	@Override
	    	public boolean canSetProperty(Object object) {
	    		if (object instanceof Conditions) {
					return EPlanUtils.canEdit(((Conditions) object).getMember());
	    		}
	    		return super.canSetProperty(object);
	    	}
	    };
	  }

}
