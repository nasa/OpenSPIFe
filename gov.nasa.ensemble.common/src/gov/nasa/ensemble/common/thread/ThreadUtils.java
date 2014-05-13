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

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A class of static utility methods for dealing with threads and threadpools.
 */
public class ThreadUtils {
	/**
	 * Like Thread.sleep() except without the necessity of surrounding with try/catch
	 */
	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	
	/**
	 * Creates a fixed priority thread pool.
	 */
	public static ExecutorService newPriorityThreadPool(String name, int numThreads) {
		return new ThreadPoolExecutor(numThreads, numThreads, 1, TimeUnit.SECONDS, 
				new PriorityBlockingQueue<Runnable>(numThreads), newNamedThreadFactory(name));
	}
	
	/**
	 * Creates a fixed priority thread pool, sorted with the given comparator.
	 */
	public static ExecutorService newPriorityThreadPool(String name, int numThreads, 
			Comparator<Runnable> comparator) {
		return new ThreadPoolExecutor(numThreads, numThreads, 1, TimeUnit.SECONDS, 
				new PriorityBlockingQueue<Runnable>(numThreads, comparator), 
				newNamedThreadFactory(name));
	}
	
	/**
	 * Creates a single thread executor which ignores all executions that occur
	 * while it is busy executing a Runnable. This is useful for tasks that may
	 * be requested multiple times from multiple sources, but which only need to
	 * take place once.
	 * 
	 * @param name the name for threads created within this pool
	 */
	@SuppressWarnings("serial")
	public static ExecutorService newCoalescingThreadPool(String name) {
		return new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>(), 
				newNamedThreadFactory(name)) {
			private boolean executing = false;
			
			@Override
			public void execute(final Runnable command) {
				synchronized (this) {
					if (executing)
						return;
					executing = true;
				}
				super.execute(new Runnable() {
					@Override
					public void run() {
						try {
							command.run();
						} finally {
							executing = false;
						}
					}
				});
			}
		};
	}
	
	/**
	 * Similar to {@link #newCoalescingThreadPool(String)}, but always runs the
	 * last runnable put into the pool. If runnable a is executed, and then
	 * runnables b and c are executed while a is still running, runnable b will
	 * be dropped and c will executed.
	 */
	public static ExecutorService newLastRequestThreadPool(String name) {
		return new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>() {
					@Override
					public boolean offer(Runnable e) {
						clear();
						return super.offer(e);
					}}, newNamedThreadFactory(name));
	}
	
	public static ExecutorService newFixedThreadPool(String name, int numThreads) {
		return Executors.newFixedThreadPool(numThreads, newNamedThreadFactory(name));
	}
	
	
	public static ExecutorService newFixedThreadPool(String name, int numThreads, int maxPoolSize, int keepAliveTimeInSeconds) {
		LinkedBlockingQueue<Runnable> lbq = new LinkedBlockingQueue<Runnable>();
		ThreadFactory tf = newNamedThreadFactory(name);
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(numThreads, maxPoolSize, keepAliveTimeInSeconds, TimeUnit.SECONDS, lbq, tf);
		return Executors.newFixedThreadPool(numThreads, tpe.getThreadFactory());
	}
	
	public static ExecutorService newSingleThreadExecutor(String name) {
		return Executors.newSingleThreadExecutor(newNamedThreadFactory(name));
	}
	
	public static ThreadFactory newNamedThreadFactory(final String name) {
		return new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				Thread t = new Thread(r);
				t.setName(name);
				return t;
			}
		};
	}
	
	/**
	 * @see Runtime#addShutdownHook(Thread)
	 */
	public static void addShutdownHook(Thread hook) {
		Runtime.getRuntime().addShutdownHook(hook);
	}
}
