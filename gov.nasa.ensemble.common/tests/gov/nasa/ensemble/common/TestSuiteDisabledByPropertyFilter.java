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

import java.util.List;

public class TestSuiteDisabledByPropertyFilter implements ITestSuiteFilter {

	private List<String> disabledClassNames;
	

	public TestSuiteDisabledByPropertyFilter() {
		this("junit.tests.disabled");
	}
	
	public TestSuiteDisabledByPropertyFilter(String propertyName) {
		disabledClassNames = EnsembleProperties.getStringListPropertyValue(propertyName);
		System.out.println("TestSuiteDisabledByPropertyFilter finds " +
				propertyName + "=" + disabledClassNames);
	}

	@Override
	public boolean acceptTestSuite(IEnsemblePluginTest test) {
		return acceptTestSuite(test.getClass());
	}

	public boolean acceptTestSuite(Class testClass) {		
		if (disabledClassNames==null) return true;
		if (disabledClassNames.contains(testClass.getCanonicalName())) {
			System.out.println("TestSuiteDisabledByPropertyFilter rejects " + testClass.getCanonicalName());
			return false;
		}
		System.out.println("TestSuiteDisabledByPropertyFilter accepts " + testClass.getCanonicalName());
		return true;
	}

}
