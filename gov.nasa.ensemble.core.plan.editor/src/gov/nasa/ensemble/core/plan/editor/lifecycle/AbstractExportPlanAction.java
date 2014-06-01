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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

abstract public class AbstractExportPlanAction implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window = null;

	public AbstractExportPlanAction() {
		super();
	}

	@Override
	public void dispose() {
		window = null;
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	protected IStructuredSelection getCurrentSelection() {
		IStructuredSelection structuredSeleciton = StructuredSelection.EMPTY;
		IEditorPart current = EditorPartUtils.getCurrent(window);
		if(current != null) {
			Object adapter = current.getAdapter(EPlan.class);
			if(adapter instanceof EPlan) {
				EPlan plan = (EPlan)adapter;
				adapter = plan.getAdapter(IResource.class);
				if(adapter != null) {
					structuredSeleciton = new StructuredSelection(adapter);
				}
			}			
		}
		
		return structuredSeleciton;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		IEditorPart current = EditorPartUtils.getCurrent(window);
		if(current instanceof MultiPagePlanEditor) {
			action.setEnabled(true);
		} else {
			action.setEnabled(false);
		}
	}
	
	protected IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}
	
}
