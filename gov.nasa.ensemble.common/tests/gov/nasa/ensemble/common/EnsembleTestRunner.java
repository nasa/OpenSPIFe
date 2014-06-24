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


import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestSuite;

import org.eclipse.core.internal.jobs.JobManager;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

@SuppressWarnings("restriction")
public class EnsembleTestRunner implements IApplication {

	private static final String TEXT_DESTINATION_OPTION = "-text";
	private static final String XML_DESTINATION_OPTION = "-xml";
	private static final String TIMEOUT_OPTION = "-timeout";
	private static final String TEST_SUITE_FILTER_OPTION = "-filterTestSuitesByPattern";
	
	public static final Integer SUCCESS_EXIT = 0;
	public static final Integer FAILURE_EXIT = 1;
	public static final Integer EXCEPTION_EXIT = 2;
	public static final Integer HELP_EXIT = 3;
	public static final Integer TIMED_OUT_EXIT = 5;
	
	private long timeoutMillis = -1L;
	private boolean timedOut = false;
	private Thread mainThread = null;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		mainThread = Thread.currentThread();
		System.setProperty(CommonPlugin.JUNIT_IS_RUNNING_PROPERTY, "true");
		String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		List<ITestSuiteListener> listeners = Collections.emptyList();
		List<ITestSuiteFilter> filters = Collections.emptyList();
		try {
			listeners = processListenerArguments(arguments);
			filters = processFilterArguments(arguments);
		} catch (BadArguments e) {
			// fall through and listeners will be empty, printing help and exiting
		}
		
		if (listeners.isEmpty())
			listeners = processListenerArguments("-text", "-");
		
		long startTime = System.currentTimeMillis();
		EnsembleTestResult result = null;
		RuntimeException exception = null;
		TimeoutThread timeoutThread = null;
		try {
			if (timeoutMillis > 0) {
				timeoutThread = new TimeoutThread();
				timeoutThread.start();
			}
			TestSuite suite = EnsemblePluginTestExtensionRegistry.testSuite(filters);
			for (ITestSuiteListener listener : listeners) {
				listener.startTestSuite(suite);
			}
			result = new EnsembleTestResult();
			for (ITestSuiteListener listener : listeners) {
				result.addListener(listener);
			}
			suite.run(result);
		} catch (TimeoutError e) {
			if (timeoutThread != null) {
				timeoutThread.quit(); // do a regular exit on the main thread instead of a System.exit on the timeout thread
			}
			if (result != null) {
				result.addError(result.getLastTest(), e);
			}
			System.err.println("---Main thread exiting due to timeout---");
		} catch (RuntimeException e) {
			exception = e;
		}
		long endTime = System.currentTimeMillis();
		long runTime = endTime-startTime;
		if (result != null) {
			for (ITestSuiteListener listener : listeners) {
				listener.endTestSuite(result, runTime);
			}
		}
		printAndKillNonDaemons();
		if (timedOut) {
			System.err.println("Exiting after timeout of " + (timeoutMillis / 1000.0) + " seconds.");
			System.exit(TIMED_OUT_EXIT);
		}
		if (exception != null) {
			System.err.println("Error in encountered: ");
			exception.printStackTrace();
			System.exit(EXCEPTION_EXIT);
		}
		if ((result != null) && result.wasSuccessful()) {
			System.exit(SUCCESS_EXIT);
		}
		System.exit(FAILURE_EXIT);
		return -1; // should never reach here
	}

	@Override
	public void stop() {
		// no stop implemented
	}

	/**
	 * Print all the living non-daemon threads.
	 * @return 
	 */
	@SuppressWarnings("deprecation")
	private void printAndKillNonDaemons() {
		JobManager.shutdown(); // try to stop job threads
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		threads.remove(Thread.currentThread()); // current thread is obviously living, since it is running this
		Iterator<Thread> threadIterator = threads.iterator();
		while (threadIterator.hasNext()) {
			Thread thread = threadIterator.next();
			if (thread.isDaemon() || !thread.isAlive()) {
				threadIterator.remove();
			}
		}
		if (!threads.isEmpty()) {
			System.err.println("---Living threads that are not daemons---");
			for (Thread thread : threads) {
				dumpThreadInfo(thread, thread.getStackTrace());
			}
			System.err.println("---Stopping living threads that are not daemons---");
			for (Thread thread : threads) {
				ThreadDeath stop = new LivingNonDaemonThreadDeath();
				stop.setStackTrace(thread.getStackTrace());
				thread.stop(stop);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void printHelp() {
		System.out.println("Use arguments to specify the output destination(s) and timeout.");
		System.out.println();
		System.out.println("   "+XML_DESTINATION_OPTION+" [filename]     output in xml format");
		System.out.println("   "+TEXT_DESTINATION_OPTION+" [filename]    output in text format");
		System.out.println("   "+TIMEOUT_OPTION+            " [seconds]  timeout in seconds");
		System.out.println("   "+TEST_SUITE_FILTER_OPTION+ " [pattern]   run a TestSuite if it is accepted by the filter pattern");
		System.out.println("    The argument can be either a Java regular expression or a simple class name prefix");
		System.out.println("    If the pattern is wrapped in a pair of single or double quotes, the begining/end quote characters will be removed prior to filtering."); 
		System.out.println();
		System.out.println("Multiple xml and/or text files can be specified.  Use");
		System.out.println("the character '-' as the filename to output to the");
		System.out.println("standard output.");
	}

	private List<ITestSuiteListener> processListenerArguments(String... arguments) throws BadArguments {
		System.out.print("processing listener arguments: ");
		for (String string : arguments) {
			System.out.print(string + " ");
		}
		System.out.println();
		List<ITestSuiteListener> listeners = new ArrayList<ITestSuiteListener>();
		for (int i = 0 ; i < arguments.length ; i++) {
			String argument = arguments[i];
			if (XML_DESTINATION_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing destination for " + XML_DESTINATION_OPTION);
				}
				String destination = arguments[++i];
				OutputStream stream = getDestinationStream(destination);
				listeners.add(new XMLJUnitResultFormatter(stream));
			} else if (TEXT_DESTINATION_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing destination for " + TEXT_DESTINATION_OPTION);
				}
				String destination = arguments[++i];
				OutputStream stream = getDestinationStream(destination);
				listeners.add(new TextJunitResultFormatter(stream));
			} else if (TIMEOUT_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing time for " + TIMEOUT_OPTION);
				}
				String timeoutString = arguments[++i];
				try {
					timeoutMillis = Long.parseLong(timeoutString) * 1000L;
				} catch (NumberFormatException e) {
					throw new BadArguments("bad format for timeout: " + timeoutString);
				}
			} else if (TEST_SUITE_FILTER_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing pattern for " + TEST_SUITE_FILTER_OPTION);
				}
				i++; // ignore
			} else {
				System.out.println("ignoring unexpected argument: " + argument);
			}
		}
		return listeners;
	}

	private List<ITestSuiteFilter> processFilterArguments(String[] arguments) throws BadArguments {
		System.out.print("processing filter arguments: ");
		for (String string : arguments) {
			System.out.print(string + " ");
		}
		System.out.println();
		List<ITestSuiteFilter> filters = new ArrayList<ITestSuiteFilter>();
		for (int i = 0 ; i < arguments.length ; i++) {
			String argument = arguments[i];
			if (XML_DESTINATION_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing destination for " + XML_DESTINATION_OPTION);
				}
				i++; // ignore
			} else if (TEXT_DESTINATION_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing destination for " + TEXT_DESTINATION_OPTION);
				}
				i++; // ignore
			} else if (TIMEOUT_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing time for " + TIMEOUT_OPTION);
				}
				i++; // ignore
			} else if (TEST_SUITE_FILTER_OPTION.equalsIgnoreCase(argument)) {
				if (i + 1 == arguments.length) {
					throw new BadArguments("missing pattern for " + TEST_SUITE_FILTER_OPTION);
				}
				String pattern = arguments[++i];
				filters.add(new TestSuitePatternFilter(pattern));
			} else {
				System.out.println("ignoring unexpected argument: " + argument);
			}
		}
		filters.add(new TestSuiteDisabledByPropertyFilter());
		return filters;
	}
	
	private OutputStream getDestinationStream(String destination) throws BadArguments {
		if ("-".equals(destination)) {
			return System.out;
		}
		try {
			return new FileOutputStream(destination);
		} catch (FileNotFoundException e) {
			throw new BadArguments("Could not open file for output: " + e.getMessage());
		}
	}

	/**
	 * Dump the thread info.
	 * 
	 * @param thread
	 * @param trace
	 * @return
	 */
	private boolean dumpThreadInfo(Thread thread, StackTraceElement[] trace) {
		boolean forciblyExit = false;
		System.err.println();
		System.err.println("Thread: " + thread.getName());
		for (StackTraceElement element : trace) {
			String string = element.toString();
			System.err.println("  " + string);
			if (string.contains("ThreadPoolExecutor")) {
				forciblyExit = true;
			}
		}
		return forciblyExit;
	}
	
	private static final class LivingNonDaemonThreadDeath extends ThreadDeath {
		@Override
		public String getMessage() {
			return "Stopped before main thread exit";
		}
	}

	public class TimeoutThread extends Thread {
		
		private boolean quit = false;

		public TimeoutThread() {
			super("EnsembleTestRunner-TimeoutThread");
			setDaemon(true);
		}
		
		public void quit() {
			quit  = true;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(timeoutMillis);
				timedOut();
			} catch (InterruptedException e) {
				System.err.println("TimeoutThread interrupted: " + e.getMessage());
			}
		}

		@SuppressWarnings("deprecation")
		private void timedOut() {
			timedOut = true;
			System.err.println("---TIMED OUT---");
			Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
			for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
				Thread thread = entry.getKey();
				StackTraceElement[] trace = entry.getValue();
				dumpThreadInfo(thread, trace);
			}
			System.err.println();
			if (mainThread.isAlive()) {
				mainThread.stop(new TimeoutError());
			}
			waitToDie();
		}

		private void waitToDie() {
			try {
				Thread.sleep(30*1000L);
				if (quit) {
					return;
				}
				System.err.println("---Main thread failed to exit, terminating via System.exit---");
				System.exit(TIMED_OUT_EXIT);
			} catch (InterruptedException e) {
				if (quit) {
					return;
				}
				System.err.println("waitToDie interrupted: " + e.getMessage());
				System.exit(EXCEPTION_EXIT);
			}
		}
	}
	
	public static final class BadArguments extends Exception {

		public BadArguments(String string) {
			super(string);
		}

		public BadArguments() {
			super();
		}

		public BadArguments(String message, Throwable cause) {
			super(message, cause);
		}

		public BadArguments(Throwable cause) {
			super(cause);
		}

	}

	public final class TimeoutError extends ThreadDeath {

		private final State state;

		public TimeoutError() {
			this.state = mainThread.getState();
			StackTraceElement[] trace = mainThread.getStackTrace();
			try {
				if ((state == State.WAITING) || (state == State.TIMED_WAITING)) {
					List<StackTraceElement> elements = new ArrayList<StackTraceElement>(Arrays.asList(trace));
					long ownerId = mainThread.getId();
					ThreadMXBean bean = ManagementFactory.getThreadMXBean();
					ThreadInfo threadInfo = null;
					long nextId = ownerId;
					while (nextId != -1) {
						threadInfo = bean.getThreadInfo(nextId, Integer.MAX_VALUE);
						if (threadInfo == null) {
							break; // the thread being blocked on has terminated
						}
						nextId = threadInfo.getLockOwnerId();
						if (nextId == ownerId) {
							break;
						}
						String threadName = threadInfo.getThreadName();
						elements.add(new StackTraceElement(TimeoutError.class.getCanonicalName(), "THREAD_" + threadName, "STACK$TRACE", 1));
						elements.addAll(Arrays.asList(threadInfo.getStackTrace()));
						if (nextId == -1) {
							break;
						}
					}
					trace = elements.toArray(trace);
				}
			} catch (Exception e) {
				LogUtil.error(e);
			}
			StackTraceElement[] newTrace = new StackTraceElement[trace.length+1];
			newTrace[0] = new StackTraceElement(TimeoutError.class.getCanonicalName(), "ESTIMATED", "STACK$TRACE", 1);
			System.arraycopy(trace, 0, newTrace, 1, trace.length);
			setStackTrace(trace);
		}
		
		@Override
		public String getMessage() {
			return "TIMED OUT in state '" + String.valueOf(state) + "'" + " (" + String.valueOf(timeoutMillis) + ")";
		}

		public State getState() {
			return state;
		}
		
	}


}
