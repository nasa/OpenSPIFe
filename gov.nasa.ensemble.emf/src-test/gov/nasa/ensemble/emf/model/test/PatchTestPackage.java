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
package gov.nasa.ensemble.emf.model.test;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.emf.model.test.PatchTestFactory
 * @model kind="package"
 * @generated
 */
public interface PatchTestPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "test";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.emf/model/PatchTest.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "test";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PatchTestPackage eINSTANCE = gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl
	 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getPatchTestModel()
	 * @generated
	 */
	int PATCH_TEST_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__ID = 0;

	/**
	 * The feature id for the '<em><b>One Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__ONE_ATTRIBUTE = 1;

	/**
	 * The feature id for the '<em><b>Multi Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__MULTI_ATTRIBUTE = 2;

	/**
	 * The feature id for the '<em><b>One Non Containment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE = 3;

	/**
	 * The feature id for the '<em><b>Multi Non Containment References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES = 4;

	/**
	 * The feature id for the '<em><b>One Containment Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE = 5;

	/**
	 * The feature id for the '<em><b>Multi Containment Reference</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE = 6;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_TEST_MODEL_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.test.impl.SomeTestClassImpl <em>Some Test Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.test.impl.SomeTestClassImpl
	 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getSomeTestClass()
	 * @generated
	 */
	int SOME_TEST_CLASS = 1;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOME_TEST_CLASS__ATTRIBUTE = 0;

	/**
	 * The number of structural features of the '<em>Some Test Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOME_TEST_CLASS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.test.impl.SomeTestClassWithIDImpl <em>Some Test Class With ID</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.test.impl.SomeTestClassWithIDImpl
	 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getSomeTestClassWithID()
	 * @generated
	 */
	int SOME_TEST_CLASS_WITH_ID = 2;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOME_TEST_CLASS_WITH_ID__ATTRIBUTE = SOME_TEST_CLASS__ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOME_TEST_CLASS_WITH_ID__ID = SOME_TEST_CLASS_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Some Test Class With ID</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOME_TEST_CLASS_WITH_ID_FEATURE_COUNT = SOME_TEST_CLASS_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel
	 * @generated
	 */
	EClass getPatchTestModel();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getId()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EAttribute getPatchTestModel_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneAttribute <em>One Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>One Attribute</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneAttribute()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EAttribute getPatchTestModel_OneAttribute();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiAttribute <em>Multi Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Multi Attribute</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiAttribute()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EAttribute getPatchTestModel_MultiAttribute();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneNonContainmentReference <em>One Non Containment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>One Non Containment Reference</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneNonContainmentReference()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EReference getPatchTestModel_OneNonContainmentReference();

	/**
	 * Returns the meta object for the reference list '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiNonContainmentReferences <em>Multi Non Containment References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Multi Non Containment References</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiNonContainmentReferences()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EReference getPatchTestModel_MultiNonContainmentReferences();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneContainmentReference <em>One Containment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>One Containment Reference</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneContainmentReference()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EReference getPatchTestModel_OneContainmentReference();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiContainmentReference <em>Multi Containment Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Multi Containment Reference</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiContainmentReference()
	 * @see #getPatchTestModel()
	 * @generated
	 */
	EReference getPatchTestModel_MultiContainmentReference();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.test.SomeTestClass <em>Some Test Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Some Test Class</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.SomeTestClass
	 * @generated
	 */
	EClass getSomeTestClass();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.test.SomeTestClass#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.SomeTestClass#getAttribute()
	 * @see #getSomeTestClass()
	 * @generated
	 */
	EAttribute getSomeTestClass_Attribute();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.test.SomeTestClassWithID <em>Some Test Class With ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Some Test Class With ID</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.SomeTestClassWithID
	 * @generated
	 */
	EClass getSomeTestClassWithID();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.test.SomeTestClassWithID#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.emf.model.test.SomeTestClassWithID#getId()
	 * @see #getSomeTestClassWithID()
	 * @generated
	 */
	EAttribute getSomeTestClassWithID_Id();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PatchTestFactory getPatchTestFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestModelImpl
		 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getPatchTestModel()
		 * @generated
		 */
		EClass PATCH_TEST_MODEL = eINSTANCE.getPatchTestModel();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_TEST_MODEL__ID = eINSTANCE.getPatchTestModel_Id();

		/**
		 * The meta object literal for the '<em><b>One Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_TEST_MODEL__ONE_ATTRIBUTE = eINSTANCE.getPatchTestModel_OneAttribute();

		/**
		 * The meta object literal for the '<em><b>Multi Attribute</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_TEST_MODEL__MULTI_ATTRIBUTE = eINSTANCE.getPatchTestModel_MultiAttribute();

		/**
		 * The meta object literal for the '<em><b>One Non Containment Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE = eINSTANCE.getPatchTestModel_OneNonContainmentReference();

		/**
		 * The meta object literal for the '<em><b>Multi Non Containment References</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES = eINSTANCE.getPatchTestModel_MultiNonContainmentReferences();

		/**
		 * The meta object literal for the '<em><b>One Containment Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE = eINSTANCE.getPatchTestModel_OneContainmentReference();

		/**
		 * The meta object literal for the '<em><b>Multi Containment Reference</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE = eINSTANCE.getPatchTestModel_MultiContainmentReference();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.test.impl.SomeTestClassImpl <em>Some Test Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.test.impl.SomeTestClassImpl
		 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getSomeTestClass()
		 * @generated
		 */
		EClass SOME_TEST_CLASS = eINSTANCE.getSomeTestClass();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOME_TEST_CLASS__ATTRIBUTE = eINSTANCE.getSomeTestClass_Attribute();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.test.impl.SomeTestClassWithIDImpl <em>Some Test Class With ID</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.test.impl.SomeTestClassWithIDImpl
		 * @see gov.nasa.ensemble.emf.model.test.impl.PatchTestPackageImpl#getSomeTestClassWithID()
		 * @generated
		 */
		EClass SOME_TEST_CLASS_WITH_ID = eINSTANCE.getSomeTestClassWithID();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOME_TEST_CLASS_WITH_ID__ID = eINSTANCE.getSomeTestClassWithID_Id();

	}

} //PatchTestPackage
