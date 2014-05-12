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
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.List;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;


public class NogoodPrinter extends ConstraintViolationPrinter {

	public NogoodPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		super(identifiableRegistry);
	}
	
	public String getText(NogoodTemporalDistanceConstraint part) {
		BinaryTemporalConstraint constraint = part.getConstraint();
		ConstraintPoint pointA = constraint.getPointA();
		ConstraintPoint pointB = constraint.getPointB();
		Amount<Duration> minimum = constraint.getMinimumBminusA();
		Amount<Duration> maximum = constraint.getMaximumBminusA();
		if (minimum != null) {
			minimum = minimum.abs();
		}
		if (maximum != null) {
			maximum = maximum.abs();
		}
		return getDistanceRequirementText(pointA, pointB, minimum, maximum, false);
	}

	public String getText(NogoodTimepointConstraint part) {
		PeriodicTemporalConstraint constraint = part.getConstraint();
		String result = "The ";
		result += getHypertext(constraint.getPoint());
		result += getBoundRequirementText(constraint);
		return result;
	}

	public String getText(NogoodTemporalDuration part) {
		EPlanElement node = part.getPlanElement();
		if (node == null) {
			return "The duration of <a>$element$</a> is ???.";
		}
		TemporalExtent extent = node.getMember(TemporalMember.class).getExtent();
		String result = "The duration of ";
		result += getText(node);
		result += " is ";
		if (extent != null) {
			result += DurationFormat.getFormattedDuration((int)(extent.getDurationMillis()/1000));
		} else {
			result += "computing";
		}
		return result;
	}

	public String getText(NogoodTemporalChain part) {
		StringBuilder result = new StringBuilder();
		result.append("The following chained elements must occur one before another.<br/>");
		List<EPlanElement> participants = part.getParticipants();
		for (EPlanElement element : participants) {
			result.append(' ');
			result.append(getText(element));
		}
		return result.toString();
	}

	public String getText(NogoodContainment part) {
		StringBuilder result = new StringBuilder();
		EPlanElement element = part.getChild();
		result.append(getText(element));
		result.append(" has a conflicting temporal constraints with a parent element");
		return result.toString();
	}

}
