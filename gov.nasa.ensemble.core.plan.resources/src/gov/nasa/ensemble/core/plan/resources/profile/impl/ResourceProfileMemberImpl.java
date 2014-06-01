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
package gov.nasa.ensemble.core.plan.resources.profile.impl;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Profile Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ResourceProfileMemberImpl#getResourceProfiles <em>Resource Profiles</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResourceProfileMemberImpl extends MinimalEObjectImpl.Container implements ResourceProfileMember {
	
	/**
	 * The cached value of the '{@link #getResourceProfiles() <em>Resource Profiles</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<Profile<?>> resourceProfiles;
	
	protected Map<String, Profile<?>> profileById = new HashMap<String, Profile<?>>();
	
	protected boolean initialized = false;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected ResourceProfileMemberImpl() {
		super();
		eAdapters().add(new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == msg.getFeature()) {
					for (Profile profile : EMFUtils.getRemovedObjects(msg, Profile.class)) {
						profileById.remove(profile.getId());
					}
					for (Profile profile : EMFUtils.getAddedObjects(msg, Profile.class)) {
						Profile<?> oldProfile = profileById.get(profile.getId());
						if (oldProfile != null) {
							// Remove old profile from the list of profiles if there is an id conflict
							getResourceProfiles().remove(oldProfile);
							if (oldProfile.eResource() == eResource()) {
								// Remove the old profile from the resource if there is an id conflict
								eResource().getContents().remove(oldProfile);
							}
							// "Reset" the proxy uri of the loaded profile so that things can point to it again
							((BasicEObjectImpl)oldProfile).eSetProxyURI(EcoreUtil.getURI(profile));
						}
						profileById.put(profile.getId(), profile);
					}
				}
			}
		});
	}

	@Override
	public void dispose() {
		profileById.clear();
		resourceProfiles = null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<Profile<?>> getResourceProfiles() {
		if (resourceProfiles == null) {
			resourceProfiles = new EObjectResolvingEList<Profile<?>>(Profile.class, this, ProfilePackage.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES);
		}
		return resourceProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Profile<?> getProfile(String name) {
		return profileById.get(name);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProfilePackage.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES:
				return getResourceProfiles();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ProfilePackage.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES:
				getResourceProfiles().clear();
				getResourceProfiles().addAll((Collection<? extends Profile<?>>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ProfilePackage.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES:
				getResourceProfiles().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ProfilePackage.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES:
				return resourceProfiles != null && !resourceProfiles.isEmpty();
		}
		return super.eIsSet(featureID);
	}
	
} //ResourceProfileMemberImpl
