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
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.action.ZoomTimelineCommandHandler;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import java.util.Date;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;

public class SplitScrollEditPartFactory<T> implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart ep = new SplitScroller<T>();
		ep.setModel(model);
		return ep;
	}
	
	public static class SplitScroller<T> extends AbstractTimelineEditPart<T> {

		@Override
		public boolean isSelectable() {
			return false;
		}

		@Override
		public GraphicalEditPart createHeaderEditPart(T model) {
			return new CurrentTimeEditPart();
		}

		@Override
		public GraphicalEditPart createDataEditPart(T model) {
			return new ScrollPaneEditPart();
		}
		
		@Override
		protected int getHorizontalScrollBarVisibility() {
			return ScrollPane.AUTOMATIC;
		}
		
	}
	
	private static class ScrollPaneEditPart extends TimelineViewerEditPart<Object> {

		private Listener listener = new Listener();
		
		@Override
		public void activate() {
			super.activate();
			getTimeline().getPage().eAdapters().add(listener);
		}

		@Override
		public void deactivate() {
			super.deactivate();
			getTimeline().getPage().eAdapters().remove(listener);
		}
		
		@Override 
		protected IFigure createFigure() {
			IFigure figure = new RectangleFigure();
			figure.setPreferredSize(computeFigureBounds());
			return figure;
		}
		
		@Override 
		protected void createEditPolicies() {
			// no special edit policies
		}
		
		protected Dimension computeFigureBounds() {
			Page page = getTimeline().getPage();
			return new Dimension(page.getWidth(), 16);
		}

		@Override
		protected void refreshVisuals() {
			IFigure f = getFigure();
			f.setPreferredSize(computeFigureBounds());
			f.invalidate();
		}
		
		private class Listener extends AdapterImpl {
			@Override
			public void notifyChanged(Notification notification) {
				Object f = notification.getFeature();
				if (TimelinePackage.Literals.PAGE__ZOOM_OPTION == f) {
					final TimelineViewer viewer = getViewer();
					// get the location we are zooming to
					int x = viewer.getZoomManager().getZoomLocation().x;
					int width = viewer.getTimelineEditPart().getDataScrollPane().getBounds().width;
					final float split[] = { 0.5f };
					if (x > -1 && width > x) {
						split[0] = ((float)x)/width;
					}
					// set up a listener to scroll to location after layout is complete
					final ScrollPane dataScrollPane = viewer.getTimelineEditPart().getDataScrollPane();
					ScrollBar hbar = dataScrollPane.getHorizontalScrollBar();
					final int oldCenter = hbar.getValue() + ((int)(hbar.getExtent()*split[0]));
					ZoomOption oval = (ZoomOption)notification.getOldValue();
					ZoomOption nval = (ZoomOption)notification.getNewValue();
					final double d = ((double)oval.getMsInterval()) / nval.getMsInterval();
					GEFUtils.runLaterInDisplayThread(ScrollPaneEditPart.this, new Runnable() {
						@Override
						public void run() {
							HbarUpdater updater = new HbarUpdater(viewer.getTimeline(), dataScrollPane, (int)(d * oldCenter)
									, split[0], viewer.getZoomManager().getZoomDate());
							dataScrollPane.addLayoutListener(updater);
							refresh();
						}
					});
				} else if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
							|| TimelinePackage.Literals.PAGE__START_TIME == f
							|| TimelinePackage.Literals.PAGE__DURATION == f) {
					GEFUtils.runLaterInDisplayThread(ScrollPaneEditPart.this, new Runnable() {
						@Override
						public void run() {
							refresh();
						}
					});
				}
			}
		}
		
		public class HbarUpdater extends LayoutListener.Stub {
			private final Timeline timeline;
			private ScrollPane sp;
			private int newCenter;
			private Date centerDate;
			private float split;
			
			public HbarUpdater(Timeline timeline, ScrollPane sp, int newCenter, float split, Date centerDate) {
				this.timeline = timeline;
				this.sp = sp;
				this.newCenter = newCenter;
				this.split = split;
				this.centerDate = centerDate;
			}
			
			@Override
			public boolean layout(IFigure container) {
				return false;
			}
			
			@Override
			public void postLayout(IFigure container) {
				if (timeline != null && centerDate != null) {
					sp.removeLayoutListener(this);
					timeline.centerOnTime(centerDate);	
					ZoomManager zoomManager = ZoomTimelineCommandHandler.getZoomManager();
					zoomManager.setZooming(false);
				} else {				
					ScrollBar hbar = sp.getHorizontalScrollBar();
					hbar.setValue(newCenter - (int)(hbar.getExtent()*split));
					sp.invalidate();
					sp.removeLayoutListener(this);
				}
			}
		}
	}
	
	private static class CurrentTimeEditPart extends TimelineViewerEditPart<Object> {

		@Override 
		protected void createEditPolicies() {
			// no special edit policies
		}
		
		@Override 
		protected IFigure createFigure() {
			return new Label();
		}
		
	}

}
