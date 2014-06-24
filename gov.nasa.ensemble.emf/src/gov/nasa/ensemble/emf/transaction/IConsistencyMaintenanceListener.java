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
package gov.nasa.ensemble.emf.transaction;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

public interface IConsistencyMaintenanceListener {

	/**
	 * Return a command to maintain the internal consistency of the domain with
	 * respect to the changes in the ResourceSetChangeEvent.  May return null
	 * if nothing needs to be changed.
	 * 
	 * @param event
	 * @return Command
	 */
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event);
	
}
