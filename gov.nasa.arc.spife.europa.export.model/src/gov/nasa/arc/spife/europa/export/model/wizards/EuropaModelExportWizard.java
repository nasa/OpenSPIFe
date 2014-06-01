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

import gov.nasa.arc.spife.europa.Europa;
import gov.nasa.ensemble.common.ui.ThreadedCancellableRunnableWithProgress;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ui.preference.ActivityDictionaryPreferences;
import gov.nasa.ensemble.dictionary.EActivityDictionary;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class EuropaModelExportWizard extends Wizard implements IExportWizard 
{
	//private static final Logger logger = Logger.getLogger(EuropaModelExportWizard.class);

	private static final WizardPage ERROR_PAGE = new ErrorWizardPage();
	private final EuropaServerSelectionPage serverSelectionPage = new EuropaServerSelectionPage();

	String adURL = null;

	public EuropaModelExportWizard() {
		super();
		setNeedsProgressMonitor(true);
		serverSelectionPage.setTitle("Europa Model Export");
		serverSelectionPage.setMessage("Select server information to export the model to");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		try {
			adURL = ActivityDictionaryPreferences.getActivityDictionaryLocation().toString();
		} catch (MalformedURLException e) {
			/* nothing */
		}
	}

	@Override
	public boolean performFinish() {
		URI uri = URI.createURI(adURL);
		if (uri == null) {
			return false;
		}
		try {
			ActivityDictionary ad = ActivityDictionary.getInstance();
			boolean overwriteModel = serverSelectionPage.overwriteModel();
			IRunnableWithProgress runnable = new ExportCancellableRunnable(ad, overwriteModel);
			getContainer().run(true, true, runnable);
			return true;
		} catch (InterruptedException e) {
			Logger logger = Logger.getLogger(EuropaModelExportWizard.class);
			logger.debug("export cancelled: " + uri, e);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable throwable) {
			if (throwable instanceof InvocationTargetException) {
				throwable = ((InvocationTargetException)throwable).getCause();
			}
			Logger logger = Logger.getLogger(EuropaModelExportWizard.class);
			logger.error("export failed: " + uri, throwable);
			Throwable cause = throwable.getCause();
			if (cause != null) {
				logger.error("***Cause: " + cause.getMessage());
				String message = "Error " + throwable.getMessage();
				if (cause.getMessage() != null) {
					MessageDialog.openError(getShell(), message, cause.getMessage());
				} else {
					MessageDialog.openError(getShell(), message, throwable.getMessage());
				}
			} else {
				String title = "Error uploading the model";
				String message = throwable.getClass().getSimpleName() + ":";
				message += "\n" + throwable.getMessage();
				MessageDialog.openError(getShell(), title, message);
			}
			return false;
		}
		return true;
	}
	
	public static synchronized void uploadModel(
			final EActivityDictionary ad, 
			final boolean overwriteModel, 
			IProgressMonitor monitor) throws Exception 
	{
		Europa.uploadModel(ad, overwriteModel, monitor);
    }

	@Override
	public final void addPages() {
		if (adURL == null) {
			ERROR_PAGE.setMessage("No active AD selected in workspace");
			ERROR_PAGE.setPageComplete(false);
			addPage(ERROR_PAGE);
		} else {
			addPage(serverSelectionPage);
		}
	}

	private class ExportCancellableRunnable extends ThreadedCancellableRunnableWithProgress {

		private final EActivityDictionary ad;
		private final boolean overwriteModel;

	    private ExportCancellableRunnable(EActivityDictionary ad, boolean overwriteModel) {
	    	this.ad = ad;
			this.overwriteModel = overwriteModel;
	    }

		@Override
		public void doRun(IProgressMonitor monitor) throws Exception {
	        uploadModel(ad, overwriteModel, monitor);
        }
    }

	private static class ErrorWizardPage extends WizardPage {

		public ErrorWizardPage() {

			super("error");
		}

		@Override
		public void createControl(Composite parent) {
			setControl(new Composite(parent, SWT.NONE));
		}
	}
}
