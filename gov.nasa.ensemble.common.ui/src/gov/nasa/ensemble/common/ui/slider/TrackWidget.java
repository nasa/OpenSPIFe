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
package gov.nasa.ensemble.common.ui.slider;

import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Widget that paints a slider track for the sol range controls.
 */
public class TrackWidget extends Canvas implements PaintListener {

	private static final Color TRACK_BLUE = new Color(null, 170, 170, 170);
	private static final Color TRACK_BLACK = new Color(null, 65, 64, 66);

	/**
	 * Construct a TrackWidget.
	 * 
	 * @param parent
	 *            the parent Composite of the track widget
	 * @param style
	 *            SWT style hints
	 */
	public TrackWidget(Composite parent, int style) {
		super(parent, style);
		addPaintListener(this);
	}

	/**
	 * Paint method to draw the track widget.
	 * 
	 * @param e
	 *            the PaintEvent
	 */
	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setBackground(ColorConstants.white);
		e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
		e.gc.setBackground(TRACK_BLACK);
		e.gc.fillRectangle(0, 9, getBounds().width, 3);
		e.gc.setBackground(TRACK_BLUE);
		e.gc.fillRectangle(1, 10, getBounds().width - 2, 1);
	}

}
