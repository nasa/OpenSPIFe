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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage.Tooltip;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class TooltipShellManager {

	/* listen to the figure belonging to this edit policy's host to dismiss
	 * any tooltip being shown during the moving of the figure.
	 */
	private FigureListener figureListener = new FigureListenerImpl();

	/*
	 * listen to the main application window to dismiss a tooltip if the
	 * window goes out of focus.
	 */
	@SuppressWarnings("unused")
	private IWindowListener windowListener = new WindowListener();

	//-------------
	
	// keep track of the last window active event, to improve tooltip behavior
	private boolean windowActive = true;
	
	// The figure for the active edit part
	private IFigure figure = null;
	
	// the tooltip that is currently being displayed, if any.
	private Shell tooltip = null;

	// the last date/time that a tooltip was shown
	private volatile long lastTimeTooltipDisplayed = 0;

	/**
	 * Cause the tooltip shell to activate for the provided host.
	 * Intentionally does not retain the host, to assist in garbage collection.
	 * 
	 * @param host
	 * @param location
	 */
	public void display(GraphicalEditPart host, Point location) {
		if (Tooltip.NONE != getTooltipMode() && windowActive) {
			EditPartViewer viewer = GEFUtils.getViewerSafely(host);
			if (viewer != null) {
				Control control = viewer.getControl();
				if (!control.isDisposed()) {
					if (host.getFigure() != figure) {
						activateTooltip(host, control, location);
					}
				}
			}
		}
	}

	/**
	 * Call this to hide the tooltip.  Has no effect if called from
	 * the host that is the one displaying the tooltip.
	 * 
	 * @param host
	 */
	public void hide(GraphicalEditPart host) {
		if (host.getFigure() == figure) {
			deactivateTooltip();
		}
	}

	/**
	 * Then create a new tooltip for the element represented by the edit part,
	 * and initializes fields for the display, figure, and tooltip shell.
	 * Deactivate the tooltip if necessary,
	 * 
	 * @param hostw
	 * @param control
	 */
	private void activateTooltip(GraphicalEditPart host, Control control, Point location) {
		deactivateTooltip();
		figure = host.getFigure();
		if (figure != null) {
			tooltip = createAndLocateTooltipShell(control, host.getModel(), location);
			figure.addFigureListener(figureListener);
			final long time = System.currentTimeMillis();
			final int tooltipDisplayTime = getCurrentTooltipDisplayTime();
			lastTimeTooltipDisplayed = time;
			WidgetUtils.runInDisplayThreadAfterTime(tooltipDisplayTime, tooltip, new Runnable() {
				@Override
				public void run() {
					if (lastTimeTooltipDisplayed == time) {
						deactivateTooltip();
					}
				}
			});
		}
	}

	/**
	 * Dispose the tooltip if necessary and clear out fields
	 * pointing to the current figure, display, and tooltip.
	 */
	private void deactivateTooltip() {
		final Shell oldTooltip = tooltip;
		if (oldTooltip != null && !oldTooltip.isDisposed()) {
			WidgetUtils.runInDisplayThread(oldTooltip, new Runnable() {
				@Override
				public void run() {
					if (!oldTooltip.isDisposed()) {
						oldTooltip.dispose();
					}
					tooltip = null;
					if (figure != null) {
						figure.removeFigureListener(figureListener);
						figure = null;
					}
				}
			}, true);
		}
	}

	/**
	 * Create the tooltip shell and format it so that it fits on the screen, and display it.
	 * @param control
	 * @param object
	 * @param location
	 * @return
	 */
	private Shell createAndLocateTooltipShell(Control control, Object object, Point location) {
		Shell tooltip = TooltipShellBuilder.createTooltipShell(control, object);
		Display display = control.getDisplay();
		/*
		 * Map the request coordinates to null; this means that they will be
		 * mapped to the entire display area. The input required to show
		 * a tooltip is relative to the entire display area.
		 *
		 * so... take the request location, map to the entire display area
		 * and pass the result as the location where the tooltip should be shown.
		 */
		org.eclipse.swt.graphics.Point eclipsePoint = display.map(control, null, location.x, location.y);
		//TODO: replace "hard-coded" value with something else...
		eclipsePoint.y += 21; // move below cursor
		tooltip.pack();
		Rectangle tooltipBounds = tooltip.getBounds();
		tooltipBounds = tooltip.getBounds();
		Rectangle displayBounds = display.getBounds();
		// top left off screen
		/*
		if(!bounds.contains(tooltipShellBounds.x, tooltipShellBounds.y)) {
			System.out.println("top left off screen");
		}
		*/
		// top right off screen
		Rectangle goodBounds = new Rectangle(eclipsePoint.x, eclipsePoint.y, tooltipBounds.width, tooltipBounds.height);
		if (!displayBounds.contains(goodBounds.x + goodBounds.width, goodBounds.y)) {
			goodBounds = new Rectangle(goodBounds.x - goodBounds.width, goodBounds.y, goodBounds.width, goodBounds.height);
		}
		// bottom left off screen
		if (!displayBounds.contains(goodBounds.x, goodBounds.y + goodBounds.height)) {
			goodBounds = new Rectangle(goodBounds.x, goodBounds.y - goodBounds.height - 21, goodBounds.width, goodBounds.height);
		}
		tooltip.setBounds(goodBounds);
		tooltip.setVisible(true);
		return tooltip;
	}
	
	private Tooltip getTooltipMode() {
		String key = TimelinePreferencePage.P_TOOLTIP_SPEED;
		String tooltipSpeed = TimelineConstants.TIMELINE_PREFERENCES.getString(key);
		try {
			return Tooltip.valueOf(tooltipSpeed);
		} catch (NumberFormatException e) {
			LogUtil.warnOnce("failed to parse tooltip speed preference: " + key + "=" + tooltipSpeed);
			return Tooltip.NORMAL;
		}
	}
	
	private int getCurrentTooltipDisplayTime() {
		String key = TimelinePreferencePage.P_TOOLTIP_TIME;
		String tooltipTime = TimelineConstants.TIMELINE_PREFERENCES.getString(key);
		try {
			return Integer.parseInt(tooltipTime);
		} catch (NumberFormatException e) {
			LogUtil.warnOnce("failed to parse tooltip time preference: " + key + "=" + tooltipTime);
			return 5*1000; // 5 seconds for a default
		}
	}

	private final class FigureListenerImpl implements FigureListener {
		@Override
		public void figureMoved(IFigure source) {
			deactivateTooltip();
		}
	}

	private final class WindowListener implements IWindowListener {

		public WindowListener() {
			super();
			PlatformUI.getWorkbench().addWindowListener(this);
		}
		
		@Override
		public void windowActivated(IWorkbenchWindow window) {
			windowActive = true;
		}

		@Override
		public void windowClosed(IWorkbenchWindow window) {
			windowActive = false;
		}

		// hide a tooltip if the window is deactivated
		@Override
		public void windowDeactivated(IWorkbenchWindow window) {
			deactivateTooltip();
			windowActive = false;
		}

		@Override
		public void windowOpened(IWorkbenchWindow window) {
			// no impl
		}
			
	}
	
}
