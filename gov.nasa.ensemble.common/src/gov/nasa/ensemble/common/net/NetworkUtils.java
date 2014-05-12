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
package gov.nasa.ensemble.common.net;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Some useful functions for doing ensemble networking.
 * 
 * @author Andrew
 */
public class NetworkUtils {

	/**
	 * This type describes the local network that we are on. 
	 * @author Andrew
	 */
	public static enum LocalNetwork {
		AMES,   // *.arc.nasa.gov
		IC_ARC, // *.ic-arc.nasa.gov
		JPL,    // *.jpl.nasa.gov
	}
	
	/**
	 * Attempt to infer what local network we are on, by checking
	 * the localhost address.
	 * @return the local network inferred, or null if one was not matched.
	 */
	public static LocalNetwork getLocalNetwork() {
		byte[] address = new byte[] { (byte)127, (byte)0, (byte)0, (byte)1 };
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			if (localhost != null) {
				// On my machine, getCanonicalHostName merely returns "pauli",
				// and not "pauli.ic-arc.nasa.gov".  So, the follow code may
				// or may not be exercised on your machine.
				if (localhost.getCanonicalHostName().contains("ic-arc")) {
					return LocalNetwork.IC_ARC;
				}
				if (localhost.getCanonicalHostName().contains("arc")) {
					return LocalNetwork.AMES;
				}
				if (localhost.getCanonicalHostName().contains("jpl")) {
					return LocalNetwork.JPL;
				}
				address = localhost.getAddress();
			}
		} catch (UnknownHostException e) {
			// fall back to default
		}
		// Assuming that a private network means IC_ARC is a little bit dodgy,
		// but probably good enough for now.  (it's only a default, after all)
		// The other catch-alls are a bit too broad as well, but good enough.
		if ((address[0] == (byte)192) && (address[1] == (byte)168)) {
			return LocalNetwork.IC_ARC;
		}
		if (address[0] == (byte)137) {
			return LocalNetwork.JPL;
		}
		if (address[0] == (byte)143) {
			return LocalNetwork.AMES;
		}
		return null;
	}

	public static int portFromURL(String url) {
		try {
			return portFromURL(new URL(url));
		} catch (MalformedURLException e) {
			return -1;
		}
	}
	
	public static int portFromURL(URL hostUrl) {
		int port = hostUrl.getPort();
		if (port == -1) {
			port = hostUrl.getDefaultPort();
		}
		if (port == -1) {
			if (hostUrl.getProtocol().equals("http")) {
				port = 80;
			} else if (hostUrl.getProtocol().equals("https")) {
				port = 443;
			}
		}
		return port;
	}
	
	public static int getResponseCode(String URL) throws Exception {
		int connectionTimeoutInMS = 3000;
		int socketOperationsTimeoutInMS = 3000;
		
		HttpURLConnection conn = null;
		URL testUrl = new URL(URL);
		conn = (HttpURLConnection) testUrl.openConnection();
		conn.setConnectTimeout(connectionTimeoutInMS);
		conn.setReadTimeout(socketOperationsTimeoutInMS);
		conn.setRequestMethod("HEAD");
		int responseCode = conn.getResponseCode();
		return responseCode;
	}
}
