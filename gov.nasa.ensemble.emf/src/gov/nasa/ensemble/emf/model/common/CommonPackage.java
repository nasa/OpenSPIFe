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
package gov.nasa.ensemble.emf.model.common;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see gov.nasa.ensemble.emf.model.common.CommonFactory
 * @model kind="package"
 * @generated
 */
public interface CommonPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "common";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.emf/model/Common.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "common";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CommonPackage eINSTANCE = gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.core.runtime.IAdaptable <em>IAdaptable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.core.runtime.IAdaptable
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getIAdaptable()
	 * @generated
	 */
	int IADAPTABLE = 0;

	/**
	 * The number of structural features of the '<em>IAdaptable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IADAPTABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.common.impl.ObjectFeatureImpl <em>Object Feature</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.common.impl.ObjectFeatureImpl
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getObjectFeature()
	 * @generated
	 */
	int OBJECT_FEATURE = 1;

	/**
	 * The feature id for the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_FEATURE__OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_FEATURE__FEATURE = 1;

	/**
	 * The number of structural features of the '<em>Object Feature</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_FEATURE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.common.mission.MissionExtendable <em>Mission Extendable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.common.mission.MissionExtendable
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getMissionExtendable()
	 * @generated
	 */
	int MISSION_EXTENDABLE = 2;

	/**
	 * The number of structural features of the '<em>Mission Extendable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MISSION_EXTENDABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.emf.model.common.Timepoint <em>Timepoint</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getTimepoint()
	 * @generated
	 */
	int TIMEPOINT = 3;

	/**
	 * The meta object id for the '<em>EURI</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.URI
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getEURI()
	 * @generated
	 */
	int EURI = 4;

	/**
	 * The meta object id for the '<em>EURL</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.net.URL
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getEURL()
	 * @generated
	 */
	int EURL = 5;


	/**
	 * The meta object id for the '<em>IPath</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.core.runtime.IPath
	 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getIPath()
	 * @generated
	 */
	int IPATH = 6;


	/**
	 * Returns the meta object for class '{@link org.eclipse.core.runtime.IAdaptable <em>IAdaptable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IAdaptable</em>'.
	 * @see org.eclipse.core.runtime.IAdaptable
	 * @model instanceClass="org.eclipse.core.runtime.IAdaptable"
	 * @generated
	 */
	EClass getIAdaptable();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.emf.model.common.ObjectFeature <em>Object Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Feature</em>'.
	 * @see gov.nasa.ensemble.emf.model.common.ObjectFeature
	 * @generated
	 */
	EClass getObjectFeature();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.common.ObjectFeature#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Object</em>'.
	 * @see gov.nasa.ensemble.emf.model.common.ObjectFeature#getObject()
	 * @see #getObjectFeature()
	 * @generated
	 */
	EReference getObjectFeature_Object();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.emf.model.common.ObjectFeature#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see gov.nasa.ensemble.emf.model.common.ObjectFeature#getFeature()
	 * @see #getObjectFeature()
	 * @generated
	 */
	EReference getObjectFeature_Feature();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.common.mission.MissionExtendable <em>Mission Extendable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mission Extendable</em>'.
	 * @see gov.nasa.ensemble.common.mission.MissionExtendable
	 * @model instanceClass="gov.nasa.ensemble.common.mission.MissionExtendable"
	 * @generated
	 */
	EClass getMissionExtendable();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.emf.model.common.Timepoint <em>Timepoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Timepoint</em>'.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @generated
	 */
	EEnum getTimepoint();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.emf.common.util.URI <em>EURI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EURI</em>'.
	 * @see org.eclipse.emf.common.util.URI
	 * @model instanceClass="org.eclipse.emf.common.util.URI"
	 * @generated
	 */
	EDataType getEURI();

	/**
	 * Returns the meta object for data type '{@link java.net.URL <em>EURL</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EURL</em>'.
	 * @see java.net.URL
	 * @model instanceClass="java.net.URL"
	 * @generated
	 */
	EDataType getEURL();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.core.runtime.IPath <em>IPath</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IPath</em>'.
	 * @see org.eclipse.core.runtime.IPath
	 * @model instanceClass="org.eclipse.core.runtime.IPath"
	 * @generated
	 */
	EDataType getIPath();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CommonFactory getCommonFactory();

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
		 * The meta object literal for the '{@link org.eclipse.core.runtime.IAdaptable <em>IAdaptable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.core.runtime.IAdaptable
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getIAdaptable()
		 * @generated
		 */
		EClass IADAPTABLE = eINSTANCE.getIAdaptable();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.common.impl.ObjectFeatureImpl <em>Object Feature</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.common.impl.ObjectFeatureImpl
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getObjectFeature()
		 * @generated
		 */
		EClass OBJECT_FEATURE = eINSTANCE.getObjectFeature();
		/**
		 * The meta object literal for the '<em><b>Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_FEATURE__OBJECT = eINSTANCE.getObjectFeature_Object();
		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_FEATURE__FEATURE = eINSTANCE.getObjectFeature_Feature();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.common.mission.MissionExtendable <em>Mission Extendable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.common.mission.MissionExtendable
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getMissionExtendable()
		 * @generated
		 */
		EClass MISSION_EXTENDABLE = eINSTANCE.getMissionExtendable();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.emf.model.common.Timepoint <em>Timepoint</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.emf.model.common.Timepoint
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getTimepoint()
		 * @generated
		 */
		EEnum TIMEPOINT = eINSTANCE.getTimepoint();
		/**
		 * The meta object literal for the '<em>EURI</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.emf.common.util.URI
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getEURI()
		 * @generated
		 */
		EDataType EURI = eINSTANCE.getEURI();
		/**
		 * The meta object literal for the '<em>EURL</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.net.URL
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getEURL()
		 * @generated
		 */
		EDataType EURL = eINSTANCE.getEURL();
		/**
		 * The meta object literal for the '<em>IPath</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.core.runtime.IPath
		 * @see gov.nasa.ensemble.emf.model.common.impl.CommonPackageImpl#getIPath()
		 * @generated
		 */
		EDataType IPATH = eINSTANCE.getIPath();

	}

} //CommonPackage
