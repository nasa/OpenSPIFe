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
package gov.nasa.arc.spife.ui.timeline.part;


import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;

import java.util.NoSuchElementException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class EMFTimelineEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart ep = null;
		if (context == null) {
			ep = new TreeTimelineEditPart<EObject>();
		} else if (model instanceof TimelineMarkerManager) {
			ep = new MarkerManagerEditPart();
		} else if (model instanceof TimelineMarker) {
			ep = new TimelineMarkerEditPart();
		} else if (model instanceof TimelineMarkerManager) {
			ep = new MarkerManagerEditPart();
		} else if (model instanceof TimelineMarker) {
			ep = new TimelineMarkerEditPart();
		} else if (context instanceof TreeTimelineDataEditPart) {
			ep = new TreeTimelineDataRowEditPart();
		} else if (context instanceof TreeTimelineHeaderEditPart) {
			ep = new TreeTimelineHeaderRowEditPart();
		} else if (context instanceof TreeTimelineDataRowEditPart) {
			ep = new EMFTimelineNodeEditPart();
		}
		
		if (ep == null) {
			throw new NoSuchElementException (
					"Can't create part for model element: "
					+ (((model != null) ? model.getClass().getName() : "null")
					+ " in context "+context));
		}
		ep.setModel(model);
		return ep;
	}
	
}
