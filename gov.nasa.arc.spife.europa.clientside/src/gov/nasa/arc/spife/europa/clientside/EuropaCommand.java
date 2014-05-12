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
/**
 * 
 */
package gov.nasa.arc.spife.europa.clientside;

public enum EuropaCommand 
{
	// Europa2 commands.
	
	// Server methods
	GET_SERVER_VERSION("GetServerVersion"),
	STOP_SERVER("StopServer"),
	START_SESSION("StartSession"),
	STOP_SESSION("StopSession"),

	// Session-specific methods
	ADD_CONTAINS_RELATION("AddContainsRelation"),
	ADD_TEMPORAL_BOUND_CONSTRAINT("AddTemporalBoundConstraint"),
	ADD_TEMPORAL_RELATION_CONSTRAINT("AddTemporalRelationConstraint"),
	CLEAR_ACTIVITY_SERVER_INFO("ClearActivityServer"),
	CONTINUE_FIXING("ContinueFixing"),
	CREATE_OBJECTS("CreateObjects"),
	DISABLE_ACTIVITY_FLIGHT_RULE_ACTIVE_ENFORCEMENT("DisableActivityFlightRuleActiveEnforcement"),
	DISABLE_CATEGORY_ACTIVE_ENFORCEMENT("DisableCategoryActiveEnforcement"),
	DISABLE_GLOBAL_FLIGHT_RULE_ACTIVE_ENFORCEMENT("DisableGlobalFlightRuleActiveEnforcement"),
	DISABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT("DisableGlobalFlightRulePassiveEnforcement"),
	DISABLE_SINGLE_RULE_ACTIVE_ENFORCEMENT("DisableSingleRuleActiveEnforcement"),
	ENABLE_ACTIVITY_FLIGHT_RULE_ACTIVE_ENFORCEMENT("EnableActivityFlightRuleActiveEnforcement"),
	ENABLE_CATEGORY_ACTIVE_ENFORCEMENT("EnableCategoryActiveEnforcement"),
	ENABLE_GLOBAL_FLIGHT_RULE_ACTIVE_ENFORCEMENT("EnableGlobalFlightRuleActiveEnforcement"),
	ENABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT("EnableGlobalFlightRulePassiveEnforcement"),
	ENABLE_SINGLE_RULE_ACTIVE_ENFORCEMENT("EnableSingleRuleActiveEnforcement"),
	END_FIXING("EndFixing"),
	FIX_VIOLATIONS("FixViolations"),
	FIX_VIOLATIONS_PROGRESSIVELY("FixViolationsProgressive"),
	GET_ACTIVITY_BOUNDS("GetActivityBounds"),
	GET_ACTIVITY_CONSTRAINTS("GetActivityConstraints"),
	GET_CPU_WINDOWS("GetCpuWindows"),
	GET_INCON_PARAMETERS("GetInconParameters"),
	GET_FINCON_INFO("GetFinconInfo"),
	GET_FLIGHT_RULE_NAMES("GetFlightRuleNames"),
	GET_GLOBAL_STRING("GetGlobalString"),
	GET_NOGOOD("GetNogood"),
	GET_RESOURCE_IN_TIME_FORMAT("getResourceInTmFormat"),
	GET_VIOLATIONS("GetViolations"),
	IS_CONSISTENT("IsConsistent"),
	REGISTER_ACTIVITY("RegisterActivity"),
	REGISTER_CONTAINER("RegisterContainer"),
	REGISTER_SUBACTIVITY("RegisterSubActivity"),
	REMOVE_CONTAINS_RELATION("RemoveContainsRelation"),
	REMOVE_TEMPORAL_CONSTRAINT("RemoveTemporalConstraint"),
	RENAME_ACTIVITY("RenameActivity"),
	SET_ACTIVITY_OBJECT("SetActivityObject"),    
	SET_ACTIVITY_PARAM_VALUE("SetActivityParamValue"),
	SET_ACTIVITY_START_TIME("SetActivityStartTime"),
	SET_SINGLE_ACTIVITY_SINGLE_RULE_ENFORCEMENT("SetEnforcementSubvalue"),
	SET_SUBACTIVITY_OFFSET("SetSubActivityOffset"),
	START_FIXING("StartFixing"),
	UNREGISTER_ACTIVITY("UnregisterActivity"),
	UNREGISTER_CONTAINER("UnregisterContainer"),
	UPDATE_ACTIVITY_DURATION("UpdateActivityDuration"),

	// Server side debug commands.
	TIMER_START("TimerStart"),
	TIMER_STOP("TimerStop"), 
	;
	final String xmlrpcString;
	private EuropaCommand(String xmlrpcString) {
		this.xmlrpcString = xmlrpcString;
	}
	public String getXmlrpcString() {
		return xmlrpcString;
	}
}
