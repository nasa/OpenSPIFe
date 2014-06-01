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
package gov.nasa.ensemble.core.model.plan.activityDictionary.impl;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.dictionary.EResourceDef;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>AD Effect Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl#getObject <em>Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl#getResourceDef <em>Resource Def</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ADEffectKeyImpl extends EObjectImpl implements ADEffectKey {
	/**
	 * The cached value of the '{@link #getObject() <em>Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObject()
	 * @generated
	 * @ordered
	 */
	protected EObject object;

	/**
	 * The cached value of the '{@link #getResourceDef() <em>Resource Def</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceDef()
	 * @generated
	 * @ordered
	 */
	protected EResourceDef resourceDef;

	private Integer hashCode = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADEffectKeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActivityDictionaryPackage.Literals.AD_EFFECT_KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getObject() {
		if (object != null && object.eIsProxy()) {
			InternalEObject oldObject = (InternalEObject)object;
			object = eResolveProxy(oldObject);
			if (object != oldObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT, oldObject, object));
			}
		}
		return object;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetObject() {
		return object;
	}

	/**
	 * According to Object.hashCode, the hashCode may be changed
	 * if information used in the equals comparison on the object
	 * is modified.  We need to invalidate this cache because
	 * EMF loading causes the cache to be populated before the
	 * fields are set.
	 */
	@Override
	public void setObject(EObject value) {
		hashCode = null; // it is allowed to recompute the hash code if something used in the equals method changed
		setObjectGen(value);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private void setObjectGen(EObject newObject) {
		EObject oldObject = object;
		object = newObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT, oldObject, object));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EResourceDef getResourceDef() {
		if (resourceDef != null && resourceDef.eIsProxy()) {
			InternalEObject oldResourceDef = (InternalEObject)resourceDef;
			resourceDef = resolveResourceDef(oldResourceDef);
			if (resourceDef != oldResourceDef) {
				hashCode = null; // it is allowed to recompute the hash code if something used in the equals method changed
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF, oldResourceDef, resourceDef));
			}
		}
		return resourceDef;
	}

	private EResourceDef resolveResourceDef(InternalEObject oldResourceDef) {
		EResourceDef resourceDef = (EResourceDef)eResolveProxy(oldResourceDef);
		if (resourceDef == oldResourceDef) {
			URI uri = oldResourceDef.eProxyURI();
			String fragment = uri.fragment();
			if (fragment != null) {
				int characterIndex = fragment.lastIndexOf('/');
				String name = fragment.substring(characterIndex + 1);
				EResourceDef definition = ActivityDictionary.getInstance().getDefinition(EResourceDef.class, name);
				if (definition != null) {
					resourceDef = definition;
				} else {
					characterIndex = name.indexOf('.');
					if (characterIndex != -1) {
						try {
							int definitionIndex = Integer.parseInt(name.substring(characterIndex + 1));
							name = name.substring(0, characterIndex);
							List<EResourceDef> defs = ActivityDictionary.getInstance().getDefinitions(EResourceDef.class);
							int counter = 1;
							for (EResourceDef def : defs) {
								if (name.equals(def.getName())) {
									if (definitionIndex == counter++) {
										resourceDef = def;
										break;
									}
								}
							}
						} catch (NumberFormatException e) {
							// fall out and report failure
						}
					}
				}
				if (resourceDef == oldResourceDef) {
					LogUtil.error("couldn't find resource def for URI: " + uri);
				}
			}
		}
		return resourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EResourceDef basicGetResourceDef() {
		return resourceDef;
	}

	/**
	 * According to Object.hashCode, the hashCode may be changed
	 * if information used in the equals comparison on the object
	 * is modified.  We need to invalidate this cache because
	 * EMF loading causes the cache to be populated before the
	 * fields are set.
	 */
	@Override
	public void setResourceDef(EResourceDef newResourceDef) {
		hashCode = null; // it is allowed to recompute the hash code if something used in the equals method changed
		setResourceDefGen(newResourceDef);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private void setResourceDefGen(EResourceDef newResourceDef) {
		EResourceDef oldResourceDef = resourceDef;
		resourceDef = newResourceDef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF, oldResourceDef, resourceDef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT:
				if (resolve) return getObject();
				return basicGetObject();
			case ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF:
				if (resolve) return getResourceDef();
				return basicGetResourceDef();
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
			case ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT:
				setObject((EObject)newValue);
				return;
			case ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF:
				setResourceDef((EResourceDef)newValue);
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
			case ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT:
				setObject((EObject)null);
				return;
			case ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF:
				setResourceDef((EResourceDef)null);
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
			case ActivityDictionaryPackage.AD_EFFECT_KEY__OBJECT:
				return object != null;
			case ActivityDictionaryPackage.AD_EFFECT_KEY__RESOURCE_DEF:
				return resourceDef != null;
		}
		return super.eIsSet(featureID);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hashCode == null) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			result = prime * result + ((resourceDef == null) ? 0 : resourceDef.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ADEffectKeyImpl other = (ADEffectKeyImpl) obj;
		if (object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!object.equals(other.object)) {
			return false;
		}
		if (resourceDef == null) {
			if (other.resourceDef != null) {
				return false;
			}
		} else if (!resourceDef.equals(other.resourceDef)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String text = "";
		if (object != null) {
			text = EMFUtils.getText(object, object.toString()) + ".";
		}
		if (resourceDef != null) {
			text += resourceDef.getName();
		} else {
			text += "<null resource>";
		}
		return text;
	}
	
	
} //ADEffectKeyImpl
