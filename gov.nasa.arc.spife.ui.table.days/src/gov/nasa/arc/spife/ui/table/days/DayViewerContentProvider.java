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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.common.ui.treetable.TreeTableContentProvider;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;

import org.eclipse.jface.viewers.Viewer;

public class DayViewerContentProvider extends TreeTableContentProvider {

	private static final Object[] EMPTY_CHILDREN_ARRAY = new Object[0];
	private static final EActivityGroup GROUP = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEActivityGroup();
	private static final EActivity ACTIVITY = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEActivity();
	static {
		GROUP.setName("Group");
		ACTIVITY.setName("Activity");
	}
	
	@SuppressWarnings("unused")
	private Day day;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		this.day = (Day)newInput;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Day) {
			return new Object[] { GROUP, GROUP };
		}
		if (parentElement instanceof EActivityGroup) {
			return new Object[] { ACTIVITY, ACTIVITY };
		}
		return EMPTY_CHILDREN_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Object[]) {
			return ((Object[])element)[0];
		}
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
}
