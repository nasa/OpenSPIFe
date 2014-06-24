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
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.models.DynamicActivityGroup;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.PlanTimelineDataContextMenuEditPolicy;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineHeaderRowEditPart;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.gef.ContextMenuEditPolicy;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class PlanElementRowHeaderEditPart extends TreeTimelineHeaderRowEditPart<EPlanElement> {

	private ILabelProvider labelProvider 						= null;
	
	private AdapterFactory adapterFactory 						= null;
	
	@Override
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			final ILabelProvider delegate = CommonUtils.getAdapter(getModel(), ILabelProvider.class);
			labelProvider = new DelegatingLabelProvider(delegate);
		}
		return labelProvider;
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		ILabelProvider labelProvider = getLabelProvider();
		if (labelProvider != null) {
			labelProvider.dispose();
		}
	}

	@SuppressWarnings("unchecked")
	public <A> A adapt(Class<A> type) {
		return (A) getAdapter(type);
	}
	
	@Override
	public Object getAdapter(Class type) {
//		if (ISelection.class == type) {
//			EPlanElement model = getModel();
//			if (model instanceof DynamicActivityGroup) {
//				List<EPlanChild> children = ((DynamicActivityGroup)model).getChildren();
//				LinkedHashSet<Object> set = new LinkedHashSet<Object>(children);
//				Set<EditPart> editParts = getViewer().getEditParts(set);
//				return new StructuredSelection(editParts.toArray(new EditPart[0]));
//			}
//		}
		AdapterFactory adapterFactory = getAdapterFactory();
		Object result = adapterFactory == null ? null : adapterFactory.adapt(getModel(), type);
		if (result == null) {
			result = super.getAdapter(type);
		}
		return result;
	}

	public AdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = EMFUtils.getAdapterFactory(getModel());
		}
		return adapterFactory;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		installEditPolicy(ContextMenuEditPolicy.CONTEXT_MENU_ROLE, new PlanTimelineDataContextMenuEditPolicy());
	}
	
	@Override
	public boolean isScrollInhibited() {
		return true;
	}

	private final class DelegatingLabelProvider implements ILabelProvider {
		private final ILabelProvider delegate;

		private DelegatingLabelProvider(ILabelProvider delegate) {
			this.delegate = delegate;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			delegate.removeListener(listener);
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return delegate.isLabelProperty(element, property);
		}

		@Override
		public void dispose() {
			delegate.dispose();
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			delegate.addListener(listener);
		}

		@Override
		public String getText(Object element) {
			return delegate.getText(element);
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof DynamicActivityGroup) {
				DynamicActivityGroup dag = (DynamicActivityGroup) element;
				Object value = dag.getValue();
				if (value instanceof EEnumLiteral) {
					return null;
				}
				IItemLabelProvider lp = EMFUtils.adapt(value, IItemLabelProvider.class);
				if (lp != null) {
					Object image = lp.getImage(value);
					return ExtendedImageRegistry.getInstance().getImage(image);
				}
				return null;
			}
			return delegate.getImage(element);
		}
	}

}
