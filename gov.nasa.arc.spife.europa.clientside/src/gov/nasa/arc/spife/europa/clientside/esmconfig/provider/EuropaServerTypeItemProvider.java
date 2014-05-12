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
package gov.nasa.arc.spife.europa.clientside.esmconfig.provider;


import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;

import gov.nasa.arc.spife.europa.clientside.provider.EsmConfigEditPlugin;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

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
 * This is the item provider adapter for a {@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EuropaServerTypeItemProvider
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
	public EuropaServerTypeItemProvider(AdapterFactory adapterFactory) {
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

			addConfigPathPropertyDescriptor(object);
			addDebugPropertyDescriptor(object);
			addInitialStatePropertyDescriptor(object);
			addLogFilePropertyDescriptor(object);
			addNamePropertyDescriptor(object);
			addPlannerConfigPropertyDescriptor(object);
			addPlannerConfigElementPropertyDescriptor(object);
			addPortPropertyDescriptor(object);
			addServerVersionPropertyDescriptor(object);
			addTimeoutPropertyDescriptor(object);
			addVerbosityPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Config Path feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addConfigPathPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_configPath_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_configPath_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__CONFIG_PATH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Debug feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDebugPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_debug_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_debug_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__DEBUG,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Initial State feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInitialStatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_initialState_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_initialState_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__INITIAL_STATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Log File feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLogFilePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_logFile_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_logFile_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__LOG_FILE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_EuropaServerType_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_name_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Planner Config feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPlannerConfigPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_plannerConfig_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_plannerConfig_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__PLANNER_CONFIG,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Planner Config Element feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPlannerConfigElementPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_plannerConfigElement_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_plannerConfigElement_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_port_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_port_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__PORT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Server Version feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addServerVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_serverVersion_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_serverVersion_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__SERVER_VERSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Timeout feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTimeoutPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_timeout_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_timeout_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__TIMEOUT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Verbosity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVerbosityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EuropaServerType_verbosity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EuropaServerType_verbosity_feature", "_UI_EuropaServerType_type"),
				 EsmConfigPackage.Literals.EUROPA_SERVER_TYPE__VERBOSITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns EuropaServerType.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/EuropaServerType"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		Object labelValue = ((EuropaServerType)object).getName();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_EuropaServerType_type") :
			getString("_UI_EuropaServerType_type") + " " + label;
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

		switch (notification.getFeatureID(EuropaServerType.class)) {
			case EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__NAME:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PORT:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT:
			case EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	@SuppressWarnings("restriction")
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return EsmConfigEditPlugin.INSTANCE;
	}

}
