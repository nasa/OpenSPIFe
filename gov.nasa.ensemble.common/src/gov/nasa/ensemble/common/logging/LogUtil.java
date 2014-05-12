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
package gov.nasa.ensemble.common.logging;

import static fj.P.p;
import static fj.data.List.list;
import static fj.function.Strings.contains;
import static gov.nasa.ensemble.common.functional.Predicates.or;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import fj.F;
import fj.data.List;

public class LogUtil {
	
	private static final ConsoleAppender SYSERR_CONSOLE_APPENDER = new ConsoleAppender(new SimpleLayout(), "System.err");
	private static final Map<Logger,Set<Object>> warnMessagesMap = new HashMap<Logger,Set<Object>>();
	private static final Map<Logger,Set<Object>> errorMessagesMap = new HashMap<Logger,Set<Object>>();
	private static boolean reported = false;
	
	public static void error(Object message) {
		internalLogger().error(message);
	}
	
	public static void error(Throwable t) {
		internalLogger().error(t, t);
	}
	
	public static void warn(Object message) {
		internalLogger().warn(message);
	}
	
	/**
	 * Checks to see if an equal message has already been logged by the same logger.  Only logs
	 * the message if it is new.
	 * 
	 * @param message to be logged if not logged before
	 */
	public static void warnOnce(Throwable t) {
		throwableOnce(warnMessagesMap, t, false);
	}

	public static void errorOnce(Throwable t) {
		throwableOnce(errorMessagesMap, t, true);
	}

	private static void throwableOnce(Map<Logger, Set<Object>> map, Throwable t, boolean isError) {
		Object message = t.getMessage();
		Logger logger = internalLogger();
		Set<Object> messages = map.get(logger);
		if (messages == null) {
			messages = new HashSet<Object>();
			map.put(logger, messages);
		}
		if (messages.add(message)) {
			if (isError) {
				logger.error(message, t);
			} else {
				logger.warn(message, t);
			}
		}
	}
	
	/**
	 * Checks to see if an equal message has already been logged by the same logger.  Only logs
	 * the message if it is new.
	 * 
	 * @param message to be logged if not logged before
	 */
	public static void warnOnce(Object message) {
		messageOnce(warnMessagesMap, message, false);
	}

	public static void errorOnce(String message) {
		messageOnce(errorMessagesMap, message, true);
	}

	private static void messageOnce(Map<Logger, Set<Object>> map, Object message, boolean isError) {
		Logger logger = internalLogger();
		Set<Object> messages = map.get(logger);
		if (messages == null) {
			messages = new HashSet<Object>();
			map.put(logger, messages);
		}
		if (messages.add(message)) {
			if (isError) {
				logger.error(message);
			} else {
				logger.warn(message);
			}
		}
	}
	
	public static void warn(Throwable t) {
		internalLogger().warn(t, t);
	}
	
	public static void info(Object message) {
		internalLogger().info(message);
	}
	
	public static void info(Throwable t) {
		internalLogger().info(t, t);
	}
	
	public static void debug(Object message) {
		internalLogger().debug(message);
	}
	
	public static void debug(Throwable t) {
		internalLogger().debug(t, t);
	}
	
	public static void error(Object o, Throwable t) {
		internalLogger().error(o, t);
	}
	
	public static void warn(Object o, Throwable t) {
		internalLogger().warn(o, t);
	}
	
	public static void info(Object o, Throwable t) {
		internalLogger().info(o, t);
	}
	
	public static void debug(Object o, Throwable t) {
		internalLogger().debug(o, t);
	}
	
	public static Logger logger() {
		return internalLogger(3);
	}
	
	private static Logger internalLogger() {
		return internalLogger(4);
	}
	
	private static Logger internalLogger(int stackIndex) {
		try {
			final F<StackTraceElement, String> toClassName = new F<StackTraceElement, String>() {
				@Override
				public String f(final StackTraceElement element) {
					return element.getClassName();
				}
			};
			final List<String> ignorableFragments = list("java.lang", "clover", LogUtil.class.getName());
			final F<String, Boolean> ignore = or(ignorableFragments.map(contains));
			final List<StackTraceElement> stackTrace = list(Thread.currentThread().getStackTrace());
			final String className = stackTrace.map(toClassName).dropWhile(ignore).orHead(p("NULL"));
			Logger logger = Logger.getLogger(className);
			ensureSomeLogging();
			return logger;
		} catch (Exception e) {
			if (!reported) {
				reported = true;
				System.err.print("LogUtil.internalLogger: " + e.getMessage());
				e.printStackTrace(System.err);
			}
			return null;
		}
	}

	private static void ensureSomeLogging() {
		Logger rootLogger = Logger.getRootLogger();
		Enumeration rootAppenders = rootLogger.getAllAppenders();
		if (!rootAppenders.hasMoreElements()) {
			rootLogger.addAppender(SYSERR_CONSOLE_APPENDER);
		} else {
			rootAppenders.nextElement();
			if (rootAppenders.hasMoreElements()) {
				// it got another appender, so take the syserr one away.
				rootLogger.removeAppender(SYSERR_CONSOLE_APPENDER);
			}
		}
	}

	public static void configureSimpleLogger() {
		Logger.getRootLogger().addAppender(SYSERR_CONSOLE_APPENDER);
	}
	
}
