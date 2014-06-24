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

import junit.framework.TestSuite;

/**
 * Implement this interface to provide some tests to be run
 * as part of the EnsemblePluginTestExtensionRegistry.
 * This is used to conveniently run all junit plugin tests.
 * 
 * @author Andrew
 * @see EnsemblePluginTestExtensionRegistry
 */
public interface IEnsemblePluginTest {

	/**
	 * Implement to add tests to the provided suite
	 * 
	 * @see TestSuite.addTest()
	 * @see TestSuite.addTestSuite()
	 * 
	 * @param suite
	 */
	public void addTests(TestSuite suite);
	
}
