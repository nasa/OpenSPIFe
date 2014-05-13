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
package gov.nasa.ensemble.core.model.plan.temporal;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import gov.nasa.ensemble.common.ui.date.defaulting.IDefaultDateProvider;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

public class DefaultDateProvider implements IDefaultDateProvider {

	@Override
	public Date getDefaultDate(Object target) {
		if (target==null) {
			return null;
		} else if (target instanceof EPlanElement) {
			EPlanElement element = (EPlanElement) target;
			TemporalMember times = element.getMember(TemporalMember.class);
			if (times != null) {
				Date time = times.getStartTime();
				if (time != null) return time;
				time = times.getEndTime();
				if (time != null) return time;
			}
		}
		if (target instanceof EObject) {
			return getDefaultDate(((EObject) target).eContainer()); // e.g. an activity's data's container is the activity
		} else {
			return null;
		}
	}

}
