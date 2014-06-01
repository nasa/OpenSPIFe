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
import gov.nasa.ensemble.core.plan.constraints.PinUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UnpinOperation extends AbstractTransactionUndoableOperation {

	private final Set<? extends EPlanElement> selectedElements;
	private final List<PeriodicTemporalConstraint> bounds;
	
	/**
	 * Remove any existing pins on any of the selected elements, if any exist.
	 * @param elements
	 */
	public UnpinOperation(Set<? extends EPlanElement> elements) {
		super(createLabel(elements));
		this.selectedElements = elements;
		this.bounds = Collections.unmodifiableList(PinUtils.getPinConstraints(elements));
	}

	private static String createLabel(Set<? extends EPlanElement> selectedElements) {
		String target = "items";
		if (selectedElements.size() == 1) {
			target = selectedElements.iterator().next().getName();
		}
		return "unpin " + target;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(selectedElements, new Runnable() {
			@Override
			public void run() {
				doExecute();
			}
		});
	}
	
	public void doExecute() {
		for (PeriodicTemporalConstraint bound : bounds) {
			ConstraintUtils.detachConstraint(bound);
		}
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(selectedElements, new Runnable() {
			@Override
			public void run() {
				doUndo();
			}
		});
	}
	
	public void doUndo() {
		for (PeriodicTemporalConstraint bound : bounds) {
			ConstraintUtils.attachConstraint(bound);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(UnpinOperation.class.getSimpleName());
		builder.append(":");
		builder.append(selectedElements);
		builder.append(" from ");
		builder.append(String.valueOf(bounds));
		return builder.toString();
	}
	
}
