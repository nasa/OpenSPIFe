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


import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.CopyCommand.Helper;
import org.eclipse.emf.edit.domain.EditingDomain;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfileReferenceItemProvider
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
	public ProfileReferenceItemProvider(AdapterFactory adapterFactory) {
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

			addProfileKeyPropertyDescriptor(object);
			addStartOffsetAmountPropertyDescriptor(object);
			addStartOffsetTimepointPropertyDescriptor(object);
			addEndOffsetAmountPropertyDescriptor(object);
			addEndOffsetTimepointPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Profile Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addProfileKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(new ProfileKeyPropertyDescriptor((ComposeableAdapterFactory)adapterFactory));
	}

	/**
	 * This adds a property descriptor for the Start Offset Amount feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartOffsetAmountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileReference_startOffsetAmount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileReference_startOffsetAmount_feature", "_UI_ProfileReference_type"),
				 ProfilePackage.Literals.PROFILE_REFERENCE__START_OFFSET_AMOUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Offset Timepoint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartOffsetTimepointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileReference_startOffsetTimepoint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileReference_startOffsetTimepoint_feature", "_UI_ProfileReference_type"),
				 ProfilePackage.Literals.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Offset Amount feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndOffsetAmountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileReference_endOffsetAmount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileReference_endOffsetAmount_feature", "_UI_ProfileReference_type"),
				 ProfilePackage.Literals.PROFILE_REFERENCE__END_OFFSET_AMOUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Offset Timepoint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndOffsetTimepointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfileReference_endOffsetTimepoint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfileReference_endOffsetTimepoint_feature", "_UI_ProfileReference_type"),
				 ProfilePackage.Literals.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	protected void addStartOffsetTimepointDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor(
				 ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(), 
				 "Start timepoint",
				 "Relevant timepoint",
				 ProfilePackage.Literals.PROFILE_REFERENCE__START_OFFSET,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {

					@Override
					protected Collection<?> getComboBoxObjects(Object object) {
						return Timepoint.VALUES;
					}

					@Override
					public Object getPropertyValue(Object object) {
						TemporalOffset oldOffset = getPropertyValueUnwrapped(object);
						return oldOffset == null ? null : oldOffset.getTimepoint();
					}

					@Override
					public void setPropertyValue(Object object, Object value) {
						TemporalOffset oldOffset = getPropertyValueUnwrapped(object);
						TemporalOffset newOffset = oldOffset.setTimepoint((Timepoint) value);
						super.setPropertyValue(object, newOffset);
					}

					private TemporalOffset getPropertyValueUnwrapped(Object object) {
						TemporalOffset oldOffset;
						Object propertyValue = super.getPropertyValue(object);
						if (propertyValue instanceof PropertyValueWrapper) {
							oldOffset = (TemporalOffset) ((PropertyValueWrapper) propertyValue).getEditableValue(object);
						} else {
							oldOffset = (TemporalOffset) propertyValue;
						}
						return oldOffset;
					}

			});
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
			childrenFeatures.add(ProfilePackage.Literals.PROFILE_REFERENCE__METADATA);
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
	 * This returns ProfileReference.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProfileReference"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((ProfileReference)object).getProfileKey();
		return label == null || label.length() == 0 ?
			getString("_UI_ProfileReference_type") :
			getString("_UI_ProfileReference_type") + " " + label;
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

		switch (notification.getFeatureID(ProfileReference.class)) {
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET:
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT:
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT:
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET:
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT:
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT:
			case ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
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
				(ProfilePackage.Literals.PROFILE_REFERENCE__METADATA,
				 EcoreFactory.eINSTANCE.create(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY)));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ResourceProfileEditPlugin.INSTANCE;
	}
	
	@Override
	protected Command createCopyCommand(EditingDomain domain, EObject owner, Helper helper) {
		return new CopyCommand(domain, owner, helper, domain.getOptimizeCopy()) {
			@Override
			public void execute() {
				super.execute();
				for (Object object : getResult()) {
					if (object instanceof ProfileReference) {
						((ProfileReference) object).setId(EcoreUtil.generateUUID());
					}
				}
			}
			
		};
	}

	protected Profile<?> getProfile(Object object) {
		ProfileReference reference = (ProfileReference)object;
		EPlan plan = EPlanUtils.getPlan(reference);
		if (plan == null) {
			plan = EPlanUtils.getPlanFromResourceSet(reference);
		}
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		if (member==null) {
			return null;
		}
		String id = reference.getProfileKey();
		Profile<?> profile = member.getProfile(id);
		return profile;
	}

	protected class ProfileKeyPropertyDescriptor extends ItemPropertyDescriptor {
		
		protected ProfileKeyPropertyDescriptor(ComposeableAdapterFactory adapterFactory) {
			super(adapterFactory.getRootAdapterFactory(), getResourceLocator(), getString("_UI_ProfileReference_profileKey_feature"),
					getString("_UI_PropertyDescriptor_description", "_UI_ProfileReference_profileKey_feature", "_UI_ProfileReference_type"), ProfilePackage.Literals.PROFILE_REFERENCE__PROFILE_KEY, true, false, false,
					ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null);
		}

		@Override
		public Object getPropertyValue(Object object) {
			return getProfile(object);
		}

		@Override
		public IItemLabelProvider getLabelProvider(Object object) {
			ProfileReference reference = (ProfileReference) object;
			final EPlan plan = EPlanUtils.getPlan(reference);
			return new IItemLabelProvider() {
				
				@Override
				public String getText(Object object) {
					Profile<?> profile = null;
					if (object instanceof Profile) {
						profile = (Profile<?>) object;
					} else if (object instanceof String) {
						String id = (String) object;
						ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
						profile = member.getProfile(id);
					}
					if (profile != null) {
						IItemLabelProvider lp = EMFUtils.adapt(profile, IItemLabelProvider.class);
						return lp.getText(profile);
					}
					return null;
				}
				
				@Override
				public Object getImage(Object object) {
					Profile<?> profile = null;
					if (object instanceof Profile) {
						profile = (Profile<?>) object;
					} else if (object instanceof String) {
						String id = (String) object;
						ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
						profile = member.getProfile(id);
					}
					if (profile != null) {
						IItemLabelProvider lp = EMFUtils.adapt(profile, IItemLabelProvider.class);
						return lp.getImage(profile);
					}
					return null;
				}
			};
		}

		@Override
		public Collection<?> getChoiceOfValues(Object object) {
			return getProfileKeys((ProfileReference) object, new Predicate<Profile<?>>() {
				@Override public boolean apply(Profile<?> value) { return true; }
			});
		}

		protected Collection<?> getProfileKeys(ProfileReference reference, Predicate<Profile<?>> predicate) {
			EPlan plan = EPlanUtils.getPlan(reference);
			ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
			EList<Profile<?>> profiles = member.getResourceProfiles();
			List<Profile<?>> filtered = Lists.filter(profiles, predicate);
			List<Profile<?>> profileKeys = new ArrayList<Profile<?>>();
			for (Profile profile : filtered) {
				profileKeys.add(profile);
			}
			Collections.sort(profileKeys, new Comparator<Profile>() {
				@Override
				public int compare(Profile o1, Profile o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});
			return profileKeys;
		}

		@Override
		public void setPropertyValue(Object object, Object value) {
			if (value instanceof Profile) {
				value = ((Profile)value).getId();
			}
			super.setPropertyValue(object, value);
		}
	}

}
