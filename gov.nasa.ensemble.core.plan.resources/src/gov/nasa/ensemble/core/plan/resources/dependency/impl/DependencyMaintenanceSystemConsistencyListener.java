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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;
import gov.nasa.ensemble.emf.transaction.IConsistencyMaintenanceListener;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class DependencyMaintenanceSystemConsistencyListener implements IConsistencyMaintenanceListener {

	@Override
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event) {
		EPlan plan = EPlanUtils.getPlanNotifications(event);
		if (plan != null) {
			List<Notification> notifications = event.getNotifications();
			return getPlanCommand(event.getEditingDomain(), plan, notifications);
		}
		return null;
	}

	private Command getPlanCommand(TransactionalEditingDomain editingDomain, EPlan plan, final List<Notification> notifications) {
		ResourceUpdater resourceUpdater = WrapperUtils.getMember(plan, ResourceUpdater.class);
		if (resourceUpdater == null) {
			return null;
		}
		final DependencyMaintenanceSystem dms = resourceUpdater.getDependencyMaintenanceSystem();
		if (dms != null) {
			return new DependencyMaintenanceSystemConsistencyCommand(editingDomain, dms, notifications);
		}
		return null;
	}

	private static final class DependencyMaintenanceSystemConsistencyCommand extends RecordingCommand {
		
		private final DependencyMaintenanceSystem dms;
		private final List<Notification> notifications;

		private DependencyMaintenanceSystemConsistencyCommand(TransactionalEditingDomain domain, DependencyMaintenanceSystem dms, List<Notification> notifications) {
			super(domain);
			this.dms = dms;
			this.notifications = notifications;
		}

		@Override
		protected void doExecute() {
			dms.clearAboutToBeRemovedMap();
			for (Notification notification : notifications) {
				dms.processNotification(notification);
			}
			dms.removeActivities();
		}
		
	}

}
