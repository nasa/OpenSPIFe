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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.constraints.TemporalPrinter;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;
import java.util.Map;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.forms.widgets.FormText;

public abstract class TemporalViolation extends Violation {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private IConstraintNetworkAdvisor constraintsPlanAdvisor;

	public TemporalViolation(PlanAdvisor advisor, IConstraintNetworkAdvisor networkAdvisor) {
		super(advisor);
		constraintsPlanAdvisor = networkAdvisor;
	}
	
	@Override
	public String getType() {
		return "Temporal";
	}

	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isFixable() {
		return constraintsPlanAdvisor.areViolationsFixable();
	}
	
	protected void setupFormText(FormText text) {
		if(text != null) {
			text.setColor("start", ColorConstants.darkGreen);
			text.setColor("end", ColorConstants.red);
		}
	}

	/**
	 * Suggest moving the element's timepoint to the suggestedTime,
	 * where oldExtent is the old extent of the element, or null if none.
	 * 
	 * @param element
	 * @param timepoint
	 * @param suggestedTime
	 * @param oldExtent
	 * @return
	 */
	protected Suggestion createMoveSuggestion(EPlanElement element, Timepoint timepoint,
			Date suggestedTime, TemporalExtent oldExtent) {
		String description = "Move the " + TemporalPrinter.getText(ConstraintUtils.createConstraintPoint(element, timepoint))
		                   + " to " + DATE_STRINGIFIER.getDisplayString(suggestedTime);
		EPlan plan = EPlanUtils.getPlan(element);
		TemporalExtentsCache temporalExtentsCache = new TemporalExtentsCache(plan);
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		TemporalExtentsCache cache = new TemporalExtentsCache(plan);
		Map<EPlanElement, TemporalExtent> changedTimes;
		if (timepoint == Timepoint.START) {
			changedTimes = modifier.moveToStart(element, suggestedTime, cache);
		} else {
			changedTimes = modifier.moveToEnd(element, suggestedTime, cache);
		}
		String operationDescription = "move the " + TemporalPrinter.getText(ConstraintUtils.createConstraintPoint(element, timepoint));
		IUndoableOperation operation = new SetExtentsOperation(operationDescription, plan, changedTimes, temporalExtentsCache);
		ImageDescriptor icon = ConstraintsPlugin.getImageDescriptor("icons/move_later.png");
		if ((oldExtent != null) && suggestedTime.before(oldExtent.getTimepointDate(timepoint))) {
			icon = ConstraintsPlugin.getImageDescriptor("icons/move_earlier.png");
		}
		return new Suggestion(icon, description, operation);
	}

	@Override
	public void dispose() {
		super.dispose();
		constraintsPlanAdvisor = null;
	}
	
}
