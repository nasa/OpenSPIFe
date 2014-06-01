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
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Effect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl#getStartValueLiteral <em>Start Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl#getEndValueLiteral <em>End Value Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileEffectImpl extends ProfileReferenceImpl implements ProfileEffect {
	/**
	 * The default value of the '{@link #getStartValueLiteral() <em>Start Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String START_VALUE_LITERAL_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getStartValueLiteral() <em>Start Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected String startValueLiteral = START_VALUE_LITERAL_EDEFAULT;
	/**
	 * The default value of the '{@link #getEndValueLiteral() <em>End Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String END_VALUE_LITERAL_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getEndValueLiteral() <em>End Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected String endValueLiteral = END_VALUE_LITERAL_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileEffectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.PROFILE_EFFECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getStartValueLiteral() {
		return startValueLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartValueLiteral(String newStartValueLiteral) {
		String oldStartValueLiteral = startValueLiteral;
		startValueLiteral = newStartValueLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL, oldStartValueLiteral, startValueLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getEndValueLiteral() {
		return endValueLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndValueLiteral(String newEndValueLiteral) {
		String oldEndValueLiteral = endValueLiteral;
		endValueLiteral = newEndValueLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL, oldEndValueLiteral, endValueLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getEffectLiteral(Timepoint timepoint) {
		switch (timepoint) {
		case START:
			return getStartValueLiteral();
		case END:
			String endValueLiteral = getEndValueLiteral();
			if (endValueLiteral == null) {
				Profile<?> profile = ResourceUtils.getProfile(this);
				if (profile != null) {
					EDataType dataType = profile.getDataType();
					if (dataType != null) {
						try {
							Object v = EcoreUtil.createFromString(dataType, startValueLiteral);
							if (v instanceof Number) {
								Number n = (Number) v;
								if (n.doubleValue() == n.intValue()) {
									return String.valueOf(-1*n.intValue());
								} else {
									return String.valueOf(-1*n.doubleValue());
								}
							} else if (v instanceof Boolean) {
								return String.valueOf(!(Boolean)v);
							}
						} catch (Exception e) {
							// Return null if there was a problem parsing the startValueLiteral
							return null;
						}
					}
				}
			}
			else if (endValueLiteral.equals(NO_EFFECT)) {
				return null;
			}
			return endValueLiteral;
		}
		return null;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL:
				return getStartValueLiteral();
			case ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL:
				return getEndValueLiteral();
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
			case ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL:
				setStartValueLiteral((String)newValue);
				return;
			case ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL:
				setEndValueLiteral((String)newValue);
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
			case ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL:
				setStartValueLiteral(START_VALUE_LITERAL_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL:
				setEndValueLiteral(END_VALUE_LITERAL_EDEFAULT);
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
			case ProfilePackage.PROFILE_EFFECT__START_VALUE_LITERAL:
				return START_VALUE_LITERAL_EDEFAULT == null ? startValueLiteral != null : !START_VALUE_LITERAL_EDEFAULT.equals(startValueLiteral);
			case ProfilePackage.PROFILE_EFFECT__END_VALUE_LITERAL:
				return END_VALUE_LITERAL_EDEFAULT == null ? endValueLiteral != null : !END_VALUE_LITERAL_EDEFAULT.equals(endValueLiteral);
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" goes from ");
		result.append(startValueLiteral);
		result.append(" to ");
		result.append(endValueLiteral);
		return result.toString();
	}

} //ProfileEffectImpl
