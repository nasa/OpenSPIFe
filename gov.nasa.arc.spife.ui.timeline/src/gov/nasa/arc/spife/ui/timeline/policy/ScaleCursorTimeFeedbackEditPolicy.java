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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class ScaleCursorTimeFeedbackEditPolicy extends CursorTimeFeedbackEditPolicy {

	private Label label = new ScaleLabel();
	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		label.setForegroundColor(ColorConstants.black);
		Font font = label.getFont();
		if (font == null) {
			font = FontUtils.getStyledFont(SWT.BOLD);
		}
		font = TimelineUtils.deriveScaleHeightFont(font);
		font = FontUtils.getStyledFont(font, SWT.BOLD);
		label.setFont(font);
		label.setOpaque(true);
		getLayer(LAYER_DATA_PRIMARY_LAYER).add(label);
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		getLayer(LAYER_DATA_PRIMARY_LAYER).remove(label);
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
	}
	
	@Override
	protected void updateCursorTimeFeedback(Date date) {
		boolean enabled = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_CURSOR_TIME_ENABLED);
		label.setVisible(enabled);
		
		super.updateCursorTimeFeedback(date);
		final IFigure layer = getLayer(LAYER_DATA_PRIMARY_LAYER);
		final String text = StringifierRegistry.getStringifier(Date.class).getDisplayString(date);
		Dimension extents = FigureUtilities.getTextExtents(text, label.getFont());
		
		Page page = getViewer().getTimeline().getPage();
		int pos = page.getScreenPosition(date);
		int x = pos - (extents.width/2);
		int y = (int) (layer.getBounds().height - extents.height * 1.75 - 5);
		final Rectangle constraint = new Rectangle(x, y, extents.width, extents.height);
		
		Display display = WidgetUtils.getDisplay();
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				// by the time this code runs, the widgets could be out of date so try/catch
				try {
					label.setText(text);
					label.setBounds(constraint);
					IFigure parent = label.getParent();
					if(parent != null) {
						layer.setConstraint(label, constraint);	
					}
				}

				catch (IllegalArgumentException illegalArgumentException) {
					LogUtil.warn(illegalArgumentException);
				}
			}
		});
	}

	private class ScaleLabel extends Label {

		@Override
		protected void paintFigure(Graphics graphics) {
			if (isOpaque()) {
				graphics.setAlpha(192);
				super.paintFigure(graphics);
				graphics.setAlpha(255);
			}
			Rectangle bounds = getBounds();
			graphics.translate(bounds.x, bounds.y);
			if (getIcon() != null)
				graphics.drawImage(getIcon(), getIconLocation());
			if (!isEnabled()) {
				graphics.translate(1, 1);
				graphics.setForegroundColor(ColorConstants.buttonLightest);
				graphics.drawText(getSubStringText(), getTextLocation());
				graphics.translate(-1, -1);
				graphics.setForegroundColor(ColorConstants.buttonDarker);
			}
			graphics.drawText(getSubStringText(), getTextLocation());
			graphics.translate(-bounds.x, -bounds.y);
		}
		
	}

	private class Listener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (TimelinePreferencePage.P_SCALE_FONT_SIZE.equals(event.getProperty())) {
				Font font = label.getFont();
				if (font == null) {
					font = FontUtils.getStyledFont(SWT.BOLD);
				}
				font = TimelineUtils.deriveScaleHeightFont(font);
				font = FontUtils.getStyledFont(font, SWT.BOLD);
				label.setFont(font);
			}
		}
		
	}
	
}
