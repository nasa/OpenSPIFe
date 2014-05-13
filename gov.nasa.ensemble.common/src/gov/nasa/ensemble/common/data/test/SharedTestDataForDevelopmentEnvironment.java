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
package gov.nasa.ensemble.common.data.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

/**
 * This is the implementation by SharedTestData when it finds
 * it's not running on Bamboo.  The first call to its static methods
 * creates one instance of this class, which it keeps statically for the duration of the build job.
 * <p>
 * When a developer wants to run a test manually in the IDE,
 * he or she must manually check out the data plugin (since you no longer get that huge plugin
 * whether you like it or not, and more importantly, it's not part of the build),
 * and must check the box in the Run Configurations.
 * The job of this class is just to return a "platform:/" URL, or to give a
 * sensible error message if the developer has not set it up right.
 */

/* private to this package */class SharedTestDataForDevelopmentEnvironment {

	private final String ROOT_IN_IDE = "platform:/plugin/";
	private boolean alreadyKnownConfigurationIsGood = false;
	private boolean alreadyKnownConfigurationIsBad = false;
	private String dataPluginId;

	public SharedTestDataForDevelopmentEnvironment(String dataPluginId) {
		this.dataPluginId = dataPluginId;
	}

	URL findDirectoryCheckedOutInWorkspace(String path) throws MalformedURLException {
		return new URL(ROOT_IN_IDE + path);
	}

	void checkWhetherConfiguredCorrectly() throws IOException {
		if (alreadyKnownConfigurationIsGood) return;
		if (!alreadyKnownConfigurationIsBad) {
			try {
				FileLocator.resolve(new URL(ROOT_IN_IDE + dataPluginId)).openConnection();
				alreadyKnownConfigurationIsGood = true;
			} catch (IOException e) {
				alreadyKnownConfigurationIsBad = true;
			}
		}
		if (alreadyKnownConfigurationIsBad) {
			throw new IOException("Please check out \n   "
					+ dataPluginId
					+ "\n   from the svn/ensemble/trunk/data/ folder"
					+ "\n   and include it in the Run Configuration's Plugins in order to run this test.");
		}
	}

}
