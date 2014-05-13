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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.core.model.plan.EPlanElement;

public interface IPlanConstraintInfo {

	/**
	 * Returns the consistency properties that express:
	 * 1. the ways it is consistent to modify this node
	 * 2. the ways other nodes are affected when this node is modified
	 * 
	 * Must not return null.  Implementors should return 
	 * ConsistencyProperties.EMPTY_PROPERTIES instead.
	 * @param element The node being modified
	 * @return the consistency properties
	 */
	public ConsistencyProperties getConstraintProperties(EPlanElement element);

	/**
	 * Returns the implied bounds on a particular node.
	 * Note: this might be more efficient than calling getConstraintProperties
	 * and getting the bounds from the returned ConsistencyProperties object.
	 * The bounds in the returned ConsistencyProperties object should be the
	 * same as those returned here. 
	 * 
	 * Must not return null.  Implementors should return 
	 * ConsistencyBounds.EMPTY_PROPERTIES instead.
	 * @param element The node being modified
	 * @return the implied bounds on a particular node.
	 */
	public ConsistencyBounds getBounds(EPlanElement element);

}
