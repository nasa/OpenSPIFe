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

import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getStartOffsetAmount <em>Start Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getEndOffset <em>End Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getEndOffsetAmount <em>End Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getEndOffsetTimepoint <em>End Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl#getProfileKey <em>Profile Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileReferenceImpl extends MinimalEObjectImpl.Container implements ProfileReference {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated NOT
	 * @ordered
	 */
	protected String id = EcoreUtil.generateUUID();

	/**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> metadata;

	/**
	 * The default value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalOffset START_OFFSET_EDEFAULT = (TemporalOffset)JScienceFactory.eINSTANCE.createFromString(JSciencePackage.eINSTANCE.getTemporalOffset(), "START, 0 s");

	/**
	 * The cached value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected TemporalOffset startOffset = START_OFFSET_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartOffsetTimepoint() <em>Start Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected static final Timepoint START_OFFSET_TIMEPOINT_EDEFAULT = Timepoint.START;

	/**
	 * The default value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalOffset END_OFFSET_EDEFAULT = (TemporalOffset)JScienceFactory.eINSTANCE.createFromString(JSciencePackage.eINSTANCE.getTemporalOffset(), "END, 0 s");

	/**
	 * The cached value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected TemporalOffset endOffset = END_OFFSET_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndOffsetTimepoint() <em>End Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected static final Timepoint END_OFFSET_TIMEPOINT_EDEFAULT = Timepoint.START;

	/**
	 * The default value of the '{@link #getProfileKey() <em>Profile Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfileKey()
	 * @generated
	 * @ordered
	 */
	protected static final String PROFILE_KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProfileKey() <em>Profile Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfileKey()
	 * @generated
	 * @ordered
	 */
	protected String profileKey = PROFILE_KEY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.PROFILE_REFERENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, String> getMetadata() {
		if (metadata == null) {
			metadata = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, ProfilePackage.PROFILE_REFERENCE__METADATA);
		}
		return metadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalOffset getEndOffset() {
		return endOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setEndOffset(TemporalOffset newEndOffset) {
		TemporalOffset oldEndOffset = endOffset;
		endOffset = newEndOffset;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__END_OFFSET, oldEndOffset, endOffset));
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT, oldEndOffset.getTimepoint(), endOffset.getTimepoint()));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Amount<Duration> getEndOffsetAmount() {
		return getEndOffset().getOffset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setEndOffsetAmount(Amount<Duration> newEndOffsetAmount) {
		Amount<Duration> oldEndOffsetAmount = getEndOffsetAmount();
		setEndOffset(getEndOffset().setOffset(newEndOffsetAmount));
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT, oldEndOffsetAmount, newEndOffsetAmount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Timepoint getEndOffsetTimepoint() {
		return getEndOffset().getTimepoint();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setEndOffsetTimepoint(Timepoint newEndOffsetTimepoint) {
		Timepoint oldEndOffsetTimepoint = getEndOffsetTimepoint();
		setEndOffset(getEndOffset().setTimepoint(newEndOffsetTimepoint));
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT, oldEndOffsetTimepoint, newEndOffsetTimepoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProfileKey() {
		return profileKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfileKey(String newProfileKey) {
		String oldProfileKey = profileKey;
		profileKey = newProfileKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY, oldProfileKey, profileKey));
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalOffset getStartOffset() {
		return startOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setStartOffset(TemporalOffset newStartOffset) {
		TemporalOffset oldStartOffset = startOffset;
		startOffset = newStartOffset;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__START_OFFSET, oldStartOffset, startOffset));
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT, oldStartOffset.getTimepoint(), startOffset.getTimepoint()));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Amount<Duration> getStartOffsetAmount() {
		return getStartOffset().getOffset();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setStartOffsetAmount(Amount<Duration> newStartOffsetAmount) {
		Amount<Duration> oldStartOffsetAmount = getStartOffsetAmount();
		setStartOffset(getStartOffset().setOffset(newStartOffsetAmount));
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT, oldStartOffsetAmount, newStartOffsetAmount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Timepoint getStartOffsetTimepoint() {
		return getStartOffset().getTimepoint();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setStartOffsetTimepoint(Timepoint newStartOffsetTimepoint) {
		Timepoint oldStartOffsetTimepoint = getStartOffsetTimepoint();
		setStartOffset(getStartOffset().setTimepoint(newStartOffsetTimepoint));
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT, oldStartOffsetTimepoint, newStartOffsetTimepoint));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
				return ((InternalEList<?>)getMetadata()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProfilePackage.PROFILE_REFERENCE__ID:
				return getId();
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
				if (coreType) return getMetadata();
				else return getMetadata().map();
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET:
				return getStartOffset();
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT:
				return getStartOffsetAmount();
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT:
				return getStartOffsetTimepoint();
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET:
				return getEndOffset();
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT:
				return getEndOffsetAmount();
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT:
				return getEndOffsetTimepoint();
			case ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY:
				return getProfileKey();
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
			case ProfilePackage.PROFILE_REFERENCE__ID:
				setId((String)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
				((EStructuralFeature.Setting)getMetadata()).set(newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET:
				setStartOffset((TemporalOffset)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT:
				setStartOffsetAmount((Amount<Duration>)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT:
				setStartOffsetTimepoint((Timepoint)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET:
				setEndOffset((TemporalOffset)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT:
				setEndOffsetAmount((Amount<Duration>)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT:
				setEndOffsetTimepoint((Timepoint)newValue);
				return;
			case ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY:
				setProfileKey((String)newValue);
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
			case ProfilePackage.PROFILE_REFERENCE__ID:
				setId(ID_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
				getMetadata().clear();
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET:
				setStartOffset(START_OFFSET_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT:
				setStartOffsetAmount((Amount<Duration>)null);
				return;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT:
				setStartOffsetTimepoint(START_OFFSET_TIMEPOINT_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET:
				setEndOffset(END_OFFSET_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT:
				setEndOffsetAmount((Amount<Duration>)null);
				return;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT:
				setEndOffsetTimepoint(END_OFFSET_TIMEPOINT_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY:
				setProfileKey(PROFILE_KEY_EDEFAULT);
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
			case ProfilePackage.PROFILE_REFERENCE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ProfilePackage.PROFILE_REFERENCE__METADATA:
				return metadata != null && !metadata.isEmpty();
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET:
				return START_OFFSET_EDEFAULT == null ? startOffset != null : !START_OFFSET_EDEFAULT.equals(startOffset);
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_AMOUNT:
				return getStartOffsetAmount() != null;
			case ProfilePackage.PROFILE_REFERENCE__START_OFFSET_TIMEPOINT:
				return getStartOffsetTimepoint() != START_OFFSET_TIMEPOINT_EDEFAULT;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET:
				return END_OFFSET_EDEFAULT == null ? endOffset != null : !END_OFFSET_EDEFAULT.equals(endOffset);
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_AMOUNT:
				return getEndOffsetAmount() != null;
			case ProfilePackage.PROFILE_REFERENCE__END_OFFSET_TIMEPOINT:
				return getEndOffsetTimepoint() != END_OFFSET_TIMEPOINT_EDEFAULT;
			case ProfilePackage.PROFILE_REFERENCE__PROFILE_KEY:
				return PROFILE_KEY_EDEFAULT == null ? profileKey != null : !PROFILE_KEY_EDEFAULT.equals(profileKey);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer("Beginning ");
		result.append(duration(startOffset)).append(" from ").append(startOffset.getTimepoint().getName().toLowerCase());
		result.append(" and lasting until ");
		result.append(duration(endOffset)).append(" from ").append(endOffset.getTimepoint().getName().toLowerCase());
		result.append(", ").append(profileKey);

		return result.toString();
	}
	
	private String duration(TemporalOffset offset) {
		return DurationFormat.getEnglishDuration(offset.getOffset().to(SI.SECOND).getExactValue());
	}

	/**
	 * Returns whether the two references are functionally equivalent.
	 * 
	 * This isn't the default equals method since it would imply a changing
	 * hashCode for a ProfileReference over time, if the reference was
	 * updated by the user.  That has other bad implications for our APIs.
	 * 
	 * @param reference1
	 * @param reference2
	 * @return
	 */
	public static boolean equals(ProfileReference reference1, ProfileReference reference2) {
		if (reference1 == reference2) {
			return true;
		}
		if (reference2 == null) {
			return false;
		}
		if (reference1.getClass() != reference2.getClass()) {
			return false;
		}
		ProfileReferenceImpl impl1 = (ProfileReferenceImpl) reference1;
		ProfileReferenceImpl impl2 = (ProfileReferenceImpl) reference2;
		if (impl1.endOffset == null) {
			if (impl2.endOffset != null) {
				return false;
			}
		} else if (!impl1.endOffset.equals(impl2.endOffset)) {
			return false;
		}
		if (impl1.profileKey == null) {
			if (impl2.profileKey != null) {
				return false;
			}
		} else if (!impl1.profileKey.equals(impl2.profileKey)) {
			return false;
		}
		if (impl1.startOffset == null) {
			if (impl2.startOffset != null) {
				return false;
			}
		} else if (!impl1.startOffset.equals(impl2.startOffset)) {
			return false;
		}
		return true;
	}
	
} //ProfileReferenceImpl
