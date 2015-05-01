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

public class TestSuitePatternFilter implements ITestSuiteFilter {

	private final String pattern;
	
	public TestSuitePatternFilter(String pattern) {
		if (pattern.startsWith("'") && pattern.endsWith("'")) {
			this.pattern = pattern.substring(1, pattern.length()-1);
		} else if (pattern.startsWith("\"") && pattern.endsWith("\"")) {
			this.pattern = pattern.substring(1, pattern.length()-1);
		} else {
			this.pattern = pattern;
		}
	}

	@Override
	public boolean acceptTestSuite(IEnsemblePluginTest test) {
		String testClassName = test.getClass().getName();
		boolean accept = testClassName.startsWith(pattern) || testClassName.matches(pattern); 
		String outcome = (accept) ? "accepts" : "rejects";
		System.out.println("TestSuitePatternFilter(" + pattern + ") " + outcome + " " + testClassName);
		return accept;
	}

}
