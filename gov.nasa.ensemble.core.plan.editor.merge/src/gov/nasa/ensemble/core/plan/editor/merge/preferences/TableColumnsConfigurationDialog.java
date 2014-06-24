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
package gov.nasa.ensemble.core.plan.editor.merge.preferences;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.PickListFieldEditor;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.configuration.ColumnConfigurationResource;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class TableColumnsConfigurationDialog extends Dialog {

	final static String TITLE = "Table Column Configuration";
	final static String REOPEN_EDITOR_MESSAGE = "For the column configuration to take effect the editor will close and reopen.";
		
	private Collection<AbstractMergeColumn> allColumns;
	private ColumnConfigurationResource resource;
	private Map<String, AbstractMergeColumn> columnMap;
	private PickListFieldEditor columnPickList;
	private URI planURI;
	private boolean reopenEditorOnColumnChange;
	
	
	public TableColumnsConfigurationDialog(Shell parent, Collection<AbstractMergeColumn> allColumns, 
			ColumnConfigurationResource r, URI planURI, boolean reopenEditor) {
		super(parent);
		this.allColumns = allColumns;
		this.resource = r;
		setBlockOnOpen(true);
		setShellStyle(SWT.SHEET);
		this.planURI = planURI;
		this.reopenEditorOnColumnChange = reopenEditor;
		if (reopenEditorOnColumnChange && planURI == null) {
			throw new IllegalArgumentException("planLocatioURI needs to be provided for reopening editor on column configuration change.");
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(TITLE);
		super.configureShell(newShell);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		columnMap = new LinkedHashMap(); 
		for (AbstractMergeColumn column : allColumns) {
			columnMap.put(column.getId(), column);
		}
		columnPickList = new PickListFieldEditor("", TITLE, composite, columnMap.keySet(), true) {
			@Override
			protected void doLoad() {
				super.doLoad(getCurrentColumnNames());
			}
			
			@Override
			protected void doLoadDefault() {
				super.doLoad(getCurrentColumnNames());
			}
		
			@Override
			protected void doLoad(String commaSeparated) {
				if (columnPickList == null) {
					this.doLoad();
				} else {
					super.doLoad(commaSeparated);
				}
			}
			
			@Override
			protected void doLoad(java.util.List<String> showOnRight) {
				if (columnPickList == null) {
					this.doLoad();
				} else {
					super.doLoad(showOnRight);
				}
			}
		};
		if (reopenEditorOnColumnChange) {
			Label label = new Label(composite, SWT.NONE);
			label.setText(REOPEN_EDITOR_MESSAGE);
		}
		return composite;
	}
	
	@Override
	protected void okPressed() {
		//get the new columns & close dialog
		String[] selectedItems = getSelectedItems();
		super.okPressed();
		
		IWorkbenchPage activePage = null;
		int tabIndex = 0;
		if (reopenEditorOnColumnChange) {
			//close editor and save for prompt
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			activePage = window.getActivePage();
			IEditorPart planEditor = null;
			for (IEditorPart editorPart : EditorPartUtils.getEditors()) {
				IEditorInput editorInput = editorPart.getEditorInput();
				PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(editorInput);
				EPlan plan = model.getEPlan();
				IFile file = PlanUtils.getFile(plan);
				URI uri = file.getLocationURI();
				if (CommonUtils.equals(uri, planURI)) {
					planEditor = editorPart;
					break;
				}
			}
			if (planEditor == null) {
				planEditor = EditorPartUtils.getCurrent(window);
			}
			if (planEditor instanceof MultiPagePlanEditor) {
				tabIndex = ((MultiPagePlanEditor) planEditor).getActivePage();
			}
			activePage.closeEditor(planEditor, true);
		}
		
		//set columns
		List<AbstractMergeColumn> selectedColumns = new ArrayList();
		for (String item : selectedItems) {
			selectedColumns.add(columnMap.get(item));
		}
		TreeTableColumnConfiguration<AbstractMergeColumn> configuration = resource.getConfiguration();
		selectedColumns.add(0, configuration.getZeroColumn());	
		configuration.setColumns(selectedColumns);
		try {
			resource.save(null);
		} catch (IOException e) {
			LogUtil.error("Couldn't save column configuration: " + resource.getURI().toString());
		}
		
		if (reopenEditorOnColumnChange && activePage != null) {
			//reopen editor
			try {
				IEditorPart openEditor = IDE.openEditor(activePage, this.planURI, MultiPagePlanEditor.ID, true);
				if (openEditor instanceof MultiPagePlanEditor) {
					MultiPagePlanEditor editor = (MultiPagePlanEditor) openEditor;
					editor.setActiveEditor(editor.getEditor(tabIndex));
				}
			} catch (PartInitException e) {
				LogUtil.error(e);
			}
		}
		
	}
	
	private List<String> getCurrentColumnNames() {
    	List<String> currentColumnNames = new ArrayList();
    	if (resource != null) {
    		for (AbstractMergeColumn column : (List<AbstractMergeColumn>) resource.getConfiguration().getColumns()) {
    			currentColumnNames.add(column.getId());
    		}
    		currentColumnNames.remove(0);
    	}
    	return currentColumnNames;
    }
	
	private String[] getSelectedItems() {
		return columnPickList.getSelectedListControl().getItems();
	}
}
