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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.core.model.plan.EActivity;

import java.util.Date;
import java.util.List;

public interface IFlightRuleViolation {

	/**
	 * @return generally the time the violation starts occuring.
	 */
	public Date getStartTime();

	public String getType();

	/**
	 * @return amount by which the resource is oversubscribed.
	 */
	public double getLevel();
	
	/**
	 * 
	 * @return list of activities (etc.) that are in conflict with each other or themselves.
	 */

	public List<EActivity> getCulprits();
	
	/**
	 * Is this violation being waived?
	 * @return true if any of the culprits is waiving flight rules.
	 */
	public boolean isWaived ();

}
