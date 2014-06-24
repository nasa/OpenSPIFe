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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.commands.TimelineKeyHandler;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class PlanTimelineViewer extends TimelineViewer implements PlanTimelineConstants, IPropertyChangeListener {
	
	public PlanTimelineViewer(PlanTimeline timeline) {
		super(timeline);
		PLAN_TIMELINE_PREFERENCES.addPropertyChangeListener(this);
		setKeyHandler(new TimelineKeyHandler());
		
		setProperty(PlanTimelineConstants.PLAN_CONSTRAINTS_VISIBLE, PLAN_TIMELINE_PREFERENCES.getBoolean(PlanTimelineConstants.PLAN_CONSTRAINTS_VISIBLE));
		setProperty(ExpansionModel.ID, new PlanExpansionModel(timeline));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		PLAN_TIMELINE_PREFERENCES.removePropertyChangeListener(this);
	}

	public PlanEditorModel getPlanEditorModel() {
		return getTimeline().getPlanEditorModel();
	}
	
	@Override
	public PlanTimeline getTimeline() {
		return (PlanTimeline) super.getTimeline();
	}

	public EPlan getPlan() {
		return getTimeline().getPlan();
	}
	
	/**
	 * Sets the tolerance the edit part will use to snap to the start/end
	 * of a neighboring edit part.  Setting the tolerance to 0 will disable snap.
	 * @param pixels in pixels
	 */
	public int getSnapToTolerance() {
		return getTimeline().getSnapToTolerance();
	}
	
	@Override
	protected Set<EditPart> getSelectableEditParts(Set<Object> models) {
		Set<EditPart> editParts = super.getSelectableEditParts(models);
		//
		// Filter the edit parts that can be "layed out"
		return getLayedOutEditParts(editParts);
	}

	protected Set<EditPart> getLayedOutEditParts(Set<EditPart> editParts) {
		ExpansionModel expansionModel = (ExpansionModel) getProperty(ExpansionModel.ID);
		if (expansionModel == null) {
			return editParts;
		}
		LinkedHashSet<EditPart> result = new LinkedHashSet<EditPart>();
		if(expansionModel != null) {				
			for(EditPart editPart: editParts) {
				Object model = editPart.getModel();
				// get the activity group 
				EPlanElement eContainer = null;
				if (model instanceof EPlanChild) {
					eContainer = ((EPlanChild)model).getParent();
				}
				// it's a resource, cannot be layed out.
				else if (model instanceof EObject) {
					result.add(editPart);
				}
				if (expansionModel.isExpanded(eContainer) || eContainer instanceof EPlanParent) {
					result.add(editPart);
				}
			}		
		}
		return result;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		Object property = event.getProperty();
		if(property.equals(PLAN_CONSTRAINTS_VISIBLE)) {
			setProperty((String) property, event.getNewValue());
		}
	}
	
}
