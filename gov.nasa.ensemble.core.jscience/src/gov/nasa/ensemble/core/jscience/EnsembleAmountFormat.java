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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.text.NumberFormat;
import java.text.ParseException;

import javax.measure.quantity.Angle;
import javax.measure.quantity.DataAmount;
import javax.measure.quantity.DataRate;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Power;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

@SuppressWarnings("unchecked")
public class EnsembleAmountFormat implements MissionExtendable {

	protected static final EnsembleUnitFormat UNIT_FORMAT = EnsembleUnitFormat.INSTANCE;

	private static final Logger trace = Logger.getLogger(EnsembleAmountFormat.class);
	private static final String BLANK_STRING = "";
	
	protected static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
	static {
		NUMBER_FORMAT.setMaximumFractionDigits(2);
		NUMBER_FORMAT.setGroupingUsed(false);
	}

	public static final EnsembleAmountFormat INSTANCE = getInstance();
	private static EnsembleAmountFormat getInstance() {
		try {
			return MissionExtender.construct(EnsembleAmountFormat.class);
		} catch (ConstructionException e) {
			LogUtil.error("failed to construct", e);
			return new EnsembleAmountFormat();
		}
	}

	/**
	 * Formats the measure. This is the most logical point to overide the class in order to customize (either with or without the
	 * boolean 'detailed' argument).
	 * 
	 * @param amount
	 * @return String
	 */
	public <T extends Quantity> String formatAmount(Amount<T> amount) {
		return formatAmount(amount, false);
	}

	/**
	 * Formats the measure. This is the most logical point to overide the class in order to customize.
	 * 
	 * @param amount
	 * @return String
	 */
	public <T extends Quantity> String formatAmount(Amount<T> amount, boolean detailed) {
		if(amount == null) return BLANK_STRING;
		Unit<T> unit = amount.getUnit();
		if (unit == null) {
			Number value = AmountUtils.getNumericValue(amount);
			return formatNumber(value);
		}
		if (Unit.ONE != unit) {
			Unit<? super T> su = unit.getStandardUnit();
			if (DataAmount.UNIT == su) {
				return convertToBinaryExponentialString(amount);
			} else if (DataRate.UNIT == su || Power.UNIT == su) {
				return convertToExponentialString(amount);
			} else
			// ANGLE
			if (NonSI.DEGREE_ANGLE.equals(unit)) {
				Number value = AmountUtils.getNumericValue(amount);
				return formatNumber(value) + " degrees";
			} else if (Angle.UNIT == su) {
					long v = amount.longValue(unit);
					return String.valueOf(v) + " " + UNIT_FORMAT.format(unit);
			} else
			// TIME
			if (Duration.UNIT == su) {
				long seconds = amount.longValue((Unit<T>) SI.SECOND);
				return DurationFormat.getFormattedDuration(seconds);
			} else 
			// TEMP
			if (SI.KELVIN.equals(unit)) {
				amount = (Amount<T>) amount.to(SI.CELSIUS);
			} else
			// BINARY_CHOICE
			if (ProfileUtil.BINARY_CHOICE.equals(unit)) {
				// don't include unit name
				return formatNumber(AmountUtils.getNumericValue(amount));
			}
		}
		Number value = AmountUtils.getNumericValue(amount);
		return formatNumber(value) + " " + UNIT_FORMAT.format(unit);
	}
	
	protected <T extends Quantity> String convertToBinaryExponentialString(Amount<T> amount) {
		Unit unit = SI.BIT;
		int i = 0;
		amount = amount.to(unit);
		while (Math.abs(amount.doubleValue(unit)) >= JSciencePackage.TEN_BASE_2) {
			unit = unit.times(JSciencePackage.TEN_BASE_2);
			if (JSciencePackage.TERA_BIT.equals(unit) 
					|| JSciencePackage.TERA_BYTE.equals(unit)
					|| ++i > 5) {
				break;
			}
		}
		Number value = AmountUtils.getNumericValue(amount.to(unit));
		return formatNumber(value) + " " + UNIT_FORMAT.format(unit);
	}
	
	private <T extends Quantity> String convertToExponentialString(Amount<T> amount) {
		Unit unit = amount.getUnit();
		Unit baseUnit = unit.getStandardUnit();
		Unit currentUnit = baseUnit; 
		double currentAmount = amount.doubleValue(currentUnit);
		while (Math.abs(currentAmount) >= 1000) {
			currentUnit = SI.KILO(currentUnit);
			// If next is peta, roll over and play dead
			if (SI.PETA(baseUnit).equals(SI.KILO(currentUnit))) {
				break;
			}
			currentAmount = amount.doubleValue(currentUnit);
		}
		Number value = AmountUtils.getNumericValue(amount.to(currentUnit));
		return formatNumber(value) + " " + UNIT_FORMAT.format(currentUnit);
	}
	
	/**
	 * Convenience method that formats a number according to
	 * the NUMBER_FORMAT object.
	 * @param number to format
	 * @return formatted number
	 */
	public String formatNumber(Number number) {
		return NUMBER_FORMAT.format(number);
	}
	
	/**
	 * Forwards the call to the standard instance UnitFormat object. This
	 * instance has been initialized above, so we wish to use that instance
	 * and no other, thus it is final.
	 * 
	 * @param u unit to format
	 * @return formatted unit
	 * 
	 * @deprecated - Use EnsembleUnitFormat.INSTANCE.format(u);
	 */
	@Deprecated
	public final String formatUnit(Unit u) {
		return UNIT_FORMAT.format(u);
	}

	/**
	 * Parse an amount 
	 * 
	 * @param string
	 * @param defaultUnit
	 * @return Amount
	 * @throws ParseException
	 */
	public final Amount parseAmount(String string, Unit defaultUnit) throws Exception {
		Amount amount = null;
		if (string != null) {
			string = string.replace(",", "").trim(); // strip commas
			if(string == BLANK_STRING) return null;
			try {
				amount = Amount.valueOf(string);
			} catch (StringIndexOutOfBoundsException e) {
				// this happens if it is just a number, but we'd 
				// like to tolerate that and use the default unit
				string = string.trim();
				Number number = parseNumber(string);
				amount = AmountUtils.valueOf(number, defaultUnit);
			} catch (IllegalArgumentException e) {
				// this happens if it is a duration "Incomplete Parsing"
				// so try it as a duration
				try {
					long duration = DurationFormat.parseFormattedDuration(string);
					amount = AmountUtils.toAmount(duration, SI.SECOND);
				} catch (Exception x) {
					throw e;
				}
			}
		}
		return amount;
	}
	
	/**
	 * Parse the number from a string to a number
	 * @throws ParseException 
	 */
	public final Number parseNumber(String number) throws ParseException {
		return NUMBER_FORMAT.parse(number);
	}
	
	/**
	 * Parse the unit from a string to the Unit object
	 * 
	 * @param units string to parse
	 * @return the Unit object
	 * 
	 */
	public final Unit parseUnit(String units) {
		try {
			Unit unit = UNIT_FORMAT.parse(units);
			trace.debug("Converting '"+units+"' to "+unit);
			return unit;
		} catch (ParseException e) {
			// silence, let caller complain
		}
		return null;
	}
	
}
