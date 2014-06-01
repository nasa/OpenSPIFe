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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.constraints.PinUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class PinOperation extends AbstractTransactionUndoableOperation {

	private final List<EPlanElement> elements;
	private final List<Amount<Duration>> offsets;
	private List<PeriodicTemporalConstraint> bounds;
	
	/**
	 * Pin the elements at their current start times.  Won't add additional pin 
	 * constraints if an element is already pinned to its current start time.
	 * @param elements
	 */
	public PinOperation(Set<? extends EPlanElement> selectedElements) {
		super(createLabel(selectedElements));
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		List<Amount<Duration>> offsets = new ArrayList<Amount<Duration>>();
		for (EPlanElement element : selectedElements) {
			Date startTime = element.getMember(TemporalMember.class).getStartTime();
			if (startTime != null) {
				Amount<Duration> offset = ConstraintUtils.getPeriodicConstraintOffset(startTime);
				elements.add(element);
				offsets.add(offset);
			}
		}
		this.elements = Collections.unmodifiableList(elements);
		this.offsets = Collections.unmodifiableList(offsets);
	}

	private static String createLabel(Set<? extends EPlanElement> selectedElements) {
		String target = "items";
		if (selectedElements.size() == 1) {
			target = selectedElements.iterator().next().getName();
		}
		return "pin " + target;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}
	
	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(elements, new Runnable() {
			@Override
			public void run() {
         				doExecute();
			}});
	}

	public void doExecute() {
		List<PeriodicTemporalConstraint> bounds = new ArrayList<PeriodicTemporalConstraint>();
		int i = 0;
		while ((i < elements.size()) && (i < offsets.size())) {
			EPlanElement element = elements.get(i);
			Amount<Duration> offset = offsets.get(i);
			i++;
			boolean add_constraint = true;
			List<PeriodicTemporalConstraint> constraints = PinUtils.getPinConstraints(elements);
			for (PeriodicTemporalConstraint constraint : constraints) {
				if (constraint.getEarliest() == offset) {
					add_constraint = false;
				}
			}
			if (add_constraint) {
				PeriodicTemporalConstraint bound = PinUtils.createPinConstraint(element, offset);
				ConstraintUtils.attachConstraint(bound);
				bounds.add(bound);
			}
		}
		PinOperation.this.bounds = Collections.unmodifiableList(bounds);
	}

	@Override
	protected void redo() throws Throwable {
		TransactionUtils.writing(elements, new Runnable() {
			@Override
			public void run() {
				for (PeriodicTemporalConstraint bound : bounds) {
					ConstraintUtils.attachConstraint(bound);
				}
			}
		});
	}
	
	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(elements, new Runnable() {
			@Override
			public void run() {
				doUndo();
			}
		});
	}
	
	public void doUndo() {
		for (PeriodicTemporalConstraint bound : bounds) {
			ConstraintUtils.detachConstraint(bound);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(PinOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(elements));
		builder.append(" at ");
		builder.append(String.valueOf(offsets));
		return builder.toString();
	}
	
}
