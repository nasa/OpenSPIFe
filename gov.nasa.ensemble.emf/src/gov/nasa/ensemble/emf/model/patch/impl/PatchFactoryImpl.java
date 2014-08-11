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

import gov.nasa.ensemble.emf.model.patch.ChangeType;
import gov.nasa.ensemble.emf.model.patch.ObjectChanges;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFactory;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;
import gov.nasa.ensemble.emf.model.patch.PatchResourceChange;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class PatchFactoryImpl extends EFactoryImpl implements PatchFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static PatchFactory init() {
		try {
			PatchFactory thePatchFactory = (PatchFactory) EPackage.Registry.INSTANCE.getEFactory(PatchPackage.eNS_URI);
			if (thePatchFactory != null) {
				return thePatchFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PatchFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PatchFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case PatchPackage.PATCH:
			return createPatch();
		case PatchPackage.OBJECT_CHANGES:
			return createObjectChanges();
		case PatchPackage.PATCH_FEATURE_CHANGE:
			return createPatchFeatureChange();
		case PatchPackage.PATCH_LIST_CHANGE:
			return createPatchListChange();
		case PatchPackage.PATCH_RESOURCE_CHANGE:
			return createPatchResourceChange();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
		case PatchPackage.CHANGE_TYPE:
			return createChangeTypeFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case PatchPackage.CHANGE_TYPE:
			return convertChangeTypeToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Patch createPatch() {
		PatchImpl patch = new PatchImpl();
		return patch;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ObjectChanges createObjectChanges() {
		ObjectChangesImpl objectChanges = new ObjectChangesImpl();
		return objectChanges;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PatchFeatureChange createPatchFeatureChange() {
		PatchFeatureChangeImpl patchFeatureChange = new PatchFeatureChangeImpl();
		return patchFeatureChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PatchListChange createPatchListChange() {
		PatchListChangeImpl patchListChange = new PatchListChangeImpl();
		return patchListChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PatchResourceChange createPatchResourceChange() {
		PatchResourceChangeImpl patchResourceChange = new PatchResourceChangeImpl();
		return patchResourceChange;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ChangeType createChangeTypeFromString(EDataType eDataType, String initialValue) {
		ChangeType result = ChangeType.get(initialValue);
		if (result == null)
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertChangeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PatchPackage getPatchPackage() {
		return (PatchPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PatchPackage getPackage() {
		return PatchPackage.eINSTANCE;
	}

} // PatchFactoryImpl
