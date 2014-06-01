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
package gov.nasa.ensemble.emf.model.patch.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getFeature <em>Feature</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getValueString <em>Value String</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getOldValueString <em>Old Value String</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getListChanges <em>List Changes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getDisplayMessage <em>Display Message</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl#getReference <em>Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PatchFeatureChangeImpl extends EObjectImpl implements PatchFeatureChange {
	/**
	 * The cached value of the '{@link #getFeature() <em>Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeature()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature feature;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected Object value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getValueString() <em>Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueString()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueString() <em>Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueString()
	 * @generated
	 * @ordered
	 */
	protected String valueString = VALUE_STRING_EDEFAULT;

	/**
	 * The default value of the '{@link #getOldValueString() <em>Old Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValueString()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_VALUE_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldValueString() <em>Old Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldValueString()
	 * @generated
	 * @ordered
	 */
	protected String oldValueString = OLD_VALUE_STRING_EDEFAULT;

	/**
	 * The cached value of the '{@link #getListChanges() <em>List Changes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<PatchListChange> listChanges;

	/**
	 * The default value of the '{@link #getDisplayMessage() <em>Display Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayMessage()
	 * @generated
	 * @ordered
	 */
	protected static final String DISPLAY_MESSAGE_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getDisplayMessage() <em>Display Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplayMessage()
	 * @generated
	 * @ordered
	 */
	protected String displayMessage = DISPLAY_MESSAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
	protected EObject reference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PatchFeatureChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PatchPackage.Literals.PATCH_FEATURE_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EStructuralFeature getFeature() {
		if (feature != null && feature.eIsProxy()) {
			InternalEObject oldFeature = (InternalEObject)feature;
			feature = (EStructuralFeature)eResolveProxy(oldFeature);
		}
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetFeature() {
		return feature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setFeature(EStructuralFeature newFeature) {
		feature = newFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getValue() {
		EStructuralFeature feature = getFeature();
		if (value == null) {
			if (feature instanceof EAttribute) {
				EDataType type = ((EAttribute)feature).getEAttributeType();
				Object object = EcoreUtil.createFromString(type, getValueString());
				value = object;
			} else if (feature instanceof EReference) {
				EObject object = getReference();
				if (object != null) {
					value = object;
				} else if (valueString != null) {
					URI uri = URI.createURI(valueString);
					EObject eObject = EcoreFactory.eINSTANCE.createEObject();
					((InternalEObject) eObject).eSetProxyURI(uri);
					EObject resolved = eResolveProxy((InternalEObject) eObject);				
					value = resolved;
				} else {
					value = null;
				}
			}
		}
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setValue(Object newValue) {
		value = newValue;
		valueString = getObjectAsString(newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getValueString() {
		return valueString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValueString(String newValueString) {
		String oldValueString = valueString;
		valueString = newValueString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_FEATURE_CHANGE__VALUE_STRING, oldValueString, valueString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOldValueString() {
		return oldValueString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOldValueString(String newOldValueString) {
		String oldOldValueString = oldValueString;
		oldValueString = newOldValueString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_FEATURE_CHANGE__OLD_VALUE_STRING, oldOldValueString, oldValueString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PatchListChange> getListChanges() {
		if (listChanges == null) {
			listChanges = new EObjectContainmentEList<PatchListChange>(PatchListChange.class, this, PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES);
		}
		return listChanges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDisplayMessage() {
		return displayMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDisplayMessage(String newDisplayMessage) {
		String oldDisplayMessage = displayMessage;
		displayMessage = newDisplayMessage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE, oldDisplayMessage, displayMessage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getReference() {
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetReference(EObject newReference, NotificationChain msgs) {
		EObject oldReference = reference;
		reference = newReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE, oldReference, newReference);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReference(EObject newReference) {
		if (newReference != reference) {
			NotificationChain msgs = null;
			if (reference != null)
				msgs = ((InternalEObject)reference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE, null, msgs);
			if (newReference != null)
				msgs = ((InternalEObject)newReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE, null, msgs);
			msgs = basicSetReference(newReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE, newReference, newReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(EObject target) {
		EStructuralFeature feature = getFeature();
		if (!feature.isMany()) {
			Object currentValue = target.eGet(feature);
			Object value = getValue();
			if (!CommonUtils.equals(currentValue, value)) {
				target.eSet(feature, value);
				setOldValue(currentValue); //for rollback
			}
		} else {
			for (PatchListChange listChange : getListChanges()) {
				listChange.apply(target, feature);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void applyAndReverse(EObject target) {
		EStructuralFeature feature = getFeature();
		if (!feature.isMany()) {
			Object valueToSet = getValue();
			Object currentValue = target.eGet(feature);
			target.eSet(feature, valueToSet);
			setOldValue(valueToSet);
			setValue(currentValue);
		} else {
			EList<PatchListChange> reversedListChange = new BasicEList();
			for (PatchListChange listChange : getListChanges()) {
				PatchListChange reversed = listChange.applyAndReverse(target, feature);
				if (reversed != null) {
					reversedListChange.add(0, reversed);
				}
			}
			getListChanges().clear();
			getListChanges().addAll(reversedListChange);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getObjectAsString(Object object) {
		if (feature instanceof EAttribute) {
			if (object instanceof String) {
				return (String) object;
			} else {
				EDataType type = (EDataType)feature.getEType();
				return EcoreUtil.convertToString(type, object);
			}
		} else if (feature instanceof EReference) {
			if (object instanceof EObject) {
				((InternalEObject) object).eSetProxyURI(null);
				URI uri = EcoreUtil.getURI((EObject) object);
				return uri.toString();
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
	public void setOldValue(Object oldValue) {
		oldValueString = getObjectAsString(oldValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES:
				return ((InternalEList<?>)getListChanges()).basicRemove(otherEnd, msgs);
			case PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE:
				return basicSetReference(null, msgs);
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
			case PatchPackage.PATCH_FEATURE_CHANGE__FEATURE:
				if (resolve) return getFeature();
				return basicGetFeature();
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE:
				return getValue();
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE_STRING:
				return getValueString();
			case PatchPackage.PATCH_FEATURE_CHANGE__OLD_VALUE_STRING:
				return getOldValueString();
			case PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES:
				return getListChanges();
			case PatchPackage.PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE:
				return getDisplayMessage();
			case PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE:
				return getReference();
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
			case PatchPackage.PATCH_FEATURE_CHANGE__FEATURE:
				setFeature((EStructuralFeature)newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE:
				setValue(newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE_STRING:
				setValueString((String)newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__OLD_VALUE_STRING:
				setOldValueString((String)newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES:
				getListChanges().clear();
				getListChanges().addAll((Collection<? extends PatchListChange>)newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE:
				setDisplayMessage((String)newValue);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE:
				setReference((EObject)newValue);
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
			case PatchPackage.PATCH_FEATURE_CHANGE__FEATURE:
				setFeature((EStructuralFeature)null);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE_STRING:
				setValueString(VALUE_STRING_EDEFAULT);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__OLD_VALUE_STRING:
				setOldValueString(OLD_VALUE_STRING_EDEFAULT);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES:
				getListChanges().clear();
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE:
				setDisplayMessage(DISPLAY_MESSAGE_EDEFAULT);
				return;
			case PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE:
				setReference((EObject)null);
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
			case PatchPackage.PATCH_FEATURE_CHANGE__FEATURE:
				return feature != null;
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case PatchPackage.PATCH_FEATURE_CHANGE__VALUE_STRING:
				return VALUE_STRING_EDEFAULT == null ? valueString != null : !VALUE_STRING_EDEFAULT.equals(valueString);
			case PatchPackage.PATCH_FEATURE_CHANGE__OLD_VALUE_STRING:
				return OLD_VALUE_STRING_EDEFAULT == null ? oldValueString != null : !OLD_VALUE_STRING_EDEFAULT.equals(oldValueString);
			case PatchPackage.PATCH_FEATURE_CHANGE__LIST_CHANGES:
				return listChanges != null && !listChanges.isEmpty();
			case PatchPackage.PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE:
				return DISPLAY_MESSAGE_EDEFAULT == null ? displayMessage != null : !DISPLAY_MESSAGE_EDEFAULT.equals(displayMessage);
			case PatchPackage.PATCH_FEATURE_CHANGE__REFERENCE:
				return reference != null;
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
		result.append(" (value: ");
		result.append(value);
		result.append(", valueString: ");
		result.append(valueString);
		result.append(", oldValueString: ");
		result.append(oldValueString);
		result.append(", displayMessage: ");
		result.append(displayMessage);
		result.append(')');
		return result.toString();
	}

} //PatchFeatureChangeImpl
