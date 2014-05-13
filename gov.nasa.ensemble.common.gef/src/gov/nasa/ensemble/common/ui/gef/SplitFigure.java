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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

public class SplitFigure extends Figure implements SplitConstants {

    /**
     * Used to add a <code>Figure</code> to the left of the other
     * <code>Figure</code>.
     */
    public final static String LEFT = "left";

    /**
     * Used to add a <code>Figure</code> to the right of the other
     * <code>Figure</code>.
     */
    public final static String RIGHT = "right";

    /**
     * Used to add a <code>Figure</code> above the other
     * <code>Figure</code>.
     */
    public final static String TOP = "top";

    /**
     * Used to add a <code>Figure</code> below the other
     * <code>Figure</code>.
     */
    public final static String BOTTOM = "bottom";

    /**
     * Used to add a <code>Figure</code> that will represent the divider.
     */
    public final static String DIVIDER = "divider";

    /**
     * How the views are split.
     */
    private int orientation = -1;

    /**
     * The left or top figure.
     */
    protected IFigure leftFigure;

    /**
     * The divider figure.
     */
    protected RectangleFigure dividerFigure;
    
    /**
     * The right or bottom figure.
     */
    protected IFigure rightFigure;
    
    public SplitFigure(int orientation) {
    	this.orientation = orientation;
    	setLayoutManager(new SplitFigureLayout());
    	
    	dividerFigure = new RectangleFigure();
    	dividerFigure.setOutline(false);
    	dividerFigure.setBorder(new SimpleRaisedBorder());
    	switch(getOrientation()) {
    	case HORIZONTAL_SPLIT:
        	dividerFigure.setSize(5, 0);
        	dividerFigure.setPreferredSize(new Dimension(5, 0));
    		break;
    	case VERTICAL_SPLIT:
        	dividerFigure.setSize(0, 5);
        	dividerFigure.setPreferredSize(new Dimension(0, 5));
    		break;
    	}
    	
		add(dividerFigure, DIVIDER);
    }
    
    public int getOrientation() {
    	return orientation;
    }

    public IFigure getTopFigure() {
    	return getLeftFigure();
    }
    
    public void setTopFigure(IFigure figure) {
    	setLeftFigure(figure);
    }

    public IFigure getLeftFigure() {
    	return leftFigure;
    }

    public void setLeftFigure(IFigure figure) {
    	if( leftFigure == figure ) {
    		return;
    	}
    	
    	if( leftFigure != null ) {
    		remove(leftFigure);
    	}
    	leftFigure = figure;
    	add(leftFigure, LEFT);
    }
    
    public IFigure getDividerFigure() {
    	return dividerFigure;
    }
    
    public void setDividerLocation(int location) {
    	switch(getOrientation()) {
    	case HORIZONTAL_SPLIT:
    		dividerFigure.setLocation(new Point(location, 0));
    		break;
    	case VERTICAL_SPLIT:
    		dividerFigure.setLocation(new Point(0, location));
    		break;
    	}
    	revalidate();
    }
    
    public IFigure getBottomFigure() {
    	return getRightFigure();
    }
    
    public void setBottomFigure(IFigure figure) {
    	setRightFigure(figure);
    }

    public IFigure getRightFigure() {
    	return rightFigure;
    }
    
    public void setRightFigure(IFigure figure) {
    	if( rightFigure == figure ) {
    		return;
    	}
    	
    	if( rightFigure != null ) {
    		remove(rightFigure);
    	}
    	rightFigure = figure;
    	add(rightFigure, RIGHT);
    }
    
}
