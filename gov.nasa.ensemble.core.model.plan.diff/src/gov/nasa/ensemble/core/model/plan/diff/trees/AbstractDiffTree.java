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
package gov.nasa.ensemble.core.model.plan.diff.trees;

import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;


abstract public class AbstractDiffTree {
	
	protected PlanDiffNode root;
	private boolean includeUnchanged = true;

	abstract public String getDescriptionForUser();
	
	protected abstract void add (PlanDiffList diffModel);
	
	protected abstract void init ();
	
	public PlanDiffNode getRoot() {
		init();
		return root;
	}

	/** Including unchanged activities as context was part of Jack Li's design, but in 2011 the other designers don't like it.
	 * Also, it increases time and file size significantly.
	 * However, the JUnit tests depend on it, so we can't turn it off just yet.
	 */
	public boolean includeUnchanged() {
		return includeUnchanged;
	}
	
	/** Including unchanged activities as context was part of Jack Li's design, but in 2011 the other designers don't like it.
	 * Also, it increases time and file size significantly.
	 * However, the JUnit tests depend on it, so we can't turn it off just yet.
	 */
	public void setIncludeUnchanged(boolean newValue) {
		includeUnchanged = newValue;
	}

}
