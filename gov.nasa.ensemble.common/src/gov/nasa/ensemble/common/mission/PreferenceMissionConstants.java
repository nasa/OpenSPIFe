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
package gov.nasa.ensemble.common.mission;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.util.Date;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class PreferenceMissionConstants extends MissionConstants {

	private static IEclipsePreferences getPreferences() {
		return getScope().getNode(CommonPlugin.ID);
	}
	
	@Override
	protected Date getDatePropertyValue(String property) {
		IEclipsePreferences store = getPreferences();
		
		try {
			String longString = store.get(property, null);
			if (longString != null) {
				return ISO8601DateFormat.parseISO8601(longString);
			}
		} catch (Exception e) {
			LogUtil.error("Parsing property '"+property+"'", e);
		}
		return super.getDatePropertyValue(property);
	}
	
	@Override
	protected boolean getBooleanPropertyValue(String property) {
		IEclipsePreferences store = getPreferences();
		try {
			if (store.get(property, null) != null) {
				return store.getBoolean(property, false);
			}
		} catch (Exception e) {
			LogUtil.error("Parsing property '"+property+"'", e);
		}
		return super.getBooleanPropertyValue(property);
	}
	
	@Override
	protected Integer getIntegerPropertyValue(String property) {
		IEclipsePreferences store = getPreferences();
		try {
			if (store.get(property, null) != null) {
				return store.getInt(property, -1);
			}
		} catch (Exception e) {
			LogUtil.error("Parsing property '"+property+"'", e);
		}
		return super.getIntegerPropertyValue(property);
	}

}
