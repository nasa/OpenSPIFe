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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.plan.editor.PlanDeleteOperation;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class TimelinePlanDeleteOperation extends PlanDeleteOperation implements IDisplayOperation {
	
	public TimelinePlanDeleteOperation(ITransferable transferable, IStructureModifier modifier) {
		super(transferable, modifier);
	}

	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		selectionProvider.setSelection(StructuredSelection.EMPTY);
	}

}
