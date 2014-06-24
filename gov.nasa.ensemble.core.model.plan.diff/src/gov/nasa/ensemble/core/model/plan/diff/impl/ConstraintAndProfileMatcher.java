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
package gov.nasa.ensemble.core.model.plan.diff.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

/**
 * Matches constraints and also profiles references (constraints and effects).
 */
public class ConstraintAndProfileMatcher<T extends EObject> {

	private Set<T> additions;
	private Set<T> deletions;
	private Set<T> modifications;
	private PlanMatcher planMatcher;

	public ConstraintAndProfileMatcher(List<T> oldConstraints, List<T> newConstraints, PlanMatcher matcher) {
		additions = new HashSet(newConstraints);
		deletions = new HashSet(oldConstraints);
		modifications  = new HashSet();
		this.planMatcher = matcher;
		for (T constraint1 : newConstraints) {
			for (T constraint2 : oldConstraints) {
				if (constraintsSimilar(constraint1, constraint2)) {
					additions.remove(constraint1);
					deletions.remove(constraint2);
					if (!similarConstraintsEqual(constraint1, constraint2)) {
						modifications.add(constraint1); // TODO:  extend OldAndNewCopyOfSameThing?
					}
				}
			}
		}
	}
	
	private boolean constraintsSimilar(T constraint1, T constraint2) {
		if (constraint1==null || constraint2==null) return false;
		if (constraint1 instanceof PeriodicTemporalConstraint
		 && constraint2 instanceof PeriodicTemporalConstraint) {
			ConstraintPoint p1 = ((PeriodicTemporalConstraint)constraint1).getPoint();
			ConstraintPoint p2 = ((PeriodicTemporalConstraint)constraint2).getPoint();
			return equal(p1.getEndpoint(), p2.getEndpoint())
					&& planMatcher.equalIDs(p1.getElement(), p2.getElement());
		} else if (constraint1 instanceof BinaryTemporalConstraint
				&& constraint2 instanceof BinaryTemporalConstraint) {
			ConstraintPoint a1 = ((BinaryTemporalConstraint)constraint1).getPointA();
			ConstraintPoint a2 = ((BinaryTemporalConstraint)constraint2).getPointA();
			ConstraintPoint b1 = ((BinaryTemporalConstraint)constraint1).getPointB();
			ConstraintPoint b2 = ((BinaryTemporalConstraint)constraint2).getPointB();
			return equal(a1.getEndpoint(), a2.getEndpoint())
			&&    equal(b1.getEndpoint(), b2.getEndpoint())
			&&     planMatcher.equalIDs(a1.getElement(), a2.getElement())
			&&     planMatcher.equalIDs(b1.getElement(), b2.getElement());
		} else if (constraint1 instanceof TemporalChain
				&& constraint2 instanceof TemporalChain) {
			// Check for same list of it's in same order
			Iterator<EPlanElement> it1 = ((TemporalChain) constraint1).getElements().iterator();
			Iterator<EPlanElement> it2 = ((TemporalChain) constraint2).getElements().iterator();
			while (it1.hasNext() || it2.hasNext()) {
				if (it1.hasNext() != it2.hasNext()) return false; // different lengths
				if (!planMatcher.equalIDs(it1.next(), it2.next())) return false; // mismatch
			}
			return true; // reached end of both lists at same time without finding mismatch
		} else if (constraint1 instanceof ProfileReference
				&& constraint2 instanceof ProfileReference) {
			{
				// ProfileEffect, ProfileEqualityConstraint, ProfileEnvelopeConstraint
			ProfileReference profile1 = (ProfileReference) constraint1;
			ProfileReference profile2 = (ProfileReference) constraint2;
			return equal(
					profile1.getProfileKey(),
					profile2.getProfileKey())
					&& 
					profile1.getStartOffset().getTimepoint()
					== profile2.getStartOffset().getTimepoint()
					&&
					profile1.getEndOffset().getTimepoint()
					== profile2.getEndOffset().getTimepoint()
					&&
					// SPF-12140:  Since there can be two (or more) equality constraints on the same profile for different periods,
					// we need to be able to not confuse them with each other and report two modifications when nothing changes.
					// Since overlaps are presumably not allowed, start time should be enough for identification,
					// allowing end time to be modified.
					profile1.getStartOffset().getOffset()
					.equals(profile2.getStartOffset().getOffset());
			}
		} else {
			return false;
		}
	}
	
	private boolean similarConstraintsEqual(T constraint1, T constraint2) {
		if (constraint1 instanceof PeriodicTemporalConstraint
		 && constraint2 instanceof PeriodicTemporalConstraint) {
			return equal(((PeriodicTemporalConstraint)constraint1).getEarliest(),
					(((PeriodicTemporalConstraint)constraint2).getEarliest()))
			&&  equal(((PeriodicTemporalConstraint)constraint1).getLatest(),
					(((PeriodicTemporalConstraint)constraint2).getLatest()));
		} else if (constraint1 instanceof BinaryTemporalConstraint
				&& constraint2 instanceof BinaryTemporalConstraint) {
			return
			equal(((BinaryTemporalConstraint)constraint1).getMinimumBminusA(),
					(((BinaryTemporalConstraint)constraint2).getMinimumBminusA()))
			&&
			equal(((BinaryTemporalConstraint)constraint1).getMaximumBminusA(),
					(((BinaryTemporalConstraint)constraint2).getMaximumBminusA()));
		} else if (constraint1 instanceof TemporalChain
				&& constraint2 instanceof TemporalChain) {
			return true;
		} else if (constraint1 instanceof ProfileReference
				&& constraint2 instanceof ProfileReference) {
			if (constraint1 instanceof ProfileEqualityConstraint
			 && constraint2 instanceof ProfileEqualityConstraint
				&& (!equal(((ProfileEqualityConstraint) constraint1).getMaximumGap(),
						(((ProfileEqualityConstraint) constraint2).getMaximumGap()))
					|| !equal(((ProfileEqualityConstraint) constraint1).getValueLiteral(),
							(((ProfileEqualityConstraint) constraint2).getValueLiteral())))) {
				return false;
			}
			if (constraint1 instanceof ProfileEnvelopeConstraint
				&& constraint2 instanceof ProfileEnvelopeConstraint
				&& (!equal(((ProfileEnvelopeConstraint) constraint1).getMinLiteral(),
						(((ProfileEnvelopeConstraint) constraint2).getMinLiteral()))
					|| !equal(((ProfileEnvelopeConstraint) constraint1).getMinLiteral(),
							(((ProfileEnvelopeConstraint) constraint2).getMinLiteral())))) {
				return false;
			}
			if (constraint1 instanceof ProfileEffect
				&& constraint2 instanceof ProfileEffect
				&& (!equal(((ProfileEffect) constraint1).getEffectLiteral(Timepoint.START),
							(((ProfileEffect) constraint2).getEffectLiteral(Timepoint.START)))
					|| !equal(((ProfileEffect) constraint1).getEffectLiteral(Timepoint.END),
							(((ProfileEffect) constraint2).getEffectLiteral(Timepoint.END))))) {
				return false;
			}
			// The rest are shared by ProfileEffect, ProfileEqualityConstraint, and ProfileEnvelopeConstraint
			ProfileReference c1 = ((ProfileReference) constraint1);
			ProfileReference c2 = ((ProfileReference) constraint2);
			return
					equal(c1.getStartOffsetAmount(), c2.getStartOffsetAmount())
					&&
					equal(c1.getStartOffsetTimepoint(), c2.getStartOffsetTimepoint())
					&&
					equal(c1.getEndOffsetAmount(), c2.getEndOffsetAmount())
					&&
					equal(c1.getEndOffsetTimepoint(), (c2.getEndOffsetTimepoint()));			
		} else {
			return false;
		}
	}
	
	private boolean equal(Object a, Object b) {
		return CommonUtils.equals(a, b);
	}

	public Set<T> getAdditions() {
		return additions;
	}

	public Set<T> getDeletions() {
		return deletions;
	}

	public Set<T> getModifications() {
		return modifications;
	}



}
