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

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;

import redstone.xmlrpc.XmlRpcClient;
import gov.nasa.arc.spife.europa.clientside.xmlrpc.Client;


public class EuropaServerProxyXmlRpc 
	extends EuropaServerProxyBase 
{
	private static final Logger LOG = Logger.getLogger(EuropaServerProxyXmlRpc.class);
	
    protected Client client_;
	
	public EuropaServerProxyXmlRpc(EuropaServerConfig config, boolean useRemoteServer)
	{
		super(config,useRemoteServer);
		String host = EuropaServerManagerConfig.LOCALHOST.getCanonicalHostName(); // TODO: get this from config
		int port = serverConfig_.GetPort();
		
		try {
			client_ = new Client(host,port);
		}
		catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Object executeCommand(EuropaCommand command, Vector<Object> parameters)
	{
		try {
			return client_.execute(command.getXmlrpcString(),parameters);
		}
		catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void queueCommand(EuropaCommand command, Vector<Object> parameters)
	{
		try {
			client_.addCall(command.getXmlrpcString(), parameters);
		}
		catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}		
	}

	@Override
	protected List<?> flushCommandQueue()
	{
		try {
			return client_.executeAll();
		}
		catch (Exception e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}				
	}
	
	@Override
	public void stopSession() 
	{
		super.stopSession();
	}
	
	@Override
	protected boolean serverResponds(EuropaServerConfig config, int timeout) 
	{
		String host = config.GetHost();
		int port = config.GetPort(); 
		XmlRpcClient client;
		
		try {
			client = XmlRpcUtil.createXmlRpcClient(host, port);
		}
		catch (Exception e) {
			LOG.error("Failed to create XmlRpc client",e);
			return false;
		}
		
		long time = System.currentTimeMillis();
		while((System.currentTimeMillis() - time) < (timeout * 1000)) {
			try {
				Object foo = client.invoke("system.listMethods", new Object[0]);
				if(foo != null) 
					return true;
				
				Thread.yield();
			}
			catch(Exception e) {
				if ((e.getCause() != null) && (e.getCause() instanceof java.net.ConnectException))
					continue;
				else
					return false;
			}
		}
		
		return false;
	}	
}
