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

import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class TimepointConnectionAnchor extends AbstractConnectionAnchor {

	private final Timepoint timepoint;
	
	public TimepointConnectionAnchor(Timepoint timepoint, IFigure owner) {
		super(owner);
		this.timepoint = timepoint;
	}

	@Override
	public Point getLocation(Point reference) {
		int x = 0;
		Rectangle b = getOwner().getBounds();
		if (timepoint == Timepoint.END)
			x = b.width;
		if (timepoint == Timepoint.START || timepoint == Timepoint.END)
			x += b.x;
		return new Point(x, b.y + b.height/2);
	}

}
