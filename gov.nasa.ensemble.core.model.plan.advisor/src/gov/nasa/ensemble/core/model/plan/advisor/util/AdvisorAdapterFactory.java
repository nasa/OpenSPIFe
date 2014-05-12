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
package gov.nasa.ensemble.core.model.plan.advisor.util;

import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.advisor.*;

import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage
 * @generated
 */
public class AdvisorAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AdvisorPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdvisorAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = AdvisorPackage.eINSTANCE;
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
	protected AdvisorSwitch<Adapter> modelSwitch =
		new AdvisorSwitch<Adapter>() {
			@Override
			public Adapter caseRuleAdvisorMember(RuleAdvisorMember object) {
				return createRuleAdvisorMemberAdapter();
			}
			@Override
			public Adapter casePlanAdvisorMember(PlanAdvisorMember object) {
				return createPlanAdvisorMemberAdapter();
			}
			@Override
			public Adapter caseActivityAdvisorMember(ActivityAdvisorMember object) {
				return createActivityAdvisorMemberAdapter();
			}
			@Override
			public Adapter caseWaiverPropertiesEntry(WaiverPropertiesEntry object) {
				return createWaiverPropertiesEntryAdapter();
			}
			@Override
			public Adapter caseIWaivable(IWaivable object) {
				return createIWaivableAdapter();
			}
			@Override
			public Adapter caseEMember(EMember object) {
				return createEMemberAdapter();
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
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember <em>Rule Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember
	 * @generated
	 */
	public Adapter createRuleAdvisorMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember <em>Plan Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember
	 * @generated
	 */
	public Adapter createPlanAdvisorMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember <em>Activity Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember
	 * @generated
	 */
	public Adapter createActivityAdvisorMemberAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry <em>Waiver Properties Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry
	 * @generated
	 */
	public Adapter createWaiverPropertiesEntryAdapter() {
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

} //AdvisorAdapterFactory
