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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MultiTemplatePlanViewContentProvider extends TemplatePlanViewContentProvider {

	private Map<EPlan, AdapterFactoryContentProvider> planContentProviderMap = new HashMap<EPlan, AdapterFactoryContentProvider>();
	private List<EPlan> inputPlans = null;
	
	@Override
	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		List<EPlan> elementsList = (List<EPlan>)inputElement;
		if(elementsList == null) {
			elementsList = inputPlans;
		}
		return elementsList.toArray(new EPlan[elementsList.size()]);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (inputPlans != null) {
			for (EPlan plan : inputPlans) {
				TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
				domain.removeResourceSetListener(listener);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (AbstractTreeViewer) viewer;
		List<EPlan> oldPlans = (List<EPlan>)oldInput;
		List<EPlan> newPlans = (List<EPlan>)newInput;
		if (oldPlans != null) {
			for (EPlan plan : oldPlans) {
				if (newPlans == null || !newPlans.contains(plan)) {
					planContentProviderMap.remove(plan);
				}
			}
		}
		inputPlans = newPlans;
		if (newPlans != null) {
			for (EPlan plan : newPlans) {
				if (oldPlans == null || !oldPlans.contains(plan)) {
					TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
					domain.addResourceSetListener(listener);
					AdapterFactory factory = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
					planContentProviderMap.put(plan, new AdapterFactoryContentProvider(factory));
				}
			}
			contentProvider = new DelegatingPlanContentProvider();
			initializeSearch(viewer, oldInput, newInput);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void doInputChanged(Viewer viewer, Object oldInput, Object newInput) {
		List<EPlan> oldPlans = (List<EPlan>)oldInput;
		List<EPlan> newPlans = (List<EPlan>)newInput;
		if (newPlans != null) {
			for (EPlan plan : newPlans) {
				if (oldPlans == null || !oldPlans.contains(plan)) {
					indexElements(plan.getChildren());
				}
			}
		}
	}
	
	@Override
	protected boolean isRelevant(EPlanElement element) {
		return inputPlans != null && inputPlans.contains(EPlanUtils.getPlan(element));
	}

	private class DelegatingPlanContentProvider implements ITreeContentProvider {
		
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { /* do nothing */ }
		@Override
		public void dispose() { /* do nothing */ }
		
		@Override
		public boolean hasChildren(Object element) {
			return getChildren(element).length != 0;
		}
		
		@Override
		public Object getParent(Object child) {
			if (child instanceof EObject) {
				EPlan plan = EPlanUtils.getPlan((EObject)child);
				if (plan != null) {
					AdapterFactoryContentProvider contentProvider = planContentProviderMap.get(plan);
					if (contentProvider != null) {
						return contentProvider.getParent(child);
					}
				}
			}
			return null;
		}
		
		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}
		
		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof EObject) {
				EPlan plan = EPlanUtils.getPlan((EObject)parentElement);
				if (plan != null) {
					AdapterFactoryContentProvider contentProvider = planContentProviderMap.get(plan);
					if (contentProvider != null) {
						return contentProvider.getChildren(parentElement);
					}
				}
			}
			return new Object[]{};
		}
	}

}
