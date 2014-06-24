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
package gov.nasa.arc.spife.ui.timeline.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class LabeledPolylineConnection extends PolylineConnection {

	private Label label = null;
	
	public LabeledPolylineConnection() {
		// default constructor
	}
	
	public LabeledPolylineConnection(Label label) {
		setLabel(label);
	}
	
	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		if (this.label != null) {
			remove(label);
		}
		this.label = label;
		if (this.label != null) {
			add(this.label);
		}
		repaint();
	}

	@Override
	public void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		if (label != null) {
			label.paint(graphics);
		}
	}

	@Override
	public void layout() {
		if (getSourceAnchor() != null && getTargetAnchor() != null)
			getConnectionRouter().route(this);

		if (label != null
				&& label.getText() != null 
				&& label.getText().length() != 0) {
			Point start = getStart();
			Point end = getEnd();
			Dimension r = label.getPreferredSize();
			
			label.setLocation(new Point(
				start.x + (end.x - start.x - r.width)/2,
				start.y + (end.y - start.y - r.height)/2
			));
			
			label.setSize(r);
		}
		
		Rectangle oldBounds = bounds;
		super.layout();
		bounds = null;
		
		if (!getBounds().contains(oldBounds)) {
			getParent().translateToParent(oldBounds);
			getUpdateManager().addDirtyRegion(getParent(), oldBounds);
		}
		
		repaint();
		fireFigureMoved();
	}
	
}
