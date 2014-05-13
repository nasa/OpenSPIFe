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
import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.constraints.ConstraintAdvisorUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.constraints.ui.preference.PlanConstraintsPreferences;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.ui.forms.widgets.FormText;
import org.jscience.physics.amount.Amount;

public class TemporalChainLinkViolation extends TemporalViolation {

	public EPlanElement element1;
	public TemporalExtent extent1;
	public EPlanElement element2;
	public TemporalExtent extent2;

	public TemporalChainLinkViolation(PlanAdvisor advisor, IConstraintNetworkAdvisor networkAdvisor, EPlanElement element1, TemporalExtent extent1, EPlanElement element2, TemporalExtent extent2) {
		super(advisor, networkAdvisor);
		this.element1 = element1;
		this.extent1 = extent1;
		this.element2 = element2;
		this.extent2 = extent2;
	}
	
	@Override
	public int hashCode() {
		return element1.hashCode() * 43 + element2.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TemporalChainLinkViolation) {
			TemporalChainLinkViolation other = (TemporalChainLinkViolation) obj;
			return (this.element1 == other.element1) && (this.element2 == other.element2) && (this.advisor == other.advisor);
		}
		return false;
	}
	
	@Override
	public String getDescription() {
		if (PlanConstraintsPreferences.getUseMeetsChains()) {
			return "The chained elements are not adjacent.";
		}
		return "The chained elements are out of order";
	}

	@Override
	public List<EPlanElement> getElements() {
		List<EPlanElement> elements = new ArrayList<EPlanElement>(2);
		elements.add(element1);
		elements.add(element2);
		return elements;
	}

	@Override
	public String getName() {
		return "Chain";
	}

	@Override
	public Date getTime() {
		return DateUtils.earliest(extent1.getEnd(), extent2.getStart());
	}

	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		TemporalChain chain1 = element1.getMember(ConstraintsMember.class, true).getChain();
		TemporalChain chain2 = element2.getMember(ConstraintsMember.class, true).getChain();
		if ((chain1 == null) || (chain2 == null)) {
			return true;
		}
		return (chain1 != chain2);
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		if (isObsolete()) {
			return false;
		}
		extent1 = element1.getMember(TemporalMember.class).getExtent();
		extent2 = element2.getMember(TemporalMember.class).getExtent();
		if ((SpifePlanUtils.getScheduled(element1) == TriState.FALSE)
			|| (SpifePlanUtils.getScheduled(element2)  == TriState.FALSE)) {
			return false;
		}
		if ((extent1 == null) || (extent2 == null)) {
			return false;
		}
		return isViolated(extent1.getEnd(), extent2.getStart());
	}

	@Override
	public boolean isWaivedByInstance() {
		TemporalChain chain = getChain();
		return (chain != null) && (chain.getWaiverRationale() != null);
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		setupFormText(text);
		String printed = super.getFormText(text, identifiableRegistry);
		StringBuilder builder = new StringBuilder(printed);
		TemporalChain chain = getChain();
		if (chain != null) {
			appendWaiverRationale(builder, chain.getWaiverRationale());
		}
		return builder.toString();
	}

	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		TemporalChain chain = getChain();
		if (chain != null) {
			suggestions.add(createToggleWaiveSuggestion("chain", chain));
		}
		addMoveSuggestions(suggestions);
		addRemoveSuggestion(suggestions);
		suggestions.addAll(super.getSuggestions());
		return suggestions;
	}

	@Override
	public String getMarkerType() {
	    return ConstraintsPlugin.PLUGIN_ID + ".temporalchainlinkviolation";
	}
	
	private void addMoveSuggestions(Set<Suggestion> suggestions) {
		if ((extent1 != null) && (extent2 == null)) {
			suggestions.add(createMoveSuggestion(element2, Timepoint.START, extent1.getEnd(), null));
			return;
		}
		if ((extent1 == null) && (extent2 != null)) {
			suggestions.add(createMoveSuggestion(element1, Timepoint.END, extent2.getStart(), null));
			return;
		}
		if (extent1.getEnd().after(extent2.getStart())) {
			suggestions.add(createMoveSuggestion(element2, Timepoint.START, extent1.getEnd(), extent2));
			suggestions.add(createMoveSuggestion(element1, Timepoint.END, extent2.getStart(), extent1));
			return;
		}
	}
	
	private void addRemoveSuggestion(Set<Suggestion> suggestions) {
		Suggestion suggestion = createRemoveChainSuggestion();
		if (suggestion != null) {
			suggestions.add(suggestion);
		}
	}

	private Suggestion createRemoveChainSuggestion() {
		return ConstraintAdvisorUtils.createRemoveChainSuggestion(getChain());
	}

	private TemporalChain getChain() {
		TemporalChain chain1 = element1.getMember(ConstraintsMember.class, true).getChain();
		TemporalChain chain2 = element2.getMember(ConstraintsMember.class, true).getChain();
		if (chain1 == chain2) {
			return chain1;
		}
		return null;
	}

	public static boolean isViolated(Date priorEnd, Date subsequentStart) {
		if (PlanConstraintsPreferences.getUseMeetsChains()) {
			if (!DateUtils.closeEnough(priorEnd, subsequentStart, ConstraintsPlanAdvisor.MEETS_TOLERANCE)) {
				return !closeEnough(priorEnd, subsequentStart);
			}
		} else {
			if (priorEnd.after(subsequentStart)) {
				return !closeEnough(priorEnd, subsequentStart);
			}
		}
		return false;
	}
	
	private static Amount<Duration> TOLERANCE = Amount.valueOf(750, SI.MILLI(SI.SECOND));
	protected static boolean closeEnough(Date date1, Date date2) {
		Amount<Duration> discrepancy = gov.nasa.ensemble.core.jscience.util.DateUtils.subtract(date1, date2).abs();
		return discrepancy.isLessThan(TOLERANCE);
	}

	@Override
	public void dispose() {
		super.dispose();
		element1 = null;
		element2 = null;
		extent1 = null;
		extent2 = null;
	}
	
}
