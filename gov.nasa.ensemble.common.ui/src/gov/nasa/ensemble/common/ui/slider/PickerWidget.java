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

import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * A widget that paints one of the "thumbs" (draggable bits) that rides along
 * the track in the slider.
 */
public class PickerWidget extends Canvas implements PaintListener, DisposeListener {

	private static final Image PICKER_SMALL = WidgetPlugin.getImage("icons/picker2small.gif");

	private int mouseDownLocation;
	private boolean dragging, moving;
	private TrackWidget trackWidget;
	private SolPickerWidget solPickerWidget;

	/**
	 * Construct a PickerWidget.
	 * 
	 * @param parent
	 *            the parent Composite containing this picker widget.
	 * @param style
	 *            SWT style hints.
	 */
	public PickerWidget(Composite parent, int style) {
		super(parent, style);

		addPaintListener(this);
		addDisposeListener(this);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}

			@Override
			public void mouseDown(MouseEvent e) {
				Rectangle bounds = new Rectangle(0, 0, getSize().x, getSize().y);
				if (bounds.contains(e.x, e.y)) {
					dragging = true;
				}
				mouseDownLocation = e.x;
			}

			@Override
			public void mouseUp(MouseEvent e) {
				dragging = false;
			}
		});

		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if (dragging) {
					int xLocationInParentCoordinates = e.x + getLocation().x
							- mouseDownLocation;
					int leftmostPickerLocation = 0;
					int rightmostPickerLocation = trackWidget.getBounds().width;

					if (xLocationInParentCoordinates >= leftmostPickerLocation
							&& xLocationInParentCoordinates <= rightmostPickerLocation)
						solPickerWidget.dragChanged(
								xLocationInParentCoordinates, 0);

					if (xLocationInParentCoordinates < leftmostPickerLocation)
						solPickerWidget.dragChanged(leftmostPickerLocation, 0);

					if (xLocationInParentCoordinates > rightmostPickerLocation)
						solPickerWidget.dragChanged(rightmostPickerLocation, 0);
				}
			}
		});

	}

	/**
	 * Paint method to draw a picker widget.
	 * 
	 * @e the PaintEvent.
	 */
	@Override
	public void paintControl(PaintEvent e) {

		// Region region = new Region();
		// region.add(new int [] {9,0, 0,20, 18,20});
		// e.gc.setClipping(region);
		// e.gc.setAlpha(0);

		e.gc.setBackground(ColorConstants.white);
		e.gc.fillRectangle(0, 0, getBounds().width, getBounds().height);
		e.gc.drawImage(PICKER_SMALL, 0, 0);

	}

	/**
	 * Set flag used to indicate this picker is being moved.
	 * 
	 * @param move
	 *            flag that indicates this picker is being moved.
	 */
	public void setMoving(boolean move) {
		moving = move;
	}

	/**
	 * Get the flag to check if this picker is being moved.
	 * 
	 * @return true if this picker is moving else false.
	 */
	public boolean isMoving() {
		return (moving);
	}

	/**
	 * Callback when this widget is being disposed to free resources.
	 * 
	 * @param e
	 *            the DisposeEvent.
	 */
	@Override
	public void widgetDisposed(DisposeEvent e) {
		// dispose?
	}

	/**
	 * Store a reference to the TrackWidget that this PickerWidget interacts
	 * with.
	 * 
	 * @param tw
	 *            the TrackWidget.
	 */
	public void setTrackWidget(TrackWidget tw) {
		this.trackWidget = tw;
	}

	/**
	 * Store a reference to the SolPickerWidget that this PickerWidget is part
	 * of.
	 * 
	 * @param spw
	 *            the sol picker widget.
	 */
	public void setSolPickerWidget(SolPickerWidget spw) {
		this.solPickerWidget = spw;
	}
}
