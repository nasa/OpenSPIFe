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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;

/**
 * Access data files that are stored in a central data file not checked out by Bamboo.
 * (It's checked out or updated using the 'svn' shell command.)
 * That way, it doesn't get built into the build artifact.
 * Also, developers don't need wait for as big a checkout -- unless they plan to run tests,
 * which which case they need to check out TestData
 * and include it in the Run Configuration by checking the box in plugins.
 *    <p>
 * API is very simple.  See static method findTestData or findTestDataFile.
 *    <p>
 * Implemented one (simple) way for IDE and another (complicated) way for Bamboo.
 * Technical details are in the Bamboo implementation's JavaDoc.
 * @see SharedTestDataForBambooEnvironment
 * @see SharedTestDataForDevelopmentEnvironment
 * 
 * @author kanef
 *
 */
public class SharedTestData {
	
	private static final String DATA_PLUGIN_ID = "TestData";

	private static boolean isBamboo;
	private static SharedTestDataForBambooEnvironment bambooImplementation;	
	private static SharedTestDataForDevelopmentEnvironment ideImplementation;
	
	private static void init() throws IOException {
		if (bambooImplementation==null && ideImplementation==null) {
			isBamboo = TestUtil.isBamboo();
			if (isBamboo) {
				bambooImplementation = new SharedTestDataForBambooEnvironment(DATA_PLUGIN_ID);
			} else {
				ideImplementation = new SharedTestDataForDevelopmentEnvironment(DATA_PLUGIN_ID);
			}
			checkWhetherConfiguredCorrectly();
		}
	}
	
	/**
	 * Access data files that are stored in a central data file not checked out by Bamboo.
	 * That way, it doesn't get built into the build artifact.
	 * Also, developers not planning to run a test that uses this data need not check it out.
	 * @param pluginId -- by convention, use the plugin-id (Activator.PLUGIN_ID) as the folder name.
	 * @param relativePath
	 * @return platform:// URL when called in IDE, file:// URL when called by Bamboo
	 * @throws IOException with a message, when run in the IDE, if you don't have the data plugin checked out when you run the test
	 * @throws IOException  with a different message in Bamboo if Bamboo files organization is not as expected.
	 * @throws MalformedURLException if relativePath, for example, is malformed.
	 */
	public static URL findTestData(String pluginId, String relativePath) throws IOException {
		init();
		String pluginPath = DATA_PLUGIN_ID + "/for/" + pluginId;
		if (isBamboo) {
			File directory = bambooImplementation.findDirectoryForBambooAndCheckout(pluginPath);
			URI uri = directory.toURI();
			URI file = uri.resolve(relativePath);
			return file.toURL();
		}
		else {
			checkWhetherConfiguredCorrectly();
			String path = pluginPath + '/' + relativePath;
			return ideImplementation.findDirectoryCheckedOutInWorkspace(path);
		}
	}

	/**
	 * Access data files that are stored in a central data file not checked out
	 * by Bamboo (or by developers not planning to run a test that uses this
	 * data).
	 * 
	 * @param pluginId
	 *            by convention, use the plugin-id (Activator.PLUGIN_ID) as the
	 *            folder name.
	 * @param relativePath
	 * @return platform: URL when called in IDE, file: URL when called by Bamboo
	 * @throws IOException
	 *             with a message, when run in the IDE, if you don't have the
	 *             data plugin checked out when you run the test
	 * @throws IOException
	 *             with a different message in Bamboo if Bamboo files
	 *             organization is not as expected.
	 * @throws MalformedURLException
	 *             if relativePath, for example, is malformed.
	 */
	public static URI findTestDataURI(String pluginId, String relativePath) throws IOException,  MalformedURLException {
		return toURI(findTestData(pluginId, relativePath));
	}

	/** @see findTestDataURI */
	public static File findTestDataFile(String pluginId, String relativePath) throws IOException {
		URL url = findTestData(pluginId, relativePath);
		try {
			url = FileLocator.resolve(url);
		} catch (IOException originalError) {
			checkWhetherConfiguredCorrectly();
			// Top-level directory is there, but maybe file does not exist:
			throw originalError;
		}	
		return new File(toURI(url));
	}
	
	private static URI toURI(URL url) throws IOException {
		try {
			return URI.create(url.toURI().toString());
		} catch (URISyntaxException e) {
			LogUtil.error("URL from SharedTestData somehow didn't make a valid URI: " + url);
			throw new IOException(e);
		}
	}

	public static void checkWhetherConfiguredCorrectly() throws IOException {
		if (isBamboo) {
			bambooImplementation.checkWhetherConfiguredCorrectly();
		}
		else {
			ideImplementation.checkWhetherConfiguredCorrectly();
		}
	}

}
