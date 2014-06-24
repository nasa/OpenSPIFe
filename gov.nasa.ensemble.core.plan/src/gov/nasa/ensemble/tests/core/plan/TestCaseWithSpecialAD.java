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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

public abstract class TestCaseWithSpecialAD extends TestCase {
	
	/**
	 * I don't like the fact that the AD is a global singleton and shared across tests,
	 * when this one needs its own, but given that it is, and that we overwrite it,
	 * let's try to put it back the way we found it.
	 * This JUnit 3 superclass will load a special AD on setup and try to
	 * restore the previous one on tear-down.
	 * @author kanef
	 */
	public TestCaseWithSpecialAD() {
		super();
	}

	public TestCaseWithSpecialAD(String name) {
		super(name);
	}
	
	private URL defaultADLocation;
	
	@Override
	public void setUp() {
		try {
			defaultADLocation = ActivityDictionaryPreferences.getActivityDictionaryLocation();
		} catch (MalformedURLException e1) {
			defaultADLocation = null;
		}
		ActivityDictionary AD = ActivityDictionary.getInstance();
		
		// Now load the temporary AD.
		URL location = null;
		try {
			location = getADlocation();
		} catch (Exception e) {
			fail("Failed to find AD location: " + e.getMessage());
		}
		try {
			AD.load(location);
			if (AD.getActivityDefs().isEmpty()) {
				fail("Loaded no definitions from AD at " + location);
			}
//			debugAD(AD);
		} catch (Exception e) {
			fail("Failed to load AD from " + location + ": " + e.getMessage());
		}
		registerNamespace(AD);
	}
	
	protected void registerNamespace(ActivityDictionary AD) {
		String prefix = getDefinitionPrefix();
		if (prefix != null) {
			AD.setNsPrefix(prefix);
			AD.setNsURI(getADmodelURI());
		}
	}

	/** If the .plan file says <data xsi:type="foo:...">, it may be necessary to override this and return "foo". */
	protected String getDefinitionPrefix() {
		return null;
	}

	/** If the .plan file says xmlns:test="http://foo.nasa.gov/", it may be necessary to override this and return "http://foo.nasa.gov/". */
	private String getADmodelURI() {
		return "http://" + getDefinitionPrefix() + ".nasa.gov/";
	}

	protected abstract URL getADlocation() throws IOException;

	@Override
	public void tearDown() {
		// Restore default AD, if one was initialized (will not be, if running headless).
		ActivityDictionary AD = ActivityDictionary.getInstance();
		// If there was an AD before any test changed it, then be sure to restore it on tear-down.
		// Otherwise, don't worry about it from now on.
		if (defaultADLocation != null) {
			AD.restoreDefaultDictionary();
			try {
			assertEquals("Default AD not restored",
					defaultADLocation,
					AD.getURL());
			} catch (IOException e) {
				warn("Error getting restored AD's URL: " + e);
			}
		}
	}

	private void warn(String string) {
		LogUtil.warn(string);
	}

	@SuppressWarnings("unused")
	private void opineEquals(String message, String expected, String actual) {
		if (!expected.equals(actual)) {
			warn(message + " - Expected " + expected + " but got " + actual);
		}
	}

	@SuppressWarnings("unused")
	private void opineEquals(String message, int expected, int actual) {
		if (expected != actual) {
			warn(message + " - Expected " + expected + " but got " + actual);
		}
	}

	@SuppressWarnings("unused")
	private String getFirstFewNames(List<EActivityDef> activityDefs) {
		StringBuilder sb = new StringBuilder(150);
		for (EActivityDef def : activityDefs) {
			if (sb.length() > 0) sb.append(", ");
			String name = def.getName();
			if (sb.length()+name.length() > 100) {
				return sb.toString()+"...";
			}
			sb.append(name);
		}
		return sb.toString();
	}


	public static void debugAD(ActivityDictionary ad) {
		System.out.println("TestOSTPVExportOperation.debugAD()");
		List<EActivityDef> activityDefs = ad.getActivityDefs();
		System.out.println("  There are " + activityDefs.size() + " activities defined"
				+ " in " + ad.getNsURI()
				+ " for the '" + ad.getNsPrefix() + ":' prefix:");
		for (EActivityDef def : activityDefs) {
			System.out.println("   * " + def.getName());
		}
	}


}
