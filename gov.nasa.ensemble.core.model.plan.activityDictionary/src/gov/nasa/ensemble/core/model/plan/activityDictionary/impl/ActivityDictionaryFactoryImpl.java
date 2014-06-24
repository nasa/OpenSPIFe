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
package gov.nasa.ensemble.core.model.plan.activityDictionary.impl;

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.model.plan.activityDictionary.*;

import java.util.Map;
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
public class ActivityDictionaryFactoryImpl extends EFactoryImpl implements ActivityDictionaryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ActivityDictionaryFactory init() {
		try {
			ActivityDictionaryFactory theActivityDictionaryFactory = (ActivityDictionaryFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.ensemble.core.model.plan.activityDictionary/model/ActivityDictionaryPlanning.ecore"); 
			if (theActivityDictionaryFactory != null) {
				return theActivityDictionaryFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ActivityDictionaryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActivityDictionaryFactoryImpl() {
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
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER: return createADEffectMember();
			case ActivityDictionaryPackage.AD_PLAN_MEMBER: return createADPlanMember();
			case ActivityDictionaryPackage.AD_EFFECT_ENTRY: return (EObject)createADEffectEntry();
			case ActivityDictionaryPackage.AD_EFFECT_KEY: return createADEffectKey();
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
	public ADEffectMember createADEffectMember() {
		ADEffectMemberImpl adEffectMember = new ADEffectMemberImpl();
		return adEffectMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPlanMember createADPlanMember() {
		ADPlanMemberImpl adPlanMember = new ADPlanMemberImpl();
		return adPlanMember;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<ADEffectKey, ComputableAmount> createADEffectEntry() {
		ADEffectEntryImpl adEffectEntry = new ADEffectEntryImpl();
		return adEffectEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADEffectKey createADEffectKey() {
		ADEffectKeyImpl adEffectKey = new ADEffectKeyImpl();
		return adEffectKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActivityDictionaryPackage getActivityDictionaryPackage() {
		return (ActivityDictionaryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ActivityDictionaryPackage getPackage() {
		return ActivityDictionaryPackage.eINSTANCE;
	}

} //ActivityDictionaryFactoryImpl
