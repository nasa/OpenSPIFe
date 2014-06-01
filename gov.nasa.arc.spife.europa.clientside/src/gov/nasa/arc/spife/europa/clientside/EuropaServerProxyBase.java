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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public abstract class EuropaServerProxyBase 
	implements EuropaServerProxy 
{
	private static final Logger logger = Logger.getLogger(EuropaServerProxyBase.class);
	
	protected ServerLauncher serverLauncher_=null;
	protected EuropaServerConfig serverConfig_=null;
    protected String sessionId_=null;
    
    public EuropaServerProxyBase(EuropaServerConfig config, boolean useRemoteServer)
    {
    	serverConfig_=config;
    	serverLauncher_ = (
    			useRemoteServer 
    			? new RemoteServerLauncher(config)
    			: new EmbeddedServerLauncher(config)
    	);
    }

	@Override
	public void startServer(String sessionId)
	{
		serverLauncher_.startServer(sessionId);
		
		int timeout=5; // in seconds
		if (!serverResponds(serverConfig_, timeout)) 
			throw new RuntimeException("Failed starting DynamicEuropa server");		
	}
	
	@Override
	public void stopServer()
	{
		try {
			executeCommand(EuropaCommand.STOP_SERVER,new Vector<Object>());
			serverLauncher_.waitForServerShutdown();
		}
		catch (Throwable t) {
			logger.warn("Exception caught while stopping DynamicEuropa server");
		}
	}

	@Override
	public void startSession(
			String sessionId, 
			String modelName) 
	{
		try {
			sessionId_ = sessionId;
			StringBuffer info = new StringBuffer();
			info.append("Session id: ").append(sessionId_).append("\n"); 
			
			// Start a new session with DynamicEuropa		
			List<Object> params = new ArrayList<Object>();
			params.add(modelName);
			executeCommand(EuropaCommand.START_SESSION, params);
			
			// Print server version if available
			params.clear();
			Object version = executeCommand(EuropaCommand.GET_SERVER_VERSION,params);
			if (version != null) 
				info.append("Server Version: ").append(version).append("\n");

			logger.info(info);
			EuropaServerMonitor.setInfo(info.toString());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stopSession() 
	{
		try {
			logger.info("Europa session [" + sessionId_ + "] stopping session...");
			executeCommand(EuropaCommand.STOP_SESSION, new ArrayList<Object>());
			logger.info("Europa session [" + sessionId_ + "] has been shutdown");
		} catch (Exception e) {
			logger.warn("disconnect failed: ", e);
		}
	}

	@Override
	public Object syncExecute(
			EuropaCommand command,
			List<? extends Object> parameters, 
			boolean reportErrors) 
	{		
		try {
			autoFlushQueue();
			Object result = executeCommand(command, parameters);
			return result;
		} 
		catch (Exception e) {
			if (reportErrors) {
				EuropaServerMonitor.getInstance().setException(e);
				logger.error(command + " failed", e);
				return e;
			}
			logger.warn(command + " failed", e);
			return null;
		}
	}
	
	@Override
	public void queueExecute(
			EuropaCommand command, 
			List<? extends Object> parameters) 
	{
		queueCommand(command, getCommandParameters(command, parameters));
		logger.debug(command + " queued.");
	}
	

	@Override
	public List<?> flushQueue() 
	{
		EuropaServerMonitor.getInstance().updateLastRequestTimeMillis();
		List<?> results = flushCommandQueue();
		EuropaServerMonitor.getInstance().updateLastResponseTimeMillis();
		logger.debug("flushQueue results: " + formatExecuteResult(results));
		return results;
	}
	
	protected Object executeCommand(EuropaCommand command,List<? extends Object> parameters)
	{
		try {
			EuropaServerMonitor.getInstance().updateLastRequestTimeMillis();
			Object result = executeCommand(command,getCommandParameters(command,parameters));
			EuropaServerMonitor.getInstance().updateLastResponseTimeMillis();
			logger.debug(command + " result: " + formatExecuteResult(result));
			return result;
		}
		catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}
	
	protected abstract Object executeCommand(EuropaCommand command, Vector<Object> parameters); 
	protected abstract void queueCommand(EuropaCommand command, Vector<Object> parameters);
	protected abstract List<?> flushCommandQueue();
	
	protected void autoFlushQueue()
	{
		List<?> queuedResults = flushQueue();
		if (!queuedResults.isEmpty()) {
			logger.warn("queue auto-flushed, result count = " + queuedResults.size());
			for (Object result : queuedResults) {
				if (result instanceof Exception) {
					Exception exception = (Exception) result;
					logger.warn("exception during auto-flush", exception);
				}
			}
		}		
	}

	protected Vector<Object> getCommandParameters(EuropaCommand command, List<? extends Object> parameters) {
		for (Object object : parameters) {
			if (object == null) {
				logger.warn("Found null parameter while invoking EuropaCommand "+command.getXmlrpcString());
				return new Vector<Object>();
			}
		}
		
		Vector<Object> commandParameters = new Vector<Object>();
		if (needsSession(command))
			commandParameters.add(sessionId_);
		commandParameters.addAll(parameters);
		
		return commandParameters;
	}
	
	protected String formatExecuteResult(Object result) {
		String output = (result == null ? "<null>" : result.toString());
		if (output.length() > 40) {
			output = "\n    " + result;
		} else if (output.length() == 0) {
			output = "<empty>";
		}
		return output;
	}	
	
	protected abstract boolean serverResponds(EuropaServerConfig config, int timeout); 
	
	protected static boolean needsSession(EuropaCommand command)
	{
		return !s_sessionlessMethods.contains(command);
	}
	
	static Set<EuropaCommand> s_sessionlessMethods = new HashSet<EuropaCommand>();
	static {
		s_sessionlessMethods.add(EuropaCommand.GET_SERVER_VERSION);
		s_sessionlessMethods.add(EuropaCommand.STOP_SERVER);
		s_sessionlessMethods.add(EuropaCommand.GET_RESOURCE_IN_TIME_FORMAT);		
	}
	
	protected static interface ServerLauncher
	{
		public void startServer(String sessionId);
		public void waitForServerShutdown();
		public EuropaServer getServer();
	}
	
	protected static class EmbeddedServerLauncher
		implements ServerLauncher
	{
		protected EuropaServer embeddedServer_=null;
		protected EuropaServerConfig serverConfig_=null;
		protected Future<Object> embeddedServerThread_ = null;

		public EmbeddedServerLauncher(EuropaServerConfig config)
		{
			serverConfig_=config;
			embeddedServer_=new EuropaServer(config);
		}
		
		@Override
		public void startServer(String sessionId) 
		{
			ExecutorService executor = new ThreadPoolExecutor(
					2, 
					Integer.MAX_VALUE, 
					Long.MAX_VALUE, 
					TimeUnit.MILLISECONDS, 
					new SynchronousQueue<Runnable>()
			);
			
			try {
				embeddedServerThread_ = executor.submit(
					new Callable<Object>() 
					{ 
						@Override
						public Object call() { return embeddedServer_.Start(); }
					}
				);
				
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void waitForServerShutdown() 
		{
			if (embeddedServerThread_ == null)
				return;
			
			int timeout=10; // seconds
			try {
				embeddedServer_.SetTimeout(0);
				embeddedServerThread_.get(timeout,TimeUnit.SECONDS);
				logger.info("Stopped Embedded DynamicEuropa Server running on port:"+serverConfig_.GetPort());
			}
			catch (TimeoutException e) {
				logger.warn("DynamicEuropa server failed to stop after "+timeout+" seconds",e);
			}
			catch (InterruptedException e) {
				logger.warn("Exception caught while shutting down DynamicEuropa server",e);
			}
			catch (ExecutionException e) {
				logger.warn("Exception caught while shutting down DynamicEuropa server",e);
			}
			
			embeddedServer_ = null;
			embeddedServerThread_ = null;			
		}

		@Override
		public EuropaServer getServer() 
		{
			return embeddedServer_;
		}		
	}
	
	protected static class RemoteServerLauncher
		implements ServerLauncher
	{
		protected EuropaServerConfig serverConfig_=null;
		protected Process remoteServerProcess_=null;

		public RemoteServerLauncher(EuropaServerConfig config)
		{
			serverConfig_=config;
		}
		
		@Override
		public void startServer(String sessionId) 
		{
			String exePath = getExePath();
			String configPath = makeConfigFile(sessionId);
			ProcessBuilder pb = new ProcessBuilder(exePath, configPath);
			pb.directory(EuropaServerManager.localStatePath().toFile());
			pb.redirectErrorStream(true); // merge stdout and stdin
			try {
				remoteServerProcess_ = pb.start();
				new Thread(
					new StreamDrain(remoteServerProcess_.getInputStream())
				).start();
			}
			catch (Exception e) {
				throw new RuntimeException("Failed starting remote server:",e);
			}
		}
			
		protected String getExePath()
		{
			try {
				String bundleName = "gov.nasa.arc.spife.europa.clientside";
				String exeName = "DynamicEuropa_o"; // TODO: deal with debug version
				if (Platform.getOS().equals(Platform.OS_WIN32)) {
					exeName += ".exe";
				}
				Bundle b = Platform.getBundle(bundleName);
				URL url = FileLocator.find(b, new Path(exeName), null /*override*/);
				url = FileLocator.toFileURL(url);
				return url.getFile();
			}
			catch (Exception e) {
				throw new RuntimeException("Failed trying to get path to Europa executable",e);
			}
		}
		
		protected String makeConfigFile(String sessionId)
		{
			String outputFileName = sessionId+".cfg";
			File outputFile = EuropaServerManager.localStatePath().append(outputFileName).toFile();
			try {
				outputFile.createNewFile();
				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
				writer.println(toString(serverConfig_));
				writer.close();
			}
			catch (IOException e) {
				throw new RuntimeException("Failed creating configuration file for new Europa server",e);
			}
			
			return outputFile.getAbsolutePath();
		}

		protected String toString(EuropaServerConfig serverConfig) 
		{
			StringBuffer buf = new StringBuffer();
			
			/*
			buf
				.append("port").append(" ").append(serverConfig.GetPort()).append("\n")
				.append("debug").append(" ").append(serverConfig.GetDebug()).append("\n")
				.append("verbosity").append(" ").append(serverConfig.GetVerbosity()).append("\n")
				.append("initial_state").append(" ").append(serverConfig.GetInitialStateFilename()).append("\n")
				.append("model_paths").append(" ").append(serverConfig.GetModelPaths()).append("\n")
				.append("server_host").append(" ").append(serverConfig.GetHost()).append("\n")
				.append("server_host_local").append(" ").append(serverConfig.GetHostLocal()).append("\n")
				.append("server_version").append(" ").append(serverConfig.GetVersion()).append("\n")
				.append("planner_config").append(" ").append(serverConfig.GetPlannerConfigFilename()).append("\n")
				.append("planner_elem").append(" ").append(serverConfig.GetPlannerConfigElement()).append("\n")
				.append("server_timeout").append(" ").append(serverConfig.GetServerTimeout()).append("\n")
				.append("log").append(" ").append(serverConfig.GetLogFile()).append("\n")
			;
			*/
			buf.append(serverConfig.toString());
			
			return buf.toString();
		}

		@Override
		public void waitForServerShutdown() 
		{
			if (remoteServerProcess_ == null)
				return;
			
			boolean serverExited=false;
			int timeout=10; //seconds
			long startTime=System.currentTimeMillis();
			while (!serverExited && (elapsedTime(startTime) < (timeout*1000))) {
				try {
					remoteServerProcess_.exitValue();
					serverExited=true;
					logger.info("Stopped Remote DynamicEuropa Server running on port:"+serverConfig_.GetPort());
				}
				catch (IllegalThreadStateException e) {
					Thread.yield();
				}
				catch (Exception e) {
					logger.warn("Exception caught while shutting down DynamicEuropa server",e);
					break;
				}
			}
			
			if (!serverExited) {
				logger.warn("DynamicEuropa server failed to stop after "+timeout+" seconds");
				remoteServerProcess_.destroy();
			}
			
			remoteServerProcess_ = null;
		}

		protected long elapsedTime(long startInMillis)
		{
			return (System.currentTimeMillis()-startInMillis);
		}
		
		@Override
		public EuropaServer getServer() 
		{
			throw new RuntimeException("Can't get a handle on a remote server");
		}
	}
	
	protected static class StreamDrain
		implements Runnable
	{
		protected InputStream stream_;

        private StreamDrain (InputStream s) 
        {
            stream_ = s;
        }

        @Override
        public void run() 
        {
            try {
            	BufferedReader br = new BufferedReader(new InputStreamReader(stream_));
                while ((br.readLine()) != null) {
                	// Do nothing
                	// we just want to drain the stream to prevent the child process from blocking
                }
                logger.debug("InputStream drainer finished");
            } catch (Exception e) {
            	logger.error("Died while draining stderr & stdout for DynamicEuropa server process");
            }
        }		
	}
}
