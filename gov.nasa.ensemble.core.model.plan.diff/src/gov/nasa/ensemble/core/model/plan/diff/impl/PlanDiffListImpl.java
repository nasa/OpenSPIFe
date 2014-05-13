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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange.DiffType;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffFeatureFilter;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.dictionary.EReferenceParameter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

public class PlanDiffListImpl extends AbstractPlanDiffContainer implements PlanDiffList {
	
	public static boolean debugUnchanged = false;
	
	private PlanMatcher matcher;
	private PlanDiffFeatureFilter featureFilter;
	
	public PlanDiffListImpl() {
		commonElements = Collections.EMPTY_LIST;
	}
	
	public PlanDiffListImpl(EPlan oldPlan, EPlan newPlan, PlanMatcher matcher, PlanDiffFeatureFilter filter) {
		if (matcher == null) {
			matcher = new PlanMatcher(oldPlan, newPlan);
		}
		this.matcher = matcher;
		this.featureFilter = filter;
		matcher.match();
		commonElements = matcher.getCommonElements();
		initializeUnchanged(EActivityGroup.class, unchangedActivityGroups);
		initializeUnchanged(EActivity.class, unchangedActivities);
		for (EPlanChild element : matcher.getAddedElements()) {
			additions.add(new ChangedByAddingNewElementImpl(element, this));
			removeFromUnchanged(element, true);
		}
		for (EPlanChild element : matcher.getDeletedElements()) {
			ChangedByRemovingElementImpl deletion = new ChangedByRemovingElementImpl(element, this);
			deletions.add(deletion);
			removeFromUnchanged(deletion.getParent(), false);
		}
		for (OldAndNewCopyOfSameThing oldAndNew : commonElements) {
			compareOldAndNewElements(oldAndNew, true);
		}
	}
	
	public PlanDiffListImpl(OldAndNewCopyOfSameThing oldAndNew, PlanMatcher matcher, PlanDiffFeatureFilter filter) {
		if (matcher == null) {
			matcher = new PlanMatcher(null, null);
		}
		commonElements = Collections.EMPTY_LIST;
		this.matcher = matcher;
		this.featureFilter = filter;
		initializeUnchanged(EActivityGroup.class, unchangedActivityGroups);
		initializeUnchanged(EActivity.class, unchangedActivities);
		compareOldAndNewElements(oldAndNew, false);
	}

	private void compareOldAndNewElements(OldAndNewCopyOfSameThing oldAndNew, boolean lookForMoves) {
		// 1.  Moves
		if (lookForMoves) {
			ChangedByMovingChildImpl potentialMove = new ChangedByMovingChildImpl(oldAndNew, this);
			if (!matcher.equalIDs(potentialMove.getNewParent(), potentialMove.getOldParent())
					&& !(potentialMove.getOldParent() instanceof EPlan &&
							potentialMove.getNewParent() instanceof EPlan)) {
				moves.add(potentialMove);
				removeFromUnchanged(oldAndNew.getNewCopy(), true);
			}
		}
		// 2. Parameter modifications
		//   a. Common attributes:
		maybeAddModification(new ChangedByModifyingDirectAttribute(oldAndNew, PlanPackage.Literals.EPLAN_ELEMENT__NAME));
		for (EMember member : oldAndNew.getNewCopy().getMembers()) {
			for (EAttribute attribute : member.eClass().getEAllAttributes()) {
				maybeAddModification(new ChangedByModifyingMemberAttribute(oldAndNew, member.getClass(), attribute));
			}
		}
		//   b. Activity-type-specific arguments:
		EObject activityDef = oldAndNew.getNewCopy().getData();
		if (activityDef != null) { // null in some JUnit tests
			for (EStructuralFeature attribute : activityDef.eClass().getEAllStructuralFeatures()) {
				try {
					maybeAddModification(new ChangedByModifyingActivitySpecificArgument(oldAndNew, attribute));
				} catch (Exception e) {System.err.println(e);}
			}
		}
		// 3. Reference modifications
		if (oldAndNew.getNewCopy() instanceof EActivity) {
			addAnyReferenceChanges(oldAndNew);
		}
		
		// 4.  Constraints and Effects (SPF-6408)
		addAnyConstraintChanges(
				oldAndNew,
				oldAndNew.getOldCopy().getMember(ConstraintsMember.class),
				oldAndNew.getNewCopy().getMember(ConstraintsMember.class));
		addAnyProfileChanges(
				oldAndNew,
				oldAndNew.getOldCopy().getMember(ProfileMember.class),
				oldAndNew.getNewCopy().getMember(ProfileMember.class));
	}

	private void maybeAddModification(ChangedByModifyingParameter potentialModification) {
		EStructuralFeature parameter = potentialModification.getParameter();
		if (featureFilter != null) {
			if (featureFilter.isFeatureIgnored(potentialModification, parameter)) {
				return;
			}
		} else if (AttributePreferences.isAttributeIgnored(parameter)) {
			// SPF-4197 -- Ignore selected attributes.
			return;
		}
		// SPF-4052 -- filter out changes that are invisible when printed for user
		// e.g. 0 s vs. 0 ms if printed as duration; changes below printed precision,
		// and things left out such as error in Amount.
		Object oldValue = potentialModification.getOldValue();
		Object newValue = potentialModification.getNewValue();
		boolean unchanged = oldValue==newValue || PlanDiffUtils.deepEquals(oldValue, newValue);
		if (!unchanged && parameter instanceof EAttribute) {
			String oldValueAsDisplayed = PlanDiffUtils.getDisplayString((EAttribute) parameter, oldValue);
			String newValueAsDisplayed = PlanDiffUtils.getDisplayString((EAttribute) parameter, newValue);
			unchanged = oldValueAsDisplayed.equals(newValueAsDisplayed);
		}
		if (!unchanged) {
			parameterChanges.add(potentialModification);
			removeFromUnchanged(potentialModification.getNewCopyOfOwner(), false);
		}
	}
	
	
	private void addAnyReferenceChanges(OldAndNewCopyOfSameThing oldAndNew) {
		EActivity oldActivity = (EActivity) oldAndNew.getOldCopy();
		EActivity newActivity = (EActivity) oldAndNew.getNewCopy();
		Set<ReferenceAndEObject> newReferences = getReferences(newActivity);
		Set<ReferenceAndEObject> oldReferences = getReferences(oldActivity);
		
		for (ReferenceAndEObject potentialAddition : newReferences) {
			ReferenceAndEObject referenceInOldPlan = findCorrespondingReference(potentialAddition, oldReferences);
			if (referenceInOldPlan==null) {
				ChangedByAddingOrRemovingReference change 
				= new ChangedByAddingOrRemovingReferenceImpl(oldActivity, newActivity, null, potentialAddition, DiffType.ADD);
				referenceChanges.add(change);
				removeFromUnchanged(newActivity, false);
				} else {
					// found a reference whose name and type match
					oldReferences.remove(referenceInOldPlan); // accounted for and no longer a candidate for matching
					if (referenceHasBeenModified(potentialAddition, referenceInOldPlan)) {
						ChangedByAddingOrRemovingReference change 
						= new ChangedByAddingOrRemovingReferenceImpl(oldActivity, newActivity, referenceInOldPlan, potentialAddition, DiffType.MODIFY);
						referenceChanges.add(change);					
					}
				}	
		}		
		for (ReferenceAndEObject potentialDeletion : oldReferences) {
			ReferenceAndEObject referenceInNewPlan = findCorrespondingReference(potentialDeletion, newReferences);
			if (referenceInNewPlan==null) {
				ChangedByAddingOrRemovingReference change 
				= new ChangedByAddingOrRemovingReferenceImpl(oldActivity, newActivity, potentialDeletion, null, DiffType.REMOVE);
				referenceChanges.add(change);
				removeFromUnchanged(newActivity, false);
			}
		}
	}

	private boolean referenceHasBeenModified(
			ReferenceAndEObject reference1,
			ReferenceAndEObject reference2) {
		return !treesEqual(reference1.getObject(), reference2.getObject());
	}
	
	private boolean treesEqual(Object object1, Object object2) {
		if (object1==object2) {
			return true;
		}
		else if (object1 instanceof List && object2 instanceof List) {
			List list1 = (List) object1;
			List list2 = (List) object2;
			if (list1.size() != list2.size()) {
				return false;
			}
			for (int i=0; i < list1.size(); i++) {
				if (!treesEqual(list1.get(i), list2.get(i))) {
					return false;
				}
			}
			return true;
		}
		else if (object1==null || object2==null) {
			return false; // since they're not both null
		}
		else if (!(object1 instanceof EObject && object2 instanceof EObject)) {
			return object1.equals(object2);
		}
		EObject eObject1 = (EObject) object1;
		EObject eObject2 = (EObject) object2;
		if (!eObject1.eClass().equals(eObject2.eClass())) {
			return false;
		} else if (object1 instanceof EStringToStringMapEntryImpl && object2 instanceof EStringToStringMapEntryImpl) {
			EStringToStringMapEntryImpl map1 = (EStringToStringMapEntryImpl) object1;
			EStringToStringMapEntryImpl map2 = (EStringToStringMapEntryImpl) object2;
			return map1.getValue().equals(map2.getValue()) && map1.getKey().equals(map2.getKey());
		} else {
			return everythingExceptIdEqual(eObject1, eObject2);				
		}
	}

	private boolean everythingExceptIdEqual(EObject eObject1, EObject eObject2) {
		for (EAttribute att : eObject1.eClass().getEAllAttributes()) {
			if (!PlanDiffUtils.isIdFeature(att)) {
				if (!treesEqual(eObject1.eGet(att), eObject2.eGet(att))) {
					return false;
				}
			}
		}
		return true;
	}

	private ReferenceAndEObject findCorrespondingReference(
			ReferenceAndEObject thisReference,
			Set<ReferenceAndEObject> otherPlanRefs) {
		String name = PlanDiffUtils.getNameOfReference(thisReference);
		String type = thisReference.getObject().eClass().getName();
		for (ReferenceAndEObject otherReference : otherPlanRefs) {
			String otherName = PlanDiffUtils.getNameOfReference(otherReference);
			if (name != null && otherName != null
					&& type.equals(otherReference.getObject().eClass().getName())
					&& name.equals(otherName)) {
				return otherReference;
			}
		}
		return null;
	}

	private Set<ReferenceAndEObject> getReferences(EActivity activityInstance) {
		Set<ReferenceAndEObject> result = new HashSet<ReferenceAndEObject>();
		EObject activityDef = activityInstance.getData();
		if (activityDef == null) { // one case it's null is in some JUnit tests
			return result; // return empty set
		}
		EList<EReference> allReferences = activityDef.eClass().getEAllReferences();
		for(EReference reference : allReferences) {
			// Is this a dynamic resource?
			if (reference instanceof EReferenceParameter) {
				Object refList = activityDef.eGet(reference);
				if (refList instanceof Collection) // e.g. crew, filters, execution notes {
					for (DynamicEObjectImpl object : (Collection<? extends DynamicEObjectImpl>) refList) {
						result.add(new ReferenceAndEObject(reference, object));
					}
				else if (refList instanceof EObject) { // e.g. OpsNote
					result.add(new ReferenceAndEObject(reference, (EObject) refList));
				}
			}
        }
		return result;
	}
	
	private void addAnyConstraintChanges(OldAndNewCopyOfSameThing oldAndNew, ConstraintsMember oldOnes, ConstraintsMember newOnes) {
		ConstraintAndProfileMatcher<BinaryTemporalConstraint> binaryChanges = 
			new ConstraintAndProfileMatcher<BinaryTemporalConstraint>(
					oldOnes.getBinaryTemporalConstraints(),
					newOnes.getBinaryTemporalConstraints(),
					matcher);
		ConstraintAndProfileMatcher<PeriodicTemporalConstraint> periodicChanges = 
			new ConstraintAndProfileMatcher<PeriodicTemporalConstraint>(
					oldOnes.getPeriodicTemporalConstraints(),
					newOnes.getPeriodicTemporalConstraints(),
					matcher);
		List<TemporalChain> oldChains = oldOnes.getChain()==null? Collections.EMPTY_LIST : Collections.singletonList(oldOnes.getChain());
		List<TemporalChain> newChains = newOnes.getChain()==null? Collections.EMPTY_LIST : Collections.singletonList(newOnes.getChain());
		ConstraintAndProfileMatcher<TemporalChain> chainChanges = 
			new ConstraintAndProfileMatcher<TemporalChain>(oldChains, newChains, matcher);

		addTheseConstraintOrProfileChanges(oldAndNew, binaryChanges.getAdditions(), DiffType.ADD);
		addTheseConstraintOrProfileChanges(oldAndNew, binaryChanges.getDeletions(), DiffType.REMOVE);
		addTheseConstraintOrProfileChanges(oldAndNew, binaryChanges.getModifications(), DiffType.MODIFY);

		addTheseConstraintOrProfileChanges(oldAndNew, periodicChanges.getAdditions(), DiffType.ADD);
		addTheseConstraintOrProfileChanges(oldAndNew, periodicChanges.getDeletions(), DiffType.REMOVE);
		addTheseConstraintOrProfileChanges(oldAndNew, periodicChanges.getModifications(), DiffType.MODIFY);

		addTheseConstraintOrProfileChanges(oldAndNew, chainChanges.getAdditions(), DiffType.ADD);
		addTheseConstraintOrProfileChanges(oldAndNew, chainChanges.getDeletions(), DiffType.REMOVE);
		addTheseConstraintOrProfileChanges(oldAndNew, chainChanges.getModifications(), DiffType.MODIFY);
	}

	private void addAnyProfileChanges(OldAndNewCopyOfSameThing oldAndNew, ProfileMember oldMember, ProfileMember newMember) {
		ConstraintAndProfileMatcher<ProfileEffect> effects = 
			new ConstraintAndProfileMatcher<ProfileEffect>(
					oldMember.getEffects(),
					newMember.getEffects(),
					matcher);
		ConstraintAndProfileMatcher<ProfileConstraint> constraints = 
			new ConstraintAndProfileMatcher<ProfileConstraint>(
					oldMember.getConstraints(),
					newMember.getConstraints(),
					matcher);

		addTheseConstraintOrProfileChanges(oldAndNew, effects.getAdditions(), DiffType.ADD);
		addTheseConstraintOrProfileChanges(oldAndNew, effects.getDeletions(), DiffType.REMOVE);
		addTheseConstraintOrProfileChanges(oldAndNew, effects.getModifications(), DiffType.MODIFY);

		addTheseConstraintOrProfileChanges(oldAndNew, constraints.getAdditions(), DiffType.ADD);
		addTheseConstraintOrProfileChanges(oldAndNew, constraints.getDeletions(), DiffType.REMOVE);
		addTheseConstraintOrProfileChanges(oldAndNew, constraints.getModifications(), DiffType.MODIFY);

	}


	private void removeFromUnchanged(EPlanElement newCopy, boolean parentToo) {
		if (newCopy instanceof EActivityGroup) {
			unchangedActivityGroups.remove(newCopy);
		}
		if (newCopy instanceof EActivity) {
			unchangedActivities.remove(newCopy);
			if (parentToo) {
				unchangedActivityGroups.remove(((EActivity) newCopy).getParent());
			}
		}
	}

	private <T> void initializeUnchanged(Class desiredClass, Collection<T> collection) {
		for (OldAndNewCopyOfSameThing element : commonElements) {
			EPlanElement newCopy = element.getNewCopy();
			if (desiredClass.isInstance(newCopy)) {
				collection.add((T) newCopy);
			}
		}
	}

	@Override
	public EPlanElement findOldCopy(EPlanElement newCopy) {
		return matcher.findOldCopy(newCopy);
	}

	@Override
	public EPlanElement findNewCopy(EPlanElement oldCopy) {
		return matcher.findNewCopy(oldCopy);
	}

	@Override
	public EPlanElement findOtherCopy(EPlanElement eitherCopy) {
		EPlanElement try1 = matcher.findNewCopy(eitherCopy);
		if (try1 != null && try1 != eitherCopy) return try1;
		EPlanElement try2 = matcher.findOldCopy(eitherCopy);
		if (try2 != null && try2 != eitherCopy) return try2;
		return null;
	}

	private void addTheseConstraintOrProfileChanges (OldAndNewCopyOfSameThing oldAndNew, Collection<? extends EObject> objects, DiffType diffType) {
		for (EObject object : objects) {
			constraintAndProfileChanges.add(new ChangedConstraintOrProfileImpl(oldAndNew, diffType, object));
		}
	}

}
