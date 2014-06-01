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

import gov.nasa.arc.spife.core.plan.editor.timeline.TimelinePlugin;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.TemporalBoundLink;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.TemporalChainLink;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.part.DefaultConnectionEditPart;
import gov.nasa.arc.spife.ui.timeline.part.MarkerManagerEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TimelineMarkerEditPart;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataRowEditPart;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;

import java.util.NoSuchElementException;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.swt.graphics.Image;

public class PlanTimelineEditPartFactory implements EditPartFactory {
	
	private static final Image IMAGE_CHAIN_LINK = TimelinePlugin.getDefault().getIcon("chain-link.gif");
	private static final Image IMAGE_TEMPORAL_RELATION = TimelinePlugin.getDefault().getIcon("constraint_present_icon.gif");
	
	/**
	 * PlanTimelineEditPart 					TimelineModel
	 * 
	 *  +- PlanTimelineHeaderEditPart			  +- Plan
	 *      +- PlanElementRowEditPart			      +- ActivityGroup
	 *          +- PlanElementRowEditPart				  +- Activity
	 *              +- PlanElementRowEditPart			      +- SubActivity
	 *              
	 *  +- PlanTimelineDataEditPart				  +- Plan
	 *      +- PlanElementNestingEditPart			  +- ActivityGroup
	 *          +- RowData								  +- ActivityGroup
	 *              +- TemporalNodeEditPart					  +- ActivityGroup
	 *          +- PlanElementNestingEditPart		      +- Activity
	 *              +- RowData								  +- Activity
	 *                  +- TemporalNodeEditPart					  +- Activity
	 *              +- PlanElementNestingEditPart			  +- SubActivity
	 *                  +- RowData								  +- SubActivity
	 *                      +- TemporalNodeEditPart					  +- SubActivity
	 *      
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		//
		// Is top level edit part
		if (model instanceof EPlan && context == null) {
			editPart = new PlanTimelineEditPart();
		//
		// Is top level data edit part
		} else if (context instanceof PlanTimelineDataEditPart && model instanceof EPlanElement) {
			editPart = new PlanTimelineDataRowEditPart();
		//
		// Is top level header edit part
		} else if (context instanceof PlanTimelineHeaderEditPart && model instanceof EPlanElement) {
			editPart = new PlanElementRowHeaderEditPart();
		//
		// Is top level data row edit part for PlanElement models
		} else if (context instanceof TreeTimelineDataRowEditPart && model instanceof EPlanElement) {
			editPart = new TemporalNodeEditPart();
		//
		// Is top level data row edit part for Section models
		} else if (context instanceof PlanTimelineDataEditPart && model instanceof PlanSectionRow) {
			editPart = new PlanTreeTimelineDataRowEditPart<PlanSectionRow>();
		//
		// Is top level header row edit part for Section models
		} else if (context instanceof PlanTimelineHeaderEditPart && model instanceof PlanSectionRow) {
			editPart = new PlanSectionHeaderRowEditPart();
		} else if (model instanceof BinaryTemporalConstraint)  {
			editPart = new TemporalRelationConnectionEditPart(IMAGE_TEMPORAL_RELATION);
		} else if (model instanceof TemporalChainLink) {
			editPart = new DefaultConnectionEditPart(IMAGE_CHAIN_LINK);
		}
		else if (model instanceof PeriodicTemporalConstraint) editPart = new TemporalBoundEditPart();
		else if (model instanceof TemporalBoundLink) 		editPart = new TemporalBoundConnectionEditPart();
		else if (model instanceof TimelineMarkerManager) 	editPart = new MarkerManagerEditPart();
		else if (model instanceof TimelineMarker) 	 		editPart = new TimelineMarkerEditPart();
		
		if(editPart == null) {
			throw new NoSuchElementException (
					"Can't create part for model element: "
					+ (((model != null) ? model.getClass().getName() : "null")
					+ " in context "+context));
		} // else...
		editPart.setModel(model);
		return editPart;
	}

}
