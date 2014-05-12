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
package gov.nasa.arc.spife.europa;

import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager;
import gov.nasa.arc.spife.europa.clientside.EuropaServerProxy;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class EuropaSessionClient 
{
	private static final Logger trace = Logger.getLogger(EuropaSessionClient.class);
	
	protected EuropaServerProxy deuropa_ = null;
	private final String sessionId_;
	private final String modelName_;
	protected Set<String> inconParams_ = null;

	/**
	 * Construct a session-based client, uniqifying the given name.
	 * @param name
	 */
	public EuropaSessionClient(String name) 
	{
		this(name, true);
	}
	
	/**
	 * Construct a session-based client using the given name.  Optionally
	 * uniqify the name using host and system time.
	 * @param name
	 * @param uniqify
	 */
	public EuropaSessionClient(String name, boolean uniqify) 
	{
		if (uniqify) 
			sessionId_ = getSessionId(name);
		else 
			sessionId_ = name;
		
		modelName_ = EuropaPreferences.getModelName();
		trace.info("session ID = " + sessionId_ + " ; modelName = " + modelName_);
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#isConnected()
	 */
	public boolean isConnected() {
		return (deuropa_ != null);
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#connect(java.lang.String, int)
	 */
	public synchronized void connect(String host, int port) 
	{
		if (isConnected()) 
			throw new IllegalStateException("already connected");

		try {
			deuropa_ = EuropaServerManager.getInstance().StartSession(sessionId_, modelName_);
		}
		catch (Exception e) {
			String msg = "Failed to Start Session on the Europa server";
			trace.error(msg,e);
			// Don't try to reconnect
			throw new EuropaFatalException(msg,e);
		}		
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#disconnect()
	 */
	public synchronized void disconnect() 
	{
		if (isConnected()) {
			deuropa_ = null;
			EuropaServerManager.getInstance().StopSession(sessionId_);
		}
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#syncExecute(gov.nasa.arc.spife.europa.EuropaCommand)
	 */
	public Object syncExecute(EuropaCommand command) 
	{
		return syncExecute(command, Collections.emptyList(), true);
	}	
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#syncExecute(gov.nasa.arc.spife.europa.EuropaCommand, java.util.List, boolean)
	 */
	public synchronized Object syncExecute(EuropaCommand command, List<? extends Object> parameters, boolean reportErrors) 
	{
		if (isConnected()) 
			return deuropa_.syncExecute(command, parameters, reportErrors);
			
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("Trying to execute a command after session shutdown: " + command.getXmlrpcString());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#queueExecute(gov.nasa.arc.spife.europa.EuropaCommand)
	 */
	public void queueExecute(EuropaCommand command) {
		queueExecute(command, Collections.emptyList());
	}	
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#queueExecute(gov.nasa.arc.spife.europa.EuropaCommand, java.util.List)
	 */
	public void queueExecute(EuropaCommand command, List<? extends Object> parameters) {
		if (!Thread.holdsLock(this)) {
			String string = "current thread must hold lock when calling queueExecute";
			trace.error(string);
			throw new IllegalStateException(string);
		}
		
		if (isConnected()) {
			deuropa_.queueExecute(command, parameters);
		} 
		else {
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("Trying to execute a command after session shutdown: " + command.getXmlrpcString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#flushQueue()
	 */
	public List<?> flushQueue() {
		if (!Thread.holdsLock(this)) {
			String string = "current thread must hold lock when calling flushQueue";
			trace.error(string);
			throw new IllegalStateException(string);
		}
		
		if (isConnected()) 
			return deuropa_.flushQueue();
			
		trace.debug("Trying to execute commands after session shutdown.");
		return Collections.emptyList();
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.arc.spife.europa.EuropaServerProxy#getLog()
	 */
	public String getLog() 
	{
		return (isConnected() 
				? EuropaServerManager.getInstance().GetLog(EuropaPreferences.getModelName(),sessionId_) 
				: null);
	}

	/*
	 * Although the C++ server returns a bool, xml-rpc transforms the value into a long,
	 * which is inconsistent with JNI. So adding this method here to take care of casting
	 * when necessary
	 */
	public boolean isConsistent() {		
		// Check if the network is consistent. Do the constraints make sense?
		Object result = syncExecute(EuropaCommand.IS_CONSISTENT, Collections.emptyList(), true);
		if (result instanceof Number) {
			Number consistent = (Number)result;
			return (consistent.longValue() != 0);
		}
		if (result instanceof Boolean)
			return ((Boolean)result).booleanValue();
		
		return false;
	}

	/*
	 * Some incon parameters are optimized out by the AD-to-NDDL translator, 
	 * this method finds out whether an incon parameter is present in the server-side model
	 */
	public synchronized boolean isInconParameter(String name)
	{
		if (inconParams_ == null) {
			// Incon params are cached since they're not supposed to change during the life of a session
			List<Object> args = new ArrayList<Object>();
			args.add(EuropaQueuer.INCON_ID);
			args.add(""); // no prefix -> get all parameters
			Object params[] = (Object[]) syncExecute(EuropaCommand.GET_INCON_PARAMETERS,args,true);
			inconParams_ = new HashSet<String>();
			for (Object param : params)
				inconParams_.add(param.toString());
		}
		
		return  inconParams_.contains(name); 
	}

	/*
	 * PHM 06/15/12: Similar method to isInconParameter.  Works for any activity and returns
	 * all the parameters that the activity has in the nddl model.
	 */
    public synchronized void getActivityParameters(String act_name, HashSet<String> actParams)
	{
    	List<Object> args = new ArrayList<Object>();
	    args.add(act_name);
	    args.add(""); // no prefix -> get all parameters
	    Object params[] = (Object[]) syncExecute(EuropaCommand.GET_INCON_PARAMETERS,args,true);
	    for (Object param : params)
		actParams.add(param.toString());
	    return; 
	}

	/**
	 * Return a list of model names from the given server.
	 * 
	 * @param europaHost
	 * @param europaPort
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getModelList(String europaHost, int europaPort) 
	{
		String[] result = EuropaServerManager.getInstance().GetModelVersions();		
		return Arrays.asList(result);
	}

	/**
	 * Delete each of the models named, from the given server.
	 * 
	 * @param europaHost
	 * @param europaPort
	 * @param modelList
	 * @throws ClientException 
	 */
	public static void deleteModels(String europaHost, int europaPort, List<String> modelList) 
	{
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String type = properties.getProperty("europa.modelname");
		for (String modelName : modelList) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ADidentifier", modelName);
			String typeName = (type != null && type.length() > 0) ? type.replace(".","_") : "COERE";
			parameters.put("type", typeName);
			try {
				Map<String, Object> result = EuropaServerManager.getInstance().DeleteModel(parameters);
				trace.info("deleted model: " + modelName + ", result: " + result);
			} 
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/*
	 * Utility methods
	 */
	
	/**
	 * Get a unique identifier that should be different from another instance of this 
	 * client running on another computer.
	 * 
	 * @return a session identifier.
	 */
	private static String getSessionId(String name) {
   	    String compID = System.getenv("HOST");
   	    if (compID == null) {
   	    	compID = System.getenv("COMPUTERNAME");
   	    }
   	    if (compID == null) {
   	    	compID = "unknown_host";
   	    }
 	    return "session_" + compID + "_" + name + "_" + System.nanoTime();
	}	
}
