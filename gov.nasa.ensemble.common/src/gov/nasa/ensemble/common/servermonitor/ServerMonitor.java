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
package gov.nasa.ensemble.common.servermonitor;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class ServerMonitor implements IServerMonitor {

	private final List<IServerMonitorListener> listeners = new ArrayList<IServerMonitorListener>();
	private long lastRequestTimeMillis;
	private long lastResponseTimeMillis;
	private Throwable exception;

	@Override
	public Throwable getException() {
		return exception;
	}
	
	@Override
	public boolean isUp() {
		return getException() == null && lastResponseTimeMillis != 0;
	}
	
	@Override
	public String getLongName() {
		return getShortName();
	}
	
	@Override
	public String getInfo() {
		return "";
	}
	
	@Override
	public long lastRequestTimeMillis() {
		return lastRequestTimeMillis;
	}
	
	@Override
	public long lastResponseTimeMillis() {
		return lastResponseTimeMillis;
	}
	
	@Override
	public long timeSinceLastResponse() {
		return System.currentTimeMillis() - lastResponseTimeMillis;
	}

	@Override
	public void addListener(IServerMonitorListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IServerMonitorListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	public void updateLastRequestTimeMillis() {
		lastRequestTimeMillis = System.currentTimeMillis();
		List<IServerMonitorListener> listeners;
		synchronized (this.listeners) {
			listeners = new ArrayList<IServerMonitorListener>(this.listeners);
		}
		for (IServerMonitorListener listener : listeners) {
			try {
				listener.noteResponseReceived(lastResponseTimeMillis);
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				LogUtil.error(t);
			}
		}
	}

	public void updateLastResponseTimeMillis() {
		lastResponseTimeMillis = System.currentTimeMillis();
		setException(null);
		List<IServerMonitorListener> listeners;
		synchronized (this.listeners) {
			listeners = new ArrayList<IServerMonitorListener>(this.listeners);
		}
		for (IServerMonitorListener listener : listeners) {
			try {
				listener.noteResponseReceived(lastResponseTimeMillis);
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				LogUtil.error(t);
			}
		}
	}
	
	public void setException(Throwable exception) {
		final Throwable oldException = this.exception;
		this.exception = exception;
		List<IServerMonitorListener> listeners;
		synchronized (this.listeners) {
			listeners = new ArrayList<IServerMonitorListener>(this.listeners);
		}
		for (IServerMonitorListener listener : listeners) {
			try {
				listener.handleException(exception);
				if (listener instanceof IServerMonitorListenerExt && exception == null && oldException != null) {
					((IServerMonitorListenerExt)listener).handleServerRecovered();
				}
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				LogUtil.error(t);
			}
		}
	}
}
