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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class PinUtils {

	/**
	 * Get a list of all the pin constraints on all the provided elements
	 * @param selectedElements
	 * @return
	 */
	public static List<PeriodicTemporalConstraint> getPinConstraints(Collection<? extends EPlanElement> selectedElements) {
		List<PeriodicTemporalConstraint> bounds = new ArrayList<PeriodicTemporalConstraint>();
		for (EPlanElement element : selectedElements) {
			bounds.addAll(getPinConstraints(element));
		}
		return bounds;
	}

	/**
	 * Get a list of all the pin constraints on a particular plan element
	 * @param element
	 * @return
	 */
	public static List<PeriodicTemporalConstraint> getPinConstraints(EPlanElement element) {
		List<PeriodicTemporalConstraint> bounds = new ArrayList<PeriodicTemporalConstraint>();
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			List<PeriodicTemporalConstraint> elementBounds = constraintsMember.getPeriodicTemporalConstraints();
			for (PeriodicTemporalConstraint bound : elementBounds) {
				if (isPinConstraint(bound)) {
					bounds.add(bound);
				}
			}
		}
		return bounds;
	}

	/**
	 * Test a bound to see if it is a pin constraint
	 * @param bound
	 * @return true if the constraint is a pin constraint
	 */
	public static boolean isPinConstraint(PeriodicTemporalConstraint bound) {
		if (bound.getPoint().getEndpoint() == Timepoint.START) {
			Amount<Duration> earliest = bound.getEarliest();
			Amount<Duration> latest = bound.getLatest();
			return (earliest != null) && (latest != null) && (earliest.compareTo(latest) == 0);
		}
		return false;
	}

	/**
	 * Create a constraint pinning the element at the given date
	 * @param element
	 * @param date
	 * @return
	 */
	public static PeriodicTemporalConstraint createPinConstraint(EPlanElement element, Amount<Duration> offset) {
		if (element == null) {
			throw new NullPointerException("must specify a non-null element for createPinConstraint");
		}
		PeriodicTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		constraint.getPoint().setElement(element);
		constraint.getPoint().setEndpoint(Timepoint.START);
		constraint.setEarliest(offset);
		constraint.setLatest(offset);
		constraint.setRationale("pinned");
		return constraint;
	}

}
