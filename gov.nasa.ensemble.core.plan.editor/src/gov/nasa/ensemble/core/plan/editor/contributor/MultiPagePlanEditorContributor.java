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
package gov.nasa.ensemble.core.plan.editor.contributor;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorPart;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.IPlanEditorConstants;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.SetExpandedOperation;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;


/**
 * Manages the installation/deinstallation of global actions for multi-page
 * editors. Responsible for the redirection of global actions to the active
 * editor. Multi-page contributor replaces the contributors for the individual
 * editors in the multi-page editor.
 * 
 * Some Editors use Cell Editors to support the modification of cells inside
 * trees or tables. We want to pass copy/cut/undo/etc actions to these Cell
 * Editors when they are active. To do this we use a CellEditorActionHandler.
 * 
 */
public class MultiPagePlanEditorContributor extends MultiPageEditorActionBarContributor {
	
	private static final List<MultiPageEditorActionBarContributor> INSTANCES = ClassRegistry.createInstances(MultiPageEditorActionBarContributor.class);
	
	/** The "collapse all" action */
	private IAction collapseActionHandler = new Action() {
		@Override
		public void run() {
			createAndExecuteSetExpandedOperation(false);
		}
	};

	/** The "expand all" action */
	private IAction expandActionHandler = new Action() {
		@Override
		public void run() {
			createAndExecuteSetExpandedOperation(true);
		}
	};
	
	@Override
	public void init(IActionBars bars) {
		super.init(bars);
		for (MultiPageEditorActionBarContributor c : INSTANCES) {
			c.init(bars);
		}
	}

	@Override
	public void setActiveEditor(IEditorPart part) {
		super.setActiveEditor(part);
		for (MultiPageEditorActionBarContributor c : INSTANCES)
			c.setActiveEditor(part);
	}

	@Override
	public void setActivePage(IEditorPart page) {
		for (MultiPageEditorActionBarContributor c : INSTANCES) {
			c.setActivePage(page);
		}
		IActionBars bars = getActionBars();
		if (bars != null) {
			if (page instanceof IEnsembleEditorPart) {
				((IEnsembleEditorPart)page).updateActionBars(bars);
			} 
			bars.setGlobalActionHandler(IPlanEditorConstants.ACTION_SET_COLLAPSE_ALL_RETARGET, collapseActionHandler);
			bars.setGlobalActionHandler(IPlanEditorConstants.ACTION_SET_EXPAND_ALL_RETARGET, expandActionHandler);
//			bars.setGlobalActionHandler(IPlanEditorConstants.ACTION_SET_VALIDATE_PLAN_RETARGET, validatePlanActionHandler);
			bars.updateActionBars();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		for (MultiPageEditorActionBarContributor c : INSTANCES) {
			c.dispose();
		}		
	}

	/**
	 * If the editor is a plan editor, then create the appropriate operation
	 * to set the expanded state.  Also, execute the newly created operation.
	 * 
	 * @param expanded
	 */
	private void createAndExecuteSetExpandedOperation(boolean expanded) {
		IWorkbenchPart part = MultiPagePlanEditorContributor.this.getPage().getActivePart();
		if (part instanceof MultiPagePlanEditor) {
			MultiPagePlanEditor editor = (MultiPagePlanEditor) part;
			EPlan plan = editor.getPlan();
			IUndoableOperation operation = new SetExpandedOperation(plan, expanded);
			try {
				operation.execute(new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
	}

}
