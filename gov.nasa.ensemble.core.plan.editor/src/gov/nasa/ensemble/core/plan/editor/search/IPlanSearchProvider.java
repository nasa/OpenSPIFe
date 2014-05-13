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
package gov.nasa.ensemble.core.plan.editor.search;

/**
 * Provides new features to index for Plan Search.
 * @author ideliz
 * 
 * @see PlanIndexer
 * @see PlanSearchPage
 */
public interface IPlanSearchProvider {

	/**
	 * @return Name of feature to index
	 */
	public String getFeatureName();
	
	/**
	 * @return Display name for selecting in Plan Search Dialog
	 */
	public String getDisplayName();
	
	/**
	 * @param object
	 * @return String representation of the feature value you are trying to index.
	 */
	public String getFacet(Object object);
	
}
