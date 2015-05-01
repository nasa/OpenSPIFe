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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.EnsembleTestRunner.TimeoutError;

import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.AssertionFailedError;
import junit.framework.JUnit4TestCaseFacade;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TextJunitResultFormatter implements ITestSuiteListener {

	private final OutputStream out;
	private long testStart = 0L;
	private Test currentTest = null;
	private IOException exception;
	private SortedMap<Long, Test> testTimes = new TreeMap<Long, Test>();

	public TextJunitResultFormatter() {
		this(System.out);
	}
	
	public TextJunitResultFormatter(OutputStream stream) {
		this.out = stream;
	}

	@Override
	public void startTestSuite(TestSuite suite) {
		println("\nstarting test suite: " + suite.getName());
	}

	@Override
	public void endTestSuite(TestResult result, long runTime) throws IOException {
		println("\ntests run: " + result.runCount());
		println("errors: " + result.errorCount());
		println("failures: " + result.failureCount());
		println("total run time: " + (runTime / 1000.0) + " seconds");
		println("test lengths in ascending order:");
		for (long duration : testTimes.keySet())
			println(testTimes.get(duration) + " - " + (duration / 1000.0) + " seconds");
		
		if (exception != null) {
			throw exception;
		}
		if ((out != System.out) && (out != System.err) && (out != null)) {
           	out.close();
        }
	}

	@Override
	public void startTest(Test test) {
		testStart = System.currentTimeMillis();
		currentTest = test;
		println("\nstarting test case: " + getName(test));
	}

	@Override
	public void endTest(Test test) {
		if (test == currentTest) {
			long testEnd = System.currentTimeMillis();
			long duration = (testEnd - testStart);
			println("finished test case: " + getName(test) + " elapsed " + (duration / 1000.0) + " seconds.");
			testTimes.put(duration, test);
		}
		currentTest = null;
	}

	@Override
	public void addError(Test test, Throwable t) {
		println(getName(test) + " ERROR - " + t.getMessage());
		if (t instanceof TimeoutError) {
			timedOut(test, (TimeoutError) t);
		}
	}

	@Override
	public void addFailure(Test test, AssertionFailedError t) {
		println(getName(test) + " FAILURE - " + t.getMessage());
	}

	public void timedOut(Test test, TimeoutError error) {
		long testTime = System.currentTimeMillis();
		long duration = (testTime - testStart);
		println(getName(test) + " " + error.getMessage() + " after " + (duration / 1000.0) + " seconds.");
		for (StackTraceElement element : error.getStackTrace()) {
			println("  " + element.toString());
		}
	}
	
	/*
	 * Utility methods
	 */
	
	private void println(String string) {
		try {
			out.write(string.getBytes());
			out.write('\n');
		} catch (IOException e) {
			exception = e;
			System.err.println(e);
		}
	}

	private static String getName(Test test) {
		if (test == null) {
			return "<Null Test>";
		}
		String name = test.getClass().getName();
		if (test instanceof TestCase) {
			TestCase testCase = (TestCase) test;
			return name + "." + testCase.getName() + "()";
		}
		
		if (test instanceof JUnit4TestCaseFacade) {
			JUnit4TestCaseFacade junit4TestCase = (JUnit4TestCaseFacade) test;
			return junit4TestCase.getDescription().getDisplayName();
		}
		return name;
	}

}
