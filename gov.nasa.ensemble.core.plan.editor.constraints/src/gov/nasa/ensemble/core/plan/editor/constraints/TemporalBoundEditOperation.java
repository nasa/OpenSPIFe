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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class TemporalBoundEditOperation extends AbstractTransactionUndoableOperation {

	private final EPlanElement element;
	private final Set<PeriodicTemporalConstraint> oldConstraints;
	private final PeriodicTemporalConstraint newConstraint;
	private TemporalExtentsCache initialExtents = null;
	private Map<EPlanElement, TemporalExtent> changedTimes = Collections.emptyMap();
	
	public TemporalBoundEditOperation(EPlanElement element, Set<PeriodicTemporalConstraint> oldConstraints, PeriodicTemporalConstraint newConstraint) {
		this("Edit TOS Constraint", element, oldConstraints, newConstraint);
	}

	public TemporalBoundEditOperation(String name, EPlanElement element, Set<PeriodicTemporalConstraint> oldConstraints, PeriodicTemporalConstraint newConstraint) {
		super(name);
		this.element = element;
		this.oldConstraints = oldConstraints;
		this.newConstraint = newConstraint;
		computeChangedTimes(element, newConstraint);
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}

	@Override
	protected void execute() {
		doit(true);
	}

	private void doit(final boolean execute) {
		TransactionUtils.writing(element, new Runnable() {
			@Override
			public void run() {
				ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
				constraintsMember.getPeriodicTemporalConstraints().removeAll(oldConstraints);
			}
		});
		if (execute) {
			computeChangedTimes(element, newConstraint);
		}
		TransactionUtils.writing(element, new Runnable() {
			@Override
			public void run() {
				TemporalUtils.setExtents(changedTimes);
				if (newConstraint != null) {
					ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
					constraintsMember.getPeriodicTemporalConstraints().add(newConstraint);
				}
			}
		});
	}

	@Override
	protected void undo() {
		TransactionUtils.writing(element, new Runnable() {
			@Override
			public void run() {
				ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, true);
				if (newConstraint != null) {
					constraintsMember.getPeriodicTemporalConstraints().remove(newConstraint);
				}
				TemporalUtils.resetExtents(changedTimes.keySet(), initialExtents);
				constraintsMember.getPeriodicTemporalConstraints().addAll(oldConstraints);
			}
		});
	}

	@Override
	protected void redo() {
		doit(false);
	}

	private void computeChangedTimes(EPlanElement element, PeriodicTemporalConstraint newConstraint) {
		if (newConstraint != null) {
			EPlan plan = EPlanUtils.getPlan(element);
			if (plan != null) {
				this.initialExtents = new TemporalExtentsCache(plan);
				IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
				TemporalMember temporalMember = element.getMember(TemporalMember.class);
				Date earliest = ConstraintUtils.getPeriodicConstraintEarliestDate(newConstraint);
				Date latest = ConstraintUtils.getPeriodicConstraintLatestDate(newConstraint);
				if (newConstraint.getPoint().getEndpoint() == Timepoint.START) {
					Date oldStart = temporalMember.getStartTime();
					if (oldStart != null) {
						Date newStart = DateUtils.bind(oldStart, earliest, latest);
						this.changedTimes = modifier.moveToStart(element, newStart, initialExtents);
					}
				} else {
					Date oldEnd = temporalMember.getEndTime();
					if (oldEnd != null) {
						Date newEnd = DateUtils.bind(oldEnd, earliest, latest);
						this.changedTimes = modifier.moveToEnd(element, newEnd, initialExtents);
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(TemporalBoundEditOperation.class.getSimpleName());
		builder.append(":");
		builder.append(element.getName());
		builder.append(" from ");
		builder.append(String.valueOf(oldConstraints));
		builder.append(" to ");
		builder.append(String.valueOf(newConstraint));
		return builder.toString();
	}
	
}
