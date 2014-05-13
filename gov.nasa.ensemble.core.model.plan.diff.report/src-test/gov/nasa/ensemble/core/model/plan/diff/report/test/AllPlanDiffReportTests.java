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
package gov.nasa.ensemble.core.model.plan.diff.report.test;

import gov.nasa.ensemble.common.IEnsemblePluginTest;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.INamedDefinition;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlanDiffReportTests implements IEnsemblePluginTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllPlanDiffReportTests.class.getName());
		new AllPlanDiffReportTests().addTests(suite);
		return suite;
	}

	public void addTests(TestSuite suite) {
		//$JUnit-BEGIN$
		if (ActivityDictionary.getInstance().getDefinition(INamedDefinition.class, "CrewMember") != null) {
			// Since the AD is global (I use a self-contained one for this test, but
			// there's something else needed from somewhere),
			// only Score and PLATO should run these tests, which expect to compare crewmember use.

			suite.addTestSuite(TestPlanDiffTreeHtmlFromPlanFiles.class);
			suite.addTestSuite(TestPlanDiffTableReportFromPlanFiles.class);
		}
		
//		suite.addTestSuite(diff_match_patch_test.class);
		
		//$JUnit-END$
	}
	
}
