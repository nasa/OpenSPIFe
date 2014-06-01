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
/*
 * Created on Aug 30, 2004
 */
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.common.IEnsemblePluginTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author jmfox
 *
 */
public class AllTests implements IEnsemblePluginTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getCanonicalName());
		new AllTests().addTests(suite);
		return suite;
	}

	@Override
	public void addTests(TestSuite suite) {
		//$JUnit-BEGIN$
		//$JUnit-END$
	}
}
