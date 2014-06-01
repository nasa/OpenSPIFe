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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;

public class TreeTimelineHeaderEditPart<T> extends TimelineViewerEditPart<T> {

	private Listener listener = new Listener();
	
	private Color alternatingColor = null;
	
	public TreeTimelineHeaderEditPart() {
		super();
		alternatingColor = createAlternatingColor();
	}
	
	@Override
	protected List getModelChildren() {
		List<Object> children = new ArrayList<Object>();
		children.addAll(getModelChildren(getModel()));
		if (children.size() == 0) {
			children.add(getModel());
		}
		return children;
	}
	
	private Collection<? extends Object> getModelChildren(Object model) {
		return TimelineUtils.getModelChildren(getViewer(), this, model);
	}

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			getViewer().addPropertyChangeListener(listener);
			TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
			ExpansionModel expansion = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
			if (expansion != null) {
				expansion.addPropertyChangeListener(listener);
			}
			TreeTimelineContentProvider cp = getViewer().getTreeTimelineContentProvider();
			if (cp != null) {
				cp.addListener(listener);
			}
			refreshVisuals();
		}
	}
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			getViewer().removePropertyChangeListener(listener);
			TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
			ExpansionModel expansion = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
			if (expansion != null) {
				expansion.removePropertyChangeListener(listener);
			}
			if (alternatingColor != null) {
				//alternatingColor.dispose();
				alternatingColor = null;
			}
			TreeTimelineContentProvider cp = getViewer().getTreeTimelineContentProvider();
			if (cp != null) {
				cp.removeListener(listener);
			}
		}
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new Figure();
		ToolbarLayout layout = new ToolbarLayout(false);
		layout.setStretchMinorAxis(true);
		figure.setLayoutManager(layout);
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	}
	
	@Override
	protected void refreshVisuals() {
		updateChildVisuals(0);
		super.refreshVisuals();
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		super.addChildVisual(childEditPart, index);
		updateChildVisuals(index);
	}
	
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		super.removeChildVisual(childEditPart);
		int index = getContentPane().getChildren().indexOf(childEditPart);
		if (index == -1) {
			index = 0;
		}
		updateChildVisuals(index);
	}

	private void updateChildVisuals(int index) {
		List children = getContentPane().getChildren();
		for (int i=index; i<children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
			if (i%2 == 0) child.setBackgroundColor(ColorConstants.white);
			else		  child.setBackgroundColor(alternatingColor);
		}
	}

	private Color createAlternatingColor() {
		return ColorMap.RGB_INSTANCE.getColor(StringConverter.asRGB(TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_ALTERNATING_COLOR)));
	}
	
	private class Listener implements PropertyChangeListener, IPropertyChangeListener, TreeTimelineContentProvider.Listener {

		@Override
		public void contentRefresh(Set<? extends Object> elements) {
			refresh();
			Timeline timeline = TimelineUtils.getTimeline(TreeTimelineHeaderEditPart.this);
			if(timeline != null) {
				timeline.layoutTimelineContentInDisplayThread();
			}
		}
		
		@Override
		public void labelUpdate(Set<? extends Object> elements) {
			// do nothing
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String p = evt.getPropertyName();
			if (ExpansionModel.EXPANDED.equals(p)) {
				refreshInDisplayThread();
			} else if (TimelineViewer.CONTENT_PROVIDER.equals(p)) {
				TreeTimelineContentProvider oldValue = (TreeTimelineContentProvider) evt.getOldValue();
				if (oldValue != null) {
					oldValue.removeListener(this);
				}
				
				TreeTimelineContentProvider newValue = (TreeTimelineContentProvider) evt.getNewValue();
				if (newValue != null) {
					newValue.addListener(this);
				}
				refreshInDisplayThread();
			} else if (TIMELINE_GROUP_ELEMENTS.equals(p)) {
				refreshInDisplayThread();
			}
		}
		
		@Override
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			String p = event.getProperty();
			if (TimelinePreferencePage.P_ALTERNATING_COLOR.equals(p)) {
				if (alternatingColor != null) {
					//alternatingColor.dispose();
				}
				alternatingColor = createAlternatingColor();
				refreshVisuals();
			}
		}
		
	}
	
	
}
