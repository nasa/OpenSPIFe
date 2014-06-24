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

 import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.constraints.network.DistanceGraph.Dedge;
import gov.nasa.ensemble.core.plan.constraints.network.DistanceGraph.Dnode;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetwork.Bounds;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetwork.Scope;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.INogoodPart;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodContainment;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTemporalChain;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTemporalDistanceConstraint;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTemporalDuration;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTimepointConstraint;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TemporalNetworkMember<Time extends Long> implements IMember {

	/**
	 * 
	 */
	private static final long serialVersionUID = 799257156034675999L;
	private EPlan plan;
	private TemporalNetworkModel<Time> temporalNetworkModel;
	private TemporalNetwork<Time> temporalNetwork;

	public static TemporalNetworkMember get(EPlan plan) {
		return WrapperUtils.getMember(plan, TemporalNetworkMember.class);
	}

	public TemporalNetworkMember(EPlan plan) {
		this.plan = plan;
		PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
		Date epoch = planTemporalMember.getStartBoundary();
		if (epoch == null) {
			epoch = planTemporalMember.getStartTime();
		}
		if (epoch == null) {
			epoch = new Date();
		}
		this.temporalNetworkModel = new TemporalNetworkModel<Time>(epoch);
		this.temporalNetwork = new TemporalNetwork<Time>();
		TemporalNetworkCommandBuilder builder = new TemporalNetworkCommandBuilder(this);
		builder.initialize();
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	public EPlan getPlan() {
		return plan;
	}

	public TemporalNetwork<Time> getNetwork() {
		return temporalNetwork;
	}
	
	public TemporalNetworkModel getModel() {
		return temporalNetworkModel;
	}

	public boolean isConsistent() {
		return temporalNetwork.isConsistent();
	}
	
	public ConsistencyBounds getBounds(EPlanElement element) {
		PlanElementConstraintCache<Time> cache = temporalNetworkModel.elementToCache.get(element);
		if (cache == null) {
			return null;
		}
		if (cache.propertiesTime > temporalNetworkModel.invalidPropertiesTime) {
			return cache.consistencyBounds;
		}
		long now = System.nanoTime();
		ConsistencyBounds bounds = computeBounds(cache);
		cache.consistencyBounds = bounds;
		cache.boundsTime = now;
		return bounds;
	}

	private ConsistencyBounds computeBounds(PlanElementConstraintCache<Time> cache) {
		boolean wasFrozen = frozen;
		try {
			if (!wasFrozen) {
				freeze();
			}
			// System.out.println("compute bounds: " + cache.planElement.getName());
			Date earliestStart = getEarliestDate(cache.start);
			Date latestStart = getLatestDate(cache.start);
			Date earliestEnd = getEarliestDate(cache.end);
			Date latestEnd = getLatestDate(cache.end);
			// System.out.println("[ " + earliestStart + ", " + latestEnd + "]");
			return new ConsistencyBounds(earliestStart, latestStart, earliestEnd, latestEnd);
		} finally {
			if (!wasFrozen) {
				thaw();
			}
		}
	}

	private Date getEarliestDate(TemporalNetwork<Time>.Timepoint timepoint) {
		Time lower = temporalNetwork.getLowerTimepointBound(timepoint);
		return temporalNetworkModel.convertNetworkToEarliestDate(lower);
	}

	private Date getLatestDate(TemporalNetwork<Time>.Timepoint timepoint) {
		Time lower = temporalNetwork.getUpperTimepointBound(timepoint);
		return temporalNetworkModel.convertNetworkToLatestDate(lower);
	}
	
	public void invalidateModel() {
		boolean consistent = temporalNetwork.isConsistent();
		temporalNetworkModel.invalidPropertiesTime = System.nanoTime();
		if (consistent) {
//			updateSuggestedStartTimes();
		} else {
			Logger logger = Logger.getLogger(TemporalNetworkMember.class);
			List<Dedge> nogoodList = temporalNetwork.getEdgeNogoodList();
			for (Dedge nogood : nogoodList) {
				logger.debug(nogood);
			}
		}
	}

//	private static class CursorSuggestions {
//		public Date cursor = null;
//		public Date groupStart = null;
//	}
//	
//	private void updateSuggestedStartTimes() {
//		PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
//		Date planStart = planTemporalMember.getStartBoundary();
//		if (planStart == null) {
//			planStart = planTemporalMember.getStartTime();
//		}
//		if (planStart == null) {
//			return;
//		}
//		CursorSuggestions suggestions = new CursorSuggestions();
//		suggestions.cursor = planStart;
//		for (EPlanChild child : plan.getChildren()) {
//			if (child instanceof EActivity) {
//				EActivity activity = (EActivity) child;
//				updateActivitySuggestion(suggestions, activity);
//			} else if (child instanceof EActivityGroup) {
//				EActivityGroup group = (EActivityGroup) child;
//				updateGroupSuggestion(suggestions, group);
//			}
//		}
//	}
//	
//	private void updateGroupSuggestion(CursorSuggestions parentSuggestions, EActivityGroup parent) {
//		CursorSuggestions suggestions = new CursorSuggestions();
//		suggestions.cursor = parentSuggestions.cursor;
//		for (EPlanChild child : parent.getChildren()) {
//			if (child instanceof EActivity) {
//				EActivity activity = (EActivity) child;
//				updateActivitySuggestion(suggestions, activity);
//			} else if (child instanceof EActivityGroup) {
//				EActivityGroup group = (EActivityGroup) child;
//				updateGroupSuggestion(suggestions, group);
//			}
//			if ((parentSuggestions.groupStart == null) 
//				|| ((suggestions.groupStart != null) && suggestions.groupStart.before(parentSuggestions.groupStart))) {
//				parentSuggestions.groupStart = suggestions.groupStart;
//			}
//		}
//		if (suggestions.groupStart != null) {
//			TemporalMember temporalMember = parent.getMember(TemporalMember.class);
//			if (!CommonUtils.equals(temporalMember.getSuggestedStartTime(), suggestions.groupStart)) {
//				temporalMember.setSuggestedStartTime(suggestions.groupStart);
//			}
//		}
//		parentSuggestions.cursor = suggestions.cursor;
//	}
//	
//	private void updateActivitySuggestion(CursorSuggestions suggestions, EActivity activity) {
//	    ConsistencyBounds bounds = getBounds(activity);
//	    if (bounds != null) {
//	    	Date earliest = bounds.getEarliestStart();
//	    	Date latest = bounds.getLatestStart();
//	    	Date suggestion = DateUtils.bind(suggestions.cursor, earliest, latest);
//	    	TemporalMember temporalMember = activity.getMember(TemporalMember.class);
//	    	if (!CommonUtils.equals(temporalMember.getSuggestedStartTime(), suggestion)) {
//	    		temporalMember.setSuggestedStartTime(suggestion);
//	    	}
//	    	Date end = temporalMember.getEndTime();
//	    	Date startTime = temporalMember.getStartTime();
//	    	if (!CommonUtils.equals(suggestion, startTime) 
//	    		&& temporalMember.getUserStartTime() == null) {
//	    		startTime = suggestion;
//	    		Amount<Duration> duration = temporalMember.getDuration();
//	    		if (duration == null) {
//	    			end = suggestion;
//	    		} else {
//	    			end = gov.nasa.ensemble.core.jscience.util.DateUtils.add(suggestion, duration);
//	    		}
//	    	}
//	    	if ((suggestions.groupStart == null) 
//	    		|| ((startTime != null) && startTime.before(suggestions.groupStart))) {
//	    		suggestions.groupStart = startTime;
//	    	}
//	    	if (end != null) {
//	    		suggestions.cursor = end;
//	    	} else {
//	    		suggestions.cursor = suggestion;
//	    	}
//	    }
//    }
	public static class NogoodParticipants {
		public final List<PeriodicTemporalConstraint> periodicConstraints;
		public final List<BinaryTemporalConstraint> binaryConstraints;
		public final List<TemporalChain> chains;
		public final List<EPlanElement> elements;
		public NogoodParticipants(
				List<PeriodicTemporalConstraint> periodicConstraints,
				List<BinaryTemporalConstraint> binaryConstraints,
				List<TemporalChain> chains, List<EPlanElement> elements) {
			this.periodicConstraints = Collections.unmodifiableList(periodicConstraints);
			this.binaryConstraints = Collections.unmodifiableList(binaryConstraints);
			this.chains = Collections.unmodifiableList(chains);
			this.elements = Collections.unmodifiableList(elements);
		}
	}
	
	/**
	 * Returns a list of parts participating in the nogood.
	 * @return
	 */
	public Set<INogoodPart> getNogoodParts() {
		List<Dedge> nogood = new ArrayList<Dedge>(temporalNetwork.getEdgeNogoodList());
		Set<INogoodPart> nogoodParts = new LinkedHashSet<INogoodPart>();
		gatherPeriodicNogoodParts(nogood, nogoodParts);
		gatherBinaryNogoodParts(nogood, nogoodParts);
		gatherChainNogoodParts(nogood, nogoodParts);
		getElementNogoodParts(nogood, nogoodParts);
		return nogoodParts;
	}

	/**
	 * Add nogood parts related to periodic temporal constraints into the argument nogoodParts.
	 * @param nogood
	 * @param nogoodParts
	 */
	private void gatherPeriodicNogoodParts(List<Dedge> nogood, Set<INogoodPart> nogoodParts) {
		for (Map.Entry<PeriodicTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint> entry : temporalNetworkModel.timepointConstraintToTemporalConstraint.entrySet()) {
			PeriodicTemporalConstraint modelConstraint = entry.getKey();
			TemporalNetwork<Time>.TemporalConstraint networkConstraint = entry.getValue();
			if (matches(networkConstraint, nogood)) {
				nogoodParts.add(new NogoodTimepointConstraint(modelConstraint));
			}
		}
	}

	/**
	 * Add nogood parts related to binary temporal constraints into the argument nogoodParts.
	 * @param nogood
	 * @param nogoodParts
	 */
	private void gatherBinaryNogoodParts(List<Dedge> nogood, Set<INogoodPart> nogoodParts) {
		for (Map.Entry<BinaryTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint> entry : temporalNetworkModel.distanceConstraintToTemporalConstraint.entrySet()) {
			BinaryTemporalConstraint modelConstraint = entry.getKey();
			TemporalNetwork<Time>.TemporalConstraint networkConstraint = entry.getValue();
			if (matches(networkConstraint, nogood)) {
				nogoodParts.add(new NogoodTemporalDistanceConstraint(modelConstraint));
			}
		}
	}

	/**
	 * Add nogood parts related to temporal chains into the argument nogoodParts.
	 * @param nogood
	 * @param nogoodParts
	 */
	private void gatherChainNogoodParts(List<Dedge> nogood, Set<INogoodPart> nogoodParts) {
		for (Map.Entry<TemporalChain, Set<TemporalNetwork<Time>.TemporalConstraint>> entry : temporalNetworkModel.chainToConstraints.entrySet()) {
			TemporalChain chain = entry.getKey();
			Set<TemporalNetwork<Time>.TemporalConstraint> networkConstraints = entry.getValue();
			List<EPlanElement> chainElements = chain.getElements();
			int i = 0;
			List<List<EPlanElement>> participants = new ArrayList<List<EPlanElement>>();
			List<EPlanElement> current = new ArrayList<EPlanElement>();
			for (TemporalNetwork<Time>.TemporalConstraint networkConstraint : networkConstraints) {
				if (matches(networkConstraint, nogood)) {
					EPlanElement element = chainElements.get(i);
					current.add(element);
				} else if (!current.isEmpty()) {
					EPlanElement element = chainElements.get(i);
					current.add(element);
					participants.add(current);
					current = new ArrayList<EPlanElement>(); // don't use clear here!
				}
				i++;
			}
			if (!current.isEmpty()) {
				EPlanElement element = chainElements.get(i);
				current.add(element);
				participants.add(current);
			}
			for (List<EPlanElement> list : participants) {
				nogoodParts.add(new NogoodTemporalChain(chain, list));
			}
		}
	}

	/**
	 * Add nogood parts related to the duration of elements, or their containment,
	 * into the argument nogoodParts.
	 * 
	 * @param nogood
	 * @param nogoodParts
	 */
	private void getElementNogoodParts(List<Dedge> nogood, Set<INogoodPart> nogoodParts) {
		Collection<PlanElementConstraintCache<Time>> caches = temporalNetworkModel.elementToCache.values();
		for (PlanElementConstraintCache<Time> cache : caches) {
			EPlanElement element = cache.planElement;
			if (matches(cache.durationConstraint, nogood)) {
				nogoodParts.add(new NogoodTemporalDuration(element));
				continue;
			}
			if (matches(cache.parentStartConstraint, nogood)) {
				nogoodParts.add(new NogoodContainment(element, Timepoint.START));
				continue;
			}
			if (matches(cache.parentEndConstraint, nogood)) {
				nogoodParts.add(new NogoodContainment(element, Timepoint.END));
				continue;
			}
			// Every case should have been covered already so
			// code like the following *should* be unnecessary?
//			TemporalNetwork<Time>.Timepoint start = cache.start;
//			TemporalNetwork<Time>.Timepoint end = cache.end;
//			for (Dedge edge : nogood) {
//				if ((start == edge.from) || (start == edge.to)
//					|| (end == edge.from) || (end == edge.to)) {
//					nogoodParts.add(new INogoodPart() {
//						@Override
//						public String toString() {
//							return String.valueOf(element.getName());
//						}
//					});
//				}
//			}
		}
	}

	/**
	 * Returns true if this network constraint participates in the nogood.
	 * @param networkConstraint
	 * @param nogood
	 * @return
	 */
	private boolean matches(TemporalNetwork<Time>.TemporalConstraint networkConstraint, List<Dedge> nogood) {
		if (networkConstraint == null) {
			return false;
		}
		Bounds bounds = networkConstraint.getBounds();
		Scope scope = temporalNetwork.getConstraintScope(networkConstraint);
		for (Dedge edge : nogood) {
			Dnode from = edge.from;
			Dnode to = edge.to;
			if ((from == scope.source) && (to == scope.target)) {
				if (bounds.upperBound == edge.length) {
					return true;
				}
			}
			if ((to == scope.source) && (from == scope.target)) {
				if (-bounds.lowerBound == edge.length) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ConsistencyProperties getProperties(EPlanElement element) {
		PlanElementConstraintCache<Time> elementCache = temporalNetworkModel.elementToCache.get(element);
		if (elementCache == null) {
			return null;
		}
		if (elementCache.propertiesTime > temporalNetworkModel.invalidPropertiesTime) {
			return elementCache.consistencyProperties;
		}
		long now = System.nanoTime();
		ConsistencyProperties properties = computeProperties(elementCache);
		elementCache.consistencyProperties = properties;
		elementCache.propertiesTime = now;
		return properties;
	}

	private ConsistencyProperties computeProperties(PlanElementConstraintCache<Time> elementCache) {
		TemporalNetwork<Time>.TemporalConstraint durationConstraint = null;
		try {
			freeze();
			durationConstraint = elementCache.durationConstraint;
			if (durationConstraint != null) {
				temporalNetwork.removeTemporalConstraint(durationConstraint);
			}
			EPlanElement element = elementCache.planElement;
			// System.out.println("compute properties: " + element.getName());
			Vector<TemporalNetwork<Time>.Timepoint> affectedTimepoints = new Vector<TemporalNetwork<Time>.Timepoint>();
			Collection<PlanElementConstraintCache<Time>> allCaches = temporalNetworkModel.elementToCache.values();
			List<PlanElementConstraintCache<Time>> caches = new ArrayList<PlanElementConstraintCache<Time>>(allCaches.size() - 1);
			for (PlanElementConstraintCache<Time> cache : allCaches) {
				if (cache.planElement != element) {
					caches.add(cache);
					affectedTimepoints.add(cache.start); // START then END order has to match convertToConsistencyConstraints
					affectedTimepoints.add(cache.end);
				}
			}
			Vector<Time> lbs = new Vector<Time>();
			Vector<Time> ubs = new Vector<Time>();
			Set<ConsistencyConstraint> constraints = new HashSet<ConsistencyConstraint>();
			temporalNetwork.calcDistanceBounds(elementCache.start, affectedTimepoints, lbs, ubs);
			convertToConsistencyConstraints(element, Timepoint.START, caches, lbs, ubs, constraints);
			// lbs and ubs cleared by calcDistanceBounds, so no need to clear them here
			temporalNetwork.calcDistanceBounds(elementCache.end, affectedTimepoints, lbs, ubs);
			convertToConsistencyConstraints(element, Timepoint.END, caches, lbs, ubs, constraints);
			ConsistencyBounds bounds = getBounds(element);
			return new ConsistencyProperties(bounds, constraints);
		} finally {
			if (durationConstraint != null) {
				TemporalNetwork<Time>.Bounds bounds = durationConstraint.getBounds();
				elementCache.durationConstraint = temporalNetwork.addTemporalConstraint(
					durationConstraint.getSource(), 
					durationConstraint.getTarget(),
					bounds.lowerBound,
					bounds.upperBound);
			}
			thaw();
		}
	}

	private void convertToConsistencyConstraints(EPlanElement element, Timepoint timepoint,
			List<PlanElementConstraintCache<Time>> caches, Vector<Time> lbs, Vector<Time> ubs, Set<ConsistencyConstraint> constraints) {
		Iterator<Time> lowerIterator = lbs.iterator();
		Iterator<Time> upperIterator = ubs.iterator();
		for (PlanElementConstraintCache<Time> cache : caches) {
			EPlanElement affectedElement = cache.planElement;
			// START then END order has to match computeProperties
			Time startLower = lowerIterator.next();
			Time startUpper = upperIterator.next();
			convertConstraint(constraints, element, timepoint, affectedElement, Timepoint.START, startLower, startUpper);
			Time endLower = lowerIterator.next();
			Time endUpper = upperIterator.next();
			convertConstraint(constraints, element, timepoint, affectedElement, Timepoint.END, endLower, endUpper);
		}
	}

	private void convertConstraint(Set<ConsistencyConstraint> constraints, EPlanElement element, Timepoint timepoint, 
			EPlanElement affectedElement, Timepoint affectedTimepoint, Time lower, Time upper) {
		boolean lowerUnbounded = (lower <= DistanceGraph.NEG_INFINITY);
		boolean upperUnbounded = (upper >= DistanceGraph.POS_INFINITY);
		if (lowerUnbounded && upperUnbounded) {
			return; // not constraining
		}
		Amount<Duration> min = AmountUtils.toAmount(TemporalNetworkModel.convertNetworkToTimeDistance(lower), DateUtils.MILLISECONDS);
		Amount<Duration> max = AmountUtils.toAmount(TemporalNetworkModel.convertNetworkToTimeDistance(upper), DateUtils.MILLISECONDS);
		constraints.add(new ConsistencyConstraint(element, timepoint, affectedElement, affectedTimepoint, min, max));
	}

	private boolean freezing = true;
	private boolean thick = false;
	private boolean frozen = false;
	private Freezer freezer;
	
	/**
	 * Adding "freezing" constraints.
	 */
	private void freeze() {
		if (freezing) {
			EPlan plan = getPlan();
			freezer = (thick ? new FreezeThick<Time>(plan, temporalNetwork, temporalNetworkModel) 
							 : new FreezeLight<Time>(plan, temporalNetwork, temporalNetworkModel));
			freezer.freeze();
			frozen = true;
		}
	}

	/**
	 * Removing "freezing" constraints.
	 */
	private void thaw() {
		if (freezing) {
			freezer.thaw();
			frozen = false;
		}
	}


}
