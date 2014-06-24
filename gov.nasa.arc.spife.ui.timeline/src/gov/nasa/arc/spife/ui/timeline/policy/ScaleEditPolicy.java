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

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.figure.ScaleFigure;
import gov.nasa.arc.spife.ui.timeline.preference.TimelineDateFormatPreferencePage;
import gov.nasa.arc.spife.ui.timeline.preference.TimelineDateFormatRegistry;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import java.text.DateFormat;
import java.util.List;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ScaleEditPolicy extends TimelineViewerEditPolicy {

	public static final String ROLE = ScaleEditPolicy.class.getSimpleName(); 
	
	private Listener listener = new Listener();
	
	private Color alternatingColor;
	private Color verticalLineColor;
	private Color tickmarkColor;

	private final ScaleFigure figure;
	
	public ScaleEditPolicy(ScaleFigure figure) {
		this.figure = figure;
	}

	public Color getAlternatingColor() {
		if (alternatingColor == null) {
			String rgbString = TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_ALTERNATING_COLOR);
			RGB rgb = StringConverter.asRGB(rgbString);
			alternatingColor = ColorMap.RGB_INSTANCE.getColor(rgb);
		}
		return alternatingColor;
	}
	
	public Color getVerticalLineColor() {
		if (verticalLineColor == null) {
			String rgbString = TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_VERTICAL_LINES_COLOR);
			RGB rgb = StringConverter.asRGB(rgbString);
			verticalLineColor = ColorMap.RGB_INSTANCE.getColor(rgb);
		}
		return verticalLineColor;
	}
	
	public Color getTickmarkColor() {
		if (tickmarkColor == null) {
			String rgbString = TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_SCALE_TICKMARK_COLOR);
			RGB rgb = StringConverter.asRGB(rgbString);
			tickmarkColor = ColorMap.RGB_INSTANCE.getColor(rgb);
		}
		return tickmarkColor;
	}

	@Override
	public void activate() {
		super.activate();
		figure.setAlternatingRowColor(getAlternatingColor());
		figure.setVerticalLineColor(getVerticalLineColor());
		figure.setTickmarkColor(getTickmarkColor());
		figure.setDateFormats(TimelineDateFormatRegistry.getDateFormats());
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(listener);
	}

	@Override
	public void deactivate() {
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(listener);
		disposeAlternatingColor();
		disposeVerticalLineColor();
		disposeTickmarkColor();
		super.deactivate();
	}
	
	private void disposeAlternatingColor() {
		if (alternatingColor != null) {
			alternatingColor = null;
		}
	}
	
	private void disposeVerticalLineColor() {
		if (verticalLineColor != null) {
			verticalLineColor = null;
		}
	}
	
	private void disposeTickmarkColor() {
		if (tickmarkColor != null) {
			tickmarkColor = null;
		}
	}
	
	protected void dateFormatsUpdated(final List<DateFormat> dateFormats) {
		figure.setDateFormats(dateFormats);
	}

	private class Listener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (TimelineDateFormatPreferencePage.P_DATE_FORMATS.equals(event.getProperty())) {
				final List<DateFormat> dateFormats = TimelineDateFormatRegistry.getDateFormats();
				dateFormatsUpdated(dateFormats);
			} else if (TimelinePreferencePage.P_ALTERNATING_COLOR.equals(event.getProperty())) {
				disposeAlternatingColor();
				GEFUtils.runInDisplayThread(getHost(), new Runnable() {
					@Override
					public void run() {
						figure.setAlternatingRowColor(getAlternatingColor());
					}
				});
			} 
			else if (TimelinePreferencePage.P_VERTICAL_LINES_COLOR.equals(event.getProperty())) {
				disposeVerticalLineColor();
				GEFUtils.runInDisplayThread(getHost(), new Runnable() {
					@Override
					public void run() {
						figure.setVerticalLineColor(getVerticalLineColor());
					}
				});
			} else if (TimelinePreferencePage.P_SCALE_TICKMARK_COLOR.equals(event.getProperty())) {
				disposeTickmarkColor();
				GEFUtils.runInDisplayThread(getHost(), new Runnable() {
					@Override
					public void run() {
						figure.setTickmarkColor(getTickmarkColor());
					}
				});
			}
			
		}
		
	}

}
