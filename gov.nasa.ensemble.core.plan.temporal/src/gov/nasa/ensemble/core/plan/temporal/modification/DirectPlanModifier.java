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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class DirectPlanModifier implements IPlanModifier {

	private EPlan plan;

	@Override
	public void initialize(EPlan plan) {
		this.plan = plan;
	}

	public void revalidate() {
		// nothing to do
	}

	@Override
	public Map<EPlanElement, TemporalExtent> setStart(EPlanElement element, Date start, TemporalExtentsCache initialState) {
		List<? extends EPlanChild> children = EPlanUtils.getChildren(element);
		TemporalExtent newExtent = getNewExtentFromStart(element, start, initialState);
		if ((element instanceof EActivity) || children.isEmpty()) {
			if (newExtent != null) {
				return Collections.singletonMap(element, newExtent);
			}
			return Collections.emptyMap(); // nothing to do
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		if (newExtent != null && !element.getMember(TemporalMember.class).isUseChildTimes()) {
			changedTimes.put(element, newExtent);
		}
		for (EPlanElement child : children) {
			TemporalMember childTemporal = child.getMember(TemporalMember.class);
			if (childTemporal.isUseParentTimes()) {
				if (newExtent != null) {
					TemporalUtils.getExtentsFromOffsets(child, newExtent, changedTimes);
				}
			} else {
				Date childStart = childTemporal.getStartTime();
				if (childStart == null) {
					recursiveSetStart(child, start, changedTimes);
				} else if (childStart.before(start)) {
					changedTimes.putAll(moveToStart(child, start, initialState));
				}
			}
		}
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}

	private void performAnyRegisteredTweaks(Map<EPlanElement, TemporalExtent> changedTimes) {
		if (!changedTimes.isEmpty()) {
			PlanModificationTweakerRegistry.getInstance().applyTweaksDuringMove(plan, changedTimes);
		}
	}

	private TemporalExtent getNewExtentFromStart(EPlanElement element, Date start, TemporalExtentsCache initialState) {
		TemporalExtent oldExtent = initialState.get(element);
		TemporalExtent newExtent = null;
		if ((oldExtent != null) && (start != null)) {
			Date oldStart = oldExtent.getStart();
			Amount<Duration> delta = DateUtils.subtract(start, oldStart);
			if (!AmountUtils.approximatesZero(delta))
				newExtent = oldExtent.setStart(start);
		}
		return newExtent;
	}

	@Override
	public Map<EPlanElement, TemporalExtent> setEnd(EPlanElement element, Date end, TemporalExtentsCache initialState) {
		List<? extends EPlanChild> children = EPlanUtils.getChildren(element);
		TemporalExtent newExtent = getNewExtentFromEnd(element, end, initialState);
		if ((element instanceof EActivity) || children.isEmpty()) {
			if (newExtent != null) {
				return Collections.singletonMap(element, newExtent);
			}
			return Collections.emptyMap(); // nothing to do
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		if (newExtent != null && !element.getMember(TemporalMember.class).isUseChildTimes()) {
			changedTimes.put(element, newExtent);
		}
		for (EPlanElement child : children) {
			TemporalMember childTemporal = child.getMember(TemporalMember.class);
			if (childTemporal.isUseParentTimes()) {
				if (newExtent != null) {
					TemporalUtils.getExtentsFromOffsets(child, newExtent, changedTimes);
				}
			} else {
				Date childEnd = childTemporal.getEndTime();
				if (childEnd == null) {
					recursiveSetEnd(child, end, changedTimes);
				} else if (childEnd.after(end)) {
					changedTimes.putAll(moveToEnd(child, end, initialState));
				}
			}
		}
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}
	
	private TemporalExtent getNewExtentFromEnd(EPlanElement element, Date end, TemporalExtentsCache initialState) {
		TemporalExtent oldExtent = initialState.get(element);
		TemporalExtent newExtent = null;
		if ((oldExtent != null) && (end != null)) {
			Date oldEnd = oldExtent.getEnd();
			Amount<Duration> delta = DateUtils.subtract(end, oldEnd);
			if (!AmountUtils.approximatesZero(delta))
				newExtent = oldExtent.setEnd(end);
		}
		return newExtent;
	}

	/**
	 * Moves the node to a new start time 1. set parent end to the later start
	 * time, if necessary 2. move this node and children by the displacement of
	 * this node 3. set parent start as close as possible to their preferred
	 * times
	 * 
	 * @param pe
	 * @param delta
	 * @param initialState
	 */
	@Override
	public Map<EPlanElement, TemporalExtent> shiftElement(EPlanElement pe, Amount<Duration> delta, TemporalExtentsCache initialState) {
		Map<EPlanElement, TemporalExtent> changedTimes = new HashMap<EPlanElement, TemporalExtent>();
		if (!AmountUtils.approximatesZero(delta)) {
			TemporalExtent oldExtent = initialState.get(pe);
			if (oldExtent != null) {
				TemporalExtent newExtent = oldExtent.shift(delta);
				changedTimes.put(pe, newExtent);
				moveChildren(pe, delta, newExtent.getStart(), initialState, changedTimes);
			}
		}
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}

	@Override
	public Map<EPlanElement, TemporalExtent> moveToStart(EPlanElement pe, final Date start, TemporalExtentsCache initialState) {
		if (start == null) {
			return Collections.emptyMap(); // nothing to do
		}
		TemporalExtent oldExtent = initialState.get(pe);
		if (oldExtent != null) {
			Date oldStart = oldExtent.getStart();
			Amount<Duration> delta = DateUtils.subtract(start, oldStart);
			if (!AmountUtils.approximatesZero(delta)) {
				return shiftElement(pe, delta, initialState);
			}
			return Collections.emptyMap(); // nothing to do
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		recursiveSetStart(pe, start, changedTimes);
		return changedTimes;
	}

	private void recursiveSetStart(EPlanElement parent, final Date start, final Map<EPlanElement, TemporalExtent> changedTimes) {
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				Amount<Duration> duration = element.getMember(TemporalMember.class).getDuration();
				changedTimes.put(element, new TemporalExtent(start, duration));
			}
		}.visitAll(parent);
	}

	@Override
	public Map<EPlanElement, TemporalExtent> moveToEnd(EPlanElement pe, final Date end, TemporalExtentsCache initialState) {
		if (end == null) {
			return Collections.emptyMap(); // nothing to do
		}
		TemporalExtent oldExtent = initialState.get(pe);
		if (oldExtent != null) {
			Date oldEnd = oldExtent.getEnd();
			Amount<Duration> delta = DateUtils.subtract(end, oldEnd);
			if (!AmountUtils.approximatesZero(delta)) {
				return shiftElement(pe, delta, initialState);
			}
			return Collections.emptyMap(); // nothing to do
		}
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		recursiveSetEnd(pe, end, changedTimes);
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}

	private void recursiveSetEnd(EPlanElement parent, final Date end, final Map<EPlanElement, TemporalExtent> changedTimes) {
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				Amount<Duration> duration = element.getMember(TemporalMember.class).getDuration();
				changedTimes.put(element, new TemporalExtent(duration, end));
			}
		}.visitAll(parent);
	}

	/**
	 * Moves the children by a positive delta
	 * 
	 * @param pe
	 * @param delta
	 * @param parentStart
	 * @param initialState
	 * @param changedTimes
	 */
	private void moveChildren(EPlanElement pe, Amount<Duration> delta, Date parentStart, TemporalExtentsCache initialState, Map<EPlanElement, TemporalExtent> changedTimes) {
		for (EPlanElement child : EPlanUtils.getChildren(pe)) {
			TemporalExtent initialExtent = initialState.get(child);
			TemporalExtent newExtent;
			if (initialExtent != null) {
				newExtent = initialExtent.shift(delta);
			} else {
				Amount<Duration> duration = child.getMember(TemporalMember.class).getDuration();
				newExtent = new TemporalExtent(parentStart, duration);
			}
			changedTimes.put(child, newExtent);
			moveChildren(child, delta, newExtent.getStart(), initialState, changedTimes);
		}
	}

	@Override
	public Map<EPlanElement, TemporalExtent> setDuration(EPlanElement element, Amount<Duration> duration, TemporalExtentsCache initialState, boolean fromStart) {
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		TemporalExtent oldExtent = initialState.get(element);
		TemporalExtent newExtent = null;
		if ((oldExtent != null) && (duration != null)) {
			if (fromStart) {
				newExtent = new TemporalExtent(oldExtent.getStart(), duration);
			} else {
				newExtent = new TemporalExtent(duration, oldExtent.getEnd());
			}
			changedTimes.put(element, newExtent);
			for (EPlanChild child : element.getChildren()) {
				TemporalUtils.getExtentsFromOffsets(child, newExtent, changedTimes);
			}
		}
		performAnyRegisteredTweaks(changedTimes);
		return changedTimes;
	}

}
