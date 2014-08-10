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
package gov.nasa.ensemble.emf.model.patch;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see gov.nasa.ensemble.emf.model.patch.PatchFactory
 * @model kind="package"
 * @generated
 */
public interface PatchPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "patch";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.emf/model/Patch.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "patch";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PatchPackage eINSTANCE = gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchImpl <em>Patch</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchImpl
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatch()
	 * @generated
	 */
	int PATCH = 0;

	/**
	 * The feature id for the '<em><b>Reversed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH__REVERSED = 0;

	/**
	 * The feature id for the '<em><b>Resource Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH__RESOURCE_CHANGES = 1;

	/**
	 * The feature id for the '<em><b>Object Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH__OBJECT_CHANGES = 2;

	/**
	 * The number of structural features of the '<em>Patch</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.impl.ObjectChangesImpl <em>Object Changes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.impl.ObjectChangesImpl
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getObjectChanges()
	 * @generated
	 */
	int OBJECT_CHANGES = 1;

	/**
	 * The feature id for the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_CHANGES__OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Change</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_CHANGES__CHANGE = 1;

	/**
	 * The number of structural features of the '<em>Object Changes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_CHANGES_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl <em>Feature Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchFeatureChange()
	 * @generated
	 */
	int PATCH_FEATURE_CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__FEATURE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Value String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__VALUE_STRING = 2;

	/**
	 * The feature id for the '<em><b>Old Value String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__OLD_VALUE_STRING = 3;

	/**
	 * The feature id for the '<em><b>List Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__LIST_CHANGES = 4;

	/**
	 * The feature id for the '<em><b>Display Message</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE = 5;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE__REFERENCE = 6;

	/**
	 * The number of structural features of the '<em>Feature Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_FEATURE_CHANGE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl <em>List Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchListChange()
	 * @generated
	 */
	int PATCH_LIST_CHANGE = 3;

	/**
	 * The feature id for the '<em><b>Reversed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__REVERSED = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__INDEX = 2;

	/**
	 * The feature id for the '<em><b>Contained Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__CONTAINED_OBJECT = 3;

	/**
	 * The feature id for the '<em><b>Non Contained Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT = 4;

	/**
	 * The feature id for the '<em><b>Value String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE__VALUE_STRING = 5;

	/**
	 * The number of structural features of the '<em>List Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_LIST_CHANGE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchResourceChangeImpl <em>Resource Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchResourceChangeImpl
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchResourceChange()
	 * @generated
	 */
	int PATCH_RESOURCE_CHANGE = 4;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_RESOURCE_CHANGE__RESOURCE = 0;

	/**
	 * The feature id for the '<em><b>Resource URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_RESOURCE_CHANGE__RESOURCE_URI = 1;

	/**
	 * The feature id for the '<em><b>List Changes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_RESOURCE_CHANGE__LIST_CHANGES = 2;

	/**
	 * The number of structural features of the '<em>Resource Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PATCH_RESOURCE_CHANGE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.patch.ChangeType <em>Change Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.patch.ChangeType
	 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getChangeType()
	 * @generated
	 */
	int CHANGE_TYPE = 5;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.patch.Patch <em>Patch</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Patch</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.Patch
	 * @generated
	 */
	EClass getPatch();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.Patch#isReversed <em>Reversed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reversed</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.Patch#isReversed()
	 * @see #getPatch()
	 * @generated
	 */
	EAttribute getPatch_Reversed();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.emf.model.patch.Patch#getObjectChanges <em>Object Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Object Changes</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.Patch#getObjectChanges()
	 * @see #getPatch()
	 * @generated
	 */
	EReference getPatch_ObjectChanges();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.patch.ObjectChanges <em>Object Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Changes</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.ObjectChanges
	 * @generated
	 */
	EClass getObjectChanges();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.patch.ObjectChanges#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Object</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.ObjectChanges#getObject()
	 * @see #getObjectChanges()
	 * @generated
	 */
	EReference getObjectChanges_Object();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.emf.model.patch.ObjectChanges#getChange <em>Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Change</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.ObjectChanges#getChange()
	 * @see #getObjectChanges()
	 * @generated
	 */
	EReference getObjectChanges_Change();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.emf.model.patch.Patch#getResourceChanges <em>Resource Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resource Changes</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.Patch#getResourceChanges()
	 * @see #getPatch()
	 * @generated
	 */
	EReference getPatch_ResourceChanges();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange <em>Feature Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Change</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange
	 * @generated
	 */
	EClass getPatchFeatureChange();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getFeature()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EReference getPatchFeatureChange_Feature();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValue()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EAttribute getPatchFeatureChange_Value();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValueString <em>Value String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value String</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValueString()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EAttribute getPatchFeatureChange_ValueString();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getOldValueString <em>Old Value String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Value String</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getOldValueString()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EAttribute getPatchFeatureChange_OldValueString();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getListChanges <em>List Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>List Changes</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getListChanges()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EReference getPatchFeatureChange_ListChanges();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getDisplayMessage <em>Display Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Display Message</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getDisplayMessage()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EAttribute getPatchFeatureChange_DisplayMessage();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reference</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getReference()
	 * @see #getPatchFeatureChange()
	 * @generated
	 */
	EReference getPatchFeatureChange_Reference();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange <em>List Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>List Change</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange
	 * @generated
	 */
	EClass getPatchListChange();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#isReversed <em>Reversed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reversed</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#isReversed()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EAttribute getPatchListChange_Reversed();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getType()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EAttribute getPatchListChange_Type();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getIndex()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EAttribute getPatchListChange_Index();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getContainedObject <em>Contained Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Contained Object</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getContainedObject()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EReference getPatchListChange_ContainedObject();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getNonContainedObject <em>Non Contained Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Non Contained Object</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getNonContainedObject()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EReference getPatchListChange_NonContainedObject();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getValueString <em>Value String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value String</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchListChange#getValueString()
	 * @see #getPatchListChange()
	 * @generated
	 */
	EAttribute getPatchListChange_ValueString();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange <em>Resource Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Change</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchResourceChange
	 * @generated
	 */
	EClass getPatchResourceChange();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResource()
	 * @see #getPatchResourceChange()
	 * @generated
	 */
	EAttribute getPatchResourceChange_Resource();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResourceURI <em>Resource URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource URI</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResourceURI()
	 * @see #getPatchResourceChange()
	 * @generated
	 */
	EAttribute getPatchResourceChange_ResourceURI();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getListChanges <em>List Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>List Changes</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getListChanges()
	 * @see #getPatchResourceChange()
	 * @generated
	 */
	EReference getPatchResourceChange_ListChanges();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.emf.model.patch.ChangeType <em>Change Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Change Type</em>'.
	 * @see gov.nasa.ensemble.emf.model.patch.ChangeType
	 * @generated
	 */
	EEnum getChangeType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PatchFactory getPatchFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchImpl <em>Patch</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchImpl
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatch()
		 * @generated
		 */
		EClass PATCH = eINSTANCE.getPatch();

		/**
		 * The meta object literal for the '<em><b>Reversed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH__REVERSED = eINSTANCE.getPatch_Reversed();

		/**
		 * The meta object literal for the '<em><b>Object Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH__OBJECT_CHANGES = eINSTANCE.getPatch_ObjectChanges();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.impl.ObjectChangesImpl <em>Object Changes</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.impl.ObjectChangesImpl
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getObjectChanges()
		 * @generated
		 */
		EClass OBJECT_CHANGES = eINSTANCE.getObjectChanges();

		/**
		 * The meta object literal for the '<em><b>Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_CHANGES__OBJECT = eINSTANCE.getObjectChanges_Object();

		/**
		 * The meta object literal for the '<em><b>Change</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_CHANGES__CHANGE = eINSTANCE.getObjectChanges_Change();

		/**
		 * The meta object literal for the '<em><b>Resource Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH__RESOURCE_CHANGES = eINSTANCE.getPatch_ResourceChanges();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl <em>Feature Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchFeatureChangeImpl
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchFeatureChange()
		 * @generated
		 */
		EClass PATCH_FEATURE_CHANGE = eINSTANCE.getPatchFeatureChange();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_FEATURE_CHANGE__FEATURE = eINSTANCE.getPatchFeatureChange_Feature();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_FEATURE_CHANGE__VALUE = eINSTANCE.getPatchFeatureChange_Value();

		/**
		 * The meta object literal for the '<em><b>Value String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_FEATURE_CHANGE__VALUE_STRING = eINSTANCE.getPatchFeatureChange_ValueString();

		/**
		 * The meta object literal for the '<em><b>Old Value String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_FEATURE_CHANGE__OLD_VALUE_STRING = eINSTANCE.getPatchFeatureChange_OldValueString();

		/**
		 * The meta object literal for the '<em><b>List Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_FEATURE_CHANGE__LIST_CHANGES = eINSTANCE.getPatchFeatureChange_ListChanges();

		/**
		 * The meta object literal for the '<em><b>Display Message</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE = eINSTANCE.getPatchFeatureChange_DisplayMessage();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_FEATURE_CHANGE__REFERENCE = eINSTANCE.getPatchFeatureChange_Reference();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl <em>List Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchListChangeImpl
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchListChange()
		 * @generated
		 */
		EClass PATCH_LIST_CHANGE = eINSTANCE.getPatchListChange();

		/**
		 * The meta object literal for the '<em><b>Reversed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_LIST_CHANGE__REVERSED = eINSTANCE.getPatchListChange_Reversed();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_LIST_CHANGE__TYPE = eINSTANCE.getPatchListChange_Type();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_LIST_CHANGE__INDEX = eINSTANCE.getPatchListChange_Index();

		/**
		 * The meta object literal for the '<em><b>Contained Object</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_LIST_CHANGE__CONTAINED_OBJECT = eINSTANCE.getPatchListChange_ContainedObject();

		/**
		 * The meta object literal for the '<em><b>Non Contained Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT = eINSTANCE.getPatchListChange_NonContainedObject();

		/**
		 * The meta object literal for the '<em><b>Value String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_LIST_CHANGE__VALUE_STRING = eINSTANCE.getPatchListChange_ValueString();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.impl.PatchResourceChangeImpl <em>Resource Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchResourceChangeImpl
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getPatchResourceChange()
		 * @generated
		 */
		EClass PATCH_RESOURCE_CHANGE = eINSTANCE.getPatchResourceChange();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_RESOURCE_CHANGE__RESOURCE = eINSTANCE.getPatchResourceChange_Resource();

		/**
		 * The meta object literal for the '<em><b>Resource URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PATCH_RESOURCE_CHANGE__RESOURCE_URI = eINSTANCE.getPatchResourceChange_ResourceURI();

		/**
		 * The meta object literal for the '<em><b>List Changes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PATCH_RESOURCE_CHANGE__LIST_CHANGES = eINSTANCE.getPatchResourceChange_ListChanges();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.patch.ChangeType <em>Change Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.patch.ChangeType
		 * @see gov.nasa.ensemble.emf.model.patch.impl.PatchPackageImpl#getChangeType()
		 * @generated
		 */
		EEnum CHANGE_TYPE = eINSTANCE.getChangeType();

	}

} //PatchPackage
