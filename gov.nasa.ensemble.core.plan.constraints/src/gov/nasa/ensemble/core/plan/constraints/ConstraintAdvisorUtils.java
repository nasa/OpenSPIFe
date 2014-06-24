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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalBoundOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.UnchainOperation;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.resource.ImageDescriptor;

public class ConstraintAdvisorUtils {

	public static Suggestion createRemoveConstraintSuggestion(PeriodicTemporalConstraint constraint) {
		String description = "Remove the constraint";
		IUndoableOperation operation = new DeleteTemporalBoundOperation(constraint);
		ImageDescriptor icon = null;
		ConstraintPoint point = constraint.getPoint();
		Object anchorElement = point.getAnchorElement();
		if (PinUtils.isPinConstraint(constraint)) {
			icon = ConstraintsPlugin.getImageDescriptor("icons/delete_pin.png");
		} else if (Timepoint.START == anchorElement) {
			icon = ConstraintsPlugin.getImageDescriptor("icons/delete_earliest.png");
		} else if (Timepoint.END == anchorElement) {
			icon = ConstraintsPlugin.getImageDescriptor("icons/delete_latest.png");
		}
		return new Suggestion(icon, description, operation);
	}

	public static Suggestion createRemoveConstraintSuggestion(BinaryTemporalConstraint constraint) {
		String description = "Remove the constraint";
		IUndoableOperation operation = new DeleteTemporalRelationOperation(constraint);
		ImageDescriptor icon = ConstraintsPlugin.getImageDescriptor("icons/delete_constraint.png");
		return new Suggestion(icon, description, operation);
	}

	public static Suggestion createRemoveChainSuggestion(TemporalChain chain) {
		if (chain == null) {
			return null;
		}
		String description = "Remove the chain";
		IUndoableOperation operation = new UnchainOperation(chain.getElements());
		ImageDescriptor icon = ConstraintsPlugin.getImageDescriptor("icons/delete_constraint.png");
		return new Suggestion(icon, description, operation);
	}
	
}
