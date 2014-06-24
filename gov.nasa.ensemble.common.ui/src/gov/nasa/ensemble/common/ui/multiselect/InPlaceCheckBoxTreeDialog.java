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
package gov.nasa.ensemble.common.ui.multiselect;

/*******************************************************************************
 * Copyright (c) 2004, 2009 Tasktop Technologies and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.progress.WorkbenchJob;

/**
 * @author Ivy Deliz

 */
public class InPlaceCheckBoxTreeDialog extends AbstractInPlaceDialog {

	private CheckboxFilteredTree valueTree;

	private final List<String> optionsList;
	public List<String> selectedList;
	public List<String> greyedList;
	
	private class CheckboxFilteredTree extends FilteredTree {

		@SuppressWarnings("deprecation")
		public CheckboxFilteredTree(Composite parent, int treeStyle, PatternFilter filter) {
			super(parent, treeStyle, filter);
		}

		@Override
		protected WorkbenchJob doCreateRefreshJob() {
			WorkbenchJob job = super.doCreateRefreshJob();
			job.addJobChangeListener(new JobChangeAdapter() {
				@SuppressWarnings("deprecation")
				@Override
				public void done(IJobChangeEvent event) {
					if (event.getResult() != null && event.getResult().isOK() && !getViewer().getTree().isDisposed()) {
						Set<String> joinChecked = new HashSet(getSelectedValues());
						for(String i : getPartialValues()) {
							getViewer().setGrayed(i, true);
							joinChecked.add(i);
						}
						getViewer().setCheckedElements(joinChecked.toArray());
					}
				}
			});
			return job;
		}

		@Override
		protected TreeViewer doCreateTreeViewer(Composite parent, int style) {
			return new CheckboxTreeViewer(parent, style);
		}

		@Override
		public CheckboxTreeViewer getViewer() {
			return (CheckboxTreeViewer) super.getViewer();
		}

	}
	
	public InPlaceCheckBoxTreeDialog(Shell shell, Control openControl, List<String> values, List<String> greyed,
			List<String> options) {
		super(shell, SWT.RIGHT, openControl);
		setShellStyle(getShellStyle());
		
		this.selectedList = values;
		this.greyedList = greyed;
		this.optionsList = options;
	}

	public InPlaceCheckBoxTreeDialog(Shell shell, Control openControl, List<String> values, List<String> greyed,
			List<String> options, String dialogLabel) {
		
		super(shell, SWT.RIGHT, openControl);
		setShellStyle(getShellStyle());
		
		this.selectedList = values;
		this.greyedList = greyed;
		this.optionsList = options;
	}

	@Override
	protected Control createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = MARGIN_SIZE;
		layout.marginWidth = MARGIN_SIZE;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		composite.setLayoutData(gd);

		valueTree = new CheckboxFilteredTree(composite, SWT.CHECK | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER, new SubstringPatternFilter());
		gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH);
		gd.heightHint = 175;
		gd.widthHint = 160;
		CheckboxTreeViewer viewer = valueTree.getViewer();
		viewer.getControl().setLayoutData(gd);

		if (optionsList != null) {
			
			viewer.setContentProvider(new ITreeContentProvider() {
				
				@Override
				public Object[] getChildren(Object parentElement) {
					if (parentElement instanceof List<?>) {
						return ((List<?>) parentElement).toArray();
					}
					return null;
				}
				
				@Override
				public Object getParent(Object element) {
					return null;
				}

				@Override
				public boolean hasChildren(Object element) {
					return false;
				}

				@Override
				public Object[] getElements(Object inputElement) {
					return getChildren(inputElement);
				}

				@Override
				public void dispose() {
					//do nothing
				}

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					//do nothing
				}

			});

			viewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof String) {
						return (String) element;
					}
					return super.getText(element);
				}
			});
			
			optionsList.remove(null);
			viewer.setInput(optionsList);
			
			Set<String> invalidValues = new HashSet<String>();

			// Remove any currently entered invalid values
			for (String value : selectedList) {
				if (!optionsList.contains(value)) {
					invalidValues.add(value);
				}
			}
			// Remove any unselected values
			for (String value : optionsList) {
				if (!viewer.setChecked(value, true)) {
					invalidValues.add(value);
				}
			}
			
			selectedList.removeAll(invalidValues);
			
			Set<String> joinChecked = new HashSet<String>(getSelectedValues());
			for(String i : getPartialValues()) {
				viewer.setGrayed(i, true);
				joinChecked.add(i);
			}
			viewer.setCheckedElements(joinChecked.toArray());
		}

		this.addEventListener(new IInPlaceDialogListener() {

			@Override
			public void buttonPressed(InPlaceDialogEvent event) {
				if (event.getReturnCode() == AbstractInPlaceDialog.ID_OK) {
					dialogButtonPressOK();
				} else if (event.getReturnCode() == AbstractInPlaceDialog.ID_DESELECT_ALL) {
					dialogButtonPressDeselect();
				}

			}
		});
		
		viewer.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
								
				if (event.getChecked()) {
					if(greyedList.contains(event.getElement())) {
						greyedList.remove(event.getElement());
						valueTree.getViewer().setGrayed(event.getElement(), false);
					}
					selectedList.add((String) event.getElement());
				} else {	
					if(greyedList.contains(event.getElement())) {
						greyedList.remove(event.getElement());
						valueTree.getViewer().setGrayed(event.getElement(), false);
					} else {
						selectedList.remove(event.getElement());
					}
				}
			}
		});

		return valueTree;
	}

	public void dialogButtonPressOK() {
		//do nothing... so override
	}

	public void dialogButtonPressDeselect() {
		//do nothing... so override
	}

	public void setSelectedValues(List<String> newValues) {
		selectedList = newValues;
	}
		
	public List<String> getSelectedValues() {
		return selectedList;
	}
	
	public void setPartialValues(List<String> newPartialValues) {
		greyedList = newPartialValues;
	}
	
	public List<String> getPartialValues() {
		return greyedList;
	}

}
