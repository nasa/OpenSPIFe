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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
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
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory
 * @model kind="package"
 * @generated
 */
public interface ProfilePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "profile";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/gov.nasa.ensemble.core.plan.resources/model/ResourceProfile.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "profile";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProfilePackage eINSTANCE = gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl.init();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint
	 * @generated
	 */
	EClass getProfileConstraint();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect <em>Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Effect</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect
	 * @generated
	 */
	EClass getProfileEffect();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getStartValueLiteral <em>Start Value Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Value Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getStartValueLiteral()
	 * @see #getProfileEffect()
	 * @generated
	 */
	EAttribute getProfileEffect_StartValueLiteral();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getEndValueLiteral <em>End Value Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Value Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getEndValueLiteral()
	 * @see #getProfileEffect()
	 * @generated
	 */
	EAttribute getProfileEffect_EndValueLiteral();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileMember
	 * @generated
	 */
	EClass getProfileMember();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getConstraints <em>Constraints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Constraints</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getConstraints()
	 * @see #getProfileMember()
	 * @generated
	 */
	EReference getProfileMember_Constraints();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getEffects <em>Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Effects</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getEffects()
	 * @see #getProfileMember()
	 * @generated
	 */
	EReference getProfileMember_Effects();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileConstraint()
	 * @generated
	 */
	int PROFILE_CONSTRAINT = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl <em>Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEffect()
	 * @generated
	 */
	int PROFILE_EFFECT = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl <em>Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileMember()
	 * @generated
	 */
	int PROFILE_MEMBER = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.IMember <em>IMember</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.IMember
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getIMember()
	 * @generated
	 */
	int IMEMBER = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ResourceProfileMemberImpl <em>Resource Profile Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ResourceProfileMemberImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getResourceProfileMember()
	 * @generated
	 */
	int RESOURCE_PROFILE_MEMBER = 6;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.StructuralFeatureProfileImpl <em>Structural Feature Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.StructuralFeatureProfileImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getStructuralFeatureProfile()
	 * @generated
	 */
	int STRUCTURAL_FEATURE_PROFILE = 8;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl <em>Envelope Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEnvelopeConstraint()
	 * @generated
	 */
	int PROFILE_ENVELOPE_CONSTRAINT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl <em>Equality Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEqualityConstraint()
	 * @generated
	 */
	int PROFILE_EQUALITY_CONSTRAINT = 4;

	/**
	 * The number of structural features of the '<em>IMember</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMEMBER_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl <em>Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileReference()
	 * @generated
	 */
	int PROFILE_REFERENCE = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__ID = 0;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__METADATA = 1;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__START_OFFSET = 2;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__START_OFFSET_AMOUNT = 3;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__START_OFFSET_TIMEPOINT = 4;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__END_OFFSET = 5;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__END_OFFSET_AMOUNT = 6;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__END_OFFSET_TIMEPOINT = 7;

	/**
	 * The feature id for the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE__PROFILE_KEY = 8;

	/**
	 * The number of structural features of the '<em>Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_REFERENCE_FEATURE_COUNT = 9;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__ID = PROFILE_REFERENCE__ID;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__METADATA = PROFILE_REFERENCE__METADATA;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__START_OFFSET = PROFILE_REFERENCE__START_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__START_OFFSET_AMOUNT = PROFILE_REFERENCE__START_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__START_OFFSET_TIMEPOINT = PROFILE_REFERENCE__START_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__END_OFFSET = PROFILE_REFERENCE__END_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__END_OFFSET_AMOUNT = PROFILE_REFERENCE__END_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__END_OFFSET_TIMEPOINT = PROFILE_REFERENCE__END_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__PROFILE_KEY = PROFILE_REFERENCE__PROFILE_KEY;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT__WAIVER_RATIONALE = PROFILE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_CONSTRAINT_FEATURE_COUNT = PROFILE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__ID = PROFILE_REFERENCE__ID;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__METADATA = PROFILE_REFERENCE__METADATA;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__START_OFFSET = PROFILE_REFERENCE__START_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__START_OFFSET_AMOUNT = PROFILE_REFERENCE__START_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__START_OFFSET_TIMEPOINT = PROFILE_REFERENCE__START_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__END_OFFSET = PROFILE_REFERENCE__END_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__END_OFFSET_AMOUNT = PROFILE_REFERENCE__END_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__END_OFFSET_TIMEPOINT = PROFILE_REFERENCE__END_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__PROFILE_KEY = PROFILE_REFERENCE__PROFILE_KEY;

	/**
	 * The feature id for the '<em><b>Start Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__START_VALUE_LITERAL = PROFILE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT__END_VALUE_LITERAL = PROFILE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EFFECT_FEATURE_COUNT = PROFILE_REFERENCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__ID = PROFILE_CONSTRAINT__ID;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__METADATA = PROFILE_CONSTRAINT__METADATA;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__START_OFFSET = PROFILE_CONSTRAINT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__START_OFFSET_AMOUNT = PROFILE_CONSTRAINT__START_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__START_OFFSET_TIMEPOINT = PROFILE_CONSTRAINT__START_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__END_OFFSET = PROFILE_CONSTRAINT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__END_OFFSET_AMOUNT = PROFILE_CONSTRAINT__END_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__END_OFFSET_TIMEPOINT = PROFILE_CONSTRAINT__END_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__PROFILE_KEY = PROFILE_CONSTRAINT__PROFILE_KEY;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE = PROFILE_CONSTRAINT__WAIVER_RATIONALE;

	/**
	 * The feature id for the '<em><b>Min Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL = PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Max Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL = PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>From AD</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__FROM_AD = PROFILE_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Waivers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT__WAIVERS = PROFILE_CONSTRAINT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Envelope Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_ENVELOPE_CONSTRAINT_FEATURE_COUNT = PROFILE_CONSTRAINT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__ID = PROFILE_CONSTRAINT__ID;

	/**
	 * The feature id for the '<em><b>Metadata</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__METADATA = PROFILE_CONSTRAINT__METADATA;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__START_OFFSET = PROFILE_CONSTRAINT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__START_OFFSET_AMOUNT = PROFILE_CONSTRAINT__START_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__START_OFFSET_TIMEPOINT = PROFILE_CONSTRAINT__START_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__END_OFFSET = PROFILE_CONSTRAINT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__END_OFFSET_AMOUNT = PROFILE_CONSTRAINT__END_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__END_OFFSET_TIMEPOINT = PROFILE_CONSTRAINT__END_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__PROFILE_KEY = PROFILE_CONSTRAINT__PROFILE_KEY;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE = PROFILE_CONSTRAINT__WAIVER_RATIONALE;

	/**
	 * The feature id for the '<em><b>Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL = PROFILE_CONSTRAINT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Maximum Gap</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP = PROFILE_CONSTRAINT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Equality Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_EQUALITY_CONSTRAINT_FEATURE_COUNT = PROFILE_CONSTRAINT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Constraints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_MEMBER__CONSTRAINTS = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_MEMBER__EFFECTS = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Resource Profiles</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES = IMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Profile Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_PROFILE_MEMBER_FEATURE_COUNT = IMEMBER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__ID = JSciencePackage.PROFILE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__NAME = JSciencePackage.PROFILE__NAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__CATEGORY = JSciencePackage.PROFILE__CATEGORY;

	/**
	 * The feature id for the '<em><b>External Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__EXTERNAL_CONDITION = JSciencePackage.PROFILE__EXTERNAL_CONDITION;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__UNITS = JSciencePackage.PROFILE__UNITS;

	/**
	 * The feature id for the '<em><b>Min Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__MIN_LITERAL = JSciencePackage.PROFILE__MIN_LITERAL;

	/**
	 * The feature id for the '<em><b>Max Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__MAX_LITERAL = JSciencePackage.PROFILE__MAX_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__DEFAULT_VALUE = JSciencePackage.PROFILE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__EXTENT = JSciencePackage.PROFILE__EXTENT;

	/**
	 * The feature id for the '<em><b>Interpolation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__INTERPOLATION = JSciencePackage.PROFILE__INTERPOLATION;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__VALID = JSciencePackage.PROFILE__VALID;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__DATA_TYPE = JSciencePackage.PROFILE__DATA_TYPE;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__ATTRIBUTES = JSciencePackage.PROFILE__ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>Data Points</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__DATA_POINTS = JSciencePackage.PROFILE__DATA_POINTS;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__DESCRIPTION = JSciencePackage.PROFILE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__OBJECT = JSciencePackage.PROFILE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE__FEATURE = JSciencePackage.PROFILE_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Structural Feature Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRUCTURAL_FEATURE_PROFILE_FEATURE_COUNT = JSciencePackage.PROFILE_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl <em>Violation Waiver</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl
	 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getViolationWaiver()
	 * @generated
	 */
	int VIOLATION_WAIVER = 9;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIOLATION_WAIVER__ID = 0;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIOLATION_WAIVER__START = 1;

	/**
	 * The feature id for the '<em><b>End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIOLATION_WAIVER__END = 2;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIOLATION_WAIVER__WAIVER_RATIONALE = 3;

	/**
	 * The number of structural features of the '<em>Violation Waiver</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VIOLATION_WAIVER_FEATURE_COUNT = 4;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember <em>Resource Profile Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Profile Member</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember
	 * @generated
	 */
	EClass getResourceProfileMember();

	/**
	 * Returns the meta object for the reference list '{@link gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember#getResourceProfiles <em>Resource Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Resource Profiles</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember#getResourceProfiles()
	 * @see #getResourceProfileMember()
	 * @generated
	 */
	EReference getResourceProfileMember_ResourceProfiles();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference
	 * @generated
	 */
	EClass getProfileReference();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getId()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_Id();

	/**
	 * Returns the meta object for the map '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Metadata</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getMetadata()
	 * @see #getProfileReference()
	 * @generated
	 */
	EReference getProfileReference_Metadata();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffset <em>End Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffset()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_EndOffset();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetAmount <em>End Offset Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset Amount</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetAmount()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_EndOffsetAmount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetTimepoint <em>End Offset Timepoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset Timepoint</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetTimepoint()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_EndOffsetTimepoint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getProfileKey <em>Profile Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Profile Key</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getProfileKey()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_ProfileKey();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffset <em>Start Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffset()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_StartOffset();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetAmount <em>Start Offset Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset Amount</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetAmount()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_StartOffsetAmount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset Timepoint</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetTimepoint()
	 * @see #getProfileReference()
	 * @generated
	 */
	EAttribute getProfileReference_StartOffsetTimepoint();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile <em>Structural Feature Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Structural Feature Profile</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile
	 * @generated
	 */
	EClass getStructuralFeatureProfile();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getDescription()
	 * @see #getStructuralFeatureProfile()
	 * @generated
	 */
	EAttribute getStructuralFeatureProfile_Description();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Object</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getObject()
	 * @see #getStructuralFeatureProfile()
	 * @generated
	 */
	EReference getStructuralFeatureProfile_Object();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile#getFeature()
	 * @see #getStructuralFeatureProfile()
	 * @generated
	 */
	EReference getStructuralFeatureProfile_Feature();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver <em>Violation Waiver</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Violation Waiver</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver
	 * @generated
	 */
	EClass getViolationWaiver();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getId()
	 * @see #getViolationWaiver()
	 * @generated
	 */
	EAttribute getViolationWaiver_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getStart()
	 * @see #getViolationWaiver()
	 * @generated
	 */
	EAttribute getViolationWaiver_Start();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getEnd()
	 * @see #getViolationWaiver()
	 * @generated
	 */
	EAttribute getViolationWaiver_End();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getWaiverRationale <em>Waiver Rationale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Waiver Rationale</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver#getWaiverRationale()
	 * @see #getViolationWaiver()
	 * @generated
	 */
	EAttribute getViolationWaiver_WaiverRationale();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.IMember <em>IMember</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IMember</em>'.
	 * @see gov.nasa.ensemble.core.plan.IMember
	 * @model instanceClass="gov.nasa.ensemble.core.plan.IMember"
	 * @generated
	 */
	EClass getIMember();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint <em>Envelope Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Envelope Constraint</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint
	 * @generated
	 */
	EClass getProfileEnvelopeConstraint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMinLiteral <em>Min Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMinLiteral()
	 * @see #getProfileEnvelopeConstraint()
	 * @generated
	 */
	EAttribute getProfileEnvelopeConstraint_MinLiteral();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMaxLiteral <em>Max Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMaxLiteral()
	 * @see #getProfileEnvelopeConstraint()
	 * @generated
	 */
	EAttribute getProfileEnvelopeConstraint_MaxLiteral();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#isFromAD <em>From AD</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From AD</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#isFromAD()
	 * @see #getProfileEnvelopeConstraint()
	 * @generated
	 */
	EAttribute getProfileEnvelopeConstraint_FromAD();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getWaivers <em>Waivers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Waivers</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getWaivers()
	 * @see #getProfileEnvelopeConstraint()
	 * @generated
	 */
	EReference getProfileEnvelopeConstraint_Waivers();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint <em>Equality Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Equality Constraint</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint
	 * @generated
	 */
	EClass getProfileEqualityConstraint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getValueLiteral <em>Value Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getValueLiteral()
	 * @see #getProfileEqualityConstraint()
	 * @generated
	 */
	EAttribute getProfileEqualityConstraint_ValueLiteral();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getMaximumGap <em>Maximum Gap</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum Gap</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getMaximumGap()
	 * @see #getProfileEqualityConstraint()
	 * @generated
	 */
	EAttribute getProfileEqualityConstraint_MaximumGap();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ProfileFactory getProfileFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileConstraint()
		 * @generated
		 */
		EClass PROFILE_CONSTRAINT = eINSTANCE.getProfileConstraint();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl <em>Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEffectImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEffect()
		 * @generated
		 */
		EClass PROFILE_EFFECT = eINSTANCE.getProfileEffect();

		/**
		 * The meta object literal for the '<em><b>Start Value Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_EFFECT__START_VALUE_LITERAL = eINSTANCE.getProfileEffect_StartValueLiteral();

		/**
		 * The meta object literal for the '<em><b>End Value Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_EFFECT__END_VALUE_LITERAL = eINSTANCE.getProfileEffect_EndValueLiteral();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl <em>Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileMember()
		 * @generated
		 */
		EClass PROFILE_MEMBER = eINSTANCE.getProfileMember();

		/**
		 * The meta object literal for the '<em><b>Constraints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_MEMBER__CONSTRAINTS = eINSTANCE.getProfileMember_Constraints();

		/**
		 * The meta object literal for the '<em><b>Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_MEMBER__EFFECTS = eINSTANCE.getProfileMember_Effects();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ResourceProfileMemberImpl <em>Resource Profile Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ResourceProfileMemberImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getResourceProfileMember()
		 * @generated
		 */
		EClass RESOURCE_PROFILE_MEMBER = eINSTANCE.getResourceProfileMember();

		/**
		 * The meta object literal for the '<em><b>Resource Profiles</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES = eINSTANCE.getResourceProfileMember_ResourceProfiles();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl <em>Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileReferenceImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileReference()
		 * @generated
		 */
		EClass PROFILE_REFERENCE = eINSTANCE.getProfileReference();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__ID = eINSTANCE.getProfileReference_Id();

		/**
		 * The meta object literal for the '<em><b>Metadata</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_REFERENCE__METADATA = eINSTANCE.getProfileReference_Metadata();

		/**
		 * The meta object literal for the '<em><b>End Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__END_OFFSET = eINSTANCE.getProfileReference_EndOffset();

		/**
		 * The meta object literal for the '<em><b>End Offset Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__END_OFFSET_AMOUNT = eINSTANCE.getProfileReference_EndOffsetAmount();

		/**
		 * The meta object literal for the '<em><b>End Offset Timepoint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__END_OFFSET_TIMEPOINT = eINSTANCE.getProfileReference_EndOffsetTimepoint();

		/**
		 * The meta object literal for the '<em><b>Profile Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__PROFILE_KEY = eINSTANCE.getProfileReference_ProfileKey();

		/**
		 * The meta object literal for the '<em><b>Start Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__START_OFFSET = eINSTANCE.getProfileReference_StartOffset();

		/**
		 * The meta object literal for the '<em><b>Start Offset Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__START_OFFSET_AMOUNT = eINSTANCE.getProfileReference_StartOffsetAmount();

		/**
		 * The meta object literal for the '<em><b>Start Offset Timepoint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_REFERENCE__START_OFFSET_TIMEPOINT = eINSTANCE.getProfileReference_StartOffsetTimepoint();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.StructuralFeatureProfileImpl <em>Structural Feature Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.StructuralFeatureProfileImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getStructuralFeatureProfile()
		 * @generated
		 */
		EClass STRUCTURAL_FEATURE_PROFILE = eINSTANCE.getStructuralFeatureProfile();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRUCTURAL_FEATURE_PROFILE__DESCRIPTION = eINSTANCE.getStructuralFeatureProfile_Description();

		/**
		 * The meta object literal for the '<em><b>Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STRUCTURAL_FEATURE_PROFILE__OBJECT = eINSTANCE.getStructuralFeatureProfile_Object();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STRUCTURAL_FEATURE_PROFILE__FEATURE = eINSTANCE.getStructuralFeatureProfile_Feature();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl <em>Violation Waiver</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getViolationWaiver()
		 * @generated
		 */
		EClass VIOLATION_WAIVER = eINSTANCE.getViolationWaiver();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIOLATION_WAIVER__ID = eINSTANCE.getViolationWaiver_Id();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIOLATION_WAIVER__START = eINSTANCE.getViolationWaiver_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIOLATION_WAIVER__END = eINSTANCE.getViolationWaiver_End();

		/**
		 * The meta object literal for the '<em><b>Waiver Rationale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VIOLATION_WAIVER__WAIVER_RATIONALE = eINSTANCE.getViolationWaiver_WaiverRationale();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.IMember <em>IMember</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.IMember
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getIMember()
		 * @generated
		 */
		EClass IMEMBER = eINSTANCE.getIMember();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl <em>Envelope Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEnvelopeConstraint()
		 * @generated
		 */
		EClass PROFILE_ENVELOPE_CONSTRAINT = eINSTANCE.getProfileEnvelopeConstraint();

		/**
		 * The meta object literal for the '<em><b>Min Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL = eINSTANCE.getProfileEnvelopeConstraint_MinLiteral();

		/**
		 * The meta object literal for the '<em><b>Max Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL = eINSTANCE.getProfileEnvelopeConstraint_MaxLiteral();

		/**
		 * The meta object literal for the '<em><b>From AD</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_ENVELOPE_CONSTRAINT__FROM_AD = eINSTANCE.getProfileEnvelopeConstraint_FromAD();

		/**
		 * The meta object literal for the '<em><b>Waivers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE_ENVELOPE_CONSTRAINT__WAIVERS = eINSTANCE.getProfileEnvelopeConstraint_Waivers();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl <em>Equality Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl
		 * @see gov.nasa.ensemble.core.plan.resources.profile.impl.ProfilePackageImpl#getProfileEqualityConstraint()
		 * @generated
		 */
		EClass PROFILE_EQUALITY_CONSTRAINT = eINSTANCE.getProfileEqualityConstraint();

		/**
		 * The meta object literal for the '<em><b>Value Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL = eINSTANCE.getProfileEqualityConstraint_ValueLiteral();

		/**
		 * The meta object literal for the '<em><b>Maximum Gap</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP = eINSTANCE.getProfileEqualityConstraint_MaximumGap();

	}

} //ProfilePackage
