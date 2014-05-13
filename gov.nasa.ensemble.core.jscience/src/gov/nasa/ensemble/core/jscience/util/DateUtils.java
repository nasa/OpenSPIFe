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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.jscience.physics.amount.Amount;

public class DateUtils {

	// There is no other expression of zero duration more compact than "P0D":
	private static final String ZERO_DURATION_STRING = "P0D";
	
	public static final Unit<Duration> MILLISECONDS = SI.MILLI(SI.SECOND);
	public static final Amount<Duration> ZERO_DURATION = AmountUtils.exactZero(MILLISECONDS);

	private static final DatatypeFactory DATATYPE_FACTORY;
	private static final javax.xml.datatype.Duration ZERO_XML_DURATION;
	static {
		DatatypeFactory factory = null;
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			LogUtil.error("failed to instantiate DatatypeFactory");
		}
		DATATYPE_FACTORY = factory;
		javax.xml.datatype.Duration duration = null;
		if (DATATYPE_FACTORY != null) {
			try {
				duration = DATATYPE_FACTORY.newDuration(ZERO_DURATION_STRING);
			} catch (Exception e) {
				LogUtil.error("failed to create a duration of zero", e);
			}
		}
		ZERO_XML_DURATION = duration;
	}
	
	/**
	 * Returns a new date that is duration past the date provided
	 * @param date
	 * @param duration
	 * @return a new date that is duration past the date provided
	 */
	public static final Date add(Date date, Amount<Duration> duration) {
		long longValue = duration.longValue(MILLISECONDS);
		if (longValue == 0) {
			return date;
		}
		return new Date(date.getTime() + longValue);
	}
	
	/**
	 * Returns a new date that is duration before the date provided
	 * 
	 * @param date
	 * @param duration
	 * @return a new date that is duration before the date provided
	 */
	public static final Date subtract(Date date, Amount<Duration> duration) {
		long longValue = duration.longValue(MILLISECONDS);
		if (longValue == 0) {
			return date;
		}
		return new Date(date.getTime() - longValue);
	}
	
	/**
	 * Returns the duration elapsed from subtrahend until minuend.
	 * Example: subtract(9:01, 9:00) = SI.SECOND(60)
	 * @param minuend
	 * @param subtrahend
	 * @return the duration elapsed from subtrahend until minuend.
	 */
	public static final Amount<Duration> subtract(Date minuend, Date subtrahend) {
		long value = minuend.getTime() - subtrahend.getTime();
		return Amount.valueOf(value, MILLISECONDS);
	}
	
	/**
	 * Returns true if both date1 and date2 are valid dates (non null)
	 * and they are within duration of each other.
	 * @param date1
	 * @param date2
	 * @param duration
	 * @return true if both date1 and date2 are valid dates (non null)
	 * and they are within duration of each other.
	 */
	public static final boolean closeEnough(Date date1, Date date2, Amount<Duration> duration) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		Amount<Duration> diff = DateUtils.subtract(date2, date1);
		return !diff.abs().isGreaterThan(duration);
	}
	
	/**
	 * Return the XML Duration string representation of the Duration Amount 
	 * 
	 * @param duration
	 * @return
	 */
	public static String getXMLDurationString(Amount<Duration> duration) {
		if (AmountUtils.approximatesZero(duration)) {
			return ZERO_DURATION_STRING;
		}
		javax.xml.datatype.Duration xmlDuration = getXMLDuration(duration);
		return xmlDuration.toString();
	}	

	/**
	 * Create an XML Duration object for the Duration Amount
	 * 
	 * @param duration
	 * @return
	 */
	public static javax.xml.datatype.Duration getXMLDuration(Amount<Duration> duration) {
		if (AmountUtils.approximatesZero(duration)) {
			return ZERO_XML_DURATION;
		}
        boolean is0x8000000000000000L = false;
        long l;
        boolean isDurationInSeconds = (duration.getUnit() == SI.SECOND);
		if (isDurationInSeconds) {
			// special casing seconds makes us much faster for this common case
        	l = duration.longValue(SI.SECOND);
        } else {
        	l = duration.longValue(DateUtils.MILLISECONDS);
        }
        int signum;
		if (l > 0) {
            signum = 1;
        } else if (l < 0) {
            signum = -1;
            if (l == 0x8000000000000000L) {
                // negating 0x8000000000000000L causes an overflow
                l++;
                is0x8000000000000000L = true;
            }
            l *= -1;
        } else {
            signum = 0;
        }
        BigDecimal seconds;
        if (isDurationInSeconds) {
			// special casing seconds makes us much faster for this common case
			seconds = BigDecimal.valueOf((l % 60L) + (is0x8000000000000000L ? 1 : 0));
	        l /= 60L;
        } else {
			seconds = BigDecimal.valueOf((l % 60000L) + (is0x8000000000000000L ? 1 : 0), 3);
	        l /= 60000L;
        }
        BigInteger minutes = BigInteger.valueOf(l % 60L);
        l /= 60L;
        BigInteger hours = BigInteger.valueOf(l % 24L);
        l /= 24L;
        BigInteger days = BigInteger.valueOf(l);
		return DATATYPE_FACTORY.newDuration(signum >= 0, null, null, days, hours, minutes, seconds);
	}

	public static Date earliest(Date date1, Date date2) {
		return gov.nasa.ensemble.common.time.DateUtils.earliest(date1, date2);
	}

	public static Date latest(Date date1, Date date2) {
		return gov.nasa.ensemble.common.time.DateUtils.latest(date1, date2);
	}

	public static Date bind(Date date, Date early, Date late) {
		return gov.nasa.ensemble.common.time.DateUtils.bind(date, early, late);
	}

}
