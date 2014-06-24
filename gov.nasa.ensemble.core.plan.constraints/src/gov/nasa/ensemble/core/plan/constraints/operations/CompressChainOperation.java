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
package gov.nasa.ensemble.core.plan.constraints.operations;


import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyBounds;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyConstraint;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyProperties;
import gov.nasa.ensemble.core.plan.constraints.network.ConstrainedPlanModifier;
import gov.nasa.ensemble.core.plan.constraints.network.IPlanConstraintInfo;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.jscience.physics.amount.Amount;


public class CompressChainOperation extends AbstractTransactionUndoableOperation {

	private static final Logger trace = Logger.getLogger(CompressChainOperation.class);
	
	private final List<? extends EPlanElement> representatives;
	private final TemporalExtentsCache initialExtents;
	private final ChainCache intermediateExtents;
	private final IPlanModifier modifier;
	private final Map<EPlanElement, TemporalExtent> changedTimes = new HashMap<EPlanElement, TemporalExtent>();
	private final EPlan context; 
	
	/**
	 * Chain the elements in the specified order.
	 * @param modifier
	 * @param representatives
	 */
	public CompressChainOperation(List<? extends EPlanElement> representatives) {
		super("compress chain(s)");
		trace.debug("creating " + CompressChainOperation.class.getSimpleName() + ": " + representatives);
		if ((representatives == null) || representatives.isEmpty()) {
			throw new IllegalArgumentException("must compress at least one chain element");
		}
		this.representatives = representatives;
		EPlan plan = EPlanUtils.getPlan(representatives.iterator().next());
		if (plan == null) {
			throw new IllegalArgumentException("representatives must belong to a plan");
		}
		this.initialExtents = new TemporalExtentsCache(plan);
		this.intermediateExtents = new ChainCache(plan);
		this.modifier = PlanModifierMember.get(plan).getModifier();
		for (EPlanElement representative : representatives) {
			compressChainStarts(representative);
		}
		this.context = plan;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("executing " + CompressChainOperation.class.getSimpleName() + ": " + this);
				TemporalUtils.setExtents(changedTimes);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("undoing " + CompressChainOperation.class.getSimpleName() + ": " + this);
				TemporalUtils.resetExtents(changedTimes.keySet(), initialExtents);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(CompressChainOperation.class.getSimpleName());
		builder.append(":");
		for (EPlanElement element : representatives) {
			builder.append(element.getName() + " ");
		}
		return builder.toString();
	}

	/*
	 * Operation initialization functions
	 */
	
	private void compressChainStarts(EPlanElement representative) {
		Date targetStart = representative.getMember(TemporalMember.class).getStartTime();
		if (targetStart == null) {
			trace.error("current start missing for chain representative: check getRepresentativePlanElements");
			return; // skip this chain
		}
		if (modifier instanceof ConstrainedPlanModifier) {
			compressConstrainedChain((ConstrainedPlanModifier)modifier, representative, targetStart);
		} else {
			compressUnconstrainedChain(representative, targetStart);
		}
	}

	/**
	 * Compress a chain while obeying constraints.  Will modify the
	 * intermediateExtents to reflect the new positions of any changed
	 * elements in the plan.  Will also modify changedTimes to reflect
	 * the new times.
	 * 
	 * @param modifier
	 * @param representative
	 * @param targetStart
	 */
	private void compressConstrainedChain(ConstrainedPlanModifier modifier, EPlanElement representative, Date targetStart) {
		IPlanConstraintInfo constraintInfo = modifier.getPlanConstraintInfo();
		ConsistencyProperties properties = constraintInfo.getConstraintProperties(representative);
		ConsistencyBounds bounds = properties.getBounds();
		targetStart = DateUtils.bind(targetStart, bounds.getEarliestStart(), bounds.getLatestEnd());
		TemporalChain chain = representative.getMember(ConstraintsMember.class, false).getChain();
		if (chain == null) {
			trace.error("chain missing for chain representative: check getRepresentativePlanElements");
			return; // skip this chain
		}
		Map<EPlanElement, Date> chainDates = createChainPlanElementsToNearestDateMap(chain, representative, targetStart, properties);
		int elementCount = chain.getElements().size();
		EPlanElement[] reverseElements = new EPlanElement[elementCount];
		int index = elementCount - 1;
		for (EPlanElement chainElement : chain.getElements()) {
			reverseElements[index--] = chainElement;
		}
		// Going through the elements in reverse order causes us to prefer
		// to move earlier elements closer to the representative at the expense
		// of later elements moving farther from the representative.  The
		// overall effect of this is to cause elements to generally move to
		// later times rather than earlier.  It's my conjecture that this will
		// be more intuitive to users and cause fewer conflicts.
		for (EPlanElement key : reverseElements) {
			Date value = chainDates.get(key);
			if (value == null) {
				continue; // skip missing (unscheduled) elements
			}
			Map<EPlanElement, TemporalExtent> affects = modifier.moveToStart(key, value, intermediateExtents);
			for (Map.Entry<EPlanElement, TemporalExtent> affect : affects.entrySet()) {
				EPlanElement element = affect.getKey();
				TemporalExtent extent = affect.getValue();
				if (extent == null) {
					continue; // skip missing (unscheduled) elements
				}
				changedTimes.put(element, extent);
				TemporalExtent oldExtent = intermediateExtents.get(element);
				TemporalExtent newExtent = (oldExtent != null ? oldExtent.moveToStart(extent.getStart()) : extent);
				intermediateExtents.set(element, newExtent);
			}
		}
	}

	/**
	 * Determine the most compressed start times for a chain respecting constraints,
	 * without moving the representative from targetStart.
	 * 
	 * @param representative
	 * @param targetStart
	 * @param properties
	 * @return
	 */
	private Map<EPlanElement, Date> createChainPlanElementsToNearestDateMap(TemporalChain chain, EPlanElement representative, Date targetStart,
			ConsistencyProperties properties) {
		Map<EPlanElement, ConsistencyConstraint> affectedToConstraint = new HashMap<EPlanElement, ConsistencyConstraint>();
		for (ConsistencyConstraint constraint : properties.getConstraints()) {
			if ((constraint.affectedTimepoint == Timepoint.START)
				&& (constraint.sourceTimepoint == Timepoint.START)) {
				affectedToConstraint.put(constraint.affectedElement, constraint);
			}
		}
		Map<EPlanElement, Date> result = new HashMap<EPlanElement, Date>();
		boolean reachedRepresentative = false;
		for (EPlanElement element : chain.getElements()) {
			if (element == representative) {
				result.put(element, targetStart);
				reachedRepresentative = true;
			} else {
				ConsistencyConstraint constraint = affectedToConstraint.get(element);
				if (constraint != null) {
					Date start;
					if (reachedRepresentative) {
						start = DateUtils.add(targetStart, constraint.minimumDistance);
					} else {
						start = DateUtils.add(targetStart, constraint.maximumDistance);
					}
					result.put(element, start);
				}
			}
		}
		return result;
	}

	/**
	 * Compress a chain without regard to constraints.  Places
	 * all elements in the chain immediately after one another,
	 * with the plan element representative at targetStart.
	 * 
	 * @param representative
	 * @param targetStart
	 */
	private void compressUnconstrainedChain(EPlanElement representative, Date targetStart) {
		List<EPlanElement> elementsBeforeRepresentative = new ArrayList<EPlanElement>();
		List<EPlanElement> elementsAfterRepresentative = new ArrayList<EPlanElement>();
		splitChain(representative, elementsBeforeRepresentative, elementsAfterRepresentative);
		List<Date> datesForElementsBefore = computeStartsBefore(targetStart, elementsBeforeRepresentative);
		List<Date> datesForElementsAfter = computeStartsAfter(representative, targetStart, elementsAfterRepresentative);
		setCurrentStarts(elementsBeforeRepresentative, datesForElementsBefore);
		changedTimes.put(representative, representative.getMember(TemporalMember.class).getExtent());
		setCurrentStarts(elementsAfterRepresentative, datesForElementsAfter);
	}

	/**
	 * Place the elements at their given position, using the plan modifier.
	 * @param elementsBeforeRepresentative
	 * @param datesForElementsBefore
	 */
	private void setCurrentStarts(List<EPlanElement> elementsBeforeRepresentative, List<Date> datesForElementsBefore) {
		Iterator<EPlanElement> elementIterator = elementsBeforeRepresentative.iterator();
		ListIterator<Date> dateIterator = datesForElementsBefore.listIterator();
		while (elementIterator.hasNext() && dateIterator.hasNext()) {
			EPlanElement element = elementIterator.next();
			Date date = dateIterator.next();
			Boolean scheduled = element.getMember(TemporalMember.class).getScheduled();
			if ((scheduled != null) && !scheduled.booleanValue()) {
				continue; // skip unscheduled activities
			}
			Map<EPlanElement, TemporalExtent> changedStarts = modifier.moveToStart(element, date, initialExtents);
			changedTimes.putAll(changedStarts);
			TemporalExtent startDate = changedStarts.get(element); 
			if (startDate == null) {
				changedTimes.put(element, new TemporalExtent(date, element.getMember(TemporalMember.class).getDuration()));
			}
		}
		if (elementIterator.hasNext() || dateIterator.hasNext()) {
			trace.warn("element/date mismatch");
		}
	}

	/**
	 * Compute start times for elements in front of the representative
	 * 
	 * @param targetStart
	 * @param elementsBeforeRepresentative
	 * @return
	 */
	private List<Date> computeStartsBefore(Date targetStart, List<EPlanElement> elementsBeforeRepresentative) {
		ArrayList<EPlanElement> reversed = new ArrayList<EPlanElement>(elementsBeforeRepresentative);
		Collections.reverse(reversed);
		Date priorStart = targetStart;
		List<Date> dates = new ArrayList<Date>();
		for (EPlanElement element : reversed) {
			priorStart = computePriorStart(priorStart, element);
			dates.add(priorStart);
		}
		Collections.reverse(dates);
		return dates;
	}

	/**
	 * Compute start times for elements behind the representative.
	 * 
	 * @param representative
	 * @param targetStart
	 * @param elementsAfterRepresentative
	 * @return
	 */
	private List<Date> computeStartsAfter(EPlanElement representative, Date targetStart, List<EPlanElement> elementsAfterRepresentative) {
		Date nextStart = computeNextStart(targetStart, representative);
		List<Date> dates = new ArrayList<Date>();
		for (EPlanElement element : elementsAfterRepresentative) {
			dates.add(nextStart);
			nextStart = computeNextStart(nextStart, element);
		}
		return dates;
	}

	/**
	 * Divide a chain at the representative chain element
	 * 
	 * @param representativeChainElement
	 * @param elementsBeforeRepresentative
	 * @param elementsAfterRepresentative
	 */
	private void splitChain(EPlanElement representative, List<EPlanElement> elementsBeforeRepresentative,
			List<EPlanElement> elementsAfterRepresentative) {
		TemporalChain chain = representative.getMember(ConstraintsMember.class, true).getChain();
		if (chain == null) {
			trace.error("chain missing for chain representative: check getRepresentativePlanElements");
			return; // skip this chain
		}
		boolean reachedRepresentative = false;
		for (EPlanElement chainElement : chain.getElements()) {
			if (chainElement == representative) {
				reachedRepresentative = true;
				continue;
			}
			EPlanElement memberElement = chainElement;
			if (reachedRepresentative) {
				elementsAfterRepresentative.add(memberElement);
			} else {
				elementsBeforeRepresentative.add(memberElement);
			}
		}
	}

	/**
	 * Given that the element should be starting at the given start,
	 * return the start time for an element following this one.
	 * Will return the same start time if the given element has no duration.
	 * 
	 * @param targetStart
	 * @param representative
	 * @return
	 */
	private Date computeNextStart(Date start, EPlanElement element) {
		Boolean scheduled = element.getMember(TemporalMember.class).getScheduled();
		if ((scheduled != null) && !scheduled.booleanValue()) {
			return start; // treat unscheduled plan elements as zero length
		}
		TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
		if (extent != null) {
			return DateUtils.add(start, extent.getDuration());
		}
		return start;
	}

	/**
	 * Given that the element should be ending at the given start,
	 * return the start time for this element.
	 * Will return the same start time if the given element has no duration.
	 * 
	 * @param targetStart
	 * @param representative
	 * @return
	 */
	private Date computePriorStart(Date start, EPlanElement element) {
		Boolean scheduled = element.getMember(TemporalMember.class).getScheduled();
		if ((scheduled != null) && !scheduled.booleanValue()) {
			return start; // treat unscheduled plan elements as zero length
		}
		TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
		if (extent != null) {
			return DateUtils.subtract(start, extent.getDuration());
		}
		return start;
	}

	/*
	 * Public utility function
	 */
	
	/**
	 * Given a list representing a selection, return a list
	 * with the following properties:
	 * 
	 * 1. the list is in temporal order (all elements will have a start time)
	 * 2. each element in the returned list was in the source list
	 * 3. each element in the returned list is in some chain
	 * 4. no two elements in the returned list are from the same chain
	 * 
	 * @param list
	 * @return
	 */
	public static List<? extends EPlanElement> getRepresentativePlanElements(Collection<? extends EPlanElement> list) {
		Map<TemporalChain, EPlanElement> chainToRepresentativeMap = createChainToRepresentiveMap(list);
		EList<EPlanElement> representatives = new BasicEList<EPlanElement>(chainToRepresentativeMap.values());
		ECollections.sort(representatives, new Comparator<EPlanElement>() {
			@Override
			public int compare(EPlanElement o1, EPlanElement o2) {
				Date start1 = o1.getMember(TemporalMember.class).getStartTime();
				Date start2 = o2.getMember(TemporalMember.class).getStartTime();
				Amount<Duration> duration = DateUtils.subtract(start1, start2);
				int comparison = duration.compareTo(DateUtils.ZERO_DURATION);
				if (comparison != 0) {
					return comparison;
				}
				return PlanUtils.INHERENT_ORDER.compare(o1, o2);
			}
		});
		return representatives;
	}

	/**
	 * Given a list representing a selection, return a map
	 * with the following properties:
	 * 
	 * 1. for each element from the list which has a start time and is in a chain, 
	 *    that chain is a key in the map
	 * 2. for each key in the map, the value is the plan element from the list that
	 *    is in that chain and has the earliest start time
	 * 
	 * @param list
	 * @return
	 */
	private static Map<TemporalChain, EPlanElement> createChainToRepresentiveMap(Collection<? extends EPlanElement> list) {
		Map<TemporalChain, EPlanElement> chainToRepresentativeMap = new HashMap<TemporalChain, EPlanElement>();
		for (EPlanElement planElement : list) {
			Date planElementStart = planElement.getMember(TemporalMember.class).getStartTime();
			if (planElementStart == null) {
				continue; // skip elements with no start time
			}
			Boolean scheduled = planElement.getMember(TemporalMember.class).getScheduled();
			if ((scheduled != null) && !scheduled.booleanValue()) {
				continue; // skip unscheduled elements
			}
			ConstraintsMember member = planElement.getMember(ConstraintsMember.class, false);
			if (member != null) {
				TemporalChain chain = member.getChain();
				if (chain != null) {
					EPlanElement currentRepresentative = chainToRepresentativeMap.get(chain);
					if (currentRepresentative == null) {
						chainToRepresentativeMap.put(chain, planElement);
						continue;
					}
					Date currentStart = currentRepresentative.getMember(TemporalMember.class).getStartTime();
					if (planElementStart.before(currentStart)) {
						chainToRepresentativeMap.put(chain, planElement);
					}
				}
			}
		}
		return chainToRepresentativeMap;
	}

	private static class ChainCache extends TemporalExtentsCache {
		
		@SuppressWarnings("unused")
		public ChainCache() {
			super();
		}

		public ChainCache(EPlan plan) {
			super(plan);
		}

		@Override
		public void set(EPlanElement element, TemporalExtent extent) {
			super.set(element, extent);
		}
	}
	
}
