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
package gov.nasa.ensemble.core.model.plan.constraints.util;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class ConstraintUtils {

	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);
	
	public static PeriodicTemporalConstraint createConstraint(EPlanElement element, Timepoint endpoint, Amount<Duration> earliest, Amount<Duration> latest, String rationale) {
		PeriodicTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		constraint.getPoint().setElement(element);
		constraint.getPoint().setEndpoint(endpoint);
		constraint.setEarliest(earliest);
		constraint.setLatest(latest);
		constraint.setRationale(rationale);
		return constraint;
	}
	
	public static BinaryTemporalConstraint createConstraint(EPlanElement elementA, Timepoint endpointA, EPlanElement elementB, Timepoint endpointB, Amount<Duration> min, Amount<Duration> max, String rationale) {
		BinaryTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
		constraint.getPointA().setElement(elementA);
		constraint.getPointA().setEndpoint(endpointA);
		constraint.getPointB().setElement(elementB);
		constraint.getPointB().setEndpoint(endpointB);
		constraint.setMinimumBminusA(min);
		constraint.setMaximumBminusA(max);
		constraint.setRationale(rationale);
		return constraint;
	}
	
	public static ConstraintPoint createConstraintPoint(EPlanElement planElement, Timepoint timepoint) {
		ConstraintPoint constraintPoint = ConstraintsFactory.eINSTANCE.createConstraintPoint();
		constraintPoint.setElement(planElement);
		constraintPoint.setEndpoint(timepoint);
		return constraintPoint;
	}
	
	/*
	 * Periodic constraint utilities
	 */
	
	public static Date getPeriodicConstraintEarliestDate(PeriodicTemporalConstraint constraint) {
		Amount<Duration> earliest = constraint.getEarliest();
		if (earliest != null) {
			Date midnight = getPeriodicConstraintDate(constraint);
			if (midnight != null) {
				return DateUtils.add(midnight, earliest);
			}
		}
		return null;
	}
	
	public static Date getPeriodicConstraintLatestDate(PeriodicTemporalConstraint constraint) {
		Amount<Duration> latest = constraint.getLatest();
		if (latest != null) {
			Date midnight = getPeriodicConstraintDate(constraint);
			if (midnight != null) {
				return DateUtils.add(midnight, latest);
			}
		}
		return null;
	}
	
	private static Date getPeriodicConstraintDate(PeriodicTemporalConstraint constraint) {
		ConstraintPoint point = constraint.getPoint();
		Date date = point.getDate();
		if (date == null) {
			return null;
		}
		return MissionCalendarUtils.getMidnight(date);
	}
	
	public static Amount<Duration> getPeriodicConstraintOffset(Date date) {
		if (date == null) {
			return null;
		}
		Date midnight = MissionCalendarUtils.getMidnight(date);
		Amount<Duration> offset = DateUtils.subtract(date, midnight);
		return offset;
	}

	/*
	 * Attach/detach constraints
	 */
	
	public static void attachConstraint(final PeriodicTemporalConstraint constraint) {
		ConstraintsMember member = constraint.getPoint().getElement().getMember(ConstraintsMember.class, true);
		member.getPeriodicTemporalConstraints().add(constraint);
	}

	public static void detachConstraint(final PeriodicTemporalConstraint constraint) {
		ConstraintsMember member = constraint.getPoint().getElement().getMember(ConstraintsMember.class, true);
		member.getPeriodicTemporalConstraints().remove(constraint);
	}

	public static void attachConstraint(final BinaryTemporalConstraint constraint) {
		ConstraintsMember memberA = constraint.getPointA().getElement().getMember(ConstraintsMember.class, true);
		memberA.getBinaryTemporalConstraints().add(constraint);
		ConstraintsMember memberB = constraint.getPointB().getElement().getMember(ConstraintsMember.class, true);
		memberB.getBinaryTemporalConstraints().add(constraint);
	}

	public static void detachConstraint(final BinaryTemporalConstraint constraint) {
		EPlanElement elementA = constraint.getPointA().getElement();
		if (elementA != null) {
			ConstraintsMember memberA = elementA.getMember(ConstraintsMember.class, true);
			memberA.getBinaryTemporalConstraints().remove(constraint);
		}
		EPlanElement elementB = constraint.getPointB().getElement();
		if (elementB != null) {
			ConstraintsMember memberB = elementB.getMember(ConstraintsMember.class, true);
			memberB.getBinaryTemporalConstraints().remove(constraint);
		}
	}

	/*
	 * Access constraints
	 */
	
	public static Collection<ConstraintsMember> getConstraintMembersRecursively(EPlan plan) {
		return EPlanUtils.getMembers(plan, ConstraintsMember.class);
	}

	public static List<PeriodicTemporalConstraint> getPeriodicConstraints(EPlanElement element, boolean mustExist) {
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, mustExist);
		if (constraintsMember == null) {
			return Collections.emptyList();
		}
		return constraintsMember.getPeriodicTemporalConstraints();
	}
	
	/**
	 * Gets the constraint with the Earliest and Latest Start or End of a plan element.
	 * @param element -- activity or group
	 * @param whichEnd -- START or END
	 * @return null if unconstrained, else the constraint
	 */
	public static PeriodicTemporalConstraint getPeriodicConstraint(EPlanElement element, Timepoint whichEnd) {
		List<PeriodicTemporalConstraint> periodicConstraints = getPeriodicConstraints(element, false);
		if (periodicConstraints==null) return null;
		for (PeriodicTemporalConstraint constraint : periodicConstraints) {
			if (whichEnd==constraint.getPoint().getEndpoint()) {
				return constraint;
			}
		}
		return null;
	}

	
	public static List<PeriodicTemporalConstraint> getPeriodicConstraints(EPlanElement element, Timepoint timepoint, boolean mustExist) {
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, mustExist);
		if (constraintsMember == null) {
			return Collections.emptyList();
		}
		List<PeriodicTemporalConstraint> result = new ArrayList<PeriodicTemporalConstraint>();
		for (PeriodicTemporalConstraint constraint : constraintsMember.getPeriodicTemporalConstraints()) {
			ConstraintPoint point = constraint.getPoint();
			if ((point.getElement() == element) && (point.getEndpoint() == timepoint)) {
				result.add(constraint);
			}
		}
		return result;
	}
	
	public static List<BinaryTemporalConstraint> getBinaryConstraints(EPlanElement element, boolean mustExist) {
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, mustExist);
		if (constraintsMember == null) {
			return Collections.emptyList();
		}
		return constraintsMember.getBinaryTemporalConstraints();
	}
	
	public static List<BinaryTemporalConstraint> getBinaryConstraints(EPlanElement element, Timepoint timepoint, boolean mustExist) {
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, mustExist);
		if (constraintsMember == null) {
			return Collections.emptyList();
		}
		List<BinaryTemporalConstraint> result = new ArrayList<BinaryTemporalConstraint>();
		for (BinaryTemporalConstraint constraint : constraintsMember.getBinaryTemporalConstraints()) {
			ConstraintPoint pointA = constraint.getPointA();
			ConstraintPoint pointB = constraint.getPointB();
			if (((pointA.getElement() == element) && (pointA.getEndpoint() == timepoint))
				|| ((pointB.getElement() == element) && (pointB.getEndpoint() == timepoint))) {
				result.add(constraint);
			}
		}
		return result;
	}
	
	public static List<BinaryTemporalConstraint> getBinaryStartConstraints(EPlanElement element, boolean mustExist) {
		return getBinaryConstraints(element, Timepoint.START, mustExist);
	}
	
	public static List<BinaryTemporalConstraint> getBinaryEndConstraints(EPlanElement element, boolean mustExist) {
		return getBinaryConstraints(element, Timepoint.END, mustExist);
	}

	/**
	 * Given a particular constraint and EPlan (the context of the plan elements),
	 * this method will determine the elements that the constraint constrains.
	 * @param constraint
	 * @param ePlan
	 * @return
	 */
	public static LinkedHashSet<EPlanElement> getConstrainedEPlanElements(BinaryTemporalConstraint constraint) {
		LinkedHashSet<EPlanElement> elements = new LinkedHashSet<EPlanElement>();
		elements.add(constraint.getPointA().getElement());
		elements.add(constraint.getPointB().getElement());
		return elements;
	}
	
	/**
	 * Creates a new binary constraint which is the reverse but equivalent to the original constraint.
	 * For example, "A must end before B starts" can also be stated as "B must start after A ends".
	 * 
	 * @param originalConstraint
	 * @return
	 */
	public static BinaryTemporalConstraint reverseConstraint(BinaryTemporalConstraint originalConstraint) {
		if (originalConstraint==null) {
			// Might happen if an activity was removed or filtered out of an import.
			return null;
		}
		
		BinaryTemporalConstraint newConstraint = EMFUtils.copy(originalConstraint);
		
		newConstraint.getPointA().setElement(originalConstraint.getPointB().getElement());
		newConstraint.getPointA().setEndpoint(originalConstraint.getPointB().getEndpoint());
		newConstraint.getPointA().setAnchor(originalConstraint.getPointB().getAnchor());
		newConstraint.getPointB().setElement(originalConstraint.getPointA().getElement());
		newConstraint.getPointB().setEndpoint(originalConstraint.getPointA().getEndpoint());
		newConstraint.getPointB().setAnchor(originalConstraint.getPointA().getAnchor());
		if (originalConstraint.getMaximumBminusA() != null) {
			newConstraint.setMinimumBminusA(originalConstraint.getMaximumBminusA().opposite());
		} else {
			newConstraint.setMinimumBminusA(null);
		}
		if (originalConstraint.getMinimumBminusA() != null) {
			newConstraint.setMaximumBminusA(originalConstraint.getMinimumBminusA().opposite());
		} else {
			newConstraint.setMaximumBminusA(null);
		}
		
		return newConstraint;
	}
	
	/**
	 * Returns a short description of the type of constraint.
	 * @param constraint
	 * @param includeThat
	 * @return null if the constraint is invalid
	 */
	public static String getDescription(BinaryTemporalConstraint constraint, boolean includeThat) {
		Amount<Duration> minimum = constraint.getMinimumBminusA();
		Amount<Duration> maximum = constraint.getMaximumBminusA();
		// construct the string
		if (minimum == null && maximum == null) {
			return null;
		}
		
		String minimumStr = minimum == null ? null : EnsembleAmountFormat.INSTANCE.formatAmount(minimum.abs());
		String maximumStr = maximum == null ? null : EnsembleAmountFormat.INSTANCE.formatAmount(maximum.abs());
		String string = "";
		if (minimum == null && maximum != null) {
			// any time after
			if (maximum.approximates(ZERO)) {
				string += "anytime after";
			}
			// at least after
			else if (maximum.isLessThan(ZERO)) {
				string += "at least " + maximumStr + " after";
			}
			// at most before
			else { // max > 0
				string += "at most " + maximumStr + " before";
			}
		} else if (maximum == null && minimum != null) {
			// anytime before
			if (minimum.approximates(ZERO)) {
				string += "anytime before";
			}
			// at most after
			else if (minimum.isLessThan(ZERO)) {
				string += "at most " + minimumStr + " after";
			}
			// at least before
			else { // min > 0
				string += "at least " + minimumStr + " before";
			}
		}
		// at the same time
		else if (minimum != null && minimum.approximates(ZERO) && maximum != null && maximum.approximates(ZERO)) {
			string += "at the same time";
			if (includeThat) {
				if (constraint.getPointB().hasEndpoint()) {
					string += " that";
				} else {
					string += " as";
				}
			}
		}
		// exactly
		else if (minimum != null && minimum.approximates(maximum)) {
			string += "exactly ";
			string += minimumStr;
			if (minimum.isLessThan(ZERO)) {
				string += " after";
			} else { // min > 0
				string += " before";
			}
		}
		// between
		else {
			string += "between ";
			String minAndMaxStr;
			if (maximum != null && minimum != null && minimum.abs().isLessThan(maximum.abs())) {
				minAndMaxStr = minimumStr + " and " + maximumStr;
			} else {
				minAndMaxStr = maximumStr + " and " + minimumStr;
			}
			// after
			if (minimum != null && maximum != null && !minimum.isGreaterThan(ZERO) && !maximum.isGreaterThan(ZERO)) { // min <= 0 && max <= 0
				string += minAndMaxStr;
				string += " after";
			}
			// before
			else if (minimum != null && maximum != null && !minimum.isLessThan(ZERO) && !maximum.isLessThan(ZERO)) { // min >= 0 && max >= 0
				string += minAndMaxStr;
				string += " before";
			}
			// before and after
			else if (minimum != null && minimum.isLessThan(ZERO) && maximum != null && maximum.isGreaterThan(ZERO)) {
				string += maximumStr + " before and " + minimumStr + " after";
			} else {
				return null;
			}
		}
		return string;
	}
	
	public static List<BinaryTemporalConstraint> convertChainToBinaryTemporalConstraints(TemporalChain chain) {
		List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>();
		List<EPlanElement> chainedElements = chain.getElements();
		for (int i=1; i < chainedElements.size(); i++) {
			EPlanElement a = chainedElements.get(i-1);
			EPlanElement b = chainedElements.get(i);
			BinaryTemporalConstraint constraint = createConstraint(a, Timepoint.END, b, Timepoint.START, AmountUtils.exactZero(SI.SECOND), null, "");
			constraints.add(constraint);
		}
		return constraints;
	}

	public static boolean isAnchorable(EStructuralFeature feature) {
		String annotation = EMFUtils.getAnnotation(feature, "constraint", "anchor");
		return Boolean.parseBoolean(annotation);
	}
	
	/**
	 * Utility method that determines if the feature is relevant to the anchor
	 * 
	 * @param point
	 * @param planElement
	 * @param feature
	 * @return
	 */
	public static boolean isAnchorPointForElement(ConstraintPoint point, EPlanElement planElement, EStructuralFeature feature) {
		return !point.hasEndpoint() && point.getElement() == planElement && feature.getName().equals(point.getAnchor());
	}
	
	public static boolean areAnchorsAllowed() {
		return EnsembleProperties.getBooleanPropertyValue("constraint.anchor.allowed", false);
	}

	public static boolean isNested(List<EPlanElement> elements) {
		Set<String> hierarchy = new HashSet();
		for (EPlanElement element : elements) {
			while (element instanceof EPlanChild 
				&& (((EPlanChild)element).getParent() instanceof EPlanChild)) {
				EPlanElement parent = ((EPlanChild)element).getParent();
				hierarchy.add(parent.getPersistentID());
				element = parent;
			}
		}
		for (EPlanElement element : elements) {
			if (hierarchy.contains(element.getPersistentID())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if all elements have the same parent
	 * @param elements
	 * @return
	 */
	public static boolean sameParent(List<? extends EPlanElement> elements) {
		EObject firstParent = elements.get(0).eContainer();
		for (EPlanElement element : elements) {
			EObject parent = element.eContainer();
			if (firstParent != parent) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean sameType(List<EPlanElement> elements) {
		Class clazz = null;
		for (EPlanElement element : elements) {
			if (clazz == null) {
				clazz = element.getClass();
			} else if (element.getClass() != clazz) {
				return false;
			}
		}
		return true;
	}

}
