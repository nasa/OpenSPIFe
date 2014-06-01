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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

public class TestPlanIORoundTrip extends AbstractTestPlanIORoundTrip {

	protected static final ActivityDictionary AD = ActivityDictionary.getInstance();
	
	protected EPlan createTestPlan() {
		final EPlan plan = PlanFactory.getInstance().createPlan("TestPlanRMLRoundTripping");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivityGroup group = PlanFactory.getInstance().createActivityGroup(plan);
				plan.getChildren().add(group);
				for (EActivityDef def : AD.getActivityDefs()) {
					group.getChildren().add(createActivityRandomly(def));
					plan.getChildren().add(createActivityRandomly(def));
				}
			}
		});
		return plan;
	}
	
	protected EActivity createActivityRandomly(EActivityDef def) {
		EActivity activity = PlanFactory.getInstance().createActivity(def, null);
		for (EStructuralFeature feature : def.getEAllStructuralFeatures()) {
			if (!feature.isChangeable()) {
				continue;
			}
			Object value = activity.getData().eGet(feature);
			EClassifier type = feature.getEType();
			if (type instanceof EEnum) {
				EList<EEnumLiteral> literals = ((EEnum)type).getELiterals();
				int index = (int) (Math.random() * literals.size());
				value = literals.get(index);
				if (feature.isMany()) {
					value = Collections.singletonList(value);
				}
			} else if (EcorePackage.Literals.ESTRING == type) {
				value = "String "+Math.random();
			}
			activity.getData().eSet(feature, value);
		}
		return activity;
	}
	
	protected void assertPlanEquality(EPlan planOut, EPlan planIn) throws Exception {
		assertObjectAttributeEquality(planOut, planIn);
		assertPlanChildren(planOut, planIn);
	}

	private void assertPlanChildren(EPlanElement parentOut, EPlanElement parentIn) throws UndefinedParameterException {
		int expectedChildCount = parentOut.getChildren().size();
		assertEquals(expectedChildCount, parentIn.getChildren().size());
		for (int i=0; i<expectedChildCount; i++) {
			EPlanChild childOut = parentOut.getChildren().get(i);
			EPlanChild childIn = parentIn.getChildren().get(i);
			if (childOut instanceof EActivity) {
				assertActivityEqulity(childOut, childIn, ADParameterUtils.getActivityDef((EActivity)childOut));
			}
			assertPlanChildren(childOut, childIn);
		}
	}

	protected void assertActivityEqulity(EPlanChild outChild, EPlanChild inChild, EActivityDef activityDef) throws UndefinedParameterException {
		assertEquals(activityDef.getName(), outChild.getName(), inChild.getName());
		assertEquals(activityDef.getName(), outChild.getName(), inChild.getName());
		for (EStructuralFeature feature : activityDef.getEAllStructuralFeatures()) {
			if (feature instanceof EParameterDef) {
				String featureName = feature.getName();
				Object outObject = ADParameterUtils.getParameterObject(outChild, featureName);
				Object inObject = ADParameterUtils.getParameterObject(inChild, featureName);
				if (feature instanceof EReference) {
					if (feature.isMany()) {
						List<EObject> outList = (List<EObject>) outObject;
						List<EObject> inList = (List<EObject>) inObject;
						assertEquals(outList.size(), inList.size());
						for (int i=0; i<outList.size(); i++) {
							assertObjectAttributeEquality(outList.get(i), inList.get(i));
						}
					} else {
						assertObjectAttributeEquality((EObject)outObject, (EObject)inObject);
					}
				} else {
					assertValueEquality(activityDef, feature, outObject, inObject);
				}
			}
		}
	}

	public void testPlanIO() throws Exception {
		EPlan planOut = null;
		EPlan planIn = null;
		try {
			planOut = createTestPlan();
			planIn = performRoundTrip(planOut);
			assertPlanEquality(planOut, planIn);
		} finally {
			WrapperUtils.dispose(planOut);
			WrapperUtils.dispose(planIn);
		}
	}

}
