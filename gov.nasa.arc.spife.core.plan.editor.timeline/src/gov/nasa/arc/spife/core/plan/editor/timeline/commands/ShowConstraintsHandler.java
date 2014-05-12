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
package gov.nasa.arc.spife.core.plan.editor.timeline.commands;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public class ShowConstraintsHandler extends AbstractPlanEditorHandler implements PlanTimelineConstants {

	@Override
	protected void selectionChanged(ISelection selection) {
		//ignore selection
	}
	
	@Override
	protected void partActivated(IWorkbenchPart part) {
		setBaseEnabled(part != null && part.getAdapter(Timeline.class) != null);
		try {
			boolean isChecked = PLAN_TIMELINE_PREFERENCES.getBoolean(PLAN_CONSTRAINTS_VISIBLE);
			setCommandState(getCommand(), isChecked);
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	protected void pageChanged(PageChangedEvent event) {
		Object page = event.getSelectedPage();
		if (page instanceof IWorkbenchPart) {
			setBaseEnabled(page != null && ((IWorkbenchPart) page).getAdapter(Timeline.class) != null);
		}
	}

	@Override
	public Object execute(ExecutionEvent event) {
		String key = PLAN_CONSTRAINTS_VISIBLE;
		boolean showConstraints = PLAN_TIMELINE_PREFERENCES.getBoolean(key);
		PLAN_TIMELINE_PREFERENCES.setValue(PLAN_CONSTRAINTS_VISIBLE, !showConstraints);
		try {
			setCommandState(event.getCommand(), !showConstraints);
		} catch (ExecutionException e) {
			LogUtil.error(e);
		}
		return null;
	}

	@Override
	public String getCommandId() {
		return SHOW_CONSTRAINTS_COMMAND_ID;
	}

}
