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
package gov.nasa.arc.spife.rcp;

import gov.nasa.arc.spife.rcp.events.EventTimeUpdateOperation;
import gov.nasa.arc.spife.rcp.events.OrbitEventUtil;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.ChangeFactory;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ListChange;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.jscience.physics.amount.Amount;

public class SPIFePlanIntegrationOperation extends AbstractTransactionUndoableOperation {
	
	protected static String specialActivityParameter = EnsembleProperties.getProperty("lass.integrate.ad.special.field.name", "Terminator");
	protected Map<String, EPlanChild> specialActivitiesInExistingPlan = new HashMap<String, EPlanChild>();
	protected Map<String, EPlanChild> specialActivitiesInPlanToBeIntegrated = new HashMap<String, EPlanChild>();
	protected EPlan existingPlan;
	protected EPlan planToBeIntegrated;
	protected Set<String> diffIDsToDelete = null;
	protected Map<String, EPlanChild> idsInExistingPlan = new HashMap<String, EPlanChild>();
	protected Map<String, EPlanChild> idsInPlanToBeIntegrated = new HashMap<String, EPlanChild>();
	protected Date startTime;
	protected Date endTime;
	public boolean ignoreDeletionsOfActivitiesInNewPlan = true;
	private Collection<BinaryTemporalConstraint> constraintsBeingAdded = new HashSet<BinaryTemporalConstraint>();	

	//differences
	public SPIFePlanIntegrationOperation(EPlan existingPlan, EPlan planToBeIntegrated) {
		super("Plan Integration");
		setOrbitEventDiffIds(existingPlan); // see SPF-8089
		setOrbitEventDiffIds(planToBeIntegrated); // see SPF-8089
		this.diffIDsToDelete = null;
		this.existingPlan = existingPlan;
		this.planToBeIntegrated = planToBeIntegrated;
		setDiffIDsToDelete(getDiffIDsToDelete(existingPlan, planToBeIntegrated));
		cacheIds(existingPlan, idsInExistingPlan);
		cacheIds(planToBeIntegrated, idsInPlanToBeIntegrated);
	}
	
	protected Set<String> getDiffIDsToDelete(EPlan existingPlan, EPlan planToBeIntegrated){
		Set<String> diffIDsToDelete = new HashSet();
		cacheSpecialActivities(existingPlan, specialActivitiesInExistingPlan);
		cacheSpecialActivities(planToBeIntegrated, specialActivitiesInPlanToBeIntegrated);
		
		for (String idExisting : specialActivitiesInPlanToBeIntegrated.keySet()){
			EPlanChild specialActivityTobeIntegrated = specialActivitiesInPlanToBeIntegrated.get(idExisting);
			
			if (isTheActivityInPlanMap(specialActivitiesInExistingPlan, specialActivityTobeIntegrated)){
				diffIDsToDelete.add(getDiffId(specialActivityTobeIntegrated));
			}
		}
		
		if (diffIDsToDelete.size() > 0)
			return diffIDsToDelete;
		else
			return null;
	}
	
	protected void cacheSpecialActivities(EPlanParent parent, Map<String, EPlanChild> cache) {
		for (EPlanChild child : parent.getChildren()) {
			if (child instanceof EActivity) {
				if (child != null){
					String specialActivityParameterValue = ADParameterUtils.getParameterString(child, specialActivityParameter);
					if (specialActivityParameterValue != null && specialActivityParameterValue.equalsIgnoreCase("true")){
						String id = getDiffId(child);
						if (id != null) cache.put(id, child);
					}
				}
			}
			//Recursively look in activity groups
			if (child instanceof EPlanParent) 
				cacheSpecialActivities((EPlanParent) child, cache);
		}
	}

	protected void mergePlansStep1() {
		mergePlansStep2(planToBeIntegrated);		
		if (diffIDsToDelete!=null) {
			deleteFromPlan(existingPlan, diffIDsToDelete);
		}
		//copy constraints from planToBeIntegrated to existingPlan
		selectivelyCopyConstraints(planToBeIntegrated, existingPlan, whenToAddConstraints());
		expandPlanBoundary();
	}	

	private void expandPlanBoundary() {
		Class<PlanTemporalMember> boundaryClass = PlanTemporalMember.class;
		Date start1 = existingPlan.getMember(boundaryClass).getStartBoundary();
		Date start2 = planToBeIntegrated.getMember(boundaryClass).getStartBoundary();
		Date end1 = existingPlan.getMember(boundaryClass).getEndBoundary();
		Date end2 = planToBeIntegrated.getMember(boundaryClass).getEndBoundary();
		if (start2==null) start2=start1;
		if (start1==null) start1=start2;
		if (end2==null) end2=end1;
		if (end1==null) end1=end2;
		if (start1 != null) existingPlan.getMember(boundaryClass).setStartBoundary(DateUtils.earliest(start1, start2));
		if (end1 != null) existingPlan.getMember(boundaryClass).setEndBoundary(DateUtils.latest(end1, end2));
	}
	
	protected boolean existsInDestinationPlan(EPlanElement element) {
		 if (idsInExistingPlan.containsKey(getDiffId(element)))
			return true;
		 else if (element instanceof EActivity) {
			String specialActivityParameterValue = "";
			if (element != null)
				 specialActivityParameterValue = ADParameterUtils.getParameterString(element, specialActivityParameter);
			if (specialActivityParameterValue != null && specialActivityParameterValue.equalsIgnoreCase("true"))
				return isTheActivityInPlanMap(specialActivitiesInExistingPlan, element);
			else
				return false;
		 }
		 else
			 return false;
	}
	
	protected static boolean isTheActivityInPlanMap(Map<String, EPlanChild> planMap, EPlanElement element){
		if (!(element instanceof EActivity)) 
			return false;
		
		String theActivityName = element.getName();
		Date theActivityStartTime = element.getMember(TemporalMember.class).getStartTime();
		Date theActivityEndTime = element.getMember(TemporalMember.class).getEndTime();
		Boolean isTheActivityScheduled = element.getMember(TemporalMember.class).getScheduled();
				
		for (String id : planMap.keySet()){
			EPlanChild activity = planMap.get(id);
			if (activity.getName().equalsIgnoreCase(theActivityName) 
					&& activity.getMember(TemporalMember.class).getStartTime().equals(theActivityStartTime)
					&& activity.getMember(TemporalMember.class).getEndTime().equals(theActivityEndTime)
					&& activity.getMember(TemporalMember.class).getScheduled().equals(isTheActivityScheduled)){
				return true;
			}
		}
		
		return false;	
	}
	//differences end
	
	protected void setDiffIDsToDelete(Set<String> diffIDsToDelete){
		this.diffIDsToDelete = diffIDsToDelete;
	}
	
	private static void cacheIds(EPlanParent parent, Map<String, EPlanChild> cache) {
		for (EPlanChild child : parent.getChildren()) {
			String id = getDiffId(child);
			if (id != null) cache.put(id, child);
			//Recursively look in activity groups
			if (child instanceof EPlanParent) cacheIds((EPlanParent) child, cache);
		}
	}

	public static String getDiffId(EPlanElement element) {
		if (element == null)
			return null;
		
		CommonMember member = element.getMember(CommonMember.class);
		if (member==null) return null;
		return member.getDiffID();
	}

	public void mergePlansWithoutConstraintMerging() {
		if(Platform.isRunning()) {
			TransactionUtils.writing(existingPlan, new Runnable() {
				@Override
				public void run() {
					mergePlansStep1();
				}
			});
		} else {
			// Allow running without GUI, e.g. inside JUnit test.
			mergePlansStep1();
		}
	}
	
	/** see SPF-8089 */
	public void setOrbitEventDiffIds(final EPlan plan) {
		if(Platform.isRunning()) {
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					OrbitEventUtil.setSpecialDiffIds(plan);
				}
			});
		} else {
			// Allow running without GUI, e.g. inside JUnit test.
			OrbitEventUtil.setSpecialDiffIds(plan);
		}
	}
		

	
	protected void selectivelyMergeConstraints(){
		selectivelyDeleteConstraints(existingPlan, whenToDeleteConstraints());
		selectivelyCopyConstraints(planToBeIntegrated, existingPlan, whenToAddConstraints());
	}
	
	protected void mergePlansStep2(EPlanParent parentToIntegrate) {
		EPlanParent adoptiveParent = (EPlanParent) findOriginalCopy(parentToIntegrate);
		if (adoptiveParent==null) adoptiveParent = existingPlan; // default to adding at top level
		int indexCounter = 0;
		for (EPlanChild child : parentToIntegrate.getChildren()) {
			if (isInTimeRange(child) && matchesCriteria(child))
			{
				// Add: Any activity or sequence (activity group) that exists in the new plan in the specified time frame & matches the specified criteria, which does not exist in the existing plan should be added. Activities should be identified by their DiffID.
				if (!existsInDestinationPlan(child)) {
					adoptChild(adoptiveParent, child, indexCounter);
					indexCounter += 1;
				}
				// Modify: Any activity that exists in the new plan in the specified time frame & matches the specified criteria, which also exists in the existing plan should be updated. 
				else {
					if (!replaceUnder(adoptiveParent, child)) // first look in obvious place:  twin's family
						replaceUnder(existingPlan, child); // look everywhere; it may have started elsewhere
				}
			}

			//Recursively look in activity groups.
			// At first Mel thought this was a bug (SPF-5985), but once she realize what was happening,
			// she decided it was a legitimately gray area and that we should leave it as is
			// pending JSC feedback (email query sent September 24, 2010 4:41:21 PM PDT).
			if (child instanceof EPlanParent) 
				mergePlansStep2((EPlanParent) child);
		}
	}

	
	private void adoptChild(EPlanParent adoptiveParent, EPlanChild child, int childIndex) {
		EPlanChild copy = copyWithReferences(child);
		new Addition<EPlanChild>(adoptiveParent, copy, childIndex,
				PlanPackage.Literals.EPLAN_PARENT__CHILDREN).execute();

		idsInExistingPlan.put(getDiffId(child), copy);
	}

	private EPlanChild copyWithReferences(EPlanChild child) {
//		if (child instanceof EPlanParent)
//			return EPlanUtils.copy(Collections.singletonList(child), true).get(0);
//		else
//			return EPlanUtils.copy(Collections.singletonList(child), false).get(0);
		return EPlanUtils.copy(Collections.singletonList(child), false).get(0);
	}

	private void replaceChild(EPlanParent adoptiveParent, EPlanChild originalChild, EPlanChild childToCopy) {
		int index = adoptiveParent.getChildren().indexOf(originalChild);
		EPlanChild changelingCopy = copyWithReferences(childToCopy);
		new Addition<EPlanChild>(adoptiveParent, changelingCopy, index, 
				PlanPackage.Literals.EPLAN_PARENT__CHILDREN).execute();
		new Removal<EPlanChild>(adoptiveParent, adoptiveParent.getChildren(), originalChild, 
				PlanPackage.Literals.EPLAN_PARENT__CHILDREN).execute();
		adoptiveParent.eClass().getEAllStructuralFeatures();
		idsInExistingPlan.put(getDiffId(originalChild), changelingCopy);
	}

	private boolean replaceUnder(EPlanParent adoptiveParent, EPlanChild changelingChild) {
		EPlanChild originalChild = (EPlanChild) findOriginalCopy(changelingChild);
		if (adoptiveParent.getChildren().contains(originalChild)) {
			replaceChild(adoptiveParent, originalChild, changelingChild);
			return true;
		}
		else {
			for (EPlanChild child : adoptiveParent.getChildren()) {
				if (child instanceof EPlanParent
						&& replaceUnder((EPlanParent) child, changelingChild)) {
					return true;
				}
			}
		}
		return false;
	}

	protected void deleteFromPlan(EPlanParent parent, Set<String> diffIDs) {
		// Deletes (with list):  If an ID exists in this list that corresponds to an activity in the existing plan which is scheduled within the specified time frame & matches the specified criteria, it should be deleted.
		List<EPlanChild> toRemove = new ArrayList<EPlanChild>();
		for (EPlanChild child : parent.getChildren()) {
			if (isInTimeRange(child)
					&& diffIDs.contains(getDiffId(child))
					&& matchesCriteria(child)) {
				toRemove.add(child);
			}
			if (child instanceof EPlanParent) {
				deleteFromPlan((EPlanParent) child, diffIDs);
			}
		}
		new MultipleRemoval<EPlanChild>(parent, parent.getChildren(), toRemove, 
				PlanPackage.Literals.EPLAN_PARENT__CHILDREN).execute();
	}
	
	protected void deleteActivitiesNotInNewPlan(EPlanParent parent) {
		// Deletes (without list): If no delete list is specified (in CPS integration), the following heuristic should be used:
		// * If an activity exists in the existing plan in the specified time frame that
		//   matches the specified criteria, which do not exist in the specified time frame in the new plan.
		//   [then it should be deleted].
		List<EPlanChild> toRemove = new ArrayList<EPlanChild>();
		for (EPlanChild childToConsiderDeleting : parent.getChildren()) {
			if (isInTimeRange(childToConsiderDeleting)
					&& matchesCriteria(childToConsiderDeleting)) {
				EPlanChild twin = idsInPlanToBeIntegrated.get(getDiffId(childToConsiderDeleting));
				if (twin==null || !isInTimeRange(twin)) {
					toRemove.add(childToConsiderDeleting);
				}
			}
			if (childToConsiderDeleting instanceof EPlanParent) {
				deleteActivitiesNotInNewPlan((EPlanParent) childToConsiderDeleting);
			}
		}
		new MultipleRemoval<EPlanChild>(parent, parent.getChildren(), toRemove, 
				PlanPackage.Literals.EPLAN_PARENT__CHILDREN).execute();
	}
	
	private EPlanElement findOriginalCopy(EPlanElement element) {
		return idsInExistingPlan.get(getDiffId(element));
	}
	
	
	/**
	 * The ePlan integration should accept a time frame which specifies what activities should be imported from the new plan. This time frame should be inclusive (if an activity overlaps with the start or end of the time frame, it should be imported).
	 */
	
	protected boolean isInTimeRange (EPlanElement element) {
		if (startTime==null && endTime==null) return true;
		TemporalMember time = element.getMember(TemporalMember.class);
		if (time.getStartTime()==null) return false;
		if (time.getEndTime()==null) return false;
		
//		if (startTime != null && time.getEndTime().before(startTime)) return false;
//		if (endTime != null && time.getStartTime().after(endTime)) return false;
//		return true;
		
		// Rev 93945 seems to change the criterion to "lies entirely within timeframe",
		// while JUnit tests interpret "overlap" to mean "does not lie entirely outside".
		// The interpretation below breaks the tests written July-September 2010.
		
		if (startTime == null) {
			return (time.getStartTime().before(endTime));
		}
		if (endTime == null) {
			return (time.getEndTime().after(startTime));
		}
		// (activity.start >= bound.start AND activity.start < bound.end) OR (activity.end > bound.start AND activity.end <= bound.end) OR (activity.start <= bound.start AND activity.end >= bound.end)
		if (!time.getStartTime().before(startTime) && time.getStartTime().before(endTime)) return true;
		if (time.getEndTime().after(startTime) && !time.getEndTime().after(endTime)) return true;
		if (!time.getStartTime().after(startTime) && !time.getEndTime().before(endTime)) return true;
		return false;
	}

	/**
	 * Sets the time range to be merged.
	 * This time frame should be inclusive (if an activity overlaps with the start or end of the time frame, it should be imported).
	 * @param startTime (null for unbounded):  exclude activities ending before this start time
	 * @param endTime (null for unbounded):  exclude activities starting after this end time
	 */
	public void setTimeRange(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * SPF-8646 change: Plan Orbital Events should never be copied into the Driver Plan.
	 * Everything else should.
	 * @param element -- possible plan orbit event
	 * @return
	 */
	protected boolean matchesCriteria (EPlanElement element) {
		return !(OrbitEventUtil.eventsAreDefined() &&
				OrbitEventUtil.isOrbitEvent(element) && !OrbitEventUtil.isGeneric(element));
	}


	protected ConstraintCriteria whenToAddConstraints() {
		return new ConstraintCriteria () {

			@Override
			public boolean include(BinaryTemporalConstraint constraint) {
				EPlanElement aInIntegratedPlan = constraint.getPointA().getElement();
				EPlanElement bInIntegratedPlan = constraint.getPointB().getElement();
				// Do not add/update if either A or B does not exist in the plan post-integration
				if (aInIntegratedPlan==null) return false;
				if (bInIntegratedPlan==null) return false;
				EPlanElement aInExistingPlan = idsInExistingPlan.get(getDiffId(aInIntegratedPlan));
				EPlanElement bInExistingPlan = idsInExistingPlan.get(getDiffId(bInIntegratedPlan));
				EPlanElement aInNewPlan = idsInPlanToBeIntegrated.get(getDiffId(aInIntegratedPlan));
				EPlanElement bInNewPlan = idsInPlanToBeIntegrated.get(getDiffId(bInIntegratedPlan));
				
				// Simplify the logic by not having to check for null; check redundantly instead.
				if (aInExistingPlan==null) aInExistingPlan = aInNewPlan;
				if (aInNewPlan==null) aInNewPlan = aInExistingPlan;
				if (bInExistingPlan==null) bInExistingPlan = bInNewPlan;
				if (bInNewPlan==null) bInNewPlan = bInExistingPlan;
				
				// Do not add/update if neither A nor B matches the criteria in either the new plan or the existing plan
				// Do not add/update if neither A nor B is in the time range both the new plan and the existing plan
				return (matchesCriteria(aInExistingPlan)
						|| matchesCriteria(bInExistingPlan)
						|| matchesCriteria(aInNewPlan)
						|| matchesCriteria(bInNewPlan))
						&&
						(isInTimeRange(aInExistingPlan)
						 || isInTimeRange(aInNewPlan)
						 || isInTimeRange(bInExistingPlan)
						 || isInTimeRange(bInNewPlan));
			}
		};
	}

	protected ConstraintCriteria whenToDeleteConstraints() {
		return ConstraintCriteria.ALWAYS;
	}

	public void selectivelyCopyConstraints(EPlan source,
			EPlan destination, ConstraintCriteria criteria) {
		Map<String, EPlanChild> idsInDestination = new HashMap<String, EPlanChild>();
		cacheIds(destination, idsInDestination);
		selectivelyCopyConstraintsRecursively(source, idsInDestination, criteria);
	}

	
	private void selectivelyCopyConstraintsRecursively(EPlanParent source,
			Map<String, EPlanChild> idsInDestination,
			ConstraintCriteria criteria) {
		for (EPlanChild child : source.getChildren()) {
			EPlanChild twin = idsInDestination.get(getDiffId(child));
			if (twin != null) {
				ConstraintsMember member = twin.getMember(ConstraintsMember.class);
				for (BinaryTemporalConstraint binaryTemporalConstraint : child.getMember(ConstraintsMember.class).getBinaryTemporalConstraints()) {
					if (criteria.include(binaryTemporalConstraint)) {
						BinaryTemporalConstraint copy = makeCopyOfBinaryConstraintForNewPlan(binaryTemporalConstraint, idsInDestination);
						if (copy != null && !constraintAlreadyExists(copy) && !constraintAlreadyExists(copy, constraintsBeingAdded)) {
							constraintsBeingAdded.add(copy);
							
							new Addition<BinaryTemporalConstraint>
							(member, copy, member.getBinaryTemporalConstraints().size(), 
							ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS).execute();
							
							//add constraint reference to destination point
							EPlanElement otherEndElement = null;
					
							if (!getDiffId(copy.getPointA().getElement()).equals(getDiffId(twin)))
								otherEndElement = copy.getPointA().getElement();
							else
								otherEndElement = copy.getPointB().getElement();
							
							ConstraintsMember othersMember = otherEndElement.getMember(ConstraintsMember.class);
							new Addition<BinaryTemporalConstraint>
							(othersMember,
							copy, othersMember.getBinaryTemporalConstraints().size(),
							ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS).execute();
						}
					}
				}
				
				TemporalChain sourceChainConstraint = child.getMember(ConstraintsMember.class).getChain();	
				if (sourceChainConstraint != null){
					TemporalChain destinationChainConstraint = getTemporalChainConstraintForNewPlan(sourceChainConstraint, idsInDestination);
					if (destinationChainConstraint != null) 
						member.setChain(destinationChainConstraint);
				}
			}

			if (child instanceof EPlanParent)
				selectivelyCopyConstraintsRecursively((EPlanParent) child,
						idsInDestination, criteria);
		}
	}


	private boolean constraintAlreadyExists(BinaryTemporalConstraint constraintInNewPlan) {
		return constraintAlreadyExists(constraintInNewPlan, constraintInNewPlan.getPointA().getElement())
		&&
		constraintAlreadyExists(constraintInNewPlan, constraintInNewPlan.getPointB().getElement());
	}
	
	private boolean constraintAlreadyExists(BinaryTemporalConstraint constraint, EPlanElement in) {
		ConstraintsMember member = in.getMember(ConstraintsMember.class);
		if (member==null) return false;
		List<BinaryTemporalConstraint> list = member.getBinaryTemporalConstraints();
		if (list==null) return false;
		return constraintAlreadyExists(constraint, list);
	}
	
	private boolean constraintAlreadyExists(BinaryTemporalConstraint constraint,
			Collection<BinaryTemporalConstraint> list) {
		for (BinaryTemporalConstraint existingConstraint : list) {
			if (equivalent(existingConstraint, constraint)) {
				return true;
			}
		}
		return false;
	}

	private boolean equivalent (BinaryTemporalConstraint constraint1, BinaryTemporalConstraint constraint2) {		
		return equivalent(constraint1.getPointA(), constraint2.getPointA())
		&& equivalent(constraint1.getPointB(), constraint2.getPointB())
		&& durationsEqual(constraint1.getMinimumBminusA(), constraint2.getMinimumBminusA())
		&& durationsEqual(constraint1.getMaximumBminusA(), constraint2.getMaximumBminusA());
	}

	private boolean durationsEqual(Amount<Duration> a, Amount<Duration> b) {
		if (a==null && b==null) return true;
		if (a==null || b==null) return false;
		return a.equals(b);
	}

	private boolean equivalent(ConstraintPoint point1, ConstraintPoint point2) {
		return point1.getEndpoint().equals(point2.getEndpoint())
		&& SPIFePlanIntegrationOperation.getDiffId(point1.getElement())
		.equals(SPIFePlanIntegrationOperation.getDiffId(point2.getElement()));
	}

	
	
	private List<TemporalChain> chainsInDestination = new ArrayList<TemporalChain>();
	
	private TemporalChain getTemporalChainConstraintForNewPlan(TemporalChain sourceChain, Map<String, EPlanChild> idsInDestination) {
		TemporalChain result = null;
		List<String> sourceIds = getElementIdsOfChainConstraint(sourceChain);
		
		for(TemporalChain destinationChain : chainsInDestination){
			List<String> destinationIds = getElementIdsOfChainConstraint(destinationChain);
			if (destinationIds.size() == sourceIds.size() && destinationIds.containsAll(sourceIds))
				return destinationChain;
		}
		
		if (result == null){
			result = makeCopyOfChainConstraintForNewPlan(sourceChain, idsInDestination);
			chainsInDestination.add(result);
		}
		return result;
	}
	
	private static List<String> getElementIdsOfChainConstraint(TemporalChain chain){
		List<String> retIds = new ArrayList<String>();
		List<EPlanElement> elements = chain.getElements();
		for (EPlanElement element : elements)
			retIds.add(getDiffId(element));

		return retIds;
	}
	
	private static BinaryTemporalConstraint makeCopyOfBinaryConstraintForNewPlan(
			BinaryTemporalConstraint constraint,
			Map<String, EPlanChild> idsInDestination) {
		BinaryTemporalConstraint result = (BinaryTemporalConstraint) new Copier().copy(constraint);
		EPlanChild newA = idsInDestination.get(getDiffId(constraint.getPointA().getElement()));
		if (newA == null)
			newA = getSameActivityFromPlanMap(idsInDestination, constraint.getPointA().getElement());
		
		EPlanChild newB = idsInDestination.get(getDiffId(constraint.getPointB().getElement()));
		if (newB == null)
			newB = getSameActivityFromPlanMap(idsInDestination, constraint.getPointB().getElement());

		if (newA == null || newB == null) return null;
		result.getPointA().setElement(newA);
		result.getPointB().setElement(newB);
		return result;
	}

	protected static EPlanChild getSameActivityFromPlanMap(Map<String, EPlanChild> planMap, EPlanElement element){
		if (!(element instanceof EActivity)) 
			return null;
		
		String theActivityName = element.getName();
		Date theActivityStartTime = element.getMember(TemporalMember.class).getStartTime();
		Date theActivityEndTime = element.getMember(TemporalMember.class).getEndTime();
		Boolean isTheActivityScheduled = element.getMember(TemporalMember.class).getScheduled();
				
		for (String id : planMap.keySet()){
			EPlanChild sameActivity = planMap.get(id);
			if (sameActivity.getName().equalsIgnoreCase(theActivityName) 
					&& sameActivity.getMember(TemporalMember.class).getStartTime().equals(theActivityStartTime)
					&& sameActivity.getMember(TemporalMember.class).getEndTime().equals(theActivityEndTime)
					&& sameActivity.getMember(TemporalMember.class).getScheduled().equals(isTheActivityScheduled)){
				return sameActivity;
			}
		}
		
		return null;	
	}
	
	private static TemporalChain makeCopyOfChainConstraintForNewPlan(TemporalChain constraint, Map<String, EPlanChild> idsInDestination) {
		if (constraint == null) return null;
		TemporalChain result = (TemporalChain) new Copier().copy(constraint);
		List<EPlanElement> elements = constraint.getElements();
		List<EPlanElement> newElements = new ArrayList<EPlanElement>();
		
		for (EPlanElement element : elements){
			EPlanChild newElement = idsInDestination.get(getDiffId(element));
			newElements.add(newElement);
		}
		
		EStructuralFeature eStructuralFeature = ConstraintsPackage.Literals.TEMPORAL_CHAIN__ELEMENTS;
		result.eSet(eStructuralFeature, newElements);
		return result;
	}
	
	public void selectivelyDeleteConstraints(EPlanParent root,
			ConstraintCriteria criteria) {
		for (EPlanChild child : root.getChildren()) {
			ConstraintsMember member = child.getMember(ConstraintsMember.class);
			List<BinaryTemporalConstraint> constraintsToRemove = new ArrayList<BinaryTemporalConstraint>();			
			for (BinaryTemporalConstraint constraint : member.getBinaryTemporalConstraints()) {
				if (criteria.include(constraint)) {
					constraintsToRemove.add(constraint);
					}
			}
			new MultipleRemoval<BinaryTemporalConstraint>(child, member.getBinaryTemporalConstraints(),
					constraintsToRemove, child.eClass().getEStructuralFeature("members")).execute();
			if (child instanceof EPlanParent) {
				selectivelyDeleteConstraints((EPlanParent) child, criteria);
			}
		}
	}
	
	protected ChangeDescription changeDescription1 = ChangeFactory.eINSTANCE.createChangeDescription();
	protected ChangeDescription changeDescription2 = ChangeFactory.eINSTANCE.createChangeDescription();
	protected EList<FeatureChange> featureChanges = new BasicEList();
	protected EList<ListChange> listChanges = new BasicEList();
	
	/** Performs the actual merging of the plans specified in the constructor
	 * according to the rules supplied in the other methods.
	 */
	public void mergePlans0() {
		makeModifiableCopyOfPlanToBeIntegrated();
		reverseMergeOrbitalTimesAndSnapToOrbit();
		mergePlansWithoutConstraintMerging();
		applyAndReverseChangeDescription(changeDescription1);
		selectivelyMergeConstraints();
		applyAndReverseChangeDescription(changeDescription2);
	}
	
	/**
	 * planToBeIntegrated is an in-memory copy that will never be written back out to
	 * the file system
	 * @see makeModifiableCopyOfPlanToBeIntegrated().
	 * @since SPF-8623
	 */
	private void reverseMergeOrbitalTimesAndSnapToOrbit() {
		if(Platform.isRunning()) {
			TransactionUtils.writing(planToBeIntegrated, new Runnable() {
				@Override
				public void run() {
					_reverseMergeOrbitalTimesAndSnapToOrbit();
				}
			});
		} else {
			// Allow running without GUI, e.g. inside JUnit test.
			_reverseMergeOrbitalTimesAndSnapToOrbit();
		}
	}
	
	/**
	 * Without this, although an unopened plan would not be modified in the file system,
	 * the plan would be modified in the editor if it happens to be open.
	 * @since SPF-8623
	 */
	private void makeModifiableCopyOfPlanToBeIntegrated() {
		planToBeIntegrated = PlanUtils.duplicatePlan(planToBeIntegrated, EMFUtils.createResourceSet());
	}

	private void _reverseMergeOrbitalTimesAndSnapToOrbit() {
		Set<EActivity> plannedOrbitalEvents = OrbitEventUtil.getPlannedOrbitalEvents(planToBeIntegrated);
		for (EActivity outdatedEvent : plannedOrbitalEvents) {
			EPlanElement updatedEvent = findOriginalCopy(outdatedEvent);
			if (updatedEvent != null) {
				outdatedEvent.getMember(TemporalMember.class).setStartTime(
						updatedEvent.getMember(TemporalMember.class).getStartTime());
			}
		}
		try {
			new EventTimeUpdateOperation(planToBeIntegrated, plannedOrbitalEvents).execute();
		} catch (Throwable e) {
			LogUtil.error("Could not update event times", e);
		}
	}

	public void mergePlans() {
		if(Platform.isRunning()) {
			TransactionUtils.writing(existingPlan, new Runnable() {
				@Override
				public void run() {
					mergePlans0();
				}
			});
		} else {
			// Allow running without GUI, e.g. inside JUnit test.
			mergePlans0();
		}
	}
	
	/////// Undo support:
	@Override
	protected void dispose(UndoableState state) {
		// No cleanup needed?
	}

	@Override
	public String toString() {
		try {
			StringBuilder s = new StringBuilder();
			s.append("Integrate Plan '");
		s.append(planToBeIntegrated.getName());
		s.append("' into Plan '");
		s.append(existingPlan.getName());
		s.append("'");
		return s.toString();
		}
		catch (Exception e) { return "Integrate plans"; }
	}

	@Override
	public void execute() throws Throwable {
		mergePlans();
	}
	
	protected EPlanChild findActivity(EPlan plan, String activityName) {
		for (EPlanChild activity : plan.getChildren()) {
			if (activity.getName().equalsIgnoreCase(activityName)) {
				return activity;
			}
		}
	
		return null;
	}
	
	public EPlan getExistingPlan() {
		return existingPlan;
	}

	public EPlan getPlanToBeIntegrated() {
		return planToBeIntegrated;
	}
	
	protected ChangeDescription createChangeDescriptionInstance(){
		ChangeDescription changeDescription = ChangeFactory.eINSTANCE.createChangeDescription();
		return changeDescription;
	}
	
	@Override
	public void undo() throws Throwable {
		undoInternal();
	}
	
	@Override
	public void redo() throws Throwable {
		redoInternal();
	}
	
	private void applyAndReverseChangeDescription(ChangeDescription changeDescription){
		final ChangeDescription inputChangeDescription = changeDescription;
		TransactionUtils.writeIfNecessary(existingPlan, new Runnable() {
			@Override
			public void run() {
				if(inputChangeDescription != null) {
					inputChangeDescription.applyAndReverse();
				}
			}
		});		
	}
	
	private void undoInternal() {	
		applyAndReverseChangeDescription(changeDescription2);
		applyAndReverseChangeDescription(changeDescription1);
	}

	private void redoInternal() {		
		applyAndReverseChangeDescription(changeDescription1);
		applyAndReverseChangeDescription(changeDescription2);
	}
	
	private void addToChangeDescription(EObject target, EStructuralFeature feature, ListChange listChange, ChangeDescription changeDescription) {
		if (listChange != null && feature != null){
			EMap<EObject, EList<FeatureChange>> objectChanges = changeDescription.getObjectChanges();
			
			if (objectChanges.containsKey(target)){
				featureChanges = objectChanges.get(target);
				for (FeatureChange fc : featureChanges) {
					if (fc.getFeatureName().equalsIgnoreCase(feature.getName())){
						fc.getListChanges().add(listChange);
						break;
					}
				}
			}
			else{
				Object value = target.eGet(feature);
				FeatureChange featureChange = factory.createFeatureChange(feature, value, true);
				if(value instanceof List) {
					featureChange.getListChanges().add(0, listChange);
				}
				
				EList<FeatureChange> featureChanges = new BasicEList();
				featureChanges.add(featureChange);
				objectChanges.put(target, featureChanges);
			}
		}
	}
	
	private void addToChangeDescription(EObject target, EStructuralFeature feature, FeatureChange featureChange, ChangeDescription changeDescription) {
		if (feature != null){
			EMap<EObject, EList<FeatureChange>> objectChanges = changeDescription.getObjectChanges();
			
			if (objectChanges.containsKey(target)){
				featureChanges = objectChanges.get(target);
				featureChanges.add(featureChange);
			}
			else{
				EList<FeatureChange> featureChanges = new BasicEList();
				featureChanges.add(featureChange);
				objectChanges.put(target, featureChanges);
			}
		}
	}
	
	private abstract class UndoableChange {
		abstract void execute ();
	}

	private class Addition<T> extends UndoableChange {
		private T newElement;
		private int position;
		private EObject parent;
		private EStructuralFeature featureOfElement;

		Addition(EObject parent, T newElement, int position, EStructuralFeature featureOfElement) {
			this.newElement = newElement;
			this.position = position;
			this.parent = parent;
			this.featureOfElement = featureOfElement;
		}

		@Override
		void execute() {
			ListChange listChange = null;
			
			if (newElement instanceof BinaryTemporalConstraint){
				listChange = createListFeatureChange(ChangeKind.ADD_LITERAL, position, (EObject)newElement);
				addToChangeDescription(parent, featureOfElement, listChange, changeDescription2);
			}
			else if(newElement instanceof TemporalChain){
				FeatureChange featureChange = createFeatureChange(parent, featureOfElement, newElement);
				addToChangeDescription(parent, featureOfElement, featureChange, changeDescription2);
			}
			else {
				listChange = createListFeatureChange(ChangeKind.ADD_LITERAL, position, (EObject)newElement);
				addToChangeDescription(parent, featureOfElement, listChange, changeDescription1);
			}
		}
	}

	private class Removal<T> extends UndoableChange {
		private T oldElement;
		private int position;
		private EObject parent;
		private EStructuralFeature featureOfElement;

		Removal(EObject parent, List<T> parentList, T oldElement, EStructuralFeature featureOfElement) {
			this.oldElement = oldElement;
			this.position = parentList.indexOf(oldElement);
			this.featureOfElement = featureOfElement;
			this.parent = parent;
		}

		@Override
		void execute() {			
			ListChange listChange = createListFeatureChange(ChangeKind.REMOVE_LITERAL, position, (EObject)oldElement);
			
			if (oldElement instanceof BinaryTemporalConstraint)
				addToChangeDescription(parent, featureOfElement, listChange, changeDescription2);
			else
				addToChangeDescription(parent, featureOfElement, listChange, changeDescription1);
		}
	}

	private class MultipleRemoval<T> extends UndoableChange {
		// Attempted optimization in light of SPF-5872 performance problem,
		// after Arash found many instances of Removal being created,
		// each one large, presumably due to length of parentList.

		private List<T> oldElements;
		private EStructuralFeature featureOfElement;
		private EObject parent;
		
		MultipleRemoval(EObject parent, List<T> parentList, List<T> oldElements, EStructuralFeature featureOfElement) {
			this.oldElements = oldElements;
			this.featureOfElement = featureOfElement;
			this.parent = parent;
		}

		@Override
		void execute() {			
			Collection<EObject> toDelete = new ArrayList();
			toDelete.addAll((Collection<? extends EObject>) oldElements);
			
			Object value = parent.eGet(featureOfElement);
			if(value instanceof List) {
				List<EObject> current = (List) value;
				int currentSize = current.size();

				if(toDelete != null && !toDelete.isEmpty()) {
					for(int i = currentSize - 1; i >= 0; i--) {
						EObject iObject = current.get(i);
						if(toDelete.contains(iObject)) {
							ListChange listChange = createListFeatureChange(ChangeKind.REMOVE_LITERAL, i, iObject);
							
							if (oldElements instanceof BinaryTemporalConstraint)
								addToChangeDescription(parent, featureOfElement, listChange, changeDescription2);
							else
								addToChangeDescription(parent, featureOfElement, listChange, changeDescription1);

						}
					}
				}
			}
		}
	}




	static ChangeFactory factory = ChangeFactory.eINSTANCE;

	/** Receives the EObject to be modified and Map of EStructuralFeatures and the new values to be set 
	 * and generates the proper FeatureChanges for target to be applied using
	 * the ChangeDescription and ChangeDescriptionOperation.
	 * 
	 * @param target - EObject to be modified
	 * @param map - Map where key -> EStructuralFeature, value -> new value
	 * @return
	 */
	public static EList<FeatureChange> createFeatureChangesFromFeatureMap(EObject target, Map<EStructuralFeature, Object> changesMap) {

		EList<FeatureChange> featureChanges = new BasicEList();
		for (Entry<EStructuralFeature, Object> changeEntry : changesMap.entrySet()) {
			EStructuralFeature feature = changeEntry.getKey();
			Object value = changeEntry.getValue();

			FeatureChange featureChange = null;
			if(value instanceof List) {
				List newValue = (List) value;

				Object currentValue = target.eGet(feature);
				if(currentValue instanceof List) {					
					List listCurrentValues = (List) currentValue;

					Set toDelete = new HashSet(listCurrentValues);
					toDelete.removeAll(newValue);

					Set toAdd = new HashSet(newValue);
					toAdd.removeAll(listCurrentValues);

					featureChange = createFeatureChangeForMultivalueFeatures(target, feature, toAdd, toDelete);
				}
			} else {
				featureChange = createFeatureChange(target, feature, value);
			}

			if(featureChange != null) {
				featureChanges.add(featureChange);
			}
		}

		return featureChanges;

	}

	/** Received the target EObject, the feature to be edited and the values to add and delete.
	 * This method is used for features that are instances of a list, else returns a blank FeatureChange. To make sure that the FeatureChange 
	 * is properly generated please use this method to create it.
	 * 
	 * @param target - object to be edited
	 * @param feature - feature of target to edit
	 * @param toAdd - EObjects to be added to value of the feature
	 * @param toDelete - EObjects to be deleted from value of the feature
	 * @return
	 */
	public static FeatureChange createFeatureChangeForMultivalueFeatures(EObject target, EStructuralFeature feature, Collection<EObject> toAdd, Collection<EObject> toDelete) {
		Object value = target.eGet(feature);
		if(value instanceof List) {
			FeatureChange fc = factory.createFeatureChange(feature, value, true);
			List<EObject> current = (List) value;
			int currentSize = current.size();
			if ((toAdd != null) && !toAdd.isEmpty()) {
				Iterator<EObject> iterator = toAdd.iterator();
				ListChange lc = 
					createListFeatureChange(ChangeKind.ADD_LITERAL, 
							currentSize, iterator.next());
				while (iterator.hasNext()) {
					lc.getReferenceValues().add(iterator.next());
				}
				fc.getListChanges().add(lc);
			}

			if(toDelete != null && !toDelete.isEmpty()) {
				for(int i = current.size()-1; i >= 0; i--) {
					EObject iObject = current.get(i);
					if(toDelete.contains(iObject)) {
						ListChange lc = createListFeatureChange(ChangeKind.REMOVE_LITERAL, i, iObject);
						fc.getListChanges().add(lc);
					}
				}
			}
			if (fc != null)
				return fc;
			else 
				return null;
		}
		return null;
	}


	/** Received the target EObject, the feature to be edited and new value to set.
	 * This method is used for features that are NOT instances of a list, if it's a List 
	 * returns <i>null</i> (please use other createFeatureChange method).
	 * 
	 * @param target - object to be edited
	 * @param feature - feature to be edited
	 * @param newValue - value to set
	 * @return FeatureChange
	 */
	public static FeatureChange createFeatureChange(EObject target, EStructuralFeature feature, Object newValue) {
		Object value = target.eGet(feature);
		if(!(value instanceof List)) {
			return factory.createFeatureChange(feature, newValue, true);
		}
		return null;
	}

	/** This method creates a ListChange with the proper ChangeKind, index and referenced object.
	 * 
	 * @param kind - ChangeKind (ADD or REMOVE)
	 * @param index - where the change is going to happen
	 * @param object - object to ADD or REMOVE
	 * @return ListChange
	 */
	public static ListChange createListFeatureChange(ChangeKind kind, int index, EObject object) {
		ListChange listChange = factory.createListChange();
		listChange.setKind(kind);
		listChange.getReferenceValues().add(object);
		listChange.setIndex(index);	
		return listChange;
	}

	//******************************************** DELETE *********************************************//


	/** For completion. To ensure a proper FeatureChange use <i>createFeatureChange()</i> methods.
	 * @param target
	 * @param feature
	 * @param indexToRemove
	 * @return
	 */
	public static FeatureChange createRemoveFromListFeatureChange(EObject target, EStructuralFeature feature, int indexToRemove) {
		Object value = target.eGet(feature);
		FeatureChange fc = null;
		if(value instanceof List) {
			if ((((List) value).size() - 1) < indexToRemove)
				return null;

			fc = factory.createFeatureChange(feature, value, false);
			EObject object = (EObject) ((List) value).get(indexToRemove);
			ListChange listChange = createListFeatureChange(ChangeKind.REMOVE_LITERAL, indexToRemove, object);
			fc.getListChanges().add(listChange);
		}

		return fc;
	}

	//******************************************** ADD *********************************************//

	/** For completion. To ensure a proper FeatureChange use <i>createFeatureChange()</i> methods.
	 * @param target
	 * @param feature
	 * @param objectToAdd
	 * @param whereToAdd
	 * @return
	 */
	public static FeatureChange createAddToListFeatureChange(EObject target, EStructuralFeature feature, EObject objectToAdd, int whereToAdd) {
		Object value = target.eGet(feature);
		FeatureChange fc = null;
		if(value instanceof List) {
			if ((((List) value).size() - 1) < whereToAdd)
				whereToAdd = ((List) value).size();

			fc = factory.createFeatureChange(feature, value, true);
			ListChange listChange = createListFeatureChange(ChangeKind.ADD_LITERAL, whereToAdd, objectToAdd);
			fc.getListChanges().add(listChange);
		}

		return fc;
	}

	public ChangeDescription createAddActivityToPlanChangeDescription(EPlan plan, EActivity activity) {

		ChangeDescription cd = ChangeFactory.eINSTANCE.createChangeDescription();
		EStructuralFeature feature = PlanPackage.Literals.EPLAN_PARENT__CHILDREN;
		List activitiesToAdd = new BasicEList(); 
		activitiesToAdd.add(activity);

		FeatureChange featureChange = createFeatureChangeForMultivalueFeatures(plan, feature, activitiesToAdd, null);
		if(featureChange != null) {
			EList<FeatureChange> allFeatureChanges = new BasicEList();
			allFeatureChanges.add(featureChange);
			cd.getObjectChanges().put(plan, allFeatureChanges);
		}
		return cd;
	}


}


