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
package gov.nasa.ensemble.common.ui.editor;

import org.eclipse.core.resources.IMarker;

public interface MarkerConstants {

	/*
	 * ====================================================================
	 * 
	 * Marker types:
	 * 
	 * ====================================================================
	 */

	/**
	 * Base marker type.
	 */
	public static final String TEMPORAL_MARKER = "gov.nasa.ensemble.core.plan.editor.temporalmarker";
	public static final String OVERLAY_MARKER = "gov.nasa.ensemble.core.plan.editor.overlaymarker";

	/*
	 * ====================================================================
	 * 
	 * Marker attributes:
	 * 
	 * ====================================================================
	 */

	/**
	 * Short name associated with the marker
	 */
	public static final String NAME = "name";
	
	/**
	 * Color attribute if any. Use RGB to specify.
	 */
	public static final String COLOR = "color";

	/**
	 * Duration represented as an Amount<Duration>
	 */
	public static final String DURATION = "duration";

	/**
	 * Start time represented as a long String or a Date
	 */
	public static final String START_TIME = "startTime";
	
	/**
	 * End time represented as a long String
	 */
	public static final String END_TIME = "endTime";

	/**
	 * Id of the plugin which defines the marker
	 */
	public static final String PLUGIN_ID = "PLUGIN_ID";

	/**
	 * key value indicating a path to the image descriptor to be used with the marker (relative to the plugin).
	 */
	public static final String IMAGE_DESCRIPTOR_PATH = "IMAGE_DESCRIPTOR_PATH";

	/**
	 * path to the image descriptor to be used with the marker (relative to the plugin).
	 */
	public static final String OVERLAY_MARKER_IMAGE_DESCRIPTOR_PATH = "icons/timeline_overlay.gif";

	public static final String TOOLTIP_TEXT = "TOOLTIP_TEXT";

	/**
	 * an addition to the IMarker.SEVERITY attributes
	 */
	public static final int SEVERITY_WAIVED = IMarker.SEVERITY_INFO - 100;
	
	/**
	 * an addition to the IMarker.SEVERITY attributes
	 */
	public static final int SEVERITY_FIXED = IMarker.SEVERITY_INFO - 200;

}
