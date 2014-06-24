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
/**
 * 
 */
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.emf.util.ControlNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;

public class FixedTransactionChangeRecorder extends TransactionChangeRecorder {

	private boolean disposed;
	
	public FixedTransactionChangeRecorder(InternalTransactionalEditingDomain domain, ResourceSet rset) {
		super(domain, rset);
	}

	@Override
	public void dispose() {
		disposed = true;
		super.dispose();
	}
	
	@Override
	public void notifyChanged(final Notification notification) {
		if (disposed) {
			// @see TransactionChangeRecorder.notifyChanged
			// I am disposed, so I should no longer be responding to or even
			//    receiving events.  Moreover, I should not propagate myself
			//    to any elements that are later added to me!
			if ((notification.getEventType() != Notification.REMOVING_ADAPTER)
				|| (notification.getOldValue() != this)) {
				Object notifier = notification.getNotifier();
				if (notifier instanceof Notifier) {
					removeAdapter((Notifier) notifier);
				}
			}
			return;
		}
		try {
			if (notification.getEventType() == Notification.SET) {
				if (notification.getOldValue() == notification.getNewValue()) {
					return;
				}
			}
			switch (notification.getEventType()) {
			case Notification.RESOLVE:
			case ControlNotification.PAUSE:
			case ControlNotification.RESUME:
				break;
			default:
				if (notification.getNotifier() instanceof Resource 
					&& (notification.getFeatureID(Resource.class) != Resource.RESOURCE__CONTENTS)) {
					break;
				}
				InternalTransactionalEditingDomain internalDomain = getEditingDomain();
				InternalTransaction activeTransaction = internalDomain.getActiveTransaction();
				if (activeTransaction != null) {
					// There is already an active transaction
					Thread owner = activeTransaction.getOwner();
					Thread currentThread = Thread.currentThread();
					if (owner != currentThread) {
						TransactionUtils.writing(internalDomain, new Runnable() {
							@Override
							public void run() {
								performNotifyChanged(notification);
							}
						});
						return;
					}
				}
			}
			performNotifyChanged(notification);
		} catch (UnsupportedOperationException e) { 
			/* ignore from CDOInvalidationNotification */
		}
	}
	
	private void performNotifyChanged(final Notification notification) {
		if (notification.getNotifier() instanceof Resource 
				&& ((notification.getFeatureID(Resource.class) == Resource.RESOURCE__CONTENTS)
					|| (notification.getFeatureID(Resource.class) == Resource.RESOURCE__TIME_STAMP))) {
			handleResource(notification);
		} else {
			super.notifyChanged(notification);
		}
	}
	
	@Override
	protected void handleFeature(EStructuralFeature feature,
			EReference containment, Notification notification,
			EObject object) {
		super.handleFeature(feature, containment, notification, object);
		switch (notification.getEventType()) {
		case Notification.REMOVE: {
			Object oldValue = notification.getOldValue();
			if (oldValue instanceof Notifier) {
				Notifier oldNotifier = (Notifier) oldValue;
				removeAdapter(oldNotifier);
			}
			break;
		}
		case Notification.REMOVE_MANY: {
			@SuppressWarnings("unchecked")
			Collection<Object> oldValues = (Collection<Object>)notification.getOldValue();
			for (Object oldValue : oldValues) {
				if (oldValue instanceof Notifier) {
					removeAdapter((Notifier) oldValue);
				}
			}
			break;
		}
		}
	}

	@Override
	protected void handleResource(Notification notification) {
		if (Resource.RESOURCE__TIME_STAMP == notification.getFeatureID(Resource.class))
			return;
		super.handleResource(notification);
		switch (notification.getEventType()) {
		case Notification.REMOVE: {
			Notifier oldValue = (Notifier)notification.getOldValue();
			removeAdapter(oldValue);
			break;
		}
		case Notification.REMOVE_MANY: {
			@SuppressWarnings("unchecked")
			Collection<Notifier> oldValues = (Collection<Notifier>)notification.getOldValue();
			for (Notifier oldValue : oldValues) {
				removeAdapter(oldValue);
			}
			break;
		}
		}
	}
	
	@Override
	public void setTarget(Notifier target) {
		super.setTarget(target);
		for (Adapter adapter : new ArrayList<Adapter>(target.eAdapters()))
			if (this != adapter && adapter instanceof FixedTransactionChangeRecorder)
				// MAE-3374 there should be at most one FTCR per object, so remove others
				target.eAdapters().remove(adapter);
	}
	
	@Override
	public void unsetTarget(Notifier oldTarget) {
		super.unsetTarget(oldTarget);
		Iterator<?> contents = 
		      oldTarget instanceof EObject ? 
		        resolveProxies ?  
		          ((EObject)oldTarget).eContents().iterator() : 
		          ((InternalEList<?>)((EObject)oldTarget).eContents()).basicIterator() :
		        oldTarget instanceof ResourceSet ? 
		          ((ResourceSet)oldTarget).getResources().iterator() : 
		            oldTarget instanceof Resource ? 
		              ((Resource)oldTarget).getContents().iterator() : 
		                null;
	    if (contents != null)
	    {
	      while (contents.hasNext())
	      {
	        Notifier notifier = (Notifier)contents.next();
	        removeAdapter(notifier);
	      }
	    }
	}

	@Override
	public ChangeDescription endRecording() {
		try {
			return super.endRecording();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(FixedTransactionChangeRecorder.class);
			logger.error("consolidateChanges error", e);
			return null;
		}
	}
	
	@Override
	protected void consolidateChanges() {
		try {
			super.consolidateChanges();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(FixedTransactionChangeRecorder.class);
			logger.error("consolidateChanges error", e);
		}
	}
	
	@Override
	protected void eliminateEmptyChanges() {
		try {
			super.eliminateEmptyChanges();
		} catch (Exception e) {
			Logger logger = Logger.getLogger(FixedTransactionChangeRecorder.class);
			logger.error("eliminateEmptyChanges error", e);
		}
	}

	@Override
	protected void removeAdapter(Notifier notifier) {
		if (notifier != null) {
			super.removeAdapter(notifier);
		}
	}
	
}
