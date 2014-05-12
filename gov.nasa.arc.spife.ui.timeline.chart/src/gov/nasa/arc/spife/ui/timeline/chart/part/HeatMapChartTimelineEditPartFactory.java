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
/**
 * 
 */
package gov.nasa.arc.spife.ui.timeline.chart.part;

import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartHeaderLayoutEditPolicy;
import gov.nasa.arc.spife.ui.timeline.part.EMFTimelineEditPartFactory;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataRowEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineHeaderEditPart;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

public class HeatMapChartTimelineEditPartFactory extends EMFTimelineEditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart ep = null;
		if (context == null) {
			ep = new ChartTimelineEditPart();
		} else if (context instanceof TreeTimelineHeaderEditPart && model instanceof Plot) {
			ep = new PlotHeaderEditPart();
		} else if (context instanceof TreeTimelineDataRowEditPart && model instanceof Plot) {
			ep = new HeatMapDataEditPart();
		}
		
		if (ep != null) {
			ep.setModel(model);
			return ep;
		}
		ep = super.createEditPart(context, model);
		ep.installEditPolicy(DropEditPolicy.DROP_ROLE, new ChartDropEditPolicy());
		return ep;
	}

	private final class ChartTimelineEditPart extends TreeTimelineEditPart<EObject> {
		
		@Override
		public GraphicalEditPart createHeaderEditPart(EObject model) {
			GraphicalEditPart ep = new TreeTimelineHeaderEditPart<EObject>() {

				@Override
				protected void createEditPolicies() {
					super.createEditPolicies();
					installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChartHeaderLayoutEditPolicy());
				}
				
			};
			ep.setModel(model);
			return ep;
		}
		
	}
	
}
