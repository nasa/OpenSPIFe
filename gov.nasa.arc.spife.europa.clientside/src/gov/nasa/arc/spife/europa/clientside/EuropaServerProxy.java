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

public interface EuropaServerProxy 
{
	public abstract void startServer(String sessionId);
	
	public abstract void stopServer();
	
	/**
	 * If not already connected, attempts to connect to 
	 * the europa server.  Otherwise, does nothing.
	 * @return false if we were already connected 
	 */
	public abstract void startSession(String sessionId, String modelName);

	/**
	 * If connected, shutdown the connection.
	 * If not connected, does nothing.
	 */
	public abstract void stopSession();

	/**
	 * Execute the given command immediately and return the result.
	 * @param command
	 * @param parameters
	 * @param reportErrors makes exceptions report as errors and mark the server icon
	 * @return the result
	 */
	public abstract Object syncExecute(
			EuropaCommand command,
			List<? extends Object> parameters, boolean reportErrors);

	/**
	 * Queue the given command for execution later.
	 * Current thread must hold the lock for the client.
	 * 
	 * This should be used like this:
	 * EuropaSessionClient client;
	 * synchronized (client) {
	 *    client.queueExecute(.....);
	 *    client.queueExecute(.....);
	 *    client.flushQueue();
	 * }
	 * 
	 * @param command
	 * @param parameters
	 * @see flushQueue
	 */
	public abstract void queueExecute(
			EuropaCommand command,
			List<? extends Object> parameters);

	/**
	 * Call this to send all queued calls and get back a list of results for them.
	 * Current thread must hold the lock for the client.
	 * @param reportErrors
	 * @return
	 * @see queueExecute
	 */
	public abstract List<?> flushQueue();
}
