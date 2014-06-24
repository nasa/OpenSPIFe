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
package gov.nasa.ensemble.core.plan.advisor.markers;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.advisor.Violation;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class MarkerViolationUtils {

	private static final Map<IMarker, Violation> markerToViolationMap = Collections.synchronizedMap(new WeakHashMap<IMarker, Violation>());
	
	public static Violation getMarkerViolation(IMarker marker) {
		try {
			Object source = marker.getAttribute(IMarker.SOURCE_ID);
			if (source instanceof Violation) {
				return (Violation)source;
			}
			return markerToViolationMap.get(marker);
		} catch (CoreException e) {
			LogUtil.error("fetching violation from marker", e);
		}
		return null;
	}
	
	public static void putMarkerViolation(IMarker marker, Violation violation) {
		markerToViolationMap.put(marker, violation);
	}
	
}
