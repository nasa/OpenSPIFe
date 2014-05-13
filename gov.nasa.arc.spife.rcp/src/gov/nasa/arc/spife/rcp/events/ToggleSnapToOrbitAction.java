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
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.commands.AbstractTimelineAction;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
/**
 * @since SPF-7695
 *
 */
public class ToggleSnapToOrbitAction extends AbstractTimelineAction
	implements PlanTimelineConstants, IWorkbenchWindowActionDelegate
{
	private static ToggleSnapToOrbitAction toggleSnapToOrbitAction;
	
	public static final String ID = ToggleSnapToOrbitAction.class.getName();
	
	@Override
	public String getId()
	{
		return ID;
	}
	
	public static ToggleSnapToOrbitAction getInstance()
	{
		if(toggleSnapToOrbitAction == null)
			toggleSnapToOrbitAction = new ToggleSnapToOrbitAction();
		
		return toggleSnapToOrbitAction;
	}
	
	public ToggleSnapToOrbitAction() {
		super();
		setText("Snap to " + OrbitEventUtil.ORBIT_NAME + " mode");
		setToolTipText("Toggle Snap to "+ OrbitEventUtil.ORBIT_NAME + " mode");
		updateChecked(this);
	}
		
	@Override
	public void run(IAction action)
	{
		String key = TimelinePreferencePage.P_SNAP_TO_ORBIT_ACTIVE;
		boolean oldValue = TIMELINE_PREFERENCES.getBoolean(key);
		boolean newValue = !oldValue;
		TIMELINE_PREFERENCES.setValue(key, newValue);
		updateChecked(action);
		changeInExistingTimeline(newValue);
	}
	
	

	@Override
	public void run() {
		run(this);
	}

	private void changeInExistingTimeline(boolean enforced) {
		@SuppressWarnings("deprecation")
		PlanTimeline timeline = PlanTimeline.getCurrent();
		if (timeline != null) {
			timeline.setSnapToOrbitEnabled(enforced);
		}
	}

	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		updateChecked(action);
	}

	private void updateChecked(IAction action) {
		String key = TimelinePreferencePage.P_SNAP_TO_ORBIT_ACTIVE;
		action.setChecked(TIMELINE_PREFERENCES.getBoolean(key));
	}
	
	@Override
	public void init(IWorkbenchWindow window) {
		// Nothing to do.
	}

	@Override
	public void dispose() {
		// Nothing to do.		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		updateChecked(action);
	}


}
