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

import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameterOrReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.CreateWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.ISuggestionOperation;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.RemoveWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public abstract class PlanDiffPlanAdvisor extends PlanAdvisor {

	private PlanDiffList planDiffs;
	private EPlan plan;

	public PlanDiffPlanAdvisor(String name, PlanAdvisorMember planAdvisorMember) {
		super(name, planAdvisorMember);
		this.plan = planAdvisorMember.getPlan();
	}
	
	public PlanDiffList getPlanDiffs() {
		return planDiffs;
	}

	public void setPlanDiffs(PlanDiffList planDiffs) {
		this.planDiffs = planDiffs;
		updateInitialAdvice();
	}
	
	public EPlan getPlan() {
		return plan;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	protected List<? extends Advice> initialize() {
		if (planDiffs == null) {
			return null;
		}
		List<PlanDiffViolation> violations = new ArrayList<PlanDiffViolation>();
		for (ChangedByModifyingParameter change : getParameterChanges(planDiffs)) {
			PlanDiffViolation violation = createViolation(change);
			if (violation != null) {
				violations.add(violation);
			}
		}
		for (ChangedByAddingOrRemovingReference change : planDiffs.getReferenceChanges()) {
			PlanDiffViolation violation = createViolation(change);
			if (violation != null) {
				violations.add(violation);
			}
		}
		for (ChangedConstraintOrProfile change : planDiffs.getConstraintAndProfileChanges()) {
			PlanDiffViolation violation = createViolation(change);
			if (violation != null) {
				violations.add(violation);
			}
		}
		for(ChangedByRemovingElement change : planDiffs.getDeletions()) {
			PlanDiffViolation violation = createViolation(change);
			if (violation != null) {
				violations.add(violation);
			}
		}
		for(ChangedByAddingNewElement change : planDiffs.getAdditions()) {
			PlanDiffViolation violation = createViolation(change);
			if (violation != null) {
				violations.add(violation);
			}
		}
		return violations;
	}
	
	protected Collection<ChangedByModifyingParameter> getParameterChanges(PlanDiffList diffs) {
		return diffs.getParameterChanges();
	}
	
	public boolean hasCurrentViolations() {
		for (ViolationTracker tracker : planAdvisorMember.getViolationTrackers()) {
			Violation violation = tracker.getViolation();
			if (violation instanceof PlanDiffViolation
					&& violation.isCurrentlyViolated()
					&& !violation.isWaivedByInstance()) {
				return true;
			}
		}
		return false;
	}

	protected abstract PlanDiffViolation createViolation(PlanChange change);

	@Override
	protected boolean affectsViolations(Notification notification) {
		return false;
	}

	@Override
	protected List<? extends Advice> check(List<Notification> notifications) {
		return Collections.emptyList();
	}

	@Override
	public Set<Suggestion> getSuggestions(Set<ViolationTracker> trackers) {
		List<PlanDiffViolation> violations = new ArrayList<PlanDiffViolation>(trackers.size());
		for (ViolationTracker tracker : trackers) {
			violations.add((PlanDiffViolation)tracker.getViolation());
		}
		if (violations.size() > 0) {
			switch (violations.get(0).getDiffType()) {
			case ADD:
				return getAdditionSuggestions(violations.get(0));
			case REMOVE:
				return getRemovalSuggestions(violations.get(0));
			case MODIFY:
				return getModifySuggestions(violations);
			default:
				return Collections.emptySet();
			}
		}
		return super.getSuggestions(trackers);
	}

	protected Set<Suggestion> getAdditionSuggestions(PlanDiffViolation planDiffViolation) {
		return Collections.emptySet();
	}
	
	protected Set<Suggestion> getRemovalSuggestions(PlanDiffViolation planDiffViolation) {
		return Collections.emptySet();
	}
	
	protected Set<Suggestion> getModifySuggestions(List<PlanDiffViolation> violations) {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		suggestions.add(createUpdateAllSuggestion(PlanDiffViolation.UPDATE_ACTIVITY_ICON, "Accept All Changes", violations));
		suggestions.addAll(createToggleWaiveChangesSuggestion(violations));
		return suggestions;
	}
	
	protected Suggestion createUpdateAllSuggestion(ImageDescriptor icon, String description, List<PlanDiffViolation> violations) {
		CompositeOperation operation = new CompositeOperation(description);
		for (PlanDiffViolation violation : violations) {
			PlanChange difference = violation.getDifference();
			if (difference instanceof ChangedByModifyingParameterOrReference) {
				operation.add(PlanDiffAdvisorUtils.getFeatureModificationOperation((ChangedByModifyingParameterOrReference)difference));
			}
		}
		return new Suggestion(icon, description, operation);
	}
	
	protected List<Suggestion> createToggleWaiveChangesSuggestion(List<PlanDiffViolation> violations) {
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		PlanDiffViolation firstViolation = violations.get(0);
		EPlanElement target = firstViolation.getTarget();
		if (target == null) {
			return Collections.emptyList();
		}
		List<String> waivers = firstViolation.getPlanDiffWaivers();
		List<PlanDiffViolation> waivedViolations = new ArrayList<PlanDiffViolation>();
		List<PlanDiffViolation> unwaivedViolations = new ArrayList<PlanDiffViolation>();
		for (PlanDiffViolation violation : violations) {
			String prefix = violation.getWaiverPrefix();
			if (WaiverUtils.getRationale(prefix, waivers) != null) {
				waivedViolations.add(violation);
			} else {
				unwaivedViolations.add(violation);
			}
		}
		if (!waivedViolations.isEmpty()) {
			ImageDescriptor icon = null;
			String description = "Unwaive child differences";
			CompositeOperation operation = new WaiverCompositeOperation(description, waivedViolations, null);
			for (PlanDiffViolation violation : waivedViolations) {
				String prefix = violation.getWaiverPrefix();
				operation.add(new RemoveWaiverOperation(description, prefix, waivers));
			}
			suggestions.add(new Suggestion(icon, description, operation));
		}
		if (!unwaivedViolations.isEmpty()) {
			// Create a waiver entry on the ActivityAdvisorMember for this advisor if it didn't previously exist
			if (!(waivers instanceof EList)) {
				ActivityAdvisorMember member = target.getMember(ActivityAdvisorMember.class);
				waivers = WaiverUtils.getWaivedViolations(member, this.getClass().getSimpleName());
			}
			ImageDescriptor icon = Violation.WAIVE_ICON;
			String description = "Waive child differences";
			final String[] rationale = new String[] {null};
			CompositeOperation operation = new WaiverCompositeOperation(description, unwaivedViolations, rationale);
			for (PlanDiffViolation violation : unwaivedViolations) {
				String prefix = violation.getWaiverPrefix();
				operation.add(new CreateWaiverOperation(description, prefix, waivers) {

					@Override
					protected void execute() throws Throwable {
						setRationale(rationale[0]);
					}

					@Override
					protected void redo() throws Throwable {
						setRationale(rationale[0]);
					}

					
				});
			}
			suggestions.add(new Suggestion(icon, description, operation));
		}
		return suggestions;
	}
	
	protected void updateAdvice(Violation violation) {
		PlanAdvisorMember planAdvisorMember = getPlanAdvisorMember();
		if (planAdvisorMember != null) {
			planAdvisorMember.updateAdvice(this, Collections.singletonList(violation));
		}
	}
	
	private final class WaiverCompositeOperation extends CompositeOperation implements ISuggestionOperation {
		
		final List<PlanDiffViolation> violations;
		final String[] rationale;
		
		private WaiverCompositeOperation(String label, List<PlanDiffViolation> violations, String[] rationale) {
			super(label);
			this.violations = violations;
			this.rationale = rationale;
		}
		
		public boolean preExecute() {
			if (rationale != null) {
				Shell shell = WidgetUtils.getShell();
				InputDialog dialog = CreateWaiverOperation.getWaiverRationaleDialog(shell, "What is the rationale for waiving differences?");
				int result = dialog.open();
				if (result == Window.CANCEL) {
					return false;
				}
				rationale[0] = dialog.getValue();
				return true;
			}
			return true;
		}

		@Override
		protected IStatus doit(IProgressMonitor monitor, IAdaptable info, boolean execute) throws ExecutionException {
			IStatus status = super.doit(monitor, info, execute);
			if (planAdvisorMember != null) {
				planAdvisorMember.updateAdvice(PlanDiffPlanAdvisor.this, violations);
			}
			return status;
		}

		@Override
		protected IStatus undoit(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			IStatus status = super.undoit(monitor, info);
			if (planAdvisorMember != null) {
				planAdvisorMember.updateAdvice(PlanDiffPlanAdvisor.this, violations);
			}
			return status;
		}

	}

	
}
