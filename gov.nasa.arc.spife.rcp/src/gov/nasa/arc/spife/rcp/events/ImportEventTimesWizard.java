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
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.arc.spife.rcp.Activator;
import gov.nasa.arc.spife.rcp.profiles.FileImportWizardPage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoader;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.lifecycle.ActivePlanImportWizard;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ImportEventTimesWizard extends ActivePlanImportWizard {
	
	private static final ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/full/wizban/importOrbitalEvents.png");
	private static final String[] FILE_EXTENSIONS = new String[] { "*.xml","*.csv"};
	
	private FileImportWizardPage mainPage;
	
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			if (dialogSettings.getSection(ImportEventTimesWizard.class.getName()) == null)
				dialogSettings.addNewSection(ImportEventTimesWizard.class.getName());
			setDialogSettings(dialogSettings.getSection(ImportEventTimesWizard.class.getName()));
		}
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}

    @Override
    public void addPages(EPlan plan) {
		mainPage = new FileImportWizardPage("Import Orbital Events",
				"Import orbital event file(s) into the loaded plan.");
    	mainPage.setExtensions(FILE_EXTENSIONS);
		addPage(mainPage);
    }
    
	@Override
	public boolean needsProgressMonitor() {
		return true;
	}

	@Override
	public boolean performFinish() {
		mainPage.saveState();
		try {
			List<File> selectedFiles = mainPage.getSelectedFiles();
			Collection<? extends Profile> profiles = null;
			List<EActivity> allEventList = new ArrayList<EActivity>();
			for(File selectedFile : selectedFiles) {
				URI uri = URI.createFileURI(selectedFile.toString());
				profiles = new ProfileLoader(uri).readProfiles();
				Collection<EActivity> events = EventTimeUpdateOperation.parseEvents(profiles);
				allEventList.addAll(events);
			}
			// Set the operation with all the events
			final IUndoableOperation op = new EventTimeUpdateOperation(getPlan(), allEventList);
			IUndoContext undoContext = TransactionUtils.getUndoContext(getPlan());
			CommonUtils.execute(op, undoContext);
			return true;
		} catch (Exception e) {
			mainPage.setError(getClass(), e.toString());
			LogUtil.error("Import failed.", e);
		}
		return false; // on error
	}

}
