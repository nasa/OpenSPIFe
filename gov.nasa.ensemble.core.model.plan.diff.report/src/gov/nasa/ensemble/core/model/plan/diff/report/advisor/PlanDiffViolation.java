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
package gov.nasa.ensemble.core.model.plan.diff.report.advisor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameterOrReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange.DiffType;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.Activator;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionAddOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionRemoveOperation;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.advisor.CreateWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.RemoveWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.constraints.TemporalConstraintPrinter;
import gov.nasa.ensemble.core.plan.constraints.operations.CreateTemporalBoundOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.CreateTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalBoundOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.UnchainOperation;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TemporalDistanceViolation;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.TemporalEndpointViolation;
import gov.nasa.ensemble.core.plan.constraints.ui.operation.ChainOperation;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormText;

public abstract class PlanDiffViolation extends Violation {
	
	public static final ImageDescriptor UPDATE_ACTIVITY_ICON = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/updateActivity.png");
	public static final ImageDescriptor ADD_TO_PPCR_ICON = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/addToPPCR.png");
	
	protected PlanDiffList diffList;
	protected PlanChange difference;
	protected EPlanElement target;
	protected EPlanElement updated;
	protected EStructuralFeature feature;
	protected IWaivable constraint;
	protected ProfileEffect effect;
	protected Violation dummyViolation;

	public PlanDiffViolation(PlanDiffPlanAdvisor advisor, PlanChange difference) {
		super(advisor);
		this.diffList = advisor.getPlanDiffs();
		init(difference);
	}

	private void init(PlanChange difference) {
		if (difference instanceof ChangedByModifyingParameterOrReference) {
			ChangedByModifyingParameterOrReference modification = (ChangedByModifyingParameterOrReference)difference;
			target = modification.getOldCopyOfOwner();
			updated = modification.getNewCopyOfOwner();
			feature = modification.getParameter();
		} else if (difference instanceof ChangedByAddingNewElement) {
			updated = ((ChangedByAddingNewElement)difference).getAddedElement();
		} else if (difference instanceof ChangedByRemovingElement) {
			target = ((ChangedByRemovingElement)difference).getRemovedElement();
		} else if (difference instanceof ChangedConstraintOrProfile) {
			ChangedConstraintOrProfile change = (ChangedConstraintOrProfile)difference;
			EObject object = change.getObject();
			target = change.getOldCopyOfOwner();
			updated = change.getNewCopyOfOwner();
			if (object instanceof IWaivable) {
				constraint = (IWaivable)object;
				if (object instanceof BinaryTemporalConstraint) {
					dummyViolation = new TemporalDistanceViolation(advisor, null, (BinaryTemporalConstraint)object);
				} else if (object instanceof PeriodicTemporalConstraint) {
					dummyViolation = new TemporalEndpointViolation(advisor, null, (PeriodicTemporalConstraint)object);
				}
			} else if (object instanceof ProfileEffect) {
				effect = (ProfileEffect)object;
			}
		}
		this.difference = difference;
	}
	
	public PlanChange getDifference() {
		return difference;
	}

	public EPlanElement getTarget() {
		return target;
	}

	public EPlanElement getUpdated() {
		return updated;
	}

	public EStructuralFeature getFeature() {
		return feature;
	}
	
	@Override
	public PlanDiffPlanAdvisor getAdvisor() {
		return (PlanDiffPlanAdvisor)advisor;
	}

	@Override
	public String getPrintString(ViolationKey key) {
		switch (key) {
		case ELEMENTS: {
			if (constraint != null || effect != null) {
				return PlanUtils.getNameAndDateListString((Iterable<EPlanElement>) getElements());
			}
			Set<EPlanElement> elements = getDeletableElements();
			if (elements.isEmpty() && updated != null) {
				elements = Collections.singleton(updated);
			}
			return PlanUtils.getNameAndDateListString(elements);
		}
		default:
			return super.getPrintString(key);
		}
	}

	@Override
	public String getName() {
		switch (getDiffType()) {
		case ADD:
			if (constraint != null) {
				return "Missing constraint";
			}
			if (effect != null) {
				return "Missing effect";
			}
			return "Added";
		case REMOVE:
			if (constraint != null) {
				return "Extra constraint";
			}
			if (effect != null) {
				return "Extra effect";
			}
			return "Removed";
		case MODIFY:
			return "Modified";
		case MOVE:
			return "Moved";
		default:
			return super.getName();
		}
	}
	
	@Override
	public String getDescription() {
		if (difference instanceof ChangedByModifyingParameterOrReference) {
			return EMFUtils.getDisplayName(target, feature) + " does not match";
		}
		if (dummyViolation != null) {
			return dummyViolation.getDescription();
		}
		if (constraint instanceof TemporalChain) {
			return "Temporal chain on ";
		}
		if (constraint instanceof ProfileConstraint) {
			return "Constraint on the " + ((ProfileConstraint)constraint).getProfileKey() + " profile";
		}
		if (effect != null) {
			return "Effect on the " + effect.getProfileKey() + " profile";
		}
		return super.getDescription();
	}
	
	@Override
	public String getType() {
		return "Plan Difference";
	}
	
	public DiffType getDiffType() {
		return difference.getDiffType();
	}

	@Override
	public List<? extends EPlanElement> getElements() {
		if (dummyViolation != null) {
			return dummyViolation.getElements();
		}
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		if (constraint instanceof TemporalChain) {
			elements.addAll(((TemporalChain)constraint).getElements());
			return elements;
		}
		if (constraint instanceof ProfileConstraint || effect != null) {
			 elements.add(target);
			 return elements;
		}
		if (target != null) {
			elements.add(target);
		}
		if (updated != null) {
			elements.add(updated);
		}
		return elements;
	}
	
	@Override
	protected Set<EPlanElement> getDeletableElements() {
		if (target != null) {
			return Collections.singleton(target);
		}
		return Collections.emptySet();
	}
	
	@Override
	public boolean isObsolete() {
		if (diffList != getAdvisor().getPlanDiffs()) {
			// the plan advisors diff list has changed -- existing violations are obsolete
			return true;
		}
		// Only the target element is from the plan
		if (target != null && EPlanUtils.getPlan(target) == null) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		if (target != null && EPlanUtils.getPlan(target) == null) {
			return false;
		}
		if (difference instanceof ChangedByModifyingParameterOrReference) {
			ChangedByModifyingParameterOrReference modification = (ChangedByModifyingParameterOrReference)difference;
			return !PlanDiffUtils.deepEquals(modification.getOldValue(), modification.getNewValue());
		} else if (difference instanceof ChangedConstraintOrProfile) {
			boolean found = false;
			if (constraint instanceof TemporalChain) {
				found = PlanDiffUtils.deepEquals(constraint, target.getMember(ConstraintsMember.class).getChain());
			} else {
				List<? extends EObject> currentObjects = null;
				Object searchObject = constraint;
				if (effect != null) {
					currentObjects = target.getMember(ProfileMember.class).getEffects();
					searchObject = effect;
				} else if (constraint instanceof BinaryTemporalConstraint) {
					currentObjects = target.getMember(ConstraintsMember.class).getBinaryTemporalConstraints();
				} else if (constraint instanceof PeriodicTemporalConstraint) {
					currentObjects = target.getMember(ConstraintsMember.class).getPeriodicTemporalConstraints();
				} else if (constraint instanceof ProfileConstraint) {
					currentObjects = target.getMember(ProfileMember.class).getConstraints();
				}
				if (currentObjects != null) {
					for (EObject item : currentObjects) {
						if (PlanDiffUtils.deepEquals(searchObject, item)) {
							found = true;
							break;
						}
					}
				} 
			}
			switch (getDiffType()) {
			case ADD:
			case MODIFY:
				return !found;
			case REMOVE:
				return found;
			default:
				return found;
			}
		}
		return super.isCurrentlyViolated();
	}

	@Override
	public boolean isWaivedByInstance() {
		List<String> waivers = getPlanDiffWaivers();
		String prefix = getWaiverPrefix();
		return WaiverUtils.getRationale(prefix, waivers) != null;
	}
	
	public List<String> getPlanDiffWaivers() {
		if (target != null) {
			return getTargetWaivers(false);
		} else {
			return getUpdatedWaivers(false);
		}
	}
	
	protected List<String> getTargetWaivers(boolean create) {
		List<String> waivedViolations = null;
		if (target instanceof EPlanChild) {
			ActivityAdvisorMember member = target.getMember(ActivityAdvisorMember.class);
			String key = getAdvisor().getClass().getSimpleName();

			if (create) {
				waivedViolations = WaiverUtils.getWaivedViolations(member, key);
			} else {
				waivedViolations = WaiverUtils.getExistingWaivedViolations(member, key);
			}
		}
		if (waivedViolations == null) {
			waivedViolations = Collections.emptyList();
		}
		return waivedViolations;
	}
	
	protected List<String> getUpdatedWaivers(boolean create) {
		return Collections.emptyList();
	}
	
	public String getWaiverPrefix() {
		String qualifier = "";
		if (feature != null) {
			qualifier = feature.getName();
		} else {
			qualifier = getName();
		}
		EPlanElement waiverElement = target;
		if (waiverElement == null) {
			waiverElement = updated;
		}
		if (waiverElement == null) {
			return "";
		}
		return waiverElement.getPersistentID() + "!" + qualifier + ":::";
	}
	
	@Override
	public boolean isFixable() {
		return true;
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		if (constraint != null) {
			return getConstraintFormText(text, identifiableRegistry);
		}
		if (effect != null) {
			return getEffectFormText(text, identifiableRegistry);
		}
		PlanPrinter printer = new PlanPrinter(identifiableRegistry);
		StringBuilder builder = new StringBuilder();
		if (difference instanceof ChangedByModifyingParameterOrReference) {
			buildModificationFormText((ChangedByModifyingParameterOrReference)difference, printer, builder);
		} else if (difference instanceof ChangedByRemovingElement) {
			buildRemovalFormText((ChangedByRemovingElement)difference, printer, builder);
		} else if (difference instanceof ChangedByAddingNewElement) {
			buildAdditionFormText((ChangedByAddingNewElement)difference, printer, builder);
		}
		return builder.toString();
	}

	private String getConstraintFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		if (text != null) {
			text.setColor("start", ColorConstants.darkGreen);
			text.setColor("end", ColorConstants.red);
		}
		if (constraint instanceof ProfileConstraint) {
			return getProfileConstraintFormText((ProfileConstraint)constraint, identifiableRegistry);
		}
		TemporalConstraintPrinter constraintPrinter = new TemporalConstraintPrinter(identifiableRegistry);
		if (constraint instanceof BinaryTemporalConstraint) {
			BinaryTemporalConstraint binary = (BinaryTemporalConstraint)constraint;
			return constraintPrinter.getText(binary, null);
		} else if (constraint instanceof PeriodicTemporalConstraint) {
			PeriodicTemporalConstraint periodic = (PeriodicTemporalConstraint)constraint;
			return constraintPrinter.getText(periodic, false);
		} else if (constraint instanceof TemporalChain) {
			TemporalChain chain = (TemporalChain)constraint;
			return constraintPrinter.getText(chain, null);
		}
		return "";
	}
	
	private String getProfileConstraintFormText(ProfileConstraint profileConstraint, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		PlanPrinter printer = new PlanPrinter(identifiableRegistry);
		StringBuilder builder = new StringBuilder();
		switch (getDiffType()) {
		case MODIFY:
			EcoreEList<ProfileConstraint> currentConstraints = (EcoreEList)target.getMember(ProfileMember.class).getConstraints();
			ProfileConstraint oldConstraint = null;
			for (ProfileConstraint currentConstraint : currentConstraints) {
				if (similarProfileReferences(currentConstraint, (ProfileConstraint)constraint)) {
					oldConstraint = currentConstraint;
					break;
				}
			}
			if (oldConstraint != null) {
				builder.append(getTargetSource()).append(": ");
				buildConstraintFormText(target, oldConstraint, printer, builder);
				builder.append("<BR/>");
			}
			builder.append(getUpdatedSource()).append(": ");
			buildConstraintFormText(updated, profileConstraint, printer, builder);
			break;
		case ADD:
			builder.append(getUpdatedSource()).append(": ");
			buildConstraintFormText(updated, profileConstraint, printer, builder);
			break;
		case REMOVE:
			builder.append(getTargetSource()).append(": ");
			buildConstraintFormText(target, profileConstraint, printer, builder);
			break;
		default:
		}
		return builder.toString();
	}
	
	private void buildConstraintFormText(EPlanElement element, ProfileConstraint profileConstraint, PlanPrinter printer, StringBuilder builder) {
		String profileKey = profileConstraint.getProfileKey();
		builder.append("The ").append(profileKey).append(" profile ");
		if (profileConstraint instanceof ProfileEqualityConstraint) {
			builder.append(" must have the value ");
			builder.append(((ProfileEqualityConstraint)profileConstraint).getValueLiteral());
		} else if (profileConstraint instanceof ProfileEnvelopeConstraint) {
			ProfileEnvelopeConstraint envelope = (ProfileEnvelopeConstraint)profileConstraint;
			String min = envelope.getMinLiteral();
			boolean hasMin = false;
			String max = envelope.getMaxLiteral();
			if (min != null && !min.isEmpty()) {
				hasMin = true;
				builder.append(" has a min of ").append(min);
			}
			if (max != null && !max.isEmpty()) {
				if (hasMin) {
					builder.append(" and ");
				} else {
					builder.append(" has ");
				}
				builder.append(" a max of ").append(max);
			}
		}
		builder.append(" from start to end of");
		builder.append(printer.getText(element));
	}

	private String getEffectFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		if (text != null) {
			text.setColor("start", ColorConstants.darkGreen);
			text.setColor("end", ColorConstants.red);
		}
		PlanPrinter printer = new PlanPrinter(identifiableRegistry);
		StringBuilder builder = new StringBuilder();
		switch (getDiffType()) {
		case MODIFY:
			EcoreEList<ProfileEffect> currentEffects = (EcoreEList)target.getMember(ProfileMember.class).getEffects();
			ProfileEffect oldEffect = null;
			for (ProfileEffect currentEffect : currentEffects) {
				if (similarProfileReferences(currentEffect, effect)) {
					oldEffect = currentEffect;
					break;
				}
			}
			if (oldEffect != null) {
				builder.append(getTargetSource()).append(": ");
				buildEffectFormText(target, oldEffect, printer, builder);
				builder.append("<BR/>");
			}
			builder.append(getUpdatedSource()).append(": ");
			buildEffectFormText(updated, effect, printer, builder);
			break;
		case ADD:
			builder.append(getUpdatedSource()).append(": ");
			buildEffectFormText(updated, effect, printer, builder);
			break;
		case REMOVE:
			builder.append(getTargetSource()).append(": ");
			buildEffectFormText(target, effect, printer, builder);
			break;
		default:
		}
		return builder.toString();
	}
	
	private void buildEffectFormText(EPlanElement element, ProfileEffect profileEffect, PlanPrinter printer, StringBuilder builder) {
		String startEffect = profileEffect.getEffectLiteral(Timepoint.START);
		boolean hasStartEffect = false;
		String endEffect = profileEffect.getEffectLiteral(Timepoint.END);
		builder.append(printer.getText(element));
		if (startEffect != null && !startEffect.isEmpty()) {
			hasStartEffect = true;
			builder.append(" has a start effect of ").append(startEffect);
		}
		if (endEffect != null && !endEffect.isEmpty()) {
			if (hasStartEffect) {
				builder.append(" and ");
			} else {
				builder.append(" has ");
			}
			builder.append(" an end effect of ").append(endEffect);
		}
		builder.append(" on the ").append(profileEffect.getProfileKey()).append(" profile.");
	}

	protected void buildModificationFormText(ChangedByModifyingParameterOrReference modification, PlanPrinter printer, StringBuilder builder) {
		IStringifier stringifier = EMFUtils.getStringifier(feature);
		builder.append(getTargetSource()).append(" activity ");
		builder.append(printer.getText(target)).append(' ');
		Object oldValue = modification.getOldValue();
		String oldValueString = stringifier.getDisplayString(oldValue);
//		oldValueString = oldValueString.replaceAll("\n", "<BR/>").replaceAll("\r", "");
		builder.append(EMFUtils.getDisplayName(target, feature)).append(": ").append(StringEscapeFormat.escape(oldValueString)).append("<BR/>");
		builder.append(getUpdatedSource()).append(" activity ");
		builder.append(printer.getText(updated)).append(' ');
		Object newValue = modification.getNewValue();
		String newValueString = stringifier.getDisplayString(newValue);
//		newValueString = newValueString.replaceAll("\n", "<BR/>").replaceAll("\r", "");
		builder.append(EMFUtils.getDisplayName(updated, feature)).append(": ").append(StringEscapeFormat.escape(newValueString));
	}
	
	protected void buildRemovalFormText(ChangedByRemovingElement removal, PlanPrinter printer, StringBuilder builder) {
		builder.append(getTargetSource()).append(" activity ");
		builder.append(printer.getText(target)).append(" not matched");
	}
	
	protected void buildAdditionFormText(ChangedByAddingNewElement difference2, PlanPrinter printer, StringBuilder builder) {
		builder.append(getUpdatedSource()).append(" activity ");
		builder.append(printer.getText(updated)).append(" not matched");
	}
		
	protected String getTargetSource() {
		return Display.getAppName();
	}
	
	protected abstract String getUpdatedSource();
	
	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		if (target != null) {
			if (updated != null) {
				if (constraint != null) {
					addConstraintSuggestions(suggestions);
				} else if (effect != null) {
					addEffectSuggestions(suggestions);
				} else {
					addMatchedSuggestions(suggestions);
				}
			} else {
				addTargetOnlySuggestions(suggestions);
			}
		} else if (updated != null) {
			addUpdatedOnlySuggestions(suggestions);
		}
		suggestions.add(createToggleWaiveSuggestion());
		return suggestions;
	}
	
	protected void addMatchedSuggestions(Set<Suggestion> suggestions) {
		return;
	}
	
	protected void addTargetOnlySuggestions(Set<Suggestion> suggestions) {
		return;
	}
	
	protected void addUpdatedOnlySuggestions(Set<Suggestion> suggestions) {
		return;
	}
	
	private void addConstraintSuggestions(Set<Suggestion> suggestions) {
		Suggestion suggestion = null;
		switch (getDiffType()) {
		case ADD:
			suggestion = createAddConstraintSuggestion();
			break;
		case REMOVE:
			suggestion = createRemoveConstraintSuggestion();
			break;
		case MODIFY:
			suggestion = createModifyConstraintSuggestion();
			break;
		default:
		}
		if (suggestion != null) {
			suggestions.add(suggestion);
		}
	}
	
	private void addEffectSuggestions(Set<Suggestion> suggestions) {
		Suggestion suggestion = null;
		switch (getDiffType()) {
		case ADD:
			suggestion = createAddEffectSuggestion();
			break;
		case REMOVE:
			suggestion = createRemoveEffectSuggestion();
			break;
		case MODIFY:
			suggestion = createModifyEffectSuggestion();
			break;
		default:
		}
		if (suggestion != null) {
			suggestions.add(suggestion);
		}
	}
	
	private Suggestion createAddConstraintSuggestion() {
		IUndoableOperation operation = null;
		if (constraint instanceof ProfileConstraint) {
			EcoreEList<ProfileConstraint> currentConstraints = (EcoreEList)target.getMember(ProfileMember.class).getConstraints();
			// Need to make a copy so it doesn't get removed from the template
			ProfileConstraint constraintCopy = (ProfileConstraint) EMFUtils.copy(constraint);
			operation = new FeatureTransactionAddOperation("Add Profile Constraint", currentConstraints, constraintCopy);
		} else if (constraint instanceof BinaryTemporalConstraint) {
			operation = new CreateTemporalRelationOperation((BinaryTemporalConstraint)constraint);
		} else if (constraint instanceof PeriodicTemporalConstraint) {
			operation = new CreateTemporalBoundOperation((PeriodicTemporalConstraint)constraint);
		} else if (constraint instanceof TemporalChain) {
			List<EPlanChild> linked = CommonUtils.castList(EPlanChild.class, ((TemporalChain)constraint).getElements());
			operation = new ChainOperation(PlanStructureModifier.INSTANCE, linked, false);
		}
		if (operation != null) {
			return new Suggestion(UPDATE_ACTIVITY_ICON, "Add Constraint to Plan", operation);
		}
		return null;
	}
	
	private Suggestion createRemoveConstraintSuggestion() {
		IUndoableOperation operation = null;
		if (constraint instanceof ProfileConstraint) {
			EcoreEList<ProfileConstraint> currentConstraints = (EcoreEList)target.getMember(ProfileMember.class).getConstraints();
			operation = new FeatureTransactionRemoveOperation("Remove Profile Constraint", currentConstraints, constraint);
		} else if (constraint instanceof BinaryTemporalConstraint) {
			operation = new DeleteTemporalRelationOperation((BinaryTemporalConstraint)constraint);
		} else if (constraint instanceof PeriodicTemporalConstraint) {
			operation = new DeleteTemporalBoundOperation((PeriodicTemporalConstraint)constraint);
		} else if (constraint instanceof TemporalChain) {
			operation = new UnchainOperation(((TemporalChain)constraint).getElements());
		}
		if (operation != null) {
			return new Suggestion(UPDATE_ACTIVITY_ICON, "Remove Constraint from Plan", operation);
		}
		return null;
	}
	
	private Suggestion createModifyConstraintSuggestion() {
		CompositeOperation operation = null;
		if (constraint instanceof ProfileConstraint) {
			operation = new CompositeOperation("Update Profile Constraint");
			EcoreEList<ProfileConstraint> currentConstraints = (EcoreEList)target.getMember(ProfileMember.class).getConstraints();
			for (ProfileConstraint currentConstraint : currentConstraints) {
				if (similarProfileReferences(currentConstraint, (ProfileConstraint)constraint)) {
					operation.add(new FeatureTransactionRemoveOperation("Remove Profile Constraint", currentConstraints, currentConstraint));
				}
			}
			// Need to make a copy so it doesn't get removed from the template
			ProfileConstraint constraintCopy = (ProfileConstraint) EMFUtils.copy(constraint);
			operation.add(new FeatureTransactionAddOperation("Add Profile Constraint", currentConstraints, constraintCopy));
		}
		if (operation != null) {
			return new Suggestion(UPDATE_ACTIVITY_ICON, "Update Constraint in Plan", operation);
		}
		return null;
	}
	
	private boolean similarProfileReferences(ProfileReference reference1, ProfileReference reference2) {
		return CommonUtils.equals(reference1.getProfileKey(), reference2.getProfileKey())
				&& 
				reference1.getStartOffset().getTimepoint() == reference2.getStartOffset().getTimepoint()
				&&
				reference1.getEndOffset().getTimepoint() == reference2.getEndOffset().getTimepoint();
	}

	private Suggestion createAddEffectSuggestion() {
		EcoreEList<ProfileEffect> currentEffects = (EcoreEList)target.getMember(ProfileMember.class).getEffects();
		// Need to make a copy so it doesn't get removed from the template
		ProfileEffect effectCopy = EMFUtils.copy(effect);
		IUndoableOperation operation = new FeatureTransactionAddOperation("Add Profile Effect", currentEffects, effectCopy);
		return new Suggestion(UPDATE_ACTIVITY_ICON, "Add Effect to Plan", operation);
	}
	
	private Suggestion createRemoveEffectSuggestion() {
		EcoreEList<ProfileEffect> currentEffects = (EcoreEList)target.getMember(ProfileMember.class).getEffects();
		IUndoableOperation operation = new FeatureTransactionRemoveOperation("Remove Profile Effect", currentEffects, effect);
		return new Suggestion(UPDATE_ACTIVITY_ICON, "Remove Effect from Plan", operation);
	}
	
	private Suggestion createModifyEffectSuggestion() {
		EcoreEList<ProfileEffect> currentEffects = (EcoreEList)target.getMember(ProfileMember.class).getEffects();
		CompositeOperation operation = new CompositeOperation("Update Profile Effect");
		for (ProfileEffect currentEffect : currentEffects) {
			if (similarProfileReferences(currentEffect, effect)) {
				operation.add(new FeatureTransactionRemoveOperation("Remove Profile Effect", currentEffects, currentEffect));
			}
		}
		// Need to make a copy so it doesn't get removed from the template
		ProfileEffect effectCopy = EMFUtils.copy(effect);
		operation.add(new FeatureTransactionAddOperation("Add Profile Effect", currentEffects, effectCopy));
		return new Suggestion(UPDATE_ACTIVITY_ICON, "Update Effect in Plan", operation);
	}

	protected void addDeleteSuggestions(Set<Suggestion> suggestions) {
		suggestions.addAll(super.getSuggestions());
	}

	protected Suggestion createUpdateSuggestion(ImageDescriptor icon, String description) {
		ChangedByModifyingParameterOrReference modification = (ChangedByModifyingParameterOrReference)difference;
		IUndoableOperation operation = PlanDiffAdvisorUtils.getFeatureModificationOperation(modification);
		return new Suggestion(icon, description, operation);
	}
	
	
	protected Suggestion createToggleWaiveSuggestion() {
		List<String> waivers = getPlanDiffWaivers();
		String prefix = getWaiverPrefix();
		ImageDescriptor icon = null;
		String description = null;
		IUndoableOperation operation = null;
		if (WaiverUtils.getRationale(prefix, waivers) != null) {
			description = "Unwaive the requirement";
			operation = new RemoveWaiverOperation(description, prefix, waivers) {
				@Override
				protected void setRationale(String rationale) {
					super.setRationale(rationale);
					updateAdvice();
				}
			};
		} else {
			if (!(waivers instanceof EList)) {
				if (target != null) {
					waivers = getTargetWaivers(true);
				} else {
					waivers = getUpdatedWaivers(true);
				}
			}
			icon = WAIVE_ICON;
			description = "Waive the requirement";
			operation = new CreateWaiverOperation(description, prefix, waivers) {
				@Override
				protected void setRationale(String rationale) {
					super.setRationale(rationale);
					updateAdvice();
				}
			};
		}
		return new Suggestion(icon, description, operation);
	}

	protected void updateAdvice() {
		getAdvisor().updateAdvice(this);
	}

	public boolean isPPCRNeeded() {
		return false;
	}

}
