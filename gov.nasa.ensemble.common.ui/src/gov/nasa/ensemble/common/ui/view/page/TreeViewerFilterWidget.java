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
package gov.nasa.ensemble.common.ui.view.page;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetPlugin;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class TreeViewerFilterWidget extends Composite {

	private TreeViewerPage treeViewerPage;
	
	/** 
	 * If greater than 0, prevent excess activity by not searching with every keystroke; pause for the 
	 * specified number of milliseconds to calm things down. 
	 */
	protected int searchDelay = 0;
	
	/**
	 * The user can filter the elements in the view by entering search criteria in the filter text widget.
	 * Initially this text box is empty.  Set in createControl().
	 */
	protected Text textField  = null;

	/**
	 * The label for the search. Set in createControl().
	 */
	protected Combo findFilterCombo = null;
	private String[] searchKeys;

	public TreeViewerFilterWidget(TreeViewerPage treeViewerPage, Composite parent, String... searchKeys) {
		super(parent, SWT.NONE);
		this.treeViewerPage = treeViewerPage;
		this.searchKeys = searchKeys;
		init();
	}
	
	/**
	 * Create a combo control and a text control for filtering. The combo has only one element,
	 * but if that is ever changed, it clears the text box. When the text control changes, the
	 * tree viewer is refreshed.
	 * Auxiliary to createControl().
	 */
	private void init() {
		GridLayout rowGrid = new GridLayout(3, false);
		setLayout(rowGrid);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		findFilterCombo = new Combo(this, SWT.READ_ONLY | SWT.BORDER | SWT.DROP_DOWN);
		for (String choice : searchKeys) {			
			findFilterCombo.add(choice);
		}
		findFilterCombo.select(0);
		findFilterCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(textField != null){
					textField.setText("");
				}
			}
		});
		
		// create the find filter text field
		textField = new Text(this, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		final GridData gridData = new GridData();
		gridData.widthHint = 250;
		textField.setLayoutData(gridData);
		setupFilterTextListener(searchDelay);
		
		if (CommonUtils.isOSWindows()) {
			if ((textField.getStyle() & SWT.ICON_CANCEL) == 0) {
				ImageRegistry registry = WidgetPlugin.getDefault().getImageRegistry();
				Image image = registry.get(WidgetPlugin.KEY_IMAGE_CLOSE_NICE);
				ToolBar toolBar = new ToolBar (this, SWT.FLAT);
				ToolItem clearButton = new ToolItem (toolBar, SWT.PUSH);
				clearButton.setImage (image);
				clearButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (e.detail == 0) {
							textField.setText("");
						}
					}
				});
			}
		}
		
	}

	/** 
	 * Subclasses should override this method if they support a non-zero searchDelay
	 * @param textField
	 * @param searchDelay
	 */
	protected void setupFilterTextListener(int searchDelay) {
		textField.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				performSearch();
			}
		});
	}
	
	protected void performSearch() {
		treeViewerPage.doFiltering(findFilterCombo.getText(), textField.getText());
	}

	public void setSearchDelay(int searchDelay) {
		this.searchDelay = searchDelay;
	}

	public String getNameFilterText() {
		if(textField == null || textField.isDisposed()) {
			return null; 
		}
		return textField.getText();
	}

	public Text getTextField() {
		return textField;
	}

}
