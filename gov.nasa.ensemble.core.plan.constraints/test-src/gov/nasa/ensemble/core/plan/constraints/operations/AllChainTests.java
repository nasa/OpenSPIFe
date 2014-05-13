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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TestConstraintViolationBoundPrinter;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TestConstraintViolationDistancePrinter;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllChainTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllChainTests.class.getCanonicalName());
		//$JUnit-BEGIN$
		suite.addTestSuite(TestChainActivityDefAdd.class);
		suite.addTestSuite(TestChainCopies.class);
		suite.addTestSuite(TestChainDeletes.class);
		suite.addTestSuite(TestChainCuts.class);
		suite.addTestSuite(TestChainCutCopyPastes.class);
		suite.addTestSuite(TestConstraintViolationBoundPrinter.class);
		suite.addTestSuite(TestConstraintViolationDistancePrinter.class);
		//$JUnit-END$
		return suite;
	}
	
}
