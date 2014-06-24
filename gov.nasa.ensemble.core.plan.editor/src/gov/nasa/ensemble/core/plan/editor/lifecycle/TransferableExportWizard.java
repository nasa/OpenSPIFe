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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.SimpleByteArrayTransferProvider;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;

public class TransferableExportWizard extends PlanExportWizardImpl {

	private static final PlanContainerTransferProvider containerTransferProvider = new PlanContainerTransferProvider();
	private static final ActivityTransferProvider activityTransferProvider = new ActivityTransferProvider();
	private IStructuredSelection selection;
	private ExtraWizardPageOptions extraOptions;
	
	public TransferableExportWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		this.selection = selection;
	}

	@Override
	protected FileSelectionPage createFileSelectionPage() {
		TransferableExportFileSelectionPage page = new TransferableExportFileSelectionPage() {
			@Override
			public boolean isPageComplete() {
				URI fileURI = URI.createFileURI(getFieldEditorValue());
				if (fileURI == null || fileURI.isEmpty()) {
					clearError(this.getClass());
					return false;
				}
				String requiredExt = getPreferredExtension();
				String enteredExt = fileURI.fileExtension();
				if (requiredExt != null && (enteredExt == null || !enteredExt.equals(requiredExt))) {
					setError(this.getClass(), "The file name must end in " + requiredExt);
					return false;
				}
				return true;
			}
		};
		page.setFileType(getPreferredExtension());
		page.setSelection(selection);
		
		extraOptions = ExtraWizardPageOptions.buildExtraWizardPageOptions();
		page.setExtraWizardPageOptions(extraOptions);
		
		return page;
	}

	@Override
	protected String getPreferredExtension() {
		return "spife";
	}
	
	private boolean isExportSelected() {
		if (getPage() == null) {
			return false;
		}
		return ((TransferableExportFileSelectionPage)getPage()).isExportSelection();
	}

	@Override
	protected void savePlan(final EPlan plan, final File file) throws Exception {
		final boolean isExportSelected = isExportSelected();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					exportPlan(plan, file, isExportSelected, monitor);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					throw new InvocationTargetException(t);
				}
			}
		};
		getContainer().run(true, true, op);
	}
	
	private void exportPlan(EPlan plan, File file, boolean isExportSelected, IProgressMonitor monitor) throws Exception {
	    monitor.beginTask("Exporting plan...", 100);
	    if (monitor.isCanceled()) {
	    	return;
	    }
	    ISelection exportSelection = new StructuredSelection(plan);
	    if (isExportSelected) {
	    	monitor.subTask("setting selection");
	    	exportSelection = selection; 
	    }
	    if (monitor.isCanceled()) {
	    	return;
	    }
	    monitor.subTask("serializing plan elements");
	    PlanTransferable originalTransferable = PlanStructureModifier.INSTANCE.getTransferable(exportSelection);
	    ITransferable outputTransferable = PlanStructureModifier.INSTANCE.copy(originalTransferable);
	    SimpleByteArrayTransferProvider provider = null;
	    if (containerTransferProvider.canPack(outputTransferable)) {
	    	provider = containerTransferProvider;
	    } else if (activityTransferProvider.canPack(outputTransferable)) {
	    	provider = activityTransferProvider;
	    } else {
	    	throw new IllegalArgumentException("incompatible selection for export");
	    }
	    byte[] outputObject = provider.packTransferObject(outputTransferable);
	    outputTransferable.dispose();
	    monitor.worked(10);
	    if (monitor.isCanceled()) {
	    	return;
	    }
	    monitor.subTask("writing the bytes to disk");
	    FileOutputStream output = new FileOutputStream(file, false);
	    output.write(outputObject);
	    output.flush();
	    output.close();
	    monitor.worked(75);
	    if (monitor.isCanceled()) {
	    	return;
	    }
	    if (extraOptions != null) {
	    	extraOptions.executeOptions(plan, file.getAbsolutePath());
	    }
	    monitor.worked(10);
    }

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		// FIXME provide large icon
		return null;
	}
}
