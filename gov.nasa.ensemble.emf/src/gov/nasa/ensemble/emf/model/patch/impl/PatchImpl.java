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
import gov.nasa.ensemble.emf.model.patch.ObjectChanges;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFactory;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;
import gov.nasa.ensemble.emf.model.patch.PatchResourceChange;
import gov.nasa.ensemble.emf.patch.PatchRollbackException;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Patch</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchImpl#isReversed <em>Reversed</em>}</li>
 * <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchImpl#getResourceChanges <em>Resource Changes</em>}</li>
 * <li>{@link gov.nasa.ensemble.emf.model.patch.impl.PatchImpl#getObjectChanges <em>Object Changes</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PatchImpl extends EObjectImpl implements Patch {
	/**
	 * The default value of the '{@link #isReversed() <em>Reversed</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isReversed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REVERSED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReversed() <em>Reversed</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isReversed()
	 * @generated
	 * @ordered
	 */
	protected boolean reversed = REVERSED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getResourceChanges() <em>Resource Changes</em>}' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getResourceChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<PatchResourceChange> resourceChanges;

	/**
	 * The cached value of the '{@link #getObjectChanges() <em>Object Changes</em>}' containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getObjectChanges()
	 * @generated
	 * @ordered
	 */
	protected EList<ObjectChanges> objectChanges;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PatchImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PatchPackage.Literals.PATCH;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isReversed() {
		return reversed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public void setReversed(boolean newReversed) {
		reversed = newReversed;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<ObjectChanges> getObjectChanges() {
		if (objectChanges == null) {
			objectChanges = new EObjectContainmentEList<ObjectChanges>(ObjectChanges.class, this, PatchPackage.PATCH__OBJECT_CHANGES);
		}
		return objectChanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<PatchResourceChange> getResourceChanges() {
		if (resourceChanges == null) {
			resourceChanges = new EObjectContainmentEList<PatchResourceChange>(PatchResourceChange.class, this, PatchPackage.PATCH__RESOURCE_CHANGES);
		}
		return resourceChanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @throws PatchRollbackException
	 * @generated NOT
	 */
	@Override
	public void apply() throws PatchRollbackException {
		if (eResource() != null) {
			ResourceSet resourceSet = eResource().getResourceSet();
			if (resourceSet == null) {
				LogUtil.warn("Patch must be in a resource set to apply Resource Set Changes.");
			} else {
				for (PatchResourceChange resourceChange : getResourceChanges()) {
					try {
						Resource resource = resourceChange.getResource();
						if (resource != null) {
							resourceChange.apply(resource);
						}
					} catch (Exception e) {
						LogUtil.warn(e.getMessage());
					}
				}
			}
		}

		// EObject feature changes
		for (ObjectChanges entry : getObjectChanges()) {
			EObject target = entry.getObject();
			entry.getChange().apply(target);
		}

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @throws PatchRollbackException
	 * @generated NOT
	 */
	@Override
	public void applyAndReverse() throws PatchRollbackException {
		try {
			for (ObjectChanges entry : getObjectChanges()) {
				EObject target = entry.getObject();
				entry.getChange().applyAndReverse(target);
			}
			setReversed(!isReversed());
		} catch (Exception e) {
			handleApplyException(e);
		}
	}

	private void handleApplyException(Exception e) throws PatchRollbackException {
		int maxPatchString = 1000;
		LogUtil.error("applying patch", e);
		String xml = EMFUtils.convertToXML(this);
		StringBuffer buffer = new StringBuffer("patch message ");
		if (xml != null && xml.length() > maxPatchString) {
			buffer.append("(tuncated)");
			xml = xml.substring(0, maxPatchString);
		}
		buffer.append(":\n\n");
		buffer.append(xml);
		throw new PatchRollbackException(this);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public PatchFeatureChange getFeatureChange(EObject target, EStructuralFeature feature) {
		ObjectChanges changes = PatchFactory.eINSTANCE.createObjectChanges();
		getObjectChanges().add(changes);

		PatchFeatureChange change = PatchFactory.eINSTANCE.createPatchFeatureChange();
		change.setFeature(feature);
		changes.setObject(target);
		changes.setChange(change);
		return change;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public PatchResourceChange getResourceChange(Resource resource) {
		for (PatchResourceChange change : getResourceChanges()) {
			Resource r = change.getResource();
			if (CommonUtils.equals(resource.getURI(), r.getURI())) {
				return change;
			}
		}
		PatchResourceChange change = PatchFactory.eINSTANCE.createPatchResourceChange();
		change.setResource(resource);
		change.setResourceURI(resource.getURI().toString());
		return change;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case PatchPackage.PATCH__RESOURCE_CHANGES:
			return ((InternalEList<?>) getResourceChanges()).basicRemove(otherEnd, msgs);
		case PatchPackage.PATCH__OBJECT_CHANGES:
			return ((InternalEList<?>) getObjectChanges()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case PatchPackage.PATCH__REVERSED:
			return isReversed();
		case PatchPackage.PATCH__RESOURCE_CHANGES:
			return getResourceChanges();
		case PatchPackage.PATCH__OBJECT_CHANGES:
			return getObjectChanges();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case PatchPackage.PATCH__REVERSED:
			setReversed((Boolean) newValue);
			return;
		case PatchPackage.PATCH__RESOURCE_CHANGES:
			getResourceChanges().clear();
			getResourceChanges().addAll((Collection<? extends PatchResourceChange>) newValue);
			return;
		case PatchPackage.PATCH__OBJECT_CHANGES:
			getObjectChanges().clear();
			getObjectChanges().addAll((Collection<? extends ObjectChanges>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case PatchPackage.PATCH__REVERSED:
			setReversed(REVERSED_EDEFAULT);
			return;
		case PatchPackage.PATCH__RESOURCE_CHANGES:
			getResourceChanges().clear();
			return;
		case PatchPackage.PATCH__OBJECT_CHANGES:
			getObjectChanges().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case PatchPackage.PATCH__REVERSED:
			return reversed != REVERSED_EDEFAULT;
		case PatchPackage.PATCH__RESOURCE_CHANGES:
			return resourceChanges != null && !resourceChanges.isEmpty();
		case PatchPackage.PATCH__OBJECT_CHANGES:
			return objectChanges != null && !objectChanges.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (reversed: ");
		result.append(reversed);
		result.append(')');
		return result.toString();
	}

} // PatchImpl
