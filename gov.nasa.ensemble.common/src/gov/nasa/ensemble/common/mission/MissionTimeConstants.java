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

public class MissionTimeConstants {

	/**
	 *  Property key to access the start time of mission. Should be ignored if
	 *  the <code>MISSION_SIMULATION_START_TIME_ACTIVE</code> property value
	 *  is 'true', and <code>MISSION_SIMULATION_START_TIME</code> should be
	 *  used instead.
	 */
	public static final String MISSION_START_TIME = "mission.time.start";
	
	
	/**
	 * Property key to access the default start day of time for a specific mission. 
	 * This value can be overriden in the new plan wizard.
	 */
	public static final String MISSION_HOUR_START_OF_DAY = "mission.time.hour.startofday";
		
	/** 
	 * Property key to access in order to determine if the mission start time simulation is active.
	 * If so, the <code>MISSION_START_TIME</code> property is to be ignored, and 
	 * <code>MISSION_SIMULATION_START_TIME</code> should be used instead.
	 */
	public static final String MISSION_SIMULATION_START_TIME_ACTIVE = "mission.simulation.start.active";
	
	/**
	 * Property key to access the simulated mission start time. Should be ignored if
	 * <code> MISSION_SIMULATION_START_TIME_ACTIVE</code> property value is 'false'
	 */
	public static final String MISSION_SIMULATION_START_TIME = "mission.simulation.start";
	
	/**
	 * Property key to access in order to determine if the Mission Elapsed Time is to be determined
	 * from the current date, or from the <code>MISSION_SIMULATION_PLAN_DAY</code>.
	 */
	public static final String MISSION_SIMULATION_PLAN_DAY_ACTIVE = "mission.simulation.planday.default.active";
	
	/**
	 * Property key to access the simulated current time in order to determine the Mission Elapsed
	 * Time for the application. This should not be referenced unless the property value for
	 * <code>MISSION_SIMULATION_PLAN_DAY_ACTIVE</code> evaluates to true.
	 */
	public static final String MISSION_SIMULATION_PLAN_DAY = "mission.simulation.planday.default";
	
}
