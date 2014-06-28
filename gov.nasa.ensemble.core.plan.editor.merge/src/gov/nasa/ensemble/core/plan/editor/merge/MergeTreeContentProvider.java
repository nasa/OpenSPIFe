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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.common.ui.treetable.TreeTableContentProvider;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanMoveOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureLocation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanStructureOperationContributor;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;
import gov.nasa.ensemble.core.plan.editor.merge.operations.MergeTreeDropOperation;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;


public class MergeTreeContentProvider extends TreeTableContentProvider {

	private EPlan plan = null;
	private PostCommitListener listener = new ModelChangeListenerImpl();
	private IResourceChangeListener resourceListener = new ResourceListener();
	protected AdapterFactoryContentProvider contentProvider;
	private boolean flattened = false;
	
	public MergeTreeContentProvider() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener);
	}
	
	@Override
	public IStructureModifier getStructureModifier() {
		return PlanStructureModifier.INSTANCE;
	}

	public boolean isFlattened() {
		return flattened;
	}
	
	/**
	 * Toggle the flattened state.
	 * NOTE: caller must call refresh on the corresponding viewer!
	 * 
	 * @param flattened
	 */
	public void toggleFlatten() {
		flattened = !flattened;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		Object[] elements = super.getElements(inputElement);
		List<Object> contributions = MergeTreePlanContributor.getInstance().getContributions(plan);
		if (contributions != null) {
			contributions.addAll(Arrays.asList(elements));
			return contributions.toArray();
		}
		return elements;
	}
	
    @Override
	public Object[] getChildren(Object parent) {
    	if (flattened) {
    		return getFlatChildren(parent);
    	}
		return getNormalChildren(parent);
	}
    
    /**
     * Return the children in the normal way, including groups and activities
     * from only one level at a time.
     * 
     * @param parent
     * @return
     */
	private Object[] getNormalChildren(Object parent) {
		List<EPlanElement> planElements = new ArrayList<EPlanElement>();
		for (Object child : contentProvider.getChildren(parent)) {
			if (child instanceof EPlanElement) {
				planElements.add((EPlanElement) child);
			}
		}
		return planElements.toArray(new EPlanElement[0]);
	}

	/**
	 * Return the children as flattened out to activities only (no groups)
	 * 
	 * @param parent
	 * @return
	 */
	private Object[] getFlatChildren(Object parent) {
		if (parent instanceof EPlan) {
			EPlan plan = (EPlan)parent;
			final List<EActivity> activities = new ArrayList<EActivity>();
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
				    if (element instanceof EActivity) {
						EActivity activity = (EActivity) element;
				    	activities.add(activity);
				    }
				}
			}.visitAll(plan);
			return activities.toArray();
		} else
		if (parent instanceof EPlanElement) {
			return new Object[0];
		}
		if (parent instanceof EActivity) {
			EActivity activity = (EActivity) parent;
			return activity.getChildren().toArray();
		}
		LogUtil.error("getChildren: Unexpected parent type: " + parent.getClass().getName(), new Throwable());
		return null;
	}
	
	@Override
	public boolean hasChildren(Object parent) {
		if (flattened) {
			return hasFlatChildren(parent);
		}
		return super.hasChildren(parent);
	}

	/**
	 * The normal way of checking for children is much slower than it
	 * has to be for the case where we are flat.  This method checks
	 * for the first activity and then shortcuts to return true.
	 * 
	 * @param parent
	 * @return
	 */
	private boolean hasFlatChildren(Object parent) {
		if (parent instanceof EPlan) {
			EPlan plan = (EPlan)parent;
			class ChildFound extends RuntimeException { private static final long serialVersionUID = 1L; }
			try {
				new PlanVisitor() {
					@Override
					protected void visit(EPlanElement element) {
					    if (element instanceof EActivity) {
					    	throw new ChildFound();
					    }
					}
				}.visitAll(plan);
			} catch (ChildFound f) {
				return true;
			}
		}
		if (parent instanceof EActivity) {
			EActivity activity = (EActivity) parent;
			return !activity.getChildren().isEmpty();
		}
		LogUtil.error("hasChildren: Unexpected parent type: " + parent.getClass().getName(), new Throwable());
		return false;
	}
	
	@Override
	public Object getParent(Object child) {
		if (flattened) {
			if (child instanceof EActivity) {
				return EPlanUtils.getPlan((EPlanElement)child);
			}
			return null;
		}
		return contentProvider.getParent(child);
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceListener);
		if (plan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
			domain.removeResourceSetListener(listener);
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		EPlan oldPlan = (EPlan) oldInput;
		if (oldPlan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(oldPlan);
			if (domain != null) {
				domain.removeResourceSetListener(listener);
			}
		}
		EPlan newPlan = (EPlan) newInput;
		plan = newPlan;
		if (newPlan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(newPlan);
			domain.addResourceSetListener(listener);
			AdapterFactory factory = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
			this.contentProvider = new AdapterFactoryContentProvider(factory);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static class Event {
		public enum TYPE { REMOVE, ADD }
		public TYPE type;
		public EPlanElement parent;
		public Object[] children;
		public Event(TYPE type, EPlanElement parent, Object[] objects) {
			this.type = type;
			this.parent = parent;
			this.children = objects;
		}
	}
	
	private void processNormalNotifications(final Collection<Notification> notifications) {
        final Map<EPlanElement, Set<EStructuralFeature>> map = new AutoSetMap<EPlanElement, EStructuralFeature>(EPlanElement.class);
        final List<Event> events = new ArrayList<Event>();
		for (Notification notification : notifications) {
    		if (notification == null) {
    			continue;
    		}
    		Object notifier = notification.getNotifier();
    		if (notifier instanceof EPlanElement) {
    			EPlanElement element = (EPlanElement) notifier;
    			List<EPlanElement> childrenRemoved = EPlanUtils.getElementsRemoved(notification);
    			if (!childrenRemoved.isEmpty()) {
    				events.add(new Event(Event.TYPE.REMOVE, element, childrenRemoved.toArray()));
    			}
    			List<EPlanElement> childrenAdded = EPlanUtils.getElementsAdded(notification);
    			if (!childrenAdded.isEmpty()) {
    				events.add(new Event(Event.TYPE.ADD, element, childrenAdded.toArray()));
    			}
    			int eventType = notification.getEventType();
    			if (eventType == Notification.MOVE) {
    				Logger logger = Logger.getLogger(MergeTreeContentProvider.class);
    				logger.warn("move!");
    			}
    			if (eventType == Notification.SET) {
    				EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
    				map.get(element).add(feature);
    			}
    		} else if (notifier instanceof EObject) {
    			EObject object = (EObject)notifier;
    			while ((object != null) && !(object instanceof EPlanElement)) {
    				object = object.eContainer();
    			}
    			if (object instanceof EPlanElement) {
    				EPlanElement element = ((EPlanElement)object);
    				if (element instanceof EPlan) {
    					// Unless it is a structural event on the plan, we 
    					// do not care since the plan is not visible in the content provider
        				continue;
        			}
    				EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
    				map.get(element).add(feature);
    			}
    		}
    	}
		if (!events.isEmpty()) {
			WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
				@Override
				public void run() {
					viewer.preservingSelection(new Runnable() {
						@Override
						public void run() {
							for (Event event : events) {
								switch (event.type) {
								case ADD:
									viewer.add(event.parent, event.children);
									break;
								case REMOVE:
									viewer.remove(event.parent, event.children);
									break;
								}
							}
						}
					});
					viewer.updateElementFeatures(map);
				}
			});
		} else {
			viewer.updateElementFeatures(map);
		}
    }
	
	private void processFlatNotifications(Collection<Notification> notifications) {
		TreeTableViewer viewer = getViewer();
        Map<EActivity, Set<EStructuralFeature>> map = new AutoSetMap<EActivity, EStructuralFeature>(EActivity.class);
    	for (Notification notification : notifications) {
    		if (notification == null)
    			return;
    		Object notifier = notification.getNotifier();
    		if (notifier instanceof EPlanElement) {
    			EPlanElement element = (EPlanElement) notifier;
    			List<? extends EPlanElement> childrenRemoved = EPlanUtils.getElementsRemoved(notification);
    			if (!childrenRemoved.isEmpty()) {
    				if (element instanceof EPlan) {
    					List<EActivity> activities = new ArrayList<EActivity>(childrenRemoved.size());
    					for (EPlanElement child : childrenRemoved) {
    						if (child instanceof EActivity) {
    							activities.add((EActivity) child);
    						}
							activities.addAll(EPlanUtils.getActivities(child));
    					}
    					childrenRemoved = activities;
    				}
    				if (element instanceof EActivityGroup) {
    					element = EPlanUtils.getPlan(element);
    				}
    				viewer.remove(element, childrenRemoved.toArray());
    			}
    			List<? extends EPlanElement> childrenAdded = EPlanUtils.getElementsAdded(notification);
    			if (!childrenAdded.isEmpty()) {
    				if (element instanceof EPlan) {
    					List<EActivity> activities = new ArrayList<EActivity>(childrenRemoved.size());
    					for (EPlanElement child : childrenAdded) {
    						if (child instanceof EActivity) {
    							activities.add((EActivity) child);
    						}
							activities.addAll(EPlanUtils.getActivities(child));
    					}
    					childrenAdded = activities;
    				}
    				if (element instanceof EActivityGroup) {
    					element = EPlanUtils.getPlan(element);
    				}
    				viewer.add(element, childrenAdded.toArray());
    			}
    			int eventType = notification.getEventType();
    			if (eventType == Notification.MOVE) {
    				Logger logger = Logger.getLogger(MergeTreeContentProvider.class);
    				logger.warn("move!");
    			}
    			if ((element instanceof EActivity) && (eventType == Notification.SET)) {
    				EActivity activity = (EActivity) element;
					EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
    				map.get(activity).add(feature);
    			}
    		} else if (notifier instanceof EObject) {
    			EObject object = (EObject)notifier;
    			while ((object != null) && !(object instanceof EPlanElement)) {
    				object = object.eContainer();
    			}
    			if (object instanceof EActivity) {
    				EActivity activity = ((EActivity)object);
    				EStructuralFeature feature = (EStructuralFeature)notification.getFeature();
    				map.get(activity).add(feature);
    			}
    		}
    	}
    	if (!map.isEmpty()) {
    		viewer.updateElementFeatures(map);
    	}
    }
	
	@Override
	public boolean isValidDrop(ISelection sourceSelection, Object target) {
		if (sourceSelection instanceof StructuredSelection) {
			List selectedElements = ((StructuredSelection) sourceSelection).toList();
			Set<EPlanElement> containedElements = EPlanUtils.computeContainedElements(selectedElements);
			if (containedElements.contains(target)) {
				return false;
			}
		}
		return super.isValidDrop(sourceSelection, target);
	}
	
	@Override
	public IUndoableOperation createDropOperation(Object targetElement, InsertionSemantics semantics, TransferData currentDataType, int detail, Object data) {
		ITransferable transferable = TransferRegistry.getInstance().getDroppedObjects(data, currentDataType);
		if (!(transferable instanceof IPlanElementTransferable)) {
			return null;
		}
		StructuredSelection targetSelection = new StructuredSelection(targetElement);
		if (detail == DND.DROP_COPY) {
			transferable = getStructureModifier().copy(transferable);
		}
		IStructureModifier structureModifier = getStructureModifier();
		IStructureLocation location = structureModifier.getInsertionLocation(transferable, targetSelection, semantics);
		IUndoableOperation operation = createDropOperation(currentDataType, detail, structureModifier, (IPlanElementTransferable)transferable, (PlanStructureLocation)location);
		operation = PlanStructureOperationContributor.addContributorOperations(operation, (IPlanElementTransferable)transferable, (PlanStructureLocation)location);
		return operation;
	}

	/**
	 * Override this in your content provider to do something specific to drops in your tree.
	 * 
	 * @see DayContentProvider
	 * @param currentDataType
	 * @param detail
	 * @param modifier
	 * @param transferable
	 * @param location
	 * @return
	 */
	protected IUndoableOperation createDropOperation(TransferData currentDataType, int detail, IStructureModifier modifier, IPlanElementTransferable transferable, PlanStructureLocation location) {
		if (EnsembleDragAndDropOracle.isDragSourceEditorModel(viewer.getModel()) && (detail == DND.DROP_MOVE)) {
			return new PlanMoveOperation(transferable, modifier, location);
		}
		return new MergeTreeDropOperation(viewer, transferable, modifier, location);
	}

	private class ModelChangeListenerImpl extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			final List<Notification> notifications = new ArrayList<Notification>(event.getNotifications());
			WidgetUtils.runInDisplayThread(viewer.getTree(), new Runnable() {
				@Override
				public void run() {
					if (flattened) {
						processFlatNotifications(notifications);
					} else {
						processNormalNotifications(notifications);
					}
				}
			});
		}

	}

	private static final Set<EStructuralFeature> MARKER_FEATURES = Collections.singleton((EStructuralFeature) PlanPackage.Literals.EPLAN_ELEMENT__NAME);
	private class ResourceListener implements IResourceChangeListener {
		
		/**
		 * Listen for workspace changes to update text and image.
		 *
		 * @param event the event
		 */
		@Override
		@SuppressWarnings("unchecked")
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				EPlan thisPlan = MergeTreeContentProvider.this.plan;
				if (thisPlan != null) {
					Resource resource = thisPlan.eResource();
					if (resource != null) {
						boolean includeSubTypes = true;
						IMarkerDelta[] markerDeltas = event.findMarkerDeltas(IMarker.MARKER, includeSubTypes);
						Map<EPlanElement, Set<EStructuralFeature>> featuresByElement = new HashMap<EPlanElement, Set<EStructuralFeature>>();
						for (IMarkerDelta delta : markerDeltas) {
							List<EPlanElement> culprits = EPlanUtils.getCulprits(delta);
							for (EPlanElement culprit : culprits) {
								if (!(culprit instanceof EPlan)) {
									featuresByElement.put(culprit, MARKER_FEATURES);
								}										
							}
							String location = delta.getAttribute(IMarker.LOCATION, null);
							if (location != null) {
								EObject object = null;
								if (!culprits.isEmpty()) {
									for (EPlanElement candidate : culprits) {
										if (candidate==null) continue;
										Resource eResource = candidate.eResource();
										if (eResource != null && location.equals(eResource.getURIFragment(candidate))) {
											object = resource.getEObject(location);
											break;
										}
									}
								} else {
									// for persistent markers not created by a plan advisor
									object = resource.getEObject(location);
								}
								if ((object instanceof EPlanElement) && !(object instanceof EPlan)) {
									EPlanElement element = (EPlanElement) object;
									featuresByElement.put(element, MARKER_FEATURES);
									// add every element on the path to this element
									EObject container = element.eContainer();									
									while (container instanceof EPlanElement && !(container instanceof EPlan)) {
										EPlanElement planElement = (EPlanElement) container;
										featuresByElement.put(planElement, MARKER_FEATURES);
										container = planElement.eContainer();
									}
								}
							}
						}
						if (!featuresByElement.isEmpty()) {
							viewer.updateElementFeatures(featuresByElement);
						}
					}
				}
			}
		}
		
	}
	
}
