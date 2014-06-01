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
package gov.nasa.arc.spife.europa.advisor;

import gov.nasa.arc.spife.europa.Europa;
import gov.nasa.arc.spife.europa.EuropaMember;
import gov.nasa.arc.spife.europa.ICpuWindow;
import gov.nasa.arc.spife.europa.IFlightRuleViolation;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.thread.ThreadUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.impl.WaiverPropertiesEntryImpl;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.advisor.ActivityWindow;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.RuleUtils;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.IConstraintNetworkAdvisor;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.INogoodPart;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodViolation;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TemporalChainLinkViolation;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TemporalDistanceViolation;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TemporalEndpointViolation;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jscience.physics.amount.Amount;

public class EuropaPlanAdvisor 
	extends PlanAdvisor 
	implements EuropaMember.EuropaMemberListener, IConstraintNetworkAdvisor 
{

	public static final String WAIVERS_KEY = "EuropaPlanAdvisor";
	private static final String CPU_DEF_NAME = "Awake";
	private static final String CPU_DURATION_PARAMETER = "time_on";
	
	private final EPlan plan;

	private Europa europa;
	private Set<IFlightRuleViolation> flightRuleViolations = Collections.emptySet();
	private Set<INogoodPart> nogoodParts = Collections.emptySet();
	private Set<BinaryTemporalConstraint> binaryTemporalViolations = Collections.emptySet();
	private Set<PeriodicTemporalConstraint> periodicTemporalViolations = Collections.emptySet();
	private boolean violationsFixable;

	public EuropaPlanAdvisor(PlanAdvisorMember planAdvisorMember) {
		super("Europa", planAdvisorMember);
		this.plan = planAdvisorMember.getPlan();
	}

	@Override
	public void dispose() {
		// nothing to cleanup
	}

	@Override
	public PlanAdvisorMember getPlanAdvisorMember() {
		return planAdvisorMember;
	}

	@Override
	protected List<? extends Advice> initialize() {
		EuropaMember em = EuropaMember.get(plan);
		em.addEuropaMemberListener(this);
		while (!isQuit()) {
			europa = em.getEuropa();
			if (europa != null) {
				ResourceUpdater updater = WrapperUtils.getMember(plan, ResourceUpdater.class);
				updater.joinInitialization();
				if (isQuit()) {
					return Collections.emptyList();
				}
				return updateAdvice();
			}
			ThreadUtils.sleep(500);
		}
		return Collections.emptyList();
	}
	
	@Override
	protected void ping() {
		europa.ping();
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
		if ((feature == AdvisorPackage.Literals.RULE_ADVISOR_MEMBER__WAIVERS)
			|| (feature == AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY__VALUE)
			|| (feature == AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES)
			|| (feature == AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER__PRIORITY)) {
		 	return true;
		}
		if ((feature == MemberPackage.Literals.RESOURCE_CONDITIONS_MEMBER__CONDITIONS)
			|| (feature == MemberPackage.Literals.CONDITIONS__ACTIVE)) {
			return true;
		}
		if ((feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS)
			|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS)
			|| (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN)) {
			return true;
		}
		if (ADParameterUtils.isParameterNotifier(notification.getNotifier())) {
			return true;
		}
		return false;
	}

	@Override
	protected List<? extends Advice> check(final List<Notification> notifications) {
		violationsFixable = false;
		LinkedHashSet<TemporalMember> temporalExtentUpdates = new LinkedHashSet<TemporalMember>();
		for (Notification notification : notifications) {
			processNotification(notification, temporalExtentUpdates);
		}
		sendExtentUpdates(temporalExtentUpdates);
		return updateAdvice();
	}

	@SuppressWarnings("unchecked")
	private void processNotification(Notification notification, LinkedHashSet<TemporalMember> temporalExtentUpdates) {
		Object notifier = notification.getNotifier();
		Object feature = notification.getFeature();
		Object newValue = notification.getNewValue();
		Object oldValue = notification.getOldValue();
		int eventType = notification.getEventType();
		if (feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN) {
			List<EPlanElement> removedElements = EPlanUtils.getElementsRemoved(notification);
			if (!removedElements.isEmpty()) {
				EPlanElement parent = (EPlanElement)notifier;
				europa.removeElements(parent, removedElements);
			}
			List<EPlanElement> addedElements = EPlanUtils.getElementsAdded(notification);
			if (!addedElements.isEmpty()) {
				europa.addElements(addedElements);
			}
		} else if ((feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION)
			|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)) {
			TemporalMember temporalMember = (TemporalMember)notifier;
			temporalExtentUpdates.add(temporalMember);
		} else if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED) {
			EMember member = (EMember)notifier;
			EPlanElement element = member.getPlanElement();
			boolean scheduled = (newValue == null || ((Boolean)newValue).booleanValue()); // default = true
			europa.setNodeScheduled(element, scheduled);
		} else if (feature == AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES) {
			EMember member = (EMember)notifier;
			EPlanElement element = member.getPlanElement();
			boolean waivingRules = (newValue != null && ((Boolean)newValue).booleanValue()); // default = false
			europa.setWaivingAllRulesForElement(element, waivingRules);
		} else if (feature == AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER__PRIORITY) {
			EMember member = (EMember)notifier;
			EPlanElement element = member.getPlanElement();
			europa.setNodePriority(element, newValue);
		} else if (feature == AdvisorPackage.Literals.RULE_ADVISOR_MEMBER__WAIVERS
				|| feature == AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY__VALUE) {
			EPlanElement element = null;
			if (feature == AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY__VALUE) {
				WaiverPropertiesEntryImpl entry = (WaiverPropertiesEntryImpl)notifier;
				if (EuropaPlanAdvisor.WAIVERS_KEY.equals(entry.getKey())) {
					EObject map = entry.eContainer();
					EMember member = (EMember)map.eContainer();
					element = member.getPlanElement();
				}
			}
			if ((feature == AdvisorPackage.Literals.RULE_ADVISOR_MEMBER__WAIVERS) && (newValue != null)) {
				//for (WaiverPropertiesEntryImpl entry : (Collection<WaiverPropertiesEntryImpl>)newValue) {
				for(WaiverPropertiesEntryImpl entry : EMFUtils.getAddedObjects(notification, WaiverPropertiesEntryImpl.class)) {
					if (EuropaPlanAdvisor.WAIVERS_KEY.equals(entry.getKey())) {
						EObject map = entry.eContainer();
						EMember member = (EMember)map.eContainer();
						element = member.getPlanElement();
						break;
					}
				}
			}
			if (element != null) {
				Set<ERule> waivedRules = RuleUtils.getWaivedRules(element);
				if (element instanceof EPlan) {
					europa.waivingRulesForPlan(waivedRules);
				} else if (element instanceof EActivity) {
					europa.waivingRulesForActivityInstance(element, waivedRules);
				}
			}
		} else if (feature == MemberPackage.Literals.RESOURCE_CONDITIONS_MEMBER__CONDITIONS) {
			if (eventType == Notification.ADD) {
				Conditions conditions = (Conditions)notification.getNewValue();
				if (conditions.isActive()) {
					europa.updateIncon(conditions);
				}
			} else if (eventType == Notification.ADD_MANY) {
				List<Conditions> conditions = (List<Conditions>)notification.getNewValue();
				for (Conditions c : conditions) {
					if (c.isActive()) {
						europa.updateIncon(c);
						break;
					}
				}
			}
		} else if (feature == MemberPackage.Literals.CONDITIONS__ACTIVE) {
			Conditions conditions = (Conditions)notifier;
			boolean active = notification.getNewBooleanValue();
			if (active) {
				europa.updateIncon(conditions);
			}
		} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS) {
			List<BinaryTemporalConstraint> oldConstraints = EMFUtils.getRemovedObjects(notification, BinaryTemporalConstraint.class);
			for (BinaryTemporalConstraint constraint : oldConstraints) {
				europa.removeConstraint(constraint);
			}
			List<BinaryTemporalConstraint> newConstraints = EMFUtils.getAddedObjects(notification, BinaryTemporalConstraint.class);
			for (BinaryTemporalConstraint constraint : newConstraints) {
				europa.addConstraint(constraint);
			}
		} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS) {
			List<PeriodicTemporalConstraint> oldConstraints = EMFUtils.getRemovedObjects(notification, PeriodicTemporalConstraint.class);
			for (PeriodicTemporalConstraint constraint : oldConstraints) {
				europa.removeConstraint(constraint);
			}
			List<PeriodicTemporalConstraint> newConstraints = EMFUtils.getAddedObjects(notification, PeriodicTemporalConstraint.class);
			for (PeriodicTemporalConstraint constraint : newConstraints) {
				europa.addConstraint(constraint);
			}
		} else if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN) {
			if (oldValue != null) {
				europa.removeChain((TemporalChain)oldValue);
			}
			if (newValue != null) {
				europa.addChain((TemporalChain)newValue);
			}
		} else if (feature == AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE) {
			if ((oldValue == null) && (newValue != null)) {
				if (notifier instanceof BinaryTemporalConstraint) {
					europa.removeConstraint((BinaryTemporalConstraint)notifier);
				} else if (notifier instanceof PeriodicTemporalConstraint) {
					europa.removeConstraint((PeriodicTemporalConstraint)notifier);
				}
			}
			if ((oldValue != null) && (newValue == null)) {
				if (notifier instanceof BinaryTemporalConstraint) {
					europa.addConstraint((BinaryTemporalConstraint)notifier);
				} else if (notifier instanceof PeriodicTemporalConstraint) {
					europa.addConstraint((PeriodicTemporalConstraint)notifier);
				}
			}
		} else if (ADParameterUtils.isParameterNotifier(notifier)) {
			EObject data = (EObject) notifier;
			EPlanElement element = (EPlanElement)data.eContainer();
			if (element instanceof EActivity) {
				EActivity activity = (EActivity) element;
				europa.updateParameters(activity, data);
			}
		} else {
			Logger logger = Logger.getLogger(EuropaPlanAdvisor.class);
			logger.debug("unhandled notification: " + feature);
		}
	}

	private void sendExtentUpdates(final LinkedHashSet<TemporalMember> temporalExtentUpdates) {
		EPlan plan = null;
		LinkedHashSet<EActivity> activities = new LinkedHashSet<EActivity>();
		for (TemporalMember temporalMember : temporalExtentUpdates) {
			EPlanElement planElement = temporalMember.getPlanElement();
			if (planElement instanceof EPlan) {
				plan = (EPlan)planElement;
			}
			EActivity activity = getActivityFor(planElement);
			if (activity != null) {
				activities.add(activity);
			}
		}
		if (plan != null) {
			// PHM 04/24/13 Workaround for erroneous plan bound updates from recent SPIFe changes.
			// Currently, there should be no legitimate plan bound changes anyway.
			//europa.updatePlanBound(plan);
		}
		europa.updateTemporalProperties(activities);
	}

	private EActivity getActivityFor(EPlanElement element) {
		if (element instanceof EActivity) {
			EObject container = element.eContainer();
			if (container instanceof EActivity) {
				return getActivityFor((EActivity)container);
			}
			return (EActivity)element;
		}
		return null;
	}

	public List<? extends Advice> updateAdvice() {
		List<Advice> advices = new ArrayList<Advice>();
		advices.addAll(getViolations());
		// TODO JRB : This is causing problems on the server with the MSL AD, there is no CPU_Windows class
		// Since nobody is using this, disabling for the time being
		//advices.addAll(getCPUWindows());
		violationsFixable = europa.isConsistent();
		return advices;
	}

	@Override
	public boolean areViolationsFixable() {
		return violationsFixable;
	}
	
	private List<Violation> getViolations() 
	{
		europa.generateViolations();
		List<Violation> violations = new ArrayList<Violation>();
		
		getFlightRuleViolations(violations);
		getTemporalViolations(violations);
		getNoGoods(violations);
		
		return violations;
	}

	protected void getFlightRuleViolations(List<Violation> violations)
	{
		flightRuleViolations = Collections.emptySet();
		
		List<? extends IFlightRuleViolation> violationList = europa.getFlightRuleViolations();
		for (IFlightRuleViolation v : violationList) {
			violations.add(new FlightRuleViolation(EuropaPlanAdvisor.this, v));
		}
		
		flightRuleViolations = new LinkedHashSet<IFlightRuleViolation>(violationList);		
	}
	
	protected void getTemporalViolations(List<Violation> violations)
	{
		binaryTemporalViolations = Collections.emptySet();
		periodicTemporalViolations = Collections.emptySet();
		
		List<? extends BinaryTemporalConstraint> binaryViolationList = europa.getTemporalDistanceViolations();
		List<? extends PeriodicTemporalConstraint> periodicViolationList = europa.getTimepointViolations();
		
		if (binaryViolationList == null) 
			binaryViolationList = Collections.emptyList();
		
		if (periodicViolationList == null) 
			periodicViolationList = Collections.emptyList();
		
		for (final BinaryTemporalConstraint spifeViolation : binaryViolationList) {
			if (europa.isChainConstraint(spifeViolation)) {
				EPlanElement elementA = spifeViolation.getPointA().getElement();
				TemporalExtent extentA = elementA.getMember(TemporalMember.class).getExtent();
				EPlanElement elementB = spifeViolation.getPointB().getElement();
				TemporalExtent extentB = elementB.getMember(TemporalMember.class).getExtent();
				violations.add(new TemporalChainLinkViolation(this, this, elementA, extentA, elementB, extentB) {
					@Override
					public boolean isCurrentlyViolated() {
						return binaryTemporalViolations.contains(spifeViolation);
					}
				});
			} 
			else {
				violations.add(new TemporalDistanceViolation(this, this, spifeViolation) {
					@Override
					public boolean isCurrentlyViolated() {
						return binaryTemporalViolations.contains(getConstraint());
					}
				});
			}
		}
		
		for (PeriodicTemporalConstraint spifeViolation : periodicViolationList) {
			violations.add(new TemporalEndpointViolation(this, this, spifeViolation) {
				@Override
				public boolean isCurrentlyViolated() {
					return periodicTemporalViolations.contains(getBound());
				}
			});
		}
		
		binaryTemporalViolations = new LinkedHashSet<BinaryTemporalConstraint>(binaryViolationList);
		periodicTemporalViolations = new LinkedHashSet<PeriodicTemporalConstraint>(periodicViolationList);		
	}
		
	protected void getNoGoods(List<Violation> violations)
	{
		List<? extends INogoodPart> nogoodList = (europa.isConsistent() ? null : europa.getNogood());
		if (nogoodList == null) {
			nogoodList = Collections.emptyList();
		}
		nogoodParts = new HashSet<INogoodPart>(nogoodList);
		for (INogoodPart nogoodPart : nogoodList) {
			violations.add(new NogoodViolation(EuropaPlanAdvisor.this, nogoodPart));
		}		
	}	
	
	private long cpu_valid_time = -1;
	@SuppressWarnings("unused")
	private List<ActivityWindow> getCPUWindows() {
		cpu_valid_time = System.currentTimeMillis();
		List<ActivityWindow> windows = new ArrayList<ActivityWindow>();
		for (ICpuWindow europaWindow : europa.getCPUWindows()) {
			String name = CPU_DEF_NAME;
			final Date start = europaWindow.getStartTime();
			final Amount<Duration> duration = DateUtils.subtract(europaWindow.getEndTime(), start);
			final Map<String, Object> parameterValues = new LinkedHashMap<String, Object>();
			parameterValues.put(CPU_DURATION_PARAMETER, duration);
			EActivityDef def = ActivityDictionary.getInstance().getActivityDef(name);
			if (def == null) {
				Logger logger = Logger.getLogger(EuropaPlanAdvisor.class);
				logger.warn("Couldn't find def with name: " + name);
				continue;
			}
			final EActivity activity = PlanFactory.getInstance().createActivity(def);
			TransactionUtils.writing(activity, new Runnable() {
				@Override
				public void run() {
					TemporalMember temporalMember = activity.getMember(TemporalMember.class);
					temporalMember.setStartTime(start);
					temporalMember.setDuration(duration);
					for (Map.Entry<String, Object> entry : parameterValues.entrySet()) {
						String parameterName = entry.getKey();
						Object parameterObject = entry.getValue();
						try {
							ADParameterUtils.setParameterObject(activity, parameterName, parameterObject);
						} catch (UndefinedParameterException e) {
							Logger logger = Logger.getLogger(EuropaPlanAdvisor.class);
							logger.warn("bad parameter value from europa: ", e);
						}
					}
				}
			});
			windows.add(new CpuActivityWindow(this, activity));
		}
		return windows;
	}

	public boolean isCurrentlyViolated(IFlightRuleViolation violation) {
		return flightRuleViolations.contains(violation);
	}
	
	@Override
	public boolean isCurrentlyViolated(INogoodPart nogoodPart) {
		return nogoodParts.contains(nogoodPart);
	}

	public boolean isCurrentlySuggested(CpuActivityWindow cpuActivityWindow) {
		return cpu_valid_time < cpuActivityWindow.getValidTime();
	}

	@Override
	public ViolationFixes fixViolations(ISelection selection) {
		if (!EuropaPreferences.isUseEuropaViolations()) {
			return null;
		}
		if (europa == null) {
			// not ready yet
			return null;
		}
		if (!europa.isConsistent()) {
			return null;
		}
		Logger logger = Logger.getLogger(EuropaPlanAdvisor.class);
		Set<EPlanElement> elements = null;
		if (selection instanceof IStructuredSelection) {
			elements = PlanEditorUtil.emfFromSelection(selection);
		}
		logger.info("fix violations sent to europa for plan: " + plan.getName());
		long start = System.currentTimeMillis();
		ViolationFixes fixes = europa.getViolationFixes(this, elements);
		long end = System.currentTimeMillis();
		logger.info("fix violations returned from europa for plan: " + plan.getName() + " -- " + (end - start) / 1000.0 + " seconds");
		return fixes;
	}
	
	/*
	 * Legacy functionality
	 */
	
	public void getConsistencyBoundsMap() {
		europa.getConsistencyBoundsMap();
	}
	
	public void startViolationFixes(List<EPlanElement> elements) {
		europa.startViolationFixes(elements);
	}
	
	public void getMoreFixes(List<EPlanElement> elements) {
		europa.getMoreFixes(elements);
	}
	
	public void finishFixing(List<EPlanElement> elements) {
		europa.finishFixingViolations(this, elements);
	}

	@Override
	public void initializationStarted(Europa europa) {
		// Ignore notifications that happened before Europa was initialized
		emptyNotificationQueue();
	}

	@Override
	public void initializationFinished(Europa europa) {
		// Nothing to do	
	}

	/*
	 * Utility methods
	 */
	
}
