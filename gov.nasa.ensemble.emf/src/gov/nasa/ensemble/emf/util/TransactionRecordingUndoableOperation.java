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
package gov.nasa.ensemble.emf.util;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ResourceChange;
import org.eclipse.emf.ecore.change.impl.ChangeDescriptionImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionChangeRecorder;

/**
 * Records model changes automatically upon initial execute and uses the recording
 * on subsequent execute and undo calls of the operation.
 * 
 * This class is intended to be used to retrofit model changes with undo functionality
 * and should not be used as a catch all method for undo/redo compliance.
 *  
 * @author aaghevli
 */
public class TransactionRecordingUndoableOperation extends AbstractTransactionUndoableOperation {

	/**
	 * Runnable to invoke on initial execute, subsequent executions will
	 * use the changeDescription
	 */
	private final Runnable runnable;
	
	/**
	 * Change description element created after the fist execution
	 */
	private ChangeDescription changeDescription = null;
	
	/**
	 * Domain to use for recording
	 */
	private final InternalTransactionalEditingDomain domain;
	
	public TransactionRecordingUndoableOperation(String label, EditingDomain domain, Runnable runnable) {
		super(label);
		this.domain = (InternalTransactionalEditingDomain) domain;
		this.runnable = runnable;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}

	@Override
	protected void execute() throws Throwable {
		final TransactionChangeRecorder recorder = new TransactionChangeRecorder(domain, domain.getResourceSet()) {

			@Override
			protected ChangeDescription createChangeDescription() {
				return new TransactionRecordingUndoableOperationChangeDescription();
			}
			
			@Override
			protected void appendNotification(Notification notification) {
				// do not append, the main transaction recorder manages that
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

		};
		recorder.beginRecording();
		TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				runnable.run();
				//SPF-10946 -- End the recording and dispose of the recorder within the scope of the write transaction
				// so that changes made by the ResourceUpdater won't be recorded and a possible ConcurrentModification
				// exception during the recorder disposal will be avoided
				changeDescription = recorder.endRecording();
				recorder.dispose();
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				changeDescription.applyAndReverse();
			}
		});
	}

	@Override
	protected void redo() throws Throwable {
		TransactionUtils.writing(domain, new Runnable() {
			@Override
			public void run() {
				changeDescription.applyAndReverse();
			}
		});
	}

	@Override
	public String toString() {
		return null;
	}

	private static class TransactionRecordingUndoableOperationChangeDescription extends ChangeDescriptionImpl {

		private static final int KIND_REFERENCE_SINGLE = 2;
		private static final int KIND_REFERENCE_MANY = 1;
		private static final int KIND_FEATURE_MAP = 3;

		@Override
		public void applyAndReverse() {
			preApply(true);

			List<EObject> objectsBeforeApply = new UniqueEList.FastCompare<EObject>();
			List<EObject> objectsAfterApply = new UniqueEList.FastCompare<EObject>();

			// Apply the change and reverse the change information.
			//
			for (Map.Entry<EObject, EList<FeatureChange>> entry : getObjectChanges()) {
				try {
					applyAndReverseObjectChanges(entry, objectsBeforeApply, objectsAfterApply);
				} catch (Exception e) {
					LogUtil.errorOnce("applying object change: " + e.getMessage());
				}
			}

			for (ResourceChange resourceChange : getResourceChanges()) {
				Resource resource = resourceChange.getResource();
				if (resource != null) {
					objectsBeforeApply.addAll(resource.getContents());
				}
				resourceChange.applyAndReverse();
				if (resource != null) {
					objectsAfterApply.addAll(resource.getContents());
				}
			}

			// The next line leaves objectsBeforeApply with all the objects that
			// were
			// added during the last recording.
			objectsBeforeApply.removeAll(objectsAfterApply);

			// Reverse the objects to attach and detach lists.
			//
			getObjectsToAttach().clear();
			for (EObject eObject : objectsBeforeApply) {
				if (eObject.eContainer() == null && eObject.eResource() == null) {
					getObjectsToAttach().add(eObject);
				}
			}
			oldContainmentInformation = null;
		}

		private void applyAndReverseObjectChanges(Map.Entry<EObject, EList<FeatureChange>> entry, List<EObject> objectsBeforeApply, List<EObject> objectsAfterApply) {
			EObject objectToChange = entry.getKey();
			for (FeatureChange featureChange : entry.getValue()) {
				EStructuralFeature feature = featureChange.getFeature();

				int featureKind =
						feature != null && FeatureMapUtil.isFeatureMap(feature) ?
								KIND_FEATURE_MAP :
								feature instanceof EReference && ((EReference) feature).isContainment() ?
										feature.isMany() ?
												KIND_REFERENCE_MANY :
												KIND_REFERENCE_SINGLE :
										0;
				switch (featureKind) {
					case KIND_REFERENCE_MANY: {
						@SuppressWarnings("unchecked")
						List<EObject> beforeValue = (List<EObject>) objectToChange.eGet(feature);
						objectsBeforeApply.addAll(beforeValue);
						break;
					}
					case KIND_REFERENCE_SINGLE: {
						Object value = objectToChange.eGet(feature);
						if (value != null) {
							objectsBeforeApply.add((EObject) objectToChange.eGet(feature));
						}
						break;
					}
					case KIND_FEATURE_MAP: {
						@SuppressWarnings("unchecked")
						List<FeatureMap.Entry> beforeValue = (List<FeatureMap.Entry>) objectToChange.eGet(feature);
						objectsBeforeApply.addAll(getContainedEObjects(beforeValue));
						break;
					}
					}
	
					featureChange.applyAndReverse(objectToChange);
	
					switch (featureKind) {
					case KIND_REFERENCE_MANY: {
						@SuppressWarnings("unchecked")
						List<EObject> afterValue = (List<EObject>) objectToChange.eGet(feature);
						objectsAfterApply.addAll(afterValue);
						break;
					}
					case KIND_REFERENCE_SINGLE: {
						Object value = objectToChange.eGet(feature);
						if (value != null) {
							objectsAfterApply.add((EObject) objectToChange.eGet(feature));
						}
						break;
					}
					case KIND_FEATURE_MAP: {
						@SuppressWarnings("unchecked")
						List<FeatureMap.Entry> afterValue = (List<FeatureMap.Entry>) objectToChange.eGet(feature);
						objectsAfterApply.addAll(getContainedEObjects(afterValue));
						break;
					}
				}
			}
		}

	}

}
