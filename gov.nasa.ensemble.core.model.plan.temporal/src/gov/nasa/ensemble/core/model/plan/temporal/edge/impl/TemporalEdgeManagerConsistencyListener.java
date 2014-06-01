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
package gov.nasa.ensemble.core.model.plan.temporal.edge.impl;

import gov.nasa.ensemble.emf.transaction.IConsistencyMaintenanceListener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class TemporalEdgeManagerConsistencyListener implements IConsistencyMaintenanceListener {

	@Override
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event) {
		final TemporalEdgeManager manager = getTemporalEdgeManager(event);
		if (manager != null) {
			final List<Notification> filtered = new ArrayList<Notification>();
			for (Notification notification : event.getNotifications()) {
				if (manager.isImportant(notification)) {
					filtered.add(notification);
				}
			}
			if (!filtered.isEmpty()) {
				return new TemporalEdgeManagerConsistencyCommand(event.getEditingDomain(), "TemporalEdgeManagerConsistency", manager, filtered);
			}
		}
		return null;
	}
	
	public TemporalEdgeManager getTemporalEdgeManager(ResourceSetChangeEvent event) {
		for (Resource resource : event.getEditingDomain().getResourceSet().getResources()) {
			for (EObject eObject : resource.getContents()) {
				if (eObject instanceof TemporalEdgeManager) {
					return (TemporalEdgeManager) eObject;
				}
			}
		}
		return null;
	}
	
	private static final class TemporalEdgeManagerConsistencyCommand extends RecordingCommand {
		
		private final TemporalEdgeManager manager;
		private final List<Notification> filtered;

		private TemporalEdgeManagerConsistencyCommand(TransactionalEditingDomain domain, String label, TemporalEdgeManager manager, List<Notification> filtered) {
			super(domain, label);
			this.manager = manager;
			this.filtered = filtered;
		}

		@Override
		protected void doExecute() {
			manager.processNotifications(filtered);
		}
		
	}

}
