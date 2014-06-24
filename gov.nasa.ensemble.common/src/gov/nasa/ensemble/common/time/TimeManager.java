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

import gov.nasa.ensemble.common.event.EventListenerList;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.EventListener;

import javax.swing.Timer;

public class TimeManager {

	private static final int TIMER_DELAY = 1000;
	private static final ActionListener timerListener = new Listener();
	private static final Timer timer = new Timer(TIMER_DELAY, timerListener);
	private static EventListenerList curTimeListenerList = new EventListenerList();
	private static EventListenerList inspectedTimeListenerList = new EventListenerList();
	private static Date currentTime;
	private static Date inspectedTime;

	public static Date getCurrentTime() {
		if (!timer.isRunning()) {
			LogUtil.warnOnce("current time does not update when no listeners are present");
			return MissionCalendarUtils.getSimulatedCurrentTime();
		}
		if (currentTime == null) {
			currentTime = new Date();
		}
		return currentTime;
	}

	public static Date getInspectedTime() {
		return inspectedTime;
	}

	public static void setInspectedTime(Date inspectedTime) {
		TimeManager.inspectedTime = inspectedTime;
		fireInspectedTimeChanged(new TimeEvent(TimeManager.class, inspectedTime));
	}

	public static void addCurrentTimeListener(TimeListener listener) {
		if (!timer.isRunning() && curTimeListenerList.getListenerCount() == 0) {
			timer.start();
		}
		curTimeListenerList.add(listener);
	}

	protected static void fireCurrentTimeChanged(TimeEvent e) {
		fireTimeChanged(curTimeListenerList, e);
	}

	public static void removeCurrentTimeListener(TimeListener listener) {
		curTimeListenerList.remove(listener);
		if (curTimeListenerList.getListenerCount() == 0) {
			timer.stop();
			currentTime = null;
		}
	}

	public static void addInspectedTimeListener(TimeListener listener) {
		inspectedTimeListenerList.add(listener);
	}

	protected static void fireInspectedTimeChanged(TimeEvent e) {
		fireTimeChanged(inspectedTimeListenerList, e);
	}

	protected static void fireTimeChanged(EventListenerList listeners, TimeEvent e) {
		for(EventListener listener : listeners.getListenerList()) {
			((TimeListener)listener).timeChanged(e);
		}
	}

	public static void removeInspectedTimeListener(TimeListener listener) {
		inspectedTimeListenerList.remove(listener);
	}

	private static final class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			currentTime = MissionCalendarUtils.getSimulatedCurrentTime();
			fireCurrentTimeChanged(new TimeEvent(this, currentTime.getTime()));
		}
	}

}
