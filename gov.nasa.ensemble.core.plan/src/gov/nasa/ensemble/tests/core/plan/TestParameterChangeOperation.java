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
 * 
 */
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Date;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RunnableWithResult;

/**
 * @author Andrew
 *
 */
public class TestParameterChangeOperation extends UndoableOperationTestCase {

	private static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	private static final String PARAM1 = "testParamSameChangeFor";
	private static final String PARAM2 = "testParamChangeFor";
	
	private EActivityDef activityDef = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		EActivityDef def = TestPlanUtils.createActivityDef(0, getClass().getSimpleName(), "test");
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM1 + "INTEGER", EcorePackage.Literals.EINTEGER_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM2 + "INTEGER", EcorePackage.Literals.EINTEGER_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM1 + "FLOAT",   EcorePackage.Literals.EFLOAT_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM2 + "FLOAT",   EcorePackage.Literals.EFLOAT_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM1 + "BOOLEAN", EcorePackage.Literals.EBOOLEAN_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM2 + "BOOLEAN", EcorePackage.Literals.EBOOLEAN_OBJECT);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM1 + "DATE",    EcorePackage.Literals.EDATE);
		TestPlanUtils.addArgumentDefToActivityDef(def, PARAM2 + "DATE",    EcorePackage.Literals.EDATE);
		
		ActivityDictionary ad = ActivityDictionary.getInstance();
		ad.clearCache();
		ad.getEClassifiers().add(def);
		ad.fireActivityDictionaryLoadedEvent();
		
		activityDef = def;
	}
	
	/*
	 * Integer tests
	 */
	
	public void testSameChangeIntegerFromNullToNull() {
		testSameChange("INTEGER", null);
	}
	
	public void testChangeIntegerFromNullToValue() {
		testChange("INTEGER", null, 4);
	}

	public void testChangeIntegerFromValueToNull() {
		testChange("INTEGER", 5, null);
	}

	public void testSameChangeIntegerFromValueToValue() {
		testSameChange("INTEGER", 9);
	}
	
	public void testChangeIntegerFromValueToValue() {
		testChange("INTEGER", -3, 8);
	}

	/*
	 * Float tests
	 */
	
	public void testSameChangeFloatFromNullToNull() {
		testSameChange("FLOAT", null);
	}
	
	public void testChangeFloatFromNullToValue() {
		testChange("FLOAT", null, 4.2f);
	}

	public void testChangeFloatFromValueToNull() {
		testChange("FLOAT", 5.3f, null);
	}

	public void testChangeFloatFromValueToValue() {
		testChange("FLOAT", -3.4f, 8.2f);
	}

	public void testSameChangeFloatFromValueToValue() {
		testSameChange("FLOAT", 9.2f);
	}
	
	/*
	 * Boolean tests
	 */
	
	public void testSameChangeBooleanFromNullToNull() {
		testSameChange("BOOLEAN", null);
	}

	public void testChangeBooleanFromNullToValue() {
		testChange("BOOLEAN", null, true);
	}

	public void testChangeBooleanFromValueToNull() {
		testChange("BOOLEAN", false, null);
	}

	public void testChangeBooleanFromTrueToFalse() {
		testChange("BOOLEAN", true, false);
	}

	public void testChangeBooleanFromFalseToTrue() {
		testChange("BOOLEAN", false, true);
	}

	public void testSameChangeBooleanFromFalseToFalse() {
		testSameChange("BOOLEAN", false);
	}

	public void testSameChangeBooleanFromTrueToTrue() {
		testSameChange("BOOLEAN", true);
	}

	/*
	 * Date tests
	 */
	
	public void testSameChangeDateFromNullToNull() {
		testSameChange("DATE", null);
	}
	
	public void testChangeDateFromNullToValue() {
		testChange("DATE", null, new Date());
	}
	
	public void testChangeDateFromValueToNull() {
		testChange("DATE", null, new Date());
	}
	
	public void testChangeDateFromLowValueToHighValue() {
		testChange("DATE", new Date(3456), new Date());
	}
	
	public void testChangeDateFromHighValueToLowValue() {
		testChange("DATE", new Date(), new Date(5060));
	}
	
	public void testSameChangeDateFromValueToValue() {
		testSameChange("DATE", new Date());
	}
	
	/**
	 * Given the type and an old object and new object,
	 * create a test plan with a parameter of the given type.
	 * Set it to the old object and then create a parameter
	 * change operation to change it to the new object.
	 * Run the parameter change operation through the typical
	 * undo/redo, plan state, and postcondition checks.
	 * 
	 * @param plan
	 * @param parameter
	 * @param object
	 */
	private <T> void testChange(String type, final T oldObject, final T newObject) {
		final EPlan plan = PLAN_FACTORY.createPlanInstance("TestParameterChangeOperation");
		final ResourceSet set = TransactionUtils.createTransactionResourceSet(false);
		EActivity act = TransactionUtils.writing(set, new RunnableWithResult.Impl<EActivity>() {
			@Override
			public void run() {
				set.getResources().add(plan.eResource());
				EActivityGroup group = PLAN_FACTORY.createActivityGroup(plan);
				group.setName("group1");
				plan.getChildren().add(group);
				EActivity act = PLAN_FACTORY.createActivity(activityDef, group);
				group.getChildren().add(act);
				setResult(act);
			}
		});
		String parameterName = PARAM2 + type;
		final EObject data = act.getData();
		final EStructuralFeature feature;
        try {
	        feature = ADParameterUtils.getParameterFeature(act, parameterName);
        } catch (UndefinedParameterException e) {
	        throw new RuntimeException(e);
        }
        TransactionUtils.writing(data, new Runnable() {
        	@Override
			public void run() {
        		data.eSet(feature, oldObject);
        	}
        });
		IUndoableOperation operation = new FeatureTransactionChangeOperation("testChange", data, feature, oldObject, newObject);
		testUndoableOperation(plan, operation, new Runnable() {
			@Override
			public void run() {
				assertEquals(newObject, data.eGet(feature));
			}
		});
		WrapperUtils.dispose(plan);
	}
	
	/**
	 * Given the type and an object, create a test plan with 
	 * a parameter of the given type.  Set it to the object 
	 * and then create a parameter change operation to change 
	 * it to the same object.  Run the operation and ensure
	 * that the plan has not been affected.
	 * 
	 * @param type
	 * @param object
	 */
	private <T> void testSameChange(String type, final T object) {
		final EPlan plan = PLAN_FACTORY.createPlanInstance("TestParameterChangeOperation");
		final ResourceSet set = TransactionUtils.createTransactionResourceSet(false);
		EActivity act = TransactionUtils.writing(set, new RunnableWithResult.Impl<EActivity>() {
			@Override
			public void run() {
				set.getResources().add(plan.eResource());
				EActivityGroup group = PLAN_FACTORY.createActivityGroup(plan);
				group.setName("group1");
				plan.getChildren().add(group);
				EActivity act = PLAN_FACTORY.createActivity(activityDef, group);
				group.getChildren().add(act);
				setResult(act);
			}
		});
		
		String parameterName = PARAM1 + type;

		final EObject data = act.getData();
		final EStructuralFeature feature;
        try {
	        feature = ADParameterUtils.getParameterFeature(act, parameterName);
        } catch (UndefinedParameterException e) {
	        throw new RuntimeException(e);
        }
        TransactionUtils.writing(data, new Runnable() {
        	@Override
			public void run() {
        		data.eSet(feature, object);
        	}
        });

		final String state = EMFUtils.convertToXML(plan);

		IUndoableOperation operation = new FeatureTransactionChangeOperation("testSameChange", data, feature, object, object);
		
		// perform the operation
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(operation, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}
		
		// "same" changing the parameter should not modify the plan 
		assertEquals(state, EMFUtils.convertToXML(plan));
		
		TransactionUtils.getDomain(plan).dispose();
	}
    
}
