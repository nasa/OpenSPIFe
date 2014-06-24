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

import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

/**
 * Due to a 1:many relationship between models and edit parts in the GEF viewer,
 * this class registers the model to edit part relation properly
 */
public abstract class PlanElementEditPart<T extends EPlanElement> extends PlanTimelineViewerEditPart<T> {

	private AdapterFactory adapterFactory = null;

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}

	@SuppressWarnings("unchecked")
	public <A> A adapt(Class<A> type) {
		return (A) getAdapter(type);
	}
	
	@Override
	public Object getAdapter(Class type) {
		AdapterFactory adapterFactory = getAdapterFactory();
		Object result = adapterFactory == null ? null : adapterFactory.adapt(getModel(), type);
		if (result == null) {
			result = super.getAdapter(type);
		}
		return result;
	}

	public AdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(getModel());
			if (editingDomain instanceof AdapterFactoryEditingDomain) {
				adapterFactory = ((AdapterFactoryEditingDomain) editingDomain).getAdapterFactory();
			} else {
				adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
			}
		}
		return adapterFactory;
	}
	
}
