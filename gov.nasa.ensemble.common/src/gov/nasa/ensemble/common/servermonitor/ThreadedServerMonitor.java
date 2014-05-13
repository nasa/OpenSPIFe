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

import gov.nasa.ensemble.common.authentication.AuthenticationUtil;
import gov.nasa.ensemble.common.net.NetworkUtils;
import gov.nasa.ensemble.common.thread.ThreadUtils;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public abstract class ThreadedServerMonitor extends ServerMonitor {

	private static final long POLLING_PERIOD = 10000; // 10 seconds
	
	public ThreadedServerMonitor() {
		if (!AuthenticationUtil.isWorkingOffline()) {
			setupMonitoringThread();
		}
	}

	private void setupMonitoringThread() {
		Thread monitoringThread = new Thread("Collaborative Editing Server Monitoring Thread") {
			@Override
			public void run() {
				while (true) {
					checkServer();
					ThreadUtils.sleep(getPollingPeriod());
				}
			}
		};
		monitoringThread.setDaemon(true);
		monitoringThread.start();
	}
	
	private long getPollingPeriod(){
		return POLLING_PERIOD;
	}
	
	private void checkServer() {
		updateLastRequestTimeMillis();
		try {
			if (isReachable()) {
				updateLastResponseTimeMillis();
			} else {
				setException(new Exception("Can't reach Plan Sharing server."));
			}
		} catch (Throwable t) {
			setException(t);
		}
	}

	protected String getReachableUrlString() {
		return getHost();
	}
	
	protected boolean isReachable() throws Exception {
		String host = getReachableUrlString();
		InetAddress address = InetAddress.getByName(new URL(host).getHost());
		if (host == null || address == null) {
			throw new IllegalArgumentException("No server host address provided.");
		}

		int responseCode = NetworkUtils.getResponseCode(host);
		switch(responseCode) {
		case HttpURLConnection.HTTP_OK:
			return true;
		case HttpURLConnection.HTTP_NOT_FOUND:
		case HttpURLConnection.HTTP_UNAUTHORIZED: // TODO: this only checks that the server is reachable, not whether we can authenticate to it.
		default: 
			throw new Exception("Server received status code " + responseCode);
		}
	}
	
}
