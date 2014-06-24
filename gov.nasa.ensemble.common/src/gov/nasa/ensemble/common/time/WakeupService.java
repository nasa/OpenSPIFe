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
package gov.nasa.ensemble.common.time;

import java.util.Date;

/** 
 * This service notifies (wakes up) a listener, not periodically,
 * but when a certain time has been reached (or up to a second later).
 * 
 * Classes interested in being woken up can set a wakeup call, and they'll all be notified
 * at the earliest time any of them requested.  If there are multiple classes sharing
 * one instance of a wakeup service, sort of like people in a hotel room sharing
 * one alarm clock, then when they're all woken up at the request of the earliest riser,
 * they must set a new wakeup call before going back to sleep.
 * 
 * @author kanef
 *
 */

public class WakeupService implements TimeListener {
	
	private Date nextScheduledWakeup = null;
	private TimeListener requestor;
	
	/**
	 * See class documentation.
	 * @param requestor -- The one listener.  In the hotel analogy, this is the room requesting wakeup.
	 */
	public WakeupService(TimeListener requestor) {
		this.requestor = requestor;
	}

	/** 
	 * Ensure a notification before or less than a second after this time.
	 * If a sleeper is woken up prematurely, it should reiterate its request
	 * for a later wakeup before going back to sleep, since only the earliest request
	 * is remembered, not a list.
	 * @param atTime
	 */
	public void requestWakeup(Date atTime) {
		debug("Wakeup requested for", atTime);
		if (!atTime.before(new Date())) {
			if (nextScheduledWakeup==null) {
				TimeManager.addCurrentTimeListener(this);
				nextScheduledWakeup = atTime;
			}
			else if (atTime.before(nextScheduledWakeup)) {
				nextScheduledWakeup = atTime;
			}
		}
		debug("Wakeup now scheduled for", nextScheduledWakeup);
	}
	
	/**
	 * Request that no more notifications be sent until a wakeup is requested.
	 */
	public void cancelWakeup() {
		TimeManager.removeCurrentTimeListener(this);
		if (nextScheduledWakeup != null) {
			debug("Canceled wakeup for", nextScheduledWakeup);
			nextScheduledWakeup = null;
		}
	}

	@Override
	public void timeChanged(TimeEvent event) {
		// TimeManager wakes up up every second.
		if (nextScheduledWakeup != null && event.getTime().after(nextScheduledWakeup)) {
			cancelWakeup();
			debug(requestor, event.getTime());
			requestor.timeChanged(event);
		} // else hit snooze
	}

	private void debug(Object message, Date atTime) {
//		LogUtil.info(message + " " + atTime);
		// or
//		System.out.println(new Date() + " " + getClass().getSimpleName() + ": " + message.toString() + " " + atTime);
	}

}
