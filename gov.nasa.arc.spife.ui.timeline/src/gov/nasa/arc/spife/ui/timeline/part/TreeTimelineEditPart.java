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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalEditPart;

public class TreeTimelineEditPart<T> extends AbstractTimelineEditPart<T> {

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
	@Override
	public GraphicalEditPart createDataEditPart(T model) {
		GraphicalEditPart ep = new TreeTimelineDataEditPart<EObject>();
		ep.setModel(model);
		return ep;
	}

	@Override
	public GraphicalEditPart createHeaderEditPart(T model) {
		GraphicalEditPart ep = new TreeTimelineHeaderEditPart<EObject>();
		ep.setModel(model);
		return ep;
	}

}
