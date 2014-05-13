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
package gov.nasa.ensemble.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Like TestSuite but respects junit.tests.disabled from ensemble.properties */
public class FilteredTestSuite extends TestSuite {
	
	TestSuiteDisabledByPropertyFilter filter = new TestSuiteDisabledByPropertyFilter();

	public FilteredTestSuite(String canonicalName) {
		super(canonicalName);
	}

	@Override
	public void addTest(Test test) {
		if (filter.acceptTestSuite(test.getClass())) {
			super.addTest(test);
		}
	}

	@Override
	public void addTestSuite(Class<? extends TestCase> testClass) {
		System.out.println("FilteredTestSuite.addTestSuite()");
		if (filter.acceptTestSuite(testClass)) {
			super.addTestSuite(testClass);
		}
	}

	
}
