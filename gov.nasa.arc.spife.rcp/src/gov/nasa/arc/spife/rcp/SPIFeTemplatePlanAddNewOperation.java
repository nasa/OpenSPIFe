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

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.util.TemporalMemberUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.view.template.operations.TemplatePlanAddNewOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.jscience.physics.amount.Amount;

public class SPIFeTemplatePlanAddNewOperation extends TemplatePlanAddNewOperation {
	
	private static final Amount<Duration> TEMPLATE_SEPARATION = AmountUtils.toAmount(3600L, SI.SECOND);

	public SPIFeTemplatePlanAddNewOperation(EPlan templatePlan, IStructuredSelection structuredSelection, String activityName) {
		super(templatePlan, structuredSelection, activityName);
	}

	@Override
	public void execute() throws Throwable {
		final EPlan templatePlan = getTemplatePlan();
		if (templatePlan != null) {
			final PlanTransferable planTransferable = createCopy(activityName, templatePlan);
			TransactionUtils.writing(templatePlan, new Runnable() {
				@Override
				public void run() {
					PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
					IStructureLocation location = modifier.getInsertionLocation(planTransferable, new StructuredSelection(templatePlan), InsertionSemantics.ON);
					modifier.add(planTransferable, location);
					needsSave = true;
				}
			});
		}
	}

	private PlanTransferable createCopy(String name, EPlan templatePlan) {
		PlanStructureModifier planStructureModifier = PlanStructureModifier.INSTANCE;
		PlanTransferable source = planStructureModifier.getTransferable(structuredSelection);
		PlanTransferable result = (PlanTransferable) planStructureModifier.copy(source);
		List<? extends EPlanElement> copiedElements = result.getPlanElements();
		EActivityGroup newTemplate = null;
		if (copiedElements.size() == 1 && copiedElements.get(0) instanceof EActivityGroup) {
			newTemplate = (EActivityGroup)copiedElements.get(0);
		} else {
			newTemplate = PlanFactory.getInstance().createActivityGroup(templatePlan);
			List<EPlanChild> children = new ArrayList<EPlanChild>();
			for (EPlanElement element : copiedElements) {
				if (element instanceof EPlanChild) {
					children.add((EPlanChild)element);
				}
			}
			newTemplate.getChildren().addAll(children);
			TemporalMemberUtils.fitToChildren(newTemplate, DateUtils.ZERO_DURATION);
			result.setPlanElements(Collections.singletonList(newTemplate));
		}
		newTemplate.setName(name);
		pruneConstraints(newTemplate);
		adjustStartTimes(newTemplate, templatePlan);
		return result;
	}
	
	private void pruneConstraints(EActivityGroup newTemplate) {
		new PlanVisitor() {

			@Override
			protected void visit(EPlanElement element) {
				ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
				if (constraintsMember != null) {
					constraintsMember.getPeriodicTemporalConstraints().clear();
				}
			}
			
		}.visitAll(newTemplate);
	}

	private void adjustStartTimes(EActivityGroup newTemplate, EPlan templatePlan) {
			Date lastEnd = getLastEndTime(templatePlan);
			Date newStart = DateUtils.add(lastEnd, TEMPLATE_SEPARATION);
			Date templateStart = newTemplate.getMember(TemporalMember.class).getStartTime();
			final Amount<Duration> delta = DateUtils.subtract(newStart, templateStart);
			new PlanVisitor() {

				@Override
				protected void visit(EPlanElement element) {
					TemporalMember temporal = element.getMember(TemporalMember.class);
					Date updatedStart = DateUtils.add(temporal.getStartTime(), delta);
					temporal.setStartTime(updatedStart);
				}
				
			}.visitAll(newTemplate);
	}
	
	private Date getLastEndTime(EPlan templatePlan) {
		final Date[] latestEndTime = {new Date(0)};
		new PlanVisitor() {

			@Override
			protected void visit(EPlanElement element) {
				Date endTime = element.getMember(TemporalMember.class).getEndTime();
				if (endTime.after(latestEndTime[0])) {
					latestEndTime[0] = endTime;
				}
			}

		}.visitAll(templatePlan.getChildren());
		return latestEndTime[0];
	}
	
}
