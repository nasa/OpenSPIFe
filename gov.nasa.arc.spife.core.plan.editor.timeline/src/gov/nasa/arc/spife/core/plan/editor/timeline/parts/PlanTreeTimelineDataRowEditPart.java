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

import gov.nasa.arc.spife.core.plan.editor.timeline.policies.PlanTimelineDataContextMenuEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.RowDataDropEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeLayoutPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.ValueDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataRowEditPart;
import gov.nasa.ensemble.common.ui.gef.ContextMenuEditPolicy;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;

import org.eclipse.gef.EditPolicy;

public class PlanTreeTimelineDataRowEditPart<T> extends TreeTimelineDataRowEditPart<T> {
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(DropEditPolicy.DROP_ROLE, new RowDataDropEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new TemporalNodeLayoutPolicy());
		installEditPolicy(ContextMenuEditPolicy.CONTEXT_MENU_ROLE, new PlanTimelineDataContextMenuEditPolicy());
		installEditPolicy(ValueDropEditPolicy.ROLE, new ValueDropEditPolicy());
	}
	
}
