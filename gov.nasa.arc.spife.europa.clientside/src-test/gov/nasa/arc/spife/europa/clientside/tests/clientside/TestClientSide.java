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
package gov.nasa.arc.spife.europa.clientside.tests.clientside;

import gov.nasa.arc.spife.europa.clientside.DynamicEuropaLogger;
import gov.nasa.arc.spife.europa.clientside.EuropaClientSidePlugin;
import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.clientside.EuropaServerConfig;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManagerConfig;
import gov.nasa.arc.spife.europa.clientside.EuropaServerProxy;
import gov.nasa.arc.spife.europa.clientside.EuropaServerProxyJNI;
import gov.nasa.arc.spife.europa.clientside.EuropaServerProxyXmlRpc;
import gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.util.EsmConfigResourceFactoryImpl;
import gov.nasa.ensemble.common.io.FileUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.osgi.framework.Bundle;

@SuppressWarnings("unused")
public class TestClientSide extends TestCase {
	private static String testDataDir;//"data/test";
	
	static {
		testDataDir = EuropaServerManager.localStatePath().append("client-side-test").toOSString();//FileUtilities.findFileInPlugin(EuropaClientSidePlugin.getDefault(), "data/test").getAbsolutePath();
	}

	@Override
	public void setUp() throws Exception 
	{
		Bundle bundle = EuropaClientSidePlugin.getDefault().getBundle();
		
		EuropaServerManager.createDirInStateArea("client-side-test");
		EuropaServerManager.copyFileToStateArea("data/PlannerConfig.nddl", "client-side-test" + File.separator + "PlannerConfig.nddl", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/DummyModel.nddl", "client-side-test" + File.separator + "DummyModel.nddl", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/SolverConfig.xml", "client-side-test" + File.separator + "SolverConfig.xml", bundle);
		EuropaServerManager.copyFileToStateArea("data/test/TestEuropaServerManager.xml", "client-side-test" + File.separator + "TestEuropaServerManager.xml", bundle);
	}
	
	@Override
	public void tearDown() throws Exception 
	{
		EuropaServerManager.clearLocalStateArea();
	}
	
	protected EuropaServerConfig makeServerConfig(String name)
	{
		return makeServerConfig(name,8100);
	}
	
	protected EuropaServerConfig makeServerConfig(String name, int port)
	{
		return new EuropaServerConfig(
			port, 
			DynamicEuropaLogger.TRACE | DynamicEuropaLogger.ERROR  | DynamicEuropaLogger.TRACE_ALL, 
			0, 
			testDataDir + "/DummyModel.nddl",
			testDataDir,
			"localhost", 
			"localhost", 
			name, 
			-1, 
			testDataDir + "/test.log", 
			testDataDir + "/SolverConfig.xml", 
			"MER2PassiveSolver"
		);
	}
	
	public void testServerManagerInstantiation() 
	{
		System.out.println("****Server Manager Instantiation");

		EuropaServerManagerConfig cfg = new EuropaServerManagerConfig(
			EuropaServerManagerConfig.DEFAULT_PORT, 
			2, 
			"foo", 
			DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE,
			new HashMap<String, EuropaServerConfig>()
		);
		
		EuropaServerManager serverManager = new EuropaServerManager(cfg);	
	}
	
	public void testServerInstantiation() 
	{
		System.out.println("****Server Instantiation");

		EuropaServerConfig config =null;
		
		
		config = makeServerConfig("Foo!",0);
		EuropaServerProxy jniProxy = new EuropaServerProxyJNI(config);
		jniProxy.startServer("Foo!");
		Object result = jniProxy.syncExecute(EuropaCommand.GET_SERVER_VERSION, new ArrayList<Object>(),true);
		assertEquals(result, "Foo!");
		jniProxy.stopServer();
		
		config = makeServerConfig("Foo!",9100);
		EuropaServerProxy xmlrpcProxy = new EuropaServerProxyXmlRpc(config,false /*use embedded server*/);
		xmlrpcProxy.startServer("Foo!");
		result = xmlrpcProxy.syncExecute(EuropaCommand.GET_SERVER_VERSION, new ArrayList<Object>(),true);
		assertEquals(result, "Foo!");
		xmlrpcProxy.stopServer();
	}	
	
	public void testStartSession() 
	{
		System.out.println("****Start Session");
		
		Map<String, EuropaServerConfig> serverConfigs = new HashMap<String, EuropaServerConfig>();
		serverConfigs.put("foo", makeServerConfig("Foo!"));
		
		EuropaServerManagerConfig cfg = new EuropaServerManagerConfig(
				EuropaServerManagerConfig.DEFAULT_PORT, 
				20, 
				"foo", 
				DynamicEuropaLogger.TRACE | DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE_ALL, 
				serverConfigs
		);

		EuropaServerManager serverManager = new EuropaServerManager(cfg);
		EuropaServerProxy proxy = serverManager.StartSession("argle", "foo");
		
		Object result = proxy.syncExecute(EuropaCommand.GET_SERVER_VERSION, new ArrayList<Object>(),true);
		assertEquals(result, "Foo!");
		
		serverManager.StopSession("argle");
	}	
	
	public void testMultipleSessions() 
	{
		System.out.println("****Multiple Sessions");
		Map<String, EuropaServerConfig> serverConfigs = new HashMap<String, EuropaServerConfig>();
		serverConfigs.put("foo",makeServerConfig("Foo!"));
		serverConfigs.put("bar",makeServerConfig("Bar!"));
		
		EuropaServerManagerConfig cfg = new EuropaServerManagerConfig(
			EuropaServerManagerConfig.DEFAULT_PORT, 
			20, 
			"foo", 
			DynamicEuropaLogger.TRACE | DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE_ALL,
			serverConfigs
		);
		
		EuropaServerManager serverManager = new EuropaServerManager(cfg);
		EuropaServerProxy server1 = serverManager.StartSession("argle", "foo");
		EuropaServerProxy server2 = serverManager.StartSession("bargle", "bar");

		Object result = server1.syncExecute(EuropaCommand.GET_SERVER_VERSION, new ArrayList<Object>(),true);
		assertEquals("Foo!", result);

		result = server2.syncExecute(EuropaCommand.GET_SERVER_VERSION, Arrays.asList(new Object[0]),true);
		assertEquals("Bar!", result);

		serverManager.StopSession("argle");
		serverManager.StopSession("bargle");
	}	
	
	public void testUploadModel() 
	{
		System.out.println("****Upload Model");
		EuropaServerManagerConfig cfg = new EuropaServerManagerConfig();
		EuropaServerManager manager = new EuropaServerManager(cfg);
		
		Hashtable<String, Object> modelInfo = new Hashtable<String, Object>();
		modelInfo.put("ADidentifier", "foo!");
		modelInfo.put("status", "testing");
		modelInfo.put("type", "test");
		
		Hashtable<String, String> initState = new Hashtable<String, String>();
		initState.put("filetype", "nddl");
		initState.put("contents", "//hello, world!");
		
		Hashtable<String, String> solverConfig = new Hashtable<String, String>();
		solverConfig.put("filetype", "solverconfig");
		solverConfig.put("contents","<Solver name=\"foo\"/>");
		
		Hashtable<String, Object> files = new Hashtable<String, Object>();
		files.put("foo-initial-state.nddl", initState);
		files.put("SolverConfig.xml", solverConfig);
		
		modelInfo.put("files", files);
		
		try {
			manager.UploadModel(modelInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		File file = Platform.getStateLocation(EuropaClientSidePlugin.getDefault().getBundle()).append("Uploads").append("test").append("foo_").append("foo-initial-state.nddl").toFile();
		assertTrue(file.exists());
		assertTrue(cfg.hasConfig("foo!"));
	}

	public void testDeleteModel() 
	{
		System.out.println("****Delete Model");
		EuropaServerManagerConfig cfg = new EuropaServerManagerConfig();
		EuropaServerManager manager = new EuropaServerManager(cfg);
		
		Hashtable<String, Object> modelInfo = new Hashtable<String, Object>();
		modelInfo.put("ADidentifier", "foo!");
		modelInfo.put("status", "testing");
		modelInfo.put("type", "test");
				
		Hashtable<String, String> initState = new Hashtable<String, String>();
		initState.put("filetype", "nddl");
		initState.put("contents", "//hello, world!");
		
		Hashtable<String, String> solverConfig = new Hashtable<String, String>();
		solverConfig.put("filetype", "solverconfig");
		solverConfig.put("contents","<Solver name=\"foo\"/>");
		
		Hashtable<String, Object> files = new Hashtable<String, Object>();
		files.put("foo-initial-state.nddl", initState);
		files.put("SolverConfig.xml", solverConfig);
		
		modelInfo.put("files", files);
		
		try {
			manager.UploadModel(modelInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		File file = Platform.getStateLocation(EuropaClientSidePlugin.getDefault().getBundle()).append("Uploads").append("test").append("foo_").append("foo-initial-state.nddl").toFile();
		assertTrue(file.exists());
		assertTrue(cfg.hasConfig("foo!"));
		
		Hashtable<String, Object> deleteInfo = new Hashtable<String, Object>();
		deleteInfo.put("ADidentifier", "foo!");
		deleteInfo.put("type", "test");
		try {
			manager.DeleteModel(deleteInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		file = Platform.getStateLocation(EuropaClientSidePlugin.getDefault().getBundle()).append("Uploads").append("test").append("foo_").append("foo-initial-state.nddl").toFile();
		assertFalse(file.exists());
		assertFalse(cfg.hasConfig("test"));
		file = Platform.getStateLocation(EuropaClientSidePlugin.getDefault().getBundle()).append("Uploads").append("test").append("foo_").toFile();
		assertFalse(file.exists());
	}
	
	@SuppressWarnings("null")
	public void testEMFConfig() 
	{
		System.out.println("****EMF Config");

		// Create a resource set to hold the resources.
		ResourceSet resourceSet = new ResourceSetImpl();

		// Register the appropriate resource factory to handle all file extensions.
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		(Resource.Factory.Registry.DEFAULT_EXTENSION, 
				new EsmConfigResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		resourceSet.getPackageRegistry().put
		(EsmConfigPackage.eNS_URI, 
				EsmConfigPackage.eINSTANCE);
		URI uri = null;
		try {
			uri = URI.createFileURI(
					FileUtilities.findFileInPlugin(
							EuropaClientSidePlugin.getDefault(),
							"data/test/TestEuropaServerManager.xml"
					).getAbsolutePath()
			);
		}
		catch(Exception e) {
			fail(e.toString());
		}
		Resource resource = resourceSet.getResource(uri, true);
		DocumentRoot root = (DocumentRoot) resource.getContents().get(0);
		EuropaServerManagerType esmt = root.getEuropaServerManager();
		assertTrue(esmt != null);
		assertTrue(esmt.getChildTimeout() == 20);
		assertTrue(((String)esmt.getDefaultType()).equals("foo"));
		assertTrue(esmt.getPort() == EuropaServerManagerConfig.DEFAULT_PORT);
		ArrayList logLevels = (ArrayList) esmt.getLogLevel();
		assertTrue(logLevels.size() == 3);

		EList<EuropaServerType> servers = esmt.getEuropaServer();
		boolean seenFoo = false, seenBar = false;

		for(EuropaServerType server : servers) {
			//direct config
			if(server.getConfigPath() == null) {
				if(server.getServerVersion().equals("Foo!")) {
					assertTrue(server.getInitialState().equals("$data/test/DummyModel.nddl"));
					assertTrue("Expected port 8100, got " + server.getPort(), server.getPort() == 8100);
					assertTrue(server.getName().equals("foo"));
					assertTrue(server.getTimeout() == -1);
					assertTrue(server.getServerVersion().equals("Foo!"));
					seenFoo = true;
				}
				else if(server.getServerVersion().equals("Bar!")) {
					assertTrue(server.getInitialState().equals("$data/test/DummyModel.nddl"));
					assertTrue(server.getPort() == 8100);
					assertTrue(server.getTimeout() == -1);
					assertTrue(server.getServerVersion().equals("Bar!"));
					seenBar = true;
				}
				else {
					fail("Unexpected server version: " + server.getServerVersion());
				}
			}
			//have to parse other file
			else {
				fail("The config-path attribute is deprecated.");
			}
		}
		assertTrue("Expected both Foo! and Bar! server types.", seenFoo && seenBar);
	}
	
	public void testESMConversion() 
	{
		System.out.println("****ESM Conversion");

		try {
			int debugLevel = (DynamicEuropaLogger.ERROR | DynamicEuropaLogger.TRACE | DynamicEuropaLogger.TRACE_ALL);
			EuropaServerManagerConfig esmc = 
				EuropaServerManagerConfig.fromEsmConfig("file:" + testDataDir + "/TestEuropaServerManager.xml");
			assertTrue(esmc.getChildTimeout() == 20);
			assertTrue(esmc.getDefaultType().equals("foo"));
			assertTrue(esmc.getPort() == EuropaServerManagerConfig.DEFAULT_PORT);
			assertTrue(esmc.getLogLevel() == debugLevel);
			assertTrue(esmc.getConfigs().size() == 2);
			assertTrue(esmc.getConfig("foo") != null);
			EuropaServerConfig cfg = esmc.getConfig("foo");
			assertTrue(cfg.GetInitialStateFilename().equals("$data/test/DummyModel.nddl"));
			assertTrue(cfg.GetPort() == 8100);
			assertTrue(cfg.GetVersion().equals("Foo!"));
			assertTrue(cfg.GetDebug() == debugLevel);
			cfg = esmc.getConfig("bar");
			assertTrue(cfg.GetInitialStateFilename().equals("$data/test/DummyModel.nddl"));
			assertTrue(cfg.GetPort() == 8100);
			assertTrue(cfg.GetVersion().equals("Bar!"));
			assertTrue(cfg.GetDebug() == debugLevel);			
		}
		catch(Exception e) {
			fail(e.toString());
		}
	}
}
