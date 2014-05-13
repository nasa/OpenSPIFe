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
package gov.nasa.ensemble.common.mission;

import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestMissionExtender extends TestCase {
	
	public void testConstruct() throws ConstructionException {

		MissionIndependentThing thing = MissionExtender.construct(MissionIndependentThing.class, "Two", new ArrayList<Object>());
		assertEquals(MissionSpecificThing.class, thing.getClass());
	}

	public static class MissionIndependentThing implements MissionExtendable {
		
		protected MissionIndependentThing(String arg1, ArrayList<Object> arg2) {
			// stub
		}
		
	}
	
	public static class MissionSpecificThing extends MissionIndependentThing {
		
		private MissionSpecificThing(String arg1, ArrayList<Object> arg2){
			super(arg1, arg2);
		}
		
	}
	
}
