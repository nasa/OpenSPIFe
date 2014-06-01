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
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import java.text.ParseException;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class EDurationStringifier extends AbstractTrimmingStringifier<Amount<Duration>> {

	@Override
	@SuppressWarnings("unchecked")
	public Amount<Duration> getJavaObjectFromTrimmed(String string, Amount<Duration> defaultObject) throws ParseException {
		try {
			long duration = DurationFormat.parseDurationFromHumanInput(string);
			return (Amount<Duration>) JScienceFactory.eINSTANCE.createFromString(JSciencePackage.Literals.EDURATION, duration+" s");
		} catch (NumberFormatException e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}
	
	@Override
	public String getDisplayString(Amount<Duration> amount) {
		if (amount == null) {
			return "";
		}
		return DurationFormat.getFormattedDuration(amount.longValue(SI.SECOND));
//		return toString(amount.longValue(SI.SECOND));
	}

//	private static final String toString(long time) {
//		StringBuffer string = new StringBuffer();
//		if (time < 0) {
//			string.append("-");
//			if (time == Long.MIN_VALUE) {
//				time = Long.MAX_VALUE;
//			} else {
//				time = -time;
//			}
//		}
//	    long seconds = time % 60; time /= 60; 
//		long minutes = time % 60; time /= 60;
//		long hours = time;
//		string.append(DateUtils.twoDigitFormat(hours)).append(":");
//		string.append(DateUtils.twoDigitFormat(minutes)).append(":");
//		string.append(DateUtils.twoDigitFormat(seconds));
//		return string.toString();
//    }

}
