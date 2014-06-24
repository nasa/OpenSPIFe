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
package gov.nasa.ensemble.core.plan.advisor;

import java.util.Set;

public abstract class AdvisorListener {

	/**
	 * Called whenever advisor updates are starting
	 */
	public void advisorsUpdating() {
		// override to show an indication that violations are being updated
	}

	/**
	 * Called whenever violation updates are finished
	 */
	public void advisorsUpdated() {
		// override to show an indication that violations have been updated
	}
	
	/**
	 * Called whenever violations are added from some plan advisor
	 * @param violations
	 */
	public void violationsAdded(Set<ViolationTracker> violations) {
		// override to handle new violations
	}
	
	/**
	 * Called whenever violations are removed
	 * @param violations
	 */
	public void violationsRemoved(Set<ViolationTracker> violations) {
		// override to handle removed violations
	}
	
	/**
	 * Called whenever the activity windows have changed
	 * @param windows
	 */
	public void windowsChanged(Set<ActivityWindow> windows) {
		// override to handle a change in the activity windows
	}
	
}
