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

import gov.nasa.ensemble.emf.model.test.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PatchTestFactoryImpl extends EFactoryImpl implements PatchTestFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PatchTestFactory init() {
		try {
			PatchTestFactory thePatchTestFactory = (PatchTestFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.ensemble.emf/model/PatchTest.ecore"); 
			if (thePatchTestFactory != null) {
				return thePatchTestFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PatchTestFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PatchTestFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case PatchTestPackage.PATCH_TEST_MODEL: return createPatchTestModel();
			case PatchTestPackage.SOME_TEST_CLASS: return createSomeTestClass();
			case PatchTestPackage.SOME_TEST_CLASS_WITH_ID: return createSomeTestClassWithID();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PatchTestModel createPatchTestModel() {
		PatchTestModelImpl patchTestModel = new PatchTestModelImpl();
		return patchTestModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SomeTestClass createSomeTestClass() {
		SomeTestClassImpl someTestClass = new SomeTestClassImpl();
		return someTestClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SomeTestClassWithID createSomeTestClassWithID() {
		SomeTestClassWithIDImpl someTestClassWithID = new SomeTestClassWithIDImpl();
		return someTestClassWithID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PatchTestPackage getPatchTestPackage() {
		return (PatchTestPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PatchTestPackage getPackage() {
		return PatchTestPackage.eINSTANCE;
	}

} //PatchTestFactoryImpl
