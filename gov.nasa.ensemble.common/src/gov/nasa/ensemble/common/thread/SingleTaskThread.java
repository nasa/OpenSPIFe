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

import org.apache.log4j.Logger;


/**
 * The SingleTaskThread accepts Runnable objects for execution via the
 * addRunnable() method. This class only keeps a pointer to the last Runnable it
 * receives. For example, if three Runnables are received in quick succession,
 * only the last one will be executed. Adding a new Runnable does not kill the
 * currently executing Runnable (if any).
 * 
 */
public class SingleTaskThread extends Thread implements Runnable {
	
	private static final Logger trace = Logger.getLogger(SingleTaskThread.class);
		
	private SingleValueSyncedStack<Runnable> stack = new SingleValueSyncedStack<Runnable>();
	private boolean keepRunning = true;
	private int periodMilliseconds;

	
	public SingleTaskThread() {
		this("SingleTaskThread", 200);
	}

	public SingleTaskThread(String name) {
		this(name, 200);
	}
	
	public SingleTaskThread(int periodMilliseconds) {
		this("SingleTaskThread", periodMilliseconds);
	}
	
	/**
	 * @param name thread name
	 * @param periodMilliseconds period (in milliseconds) -- defaults to 200 ms
	 */
	public SingleTaskThread(String name, int periodMilliseconds) {
		super(name);
		this.periodMilliseconds = periodMilliseconds;
	}
	
	/**
	 * Kindly terminate this thread after the currently executing Runnable (if
	 * any) completes.
	 * 
	 */
	public void stopRunning() {
		trace.debug("Telling thread to stop: " + getName());
		keepRunning = false;
	}
	
	/**
	 * Add a new Runnable to be executed. Overrides any un-executed Runnables.
	 * 
	 * @param runnable
	 */
	public void addRunnable(Runnable runnable) {
		stack.push(runnable);
	}
	
	/**
	 * Executes the last Runnable received via the addRunnable() method. This
	 * method loops indefinitely until the stopRunning() method is invoked.
	 */
	@Override
	public void run() {
		trace.debug("Thread started: " + getName());
		
		while(keepRunning){
			while(keepRunning && stack.empty()){
				try {
					synchronized(this){
						wait(periodMilliseconds);
					}
				} catch (InterruptedException e) {
					trace.debug(e, e);
				}
			}
			if(keepRunning && !stack.empty()){
				try {
					Runnable runnable = stack.pop();
					runnable.run();
				} catch (Exception e) {
					trace.error("Unexpected failure while executing thread "+e,e);
				}
			}
		}
		
		trace.debug("Thread finished: " + getName());
	}
}
