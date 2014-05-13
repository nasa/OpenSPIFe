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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Collections;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 */
public class TestActivity extends TestCase {
	
	private Logger rootLogger;
	private Level level;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// shut up the logger except for warnings
		rootLogger = Logger.getRootLogger();
		level = rootLogger.getLevel();		
		rootLogger.setLevel(Level.WARN);
	}
	
	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		// remove the gag order on the logger
		rootLogger.setLevel(level);
	} 
	
	public void testCloneMethod() {
		ActivityDefGenerator generator = new ActivityDefGenerator();
		for (EActivityDef def : generator) {
			EActivity a1 = PlanFactory.getInstance().createActivity(def);
			EActivity a2 = EPlanUtils.copy(Collections.singletonList(a1)).iterator().next();
			assertNotSame(a1, a2);
			WrapperUtils.assertActivityDeepEquals(a2, a2);
		}
	}
	
}
