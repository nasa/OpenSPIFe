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

import java.util.Date;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constraint Point</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl#getAnchor <em>Anchor</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl#getElement <em>Element</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl#getEndpoint <em>Endpoint</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConstraintPointImpl extends MinimalEObjectImpl.Container implements ConstraintPoint {
	/**
	 * The default value of the '{@link #getAnchor() <em>Anchor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnchor()
	 * @generated
	 * @ordered
	 */
	protected static final String ANCHOR_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getAnchor() <em>Anchor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnchor()
	 * @generated
	 * @ordered
	 */
	protected String anchor = ANCHOR_EDEFAULT;
	/**
	 * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElement()
	 * @generated
	 * @ordered
	 */
	protected EPlanElement element;
	/**
	 * The default value of the '{@link #getEndpoint() <em>Endpoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndpoint()
	 * @generated
	 * @ordered
	 */
	protected static final Timepoint ENDPOINT_EDEFAULT = Timepoint.START;
	/**
	 * The cached value of the '{@link #getEndpoint() <em>Endpoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndpoint()
	 * @generated
	 * @ordered
	 */
	protected Timepoint endpoint = ENDPOINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstraintPointImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.CONSTRAINT_POINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAnchor() {
		return anchor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAnchor(String newAnchor) {
		String oldAnchor = anchor;
		anchor = newAnchor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.CONSTRAINT_POINT__ANCHOR, oldAnchor, anchor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EPlanElement getElement() {
		if (element != null && element.eIsProxy()) {
			InternalEObject oldElement = (InternalEObject)element;
			element = (EPlanElement)eResolveProxy(oldElement);
			if (element != oldElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConstraintsPackage.CONSTRAINT_POINT__ELEMENT, oldElement, element));
			}
		}
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlanElement basicGetElement() {
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setElement(EPlanElement newElement) {
		EPlanElement oldElement = element;
		element = newElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.CONSTRAINT_POINT__ELEMENT, oldElement, element));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Timepoint getEndpoint() {
		return endpoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndpoint(Timepoint newEndpoint) {
		Timepoint oldEndpoint = endpoint;
		endpoint = newEndpoint == null ? ENDPOINT_EDEFAULT : newEndpoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.CONSTRAINT_POINT__ENDPOINT, oldEndpoint, endpoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getAnchorElement() {
		if (getAnchor() != null) {
			return getAnchor();
		}
		return getEndpoint();
	}

	@Override
	public Date getDate() {
		EPlanElement element = getElement();
		if (element == null) {
			return null;
		}
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		Boolean scheduledA = temporalMember.getScheduled();
		if ((scheduledA != null) && !scheduledA.booleanValue()) {
			return null;
		}
		Object anchorElement = getAnchorElement();
		if (anchorElement instanceof Timepoint) {
			return temporalMember.getTimepointDate((Timepoint) anchorElement);
		} else if (anchorElement instanceof String) {
			EObject data = element.getData();
			if (data != null) {
				EClass eClass = data.eClass();
				EStructuralFeature feature = eClass.getEStructuralFeature((String) anchorElement);
				if (EcorePackage.Literals.EDATE == feature.getEType()) {
					return (Date) data.eGet(feature);
				}
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean hasEndpoint() {
		return getAnchor() == null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConstraintsPackage.CONSTRAINT_POINT__ANCHOR:
				return getAnchor();
			case ConstraintsPackage.CONSTRAINT_POINT__ELEMENT:
				if (resolve) return getElement();
				return basicGetElement();
			case ConstraintsPackage.CONSTRAINT_POINT__ENDPOINT:
				return getEndpoint();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConstraintsPackage.CONSTRAINT_POINT__ANCHOR:
				setAnchor((String)newValue);
				return;
			case ConstraintsPackage.CONSTRAINT_POINT__ELEMENT:
				setElement((EPlanElement)newValue);
				return;
			case ConstraintsPackage.CONSTRAINT_POINT__ENDPOINT:
				setEndpoint((Timepoint)newValue);
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
			case ConstraintsPackage.CONSTRAINT_POINT__ANCHOR:
				setAnchor(ANCHOR_EDEFAULT);
				return;
			case ConstraintsPackage.CONSTRAINT_POINT__ELEMENT:
				setElement((EPlanElement)null);
				return;
			case ConstraintsPackage.CONSTRAINT_POINT__ENDPOINT:
				setEndpoint(ENDPOINT_EDEFAULT);
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
			case ConstraintsPackage.CONSTRAINT_POINT__ANCHOR:
				return ANCHOR_EDEFAULT == null ? anchor != null : !ANCHOR_EDEFAULT.equals(anchor);
			case ConstraintsPackage.CONSTRAINT_POINT__ELEMENT:
				return element != null;
			case ConstraintsPackage.CONSTRAINT_POINT__ENDPOINT:
				return endpoint != ENDPOINT_EDEFAULT;
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
		result.append(" (anchor: ");
		result.append(anchor);
		result.append(", endpoint: ");
		result.append(endpoint);
		result.append(')');
		return result.toString();
	}

} //ConstraintPointImpl
