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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;

import java.util.HashSet;
import java.util.Set;

public class PlanElementConstraintCache<Time extends Long> {
	public final EPlanElement planElement;
	public final TemporalNetwork<Time>.Timepoint start;
	public final TemporalNetwork<Time>.Timepoint end;
	public TemporalNetwork<Time>.TemporalConstraint durationConstraint;
	public TemporalNetwork<Time>.TemporalConstraint parentStartConstraint;
	public TemporalNetwork<Time>.TemporalConstraint parentEndConstraint;
	public final Set<PeriodicTemporalConstraint> bounds;
	public final Set<BinaryTemporalConstraint> relations;
	public long propertiesTime = Long.MIN_VALUE;
	public long boundsTime = Long.MIN_VALUE;
	public ConsistencyProperties consistencyProperties = ConsistencyProperties.EMPTY_PROPERTIES;
	public ConsistencyBounds consistencyBounds = ConsistencyBounds.EMPTY_BOUNDS;
	public PlanElementConstraintCache(EPlanElement planElement,
			TemporalNetwork<Time>.Timepoint start, TemporalNetwork<Time>.Timepoint end, 
			TemporalNetwork<Time>.TemporalConstraint durationConstraint, 
			TemporalNetwork<Time>.TemporalConstraint parentStartConstraint,
			TemporalNetwork<Time>.TemporalConstraint parentEndConstraint) {
		this.planElement = planElement;
		this.start = start;
		this.end = end;
		this.durationConstraint = durationConstraint;
		this.parentStartConstraint = parentStartConstraint;
		this.parentEndConstraint = parentEndConstraint;
		this.bounds = new HashSet<PeriodicTemporalConstraint>();
		this.relations = new HashSet<BinaryTemporalConstraint>();
	} 
}
