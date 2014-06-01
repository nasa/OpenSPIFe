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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class EPlanElementTreeContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	protected EPlan plan = null;
	protected AbstractTreeViewer viewer = null;
	protected PostCommitListener listener = new ModelChangeListenerImpl();
	protected ITreeContentProvider contentProvider;
	
    protected final AbstractTreeViewer getViewer() {
	    return viewer;
    }
	
	@Override
	public Object[] getChildren(Object parent) {
		List<EPlanElement> planElements = new ArrayList<EPlanElement>();
		if(contentProvider == null) {
			return new Object[]{};
		}
		
		for (Object child : contentProvider.getChildren(parent)) {
			if (child instanceof EPlanElement) {
				planElements.add((EPlanElement) child);
			}
		}
		
		//Collections.sort(planElements);
		return planElements.toArray(new EPlanElement[0]);
	}
	
	@Override
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length != 0;
	}
	
	@Override
	public Object getParent(Object child) {
		if(contentProvider != null) {
			return contentProvider.getParent(child);
		}
		return null;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
		contentProvider= null;
		if (plan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
			domain.removeResourceSetListener(listener);
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (AbstractTreeViewer) viewer;
		EPlan oldPlan = (EPlan) oldInput;
		if (oldPlan != null) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(oldPlan);
			if (domain != null) {
				domain.removeResourceSetListener(listener);
			} else {
				LogUtil.warn(oldPlan + " has a null TransactionalEditingDomain");
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
	
	protected void processNotifications(Collection<Notification> notifications) {
    	for (Notification notification : notifications) {
    		if (notification == null)
    			return;
    		Object notifier = notification.getNotifier();
    		if (notifier instanceof EPlanElement) {
    			EPlanElement element = (EPlanElement) notifier;
    			if (!isRelevant(element)) {
    				continue;
    			}
    			List<EPlanElement> childrenRemoved = EPlanUtils.getElementsRemoved(notification);
    			if (!childrenRemoved.isEmpty()) {
    				removeChildren(element, childrenRemoved);
    			}
    			List<EPlanElement> childrenAdded = EPlanUtils.getElementsAdded(notification);
    			if (!childrenAdded.isEmpty()) {
    				addChildren(element, childrenAdded);
    			}
    			int eventType = notification.getEventType();
    			if (eventType == Notification.MOVE) {
    				Logger logger = Logger.getLogger(EPlanElementTreeContentProvider.class);
    				logger.warn("move!");
    			}
    		}
    	}
    }
	
	protected boolean isRelevant(EPlanElement element) {
		return EPlanUtils.getPlan(element) == plan;
	}

	protected void removeChildren(EPlanElement element, List<EPlanElement> childrenRemoved) {
		viewer.remove(element, childrenRemoved.toArray());
	}
	
	protected void addChildren(EPlanElement element, List<EPlanElement> childrenAdded) {
		viewer.add(element, childrenAdded.toArray());
	}

	private class ModelChangeListenerImpl extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			final List<Notification> notifications = new ArrayList<Notification>(event.getNotifications());
			WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
				@Override
				public void run() {
					processNotifications(notifications);
				}
			});
		}

	}
	
}
