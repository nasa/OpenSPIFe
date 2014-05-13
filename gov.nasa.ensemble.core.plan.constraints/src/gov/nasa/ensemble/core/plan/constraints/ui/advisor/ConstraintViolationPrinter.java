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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalPrinter;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class ConstraintViolationPrinter extends TemporalPrinter {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);

	public ConstraintViolationPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		super(identifiableRegistry);
	}
	
	public String getDistanceText(BinaryTemporalConstraint temporalRelation) {
		ConstraintPoint pointA = temporalRelation.getPointA();
		ConstraintPoint pointB = temporalRelation.getPointB();
		Amount<Duration> minDelta = temporalRelation.getMinimumBminusA();
		Amount<Duration> maxDelta = temporalRelation.getMaximumBminusA();
		String result = getDistanceViolationText(pointA, pointB, minDelta, maxDelta);
		boolean violationPrinted = (result.length() > 0);
		if (violationPrinted) {
			result += "  ";
		}
		result += getDistanceRequirementText(pointA, pointB, minDelta, maxDelta, violationPrinted);
		return result;
	}

	public String getBoundText(PeriodicTemporalConstraint temporalBound) {
		String result = getBoundViolationText(temporalBound.getPoint());
		result += "  ";
		result += "It";
		result += getBoundRequirementText(temporalBound);
		return result;
	}

	/*
	 * Package protected helper functions which are tested by junit test case.
	 */
	
	/* package */ String getDistanceViolationText(ConstraintPoint pointA, ConstraintPoint pointB, Amount<Duration> minDelta, Amount<Duration> maxDelta) {
		Amount<Duration> delta = computeDelta(pointA, pointB);
		String result = "";
		if (minDelta != null && minDelta.approximates(ZERO) && maxDelta != null && maxDelta.approximates(ZERO) && delta.compareTo(ZERO) != 0) {
			result = "The " + getHypertext(pointA);
			result += " and the " + getHypertext(pointB);
			result += " are different.";
		} else if (maxDelta != null && maxDelta.approximates(ZERO)) {
			// A's timepoint must occur before B's timepoint
			result = "The " + getHypertext(pointA);
			result += " is after the " + getHypertext(pointB);
			result += ".";
		} else if (minDelta != null && delta.isLessThan(minDelta.abs())) {
			result = "The " + getHypertext(pointA);
			result += " and the " + getHypertext(pointB);
			result += " are ";
			result += DurationFormat.getEnglishDuration(minDelta.abs().minus(delta).longValue(SI.SECOND));
			result += " too close together.";
		} else if (maxDelta != null && delta.isGreaterThan(maxDelta.abs())) {
			result = "The " + getHypertext(pointA);
			result += " and the " + getHypertext(pointB);
			result += " are ";
			result += DurationFormat.getEnglishDuration(delta.minus(maxDelta.abs()).longValue(SI.SECOND));
			result += " too far apart.";
		}
		return result;
	}

	protected String getDistanceRequirementText(ConstraintPoint pointA, ConstraintPoint pointB, Amount<Duration> minDelta, Amount<Duration> maxDelta, boolean usePronoun) {
		String result = "";
		if (minDelta != null && minDelta.approximates(ZERO) && maxDelta != null && maxDelta.approximates(ZERO)) {
			result = getTwoNodeText(pointA, pointB, usePronoun, result);
			result += " should be the same.";
		} else if (minDelta != null && minDelta.approximates(ZERO)) {
			result += "The " + getHypertext(pointB);
			result += " should be no earlier than ";
			result += "the " + getHypertext(pointA);
			result += ".";
		} else if (maxDelta != null && maxDelta.approximates(ZERO)) {
			result += "The " + getHypertext(pointB);
			result += " should be no later than ";
			result += "the " + getHypertext(pointA);
			result += ".";
		} else {
			Amount<Duration> delta = computeDelta(pointA, pointB);
			result = getTwoNodeText(pointA, pointB, usePronoun, result);
			result += " should be separated by";
			if (minDelta != null && (maxDelta==null || minDelta.approximates(maxDelta))) {
				result += " exactly " + DurationFormat.getEnglishDuration(minDelta.longValue(SI.SECOND));
			} else if (minDelta != null && delta.isLessThan(minDelta)) {
				result += " at least " + DurationFormat.getEnglishDuration(minDelta.longValue(SI.SECOND));
			} else if (maxDelta != null && delta.isGreaterThan(maxDelta)) {
				result += " at most " + DurationFormat.getEnglishDuration(maxDelta.longValue(SI.SECOND));
			}
			result += ".";
		}
		return result;
	}

	protected String getBoundRequirementText(PeriodicTemporalConstraint temporalBound) {
		Date min = ConstraintUtils.getPeriodicConstraintEarliestDate(temporalBound);
		Date max = ConstraintUtils.getPeriodicConstraintLatestDate(temporalBound);
		String result = " should be ";
		if (max == null) {
			result += "after " + DATE_STRINGIFIER.getDisplayString(min);
		} else if (min == null) {
			result += "before " + DATE_STRINGIFIER.getDisplayString(max);
		} else if (min.equals(max)) {
			result += "at " + DATE_STRINGIFIER.getDisplayString(min);
		} else {
			result += "between " + DATE_STRINGIFIER.getDisplayString(min);
			result += " and " + DATE_STRINGIFIER.getDisplayString(max);
		}
		result += ".";
		return result;
	}

	/* package */ String getBoundViolationText(ConstraintPoint point) {
		Date current = point.getDate();
		String result = "The ";
		result += getHypertext(point);
		result += " is ";
		result += DATE_STRINGIFIER.getDisplayString(current);
		result += ".";
		return result;
	}
	
	private String getTwoNodeText(ConstraintPoint pointA, ConstraintPoint pointB, boolean usePronoun, String result) {
		if (usePronoun) {
			return "They";
		}
		return "The " + getHypertext(pointA)
		+ " and the " + getHypertext(pointB);
	}

	private Amount<Duration> computeDelta(ConstraintPoint pointA, ConstraintPoint pointB) {
		Date timeA = pointA.getDate();
		Date timeB = pointB.getDate();
		if ((timeA == null) || (timeB == null)) {
			return Amount.valueOf(-1, SI.SECOND); // error condition
		}
		Amount<Duration> delta = DateUtils.subtract(timeA, timeB).abs();
		return delta;
	}

}
