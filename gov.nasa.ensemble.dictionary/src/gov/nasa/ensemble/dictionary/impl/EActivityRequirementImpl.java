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
package gov.nasa.ensemble.dictionary.impl;

import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityRequirement;
import gov.nasa.ensemble.dictionary.Period;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl#getPeriod <em>Period</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl#getEndOffset <em>End Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EActivityRequirementImpl extends MinimalEObjectImpl.Container implements EActivityRequirement {
	/**
	 * The default value of the '{@link #getPeriod() <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriod()
	 * @generated
	 * @ordered
	 */
	protected static final Period PERIOD_EDEFAULT = Period.REQUIRES_THROUGHOUT;

	/**
	 * The cached value of the '{@link #getPeriod() <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriod()
	 * @generated
	 * @ordered
	 */
	protected Period period = PERIOD_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EActivityRequirementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EACTIVITY_REQUIREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Period getPeriod() {
		return period;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriod(Period newPeriod) {
		Period oldPeriod = period;
		period = newPeriod == null ? PERIOD_EDEFAULT : newPeriod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_REQUIREMENT__PERIOD, oldPeriod, period));
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
	 * @generated
	 */
	@Override
	public void setStartOffset(TemporalOffset newStartOffset) {
		TemporalOffset oldStartOffset = startOffset;
		startOffset = newStartOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_REQUIREMENT__START_OFFSET, oldStartOffset, startOffset));
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
	 * @generated
	 */
	@Override
	public void setEndOffset(TemporalOffset newEndOffset) {
		TemporalOffset oldEndOffset = endOffset;
		endOffset = newEndOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_REQUIREMENT__END_OFFSET, oldEndOffset, endOffset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.EACTIVITY_REQUIREMENT__PERIOD:
				return getPeriod();
			case DictionaryPackage.EACTIVITY_REQUIREMENT__START_OFFSET:
				return getStartOffset();
			case DictionaryPackage.EACTIVITY_REQUIREMENT__END_OFFSET:
				return getEndOffset();
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
			case DictionaryPackage.EACTIVITY_REQUIREMENT__PERIOD:
				setPeriod((Period)newValue);
				return;
			case DictionaryPackage.EACTIVITY_REQUIREMENT__START_OFFSET:
				setStartOffset((TemporalOffset)newValue);
				return;
			case DictionaryPackage.EACTIVITY_REQUIREMENT__END_OFFSET:
				setEndOffset((TemporalOffset)newValue);
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
			case DictionaryPackage.EACTIVITY_REQUIREMENT__PERIOD:
				setPeriod(PERIOD_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_REQUIREMENT__START_OFFSET:
				setStartOffset(START_OFFSET_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_REQUIREMENT__END_OFFSET:
				setEndOffset(END_OFFSET_EDEFAULT);
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
			case DictionaryPackage.EACTIVITY_REQUIREMENT__PERIOD:
				return period != PERIOD_EDEFAULT;
			case DictionaryPackage.EACTIVITY_REQUIREMENT__START_OFFSET:
				return START_OFFSET_EDEFAULT == null ? startOffset != null : !START_OFFSET_EDEFAULT.equals(startOffset);
			case DictionaryPackage.EACTIVITY_REQUIREMENT__END_OFFSET:
				return END_OFFSET_EDEFAULT == null ? endOffset != null : !END_OFFSET_EDEFAULT.equals(endOffset);
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
		result.append(" (period: ");
		result.append(period);
		result.append(", startOffset: ");
		result.append(startOffset);
		result.append(", endOffset: ");
		result.append(endOffset);
		result.append(')');
		return result.toString();
	}

} //EActivityRequirementImpl
