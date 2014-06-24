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
package gov.nasa.ensemble.common.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ThreadQueue extends Thread {
	
	private static Logger trace = Logger.getLogger(ThreadQueue.class);
	private List<Runnable> updates = new ArrayList<Runnable>();
	private boolean keepRunning = true;
		
	public ThreadQueue(String name) {
		super(name);
	}

	@Override
	public void run() {
		while (keepRunning) {
			try {
				for (Runnable resource : getRunnablesToUpdate()) {
					update(resource);
				}
			} catch (InterruptedException ie) {
				trace.info("resource update thread interrupted");
			}				
		}
	}

	private void update(Runnable runnable) {
		runnable.run();
	}
		
	private synchronized List<Runnable> getRunnablesToUpdate() throws InterruptedException {
		if (keepRunning && updates.isEmpty()) {
			wait();
		}
		List<Runnable> resources = updates;
		updates = new ArrayList<Runnable>();
		return resources;
	}
		
	public synchronized void addRunnable(Runnable runnable) {
		updates.add(runnable);
		notify();
	}

	public synchronized void quit() {
		keepRunning = false;
		clear();
		notify();
	}
		
	public synchronized void clear() {
		updates.clear();
	}
	
}
