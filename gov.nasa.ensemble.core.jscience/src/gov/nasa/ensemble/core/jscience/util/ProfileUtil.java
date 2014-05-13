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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.Pair;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.EarthTimeFlexibleFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.PowerValue;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.emf.resource.ProgressMonitorInputStream;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;

import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.jscience.physics.amount.Amount;

public class ProfileUtil {

	public static final String UNCATEGORIZED = "Uncategorized";

	private static DateFormat PROFILE_DATE_FORMAT = getDateFormat();
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	public static final String IGNORE_DATA_POINTS = "IGNORE_DATA_PIONTS";
	
	public static final String CLAIMABLE_ATTRIBUTE = "claimable";
	public static final String CATEGORY_ATTRIBUTE = "category";
	public static final String DATATYPE_ATTRIBUTE = "datatype";
	public static final String EXTERNAL_CONDITION_ATTRIBUTE = "external_condition";
	public static final String ID_ATTRIBUTE = "id";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String UNITS_ATTRIBUTE = "units";

	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
	static {
		NUMBER_FORMAT.setMaximumFractionDigits(4);
	}
	
	public static final Unit<Dimensionless> BINARY_CHOICE = Unit.ONE.alternate("binary choice");

	/**
	 * Given a new DataPoint and the profile data points list, insert the DataPoint into the proper place
	 * and return the index where it was inserted.  This method keeps the DataPoints ascending date order.
	 * @param newDataPoint
	 * @param dataPoints
	 * @return
	 */
	public static int insertNewDataPoint(DataPoint newDataPoint, List<DataPoint> dataPoints) {
		Date newDate = newDataPoint.getDate();
		int i = 0;
		for (ListIterator<DataPoint> it = dataPoints.listIterator(); it.hasNext();) {
			Date next = it.next().getDate();
			if (newDate.before(next)) {
				dataPoints.add(i, newDataPoint);
				return i;
			}
			i++;
		}
		dataPoints.add(newDataPoint);
		return i;
	}
	
	/**
	 * Given two lists of data points using a stepwise interpolation, return a
	 * binary stepwise list of datapoints where both inputs are non-zero.
	 * precondition: both arguments must be in temporal order.
	 * 
	 * @param <T>
	 * @param left
	 * @param right
	 * @return a binary stepwise list of datapoints where both inputs are
	 *         non-zero.
	 */
	public static List<DataPoint> getBinaryIntersection(List<DataPoint> left, List<DataPoint> right) {
		List<DataPoint> toRet = new ArrayList<DataPoint>();
		if (left == null || right == null || left.isEmpty() || right.isEmpty())
			return toRet;

		Date d = null;
		Boolean lon = null, ron = null, state = null;
		Iterator<DataPoint> lit = left.iterator(), rit = right.iterator();
		DataPoint ldp = getNext(lit), rdp = getNext(rit);
		while (ldp != null && rdp != null) {
			int c = ldp.getDate().compareTo(rdp.getDate());
			d = c <= 0 ? ldp.getDate() : rdp.getDate();
			if (c < 0) {
				lon = !ldp.isZero();
				ldp = getNext(lit);
			}
			else if (c > 0) {
				ron = !rdp.isZero();
				rdp = getNext(rit);
			}
			else {
				lon = !ldp.isZero();
				ron = !rdp.isZero();
				ldp = getNext(lit);
				rdp = getNext(rit);
			}

			if (lon != null || ron != null) {
				@SuppressWarnings("null") // should not be null given above line, but 1.6 compiler is confused.
				boolean newState = (lon != null && lon) && (ron != null && ron);
				if (state == null || state.booleanValue() != newState) {
					toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(d, newState ? Boolean.TRUE : Boolean.FALSE));
					state = newState;
				}
			}
		}
		return toRet;
	}

	/**
	 * Given two lists of data points using a stepwise interpolation, return a
	 * binary stepwise list of datapoints where either input is non-zero.
	 * precodition: both arguments must be in temporal order.
	 * 
	 * @param <T>
	 * @param left
	 * @param right
	 * @return a binary stepwise list of datapoints where either input is
	 *         non-zero.
	 */
	public static List<DataPoint> getBinaryUnion(List<DataPoint> left, List<DataPoint> right) {
		List<DataPoint> toRet = new ArrayList<DataPoint>();
		if (left == null || left.isEmpty()) {
			if (right == null || right.isEmpty())
				return toRet;
			toRet.addAll(right);
			return toRet;
		}
		else if (right == null || right.isEmpty()) {
			toRet.addAll(left);
			return toRet;
			
		}
		
		Date d = null;
		Boolean lon = null, ron = null, state = null;
		Iterator<DataPoint> lit = left.iterator(), rit = right.iterator();
		DataPoint ldp = getNext(lit), rdp = getNext(rit);
		while (ldp != null && rdp != null) {
			int c = ldp.getDate().compareTo(rdp.getDate());
			d = c <= 0 ? ldp.getDate() : rdp.getDate();
			if (c < 0) {
				lon = !ldp.isZero();
				ldp = getNext(lit);
			}
			else if (c > 0) {
				ron = !rdp.isZero();
				rdp = getNext(rit);
			}
			else {
				lon = !ldp.isZero();
				ron = !rdp.isZero();
				ldp = getNext(lit);
				rdp = getNext(rit);
			}

			if (lon != null || ron != null) {
				@SuppressWarnings("null") // should not be null given above line, but 1.6 compiler is confused.
				boolean newState = (lon != null && lon) || (ron != null && ron);
				if (state == null || state.booleanValue() != newState) {
					toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(d, newState ? Boolean.TRUE : Boolean.FALSE));
					state = newState;
				}
			}
		}
		while (ldp != null) {
			boolean newState = !ldp.isZero();
			if (state == null || state.booleanValue() != newState) {
				toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(ldp.getDate(), newState ? Boolean.TRUE : Boolean.FALSE));
				state = newState;
			}
			ldp = getNext(lit);
		}
		while (rdp != null) {
			boolean newState = !rdp.isZero();
			if (state == null || state.booleanValue() != newState) {
				toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(rdp.getDate(), newState ? Boolean.TRUE : Boolean.FALSE));
				state = newState;
			}
			rdp = getNext(rit);
		}
		return toRet;
	}
	
	private static DataPoint getNext(Iterator<DataPoint> it) {
		DataPoint toRet = null;
		while (toRet == null && it.hasNext())
			toRet = it.next();
		return toRet;
	}

	public static <T> List<DataPoint<T>> getOverlay(List<DataPoint<T>> top, List<DataPoint<T>> bottom) {
		if (bottom == null || bottom.isEmpty())
			return top == null ? null : new ArrayList<DataPoint<T>>(top);
		if (top == null || top.isEmpty())
			return new ArrayList<DataPoint<T>>(bottom);

		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>();

		Date topStart = top.get(0).getDate();
		Date topEnd = top.get(top.size() - 1).getDate();
		int bix = 0;
		while (bix < bottom.size() && topStart.after(bottom.get(bix).getDate()))
			toRet.add(bottom.get(bix++));
		
		if (bix < bottom.size() && topStart.equals(bottom.get(bix).getDate()) && bix != 0) {
			toRet.add(bottom.get(bix++));			
		}

		for (DataPoint dp : top)
			toRet.add(dp);
		while (bix < bottom.size() && topEnd.after(bottom.get(bix).getDate()))
			bix++;

		if (bix < bottom.size()) {
			if (bottom.get(bix).getDate().compareTo(topEnd) != 0) {
				if (bix != 0)
					toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(top.get(top.size() - 1).getDate(), bottom.get(bix - 1).getValue()));
			}

			while (bix < bottom.size())
				toRet.add(bottom.get(bix++));
		}
		return toRet;
	}

	public static <T> List<DataPoint<Boolean>> getCoverageMerge(Date start, Date end, List<DataPoint<T>> top, List<DataPoint<T>> bottom) {
		// This isn't the most efficient way to do this, but it's the most easy to fix with future possible changes.
		List<Pair<Date, Date>> coverageTop = getCoverage(top); 
		List<Pair<Date, Date>> coverage = getCoverage(bottom);

		for (Iterator<Pair<Date, Date>> it = coverageTop.iterator(); it.hasNext();) {
			Pair<Date, Date> coveragePeriod = it.next();
			if ((coveragePeriod.getRight() != null && coveragePeriod.getRight().before(start)) || coveragePeriod.getLeft().after(end))
				it.remove();
		}
		// start with the notion anything in between (inclusive) the replacement coverage area gets destroyed
		Date topStart = start;
		Date topEnd = end;
		if (!coverageTop.isEmpty()) {
			topStart = coverageTop.get(0).getLeft();
			topEnd = coverageTop.get(coverageTop.size() - 1).getRight();
			if (topEnd == null)
				topEnd = end;
		}
		
		Pair<Pair<Date, Date>, Pair<Date, Date>> startEdgeTrim = null;
		Pair<Pair<Date, Date>, Pair<Date, Date>> endEdgeTrim = null;
		for (Iterator<Pair<Date, Date>> it = coverage.iterator(); it.hasNext();) {
			Pair<Date, Date> coveragePeriod = it.next();
			// Four behaviors per change 2 symmetric timepoints
			// (a) keep or (c) trim
			if ((coveragePeriod.getRight() != null && coveragePeriod.getRight().before(topStart)) && coveragePeriod.getLeft().before(start)) {
				if (coveragePeriod.getRight().after(start)) {
					// (c) trim
					startEdgeTrim = new Pair<Pair<Date, Date>, Pair<Date, Date>>(coveragePeriod, new Pair<Date, Date>(coveragePeriod.getLeft(), start));
				}
				continue;
			}
			else if (coveragePeriod.getLeft().after(topEnd) && (coveragePeriod.getRight() == null || coveragePeriod.getRight().after(end))) {
				if (coveragePeriod.getLeft().before(end)) {
					// (c) trim					
					endEdgeTrim = new Pair<Pair<Date, Date>, Pair<Date, Date>>(coveragePeriod, new Pair<Date, Date>(end, coveragePeriod.getRight()));
				}
				continue;
			}
			
			// (b) delete [a merge, if ever a useful behavior, is also a delete on this profile.]
			it.remove();
		}
		if (startEdgeTrim != null) {
			int i = coverage.indexOf(startEdgeTrim.getLeft());
			coverage.remove(i);
			coverage.add(i, startEdgeTrim.getRight());
		}
		if (endEdgeTrim != null) {
			int i = coverage.indexOf(endEdgeTrim.getLeft());
			coverage.remove(i);
			coverage.add(i, endEdgeTrim.getRight());
		}
		
		int insertIndex = -1;
		for (int i = 0; i < coverage.size(); ++i) {
			if (!coverage.get(i).getLeft().before(end)) {
				insertIndex = i;
				break;
			}
		}
		if (insertIndex != -1)
			coverage.addAll(insertIndex, coverageTop);
		else
			coverage.addAll(coverageTop);

		List<DataPoint<Boolean>> toRet = new ArrayList<DataPoint<Boolean>>(coverage.size() * 2);
		for (Pair<Date, Date> coveragePeriod : coverage) {
			toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(coveragePeriod.getLeft(), Boolean.TRUE));
			if (coveragePeriod.getRight() != null)
				toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(coveragePeriod.getRight(), Boolean.FALSE));
		}
		return toRet;
	}
	
	protected static <T> List<Pair<Date, Date>> getCoverage(List<DataPoint<T>> profileData) {
		List<Pair<Date, Date>> toRet = new ArrayList<Pair<Date, Date>>();
		if (profileData == null || profileData.size() == 0)
			return toRet;
		
		Date start = null;
		for (DataPoint dp : profileData) {
			boolean currState = !dp.isZero();
			if (currState && start == null)
				start = dp.getDate();
			if (!currState && start != null && dp.getDate() != null) {
				toRet.add(new Pair<Date, Date>(start, dp.getDate()));
				start = null;
			}
		}
		// handle open ended coverage at end.
		if (start != null)
			toRet.add(new Pair<Date, Date>(start, null));
		return toRet;
	}
	
	public static <T> List<DataPoint<T>> trim(Date start, Date end, List<DataPoint<T>> dataPoints, boolean cropEnds) {
		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>(dataPoints.size());
		boolean foundStart = false;
		for (int i = 0; i < dataPoints.size(); ++i) {
			DataPoint currentPoint = dataPoints.get(i);
			if (!foundStart) {
				if (start == null) {
					toRet.add(currentPoint);
					foundStart = true;
					continue;
				}
				else if (currentPoint.getDate().after(start)) {
					if (i != 0) {
						if (cropEnds)
							toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(start, dataPoints.get(i - 1).getValue()));
						else
							toRet.add(dataPoints.get(i - 1));
					}
					foundStart = true;
				}
			}
			if (end != null && currentPoint.getDate().after(end)) {
				if (cropEnds)
					toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(end, (T)null));
				else
					toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(currentPoint.getDate(), (T)null));
				break;
			}
			else if (foundStart)
				toRet.add(currentPoint);
		}
		return toRet;
	} 
	
	public static <T> List<DataPoint<T>> stripNullTransitions(List<DataPoint<T>> dataPoints) {
		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>(dataPoints.size());
		for (int i = 0; i < dataPoints.size() - 1; ++i) {
			DataPoint left = dataPoints.get(i);
			DataPoint right = dataPoints.get(i + 1);
			if (left.getDate().before(right.getDate()) || left.getValue() != null)
				toRet.add(left);
		}
		toRet.add(dataPoints.get(dataPoints.size() - 1));
		return toRet;
	}
	
	public static <T> List<DataPoint<T>> stripInstantaneousTransitions(List<DataPoint<T>> dataPoints) {
		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>(dataPoints.size());
		for (int i = 0; i < dataPoints.size() - 1; ++i) {
			DataPoint left = dataPoints.get(i);
			DataPoint right = dataPoints.get(i + 1);
			if (left.getDate().before(right.getDate()))
				toRet.add(left);
		}
		toRet.add(dataPoints.get(dataPoints.size() - 1));
		return toRet;
	}
	
	
	/**
	 * We keep a point IF
	 * a) it's the first value present
	 * b) it's value is different than the point before
	 * c) keepEnd is true and the value BEFORE it is the same and the value after is different
	 * d) it's the last value present
	 * 
	 * @param dataPoints
	 * @param keepEnd
	 * @return
	 */
	public static <T> List<DataPoint<T>> stripRedundantMidpoints(List<DataPoint<T>> dataPoints, boolean keepEnd) {
		if (dataPoints == null || dataPoints.size() < 3)
			return dataPoints;
		// Annoying special case, but this makes the rest of the code easier to follow.
		if (dataPoints.size() == 3) {
			if (keepEnd || !CommonUtils.equals(dataPoints.get(0).getValue(), dataPoints.get(1).getValue()))
				return dataPoints;
			else
				return Arrays.<DataPoint<T>>asList(new DataPoint[]{dataPoints.get(0), dataPoints.get(2)});
		}
		
		
		List<DataPoint<T>> toRet = new ArrayList<DataPoint<T>>(dataPoints.size());
		// (a) (first value present)
		toRet.add(dataPoints.get(0));
		DataPoint before = null;
		DataPoint cursor = dataPoints.get(0);
		DataPoint after = dataPoints.get(1);
		for(int i = 1; i < dataPoints.size() - 1; ++i) {
			before = cursor;
			cursor = after;
			after = dataPoints.get(i + 1);
			if (!CommonUtils.equals(before.getValue(), cursor.getValue()))
				toRet.add(cursor); // (b) (value is different than the point before)
			else if(keepEnd 
					&& CommonUtils.equals(before.getValue(), cursor.getValue()) 
					&& !CommonUtils.equals(cursor.getValue(), after.getValue()))
				toRet.add(cursor); // (c) (keepEnd is true and the value BEFORE it is the same and the value after is different)
		}
		
		// (d) (last value present or null if penultimate value is identical)
		DataPoint penultimate = dataPoints.get(dataPoints.size() - 2);
		DataPoint ultimate = dataPoints.get(dataPoints.size() - 1);
		
		if (CommonUtils.equals(penultimate.getValue(), ultimate.getValue()) && !keepEnd)
			toRet.add(JScienceFactory.eINSTANCE.createEDataPoint(ultimate.getDate(), (T)null));
		else
			toRet.add(ultimate);
		
		return toRet;
	}

	public static <T> DataPoint<T> getDataPoint(Date time, DataPoint[] dataPoints, INTERPOLATION interpolationMethod, T defaultValue) {
		return getDataPoint(time, Arrays.asList(dataPoints), interpolationMethod, defaultValue);
	}

	private static int findLater(List<? extends DataPoint> dataPoints, Date time, int index) {
		if (index < 0 || index >= dataPoints.size() - 1)
			return index;
		int testIndex = index + 1;
		while (testIndex < dataPoints.size() - 2 && dataPoints.get(testIndex) == null)
			++testIndex;
		if (dataPoints.get(testIndex) != null && dataPoints.get(testIndex).getDate().equals(time))
			return findLater(dataPoints, time, testIndex);
		else
			return index;
	}
	
	public static <T> Pair<DataPoint<T>, DataPoint<T>> getSurroundingDataPoints(Date time, List<? extends DataPoint> dataPoints, INTERPOLATION interpolationMethod) {
		if (dataPoints == null || dataPoints.isEmpty()) {
			return null;
		}
		int searchResult = findLater(dataPoints, time, Collections.binarySearch(dataPoints, time, new DataPointComparator()));
		DataPoint<T> left = null, right = null;
		int rightIndex = 0;
		int leftIndex = 0;
		if (searchResult >= 0) {
			right = dataPoints.get(searchResult);
			if (searchResult-1 >= 0) {
				left = dataPoints.get(searchResult - 1);
			}
		}
		else {
			leftIndex = Math.abs(searchResult) - 2;
			rightIndex = Math.abs(searchResult) - 1;
			if(leftIndex < dataPoints.size() && leftIndex >= 0) {
				left = dataPoints.get(leftIndex);
			}
			if(rightIndex < dataPoints.size() && rightIndex >= 0) {
				right = dataPoints.get(rightIndex);
			}
		}
		
		return new Pair<DataPoint<T>, DataPoint<T>>(left, right);
	}

	@SuppressWarnings("unchecked")
	public static <T> DataPoint<T> getDataPoint(Date time, List<? extends DataPoint> dataPoints, INTERPOLATION interpolationMethod, T defaultValue) {
		if (dataPoints == null || dataPoints.isEmpty()) {
			return null;
		}
		int searchResult = findLater(dataPoints, time, Collections.binarySearch(dataPoints, time, new DataPointComparator()));
		if (searchResult >= 0) {
			return dataPoints.get(searchResult);
		}
		else if (searchResult == -1) {
			// prior to first point, return null.
			return (DataPoint) JScienceFactory.eINSTANCE.createEDataPoint(time, null);
		}
		else if (searchResult == -(dataPoints.size() + 1)) {
			// after last point, return last point.
			DataPoint last = dataPoints.get(dataPoints.size() - 1);
			if (last == null)
				return null;
			else {
				DataPoint<Object> newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(time, last.getValue());
				newDataPoint.getContributors().addAll(last.getContributors());
				return (DataPoint) newDataPoint;
			}
		}
		else if (interpolationMethod == INTERPOLATION.STEP) {
			int lowIx = Math.abs(searchResult) - 2;
			if (dataPoints.get(lowIx) == null) {
				return (DataPoint<T>) JScienceFactory.eINSTANCE.<Object>createEDataPoint(time, null);
			}
			DataPoint lowDataPoint = dataPoints.get(lowIx);
			DataPoint<Object> newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(time, lowDataPoint.getValue());
			newDataPoint.getContributors().addAll(lowDataPoint.getContributors());
			return (DataPoint) newDataPoint;
		}
		else if (interpolationMethod == INTERPOLATION.INSTANTANEOUS) {
			return JScienceFactory.eINSTANCE.createEDataPoint(time, defaultValue);
		}
		else if (interpolationMethod == INTERPOLATION.LINEAR) {
			int highIx = Math.abs(searchResult) - 1;
			DataPoint<T> left = dataPoints.get(highIx - 1);
			DataPoint<T> right = dataPoints.get(highIx);
			if (left == null || right == null)
				return null;
			double interpLocation = (time.getTime() - left.getDate().getTime()) / (double) (right.getDate().getTime() - left.getDate().getTime());

			T v1 = left.getValue();
			T v2 = right.getValue();
			if (v1 == null || v2 == null)
				return (DataPoint) JScienceFactory.eINSTANCE.createEDataPoint(time, null);
			Class<T> klass = (Class<T>) v1.getClass();
			if (v1 != null && v2 != null && v1.getClass() == v2.getClass()) {
				if (v1 instanceof Number) {
					double d1 = ((Number) v1).doubleValue();
					double d2 = ((Number) v2).doubleValue();
					Number value = interpLocation * d2 + (1 - interpLocation) * d1;
					if (v1 instanceof Double) {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value.doubleValue()));
					} else if (v1 instanceof Float) {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value.floatValue()));
					} else if (v1 instanceof Integer) {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value.intValue()));
					} else if (v1 instanceof Long) {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value.longValue()));
					} else if (v1 instanceof Short) {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value.shortValue()));
					} else {
						return JScienceFactory.eINSTANCE.createEDataPoint(time, klass.cast(value));
					}
				}
				else if (v1 instanceof Amount) {
					Amount a1 = ((Amount) v1).times(1 - interpLocation);
					Amount a2 = ((Amount) v2).times(interpLocation);
					Amount value = a1.plus(a2);
					return (DataPoint) JScienceFactory.eINSTANCE.createEDataPoint(time, value);
				}
			}
		}
		return null;
	}
	
	public static List<Date> getDates(List<? extends DataPoint> dataPoints) {
		List<Date> toRet = new ArrayList<Date>(dataPoints.size());
		for (DataPoint dp : dataPoints)
			toRet.add(dp.getDate());
		return toRet;
	}
	
	public static DateFormat getDateFormat() {
		if (PROFILE_DATE_FORMAT == null) {
			DateFormat defaultFormat = DateFormatRegistry.INSTANCE.getDefaultDateFormat();
			if (defaultFormat instanceof EarthTimeFlexibleFormat) {
				// For input purposes, be flexible.
				// But don't change the default to use the output format meant just for profiles.
				EarthTimeFlexibleFormat flexibleInputFormat = (EarthTimeFlexibleFormat) defaultFormat.clone();
				String timezoneString = EnsembleProperties.getStringPropertyValue("date.format.default", "GMT");
				flexibleInputFormat.setTimeZone(TimeZone.getTimeZone(timezoneString));
				String formatString = EnsembleProperties.getProperty("profile.date.format");
				if (formatString != null) {
					flexibleInputFormat.setDisplayFormat(formatString);
				}
				PROFILE_DATE_FORMAT = flexibleInputFormat;
			} else {
				PROFILE_DATE_FORMAT = defaultFormat;
			}
		}
		return PROFILE_DATE_FORMAT;
	}

	public static String getDisplayString(Profile profile, Date date) {
		if (profile == null) {
			return "No data";
		}
		return getDisplayString(profile, profile.getValue(date));
	}

	public static String getDisplayString(Profile profile, Object currentValue) {
		if (currentValue != null) {
			if (currentValue instanceof Number) {
				Unit units = profile.getUnits();
				if ((units == null) || (units == Unit.ONE) || units == BINARY_CHOICE) {
					return NUMBER_FORMAT.format(currentValue);
				}
				currentValue = AmountUtils.valueOf((Number)currentValue, units);
			}
			if (currentValue instanceof String) {
				return currentValue.toString();
			}
			if (currentValue instanceof Boolean) {
				return currentValue.toString();
			}
			if (currentValue instanceof EEnumLiteral) {
				EEnumLiteral eEnumLiteral = (EEnumLiteral) currentValue;
				return eEnumLiteral.getLiteral();
			}
			if (currentValue instanceof Amount) {
				return EnsembleAmountFormat.INSTANCE.formatAmount((Amount) currentValue);
			}
			if (currentValue instanceof PowerValue) {
				return getDisplayString(profile, ((PowerValue)currentValue).getStateValue());
			}
			@SuppressWarnings("unchecked")
			IStringifier stringifier = StringifierRegistry.getStringifier(currentValue.getClass());
			return stringifier.getDisplayString(currentValue);
		}
		return "N/A";
	}

	public static TemporalExtent getTemporalExtent(Profile<?> profile) {
		EList<?> points = profile.getDataPoints();
		boolean empty = points.isEmpty();
		if (!empty) {
			Date start = ((DataPoint<?>) points.get(0)).getDate();
			Date end = ((DataPoint<?>) points.get(points.size() - 1)).getDate();
			return new TemporalExtent(start, end);
		}
		return null;
	}
	
	public static TemporalExtent getTemporalExtentUnion(Collection<? extends Profile> profiles) {
		Date start = null;
		Date end = null;
		for (Profile<?> profile : profiles) {
			TemporalExtent te = getTemporalExtent(profile);
			if (te != null) {
				if (start==null || start.after(te.getStart())) {
					start = te.getStart();
				}
				if (end==null || end.before(te.getEnd())) {
					end = te.getEnd();
				}
			}
		}
		if (start==null || end==null) return null;
		return new TemporalExtent(start, end);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Quantity> Amount<T> getMin(Profile<T> profile) {
		AmountExtent<T> extent = (AmountExtent<T>) profile.getExtent();
		return extent == null ? null : extent.getMin();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Quantity> Amount<T> getMax(Profile<Amount<T>> profile) {
		AmountExtent<T> extent = (AmountExtent<T>) profile.getExtent();
		return extent == null ? null : extent.getMax();
	}
	
	/** @return the mean value of all the infinite number of points in the profile */
	public static <T extends Quantity> Amount<T> getMean(Profile<Amount<T>> profile) {
		long totalTime = 0;
		double weightedSum = 0;
		List<DataPoint<Amount<T>>> dataPoints = profile.getDataPoints();
		Date startOfInterval = null;
		for (DataPoint<Amount<T>> datapoint : dataPoints) {
			Date endOfInterval = datapoint.getDate();
			if (startOfInterval != null) {
				long lengthOfInterval = endOfInterval.getTime() - startOfInterval.getTime();
				Date midpointOfInterval = DateUtils.add(startOfInterval,  Amount.valueOf(lengthOfInterval/2, SI.MILLI(SI.SECOND)));
				Number meanValueForInterval = profile.getDataPoint(midpointOfInterval).getNumericValue();
				if (meanValueForInterval != null) {
					weightedSum += meanValueForInterval.doubleValue() * lengthOfInterval;
					totalTime += lengthOfInterval;
				}
			}
			startOfInterval = endOfInterval;
		}
		if (totalTime==0) return null;
		return (Amount<T>) Amount.valueOf(weightedSum/totalTime, 0, profile.getUnits());
	}

	/** @return the standard deviation from the mean of the non-interpolated points in the profile */
	public static <T extends Quantity> Amount<T> getStandardDeviation(Profile<Amount<T>> profile) {
		Amount<T> meanAmount = getMean(profile);
		if (meanAmount==null) return null;
		double mean = meanAmount.getEstimatedValue();
		double weightedVariance = 0;
		long totalTime = 0;
		List<DataPoint<Amount<T>>> dataPoints = profile.getDataPoints();
		Date startOfInterval = null;
		for (DataPoint<Amount<T>> datapoint : dataPoints) {
			Date endOfInterval = datapoint.getDate();
			if (startOfInterval != null) {
				long lengthOfInterval = endOfInterval.getTime() - startOfInterval.getTime();
				Date midpointOfInterval = DateUtils.add(startOfInterval,  Amount.valueOf(lengthOfInterval/2, SI.MILLI(SI.SECOND)));
				Number meanValueForInterval = profile.getDataPoint(midpointOfInterval).getNumericValue();
				if (meanValueForInterval != null) {
					double differenceFromMean = mean-meanValueForInterval.doubleValue();
					weightedVariance += (differenceFromMean  * differenceFromMean) * lengthOfInterval;
					totalTime += lengthOfInterval;
				}
			}
			startOfInterval = endOfInterval;
		}
		if (totalTime==0) return null;
		return (Amount<T>) Amount.valueOf(Math.sqrt(weightedVariance/totalTime), 0, profile.getUnits());
	}
	
	public static <T> boolean isNumeric(Profile<T> profile) {
		EDataType dataType = profile.getDataType();
		if (dataType==null) {
			List<DataPoint<T>> dataPoints = profile.getDataPoints();
			if (dataPoints.isEmpty()) return false;
			T value = dataPoints.get(0).getValue();
			if (value instanceof Number) return true;
			if (value instanceof Amount) return true;
			return false;
		}
		Class<?> instanceClass = dataType.getInstanceClass();
		if (instanceClass==null) return false; 
		return Number.class.isAssignableFrom(instanceClass);
	}

	private static IContentType PROFILE_CONTENT_TYPE = Platform.getContentTypeManager().getContentType("gov.nasa.ensemble.jscience.profile");
	
	public static boolean isProfileContentType (IResource resource) {
		if ((resource instanceof IFile) && resource.exists()) {
			try {
				IContentDescription cd = ((IFile)resource).getContentDescription();
				if (cd != null) {
					IContentType type = cd.getContentType();
					if ((type != null) && type.isKindOf(PROFILE_CONTENT_TYPE)) {
						return true;
					}
				}
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}
		return false;
	}

	public static void debugProfile(Profile<?> p) {
		StringBuffer buffer = new StringBuffer();
		debugProfile(p, buffer);
		System.out.print(buffer.toString());
	}

	public static void debugProfile(Profile<?> p, StringBuffer buffer) {
		buffer.append(p.getId()+"\n");
		for (DataPoint<?> pt : p.getDataPoints()) {
			String dateString = DATE_STRINGIFIER.getDisplayString(pt.getDate());
			String valueString = getDisplayString(p, pt.getValue());
			buffer.append("\t" + dateString + ": " + valueString+"\n");
		}
	}
	
	public static String getStructuralFeatureProfileId(EObject object, EStructuralFeature feature) {
		String id = getObjectId(object);
		return id + "_" + feature.getName();
	}

	public static String getObjectId(EObject object) {
		String id = null;
		String objectId = EcoreUtil.getID(object);
		if (objectId != null) {
			id = objectId;
		} else {
			IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
			if (lp != null) {
				id = lp.getText(object);
			} else {
				EStructuralFeature f = object.eClass().getEStructuralFeature("name");
				if (f != null) {
					id = (String) object.eGet(f);
				}
			}
		}
		if (id == null || id.trim().length() == 0) {
			id = "?";
		}
		return id;
	}

	public static Map<String, Map<String, Profile>> getProfilesByCategory(Collection<? extends Profile> profiles) {
		if (profiles==null) return Collections.EMPTY_MAP;
		Map<String, Map<String, Profile>> toRet = new LinkedHashMap<String, Map<String, Profile>>();
		for (Profile profile : profiles) {
			if (profile == null) {
				continue;
			}
			String category = profile.getCategory();
			if (category==null) category = UNCATEGORIZED;
			if (!toRet.containsKey(category))
				toRet.put(category, new HashMap<String, Profile>());
			toRet.get(category).put(profile.getId(), profile);
		}
		return toRet;
	}

	public static Map<String, Profile> getProfilesById(Collection<? extends Profile> profiles) {
		if (profiles==null) return Collections.EMPTY_MAP;
		Map<String, Profile> result = new LinkedHashMap<String, Profile>(profiles.size());
		for (Profile profile : profiles) {
			result.put(profile.getId(), profile);
		}
		return result;
	}
	
	/**
	 * @param profiles1
	 * @param profiles2
	 * @return A set containing all profiles in profiles1, and those profiles in profiles2 that are not in profiles1 (by id).
	 */
	public static Collection<Profile> unionProfiles(
			Collection<? extends Profile> profiles1,
			Collection<? extends Profile> profiles2) {
		Map<String, Profile> map = getProfilesById(profiles1);
		for (Profile profile : profiles2) {
			if (!map.containsKey(profile.getId())) {
				map.put(profile.getId(), profile);
			}
		}
		return map.values();
	}

	public static <T> void trim(Date start, Date end, Profile<T> profile) {
		if (start != null || end != null) {
			List<DataPoint<T>> dataPoints = trim(start, end, profile.getDataPoints(), false);
			profile.setDataPoints(dataPoints);
		}
	}
	
	/** @return a new copy of a profile that has earlier and later datapoints removed. */
	public static <T> Profile<T> trimmedSubset(Profile<T> profile, Date start, Date end) {
		Profile<T> result = EcoreUtil.copy(profile);
		List<DataPoint<T>> dataPoints = trim(start, end, result.getDataPoints(), true);
		result.setDataPoints(dataPoints);
		return result;
	}

	/**
	 * Merges two conditions.  For boolean conditions, replaces datapoints in the bottom from ones in the top, but only in the time span covered.
	 * @param start -- time span covered; defaults to left edge of top
	 * @param end -- time span covered; defaults to right edge of top
	 * @param top -- source, whose datapoints take precedence; not modified
	 * @param bottom -- destination, modified by partially overwriting with top
	 */
	public static <T> void coverageMerge(Date start, Date end, Profile<T> top, Profile<T> bottom) {
		// CoverageMerge should only be used for boolean conditions 
		List<DataPoint<T>> dataPoints = null;
		if (top.getDataType() == EcorePackage.Literals.EBOOLEAN_OBJECT && bottom.getDataType() == EcorePackage.Literals.EBOOLEAN_OBJECT) {
			EList<DataPoint<T>> topData = top.getDataPoints();
	
			if (topData.isEmpty() && (start==null || end==null)) {
				return;
			}
	
			// If no start or end is specified, use the start and end of the top
			// condition
			if (start == null) {
				start = topData.get(0).getDate();
			}
			if (end == null) {
				end = topData.get(topData.size() - 1).getDate();
			}
			dataPoints = (List) getCoverageMerge(start, end, topData, bottom.getDataPoints());
		} else {
			dataPoints = getOverlay(trim(start, end, top.getDataPoints(), false), bottom.getDataPoints());
		}
		bottom.setDataPoints(dataPoints);
		copyAttributes(top, bottom);
	}
	
	private static Profile copyAttributes(Profile source, Profile dest) {
		dest.getAttributes().map().putAll(source.getAttributes().map());
		dest.setCategory(source.getCategory());
		dest.setDataType(source.getDataType());
		dest.setId(source.getId());
		dest.setInterpolation(source.getInterpolation());
		dest.setName(source.getName());
		dest.setUnits(source.getUnits());
		dest.setValid(source.isValid());
		return dest;
	}

	/**
	 * Utility method to get and load the resource
	 * @param monitor 
	 */
	public static Resource getResource(IResource iResource, IProgressMonitor monitor, boolean load_data_points) {
		try {
			Job.getJobManager().beginRule(iResource, monitor);
			if (monitor.isCanceled()) {
				return null;
			}
			ResourceSet resourceSet = EMFUtils.createResourceSet();
			URI uri = EMFUtils.getURI(iResource);
			Resource resource = resourceSet.getResource(uri, false);
			try {
				if (resource == null) {
					resource = resourceSet.createResource(uri);
				}
				Map options = new LinkedHashMap();
				if (!load_data_points) {
					options.put(IGNORE_DATA_POINTS, Boolean.TRUE);
				}
				options.putAll(ProgressMonitorInputStream.option(monitor));
				if (monitor.isCanceled()) {
					return null;
				}
				resource.load(options);
			} catch (Exception e) {
				try {
					resource.load(null);
				} catch (Exception x) {
					LogUtil.warnOnce("loading "+uri);
					return null;
				}
			}
			return resource;
		} finally {
			Job.getJobManager().endRule(iResource);
		}
	}
	
	public static EDataType getInferredType(Collection<?> values) {
		EDataType inferredType = null;
		if (values != null) {
			for (Object datum : values) {
				if (datum == null) {
					continue;
				}
				if (datum instanceof DataPoint) {
					datum = ((DataPoint) datum).getValue();
				}
				if (datum instanceof Amount) {
					Amount amount = (Amount) datum;
					datum = AmountUtils.getNumericValue(amount);
				}
				if (datum instanceof EEnumLiteral) {
					return ((EEnumLiteral) datum).getEEnum();
				}
				if ((datum instanceof Float) || (datum instanceof Double)) {
					if ((inferredType == null) || (inferredType == EcorePackage.Literals.ELONG_OBJECT)) {
						inferredType = EcorePackage.Literals.EDOUBLE_OBJECT;
					} else if (inferredType != EcorePackage.Literals.EDOUBLE_OBJECT) {
						// if we inferred some other non-double non-long that's
						// bad
						LogUtil.error("inferred inconsistent types: DOUBLE and " + inferredType.getName());
					}
					continue;
				}
				if ((datum instanceof Integer) || (datum instanceof Long)) {
					if (inferredType != EcorePackage.Literals.EDOUBLE_OBJECT) {
						// double is already compatible so we skip this section
						// if we are double
						if (inferredType == null) {
							inferredType = EcorePackage.Literals.ELONG_OBJECT;
						} else if (inferredType != EcorePackage.Literals.ELONG_OBJECT) {
							// if we inferred some other non-double non-long
							// that's bad
							LogUtil.error("inferred inconsistent types: LONG and " + inferredType.getName());
						}
					}
					continue;
				}
				if (datum instanceof Boolean) {
					if (inferredType == null) {
						inferredType = EcorePackage.Literals.EBOOLEAN_OBJECT;
					} else if (inferredType != EcorePackage.Literals.EBOOLEAN_OBJECT) {
						// if we inferred some other non-boolean that's bad
						LogUtil.error("inferred inconsistent types: BOOLEAN and " + inferredType.getName());
					}
				}
				if (datum instanceof String) {
					String s = (String) datum;
					if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
						continue;
					}
					double d;
					try {
						d = Double.parseDouble(s);
					} catch (NumberFormatException ex) {
						return EcorePackage.Literals.ESTRING;
					}
					if (Math.floor(d) != d)
						inferredType = EcorePackage.Literals.EDOUBLE_OBJECT;
					else if (d < 0 || d > 1)
						inferredType = EcorePackage.Literals.ELONG_OBJECT;
				}
			}
		}
		if (inferredType == null) {
			// this was the default before so I am preserving it
			return EcorePackage.Literals.EBOOLEAN_OBJECT;
		}
		return inferredType;
	}

	/**
	 * Return the unit of the first amount that is in the values, if any.
	 * 
	 * @param values
	 * @return the unit
	 */
	public static Unit getInferredUnit(Collection<?> values) {
		for (Object value : values) {
			if (value instanceof Amount) {
				Amount amount = (Amount) value;
				if (amount != null) {
					Unit unit = amount.getUnit();
					if (unit != null) {
						return unit;
					}
				}
			}
		}
		return null;
	}

	public static Object convertToType(EDataType type, Object obj) {
		if (obj == null) {
			return null;
		}
		if (type.equals(EcorePackage.Literals.EBOOLEAN_OBJECT)) {
			return convertToBoolean(obj);
		} else if (type.equals(EcorePackage.Literals.ELONG_OBJECT)) {
			return convertToLong(obj);
		} else if (type.equals(EcorePackage.Literals.EINTEGER_OBJECT)) {
			return convertToInteger(obj);
		} else if (type.equals(EcorePackage.Literals.EDOUBLE_OBJECT)) {
			return convertToDouble(obj);
		} else if (type instanceof EEnum) {
			EEnum enumType = (EEnum) type;
			if (obj instanceof EEnumLiteral && ((EEnumLiteral) obj).getEEnum().equals(enumType)) {
				return obj;
			}
			String stringValue = obj.toString();
			// check literals and names for equality.
			for (EEnumLiteral literal : enumType.getELiterals()) {
				if (literal.getLiteral().equals(stringValue) || literal.getName().equals(stringValue))
					return literal;
			}
			// check literals and names for approximate equality
			for (EEnumLiteral literal : enumType.getELiterals()) {
				if (approxEquals(literal.getLiteral(), stringValue) || approxEquals(literal.getName(), stringValue))
					return literal;
			}
		}
		try {
			Object value = EcoreUtil.createFromString(type, obj.toString());
			if (value instanceof String) {
				value = ((String) value).intern();
			}
			return value;
		} catch (IllegalArgumentException ex) {
			LogUtil.error("Unable to convert " + obj + " to type " + type);
			return null;
		}
	}

	// Test for equality of letters and digits, ignoring whitespace and special characters.
	private static boolean approxEquals(String s1, String s2) {
		int i = 0;
		int j = 0;
		while (i < s1.length() && j < s2.length()) {
			int s1Char = s1.codePointAt(i);
			if (!Character.isLetterOrDigit(s1Char)) {
				++i;
				continue;
			}
			int s2Char = s2.codePointAt(j);
			if (!Character.isLetterOrDigit(s2Char)) {
				++j;
				continue;
			}
			// ignore case in the same way as the String.equalsIgnoreCase method does.
			if (Character.toUpperCase(s1Char) == Character.toUpperCase(s2Char) ||
				Character.toLowerCase(s1Char) == Character.toLowerCase(s2Char)) {
				++i;
				++j;
				continue;
			}
			break;
		}
		while (i < s1.length() && !Character.isLetterOrDigit(s1.codePointAt(i)))
			++i;
		while (j < s2.length() && !Character.isLetterOrDigit(s2.codePointAt(j)))
			++j;
		return i == s1.length() && j == s2.length();
	}

	public static void convertMapKeysToType(EDataType type, Map<?, Object> source) {
		for (Map.Entry<?, Object> entry : source.entrySet()) {
			entry.setValue(convertToType(type, entry.getValue()));
		}
	}

	private static Boolean convertToBoolean(Object obj) {
		if (obj instanceof Boolean)
			return (Boolean) obj;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
		if (obj instanceof String) {
			String s = (String) obj;
			if (s.equalsIgnoreCase("true"))
				return Boolean.TRUE;
			else if (s.equalsIgnoreCase("false"))
				return Boolean.FALSE;
			try {
				return Double.parseDouble(s) == 0 ? Boolean.FALSE : Boolean.TRUE;
			} catch (NumberFormatException ex) {
				// wha, return FALSE.
			}
		}
		return null;
	}

	private static final Long LONG_ZERO = new Long(0);
	private static final Long LONG_ONE = new Long(1);

	private static Long convertToLong(Object obj) {
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? LONG_ONE : LONG_ZERO;
		if (obj instanceof Number)
			return ((Number) obj).longValue();
		if (obj instanceof String) {
			String s = (String) obj;
			if (s.equalsIgnoreCase("true"))
				return LONG_ONE;
			else if (s.equalsIgnoreCase("false"))
				return LONG_ZERO;
			try {
				if (s.indexOf('.') != -1)
					return new Long(s.substring(0, s.indexOf('.')));
				else
					return new Long(s);
			} catch (NumberFormatException ex) {
				return null;
			}
		}
		return null;
	}

	private static final Integer INTEGER_ZERO = new Integer(0);
	private static final Integer INTEGER_ONE = new Integer(1);

	private static Integer convertToInteger(Object obj) {
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? INTEGER_ONE : INTEGER_ZERO;
		if (obj instanceof Number)
			return ((Number) obj).intValue();
		if (obj instanceof String) {
			String s = (String) obj;
			if (s.equalsIgnoreCase("true"))
				return INTEGER_ONE;
			else if (s.equalsIgnoreCase("false"))
				return INTEGER_ZERO;
			try {
				if (s.indexOf('.') != -1)
					return new Integer(s.substring(0, s.indexOf('.')));
				else
					return new Integer(s);
			} catch (NumberFormatException ex) {
				return null;
			}
		}
		return null;
	}

	private static final Double DOUBLE_ZERO = new Double(0);
	private static final Double DOUBLE_ONE = new Double(1);

	private static Double convertToDouble(Object obj) {
		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? DOUBLE_ONE : DOUBLE_ZERO;
		if (obj instanceof Number)
			return ((Number) obj).doubleValue();
		if (obj instanceof String) {
			String s = (String) obj;
			if (s.equalsIgnoreCase("true"))
				return DOUBLE_ONE;
			else if (s.equalsIgnoreCase("false"))
				return DOUBLE_ZERO;
			try {
				return new Double(s);
			} catch (NumberFormatException ex) {
				return null;
			}
		}
		return null;
	}

	public static String getId(String name) {
		if (name == null || name.equals(""))
			throw new NullPointerException("Cannot convert null or empty string into ID.");
		String source = name.toUpperCase();
		StringBuffer result = new StringBuffer(source.length());
		boolean underscoreLast = false;
		for (int i = 0; i < source.length(); ++i) {
			char c = source.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				if (Character.getNumericValue(c) >= 0) {
					result.append(c);
				} else {
					result.append("x" + Integer.toHexString(c));
				}
				underscoreLast = false;
			}
			else if (!underscoreLast) {
				result.append("_");
				underscoreLast = true;
			}
		}
		if (result.length() == 1 && result.charAt(0) == '_')
			return "_";
		int start = result.charAt(0) == '_' ? 1 : 0;
		int end = result.charAt(result.length() - 1) == '_' ? result.length() - 1 : result.length();
		return result.substring(start, end);
	}
}
