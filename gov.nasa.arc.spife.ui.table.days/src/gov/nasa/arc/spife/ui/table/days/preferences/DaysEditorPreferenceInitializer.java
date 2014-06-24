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
package gov.nasa.arc.spife.ui.table.days.preferences;

import gov.nasa.arc.spife.ui.table.days.DaysPlugin;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;

import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * Class used to initialize default preference values.
 */
public class DaysEditorPreferenceInitializer extends PropertyPreferenceInitializer {
	
	public static final String DAYS_EDITOR_DOMAIN = "days";
	public static final String FONT_SIZE = DAYS_EDITOR_DOMAIN + ".fontsize"; 
	public static final String UNSCHEDULED_AT_END = DAYS_EDITOR_DOMAIN + ".unscheduled.at.end"; 
	
	public DaysEditorPreferenceInitializer() {
		super(DaysPlugin.getDefault().getPreferenceStore());
	}

	@Override
	public void initializeDefaultPreferences() {
		setInteger(DaysEditorPreferencePage.P_DAYS_EDITOR_FONT_SIZE, FONT_SIZE, getDefaultFontSize());
		setBoolean(DaysEditorPreferencePage.P_DAYS_EDITOR_EDITOR_UNSCHEDULED_AT_END, UNSCHEDULED_AT_END, true);
	}

	private int getDefaultFontSize() {
		FontData fd = Display.getDefault().getSystemFont().getFontData()[0];
		return fd.getHeight();
	}

}
