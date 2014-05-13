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
package gov.nasa.arc.spife.core.plan.pear.view.wizard;

import gov.nasa.arc.spife.core.plan.pear.view.internal.Messages;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ProfileSourceWizardPage extends EnsembleWizardPage {

	private static final int DEFAULT_DIALOG_WIDTH = 500;
	private static final int DESCRIPTION_INDENT = 30;
	private static final int DEFAULT_LABEL_LENGTH = DEFAULT_DIALOG_WIDTH - DESCRIPTION_INDENT;

	private Button activityResourceButton;
	private Button externalConditionButton;

	
	public ProfileSourceWizardPage() {
		super("wizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ProfileSourceWizardPage_Title);
		setMessage(Messages.ProfileSourceWizardPage_Description);
	}

	@Override
	public void createControl(Composite parent) {
		
		 Composite composite = new Composite(parent, SWT.NONE);
		 GridLayoutFactory.fillDefaults().spacing(0, 10)
		 								 .applyTo(composite);
		 // Activity Resource
		 activityResourceButton = new Button(composite, SWT.RADIO);
		 activityResourceButton.setText(Messages.ProfileSourceWizardPage_ActiveResourceButton_Label);
		 activityResourceButton.setSelection(true);
		 GridDataFactory.fillDefaults().indent(0, 5)
									   .applyTo(activityResourceButton);
		 
		 final Label activityResourceDescriptionLabel = new Label(composite, SWT.WRAP);
		 activityResourceDescriptionLabel.setText(Messages.ProfileSourceWizardPage_ActiveResourceLabel_Message);
		 GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   .grab(true, false)
		 							   .applyTo(activityResourceDescriptionLabel);
		 
		 // External Condition
		 externalConditionButton = new Button(composite, SWT.RADIO);
		 externalConditionButton.setText(Messages.ProfileSourceWizardPage_ExternalConditionButton_Label);
		 GridDataFactory.fillDefaults().indent(0, 5)
									   .applyTo(externalConditionButton);
		 
		 final Label externalConditionDescriptionLabel = new Label(composite, SWT.WRAP);
		 externalConditionDescriptionLabel.setText(Messages.ProfileSourceWizardPage_ExternalConditionLabel_Description);
		 GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   .grab(true, false)
		 							   .applyTo(externalConditionDescriptionLabel);
		 
		 // Set tab list
		 composite.setTabList(new Control[]{activityResourceButton,externalConditionButton});
		 
		 setControl(composite);
	}

	public boolean isActivityResource() {
		return activityResourceButton.getSelection();
	}
	
	public boolean isExternalCondition() {
		return externalConditionButton.getSelection();
	}
	
}
