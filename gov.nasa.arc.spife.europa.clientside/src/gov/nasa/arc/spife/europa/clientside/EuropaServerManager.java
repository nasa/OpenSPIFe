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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleProperties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.osgi.framework.Bundle;

public class EuropaServerManager 
{
	private static final String CONFIG_ELEMENT = "config-element";
	private static final String AD_IDENTIFIER = "ADidentifier";
	private static final String UPLOAD_DIR = "Uploads";
		
	private static final Logger LOGGER = Logger.getLogger(EuropaServerManager.class);
	private static final Map<Integer, Level> loggerLevels;
 
	private static final String TYPE = "type";
	private static final String FILES = "files";
	private static final String CONTENTS = "contents";
	private static final String FILE_TYPE = "filetype";
	private static final String SERVER_MANAGER_CONFIG = "europa.servermanager.config";
	
	private static EuropaServerManager s_manager = null;	

	private final EuropaServerManagerConfig m_config;
	private final Map<String, String> m_replacements;
	private final Map<String, EuropaServerProxy> m_sessions;
	
	public EuropaServerManager(EuropaServerManagerConfig config) 
	{
		this(config, new TreeMap<String, String>());
	}
	
	public EuropaServerManager(EuropaServerManagerConfig config, Map<String, String> replacements) 
	{
		m_config = config;
		m_replacements = replacements;
		m_sessions = new HashMap<String, EuropaServerProxy>();
	}
	
	public static EuropaServerManager getInstance() { return s_manager; }
	
	public static synchronized void startServerManager(
			String configFile, 
			boolean wait) 
	{
		startServerManager(configFile, null, wait);
	}
		
	public static synchronized void startServerManager(
			String configFile, 
			Map<String, String> configReplacements, 
			boolean wait) 
	{
		startServerManager(configFile, configReplacements, wait, EuropaClientSidePlugin.getDefault().getBundle());
	}
	
	public static synchronized void startServerManager(
			String configFile, 
			Map<String, String> configReplacements, 
			boolean wait, 
			Bundle bundle)  
	{
			if(s_manager != null)
				stopServerManager();
			assert(s_manager == null);
			
			EuropaServerManagerConfig cfg = null;

			String eventualName = null;
			try {
				eventualName = getConfigFile(configFile, bundle);
			}
			catch(IOException ioe) {
				LOGGER.error("Failed to produce configuration file",ioe);
			}

			cfg = ((eventualName == null || eventualName.length() < 1)
				? new EuropaServerManagerConfig()
				: EuropaServerManagerConfig.fromEsmConfig(eventualName, configReplacements)
			);
			
			setupNddlIncludes();

			s_manager = new EuropaServerManager(cfg, configReplacements);			
	}
	
	public static synchronized void stopServerManager() 
	{		
		if(s_manager == null) 
			return;

		try{
			s_manager.KillAllServers();
		}
		catch(Exception e) {
			LOGGER.error("Failed to stop all existing sessions.", e);
			return;
		}

		s_manager = null;
	}		
	
	public static String copyFileToStateArea(String fileName, String outputFileName, Bundle fromBundle) throws IOException {

		File outputFile = localStatePath().append(outputFileName).toFile();
		outputFile.createNewFile();
		
		OutputStream stream = new FileOutputStream(outputFile);
		InputStream input = FileLocator.openStream(fromBundle, new Path(fileName), false);
		
		IOUtils.copy(input, stream);
		input.close();
		stream.close();
		return outputFile.getAbsolutePath();
	}

	static {
		loggerLevels = new HashMap<Integer, Level>();
		loggerLevels.put(DynamicEuropaLogger.ERROR, Level.ERROR);
		loggerLevels.put(DynamicEuropaLogger.TRACE, Level.DEBUG);
		loggerLevels.put(DynamicEuropaLogger.TRACE_ALL, Level.ALL);
		loggerLevels.put(DynamicEuropaLogger.RETURN, Level.ALL);
		loggerLevels.put(DynamicEuropaLogger.TIMER, Level.TRACE);
	}
		
	public synchronized Map<String, Object> DeleteModel(Map<String, Object> modelInfo)  
	{
		if(!modelInfo.containsKey(AD_IDENTIFIER))
			return null;
		
		String version = (String) modelInfo.get(AD_IDENTIFIER);
		String type = (String) modelInfo.get(TYPE);
		
		writeLog("[method=DeleteModel] [type=" + type + "] [" + AD_IDENTIFIER + "=" + version +"]");
		
		String safeVersion = version.replaceAll("\\W", "_");
		String safeType = type.replaceAll("\\W", "_");
		
		if(!typeExists(version)) {
			writeLog("[method=DeleteModel] [error=" + 1008 + "] [message=Server version " + version + " doesn't exist.  Not deleting." + "]");
			throw new RuntimeException(new RuntimeException("Server version " + version + " doesn't exist.  Not deleting."));
		}
		
		LinkedList<File> notDeleted = new LinkedList<File>();
		File dir = localStatePath().append(UPLOAD_DIR).append(safeType).append(safeVersion).toFile();
		if(dir.exists()) {
			for(File file : dir.listFiles()) {
				if(!file.delete())
					notDeleted.add(file);
			}
			if(!dir.delete())
				notDeleted.add(dir);
		}
		
		m_config.removeConfig(version);
		
		try {
			m_config.save();
		}
		catch(IOException ioe) {
			writeLog("[method=DeleteModel] [error=666] [message=Unable to save modified configuration to disk: " + ioe + "]");
		}
		return new HashMap<String, Object>();
	}

	// TODO: More efficient to return filename and then copy to exported log file.
	public synchronized String GetLog(final String type, final String session) {
		writeLog("[method=GetLog] [session=" + session + "] [type=" + type + "]");
		// return the contents of the log file for the given session as a string.

		File logfile = localStatePath().append(session + "EuropaServer.log").toFile();
		System.out.println("Getting Europa Session Log from " + logfile.getAbsolutePath());
		String content = "";
		if (!logfile.exists())
			System.out.println("File not found");
		else if (logfile.length() == 0)
			System.out.println("File empty");
		else
			try {
				content = new String(FileUtils.readFileToString(logfile));
			} catch (IOException e) {
				System.out.println("File read error");
			}
		return content;
	}
	
	//not called anywhere, as far as I can tell
	public synchronized String[] GetLogSessions(final String type) {
		writeLog("[method=GetLogSessions] [type=" + type + "]");
		//return the values of [session=(value)] in every line with TRACE_DETAIL.*StartSession in it 
		return new String[0];
	}
	
	public synchronized String[] GetModelVersions() {
		return m_config.getConfigs().keySet().toArray(new String[0]);
	}
	
	public synchronized String[] KillAllServers() {
		writeLog("[method=KillAllServers]");
		String[] retval = m_sessions.keySet().toArray(new String[0]);
		for(String session : retval) {
			StopSession(session);
		}
		return retval;
	}
	
	//not called anywhere, as far as I can tell
	public synchronized String RollLogs() {
		return RollLogs("");
	}
	
	public synchronized String RollLogs(String type) {
		return "";
	}

	//returns a "session" hash table consisting of the session string, the port number, and the host name
	//or one of several faults
	public synchronized EuropaServerProxy StartSession() {
		return StartSession(generateSessionName());
	}

	public synchronized EuropaServerProxy StartSession(final String session)  {
		return StartSession(session, m_config.getDefaultType());
	}
	
	public synchronized EuropaServerProxy StartSession(final String session, final String type) 
	{
		long timer = System.currentTimeMillis();
		writeLog("[method=StartSession] [session=" + session + "] [type=" + type + "]", DynamicEuropaLogger.TRACE);
		
		if(!typeExists(type)) {
			writeLog("[method=StartSession] [error=1001] [session=" + session + "] [message=Server type '" + type + "' doesn't exist.]", DynamicEuropaLogger.ERROR);
			throw new RuntimeException("Server type " + type + " doesn't exist.");
		}
		
		if(m_sessions.containsKey(session)) {
			writeLog("[method=StartSession] [session=" + session + "] [message=not starting, session already exists]", DynamicEuropaLogger.ERROR);
			throw new RuntimeException("Tried to start session more than once :"+session);
		}
		
		// For now each session has its own europa server
		EuropaServerProxy newServer = makeNewEuropaServer(session,type);
		writeLog("[method=StartSession] [session=" + session +"] [message=now forking child]", DynamicEuropaLogger.TRACE);
		newServer.startServer(session);
		try {
			newServer.startSession(session,type);
		}
		catch (Exception e) {
			newServer.stopServer();
			String msg = "Failed to Start session on the server after connecting successfully";
			throw new RuntimeException(msg,e);
		}
		
		m_sessions.put(session, newServer);
		writeLog("[method=StartSession] [session=" + session + "] [type=" + type + "] [time=" + (System.currentTimeMillis() - timer) + "]", DynamicEuropaLogger.TIMER);			
		
		return newServer;
	}
	
	protected EuropaServerConfig makeNewEuropaServerConfig(final String session, final String type)
	{
		EuropaServerConfig config = m_config.getConfig(type);
		int port = (useXmlRpc() ? findPort(config.GetPort()) : 0);
		if(port < 0) {
			writeLog("[method=StartSession] [error=1000] [session=" + session + "] [message=could not find open port]", DynamicEuropaLogger.ERROR);
			throw new RuntimeException("Could not find open port for child");
		}
		
		String logFile = generateLogFileName(session,config.GetLogFile());
		EuropaServerConfig actualConfig = 
			new EuropaServerConfig(
					port, 
					config.GetDebug(), 
					config.GetVerbosity(), 
					config.GetInitialStateFilename(),
					EuropaServerManagerConfig.nddlIncludePath(config.GetInitialStateFilename()),
					config.GetHostLocal(), 
					config.GetHost(), 
					config.GetVersion(), 
					config.GetServerTimeout(), 
					logFile, 
					config.GetPlannerConfigFilename(), 
					config.GetPlannerConfigElement()
			);
		
		actualConfig.SetValue("engine_type", getEngineType());
		return actualConfig;
	}
	
	/*
	 * Creates a new server to handle a EuropaSession, currently there is a DynamicEuropa server for each client session
	 */
	protected EuropaServerProxy makeNewEuropaServer(final String session, final String type)
	{
		EuropaServerConfig serverConfig = makeNewEuropaServerConfig(session,type);

		EuropaServerProxy retval = null;
		if (useXmlRpc()) {
			retval = new EuropaServerProxyXmlRpc(serverConfig,useRemoteServer());
			LOGGER.info("Connected to DynamicEuropa through xml-rpc on "+serverConfig.GetHost()+":"+serverConfig.GetPort()+". "+session);
		}
		else {
			retval= new EuropaServerProxyJNI(serverConfig);
			LOGGER.info("Connected to DynamicEuropa through JNI. "+session);
		}
		
		return retval;
	}
	
	public synchronized String StopSession(final String session) 
	{
		writeLog("[method=StopSession] [session=" + session + "]", DynamicEuropaLogger.TRACE_ALL);
		EuropaServerProxy sessionServer = m_sessions.get(session);
		if(sessionServer != null) {
			m_sessions.remove(session);
			sessionServer.stopSession();
			sessionServer.stopServer();
		}
		return session;
	}
	
	public boolean getServerExists(String type, String version)
	{
		return typeExists(version) || typeExists(type);
	}
	
	public synchronized Map<String, Object> UploadModel(Map<String, Object> modelInfo) 
	{
		if(!modelInfo.containsKey(AD_IDENTIFIER))
			return null; //the Perl does this, 
		
		String type = (String) modelInfo.get(TYPE);
		String version = (String) modelInfo.get(AD_IDENTIFIER);
		String configElement = (modelInfo.containsKey(CONFIG_ELEMENT) ? (String) modelInfo.get(CONFIG_ELEMENT) : "MER2PassiveSolver"); //man, this default makes me feel dirty
		writeLog("[method=UploadModel] [type=" + type + "] [" + AD_IDENTIFIER + "=" + version +"]");
		
		if(getServerExists(type,version)) {
			writeLog("[method=UploadModel] [error=" + 1008 + "] [message=Server version " + version + " already exists, not uploading" + "]");
			throw new RuntimeException("Server version " + version + " already exists, not uploading");
		}

		String safeVersion = version.replaceAll("\\W", "_");
		String safeType = type.replaceAll("\\W", "_");
		//System.out.println("safe version: " + safeVersion + " safe type: " + safeType);

		//create the upload directory, type directory, and the specific directory into which to write the model files
		String fullPath = UPLOAD_DIR + File.separator + safeType + File.separator + safeVersion;
		
		File versionDir = null;
		try {
			createDirInStateArea(UPLOAD_DIR);
			createDirInStateArea(UPLOAD_DIR + File.separator + safeType);
			versionDir = createDirInStateArea(fullPath, true); //force creation, since this should fail if the version already exists in the filesystem
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to create upload directory: " + e);
		}
		
		LinkedList<File> createdFiles = new LinkedList<File>();
		File initialState = null;
		File solverConfig = null;

		//wrapped so we don't leave too many droppings
		try {
			//create the model files
			Map<String, Object> files = (Map<String, Object>) modelInfo.get(FILES);
			for(Map.Entry<String, Object> entry : files.entrySet()) {
				String fileName = entry.getKey();
				Map<String, String> contents = (Map<String, String>) entry.getValue();
				File file = new File(versionDir.getAbsolutePath() + File.separator + fileName);
				try {
					//System.out.println("Creating file " + file.getAbsolutePath());
					file.createNewFile();
					FileWriter writer = new FileWriter(file); 
					writer.write(contents.get(CONTENTS));
					writer.flush();
					writer.close();
				}
				catch(Exception ce) {
					writeLog("[method=UploadModel] [error=1010] [message=Unable to create file'" + file + "': " + ce + "]");
					throw new RuntimeException("Unable to create file'" + file + "'");
				}
				if(file.exists())
					createdFiles.add(file);
				if(fileName.indexOf("initial-state.nddl") != -1)
					initialState = file;
				if(contents.get(FILE_TYPE).equals("solverconfig"))
					solverConfig = file;
			}
			if(initialState == null)
				throw new RuntimeException("No initial state file uploaded.");
			if(solverConfig == null)
				throw new RuntimeException("No solver configuration file uploaded.");
		}
		//clean up any droppings on an error, or at least make a token effort
		catch(Exception xre) {
			for(File file : createdFiles) 
				file.delete();
			versionDir.delete();
			throw new RuntimeException(xre);
		}
		String nddlIncludePath = EuropaServerManagerConfig.nddlIncludePath(initialState.getAbsolutePath());
		m_config.addConfig(version,
						   new EuropaServerConfig(EuropaServerManagerConfig.DEFAULT_SERVER_PORT, 
												  DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE |
												  DynamicEuropaLogger.TRACE_ALL | DynamicEuropaLogger.RETURN | DynamicEuropaLogger.TIMER,
												  0,
												  initialState.getAbsolutePath(),
												  nddlIncludePath,
												  EuropaServerManagerConfig.LOCALHOST.getCanonicalHostName(),
												  EuropaServerManagerConfig.LOCALHOST.getCanonicalHostName(),
												  version,
												  0.5 * 3600, //from the autoxmlconf.pl script
												  EuropaServerManagerConfig.DEFAULT_SERVER_LOG_FILENAME,
												  solverConfig.getAbsolutePath(),
												  configElement));
		try {
			m_config.save();
		}
		catch(IOException ioe) {
			writeLog("[method=UploadModel] [error=666] [message=Unable to save modified configuration to disk: " + ioe + "]");
		}
		return new HashMap<String, Object>();
	}
	
	/* END XML-RPC methods */
	
	private String generateSessionName() {return String.format("S%16d", System.currentTimeMillis());}
	
	private void writeLog(String value, int type) {
		if((m_config.getLogLevel() & type) != 0) {
			LOGGER.log(loggerLevels.get(type), value);
			//System.out.println(value);
		}
	}
	private void writeLog(String value) {
		writeLog(value, DynamicEuropaLogger.TRACE_ALL);
	}
	
	@SuppressWarnings("unused")
	private int findPort() 
	{
		return findPort(m_config.getPort());
	}
	
	private int findPort(int startAt) 
	{
		Socket temp = null;
		for(int testPort = startAt; testPort < startAt + 1000; ++testPort) {
			try {
				writeLog("[findPort] Testing port " + testPort);
				
				temp = new Socket(EuropaServerManagerConfig.LOCALHOST, testPort);
				if(temp.isBound())
					writeLog("[findPort] Successfully bound port.  Skipping " + testPort);
				else
					writeLog("[findPort] Port " + testPort + " is in use.  Moving on.");
			}
			catch(IOException ioe) {
				if(ioe instanceof ConnectException)
					return testPort;
			}
			//you cannot get ye port
			catch(SecurityException se) {
				writeLog("[findPort] Security exception testing a port.  Returning -1.", DynamicEuropaLogger.ERROR);
				return -1;
			}
			finally {
				try{if(temp != null) temp.close();}
				catch(Exception e) {
					writeLog("[findPort] Failed to close a test-socket.", DynamicEuropaLogger.ERROR);
				}
			}
		}
		writeLog("[findPort] Exhausted 1000 possible ports.  Returning -1.", DynamicEuropaLogger.ERROR);
		return -1;
	}
	
	private boolean typeExists(final String type) {
		return m_config.hasConfig(type);
	}
	
	private static String getConfigFile(String configFile, Bundle bundle) throws IOException {
		String fileName = getConfigFileFromConfig(configFile);
		if(!isFileWritable(fileName)) {
			fileName = copyFileToStateArea(fileName, EuropaServerManagerConfig.DEFAULT_CFG_FILENAME, bundle);
		}
		org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI(fileName);
		if(URIConverter.INSTANCE.exists(uri, null))
			return fileName;
		return "file:" + fileName;
	}
	
	private static String getConfigFileFromConfig(String configFile) throws IOException {
		if(configFile == null || configFile.length() < 1) {
			//prefer local state path file to the one in properties
			if(localStatePath().append(EuropaServerManagerConfig.DEFAULT_CFG_FILENAME).toFile().exists()) {
				return localStatePath().append(EuropaServerManagerConfig.DEFAULT_CFG_FILENAME).toFile().getAbsolutePath();
			}
			else if(EnsembleProperties.getProperty(SERVER_MANAGER_CONFIG) != null) {
				return EnsembleProperties.getProperty(SERVER_MANAGER_CONFIG);
			}
			//fake one up in the plugin state area
			else {
				return createDefaultConfigFile();
			}
		}
		return configFile;
	}
	
	private static String createDefaultConfigFile() throws IOException {
		(new EuropaServerManagerConfig()).save();
		return localStatePath().append(EuropaServerManagerConfig.DEFAULT_CFG_FILENAME).toFile().getAbsolutePath();
	}

	private static boolean isFileWritable(String file) {
		org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI(file);
		if(URIConverter.INSTANCE.exists(uri, null)) {
			Map<String, Set<String>> options = new TreeMap<String, Set<String>>();
			Set<String> attributes = new TreeSet<String>();
			attributes.add(URIConverter.ATTRIBUTE_READ_ONLY);
			options.put(URIConverter.OPTION_REQUESTED_ATTRIBUTES, attributes);
			
			Map<String, ?> readOnly = URIConverter.INSTANCE.getAttributes(uri, options);
			return !((Boolean) readOnly.get(URIConverter.ATTRIBUTE_READ_ONLY));
		}
		else {
			File foo = new File(file);
			return foo.canWrite();
		}
	}
	
	@SuppressWarnings("unused")
	private String replace(String string) {
		if(CommonPlugin.isJunitRunning()) {
			//System.out.print(string + "=>");
			for(Map.Entry<String, String> entry : m_replacements.entrySet()) {
				string = string.replaceAll(entry.getKey(), entry.getValue());
			}
			//System.out.println(string);
		}
		return string;
	}
	
	private String generateLogFileName(String session, String file) {
		File parentDir = localStatePath().toFile();
		String retval = parentDir.getAbsolutePath();
		return retval + File.separator + session + file;
  }

	public static IPath localStatePath() {
		return Platform.getStateLocation(EuropaClientSidePlugin.getDefault().getBundle());
	}

	public static File createDirInStateArea(String name) throws IOException {
		return createDirInStateArea(name, false);
	}
		

	public static File createDirInStateArea(IPath path) throws IOException {
		return createDirInStateArea(path, false);
	}
	
	public static File createDirInStateArea(String name, boolean forceCreate) throws IOException {
		IPath realPath = localStatePath().append(name);
		return doCreateDir(realPath, forceCreate);
	}
	
	
	public static File createDirInStateArea(IPath path, boolean forceCreate) throws IOException {
		IPath realPath = localStatePath().append(path);
		return doCreateDir(realPath, forceCreate);
	}
	
	private static File doCreateDir(IPath realPath, boolean forceCreate) throws IOException {
		File dir = realPath.toFile();		
		//System.out.println("Creating directory " + dir);
		if(!dir.exists() || forceCreate)
			dir.mkdirs();
		if(!dir.exists())
			throw new IOException("Failed to create " + dir);
		return dir;
	}
	
	private static void setupNddlIncludes()
	{
		try {
			if(!localStatePath().append("nddl").toFile().exists()) {
				Bundle bundle = EuropaClientSidePlugin.getDefault().getBundle();
				createDirInStateArea("nddl");
				copyFileToStateArea("data/PlannerConfig.nddl", "nddl" + File.separator + "PlannerConfig.nddl", bundle);
				copyFileToStateArea("data/Plasma.nddl", "nddl" + File.separator + "Plasma.nddl", bundle);
				copyFileToStateArea("data/Resources.nddl", "nddl" + File.separator + "Resources.nddl", bundle);
				copyFileToStateArea("data/StringData.nddl", "nddl" + File.separator + "StringData.nddl", bundle);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to set up Nddl inlude files",e);
		}
	}
	
	public static void clearLocalStateArea() throws IOException {
		StringBuilder errors = new StringBuilder();
		for(String entry : localStatePath().toFile().list()) {
			try {
			File file = localStatePath().append(entry).toFile();
			if(file.isDirectory())
				FileUtils.deleteDirectory(file);
			else
				file.delete();
			}
			catch(IOException ioe) {
				errors.append(ioe.toString()).append("\n");
			}
		}
		if(errors.length() > 0)
			throw new IOException(errors.toString());
	}
	
	public static interface EuropaServerManagerPreferences
	{
		public boolean useXmlRpc();
		public boolean useRemoteServer();
		public String engineType();
	}
	
	protected static EuropaServerManagerPreferences prefs_ = null;
	
	public static EuropaServerManagerPreferences getPreferences() { return prefs_; }
	public static void setPreferences(EuropaServerManagerPreferences p) { prefs_ = p; }
	
	protected static boolean useXmlRpc()
	{
		return (prefs_ != null ? prefs_.useXmlRpc() : false);
	}
	
	protected static boolean useRemoteServer()
	{
		return (prefs_ != null ? prefs_.useRemoteServer() : false);
	}	
	
	protected static String getEngineType()
	{
		return (prefs_ != null ? prefs_.engineType() : "dual");		
	}
}
