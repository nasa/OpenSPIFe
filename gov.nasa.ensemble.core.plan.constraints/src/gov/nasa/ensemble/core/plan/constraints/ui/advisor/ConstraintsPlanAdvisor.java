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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestedStartTime;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.network.SpifePlanConstraintInfo;
import gov.nasa.ensemble.core.plan.constraints.network.SpifePlanModifier;
import gov.nasa.ensemble.core.plan.constraints.network.TemporalNetworkMember;
import gov.nasa.ensemble.core.plan.constraints.ui.preference.ConstraintsPreferences;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ConstraintsPlanAdvisor extends PlanAdvisor implements IConstraintNetworkAdvisor {

	private EPlan plan;
	public static final long MEETS_TOLERANCE = 0L;
	private Set<INogoodPart> nogoodParts = Collections.emptySet();

	public ConstraintsPlanAdvisor(PlanAdvisorMember planAdvisorMember) {
		super("Constraints", planAdvisorMember);
		this.plan = planAdvisorMember.getPlan();
	}
	
	@Override
	public void dispose() {
		plan = null;
	}

	@Override
	protected List<? extends Violation> initialize() {
		return TransactionUtils.reading(plan, new RunnableWithResult.Impl<List<? extends Violation>>() {
			@Override
			public void run() {
				if (isQuit()) {
					return;
				}
				final List<EPlanElement> affectedElements = new ArrayList<EPlanElement>();
				new PlanVisitor() {
					@Override
					protected void visit(EPlanElement element) {
					    affectedElements.add(element);
					}
				}.visitAll(plan);
				List<Violation> violations = findViolations(affectedElements);
				setResult(violations);
			}
		});
	}
	
	@Override
	protected boolean affectsViolations(Notification notification) {
		Object feature = notification.getFeature();
		if ((feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN)
			|| (feature == PlanPackage.Literals.EACTIVITY__CHILDREN)) {
			return true;
		}
		if ((feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED)) {
			return true;
		}
		if ((feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS)
			|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS)
			|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN)) {
			return true;
		}
		if (feature == AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE) {
			return true;
		}
		if (ADParameterUtils.isActivityAttributeOrParameter(notification.getNotifier())) {
			return true;
		}
		return false;
	}

	@Override
	protected List<? extends Violation> check(final List<Notification> notifications) {
		return TransactionUtils.reading(plan, new RunnableWithResult.Impl<List<? extends Violation>>() {
			@Override
			public void run() {
				Set<EPlanElement> affectedElements = getAffectedElements(notifications);
				List<Violation> violations = findViolations(affectedElements);
				setResult(violations);
			}
		});
	}
	
	private Set<EPlanElement> getAffectedElements(List<Notification> notifications) {
		final Set<EPlanElement> affectedElements = new LinkedHashSet<EPlanElement>();
		class RemovalVisitor extends PlanVisitor {
			@Override
            protected void visit(EPlanElement element) {
				affectedElements.remove(element);
            }
		}
		class AdditionVisitor extends PlanVisitor {
			@Override
			protected void visit(EPlanElement element) {
			    affectedElements.add(element);
			}
		}
		PlanVisitor removalVisitor = new RemovalVisitor();
		PlanVisitor additionVisitor = new AdditionVisitor();
		for (Notification notification : notifications) {
			Object feature = notification.getFeature();
			Object notifier = notification.getNotifier();
			if ((feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN)
				|| (feature == PlanPackage.Literals.EACTIVITY__CHILDREN)) {
				removalVisitor.visitAll(EPlanUtils.getElementsRemoved(notification));
				additionVisitor.visitAll(EPlanUtils.getElementsAdded(notification));
			} else if ((feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
					|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION)
					|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)
					|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED)) {
				EMember member = (EMember)notifier;
				affectedElements.add(member.getPlanElement());
			} else if ((feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN)
					|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS)
					|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS)) {
				EMember member = (EMember)notifier;
				affectedElements.add(member.getPlanElement());
			} else if (feature == AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE) {
				if (notifier instanceof BinaryTemporalConstraint) {
					BinaryTemporalConstraint binary = (BinaryTemporalConstraint) notifier;
					affectedElements.add(binary.getPointA().getElement());
					affectedElements.add(binary.getPointB().getElement());
				} else if (notifier instanceof PeriodicTemporalConstraint) {
					PeriodicTemporalConstraint periodic = (PeriodicTemporalConstraint) notifier;
					affectedElements.add(periodic.getPoint().getElement());
				}
			} else if (ConstraintUtils.areAnchorsAllowed() && ADParameterUtils.isActivityAttributeOrParameter(notifier)) {
				EPlanElement planElement =  ADParameterUtils.getActivityAttributeOrParameter(notifier);
				List<TemporalConstraint> constraints = new ArrayList<TemporalConstraint>();
				constraints.addAll(ConstraintUtils.getBinaryConstraints(planElement, false));
				constraints.addAll(ConstraintUtils.getPeriodicConstraints(planElement, false));
				for (TemporalConstraint constraint : constraints) {
					if (constraint instanceof BinaryTemporalConstraint) {
						BinaryTemporalConstraint btc = (BinaryTemporalConstraint) constraint;
						if (ConstraintUtils.isAnchorPointForElement(btc.getPointA(), planElement, (EStructuralFeature) feature)
								|| ConstraintUtils.isAnchorPointForElement(btc.getPointB(), planElement, (EStructuralFeature) feature)) {
							affectedElements.add(planElement);
							break;
						}
					} else if (constraint instanceof PeriodicTemporalConstraint) {
						PeriodicTemporalConstraint ptc = (PeriodicTemporalConstraint) constraint;
						if (ConstraintUtils.isAnchorPointForElement(ptc.getPoint(), planElement, (EStructuralFeature) feature)) {
							affectedElements.add(planElement);
							break;
						}
					}
				}
			}
		}
		return affectedElements;
	}

	private List<Violation> findViolations(Collection<EPlanElement> elements) {
		if (!ConstraintsPreferences.isFindTemporalViolations()) {
			return Collections.emptyList();
		}
		// collect things that could be violated
		Set<BinaryTemporalConstraint> relations = new LinkedHashSet<BinaryTemporalConstraint>();
		List<PeriodicTemporalConstraint> bounds = new ArrayList<PeriodicTemporalConstraint>();
		LinkedHashSet<TemporalChain> chains = TemporalChainUtils.getChains(elements, false);
		for (EPlanElement element : elements) {
			relations.addAll(ConstraintUtils.getBinaryConstraints(element, false));
			bounds.addAll(ConstraintUtils.getPeriodicConstraints(element, false));
		}
		// find the violations
		List<Violation> violations = new ArrayList<Violation>();
		for (BinaryTemporalConstraint relation : relations) {
			if (relation.isViolated()) {
				violations.add(new TemporalDistanceViolation(this, this, relation));
			}
		}
		for (PeriodicTemporalConstraint bound : bounds) {
			if (bound.isViolated()) {
				violations.add(new TemporalEndpointViolation(this, this, bound));
			}
		}
		for (TemporalChain chain : chains) {
			EPlanElement lastPlanElement = null;
			TemporalExtent lastExtent = null;
			for (EPlanElement planElement : chain.getElements()) {
				if (planElement == null) {
					continue;
				}
				if (SpifePlanUtils.getScheduled(planElement) == TriState.FALSE) {
					continue;
				}
				TemporalExtent extent = planElement.getMember(TemporalMember.class).getExtent();
				if (extent == null) {
					continue;
				}
				if (lastExtent != null) {
					if (TemporalChainLinkViolation.isViolated(lastExtent.getEnd(), extent.getStart())) {
						violations.add(new TemporalChainLinkViolation(this, this, lastPlanElement, lastExtent, planElement, extent));
					}
				}
				lastPlanElement = planElement;
				lastExtent = extent;
			}
		}
		TemporalNetworkMember<?> networkMember = TemporalNetworkMember.get(plan);
		if (networkMember.isConsistent()) {
			this.nogoodParts = Collections.emptySet();
		} else {
			Set<INogoodPart> parts = networkMember.getNogoodParts();
			for (INogoodPart part : parts) {
				violations.add(new NogoodViolation(this, part));
			}
			this.nogoodParts = parts;
		}
		return violations;
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.core.plan.constraints.advisor.IConstraintNetworkAdvisor#areViolationsFixable()
	 */
	@Override
	public boolean areViolationsFixable() {
		TemporalNetworkMember networkMember = TemporalNetworkMember.get(plan);
		return networkMember.isConsistent();
	}
	
	@Override
	public boolean isCurrentlyViolated(INogoodPart nogoodPart) {
		return nogoodParts.contains(nogoodPart);
	}
	
	@Override
	public ViolationFixes fixViolations(ISelection selection) {
		TemporalNetworkMember networkMember = TemporalNetworkMember.get(plan);
		if (networkMember.isConsistent()) {
			return getSolvedFixes(selection);
		}
		Logger.getLogger(ConstraintsPlanAdvisor.class).debug("using simple fix violations when network is inconsistent.");
		return getSimpleFixes(selection);
	}

	private ViolationFixes getSimpleFixes(ISelection selection) {
		List<SuggestedStartTime> startTimes = new ArrayList<SuggestedStartTime>();
		for (EActivity activity : getActivities(selection)) {
			SuggestedStartTime startTime = getSimpleBounds(activity);
			if (startTime != null) {
				startTimes.add(startTime);
			}
		}
		if (startTimes.isEmpty()) {
			return null;
		}
		return new ViolationFixes(this, startTimes, Collections.<EPlanElement>emptyList(), Collections.<EPlanElement>emptyList());
	}
	
	private SuggestedStartTime getSimpleBounds(EPlanElement element) {
		Date latestEarliestStart = null;
		Date earliestLatestStart = null;
		Date latestEarliestEnd = null;
		Date earliestLatestEnd = null;
		for (PeriodicTemporalConstraint timepointConstraint : ConstraintUtils.getPeriodicConstraints(element, false)) {
			if (timepointConstraint.getWaiverRationale() != null) {
				continue; // skip this one because it is waived
			}
			Date earliest = ConstraintUtils.getPeriodicConstraintEarliestDate(timepointConstraint);
			Date latest = ConstraintUtils.getPeriodicConstraintLatestDate(timepointConstraint);
			switch (timepointConstraint.getPoint().getEndpoint()) {
			case START:
				latestEarliestStart = pickLater(latestEarliestStart, earliest);
				earliestLatestStart = pickEarlier(earliestLatestStart, latest);
				break;
			case END:
				latestEarliestEnd = pickLater(latestEarliestEnd, earliest);
				earliestLatestEnd = pickEarlier(earliestLatestEnd, latest);
				break;
			default:
				Logger.getLogger(SpifePlanConstraintInfo.class).warn("unexpected timepoint: " + timepointConstraint.getPoint().getEndpoint());
			}
		}
		TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
		if (extent != null) {
			if (latestEarliestEnd != null) {
				Date earliest = DateUtils.subtract(latestEarliestEnd, extent.getDurationMillis());
				latestEarliestStart = pickLater(latestEarliestStart, earliest);
			}
			if (earliestLatestEnd != null) {
				Date latest = DateUtils.subtract(earliestLatestEnd, extent.getDurationMillis());
				earliestLatestStart = pickEarlier(earliestLatestStart, latest);
			}
		}
		if ((latestEarliestStart == null) || (earliestLatestStart == null)) {
			return null;
		}
		return new SuggestedStartTime(element, latestEarliestStart, latestEarliestStart, earliestLatestStart);
	}

	private static Date pickEarlier(Date earliestLatestStart, Date latest) {
		if (latest != null) {
			if (earliestLatestStart == null) {
				earliestLatestStart = latest;
			} else {
				earliestLatestStart = DateUtils.earliest(latest, earliestLatestStart);
			}
		}
		return earliestLatestStart;
	}

	private static Date pickLater(Date latestEarliestStart, Date earliest) {
		if (earliest != null) {
			if (latestEarliestStart == null) {
				latestEarliestStart = earliest;
			} else {
				latestEarliestStart = DateUtils.latest(earliest, latestEarliestStart);
			}
		}
		return latestEarliestStart;
	}

	private ViolationFixes getSolvedFixes(ISelection selection) {
		Map<EPlanElement, TemporalExtent> solvedStarts = getSolvedStarts(selection);
		if (solvedStarts != null) {
			List<SuggestedStartTime> suggestions = new ArrayList<SuggestedStartTime>();
			for (Map.Entry<EPlanElement, TemporalExtent> entry : solvedStarts.entrySet()) {
				EPlanElement planElement = entry.getKey();
				TemporalExtent extent = entry.getValue();
				if ( extent != null ) {
				  Date date = extent.getStart();
				  suggestions.add(new SuggestedStartTime(planElement, date, date, date));
				}
			}
			return new ViolationFixes(this, suggestions, Collections.<EPlanElement>emptyList(), Collections.<EPlanElement>emptyList());
		}
		return null;
	}

	private Map<EPlanElement, TemporalExtent> getSolvedStarts(ISelection selection) {
		final TemporalExtentsCache initialExtents = new TemporalExtentsCache(plan);
		final Map<EPlanElement, TemporalExtent> solvedExtents = new HashMap<EPlanElement, TemporalExtent>();
		final IPlanModifier modifier = new SpifePlanModifier();
		modifier.initialize(plan);
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
					EActivity activity = (EActivity) element;
					TemporalExtent currentExtent = solvedExtents.get(activity);
					Date currentStart;
					if (currentExtent != null) {
						currentStart = currentExtent.getStart();
					} else {
						currentStart = activity.getMember(TemporalMember.class).getStartTime();
					}
					Map<EPlanElement, TemporalExtent> changedTimes = modifier.moveToStart(activity, currentStart, initialExtents);
					Iterator<Entry<EPlanElement, TemporalExtent>> entryIterator = changedTimes.entrySet().iterator();
					while (entryIterator.hasNext()) {
						Entry<EPlanElement, TemporalExtent> entry = entryIterator.next();
						EPlanElement planElement = entry.getKey();
						TemporalExtent oldExtent = initialExtents.get(planElement);
						if (oldExtent != null) {
							TemporalExtent newExtent = entry.getValue();
							if (newExtent.equals(oldExtent)) {
								entryIterator.remove();
							} else {
								TemporalExtent solvedExtent = solvedExtents.get(planElement);
								if (solvedExtent != null) {
									Date solvedStart = solvedExtent.getStart();
									Date oldStart = oldExtent.getStart();
									Date newStart = newExtent.getStart();
									if (Math.abs(DateUtils.subtract(oldStart, solvedStart)) >
										Math.abs(DateUtils.subtract(oldStart, newStart))) {
										entryIterator.remove();
									}
								}
							}
						}
					}
					solvedExtents.putAll(changedTimes);
			    }
			}
		}.visitAll(plan);
		List<EActivity> affectableActivities = getActivities(selection);
		solvedExtents.keySet().retainAll(affectableActivities);
		if (solvedExtents.isEmpty()) {
			return null;
		}
		return solvedExtents;
	}

	private List<EActivity> getActivities(ISelection selection) {
		final Set<EActivity> activitySet = new LinkedHashSet<EActivity>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Set<EPlanElement> selectedElements = PlanEditorUtil.emfFromSelection(structuredSelection);
			class PlanFound extends RuntimeException { /* */ }
			try {
				new PlanVisitor() {
					@Override
					protected void visit(EPlanElement element) {
					    if (element instanceof EActivity) {
							EActivity activity = (EActivity) element;
					    	activitySet.add(activity);
					    } else if (element instanceof EPlan) {
					    	throw new PlanFound();
					    }
					}
				}.visitAll(selectedElements);
			} catch (PlanFound found) {
				activitySet.clear();
			}
		}
		if (activitySet.isEmpty()) {
			List<EActivity> activities = EPlanUtils.getActivities(plan);
			Collections.reverse(activities); // heuristic to prefer to keep the plan earlier
			return activities;
		}
		return new ArrayList<EActivity>(activitySet);
	}

}
