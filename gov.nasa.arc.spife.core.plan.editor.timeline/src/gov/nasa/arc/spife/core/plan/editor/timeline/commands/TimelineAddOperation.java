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
/**
 * 
 */
package gov.nasa.arc.spife.core.plan.editor.timeline.commands;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline.SELECTION_MODE;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class TimelineAddOperation extends PlanAddOperation {

	private final PlanTimeline timeline;

	public TimelineAddOperation(ITransferable transferable, IStructureModifier modifier, IStructureLocation location, PlanTimeline timeline) {
		super(transferable, modifier, location);
		this.timeline = timeline;
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		timeline.setSelectionMode(SELECTION_MODE.NONE);
		super.displayExecute(widget, site);
	}

}	
