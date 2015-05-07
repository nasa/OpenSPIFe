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
package gov.nasa.ensemble.emf.util;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;

public class NotificationAdapter implements Adapter {

	private Queue<Notification> notificationsLeft = new LinkedList<Notification>();

	/**
	 * Use this constructor to print all received notifications,
	 * instead of checking them.
	 */
	public NotificationAdapter() {
		notificationsLeft = null;
	}
	
	/**
	 * Use this constructor to check for the supplied notifications.
	 * 
	 * @param notifications
	 */
	public NotificationAdapter(Notification ... notifications) {
		notificationsLeft.addAll(Arrays.asList(notifications));
	}

	public void attach(Notifier ... notifiers) {
		for (Notifier notifier : notifiers) {
			notifier.eAdapters().add(this);
		}
	}
	
	/**
	 * Add some more notifications to the list to check. 
	 * 
	 * @param notifications
	 */
	public void load(Notification ... notifications) {
		assertFinished();
		notificationsLeft.addAll(Arrays.asList(notifications));
	}

	@Override
	public Notifier getTarget() {
		return null;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return false;
	}

	@Override
	public void notifyChanged(Notification notification) {
		if (notificationsLeft != null) {
			if (notificationsLeft.isEmpty()) {
				String string = notificationString(notification).toString();
				System.err.println(string);
				Assert.fail("received unexpected notification: " + string);
			}
			Notification expectedNotification = notificationsLeft.poll();
			try {
				Assert.assertEquals("unexpected notifier", expectedNotification.getNotifier(), notification.getNotifier());
				Assert.assertEquals("unexpected event type", expectedNotification.getEventType(), notification.getEventType());
				Assert.assertEquals("unexpected feature", expectedNotification.getFeature(), notification.getFeature());
				Assert.assertEquals("unexpected old value", expectedNotification.getOldValue(), notification.getOldValue());
				Object expectedValue = expectedNotification.getNewValue();
				Object actualValue = notification.getNewValue();
				if ((expectedValue instanceof int[]) && (actualValue instanceof int[])) {
					int[] expectedArray = (int[]) expectedValue;
					int[] actualArray = (int[]) actualValue;
					Assert.assertTrue("unexpected new value", Arrays.equals(expectedArray, actualArray));
				} else {
					Assert.assertEquals("unexpected new value", expectedValue, actualValue);
				}
			} catch (Error e) {
				System.err.println("expected: " + notificationString(expectedNotification));
				System.err.println("received: " + notificationString(notification));
				throw e;
			}
		} else {
			System.out.println(notificationString(notification));
		}
	}

	@Override
	public void setTarget(Notifier newTarget) {
		// nothing to do
	}

	public void assertFinished() {
		if (notificationsLeft != null) {
			for (Notification notification : notificationsLeft) {
				System.out.println(notificationString(notification));
			}
			Assert.assertEquals("notifications left", 0, notificationsLeft.size());
		} else {
			System.out.println();
		}
	}

	/**
	 * Print out a notification in a shorter, more readable fashion, for debugging.
	 * 
	 * @param notification
	 * @return
	 */
	public static StringBuffer notificationString(Notification notification) {
		StringBuffer result = new StringBuffer();
		result.append(eventTypeString(notification.getEventType())).append(" ");
		result.append(featureString(notification.getFeature())).append(" (");
		result.append(notification.getOldValue()).append(") -> (");
		result.append(notification.getNewValue()).append(") : ");
		result.append(notification.getNotifier());
		return result;
	}

	private static String eventTypeString(int eventType) {
		switch (eventType) {
		case Notification.SET:
			return ("SET");
		case Notification.UNSET:
			return ("UNSET");
		case Notification.ADD:
			return ("ADD");
		case Notification.ADD_MANY:
			return ("ADD_MANY");
		case Notification.REMOVE:
			return ("REMOVE");
		case Notification.REMOVE_MANY:
			return ("REMOVE_MANY");
		case Notification.MOVE:
			return ("MOVE");
		case Notification.REMOVING_ADAPTER:
			return ("REMOVING_ADAPTER");
		case Notification.RESOLVE:
			return ("RESOLVE");
		case ControlNotification.PAUSE:
			return "PAUSE";
		case ControlNotification.RESUME:
			return "RESUME";
		default:
			return String.valueOf(eventType);
		}
	}

	private static String featureString(Object feature) {
		if (feature instanceof EStructuralFeature) {
			EStructuralFeature structuralFeature = (EStructuralFeature) feature;
			return structuralFeature.getName();
		}
		return String.valueOf(feature);
	}

}
