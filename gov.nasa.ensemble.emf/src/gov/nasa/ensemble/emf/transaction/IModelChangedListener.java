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

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * This listener is for reacting to changes to the plan 
 * asynchronously.
 * 
 * Register implementations of this interface with the
 * ModelChangedListener extension point.
 * 
 * @author abachmann
 */
public interface IModelChangedListener {

	/** 
	 * Implement this method to post this event to your
	 * thread's queue for processing.  Be sure to call
	 * notify to wake your thread up if necessary.
	 */
	public void enqueue(ResourceSetChangeEvent event);
	
	public abstract static class Impl implements IModelChangedListener {
		
		public void activate(TransactionalEditingDomain domain) {
			// no default implementation
		}
		
	}
	
}
