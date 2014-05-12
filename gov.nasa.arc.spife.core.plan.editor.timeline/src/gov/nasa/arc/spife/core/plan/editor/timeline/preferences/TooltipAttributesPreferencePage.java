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
package gov.nasa.arc.spife.core.plan.editor.timeline.preferences;

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.core.plan.editor.lifecycle.AttributesPickListFieldEditor;

public class TooltipAttributesPreferencePage extends AbstractPreferencePage {

	public static final String P_TOOLTIP_ATTRIBUTES = "timeline.tooltip.attributes";
	private static TooltipAttributesPreferencePage tooltipAttributesPreferencePage;

	public TooltipAttributesPreferencePage() {
		super(Activator.getDefault().getPreferenceStore());
		setDescription("Activity attributes displayed.");
		tooltipAttributesPreferencePage = this;
	}

	public static TooltipAttributesPreferencePage getActiveInstance() {
		return tooltipAttributesPreferencePage;
	}

	@Override
	protected void createFieldEditors() {
		AttributesPickListFieldEditor pickListFieldEditor
			= new AttributesPickListFieldEditor(
				P_TOOLTIP_ATTRIBUTES,
				"Additional Tooltip Attributes:",
				getFieldEditorParent(),
				true);
		addField(pickListFieldEditor);
	}

}
