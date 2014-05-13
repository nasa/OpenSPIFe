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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.PlanService;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.PreCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class PlanHierarchyPositionMaintenerService extends PlanService {
	
	private static final boolean hierarchyPositionMaintained = EnsembleProperties.getBooleanPropertyValue("plan.hierarchy.position.maintained", false);
	private HierachyListener listener = new HierachyListener();
	
	public PlanHierarchyPositionMaintenerService(EPlan plan) {
		super(plan);
	}
	
	@Override
	public void activate() {
		if (isHierarchyPositionMaintained()) {
			updateHierarchyPositions();
			EditingDomain editingDomain = getEditingDomain();
			if (editingDomain instanceof TransactionalEditingDomain) {
				((TransactionalEditingDomain) editingDomain).addResourceSetListener(listener);
			}
		}
	}
	
	@Override
	public void deactivate() {
		if (isHierarchyPositionMaintained()) {
			EditingDomain editingDomain = getEditingDomain();
			if (editingDomain instanceof TransactionalEditingDomain) {
				((TransactionalEditingDomain) editingDomain).removeResourceSetListener(listener);
			}
		}
	}
	
	public static boolean isHierarchyPositionMaintained() {
		return hierarchyPositionMaintained;
	}
	
	/**
	 * Visits all elements of the plan and sets the hierarchy position if it's not equal to the current one.
	 * @param plan
	 */
	private synchronized void updateHierarchyPositions() {
			TransactionUtils.writing(getPlan(), new Runnable() {
				@Override
				public void run() {
					final AtomicInteger count = new AtomicInteger(1);
					new PlanVisitor(true) {
						@Override
						public void visit(final EPlanElement element) {
							if (element instanceof EPlanChild) {
								final int value = count.getAndIncrement();
								((EPlanChild) element).setHierarchyPosition(value);
							}
						}
					}.visitAll(getPlan());
				}
			});
	}

	/**
	 * Calculates the hierarchy position of an EPlanElement.  
	 * It has to traverse the whole plan so don't call this too many times.
	 * Only used when the hierarchy position is requested and it's not being maintained.
	 * 
	 * @param target
	 * @return
	 */
	public static int getHierarchyPosition(final EPlanElement target) {
		final AtomicInteger result = new AtomicInteger(-1);
		final AtomicInteger count = new AtomicInteger();
		EPlan plan = EPlanUtils.getPlan(target);
		new PlanVisitor() {
			private boolean found = false;
			@Override
			public void visit(EPlanElement element) {
				if (!found) {
					if (element == target) {
						found = true;
						result.set(count.get());
					}
					count.getAndIncrement();
				} 
			}
		}.visitAll(plan);
		return result.get();
	}
	

	private class HierachyListener extends PostCommitListener {
		
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			if (isHierarchyPositionMaintained())  {
				List<Notification> notifications = event.getNotifications();
				for (Notification notification : notifications) {
					if (CommonUtils.equals(PlanPackage.Literals.EPLAN_PARENT__CHILDREN, notification.getFeature())) {
						HierarchyUpdatingThread updatingThread = new HierarchyUpdatingThread();
						updatingThread.schedule();
						return;
					}
				}
			}
		}
		
		private final class HierarchyUpdatingThread extends Job {

			public HierarchyUpdatingThread() {
				super("Updating Hierarchy");
			}

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				updateHierarchyPositions();
				return Status.OK_STATUS;
			}
		}

	}
	

}
