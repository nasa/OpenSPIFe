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
package gov.nasa.ensemble.core.activityDictionary.view.preferences;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.preferences.UIPreferenceUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryPlugin;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.ActivityCategoryPreferencePage;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ActivityDictionaryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private final Logger trace = Logger.getLogger(ActivityCategoryPreferencePage.class);

	private Composite top;
	private FormToolkit toolkit;
	private FormText activityInformation;
	private FileFieldEditor activityDictionaryFile;
	private BooleanFieldEditor strictADChecking;

	private ActivityDictionaryPreferencePageHelper helper;

	public ActivityDictionaryPreferencePage() {
		super();
		setPreferenceStore(UIPreferenceUtils.getPreferenceStore(ActivityDictionaryPlugin.getDefault()));
		helper = ActivityDictionaryPreferencePageHelper.INSTANCE;
	}

	@Override
	public void init(IWorkbench workbench) {
		// nothing to do
	}

	@Override
	protected Control createContents(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		top = new Composite(parent, SWT.LEFT);
		// Sets the layout data for the top composite's
		// place in its parent's layout.
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// Sets the layout for the top composite's
		// children to populate.
		top.setLayout(new GridLayout());
		createActivityDictionaryFile(top);
		createStrictADChecking(top);
		createActivityInfoComposite(top);

		if (helper != null)
			helper.createNewField(top, this);
		return top;
	}

	private void createActivityDictionaryFile(Composite parent) {
		Composite fileComp = new Composite(parent, SWT.None);
		activityDictionaryFile = new FileFieldEditor(ActivityDictionaryPreferences.ACTIVITY_DICTIONARY_LOCATION, "URL:", true, fileComp) {
			@Override
			protected String changePressed() {
				String file = super.changePressed();
				if (file != null) {
					try {
						file = URI.createFileURI(new File(file).getPath()).toFileString();
					} catch (Exception e) {
						file = null;
						trace.error(e);
					}
				}
				return file;
			}

			@Override
			protected boolean checkState() {
				String path = getTextControl().getText();
				if (path != null)
					path = path.trim();
				else
					path = "";//$NON-NLS-1$

				if (path.startsWith("file:"))
					return super.checkState();
				else
					return true; // if it is not a file, then not much validation we can do right now
			}
		};
		activityDictionaryFile.setFileExtensions(getDictionaryExtensions());
		activityDictionaryFile.setChangeButtonText("Select new AD");
		activityDictionaryFile.setPage(this);
		activityDictionaryFile.setPreferenceStore(getPreferenceStore());
		activityDictionaryFile.load();
		boolean enableChange = getPreferenceStore().getBoolean(ActivityDictionaryPlugin.ACTIVITY_DICTIONARY_USER_CHANGE_PROPERTY);
		activityDictionaryFile.setEnabled(enableChange, fileComp);
		activityDictionaryFile.getTextControl(fileComp).addModifyListener(new FileModifyListener());
	}

	private String[] getDictionaryExtensions() {
		List<String> list = new ArrayList<String>();
		String pattern = EnsembleProperties.getProperty(ActivityDictionaryPlugin.ACTIVITY_DICTIONARY_FILE_PATTERN_PROPERTY, "*.xad, *.dictionary,*.xml");
		StringTokenizer tokenizer = new StringTokenizer(pattern, ",");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken().trim());
		}
		String[] extensions = list.toArray(new String[list.size()]);
		return extensions;
	}

	private void createStrictADChecking(Composite parent) {
		Composite checkComp = new Composite(parent, SWT.NONE);
		strictADChecking = new BooleanFieldEditor(ActivityDictionaryPlugin.ACTIVITY_DICTIONARY_STRICT_CHECKING_PROPERTY, "Strict AD input checking", checkComp);
		strictADChecking.setPage(this);
		strictADChecking.setPreferenceStore(getPreferenceStore());
		strictADChecking.load();
	}

	private void createActivityInfoComposite(Composite parent) {
		Composite activityInfoComposite = toolkit.createComposite(parent, SWT.BORDER);
		activityInfoComposite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(activityInfoComposite);
		activityInformation = toolkit.createFormText(activityInfoComposite, false);
		activityInformation.setColor("red", ColorMap.RGB_INSTANCE.getColor(new RGB(200, 0, 0)));
		activityInformation.setFont("header", JFaceResources.getHeaderFont());
		updateFormText(ActivityDictionary.getInstance());
	}

	protected void updateFormText(ActivityDictionary ad) {
		String versionLabel = ad.getNsURI();
		String versionNumber = ad.getVersion();
		String author = ad.getAuthor();
		String date = ad.getDate();
		String description = ad.getDescription();

		final StringBuilder formText = new StringBuilder();

		// Add Meta Data
		if (versionLabel != null) {
			formText.append("<p>").append(versionLabel).append("</p>");
		}
		if (versionNumber != null && versionLabel == null) {
			// Phoenix (Jeff Bixler) wants Version Number omitted -- Version Label only. [SPF-1001]
			formText.append("<p><B>Version</B> ").append(versionNumber).append("</p>");
		}
		if (author != null) {
			formText.append("<p><B>Author</B> ").append(author).append("</p>");
		}
		if (date != null) {
			formText.append("<p><B>Date</B> ").append(date).append("</p>");
		}
		if (description != null) {
			formText.append("<p><B>Description</B> ").append(description).append("</p>");
		}

		if (formText.length() == 0) {
			formText.append("<p>No AD Metadata available.</p>");
		}

		// TODO: add file based meta data here... file modification date...etc? less useul then the dates in the file.

		if (ActivityDictionary.getInstance() != ad) {
			formText.insert(0, "<p><span color=\"red\" font=\"header\">").append("Info from AD to take effect at next launch").append("</span></p>");
		}

		// add form code.
		formText.insert(0, "<form>");
		formText.append("</form>");

		WidgetUtils.runInDisplayThread(activityInformation, new Runnable() {
			@Override
			public void run() {
				activityInformation.setText(formText.toString(), true, false);
			}
		});

	}

	@Override
	protected void performDefaults() {
		activityDictionaryFile.loadDefault();
		strictADChecking.loadDefault();

		if (helper != null)
			helper.loadDefault();
		// getDefaultExemptTagsPreference() is a convenience
		// method which retrieves the default preference from
		// the preference store.
		super.performDefaults();
	}

	@Override
	public boolean performOk() {
		String currentPref = getPreferenceStore().getString(ActivityDictionaryPreferences.ACTIVITY_DICTIONARY_LOCATION);
		String newPref = activityDictionaryFile.getStringValue();
		if (!currentPref.equals(newPref)) {
			boolean okRestart = MessageDialog.openConfirm(top.getShell(), "Restart?", "An application restart is required for the change to take effect.\n\n" + "You will be prompted to save current work.");

			if (!okRestart) {
				return false;
			}

			if (helper != null)
				helper.store();

			strictADChecking.store();
			activityDictionaryFile.store();
			// SPF-12113: Changing AD file does not hold after restart. Perform okay first, before restart.
			boolean performOk = super.performOk();
			PlatformUI.getWorkbench().restart();
			return performOk;
		} else {
			strictADChecking.store();
			if (helper != null)
				helper.store();
		}
		return super.performOk();
	}

	private final class FileModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			if (e.widget instanceof Text) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						activityInformation.setText("", false, false);
					}
				});
			}
		}
	}

}
