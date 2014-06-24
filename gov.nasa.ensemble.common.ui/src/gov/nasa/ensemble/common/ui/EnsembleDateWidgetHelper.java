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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.mission.MissionConstants;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.DateTime;

/**
 * Small helper class to use the Mission calendar to set the "Date" values of a datetime SWT widget
 * 
 * If a null date is provided uses the current date.
 * 
 * @author alexeiser
 * 
 */
public class EnsembleDateWidgetHelper {
	public static void setDate(DateTime dt, Date d) {
		if (d == null) {
			d = new Date();
		}

		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(d);

		dt.setYear(cal.get(Calendar.YEAR));
		dt.setMonth(cal.get(Calendar.MONTH));
		dt.setDay(cal.get(Calendar.DAY_OF_MONTH));

		// dt.setHours(cal.get(Calendar.HOUR_OF_DAY));
		// dt.setMinutes(cal.get(Calendar.MINUTE));
		// dt.setSeconds(cal.get(Calendar.SECOND));
	}

	/**
	 * get the date from the mission calendar
	 * 
	 * @param dt
	 * @return Date
	 */
	public static Date getDate(DateTime dt) {
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.set(Calendar.YEAR, dt.getYear());
		cal.set(Calendar.MONTH, dt.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, dt.getDay());
		//
		// cal.set(Calendar.HOUR_OF_DAY, dt.getHours());
		// cal.set(Calendar.MINUTE, dt.getMinutes());
		// cal.set(Calendar.SECOND, dt.getSeconds());
		return cal.getTime();
	}
}
