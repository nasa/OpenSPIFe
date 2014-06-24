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
package gov.nasa.ensemble.common.authentication;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.extension.BasicExtensionRegistry;

import java.net.URL;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.log4j.Logger;

public class AuthenticationUtil {

	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(AuthenticationUtil.class);
	private static Authenticator instance = null;
	private static boolean lookedForInstance = false;
	private static boolean workingOffline = false; 
	public static Map<URL,Authenticator> instancesForSpecificServerURLs = new HashMap<URL, Authenticator>();
	private static final String ESTHER_THE_TESTER_USERNAME = "esther";
	private static final boolean USE_TEST_CREDENTIALS = EnsembleProperties.getBooleanPropertyValue("collaborative.editing.test.credentials", false);
	
	// generate a unique ID for this instance of the client
	public static final String MY_CLIENT_ID  = new VMID().toString();	
	
	/**
	 * @return the Authenticator iff the System property
	 *         the extension point 'gov.nasa.ensemble.common.Authenticator' is defined with a
	 *         valid Authenticator, else return null.
	 */
	public static synchronized Authenticator getAuthenticator() {	
		if (!lookedForInstance) {
			lookedForInstance = true;
			BasicExtensionRegistry<Authenticator> registry = new BasicExtensionRegistry<Authenticator>(Authenticator.class, "gov.nasa.ensemble.common.Authenticator", "authenticatorClass");
			for (Authenticator authenticator : registry.getInstances()) {
				if (authenticator.isEnabled()) {
					instance = authenticator;
					break;
				}
			}
		}	
		return instance;
	}
	
	/** An alternative to relying on the extension mechanism */
	public static synchronized void setAuthenticator(Authenticator authenticator) {
		instance = authenticator;
	}
	
	/**
	 * @return the username of the user logged into this Ensemble application
	 */
	public static String getEnsembleUser() {
		String ensembleUser = null;
		
		// first ask the authenticator for the Ensemble user
		Authenticator authenticator = getAuthenticator();
		if (authenticator != null)
			ensembleUser = authenticator.getEnsembleUser();
		
		// if there is no authenticator or the user has not authenticated, then
		// ask the system properties for a username
		if (ensembleUser == null)
			ensembleUser = System.getProperty("user.name");
		
		return ensembleUser;
	}
	
	/**
	 * @return the unique id for this instance of the client
	 */
	public static String getMyClientId()
	{
		return MY_CLIENT_ID;
	}
	
	public static boolean isUsingTestCredentials() {
		Authenticator authenticator = getAuthenticator();
		return USE_TEST_CREDENTIALS	//boolean is set  AND
				&& (authenticator == null || //there's no authenticator OR
					!authenticator.isAuthenticated() || //it's not authenticated OR
					authenticator.getCredentials() == null || //no credentials are set OR
					CommonUtils.equals(ESTHER_THE_TESTER_USERNAME, authenticator.getCredentials().getUserName())); // if it's authenticated with ESTER_THE_TESTER username
	}
	
	public static UsernamePasswordCredentials getTestCredentials() {
		return new UsernamePasswordCredentials(ESTHER_THE_TESTER_USERNAME, "Te$ter#C1");
	}

	public static boolean isWorkingOffline() {
		return workingOffline && !USE_TEST_CREDENTIALS;
	}

	public static void setWorkingOffline(boolean workingOffline) {
		if (workingOffline && isUsingTestCredentials()) {
			Authenticator authenticator = getAuthenticator();
			UsernamePasswordCredentials testCredentials = getTestCredentials();
			authenticator.setCredentialKey(testCredentials.getUserName(), testCredentials.getPassword());
			System.err.println("Hello there! I'm Esther The Tester.");
		}
		AuthenticationUtil.workingOffline = workingOffline;
	}
	
}
