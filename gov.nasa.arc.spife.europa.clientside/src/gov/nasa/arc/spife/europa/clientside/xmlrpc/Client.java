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
package gov.nasa.arc.spife.europa.clientside.xmlrpc;

//import gov.nasa.arc.spife.europa.EuropaQueuer;
import gov.nasa.arc.spife.europa.clientside.XmlRpcUtil;
import gov.nasa.ensemble.common.GenericRunnable;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcException;
import redstone.xmlrpc.XmlRpcFault;

public class Client 
{		
	private static final Logger trace = Logger.getLogger(Client.class);

	// TODO: find a better place for this, probably in EuropaServerManager?
	public static final String INCON_ID = "THE_INCON";

    private XmlRpcClient xmlRpcClient;
    private URL serverUrl;
    private Vector<Map<String, Object>> multicalls;
    
    private boolean useTimer = false;    

    /**
	 * Constructor
     * @param hostname
     * @param port
     * @throws ClientException 
	 */
    public Client(String hostname, int port) throws ClientException {


		trace.debug("ctor entered");
    	try {
    		serverUrl = new URL("http", hostname, port, "");
			xmlRpcClient = new XmlRpcClient(serverUrl, false);
		} catch (MalformedURLException e) {
			throw new ClientException(e.getMessage());
		}	    
    	multicalls = new Vector<Map<String, Object>>();
 	    trace.debug("client created");
	}

    /**
     * Return the address that this client is connected to
     * @return
     */
    public URL getURL() {
    	return serverUrl;
    }
    
    /**
     * Send the given command to be executed.
     * 
     * @param command The command to execute.
     * @return The response for the command.
     * @throws ClientException Something went wrong.
     */
	public Object execute(String command) throws ClientException {
		return execute(command, new Vector<Object>());
	}

	/**
     * Send the given command with the given parameters to be executed.
	 * 
	 * @param command The command to execute.
	 * @param params The parameters.
	 * @return The response for the command.
	 * @throws ClientException Something went wrong.
	 */
    public Object execute(String command, Vector<Object> params) throws ClientException {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("Cmd: " + command + "   params: " + params);
		}
    	return executeCall(command, params);
    }
    
    /**
     * 
	 * @param command The command to execute.
	 * @param params The parameters.
	 * @return The response for the command.
	 * @throws ClientException Something went wrong.
     */
    private Object executeCall(final String command, final Vector params) throws ClientException {
		return WidgetUtils.avoidDisplayThread(new GenericRunnable<ClientException, Object>() {
			@Override
			public Object run() throws ClientException {
				Object result = null;
				try {
			    	if (useTimer) {
						// Debug timer.
						Vector<String> timer_vector = new Vector<String>();
						timer_vector.add("Timing " + command);
						xmlRpcClient.invoke("TimerStart", timer_vector.toArray());
					    result = xmlRpcClient.invoke(command, params.toArray());
						Object timer_result = xmlRpcClient.invoke("TimerStop", timer_vector.toArray());
						System.out.println(command + " => " + timer_result);
					} else {
					    result = xmlRpcClient.invoke(command, params.toArray());
					}
			    	if (result instanceof XmlRpcException) {
			    		throw (XmlRpcException)result;
			    	}
			    	else if(result instanceof XmlRpcFault) {
			    		throw (XmlRpcFault) result;
			    	}
				} 
				catch (XmlRpcException e) {
					String message = e.getMessage();
					if (message.startsWith("EnableGlobalFlightRuleActiveEnforcement: Unrecognized Variable")
						|| message.startsWith("EnableGlobalFlightRulePassiveEnforcement: Unrecognized Variable")) {
						return null;
					}
					if (message.startsWith("SetActivityParamValue: possible incorrect input data:")
						&& !message.contains(INCON_ID)) {
						return null;
					}
					throw new ClientException("xmlrpc code: ", e);
				}
				catch (XmlRpcFault e) {
					String message = e.getMessage();
					if (message.startsWith("EnableGlobalFlightRuleActiveEnforcement: Unrecognized Variable")
						|| message.startsWith("EnableGlobalFlightRulePassiveEnforcement: Unrecognized Variable")) {
						return null;
					}
					if (message.startsWith("SetActivityParamValue: possible incorrect input data:")
						&& !message.contains(INCON_ID)) {
						return null;
					}
					throw new ClientException("xmlrpc code: " + e.getErrorCode(), e);
				}
				return XmlRpcUtil.canonicalize(result);
		    }
		});
	}
    
    /**
     * Add the given command to a list of commands to be executed later.
     * 
     * @param command The command to execute.
     */
    public void addCall(String command) {
    	addCall(command, new Vector<Object>());
    }
    
    /**
     * Add the given command along with the given parameters to a list of commands to 
     * be executed later.
     * 
     * @param command The command to execute.
     * @param params The parameters.
     */
    public synchronized void addCall(String command, Vector<Object> params) {    	
    	// Each call is stored in a hashtable.
    	Map<String, Object> call = new HashMap<String, Object>();
        call.put("methodName", command);
        call.put("params", params);     	    	

        // Add the call to the vector of calls.
    	multicalls.addElement(call);
    }
    
    /**
     * Execute the list of commands.
     * 
     * @return The response for all of the given commands.
     */
	public synchronized List executeAll() {
		if (multicalls.size() > 0) {
			// Turn the vector of calls into an array of calls.
			Map<String, Object>[] callArray = new Map[multicalls.size()];
			multicalls.copyInto(callArray);

			// Wrap the array of hashtables back into a vector.
			Vector<Map[]> calls = new Vector<Map[]>();
			calls.addElement(callArray);

			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("multicall: " + multicalls);
			}

			// Clear out the old multicalls.
			multicalls = new Vector<Map<String, Object>>();

			// Execute the calls.
			try {
				Object result = executeCall("system.multicall", calls);
				return canonicalizeMulticallResult(callArray.length, result);
			} catch (ClientException e) {
				trace.error("executeAll failed: ", e);
			}
	 	}
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	private List canonicalizeMulticallResult(int expectedResultCount, Object result) {
		List<Object> list;
		if (result instanceof List) {
			list = (List<Object>) result;
		}
		else if(result instanceof Object[]) {
			Object[] temp = (Object[]) result;
			list = new LinkedList<Object>();
			for(int i = 0; i < temp.length; ++i)
				list.add(temp[i]);
		}
		else {
			trace.warn("executeAll returned a non-list.  wrapping.");
			list = Collections.singletonList(result);
		}
		instantiateXmlRpcExceptions(list);
		int missing = expectedResultCount - list.size();
		if (missing > 0) {
			trace.warn("missing " + missing + " results from multicall");
			list.addAll(Collections.nCopies(missing, null));
		}
		return list;
	}

	private void instantiateXmlRpcExceptions(List<Object> list) {
		ListIterator<Object> resultsIterator = list.listIterator();
		while (resultsIterator.hasNext()) {
			Object object = resultsIterator.next();
			if (object instanceof Map) {
				Map<String, Object> hashtable = (Map<String, Object>) object;
				Object faultObject = hashtable.get("faultString");
				Object faultCode = hashtable.get("faultCode");
				if ((faultObject instanceof String) && (faultCode instanceof Integer)) {
					String string = (String) faultObject;
					resultsIterator.set(new XmlRpcException(string));
				}
			}
		}
	}
    
    /**
     * Collect the list of methods that the XML-RPC server knows about.
     * 
     * @return A vector of strings
     */
	public Object getMethodsList() {
		Object methods = null;
		try {
			methods = executeCall("system.listMethods", new Vector());
		} catch (Exception e) {
			return e;
		}
	    @SuppressWarnings("unchecked")
	    Object[] methodsArray = (Object[]) methods;
	    Vector<String> result = new Vector<String>(methodsArray.length);
	    for(int i = 0; i < methodsArray.length; ++i)
	    	result.add((String)methodsArray[i]);
	    return result;
    }

}

