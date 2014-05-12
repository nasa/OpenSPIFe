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
package gov.nasa.arc.spife.core.plan.editor.timeline.util;

import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Color;

public class RowHighlightHelper {

	private final EditPolicy policy;
	
	private IFigure highlightedFigure = null;
	private Color previousBackgroundColor = null;
	
	public RowHighlightHelper(EditPolicy policy) {
		super();
		this.policy = policy;
	}

	public void showHighlight(Point pt) {
		IFigure hostFigure = getHostFigure(pt);			
		
		//check if highlightedFigure is the same as currentSelectedFigure, 
		//if no, 
		//1. restore highlightedFigure background color, 
		//2. update highlightedFigure to current selected figure.
		//3. highlight currentSelectedFigure 
		//if yes, do nothing
		
		Color currentSelectedFigureBackgroundColor = hostFigure.getBackgroundColor();
		if (highlightedFigure != hostFigure) {
			if (highlightedFigure != null) {
				highlightedFigure.setBackgroundColor(previousBackgroundColor);
			}
			highlightedFigure = hostFigure;
			previousBackgroundColor = currentSelectedFigureBackgroundColor;
		}
		
		int y = hostFigure.getBounds().y;
		int height = hostFigure.getBounds().height;
		int lowerBound = y;		
		int upperBound = y + height;			
		if (pt.y > lowerBound && pt.y < upperBound) {
			hostFigure.setBackgroundColor(ColorConstants.yellow);
		} else { 
			hostFigure.setBackgroundColor(previousBackgroundColor);
		}
	}

	protected IFigure getHostFigure(Point pt) {
		IFigure hostFigure = ((GraphicalEditPart)policy.getHost()).getFigure();
		return hostFigure;
	}
	
	public void eraseHighlight() {
		if (highlightedFigure != null) {
			highlightedFigure.setBackgroundColor(previousBackgroundColor);
			highlightedFigure = null;
			previousBackgroundColor = null;
		}
	}
	
}
