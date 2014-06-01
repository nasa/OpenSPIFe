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
package gov.nasa.ensemble.core.plan.temporal;

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.command.SetCommand;

public class ScheduledOperation extends AbstractTransactionUndoableOperation {
	
	private final Command command;
	private final EPlanElement planElement;
	private final boolean scheduled;

	private ScheduledOperation(EPlanElement planElement, boolean scheduled) {
		super("scheduled");
		this.planElement = planElement;
		this.scheduled = scheduled;
		TemporalMember member = planElement.getMember(TemporalMember.class);
		EAttribute feature = TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED;
		command = SetCommand.create(TransactionUtils.getDomain(planElement), member, feature, scheduled);
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do in any case
	}

	@Override
	protected boolean isExecutable() {
		return command.canExecute();
	}
	
	@Override
	protected void execute() {
		TransactionUtils.writing(planElement, new Runnable() {
			@Override
			public void run() {
				command.execute();
			}
		});
	}
	
	@Override
	protected boolean isUndoable() {
		return command.canUndo();
	}
	
	@Override
	protected void undo() {
		TransactionUtils.writing(planElement, new Runnable() {
			@Override
			public void run() {
				command.undo();
			}
		});
	}

	@Override
	protected boolean isRedoable() {
		return command.canExecute();
	}
	
	@Override
	protected void redo() {
		TransactionUtils.writing(planElement, new Runnable() {
			@Override
			public void run() {
				command.redo();
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(ScheduledOperation.class.getSimpleName());
		builder.append(":");
		builder.append(planElement.getName());
		builder.append(" scheduled to ");
		builder.append(String.valueOf(scheduled));
		return builder.toString();
	}
	
	public static void toggleScheduledness(EPlanElement element) {
		IPlanEditApprover registry = PlanEditApproverRegistry.getInstance();
		if (!registry.canModify(element)) {
			return;
		}
		TriState oldValue = SpifePlanUtils.getScheduled(element);
		boolean value = (oldValue == TriState.FALSE);
		ScheduledOperation op = new ScheduledOperation(element, value);
		IUndoContext undoContext = TransactionUtils.getUndoContext(element);
		op.addContext(undoContext);
		try {
			OperationHistoryFactory.getOperationHistory().execute(op, null, null);
		} catch (ExecutionException e) {
			// should never occur
			LogUtil.error(e);
		}
	}

}
