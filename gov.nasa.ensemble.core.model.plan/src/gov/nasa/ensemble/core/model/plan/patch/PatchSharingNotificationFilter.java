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
package gov.nasa.ensemble.core.model.plan.patch;

import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.patch.DisabledPlanSharingNotificationFilter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class PatchSharingNotificationFilter extends DisabledPlanSharingNotificationFilter {

	private static final String PATCH_URI = "http://plan/patch";

	@Override
	public boolean matches(Notification notification) {
		Object notifier = notification.getNotifier();
		if (notifier instanceof ResourceSet) {
			Object newValue = notification.getNewValue();
			Object oldValue = notification.getOldValue();
			if (newValue instanceof Resource && ((Resource) newValue).getURI().toString().equals(PATCH_URI) ||
				oldValue instanceof Resource && ((Resource) oldValue).getURI().toString().equals(PATCH_URI))
				return true;
		}
		if (notifier instanceof Resource && ((Resource) notifier).getURI().toString().equals(PATCH_URI)) {
			return true;
		}
		return (notifier instanceof Patch || notifier instanceof PatchFeatureChange || notifier instanceof PatchListChange); 
	}

}
