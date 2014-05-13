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
package gov.nasa.ensemble.common.notify;

import gov.nasa.ensemble.common.event.EventListenerList;

import java.util.EventListener;

import org.apache.log4j.Logger;

public class NotificationListenerList extends EventListenerList {

	private static final long serialVersionUID = -1283380063581045950L;

	private ISimpleNotifier notifier;
	
	private static final Logger trace = Logger
			.getLogger(NotificationListenerList.class);
	

	public NotificationListenerList(ISimpleNotifier model) {
		this.notifier = model;
	}

	public void fireNotification() {
		fireNotification(null);
	}

	public void fireNotification(Object parameter) {
		EventListener[] listeners = getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			try {
				((INotificationListener) listeners[i]).notify(notifier, parameter);
			} catch (RuntimeException re) {
				trace.error(re, re);
			}
		}
	}

}
