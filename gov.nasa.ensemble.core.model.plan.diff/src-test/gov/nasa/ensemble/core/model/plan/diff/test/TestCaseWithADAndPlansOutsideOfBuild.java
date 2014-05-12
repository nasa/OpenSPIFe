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
package gov.nasa.ensemble.core.model.plan.diff.test;

import gov.nasa.ensemble.common.data.test.SharedTestData;
import gov.nasa.ensemble.core.model.plan.diff.Activator;
import gov.nasa.ensemble.tests.core.plan.TestCaseWithSpecialAD;

import java.io.IOException;
import java.net.URL;

/** 
 * @see gov.nasa.ensemble.common.data.test.SharedTestData
 *
 */
public abstract class TestCaseWithADAndPlansOutsideOfBuild extends TestCaseWithSpecialAD {

	@Override
	protected abstract URL getADlocation();

	protected URL findFile(String filename) {
		try {
			return SharedTestData.findTestData(getPluginIdForTestFolder(), filename);
		} catch (IOException e) {
			fail("Can't find file " + filename + ":\n" + e.getMessage());
			return null;
		}
	}
	
	protected String getPluginIdForTestFolder() {
		return Activator.PLUGIN_ID;
	}

}
