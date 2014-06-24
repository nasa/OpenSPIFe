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
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.model.patch.ChangeType;
import gov.nasa.ensemble.emf.model.patch.PatchFactory;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;
import gov.nasa.ensemble.emf.patch.DeserializerContributor;
import gov.nasa.ensemble.emf.patch.DeserializerContributorRegistry;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#isReversed <em>Reversed</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#getType <em>Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#getContainedObject <em>Contained Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#getNonContainedObject <em>Non Contained Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl#getValueString <em>Value String</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PatchListChangeImpl extends EObjectImpl implements PatchListChange {
	/**
	 * The default value of the '{@link #isReversed() <em>Reversed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReversed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REVERSED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReversed() <em>Reversed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReversed()
	 * @generated
	 * @ordered
	 */
	protected boolean reversed = REVERSED_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final ChangeType TYPE_EDEFAULT = ChangeType.ADD;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected ChangeType type = TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getContainedObject() <em>Contained Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedObject()
	 * @generated
	 * @ordered
	 */
	protected EObject containedObject;

	/**
	 * The cached value of the '{@link #getNonContainedObject() <em>Non Contained Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNonContainedObject()
	 * @generated
	 * @ordered
	 */
	protected EObject nonContainedObject;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PatchListChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PatchPackage.Literals.PATCH_LIST_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isReversed() {
		return reversed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReversed(boolean newReversed) {
		boolean oldReversed = reversed;
		reversed = newReversed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_LIST_CHANGE__REVERSED, oldReversed, reversed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setType(ChangeType newType) {
		type = newType == null ? TYPE_EDEFAULT : newType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setIndex(int newIndex) {
		index = newIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getContainedObject() {
		return containedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContainedObject(EObject newContainedObject, NotificationChain msgs) {
		EObject oldContainedObject = containedObject;
		containedObject = newContainedObject;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT, oldContainedObject, newContainedObject);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setContainedObject(EObject newContainedObject) {
		if (newContainedObject != containedObject) {
			if (newContainedObject.eIsProxy()) {
				((InternalEObject) newContainedObject).eSetProxyURI(null);
			}
			NotificationChain msgs = null;
			if (containedObject != null)
				msgs = ((InternalEObject)containedObject).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT, null, msgs);
			if (newContainedObject != null)
				msgs = ((InternalEObject)newContainedObject).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT, null, msgs);
			msgs = basicSetContainedObject(newContainedObject, msgs);
			if (msgs != null) msgs.dispatch();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EObject getNonContainedObject() {
		if (nonContainedObject != null && nonContainedObject.eIsProxy()) {
			InternalEObject oldNonContainedObject = (InternalEObject)nonContainedObject;
			nonContainedObject = eResolveProxy(oldNonContainedObject);
		}
		return nonContainedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetNonContainedObject() {
		return nonContainedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setNonContainedObject(EObject newNonContainedObject) {
		nonContainedObject = newNonContainedObject;
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
	 * @generated NOT
	 */
	@Override
	public void setValueString(String newValueString) {
		valueString = newValueString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(EObject target, EStructuralFeature feature) {
		Object currentValue = target.eGet(feature);
		if (currentValue instanceof List) {
			EList<Object> currentList = (EList<Object>) currentValue;
			switch (getType()) {
			case ADD: {
				try {
					int index = getIndex();
					Object toAdd = getObject(target, feature);
					boolean isIndexInRange = (index >= 0 && index < currentList.size());
					if (toAdd instanceof EObject) {
						EObject eToAdd = (EObject) toAdd;
						boolean isProxy = eToAdd.eIsProxy();
						String idToAdd = EcoreUtil.getID(eToAdd);
						if (idToAdd != null && feature.isUnique()) {
							if (isIndexInRange) {
								Object objectAtIndex = currentList.get(index);
								String idAtIndex = EcoreUtil.getID((EObject) objectAtIndex);
								if (CommonUtils.equals(idAtIndex, idToAdd)) {
									return;
								}
							}
							if (feature instanceof EReference) {
								Resource eResource = target.eResource();
								if (eResource != null) {
									Map<String, EObject> map = ((ResourceImpl)eResource).getIntrinsicIDToEObjectMap();
									if (map != null && map.containsKey(idToAdd)) {
										EObject eObject = map.get(idToAdd);
										int i = currentList.indexOf(eObject);
										if (i >= 0 && i != index) {
											currentList.remove(eObject);
											currentList.add(index, eObject);
											return;
										}
										if (isProxy) {
											eToAdd = eObject;
										}
									}
								}
							}
						}
						if (!isIndexInRange) {
							index = currentList.size();
							setIndex(index);
						}
						if (eToAdd.eIsProxy()) {
							EcoreUtil.resolveAll(eToAdd);
							((InternalEObject) eToAdd).eSetProxyURI(null);
						}
						currentList.add(index, eToAdd);
						setNonContainedObject(eToAdd);
					} else {
						if (feature.isUnique() && isIndexInRange) {
							Object objectAtIndex = currentList.get(index);
							if (CommonUtils.equals(toAdd, objectAtIndex)) {
								return;
							}
						} 
						if (!isIndexInRange) {
							index = currentList.size();
							setIndex(index);
						}
						currentList.add(index, toAdd);
					}
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
			break;
			case REMOVE: {
				Object objectToRemove = getObject(target, feature);
				if (feature.isUnique()) {
					int i = currentList.indexOf(objectToRemove);
					//found the object...
					if (i != -1) {
						if (getIndex() == -1) {
							setIndex(i);
						}
						currentList.remove(i);
					} else if (objectToRemove instanceof EObject) {
						EObject eToRemove = (EObject) objectToRemove;
						URI toRemoveURI = EcoreUtil.getURI(eToRemove);
						if (toRemoveURI != null && !toRemoveURI.equals("//")) {
							i = getIndex();
							boolean indexInRange = i >= 0 && i < currentList.size();
							if (indexInRange) {
								EObject object = (EObject) currentList.get(i);
								URI atIndexURI = EcoreUtil.getURI(object);
								if (CommonUtils.equals(toRemoveURI.fragment(), atIndexURI.fragment())) {
									currentList.remove(i);
									return;
								}
							}
							if (feature instanceof EReference && ((EReference) feature).isContainment()) {
								Resource resource = target.eResource();
								if (resource != null) {
									Map<String, EObject> map = ((ResourceImpl) resource).getIntrinsicIDToEObjectMap();
									if (map != null) {
										EObject eObject = map.get(toRemoveURI.fragment());
										if (eObject != null) {
											currentList.remove(eObject);
											return;
										}
									}
								}
							}
						}
					}
				}
				//if it's NOT unique lets try to remove by index
				else if (getIndex() >= 0) {
					int index = getIndex();
					if (index < currentList.size()) {
						Object object = currentList.get(index);
						if (CommonUtils.equals(object, objectToRemove)) {
							currentList.remove(index);
						}
					} 
				}
			}
			break;
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public PatchListChange applyAndReverse(EObject target, EStructuralFeature feature) {
		PatchListChange reversed = PatchFactory.eINSTANCE.createPatchListChange();
		Object object = getObject(target, feature);
		if (object == null) {
			LogUtil.error("object is null");
			return null;
		}
		switch (getType()) {
		case ADD:
			reversed.setType(ChangeType.REMOVE);
			apply(target, feature);
			reversed.setObject(feature, object);
			int i = getIndex();
			if (i == -1) {
				List<?> currentList = (List<?>) target.eGet(feature);
				i = currentList.indexOf(object);
			}
			reversed.setIndex(i);
			break;
		case REMOVE:
			reversed.setType(ChangeType.ADD);
			if (feature instanceof EReference && ((EReference) feature).isContainment()) {
				List<?> currentList = (List<?>) target.eGet(feature);
				if (object instanceof EObject) {
					int index = ((List) currentList).indexOf(object);
					if (index != -1) {
						reversed.setIndex(index);
						reversed.setObject(feature, object);
					} else {
						return null;
					}
				}
			} else {
				apply(target, feature);
				reversed.setIndex(getIndex());
				reversed.setObject(feature, object);
			}
			break;
		}
		reversed.setReversed(!isReversed());
		return reversed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setObject(EStructuralFeature feature, Object object) {
		if (feature instanceof EAttribute) {
			EDataType type = ((EAttribute) feature).getEAttributeType();
			valueString = EcoreUtil.convertToString(type, object);
		} else if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			if (getType().equals(ChangeType.ADD)){
				EReference reference = (EReference) feature;
				if (reference.isContainment()) {
					setContainedObject(eObject);
				} else {
					setNonContainedObject(eObject);
				}
			} else {
				setNonContainedObject(eObject);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getObject(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	@Override
	public Object getObject(EObject target, EStructuralFeature feature) {
		if (feature instanceof EAttribute) {
			EAttribute attribute = (EAttribute) feature;
			List<DeserializerContributor> contributors = DeserializerContributorRegistry.getContributors();
			Object result = null;
			for (DeserializerContributor contributor : contributors) {
				result = contributor.deserialize(target, attribute, valueString);
				if (result != null) {
					return result;
				}
			}
			EDataType type = attribute.getEAttributeType();
			return EcoreUtil.createFromString(type, valueString);
		}
		
		EObject object = getContainedObject();
		if (object != null) {
			return object;
		}
		object = getNonContainedObject();
		return object;
	}
	

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(Resource resource) {
		int index = getIndex();
		switch (getType()) {
		case ADD: {
			EObject content = getContainedObject();
			if (index >= 0 && index < resource.getContents().size()) {
				EObject atIndex = resource.getContents().get(index);
				URI uriAtIndex = EcoreUtil.getURI(atIndex);
				URI contentUri = EcoreUtil.getURI(content);
				if (!CommonUtils.equals(uriAtIndex.fragment(), contentUri.fragment())) {
					resource.getContents().add(index, content);
				}
			} else {
				resource.getContents().add(content);
			}
			URI uri = EcoreUtil.getURI(content);
			((InternalEObject) content).eSetProxyURI(uri);
		} break;
		case REMOVE: {
//			if (index >= 0 && index < resource.getContents().size()) {
				EObject content = getNonContainedObject();
//				URI contentUri = EcoreUtil.getURI(content);
				resource.getContents().remove(content);
//				EObject atIndex = resource.getContents().get(index);
//				URI uriAtIndex = EcoreUtil.getURI(atIndex);
//				if (CommonUtils.equals(uriAtIndex.fragment(), contentUri.fragment())) {
//					resource.getContents().remove(index);
//				}
//			}
		} break;
		default:
			LogUtil.warn("ResourceChange.apply()  - unsupported opreation of type " + getType()); 
			break;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT:
				return basicSetContainedObject(null, msgs);
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
			case PatchPackage.PATCH_LIST_CHANGE__REVERSED:
				return isReversed();
			case PatchPackage.PATCH_LIST_CHANGE__TYPE:
				return getType();
			case PatchPackage.PATCH_LIST_CHANGE__INDEX:
				return getIndex();
			case PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT:
				return getContainedObject();
			case PatchPackage.PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT:
				if (resolve) return getNonContainedObject();
				return basicGetNonContainedObject();
			case PatchPackage.PATCH_LIST_CHANGE__VALUE_STRING:
				return getValueString();
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
			case PatchPackage.PATCH_LIST_CHANGE__REVERSED:
				setReversed((Boolean)newValue);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__TYPE:
				setType((ChangeType)newValue);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__INDEX:
				setIndex((Integer)newValue);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT:
				setContainedObject((EObject)newValue);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT:
				setNonContainedObject((EObject)newValue);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__VALUE_STRING:
				setValueString((String)newValue);
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
			case PatchPackage.PATCH_LIST_CHANGE__REVERSED:
				setReversed(REVERSED_EDEFAULT);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT:
				setContainedObject((EObject)null);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT:
				setNonContainedObject((EObject)null);
				return;
			case PatchPackage.PATCH_LIST_CHANGE__VALUE_STRING:
				setValueString(VALUE_STRING_EDEFAULT);
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
			case PatchPackage.PATCH_LIST_CHANGE__REVERSED:
				return reversed != REVERSED_EDEFAULT;
			case PatchPackage.PATCH_LIST_CHANGE__TYPE:
				return type != TYPE_EDEFAULT;
			case PatchPackage.PATCH_LIST_CHANGE__INDEX:
				return index != INDEX_EDEFAULT;
			case PatchPackage.PATCH_LIST_CHANGE__CONTAINED_OBJECT:
				return containedObject != null;
			case PatchPackage.PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT:
				return nonContainedObject != null;
			case PatchPackage.PATCH_LIST_CHANGE__VALUE_STRING:
				return VALUE_STRING_EDEFAULT == null ? valueString != null : !VALUE_STRING_EDEFAULT.equals(valueString);
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
		result.append(" (reversed: ");
		result.append(reversed);
		result.append(", type: ");
		result.append(type);
		result.append(", index: ");
		result.append(index);
		result.append(", valueString: ");
		result.append(valueString);
		result.append(')');
		return result.toString();
	}

} //PatchListChangeImpl
