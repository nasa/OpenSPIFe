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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.FigureStyle;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.arc.spife.ui.timeline.figure.RowDataFigureLayoutData;
import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.arc.spife.ui.timeline.policy.MarchingAntsSelectionEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.util.IPropertyChangeListener;

public abstract class TreeTimelineNodeEditPart<T> extends TimelineViewerEditPart<T>  {
	
	private final Listener listener = new Listener();
	
	public TreeTimelineNodeEditPart() {
		// default constructor
	}

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
	protected FigureStyle getDefaultBarFigureStyle() {
		return FigureStyle.SOLID;
	}
	
	public abstract TemporalExtent getTemporalExtent();
	
	public Page getPage() {
		if (getTimeline() != null) {
			return getTimeline().getPage();
		}
		return null;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new MarchingAntsSelectionEditPolicy());
	}
	
	@Override
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_OPEN) {
			final ExpansionModel model = (ExpansionModel) getViewer().getProperty(ExpansionModel.ID);
			if (model != null) {
				GEFUtils.runLaterInDisplayThread(this, new Runnable() {
					@Override
					public void run() {
						T model2 = getModel();
						Set<Object> objects = Collections.<Object>singleton(model2);
						model.setExpanded(objects, !model.isExpanded(model2));
					}
				});
			}
		}
		super.performRequest(req);
	}

	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			getPage().eAdapters().add(listener);
			getViewer().getTimelineSectionModel().eAdapters().add(listener);
			TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		}
		updateFigureBounds();
	}
	
	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			getPage().eAdapters().remove(listener);
			getViewer().getTimelineSectionModel().eAdapters().remove(listener);
			TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
			super.deactivate();
		}
	}
	
	/**
	 * Returns a <code>List</code> containing the children
	 * model objects. If this EditPart's model is a container, this method should be
	 * overridden to returns its children. This is what causes children EditParts to be
	 * created.
	 * <P>
	 * Callers must not modify the returned List. Must not return <code>null</code>.
	 * @return the List of children
	 */
	@Override
	protected List getModelChildren() {
		return Collections.emptyList();
	}
	
	/*
	 * NOTE: It is very important that this method does not
	 * become a 'catch-all' for every conceivable visual update.
	 * The methodology employed should check the origin of the 
	 * event, decode it into the relevant info for the figure, 
	 * invalidate *ONLY THE RELEVANT FIGURE BOUNDS* that correspond
	 * to the change, and then call refresh which repaints according
	 * to the invalid areas.
	 */
	@Override
	protected void refreshVisuals() {
		figure.repaint();
	}

	/*
	 * Figure related methods
	 */
	public BarFigure getCastedFigure() {
		return (BarFigure) getFigure();
	}
	
	@Override
	protected IFigure createFigure() {
		BarFigure f = new BarFigure();
		f.setLayoutManager(new XYLayout());
		f.setRound(TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_ROW_ELEMENT_ROUNDED));
		f.setPaintBorder(TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_ROW_ELEMENT_BORDER));
		return f;
	}
	
	/*
	 * Bounds computing functions 
	 */
	public void updateFigureBounds() {
		final TemporalExtent extent = getTemporalExtent();
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				IFigure parent = figure.getParent();
				RowDataFigureLayoutData data = (RowDataFigureLayoutData) parent.getLayoutManager().getConstraint(figure);
				if (data == null) {
					data = new RowDataFigureLayoutData();
				}
				data.start = extent == null ? null : extent.getStart();
				data.end = extent == null ? null : extent.getEnd();
				long duration = 0;
				if (extent != null) {
					duration = extent.getDurationMillis();
				}
				long width = getPage().convertToPixels(duration);
				data.instantaneous = width < 2 && getViewer().getProperty(P_DISPLAY_INSTANTANEOUS) == Boolean.TRUE;
				if (parent != null) {
					parent.setConstraint(figure, data);
					updateChildBounds();
				}
			}
		});
	}
	
	protected void updateChildBounds() {
		// no children defined
	}
	
	public class Listener extends AdapterImpl implements IPropertyChangeListener {

		@Override
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			final String property = event.getProperty();
			if (TimelinePreferencePage.P_ROW_ELEMENT_ROUNDED.equals(property)) {
				GEFUtils.runInDisplayThread(TreeTimelineNodeEditPart.this, new Runnable() {
					@Override
					public void run() {
						getCastedFigure().setRound(TIMELINE_PREFERENCES.getBoolean(property));
						updateChildBounds();
					}
				});
			} 
			else if (TimelinePreferencePage.P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS.equals(property)) {
				GEFUtils.runInDisplayThread(TreeTimelineNodeEditPart.this, new Runnable() {
					@Override
					public void run() {
						updateChildBounds();
					}
				});
			}else if (TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT.equals(property)) {
				updateFigureBounds();
			} else if (TimelinePreferencePage.P_ROW_ELEMENT_BORDER.equals(property)) {
				GEFUtils.runInDisplayThread(TreeTimelineNodeEditPart.this, new Runnable() {
					@Override
					public void run() {
						getCastedFigure().setPaintBorder(TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_ROW_ELEMENT_BORDER));
					}
				});
			}
		}
	
		@Override
		public void notifyChanged(Notification notification) {
			Object f = notification.getFeature();
			if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
					|| TimelinePackage.Literals.PAGE__DURATION == f
					|| TimelinePackage.Literals.PAGE__START_TIME == f
					|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == f
					|| TimelinePackage.Literals.GANTT_SECTION__ROW_HEIGHT == f
					|| TimelinePackage.Literals.TIMELINE_CONTENT__ROW_HEIGHT == f) {
				updateFigureBounds();
			}
		}
		
	}

}
