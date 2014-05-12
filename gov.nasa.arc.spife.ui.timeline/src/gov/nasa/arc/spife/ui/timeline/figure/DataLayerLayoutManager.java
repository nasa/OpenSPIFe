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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class DataLayerLayoutManager extends XYLayout {

	@Override
	protected Dimension calculatePreferredSize(IFigure f, int hint, int hint2) {
		Dimension d = super.calculatePreferredSize(f, hint, hint2);
		return new Dimension(d.width, SWT.DEFAULT);
	}

	@Override
	public void layout(IFigure parent) {
		Point offset = getOrigin(parent);
		for (Object object : parent.getChildren()) {
			IFigure child = (IFigure) object;
			Rectangle bounds = (Rectangle)getConstraint(child);
			if (bounds == null) continue;

			bounds = bounds.getCopy();
			if (bounds.width == -1 || bounds.height == -1) {
				Dimension preferredSize = child.getPreferredSize(bounds.width, bounds.height);
				if (bounds.width == -1)
					bounds.width = preferredSize.width;
				if (bounds.height == -1)
					bounds.height = parent.getBounds().height;
			}
			bounds = bounds.getTranslated(offset);
			child.setBounds(bounds);
		}
	}
	
}
