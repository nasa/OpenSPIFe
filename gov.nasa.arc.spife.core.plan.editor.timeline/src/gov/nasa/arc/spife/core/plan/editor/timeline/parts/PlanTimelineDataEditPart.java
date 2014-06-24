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
/*
 * Created on Mar 15, 2005
 */
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.PlanTimelineDataContextMenuEditPolicy;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataEditPart;
import gov.nasa.ensemble.common.ui.gef.ContextMenuEditPolicy;
import gov.nasa.ensemble.core.model.plan.EPlan;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.util.IPropertyChangeListener;

public class PlanTimelineDataEditPart extends TreeTimelineDataEditPart<EPlan> implements IPropertyChangeListener, PlanTimelineConstants {

	public static final String LAYER_DATA_ROWS_LAYER = "Data Rows Layer";
	
	private AdapterFactory adapterFactory = null;
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(ContextMenuEditPolicy.CONTEXT_MENU_ROLE, 	new PlanTimelineDataContextMenuEditPolicy());
	}

	@SuppressWarnings("unchecked")
	public <A> A adapt(Class<A> type) {
		return (A) getAdapter(type);
	}
	
	@Override
	public Object getAdapter(Class type) {
		Object result = getAdapterFactory().adapt(getModel(), type);
		if (result == null) {
			result = super.getAdapter(type);
		}
		return result;
	}

	public AdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = ((AdapterFactoryEditingDomain) AdapterFactoryEditingDomain.getEditingDomainFor(getModel())).getAdapterFactory();
		}
		return adapterFactory;
	}
	
}
