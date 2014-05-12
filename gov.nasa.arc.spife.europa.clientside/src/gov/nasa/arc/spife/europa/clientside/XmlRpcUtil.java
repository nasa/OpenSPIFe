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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import redstone.xmlrpc.XmlRpcClient;


public class XmlRpcUtil {

	public static XmlRpcClient createXmlRpcClient(String hostname, Integer port) throws MalformedURLException {
		XmlRpcClient client  = new XmlRpcClient(new URL("http", hostname, port, ""), false);
		return client;
	}
	
	public static Object canonicalize(Object input) {
		if(input instanceof ArrayList) {
			Object[] foo = ((ArrayList)input).toArray();
			canonicalize(foo);
			return foo;
		}
		else if(input instanceof Vector) {
			Object[] foo = ((Vector)input).toArray();
			canonicalize(foo);
			return foo;
		}
		else if(input instanceof Map) {
			Map foo = canonicalize((Map)input);
			return foo;
		}
		return input;
	}
	
	private static void canonicalize(Object[] input) {
		for(int i = 0; i < input.length; ++i)
			input[i] = canonicalize(input[i]);
	}
	
	private static Map canonicalize(Map input) {
		Map retval = new HashMap();
		for(Object key : input.keySet()) {
			retval.put(key, canonicalize(input.get(key)));
		}
		return retval;
	}
	
}
