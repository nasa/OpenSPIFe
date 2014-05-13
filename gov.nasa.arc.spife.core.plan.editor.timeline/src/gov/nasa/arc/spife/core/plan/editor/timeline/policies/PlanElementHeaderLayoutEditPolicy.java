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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanSectionHeaderRowEditPart;
import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineOrderedLayoutEditPolicy;
import gov.nasa.ensemble.common.ui.gef.OperationCommand;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

public class PlanElementHeaderLayoutEditPolicy extends TimelineOrderedLayoutEditPolicy {
	
	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		if (child instanceof PlanSectionHeaderRowEditPart) {
			PlanSectionRow row = (PlanSectionRow) child.getModel();
			if ( row == null ) return null;
			PlanSection section = (PlanSection) row.eContainer();
      if ( section == null ) return null;
			int index = section.getRows().size() - 1;
			if (after != null) {
				Object plot = after.getModel();
				index = section.getRows().indexOf(plot);
				int originalIndex = section.getRows().indexOf(child.getModel());
				if (originalIndex < index) {
					index--;
				}
			}
			EditingDomain domain = EMFUtils.getAnyDomain(row);
			MoveCommand command = (MoveCommand) MoveCommand.create(domain, section, PlanTimelinePackage.PLAN_SECTION__ROWS, row, index);
			CommandUndoableOperation op = new CommandUndoableOperation(domain, command);
			TimelineViewer viewer = (TimelineViewer)child.getViewer();
			return new OperationCommand(EMFUtils.getUndoContext(domain), op, viewer.getControl(), viewer.getSite()); 
		}
		return null;
	}
	
}
