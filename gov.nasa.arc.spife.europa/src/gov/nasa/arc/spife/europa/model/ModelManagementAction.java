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
package gov.nasa.arc.spife.europa.model;

import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.arc.spife.europa.EuropaSessionClient;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class ModelManagementAction extends AbstractHandler {

	private static final class BoringLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			return String.valueOf(element);
		}
	}

	@Override
	public Object execute(ExecutionEvent event) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				runDialog(window.getShell());
			}
		}
		return null;
	}

	private void runDialog(Shell shell) {
		String europaHost = EuropaPreferences.getEuropaHost();
		int europaPort = EuropaPreferences.getEuropaPort();
		try {
			List<String> modelList = EuropaSessionClient.getModelList(europaHost, europaPort);
			showDialog(shell, europaHost, europaPort, modelList);
		} catch (RuntimeException e) {
			if (!(e instanceof OperationCanceledException))
			ErrorDialog.openError(shell, "Failed to retrieve models", e.getMessage(), new ExceptionStatus(EuropaPlugin.ID, e.getMessage(), e));
		} 
	}

	private void showDialog(Shell shell, String europaHost, int europaPort, List<String> modelList) {
		ElementListSelectionDialog deleteDialog = new ElementListSelectionDialog(shell, new BoringLabelProvider());
		String message = "Select a model or models to delete.";
		deleteDialog.setTitle("Deleting Europa Models");
		deleteDialog.setEmptySelectionMessage(message);
		deleteDialog.setEmptyListMessage("No models on the server.");
		deleteDialog.setMessage(message);
		deleteDialog.setMultipleSelection(true);
		deleteDialog.setBlockOnOpen(true);
		deleteDialog.setElements(modelList.toArray());
		deleteDialog.open();
		Object[] result = deleteDialog.getResult();
		if (result != null) {
			List<String> deleteModels = new ArrayList<String>(result.length);
			for (Object object : result) {
				if (object instanceof String) {
					String string = (String) object;
					deleteModels.add(string);
				}
			}
			try {
				EuropaSessionClient.deleteModels(europaHost, europaPort, deleteModels);
			} catch (RuntimeException e) {
				ErrorDialog.openError(shell, "Failed to delete a model", e.getMessage(), new ExceptionStatus(EuropaPlugin.ID, e.getMessage(), e));
			}
		}
	}

}
