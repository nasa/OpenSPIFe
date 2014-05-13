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

import java.util.Map;
import java.util.WeakHashMap;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Dimension;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public class AmountUtils {

	private static final double ERROR_LIMIT = Math.pow(2, -50);
	
	private static final Map<Unit, Amount> zeroExactMap = new WeakHashMap<Unit, Amount>();
	private static final Map<Unit, Amount> zeroInexactMap = new WeakHashMap<Unit, Amount>();

	/**
	 * Parse the number string as a double but return an exact amount if this is possible.
	 * 
	 * @param <Q>
	 * @param numberString
	 * @param unit
	 * @return an exact amount if this is possible
	 */
	public static <Q extends Quantity> Amount<Q> valueOf(String numberString, Unit<Q> unit) {
		Number number = Double.parseDouble(numberString);
		return AmountUtils.valueOf(number, unit);
	}

	/**
	 * Given a number, we determine to use the exact or inexact variant of toAmount
	 * @param <Q> quantity parameter
	 * @param number to transform into {@link Amount}
	 * @param unit of {@link Amount}
	 * @return exact or inexact value of the number
	 */
	public static <Q extends Quantity> Amount<Q> valueOf(Number number, Unit<Q> unit) {
		if ((number instanceof Integer) || (number instanceof Long)) {
			return toAmount(number.longValue(), unit);
		}
		if (number.longValue() == number.doubleValue()) {
			return toAmount(number.longValue(), unit);
		} // else...
		return toAmount(number.doubleValue(), unit);
	}
	
	/**
	 * Check for zero value and return a shared zero instance if the value is zero.
	 * 
	 * @param <Q>
	 * @param value
	 * @param unit
	 * @return a shared zero instance if the value is zero
	 */
	public static <Q extends Quantity> Amount<Q> toAmount(double value, Unit<Q> unit) {
		if (value == 0) {
			return inexactZero(unit);
		}
		return Amount.valueOf(value, unit);
	}

	/**
	 * Return the shared inexact zero instance for the supplied unit.
	 * 
	 * @param <Q>
	 * @param unit
	 * @return the shared inexact zero instance for the supplied unit
	 */
	public static <Q extends Quantity> Amount<Q> inexactZero(Unit<Q> unit) {
		Amount amount = zeroInexactMap.get(unit);
		if (amount == null) {
			amount = Amount.valueOf(0.0, unit);
			zeroInexactMap.put(unit, amount);
		}
		return amount;
	}
	
	/**
	 * Check for zero value and return a shared zero instance if the value is zero.
	 * 
	 * @param <Q>
	 * @param value
	 * @param unit
	 * @return a shared zero instance if the value is zero
	 */
	public static <Q extends Quantity> Amount<Q> toAmount(long value, Unit<Q> unit) {
		if (value == 0) {
			return exactZero(unit);
		}
		return Amount.valueOf(value, unit);
	}

	/**
	 * Return the shared exact zero instance for the supplied unit.
	 * 
	 * @param <Q>
	 * @param unit
	 * @return shared exact zero instance for the supplied unit
	 */
	public static <Q extends Quantity> Amount<Q> exactZero(Unit<Q> unit) {
		Amount amount = zeroExactMap.get(unit);
		if (amount == null) {
			amount = Amount.valueOf(0L, unit);
			zeroExactMap.put(unit, amount);
		}
		return amount;
	}

	/**
	 * Check to see if the error bounds on the amount contain zero.
	 * 
	 * @param <Q>
	 * @param amount
	 * @return boolean
	 */
	public static <Q extends Quantity> boolean approximatesZero(Amount<Q> amount) {
		return (amount.getMaximumValue() >= 0) && (amount.getMinimumValue() <= 0);
	}
	
	/**
	 * Return a hash code for this amount, to match with the following equals method. 
	 * 
	 * @param amount
	 * @return
	 */
	public static int hashCode(Amount amount) {
		if (amount == null) {
			return 0;
		}
		if (amount.isExact()) {
			Unit unit = amount.getUnit();
			if (amount.getExactValue() == 0) {
				Dimension dimension = unit.getDimension();
				return dimension.hashCode();
			}
			Unit standardUnit = unit.getStandardUnit();
			Amount amount2 = amount.to(standardUnit);
			return (int)amount2.getExactValue();
		}
		return 0;
	}
	
	/**
	 * This utility method will return true in the case where two things have the same dimension and are both zero, regardless of
	 * the unit of each amount.
	 * 
	 * NOTE: does not currently work for non-zero based dimensions such
	 * as temperature, where zero Celsius and zero Kelvin are different.
	 * 
	 * @param <Q>
	 * @param q1
	 * @param q2
	 * @return boolean
	 */
	public static <Q extends Quantity> boolean equals(Amount<Q> q1, Amount<Q> q2) {
		if (q1 == null) {
			return (q2 == null);
		}
		if (q1.isExact() && q2.isExact()) {
			if (q1.getExactValue() == 0 && (q2.getExactValue() == 0)) {
				Dimension q1dimension = q1.getUnit().getDimension();
				Dimension q2dimension = q2.getUnit().getDimension();
				return q1dimension.equals(q2dimension);
			}
			Amount q3 = q1.to(q2.getUnit());
			return q3.approximates(q2);
		}
		return q1.equals(q2);
	}

	/**
	 * This utility method can be used to get a single numerical value for
	 * an amount.  In the case of a an exact amount, this will be the exact value.
	 * In the case of an inexact amount, this will be the estimated value, or an
	 * adjusted version thereof to maintain the following property:
	 * 
	 * AmountUtils.getNumericValue(Amount.valueOf(n, Unit.ONE)) should return n
	 *       
	 * @param amount
	 * @return the value of the amount, a Long or a Double (never null)
	 */
	public static Number getNumericValue(Amount amount) {
		if (amount.isExact()) {
			return amount.getExactValue();
		}
		double relativeError = amount.getRelativeError();
		double value;
		if (relativeError < ERROR_LIMIT) {
			value = amount.getMaximumValue();
			if (value < 0) {
				// for values less than zero, use the minimum value instead
				value = amount.getMinimumValue();
			} else if (amount.getMinimumValue() <= 0) {
				// if the value is zero or above and the minimum is zero or below, zero it is!
				return 0L;
			}
		} else {
			value = amount.getEstimatedValue();
		}
		if (value == (long)value) {
			return (long)value;
		}
		return value;
	}

}
