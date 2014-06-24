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
package gov.nasa.ensemble.core.model.plan.constraints.impl;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Temporal Chain</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl#getWaiverRationale <em>Waiver Rationale</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TemporalChainImpl extends MinimalEObjectImpl.Container implements TemporalChain {
	/**
	 * The default value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected static final String WAIVER_RATIONALE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected String waiverRationale = WAIVER_RATIONALE_EDEFAULT;
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
	 * The cached value of the '{@link #getElements() <em>Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EPlanElement> elements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemporalChainImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.TEMPORAL_CHAIN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getWaiverRationale() {
		return waiverRationale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWaiverRationale(String newWaiverRationale) {
		String oldWaiverRationale = waiverRationale;
		waiverRationale = newWaiverRationale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE, oldWaiverRationale, waiverRationale));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.TEMPORAL_CHAIN__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EPlanElement> getElements() {
		if (elements == null) {
			elements = new EObjectResolvingEList<EPlanElement>(EPlanElement.class, this, ConstraintsPackage.TEMPORAL_CHAIN__ELEMENTS);
		}
		return elements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE:
				return getWaiverRationale();
			case ConstraintsPackage.TEMPORAL_CHAIN__ID:
				return getId();
			case ConstraintsPackage.TEMPORAL_CHAIN__ELEMENTS:
				return getElements();
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
			case ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE:
				setWaiverRationale((String)newValue);
				return;
			case ConstraintsPackage.TEMPORAL_CHAIN__ID:
				setId((String)newValue);
				return;
			case ConstraintsPackage.TEMPORAL_CHAIN__ELEMENTS:
				getElements().clear();
				getElements().addAll((Collection<? extends EPlanElement>)newValue);
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
			case ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE:
				setWaiverRationale(WAIVER_RATIONALE_EDEFAULT);
				return;
			case ConstraintsPackage.TEMPORAL_CHAIN__ID:
				setId(ID_EDEFAULT);
				return;
			case ConstraintsPackage.TEMPORAL_CHAIN__ELEMENTS:
				getElements().clear();
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
			case ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE:
				return WAIVER_RATIONALE_EDEFAULT == null ? waiverRationale != null : !WAIVER_RATIONALE_EDEFAULT.equals(waiverRationale);
			case ConstraintsPackage.TEMPORAL_CHAIN__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ConstraintsPackage.TEMPORAL_CHAIN__ELEMENTS:
				return elements != null && !elements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == IWaivable.class) {
			switch (derivedFeatureID) {
				case ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE: return AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == IWaivable.class) {
			switch (baseFeatureID) {
				case AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE: return ConstraintsPackage.TEMPORAL_CHAIN__WAIVER_RATIONALE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (waiverRationale: ");
		result.append(waiverRationale);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int size = in.readInt();
		List<EPlanElement> elements = new ArrayList<EPlanElement>(size);
		for (int i = 0 ; i < size ; i++) {
			EPlanElement element = (EPlanElement)in.readObject();
			elements.add(element);
		}
		getElements().addAll(elements);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		List<EPlanElement> elements = getElements();
		out.writeInt(elements.size());
		for (EPlanElement element : elements) {
			out.writeObject(element);
		}
	}

} //TemporalChainImpl
