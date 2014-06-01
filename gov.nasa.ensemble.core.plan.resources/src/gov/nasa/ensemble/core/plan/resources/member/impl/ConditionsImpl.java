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
package gov.nasa.ensemble.core.plan.resources.member.impl;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.PowerLoad;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import gov.nasa.ensemble.core.plan.resources.util.ResourceConditionsUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conditions</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getTime <em>Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#isEditable <em>Editable</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#isActive <em>Active</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getClaims <em>Claims</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getPowerLoads <em>Power Loads</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getNumericResources <em>Numeric Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getStateResources <em>State Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getSharableResources <em>Sharable Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getUndefinedResources <em>Undefined Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl#getMember <em>Member</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConditionsImpl extends MinimalEObjectImpl.Container implements Conditions {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected static final Date TIME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getTime() <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTime()
	 * @generated
	 * @ordered
	 */
	protected Date time = TIME_EDEFAULT;
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;
	/**
	 * The default value of the '{@link #isEditable() <em>Editable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEditable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EDITABLE_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isEditable() <em>Editable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEditable()
	 * @generated
	 * @ordered
	 */
	protected static final int EDITABLE_EFLAG = 1 << 8;
	/**
	 * The default value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ACTIVE_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isActive() <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isActive()
	 * @generated
	 * @ordered
	 */
	protected static final int ACTIVE_EFLAG = 1 << 9;
	/**
	 * The cached value of the '{@link #getClaims() <em>Claims</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClaims()
	 * @generated
	 * @ordered
	 */
	protected EList<Claim> claims;
	/**
	 * The cached value of the '{@link #getPowerLoads() <em>Power Loads</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPowerLoads()
	 * @generated
	 * @ordered
	 */
	protected EList<PowerLoad> powerLoads;
	/**
	 * The cached value of the '{@link #getNumericResources() <em>Numeric Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumericResources()
	 * @generated
	 * @ordered
	 */
	protected EList<NumericResource> numericResources;
	/**
	 * The cached value of the '{@link #getStateResources() <em>State Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateResources()
	 * @generated
	 * @ordered
	 */
	protected EList<StateResource> stateResources;
	/**
	 * The cached value of the '{@link #getSharableResources() <em>Sharable Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSharableResources()
	 * @generated
	 * @ordered
	 */
	protected EList<SharableResource> sharableResources;

	/**
	 * The cached value of the '{@link #getUndefinedResources() <em>Undefined Resources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUndefinedResources()
	 * @generated
	 * @ordered
	 */
	protected EList<UndefinedResource> undefinedResources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConditionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MemberPackage.Literals.CONDITIONS;
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
			case MemberPackage.CONDITIONS__TIME:
				setTime((Date)newValue);
				return;
			case MemberPackage.CONDITIONS__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case MemberPackage.CONDITIONS__EDITABLE:
				setEditable((Boolean)newValue);
				return;
			case MemberPackage.CONDITIONS__ACTIVE:
				setActive((Boolean)newValue);
				return;
			case MemberPackage.CONDITIONS__CLAIMS:
				getClaims().clear();
				getClaims().addAll((Collection<? extends Claim>)newValue);
				return;
			case MemberPackage.CONDITIONS__POWER_LOADS:
				getPowerLoads().clear();
				getPowerLoads().addAll((Collection<? extends PowerLoad>)newValue);
				return;
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				getNumericResources().clear();
				getNumericResources().addAll((Collection<? extends NumericResource>)newValue);
				return;
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				getStateResources().clear();
				getStateResources().addAll((Collection<? extends StateResource>)newValue);
				return;
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				getSharableResources().clear();
				getSharableResources().addAll((Collection<? extends SharableResource>)newValue);
				return;
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				getUndefinedResources().clear();
				getUndefinedResources().addAll((Collection<? extends UndefinedResource>)newValue);
				return;
			case MemberPackage.CONDITIONS__MEMBER:
				setMember((ResourceConditionsMember)newValue);
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
			case MemberPackage.CONDITIONS__TIME:
				setTime(TIME_EDEFAULT);
				return;
			case MemberPackage.CONDITIONS__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case MemberPackage.CONDITIONS__EDITABLE:
				setEditable(EDITABLE_EDEFAULT);
				return;
			case MemberPackage.CONDITIONS__ACTIVE:
				setActive(ACTIVE_EDEFAULT);
				return;
			case MemberPackage.CONDITIONS__CLAIMS:
				getClaims().clear();
				return;
			case MemberPackage.CONDITIONS__POWER_LOADS:
				getPowerLoads().clear();
				return;
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				getNumericResources().clear();
				return;
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				getStateResources().clear();
				return;
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				getSharableResources().clear();
				return;
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				getUndefinedResources().clear();
				return;
			case MemberPackage.CONDITIONS__MEMBER:
				setMember((ResourceConditionsMember)null);
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
			case MemberPackage.CONDITIONS__TIME:
				return TIME_EDEFAULT == null ? time != null : !TIME_EDEFAULT.equals(time);
			case MemberPackage.CONDITIONS__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case MemberPackage.CONDITIONS__EDITABLE:
				return ((eFlags & EDITABLE_EFLAG) != 0) != EDITABLE_EDEFAULT;
			case MemberPackage.CONDITIONS__ACTIVE:
				return ((eFlags & ACTIVE_EFLAG) != 0) != ACTIVE_EDEFAULT;
			case MemberPackage.CONDITIONS__CLAIMS:
				return claims != null && !claims.isEmpty();
			case MemberPackage.CONDITIONS__POWER_LOADS:
				return powerLoads != null && !powerLoads.isEmpty();
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				return numericResources != null && !numericResources.isEmpty();
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				return stateResources != null && !stateResources.isEmpty();
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				return sharableResources != null && !sharableResources.isEmpty();
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				return undefinedResources != null && !undefinedResources.isEmpty();
			case MemberPackage.CONDITIONS__MEMBER:
				return getMember() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (time: ");
		result.append(time);
		result.append(", description: ");
		result.append(description);
		result.append(", editable: ");
		result.append((eFlags & EDITABLE_EFLAG) != 0);
		result.append(", active: ");
		result.append((eFlags & ACTIVE_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

	private void setStartTimeFromPlanElement() {
		if (eContainer instanceof ResourceConditionsMember) {
			ResourceConditionsMember member = (ResourceConditionsMember) eContainer;
			EPlanElement planElement = member.getPlanElement();
			if (planElement != null) {
				TemporalMember temporal = planElement.getMember(TemporalMember.class);
				Date startTime = temporal.getStartTime();
				if (startTime != null) {
					ResourceConditionsUtils.setConditionsDate(this, startTime);
				}
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Date getTime() {
		if (!eIsSet(MemberPackage.Literals.CONDITIONS__TIME)) {
			boolean delivering = eDeliver();
			eSetDeliver(false);
			setStartTimeFromPlanElement();
			eSetDeliver(delivering);
		}
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTime(Date newTime) {
		Date oldTime = time;
		time = newTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CONDITIONS__TIME, oldTime, time));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CONDITIONS__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isEditable() {
		return (eFlags & EDITABLE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEditable(boolean newEditable) {
		boolean oldEditable = (eFlags & EDITABLE_EFLAG) != 0;
		if (newEditable) eFlags |= EDITABLE_EFLAG; else eFlags &= ~EDITABLE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CONDITIONS__EDITABLE, oldEditable, newEditable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isActive() {
		return (eFlags & ACTIVE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setActive(boolean newActive) {
		boolean oldActive = (eFlags & ACTIVE_EFLAG) != 0;
		if (newActive) eFlags |= ACTIVE_EFLAG; else eFlags &= ~ACTIVE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CONDITIONS__ACTIVE, oldActive, newActive));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Claim> getClaims() {
		if (claims == null) {
			claims = new EObjectContainmentWithInverseEList<Claim>(Claim.class, this, MemberPackage.CONDITIONS__CLAIMS, MemberPackage.CLAIM__CONDITIONS);
		}
		return claims;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PowerLoad> getPowerLoads() {
		if (powerLoads == null) {
			powerLoads = new EObjectContainmentWithInverseEList<PowerLoad>(PowerLoad.class, this, MemberPackage.CONDITIONS__POWER_LOADS, MemberPackage.POWER_LOAD__CONDITIONS);
		}
		return powerLoads;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<NumericResource> getNumericResources() {
		if (numericResources == null) {
			numericResources = new EObjectContainmentWithInverseEList<NumericResource>(NumericResource.class, this, MemberPackage.CONDITIONS__NUMERIC_RESOURCES, MemberPackage.NUMERIC_RESOURCE__CONDITIONS);
		}
		return numericResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<StateResource> getStateResources() {
		if (stateResources == null) {
			stateResources = new EObjectContainmentWithInverseEList<StateResource>(StateResource.class, this, MemberPackage.CONDITIONS__STATE_RESOURCES, MemberPackage.STATE_RESOURCE__CONDITIONS);
		}
		return stateResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SharableResource> getSharableResources() {
		if (sharableResources == null) {
			sharableResources = new EObjectContainmentWithInverseEList<SharableResource>(SharableResource.class, this, MemberPackage.CONDITIONS__SHARABLE_RESOURCES, MemberPackage.SHARABLE_RESOURCE__CONDITIONS);
		}
		return sharableResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<UndefinedResource> getUndefinedResources() {
		if (undefinedResources == null) {
			undefinedResources = new EObjectContainmentWithInverseEList<UndefinedResource>(UndefinedResource.class, this, MemberPackage.CONDITIONS__UNDEFINED_RESOURCES, MemberPackage.UNDEFINED_RESOURCE__CONDITIONS);
		}
		return undefinedResources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceConditionsMember getMember() {
		if (eContainerFeatureID() != MemberPackage.CONDITIONS__MEMBER) return null;
		return (ResourceConditionsMember)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMember(ResourceConditionsMember newMember, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newMember, MemberPackage.CONDITIONS__MEMBER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMember(ResourceConditionsMember newMember) {
		if (newMember != eInternalContainer() || (eContainerFeatureID() != MemberPackage.CONDITIONS__MEMBER && newMember != null)) {
			if (EcoreUtil.isAncestor(this, newMember))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newMember != null)
				msgs = ((InternalEObject)newMember).eInverseAdd(this, MemberPackage.RESOURCE_CONDITIONS_MEMBER__CONDITIONS, ResourceConditionsMember.class, msgs);
			msgs = basicSetMember(newMember, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CONDITIONS__MEMBER, newMember, newMember));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.CONDITIONS__CLAIMS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getClaims()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__POWER_LOADS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPowerLoads()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getNumericResources()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getStateResources()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSharableResources()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getUndefinedResources()).basicAdd(otherEnd, msgs);
			case MemberPackage.CONDITIONS__MEMBER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetMember((ResourceConditionsMember)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.CONDITIONS__CLAIMS:
				return ((InternalEList<?>)getClaims()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__POWER_LOADS:
				return ((InternalEList<?>)getPowerLoads()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				return ((InternalEList<?>)getNumericResources()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				return ((InternalEList<?>)getStateResources()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				return ((InternalEList<?>)getSharableResources()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				return ((InternalEList<?>)getUndefinedResources()).basicRemove(otherEnd, msgs);
			case MemberPackage.CONDITIONS__MEMBER:
				return basicSetMember(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case MemberPackage.CONDITIONS__MEMBER:
				return eInternalContainer().eInverseRemove(this, MemberPackage.RESOURCE_CONDITIONS_MEMBER__CONDITIONS, ResourceConditionsMember.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MemberPackage.CONDITIONS__TIME:
				return getTime();
			case MemberPackage.CONDITIONS__DESCRIPTION:
				return getDescription();
			case MemberPackage.CONDITIONS__EDITABLE:
				return isEditable();
			case MemberPackage.CONDITIONS__ACTIVE:
				return isActive();
			case MemberPackage.CONDITIONS__CLAIMS:
				return getClaims();
			case MemberPackage.CONDITIONS__POWER_LOADS:
				return getPowerLoads();
			case MemberPackage.CONDITIONS__NUMERIC_RESOURCES:
				return getNumericResources();
			case MemberPackage.CONDITIONS__STATE_RESOURCES:
				return getStateResources();
			case MemberPackage.CONDITIONS__SHARABLE_RESOURCES:
				return getSharableResources();
			case MemberPackage.CONDITIONS__UNDEFINED_RESOURCES:
				return getUndefinedResources();
			case MemberPackage.CONDITIONS__MEMBER:
				return getMember();
		}
		return super.eGet(featureID, resolve, coreType);
	}

} //ConditionsImpl
