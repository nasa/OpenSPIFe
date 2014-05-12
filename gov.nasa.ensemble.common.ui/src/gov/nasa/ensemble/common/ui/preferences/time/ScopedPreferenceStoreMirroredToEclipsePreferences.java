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
package gov.nasa.ensemble.common.ui.preferences.time;

import gov.nasa.ensemble.common.CommonPlugin;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/** Mirrors all changes to the context, if the context supports it. 
 *  This gets around the problem (e.g. SPF-7252) that IPreferenceStore is part of JFace
 *  but MissionConstants subclasses need to consult these preferences and do not have a JFace dependency.
 **/
@SuppressWarnings("restriction")
public class ScopedPreferenceStoreMirroredToEclipsePreferences extends ScopedPreferenceStore {

	private EclipsePreferences mirrorToPreferences = null;

	public ScopedPreferenceStoreMirroredToEclipsePreferences(IScopeContext context, String qualifier) {
		super(context, qualifier);
		IEclipsePreferences preferences = context.getNode(CommonPlugin.ID);
		if (preferences instanceof EclipsePreferences) {
			mirrorToPreferences = (EclipsePreferences) preferences;
		}
	}

	@Override
	public void putValue(String name, String value) {
		if (mirrorToPreferences != null) mirrorToPreferences.put(name, value);
		super.putValue(name, value);
	}

	@Override
	public void setValue(String name, double value) {
		if (mirrorToPreferences != null) mirrorToPreferences.putDouble(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setValue(String name, float value) {
		if (mirrorToPreferences != null) mirrorToPreferences.putFloat(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setValue(String name, int value) {
		if (mirrorToPreferences != null) mirrorToPreferences.putInt(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setValue(String name, long value) {
		if (mirrorToPreferences != null) mirrorToPreferences.putLong(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setValue(String name, String value) {
		if (mirrorToPreferences != null) mirrorToPreferences.put(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setValue(String name, boolean value) {
		if (mirrorToPreferences != null) mirrorToPreferences.putBoolean(name, value);
		super.setValue(name, value);
	}

	@Override
	public void setToDefault(String name) {
		super.setToDefault(name);
		if (mirrorToPreferences != null) mirrorToPreferences.remove(name);
	}

}
