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
package gov.nasa.ensemble.common.ui.gef;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;
import org.eclipse.gef.tools.TargetingTool;

public class SplitEditPart extends AbstractGraphicalEditPart 
	implements PropertyChangeListener, SplitConstants
{ 

	protected static final String SPLITTER_ROLE = "DividerEditPolicy";
	
	private GraphicalEditPart leftEditPart = null;
	private GraphicalEditPart rightEditPart = null;
	
	public SplitEditPart() {
		// default constructor
	}
	
	@Override
	protected final IFigure createFigure() {
		SplitFigure figure = new SplitFigure(getCastedModel().getOrientation());
		figure.setDividerLocation(getCastedModel().getDividerLocation());
		return figure;
	}
	
	@Override
	protected void refreshChildren() {
		// do nothing
	}

	protected SplitFigure getCastedFigure() {
		return (SplitFigure) getFigure();
	}
		
	public SplitModel getCastedModel() {
		return (SplitModel) getModel();
	}
	
	public EditPart getTopEditPart() {
		return getLeftEditPart(); 
	}
	
	public void setTopEditPart(GraphicalEditPart part) {
		setLeftEditPart(part);
	}
	
	public EditPart getLeftEditPart() {
		return this.leftEditPart; 
	}
	
	public void setLeftEditPart(GraphicalEditPart part) {
		if( this.leftEditPart != null ) {
			removeChild(this.leftEditPart);
		}
		this.leftEditPart = part;
	}
	
	public EditPart getBottomEditPart() {
		return getRightEditPart();
	}
	
	public void setBottomEditPart(GraphicalEditPart part) {
		setRightEditPart(part);
	}
	
	public EditPart getRightEditPart() {
		return this.rightEditPart; 
	}
	
	public void setRightEditPart(GraphicalEditPart part) {
		if( this.rightEditPart != null ) {
			removeChild(this.rightEditPart);
		}
		this.rightEditPart = part;
	}
	
	@Override
	protected void addChildVisual(EditPart child, int index) {
		if (this.leftEditPart == child) {
			getCastedFigure().setLeftFigure(((GraphicalEditPart)child).getFigure());
		} else 
		if (this.rightEditPart == child) {
			getCastedFigure().setRightFigure(((GraphicalEditPart)child).getFigure());
		} else {
		    throw new NoSuchElementException("Unrecognized EditPart");
		}
	}
	
	@Override
	public void activate() {
		super.activate();
		getCastedModel().addPropertyChangeListener(this);
		
		SplitHandle handle = new SplitHandle(this, getCastedFigure().getDividerFigure());
		getLayer(LayerConstants.HANDLE_LAYER).add(handle);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getCastedModel().removePropertyChangeListener(this);
	}
	
	@Override
	protected void createEditPolicies() {
		// no special edit policies required
	}

	@Override
	protected void refreshVisuals() {
		if (getCastedFigure() != null) {
			if(this.leftEditPart != null) 	addChildVisual(getLeftEditPart(), -1);
			if(this.rightEditPart != null) 	addChildVisual(getRightEditPart(), -1);
			
			getCastedFigure().setDividerLocation(getCastedModel().getDividerLocation());
		}
		super.refreshVisuals();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if( evt.getSource() == getModel() && SplitModel.DIVIDER_LOCATION.equals(evt.getPropertyName()) ) {
			refreshVisuals();
		}
	}
	
	private class SplitHandle extends MoveHandle {

		public SplitHandle(GraphicalEditPart owner, IFigure figure) {
			super(owner, new MoveHandleLocator(figure));
			figure.addFigureListener(new FigureListener() {
				@Override
				public void figureMoved(IFigure source) {
					revalidate();
				}
			});
		}
		
		@Override
		protected DragTracker createDragTracker() {
			return new SplitDragTracker();
		}
		
		@Override
		public boolean containsPoint(int x, int y) {
			return getBounds().contains(x, y);
		}
		
		@Override
		protected void initialize() {
			switch(((SplitEditPart)getOwner()).getCastedFigure().getOrientation()) {
			case SplitConstants.HORIZONTAL_SPLIT:
				setCursor(Cursors.SIZEWE);
				break;
			case SplitConstants.VERTICAL_SPLIT:
				setCursor(Cursors.SIZENS);
				break;
			}
		}

		private class SplitDragTracker extends TargetingTool implements DragTracker {
			
			public SplitDragTracker() {
				// default constructor
			}
			
			@Override
			protected String getCommandName() {
				return "Drag Sash";
			}

			@Override
			protected boolean handleDrag() {
				Point pt = getCurrentInput().getMouseLocation();
				SplitEditPart ep = (SplitEditPart) getOwner();
				switch(ep.getCastedFigure().getOrientation()) {
				case SplitConstants.HORIZONTAL_SPLIT:
					ep.getCastedModel().setDividerLocation(pt.x);
					break;
				case SplitConstants.VERTICAL_SPLIT:
					ep.getCastedModel().setDividerLocation(pt.y);
					break;
				}
				
				return true;
			}
			
		}
		
	}

}
