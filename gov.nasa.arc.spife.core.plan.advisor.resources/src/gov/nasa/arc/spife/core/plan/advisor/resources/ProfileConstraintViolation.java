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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.advisor.CreateWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.RemoveWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.forms.widgets.FormText;
import org.jscience.physics.amount.Amount;

public class ProfileConstraintViolation extends ProfileReferenceViolation<ProfileConstraint> {

	private List<EPlanElement> elements;
	
	public ProfileConstraintViolation(PlanAdvisor advisor, EPlanElement owner, Profile profile, ProfileConstraint constraint, String description) {
		this(advisor, owner, profile, constraint, owner.getMember(TemporalMember.class).getStartTime(), owner.getMember(TemporalMember.class).getEndTime(), description);
	}
	
	public ProfileConstraintViolation(PlanAdvisor advisor, EPlanElement owner, Profile profile, ProfileConstraint constraint, Date start, Date end, String description) {
		super(advisor, owner, profile, constraint, start, end, description);

		Date time = getTime();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		if (!(owner instanceof EPlan)) {
			elements.add(owner);
		}
		if (profile != null) {
			DataPoint<?> dataPoint = profile.getDataPoint(time);
			if (dataPoint != null) {
				for (Object o : dataPoint.getContributors()) {
					if (o instanceof EPlanElement) {
						elements.add((EPlanElement) o);
					}
				}
			}
		}
		this.elements = elements;
	}
	
	public String getWaiverRationale() {
		ProfileConstraint constraint = getProfileReference();
		if (constraint instanceof ProfileEnvelopeConstraint) {
			for (ViolationWaiver waiver : ((ProfileEnvelopeConstraint)constraint).getWaivers()) {
				if (DateUtils.closeEnough(waiver.getStart(), getStart(), Amount.valueOf(1, SI.SECOND))
						&& DateUtils.closeEnough(waiver.getEnd(), getEnd(), Amount.valueOf(1, SI.SECOND))) {
					return waiver.getWaiverRationale();
				}
			}
		}
		return constraint.getWaiverRationale();
	}
	
	public void setWaiverRationale(String rationale) {
		ProfileConstraint constraint = getProfileReference();
		if (constraint instanceof ProfileEnvelopeConstraint) {
			EList<ViolationWaiver> waivers = ((ProfileEnvelopeConstraint)constraint).getWaivers();
			ViolationWaiver oldWaiver = null;
			for (ViolationWaiver waiver : waivers) {
				if (DateUtils.closeEnough(waiver.getStart(), getStart(), Amount.valueOf(1, SI.SECOND))
						&& DateUtils.closeEnough(waiver.getEnd(), getEnd(), Amount.valueOf(1, SI.SECOND))) {
					oldWaiver = waiver;
					break;
				}
			}
			if (oldWaiver != null) {
				if (rationale == null) {
					waivers.remove(oldWaiver);
				} else {
					oldWaiver.setWaiverRationale(rationale);
				}
				return;
			}
			if (rationale != null) {
				ViolationWaiver newWaiver = ProfileFactory.eINSTANCE.createViolationWaiver();
				newWaiver.setStart(getStart());
				newWaiver.setEnd(getEnd());
				newWaiver.setWaiverRationale(rationale);
				waivers.add(newWaiver);
			}
		} else {
			constraint.setWaiverRationale(rationale);
		}
	}

	@Override
	public Set<Suggestion> getSuggestions() {
		final Profile profile = getProfile();
		ProfileConstraint constraint = getProfileReference();
		String profileName = EMFUtils.getText(profile, profile.getName());
		String objectName = profileName + " restriction";
		String description;
		IUndoableOperation operation;
		ImageDescriptor icon;
		if (getWaiverRationale() != null) {
			description = "Unwaive the " + objectName;
			operation = new RemoveWaiverOperation(description, constraint) {
				@Override
				protected void setRationale(final String rationale) {
					TransactionUtils.writing(profile, new Runnable() {
						@Override
						public void run() {
							profile.setValid(false);
							setWaiverRationale(rationale);
							profile.setValid(true);
						}
					});
				}
			};
			icon = null;
		} else {
			description = "Waive the " + objectName;
			operation = new CreateWaiverOperation(description, constraint) {
				@Override
				protected void setRationale(String rationale) {
					final String userRationale = getUserRationale(rationale);
					TransactionUtils.writing(profile, new Runnable() {
						@Override
						public void run() {
							profile.setValid(false);
							setWaiverRationale(userRationale);
							profile.setValid(true);
						}
					});
				}
			};
			icon = WAIVE_ICON;
		}
		HashSet<Suggestion> suggestions = new HashSet<Suggestion>();
		suggestions.add(new Suggestion(icon, description, operation));
		Suggestion gapWaiverSuggestion = createGapWaiverSuggestion();
		if (gapWaiverSuggestion != null) {
			suggestions.add(gapWaiverSuggestion);
		}
		return suggestions;
	}
	
	private Suggestion createGapWaiverSuggestion() {
		ProfileConstraint constraint = getProfileReference();
		if (getWaiverRationale() != null
				|| !(constraint instanceof ProfileEqualityConstraint)) {
			return null;
		}
		final ProfileEqualityConstraint equalityConstraint = (ProfileEqualityConstraint) constraint;
		Amount<Duration> value = getDuration();
		final String englishDuration = DurationFormat.getEnglishDuration(value.longValue(SI.SECOND));
		final Amount<Duration> newGap = value;
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(getOwner());
		Command cmd = SetCommand.create(domain, equalityConstraint, ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP, newGap);
		IUndoableOperation operation = new CommandUndoableOperation("Set gap size to "+englishDuration, domain, cmd);
		return new Suggestion(WAIVE_ICON, "Ignore gaps less than "+englishDuration, operation);
	}

	@Override
	public List<EPlanElement> getElements() {
		if (elements == null) {
			return Collections.EMPTY_LIST;
		}
		EPlanElement owner = getOwner();
		if (!elements.isEmpty()) {
			return elements;
		}
		if (owner instanceof EPlan) return Collections.EMPTY_LIST; // SPF-6950
		List<EPlanElement> elements = new ArrayList<EPlanElement>(1);
		elements.add(owner);
		return elements;
	}
	
	@Override
	public boolean isWaivedByRule() {
		return getWaiverRationale() != null;
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		String printed = super.getFormText(text, identifiableRegistry);
		StringBuilder builder = new StringBuilder(printed);
		appendWaiverRationale(builder, getWaiverRationale());
		return builder.toString();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		elements = null;
	}
	
}
