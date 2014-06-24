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
package gov.nasa.ensemble.core.plan.resources.member.impl;

import gov.nasa.ensemble.core.plan.resources.member.*;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;

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
public class MemberFactoryImpl extends EFactoryImpl implements MemberFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MemberFactory init() {
		try {
			MemberFactory theMemberFactory = (MemberFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.ensemble.core.plan.resources/model/ResourceMembers.ecore"); 
			if (theMemberFactory != null) {
				return theMemberFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MemberFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MemberFactoryImpl() {
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
			case MemberPackage.CONDITIONS: return createConditions();
			case MemberPackage.CLAIM: return createClaim();
			case MemberPackage.NAMED_CONDITION: return createNamedCondition();
			case MemberPackage.NUMERIC_RESOURCE: return createNumericResource();
			case MemberPackage.POWER_LOAD: return createPowerLoad();
			case MemberPackage.RESOURCE_CONDITIONS_MEMBER: return createResourceConditionsMember();
			case MemberPackage.SHARABLE_RESOURCE: return createSharableResource();
			case MemberPackage.STATE_RESOURCE: return createStateResource();
			case MemberPackage.UNDEFINED_RESOURCE: return createUndefinedResource();
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
	public ResourceConditionsMember createResourceConditionsMember() {
		ResourceConditionsMemberImpl resourceConditionsMember = new ResourceConditionsMemberImpl();
		return resourceConditionsMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Conditions createConditions() {
		ConditionsImpl conditions = new ConditionsImpl();
		return conditions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedCondition createNamedCondition() {
		NamedConditionImpl namedCondition = new NamedConditionImpl();
		return namedCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Claim createClaim() {
		ClaimImpl claim = new ClaimImpl();
		return claim;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PowerLoad createPowerLoad() {
		PowerLoadImpl powerLoad = new PowerLoadImpl();
		return powerLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NumericResource createNumericResource() {
		NumericResourceImpl numericResource = new NumericResourceImpl();
		return numericResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StateResource createStateResource() {
		StateResourceImpl stateResource = new StateResourceImpl();
		return stateResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UndefinedResource createUndefinedResource() {
		UndefinedResourceImpl undefinedResource = new UndefinedResourceImpl();
		return undefinedResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SharableResource createSharableResource() {
		SharableResourceImpl sharableResource = new SharableResourceImpl();
		return sharableResource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MemberPackage getMemberPackage() {
		return (MemberPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MemberPackage getPackage() {
		return MemberPackage.eINSTANCE;
	}

} //MemberFactoryImpl
