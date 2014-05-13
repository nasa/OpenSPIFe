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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.common.search.SearchConstants;

public interface PlanSearchConstants extends SearchConstants {
	// plan
	public static final String PROJECT_TYPE					 	= "project";
	public static final String PLAN_TYPE					 	= "plan";
	public static final String PROJECT_STRING					= "plan";
	public static final String AD_URI_FIELD_NAME				= "adURI";
	public static final String AD_NAME_FIELD_NAME				= "adName";
	public static final String TEMPLATE 						= "Template";
	public static final String FINAL							= "Final";
	public static final String WORKING 							= "Working";
	public static final String SOWG 							= "SOWG";
	public static final String MY_PLANS 						= "My Plans";
	public static final String RML_NAME_TOKEN 					= "brokenUpNames";
	public static final String SOL_START_TOKEN 					= "startSol";
	public static final String SOL_END_TOKEN 					= "endSol";
	public static final String ACTIVITY_SEQ_ID_TOKEN 			= "activitySeqId";
	public static final String ACTIVITY_NAME_TOKEN 				= "activity";
	public static final String ACTIVITY_DEF_TOKEN 				= "activityType";
	public static final String ACTIVITY_COUNT_TOKEN 			= "activityCount";
	public static final String NOTES_FIELD_NAME 				= "notes";
	public static final String PLAN_GROUP_FIELD_NAME 			= "planGroup";
	public static final String PLAN_STATE_FIELD_NAME 			= "planState";
	public static final String PLAN_STATE_FACET_TOKEN 			= "state_facet";
	public static final String ACTIVITY_NOTES_TOKEN 			= "activityNotes";
	public static final String ACTIVITYGROUP_NAME_TOKEN 		= "activityGroup";
	public static final String ACTIVITYGROUP_NOTES_TOKEN 		= "activityGroupNotes";
	public static final String ACTIVITYGROUP_COUNT_TOKEN 		= "activityGroupCount";
	public static final String PLAN_FILE_PATH 					= "absoluteFilePath";
	public static final String PLAN_DIR_PATH					= "absolutePlanDir";
	public static final String PLAN_NAME						= "planName";
	public static final String NUMBER_OF_ACTIVITIES      		= "NUMBER_OF_ACTIVITIES";
	public static final String NUMBER_OF_ACTIVITY_GROUPS 		= "NUMBER_OF_ACTIVITY_GROUPS";
	
	/**HISTORY **/
	public static final String LATEST_HIST_COMMENT 				= "max_hist_comment";
	public static final String HIST_COMMENT 					= "hist_comment";
	public static final String LATEST_HIST_TIME 				= "latestHistTime";
	public static final String LATEST_USER_HIST 				= "lastUserHist";
	public static final String USER_HIST 						= "user_hist";
	
	/**Sequencing **/
	public static final String SEQ_NAME 						= "seqName";
	public static final String SEQ_TYPE							= "seqType";
	public static final String SEQ_OPTIONAL_INFO				= "seqOptInfo";
	public static final String SEQ_VERSION 						= "seqVersion";
	public static final String SEQ_NOTES 						= "seqNotes";
	public static final String SEQ_ONBOARD 						= "onBoard";
	public static final String COMMAND_NAME 					= "commandName";
	public static final String COMMAND_COMMENT 					= "commandComment";
	public static final String NUM_SEQ 							= "sequenceCount";
	public static final String NUM_CMD 							= "cmdCount";
	public static final String MAX_LONG_STR_FORMAT				= "0000000000000";
	

	
	public static final String REQ_ID_MAP						= "reqIdMap";

	public static final String DELIMITERS						= " _\"-";
	public static final String[] LUCENE_SPECIAL_CHARACTERS 	    = {"+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", 
																   "^", "~", "*", "?", ":", "\\", "/"};
	
	String[] PLAN_CLUSTERS                                      = { PLAN_STATE_FACET_TOKEN, CUSTODIAN_FIELD_NAME, SOL_FIELD_NAME };
}
