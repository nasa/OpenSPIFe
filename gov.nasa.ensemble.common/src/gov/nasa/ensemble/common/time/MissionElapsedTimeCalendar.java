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

import java.util.Calendar;
import java.util.Date;

public class MissionElapsedTimeCalendar extends Calendar {
	
	private long missionStartTimeInMillis;

	public MissionElapsedTimeCalendar(Date missionStartTime) {
		super();
		this.missionStartTimeInMillis = missionStartTime.getTime();
	}

	@Override
	public void add(int field, int amount)  {
			
		checkIfFieldIsSupported(field);

		if (amount == 0) {
			return; // Nothing to do
		}
		
		long delta = amount;
		
		switch (field) {
			case DAY_OF_YEAR:
				delta *= 24 * 60 * 60 * 1000; // 1 day
				break;
	
			case HOUR_OF_DAY:
				delta *= 60 * 60 * 1000; // 1 hour
				break;
	
			case MINUTE:
				delta *= 60 * 1000; // 1 minute
				break;
	
			case SECOND:
				delta *= 1000; // 1 second
				break;
	
			case MILLISECOND:
				break;
					
		}
		
		// Automatically computes fields if necessary
		setTimeInMillis(time + delta); 
		
	}
	
	@Override
	protected void computeFields() {
		long millisElapsed = time - missionStartTimeInMillis;
		
		int days = (int) (millisElapsed / ONE_DAY);
		int millisInDay = (int) (millisElapsed - (days * ONE_DAY));
		
		set(DAY_OF_YEAR, days);

		// Fill in all supported time-related fields based on millisInDay.
		// Set flags to indicate which fields we set
		set(MILLISECOND, millisInDay % 1000);
		
		millisInDay /= 1000;
		set(SECOND, millisInDay % 60);
		
		millisInDay /= 60;
		set(MINUTE, millisInDay % 60);
		
		millisInDay /= 60;
		set(HOUR_OF_DAY, millisInDay);
	}

	@Override
	protected void computeTime() {
		
		// first convert the supported fields to a millisecond total
		long millisElapsed = internalGet(DAY_OF_YEAR); // now have days
		millisElapsed *= 24;
		millisElapsed += internalGet(HOUR_OF_DAY); // now have hours
		millisElapsed *= 60;
		millisElapsed += internalGet(MINUTE); // now have minutes
		millisElapsed *= 60;
		millisElapsed += internalGet(SECOND); // now have seconds
		millisElapsed *= 1000;
		millisElapsed += internalGet(MILLISECOND); // now have millis
		
		time = missionStartTimeInMillis + millisElapsed;
		
	}

	@Override
	public int getGreatestMinimum(int field) {
		return getMinimum(field);
	}

	@Override
	public int getLeastMaximum(int field) {
		return getMaximum(field);
	}

	@Override
	public int getMaximum(int field) {
		checkIfFieldIsSupported(field);
		switch (field) {
			case DAY_OF_YEAR: return Integer.MAX_VALUE;
			case HOUR_OF_DAY: return 23;
			case MINUTE: return 59;
			case SECOND: return 59;
			case MILLISECOND: return 999;
			default: return 0; // shouldn't be possible after checkIfFieldIsSupported
		}
	}

	@Override
	public int getMinimum(int field) {
		checkIfFieldIsSupported(field);
		return 0;
	}

	@Override
	public void roll(int field, boolean up) {
		roll(field, up ? +1 : -1);
	}
	
	@Override
	public void roll(int field, int amount) {
		checkIfFieldIsSupported(field);
		if (amount == 0) {
			return; // Nothing to do
		}
		
		// These is the standard roll code, copied from GregorianCalendar.
		int min = 0, max = 0, gap;
		if (field >= 0 && field < FIELD_COUNT) {
			complete();
			min = getMinimum(field);
			max = getMaximum(field);
		}
		
		gap = max - min + 1;
		int value = internalGet(field) + amount;
		value = (value - min) % gap;
		if (value < 0) {
			value += gap;
		}
		value += min;
		
		set(field, value);
		
	}
	
	private void checkIfFieldIsSupported(int field) {
		switch (field) {
			case DAY_OF_YEAR: break;
			case HOUR_OF_DAY: break;
			case MINUTE: break;
			case SECOND: break;
			case MILLISECOND: break;
			case HOUR: throw new IllegalArgumentException("12-hr hour field is not supported -- use H, not h.");
			case DAY_OF_MONTH: throw new IllegalArgumentException("Day-of-month field is not supported -- use D, not d.");
			case MONTH: throw new IllegalArgumentException("Month field is not supported.");
			case YEAR: throw new IllegalArgumentException("Year field is not supported.");
			default:
				throw new IllegalArgumentException("Field " + field + " is not supported.");
		}
	}
	
	// Useful millisecond constants.
	private static final int  ONE_SECOND = 1000;
	private static final int  ONE_MINUTE = 60*ONE_SECOND;
	private static final int  ONE_HOUR   = 60*ONE_MINUTE;
	private static final long ONE_DAY    = 24*ONE_HOUR;

}
