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

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.ensemble.common.ui.gef.SplitEditPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

public abstract class AbstractTimelineEditPart<T> extends TimelineViewerEditPart<T> {

	private SplitEditPart 				splitEditPart = null;
	
	private ScrollingGraphicalEditPart	scrollingHeaderEditPart = null;
	private ScrollingGraphicalEditPart	scrollingDataEditPart = null;
	
	private GraphicalEditPart 			headerEditPart = null;
	private GraphicalEditPart 			dataEditPart = null;
	
	public AbstractTimelineEditPart() {
		// provide default constructor
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	protected IFigure createFigure() {
		IFigure figure = new Figure();
		figure.setLayoutManager(new ToolbarLayout());
		return figure;
	}

	@Override
	protected List getModelChildren() {
		List<Object> children = new ArrayList<Object>();
		children.add(getViewer().getSplitModel());
		return children;
	}

	@Override
	protected EditPart createChild(Object model) {
		EditPart ep = null;
		if (model == getViewer().getSplitModel()) {
			if (splitEditPart == null) {
				splitEditPart = new SplitEditPart();
				splitEditPart.setModel(model);
			}
			ep = splitEditPart;
		}
		return ep;
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (childEditPart == splitEditPart) {
			super.addChildVisual(childEditPart, getFigure().getChildren().size());
			
			if (scrollingHeaderEditPart == null) {
				scrollingHeaderEditPart = new ScrollingGraphicalEditPart();
				addChild(scrollingHeaderEditPart, -1);
				splitEditPart.setLeftEditPart(scrollingHeaderEditPart);
				
				headerEditPart = createHeaderEditPart(getModel());
				scrollingHeaderEditPart.addChild(headerEditPart, 0);
				ScrollPane pane = (ScrollPane) scrollingHeaderEditPart.getFigure();
				pane.setHorizontalScrollBarVisibility(getHorizontalScrollBarVisibility());
			}
			
			if (scrollingDataEditPart == null) {
				scrollingDataEditPart = new ScrollingGraphicalEditPart();
				addChild(scrollingDataEditPart, -1);
				splitEditPart.setRightEditPart(scrollingDataEditPart);
				
				dataEditPart = createDataEditPart(getModel());
				scrollingDataEditPart.addChild(dataEditPart, 0);
				ScrollPane pane = (ScrollPane) scrollingDataEditPart.getFigure();
				pane.setHorizontalScrollBarVisibility(getHorizontalScrollBarVisibility());
			}
		}
	}

	protected int getHorizontalScrollBarVisibility() {
		return ScrollPane.NEVER;
	}

	public abstract GraphicalEditPart createHeaderEditPart(T model);
	
	public abstract GraphicalEditPart createDataEditPart(T model);
	
	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		if (childEditPart == splitEditPart) {
			super.removeChildVisual(childEditPart);
		}
	}

	public GraphicalEditPart getHeaderEditPart() {
		return headerEditPart;
	}

	public GraphicalEditPart getDataEditPart() {
		return dataEditPart;
	}
	
	public ScrollPane getHeaderScrollPane() {
		return scrollingHeaderEditPart == null ? null : (ScrollPane) scrollingHeaderEditPart.getFigure();
	}

	public ScrollingGraphicalEditPart getScrollingHeaderEditPart() {
		return scrollingHeaderEditPart;
	}
	
	public ScrollPane getDataScrollPane() {
		return  scrollingDataEditPart == null ? null : (ScrollPane) scrollingDataEditPart.getFigure();
	}

	public ScrollingGraphicalEditPart getScrollingDataEditPart() {
		return scrollingDataEditPart;
	}

	@Override
	protected void createEditPolicies() {
		// no special policies necessary
	}
	
	public static class ScrollingGraphicalEditPart extends TimelineViewerEditPart<Object> {
		
		@Override
		protected IFigure createFigure() {
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setOpaque(false);
			scrollPane.setBackgroundColor(ColorConstants.white);
			return scrollPane;
		}

		@Override
		protected List getModelChildren() {
			return Collections.emptyList();
		}
		
		@Override
		public void addChild(EditPart child, int index) {
			super.addChild(child, index);
		}

		@Override
		protected void refreshChildren() {
			// ignore
		}

		@Override
		protected void addChildVisual(EditPart childEditPart, int index) {
			IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
			((ScrollPane)getFigure()).setContents(child);
		}

		@Override
		protected void createEditPolicies() {
			// no special policies necessary
		}

		/* 
		 * Not selectable
		 */
		@Override
		public boolean isSelectable() {
			return false;
		}
		
	}
	
}
