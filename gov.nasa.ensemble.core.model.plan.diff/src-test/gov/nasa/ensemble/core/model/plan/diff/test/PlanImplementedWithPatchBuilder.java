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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.patch.PlanPatchBuilder;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementCopier;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.patch.PatchOperation;

import java.util.Date;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.jscience.physics.amount.Amount;

/**
 * A way of reusing the Plan Diff test code to exercise ChangeDescription.
 * @see TestPatchBuilderUsingPlanDiff
 * @see API in https://ensemble.jpl.nasa.gov/confluence/x/zIK9Ag
 */
public class PlanImplementedWithPatchBuilder extends PlanForPlanDiffTest {

	private static final EStructuralFeature CHILDREN = PlanPackage.Literals.EPLAN_PARENT__CHILDREN;
	private PlanElementCopier copier = new PlanElementCopier();
	
	@Override
	public PlanImplementedWithPatchBuilder add(String activityGroupName) {
		return newCopy().addTopLevel(createGroup(activityGroupName));
	}
	
	@Override
	public void destructivelyAdd(String activityGroupName) {
		addTopLevel(createGroup(activityGroupName));
	}

	@Override
	public PlanImplementedWithPatchBuilder addOrphanActivity(String activityName) {
		return newCopy().addTopLevel(createActivity(activityName));
	}

	@Override
	public PlanImplementedWithPatchBuilder addUnder(String existingActivityGroupName,
			String newActivityName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		return copy.addSomethingUnder(
				createActivity(newActivityName),
				copy.find(existingActivityGroupName, copy.getPlan()));
	}

	@Override
	public PlanImplementedWithPatchBuilder addGroupUnderGroup(
			String existingActivityGroupName, String newActivityGroupName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		return copy.addSomethingUnder(
				createGroup(newActivityGroupName),
				copy.find(existingActivityGroupName, copy.getPlan()));
		}

	@Override
	public PlanImplementedWithPatchBuilder delete(String name) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement victim = copy.find(name, copy.getPlan());
		EPlanParent parent = (EPlanParent) victim.eContainer();
		PatchBuilder builder = new PlanPatchBuilder();
		builder.remove(parent, CHILDREN, victim);
		builder.getPatch();
		return copy.applyChanges(builder.getPatch());
	}

	@Override
	public PlanImplementedWithPatchBuilder move(String childName, String newParentName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement child = copy.find(childName, copy.getPlan());
		EPlanParent oldParent = (EPlanParent) child.eContainer();
		EPlanParent newParent = (EPlanParent) copy.find(newParentName, copy.getPlan());

		EPlanElement childCopy = EPlanUtils.copy(child);
		PatchBuilder builder = new PlanPatchBuilder();
		builder.add (newParent, CHILDREN, childCopy);
		builder.remove (oldParent, CHILDREN, child);
		return copy.applyChanges(builder.getPatch());
	}

	@Override
	public PlanImplementedWithPatchBuilder rename(String existingName, String newName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement target = copy.find(existingName, copy.getPlan());
		return copy.changeParameter(target,
								PlanPackage.Literals.EPLAN_ELEMENT__NAME,
								newName);
	}

	@Override
	public PlanImplementedWithPatchBuilder changeString(String existingName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement target = copy.find(existingName, copy.getPlan());
		String oldValue = target.getMember(CommonMember.class).getNotes();
		String newValue = oldValue+"*";
		return copy.changeParameter(target, CommonMember.class,
								PlanPackage.Literals.COMMON_MEMBER__NOTES,
								newValue);
	}
	@Override
	public PlanImplementedWithPatchBuilder changeBoolean(String existingName) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement target = copy.find(existingName, copy.getPlan());
		boolean oldValue = target.getMember(CommonMember.class).isMarked();
		boolean newValue = !oldValue;
		return copy.changeParameter(target, CommonMember.class,
								PlanPackage.Literals.COMMON_MEMBER__MARKED,
								newValue);
	}

	@Override
	public PlanImplementedWithPatchBuilder delay(String existingName, int hours) {
		PlanImplementedWithPatchBuilder copy = newCopy();
		EPlanElement target = copy.find(existingName, copy.getPlan());
		Date oldValue = target.getMember(TemporalMember.class).getStartTime();
		long offset = Amount.valueOf(hours, NonSI.HOUR).to(SI.MILLI(SI.SECOND)).getExactValue();
		Date newValue = DateUtils.add(oldValue, offset);
		return copy.changeParameter(target, TemporalMember.class,
				TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME ,
								newValue);
	}

	public PlanImplementedWithPatchBuilder() {
		super();
	}

	public PlanImplementedWithPatchBuilder(ResourceSet resourceSet) {
		super(resourceSet);
	}
	
	
	public PlanImplementedWithPatchBuilder(EPlan newPlan,
			ResourceSet resourceSet) {
		super(newPlan, resourceSet);
	}

	private EActivityGroup createGroup(String activityGroupName) {
		EActivityGroup result = PlanFactory.eINSTANCE.createEActivityGroup();
		result.setName(activityGroupName);
		result.getMember(TemporalMember.class).setStartTime(PLAN_START);
		return result;
	}
	
	private EActivity createActivity(String activityName) {
		EActivity result = PlanFactory.eINSTANCE.createEActivity();
		result.setName(activityName);
		result.getMember(TemporalMember.class).setStartTime(PLAN_START);
		return result;
	}
	
	protected PlanImplementedWithPatchBuilder newCopy() {
		return newCopy(getPlan());
	}
	
	@Override
	protected PlanImplementedWithPatchBuilder newCopy(EPlan newPlan) {
		return new PlanImplementedWithPatchBuilder(getCopyOf(newPlan), this.resourceSet);
	}
	
	protected EPlan getCopyOf(EPlan plan) {
		EPlan copy = (EPlan) copier.copy(plan);
		copier.copyReferences();
		return copy;
	}
	
	private PlanImplementedWithPatchBuilder addTopLevel(EPlanChild newElement) {
		return addSomethingUnder(newElement, getPlan());
	}
	
	private PlanImplementedWithPatchBuilder addSomethingUnder(EPlanChild newElement, EPlanElement parent) {
		PatchBuilder builder = new PlanPatchBuilder();
		builder.add(parent, CHILDREN, newElement);
		return applyChanges(builder.getPatch());
	}
	

	private PlanImplementedWithPatchBuilder changeParameter(
			EPlanElement target, Class<? extends EMember> memberClass,
			EStructuralFeature feature, Object newValue) {
		EMember member = target.getMember(memberClass);
		PatchBuilder builder = new PlanPatchBuilder();
		builder.modify(member, feature, newValue);
		return applyChanges(builder.getPatch());	
	}

	private PlanImplementedWithPatchBuilder changeParameter(
			EPlanElement target,
			EStructuralFeature feature, Object newValue) {
		PatchBuilder builder = new PlanPatchBuilder();
		builder.modify(target, feature, newValue);
		return applyChanges(builder.getPatch());
	}

	private PlanImplementedWithPatchBuilder applyChanges(Patch patch) {
		try {
			// Destructively make changes to new copy.
			new TestableChangeDescriptionOperation(getPlan(), patch).execute();
		} catch (Throwable e) {
			// Quietly log it and let test figure out the change wasn't made.
			LogUtil.error("Unable to construct ChangeDescription:  " + e);
		} 
		return this;
	}

	
	private class TestableChangeDescriptionOperation extends PatchOperation {
		public TestableChangeDescriptionOperation(EObject target, Patch patch) {
			super(target, patch, "TestableChangeDescriptionOperation");
		}

		@Override
		protected void execute() throws Throwable {
			// Make it visible within this class.
			super.execute();
		}
		
	}
	
}
