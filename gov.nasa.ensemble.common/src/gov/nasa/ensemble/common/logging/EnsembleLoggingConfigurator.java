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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.EnsembleTestRunner;
import gov.nasa.ensemble.common.extension.ExtensionUtils;
import gov.nasa.ensemble.common.mission.MissionExtendable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class EnsembleLoggingConfigurator implements MissionExtendable {

	private static final Logger trace = Logger.getLogger(EnsembleLoggingConfigurator.class);
	
	private static final String DEFAULT_LOG_FILE_DIRECTORY_NAME = System.getProperty("user.home");
	private static final String DEFAULT_LOG4J_PROPERTIES_FILE = "defaultLog4j.properties";
	private static final String LOG4J_EXTENSION_PT_ID = "gov.nasa.ensemble.common.Log4jExtension";
	private static final String NAME_OF_CONSOLE_APPENDER = "stdout";
	private static final String LOG_FILE_NAME_KEY = "log.file.name";
	private static final String LOG_FILE_DIR_KEY = "log.file.dir";
	
	private static final String logFilename = EnsembleProperties.getProperty(LOG_FILE_NAME_KEY, "EnsembleApplication.log");
	public  static String file = getLogFileDirectoryName() + File.separator + logFilename;
	
	public final void configureLogging() throws IOException {
		// FIXME: This code keeps logging configuration extensions from running.
//		// checks to make sure the logger hasn't already been initialized instead of forcing it
//		int cnt = 0;
//		for( Enumeration e = Logger.getRootLogger().getAllAppenders(); e.hasMoreElements(); e.nextElement()) {
//			cnt++;
//		}
//		if(cnt > 0) { // root appender already exists
//			return;
//		}
		InputStream loggingPropertiesStream = getLoggingPropertiesStream();
		if (loggingPropertiesStream == null) {
			System.err.println("Unable to configure logging - EnsembleLoggingConfigurator.getLoggingPropertiesFile() returned null");
			return;
		}
		Properties loggerProperties = new Properties();
		loggerProperties.load(loggingPropertiesStream);
		IOUtils.closeQuietly(loggingPropertiesStream);
		// set the log file property
		if (CommonPlugin.isJunitRunning()) {
			file = EnsembleTestRunner.class.getSimpleName() + ".log";
		}
		loggerProperties.setProperty("log4j.appender.file.File", file);
		PropertyConfigurator.configure(loggerProperties);
		// configure the console appender to an appropriate level
		configureConsoleAppender(loggerProperties, NAME_OF_CONSOLE_APPENDER);
	}

	/**
	 * Default instance checks first the extension point and then defaults to the
	 * properties file held locally. MissionExtendable so the behavior of this class
	 * may be overridden.
	 */
	protected InputStream getLoggingPropertiesStream() {
		InputStream stream = inspectExtensionPoints();
		if (stream == null) {
			try {
				stream = FileLocator.openStream(CommonPlugin.getDefault().getBundle(), new Path(DEFAULT_LOG4J_PROPERTIES_FILE), false);
			} catch (IOException e) {
				LogUtil.error(e);
			}
		}
		return stream;
	}
	
	protected static String getLogFileDirectoryName() {
		return System.getProperty(LOG_FILE_DIR_KEY, DEFAULT_LOG_FILE_DIRECTORY_NAME);
	}
	
	/**
	 * A convenience method to allow implementors the ability to override some
	 * of the basic functionality of this logger using the extension point mechanism
	 * rather than a programmatic MissionExtension.
	 */
	protected InputStream inspectExtensionPoints() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(LOG4J_EXTENSION_PT_ID);
		IConfigurationElement elements[] = extensionPoint.getConfigurationElements();
		if (elements == null || elements.length == 0) {
			return null;
		}
		if (elements.length > 1) {
			System.err.println("Multiple extensions for log4j found, defaulting");
			return null;
		}
		IConfigurationElement element = elements[0];
		try {
			return ExtensionUtils.getInputStream(element, "properties_file");
		} catch (IOException e) {
			LogUtil.error(e);
			return null;
		}
	}
	
	private void configureConsoleAppender(Properties properties, String consoleAppenderName) {
		Appender consoleAppender = Logger.getRootLogger().getAppender(consoleAppenderName);
		if (consoleAppender == null)
			return;
		
		final Level level = Level.toLevel(properties.getProperty(consoleAppenderName + ".level"), Level.ERROR);
		
		consoleAppender.addFilter(new Filter() {
			@Override
			public int decide(LoggingEvent evt) {
//				if (evt.getLevel().isGreaterOrEqual(level))
					return ACCEPT;
//				return DENY;
			}
		});
		
		trace.info("Setting console appender logging level to '" + level + "'");
	}
	
}
