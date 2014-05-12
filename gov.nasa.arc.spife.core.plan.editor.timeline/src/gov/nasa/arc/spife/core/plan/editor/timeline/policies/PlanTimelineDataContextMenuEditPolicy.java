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

import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.ui.editor.AbstractEnsembleEditorPart;
import gov.nasa.ensemble.common.ui.gef.ContextMenuEditPolicy;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

public class PlanTimelineDataContextMenuEditPolicy extends ContextMenuEditPolicy {

	@Override
	public void buildContextMenu(Point point, IMenuManager menu) {
		EditPartViewer viewer = getHost().getViewer();
		if (viewer instanceof TimelineViewer) {
			TimelineViewer timelineViewer = (TimelineViewer) viewer;
			IWorkbenchPart workbenchPart = timelineViewer.getSite().getPart();
			if (workbenchPart instanceof AbstractEnsembleEditorPart) {
				AbstractEnsembleEditorPart editorPart = (AbstractEnsembleEditorPart) workbenchPart;
				editorPart.fillContextMenu(menu);
			}
		}
	}

}
