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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleOption;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.ModelingConfigurationRegistry;
import gov.nasa.ensemble.core.plan.advisor.markers.MarkerManagementListener;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.domain.EditingDomain;

public class PlanAdvisorMember implements IMember {
	

	private List<PlanAdvisor> advisors;
	private final Map<Violation, ViolationTracker> violationToTracker = new LinkedHashMap<Violation, ViolationTracker>();
	private final Set<AdvisorListener> advisorListeners = new LinkedHashSet<AdvisorListener>();
	private final Set<PlanAdvisor> updatingAdvisors = new HashSet<PlanAdvisor>();
	private EPlan plan;
	private MarkerManagementListener markerListener = null;
	private volatile boolean listening = true;
	private volatile boolean updatePending = false;
	private volatile boolean outOfDate = false;
	
	public static PlanAdvisorMember get(EPlan plan) {
		return WrapperUtils.getMember(plan, PlanAdvisorMember.class);
	}

	/* package */  PlanAdvisorMember(EPlan plan) {
		this.plan = plan;
		markerListener = new MarkerManagementListener(plan);
		if (disabledForThisPlan()) {
			this.advisors = Collections.EMPTY_LIST;
		} else {
			this.advisors = Collections.unmodifiableList(instantiateAdvisors(plan));
			this.updatingAdvisors.addAll(advisors);
			if (!CommonPlugin.isJunitRunning()) {
				addViolationsListener(markerListener);
			}
			startAdvisors();
		}
	}

	private boolean disabledForThisPlan() {
		EditingDomain domain = EMFUtils.getAnyDomain(plan);
		if (domain==null || domain.getResourceSet()==null) return true;
		return Boolean.TRUE.equals(
				domain.getResourceSet().getLoadOptions().get(EnsembleOption.OPTION_TO_DISABLE_PLAN_ADVISOR));
	}

	public boolean isListening() {
		return listening;
	}
	
	public boolean isOutOfDate() {
		return outOfDate;
	}

	public void setListening(boolean listening) {
		boolean wasListening = this.listening;
		if (listening && !wasListening) {
			this.listening = listening;
			this.outOfDate = false;
			removeViolationTrackers(getViolationTrackers());
			for (PlanAdvisor advisor : getAdvisors()) {
				advisor.updateInitialAdvice();
			}
		} else if (!listening && wasListening) {
			updatePending = true;
			this.listening = listening;
		}
	}

	/**
	 * For testing, allows a single plan advisor to be instantiated and run
	 * 
	 * @param plan the EPlan with which to test the plan advisor
	 * @param factory an IPlanAdvisorFactory to be used to create the plan advisor
	 * @return PlanAdvisor the instantiated plan advisor to be used for testing
	 */
	public static final PlanAdvisor testAdvisor(EPlan plan, IPlanAdvisorFactory factory) {
		return testAdvisor(plan, factory, true);
	}

	/**
	 * For testing, allows a single plan advisor to be instantiated. The plan advisor
	 * may optionally be run in a separate thread
	 * 
	 * @param plan the EPlan with which to test the plan advisor
	 * @param factory an IPlanAdvisorFactory to be used to create the plan advisor
	 * @param advisorThread true if the plan advisor's thread should be started, else false
	 * @return PlanAdvisor the instantiated plan advisor to be used for testing
	 */
	public static final PlanAdvisor testAdvisor(EPlan plan, final IPlanAdvisorFactory factory, final boolean advisorThread) {
		final PlanAdvisor[] result = new PlanAdvisor[] { null };
		new PlanAdvisorMember(plan) {
			@Override
			protected List<PlanAdvisor> instantiateAdvisors(EPlan plan) {
				result[0] = factory.create(this);
				return Collections.singletonList(result[0]);
			}
			@Override
			protected void startAdvisors() {
				if (advisorThread) {
					result[0].start();
				}
			}
		};
		return result[0];
	}
	
	@Override
	public void dispose() {
		removeViolationsListener(markerListener);
		markerListener.dispose();
		advisorListeners.clear();
		updatingAdvisors.clear();
		for (PlanAdvisor advisor : advisors) {
			advisor.quit();
			try {
				advisor.dispose();
			} catch (Exception e) {
				LogUtil.error("disposing plan advisor "+advisor, e);
			}
		}
		plan = null;
		violationToTracker.clear();
	}

	public EPlan getPlan() {
		return plan;
	}

	protected List<PlanAdvisor> instantiateAdvisors(EPlan plan) {
		List<PlanAdvisor> advisors = new ArrayList<PlanAdvisor>();
		if (ModelingConfigurationRegistry.arePlanAdvisorsEnabled(plan)) {
			List<IPlanAdvisorFactory> factories = PlanAdvisorFactoryRegistry.instance.getInstances();
			List<String> disabledFactoryNames = ModelingConfigurationRegistry.getDisabledPlanAdvisorFactories(plan);
			for (IPlanAdvisorFactory factory : factories) {
				if (disabledFactoryNames.contains(factory.getClass().getName())) {
					continue;
				}
				try {
					PlanAdvisor advisor = factory.create(this);
					if (advisor != null) {
						advisors.add(advisor);
					}
				} catch (RuntimeException e) {
					LogUtil.error("failed to create a plan advisor from factory: " + factory.getClass() + " for plan: " + plan.getName(), e);
				}
			}
		}
		return advisors;
	}
	
	/**
	 * Starts a thread for each of the plan advisors
	 */
	protected void startAdvisors() {
		for (PlanAdvisor advisor : advisors) {
			advisor.start();
		}
	}

	/**
	 * Returns a list of all the advisors.
	 * This list can not be modified.
	 * @return
	 */
	public List<PlanAdvisor> getAdvisors() {
		return advisors;
	}
	
	public <T extends PlanAdvisor> T getPlanAdvisor(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		for (PlanAdvisor advisor : advisors) {
			if (clazz.equals(advisor.getClass())) {
				return (T) advisor;
			}
		}
		return null;
	}

	/**
	 * Returns a list of the plan advisors currently updating their violations.
	 * @return
	 */
	public List<PlanAdvisor> getUpdatingAdvisors() {
		synchronized (updatingAdvisors) {
			return new ArrayList<PlanAdvisor>(updatingAdvisors);
		}
	}

	/**
	 * Returns a list of all the violations from any advisor
	 * The list is a copy that is safe for the caller to modify.
	 * @return
	 */
	public List<ViolationTracker> getViolationTrackers() {
		synchronized (violationToTracker) {
			return new LinkedList<ViolationTracker>(violationToTracker.values());
		}
	}
	
	/**
	 * Returns the Violation associated with a IMarker, if any
	 */
	public Violation getMarkerViolation(IMarker marker) {
		if (markerListener != null) {
			return markerListener.getMarkerViolation(marker);
		}
		return null;
	}
	
	/**
	 * Returns the markers associated with an EPlanElement, if any
	 */
	public List<IMarker> getPlanElementMarkers(EPlanElement element) {
		if (markerListener != null) {
			return markerListener.getPlanElementMarkers(element);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	/**
	 * Returns a list of the current advisor listeners.
	 * The list is a copy that is safe for the caller to traverse.
	 * @return
	 */
	private List<AdvisorListener> getAdvisorListeners() {
		synchronized (advisorListeners) {
			return new ArrayList<AdvisorListener>(advisorListeners);
		}
	}

	/**
	 * Add some new violations from any advisor
	 * @param addedViolations
	 */
	public void updateAdvice(PlanAdvisor advisor, List<? extends Advice> advices) {
		if ((advices != null) && !advices.isEmpty()) {
			final Set<ViolationTracker> newTrackers = new LinkedHashSet<ViolationTracker>(advices.size());
			final Set<ActivityWindow> newWindows = new LinkedHashSet<ActivityWindow>();
			for (Advice advice : advices) {
				if (advice instanceof Violation) {
					Violation violation = (Violation) advice;
					if (advisor != violation.getAdvisor()) {
						String message = "Updating advisor didn't match the violation advisor.";
						LogUtil.warn(message);
					}
					synchronized (violationToTracker) {
						ViolationTracker existingTracker = violationToTracker.get(violation);
						if (existingTracker != null) {
							if (newTrackers.contains(existingTracker)) {
								String message = "two new violations from the advisor '" + advisor.getName();
								message += "' matched each other.  violation updates might not work correctly.";
								LogUtil.warn(message);
							}
							existingTracker.setViolation(violation);
						} else {
							ViolationTracker tracker = new ViolationTracker(violation);
							violationToTracker.put(violation, tracker);
							newTrackers.add(tracker);
						}
					}
				}
				if (advice instanceof ActivityWindow) {
					newWindows.add((ActivityWindow)advice);
				}
			}
			if (!newTrackers.isEmpty()) {
				fireViolationsAdded(newTrackers);
			}
			if (!newWindows.isEmpty()) {
				fireWindowsChanged(newWindows);
			}
		}
		updatedViolations(advisor);
	}

	/**
	 * Note: this should only be called from a UI element!
	 *       do NOT remove violations from your advisor!
	 *
	 * Remove the violations from the list of kept violations.
	 * Returns true if any of the violations were in the list.
	 * @param removedViolationTrackers
	 * @return
	 */
	public boolean removeViolationTrackers(final Collection<? extends ViolationTracker> removedViolationTrackers) {
		Set<ViolationTracker> oldTrackers = new LinkedHashSet<ViolationTracker>(removedViolationTrackers);
		synchronized (violationToTracker) {
			oldTrackers.retainAll(violationToTracker.values());
			if (oldTrackers.isEmpty()) {
				return false;
			}
			for (ViolationTracker oldViolationTracker : oldTrackers) {
				violationToTracker.remove(oldViolationTracker.getViolation());
			}
		}
		fireViolationsRemoved(oldTrackers);
		return true;
	}

  /**
   * Gather what-if violations from each of the advisors and combine them in one
   * collection.
   * 
   * @return the combined what-if violation map
   */
  public Map< Date, List< Violation > > getWhatIfViolations( EPlanElement activityElement,
                                                             Date start, Date end ) {
    // init variables
    if ( activityElement == null ) return Collections.EMPTY_MAP;
    EActivity activity = (EActivity)activityElement;
    Map< Date, List< Violation > > whatIfViolations =
        new TreeMap< Date, List< Violation > >();
    PlanAdvisorMember advisorMember =
        WrapperUtils.getMember( plan, PlanAdvisorMember.class );
    List< PlanAdvisor > advisors = advisorMember.getAdvisors();
    
    // combine whatIfViolations from each advisor
    for ( PlanAdvisor advisor : advisors ) {
      Map< Date, List< ? extends Violation > > wiv =
          advisor.getWhatIfViolations( activity, start, end );
      if ( wiv != null && !wiv.isEmpty() ) {
        for ( Entry< Date, List< ? extends Violation > > entry : wiv.entrySet() ) {
          List< Violation > list = whatIfViolations.get( entry.getKey() );
          if ( list == null ) {
            list = new ArrayList< Violation >();
            whatIfViolations.put( entry.getKey(), list );
          }
          list.addAll(entry.getValue());
        }
      }
    }
    return whatIfViolations;
  }
  
//  private void addWhatIfViolations( EPlanElement activity, Date date,
//                                    List< ? extends Violation > violations ) {
//    
//  }

	/**
	 * Add these notifications to the list to be processed asynchronously.
	 *
	 * @param notifications
	 */
	public void enqueue(List<Notification> notifications) {
		if (!listening) {
			if (updatePending) {
				for (PlanAdvisor advisor : advisors) {
					advisor.emptyNotificationQueue();
				}
				outOfDate = true;
				fireAdvisorsUpdated();
				updatePending = false;
			}
			return;
		}
		boolean updating = false;
		for (PlanAdvisor advisor : advisors) {
			if (advisor.enqueue(notifications)) {
				synchronized (updatingAdvisors) {
					updatingAdvisors.add(advisor);
				}
				updating = true;
			}
		}
		if (updating) {
			fireAdvisorsUpdating();
		}
	}

	/**
	 * Remove any violations that are not current
	 * @param listener
	 */
	public void removeFixedViolations() {
		List<ViolationTracker> fixedViolationTrackers = new ArrayList<ViolationTracker>();
		for (ViolationTracker tracker : getViolationTrackers()) { 
			Violation violation = tracker.getViolation();
			if (!violation.isCurrentlyViolated()) {
				fixedViolationTrackers.add(tracker);
			}
		}
		removeViolationTrackers(fixedViolationTrackers);
	}

	public void addViolationsListener(AdvisorListener listener) {
		synchronized (advisorListeners) {
			advisorListeners.add(listener);
		}
	}

	public void removeViolationsListener(AdvisorListener listener) {
		synchronized (advisorListeners) {
			advisorListeners.remove(listener);
		}
	}

	/*
	 * Utility methods
	 */

	/**
	 * Process existing violations, remove the obsolete
	 * violations, and notify listeners that the
	 * remaining violations need to be checked to see
	 * if they are current.
	 */
	private void updatedViolations(PlanAdvisor advisor) {
		Set<ViolationTracker> obsoleteTrackers = new LinkedHashSet<ViolationTracker>();
		for (ViolationTracker tracker : getViolationTrackers()) {
			Violation violation = tracker.getViolation();
			if (violation.getAdvisor() == advisor) {
				if (violation.isObsolete()) {
					obsoleteTrackers.add(tracker);
				}
			}
		}
		if (!obsoleteTrackers.isEmpty()) {
			removeViolationTrackers(obsoleteTrackers);
		}
		synchronized (updatingAdvisors) {
			updatingAdvisors.remove(advisor);
		}
		fireAdvisorsUpdated();
	}

	private void fireWindowsChanged(Set<ActivityWindow> windows) {
		for (AdvisorListener listener : getAdvisorListeners()) {
			try {
				listener.windowsChanged(windows);
            } catch (Exception e) {
            	LogUtil.error("fireWindowsChanged", e);
            }
		}
	}

	private void fireViolationsAdded(Set<ViolationTracker> newViolations) {
		for (AdvisorListener listener : getAdvisorListeners()) {
			try {
				listener.violationsAdded(newViolations);
            } catch (Exception e) {
            	LogUtil.error("fireViolationsAdded", e);
            }
		}
	}

	private void fireViolationsRemoved(Set<ViolationTracker> oldViolations) {
		for (AdvisorListener listener : getAdvisorListeners()) {
			try {
				listener.violationsRemoved(oldViolations);
            } catch (Exception e) {
            	LogUtil.error("fireViolationsRemoved", e);
			}
		}
	}

	private void fireAdvisorsUpdating() {
		for (AdvisorListener listener : getAdvisorListeners()) {
			try {
				listener.advisorsUpdating();
            } catch (Exception e) {
            	LogUtil.error("fireAdvisorsUpdating", e);
            }
		}
	}

	private void fireAdvisorsUpdated() {
		for (AdvisorListener listener : getAdvisorListeners()) {
			try {
				listener.advisorsUpdated();
            } catch (Exception e) {
            	LogUtil.error("fireAdvisorsUpdated", e);
			}
		}
	}

}
