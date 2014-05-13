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
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineHeaderRowEditPart;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

public class PlanSectionHeaderRowEditPart extends TreeTimelineHeaderRowEditPart<PlanSectionRow> {
	
	protected static final Image IMAGE_CLOSE = WidgetPlugin.getDefault().getImageRegistry().get(WidgetPlugin.KEY_IMAGE_CLOSE_NICE);
	
	@Override
	protected void addButtons(Figure figure) {
		final PlanSectionRow row = getModel();
		EditingDomain domain = ((PlanTimeline)getTimeline()).getEditingDomain();
		Command command = RemoveCommand.create(domain, row.eContainer(), PlanTimelinePackage.Literals.PLAN_SECTION__ROWS, row);
		if (command.canExecute()) {
			Label closeLabel = null;
			closeLabel = new Label();
			closeLabel.setIcon(IMAGE_CLOSE);
			closeLabel.addMouseListener(new MouseListener.Stub() {
				@Override
				public void mousePressed(MouseEvent me) {
					EditingDomain domain = ((PlanTimeline)getTimeline()).getEditingDomain();
					Command command = RemoveCommand.create(domain, row.eContainer(), PlanTimelinePackage.Literals.PLAN_SECTION__ROWS, row);
					EMFUtils.executeCommand(domain, command);
					getViewer().setSelection(new StructuredSelection());
				}
			});
			figure.add(closeLabel);
			super.addButtons(figure);
		}
	}

}
