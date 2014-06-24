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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.text.ParseException;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class TimeOfDayStringifier extends AbstractTrimmingStringifier<Amount<Duration>> {
	
	private TimeOfDayFormat timeOfDayFormat;
	private double standardSecondsPerLocalSecond;

	public TimeOfDayStringifier() {
		super();
		timeOfDayFormat = new TimeOfDayFormat();
		this.standardSecondsPerLocalSecond = MissionConstants.getInstance().getEarthSecondsPerLocalSeconds();
	}

	public TimeOfDayStringifier(String desiredOutputFormat, String[] allowedInputFormats, double standardSecondsPerLocalSecond) throws Exception {
		super();
		timeOfDayFormat = new TimeOfDayFormat(desiredOutputFormat, allowedInputFormats);
		this.standardSecondsPerLocalSecond = standardSecondsPerLocalSecond;
	}

	/**
	 * Parses a time string in hours, minutes, and seconds.
	 * Returns a quasi-duration, but unlike a duration, this is in local units.
	 * If the time string gives an Earth time of day, the seconds will be SI seconds after midnight.
	 * If the time string gives a Mars time of sol, the seconds will be the longer Mars "seconds".
	 * That is, 10:00 always returns 3600,000 milliseconds, not 3698,968.
	 **/
	@Override
	protected Amount<Duration> getJavaObjectFromTrimmed(String string, Amount<Duration> defaultObject) throws ParseException {
		try {
			return timeOfDayFormat.parse(string);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	/**
	 * Accepts a time in standard seconds, aka SI seconds, aka Earth seconds.
	 * Returns a time string representing the time as local hours, minutes, and seconds.
	 */
	@Override
	public String getDisplayString(Amount<Duration> javaObject) {
		if (javaObject == null) {
			return null;
		}
		return timeOfDayFormat.format(javaObject.divide(standardSecondsPerLocalSecond));
	}

	/** Parses a time string in hours, minutes, and seconds.
	 * Returns a duration (converted to standard SI seconds,
	 * if using Mars time-of-sol).
	 */
	public Amount<Duration> getStandardSeconds(String string) throws ParseException {
		Amount<Duration> standardSeconds = getJavaObjectFromTrimmed(string, null);
		if (standardSeconds==null) return null;
		return standardSeconds.times(standardSecondsPerLocalSecond);
	}

}
