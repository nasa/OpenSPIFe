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
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.constraints.ConstraintAdvisorUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.ui.forms.widgets.FormText;
import org.jscience.physics.amount.Amount;

public class TemporalDistanceViolation extends TemporalViolation {

	private BinaryTemporalConstraint temporalRelation;

	public TemporalDistanceViolation(PlanAdvisor advisor, IConstraintNetworkAdvisor networkAdvisor, BinaryTemporalConstraint relation) {
		super(advisor, networkAdvisor);
		this.temporalRelation = relation;
	}

	@Override
	public int hashCode() {
		return temporalRelation.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemporalDistanceViolation) {
			TemporalDistanceViolation key = (TemporalDistanceViolation) obj;
			return this.temporalRelation.equals(key.temporalRelation) && (this.advisor == key.advisor);
		}
		return super.equals(obj);
	}

	@Override
	public String getName() {
		return "Constraint";
	}

	@Override
	public String getDescription() {
		String description = ConstraintUtils.getDescription(temporalRelation, false);
		if (description == null) {
			return "Invalid constraint";
		}
		return description;
	}
	
	@Override
	public String getMessage() {
		return new MessageConstraintViolationPrinter().getDistanceText(temporalRelation);
	}

	@Override
	public List<EPlanElement> getElements() {
		List<EPlanElement> elements = new ArrayList<EPlanElement>(2);
		elements.add(temporalRelation.getPointA().getElement());
		elements.add(temporalRelation.getPointB().getElement());
		return elements;
	}

	public BinaryTemporalConstraint getConstraint() {
		return temporalRelation;
	}
	
	/*
	 * Utility methods
	 */
	
	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		if (!ConstraintUtils.getBinaryConstraints(temporalRelation.getPointA().getElement(), true).contains(temporalRelation)) {
			return true;
		}
		if (!ConstraintUtils.getBinaryConstraints(temporalRelation.getPointB().getElement(), true).contains(temporalRelation)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		if (isObsolete()) {
			return false;
		}
		return temporalRelation.isViolated();
	}
	
	@Override
	public boolean isWaivedByInstance() {
		return (temporalRelation.getWaiverRationale() != null);
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		setupFormText(text);
		String printed = new ConstraintViolationPrinter(identifiableRegistry).getDistanceText(temporalRelation);
		StringBuilder builder = new StringBuilder(printed);
		appendWaiverRationale(builder, temporalRelation.getWaiverRationale());
		return builder.toString();
	}
	
	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		addMoveSuggestions(suggestions);
		suggestions.add(createToggleWaiveSuggestion("constraint", temporalRelation));
		suggestions.add(createRemoveConstraintSuggestion());
		suggestions.addAll(super.getSuggestions());
		return suggestions;
	}

	@Override
	public String getMarkerType() {
	    return ConstraintsPlugin.PLUGIN_ID + ".temporaldistanceviolation";
	}
	
	private void addMoveSuggestions(Set<Suggestion> suggestions) {
		ConstraintPoint pointA = temporalRelation.getPointA();
		EPlanElement elementA = pointA.getElement();
		ConstraintPoint pointB = temporalRelation.getPointB();
		EPlanElement elementB = pointB.getElement();
		TemporalExtent extentA = elementA.getMember(TemporalMember.class).getExtent();
		TemporalExtent extentB = elementB.getMember(TemporalMember.class).getExtent();
		if ((extentA == null) && (extentB == null)) {
			return;
		}
		Timepoint timepointA = pointA.getEndpoint();
		Timepoint timepointB = pointB.getEndpoint();
		Amount<Duration> minimum = temporalRelation.getMinimumBminusA();
		Amount<Duration> maximum = temporalRelation.getMaximumBminusA();
		if (extentA == null) {
			Date dateB = pointB.getDate();
			Date suggestion1 = DateUtils.add(dateB, minimum);
			Date suggestion2 = DateUtils.add(dateB, maximum);
			if (pointA.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementA, timepointA, suggestion1, null));
				suggestions.add(createMoveSuggestion(elementA, timepointA, suggestion2, null));
			}
			return;
		}
		if (extentB == null) {
			Date dateA = pointA.getDate();
			Date suggestion1 = DateUtils.subtract(dateA, minimum);
			Date suggestion2 = DateUtils.subtract(dateA, maximum);
			if (pointB.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementB, timepointB, suggestion1, null));
				suggestions.add(createMoveSuggestion(elementB, timepointB, suggestion2, null));
			}
			return;
		}
		Date dateA = pointA.getDate();
		Date dateB = pointB.getDate();
		Amount<Duration> delta = DateUtils.subtract(dateB, dateA);
		if ((maximum != null) && delta.isGreaterThan(maximum)) {
			Date suggestionA = DateUtils.subtract(dateB, maximum);
			Date suggestionB = DateUtils.add(dateA, maximum);
			if (pointA.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementA, timepointA, suggestionA, extentA));
			}
			if (pointB.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementB, timepointB, suggestionB, extentB));
			}
			return;
		}
		if ((minimum != null) && delta.isLessThan(minimum)) {
			Date suggestionA = DateUtils.subtract(dateB, minimum);
			Date suggestionB = DateUtils.add(dateA, minimum);
			if (pointA.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementA, timepointA, suggestionA, extentA));
			}
			if (pointB.hasEndpoint()) {
				suggestions.add(createMoveSuggestion(elementB, timepointB, suggestionB, extentB));
			}
			return;
		}
	}
	
	private Suggestion createRemoveConstraintSuggestion() {
		return ConstraintAdvisorUtils.createRemoveConstraintSuggestion(temporalRelation);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		temporalRelation = null;
	}

}
