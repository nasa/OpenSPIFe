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
package gov.nasa.ensemble.core.model.plan.diff.test;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.SortCombinedPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.jscience.physics.amount.Amount;

/**
 * Provides convenient plan-manipulation shorthand for testing:
 * <ul>
 * <li> .diff(withPlan) --> TersePlanDiffSummaryNotationForTesting string
 * <li> .add("X") --> plan with group named "X" added.
 * <li> .addUnder("X", "xx") --> plan with activity named "xx" added under existing group named "X" 
 * <li> .delete("X") --> plan with existing activity or group named "X" deleted
 * <li> .rename("X", "Y") --> plan with Name parameter of existing activity or group named "X" changed
 * <li> .changeString("X") --> plan with Notes parameter of existing activity or group named "X" changed
 * <li> .changeBoolean("X") --> plan with Marked parameter of existing activity or group named "X" toggled
 * <li> .delay("X", hours) --> plan with start time of existing activity or group named "X" incremented
 * <li> .add("X") --> plan with group named "X" added.
 * <li> .addOrphanActivity("X") --> plan with group named "X" added.
 * <li> .swapIDs("A", "B") --> plan with diffID's of two elements swapped with each other.
 * </ul>
 * @throws NullPointerException if expected name not found.
 * @throws ClassCastException if name is not expected type (e.g. addUnder called with activity).
 */
public class PlanForPlanDiffTest {
	
	protected static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	protected static final Date PLAN_START = new Date(System.currentTimeMillis());
	private static final String PLAN_COPIES_ALL_HAVE_THIS_DIFF_ID = "planCopiesAllHaveThisDiffID";
    protected static final ResourceSet sharedResourceSet = EMFUtils.createResourceSet();
	protected ResourceSet resourceSet;
	
	private EPlan plan;
	private boolean suppressUnchanged = false; // whether .diff() should show "=" nodes.

	public PlanForPlanDiffTest(EPlan plan, ResourceSet resourceSet) {
		this.plan = plan;
		this.resourceSet = resourceSet;
	}

	public PlanForPlanDiffTest() {
		this(sharedResourceSet);
	}
	
	public PlanForPlanDiffTest(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		EPlan newPlan = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		newPlan.getMember(TemporalMember.class).setStartTime(PLAN_START);
		newPlan.getMember(CommonMember.class).setDiffID(PLAN_COPIES_ALL_HAVE_THIS_DIFF_ID);
		Resource planResource = new PlanResourceImpl(URI.createURI("http://junit/test.plan"));
		resourceSet.getResources().add(planResource);
		planResource.getContents().add(newPlan);
		this.plan = newPlan;
	}
	
	/**
	 * .add("X") --> plan with group named "X" added.
	 **/
	public PlanForPlanDiffTest add(String activityGroupName) {
		return addElementNamed(PLAN_FACTORY.createActivityGroupInstance(), activityGroupName);
	}

	/**
	 * Use only for creating huge test plan.  Then use non-destructive methods to change it.
	 **/
	public void destructivelyAdd(String activityGroupName) {
		destructivelyAddElementNamed(PLAN_FACTORY.createActivityGroupInstance(), activityGroupName);
	}

	/**
	 * .addOrphanActivity("X") --> plan with group named "X" added.
	 **/
	public PlanForPlanDiffTest addOrphanActivity (String activityName) {
		return addElementNamed(PLAN_FACTORY.createActivityInstance(), activityName);
	}
	
	protected PlanForPlanDiffTest addElementNamed(EPlanChild child, String name) {
		EPlan newPlan = duplicatePlan(plan);
		child.setName(name);
		child.getMember(TemporalMember.class).setStartTime(PLAN_START);
		newPlan.getChildren().add(child);
		return newCopy(newPlan);
	}
	
	protected void destructivelyAddElementNamed(EPlanChild child, String name) {
		child.setName(name);
		child.getMember(TemporalMember.class).setStartTime(PLAN_START);
		plan.getChildren().add(child);
	}


	protected PlanForPlanDiffTest newCopy(EPlan newPlan) {
		return new PlanForPlanDiffTest(newPlan, this.resourceSet);
	}
	
	/**
	 * .addUnder("X", "xx") --> plan with activity named "xx" added under existing group named "X" 
	 **/	
	public PlanForPlanDiffTest addUnder (String existingActivityGroupName, String newActivityName) {
		EPlan newPlan = duplicatePlan(plan);
		EActivityGroup group = (EActivityGroup) find(existingActivityGroupName, newPlan);
		EActivity activity = PLAN_FACTORY.createActivityInstance();
		activity.setName(newActivityName);
		activity.getMember(TemporalMember.class).setStartTime(PLAN_START);
		group.getChildren().add(activity);
		return newCopy(newPlan);
	}
	
	/**
	 * .addGroupUnderGroup("X", "Y") --> plan with group named "Y" added under existing group named "X" 
	 **/	
	public PlanForPlanDiffTest addGroupUnderGroup (String existingActivityGroupName, String newActivityGroupName) {
		EPlan newPlan = duplicatePlan(plan);
		EActivityGroup parent = (EActivityGroup) find(existingActivityGroupName, newPlan);
		EActivityGroup child = PLAN_FACTORY.createActivityGroupInstance();
		child.setName(newActivityGroupName);
		child.getMember(TemporalMember.class).setStartTime(PLAN_START);
		parent.getChildren().add(child);
		return newCopy(newPlan);
	}
	
	/**
	 * .delete("X") --> plan with existing activity or group named "X" deleted
	 */
	public PlanForPlanDiffTest delete (String name) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement target = find(name, newPlan);
		EPlanElement parent = (EPlanElement) target.eContainer();
		parent.getChildren().remove(target);
		return newCopy(newPlan);
	}
	
	/**
	 * .move("X") --> plan with existing activity or group named "X" moved under group "A"
	 */
	public PlanForPlanDiffTest move (String childName, String newParentName) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanChild child = (EPlanChild)find(childName, newPlan);
		EActivityGroup newParent = (EActivityGroup) find(newParentName, newPlan);
		EActivityGroup oldParent = (EActivityGroup) child.eContainer();
		oldParent.getChildren().remove(child);
		newParent.getChildren().add(child);
		return newCopy(newPlan);
	}

	
	/**
	 * .rename("X", "Y") --> plan with name parameter of existing activity or group named "X" changed to "Y"
	 **/
	public PlanForPlanDiffTest rename (String existingName, String newName) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement target = find(existingName, newPlan);
		target.setName(newName);
		return newCopy(newPlan);
	}
	
	/**
	 * .changeString("X") --> plan with Notes parameter of existing activity or group named "X" changed
	 **/
	public PlanForPlanDiffTest changeString(String existingName) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement target = find(existingName, newPlan);
		CommonMember member = target.getMember(CommonMember.class);
		member.setNotes(member.getNotes()+"*");
		return newCopy(newPlan);
	}
	
	/**
	 * .changeBoolean("X") --> plan with Marked parameter of existing activity or group named "X" toggled
	 **/
	public PlanForPlanDiffTest changeBoolean(String existingName) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement target = find(existingName, newPlan);
		CommonMember member = target.getMember(CommonMember.class);
		member.setMarked(!member.isMarked());
		return newCopy(newPlan);
	}
	
	/**
	 * .delay("X", hours) --> plan with start time of existing activity or group named "X" incremented
	 */	
	public PlanForPlanDiffTest delay (String existingName, int hours) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement target = find(existingName, newPlan);
		TemporalMember temporalMember = target.getMember(TemporalMember.class);
		Date oldTime = temporalMember.getStartTime();
		long offset = Amount.valueOf(hours, NonSI.HOUR).to(SI.MILLI(SI.SECOND)).getExactValue();
		Date newTime = DateUtils.add(oldTime, offset);
		temporalMember.setStartTime(newTime);
		return newCopy(newPlan);
	}
	
	/**
	* .swapIDs("A", "B") --> plan with diffID's of two elements swapped with each other.
	*/
	public PlanForPlanDiffTest swapIDs (String name1, String name2) {
		EPlan newPlan = duplicatePlan(plan);
		EPlanElement object1 = find(name1, newPlan);
		EPlanElement object2 = find(name2, newPlan);
		CommonMember guts1 = object1.getMember(CommonMember.class);
		CommonMember guts2 = object2.getMember(CommonMember.class);
		String id1 = guts1.getDiffID();
		String id2 = guts2.getDiffID();
		guts1.setDiffID(id2);
		guts2.setDiffID(id1);
		return newCopy(newPlan);
	}
	
	/** Returns differences in a terse notation.
	 * In case of exception (since a lot can theoretically go wrong),
	 * returns the exception string and does not thrown an exception.
	 * @param otherPlan -- the "after" or "right" plan to compare this one to.
	 * @param treeType -- e.g. PlanDiffTreeTopDown.class
	 * @see TersePlanDiffSummaryNotationForTesting
	 */
	public String diff(PlanForPlanDiffTest otherPlan,
			Class<? extends PlanDiffTree> treeType) {
		return  diff(otherPlan, treeType, null);
	}

	/** Returns differences in a terse notation.
	 * In case of exception (since a lot can theoretically go wrong),
	 * returns the exception string and does not thrown an exception.
	 * @param otherPlan -- the "after" or "right" plan to compare this one to.
	 * @param treeType -- e.g. PlanDiffTreeTopDown.class
	 * @param desiredOrder -- null to preserve original order, or AlphabeticalOrder or ChronologicalOrder
	 * @see TersePlanDiffSummaryNotationForTesting
	 */
	public String diff (PlanForPlanDiffTest otherPlan,
			Class<? extends PlanDiffTree> treeType,
			Comparator<PlanDiffNode> desiredOrder) {
		ensurePlanHasNoDuplicateIds(plan);
		ensurePlanHasNoDuplicateIds(otherPlan.getPlan());
		
		try {
			PlanDiffList differences = PlanDiffEngine.findChanges(plan, otherPlan.getPlan());
			if (differences==null) return "No differences";
			if (desiredOrder==null) {
				desiredOrder = new SortCombinedPlan(plan, otherPlan.getPlan(), differences);
			}
			Constructor<? extends PlanDiffTree> constructor = treeType.getConstructor(PlanDiffList.class, Comparator.class);
			AbstractDiffTree tree = constructor.newInstance(differences, desiredOrder);
			if (suppressUnchanged) {
				return TersePlanDiffSummaryNotationForTesting.getChangesOnly(tree);
			} else {
				return TersePlanDiffSummaryNotationForTesting.getSortedNames(tree);
			}
		} catch (InstantiationException e) {
			return e.getMessage();
		} catch (IllegalAccessException e) {
			return e.getMessage();
		} catch (InvocationTargetException e) {
			return e.getMessage();
		} catch (SecurityException e) {
			return e.getMessage();
		} catch (NoSuchMethodException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * whether .diff() should show "=" nodes.
	 */
	public void setSuppressUnchanged(boolean suppressUnchanged) {
		this.suppressUnchanged = suppressUnchanged;
	}

	protected EPlanElement find(String name, EPlanElement under) {
		EPlanElement result = find(name, under, true);
		if (result != null) return result;
		throw new NullPointerException("Can't find " + name);
	}
	
	private EPlanElement find(String name, EPlanElement under, boolean recursive) {
		if (name.equals(under.getName())) return under;
		for (EPlanElement child : under.getChildren()) {
			EPlanElement result = find(name, child, true);
			if (result != null) return result;
		}
		return null;
	}

	protected EPlan duplicatePlan(EPlan plan) {
		return PlanUtils.duplicatePlan(plan, resourceSet);
	}

	private void ensurePlanHasNoDuplicateIds(EPlan plan) {
		int duplicates = countDuplicateIds(plan, new HashMap<String, EPlanElement>());
		if (duplicates > 0) {
			throw new AssertionFailedError("Plan contains duplicate IDs.");
		}
	}

	private int countDuplicateIds(EPlanElement element, HashMap<String, EPlanElement> alreadyUsed) {
		int result = 0;
		String diffID = element.getMember(CommonMember.class).getDiffID();
		if (alreadyUsed.containsKey(diffID)) {
			result = 1;
			System.err.println(alreadyUsed.get(diffID) + " and " + element + " both have the same diffId (" +
					diffID + ").");
		}
		alreadyUsed.put(diffID, element);
		for (EPlanChild child : element.getChildren()) {
			result += countDuplicateIds(child, alreadyUsed);
		}
		return result;
	}

	public EPlan getPlan() {
		return plan;
	}

	
}
