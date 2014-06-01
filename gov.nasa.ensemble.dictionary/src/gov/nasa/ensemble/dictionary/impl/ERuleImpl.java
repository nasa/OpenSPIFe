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
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ERule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl#getHypertextDescription <em>Hypertext Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl#getPath <em>Path</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl#getPrintName <em>Print Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl#getShortDescription <em>Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ERuleImpl extends MinimalEObjectImpl.Container implements ERule {
	/**
	 * The default value of the '{@link #getHypertextDescription() <em>Hypertext Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHypertextDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String HYPERTEXT_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHypertextDescription() <em>Hypertext Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHypertextDescription()
	 * @generated
	 * @ordered
	 */
	protected String hypertextDescription = HYPERTEXT_DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected EList<String> path;

	/**
	 * The default value of the '{@link #getPrintName() <em>Print Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrintName()
	 * @generated
	 * @ordered
	 */
	protected static final String PRINT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrintName() <em>Print Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrintName()
	 * @generated
	 * @ordered
	 */
	protected String printName = PRINT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getShortDescription() <em>Short Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String SHORT_DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getShortDescription() <em>Short Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortDescription()
	 * @generated
	 * @ordered
	 */
	protected String shortDescription = SHORT_DESCRIPTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ERuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ERULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getHypertextDescription() {
		return hypertextDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHypertextDescription(String newHypertextDescription) {
		String oldHypertextDescription = hypertextDescription;
		hypertextDescription = newHypertextDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ERULE__HYPERTEXT_DESCRIPTION, oldHypertextDescription, hypertextDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ERULE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<String> getPath() {
		if (path == null) {
			path = new EDataTypeUniqueEList<String>(String.class, this, DictionaryPackage.ERULE__PATH);
		}
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPrintName() {
		return printName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrintName(String newPrintName) {
		String oldPrintName = printName;
		printName = newPrintName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ERULE__PRINT_NAME, oldPrintName, printName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShortDescription(String newShortDescription) {
		String oldShortDescription = shortDescription;
		shortDescription = newShortDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ERULE__SHORT_DESCRIPTION, oldShortDescription, shortDescription));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ERULE__HYPERTEXT_DESCRIPTION:
				return getHypertextDescription();
			case DictionaryPackage.ERULE__NAME:
				return getName();
			case DictionaryPackage.ERULE__PATH:
				return getPath();
			case DictionaryPackage.ERULE__PRINT_NAME:
				return getPrintName();
			case DictionaryPackage.ERULE__SHORT_DESCRIPTION:
				return getShortDescription();
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
			case DictionaryPackage.ERULE__HYPERTEXT_DESCRIPTION:
				setHypertextDescription((String)newValue);
				return;
			case DictionaryPackage.ERULE__NAME:
				setName((String)newValue);
				return;
			case DictionaryPackage.ERULE__PATH:
				getPath().clear();
				getPath().addAll((Collection<? extends String>)newValue);
				return;
			case DictionaryPackage.ERULE__PRINT_NAME:
				setPrintName((String)newValue);
				return;
			case DictionaryPackage.ERULE__SHORT_DESCRIPTION:
				setShortDescription((String)newValue);
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
			case DictionaryPackage.ERULE__HYPERTEXT_DESCRIPTION:
				setHypertextDescription(HYPERTEXT_DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.ERULE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DictionaryPackage.ERULE__PATH:
				getPath().clear();
				return;
			case DictionaryPackage.ERULE__PRINT_NAME:
				setPrintName(PRINT_NAME_EDEFAULT);
				return;
			case DictionaryPackage.ERULE__SHORT_DESCRIPTION:
				setShortDescription(SHORT_DESCRIPTION_EDEFAULT);
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
			case DictionaryPackage.ERULE__HYPERTEXT_DESCRIPTION:
				return HYPERTEXT_DESCRIPTION_EDEFAULT == null ? hypertextDescription != null : !HYPERTEXT_DESCRIPTION_EDEFAULT.equals(hypertextDescription);
			case DictionaryPackage.ERULE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DictionaryPackage.ERULE__PATH:
				return path != null && !path.isEmpty();
			case DictionaryPackage.ERULE__PRINT_NAME:
				return PRINT_NAME_EDEFAULT == null ? printName != null : !PRINT_NAME_EDEFAULT.equals(printName);
			case DictionaryPackage.ERULE__SHORT_DESCRIPTION:
				return SHORT_DESCRIPTION_EDEFAULT == null ? shortDescription != null : !SHORT_DESCRIPTION_EDEFAULT.equals(shortDescription);
		}
		return super.eIsSet(featureID);
	}
	
	@Override
	public int hashCode() {
		if (name == null)
			return super.hashCode();
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ERule) {
			ERule rule = (ERule) obj;
			if (rule.getName() == null)
				return super.equals(obj);
			return rule.getName().equals(getName());
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append("[name=" + getName() + ", printName=" + getPrintName() + "]");
		return builder.toString();
	}

} //ERuleImpl
