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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.emf.util.ControlNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.context.Context;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jface.viewers.ISelection;

public abstract class PlanAdvisor {
	
	private static final int PING_PERIOD_IN_MILLISECONDS = 5*60*1000; // five minutes
	private static final int NOTIFICATION_BATCH_WAIT_PERIOD_IN_MILLISECONDS = 50;
	protected PlanAdvisorMember planAdvisorMember;
	private final Object queueLock = new int[0];
	protected final Object updateLock = new int[0];
	private final Queue<Notification> queuedNotifications = new ConcurrentLinkedQueue();
	private volatile boolean paused = false;
	private volatile boolean keepRunning = true;
	private final String name;
	private final Thread thread;
	
	/**
	 * 
	 * @param name the name of this advisor, suitable for printing.
	 * @param planAdvisorMember
	 */
	public PlanAdvisor(String name, PlanAdvisorMember planAdvisorMember) {
		this.name = name;
		this.planAdvisorMember = planAdvisorMember;
		this.thread = new PlanAdvisorThread(name);
	}
	
	public Collection getDependentMemberClasses() {
		return Collections.emptyList();
	}

	public final String getName() {
		return name;
	}
	
	public PlanAdvisorMember getPlanAdvisorMember() {
		return planAdvisorMember;
	}
	
	public final boolean isOutOfDate() {
		return planAdvisorMember != null && planAdvisorMember.isOutOfDate();
	}
	
	/**
	 * Implement to cleanup when this plan is closed, if necessary.
	 */
	public abstract void dispose();
	
	/**
	 * Find fixes for the violations in the selection, 
	 * or in the whole plan if the selection is null.
	 * @param selection
	 * @return the fixes, or null if no fixes are suggested
	 */
	public ViolationFixes fixViolations(ISelection selection) {
		return null;
	}
	
	/** 
	 * Provide support for an advisor to provide a set of suggestions that apply to multiple violations
	 * @param trackers a set of ViolationTrackers for which suggestions are to be obtained
	 * @return a set of Suggestions
	 */
	public Set<Suggestion> getSuggestions(Set<ViolationTracker> trackers) {
		return Collections.emptySet();
	}
	
	/**
	 * Finds Violations that would be caused if the start time for the specified activity
	 * was changed to the specified time range
	 * 
	 * @param activity Activity for which start time would be changed
	 * @param rangeStart lower bound for the range of start times to consider
	 * @param rangeEnd upper bound for the range of start times to consider
	 * @return map that contains an entry for each one of the start times considered, along with the 
	 * violations that would be caused if the activity's start time was set to that particular start time
	 */
	public Map<Date, List<? extends Violation> > getWhatIfViolations(EActivity activity, Date startRange, Date endRange)
	{
	  return Collections.EMPTY_MAP;
		//throw new RuntimeException("getWhatIfViolations not implemented for "+this.getClass().getName());
	}

	/**
	 * Perform a comprehensive check for violations on the whole plan.
	 * 
	 * @return
	 */
	protected abstract List<? extends Advice> initialize();

	/**
	 * When the ping period elapses without new notifications arriving,
	 * the ping method will be invoked.  Override to keep connections
	 * alive, etc.
	 */
	protected void ping() {
		// do nothing
	}

	/**
	 * Return true if this plan advisor cares about this notification.
	 * 
	 * @param notification
	 * @return
	 */
	protected abstract boolean affectsViolations(Notification notification);

	/**
	 * Check these notifications for new violations, and return any found.
	 * 
	 * @param notifications
	 */
	protected abstract List<? extends Advice> check(List<Notification> notifications);
	
	/**
	 * Add the following notifications to the list to be processed
	 * at the next asynchronous opportunity.  Returns true if any
	 * of the notifications could remove, add, or change any 
	 * violations.
	 * 
	 * @param notifications
	 * @return
	 */
	public final boolean enqueue(List<Notification> notifications) {
		Boolean isPaused = null;
		List<Notification> filtered = new ArrayList<Notification>();
		for (Notification notification : notifications) {
			if (notification.getEventType() == Notification.RESOLVE) {
				continue;
			} else if (notification.getEventType() == ControlNotification.PAUSE) {
				isPaused = true;
				continue;
			} else if (notification.getEventType() == ControlNotification.RESUME) {
				isPaused = false;
				filtered.add(notification);
				continue;
			}
			try {
				if (affectsViolations(notification)) {
					filtered.add(notification);
				}
			} catch (Exception e) {
				LogUtil.error("exception in affectsViolations", e);
			}
		}
		if (filtered.isEmpty() && isPaused == null) {
			return false;
		}
		synchronized (queueLock) {
			if (isPaused != null) {
				paused = isPaused;
			}
			queuedNotifications.addAll(filtered);
			queueLock.notify();
		}
		return !paused;
	}

	protected void emptyNotificationQueue()
	{
		synchronized (queueLock) {
			queuedNotifications.clear();
			queueLock.notify();
		}		
	}
	
	/**
	 * Start the thread
	 */
	public final void start() {
		thread.start();
	}
	
	/**
	 * Tell the thread to exit at the next check
	 */
	public final void quit() {
		keepRunning = false;
		thread.interrupt();
		cleanup();
	}
	
	private void cleanup() {
		queuedNotifications.clear();
		planAdvisorMember = null;
	}
	
	/**
	 * Whether this thread should quit.  Polite plan advisors
	 * should check this periodically during their work and return if
	 * this is true.
	 * 
	 * @return
	 */
	protected final boolean isQuit() {
		return !keepRunning;
	}

	public final void run() {
		updateInitialAdvice();
		while (!isQuit()) {
			synchronized (queueLock) {
				while (paused || queuedNotifications.isEmpty()) {
					try {
						queueLock.wait(PING_PERIOD_IN_MILLISECONDS);
						if (queuedNotifications.isEmpty()) {
							ping();
						}
					} catch (InterruptedException e) {
						// fall out to keep running check
					}
					if (isQuit()) {
						return;
					}
				}
			}
			List<Advice> violations = processNotifications();
			if (isQuit()) {
				return;
			}
      // Synchronizing here so that the plan advisor can call this from another
      // thread later if its not ready to provide violations at the time it's
      // processing notifications.
			synchronized (updateLock) {
			  planAdvisorMember.updateAdvice(this, violations);
			}
		}
	}

	protected final void updateInitialAdvice() {
		List<? extends Advice> initialAdvices = Collections.emptyList();
		try {
			initialAdvices = initialize();
		} catch (Exception exception) {
			LogUtil.error("failed to initialize advisor: " + getName(), exception);
		}
		if (!isQuit()) {
			planAdvisorMember.updateAdvice(this, initialAdvices);
		}
	}

	private List<Advice> processNotifications() {
		List<Advice> violations = new ArrayList<Advice>();
		List<Notification> notifications = new ArrayList<Notification>();
		boolean queuedNotificationsWasEmpty = false;
		do {
			synchronized (queueLock) {
				notifications.addAll(queuedNotifications);
				queuedNotifications.clear();
				try {
					queueLock.wait(getNotificationBatchWaitPeriodInMilliseconds());
				} catch (InterruptedException e) {
					// fall out to keep running check
				}
				if (isQuit()) {
					return null;
				}
				queuedNotificationsWasEmpty = queuedNotifications.isEmpty();
			}
			if (queuedNotificationsWasEmpty) {
				// only process the notifications if no new notifications arrived during the batch wait period
				List<? extends Advice> checked;
				try {
					checked = check(notifications);
				} catch (Exception e) {
					if (isQuit()) {
						return null; // no need to report an exception during quitting
					}
					LogUtil.error("exception in check", e);
					checked = Collections.emptyList();
				}
				if (isQuit()) {
					return null;
				}
				sanitizeDuplicates(checked);
				violations.removeAll(checked); // remove any old violations that match these new ones 
				violations.addAll(checked); // now add in the new violations at the back
				notifications.clear();
			}
			synchronized (queueLock) {
				// check to see if new notifications arrived while we were checking
				queuedNotificationsWasEmpty = queuedNotifications.isEmpty();
			}
		} while (!queuedNotificationsWasEmpty);
		return violations;
	}

	protected int getNotificationBatchWaitPeriodInMilliseconds() {
		return NOTIFICATION_BATCH_WAIT_PERIOD_IN_MILLISECONDS;
	}

	/**
	 * This method checks to see if the plan advisor has returned two
	 * violations that match. (are equals())  If so, it prints a warning
	 * identifying the offending plan advisor.  It will also remove the
	 * first of the two violations that match, so that the "latest"
	 * violation will be reported.
	 * 
	 * @param checked
	 */
	private void sanitizeDuplicates(List<? extends Advice> checked) {
		for (int i = 0 ; i < checked.size() ; i++) {
			for (int j = i + 1 ; j < checked.size() ; j++) {
				Advice violationI = checked.get(i);
				Advice violationJ = checked.get(j);
				if (violationI.equals(violationJ)) {
					String message = "two new advice from the advisor '" + getName();
					message += "' matched each other.  advice updates might not work correctly.";
					Logger.getLogger(getClass()).warn(message);
					// remove the earlier violation, set the i counter back, and restart from there
					checked.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
	private String buildThreadName(String name) {
		EPlan plan = planAdvisorMember.getPlan();
		StringBuilder builder = new StringBuilder();
		builder.append("PlanAdvisorThread:");
		builder.append(name);
		builder.append("(");
		builder.append(plan.getName());
		builder.append(")");
		return builder.toString();
	}

	/**
	 * Simple thread class to give a nice name to the thread, 
	 * and call out to a protected run method in the main class.
	 * 
	 * @author abachmann
	 *
	 */
	private final class PlanAdvisorThread extends Thread {
		
		private static final int ADVISOR_PRIORITY = (MIN_PRIORITY + NORM_PRIORITY)/2;

		public PlanAdvisorThread(String name) {
			super(buildThreadName(name));
			setDaemon(true);
			setPriority(ADVISOR_PRIORITY);
		}

		@Override
		public final void run() {
			try {
				PlanAdvisor.this.run();
			} 
			catch (RuntimeException e) {
				LogUtil.error("PlanAdvisor "+PlanAdvisor.this.getClass().getSimpleName()+" exited with error",e);
			}
			finally {
				Context current = Context.getCurrent();
				ReflectionUtils.set(current, "_owner", null);
				cleanup();
			}
		}

	}

}
