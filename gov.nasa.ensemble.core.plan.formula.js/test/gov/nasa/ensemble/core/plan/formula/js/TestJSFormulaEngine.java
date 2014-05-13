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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TestJSFormulaEngine extends TestCase {

	public static final ActivityDictionary AD = ActivityDictionary.getInstance();
	public static JSFormulaEngine FORMULA_ENGINE = new JSFormulaEngine();
	
	public void testJSFormulaEngine() throws Exception {
		AD.load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.formula.js/datafiles/TestJSFormulaEngine.dictionary", true));

		EActivityDef referencingActivityDef = AD.getActivityDef("ReferencingActivity");
		assertNotNull(referencingActivityDef);
		
		EStructuralFeature crewMemberFeature = referencingActivityDef.getEStructuralFeature("crewMember");
		assertNotNull(crewMemberFeature);

		EClass crewMemberDef = (EClass) AD.getEClassifier("CrewMember");
		assertNotNull(crewMemberDef);
		
		EStructuralFeature busyFeature = crewMemberDef.getEStructuralFeature("busy");
		assertNotNull(busyFeature);
		
		EFactory factory = crewMemberDef.getEPackage().getEFactoryInstance();
		EObject crewMember = factory.create(crewMemberDef);
		
		EActivity referencingActivity = PlanFactory.getInstance().createActivity(referencingActivityDef);
		referencingActivity.getData().eSet(crewMemberFeature, crewMember);
		
		assertReferenceValue(referencingActivity, crewMember, busyFeature, 0);

		crewMember.eSet(busyFeature, 1);
		assertReferenceValue(referencingActivity, crewMember, busyFeature, 1);
	}

	private void assertReferenceValue(EActivity referencingActivity, EObject crewMember, EStructuralFeature busyFeature, int busyValue) 
		throws Exception {
		assertEquals(busyValue, crewMember.eGet(busyFeature));
		Object value = FORMULA_ENGINE.getValue(referencingActivity, "crewMember.busy");
		assertEquals(busyValue, value);
	}
	
}
