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

import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

public class CreateTemporalBoundOperation extends AbstractTransactionUndoableOperation {

	private final PeriodicTemporalConstraint constraint;

	public CreateTemporalBoundOperation(PeriodicTemporalConstraint temporalBound) {
		super("create constraint");
		this.constraint = temporalBound;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}
	
	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(constraint.getPoint().getElement(), new Runnable() {
			@Override
			public void run() {
				ConstraintUtils.attachConstraint(constraint);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(constraint.getPoint().getElement(), new Runnable() {
			@Override
			public void run() {
				ConstraintUtils.detachConstraint(constraint);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(CreateTemporalBoundOperation.class.getSimpleName());
		builder.append(":");
		builder.append("constraint=" + constraint);
		return builder.toString();
	}

}
