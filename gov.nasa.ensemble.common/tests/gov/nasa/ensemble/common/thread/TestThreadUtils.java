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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Assert;

import org.junit.Test;


public class TestThreadUtils extends Assert {

	boolean gotException = false;

	@Test
	public void testSleep() {
		Thread sleepingThread = new Thread() {
			@Override
			public void run() {
				try {
					ThreadUtils.sleep(5000);
				} catch (Throwable e) {
					if (e instanceof InterruptedException)
						gotException = true;
				}
			}
		};
		
		sleepingThread.start();
		sleepingThread.interrupt();
		try {
			sleepingThread.join();
		} catch (InterruptedException e) {
			fail("Unexpected thread interruption.");
		}
		assertFalse("Unexpected exception was thrown.", gotException);
	}

	@Test
	public void testNewPriorityThreadPool() throws Exception {
		String name0 = "foo";
		try {
			ThreadUtils.newPriorityThreadPool(name0, 0);
			fail();
		}
		catch (IllegalArgumentException e) {
			LogUtil.debug("Supposed to throw an illegal arg exception if numThreads == 0");
		}
		String name1 = "bar";
		ThreadPoolExecutor newPriorityThreadPool1 = (ThreadPoolExecutor) ThreadUtils.newPriorityThreadPool(name1, 1);
		String name2 = "baz";
		ThreadPoolExecutor newPriorityThreadPool2 = (ThreadPoolExecutor) ThreadUtils.newPriorityThreadPool(name2, 2);
		
		assertNotNull(newPriorityThreadPool1);
		assertEquals(1, newPriorityThreadPool1.getMaximumPoolSize());
		assertEquals(name1, newPriorityThreadPool1.getThreadFactory().newThread(null).getName());

		assertNotNull(newPriorityThreadPool2);
		assertEquals(2, newPriorityThreadPool2.getMaximumPoolSize());
		assertEquals(name2, newPriorityThreadPool2.getThreadFactory().newThread(null).getName());
	}
	
	
	@Test
	public void testNewPriorityThreadPoolWithComparator() throws Exception {
		String name1 = "bar";
		Comparator<Runnable> comparator = new Comparator<Runnable>() {
			@Override
			public int compare(Runnable o1, Runnable o2) {
				return 0;
			}
		};
		ThreadPoolExecutor newPriorityThreadPool1 = 
			(ThreadPoolExecutor) ThreadUtils.newPriorityThreadPool(name1, 1, comparator);

		assertNotNull(newPriorityThreadPool1);
		assertEquals(1, newPriorityThreadPool1.getMaximumPoolSize());
		assertEquals(name1, newPriorityThreadPool1.getThreadFactory().newThread(null).getName());
		PriorityBlockingQueue<Runnable> queue = (PriorityBlockingQueue)(newPriorityThreadPool1.getQueue());
		assertEquals(comparator, queue.comparator());
	}
	
	@Test
	public void testAddShutdownHook() throws Exception {
		Thread hook = new Thread();
		ThreadUtils.addShutdownHook(hook);
		boolean removeShutdownHook = Runtime.getRuntime().removeShutdownHook(hook);
		assertTrue(removeShutdownHook);
	}
	
	@Test
	public void testNewFixedThreadpool() throws Exception {
		String name = "foo";
		int numThreads = 2;
		try {
			ThreadUtils.newFixedThreadPool("error", 0);
		} catch (IllegalArgumentException e) {
			LogUtil.debug("0 size threadpool should throw Illegal arg exception.");
		}
		ThreadPoolExecutor newFixedThreadPool = 
			(ThreadPoolExecutor) ThreadUtils.newFixedThreadPool(name, numThreads);
		assertNotNull(newFixedThreadPool);
		assertEquals(numThreads, newFixedThreadPool.getMaximumPoolSize());
		assertEquals(name, newFixedThreadPool.getThreadFactory().newThread(null).getName());
		
	}
	
	@Test
	public void testNewLastRequestThreadPool() {
		final ArrayList<Thread> hasRunThreadList = new ArrayList<Thread>();
		final ArrayList<Thread> threadList = new ArrayList<Thread>();
		ThreadPoolExecutor pool = (ThreadPoolExecutor) ThreadUtils.newLastRequestThreadPool("lastRequestPool");
		for (int i = 0; i < 5; i++) {
			Thread runner = new Thread(""+i) {
				@Override
				public void run() {
					ThreadUtils.sleep(100);
					hasRunThreadList.add(this);
				}
			};
			threadList.add(runner);
			pool.execute(runner);
		}
		while (hasRunThreadList.size() < 2) {
			ThreadUtils.sleep(100);
		}
		
		/* only the first and last threads should have been run */
		
		assertEquals(2, hasRunThreadList.size());
		assertTrue(hasRunThreadList.get(0).getName().equals("0"));
		assertTrue(hasRunThreadList.get(1).getName().equals("4"));
	}
	
	@Test
	public void testNewCoalescingThreadpool() throws Exception {
		final ArrayList<Thread> hasRunThreadList = new ArrayList<Thread>();
		ThreadPoolExecutor pool = (ThreadPoolExecutor) ThreadUtils.newCoalescingThreadPool("pool");
		
		for (int i = 0; i < 3; i++) {
			Thread runner = new Thread(""+i) {
				@Override
				public void run() {
					ThreadUtils.sleep(100);
					hasRunThreadList.add(this);
				}
			};
			pool.execute(runner);
		}
		
		while (hasRunThreadList.size() == 0) {
			ThreadUtils.sleep(100);
		}
		
		assertTrue(pool.getQueue().isEmpty());
		assertEquals(1, hasRunThreadList.size());
		assertTrue(hasRunThreadList.get(0).getName().equals("0"));
	}
}
