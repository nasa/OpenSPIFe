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


import java.net.URI;

import org.apache.http.auth.UsernamePasswordCredentials;

import fj.data.Option;

public interface Authenticator {

	/**
	 * Authenticates the username and password based on the authentication scheme of the mission.
	 * 
	 * @param ensembleUser
	 * @param ensemblePassword
	 */
	public void authenticate(String ensembleUser, String ensemblePassword) throws AuthenticationException;

	/**
	 * Checks the pre-conditions before attempting to authenticate. A false value
	 * will essentially mean that this authenticator should not be used 
	 * (i.e. missing extension point, product configuration, missing property)
	 * 
	 * @return true if this particular authenticator is enabled
	 */
	public boolean isEnabled();

	/**
	 * @return true if the user has successfully authenticated, else return
	 *         false
	 */
	public boolean isAuthenticated();

	/**
	 * @return the username of the user logged into this Ensemble application
	 */
	public String getEnsembleUser();
	
	/**
	 * This method should be enough to replace the getUser() and getPass() methods, 
	 * which have now been deleted.
	 * @return a Credentials object suitable for authenticating to an http
	 *         server.  
	 */
	public UsernamePasswordCredentials getCredentials();
	
	/**
	 * This method should be enough to replace the getUser() and getPass() methods, 
	 * which have now been deleted.
	 * @return a Credentials object suitable for authenticating to an http
	 *         server.  
	 */
	public UsernamePasswordCredentials getCredentials(String quitLabel, Option<String> username);
	
	/**
	 * Hack to allow JUnit tests to set the username and password so that there
	 * is not human interaction required during unit tests.
	 * 
	 * @param user
	 * @param password
	 */
	public void setCredentialKey(String user, String password);

	public boolean handlesURI(URI uri);

}
