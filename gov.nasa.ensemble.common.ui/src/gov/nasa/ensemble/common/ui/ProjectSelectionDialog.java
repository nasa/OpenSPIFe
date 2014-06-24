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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ProjectSelectionDialog extends SelectionDialog {
	
	private Table projectsTable;
	private Label statusMessage;
	private ILabelProvider labelProvider;
	private Predicate<IProject> projectFilter;

	public ProjectSelectionDialog(Shell parentShell, String message, ILabelProvider labelProvider) {
		super(parentShell);
		setTitle("Select a working project");
		this.labelProvider = labelProvider;
		if (message != null) {
			setMessage(message);
		} else {
			setMessage("Blah blah blah");
		}
	}
	
	public void setProjectFilter(Predicate<IProject> projectFilter) {
		this.projectFilter = projectFilter;
	}
	
	@SuppressWarnings("restriction")
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		projectsTable = new Table(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 250;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		projectsTable.setLayoutData(gridData);
		
		TableViewer listViewer = new TableViewer(projectsTable);
		listViewer.setUseHashlookup(true);
		listViewer.setContentProvider(new ProjectListProvider());
		// it seems like there may be a way to do this that doesn't involve passing around this label provider explicitly.
		if (labelProvider != null)
			listViewer.setLabelProvider(labelProvider);
		listViewer.setInput(ResourcesPlugin.getWorkspace());
		listViewer.setSelection(new StructuredSelection(getInitialElementSelections()));
		listViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ProjectSelectionDialog.this.okPressed();
			}
		});
		
		statusMessage = new Label(composite, SWT.WRAP);
		statusMessage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statusMessage.setText(" \n "); //$NON-NLS-1$
		statusMessage.setFont(parent.getFont());
		return composite;
	}

	@Override
	protected void okPressed() {
        ArrayList<IProject> chosenProjectList = new ArrayList<IProject>();
        for(TableItem projectName : projectsTable.getSelection()) {
        	IProject project = (IProject) projectName.getData();
        	chosenProjectList.add(project);
        }
        setResult(chosenProjectList);
        super.okPressed();
    }

	private class ProjectListProvider implements IStructuredContentProvider {		
		@Override
		public void dispose() {/* no operation */}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {/* no operation */}
		@Override
		public Object[] getElements(Object inputElement) {
			IProject[] allProjects = ((IWorkspace)inputElement).getRoot().getProjects();
			if (projectFilter != null) {
				return Lists.filter(Arrays.asList(allProjects), projectFilter).toArray();
			}
			return allProjects;
		}
	}
}
