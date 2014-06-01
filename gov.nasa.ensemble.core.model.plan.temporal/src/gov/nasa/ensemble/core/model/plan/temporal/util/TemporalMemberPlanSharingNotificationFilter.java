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
package gov.nasa.ensemble.core.model.plan.temporal.util;

import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.emf.patch.DisabledPlanSharingNotificationFilter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TemporalMemberPlanSharingNotificationFilter extends DisabledPlanSharingNotificationFilter {

	@Override
	public boolean matches(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof TemporalMember) {
			TemporalMember member = (TemporalMember) notifier;
			Object feature = notification.getFeature();
			if (feature instanceof EStructuralFeature) {
				if (member.isCalculated((EStructuralFeature) feature)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
