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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.jscience.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.AmountConstraint;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.PowerValue;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.xml.type.internal.XMLDuration;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JScienceFactoryImpl extends EFactoryImpl implements JScienceFactory {
	
	private static final EDataType EDOUBLE_TYPE = EcorePackage.Literals.EDOUBLE_OBJECT;
	private static final EDataType EDATE_TYPE = EcorePackage.Literals.EDATE;
	private static final EcoreFactory ECORE_FACTORY = EcoreFactory.eINSTANCE;
	private static final ISO8601DateFormat EXTENT_START_FORMAT = new ISO8601DateFormat();
	static {
		EXTENT_START_FORMAT.setMillisFormatMode(true);
	}
	
	/**
	 * These cached durations help to speed up parsing during loading.
	 */
	private Map<String, Amount<Duration>> cachedDurations = cacheDurations(
			"P0DT0H0M0.000S",
			"P0DT0H1M0.000S",
			"P0DT0H5M0.000S",
			"P0DT0H10M0.000S",
			"P0DT0H15M0.000S",
			"P0DT0H20M0.000S",
			"P0DT0H25M0.000S",
			"P0DT0H30M0.000S",
			"P0DT0H40M0.000S",
			"P0DT0H45M0.000S",
			"P0DT1H0M0.000S",
			"P0DT1H30M0.000S",
			"P0DT2H0M0.000S",
			"P0DT4H0M0.000S",
			"P1DT0H0M0.000S",
			"P0Y0M0DT0H0M0.000S",
			"P0Y0M0DT0H1M0.000S",
			"P0Y0M0DT0H5M0.000S",
			"P0Y0M0DT0H10M0.000S",
			"P0Y0M0DT0H15M0.000S",
			"P0Y0M0DT0H20M0.000S",
			"P0Y0M0DT0H25M0.000S",
			"P0Y0M0DT0H30M0.000S",
			"P0Y0M0DT0H40M0.000S",
			"P0Y0M0DT0H45M0.000S",
			"P0Y0M0DT1H0M0.000S",
			"P0Y0M0DT1H30M0.000S",
			"P0Y0M0DT2H0M0.000S",
			"P0Y0M0DT4H0M0.000S",
			"P0Y0M1DT0H0M0.000S"
	);
	private Map<String, Amount<Duration>> cacheDurations(String ... strings) {
		Map<String, Amount<Duration>> map = new HashMap<String, Amount<Duration>>();
		for (String string : strings) {
			map.put(string, parseXMLDuration(string));
		}
		return map;
	}

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static JScienceFactory init() {
		try {
			JScienceFactory theJScienceFactory = (JScienceFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.ensemble.core.jscience/model/JScience.ecore"); 
			if (theJScienceFactory != null) {
				return theJScienceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JScienceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JScienceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case JSciencePackage.AMOUNT_CONSTRAINT: return createAmountConstraint();
			case JSciencePackage.COMPUTABLE_AMOUNT: return createComputableAmount();
			case JSciencePackage.PROFILE: return createProfile();
			case JSciencePackage.POWER_VALUE: return createPowerValue();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case JSciencePackage.COMPUTING_STATE:
				return createComputingStateFromString(eDataType, initialValue);
			case JSciencePackage.INTERPOLATION:
				return createINTERPOLATIONFromString(eDataType, initialValue);
			case JSciencePackage.EAMOUNT:
				return createEAmountFromString(eDataType, initialValue);
			case JSciencePackage.EAMOUNT_EXTENT:
				return createEAmountExtentFromString(eDataType, initialValue);
			case JSciencePackage.EANGLE:
				return createEAngleFromString(eDataType, initialValue);
			case JSciencePackage.ECOMPUTABLE_AMOUNT:
				return createEComputableAmountFromString(eDataType, initialValue);
			case JSciencePackage.EDATA_POINT:
				return createEDataPointFromString(eDataType, initialValue);
			case JSciencePackage.EDURATION:
				return createEDurationFromString(eDataType, initialValue);
			case JSciencePackage.EUNIT:
				return createEUnitFromString(eDataType, initialValue);
			case JSciencePackage.TEMPORAL_EXTENT:
				return createTemporalExtentFromString(eDataType, initialValue);
			case JSciencePackage.TEMPORAL_OFFSET:
				return createTemporalOffsetFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case JSciencePackage.COMPUTING_STATE:
				return convertComputingStateToString(eDataType, instanceValue);
			case JSciencePackage.INTERPOLATION:
				return convertINTERPOLATIONToString(eDataType, instanceValue);
			case JSciencePackage.EAMOUNT:
				return convertEAmountToString(eDataType, instanceValue);
			case JSciencePackage.EAMOUNT_EXTENT:
				return convertEAmountExtentToString(eDataType, instanceValue);
			case JSciencePackage.EANGLE:
				return convertEAngleToString(eDataType, instanceValue);
			case JSciencePackage.ECOMPUTABLE_AMOUNT:
				return convertEComputableAmountToString(eDataType, instanceValue);
			case JSciencePackage.EDATA_POINT:
				return convertEDataPointToString(eDataType, instanceValue);
			case JSciencePackage.EDURATION:
				return convertEDurationToString(eDataType, instanceValue);
			case JSciencePackage.EUNIT:
				return convertEUnitToString(eDataType, instanceValue);
			case JSciencePackage.TEMPORAL_EXTENT:
				return convertTemporalExtentToString(eDataType, instanceValue);
			case JSciencePackage.TEMPORAL_OFFSET:
				return convertTemporalOffsetToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComputableAmount createComputableAmount() {
		ComputableAmountImpl computableAmount = new ComputableAmountImpl();
		return computableAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountConstraint createAmountConstraint() {
		AmountConstraintImpl amountConstraint = new AmountConstraintImpl();
		return amountConstraint;
	}

	@Override
	public <T> DataPoint<T> createEDataPoint(Date date, T value) {
		DataPoint<T> eDataPoint = new DataPointImpl<T>(date, value);
		return eDataPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T> Profile<T> createProfile() {
		ProfileImpl<T> profile = new ProfileImpl<T>();
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PowerValue createPowerValue() {
		PowerValueImpl powerValue = new PowerValueImpl();
		return powerValue;
	}

	@Override
	public ComputableAmount createComputableAmount(Amount amount, ComputingState state) {
		ComputableAmount cAmount = createComputableAmount();
		cAmount.setAmount(amount);
		cAmount.setComputing(state);
		return cAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComputingState createComputingStateFromString(EDataType eDataType, String initialValue) {
		ComputingState result = ComputingState.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertComputingStateToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public INTERPOLATION createINTERPOLATIONFromString(EDataType eDataType, String initialValue) {
		INTERPOLATION result = INTERPOLATION.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertINTERPOLATIONToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Unit createEUnitFromString(EDataType eDataType, String initialValue) {
		try {
			return EnsembleUnitFormat.INSTANCE.parse(initialValue);
		} catch (ParseException e) {
			Logger.getLogger(JScienceFactoryImpl.class).error("parsing '" + initialValue + "'", e);
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEUnitToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return null;
		}
		return EnsembleUnitFormat.INSTANCE.format((Unit<?>) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Amount createEAmountFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) {
			return null;
		}
		try {
			return EnsembleAmountFormat.INSTANCE.parseAmount(initialValue, Unit.ONE);
		} catch (Exception e) {
			// We should be quiet about parse failures since it should be okay to
			// speculatively parse strings.
//			LogUtil.error("failed to parse Amount from string: " + initialValue);
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEAmountToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return null;
		}
		if (!(instanceValue instanceof Amount)) {
			LogUtil.error("Error converting instance value " + instanceValue + " to Amount");
			return null;
		}
		Amount amount = (Amount) instanceValue;
		return EnsembleAmountFormat.INSTANCE.formatAmount(amount);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	// Comment out this count code to detect commonly occurring durations for caching above.
//	Map<String, Integer> counts = new HashMap<String, Integer>();
	@SuppressWarnings("unchecked")
	public Amount<Duration> createEDurationFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) {
			return null;
		}
		initialValue = initialValue.trim();
		Amount<Duration> amount = cachedDurations.get(initialValue);
		if (amount != null) {
			return amount;
		}
//		Integer count = counts.get(initialValue);
//		if (count != null) {
//			count = count + 1;
//			if (count == 20) {
//				System.err.println("initialValue: " + initialValue);
//			}
//		} else {
//			count = 1;
//		}
//		counts.put(initialValue, count);
		try {
			return parseXMLDuration(initialValue);
		} catch (Exception e) {
			// fail silently
		}
		try {
			long s = DurationFormat.parseFormattedDuration(initialValue);
			return AmountUtils.toAmount(s, SI.SECOND);
		} catch (Exception e) {
			// fail silently
		}
		try {
			long s = Long.parseLong(initialValue);
			return AmountUtils.toAmount(s, DateUtils.MILLISECONDS);
		} catch (Exception e) {
			// fail silently
		}
		try {
			long s = DurationFormat.parseDurationFromHumanInput(initialValue);
			return AmountUtils.toAmount(s, SI.SECOND);
		} catch (Exception e) {
			// fail silently
		}
		return createEAmountFromString(eDataType, initialValue);
	}

	private Amount<Duration> parseXMLDuration(String initialValue) {
		XMLDuration duration = new XMLDuration(initialValue);
		long ms = duration.getTimeInMillis(new Date(0));
		return AmountUtils.toAmount(ms, DateUtils.MILLISECONDS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public String convertEDurationToString(EDataType eDataType, Object instanceValue) {
		Amount amount = null;
		if (instanceValue instanceof Number) {
			Number number = (Number) instanceValue;
			amount = AmountUtils.valueOf(number, SI.SECOND);
		} else if (instanceValue instanceof Amount) {
			amount = (Amount) instanceValue;
		}
		return amount == null ? null : DateUtils.getXMLDurationString(amount);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public ComputableAmount createEComputableAmountFromString(EDataType eDataType, String initialValue) {
		try {
			if (initialValue == null) {
				return null;
			}
			
			String subString = initialValue.substring(1, initialValue.length()-1);
			int index = subString.indexOf(",");
			String measureString = subString.substring(0, index).trim();
			String stateString = subString.substring(index+1).trim();
			
			Amount amount = null;
			if (!measureString.equals("null")) {
				amount = (Amount) createFromString(getJSciencePackage().getEAmount(), measureString);
			}
			
			ComputingState state = null;
			if (!stateString.equals("null")) {
				state = ComputingState.get(stateString);
			}
			ComputableAmountImpl value = new ComputableAmountImpl();
			value.setAmount(amount);
			value.setComputing(state);
			return value;
		} catch (Throwable t) {
			Logger.getLogger(JScienceFactoryImpl.class).error("Unable to create computable amount from initialValue " + initialValue + ": " + t);
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEComputableAmountToString(EDataType eDataType, Object instanceValue) {
		ComputableAmount computableAmount = (ComputableAmount) instanceValue;
		if (computableAmount == null) {
			return null;
		}
		String amountString = "null";
		if (computableAmount.getAmount() != null) {
			amountString = convertEAmountToString(getJSciencePackage().getEAmount(), computableAmount.getAmount());
		}
		String stateString = "null"; 
		if (computableAmount.getComputing() != null) {
			stateString = computableAmount.getComputing().toString();
		}
		return "["+ amountString + ", " + stateString + "]";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TemporalExtent createTemporalExtentFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null || initialValue.trim().length() == 0) {
			return null;
		}
		String[] split = CommonUtils.COMMA_PATTERN.split(initialValue);
		Date start = EXTENT_START_FORMAT.parse(split[0].trim(), new ParsePosition(0));
		Amount<Duration> duration = createEDurationFromString(JSciencePackage.Literals.EDURATION, split[1]);
		return new TemporalExtent(start, duration);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertTemporalExtentToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return "";
		}
		TemporalExtent extent = (TemporalExtent) instanceValue;
		Date start = extent.getStart();
		Amount<Duration> duration = extent.getDuration();
		return EXTENT_START_FORMAT.format(start) + "," + DateUtils.getXMLDurationString(duration);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TemporalOffset createTemporalOffsetFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null || initialValue.trim().length() == 0) {
			return null;
		}
		String[] split = CommonUtils.COMMA_PATTERN.split(initialValue);
		Timepoint timepoint = Timepoint.valueOf(split[0].trim());
		Amount<Duration> duration = createEDurationFromString(JSciencePackage.Literals.EDURATION, split[1]);
		return new TemporalOffset(timepoint, duration);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertTemporalOffsetToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return "";
		}
		TemporalOffset offset = (TemporalOffset) instanceValue;
		Timepoint timepoint = offset.getTimepoint();
		Amount<Duration> duration = offset.getOffset();
		return timepoint.toString() + "," + DateUtils.getXMLDurationString(duration);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public AmountExtent createEAmountExtentFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null || initialValue.trim().length() == 0) {
			return null;
		}
		StringTokenizer tokenizer = new StringTokenizer(initialValue, ",");
		String minString = tokenizer.nextToken();
		String maxString = tokenizer.nextToken();
		Amount min = createEAmountFromString(JSciencePackage.Literals.EAMOUNT, minString);
		Amount max = createEAmountFromString(JSciencePackage.Literals.EAMOUNT, maxString);
		return new AmountExtent(min, max);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEAmountExtentToString(EDataType eDataType, Object instanceValue) {
		AmountExtent extent = (AmountExtent) instanceValue;
		if (extent == null) {
			return "";
		}
		Amount min = extent.getMin();
		Amount max = extent.getMax();
		if (min == null && max == null) {
			return "";
		}
		String minString = convertEAmountToString(JSciencePackage.Literals.EAMOUNT, min);
		String maxString = convertEAmountToString(JSciencePackage.Literals.EAMOUNT, max);
		return minString + "," + maxString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public DataPoint<?> createEDataPointFromString(EDataType eDataType, String initialValue) {
		String v = initialValue.substring(initialValue.indexOf("[")+1, initialValue.lastIndexOf("]")); // White-space varies if its numeric or enum
		int delim = v.lastIndexOf(',');
		String dateString = v.substring(0, delim).trim();
		String valueString = v.substring(delim + 1).trim();
		
		// Support inexact amounts
		if (valueString.startsWith("(") && valueString.endsWith(")")) {
			valueString = valueString.substring(1, valueString.length()-1);
			final String[] tokens = valueString.split("\\s+");
			if (tokens.length == 3) {
				valueString = tokens[0];
			}
		}
		Date date = (Date)ECORE_FACTORY.createFromString(EDATE_TYPE, dateString);
		DataPoint pt = createEDataPoint(date, valueString);
		return pt;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEDataPointToString(EDataType eDataType, Object instanceValue) {
		DataPoint pt = (DataPoint) instanceValue;
		Object dpValue = pt.getValue();
		String valueString = ECORE_FACTORY.convertToString((dpValue instanceof Date ? EDATE_TYPE : EDOUBLE_TYPE), dpValue);
		return "[" + ECORE_FACTORY.convertToString(EDATE_TYPE, pt.getDate()) + ", " +  valueString + "]";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public Amount createEAngleFromString(EDataType eDataType, String initialValue) {
		return createEAmountFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEAngleToString(EDataType eDataType, Object instanceValue) {
		return convertEAmountToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JSciencePackage getJSciencePackage() {
		return (JSciencePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static JSciencePackage getPackage() {
		return JSciencePackage.eINSTANCE;
	}
	
} //JScienceFactoryImpl
