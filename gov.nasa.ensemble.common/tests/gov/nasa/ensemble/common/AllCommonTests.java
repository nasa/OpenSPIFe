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

import gov.nasa.ensemble.common.cache.TestLRUBoundedCapacityCache;
import gov.nasa.ensemble.common.collections.TestDirectedGraph;
import gov.nasa.ensemble.common.debug.TestEnsembleLogger;
import gov.nasa.ensemble.common.event.TestEventListenerList;
import gov.nasa.ensemble.common.id.TestUniqueIdGenerator;
import gov.nasa.ensemble.common.io.TestCSVUtilities;
import gov.nasa.ensemble.common.io.TestRemotableFile;
import gov.nasa.ensemble.common.mission.TestMissionCalendarUtils;
import gov.nasa.ensemble.common.mission.TestMissionExtender;
import gov.nasa.ensemble.common.operations.TestOperationJob;
import gov.nasa.ensemble.common.text.TestStringEscapeFormat;
import gov.nasa.ensemble.common.text.TestWordWrapper;
import gov.nasa.ensemble.common.time.TestDateUtils;
import gov.nasa.ensemble.common.time.TestDualMissionAndDateCalendar;
import gov.nasa.ensemble.common.time.TestDurationFormat;
import gov.nasa.ensemble.common.time.TestEarthTimeShorthand;
import gov.nasa.ensemble.common.time.TestISO8601DateFormat;
import gov.nasa.ensemble.common.time.TestMissionElapsedTimeFormat;
import gov.nasa.ensemble.common.type.TestStringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.TestDateOnlyStringifier;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 */
public class AllCommonTests implements IEnsemblePluginTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllCommonTests.class.getName());
		new AllCommonTests().addTests(suite);
		return suite;
	}

	@Override
	public void addTests(TestSuite suite) {
		//$JUnit-BEGIN$
		suite.addTestSuite(TestIdentifiable.class);
		suite.addTestSuite(TestJunitProperty.class);
		suite.addTestSuite(TestDateUtils.class);
		suite.addTestSuite(TestDurationFormat.class);
		suite.addTest(TestISO8601DateFormat.suite());
		suite.addTestSuite(TestMissionElapsedTimeFormat.class);
		suite.addTestSuite(TestOperationJob.class);
		if (TestDualMissionAndDateCalendar.canRun()) {
			suite.addTestSuite(TestDualMissionAndDateCalendar.class);
		}
		suite.addTestSuite(TestEarthTimeShorthand.class);
		suite.addTestSuite(TestDateOnlyStringifier.class);
		suite.addTestSuite(TestEnsembleLogger.class);
		suite.addTestSuite(TestMissionExtender.class);
		suite.addTestSuite(TestEventListenerList.class);
		suite.addTestSuite(TestRemotableFile.class);
		suite.addTestSuite(TestCSVUtilities.class);
		suite.addTestSuite(TestLRUBoundedCapacityCache.class);
		suite.addTest(new TestSuite(TestDirectedGraph.class));
		suite.addTestSuite(TestWordWrapper.class);
		suite.addTestSuite(TestStringEscapeFormat.class);
		suite.addTestSuite(TestStringifierRegistry.class);
		suite.addTestSuite(TestMissionCalendarUtils.class);
		suite.addTestSuite(TestUniqueIdGenerator.class);
		//$JUnit-END$
	}

}
