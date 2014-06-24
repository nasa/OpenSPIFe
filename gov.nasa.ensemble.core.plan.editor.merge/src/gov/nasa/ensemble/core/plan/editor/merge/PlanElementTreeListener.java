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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanUtils;

import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;

public class PlanElementTreeListener implements TreeListener {
	
	@Override
	public void treeExpanded(TreeEvent event) {
		perform(event, true);
	}

	@Override
	public void treeCollapsed(TreeEvent event) {
		perform(event, false);
	}
	
	protected void perform(TreeEvent event, boolean expanded) {
		Object data = event.item.getData();
		if (data instanceof EPlanElement) {
			EPlanElement element = (EPlanElement) data;
			PlanUtils.executeExpansionOperation(element, expanded);
		}
	}

}
