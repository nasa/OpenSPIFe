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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.IModelChangedListener;

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

public class PlanAdvisorModelChangedListener implements IModelChangedListener {

	private boolean force;
	
	public PlanAdvisorModelChangedListener() {
		this(false);
	}
	
	public PlanAdvisorModelChangedListener(boolean force) {
		this.force = force;
	}
	
	@Override
	public void enqueue(ResourceSetChangeEvent event) {
		if (!force && CommonPlugin.isJunitRunning()) {
			return;
		}
		EPlan plan = EPlanUtils.getPlanNotifications(event);
		if (plan != null) {
			List<Notification> notifications = event.getNotifications();
			PlanAdvisorMember advisorMember = PlanAdvisorMember.get(plan);
			//TODO FIXME: sometimes advisorMember is null
			// put in null check to get rid of annoying exceptions
			if (advisorMember != null) {
				advisorMember.enqueue(notifications);
			}
		}
	}

}
