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
package gov.nasa.arc.spife.core.plan.editor.timeline.io;

import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.wizard.EnsembleExportWizard;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class TimelineModelExportWizard extends EnsembleExportWizard implements IExportWizard {

	private FileSelectionPage fileSelectionPage;
	private Timeline timeline;
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		timeline = TimelineUtils.getTimeline(workbench);
	}
	
	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return null;
	}
	
	@Override
	protected void addContentPages() {
		addPage(getFileSelectionPage());
	}
	
	@Override
	public boolean performFinish() {
		File file = getFileSelectionPage().getSelectedFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LogUtil.error(e);
				return false;
			}
		}
		ETimeline eTimeline = timeline.getTimelineModel();
		Resource resource = eTimeline.eResource();
		try {
			OutputStream out = new FileOutputStream(file);
			resource.save(out , null);
		} catch (Exception e) {
			LogUtil.error(e);
			return false;
		}
		return true;
	}

	private FileSelectionPage getFileSelectionPage() {
		if (fileSelectionPage == null) {
			fileSelectionPage = new FileSelectionPage(SWT.SAVE);
			String userHomeDirectory = System.getProperty("user.home");
			if (userHomeDirectory != null) {
				File file = new File(userHomeDirectory);
				ETimeline eTimeline = timeline.getTimelineModel();
				file = new File(file, eTimeline.eResource().getURI().lastSegment());
				fileSelectionPage.setCurrentFile(file);
			}
		}
		return fileSelectionPage;
	}
	
}
