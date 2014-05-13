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
package gov.nasa.arc.spife;

import gov.nasa.ensemble.common.EnsembleProperties;


public class PageUtils {

	public static final String PAGING_ENABLED = "timeline.paging.enabled";
	
	private static Boolean isPagingEnabled = null;
	
	public static boolean isPagingEnabled() {
		if (isPagingEnabled == null) {
			isPagingEnabled = false;
			String propertyValueString = EnsembleProperties.getProperty(PAGING_ENABLED);
			isPagingEnabled = (propertyValueString != null) && Boolean.parseBoolean(propertyValueString);
		}
		return isPagingEnabled;
	}
	
}
