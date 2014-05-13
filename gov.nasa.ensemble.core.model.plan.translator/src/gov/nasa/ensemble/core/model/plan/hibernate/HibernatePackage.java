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
package gov.nasa.ensemble.core.model.plan.hibernate;

import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

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
 * @see gov.nasa.ensemble.core.model.plan.hibernate.HibernateFactory
 * @model kind="package"
 * @generated
 */
public interface HibernatePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "hibernate";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.translator/model/HibernatePlanning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gov.nasa.ensemble.model.plan.hibernate";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	HibernatePackage eINSTANCE = gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernatePackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernateMemberImpl <em>Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernateMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernatePackageImpl#getHibernateMember()
	 * @generated
	 */
	int HIBERNATE_MEMBER = 0;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Wrapper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE_MEMBER__WRAPPER = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIBERNATE_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '<em>HPlan Element</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernatePackageImpl#getHPlanElement()
	 * @generated
	 */
	int HPLAN_ELEMENT = 1;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember
	 * @generated
	 */
	EClass getHibernateMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember#getWrapper <em>Wrapper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wrapper</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember#getWrapper()
	 * @see #getHibernateMember()
	 * @generated
	 */
	EAttribute getHibernateMember_Wrapper();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement <em>HPlan Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>HPlan Element</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement
	 * @model instanceClass="gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement"
	 *        annotation="ExtendedMetaData baseType='ecore:EJavaObject'"
	 * @generated
	 */
	EDataType getHPlanElement();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	HibernateFactory getHibernateFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernateMemberImpl <em>Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernateMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernatePackageImpl#getHibernateMember()
		 * @generated
		 */
		EClass HIBERNATE_MEMBER = eINSTANCE.getHibernateMember();

		/**
		 * The meta object literal for the '<em><b>Wrapper</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HIBERNATE_MEMBER__WRAPPER = eINSTANCE.getHibernateMember_Wrapper();

		/**
		 * The meta object literal for the '<em>HPlan Element</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement
		 * @see gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernatePackageImpl#getHPlanElement()
		 * @generated
		 */
		EDataType HPLAN_ELEMENT = eINSTANCE.getHPlanElement();

	}

} //HibernatePackage
