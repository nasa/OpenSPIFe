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
package gov.nasa.ensemble.common.servermonitor;

public interface IServerMonitor {

	/**
	 * The name shown on the trim widget
	 * 
	 * @return the name shown on the trim widget
	 */
	public String getShortName();

	/**
	 * The name shown in the tooltip
	 * 
	 * @return the name shown in the tooltip
	 */
	public String getLongName();

	/**
	 * @return the main host for connecting to this type of server.
	 */
	public String getHost();

	/**
	 * @return the main port for connecting to this type of server.
	 */
	public int getPort();

	/**
	 * @return a string with some extra server specific information. If not empty, should terminate in a newline.
	 */
	public String getInfo();

	/**
	 * @return the last time we sent a request to this server
	 */
	public long lastRequestTimeMillis();

	/**
	 * @return the last time we got a response from this server
	 */
	public long lastResponseTimeMillis();

	/**
	 * @return the time that has elapsed since a response from the server
	 */
	public long timeSinceLastResponse();

	/**
	 * If we received an exception the last time we attempted contact, it should be available here.
	 * 
	 * @return
	 */
	public Throwable getException();

	/**
	 * @return whether or not this server is up, based on the results of {@link getException()}
	 */
	public boolean isUp();

	/**
	 * Start listening for changes in the server status.
	 * 
	 * @param listener
	 */
	public void addListener(IServerMonitorListener listener);

	/**
	 * Stop listening for changes in the server status.
	 * 
	 * @param listener
	 */
	public void removeListener(IServerMonitorListener listener);

}
