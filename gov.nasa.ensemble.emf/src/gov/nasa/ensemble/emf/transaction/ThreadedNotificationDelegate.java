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
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.emf.util.ControlNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.emf.common.notify.Notification;

public abstract class ThreadedNotificationDelegate {

	private static final int PING_PERIOD_IN_MILLISECONDS = 5*60*1000; // five minutes
	
	private final Thread thread;
	private final Object queueLock = new int[0];
	private final Queue<Notification> queuedNotifications = new ConcurrentLinkedQueue();
	private boolean processingNotifications = false;
	private long quiescencePeriodMillis = 50;
	private volatile boolean keepRunning = true;
	private volatile boolean paused = false;

	private boolean initialized = false;
	private int joinInitialization[] = new int[0]; 
	
	public ThreadedNotificationDelegate(String name) {
		this.thread = new InternalThread(name);
	}

	public ThreadedNotificationDelegate(String name, long quiescencePeriodMillis) {
		this.thread = new InternalThread(name);
		this.quiescencePeriodMillis = quiescencePeriodMillis;
	}
	
	private void setInitialized() {
		synchronized (joinInitialization) {
			initialized = true;
			joinInitialization.notifyAll();
		}
	}
	
	/**
	 * Returns true if the delegate has completed initialization
	 * @return boolean
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Block until this delegate has completed initialization
	 * Does nothing if the delegate has already initialized
	 */
	public void joinInitialization() {
		synchronized (joinInitialization) {
			while (!isInitialized() && !isQuit()) {
				try {
					joinInitialization.wait();
				} catch (InterruptedException e) {
					// skip interruptions
				}
			}
		}
	}
	
	/**
	 * Performs model initializations within the thread
	 */
	protected abstract void initialize();
	
	/**
	 * Return true if this plan advisor cares about this notification.
	 * 
	 * @param notification
	 * @return boolean
	 */
	protected abstract boolean isImportant(Notification notification);

	/**
	 * Process the queued notifications.
	 * @param notifications
	 */
	protected abstract void processNotifications(List<Notification> notifications);
	
	/**
	 * When the ping period elapses without new notifications arriving,
	 * the ping method will be invoked.  Override to keep connections
	 * alive, etc.
	 */
	protected void ping() {
		// do nothing
	}
	
	
	public void waitUntilFinished() throws InterruptedException {
		do {
			Thread.sleep(2500);
		} while (processingNotifications || (queuedNotifications.size() > 0));
	}
	
	/**
	 * Add the following notifications to the list to be processed
	 * at the next asynchronous opportunity.  Returns true if any
	 * of the notifications could remove, add, or change any 
	 * violations.
	 * 
	 * @param notifications
	 * @return boolean
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
			if (isImportant(notification)) {
				filtered.add(notification);
			}
		}
		if (filtered.isEmpty() && (isPaused == null || !isPaused)) {
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

	/**
	 * Start the thread
	 */
	public final void start() {
		thread.start();
	}
	
	public final boolean isAlive() {
		return thread.isAlive();
	}

	/**
	 * Tell the thread to exit at the next check
	 */
	public final void quit() {
		keepRunning = false;
		thread.interrupt();
	}
	
	/**
	 * Whether this thread should quit.  Polite plan advisors
	 * should check this periodically during their work and return if
	 * this is true.
	 * 
	 * @return boolean
	 */
	public final boolean isQuit() {
		return !keepRunning;
	}

	public final void run() {
		try {
			initialize();
			setInitialized();
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
				final List<Notification> notifications = new ArrayList<Notification>();
				boolean queuedNotificationsWasEmpty = false;
				do {
					synchronized (queueLock) {
						notifications.addAll(queuedNotifications);
						queuedNotifications.clear();
						try {
							queueLock.wait(quiescencePeriodMillis);
						} catch (InterruptedException e) {
							// fall out to keep running check
						}
						if (isQuit()) {
							return;
						}
						queuedNotificationsWasEmpty = queuedNotifications.isEmpty();
					}
					if (queuedNotificationsWasEmpty) {
						// only process the notifications if no new notifications arrived during the batch wait period
						processingNotifications = true;
						processNotifications(notifications);
						notifications.clear(); // once processed, clear the stack in case new messages were added to the queue
						processingNotifications = false;
					}
					synchronized (queueLock) {
						// check to see if new notifications arrived while we were checking
						queuedNotificationsWasEmpty = queuedNotifications.isEmpty();
					}
				} while (!queuedNotificationsWasEmpty);
			}
		} finally {
			// SPF-11147 -- ensure that isQuit is true when the run method is exited due to an unhandled exception
			keepRunning = false;
		}
	}

	/**
	 * Simple thread class to give a nice name to the thread, 
	 * and call out to a protected run method in the main class.
	 */
	private final class InternalThread extends Thread {
		
		private static final int PRIORITY = (MIN_PRIORITY + NORM_PRIORITY)/2;

		public InternalThread(String name) {
			super(name);
			setDaemon(true);
			setPriority(PRIORITY);
		}

		@Override
		public final void run() {
			ThreadedNotificationDelegate.this.run();
		}
		
	}
	
}
