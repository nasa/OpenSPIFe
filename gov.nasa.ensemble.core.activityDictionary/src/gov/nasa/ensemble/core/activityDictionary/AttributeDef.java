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
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.dictionary.INamedDefinition;


/**
 * AttributeDef represents a single attribute definition contained in the
 * activity dictionary.
 * 
 * Attribute definitions are common across all activity types (as opposed to
 * argument definitions which are specific to an activity type). For example,
 * something like the 'type' of an activity is a common attribute across all
 * activities and is therefore defined in an attribute definition. Something
 * like which compression algorithm to use for an image is a very specific
 * argument for an image activity and is therefore contained in an argument
 * definition for an image activity.
 */
public class AttributeDef extends ParameterDef implements INamedDefinition {

	/**
	 * Default constructor.
	 */
	public AttributeDef() {
		// default constructor 
	}

}
