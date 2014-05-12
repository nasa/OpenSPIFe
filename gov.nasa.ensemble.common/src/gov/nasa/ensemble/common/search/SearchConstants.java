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
package gov.nasa.ensemble.common.search;

import java.util.Set;
import java.util.TreeSet;

public interface SearchConstants {

	 int RADIX = 36;
	 String REV_LAST_MOD_FIELD_NAME 		= "revLastMod";
	 String RMC_FIELD_NAME					= "rmc";
	 String ACTIVITY_SUBSYSTEM_CAT			= "subsystem";
	 String DOC_TYPE_FIELD_NAME				= "docType";
	 String CONTRIBUTOR_TYPE_FIELD_NAME		= "contType";
	 String RESULT_ID_FIELD_NAME          	= "ER_ID";
	 String SOL_FIELD_NAME         			= "sol";
	 String EARTHTIME_FIELD_NAME				= "earthtime";
	 String ID_FIELD_NAME          			= "id";
	 String REV_SOL_FIELD_NAME         		= "Revsol";
	 String SEQUENCE_ID_FIELD_NAME 			= "sequenceId";
	 String URL_FIELD_NAME					= "url";
	
	 String SITE_FIELD_NAME        			= "site";
	 String DRIVE_FIELD_NAME    			= "position";
	
	 String CUSTODIAN_FIELD_NAME 			= "custodian";	
	 String TOTAL_RESULTS_HEADER			= "totalResults";
	 String ER_COLLECTION					= "Results";
	 String CLUSTERS_COLLECTION				= "allClusters";
	 String NAME_FIELD_NAME					= "name";
	 String NAME_FIELD_NAME_NGRAM			= "namengram";
	 String PROJ_NAME_FIELD_NAME 			= "projName";
	 String PROJ_NAME_FIELD_NAME_NGRAM		= "projNamengram";
	 String LAST_MODIFIED 					= "lastModified";

	 String ROUND_TRIP_FIELD_NAME			= "rtt";

	 int REV_SOL_FIELD_BASE                  = 99999;

	 String SPATIAL_QUERY_NAME				= "xyzr";

	 String SEQUENCE_DOC_TYPE_VALUE 		= "sequence";
	 String RML_TYPE						= "rml";
	
	/** Dir listings **/
	 String DIR_PATH = "ODS_PATH";
	 String NUM_RML = "NUM_RML";
	// tags
	 String TAG_FIELD_NAME					= "tag";
	 String TAG_NOT_FIELD_NAME				= "NOT tag";
	 String ALL_TAG_FOR_DOC_FIELD_NAME		= "allMytags"; 
	
	// This is the only one that can be more than 1 word (i.e. spaces) for lucene
	 String FREEFORM_TERMS_NAME 				= "keywords";
	 String UTF_8 							= "UTF-8";
	String[] ALL_VALUES_FIELDS									= { TAG_FIELD_NAME, FREEFORM_TERMS_NAME };
	String[] SWITCHABLE_SEARCH_FIELDS							= { FREEFORM_TERMS_NAME, TAG_FIELD_NAME };
	String[] ANY_CLUSTERS										= { SEQUENCE_ID_FIELD_NAME, SOL_FIELD_NAME, ALL_TAG_FOR_DOC_FIELD_NAME };

	
	 Set<String> CLUSTER_FIELDS = new TreeSet<String>();

	/*****************************************************************************/
	String[] ONLY_ONE_VALUE_FIELDS 								= { DOC_TYPE_FIELD_NAME, SOL_FIELD_NAME, "mosaic", FREEFORM_TERMS_NAME, SPATIAL_QUERY_NAME, ROUND_TRIP_FIELD_NAME, "directory", EARTHTIME_FIELD_NAME };
}
