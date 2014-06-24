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
package gov.nasa.ensemble.core.model.plan.constraints;

import gov.nasa.ensemble.core.model.plan.PlanPackage;

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
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory
 * @model kind="package"
 * @generated
 */
public interface ConstraintsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "constraints";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.constraints/model/ConstraintsPlanning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gov.nasa.ensemble.model.plan.constraints";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConstraintsPackage eINSTANCE = gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl <em>Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getConstraintsMember()
	 * @generated
	 */
	int CONSTRAINTS_MEMBER = 0;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Binary Temporal Constraints</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Periodic Temporal Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Chain</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER__CHAIN = PlanPackage.EMEMBER_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINTS_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 3;


	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl <em>Constraint Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getConstraintPoint()
	 * @generated
	 */
	int CONSTRAINT_POINT = 1;

	/**
	 * The feature id for the '<em><b>Anchor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_POINT__ANCHOR = 0;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_POINT__ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Endpoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_POINT__ENDPOINT = 2;

	/**
	 * The number of structural features of the '<em>Constraint Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_POINT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl <em>Temporal Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getTemporalConstraint()
	 * @generated
	 */
	int TEMPORAL_CONSTRAINT = 2;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CONSTRAINT__WAIVER_RATIONALE = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CONSTRAINT__ID = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CONSTRAINT__RATIONALE = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Temporal Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CONSTRAINT_FEATURE_COUNT = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl <em>Binary Temporal Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getBinaryTemporalConstraint()
	 * @generated
	 */
	int BINARY_TEMPORAL_CONSTRAINT = 3;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__WAIVER_RATIONALE = TEMPORAL_CONSTRAINT__WAIVER_RATIONALE;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__ID = TEMPORAL_CONSTRAINT__ID;

	/**
	 * The feature id for the '<em><b>Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__RATIONALE = TEMPORAL_CONSTRAINT__RATIONALE;

	/**
	 * The feature id for the '<em><b>Point A</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__POINT_A = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Point B</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__POINT_B = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Minimum Bminus A</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Maximum Bminus A</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Binary Temporal Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINARY_TEMPORAL_CONSTRAINT_FEATURE_COUNT = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl <em>Periodic Temporal Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getPeriodicTemporalConstraint()
	 * @generated
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT = 4;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__WAIVER_RATIONALE = TEMPORAL_CONSTRAINT__WAIVER_RATIONALE;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__ID = TEMPORAL_CONSTRAINT__ID;

	/**
	 * The feature id for the '<em><b>Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__RATIONALE = TEMPORAL_CONSTRAINT__RATIONALE;

	/**
	 * The feature id for the '<em><b>Point</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__POINT = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Earliest</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Latest</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT__LATEST = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Periodic Temporal Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERIODIC_TEMPORAL_CONSTRAINT_FEATURE_COUNT = TEMPORAL_CONSTRAINT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl <em>Temporal Chain</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl
	 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getTemporalChain()
	 * @generated
	 */
	int TEMPORAL_CHAIN = 5;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CHAIN__WAIVER_RATIONALE = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CHAIN__ID = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CHAIN__ELEMENTS = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Temporal Chain</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_CHAIN_FEATURE_COUNT = PlanPackage.IEXTERNALIZABLE_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember
	 * @generated
	 */
	EClass getConstraintsMember();

	/**
	 * Returns the meta object for the reference list '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getBinaryTemporalConstraints <em>Binary Temporal Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Binary Temporal Constraints</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getBinaryTemporalConstraints()
	 * @see #getConstraintsMember()
	 * @generated
	 */
	EReference getConstraintsMember_BinaryTemporalConstraints();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getPeriodicTemporalConstraints <em>Periodic Temporal Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Periodic Temporal Constraints</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getPeriodicTemporalConstraints()
	 * @see #getConstraintsMember()
	 * @generated
	 */
	EReference getConstraintsMember_PeriodicTemporalConstraints();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getChain <em>Chain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Chain</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getChain()
	 * @see #getConstraintsMember()
	 * @generated
	 */
	EReference getConstraintsMember_Chain();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint <em>Constraint Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint Point</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint
	 * @generated
	 */
	EClass getConstraintPoint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getAnchor <em>Anchor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Anchor</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getAnchor()
	 * @see #getConstraintPoint()
	 * @generated
	 */
	EAttribute getConstraintPoint_Anchor();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getElement()
	 * @see #getConstraintPoint()
	 * @generated
	 */
	EReference getConstraintPoint_Element();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getEndpoint <em>Endpoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Endpoint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getEndpoint()
	 * @see #getConstraintPoint()
	 * @generated
	 */
	EAttribute getConstraintPoint_Endpoint();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint <em>Temporal Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Temporal Constraint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint
	 * @generated
	 */
	EClass getTemporalConstraint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint#getId()
	 * @see #getTemporalConstraint()
	 * @generated
	 */
	EAttribute getTemporalConstraint_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint#getRationale <em>Rationale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rationale</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint#getRationale()
	 * @see #getTemporalConstraint()
	 * @generated
	 */
	EAttribute getTemporalConstraint_Rationale();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint <em>Binary Temporal Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Binary Temporal Constraint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint
	 * @generated
	 */
	EClass getBinaryTemporalConstraint();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointA <em>Point A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Point A</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointA()
	 * @see #getBinaryTemporalConstraint()
	 * @generated
	 */
	EReference getBinaryTemporalConstraint_PointA();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointB <em>Point B</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Point B</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointB()
	 * @see #getBinaryTemporalConstraint()
	 * @generated
	 */
	EReference getBinaryTemporalConstraint_PointB();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMinimumBminusA <em>Minimum Bminus A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Bminus A</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMinimumBminusA()
	 * @see #getBinaryTemporalConstraint()
	 * @generated
	 */
	EAttribute getBinaryTemporalConstraint_MinimumBminusA();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMaximumBminusA <em>Maximum Bminus A</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Bminus A</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMaximumBminusA()
	 * @see #getBinaryTemporalConstraint()
	 * @generated
	 */
	EAttribute getBinaryTemporalConstraint_MaximumBminusA();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint <em>Periodic Temporal Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Periodic Temporal Constraint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint
	 * @generated
	 */
	EClass getPeriodicTemporalConstraint();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getPoint <em>Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Point</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getPoint()
	 * @see #getPeriodicTemporalConstraint()
	 * @generated
	 */
	EReference getPeriodicTemporalConstraint_Point();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getEarliest <em>Earliest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getEarliest()
	 * @see #getPeriodicTemporalConstraint()
	 * @generated
	 */
	EAttribute getPeriodicTemporalConstraint_Earliest();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getLatest <em>Latest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getLatest()
	 * @see #getPeriodicTemporalConstraint()
	 * @generated
	 */
	EAttribute getPeriodicTemporalConstraint_Latest();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalChain <em>Temporal Chain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Temporal Chain</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalChain
	 * @generated
	 */
	EClass getTemporalChain();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalChain#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalChain#getId()
	 * @see #getTemporalChain()
	 * @generated
	 */
	EAttribute getTemporalChain_Id();

	/**
	 * Returns the meta object for the reference list '{@link gov.nasa.ensemble.core.model.plan.constraints.TemporalChain#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Elements</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.TemporalChain#getElements()
	 * @see #getTemporalChain()
	 * @generated
	 */
	EReference getTemporalChain_Elements();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ConstraintsFactory getConstraintsFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl <em>Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getConstraintsMember()
		 * @generated
		 */
		EClass CONSTRAINTS_MEMBER = eINSTANCE.getConstraintsMember();

		/**
		 * The meta object literal for the '<em><b>Binary Temporal Constraints</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS = eINSTANCE.getConstraintsMember_BinaryTemporalConstraints();

		/**
		 * The meta object literal for the '<em><b>Periodic Temporal Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS = eINSTANCE.getConstraintsMember_PeriodicTemporalConstraints();

		/**
		 * The meta object literal for the '<em><b>Chain</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINTS_MEMBER__CHAIN = eINSTANCE.getConstraintsMember_Chain();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl <em>Constraint Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintPointImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getConstraintPoint()
		 * @generated
		 */
		EClass CONSTRAINT_POINT = eINSTANCE.getConstraintPoint();

		/**
		 * The meta object literal for the '<em><b>Anchor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT_POINT__ANCHOR = eINSTANCE.getConstraintPoint_Anchor();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT_POINT__ELEMENT = eINSTANCE.getConstraintPoint_Element();

		/**
		 * The meta object literal for the '<em><b>Endpoint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT_POINT__ENDPOINT = eINSTANCE.getConstraintPoint_Endpoint();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl <em>Temporal Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getTemporalConstraint()
		 * @generated
		 */
		EClass TEMPORAL_CONSTRAINT = eINSTANCE.getTemporalConstraint();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_CONSTRAINT__ID = eINSTANCE.getTemporalConstraint_Id();

		/**
		 * The meta object literal for the '<em><b>Rationale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_CONSTRAINT__RATIONALE = eINSTANCE.getTemporalConstraint_Rationale();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl <em>Binary Temporal Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getBinaryTemporalConstraint()
		 * @generated
		 */
		EClass BINARY_TEMPORAL_CONSTRAINT = eINSTANCE.getBinaryTemporalConstraint();

		/**
		 * The meta object literal for the '<em><b>Point A</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINARY_TEMPORAL_CONSTRAINT__POINT_A = eINSTANCE.getBinaryTemporalConstraint_PointA();

		/**
		 * The meta object literal for the '<em><b>Point B</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINARY_TEMPORAL_CONSTRAINT__POINT_B = eINSTANCE.getBinaryTemporalConstraint_PointB();

		/**
		 * The meta object literal for the '<em><b>Minimum Bminus A</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A = eINSTANCE.getBinaryTemporalConstraint_MinimumBminusA();

		/**
		 * The meta object literal for the '<em><b>Maximum Bminus A</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A = eINSTANCE.getBinaryTemporalConstraint_MaximumBminusA();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl <em>Periodic Temporal Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getPeriodicTemporalConstraint()
		 * @generated
		 */
		EClass PERIODIC_TEMPORAL_CONSTRAINT = eINSTANCE.getPeriodicTemporalConstraint();

		/**
		 * The meta object literal for the '<em><b>Point</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERIODIC_TEMPORAL_CONSTRAINT__POINT = eINSTANCE.getPeriodicTemporalConstraint_Point();

		/**
		 * The meta object literal for the '<em><b>Earliest</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST = eINSTANCE.getPeriodicTemporalConstraint_Earliest();

		/**
		 * The meta object literal for the '<em><b>Latest</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERIODIC_TEMPORAL_CONSTRAINT__LATEST = eINSTANCE.getPeriodicTemporalConstraint_Latest();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl <em>Temporal Chain</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalChainImpl
		 * @see gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsPackageImpl#getTemporalChain()
		 * @generated
		 */
		EClass TEMPORAL_CHAIN = eINSTANCE.getTemporalChain();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_CHAIN__ID = eINSTANCE.getTemporalChain_Id();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPORAL_CHAIN__ELEMENTS = eINSTANCE.getTemporalChain_Elements();

	}

} //ConstraintsPackage
