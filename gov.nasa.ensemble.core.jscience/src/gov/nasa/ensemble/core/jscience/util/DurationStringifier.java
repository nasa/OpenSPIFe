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
package gov.nasa.ensemble.core.jscience.util;

import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.time.DurationFormat.DurationType;
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.text.ParseException;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class DurationStringifier extends AbstractTrimmingStringifier<Amount<Duration>> {
	
	private DurationType durationType;
	
	public DurationStringifier() {
		this.durationType = DurationFormat.getDefaultFormat();
	}

	public DurationStringifier(DurationType durationType) {
		this.durationType = durationType;
	}

	@Override
	public String getDisplayString(Amount<Duration> javaObject) {
		if (javaObject == null) {
			return "NaN";
		}
		return DurationFormat.getFormattedDuration(javaObject.longValue(SI.SECOND), durationType);
    }

	/**
	 * @throws ParseException  
	 */
	@Override
    protected Amount<Duration> getJavaObjectFromTrimmed(String string, Amount<Duration> defaultObject) throws ParseException {
		long seconds = DurationFormat.parseDurationFromHumanInput(string);
	    return Amount.valueOf(seconds, SI.SECOND);
    }
	
	public static String toEnglish(Amount<Duration> duration) {
		return DurationFormat.getEnglishDuration(duration.longValue(SI.SECOND));
	}

}
