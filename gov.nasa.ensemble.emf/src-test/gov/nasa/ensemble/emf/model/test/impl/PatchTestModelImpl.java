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
package gov.nasa.ensemble.emf.model.test.impl;

import gov.nasa.ensemble.emf.model.test.PatchTestModel;
import gov.nasa.ensemble.emf.model.test.PatchTestPackage;
import gov.nasa.ensemble.emf.model.test.SomeTestClass;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Patch Test Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getOneAttribute <em>One Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getMultiAttribute <em>Multi Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getOneNonContainmentReference <em>One Non Containment Reference</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getMultiNonContainmentReferences <em>Multi Non Containment References</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getOneContainmentReference <em>One Containment Reference</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl#getMultiContainmentReference <em>Multi Containment Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PatchTestModelImpl extends EObjectImpl implements PatchTestModel {
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
	 * The default value of the '{@link #getOneAttribute() <em>One Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneAttribute()
	 * @generated
	 * @ordered
	 */
	protected static final String ONE_ATTRIBUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOneAttribute() <em>One Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneAttribute()
	 * @generated
	 * @ordered
	 */
	protected String oneAttribute = ONE_ATTRIBUTE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMultiAttribute() <em>Multi Attribute</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiAttribute()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> multiAttribute;

	/**
	 * The cached value of the '{@link #getOneNonContainmentReference() <em>One Non Containment Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneNonContainmentReference()
	 * @generated
	 * @ordered
	 */
	protected SomeTestClass oneNonContainmentReference;

	/**
	 * The cached value of the '{@link #getMultiNonContainmentReferences() <em>Multi Non Containment References</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiNonContainmentReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<SomeTestClass> multiNonContainmentReferences;

	/**
	 * The cached value of the '{@link #getOneContainmentReference() <em>One Containment Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOneContainmentReference()
	 * @generated
	 * @ordered
	 */
	protected SomeTestClass oneContainmentReference;

	/**
	 * The cached value of the '{@link #getMultiContainmentReference() <em>Multi Containment Reference</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultiContainmentReference()
	 * @generated
	 * @ordered
	 */
	protected EList<SomeTestClass> multiContainmentReference;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PatchTestModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PatchTestPackage.Literals.PATCH_TEST_MODEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PatchTestPackage.PATCH_TEST_MODEL__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOneAttribute() {
		return oneAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOneAttribute(String newOneAttribute) {
		String oldOneAttribute = oneAttribute;
		oneAttribute = newOneAttribute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchTestPackage.PATCH_TEST_MODEL__ONE_ATTRIBUTE, oldOneAttribute, oneAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Integer> getMultiAttribute() {
		if (multiAttribute == null) {
			multiAttribute = new EDataTypeUniqueEList<Integer>(Integer.class, this, PatchTestPackage.PATCH_TEST_MODEL__MULTI_ATTRIBUTE);
		}
		return multiAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SomeTestClass getOneNonContainmentReference() {
		if (oneNonContainmentReference != null && oneNonContainmentReference.eIsProxy()) {
			InternalEObject oldOneNonContainmentReference = (InternalEObject)oneNonContainmentReference;
			oneNonContainmentReference = (SomeTestClass)eResolveProxy(oldOneNonContainmentReference);
			if (oneNonContainmentReference != oldOneNonContainmentReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE, oldOneNonContainmentReference, oneNonContainmentReference));
			}
		}
		return oneNonContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SomeTestClass basicGetOneNonContainmentReference() {
		return oneNonContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOneNonContainmentReference(SomeTestClass newOneNonContainmentReference) {
		SomeTestClass oldOneNonContainmentReference = oneNonContainmentReference;
		oneNonContainmentReference = newOneNonContainmentReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE, oldOneNonContainmentReference, oneNonContainmentReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SomeTestClass> getMultiNonContainmentReferences() {
		if (multiNonContainmentReferences == null) {
			multiNonContainmentReferences = new EObjectResolvingEList<SomeTestClass>(SomeTestClass.class, this, PatchTestPackage.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES);
		}
		return multiNonContainmentReferences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SomeTestClass getOneContainmentReference() {
		return oneContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOneContainmentReference(SomeTestClass newOneContainmentReference, NotificationChain msgs) {
		SomeTestClass oldOneContainmentReference = oneContainmentReference;
		oneContainmentReference = newOneContainmentReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE, oldOneContainmentReference, newOneContainmentReference);
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
	public void setOneContainmentReference(SomeTestClass newOneContainmentReference) {
		if (newOneContainmentReference != oneContainmentReference) {
			NotificationChain msgs = null;
			if (oneContainmentReference != null)
				msgs = ((InternalEObject)oneContainmentReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE, null, msgs);
			if (newOneContainmentReference != null)
				msgs = ((InternalEObject)newOneContainmentReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE, null, msgs);
			msgs = basicSetOneContainmentReference(newOneContainmentReference, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE, newOneContainmentReference, newOneContainmentReference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SomeTestClass> getMultiContainmentReference() {
		if (multiContainmentReference == null) {
			multiContainmentReference = new EObjectContainmentEList<SomeTestClass>(SomeTestClass.class, this, PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE);
		}
		return multiContainmentReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE:
				return basicSetOneContainmentReference(null, msgs);
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE:
				return ((InternalEList<?>)getMultiContainmentReference()).basicRemove(otherEnd, msgs);
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
			case PatchTestPackage.PATCH_TEST_MODEL__ID:
				return getId();
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_ATTRIBUTE:
				return getOneAttribute();
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_ATTRIBUTE:
				return getMultiAttribute();
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE:
				if (resolve) return getOneNonContainmentReference();
				return basicGetOneNonContainmentReference();
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES:
				return getMultiNonContainmentReferences();
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE:
				return getOneContainmentReference();
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE:
				return getMultiContainmentReference();
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
			case PatchTestPackage.PATCH_TEST_MODEL__ID:
				setId((String)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_ATTRIBUTE:
				setOneAttribute((String)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_ATTRIBUTE:
				getMultiAttribute().clear();
				getMultiAttribute().addAll((Collection<? extends Integer>)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE:
				setOneNonContainmentReference((SomeTestClass)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES:
				getMultiNonContainmentReferences().clear();
				getMultiNonContainmentReferences().addAll((Collection<? extends SomeTestClass>)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE:
				setOneContainmentReference((SomeTestClass)newValue);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE:
				getMultiContainmentReference().clear();
				getMultiContainmentReference().addAll((Collection<? extends SomeTestClass>)newValue);
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
			case PatchTestPackage.PATCH_TEST_MODEL__ID:
				setId(ID_EDEFAULT);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_ATTRIBUTE:
				setOneAttribute(ONE_ATTRIBUTE_EDEFAULT);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_ATTRIBUTE:
				getMultiAttribute().clear();
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE:
				setOneNonContainmentReference((SomeTestClass)null);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES:
				getMultiNonContainmentReferences().clear();
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE:
				setOneContainmentReference((SomeTestClass)null);
				return;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE:
				getMultiContainmentReference().clear();
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
			case PatchTestPackage.PATCH_TEST_MODEL__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_ATTRIBUTE:
				return ONE_ATTRIBUTE_EDEFAULT == null ? oneAttribute != null : !ONE_ATTRIBUTE_EDEFAULT.equals(oneAttribute);
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_ATTRIBUTE:
				return multiAttribute != null && !multiAttribute.isEmpty();
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE:
				return oneNonContainmentReference != null;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES:
				return multiNonContainmentReferences != null && !multiNonContainmentReferences.isEmpty();
			case PatchTestPackage.PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE:
				return oneContainmentReference != null;
			case PatchTestPackage.PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE:
				return multiContainmentReference != null && !multiContainmentReference.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", oneAttribute: ");
		result.append(oneAttribute);
		result.append(", multiAttribute: ");
		result.append(multiAttribute);
		result.append(')');
		return result.toString();
	}

} //PatchTestModelImpl
