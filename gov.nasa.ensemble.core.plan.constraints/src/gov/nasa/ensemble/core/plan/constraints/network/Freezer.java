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

import gov.nasa.ensemble.core.model.plan.EPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * This class adds freezing constraints,
 * keeps track of them, and enables removing them.
 * 
 * @author abachman
 */
abstract class Freezer<Time extends Long> {

	private final EPlan plan;
	private final TemporalNetwork<Time> temporalNetwork;
	private final List<TemporalNetwork<Time>.TemporalConstraint> freezingConstraints = new ArrayList<TemporalNetwork<Time>.TemporalConstraint>();
	boolean frozen = false;
	boolean thawed = false;

	public Freezer(EPlan plan, TemporalNetwork<Time> temporalNetwork) {
		this.plan = plan;
		this.temporalNetwork = temporalNetwork;
	}

	public final void freeze() {
		if (frozen) {
			throw new IllegalStateException("freeze should be called only once");
		}
		frozen = true;
		freeze(plan);
	}

	protected abstract void freeze(EPlan plan);

	/**
	 * Freeze the relationship between source and target so that they are always separate by distance
	 * 
	 * @param source
	 * @param target
	 * @param distance
	 */
	protected final void addConstraint(TemporalNetwork<Time>.Timepoint source, TemporalNetwork<Time>.Timepoint target, Time distance) {
		Vector<TemporalNetwork<Time>.Timepoint> targs = new Vector<TemporalNetwork<Time>.Timepoint>(Collections.singletonList(target));
		Vector<Time> lower = new Vector<Time>();
		Vector<Time> upper = new Vector<Time>();
		// Resist the temptation to use temporalNetwork.calcDistanceBounds(source, target);
		// which apparently does not have the same results :-(
		temporalNetwork.calcDistanceBounds(source, targs, lower, upper);
		Time l = lower.get(0);
		Time u = upper.get(0);
		if (distance < l) {
			distance = l;
		} else if (distance > u) {
			distance = u;
		}
		checkNewConstraint(temporalNetwork.addTemporalConstraint(source, target, distance, distance));
	}

	/**
	 * Add a bounds constraint to the timepoint
	 * 
	 * @param timepoint
	 * @param earliest
	 * @param latest
	 */
	protected final void addBound(TemporalNetwork<Time>.Timepoint timepoint, Time earliest, Time latest) {
		TemporalNetwork<Time>.Timepoint origin = temporalNetwork.getOrigin();
		checkNewConstraint(temporalNetwork.addTemporalConstraint(origin, timepoint, earliest, latest));
	}

	private void checkNewConstraint(TemporalNetwork<Time>.TemporalConstraint constraint) {
		if (!temporalNetwork.isConsistent()) {
			temporalNetwork.removeTemporalConstraint(constraint);
		} else {
			freezingConstraints.add(constraint);
		}
	}

	public final void thaw() {
		if (thawed) {
			throw new IllegalStateException("thaw should be called only once");
		}
		thawed = true;
		for (TemporalNetwork<Time>.TemporalConstraint constraint : freezingConstraints) {
			if (constraint.isValid()) {
				temporalNetwork.removeTemporalConstraint(constraint);
			}
		}
	}
	
}
