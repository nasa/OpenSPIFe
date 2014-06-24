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
package gov.nasa.ensemble.core.plan.editor.actions;

import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.search.internal.ui.SearchDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class OpenPlanSearchHandler extends AbstractPlanEditorHandler {

	private static final int SEARCH_ID = IDialogConstants.CLIENT_ID + 1;
	private static final int REPLACE_ID = SEARCH_ID + 1;
	private static final int CUSTOMIZE_ID = REPLACE_ID + 1;
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 0;
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		String planSearchPageId = EditorPlugin.ID + "." + "OpenPlanSearchPage";		
		SearchDialog dialog = new PlanSearchDialog(workbenchWindow, planSearchPageId);
		if(dialog != null){
			dialog.open();
		}
		return null;
	}

	protected class PlanSearchDialog extends SearchDialog {
		public PlanSearchDialog(IWorkbenchWindow workbenchWindow, String searchPageId) {
			super(workbenchWindow, searchPageId);	
		}

		@Override
		protected Control createPageArea(Composite parent) {
			Control pageArea = super.createPageArea(parent);
			if(pageArea instanceof Composite) {
				Composite composite = (Composite)pageArea;
				Control[] children = composite.getChildren();
				for(Control control : children) {
					if(control instanceof TabFolder) {
						TabFolder tabFolder = (TabFolder)control;
						TabItem[] tabItems = tabFolder.getItems();
						for(TabItem tabItem : tabItems) {
							if(tabItem.getText().equals("Java Search") ||
							   tabItem.getText().equals("Task Search")) {
								tabItem.dispose();
							}
						}
					}
				}
			}
			return pageArea;
		}

		@Override
		protected Button createButton(Composite parent, int id, String label,
				boolean defaultButton) {
			Button button = super.createButton(parent, id, label, defaultButton);
			if(id == CUSTOMIZE_ID) {
				button.dispose();
			}
			return button;
		}
	}

	@Override
	public String getCommandId() {
		return OPEN_PLAN_SEARCH_COMMAND_ID;
	}
}
