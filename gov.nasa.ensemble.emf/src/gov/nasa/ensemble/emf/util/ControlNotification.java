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
package gov.nasa.ensemble.emf.util;

import org.eclipse.emf.common.notify.Notification;

public interface ControlNotification extends Notification {
	
	// Picking a starting point for event type ints that is unlikely to clash
	// with other extensions
	public static final int EVENT_TYPE_CONTROL_START = 13330;
	
	// Recommended pause in the processing of notifications
	public static final int PAUSE = EVENT_TYPE_CONTROL_START + 1;
	
	// Resume the processing of notifications
	public static final int RESUME = EVENT_TYPE_CONTROL_START + 2;

}
