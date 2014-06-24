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
package gov.nasa.arc.spife.europa.clientside;

// TODO : move preferences to this plugin
//import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.servermonitor.ServerMonitor;
import gov.nasa.ensemble.common.servermonitor.ServerMonitorRegistry;

public class EuropaServerMonitor extends ServerMonitor {

	private static EuropaServerMonitor instance;
	private static String info = "";
	
	// TODO: read these from preferences
	private String host = "localhost"; //EuropaPreferences.getEuropaHost();
	private int port = EuropaServerManagerConfig.DEFAULT_PORT; //EuropaPreferences.getEuropaPort();
	
	public static EuropaServerMonitor getInstance() {
		if (instance == null)
			instance = ServerMonitorRegistry.getInstance().getMonitor(EuropaServerMonitor.class);
		return instance;
	}
	
	public static void setInfo(String info) {
		EuropaServerMonitor.info = info;
	}
	
	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public String getShortName() {
		return "Europa";
	}
	
	@Override
	public String getLongName() {
		return "Europa 2";
	}
}
