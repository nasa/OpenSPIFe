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

import gov.nasa.ensemble.dictionary.DefinitionContext;
import gov.nasa.ensemble.dictionary.DefinitionContextImpl;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Dictionary</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getAttributeDefs <em>Attribute Defs</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getDate <em>Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getExtendedDefinitions <em>Extended Definitions</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EActivityDictionaryImpl extends EPackageImpl implements EActivityDictionary {
	/**
	 * The default value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTHOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
	protected String author = AUTHOR_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAttributeDefs() <em>Attribute Defs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributeDefs()
	 * @generated
	 * @ordered
	 */
	protected EList<EParameterDef> attributeDefs;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final String DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected String date = DATE_EDEFAULT;

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
	 * The cached value of the '{@link #getExtendedDefinitions() <em>Extended Definitions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedDefinitions()
	 * @generated
	 * @ordered
	 */
	protected EList<INamedDefinition> extendedDefinitions;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	protected final DefinitionContext definitionContextDelegate;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EActivityDictionaryImpl() {
		super();
		definitionContextDelegate = new DefinitionContextImpl(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EACTIVITY_DICTIONARY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAuthor() {
		return author;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAuthor(String newAuthor) {
		String oldAuthor = author;
		author = newAuthor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR, oldAuthor, author));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EParameterDef> getAttributeDefs() {
		if (attributeDefs == null) {
			attributeDefs = new EObjectContainmentEList<EParameterDef>(EParameterDef.class, this, DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS);
		}
		return attributeDefs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDate(String newDate) {
		String oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DICTIONARY__DATE, oldDate, date));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<INamedDefinition> getExtendedDefinitions() {
		if (extendedDefinitions == null) {
			extendedDefinitions = new EObjectContainmentEList<INamedDefinition>(INamedDefinition.class, this, DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS);
		}
		return extendedDefinitions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DICTIONARY__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @return the activity group definition, or null
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EActivityGroupDef getActivityGroupDef() {
		List<EActivityGroupDef> groupDefinitions = getDefinitions(EActivityGroupDef.class);
		if (groupDefinitions != null && !groupDefinitions.isEmpty()) {
			return groupDefinitions.get(0);
		} else return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @return list of all activity definitions
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public List<EActivityDef> getActivityDefs() {
		return getDefinitions(EActivityDef.class);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Looks up a single activity definition by name.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EActivityDef getActivityDef(String name) {
		return getDefinition(EActivityDef.class, name);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
				return ((InternalEList<?>)getAttributeDefs()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				return ((InternalEList<?>)getExtendedDefinitions()).basicRemove(otherEnd, msgs);
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
			case DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR:
				return getAuthor();
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
				return getAttributeDefs();
			case DictionaryPackage.EACTIVITY_DICTIONARY__DATE:
				return getDate();
			case DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION:
				return getDescription();
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				return getExtendedDefinitions();
			case DictionaryPackage.EACTIVITY_DICTIONARY__VERSION:
				return getVersion();
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
			case DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR:
				setAuthor((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
				getAttributeDefs().clear();
				getAttributeDefs().addAll((Collection<? extends EParameterDef>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__DATE:
				setDate((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				getExtendedDefinitions().clear();
				getExtendedDefinitions().addAll((Collection<? extends INamedDefinition>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__VERSION:
				setVersion((String)newValue);
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
			case DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR:
				setAuthor(AUTHOR_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
				getAttributeDefs().clear();
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				getExtendedDefinitions().clear();
				return;
			case DictionaryPackage.EACTIVITY_DICTIONARY__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case DictionaryPackage.EACTIVITY_DICTIONARY__AUTHOR:
				return AUTHOR_EDEFAULT == null ? author != null : !AUTHOR_EDEFAULT.equals(author);
			case DictionaryPackage.EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS:
				return attributeDefs != null && !attributeDefs.isEmpty();
			case DictionaryPackage.EACTIVITY_DICTIONARY__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case DictionaryPackage.EACTIVITY_DICTIONARY__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DictionaryPackage.EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS:
				return extendedDefinitions != null && !extendedDefinitions.isEmpty();
			case DictionaryPackage.EACTIVITY_DICTIONARY__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
		result.append(" (author: ");
		result.append(author);
		result.append(", date: ");
		result.append(date);
		result.append(", description: ");
		result.append(description);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

	@Override
	public void clearCache() {
		if (eNameToEClassifierMap != null) {
			eNameToEClassifierMap.clear();
			eNameToEClassifierMap = null;
		}
		if (eClassifiers != null) {
			eClassifiers.clear();
			eClassifiers = null;
		}
		if (eSubpackages != null) {
			eSubpackages.clear();
		}
		definitionContextDelegate.clearCache();
	}

	@Override
	public <T extends INamedDefinition> T getDefinition(Class<T> klass, String name) {
		return definitionContextDelegate.getDefinition(klass, name);
	}

	@Override
	public <T> List<T> getDefinitions(Class<T> klass) {
		return definitionContextDelegate.getDefinitions(klass);
	}

} //EActivityDictionaryImpl
