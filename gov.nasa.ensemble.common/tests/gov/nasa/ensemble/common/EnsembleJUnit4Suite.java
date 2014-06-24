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

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.runners.Suite;

public class EnsembleJUnit4Suite implements IEnsemblePluginTest {
	@Override
	public void addTests(TestSuite suite) {
		//$JUnit-BEGIN$
		Suite.SuiteClasses suiteClasses = getClass().getAnnotation(Suite.SuiteClasses.class);
		if (suiteClasses == null) {
			Logger.getLogger(getClass()).warn("No @SuiteClasses annotation found");
			return;
		}
		for (Class testClass : suiteClasses.value())
			suite.addTest(new JUnit4TestAdapter(testClass));
		//$JUnit-END$
	}
}
