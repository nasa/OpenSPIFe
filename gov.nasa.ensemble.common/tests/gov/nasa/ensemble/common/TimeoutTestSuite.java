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
/**
 * 
 */
package gov.nasa.ensemble.common;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TimeoutTestSuite extends TestSuite {
	
	private static final long DEFAULT_TIMEOUT_IN_MILLISECONDS = 5*60*1000; // 5 minutes
	private final long timeoutInMilliseconds;

	public TimeoutTestSuite(Class<?> klass) {
		this(klass, DEFAULT_TIMEOUT_IN_MILLISECONDS);
	}

	public TimeoutTestSuite(Class<?> klass, long timeoutInMilliseconds) {
		super(klass.getCanonicalName());
		this.timeoutInMilliseconds = timeoutInMilliseconds;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void runTest(final Test test, final TestResult result) {
		final Thread waitingThread = Thread.currentThread();
		Thread thread = new Thread(test.toString()) {
			@Override
			public void run() {
				boolean timed_out = false;
				try {
					TimeoutTestSuite.super.runTest(test, result);
				} catch (ThreadDeath td) {
					timed_out = true;
				} finally {
					if (!timed_out) {
						waitingThread.interrupt();
					}
				}
			}
		};
		try {
			thread.start();
			Thread.sleep(DEFAULT_TIMEOUT_IN_MILLISECONDS);
			AssertionFailedError error = new AssertionFailedError(test.toString() + " failed to finish within " + timeoutInMilliseconds + " milliseconds.");
			error.setStackTrace(thread.getStackTrace());
			result.addFailure(test, error);
			thread.stop();
		} catch (InterruptedException e) {
			// ignore it, this means success
		}
	}
}
