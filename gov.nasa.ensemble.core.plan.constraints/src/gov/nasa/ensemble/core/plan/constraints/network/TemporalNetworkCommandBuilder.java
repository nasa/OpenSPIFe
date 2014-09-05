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

import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetwork.Bounds;
import gov.nasa.ensemble.core.plan.constraints.ui.preference.PlanConstraintsPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

/* package */class TemporalNetworkCommandBuilder<Time extends Long> {

	private final Map<EPlanElement, Set<EPlanElement>> elementsAdded = new AutoSetMap<EPlanElement, EPlanElement>(EPlanElement.class);
	private final Map<EPlanElement, Set<EPlanElement>> elementsRemoved = new AutoSetMap<EPlanElement, EPlanElement>(EPlanElement.class);

	private final Map<EPlanChild, Boolean> childrenToOldScheduledness = new LinkedHashMap<EPlanChild, Boolean>();
	private final Map<EPlanChild, Date> childrenToOldStarts = new LinkedHashMap<EPlanChild, Date>();
	private final Map<EPlanChild, Date> childrenToOldEnds = new LinkedHashMap<EPlanChild, Date>();
	private final Map<EPlanChild, Amount<Duration>> childrenToOldDurations = new LinkedHashMap<EPlanChild, Amount<Duration>>();

	private final Set<PeriodicTemporalConstraint> periodicConstraintsAffected = new LinkedHashSet<PeriodicTemporalConstraint>();
	private final Set<BinaryTemporalConstraint> binaryConstraintsAffected = new LinkedHashSet<BinaryTemporalConstraint>();
	private final Set<TemporalConstraint> anchorConstraintsChanged = new LinkedHashSet<TemporalConstraint>();
	private final Set<TemporalChain> temporalChainsAffected = new LinkedHashSet<TemporalChain>();

	private final EPlan plan;
	private final TemporalNetworkMember<Time> member;
	private TemporalNetwork<Time> network;
	private TemporalNetworkModel<Time> model;

	@SuppressWarnings("unchecked")
	public TemporalNetworkCommandBuilder(TemporalNetworkMember member) {
		this.plan = member.getPlan();
		this.member = member;
		this.network = member.getNetwork();
		this.model = member.getModel();
	}

	/**
	 * Should be called only from the TemporalNetworkMember constructor
	 */
	/* package */void initialize() {
		addElements(plan);
		member.invalidateModel();
	}

	public void childrenAdded(EPlanElement parent, List<EPlanElement> childrenAdded) {
		elementsAdded.get(parent).addAll(childrenAdded);
	}

	public void childrenRemoved(EPlanElement parent, List<EPlanElement> childrenRemoved) {
		elementsRemoved.get(parent).addAll(childrenRemoved);
	}

	public void schedulednessChanged(EPlanChild child, Boolean oldValue, Boolean newValue) {
		if (!childrenToOldScheduledness.containsKey(child)) {
			childrenToOldScheduledness.put(child, oldValue);
		}
	}

	public void startTimeChanged(EPlanChild child, Date oldValue) {
		if (!childrenToOldStarts.containsKey(child)) {
			childrenToOldStarts.put(child, oldValue);
		}
	}

	public void durationChanged(EPlanChild child, Amount<Duration> oldValue) {
		if (!childrenToOldDurations.containsKey(child)) {
			childrenToOldDurations.put(child, oldValue);
		}
	}

	public void endTimeChanged(EPlanChild child, Date oldValue) {
		if (!childrenToOldEnds.containsKey(child)) {
			childrenToOldEnds.put(child, oldValue);
		}
	}

	public void periodicConstraintsChanged(EPlanElement element, List<PeriodicTemporalConstraint> constraintsRemoved, List<PeriodicTemporalConstraint> constraintsAdded) {
		periodicConstraintsAffected.addAll(constraintsRemoved);
		periodicConstraintsAffected.addAll(constraintsAdded);
	}

	public void binaryConstraintsChanged(EPlanElement element, List<BinaryTemporalConstraint> constraintsRemoved, List<BinaryTemporalConstraint> constraintsAdded) {
		binaryConstraintsAffected.addAll(constraintsRemoved);
		binaryConstraintsAffected.addAll(constraintsAdded);
	}

	public void waiverUpdated(TemporalConstraint notifier) {
		if (notifier instanceof PeriodicTemporalConstraint) {
			periodicConstraintsAffected.add((PeriodicTemporalConstraint) notifier);
		} else if (notifier instanceof BinaryTemporalConstraint) {
			binaryConstraintsAffected.add((BinaryTemporalConstraint) notifier);
		}
	}

	public void temporalChainChanged(EPlanElement element, TemporalChain oldChain, TemporalChain newChain) {
		if (oldChain != null) {
			temporalChainsAffected.add(oldChain);
		}
		if (newChain != null) {
			temporalChainsAffected.add(newChain);
		}
	}

	public void activityParameterChanged(EPlanElement planElement, EStructuralFeature feature) {
		ConstraintsMember constraintMember = planElement.getMember(ConstraintsMember.class);
		List<BinaryTemporalConstraint> binaryTemporalConstraints = constraintMember.getBinaryTemporalConstraints();
		for (BinaryTemporalConstraint constraint : binaryTemporalConstraints) {
			ConstraintPoint pointA = constraint.getPointA();
			ConstraintPoint pointB = constraint.getPointB();
			if (ConstraintUtils.isAnchorPointForElement(pointA, planElement, feature) || ConstraintUtils.isAnchorPointForElement(pointB, planElement, feature)) {
				anchorConstraintsChanged.add(constraint);
			}
		}
		List<PeriodicTemporalConstraint> periodicTemporalConstraints = constraintMember.getPeriodicTemporalConstraints();
		for (PeriodicTemporalConstraint constraint : periodicTemporalConstraints) {
			if (ConstraintUtils.isAnchorPointForElement(constraint.getPoint(), planElement, feature)) {
				anchorConstraintsChanged.add(constraint);
			}
		}
	}

	public Command createCommand(TransactionalEditingDomain domain) {
		if (elementsAdded.isEmpty() && elementsRemoved.isEmpty() && childrenToOldScheduledness.isEmpty() && childrenToOldStarts.isEmpty() && childrenToOldDurations.isEmpty() && childrenToOldEnds.isEmpty() && periodicConstraintsAffected.isEmpty() && binaryConstraintsAffected.isEmpty() && temporalChainsAffected.isEmpty() && anchorConstraintsChanged.isEmpty()) {
			return null;
		}
		return new RecordingCommand(domain, "Temporal Network") {
			@Override
			protected void doExecute() {
				run();
			}
		};
	}

	private void run() {
		boolean invalidateModel = true; // TODO? optimize invalidation if necessary
		List<PeriodicTemporalConstraint> addedPeriodicTemporalConstraints = new ArrayList<PeriodicTemporalConstraint>();
		for (PeriodicTemporalConstraint constraint : periodicConstraintsAffected) {
			EPlanElement element = constraint.getPoint().getElement();
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
			List<PeriodicTemporalConstraint> currentConstraints = constraintsMember.getPeriodicTemporalConstraints();
			if ((constraint.getWaiverRationale() == null) && currentConstraints.contains(constraint)) {
				addedPeriodicTemporalConstraints.add(constraint);
			} else {
				temporalBoundRemoved(constraint);
			}
		}
		List<BinaryTemporalConstraint> addedBinaryTemporalConstraints = new ArrayList<BinaryTemporalConstraint>();
		for (BinaryTemporalConstraint constraint : binaryConstraintsAffected) {
			EPlanElement element = constraint.getPointA().getElement();
			ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
			List<BinaryTemporalConstraint> currentConstraints = constraintsMember.getBinaryTemporalConstraints();
			if ((constraint.getWaiverRationale() == null) && currentConstraints.contains(constraint)) {
				addedBinaryTemporalConstraints.add(constraint);
			} else {
				temporalRelationRemoved(constraint);
			}
		}
		List<TemporalChain> addedTemporalChains = new ArrayList<TemporalChain>();
		for (TemporalChain chain : temporalChainsAffected) {
			List<EPlanElement> elements = chain.getElements();
			EPlanElement representative = elements.get(0);
			ConstraintsMember constraintsMember = representative.getMember(ConstraintsMember.class, true);
			TemporalChain currentChain = constraintsMember.getChain();
			if (currentChain == chain) {
				addedTemporalChains.add(chain);
			} else {
				temporalChainRemoved(chain);
			}
		}
		for (Map.Entry<EPlanElement, Set<EPlanElement>> entry : elementsRemoved.entrySet()) {
			EPlanElement parent = entry.getKey();
			Set<EPlanElement> children = entry.getValue();
			for (EPlanElement child : children) {
				if (child.eContainer() != parent) {
					removeElements(child);
				}
			}
		}
		for (EPlanElement parent : elementsAdded.keySet()) {
			if (EPlanUtils.getPlan(parent) == plan) {
				addElements(parent);
			}
		}
		for (TemporalConstraint constraint : anchorConstraintsChanged) {
			if (constraint instanceof PeriodicTemporalConstraint) {
				PeriodicTemporalConstraint temporalBound = (PeriodicTemporalConstraint) constraint;
				temporalBoundRemoved(temporalBound);
				addedPeriodicTemporalConstraints.add(temporalBound);
			} else if (constraint instanceof BinaryTemporalConstraint) {
				BinaryTemporalConstraint temporalRelation = (BinaryTemporalConstraint) constraint;
				temporalRelationRemoved(temporalRelation);
				addedBinaryTemporalConstraints.add(temporalRelation);
			}
		}
		Set<EPlanChild> children = new LinkedHashSet<EPlanChild>();
		children.addAll(childrenToOldScheduledness.keySet());
		children.addAll(childrenToOldStarts.keySet());
		children.addAll(childrenToOldDurations.keySet());
		children.addAll(childrenToOldEnds.keySet());
		for (EPlanChild child : children) {
			updateTemporalProperties(child);
		}
		for (PeriodicTemporalConstraint constraint : addedPeriodicTemporalConstraints) {
			temporalBoundAdded(constraint);
		}
		for (BinaryTemporalConstraint constraint : addedBinaryTemporalConstraints) {
			temporalRelationAdded(constraint);
		}
		for (TemporalChain chain : addedTemporalChains) {
			temporalChainAdded(chain);
		}
		if (invalidateModel) {
			member.invalidateModel();
		}
	}

	/**
	 * Add this element and then all of its children, recursively
	 * 
	 * @param element
	 */
	private void addElements(EPlanElement element) {
		addElement(element);
		for (EPlanElement child : EPlanUtils.getChildren(element)) {
			if (!model.elementToCache.containsKey(child)) {
				addElements(child);
			}
		}
	}

	/**
	 * Remove this element's children, recursively, and then this element
	 * 
	 * @param element
	 */
	private void removeElements(EPlanElement element) {
		for (EPlanElement child : EPlanUtils.getChildren(element)) {
			removeElements(child);
		}
		removeElement(element);
	}

	/**
	 * Add this element to the constraint network if it is not already in the network
	 * 
	 * @param element
	 */
	private void addElement(EPlanElement element) {
		PlanElementConstraintCache<Time> cache = model.elementToCache.get(element);
		if (cache != null) {
			// verifyUpdateCache(cache);
			return;
		}
		TemporalNetwork<Time>.Timepoint start = network.addTimepoint();
		TemporalNetwork<Time>.Timepoint end = network.addTimepoint();
		TemporalNetwork<Time>.TemporalConstraint durationConstraint = null;
		if (!(element instanceof EPlan)) {
			TemporalMember temporalMember = element.getMember(TemporalMember.class);
			Boolean scheduled = temporalMember.getScheduled();
			Amount<Duration> duration = null;
			if (EPlanUtils.getChildren(element).isEmpty() && ((scheduled == null) || scheduled.booleanValue())) {
				duration = temporalMember.getDuration();
			}
			EPlanElement parent = (EPlanElement) element.eContainer();
			if (parent instanceof EPlanChild) {
				PlanElementConstraintCache<Time> parentCache = model.elementToCache.get(parent);
				if (parentCache != null) {
					TemporalNetwork<Time>.TemporalConstraint parentDuration = parentCache.durationConstraint;
					Bounds bounds = parentDuration.getBounds();
					if (bounds.lowerBound == bounds.upperBound) {
						network.removeTemporalConstraint(parentDuration);
						parentCache.durationConstraint = addNetworkDurationConstraint(null, parentCache.start, parentCache.end);
					}
				}
			}
			durationConstraint = addNetworkDurationConstraint(duration, start, end);
		}
		EObject parent = element.eContainer();
		TemporalNetwork<Time>.TemporalConstraint parentStartConstraint = null;
		TemporalNetwork<Time>.TemporalConstraint parentEndConstraint = null;
		TemporalExtent elementExtent = element.getMember(TemporalMember.class).getExtent();
		if ((parent instanceof EActivity) && (elementExtent != null)) {
			// see also updateSubActivityConstraints
			TemporalExtent parentExtent = ((EActivity) parent).getMember(TemporalMember.class).getExtent();
			Amount<Duration> startToStart = DateUtils.subtract(elementExtent.getStart(), parentExtent.getStart());
			Amount<Duration> endToEnd = DateUtils.subtract(parentExtent.getEnd(), elementExtent.getEnd());
			PlanElementConstraintCache<Time> parentCache = model.elementToCache.get(parent);
			Time networkStart = model.convertTimeDistanceToNetwork(startToStart);
			Time networkEnd = model.convertTimeDistanceToNetwork(endToEnd);
			parentStartConstraint = network.addTemporalConstraint(parentCache.start, start, networkStart, networkStart);
			parentEndConstraint = network.addTemporalConstraint(end, parentCache.end, networkEnd, networkEnd);
		} else if (parent instanceof EPlanElement) {
			PlanElementConstraintCache<Time> parentCache = model.elementToCache.get(parent);
			parentStartConstraint = network.addTemporalConstraint(parentCache.start, start, network.getTime(0L), network.getTime(DistanceGraph.POS_INFINITY));
			parentEndConstraint = network.addTemporalConstraint(end, parentCache.end, network.getTime(0L), network.getTime(DistanceGraph.POS_INFINITY));
		}
		cache = new PlanElementConstraintCache<Time>(element, start, end, durationConstraint, parentStartConstraint, parentEndConstraint);
		model.elementToCache.put(element, cache);
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			for (PeriodicTemporalConstraint timepointConstraint : constraintsMember.getPeriodicTemporalConstraints()) {
				if (timepointConstraint.getWaiverRationale() == null) {
					addNetworkConstraint(cache, timepointConstraint);
				}
			}
			for (BinaryTemporalConstraint distanceConstraint : constraintsMember.getBinaryTemporalConstraints()) {
				if (distanceConstraint.getWaiverRationale() == null) {
					addNetworkConstraint(distanceConstraint);
				}
			}
			TemporalChain chain = constraintsMember.getChain();
			if (chain != null) {
				boolean allPresent = true;
				for (EPlanElement chainElement : chain.getElements()) {
					if (!model.elementToCache.containsKey(chainElement)) {
						allPresent = false;
						break;
					}
				}
				if (allPresent) {
					temporalChainAdded(chain);
				}
			}
		}
	}

	/**
	 * Remove this element from the network if it is in it
	 * 
	 * @param element
	 */
	private void removeElement(EPlanElement element) {
		PlanElementConstraintCache<Time> cache = model.elementToCache.remove(element);
		if (cache == null) {
			return; // not in the network
		}
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			TemporalChain chain = constraintsMember.getChain();
			if (chain != null) {
				temporalChainRemoved(chain);
			}
		}
		for (PeriodicTemporalConstraint bound : cache.bounds) {
			removeNetworkConstraint(bound);
		}
		if (cache.parentStartConstraint != null) {
			network.removeTemporalConstraint(cache.parentStartConstraint);
		}
		if (cache.parentEndConstraint != null) {
			network.removeTemporalConstraint(cache.parentEndConstraint);
		}
		if (cache.durationConstraint != null) {
			network.removeTemporalConstraint(cache.durationConstraint);
		}
		network.deleteTimepoint(cache.start);
		network.deleteTimepoint(cache.end);
	}

	/**
	 * Update this activities duration constraint and periodic constraints to be consistent with the new temporal properties.
	 * 
	 * @param child
	 * @return
	 */
	private boolean updateTemporalProperties(EPlanChild child) {
		PlanElementConstraintCache<Time> cache = model.elementToCache.get(child);
		if (cache == null) {
			return false; // not in the network
		}
		TemporalMember temporalMember = child.getMember(TemporalMember.class);
		boolean newScheduled = (temporalMember.getScheduled() == null) || temporalMember.getScheduled().booleanValue();
		boolean oldScheduled = newScheduled;
		if (childrenToOldScheduledness.containsKey(child)) {
			Boolean bool = childrenToOldScheduledness.get(child);
			oldScheduled = (bool == null) || bool.booleanValue();
		}
		Date newStart = temporalMember.getStartTime();
		Date oldStart = newStart;
		if (childrenToOldStarts.containsKey(child)) {
			oldStart = childrenToOldStarts.get(child);
		}
		Amount<Duration> newDuration = temporalMember.getDuration();
		Amount<Duration> oldDuration = newDuration;
		if (childrenToOldDurations.containsKey(child)) {
			oldDuration = childrenToOldDurations.get(child);
		}
		Date newEnd = temporalMember.getEndTime();
		Date oldEnd = newEnd;
		if (childrenToOldEnds.containsKey(child)) {
			oldEnd = childrenToOldEnds.get(child);
		}
		if (child.eContainer() instanceof EActivity) {
			EActivity parent = (EActivity) child.eContainer();
			if ((oldStart == null) || !oldStart.equals(newStart) || (oldEnd == null) || !oldEnd.equals(newEnd)) {
				updateSubActivityConstraints(parent, (EActivity) child);
			}
		}
		boolean sameStartDay = false;
		boolean sameEndDay = false;
		if (oldScheduled && (oldStart != null) && (oldEnd != null) && newScheduled && (newStart != null) && (newEnd != null)) {
			int oldStartDay = MissionCalendarUtils.getDayOfMission(oldStart);
			int newStartDay = MissionCalendarUtils.getDayOfMission(newStart);
			int oldEndDay = MissionCalendarUtils.getDayOfMission(oldEnd);
			int newEndDay = MissionCalendarUtils.getDayOfMission(newEnd);
			sameStartDay = (oldStartDay == newStartDay);
			sameEndDay = (oldEndDay == newEndDay);
		}
		boolean sameDuration = false;
		if (oldScheduled && (oldDuration != null) && newScheduled && (newDuration != null)) {
			sameDuration = (oldDuration.compareTo(newDuration) == 0);
		}
		if (sameStartDay && sameDuration && sameEndDay) {
			return false; // no change
		}
		if ((!sameStartDay || !sameEndDay) && (newStart != null) && (newDuration != null) && (newEnd != null)) {
			ConstraintsMember constraintsMember = child.getMember(ConstraintsMember.class, false);
			List<PeriodicTemporalConstraint> bounds;
			if (constraintsMember != null) {
				bounds = constraintsMember.getPeriodicTemporalConstraints();
			} else {
				bounds = Collections.emptyList();
			}
			if (bounds.isEmpty() && sameDuration) {
				return false; // no changes after all
			}
			for (PeriodicTemporalConstraint bound : bounds) {
				removeNetworkConstraint(bound);
				addNetworkConstraint(cache, bound);
			}
		}
		if (!sameDuration) {
			TemporalNetwork<Time>.TemporalConstraint durationConstraint = cache.durationConstraint;
			Bounds durationBounds = durationConstraint.getBounds();
			if (((newDuration == null) || !newScheduled) && (durationBounds.lowerBound != durationBounds.upperBound)) {
				return false; // it was unscheduled before, nothing to do
			}
			List<? extends EPlanChild> children = EPlanUtils.getChildren(child);
			if (children.isEmpty()) {
				network.removeTemporalConstraint(durationConstraint);
				cache.durationConstraint = addNetworkDurationConstraint(newDuration, cache.start, cache.end);
			} else if (child instanceof EActivity) {
				EActivity activity = (EActivity) child;
				for (EPlanChild child2 : children) {
					updateSubActivityConstraints(activity, (EActivity) child2);
				}
			}
		}
		return true;
	}

	/**
	 * Update the constraints from the parent activity to the subactivity, to match new offsets if any.
	 * 
	 * @param parent
	 * @param child
	 * @return whether or not the bounds were updated
	 */
	private boolean updateSubActivityConstraints(EActivity parent, EActivity child) {
		boolean updated = false;
		PlanElementConstraintCache<Time> parentCache = model.elementToCache.get(parent);
		PlanElementConstraintCache<Time> childCache = model.elementToCache.get(child);
		TemporalExtent childExtent = child.getMember(TemporalMember.class).getExtent();
		TemporalExtent parentExtent = parent.getMember(TemporalMember.class).getExtent();
		Amount<Duration> startToStart = DateUtils.subtract(childExtent.getStart(), parentExtent.getStart());
		Amount<Duration> endToEnd = DateUtils.subtract(parentExtent.getEnd(), childExtent.getEnd());
		Time networkStart = model.convertTimeDistanceToNetwork(startToStart);
		Time networkEnd = model.convertTimeDistanceToNetwork(endToEnd);
		Bounds startBounds = childCache.parentStartConstraint.getBounds();
		if (startBounds.lowerBound != networkStart || startBounds.upperBound != networkStart) {
			network.removeTemporalConstraint(childCache.parentStartConstraint);
			childCache.parentStartConstraint = network.addTemporalConstraint(parentCache.start, childCache.start, networkStart, networkStart);
			updated = true;
		}
		Bounds endBounds = childCache.parentEndConstraint.getBounds();
		if (endBounds.lowerBound != networkEnd || endBounds.upperBound != networkEnd) {
			network.removeTemporalConstraint(childCache.parentEndConstraint);
			childCache.parentEndConstraint = network.addTemporalConstraint(childCache.end, parentCache.end, networkEnd, networkEnd);
			updated = true;
		}
		return updated;
	}

	/**
	 * Add the constraint to the network
	 * 
	 * @param bound
	 * @return
	 */
	private boolean temporalBoundAdded(PeriodicTemporalConstraint bound) {
		EPlanElement element = bound.getPoint().getElement();
		PlanElementConstraintCache<Time> cache = model.elementToCache.get(element);
		if (cache != null) {
			addNetworkConstraint(cache, bound);
			return true;
		}
		return false;
	}

	/**
	 * Remove the constraint from the network
	 * 
	 * @param bound
	 * @return
	 */
	private boolean temporalBoundRemoved(PeriodicTemporalConstraint bound) {
		if (removeNetworkConstraint(bound)) {
			EPlanElement element = bound.getPoint().getElement();
			PlanElementConstraintCache<Time> cache = model.elementToCache.get(element);
			if (cache != null) {
				cache.bounds.remove(bound);
			}
			return true;
		}
		return false;
	}

	/**
	 * Add the constraint to the network
	 * 
	 * @param member
	 * @return
	 */
	private boolean temporalRelationAdded(BinaryTemporalConstraint constraint) {
		if (addNetworkConstraint(constraint)) {
			return true;
		}
		return false;
	}

	/**
	 * Remove the constraint from the network
	 * 
	 * @param member
	 * @return
	 */
	private boolean temporalRelationRemoved(BinaryTemporalConstraint constraint) {
		if (removeNetworkConstraint(constraint)) {
			EPlanElement elementA = constraint.getPointA().getElement();
			PlanElementConstraintCache cacheA = model.elementToCache.get(elementA);
			if (cacheA != null) {
				cacheA.relations.remove(constraint);
			}
			EPlanElement elementB = constraint.getPointB().getElement();
			PlanElementConstraintCache cacheB = model.elementToCache.get(elementB);
			if (cacheB != null) {
				cacheB.relations.remove(constraint);
			}
			return true;
		}
		return false;
	}

	/**
	 * Add a chain to the network
	 * 
	 * @param chain
	 */
	private void temporalChainAdded(TemporalChain chain) {
		if (model.chainToConstraints.containsKey(chain)) {
			return; // already heard of this one
		}
		List<EPlanElement> elements = chain.getElements();
		if ((elements == null) || (elements.isEmpty())) {
			Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
			logger.warn("skipping empty chain: " + chain);
			return;
		}
		Iterator<EPlanElement> iterator = elements.iterator();
		EPlanElement lastPlanElement = iterator.next();
		List<String> skippedElementNames = new ArrayList<String>();
		PlanElementConstraintCache<Time> cache;
		while ((cache = model.elementToCache.get(lastPlanElement)) == null) {
			skippedElementNames.add(lastPlanElement.getName());
			if (!iterator.hasNext()) {
				Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
				logger.warn("skipped the whole chain: " + chain);
				return;
			}
			lastPlanElement = iterator.next();
		}
		if (!skippedElementNames.isEmpty()) {
			String skippedString = "";
			for (String name : skippedElementNames) {
				skippedString += name + " ";
			}
			Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
			logger.warn("skipped unknown nodes in chain: " + skippedString);
		}
		TemporalNetwork<Time>.Timepoint lastTimepoint = cache.end;
		Set<TemporalNetwork<Time>.TemporalConstraint> constraints = new LinkedHashSet<TemporalNetwork<Time>.TemporalConstraint>();
		while (iterator.hasNext()) {
			EPlanElement thisPlanElement = iterator.next();
			PlanElementConstraintCache<Time> thisCache = model.elementToCache.get(thisPlanElement);
			if (thisCache == null) {
				Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
				logger.warn("skipping unknown node in chain: " + thisPlanElement.getName());
				continue;
			}
			Time maxDistance = network.getTime(DistanceGraph.POS_INFINITY);
			if (PlanConstraintsPreferences.getUseMeetsChains()) {
				maxDistance = network.getTime(0L);
			}
			constraints.add(network.addTemporalConstraint(lastTimepoint, thisCache.start, network.getTime(0L), maxDistance));
			lastPlanElement = thisPlanElement;
			lastTimepoint = thisCache.end;
		}
		model.chainToConstraints.put(chain, constraints);
	}

	/**
	 * Remove the chain from the network
	 * 
	 * @param chain
	 */
	private void temporalChainRemoved(TemporalChain chain) {
		Set<TemporalNetwork<Time>.TemporalConstraint> constraints = model.chainToConstraints.remove(chain);
		if (constraints != null) {
			for (TemporalNetwork<Time>.TemporalConstraint constraint : constraints) {
				try {
					network.removeTemporalConstraint(constraint);
				} catch (NullPointerException e) {
					Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
					logger.warn("NPE from TemporalNetwork");
				} catch (IllegalStateException e) {
					Logger logger = Logger.getLogger(TemporalNetworkCommandBuilder.class);
					logger.warn("ISE from TemporalNetwork");
				}
			}
		}
	}

	/*
	 * Network convenience methods
	 */

	private TemporalNetwork<Time>.TemporalConstraint addNetworkDurationConstraint(Amount<Duration> duration, TemporalNetwork<Time>.Timepoint start, TemporalNetwork<Time>.Timepoint end) {
		TemporalNetwork<Time>.TemporalConstraint durationConstraint = null;
		if (duration != null) {
			if (!duration.isGreaterThan(Amount.valueOf(0, SI.SECOND))) {
				duration = Amount.valueOf(1, SI.MILLI(SI.SECOND));
			}
			Time timeDuration = model.convertTimeDistanceToNetwork(duration);
			durationConstraint = network.addTemporalConstraint(start, end, timeDuration, timeDuration);
		} else {
			durationConstraint = network.addTemporalConstraint(start, end, network.getTime(0L), network.getTime(DistanceGraph.POS_INFINITY)); // allow any nonnegative duration for activities with no
																																				// extent
		}
		return durationConstraint;
	}

	private void addNetworkConstraint(PlanElementConstraintCache<Time> cache, PeriodicTemporalConstraint timepointConstraint) {
		TemporalNetwork<Time>.Timepoint origin = network.getOrigin();
		TemporalNetwork<Time>.Timepoint affected = TemporalNetworkModel.pickTimepoint(timepointConstraint.getPoint().getEndpoint(), cache.start, cache.end);
		Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(timepointConstraint);
		Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(timepointConstraint);
		Time earliest = model.convertEarliestDateToNetwork(minTime);
		Time latest = model.convertLatestDateToNetwork(maxTime);
		try {
			TemporalNetwork<Time>.TemporalConstraint networkConstraint = createNetworkConstraint(origin, affected, earliest, latest);
			model.timepointConstraintToTemporalConstraint.put(timepointConstraint, networkConstraint);
			cache.bounds.add(timepointConstraint);
		} catch (IllegalStateException e) {
			LogUtil.error("failed to add a periodic temporal constraint: " + timepointConstraint, e);
		}
	}

	private boolean removeNetworkConstraint(PeriodicTemporalConstraint timepointConstraint) {
		TemporalNetwork<Time>.TemporalConstraint constraint = model.timepointConstraintToTemporalConstraint.remove(timepointConstraint);
		if (constraint != null) {
			network.removeTemporalConstraint(constraint);
			return true;
		}
		return false;
	}

	/**
	 * Add the distance constraint to the network. Returns true if the constraint was added, false if it wasn't
	 * 
	 * @param element
	 * @param elementStart
	 * @param elementEnd
	 * @param distanceConstraint
	 * @return
	 */
	private boolean addNetworkConstraint(BinaryTemporalConstraint distanceConstraint) {
		if (model.distanceConstraintToTemporalConstraint.get(distanceConstraint) != null || model.distanceConstraintToPeriodicTemporalConstraint.get(distanceConstraint) != null) {
			return false;
		}
		ConstraintPoint pointA = distanceConstraint.getPointA();
		ConstraintPoint pointB = distanceConstraint.getPointB();
		EPlanElement elementA = pointA.getElement();
		EPlanElement elementB = pointB.getElement();
		PlanElementConstraintCache<Time> cacheA = model.elementToCache.get(elementA);
		PlanElementConstraintCache<Time> cacheB = model.elementToCache.get(elementB);
		if ((cacheA == null) || (cacheB == null)) {
			return false; // some element hasn't been added yet, wait until it is
		}
		try {
			PeriodicTemporalConstraint[] periodicTemporalConstraints = convertToPeriodicTemporalConstraint(distanceConstraint);
			PeriodicTemporalConstraint periodicTemporalConstraintA = periodicTemporalConstraints[0];
			PeriodicTemporalConstraint periodicTemporalConstraintB = periodicTemporalConstraints[1];
			List<PeriodicTemporalConstraint> list = new ArrayList<PeriodicTemporalConstraint>();
			if (periodicTemporalConstraintA != null) {
				list.add(periodicTemporalConstraintA);
				addNetworkConstraint(cacheA, periodicTemporalConstraintA);
			}
			if (periodicTemporalConstraintB != null) {
				list.add(periodicTemporalConstraintB);
				addNetworkConstraint(cacheB, periodicTemporalConstraintB);
			}
			if (!list.isEmpty()) {
				model.distanceConstraintToPeriodicTemporalConstraint.put(distanceConstraint, list);
				return true;
			}
		} catch (Exception e1) {
			LogUtil.error(e1);
			return false;
		}
		TemporalNetwork<Time>.Timepoint timepointA = TemporalNetworkModel.pickTimepoint(pointA.getEndpoint(), cacheA.start, cacheA.end);
		TemporalNetwork<Time>.Timepoint timepointB = TemporalNetworkModel.pickTimepoint(pointB.getEndpoint(), cacheB.start, cacheB.end);
		Time lb = model.convertMinTimeDistanceToNetwork(distanceConstraint.getMinimumBminusA());
		Time ub = model.convertMaxTimeDistanceToNetwork(distanceConstraint.getMaximumBminusA());
		try {
			TemporalNetwork<Time>.TemporalConstraint networkConstraint = createNetworkConstraint(timepointA, timepointB, lb, ub);
			model.distanceConstraintToTemporalConstraint.put(distanceConstraint, networkConstraint);
			cacheA.relations.add(distanceConstraint);
			cacheB.relations.add(distanceConstraint);
			return true;
		} catch (IllegalStateException e) {
			LogUtil.error("couldn't add a distance constraint: " + distanceConstraint, e);
			return false;
		}
	}

	public PeriodicTemporalConstraint[] convertToPeriodicTemporalConstraint(BinaryTemporalConstraint distanceConstraint) throws Exception {
		PeriodicTemporalConstraint[] newConstraints = new PeriodicTemporalConstraint[2];
		ConstraintPoint pointA = distanceConstraint.getPointA();
		ConstraintPoint pointB = distanceConstraint.getPointB();
		EPlanElement elementA = pointA.getElement();
		EPlanElement elementB = pointB.getElement();
		if (!pointB.hasEndpoint()) { // If A is anchored to B parameter
			Date time = (Date) ADParameterUtils.getParameterObject(elementB, pointB.getAnchor());
			if (time != null) {
				Amount<Duration> minBminusA = distanceConstraint.getMinimumBminusA();
				Amount<Duration> maxBminusA = distanceConstraint.getMaximumBminusA();
				Date earliest = minBminusA == null ? null : DateUtils.add(time, minBminusA);
				Date latest = maxBminusA == null ? null : DateUtils.add(time, maxBminusA);

				newConstraints[0] = ConstraintUtils.createConstraint(elementA, pointA.getEndpoint(), ConstraintUtils.getPeriodicConstraintOffset(earliest), ConstraintUtils.getPeriodicConstraintOffset(latest), "internal constraint from anchor");
			}
		}
		if (!pointA.hasEndpoint()) { // If B is anchored to A parameter
			Date time = (Date) ADParameterUtils.getParameterObject(elementA, pointA.getAnchor());
			if (time != null) {
				Amount<Duration> minBminusA = distanceConstraint.getMinimumBminusA();
				Amount<Duration> maxBminusA = distanceConstraint.getMaximumBminusA();
				Date earliest = minBminusA == null ? null : DateUtils.add(time, minBminusA);
				Date latest = maxBminusA == null ? null : DateUtils.add(time, maxBminusA);

				newConstraints[1] = ConstraintUtils.createConstraint(elementB, pointB.getEndpoint(), ConstraintUtils.getPeriodicConstraintOffset(earliest), ConstraintUtils.getPeriodicConstraintOffset(latest), "internal constraint from anchor");
			}
		}
		return newConstraints;
	}

	/**
	 * Removes the network constraint Returns true if the constraint was removed, false if it wasn't (not present)
	 * 
	 * @param relation
	 * @return
	 */
	private boolean removeNetworkConstraint(BinaryTemporalConstraint relation) {
		TemporalNetwork<Time>.TemporalConstraint constraint = model.distanceConstraintToTemporalConstraint.remove(relation);
		if (constraint != null) {
			try {
				network.removeTemporalConstraint(constraint);
				return true;
			} catch (NullPointerException e) {
				Logger trace = Logger.getLogger(TemporalNetworkCommandBuilder.class);
				trace.error("Couldn't find a constraint to remove", e);
			}
		}
		List<PeriodicTemporalConstraint> periodicTemporalConstraints = model.distanceConstraintToPeriodicTemporalConstraint.get(relation);
		if (periodicTemporalConstraints != null) {
			for (PeriodicTemporalConstraint periodicTemporalConstraint : periodicTemporalConstraints) {
				temporalBoundRemoved(periodicTemporalConstraint);
			}
			model.distanceConstraintToPeriodicTemporalConstraint.remove(relation);
			return true;
		}
		return false;
	}

	/**
	 * Utility function compensates for disparity between ensemble core and temporal network ordering.
	 * 
	 * @param timepointA
	 * @param timepointB
	 * @param lb
	 * @param ub
	 * @return
	 */
	private TemporalNetwork<Time>.TemporalConstraint createNetworkConstraint(TemporalNetwork<Time>.Timepoint timepointA, TemporalNetwork<Time>.Timepoint timepointB, Time lb, Time ub) {
		return network.addTemporalConstraint(timepointA, timepointB, lb, ub);
	}

}
