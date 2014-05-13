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
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
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

public class TemporalEndpointViolation extends TemporalViolation {

	private static final IStringifier<Amount<Duration>> TIME_OF_DAY_STRINGIFIER = new TimeOfDayStringifier();
	
	private PeriodicTemporalConstraint temporalBound;

	public TemporalEndpointViolation(PlanAdvisor advisor, IConstraintNetworkAdvisor networkAdvisor, PeriodicTemporalConstraint temporalBound) {
		super(advisor, networkAdvisor);
		this.temporalBound = temporalBound;
	}
	
	@Override
	public int hashCode() {
		return temporalBound.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemporalEndpointViolation) {
			TemporalEndpointViolation key = (TemporalEndpointViolation) obj;
			return this.temporalBound.equals(key.temporalBound) && (this.advisor == key.advisor);
		}
		return super.equals(obj);
	}

	@Override
	public boolean isFixable() {
		return true;
	}
	
	@Override
	public String getName() {
		if (temporalBound.getEarliest() != null && temporalBound.getLatest() != null &&
				temporalBound.getEarliest().approximates(temporalBound.getLatest())) {
			return "Pin";
		} else if (temporalBound.getEarliest() != null) {
			return "Earliest";
		} else if (temporalBound.getLatest() != null) {
			return "Latest";
		} else {
			// This should never happen, but better to be safe
			return "Endpoint";
		}
	}

	@Override
	public String getDescription() {
		String endpoint = temporalBound.getPoint().getEndpoint() == Timepoint.START ?
				"start" : "end";
		if (temporalBound.getEarliest() != null && temporalBound.getLatest() != null &&
				temporalBound.getEarliest().approximates(temporalBound.getLatest())) {
			return "must " + endpoint + " at " + TIME_OF_DAY_STRINGIFIER.getDisplayString(temporalBound.getEarliest());
		} else if (temporalBound.getEarliest() != null) {
			return "must " + endpoint + " no earlier than " + TIME_OF_DAY_STRINGIFIER.getDisplayString(temporalBound.getEarliest());
		} else if (temporalBound.getLatest() != null) {
			return "must " + endpoint + " no later than " + TIME_OF_DAY_STRINGIFIER.getDisplayString(temporalBound.getLatest());
		} else {
			// This should never happen, but better to be safe
			return "invalid " + endpoint;
		}
	}

	@Override
	public List<EPlanElement> getElements() {
		List<EPlanElement> elements = new ArrayList<EPlanElement>(1);
		elements.add(temporalBound.getPoint().getElement());
		return elements;
	}

	public PeriodicTemporalConstraint getBound() {
		return temporalBound;
	}

	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		EPlanElement element = temporalBound.getPoint().getElement();
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
		if (!constraintsMember.getPeriodicTemporalConstraints().contains(temporalBound)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		if (isObsolete()) {
			return false;
		}
		return temporalBound.isViolated();
	}

	@Override
	public boolean isWaivedByInstance() {
		return (temporalBound.getWaiverRationale() != null);
	}
	
	@Override
	public String getMessage() {
		return new MessageConstraintViolationPrinter().getBoundText(temporalBound);
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		setupFormText(text);
		String printed = new ConstraintViolationPrinter(identifiableRegistry).getBoundText(temporalBound);
		StringBuilder builder = new StringBuilder(printed);
		appendWaiverRationale(builder, temporalBound.getWaiverRationale());
		return builder.toString();
	}
	
	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		addMoveSuggestions(suggestions);
		suggestions.add(createToggleWaiveSuggestion("constraint", temporalBound));
		suggestions.add(createRemoveConstraintSuggestion());
		suggestions.addAll(super.getSuggestions());
		return suggestions;
	}

	@Override
	public String getMarkerType() {
	    return ConstraintsPlugin.PLUGIN_ID + ".temporalendpointviolation";
	}
	
	private void addMoveSuggestions(Set<Suggestion> suggestions) {
		Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(temporalBound); 
		Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(temporalBound);
		ConstraintPoint point = temporalBound.getPoint();
		Date date = point.getDate();
		TemporalMember temporalMember = point.getElement().getMember(TemporalMember.class);
		TemporalExtent extent = new TemporalExtent(temporalMember.getStartTime(), temporalMember.getEndTime());
		if ((minTime != null) && date.before(minTime)) {
			suggestions.add(createMoveSuggestion(point.getElement(), point.getEndpoint(), minTime, extent));
		}
		if ((maxTime != null) && date.after(maxTime)) {
			suggestions.add(createMoveSuggestion(point.getElement(), point.getEndpoint(), maxTime, extent));
		}
	}

	private Suggestion createRemoveConstraintSuggestion() {
		return ConstraintAdvisorUtils.createRemoveConstraintSuggestion(temporalBound);
	}

	@Override
	public void dispose() {
		super.dispose();
		temporalBound = null;
	}
	
}
