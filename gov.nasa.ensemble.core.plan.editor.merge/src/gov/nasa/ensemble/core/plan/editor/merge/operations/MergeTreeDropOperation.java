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
package gov.nasa.ensemble.core.plan.editor.merge.operations;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityDefTransferable;

import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class MergeTreeDropOperation extends PlanAddOperation {

	private final TreeTableViewer viewer;
	private final ITransferable transferable;

	public MergeTreeDropOperation(TreeTableViewer viewer, ITransferable transferable, IStructureModifier modifier, IStructureLocation location) {
		super(transferable, modifier, location);
		this.viewer = viewer;
		this.transferable = transferable;
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		super.displayExecute(widget, site);
		try {
			editIfNewActivity();
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			// these are optional behaviors, don't break the model edit
		}
	}
	
	private void editIfNewActivity() {
		if (transferable instanceof ActivityDefTransferable) {
	        ActivityDefTransferable activityDefTransferable = (ActivityDefTransferable) transferable;
	        if (activityDefTransferable.getPlanElements().size() == 1) {
				TreeItem[] selection = viewer.getTree().getSelection();
		        if (selection != null && selection.length == 1) {
					viewer.handleEditRequest(selection[0], 0);
				}
	        }
        }
	}

}
