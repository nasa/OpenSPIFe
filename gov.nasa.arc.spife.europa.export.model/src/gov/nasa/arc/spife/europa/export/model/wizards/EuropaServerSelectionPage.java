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
package gov.nasa.arc.spife.europa.export.model.wizards;

import java.net.MalformedURLException;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EuropaServerSelectionPage extends WizardPage {

	private static final String P_DOMAIN = "gov.nasa.arc.spife.europa";
	/* package */static final String P_MODEL_NAME = P_DOMAIN + ".modelname";

	private Text adInfoText;
	private Button overwriteModelButton;

	protected ModifyListener validator = new ModifyListener() {
		@Override
		public void modifyText(ModifyEvent e) {
			setPageComplete(validatePage());
		}
	};

	/**
	 * Constructs a WizardPage that has as its main component a file selector.
	 * The style corresponds to the style supplied to the FileDialog class.
	 * Please refer to that for further information.
	 * 
	 * @param pageName
	 * @param style
	 */
	public EuropaServerSelectionPage() {
		super("export_model_page");
	}

	@Override
	public void dispose() {
		if (overwriteModelButton != null) {
			overwriteModelButton.dispose();
			overwriteModelButton = null;
		}
		if (validator != null)
			validator = null;

		super.dispose();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridData data = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
			composite.setLayoutData(data);

			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.makeColumnsEqualWidth = false;
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			composite.setLayout(layout);
		}

		final Label adDescLabel = new Label(composite, SWT.LEFT);
		{
			final GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			adDescLabel.setLayoutData(data);
		}
		adDescLabel.setText("&AD Information:");

		adInfoText = new Text(composite, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP);
		{
			final GridData data = new GridData(GridData.FILL_BOTH);
			data.horizontalSpan = 2;
			adInfoText.setLayoutData(data);
		}
		updateadInfoText(ActivityDictionary.getInstance());

		overwriteModelButton = new Button(composite, SWT.CHECK);

		overwriteModelButton.setSelection(false);
		overwriteModelButton.setText("Overwrite the existing model");

		setPageComplete(validatePage());
		setControl(composite);
	}

	protected void updateadInfoText(ActivityDictionary ad) {
		String versionLabel = ad.getNsURI();
		String versionNumber = ad.getVersion();
		String author = ad.getAuthor();
		String date = ad.getDate();
		String description = ad.getDescription();

		final StringBuilder formText = new StringBuilder();

		try {
			formText.append(ActivityDictionaryPreferences.getActivityDictionaryLocation().toString()).append("\n\n");
		} catch (MalformedURLException e) {
			/* nothing */
		}

		if (versionNumber != null && versionLabel == null) {
			formText.append("Version: ").append(versionNumber).append("\n\n");
		}
		if (author != null) {
			formText.append("Author: ").append(author).append("\n\n");
		}
		if (date != null) {
			formText.append("Date: ").append(date).append("\n\n");
		}
		if (description != null) {
			formText.append("Description: ").append(description).append("\n\n");
		}

		if (formText.length() == 0) {
			formText.append("No AD Metadata available.");
		}

		// TODO: add file based meta data here... file modification date...etc?
		// less useul then the dates in the file.

		if (ActivityDictionary.getInstance() != ad) {
			formText.insert(0, "Info from AD to take effect at next launch");
		}

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				adInfoText.setText(formText.toString());
			}
		});

	}

	@Override
	protected void setControl(Control newControl) {
		super.setControl(newControl);
	}

	protected boolean validatePage() {
		return true;
	}

	public boolean overwriteModel() {
		return overwriteModelButton.getSelection();
	}
}
