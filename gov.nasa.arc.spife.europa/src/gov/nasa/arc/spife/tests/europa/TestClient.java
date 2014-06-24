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
 * Created on Apr 6, 2005
 *
 */
package gov.nasa.arc.spife.tests.europa;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManagerConfig;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.osgi.framework.Bundle;

/**
 * @author Dennis Michael Heher
 *
 */
public class TestClient {

	private final Logger trace = Logger.getLogger(TestClient.class);
	private String host; 
	private int port;
	
    //@Override
	@Before
    public void setUp() throws Exception {
    	//super.setUp();
		EuropaServerManager.setPreferences(EuropaPlugin.PREF_STORE_PREFERENCES);
    	IPreferenceStore preferenceStore = EuropaPlugin.getDefault().getPreferenceStore();
		preferenceStore.putValue("gov.nasa.arc.spife.europa.host", "localhost");
		preferenceStore.putValue("gov.nasa.arc.spife.europa.junit.host", "localhost");
	   	preferenceStore.setValue("gov.nasa.arc.spife.europa.port", EuropaPreferences.getEuropaPort());
		preferenceStore.setValue("gov.nasa.arc.spife.europa.junit.port", EuropaPreferences.getEuropaPort());
		preferenceStore.setValue("gov.nasa.arc.spife.europa.junit.modelname", "MER2");

		host = EuropaPreferences.getEuropaHost();
		port = EuropaPreferences.getEuropaPort();
		Bundle bundle = EuropaPlugin.getDefault().getBundle();
		
		EuropaServerManager.createDirInStateArea("europa-test");
		EuropaServerManager.copyFileToStateArea("data/test/MER2-initial-state.nddl", "europa-test" + File.separator + "MER2-initial-state.nddl", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/MER2-model.nddl", "europa-test" + File.separator + "MER2-model.nddl", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/NDDL.cfg", "europa-test" + File.separator + "NDDL.cfg", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/SolverConfig.xml", "europa-test" + File.separator + "SolverConfig.xml", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/TestEuropaServerManager.xml", "europa-test" + File.separator + "TestEuropaServerManager.xml", bundle);

		
		String absolutePath = EuropaServerManager.localStatePath().append("europa-test").toOSString();
		Map<String, String> replacements = new TreeMap<String, String>();
		replacements.put("\\$", absolutePath);
		
		
		String testEuropaServerManager = EuropaServerManager.localStatePath().append("europa-test").append("TestEuropaServerManager.xml").toString();
		EuropaServerManager.startServerManager(testEuropaServerManager, replacements, true, bundle);
    	System.out.println("Testing with URL: "+ host + ":" + port);
    	trace.info("Testing with URL: "+ host + ":" + port);
    }
	
	//@Override
	@After
	public void tearDown() throws Exception {
		EuropaServerManager.stopServerManager();
		//super.tearDown();
		Socket temp = null;
		int port = 8050;
		boolean done = false;

		try {
			temp = new Socket(EuropaServerManagerConfig.LOCALHOST, port);
		}
		catch(IOException ioe) {
			assertTrue(ioe instanceof ConnectException);
			done = true;
		} //want this to happen
		catch(SecurityException se){/**/} //not sure what to do here
		finally {
			try { if(temp != null) temp.close();} 
			catch(Exception e) {
				fail("Failed to close test port: " + e);
			}
		}
		EuropaServerManager.clearLocalStateArea();
		assertTrue("Failed to shut down " + EuropaPreferences.getEuropaHost() + ":" + EuropaPreferences.getEuropaPort(),
					done);
		System.out.println("After TestClient teardown");
		System.gc();
	}
}
