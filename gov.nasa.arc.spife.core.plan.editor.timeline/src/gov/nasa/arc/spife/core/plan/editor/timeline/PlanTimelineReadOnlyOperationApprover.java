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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.plan.editor.PlanReadOnlyOperationApprover;
import gov.nasa.ensemble.core.plan.editor.SetExpandedOperation;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;

public class PlanTimelineReadOnlyOperationApprover extends PlanReadOnlyOperationApprover {

	public PlanTimelineReadOnlyOperationApprover() {
		super();
	}
	
	@Override
	protected IStatus checkReadOnly(IUndoableOperation operation) {
		if (isZoomOperation(operation) || isExpandedOperation(operation)) {
			return Status.OK_STATUS;
		}
		return super.checkReadOnly(operation);
	}
	
	private boolean isZoomOperation(IUndoableOperation operation) {
		if (operation instanceof CommandUndoableOperation) {
			Command command = ((CommandUndoableOperation)operation).getCommand();
			if (command instanceof SetCommand && ((SetCommand)command).getFeature() == TimelinePackage.Literals.PAGE__ZOOM_OPTION) {
				return true;
			}
		}
		return false;
	}

	private boolean isExpandedOperation(IUndoableOperation operation) {
		if (operation instanceof SetExpandedOperation) {
			return true;
		}
		if (operation instanceof FeatureTransactionChangeOperation) {
			FeatureTransactionChangeOperation featureChange = (FeatureTransactionChangeOperation)operation;
			return (featureChange.getObject() instanceof CommonMember 
					&& featureChange.getFeature() == PlanPackage.Literals.COMMON_MEMBER__EXPANDED);
		}
		return false;
	}
}
