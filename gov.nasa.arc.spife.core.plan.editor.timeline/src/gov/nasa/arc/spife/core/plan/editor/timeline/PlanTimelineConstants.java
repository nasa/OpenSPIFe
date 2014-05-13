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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;

import org.eclipse.jface.preference.IPreferenceStore;

public interface PlanTimelineConstants extends TimelineConstants {

	public static final IPreferenceStore PLAN_TIMELINE_PREFERENCES = TimelinePlugin.getDefault().getPreferenceStore();

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	public static final String PLAN_CONSTRAINTS_VISIBLE = "timeline.constraints.visible";

}
