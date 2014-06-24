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
package gov.nasa.ensemble.core.plan.resources.wizards;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ConditionsProvider {

	/**
	 * Returns the status of the contribution. If the 
	 * contributor is unable to determine the conditions,
	 * this is where the proper notification is sent
	 */
	public IStatus getStatus(EPlan plan) {
		return Status.OK_STATUS;
	}
	
	/**
	 * Returns the current resource conditions of the plan
	 * given the specified date.
	 * @param plan plan to investigate
	 * @param date date to determine conditions for
	 * @return the conditions object
	 */
	public Conditions getConditions(EPlan plan, Date date) throws Exception {
		return null;
	}
	
}
