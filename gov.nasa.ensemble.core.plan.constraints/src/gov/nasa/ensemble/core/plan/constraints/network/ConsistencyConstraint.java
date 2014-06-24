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
import gov.nasa.ensemble.emf.model.common.Timepoint;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

/**
 * Given: 
 * source = sourceTimepoint of sourceElement
 * affected = affectedTimepoint of affectedElement
 * 
 * This constraint asserts the following: (these are all mathematically equivalent)
 * 
 * 1. source + earliestDistance <= affected <= source + latestDistance
 * 2. affected - latestDistance <= source <= affected - earliestDistance
 * 3. earliestDistance <= affected - source <= latestDistance
 * 
 * @author abachmann
 *
 */
public class ConsistencyConstraint {
	public final EPlanElement sourceElement;
	public final Timepoint sourceTimepoint;
	public final EPlanElement affectedElement;
	public final Timepoint affectedTimepoint;
	public final Amount<Duration> minimumDistance;
	public final Amount<Duration> maximumDistance;
	public ConsistencyConstraint(EPlanElement sourceElement, Timepoint sourceTimepoint, 
			EPlanElement affectedElement, Timepoint affectedTimepoint,
			Amount<Duration> minimumDistance, Amount<Duration> maximumDistance) {
		this.sourceElement = sourceElement;
		this.sourceTimepoint = sourceTimepoint;
		this.affectedElement = affectedElement;
		this.affectedTimepoint = affectedTimepoint;
		this.minimumDistance = minimumDistance;
		this.maximumDistance = maximumDistance;			
	}
}
