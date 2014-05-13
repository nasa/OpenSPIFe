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
package gov.nasa.ensemble.core.plan.resources.profile.util;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.plan.resources.profile.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage
 * @generated
 */
public class ProfileAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ProfilePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfileAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ProfilePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileSwitch<Adapter> modelSwitch =
		new ProfileSwitch<Adapter>() {
			@Override
			public Adapter caseIMember(IMember object) {
				return createIMemberAdapter();
			}
			@Override
			public Adapter caseProfileConstraint(ProfileConstraint object) {
				return createProfileConstraintAdapter();
			}
			@Override
			public Adapter caseProfileEffect(ProfileEffect object) {
				return createProfileEffectAdapter();
			}
			@Override
			public Adapter caseProfileEnvelopeConstraint(ProfileEnvelopeConstraint object) {
				return createProfileEnvelopeConstraintAdapter();
			}
			@Override
			public Adapter caseProfileEqualityConstraint(ProfileEqualityConstraint object) {
				return createProfileEqualityConstraintAdapter();
			}
			@Override
			public Adapter caseProfileMember(ProfileMember object) {
				return createProfileMemberAdapter();
			}
			@Override
			public Adapter caseResourceProfileMember(ResourceProfileMember object) {
				return createResourceProfileMemberAdapter();
			}
			@Override
			public Adapter caseProfileReference(ProfileReference object) {
				return createProfileReferenceAdapter();
			}
			@Override
			public <T> Adapter caseStructuralFeatureProfile(StructuralFeatureProfile<T> object) {
				return createStructuralFeatureProfileAdapter();
			}
			@Override
			public Adapter caseViolationWaiver(ViolationWaiver object) {
				return createViolationWaiverAdapter();
			}
			@Override
			public Adapter caseIWaivable(IWaivable object) {
				return createIWaivableAdapter();
			}
			@Override
			public Adapter caseMissionExtendable(MissionExtendable object) {
				return createMissionExtendableAdapter();
			}
			@Override
			public Adapter caseEMember(EMember object) {
				return createEMemberAdapter();
			}
			@Override
			public <T> Adapter caseProfile(Profile<T> object) {
				return createProfileAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint
	 * @generated
	 */
	public Adapter createProfileConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect <em>Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect
	 * @generated
	 */
	public Adapter createProfileEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileMember
	 * @generated
	 */
	public Adapter createProfileMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember <em>Resource Profile Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember
	 * @generated
	 */
	public Adapter createResourceProfileMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileReference
	 * @generated
	 */
	public Adapter createProfileReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile <em>Structural Feature Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile
	 * @generated
	 */
	public Adapter createStructuralFeatureProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver <em>Violation Waiver</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver
	 * @generated
	 */
	public Adapter createViolationWaiverAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.IMember <em>IMember</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.IMember
	 * @generated
	 */
	public Adapter createIMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint <em>Envelope Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint
	 * @generated
	 */
	public Adapter createProfileEnvelopeConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint <em>Equality Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint
	 * @generated
	 */
	public Adapter createProfileEqualityConstraintAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.jscience.Profile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.jscience.Profile
	 * @generated
	 */
	public Adapter createProfileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.advisor.IWaivable <em>IWaivable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.IWaivable
	 * @generated
	 */
	public Adapter createIWaivableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.common.mission.MissionExtendable <em>Mission Extendable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.common.mission.MissionExtendable
	 * @generated
	 */
	public Adapter createMissionExtendableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.EMember <em>EMember</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.EMember
	 * @generated
	 */
	public Adapter createEMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ProfileAdapterFactory
