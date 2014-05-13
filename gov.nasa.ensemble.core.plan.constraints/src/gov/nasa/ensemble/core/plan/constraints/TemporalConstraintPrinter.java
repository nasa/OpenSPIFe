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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;

import java.util.Iterator;
import java.util.List;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jscience.physics.amount.Amount;

/**
 *	An PlanPrinter for creating text descriptions of of temporal relations
 */
public class TemporalConstraintPrinter extends TemporalPrinter {
	
	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(TemporalConstraintPrinter.class);
	
	private static final IStringifier<Amount<Duration>> TIME_OF_DAY_STRINGIFIER = new TimeOfDayStringifier();
	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);
	
	public TemporalConstraintPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		super(identifiableRegistry);
	}
	
	/**
	 * Create an instance of form text prepared to display a plan element. 
	 * @param toolkit
	 * @param parent
	 * @return the form text instance.
	 */
	public FormText createConstraintText(FormToolkit toolkit, Composite parent) {
		final FormText text = createPlanText(toolkit, parent);
		final FontData fontData = parent.getFont().getFontData()[0];
		final Font font = new Font(null, fontData.getName(), fontData.getHeight(), SWT.ITALIC);
		text.setFont("rationale", font);
		return text;
	}
	
	/**
	 * Returns the constraint as a form text string
	 * @param bound 
	 * @param selected indicates if text should be formatted using getTextSelected
	 * @return the constraint as a form text string
	 */
	public String getText(PeriodicTemporalConstraint bound, boolean selected) {
		// extract the fields
		final EPlanElement element = bound.getPoint().getElement();
		final Amount<Duration> earliest = bound.getEarliest();
		final Amount<Duration> latest   = bound.getLatest();
		
		// construct the string
		String string = (selected ? getTextSelected(element) : getText(element));
		if ((earliest == null) && (latest == null)) {
			string += " has a null constraint.";
			return string;
		}
		string += " must ";
		string += getHypertext(bound.getPoint(), false);
		
		final String searliest = TIME_OF_DAY_STRINGIFIER.getDisplayString(earliest);
		final String slatest   = TIME_OF_DAY_STRINGIFIER.getDisplayString(latest);
		if (earliest == null) {
			string += " no later than "; // at or before
			string += slatest;
		} else if (latest == null) {
			string += " no earlier than "; // at or after
			string += searliest;
		} else if (earliest.compareTo(latest) == 0) {
			string += " at ";
			string += searliest;
		} else {
			// both earliest and latest are not null
			string += " between ";
			string += searliest;
			string += " and ";
			string += slatest;
		}
		string += " on the day it is scheduled.";
		return string;
	}
	
	/**
	 * Returns the chain as a form text string
	 * @param origin 
	 * @param constraint 
	 * @return the chain as a form text string
	 */
	public String getText(TemporalChain chain, EPlanElement origin) {
		List<EPlanElement> elements = chain.getElements();
		String string = "";
		Iterator<EPlanElement> iterator = elements.iterator();
		while (iterator.hasNext()) {
			EPlanElement element = iterator.next();
			if (element == origin) {
				string += getTextSelected(element);
			} else {
				string += getText(element);
			}
			if (iterator.hasNext()) {
				string += " - ";
			}
		}
		return string;
	}

	/**
	 * Returns the constraint as a form text string, from the perspective of the origin node
	 * @param constraint 
	 * @param origin
	 * @return the constraint as a form text string
	 */
	public String getText(BinaryTemporalConstraint constraint, EPlanElement originElement) {
		// extract the fields
		Amount<Duration> minimum = constraint.getMinimumBminusA();
		Amount<Duration> maximum = constraint.getMaximumBminusA();
		EPlanElement planElementA = constraint.getPointA().getElement();
		EPlanElement planElementB = constraint.getPointB().getElement();
		if (minimum == null && maximum == null) {
			return "Invalid constraint on " +
					getText(planElementA) + " and " + getText(planElementB)
					+ ".  Both min and max separation are null.";
		}
		
		String description = ConstraintUtils.getDescription(constraint, true);
		if (description == null) {
			return "Invalid constraint.";
		}
		
		// Debug string matches binary constraint classifications found on confluence:
		// https://ensemble.jpl.nasa.gov/confluence/display/design/Binary+Temporal+Constraint+Classifications
		
		boolean aHasEndpoint = constraint.getPointA().hasEndpoint();
		
		// construct the string
		String string = "";
		// X must
		string += (originElement == planElementA ? getTextSelected(planElementA) : getText(planElementA));
		if (aHasEndpoint) {
			string += " must ";
		} else {
			string += " ";
		}
		// start
		string += getHypertext(constraint.getPointA(), false);
		if (!aHasEndpoint) {
			string += " must occur";
		}
		//
		string += " " + description + " ";
		// Y
		string += (originElement == planElementB ? getTextSelected(planElementB) : getText(planElementB));
		string += " ";
		// starts
		string += getHypertext(constraint.getPointB(), true);
		string += ".";
		
		return string;
	}
		
	/**
	 * Returns the duration as a formated string
	 * @param offset - the duration
	 * @return 
	 */
	public String getOffsetText(Amount<Duration> offset) {
		if (offset.compareTo(ZERO) == 0) {
			return "at the same time as";
		}
		long duration = offset.abs().longValue(SI.SECOND);
        String string = DurationFormat.getEnglishDuration(duration);
        string += " ";
		if (offset.isLessThan(ZERO)) {
			string += "after";
		} else {
			string += "before";
		}
		return string;
	}

	/**
	 * Returns the rationale as a form text string, if it exists.
	 * @param constraint
	 * @return
	 */
	public String getRationaleText(BinaryTemporalConstraint constraint) {
		String rationale = constraint.getRationale();
		if ((rationale != null) && (rationale.trim().length() != 0)) {
			return "<span font=\"rationale\">\"" + StringEscapeFormat.escape(rationale) + "\"</span>";
		}
		return "";
	}

//    /**
//     * @param milliseconds
//     * @return a readable string for the given milliseconds
//     */
//    private String getSignedDurationString(long milliseconds) {
//        if (milliseconds == 0) {
//            return "at the same time as";
//        }
//        long duration = Math.abs(milliseconds/1000);
//        long hours = Math.round(Math.max(1, duration)/3600);
//        long minutes = Math.round(Math.max(1, duration)/60);
//        long minutes_left = (duration / 60) % 60;
//        long seconds_left = duration % 60;
//        String string = String.format("%02d:%02d:%02d", hours, minutes_left, seconds_left);
//        string += " (";
//        if (hours > 48) {
//            string += Math.round(hours/24) + " days";
//        } else if (minutes > 90) {
//            string += hours + " hours";
//        } else if (duration > 90) {
//            string += minutes + " minutes";
//        } else {
//            string += Math.round(duration) + " seconds";
//        }
//        string += ") ";
//        if (milliseconds < 0) {
//            string += "before";
//        } else {
//            // milliseconds > 0;
//            string += "after";
//        }
//        return string;
//    }
//    /**
//     * @param temporalRelation
//     * @param perspective
//     * @return a string describing this DistanceTemporalRelation from the given perspective
//     */
//    private String getRelationText(DistanceTemporalRelation temporalRelation,
//            RelationTextPerspective perspective) {
//        Activity thatActivity;
//        String thisTimepoint, thatTimepoint;
//        long minDelta = temporalRelation.getMinDeltaTime();
//        long maxDelta = temporalRelation.getMaxDeltaTime();
//        if (perspective == RelationTextPerspective.A) {
//            thatActivity = temporalRelation.getActivityB();
//            thisTimepoint = temporalRelation.getTimepointA();
//            thatTimepoint = temporalRelation.getTimepointB();
//        } else {
//            thatActivity = temporalRelation.getActivityA();
//            thisTimepoint = temporalRelation.getTimepointB();
//            thatTimepoint = temporalRelation.getTimepointA();
//            minDelta = -minDelta;
//            maxDelta = -maxDelta;
//        }
//        if (minDelta == maxDelta) {
//            String text = "must " + thisTimepoint + " exactly ";
//            text += getSignedDurationString(minDelta);
//            text += " " + thatTimepoint;
//            text += " of " + thatActivity;
//            return text;
//        }
//        if (minDelta == 0) {
//            String text = "must " + thisTimepoint;
//            text += " after the " + thatTimepoint;
//            text += " of " + thatActivity;
//            text += ", but no more than " + getSignedDurationString(maxDelta);
//            return text;
//        }
//        if (maxDelta == 0) {
//            String text = "must " + thisTimepoint + " ";
//            if (minDelta > 0) {
//            	text += "after";
//            } else {
//            	text += "before";
//            }
//            text += " the " + thatTimepoint;
//            text += " of " + thatActivity;
//            text += ", but no more than " + getSignedDurationString(minDelta);
//            return text;
//        }
//        final long minusInfinityDuration = Integer.MIN_VALUE;
//        if (minDelta <= minusInfinityDuration) {
//            String text = "can't " + thisTimepoint;
//            text += " later than " + getSignedDurationString(maxDelta);
//            text += " the " + thatTimepoint;
//            text += " of " + thatActivity;
//            return text;
//        }
//        final long plusInfinityDuration = Integer.MAX_VALUE;
//        if (maxDelta >= plusInfinityDuration) {
//            String text = "can't " + thisTimepoint;
//            text += " earlier than " + getSignedDurationString(minDelta);
//            text += " the " + thatTimepoint;
//            text += " of " + thatActivity;
//            return text;
//        }
//        // minDelta < maxDelta
//        String text = "must " + thisTimepoint;
//        text += " " + getSignedDurationString(minDelta);
//        text += " to " + getSignedDurationString(maxDelta);
//        text += " the " + thatTimepoint;
//        text += " of " + thatActivity;
//        return text;
//    }        

	
}
