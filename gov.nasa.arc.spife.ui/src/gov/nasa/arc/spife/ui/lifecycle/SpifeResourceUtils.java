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
package gov.nasa.arc.spife.ui.lifecycle;

import gov.nasa.arc.spife.ui.Activator;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.osgi.framework.Bundle;

public abstract class SpifeResourceUtils extends ResourceUtils {

	private static final String TIMELINE_FILE_KEY = "timeline.template.file";
	private static final String TIMELINE_FILE = EnsembleProperties.getProperty(TIMELINE_FILE_KEY);

	public static IFile createTimelineFile(IFile planFile) {
		URI planURI = EMFUtils.getURI(planFile);
		URI timelineURI = planURI.trimFileExtension().appendFileExtension(TimelineConstants.TIMELINE_FILE_EXT);
		return EMFUtils.getFile(timelineURI);
	}

	public static void buildTimelineFile(IFile timelineFile) throws CoreException, IOException {
		if (!timelineFile.exists()) {
			Bundle sourceBundle = Activator.getDefault().getBundle();
			Path sourcePath = new Path("/datafiles/" + TIMELINE_FILE);
			InputStream inputStream = FileLocator.openStream(sourceBundle, sourcePath, true);
			timelineFile.create(inputStream, true, null);
		}
	}
	
	public static IEditorPart openPlanEditorFromWizard(final IFile planFile, final IWorkbench workbench, IWizardContainer container) {
		final IEditorPart[] result = new IEditorPart[1];
		try {
			container.run(false, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						monitor.beginTask("Opening " + planFile.getName() + " in editor", IProgressMonitor.UNKNOWN);
						result[0] = openEditorOnPlanFile(planFile, workbench);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (Exception e) {
			LogUtil.error(e);
		}
		return result[0];
	}

	/**
	 * Opens an editor on the given file resource. This method will attempt to
	 * resolve the editor based on content-type bindings as well as traditional
	 * name/extension bindings.
	 * 
	 * @param file
	 * @param workbench
	 * @return The resulting editor. Returns null if the file failed to open.
	 */
	public static IEditorPart openEditorOnPlanFile(IFile planFile, IWorkbench workbench) {
        // Open editor on new file.
        IWorkbenchWindow dw = workbench.getActiveWorkbenchWindow();
        IEditorPart editor = null;
        if (dw != null) {
	        try {
	            IWorkbenchPage page = dw.getActivePage();
	            if (page != null) {
	                editor = IDE.openEditor(page, planFile, true);
	            }
	        } catch (PartInitException e) {
	            openError(dw.getShell(), "Problems Opening Editor", e.getMessage(), e);
	        }		
        }
        return editor;
	}
}
