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

import gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigFactory;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item;
import gov.nasa.arc.spife.europa.clientside.esmconfig.util.EsmConfigResourceFactoryImpl;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


/**
 * @author miatauro
 *
 */
/**
 * @author miatauro
 *
 */
public class EuropaServerManagerConfig {
	public static final int DEFAULT_PORT = 8050;
	public static final int DEFAULT_SERVER_PORT = 8100;
	public static final InetAddress LOCALHOST; 

	public static final String DEFAULT_CFG_FILENAME = "serverManagerConfig.xml";
	public static final String DEFAULT_SERVER_LOG_FILENAME = "EuropaServer.log";
	private static String PATH_SEPARATOR = System.getProperty("path.separator");

	private static final ResourceSet s_resourceSet;
	
	private int m_port; /*!< The port for the server manager*/
	private int m_childForkTimeout; /*<! Time to wait before giving up on connecting to a starting EuropaServer */
	private String m_defaultType; /*<! The default type of EuropaServer to create. */
	private int m_logLevel; /*<! Logging level for the server manager. */
	private Map<String, EuropaServerConfig> m_typesToConfigs; /*<! Map from server types to configuration objects*/
	private Resource m_resource = null;
	@SuppressWarnings("unused")
	private static File s_data = null;
	
	static {
		s_resourceSet = new ResourceSetImpl();
		Map<String, Object> map = s_resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
		map.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new EsmConfigResourceFactoryImpl());
		s_resourceSet.getPackageRegistry().put(EsmConfigPackage.eNS_URI, EsmConfigPackage.eINSTANCE);
		InetAddress foo = null;
		try {
			foo = InetAddress.getByName("localhost");
		} catch (Exception e) {
			LogUtil.error("Failed to construct InetAddress for localhost.", e);
		}
		LOCALHOST = foo;
		try {
			URL metadataURL = FileUtilities.copyToMetadata(EuropaClientSidePlugin.getDefault().getBundle(), "data", "*", false);
			java.net.URI dataPath = java.net.URI.create(metadataURL.toString() + "/data");
			s_data = new File(dataPath);
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	/**
	 * Server manager configuration for dummy default values.
	 */
	public EuropaServerManagerConfig() {
		this(DEFAULT_PORT, 20, "foo", 
			DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE | DynamicEuropaLogger.TRACE_ALL,
			new TreeMap<String, EuropaServerConfig>());
	}
	
	/**
	 * Server manager configuration constructor for pre-determined values.
	 * 
	 * @param mPort The port for the server manager
	 * @param mChildForkTimeout Time to wait before giving up on connecting to a starting EuropaServer
	 * @param mDefaultType The default type of EuropaServer to create.
	 * @param mLogLevel Logging level for the server manager.
	 * @param mTypesToConfigs Map from server types to configuration objects
	 */
	public EuropaServerManagerConfig(int mPort,
      int mChildForkTimeout, String mDefaultType, int mLogLevel,
      Map<String, EuropaServerConfig> mTypesToConfigs) {
	  m_port = mPort;
	  m_childForkTimeout = mChildForkTimeout;
	  m_defaultType = mDefaultType;
	  m_logLevel = mLogLevel;
	  m_typesToConfigs = mTypesToConfigs;
	  URI uri = URI.createFileURI(EuropaServerManager.localStatePath().append(EuropaServerManagerConfig.DEFAULT_CFG_FILENAME).toOSString());
	  setFile(s_resourceSet.createResource(uri));
  }
	
	void addConfig(String type, EuropaServerConfig cfg) {
		m_typesToConfigs.put(type, cfg);
		EList<EuropaServerType> serverConfigs = getRoot().getEuropaServerManager().getEuropaServer();
		serverConfigs.add(toEsmConfig(cfg, type));
	}

	private static EuropaServerManagerType toEsmConfig(EuropaServerManagerConfig cfg) {
		EuropaServerManagerType retval = EsmConfigFactory.eINSTANCE.createEuropaServerManagerType();
		retval.setChildTimeout(cfg.getChildTimeout());
		retval.setDefaultType(cfg.getDefaultType());
		retval.setLogLevel(toDebugEnum(cfg.getLogLevel()));
		retval.setPort(cfg.getPort());
		EList<EuropaServerType> configs = retval.getEuropaServer();
		for(Map.Entry<String, EuropaServerConfig> entry : cfg.getConfigs().entrySet()) {
			configs.add(toEsmConfig(entry.getValue(), entry.getKey()));
		}
		return retval;
	}
	
	private static EuropaServerType toEsmConfig(EuropaServerConfig cfg, String name) {
		EuropaServerType retval = EsmConfigFactory.eINSTANCE.createEuropaServerType();
		retval.setDebug(toDebugEnum(cfg.GetDebug()));
		retval.setInitialState(cfg.GetInitialStateFilename());
		retval.setLogFile(cfg.GetLogFile());
		retval.setName(name);
		retval.setPlannerConfig(cfg.GetPlannerConfigFilename());
		retval.setPlannerConfigElement(cfg.GetPlannerConfigElement());
		retval.setPort(cfg.GetPort());
		retval.setServerVersion(cfg.GetVersion());
		retval.setTimeout((int) cfg.GetServerTimeout());
		retval.setVerbosity(cfg.GetVerbosity());
		return retval;
	}

	void removeConfig(String type) {
		m_typesToConfigs.remove(type);
		EList<EuropaServerType> serverConfigs = getRoot().getEuropaServerManager().getEuropaServer();
		for(EuropaServerType server : serverConfigs) {
			if(server.getName().equals(type)) {
				serverConfigs.remove(server);
				break;
			}
		}
	}
	
	private DocumentRoot getRoot() {
		return (DocumentRoot) m_resource.getContents().get(0);
	}
	
	private void setFile(Resource resource) 
	{
		m_resource = resource;
		// TODO JRB: there may be a better place to fix this problem
		// Make sure that we always have a root
		if (m_resource.getContents().size() == 0) {
			  DocumentRoot root = EsmConfigFactory.eINSTANCE.createDocumentRoot();
			  root.setEuropaServerManager(toEsmConfig(this));
			  m_resource.getContents().add(root);					
		}
	}
	
	/**
	 * @return The port number for the server manager.
	 */
	public int getPort() {return m_port;}
	
	/**
	 * @return The time to wait before giving up on connecting to a starting EuropaServer
	 */
	public int getChildTimeout() {return m_childForkTimeout;}
	/**
	 * @return The default type of EuropaServer to create.
	 */
	public String getDefaultType() {return m_defaultType;}
	/**
	 * @return The level of logging to perform in the server manager.
	 */
	public int getLogLevel() {return m_logLevel;}
	
	public Map<String, EuropaServerConfig> getConfigs() {
		return Collections.unmodifiableMap(m_typesToConfigs);
	}
	
	/**
	 * @param type The type of EuropaServer
	 * @return The configuration object for the type if it exists, null otherwise.
	 */
	public EuropaServerConfig getConfig(String type) {
			return m_typesToConfigs.get(type);
	}
	
	/**
	 * @param type The type of EuropaServer
	 * @return true if a configuration object exists for the type of EuropaServer, false otherwise.
	 */
	public boolean hasConfig(String type) {
		return m_typesToConfigs.containsKey(type);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(">Europa manager config: [port=").append(m_port).append(" timeout=").append(m_childForkTimeout);
		builder.append(" default=").append(m_defaultType).append(" logging=").append(m_logLevel).append("]\n");
		for(Map.Entry<String, EuropaServerConfig> entry : m_typesToConfigs.entrySet()) {
			EuropaServerConfig cfg = entry.getValue();
			builder.append(">>").append(entry.getKey()).append("[port=").append(cfg.GetPort()).append(" version=").append(cfg.GetVersion());
			builder.append(" init=").append(cfg.GetInitialStateFilename()).append(" plannerCfg=").append(cfg.GetPlannerConfigFilename());
			builder.append(" planner=").append(cfg.GetPlannerConfigElement()).append(" logging=").append(cfg.GetDebug()).append("]\n");
		}
		return builder.toString();
	}
	
	public static EuropaServerManagerConfig fromEsmConfig(String uriString) {
		return fromEsmConfig(uriString, null);
	}
	
	public static EuropaServerManagerConfig fromEsmConfig(String uriString, Map<String, String> configReplacements) {
		URI uri = URI.createURI(uriString);
		EuropaServerManagerConfig cfg = null;
		Resource resource = null;
		if(URIConverter.INSTANCE.exists(uri, null)) {
			resource = s_resourceSet.getResource(uri, true); //exception here: org.xml.sax.XAXParseException: Premature end of file
			DocumentRoot root = (DocumentRoot) resource.getContents().get(0);
			cfg = fromEsmConfig(root.getEuropaServerManager(), configReplacements);
		} else {
			resource = s_resourceSet.createResource(uri);
			cfg = new EuropaServerManagerConfig();
		}

		cfg.setFile(resource);
		
		return cfg;
	}
	
	//some way to gather multiple exceptions and throw them together?

	public static EuropaServerManagerConfig fromEsmConfig(EuropaServerManagerType esm) {
		return fromEsmConfig(esm, null);
	}
	public static EuropaServerManagerConfig fromEsmConfig(EuropaServerManagerType esm, Map<String, String> configReplacements) {
		Map<String, EuropaServerConfig> serverConfigs = new TreeMap<String, EuropaServerConfig>();
		for(EuropaServerType serverType : esm.getEuropaServer()) {
			serverConfigs.put((String)serverType.getName(), fromEuropaServerConfig(serverType, configReplacements));
		}
		return new EuropaServerManagerConfig(esm.getPort(), esm.getChildTimeout(), (String) esm.getDefaultType(),
																				 fromDebugEnum(esm.getLogLevel()), serverConfigs);
	}
	
	
	private static String replace(String string, Map<String, String> replacements) {
		if(CommonPlugin.isJunitRunning() && replacements != null) {
			//System.out.print(string + "=>");
			for(Map.Entry<String, String> entry : replacements.entrySet()) {
				string = string.replaceAll(entry.getKey(), entry.getValue());
			}
			//System.out.println(string);
		}
		return string;
	}
	
	public static String uri2file(final String uri) {
		return URI.createURI(uri).toFileString();
	}
	
	public static EuropaServerConfig fromEuropaServerConfig(EuropaServerType est) {
		return fromEuropaServerConfig(est, null);
	}
	
	public static EuropaServerConfig fromEuropaServerConfig(EuropaServerType est, Map<String, String> configReplacements) {
		if(est.getConfigPath() == null) {
			return new EuropaServerConfig((est.isSetPort() ? est.getPort() : DEFAULT_SERVER_PORT),
					(est.getDebug() instanceof Integer ? (Integer) est.getDebug() : fromDebugEnum(est.getDebug())),
					(est.isSetVerbosity() ? est.getVerbosity() : 0), 
					replace((String) est.getInitialState(), configReplacements),
					(new File(replace((String) est.getInitialState(), configReplacements))).getParent(),
					LOCALHOST.getCanonicalHostName(), LOCALHOST.getCanonicalHostName(),	(String) est.getServerVersion(),
					(est.isSetTimeout() ? est.getTimeout() : -1.0),
					(String) est.getLogFile(), replace((String) est.getPlannerConfig(), configReplacements),
					(String) est.getPlannerConfigElement());
		}
		else {
			//TODO: modify rnc, schema, and model to completely disallow this
			throw new NoSuchFieldError("The config-path attribute is deprecated");
		}
	}
	
	private static final Map<Integer, Integer> LOG_TYPE_MAP;
	private static final Map<Integer, Integer> RLOG_TYPE_MAP;
	static {
		LOG_TYPE_MAP = new TreeMap<Integer, Integer>();
		LOG_TYPE_MAP.put(LogTypeMember1Item.ERROR_VALUE, DynamicEuropaLogger.ERROR);
		LOG_TYPE_MAP.put(LogTypeMember1Item.RETURN_VALUE , DynamicEuropaLogger.RETURN);
		LOG_TYPE_MAP.put(LogTypeMember1Item.TIMER_VALUE, DynamicEuropaLogger.TIMER);
		LOG_TYPE_MAP.put(LogTypeMember1Item.TRACE_VALUE, DynamicEuropaLogger.TRACE);
		LOG_TYPE_MAP.put(LogTypeMember1Item.TRACEALL_VALUE, DynamicEuropaLogger.TRACE_ALL);
		RLOG_TYPE_MAP = new TreeMap<Integer, Integer>();
		RLOG_TYPE_MAP.put(DynamicEuropaLogger.ERROR, LogTypeMember1Item.ERROR_VALUE);
		RLOG_TYPE_MAP.put(DynamicEuropaLogger.RETURN, LogTypeMember1Item.RETURN_VALUE);
		RLOG_TYPE_MAP.put(DynamicEuropaLogger.TIMER, LogTypeMember1Item.TIMER_VALUE);
		RLOG_TYPE_MAP.put(DynamicEuropaLogger.TRACE, LogTypeMember1Item.TRACE_VALUE);
		RLOG_TYPE_MAP.put(DynamicEuropaLogger.TRACE_ALL, LogTypeMember1Item.TRACEALL_VALUE);
	}
	
	protected static int fromDebugEnum(Object foo){
		if(foo instanceof Integer)
			return (Integer) foo;
		List<LogTypeMember1Item> values = (List<LogTypeMember1Item>) foo;
		int retval = 0;
		for(LogTypeMember1Item value : values)
			retval |= LOG_TYPE_MAP.get(value.getValue());
		return retval;
	}
	
	protected static List<LogTypeMember1Item> toDebugEnum(int getDebug) {
		List<LogTypeMember1Item> values = new ArrayList<LogTypeMember1Item>();
		for(Map.Entry<Integer, Integer> rmap : RLOG_TYPE_MAP.entrySet()) {
			if((getDebug & rmap.getKey()) != 0)
				values.add(LogTypeMember1Item.get(rmap.getValue()));
		}
		return values;
	}
	
	private static final Map<Integer, String> LOG_STRING_MAP;
	static {
		LOG_STRING_MAP = new TreeMap<Integer, String>();
		LOG_STRING_MAP.put(DynamicEuropaLogger.ERROR, "ERROR");
		LOG_STRING_MAP.put(DynamicEuropaLogger.RETURN, "RETURN");
		LOG_STRING_MAP.put(DynamicEuropaLogger.TIMER, "TIMER");
		LOG_STRING_MAP.put(DynamicEuropaLogger.TRACE, "TRACE");
		LOG_STRING_MAP.put(DynamicEuropaLogger.TRACE_ALL, "TRACE_ALL");
		
	}
	
	/**
	 * @brief Generates an include path for the NDDL parser
	 * @return An include path containing '.', a "nddl" directory in the plugin state directory, and anything in europa.nddlIncludePath in the ensemble.properites file.
	 */
	public static String nddlIncludePath() {
		String localIncludePath = EuropaServerManager.localStatePath().append("nddl").toOSString();
		String includePath = "." + PATH_SEPARATOR +  localIncludePath;
		if (EnsembleProperties.getProperty("europa.nddlIncludePath") != null)
			includePath += PATH_SEPARATOR + EnsembleProperties.getProperty("europa.nddlIncludePath");
		return includePath;
	}
	
	
	/**
	 * @brief Generates an include path for the NDDL parser
	 * In addition to the default include path given by the 0-ary nddlIncludePath, this function includes a path based on the file argument--if it is a directory, it is
	 * included directly, if it is a file, the parent directory is added.
	 * 
	 * @param file A file in the directory to include in the path or the directory to include.
	 * @return The include path from nddlIncludePath plus the directory referred to by file
	 * @see nddlIncludePath
	 */
	public static String nddlIncludePath(String file) {
		File f = new File(file);
		if(!f.isDirectory())
			f = f.getParentFile();
		return nddlIncludePath() + PATH_SEPARATOR + f.getAbsolutePath();
	}
	
	public void save() throws IOException {
		if(m_resource == null) {
			URI uri = URI.createFileURI(EuropaServerManager.localStatePath().append(EuropaServerManagerConfig.DEFAULT_CFG_FILENAME).toOSString());
			m_resource = s_resourceSet.createResource(uri);
			return;
		}
		m_resource.save(null);
	}
}
