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
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.constraints.ConstraintAdvisorUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.ui.forms.widgets.FormText;

public class NogoodViolation extends Violation {

	protected INogoodPart nogoodPart;

	public NogoodViolation(PlanAdvisor advisor, INogoodPart nogoodPart) {
		super(advisor);
		this.nogoodPart = nogoodPart;
	}

	@Override
	public int hashCode() {
		return nogoodPart.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NogoodViolation) {
			NogoodViolation key = (NogoodViolation) obj;
			return this.nogoodPart.equals(key.nogoodPart);
		}
		return super.equals(obj);
	}

	@Override
	public String getType() {
		return "Inconsistency";
	}

	@Override
	public boolean isWaivedByInstance() {
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			BinaryTemporalConstraint constraint = ((NogoodTemporalDistanceConstraint) nogoodPart).getConstraint();
			return constraint.getWaiverRationale() != null;
		} else if (nogoodPart instanceof NogoodTimepointConstraint) {
			PeriodicTemporalConstraint constraint = ((NogoodTimepointConstraint) nogoodPart).getConstraint();
			return constraint.getWaiverRationale() != null;
		} else if (nogoodPart instanceof NogoodTemporalChain) {
			TemporalChain chain = ((NogoodTemporalChain)nogoodPart).getChain();
			return (chain.getWaiverRationale() != null);
		}
		return false;
	}
	
	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		return ((IConstraintNetworkAdvisor)advisor).isCurrentlyViolated(nogoodPart);
	}

	@Override
	public String getName() {
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			return "Distance";
		}
		if (nogoodPart instanceof NogoodTemporalDuration) {
			return "Duration";
		}
		if (nogoodPart instanceof NogoodTimepointConstraint) {
			return "Endpoint";
		}
		if (nogoodPart instanceof NogoodTemporalChain) {
			return "Chain";
		}
		if (nogoodPart instanceof NogoodContainment) {
			return "Parent/child";
		}
		return "Nogood";
	}

	@Override
	public String getDescription() {
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			BinaryTemporalConstraint constraint = ((NogoodTemporalDistanceConstraint)nogoodPart).getConstraint();
			return constraint.getMinimumBminusA() + " to " + constraint.getMaximumBminusA();
		}
		if (nogoodPart instanceof NogoodTemporalDuration) {
			EPlanElement element = ((NogoodTemporalDuration)nogoodPart).getPlanElement();
			if (element == null) {
				return "??? seconds";
			}
			TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
			return extent.getDurationMillis() / 1000.0 + " seconds";
		}
		if (nogoodPart instanceof NogoodTimepointConstraint) {
			PeriodicTemporalConstraint constraint = ((NogoodTimepointConstraint)nogoodPart).getConstraint();
			return constraint.getPoint().getAnchorElement().toString();
		}
		if (nogoodPart instanceof NogoodTemporalChain) {
			List<EPlanElement> elements = ((NogoodTemporalChain)nogoodPart).getParticipants();
			return PlanUtils.getNameListString(elements);
		}
		if (nogoodPart instanceof NogoodContainment) {
			return "Parent constraint conflict";
		}
		return super.getDescription();
	}

	@Override
	public List<EPlanElement> getElements() {
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			BinaryTemporalConstraint constraint = ((NogoodTemporalDistanceConstraint)nogoodPart).getConstraint();
			elements.add(constraint.getPointA().getElement());
			elements.add(constraint.getPointB().getElement());
		}
		if (nogoodPart instanceof NogoodTemporalDuration) {
			elements.add(((NogoodTemporalDuration)nogoodPart).getPlanElement());
		}
		if (nogoodPart instanceof NogoodTimepointConstraint) {
			PeriodicTemporalConstraint constraint = ((NogoodTimepointConstraint)nogoodPart).getConstraint();
			elements.add(constraint.getPoint().getElement());
		}
		if (nogoodPart instanceof NogoodTemporalChain) {
			List<EPlanElement> participants = ((NogoodTemporalChain)nogoodPart).getParticipants();
			elements.addAll(participants);
		}
		if (nogoodPart instanceof NogoodContainment) {
			elements.add(((NogoodContainment)nogoodPart).getChild());
		}
		return elements;
	}

	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		String printed = null;
		String waiverRationale = null;
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			NogoodTemporalDistanceConstraint part = (NogoodTemporalDistanceConstraint)nogoodPart;
			waiverRationale = part.getConstraint().getWaiverRationale();
			printed = new NogoodPrinter(identifiableRegistry).getText(part);
		}
		if (nogoodPart instanceof NogoodTemporalDuration) {
			NogoodTemporalDuration part = (NogoodTemporalDuration)nogoodPart;
			printed = new NogoodPrinter(identifiableRegistry).getText(part);
		}
		if (nogoodPart instanceof NogoodTimepointConstraint) {
			NogoodTimepointConstraint part = (NogoodTimepointConstraint)nogoodPart;
			waiverRationale = part.getConstraint().getWaiverRationale();
			printed = new NogoodPrinter(identifiableRegistry).getText(part);
		}
		if (nogoodPart instanceof NogoodTemporalChain) {
			NogoodTemporalChain part = (NogoodTemporalChain) nogoodPart;
			waiverRationale = part.getChain().getWaiverRationale();
			printed = new NogoodPrinter(identifiableRegistry).getText(part);
		}
		if (nogoodPart instanceof NogoodContainment) {
			NogoodContainment part = (NogoodContainment) nogoodPart;
			printed = new NogoodPrinter(identifiableRegistry).getText(part);
		}
		if (printed != null) {
			StringBuilder builder = new StringBuilder(printed);
			appendTime(builder);
			appendWaiverRationale(builder, waiverRationale);
			return builder.toString();
		}
		return super.getFormText(text, identifiableRegistry);
	}

	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		if (nogoodPart instanceof NogoodTemporalDistanceConstraint) {
			BinaryTemporalConstraint constraint = ((NogoodTemporalDistanceConstraint) nogoodPart).getConstraint();
			suggestions.add(createToggleWaiveSuggestion("constraint", constraint));
			suggestions.add(createRemoveConstraintSuggestion(constraint));
		} else if (nogoodPart instanceof NogoodTimepointConstraint) {
			PeriodicTemporalConstraint constraint = ((NogoodTimepointConstraint) nogoodPart).getConstraint();
			suggestions.add(createToggleWaiveSuggestion("constraint", constraint));
			suggestions.add(createRemoveConstraintSuggestion(constraint));
		} else if (nogoodPart instanceof NogoodTemporalChain) {
			TemporalChain chain = ((NogoodTemporalChain) nogoodPart).getChain();
			suggestions.add(createToggleWaiveSuggestion("chain", chain));
			suggestions.add(createRemoveChainSuggestion(chain));
		}
		suggestions.addAll(super.getSuggestions());
		return suggestions;
	}

	private Suggestion createRemoveConstraintSuggestion(BinaryTemporalConstraint constraint) {
		return ConstraintAdvisorUtils.createRemoveConstraintSuggestion(constraint);
	}

	private Suggestion createRemoveConstraintSuggestion(PeriodicTemporalConstraint constraint) {
		return ConstraintAdvisorUtils.createRemoveConstraintSuggestion(constraint);
	}

	private Suggestion createRemoveChainSuggestion(TemporalChain chain) {
		return ConstraintAdvisorUtils.createRemoveChainSuggestion(chain);
	}

	@Override
	public String getMarkerType() {
	    return ConstraintsPlugin.PLUGIN_ID + ".nogoodviolation";
	}
	
	@Override
	public void dispose() {
		super.dispose();
		nogoodPart = null;
	}

}
