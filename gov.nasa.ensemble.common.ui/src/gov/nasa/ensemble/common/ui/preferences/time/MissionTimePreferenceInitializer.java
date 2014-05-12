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
package gov.nasa.ensemble.common.ui.preferences.time;

import gov.nasa.ensemble.common.mission.MissionTimeConstants;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;

import java.util.Date;

public class MissionTimePreferenceInitializer extends PropertyPreferenceInitializer {

	public MissionTimePreferenceInitializer() {
		super(MissionTimePreferencePage.PREFERENCE_STORE);
	}
	
	@Override
	public void initializeDefaultPreferences() {
		Date defaultStartTime = new Date();
		
		setDate		(MissionTimeConstants.MISSION_START_TIME, MissionTimeConstants.MISSION_START_TIME, defaultStartTime);
		
		setBoolean	(MissionTimeConstants.MISSION_SIMULATION_START_TIME_ACTIVE, MissionTimeConstants.MISSION_SIMULATION_START_TIME_ACTIVE, false);
		setDate		(MissionTimeConstants.MISSION_SIMULATION_START_TIME, MissionTimeConstants.MISSION_SIMULATION_START_TIME, defaultStartTime);
		
		setBoolean	(MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY_ACTIVE, MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY_ACTIVE, false);
		setInteger		(MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY, MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY, 42);
		
		setInteger (MissionTimeConstants.MISSION_HOUR_START_OF_DAY, MissionTimeConstants.MISSION_HOUR_START_OF_DAY, 0);	
//		setString	(ClockDateFormatPreferencePage.P_DATE_FORMATS, "");
	}
	
}
