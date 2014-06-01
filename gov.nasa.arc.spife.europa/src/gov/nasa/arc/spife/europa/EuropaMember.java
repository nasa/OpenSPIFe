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

import java.util.ArrayList;
import java.util.List;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.IMember;

@SuppressWarnings("serial")
public class EuropaMember implements IMember {

	private final EuropaSessionClient client;
	private Europa europa;
	private EuropaMemberInitializationThread thread;
	protected List<EuropaMemberListener> listeners;

	public static EuropaMember get(EPlan plan) {
		return WrapperUtils.getMember(plan, EuropaMember.class);
    }
	
	/* public for testing only */
	public EuropaMember(EPlan plan) {
		this.client = new EuropaSessionClient(plan.getName());
		thread = new EuropaMemberInitializationThread(this, plan, client);
		thread.setPriority(3);
		thread.start();
		listeners = new ArrayList<EuropaMemberListener>();
	}
	
	public void addEuropaMemberListener(EuropaMemberListener l) { listeners.add(l); }
	public void removeEuropaMemberListener(EuropaMemberListener l) { listeners.remove(l); }

	void initializationStarted(Europa europa) {
		for (EuropaMemberListener l : listeners)
			l.initializationStarted(europa);
	}
	
	/**
	 * This method is called from the initialization thread after
	 * the connection has been established and the Europa object
	 * has been initialized.
	 * 
	 * @param europa
	 */
	/* package */ void initializationFinished(Europa europa) {
		this.europa = europa;
		for (EuropaMemberListener l : listeners)
			l.initializationFinished(europa);
	}
	
	@Override
	public void dispose() {
		disconnect();
	}

	/**
	 * This method will return a Europa object if the connection
	 * has been established and initialized with the model state.
	 * 
	 * @return
	 */
	public Europa getEuropa() {
		return europa;
	}
	
	/*
	 * Utility functions
	 */
	
	/**
	 * If connected, disconnects.  Otherwise does nothing.
	 */
	private void disconnect() {
		if (thread != null) {
			// time to die
			thread.setQuit(true);
			thread.interrupt();
		}
		Thread shutdownThread = new Thread("EuropaShutdownThread") {
			@Override
			public void run() {
				client.disconnect();
			}
		};
		shutdownThread.start();
	}

	static public interface EuropaMemberListener
	{
		public void initializationStarted(Europa europa);
		public void initializationFinished(Europa europa);
	}
}
