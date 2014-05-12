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

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.PlanElementHeaderLayoutEditPolicy;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineHeaderEditPart;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.gef.EditPolicy;

public class PlanTimelineHeaderEditPart extends TreeTimelineHeaderEditPart<EPlan> implements PlanTimelineConstants {

	private AdapterFactory adapterFactory = null;
	
	public PlanTimelineHeaderEditPart() {
		super();
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
			adapterFactory = EMFUtils.getAdapterFactory(getModel());
		}
		return adapterFactory;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new PlanElementHeaderLayoutEditPolicy());
	}

}
