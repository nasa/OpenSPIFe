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
package gov.nasa.arc.spife.ui.timeline.preference;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage.Tooltip;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

public class TimelinePreferenceInitializer extends PropertyPreferenceInitializer implements TimelineConstants {

	private static final String 	DEFAULT_ALTERNATING_COLOR = StringConverter.asString(new RGB(240,240,240));
	private static final boolean 	DEFAULT_HORIZONTAL_LINES_HIDE = false;
	private static final String 	DEFAULT_HORIZONTAL_LINES_COLOR = StringConverter.asString(ColorConstants.lightGray.getRGB());
	private static final String 	DEFAULT_VERTICAL_LINES_COLOR = StringConverter.asString(ColorConstants.lightGray.getRGB());
	private static final String 	DEFAULT_SCALE_TICKMARK_COLOR = StringConverter.asString(ColorConstants.lightGray.getRGB());
	private static final int 		DEFAULT_CONTENT_FONT_SIZE = (System.getProperty("os.name").toLowerCase().contains("linux")?20:19);
	private static final int 		DEFAULT_SCALE_FONT_SIZE = 12;
	private static final int 		DEFAULT_ROW_ELEMENT_HEIGHT = (System.getProperty("os.name").toLowerCase().contains("linux")?20:19);
	private static final boolean 	DEFAULT_ROW_ELEMENT_ROUNDED = false;
	private static final float 		DEFAULT_ROW_OVERLAP = 1.0f;
	private static final boolean 	DEFAULT_SNAP_TO_ACTIVE = false;
	private static final boolean 	DEFAULT_SNAP_TO_ORBIT_ACTIVE = false;
	private static final boolean 	DEFAULT_VIOLATIONS_VISIBLE = true;
	private static final int		DEFAULT_TOOLTIP_WIDTH = Integer.valueOf(300);

	private static final String 	DEFAULT_TOOLTIP_SPEED = Tooltip.FAST.toString();
	private static final String 	DEFAULT_TOOLTIP_TIME = String.valueOf(3000L);
	private static final boolean 	DEFAULT_CURSOR_TIME_ENABLED = true;
	private static final boolean 	P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS = true;
	private static final boolean	DEFAULT_PIN_ICON_VISIBLE = false;
	private static final boolean 	DEFAULT_MIN_MAX_CONSTRAINT_LINES_VISIBLE = false;

	public TimelinePreferenceInitializer() {
		super(TIMELINE_PREFERENCES);
	}

	@Override
	public void initializeDefaultPreferences() {
		setString	(TimelinePreferencePage.P_ALTERNATING_COLOR, DEFAULT_ALTERNATING_COLOR);
		setBoolean	(TimelinePreferencePage.P_HORIZONTAL_LINES_HIDE, DEFAULT_HORIZONTAL_LINES_HIDE);
		setString	(TimelinePreferencePage.P_HORIZONTAL_LINES_COLOR, DEFAULT_HORIZONTAL_LINES_COLOR);
		setString	(TimelinePreferencePage.P_VERTICAL_LINES_COLOR, DEFAULT_VERTICAL_LINES_COLOR);
		setString	(TimelinePreferencePage.P_SCALE_TICKMARK_COLOR, DEFAULT_SCALE_TICKMARK_COLOR);
		setInteger	(TimelinePreferencePage.P_CONTENT_FONT_SIZE, DEFAULT_CONTENT_FONT_SIZE);
		setInteger	(TimelinePreferencePage.P_SCALE_FONT_SIZE, DEFAULT_SCALE_FONT_SIZE);
		setInteger	(TimelinePreferencePage.P_INFO_FONT_SIZE, 10);
		setString	(TimelinePreferencePage.P_DECORATOR_TEXT_KEY, TimelinePreferencePage.P_DECORATOR_TEXT_KEY_NONE);
		setInteger	(TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT, DEFAULT_ROW_ELEMENT_HEIGHT);
		setFloat	(TimelinePreferencePage.P_ROW_ELEMENT_OVERLAP, DEFAULT_ROW_OVERLAP);
		setBoolean	(TimelinePreferencePage.P_SNAP_TO_ACTIVE, DEFAULT_SNAP_TO_ACTIVE);
		setBoolean	(TimelinePreferencePage.P_SNAP_TO_ORBIT_ACTIVE, DEFAULT_SNAP_TO_ORBIT_ACTIVE);
		setBoolean	(TimelinePreferencePage.P_ROW_ELEMENT_ROUNDED, DEFAULT_ROW_ELEMENT_ROUNDED);
		setString	(TimelinePreferencePage.P_TOOLTIP_SPEED, DEFAULT_TOOLTIP_SPEED);
		setString	(TimelinePreferencePage.P_TOOLTIP_TIME, DEFAULT_TOOLTIP_TIME);
		setBoolean 	(TimelineConstants.VIOLATIONS_VISIBLE, DEFAULT_VIOLATIONS_VISIBLE);
		setInteger  (TimelineConstants.TOOLTIP_WIDTH, DEFAULT_TOOLTIP_WIDTH);
		setLong     (TimelinePreferencePage.P_TOOLTIP_TIME, Long.valueOf(DEFAULT_TOOLTIP_TIME));
		setBoolean	(TimelinePreferencePage.P_CURSOR_TIME_ENABLED, DEFAULT_CURSOR_TIME_ENABLED);
		setBoolean	(TimelinePreferencePage.P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS, P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS);
		setBoolean	(TimelinePreferencePage.P_PIN_ICON_VISIBLE, DEFAULT_PIN_ICON_VISIBLE);
		setBoolean	(TimelinePreferencePage.P_MIN_MAX_CONSTRAINT_LINES_VISIBLE, DEFAULT_MIN_MAX_CONSTRAINT_LINES_VISIBLE);
	}

}
