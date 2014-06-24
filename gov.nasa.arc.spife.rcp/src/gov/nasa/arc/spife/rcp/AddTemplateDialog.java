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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.core.model.plan.EPlan;

import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class AddTemplateDialog extends Dialog {

	private final Shell parent;
	private final List<EPlan> templatePlans;
	AdapterFactoryLabelProvider labelProvider;
	private String[] templatePlanLabels;
	private EPlan selectedPlan;
	private Text nameText;
	private String templateName;
	private Combo templatePlanCombo;
	private Button addButton;
	
	protected AddTemplateDialog(Shell parentShell, List<EPlan> templatePlans, AdapterFactoryLabelProvider labelProvider, EPlan selectedPlan) {
		super(parentShell);
		parent = parentShell;
		this.templatePlans = templatePlans;
		this.labelProvider = labelProvider;
		this.selectedPlan = selectedPlan;
	}
	
	public EPlan getSelectedPlan() {
		return selectedPlan;
	}

	public String getTemplateName() {
		return templateName;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(400, 200);
        newShell.setText("Add a New Template");
        placeDialogInCenter(parent, newShell);
        super.configureShell(newShell);
	}
	
	private static void placeDialogInCenter(Shell parent, Shell shell){
		Rectangle parentSize = parent.getBounds();
		Rectangle mySize = shell.getBounds();

		int locationX, locationY;
		locationX = (parentSize.width - mySize.width)/2+parentSize.x;
		locationY = (parentSize.height - mySize.height)/2+parentSize.y;

		shell.setLocation(new Point(locationX, locationY));
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		// grab the correct button for display
		addButton = getButton(IDialogConstants.OK_ID);
		addButton.setText("Add");
		addButton.setEnabled(false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginLeft = 25;
        layout.marginTop = 25;
        layout.verticalSpacing = 25;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		nameLabel.setText("Template Name:");
		
		nameText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		nameText.setLayoutData(new GridData(200, SWT.DEFAULT));
		
		nameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				checkAddButton();
			}
		});
		
        Label comboLabel = new Label(composite, SWT.NONE);
		comboLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		comboLabel.setText("Template Plan:");
		
		templatePlanCombo = new Combo(composite, SWT.NONE);
		templatePlanCombo.setLayoutData(new GridData(200, SWT.DEFAULT));
		templatePlanCombo.setItems(getTemplatePlanLabels());
		templatePlanCombo.select(getInitialSelection());
		templatePlanCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				checkAddButton();
			}
		});
		
        return composite;
	}

	private String[] getTemplatePlanLabels() {
		if (templatePlanLabels == null) {
			templatePlanLabels = new String[templatePlans.size()];
			int index = 0;
			for (EPlan plan : templatePlans) {
				templatePlanLabels[index] = labelProvider.getText(plan);
				index++;
			}
		}
		return templatePlanLabels;
	}
	
	private int getInitialSelection() {
		if (selectedPlan == null) {
			return -1;
		}
		int index = 0;
		for (EPlan plan : templatePlans) {
			if (plan == selectedPlan) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	private EPlan updateSelectedPlan() {
		int index = templatePlanCombo.getSelectionIndex();
		if (index >= 0) {
			return templatePlans.get(index);
		}
		return null;
	}
	
	protected void checkAddButton() {
		boolean enabled = !nameText.getText().trim().isEmpty() && templatePlanCombo.getSelectionIndex() != -1;
		addButton.setEnabled(enabled);
	}

	@Override
	protected void okPressed() {
		templateName = nameText.getText().trim();
		selectedPlan = updateSelectedPlan();
		super.okPressed();
	}

}
