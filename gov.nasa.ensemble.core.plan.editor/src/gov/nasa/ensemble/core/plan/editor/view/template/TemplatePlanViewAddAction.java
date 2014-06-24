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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class TemplatePlanViewAddAction extends Action implements ISelectionChangedListener {

	protected TemplatePlanView templatePlanView;
	protected ImageDescriptor addButtonImageDescriptor;
	private IPartListener partListener;
	
	public TemplatePlanViewAddAction(String text, int style, TemplatePlanView templatePlanView) {
		super(text, style);
		this.templatePlanView = templatePlanView;
		addButtonImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "icons/add.png");
		this.setEnabled(false);
		IWorkbenchPartSite site = templatePlanView.getSite();
		if(partListener == null) {
			partListener = new PartListener();
		}
		if(site != null) {
			IWorkbenchWindow workbenchWindow = site.getWorkbenchWindow();
			if(workbenchWindow != null) {
				IPartService partService = workbenchWindow.getPartService();
				if(partService != null) {
					partService.addPartListener(partListener);
				}
			}
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return addButtonImageDescriptor;
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		boolean enabled = shouldBeEnabled(selection);
		this.setEnabled(enabled);
	}
	
	public void dispose() {
		IWorkbenchPartSite site = templatePlanView.getSite();
		if(partListener != null) {
			if(site != null) {
				IWorkbenchWindow workbenchWindow = site.getWorkbenchWindow();
				if(workbenchWindow != null) {
					IPartService partService = workbenchWindow.getPartService();
					partService.removePartListener(partListener);
				}
			}	
		}
		addButtonImageDescriptor = null;
	}

	public void updateEnablement() {
		boolean shouldBeEnabled = false;
		IWorkbenchPartSite site = templatePlanView.getSite();
		if (site != null) {
			ISelectionProvider selectionProvider = site.getSelectionProvider();
			if (selectionProvider != null) {
				ISelection selection = selectionProvider.getSelection();
				shouldBeEnabled = shouldBeEnabled(selection);
			}
		}
		setEnabled(shouldBeEnabled);
	}
	
	/**
	 * Given a particular selection, determine if this action should be enabled
	 * @param selection a selection
	 * @return true if this action should be enabled, false otherwise
	 */
	public abstract boolean shouldBeEnabled(ISelection selection);
	
	private class PartListener implements IPartListener {
		@Override
		public void partOpened(IWorkbenchPart part) {
			// do nothing
		}

		@Override
		public void partDeactivated(IWorkbenchPart part) {
			// do nothing
		}

		@Override
		public void partClosed(IWorkbenchPart part) {
			// if there current page is not a template plan page, the button should be disabled
			if(templatePlanView.getCurrentTemplatePlan() == null) {
				TemplatePlanViewAddAction.this.setEnabled(false);
			}
		}

		@Override
		public void partBroughtToTop(IWorkbenchPart part) {
			// do nothing
		}

		@Override
		public void partActivated(IWorkbenchPart part) {
			// do nothing
		}

	}

}
