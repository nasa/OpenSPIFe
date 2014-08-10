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
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.common.extension.BasicExtensionRegistry;
import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class ExtensionPointResourceSetListener implements ResourceSetListener {

	// TODO: ORDER EXTENSIONS WITH PARTIAL ORDERING?
	private static final List<IModelChangedListener> MODEL_CHANGE_LISTENERS = getListenerInstances(IModelChangedListener.class, "gov.nasa.ensemble.emf.ModelChangedListener");

	// TODO: ORDER EXTENSIONS WITH PARTIAL ORDERING
	private static final List<IConsistencyMaintenanceListener> CONSISTENCY_MAINTENANCE_LISTENERS = getListenerInstances(IConsistencyMaintenanceListener.class, "gov.nasa.ensemble.emf.ConsistencyMaintenanceListener");

	private final List<? extends IModelChangedListener> modelChangedListeners;
	private final List<? extends IConsistencyMaintenanceListener> consistencyMaintenanceListeners;

	public static void addListener(TransactionalEditingDomain domain) {
		addListener(domain, false);
	}

	public static void addListener(TransactionalEditingDomain domain, boolean consistencyOnly) {
		List<IModelChangedListener> modelChangeListeners = Collections.<IModelChangedListener> emptyList();
		if (!consistencyOnly) {
			modelChangeListeners = MODEL_CHANGE_LISTENERS;
		}
		addListener(domain, CONSISTENCY_MAINTENANCE_LISTENERS, modelChangeListeners);
	}

	public static void addListener(TransactionalEditingDomain domain, List<? extends IConsistencyMaintenanceListener> consistencyMaintenanceListeners, List<? extends IModelChangedListener> modelChangeListeners) {
		ExtensionPointResourceSetListener listener = new ExtensionPointResourceSetListener(consistencyMaintenanceListeners, modelChangeListeners);
		domain.addResourceSetListener(listener);
		for (IModelChangedListener l : modelChangeListeners) {
			if (l instanceof IModelChangedListener.Impl) {
				((IModelChangedListener.Impl) l).activate(domain);
			}
		}
	}

	public ExtensionPointResourceSetListener(List<? extends IConsistencyMaintenanceListener> consistencyMaintenanceListeners, List<? extends IModelChangedListener> modelChangedListeners) {
		super();
		this.modelChangedListeners = modelChangedListeners;
		this.consistencyMaintenanceListeners = consistencyMaintenanceListeners;
	}

	/**
	 * Our transactions don't notify on touch, so just use ANY. (fast and cheap!)
	 */
	@Override
	public NotificationFilter getFilter() {
		return NotificationFilter.ANY;
	}

	@Override
	public boolean isAggregatePrecommitListener() {
		return true;
	}

	@Override
	public boolean isPostcommitOnly() {
		return false;
	}

	@Override
	public boolean isPrecommitOnly() {
		return modelChangedListeners.isEmpty();
	}

	@Override
	public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
		detectDuplicateNotifications(event);
		CompoundCommand compound = new SafeCompoundCommand("ConsistencyMaintenance");
		for (IConsistencyMaintenanceListener listener : consistencyMaintenanceListeners) {
			try {
				Command command = listener.createConsistencyMaintenanceCommand(event);
				if (command != null) {
					compound.append(command);
				}
			} catch (RuntimeException e) {
				String message = "RuntimeException from IConsistencyMaintenanceListener: " + listener.getClass();
				Logger.getLogger(ExtensionPointResourceSetListener.class).error(message, e);
			}
		}
		if (compound.isEmpty()) {
			return null;
		}
		return compound;
	}

	private int counter = 0;

	@Override
	public void resourceSetChanged(ResourceSetChangeEvent event) {
		Logger.getLogger(ExtensionPointResourceSetListener.class).debug("event: " + counter++);
		if (event.getNotifications().isEmpty()) {
			return;
		}
		if (isLoadNotification(event)) {
			// ignore notifications from loading
			return;
		}
		detectDuplicateNotifications(event);
		event = unmodifiableEvent(event);
		for (IModelChangedListener listener : modelChangedListeners) {
			try {
				listener.enqueue(event);
			} catch (RuntimeException e) {
				Logger logger = Logger.getLogger(ExtensionPointResourceSetListener.class);
				logger.error("RuntimeException from IModelChangedListener: " + listener.getClass(), e);
			}
		}
	}

	private ResourceSetChangeEvent unmodifiableEvent(ResourceSetChangeEvent event) {
		TransactionalEditingDomain source = (TransactionalEditingDomain) event.getSource();
		Transaction transaction = event.getTransaction();
		List<Notification> notifications = Collections.unmodifiableList(new ArrayList<Notification>(event.getNotifications()));
		return new ResourceSetChangeEvent(source, transaction, notifications);
	}

	private boolean warned = false;

	/**
	 * Check for duplicate notifications and remove them if present. This may occur if there is an adapter that is bubbling up notifications. Instead of bubbling up notifications within the model,
	 * developers should implement either IConsistencyMaintenanceListener or IModelChangedListener, both of which will be called with all notifications.
	 * 
	 * @param event
	 */
	private void detectDuplicateNotifications(ResourceSetChangeEvent event) {
		if (!warned) {
			List<Notification> notifications = event.getNotifications();
			ListIterator<Notification> notificationsIterator = notifications.listIterator();
			Notification lastNotification = null;
			while (notificationsIterator.hasNext()) {
				Notification notification = notificationsIterator.next();
				if (notification == lastNotification) {
					warned = true;
					String message = "detected duplicate notifications: events bubbling?";
					Logger.getLogger(ExtensionPointResourceSetListener.class).warn(message);
					break;
				}
				lastNotification = notification;
			}
		}
	}

	private boolean isLoadNotification(ResourceSetChangeEvent event) {
		for (Notification notification : event.getNotifications()) {
			if ((notification.getFeature() != null) || (notification.getEventType() != Notification.ADD) || (notification.getOldValue() != null) || !(notification.getNewValue() instanceof XMIException)) {
				return false;
			}
		}
		return true;
	}

	private static <T> List<T> getListenerInstances(Class<T> klass, String extensionId) {
		List<T> instances = new ArrayList<T>();
		instances.addAll(new BasicExtensionRegistry<T>(klass, extensionId).getInstances());
		instances.addAll(ClassRegistry.createInstances(klass));
		return instances;
	}

	private final class SafeCompoundCommand extends CompoundCommand {
		private List<Command> executed;

		private SafeCompoundCommand(String label) {
			super(label);
		}

		@Override
		public void execute() {
			executed = new ArrayList<Command>();
			for (Command command : commandList) {
				try {
					command.execute();
					executed.add(command);
				} catch (RuntimeException exception) {
					Logger logger = Logger.getLogger(ExtensionPointResourceSetListener.class);
					logger.error("exception in consistency command execute: " + command.getClass(), exception);
				}
			}
		}

		@Override
		public void undo() {
			for (Command command : executed) {
				try {
					command.undo();
				} catch (RuntimeException exception) {
					Logger logger = Logger.getLogger(ExtensionPointResourceSetListener.class);
					logger.error("exception in consistency command undo: " + command.getClass(), exception);
				}
			}
		}
	}

}
