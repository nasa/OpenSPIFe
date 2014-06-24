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
package gov.nasa.ensemble.core.plan.resources.profile.provider;


import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcorePackage;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfileEqualityConstraintItemProvider
	extends ProfileReferenceItemProvider
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
	public ProfileEqualityConstraintItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addValueLiteralPropertyDescriptor(object);
			addMaximumGapPropertyDescriptor(object);
			addWaiverRationalePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Waiver Rationale feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWaiverRationalePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IWaivable_waiverRationale_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IWaivable_waiverRationale_feature", "_UI_IWaivable_type"),
				 AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Value Literal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addValueLiteralPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileEqualityConstraint_valueLiteral_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileEqualityConstraint_valueLiteral_feature", "_UI_ProfileEqualityConstraint_type"),
				 ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {

					@Override
					protected Collection<?> getComboBoxObjects(Object object) {
						Profile<?> profile = getProfile(object);
						if (profile != null) {
							List<String> comboBoxObjects = new ArrayList<String>();
							EDataType dataType = profile.getDataType();
							if (dataType instanceof EEnum) {
								EEnum eEnum = (EEnum) dataType;
								for (EEnumLiteral literal : eEnum.getELiterals()) {
									comboBoxObjects.add(literal.getLiteral());
								}
								return comboBoxObjects;
							} else if (EcorePackage.Literals.EBOOLEAN == dataType
										|| EcorePackage.Literals.EBOOLEAN_OBJECT == dataType) {
								comboBoxObjects.add(Boolean.toString(true));
								comboBoxObjects.add(Boolean.toString(false));
								return comboBoxObjects;
							}
						}
						return super.getComboBoxObjects(object);
					}
				
			});
	}

	/**
	 * This adds a property descriptor for the Maximum Gap feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaximumGapPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileEqualityConstraint_maximumGap_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileEqualityConstraint_maximumGap_feature", "_UI_ProfileEqualityConstraint_type"),
				 ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns ProfileEqualityConstraint.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProfileEqualityConstraint"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ProfileEqualityConstraint)object).getProfileKey();
		return label == null || label.length() == 0 ?
			getString("_UI_ProfileEqualityConstraint_type") :
			getString("_UI_ProfileEqualityConstraint_type") + " " + label;
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

		switch (notification.getFeatureID(ProfileEqualityConstraint.class)) {
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE:
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL:
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP:
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
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

}
