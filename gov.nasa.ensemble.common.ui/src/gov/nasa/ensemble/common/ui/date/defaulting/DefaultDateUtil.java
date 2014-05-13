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
package gov.nasa.ensemble.common.ui.date.defaulting;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.type.IStringifier;

import java.util.Date;

public abstract class DefaultDateUtil {
	
	/** 
	 * SPF-10620:  In Apex, AD-defined fields like ACR Start Time should default to Activity Start Time.
	 * @param eObject -- the idea is that this applies to EActivity (or other EPlanElement)
	 * @param stringifier -- only applies if it's the DateStringifier
	 * @param target 
	 * @return null if N/A or none found, or a default date.
	 */
	public static Date tryHarderToFindDefaultDateIfApplicable(Object target, IStringifier<?> stringifier) {
		for (IDefaultDateProvider provider : ClassRegistry.createInstances(IDefaultDateProvider.class)) {
			Date offer = provider.getDefaultDate(target);
			if (offer != null) {
				return offer;
			}
		}
		return null;
	}

}
