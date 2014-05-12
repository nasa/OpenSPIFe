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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;

import java.util.Date;

import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.preference.IPreferenceStore;

public interface TimelineConstants extends RequestConstants {

	public static final IPreferenceStore TIMELINE_PREFERENCES 	= Activator.getDefault().getPreferenceStore();

	public static final String TIMELINE_FILE_EXT = "timeline";
	
	// ------------------------------------------------------------------------
	// Property
	// ------------------------------------------------------------------------

	public static final String TIMELINE_GROUP_ELEMENTS = "timeline.plan.grouped";
	
	public static final String TIMELINE_EVENT_VERTICAL_REFRESH = "timeline.vertical.refresh";

	// ------------------------------------------------------------------------
	// Stringifiers
	// ------------------------------------------------------------------------

	public static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	// ------------------------------------------------------------------------
	// Layers
	// ------------------------------------------------------------------------

	public static final String LAYER_FEEDBACK_DATA = "Timeline Data Feedback Layer";

	public static final String LAYER_FEEDBACK_HEADER = "Timeline Header Feedback Layer";

	public static final String LAYER_DATA_PRIMARY_LAYER = "Data Primary Layer";

	public static final String LAYER_DATA_MARKER_LAYER = "Data Marker Layer";

	// ------------------------------------------------------------------------
	// Requests
	// ------------------------------------------------------------------------

	public static final String REQ_MOVE_INITIATED = "move initiated";

	public static final String REQ_MOVE_COMPLETED = "move completed";

	public static final String REQ_ROW_DATA_LAYOUT = "row data layout";

	public static final String REQ_MOVE_CHILDREN_INITIATED = "move children initiated";

	public static final String REQ_MOVE_CHILDREN_COMPLETED = "move children completed";

	public static final String REQ_ERASE_TOOLTIP_FEEDBACK = "erase tooltip feedback";

	public static final String REQ_CHANGE_START_TIME_INITIATED = "change start time initiated";

	public static final String REQ_CHANGE_END_TIME_INITIATED = "change end time initiated";

	public static final String REQ_CHANGE_START_TIME_COMPLETED = "change start time completed";

	public static final String REQ_CHANGE_END_TIME_COMPLETED = "change end time completed";

	public static final String REQ_CHANGE_VALUE_VIA_DROP = "change value via drop";
	
	public static final String REQ_ADD_VALUE_VIA_DROP = "add value via drop";
	
	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	public static final String P_DISPLAY_INSTANTANEOUS = "P_DISPLAY_INSTANTANEOUS";

	public static final String VIOLATIONS_VISIBLE = "timeline.violations.visible";

	public static final String TOOLTIP_WIDTH = "timeline.tooltip.temporal.node.value.width";
	
	public static final String CURSOR_TIME = "timeline.cursor.time";
	
	public enum ScrollAlignment {
		CENTER, LEFT, RIGHT
	}	

}
