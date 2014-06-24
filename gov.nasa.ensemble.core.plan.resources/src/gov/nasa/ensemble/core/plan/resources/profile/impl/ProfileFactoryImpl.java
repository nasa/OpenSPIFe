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
package gov.nasa.ensemble.core.plan.resources.profile.impl;

import gov.nasa.ensemble.core.plan.resources.profile.*;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfileFactoryImpl extends EFactoryImpl implements ProfileFactory {
		
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ProfileFactory init() {
		try {
			ProfileFactory theProfileFactory = (ProfileFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/gov.nasa.ensemble.core.plan.resources/model/ResourceProfile.ecore"); 
			if (theProfileFactory != null) {
				return theProfileFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ProfileFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfileFactoryImpl() {
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
			case ProfilePackage.PROFILE_EFFECT: return createProfileEffect();
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT: return createProfileEnvelopeConstraint();
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT: return createProfileEqualityConstraint();
			case ProfilePackage.PROFILE_MEMBER: return createProfileMember();
			case ProfilePackage.RESOURCE_PROFILE_MEMBER: return createResourceProfileMember();
			case ProfilePackage.PROFILE_REFERENCE: return createProfileReference();
			case ProfilePackage.STRUCTURAL_FEATURE_PROFILE: return createStructuralFeatureProfile();
			case ProfilePackage.VIOLATION_WAIVER: return createViolationWaiver();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public ProfileEffect createProfileEffect() {
		ProfileEffect profileEffect;
		try {
			profileEffect = MissionExtender.construct(ProfileEffect.class);
		} catch (ConstructionException e) {
			profileEffect = new ProfileEffectImpl();
		}
		return profileEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileMember createProfileMember() {
		ProfileMemberImpl profileMember = new ProfileMemberImpl();
		return profileMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceProfileMember createResourceProfileMember() {
		ResourceProfileMemberImpl resourceProfileMember = new ResourceProfileMemberImpl();
		return resourceProfileMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileReference createProfileReference() {
		ProfileReferenceImpl profileReference = new ProfileReferenceImpl();
		return profileReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T> StructuralFeatureProfile<T> createStructuralFeatureProfile() {
		StructuralFeatureProfileImpl<T> structuralFeatureProfile = new StructuralFeatureProfileImpl<T>();
		return structuralFeatureProfile;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ViolationWaiver createViolationWaiver() {
		ViolationWaiverImpl violationWaiver = new ViolationWaiverImpl();
		return violationWaiver;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileEnvelopeConstraint createProfileEnvelopeConstraint() {
		ProfileEnvelopeConstraintImpl profileEnvelopeConstraint = new ProfileEnvelopeConstraintImpl();
		return profileEnvelopeConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileEqualityConstraint createProfileEqualityConstraint() {
		ProfileEqualityConstraintImpl profileEqualityConstraint = new ProfileEqualityConstraintImpl();
		return profileEqualityConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	@SuppressWarnings("unchecked")
	public StructuralFeatureProfile createStructuralFeatureProfile(EObject object, EStructuralFeature feature) {
		StructuralFeatureProfile profile = createStructuralFeatureProfile();
		IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
		if (labeler != null) {
			profile.setName(labeler.getText(object));
		}
		profile.setObject(object);
		profile.setFeature(feature);
		profile.setCategory(object.eClass().getName());
		if (feature instanceof ENumericResourceDef) {
			ENumericResourceDef def = (ENumericResourceDef) feature;
			profile.setUnits((def.getUnits() == null) ? Unit.ONE : def.getUnits());
			if (def.getDefaultValue() != null) {
				profile.setDefaultValue(def.getDefaultValue());
			}
			if (def.getMinimum() != null) {
				profile.setMinLiteral(String.valueOf(def.getMinimum()));
			}
			if (def.getMaximum() != null) {
				profile.setMaxLiteral(String.valueOf(def.getMaximum()));
			}
		}
		if (feature instanceof EResourceDef) {
			EResourceDef def = (EResourceDef) feature;
			profile.setDescription(def.getDescription());
		}
		if (feature instanceof EAttribute) {
			profile.setDataType(((EAttribute)feature).getEAttributeType());
		}
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfilePackage getProfilePackage() {
		return (ProfilePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ProfilePackage getPackage() {
		return ProfilePackage.eINSTANCE;
	}

} //ProfileFactoryImpl
