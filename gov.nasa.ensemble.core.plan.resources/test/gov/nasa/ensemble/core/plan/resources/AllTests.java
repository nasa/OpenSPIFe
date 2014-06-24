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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.common.IEnsemblePluginTest;
import gov.nasa.ensemble.core.plan.resources.conditions.TestResourceConditions;
import gov.nasa.ensemble.core.plan.resources.profile.TestProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.TestProfileOperations;
import gov.nasa.ensemble.core.plan.resources.profile.TestProfileReferenceIO;
import gov.nasa.ensemble.core.plan.resources.profile.TestResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.reference.TestContainmentReferenceUpdates;
import gov.nasa.ensemble.core.plan.resources.reference.TestReferences;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests implements IEnsemblePluginTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getCanonicalName());
		new AllTests().addTests(suite);
		return suite;
	}

	@Override
	public void addTests(TestSuite suite) {
		//$JUnit-BEGIN$
		suite.addTestSuite(TestCsvTests.class);
		suite.addTestSuite(TestActivityEffect.class);
		suite.addTestSuite(TestConstantEvaluation.class);
		suite.addTestSuite(TestDataTransferScenario.class);
		suite.addTestSuite(TestLocalVariable.class);
		suite.addTestSuite(TestReferences.class);
		suite.addTestSuite(TestProfileReferenceIO.class);
		suite.addTestSuite(TestResourceProfileMember.class);
		suite.addTestSuite(TestResourceConditions.class);
		suite.addTestSuite(TestWorkSleepScenario.class);
		suite.addTestSuite(TestProfileEffect.class);
		suite.addTestSuite(TestConditions.class);
		suite.addTestSuite(TestSummaryResource.class);
		suite.addTestSuite(TestProfileOperations.class);
		suite.addTestSuite(TestDurationUpdates.class);
		suite.addTestSuite(TestContainmentReferenceUpdates.class);
		//$JUnit-END$
	}

}
