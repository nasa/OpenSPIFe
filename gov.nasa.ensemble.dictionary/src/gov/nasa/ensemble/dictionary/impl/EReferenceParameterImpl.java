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

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityRequirement;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.EReferenceParameter;

import gov.nasa.ensemble.dictionary.Effect;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EReference Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl#getDefaultLength <em>Default Length</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl#getEffects <em>Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl#getRequirements <em>Requirements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EReferenceParameterImpl extends EReferenceImpl implements EReferenceParameter {
	/**
	 * The default value of the '{@link #getDefaultLength() <em>Default Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLength()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultLength() <em>Default Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLength()
	 * @generated
	 * @ordered
	 */
	protected int defaultLength = DEFAULT_LENGTH_EDEFAULT;

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
	 * The cached value of the '{@link #getEffects() <em>Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<Effect<?>> effects;

	/**
	 * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequirements()
	 * @generated
	 * @ordered
	 */
	protected EList<EActivityRequirement> requirements;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EReferenceParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EREFERENCE_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDefaultLength() {
		return defaultLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultLength(int newDefaultLength) {
		int oldDefaultLength = defaultLength;
		defaultLength = newDefaultLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH, oldDefaultLength, defaultLength));
	}

	@Override
	public Object getDefaultValue() {
		String literal = getDefaultValueLiteral();
		if (literal != null) {
			return super.getDefaultValue();
		}
		return defaultValue;
	}
	
	@Override
	public void setDefaultValue(Object newDefaultValue) {
		EClassifier eType = getEType();
	    if (eType instanceof EDataType) {
	    	super.setDefaultValue(newDefaultValue);
	    }
	    defaultValue = newDefaultValue;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<Effect<?>> getEffects() {
		if (effects == null) {
			effects = new EObjectContainmentEList<Effect<?>>(Effect.class, this, DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS);
		}
		return effects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EActivityRequirement> getRequirements() {
		if (requirements == null) {
			requirements = new EObjectContainmentEList<EActivityRequirement>(EActivityRequirement.class, this, DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS);
		}
		return requirements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
				return ((InternalEList<?>)getEffects()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
				return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
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
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH:
				return getDefaultLength();
			case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION:
				return getDescription();
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
				return getEffects();
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
				return getRequirements();
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
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH:
				setDefaultLength((Integer)newValue);
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
				getEffects().clear();
				getEffects().addAll((Collection<? extends Effect<?>>)newValue);
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
				getRequirements().clear();
				getRequirements().addAll((Collection<? extends EActivityRequirement>)newValue);
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
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH:
				setDefaultLength(DEFAULT_LENGTH_EDEFAULT);
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
				getEffects().clear();
				return;
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
				getRequirements().clear();
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
			case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH:
				return defaultLength != DEFAULT_LENGTH_EDEFAULT;
			case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DictionaryPackage.EREFERENCE_PARAMETER__EFFECTS:
				return effects != null && !effects.isEmpty();
			case DictionaryPackage.EREFERENCE_PARAMETER__REQUIREMENTS:
				return requirements != null && !requirements.isEmpty();
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
		if (baseClass == EParameterDef.class) {
			switch (derivedFeatureID) {
				case DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH: return DictionaryPackage.EPARAMETER_DEF__DEFAULT_LENGTH;
				case DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION: return DictionaryPackage.EPARAMETER_DEF__DESCRIPTION;
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
		if (baseClass == EParameterDef.class) {
			switch (baseFeatureID) {
				case DictionaryPackage.EPARAMETER_DEF__DEFAULT_LENGTH: return DictionaryPackage.EREFERENCE_PARAMETER__DEFAULT_LENGTH;
				case DictionaryPackage.EPARAMETER_DEF__DESCRIPTION: return DictionaryPackage.EREFERENCE_PARAMETER__DESCRIPTION;
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
		result.append(" (defaultLength: ");
		result.append(defaultLength);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //EReferenceParameterImpl
