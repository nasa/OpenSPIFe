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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.RowDataFigureLayout;
import gov.nasa.arc.spife.ui.timeline.policy.RowDataBackgroundEditPolicy;
import gov.nasa.arc.spife.ui.timeline.policy.TickIntervalsEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutAnimator;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class TreeTimelineDataRowEditPart<T> extends TimelineViewerEditPart<T> {

	private final MarqueeDragTracker marqueeDragTracker = new MarqueeDragTracker();
	private Listener listener = new Listener();

	public TreeTimelineDataRowEditPart() {
		super();
	}
	
	/**
	 * Returns true. Should not really be selectable, but otherwise the drag 
	 * tracker is not called and sweep select does not work.
	 */
	@Override
	public boolean isSelectable() {
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected List getModelChildren() {
		List list = new ArrayList();
		T model = getModel();
		if (model != getViewer().getTimeline().getModel()) {
			if (getBoolean(TIMELINE_GROUP_ELEMENTS)) {
				list.add(model);
			} else {
				TreeTimelineContentProvider cp = getViewer().getTreeTimelineContentProvider();
				for (Object child : cp.getChildren(model)) {
					list.add(child);
				}
			}
		}
		return list;
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
	public void activate() {
		if (getViewer().isAnimated()) {
			getFigure().addLayoutListener(LayoutAnimator.getDefault());
		}
		getFigure().addLayoutListener(new LayoutListener.Stub() {
			@Override public void postLayout(IFigure container) { updateVisibility(); }
		});
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		getTimeline().addPropertyChangeListener(listener);
		getViewer().addPropertyChangeListener(listener);
		getViewer().getTreeTimelineContentProvider().addListener(listener);
		getViewer().getTimelineSectionModel().eAdapters().add(listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (getViewer().isAnimated()) {
			getFigure().removeLayoutListener(LayoutAnimator.getDefault());
		}
		getTimeline().removePropertyChangeListener(listener);
		getViewer().getTimelineSectionModel().eAdapters().remove(listener);
		getViewer().removePropertyChangeListener(listener);
		getViewer().getTreeTimelineContentProvider().removeListener(listener);
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
		super.deactivate();
	}

	@Override
	protected IFigure createFigure() {
		IFigure r = new Figure();
		RowDataFigureLayout layout = new RowDataFigureLayout(getTimeline().getPage());
		layout.setRowElementHeight(TimelineUtils.getRowElementHeight(this));
		layout.setOverlapFactor(TIMELINE_PREFERENCES.getFloat(TimelinePreferencePage.P_ROW_ELEMENT_OVERLAP));
		r.setLayoutManager(layout);
		r.setOpaque(false);
		//
		// This tricky little method makes sure that the headers of the timeline
		// that correspond to this row have the same height
		r.addLayoutListener(new LayoutListener.Stub() {
			@Override
			public void postLayout(IFigure container) {
				int height = container.getBounds().height;
				List list = (List) getViewer().getEditPartRegistry().get(getModel());
				for (Object o : list) {
					GraphicalEditPart ep = (GraphicalEditPart) o;
					Request request = new Request(REQ_ROW_DATA_LAYOUT);
					request.getExtendedData().put("height", height);
					ep.performRequest(request);
				}
			}
		});
		return r;
	}

	@Override
	public DragTracker getDragTracker(Request request) {
		return marqueeDragTracker;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(TickIntervalsEditPolicy.ROLE, new TickIntervalsEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		installEditPolicy(RowDataBackgroundEditPolicy.class.getSimpleName(), new RowDataBackgroundEditPolicy());
	}

	private void updateVisibility() {
		// Check if it is from the visible frozen content
		boolean visible = getViewer().isTimelineSectionFrozen();
		if(!visible) {
			// Check if it is visible within the scrollable content
			visible = TimelineUtils.isVerticallyVisible(this);
		}
		getFigure().setVisible(visible);
	}
	
	private class Listener extends AdapterImpl implements IPropertyChangeListener, PropertyChangeListener, TreeTimelineContentProvider.Listener {

		@Override
		public void notifyChanged(Notification msg) {
			Object feature = msg.getFeature();
			if (TimelinePackage.Literals.GANTT_SECTION__ROW_HEIGHT == feature
					|| TimelinePackage.Literals.TIMELINE_CONTENT__ROW_HEIGHT == feature) {
				updateRowElementHeight();
			}
			super.notifyChanged(msg);
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_ROW_ELEMENT_OVERLAP.equals(property)) {
				RowDataFigureLayout layout = (RowDataFigureLayout) getFigure().getLayoutManager();
				layout.setOverlapFactor(Float.parseFloat((String)event.getNewValue()));
				getFigure().getParent().invalidate();
			} else if (TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT.equals(property)) {
				updateRowElementHeight();
			}
		}

		@Override
		public void propertyChange(java.beans.PropertyChangeEvent e) {
			if (TIMELINE_GROUP_ELEMENTS.equals(e.getPropertyName())) {
				refreshInDisplayThread();
			} else if (TIMELINE_EVENT_VERTICAL_REFRESH.equals(e.getPropertyName())) {
				updateVisibility();
			}
		}

		@Override
		public void contentRefresh(Set<? extends Object> elements) {
			if (!getBoolean(TIMELINE_GROUP_ELEMENTS)
					&& elements.contains(getModel())) {
				refreshChildren();
			}
		}
		
		@Override
		public void labelUpdate(Set<? extends Object> elements) {
			// do nothing
		}

		private void updateRowElementHeight() {
			RowDataFigureLayout layout = (RowDataFigureLayout) getFigure().getLayoutManager();
			layout.setRowElementHeight(TimelineUtils.getRowElementHeight(TreeTimelineDataRowEditPart.this));
		}

	}
	
}
