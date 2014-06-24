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
package gov.nasa.arc.spife.rcp.importer;

import gov.nasa.arc.spife.rcp.Activator;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.lifecycle.FileSelectionPage;
import gov.nasa.ensemble.common.ui.wizard.EnsembleImportWizard;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ImportCsvActivitiesWizard extends EnsembleImportWizard {
	
	private EPlan plan;
	private FileSelectionPage mainPage;
    private ImageDescriptor largeImageDescriptor
	= AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID
		   , "icons/full/wizban/csv.png");  

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(workbench);
		if (model != null) {
			plan = model.getEPlan();
		}
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}

    @Override
	public void addContentPages() {
		mainPage = new FileSelectionPage("Choose event file to import");
		addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
    	InputStream stream = null;
    	try {
    		stream = new FileInputStream(mainPage.getSelectedFile());
    		List<EActivity> activities = new CsvActivityInstanceParser(stream).parse();
    		final IUndoableOperation op = new ImportCsvActivitiesOperation(plan, activities);
    		IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
    		CommonUtils.execute(op, undoContext);
    		return true;
    	} catch (Exception e) {
    		mainPage.setErrorMessage(e.toString());
    	} finally {
    		if (stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					// ignore error when closing
				}
    	}
    	return false; // on error
    }

}
