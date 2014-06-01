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
package gov.nasa.arc.spife.ui.timeline.chart.part;

import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.part.MarkerManagerEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TimelineMarkerEditPart;

import java.util.NoSuchElementException;

import org.apache.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class LineChartTimelineFactory implements EditPartFactory {
	
	public static final Logger trace = Logger.getLogger(LineChartTimelineFactory.class);
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (context == null) {
			editPart =  new ChartEditPart();
		} else if (model instanceof Plot && context instanceof ChartDataEditPart) {
			editPart = new LineDataEditPart();
		} else if (model instanceof AmountLine && context instanceof ChartDataEditPart) {
			editPart = new AmountLineEditPart();
		} else if (model instanceof Plot && context instanceof ChartHeaderEditPart) {
			editPart = getPlotHeaderEditPart();
		}
		else if (model instanceof TimelineMarkerManager) 	editPart = new MarkerManagerEditPart();
		else if (model instanceof TimelineMarker) 	 		editPart = new TimelineMarkerEditPart();
		
		if(editPart == null) {
			throw new NoSuchElementException (
					"Can't create part for model element: "
					+ ((model != null) ? model.getClass().getName() : "null"));
		} else {
			editPart.setModel(model);
		}
		
		return editPart;
	}
	
	protected EditPart getPlotHeaderEditPart() {
		return new PlotHeaderEditPart();
	}
}
