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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Date;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class MarkerUtils {

	public static Date getDate(IMarker marker, String attribute) {
		Object value;
		try {
			value = marker.getAttribute(attribute);
		} catch (CoreException e) {
			LogUtil.warnOnce(e);
			return null;
		}
		if (value instanceof String) {
			String string = (String) value;
			long milliseconds;
			try {
				milliseconds = Long.valueOf(string);
			} catch (NumberFormatException e) {
				LogUtil.warnOnce(e);
				return null;
			}
			return new Date(milliseconds);
		}
		return (Date)value;
	}
	
}
