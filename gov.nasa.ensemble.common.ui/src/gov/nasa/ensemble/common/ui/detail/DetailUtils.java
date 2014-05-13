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
package gov.nasa.ensemble.common.ui.detail;

import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;

public class DetailUtils {

	public static IPreferenceStore PREFERENCES = WidgetPlugin.getDefault().getPreferenceStore();
	
	public static final String P_DISABLED_FILTER_FLAGS = "detail.filter.disabled";
	public static final String P_AVAILABLE_FILTER_FLAGS = "detail.filter.available";
	
	public static Collection<String> getAvailableFilterFlags() {
		return getFilterFlags(P_AVAILABLE_FILTER_FLAGS);
	}
	
	public static void addAvailableFilterFlags(String filterFlag) {
		addFilterFlag(P_DISABLED_FILTER_FLAGS, filterFlag);
	}
	
	public static void removeAvailableFilterFlags(String filterFlag) {
		removeFilterFlag(P_DISABLED_FILTER_FLAGS, filterFlag);
	}

	public static Collection<String> getDisabledFilterFlags() {
		return getFilterFlags(P_DISABLED_FILTER_FLAGS);
	}
	
	public static void addDisabledFilterFlags(String filterFlag) {
		addFilterFlag(P_DISABLED_FILTER_FLAGS, filterFlag);
	}
	
	public static void removeDisabledFilterFlags(String filterFlag) {
		removeFilterFlag(P_DISABLED_FILTER_FLAGS, filterFlag);
	}
	
	private static Collection<String> getFilterFlags(String property) {
		String string = PREFERENCES.getString(property);
		if (string == null) {
			return Collections.emptySet();
		}
		String[] array = StringConverter.asArray(string);
		return new HashSet<String>(Arrays.asList(array));
	}
	
	private static void removeFilterFlag(String property, String filterFlag) {
		Collection<String> flags = getFilterFlags(property);
		if (flags.remove(filterFlag)) {
			setFilterFlags(property, flags);
		}
	}
	
	private static void addFilterFlag(String property, String filterFlag) {
		Collection<String> flags = getFilterFlags(property);
		if (flags.add(filterFlag)) {
			setFilterFlags(property, flags);
		}
	}
		
	private static void setFilterFlags(String property, Collection<String> flags) {
		StringBuffer buffer = new StringBuffer();
		for (String flag : flags) {
			buffer.append(flag).append(" ");
		}
		PREFERENCES.setValue(property, buffer.toString());
	}
	
	public static final class DetailPreferenceInitializer extends PropertyPreferenceInitializer {

		private static final String FILTER_FLAG_EXPERT = "expert";

		public DetailPreferenceInitializer() {
			super(PREFERENCES);
		}

		@Override
		public void initializeDefaultPreferences() {
			setStringList(P_AVAILABLE_FILTER_FLAGS, Collections.singletonList(FILTER_FLAG_EXPERT));
			setStringList(P_DISABLED_FILTER_FLAGS, Arrays.asList(new String[0]));
		}
		
	}
	
}
