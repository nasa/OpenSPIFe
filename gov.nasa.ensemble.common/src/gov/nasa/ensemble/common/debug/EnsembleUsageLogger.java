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
package gov.nasa.ensemble.common.debug;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * The mission independent version of a usage logger.  This usage 
 * logger does nothing more than simply chain the <code>logUsage</code>
 * call to a <code>trace.debug</code> call. 
 */
public abstract class EnsembleUsageLogger implements MissionExtendable {

	public static enum FIELDS {
		ID("usageLog_ID"),
		DATE("userlocaltime"),
		USER("userName"),
		MACHINE("hostName"),
		OS("osName"),
		OSVERSION("osVersion"),
		JVM("javaVersion"),
		TYPE("type"),
		DETAILS("details"),
		NUMBER("numericValue"),
		EXERCISE("exercise"),
		SESSIONID("usageSession_ID"),
		VERSION("version"),
		DEPLOYMENT("deployment"),
		;
		
		private String dbColumn;

		FIELDS(String dbColumn) {
			this.dbColumn = dbColumn;
		}

		public String getDbColumn() {
			return dbColumn;
		}
	}

	private static final Logger trace = Logger.getLogger(EnsembleUsageLogger.class);
	private static EnsembleUsageLogger instance;
	private static boolean silent = CommonPlugin.isJunitRunning();
	
	public static final String dateFormatString = "yyyy-MM-dd'T'HH:mm:ss";
	
	/**
	 * A log4j appender that will write ERROR and FATAL log messages to the
	 * usage log
	 */
	private static AppenderSkeleton usageAppender;
	
	protected EnsembleUsageLogger() {
		// default constructor
	}

	public static EnsembleUsageLogger getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(EnsembleUsageLogger.class);
			} catch (ConstructionException e) {
				instance = new EnsembleUsageLogger() {
					@Override
					protected void logUsageImpl(String usageType, String usageDetails, Double usageNumeric) {
						trace.debug(usageType + " : " + usageDetails + " : " + usageNumeric);
					}
				};
				trace.warn("couldn't instantiate mission-specific usage logger");
			}
		}
		return instance;
	}

	public static void setSilent(boolean silent) {
		EnsembleUsageLogger.silent = silent;
	}
	
	public static void logUsage(String usageType, String usageDetails, Double usageNumeric) {
		if (!silent) {
			if (usageType == null)
				usageType = Level.INFO.toString();
			getInstance().logUsageImpl(usageType, usageDetails, usageNumeric);
		}
	}
	
	public static void logUsage(String usageType, String usageDetails, long usageNumeric) {
		logUsage(usageType, usageDetails, (double)usageNumeric);
	}
	
	public static void logUsage(String usageType, String usageDetails) {
		logUsage(usageType, usageDetails, null);
	}
	
	public static void logUsage(String usageDetails, double usageNumeric) {
		logUsage(null, usageDetails, usageNumeric);
	}
	
	public static void logUsage(String usageDetails, long usageNumeric) {
		logUsage(null, usageDetails, (double)usageNumeric);
	}
	
	public static void logUsage(String usageDetails) {
		logUsage(null, usageDetails, null);
	}

	public static void logUsage(double usageNumeric) {
		logUsage(null, usageNumeric);
	}
	
	public static void logUsage() {
		logUsage(0.);
	}
	
	/**
	 * Chain this call to a <code>trace.debug</code> call.
	 * 
	 * @param usageType
	 * @param usageDetails
	 * @param usageNumeric
	 */
	protected abstract void logUsageImpl(String usageType, String usageDetails, Double usageNumeric);
	
	/**
	 * Add a usage appender for the root logger. This appender will write ERROR and FATAL log messages 
	 * to the usage database.
	 */
	public static void addErrorAppender() {
		usageAppender = new AppenderSkeleton() {
			boolean logAll = Boolean.getBoolean("logAll");
			PatternLayout patternLayout = new PatternLayout("[%t] %c - %m");
			
			@Override
			protected void append(LoggingEvent event) {
				
				if (logAll || event.getLevel().equals(Level.ERROR) ||
					event.getLevel().equals(Level.FATAL)) {
					ThrowableInformation info = event.getThrowableInformation();
					StringBuffer buf = new StringBuffer();
					if (info == null) buf.append("No stack trace");
					else {
						for (String line : info.getThrowableStrRep())
							buf.append(line).append('\n');
					}

					EnsembleUsageLogger.logUsage(
							event.getLevel().toString(),
							patternLayout.format(event) +" -- "+ buf.toString());
				}
			}
			@Override
			public void close() { /* do nothing */ }
			@Override
			public boolean requiresLayout() { return false;	}
			
		};
		
		Logger.getRootLogger().addAppender(usageAppender);
	}

	/**
	 * Remove the usage database appender.
	 */
	public static void removeErrorAppender() {
		Logger.getRootLogger().removeAppender(usageAppender);
	}

}
