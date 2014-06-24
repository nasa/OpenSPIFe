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
package gov.nasa.ensemble.core.detail.emf.util;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class EMFReadWriteUtils {
	
	public static void modify(Object facet, Object value, IUndoContext undoContext, IItemPropertyDescriptor itemPropertyDescriptor, EditingDomain editingDomain) {
		Object feature = itemPropertyDescriptor.getFeature(facet);
		modify(facet, value, feature, undoContext, itemPropertyDescriptor, editingDomain);
	}
	
	public static void modify(Object facet, Object value, IUndoContext undoContext, IItemPropertyDescriptor itemPropertyDescriptor) {
		Object feature = itemPropertyDescriptor.getFeature(facet);
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(facet);
		if (domain == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench != null) {
				IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
				if (activeWorkbenchWindow != null) {
					IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
					if (activePage != null) {
						IEditorPart activeEditor = activePage.getActiveEditor();
						Object adapter = activeEditor.getAdapter(EditingDomain.class);						
						domain = (EditingDomain)adapter;
					}
				}
			}
		}
		modify(facet, value, feature, undoContext, itemPropertyDescriptor, domain);

	}
	
	public static void modify(Object facet, Object value, Object feature, IUndoContext undoContext, IItemPropertyDescriptor itemPropertyDescriptor, EditingDomain domain) {
		if (domain == null) {
			LogUtil.error("can't get editing domain for facet of class: " + facet.getClass().getSimpleName());
		} else {
			Command command = SetCommand.create(domain, facet, feature, value);
			EMFUtils.executeCommand(domain, command);
		}
	}	
}
