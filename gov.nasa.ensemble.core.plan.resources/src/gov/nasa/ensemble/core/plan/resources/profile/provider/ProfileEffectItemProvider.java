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


import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfileEffectItemProvider
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
	public ProfileEffectItemProvider(AdapterFactory adapterFactory) {
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

			addStartValueLiteralPropertyDescriptor(object);
			addEndValueLiteralPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	@Override
	protected void addProfileKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ProfileKeyPropertyDescriptor((ComposeableAdapterFactory)adapterFactory) {

			@Override
			public Collection<?> getChoiceOfValues(Object object) {
				return getProfileKeys((ProfileReference) object, new Predicate<Profile<?>>() {
					@Override
					public boolean apply(Profile<?> profile) {
						boolean isExternalCondition = profile.isExternalCondition();
						boolean isComputedProfile = profile instanceof StructuralFeatureProfile;
						return !isExternalCondition && !isComputedProfile;
					}
				});
			}
			
		});
	}

	/**
	 * This adds a property descriptor for the Start Value Literal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addStartValueLiteralPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileEffect_startValueLiteral_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileEffect_startValueLiteral_feature", "_UI_ProfileEffect_type"),
				 ProfilePackage.Literals.PROFILE_EFFECT__START_VALUE_LITERAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {
				@Override
				protected Object getValue(EObject object, EStructuralFeature feature) {
					if (object instanceof ProfileEffect) {
						ProfileEffect profileEffect = (ProfileEffect) object;
						String effectLiteral = profileEffect.getEffectLiteral(Timepoint.START);
						return effectLiteral;
					}
					return super.getValue(object, feature);
				}
			});
	}

	/**
	 * This adds a property descriptor for the End Value Literal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addEndValueLiteralPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileEffect_endValueLiteral_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileEffect_endValueLiteral_feature", "_UI_ProfileEffect_type"),
				 ProfilePackage.Literals.PROFILE_EFFECT__END_VALUE_LITERAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {
				@Override
				protected Object getValue(EObject object, EStructuralFeature feature) {
					if (object instanceof ProfileEffect) {
						ProfileEffect profileEffect = (ProfileEffect) object;
						String effectLiteral = profileEffect.getEffectLiteral(Timepoint.END);
						return effectLiteral;
					}
					return super.getValue(object, feature);
				}
			});
	}

	/**
	 * This returns ProfileEffect.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProfileEffect"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ProfileEffect)object).getProfileKey();
		return label == null || label.length() == 0 ?
			getString("_UI_ProfileEffect_type") :
			getString("_UI_ProfileEffect_type") + " " + label;
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

		switch (notification.getFeatureID(ProfileEffect.class)) {
			case ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL:
			case ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL:
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
