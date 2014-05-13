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
/*
 * Created on Aug 5, 2004
 */
package gov.nasa.ensemble.common.debug;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.log4j.Logger;


/**
 * @author mpowell
 */
public class TestEnsembleLogger extends TestCase {

	private ArrayList<Logger> traceList;
	
//	private static final LoggerFactory logFactory = new LoggerFactory() {
//		public Logger makeNewLoggerInstance(String loggerName) {
//			return Logger.getLogger(loggerName);
//		}
//	};
	
	public TestEnsembleLogger(String name) {
		super(name);
	}
	
	@Override
	protected void setUp() {
		traceList = new ArrayList<Logger>();
		traceList.add(Logger.getLogger("core.common.debug"));
		traceList.add(Logger.getLogger(new Object().getClass()));
	}	
	
	public void testDebug() {
		for(Logger trace : traceList) {
			try {
				trace.debug("Debug message.");
			}
			catch (RuntimeException re) {
				fail();
			}
		}
	}
	
	public void testWarning() {
		for(Logger trace : traceList) {
			try {
				trace.warn("Warning message.");
			}
			catch (RuntimeException re) {
				fail();
			}
		}
	}
	
	public void testError() {
		for(Logger trace : traceList) {
			try {
				trace.error("An error has occurred.");
			}
			catch (RuntimeException re) {
				fail(); //we do not want to catch an exception anymore.
			}
			
			//if we get here without having thrown a RuntimeException, we succeed.
		}
	}
	
	public void testErrorWithException() {
		for(Logger trace : traceList) {
			try {
				Exception e = new Exception("An error has occurred.");
				trace.error(e);
			}
			catch (RuntimeException re) {
				fail(); //we do not want to catch an exception anymore.
			}
			
			//if we get here without having thrown a RuntimeException, we succeed.
		}
	}	
	
	public void testAssertLog() {
		for(Logger trace : traceList) {
			try {
				trace.assertLog(false, "An assertion was false.");
			}
			catch (RuntimeException re) {
				fail(); //we do not want to catch an exception anymore.
			}
			
			//if we get here without having thrown a RuntimeException, we succeed.
		}
	}
	
	public void testFatal() {
		for(Logger trace : traceList) {
			try {
				trace.fatal("A fatal error has occurred.");
			}
			catch (RuntimeException re) {
				fail(); //we do not want to catch an exception anymore.
			}
			
			//if we get here without having thrown a RuntimeException, we succeed.
		}
	}
	
	

}
