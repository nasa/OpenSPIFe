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
package gov.nasa.arc.spife.ui.timeline.chart.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineOrderedLayoutEditPolicy;
import gov.nasa.ensemble.common.ui.gef.OperationCommand;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;

import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

public class ChartHeaderLayoutEditPolicy extends TimelineOrderedLayoutEditPolicy {
	
	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		Chart chart = (Chart) getHost().getModel();
		int index = chart.getPlots().size() - 1;
		if (after != null) {
			Object plot = after.getModel();
			index = chart.getPlots().indexOf(plot);
			int originalIndex = chart.getPlots().indexOf(child.getModel());
			if (originalIndex < index) {
				index--;
			}
		}
		EditingDomain domain = EMFUtils.getAnyDomain(chart);
		MoveCommand command = (MoveCommand) MoveCommand.create(domain, chart, ChartPackage.Literals.CHART__PLOTS, child.getModel(), index);
		CommandUndoableOperation op = new CommandUndoableOperation(domain, command);
		TimelineViewer viewer = (TimelineViewer)child.getViewer();
		return new OperationCommand(EMFUtils.getUndoContext(domain), op, viewer.getControl(), viewer.getSite()); 
	}
	
}
