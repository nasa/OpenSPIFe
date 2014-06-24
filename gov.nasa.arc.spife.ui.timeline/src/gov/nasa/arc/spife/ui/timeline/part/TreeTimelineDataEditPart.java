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
/*
 * Created on Mar 15, 2005
 */
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class TreeTimelineDataEditPart<T> extends AbstractTimelineDataEditPart<T> implements IPropertyChangeListener {

	public static final String LAYER_DATA_ROWS_LAYER = "Data Rows Layer";
	
	private Color alternatingColor;
	private Color alternatingBorderColor;
	private Color horizontalLineColor;
	private boolean isHorizontalLinesHidden;
	
	private Listener listener = new Listener();

    public TreeTimelineDataEditPart() {
		createAlternatingColors();
		createHorizontalLineColor();
		setHideHorizontalLinesCheck();
	}
	
	@Override
	protected List getModelChildren() {
		List<Object> children = new ArrayList<Object>();
		children.addAll(getModelChildren(getModel()));
		if (children.size() == 0) {
			children.add(getModel());
		}
		TimelineMarkerManager markerManager = CommonUtils.getAdapter(getViewer(), TimelineMarkerManager.class);
		if (markerManager != null) {
			children.add(markerManager);
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
    
	@Override
	protected void createLayers(LayeredPane layeredPane) {
		Layer rowsLayer = new Layer();
		rowsLayer.setLayoutManager(new XYLayout());
		rowsLayer.setOpaque(false);
		layeredPane.add(rowsLayer, LAYER_DATA_ROWS_LAYER);
		getViewer().registerLayer(LAYER_DATA_ROWS_LAYER, rowsLayer);
		super.createLayers(layeredPane);
	}
	
	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			TIMELINE_PREFERENCES.addPropertyChangeListener(this);
			TimelineViewer viewer = getViewer();
			viewer.addPropertyChangeListener(listener);
			if (viewer.isAnimated()) {
				figure.addLayoutListener(LayoutAnimator.getDefault());
			}
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
			TIMELINE_PREFERENCES.removePropertyChangeListener(this);
			TimelineViewer viewer = getViewer();
			viewer.removePropertyChangeListener(listener);
			if (viewer.isAnimated()) {
				figure.removeLayoutListener(LayoutAnimator.getDefault());
			}
			ExpansionModel expansion = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
			if (expansion != null) {
				expansion.removePropertyChangeListener(listener);
			}
			if (alternatingColor != null) {
				//alternatingColor.dispose();
				alternatingColor = null;
			}
			if (alternatingBorderColor != null) {
				alternatingBorderColor = null;
			}
			if (horizontalLineColor != null) {
				horizontalLineColor = null;
			}
			TreeTimelineContentProvider cp = getViewer().getTreeTimelineContentProvider();
			if (cp != null) {
				cp.removeListener(listener);
			}
			super.deactivate();
		}
	}

	@Override
	protected Layer createPrimaryFigure() {
		Layer primaryLayer = new Layer();
		primaryLayer.setOpaque(false);
		primaryLayer.setLayoutManager(new ToolbarLayout(false));
		primaryLayer.setBackgroundColor(ColorConstants.white);
		return primaryLayer;
	}

	@Override
	public IFigure getContentPane() {
		return getLayer(LAYER_DATA_PRIMARY_LAYER);
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	}

	private void createAlternatingColors() {
		alternatingColor = ColorMap.RGB_INSTANCE.getColor(StringConverter.asRGB(TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_ALTERNATING_COLOR)));

		final RGB rgbBase = alternatingColor.getRGB();
		final float hsb[] = ColorUtils.getHSB(rgbBase);
		final float h = hsb[0];
		final float s = hsb[1];
		final float b = hsb[2];
		alternatingBorderColor = ColorMap.RGB_INSTANCE.getColor(new RGB(h, s, b*0.8f));
	}

	private void createHorizontalLineColor() {
		horizontalLineColor = ColorMap.RGB_INSTANCE.getColor(StringConverter.asRGB(TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_HORIZONTAL_LINES_COLOR)));
	}

	private void setHideHorizontalLinesCheck() {
		isHorizontalLinesHidden = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_HORIZONTAL_LINES_HIDE);
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
			child.setBorder(new LineBorder(alternatingBorderColor, 1) {

				@Override
				public void paint(IFigure figure, Graphics graphics, Insets insets) {
					tempRect.setBounds(getPaintRectangle(figure, insets));
					if (getWidth() % 2 == 1) {
						tempRect.width--;
						tempRect.height--;
					}
					tempRect.shrink(getWidth() / 2, getWidth() / 2);
					graphics.setLineWidth(getWidth());
					// Set the timeline horizontal line color
					if(horizontalLineColor != null) {
						graphics.setForegroundColor(horizontalLineColor);
						graphics.setBackgroundColor(horizontalLineColor);
					}
					else if (getColor() != null) {
						graphics.setForegroundColor(getColor());
						graphics.setBackgroundColor(getColor());
					}
					if(!isHorizontalLinesHidden) {
						// The timeline horizontal grid line
						graphics.drawLine(tempRect.getBottomLeft(), tempRect.getBottomRight());
					}
				}
				
			});
		}
	}

	@Override
	public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
		if (event.getProperty().equals(TimelinePreferencePage.P_ALTERNATING_COLOR)) {
			createAlternatingColors();
			refreshVisuals();
		}
		else if (event.getProperty().equals(TimelinePreferencePage.P_HORIZONTAL_LINES_COLOR)) {
			createHorizontalLineColor();
			refreshVisuals();
		}
		else if (event.getProperty().equals(TimelinePreferencePage.P_HORIZONTAL_LINES_HIDE)) {
			setHideHorizontalLinesCheck();
			refreshVisuals();
		}
		else if (event.getProperty().equals(TimelinePreferencePage.P_VERTICAL_LINES_COLOR)) {
			refreshVisuals();
		}
	}
	
	private class Listener implements PropertyChangeListener, TreeTimelineContentProvider.Listener {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String p = evt.getPropertyName();
			Timeline timeline = TimelineUtils.getTimeline(TreeTimelineDataEditPart.this);
			if (timeline == null) {
				return;
			}
			if (ExpansionModel.EXPANDED.equals(p)) {
				refreshInDisplayThread();
				timeline.layoutTimelineContentInDisplayThread();				
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
				timeline.layoutTimelineContentInDisplayThread();
			}
		}
		
		@Override
		public void contentRefresh(Set<? extends Object> element) {
			refresh();
		}
		
		@Override
		public void labelUpdate(Set<? extends Object> element) {
			// do nothing
		}
		
	}
    
}
