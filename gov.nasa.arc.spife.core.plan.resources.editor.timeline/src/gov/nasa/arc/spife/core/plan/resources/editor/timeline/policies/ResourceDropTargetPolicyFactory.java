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
package gov.nasa.arc.spife.core.plan.resources.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.part.ChartDataEditPart;
import gov.nasa.arc.spife.ui.timeline.chart.part.ChartHeaderEditPart;
import gov.nasa.arc.spife.ui.timeline.policy.EditPolicyFactory;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;

import org.eclipse.gef.EditPart;

public class ResourceDropTargetPolicyFactory implements EditPolicyFactory {

	@Override
	public void installEditPolicy(EditPart editPart) {
		if (!(editPart.getViewer() instanceof PlanTimelineViewer)) {
			return;
		}
		if (editPart instanceof ChartDataEditPart
				|| editPart instanceof ChartHeaderEditPart) {
			editPart.installEditPolicy(DropEditPolicy.DROP_ROLE, new ResourceDropTargetPolicy());
		}

	}

}
