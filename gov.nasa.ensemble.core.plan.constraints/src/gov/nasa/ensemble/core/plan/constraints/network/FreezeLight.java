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

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.emf.common.util.EList;

/**
 * This version freezes only those children that can be guaranteed to not over-restrict the solution.
 * In particular:
 * 
 * 1. If any child can be proven to start at least as early as all the other children,
 *    then that child will be frozen to the start of its parent.
 * 2. If any child can be proven to end at least as late as all the other children,
 *    then that child will be frozen to the end of its parent.
 *
 * The above two properties imply that single children will be completely frozen to their parent bounds.
 * 
 * @author abachman
 */
public class FreezeLight<Time extends Long> extends Freezer<Time> {

	@SuppressWarnings("unchecked")
	private final Time POS_INFINITY = (Time)(Long)DistanceGraph.POS_INFINITY;
	@SuppressWarnings("unchecked")
	private final Time NEG_INFINITY = (Time)(Long)DistanceGraph.NEG_INFINITY;
	
	private final TemporalNetwork<Time> temporalNetwork;
	private final TemporalNetworkModel<Time> temporalNetworkModel;
	private final Time zeroBound;
	private final Map<EActivityGroup, Time> groupStarts = new LinkedHashMap<EActivityGroup, Time>();
	private final Map<EActivityGroup, Time> groupEnds = new LinkedHashMap<EActivityGroup, Time>();

	public FreezeLight(EPlan plan, TemporalNetwork<Time> temporalNetwork, TemporalNetworkModel<Time> temporalNetworkModel) {
		super(plan, temporalNetwork);
		this.temporalNetwork = temporalNetwork;
		this.temporalNetworkModel = temporalNetworkModel;
		this.zeroBound = temporalNetworkModel.convertTimeDistanceToNetwork(DateUtils.ZERO_DURATION);
	}

	@Override
	protected void freeze(EPlan plan) {
		final List<EActivityGroup> unresolvedStarts = new ArrayList<EActivityGroup>();
		final List<EActivityGroup> unresolvedEnds = new ArrayList<EActivityGroup>();
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivityGroup) {
					EActivityGroup group = (EActivityGroup) element;
					if (!group.getChildren().isEmpty()) {
						if (!freezeStart(group)) {
							unresolvedStarts.add(group);
						}
						if (!freezeEnd(group)) {
							unresolvedEnds.add(group);
						}
						PlanElementConstraintCache<Time> groupCache = temporalNetworkModel.elementToCache.get(group);
						groupStarts.put(group, temporalNetwork.getLowerTimepointBound(groupCache.start));
						groupEnds.put(group, temporalNetwork.getUpperTimepointBound(groupCache.end));
					}
				}
			}
		};
		visitor.visitAll(plan);
		iterate(plan, unresolvedStarts, unresolvedEnds);
	}

	/**
	 * Go through groups that still haven't frozen their start/end,
	 * and if any can be frozen, keep trying.  (since freezing one group
	 * may constrain some activity to be in front of another)
	 * Similarly, try to constrain groups, and if any can be improved,
	 * keep trying.
	 *  
	 * @param plan 
	 * @param starts
	 * @param ends
	 */
	private void iterate(EPlan plan, List<EActivityGroup> starts, List<EActivityGroup> ends) {
		final List<EActivityGroup> unresolvedStarts = new ArrayList<EActivityGroup>();
		final List<EActivityGroup> unresolvedEnds = new ArrayList<EActivityGroup>();
		for (EActivityGroup group : starts) {
			if (!freezeStart(group)) {
				unresolvedStarts.add(group);
			}
		}
		for (EActivityGroup group : ends) {
			if (!freezeEnd(group)) {
				unresolvedEnds.add(group);
			}
		}
		boolean tighter = constrainGroups(plan);
		if ((unresolvedStarts.size() < starts.size())
			|| (unresolvedEnds.size() < ends.size())
			|| tighter) {
			iterate(plan, unresolvedStarts, unresolvedEnds);
		}
	}

	/**
	 * Try to constrain groups in the plan by inferring
	 * constraints on them from their children.
	 * 
	 * @param plan
	 * @return true if any of the groups was further constrained
	 */
	private boolean constrainGroups(EPlan plan) {
		final boolean[] result = new boolean[1];
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivityGroup) {
					EActivityGroup group = (EActivityGroup) element;
					if (!group.getChildren().isEmpty()) {
						result[0] |= constrainDuration(group);
						result[0] |= constrainBounds(group);
					}
				}
			}
		};
		visitor.visitAll(plan);
		return result[0];
	}

	/**
	 * If the children of a group can not be arbitrarily separated in distance,
	 * then this places an upper bound on the duration of the group,
	 * so add a constraint to express that upper bound.
	 * 
	 * @param group
	 * @return true if the group duration is smaller than previously
	 */
	private boolean constrainDuration(EActivityGroup group) {
		// TODO
		return false;
	}

	/**
	 * If all children of a group must start at/after a specific time,
	 * then add a constraint to the group that it must also start at/after that time.
	 * 
	 * If all children of a group must end at/before a specific time,
	 * then add a constraint to the group that it must also end at/before that time.
	 * 
	 * @return true if the group bounds are tighter than previously
	 */
	private boolean constrainBounds(EActivityGroup group) {
		Time earliestChildTime = POS_INFINITY; 
		Time latestChildTime = NEG_INFINITY;
		for (EPlanChild child : group.getChildren()) {
			PlanElementConstraintCache<Time> childCache = temporalNetworkModel.elementToCache.get(child);
			Time childMin = temporalNetwork.getLowerTimepointBound(childCache.start);
			if (childMin < earliestChildTime) {
				earliestChildTime = childMin;
			}
			Time childMax = temporalNetwork.getUpperTimepointBound(childCache.end);
			if (childMax > latestChildTime) {
				latestChildTime = childMax;
			}
			if (earliestChildTime.equals(NEG_INFINITY) && latestChildTime.equals(POS_INFINITY)) {
				// the children can be anywhere, so no constraint is possible
				return false;
			}
		}
		PlanElementConstraintCache<Time> groupCache = temporalNetworkModel.elementToCache.get(group);
		boolean tightened = false;
		Time groupMin = groupStarts.get(group);
		if (earliestChildTime > groupMin) {
			Time endOfTime = temporalNetworkModel.convertMaxTimeDistanceToNetwork(null);
			addBound(groupCache.start, earliestChildTime, endOfTime);
			groupStarts.put(group,  earliestChildTime);
			tightened = true;
		}
		Time groupMax = groupEnds.get(group);
		if (latestChildTime < groupMax) {
			Time startOfTime = temporalNetworkModel.convertMinTimeDistanceToNetwork(null);
			addBound(groupCache.end, startOfTime, latestChildTime);
			groupEnds.put(group, latestChildTime);
			tightened = true;
		}
		return tightened;
	}

	/**
	 * Find a child that must start before or at the same time as all other children,
	 * and freeze its start to the group's start.  If no such child exists, return false.
	 * 
	 * @param group
	 * @return true if a child with the property was found, false if not
	 */
	private boolean freezeStart(EActivityGroup group) {
		EList<EPlanChild> children = group.getChildren();
		nextChild: for (EPlanChild source : children) {
			PlanElementConstraintCache<Time> sourceCache = temporalNetworkModel.elementToCache.get(source);
			Vector<TemporalNetwork<Time>.Timepoint> affectedTimepoints = new Vector<TemporalNetwork<Time>.Timepoint>();
			for (EPlanChild child : children) {
				if (child != source) {
					PlanElementConstraintCache<Time> childCache = temporalNetworkModel.elementToCache.get(child);
					affectedTimepoints.add(childCache.start);
				}
			}
			Vector<Time> lbs = new Vector<Time>();
			Vector<Time> ubs = new Vector<Time>();
			temporalNetwork.calcDistanceBounds(sourceCache.start, affectedTimepoints, lbs, ubs);
			for (Time time : lbs) {
				long lb = TemporalNetworkModel.<Time>convertNetworkToTimeDistance(time);
				if (lb < 0) {
					// some other element can come before the source
					continue nextChild;
				}
			}
			PlanElementConstraintCache<Time> groupCache = temporalNetworkModel.elementToCache.get(group);
			addConstraint(sourceCache.start, groupCache.start, zeroBound);
			return true;
		}
		return false;
	}

	/**
	 * Find a child that must end after or at the same time as all other children
	 * and freeze its end to the group's end.  If no such child exists, return false.
	 * 
	 * @param group
	 * @return true if a child with the property was found, false if not
	 */
	private boolean freezeEnd(EActivityGroup group) {
		EList<EPlanChild> children = group.getChildren();
		nextChild: for (EPlanChild source : children) {
			PlanElementConstraintCache<Time> sourceCache = temporalNetworkModel.elementToCache.get(source);
			Vector<TemporalNetwork<Time>.Timepoint> affectedTimepoints = new Vector<TemporalNetwork<Time>.Timepoint>();
			for (EPlanChild child : children) {
				if (child != source) {
					PlanElementConstraintCache<Time> childCache = temporalNetworkModel.elementToCache.get(child);
					affectedTimepoints.add(childCache.end);
				}
			}
			Vector<Time> lbs = new Vector<Time>();
			Vector<Time> ubs = new Vector<Time>();
			temporalNetwork.calcDistanceBounds(sourceCache.end, affectedTimepoints, lbs, ubs);
			for (Time time : ubs) {
				long ub = TemporalNetworkModel.<Time>convertNetworkToTimeDistance(time);
				if (ub > 0) {
					// some other element can come after the source
					continue nextChild;
				}
			}
			PlanElementConstraintCache<Time> groupCache = temporalNetworkModel.elementToCache.get(group);
			addConstraint(sourceCache.end, groupCache.end, zeroBound);
			return true;
		}
		return false;
	}
	
}
