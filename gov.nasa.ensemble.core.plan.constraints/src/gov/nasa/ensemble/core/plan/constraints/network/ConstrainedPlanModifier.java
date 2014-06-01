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

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModificationTweakerRegistry;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public abstract class ConstrainedPlanModifier implements IPlanModifier {

	private EPlan plan;
	
	public abstract IPlanConstraintInfo getPlanConstraintInfo();
	
	protected EPlan getPlan() {
		return plan;
	}

	@Override
	public void initialize(EPlan plan) {
		this.plan = plan;
	}
	
	@Override
	public Map<EPlanElement, TemporalExtent> setStart(EPlanElement element, Date start, TemporalExtentsCache initialState) {
		Amount<Duration> duration = initialState.getDuration(element);
		IPlanConstraintInfo info = getPlanConstraintInfo();
		ConsistencyProperties properties = info.getConstraintProperties(element);
		ConsistencyBounds bounds = properties.getBounds();
		List<PeriodicTemporalConstraint> constraints = ConstraintUtils.getPeriodicConstraints(element, false);
		start = constrainStart(start, duration, constraints, bounds);
		Date end = initialState.getEnd(element);
		if (end == null) {
			end = start;
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		affectElements(element, start, end, properties, initialState, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	@Override
	public Map<EPlanElement, TemporalExtent> setEnd(EPlanElement element, Date end, TemporalExtentsCache initialState) {
		Amount<Duration> duration = initialState.getDuration(element);
		IPlanConstraintInfo info = getPlanConstraintInfo();
		ConsistencyProperties properties = info.getConstraintProperties(element);
		ConsistencyBounds bounds = properties.getBounds();
		List<PeriodicTemporalConstraint> constraints = ConstraintUtils.getPeriodicConstraints(element, false);
		end = constrainEnd(duration, end, constraints, bounds);
		Date start = initialState.getStart(element);
		if (start == null) {
			start = end;
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		affectElements(element, start, end, properties, initialState, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	@Override
	public Map<EPlanElement, TemporalExtent> shiftElement(EPlanElement element, Amount<Duration> delta, TemporalExtentsCache initialExtents) {
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		moveRelative(element, delta, initialExtents, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	@Override
	public Map<EPlanElement, TemporalExtent> moveToStart(EPlanElement element, Date start, TemporalExtentsCache initialExtents) {
		if (start == null) {
			return Collections.singletonMap(element, null);
		}
		TemporalExtent oldExtent = initialExtents.get(element);
		if (oldExtent != null) {
			Date oldStart = oldExtent.getStart();
			Amount<Duration> delta = DateUtils.subtract(start, oldStart);
			if (!AmountUtils.approximatesZero(delta)) {
				return shiftElement(element, delta, initialExtents);
			}
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		moveAbsoluteStart(element, start, initialExtents, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	/**
	 * Attempt to "move"* an element (usually activity) while enforcing all constraints by pushing other elements (usually activities) by the minimal amount needed.
	 * 
	 * (* Nothing really moves in the model at this level, just in the changedTimes.)
	 * 
	 * @param element -- the activity to be moved (or could be a group that has no children).
	 * @param newStart -- the desired point to move to (as specified by the mouse offset if dragging on the timeline)
	 * @param initialExtents -- the start and end times before the constrained move starts.
	 * @param changedTimes -- the result we're accumulating, to modify.  Specifies what moves as a result.
	 */
	private void moveAbsoluteStart(EPlanElement element, Date newStart, TemporalExtentsCache initialExtents, Map<EPlanElement, TemporalExtent> changedTimes) {
		Amount<Duration> duration = initialExtents.getDuration(element);
		Date newEnd = DateUtils.add(newStart, duration);
		IPlanConstraintInfo info = getPlanConstraintInfo();
		ConsistencyProperties properties = info.getConstraintProperties(element);
		ConsistencyBounds bounds = properties.getBounds();
		List<PeriodicTemporalConstraint> constraints = ConstraintUtils.getPeriodicConstraints(element, false);
		newStart = constrainStart(newStart, duration, constraints, bounds);
		newEnd = constrainEnd(duration, newEnd, constraints, bounds);
		affectElements(element, newStart, newEnd, properties, initialExtents, changedTimes);
	}

	@Override
	public Map<EPlanElement, TemporalExtent> moveToEnd(EPlanElement element, Date end, TemporalExtentsCache initialExtents) {
		if (end == null) {
			return Collections.singletonMap(element, null);
		}
		TemporalExtent oldExtent = initialExtents.get(element);
		if (oldExtent != null) {
			Date oldEnd = oldExtent.getEnd();
			Amount<Duration> delta = DateUtils.subtract(end, oldEnd);
			if (!AmountUtils.approximatesZero(delta)) {
				return shiftElement(element, delta, initialExtents);
			}
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		moveAbsoluteEnd(element, end, initialExtents, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	private void moveAbsoluteEnd(EPlanElement element, Date newEnd, TemporalExtentsCache initialExtents, Map<EPlanElement, TemporalExtent> changedTimes) {
		Amount<Duration> duration = initialExtents.getDuration(element);
		IPlanConstraintInfo info = getPlanConstraintInfo();
		ConsistencyProperties properties = info.getConstraintProperties(element);
		ConsistencyBounds bounds = properties.getBounds();
		// If the properties/bounds are empty, we will use the following periodic constraints
		// to enforce a limited version of constrained move, where we only ensure that the
		// moved element will not violate it's own periodic constraints.
		List<PeriodicTemporalConstraint> constraints = ConstraintUtils.getPeriodicConstraints(element, false);
		Date newStart = DateUtils.subtract(newEnd, duration);
		newEnd = constrainEnd(duration, newEnd, constraints, bounds);
		newStart = constrainStart(newStart, duration, constraints, bounds);
		affectElements(element, newStart, newEnd, properties, initialExtents, changedTimes);
	}

	/**
	 * Recursively shift the elements.
	 * 
	 * @param pe
	 * @param delta
	 * @param initialExtents
	 * @param changedTimes
	 */
	private void moveRelative(EPlanElement pe, Amount<Duration> delta, TemporalExtentsCache initialExtents, Map<EPlanElement, TemporalExtent> changedTimes) {
		if ((pe instanceof EActivity) || pe.getChildren().isEmpty()) {
			Date newStart = DateUtils.add(initialExtents.get(pe).getStart(), delta);
			moveAbsoluteStart(pe, newStart, initialExtents, changedTimes);
			return;
		}
		List<? extends EPlanElement> children = EPlanUtils.getChildren(pe);
		for (EPlanElement child : children) {
			moveRelative(child, delta, initialExtents, changedTimes);
		}
		if (changedTimes.get(pe) != null) {
			return;
		}
		// Below this line we compute the new bounds for the activity group.
		// However, TemporalUtils.setExtents ignores the changes for groups,
		// because the TemporalMember now enforces that internally.  So,
		// this is probably no longer necessary.
		// TODO:  Try removing this and test to verify it does not change behavior.
		Date earliest = null;
		Date latest = null;
		for (EPlanElement child : children) {
			TemporalExtent childExtent = changedTimes.get(child);
			if (childExtent != null) {
				Date childStart = childExtent.getStart();
				Date childEnd = childExtent.getEnd();
				if (earliest == null) {
					earliest = childStart;
					latest = childEnd;
				} else {
					earliest = DateUtils.earliest(earliest, childStart);
					latest = DateUtils.latest(latest, childEnd);
				}
			}
		}
		if (earliest != null) {
			changedTimes.put(pe, new TemporalExtent(earliest, latest));
		}
	}

	/**
	 * Find the closest consistent start to the current start.
	 * 
	 * @param start
	 * @param duration
	 * @param constraints
	 * @param bounds
	 * @return
	 */
	private Date constrainStart(Date start, Amount<Duration> duration, List<PeriodicTemporalConstraint> constraints, ConsistencyBounds bounds) {
		for (PeriodicTemporalConstraint constraint : constraints) {
			if (constraint.getPoint().getEndpoint() == Timepoint.START) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				start = DateUtils.bind(start, minTime, maxTime);
			}
			if (constraint.getPoint().getEndpoint() == Timepoint.END) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				if (minTime != null) {
					minTime = DateUtils.subtract(minTime, duration);
				}
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				if (maxTime != null) {
					maxTime = DateUtils.subtract(maxTime, duration);
				}
				start = DateUtils.bind(start, minTime, maxTime);
			}
		}
		if (bounds != null) {
			Date earliestStart = bounds.getEarliestStart();
			Date latestStart = bounds.getLatestStart();
			Date latestEnd = bounds.getLatestEnd();
			if ((latestStart == null) && (latestEnd != null)) {
				latestStart = DateUtils.subtract(latestEnd, duration);
			}
			start = DateUtils.bind(start, earliestStart, latestStart);
		}
		return start;
	}

	/**
	 * Find the closest consistent end to the current end.
	 * 
	 * @param duration
	 * @param end
	 * @param constraints
	 * @param bounds
	 * @return
	 */
	private Date constrainEnd(Amount<Duration> duration, Date end, List<PeriodicTemporalConstraint> constraints, ConsistencyBounds bounds) {
		for (PeriodicTemporalConstraint constraint : constraints) {
			if (constraint.getPoint().getEndpoint() == Timepoint.START) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				if (minTime != null) {
					minTime = DateUtils.add(minTime, duration);
				}
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				if (maxTime != null) {
					maxTime = DateUtils.add(maxTime, duration);
				}
				end = DateUtils.bind(end, minTime, maxTime);
			}
			if (constraint.getPoint().getEndpoint() == Timepoint.END) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				end = DateUtils.bind(end, minTime, maxTime);
			}
		}
		if (bounds != null) {
			Date earliestStart = bounds.getEarliestStart();
			Date earliestEnd = bounds.getEarliestEnd();
			Date latestEnd = bounds.getLatestEnd();
			if ((earliestEnd == null) && (earliestStart != null)) {
				earliestEnd = DateUtils.add(earliestStart, duration);
			}
			end = DateUtils.bind(end, earliestEnd, latestEnd);
		}
		return end;
	}

	/**
	 * Affect all elements that have constraints to this element in accordance with its new extent.
	 * 
	 * @param element
	 * @param newStart
	 * @param newEnd
	 * @param properties
	 * @param initialExtents
	 * @param changedTimes
	 */
	private void affectElements(EPlanElement element, Date newStart, Date newEnd, ConsistencyProperties properties, 
			TemporalExtentsCache initialExtents, Map<EPlanElement, TemporalExtent> changedTimes) {
		TemporalExtent extent = new TemporalExtent(newStart, newEnd);
		changedTimes.put(element, extent);
		for (ConsistencyConstraint constraint : properties.getConstraints()) {
			if ((constraint.affectedElement instanceof EActivity) || constraint.affectedElement.getChildren().isEmpty()) {
				switch (constraint.sourceTimepoint) {
				case START: 
					applyConstraint(extent.getStart(), constraint, initialExtents, changedTimes);
					break;
				case END: 
					applyConstraint(extent.getEnd(), constraint, initialExtents, changedTimes);
					break;
				default:
					Logger.getLogger(ConstrainedPlanModifier.class).warn("unexpected timepoint: " + constraint.sourceTimepoint);
				}
			}
		}
	}

	/**
	 * Given a source date, enforce the constraint on the affected node,
	 * using the preferred time as a guide.
	 * @param date
	 * @param constraint
	 * @param changedTimes
	 */
	private void applyConstraint(Date date, ConsistencyConstraint constraint, TemporalExtentsCache initialExtents, Map<EPlanElement, TemporalExtent> changedTimes) {
		Date earliestDate = DateUtils.add(date, constraint.minimumDistance);
		Date latestDate = DateUtils.add(date, constraint.maximumDistance);
		EPlanElement affectedPlanElement = constraint.affectedElement;
		TemporalExtent extent = changedTimes.get(affectedPlanElement);
		if (extent == null) {
			extent = initialExtents.get(affectedPlanElement);
			if (extent == null) {
				extent = new TemporalExtent(date, affectedPlanElement.getMember(TemporalMember.class).getDuration());
			}
		}
		if (constraint.affectedTimepoint == Timepoint.START) {
			Date affectedDate = DateUtils.bind(extent.getStart(), earliestDate, latestDate);
			changedTimes.put(affectedPlanElement, extent.moveToStart(affectedDate));
		}
		if (constraint.affectedTimepoint == Timepoint.END) {
			Date affectedDate = DateUtils.bind(extent.getEnd(), earliestDate, latestDate);
			changedTimes.put(affectedPlanElement, extent.moveToEnd(affectedDate));
		}
	}
	
	/**
	 * Compute the expected extent of the element, which should be as close to duration as possible.
	 * This will affect the start or end and may affect both.
	 */
	@Override
	public Map<EPlanElement, TemporalExtent> setDuration(EPlanElement element, Amount<Duration> duration, TemporalExtentsCache initialState, boolean fromStart) {
		IPlanConstraintInfo info = getPlanConstraintInfo();
		ConsistencyProperties properties = info.getConstraintProperties(element);
		ConsistencyBounds bounds = properties.getBounds();
		List<PeriodicTemporalConstraint> constraints = ConstraintUtils.getPeriodicConstraints(element, false);
		Date newEnd;
		Date newStart;
		if (fromStart) {
			// compute the end first, then back off the start if necessary
			Date oldStart = element.getMember(TemporalMember.class).getStartTime();
			newEnd = constrainEnd(duration, bounds, oldStart, constraints);
			newStart = constrainStart(duration, bounds, newEnd, constraints);
		} else {
			// compute the start first, then back off the end if necessary
			Date oldEnd = element.getMember(TemporalMember.class).getEndTime();
			newStart = constrainStart(duration, bounds, oldEnd, constraints);
			newEnd = constrainEnd(duration, bounds, newStart, constraints);
		}
		TemporalExtent newExtent = new TemporalExtent(newStart, newEnd);
		TemporalExtentsCache durationState = new TemporalExtentsCacheWithDuration(initialState, element, newExtent);
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		affectElements(element, newStart, newEnd, properties, durationState, changedTimes);
		return changedTimes;
	}

	private Date constrainEnd(Amount<Duration> duration, ConsistencyBounds bounds, Date start, List<PeriodicTemporalConstraint> constraints) {
		Date newEnd = DateUtils.add(start, duration);
		for (PeriodicTemporalConstraint constraint : constraints) {
			if (constraint.getPoint().getEndpoint() == Timepoint.END) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				newEnd = DateUtils.bind(newEnd, minTime, maxTime);
			}
		}
		newEnd = DateUtils.bind(newEnd, bounds.getEarliestEnd(), bounds.getLatestEnd());
		return newEnd;
	}

	private Date constrainStart(Amount<Duration> duration, ConsistencyBounds bounds, Date end, List<PeriodicTemporalConstraint> constraints) {
		Date newStart = DateUtils.subtract(end, duration);
		for (PeriodicTemporalConstraint constraint : constraints) {
			if (constraint.getPoint().getEndpoint() == Timepoint.START) {
				Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(constraint);
				Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(constraint);
				newStart = DateUtils.bind(newStart, minTime, maxTime);
			}
		}
		newStart = DateUtils.bind(newStart, bounds.getEarliestStart(), bounds.getLatestStart());
		return newStart;
	}
	
	private void performAnyRegisteredTweaks(Map<EPlanElement, TemporalExtent> changedTimes) {
		if (!changedTimes.isEmpty()) {
			PlanModificationTweakerRegistry.getInstance().applyTweaksDuringMove(plan, changedTimes);
		}
	}

	private final class TemporalExtentsCacheWithDuration extends TemporalExtentsCache {
		private TemporalExtentsCacheWithDuration(TemporalExtentsCache initialState, EPlanElement element, TemporalExtent newExtent) {
			super(initialState);
			set(element, newExtent);
		}
	}

}
