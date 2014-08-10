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
package gov.nasa.ensemble.core.detail.emf.treetable;

import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.Viewer;

public class EMFTreeTableContentProvider extends AdapterFactoryContentProvider {

	private final EStructuralFeature structuralFeature;
	private final EClassifier eClassifier;
	private final ModelChangeListenerImpl listener = new ModelChangeListenerImpl();

	private EObject input = null;
	private TransactionalEditingDomain domain = null;

	public EMFTreeTableContentProvider(AdapterFactory adapterFactory, EStructuralFeature structuralFeature, EClassifier eClassifier) {
		super(adapterFactory);
		this.structuralFeature = structuralFeature;
		this.eClassifier = eClassifier;
	}

	public EStructuralFeature getStructuralFeature() {
		return structuralFeature;
	}

	@Override
	public void dispose() {
		super.dispose();
		input = null;
		domain = null;
	}

	/**
	 * Override so that the only things returned are the children accessed via the eReference parameter. Otherwise, all of the children of the object would be returned... which could result in a
	 * heterogeneous list of types. Filter on the eClass type if different than the canonical EReference type.
	 */
	@Override
	public Object[] getElements(Object object) {
		List<Object> filtered = new ArrayList<Object>();
		if (structuralFeature instanceof EReference) {
			EReference reference = (EReference) structuralFeature;
			if (object instanceof EcoreEMap) {
				Collection values = ((EcoreEMap) object).values();
				filtered.addAll(values);
			} else if (object instanceof EObject) {
				List<EObject> list = EMFUtils.eGetAsList((EObject) object, reference);
				if (eClassifier == reference.getEReferenceType()) {
					filtered.addAll(list);
				} else {
					for (EObject o : list) {
						if (((EClass) eClassifier).isSuperTypeOf(o.eClass())) {
							filtered.add(o);
						}
					}
				}
			}
		} else {
			EObject eObject = (EObject) object;
			Object eGet = eObject.eGet(structuralFeature);
			if (eGet instanceof List) {
				int size = ((List) eGet).size();
				EObjectIndexPair[] items = new EObjectIndexPair[size];
				for (int i = 0; i < size; i++) {
					items[i] = new EObjectIndexPair(eObject, i);
				}
				return items;
			}
		}

		return filtered.toArray(new Object[filtered.size()]);
	}

	/**
	 * This is really more of a table implementation for now, cannot guarantee that the children will be of the same type, therefore we will only show top level elements
	 */
	@Override
	public Object[] getChildren(Object object) {
		return new Object[0];
	}

	/**
	 * This is really more of a table implementation for now, cannot guarantee that the children will be of the same type, therefore we will only show top level elements
	 */
	@Override
	public boolean hasChildren(Object object) {
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (domain != null) {
			domain.removeResourceSetListener(listener);
		}
		super.inputChanged(viewer, oldInput, newInput);
		//
		// We want to 'activate' the content providers and listeners
		adapterFactory.adapt(newInput, IStructuredItemContentProvider.class);
		//
		if (newInput instanceof EObject) {
			domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(newInput);
			if (domain != null) {
				domain.addResourceSetListener(listener);
			}
			input = (EObject) newInput;
		} else {
			input = null;
			domain = null;
		}
	}

	@SuppressWarnings("unchecked")
	protected void processNotifications(final Collection<Notification> notifications) {
		final Map<EObject, Set<EStructuralFeature>> map = new AutoSetMap<EObject, EStructuralFeature>(EObject.class);
		final TreeTableViewer treeTableViewer = (TreeTableViewer) viewer;
		treeTableViewer.preservingSelection(new Runnable() {
			@Override
			public void run() {
				for (Notification notification : notifications) {
					if (notification == null) {
						continue;
					}

					Object notifier = notification.getNotifier();
					if (!(notifier instanceof EObject)) {
						continue;
					}

					EStructuralFeature feature = (EStructuralFeature) notification.getFeature();

					EObject element = (EObject) notifier;
					switch (notification.getEventType()) {
					case Notification.ADD:
					case Notification.ADD_MANY:
						if (notifier == input && feature == structuralFeature) {
							List childrenAdded = EMFUtils.getAddedObjects(notification, (EClass) eClassifier);
							if (!childrenAdded.isEmpty()) {
								treeTableViewer.add(element, childrenAdded.toArray());
							}
						}
						break;
					case Notification.REMOVE:
					case Notification.REMOVE_MANY:
						if (notifier == input && feature == structuralFeature) {
							List childrenRemoved = EMFUtils.getRemovedObjects(notification, (EClass) eClassifier);
							if (!childrenRemoved.isEmpty()) {
								treeTableViewer.remove(element, childrenRemoved.toArray());
							}
						}
						break;
					case Notification.MOVE:
						LogUtil.warn("move!");
						break;
					case Notification.SET:
						if (notifier == input && feature == structuralFeature) {
							Object oldValue = notification.getOldValue();
							if (oldValue != null) {
								treeTableViewer.remove(element, new Object[] { oldValue });
							}
							Object newValue = notification.getNewValue();
							if (newValue != null) {
								treeTableViewer.add(element, newValue);
							}
						}
						if (structuralFeature == element.eContainingFeature()) {
							map.get(element).add(feature);
						}
						break;
					}
				}
			}
		});
		if (!map.isEmpty()) {
			treeTableViewer.updateElementFeatures(map);
		}
	}

	private class ModelChangeListenerImpl extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			final List<Notification> notifications = new ArrayList<Notification>(event.getNotifications());
			if (viewer != null) {
				WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
					@Override
					public void run() {
						processNotifications(notifications);
					}
				});
			}
		}

	}

}
