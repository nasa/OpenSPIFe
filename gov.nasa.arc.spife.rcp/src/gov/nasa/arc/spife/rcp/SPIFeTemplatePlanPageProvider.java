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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPage;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPageProvider;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.IEditorPart;

public class SPIFeTemplatePlanPageProvider extends TemplatePlanPageProvider {

	@Override
	public TemplatePlanPage getTemplatePlanPage(IEditorPart editor) {
		if (editor instanceof MultiPagePlanEditor) {
			IFile editorFile = CommonUtils.getAdapter(editor.getEditorInput(), IFile.class);
			if (editorFile != null) {
				IProject project = editorFile.getProject();
				if (project != null && !project.getName().equals(SPIFeTemplatePlanPage.TEMPLATES_PROJECT_NAME)) {
					IFile templateFile = project.getFile("template.plan");
					if (!templateFile.exists()) {
						try {
							createTemplateFile(templateFile);
						} catch (Exception e) {
							LogUtil.error("failed to create template file at: " + templateFile.getFullPath(), e);
						}
					}
					if (CommonUtils.equals(editorFile, templateFile)) {
						return null; // no template page when the template plan is in the editor
					}
					return new SPIFeTemplatePlanPage(editor, templateFile);
				}
			}
		}
		return null;
	}

	private void createTemplateFile(IFile templateFile) throws IOException {
		URI uri = EMFUtils.getURI(templateFile);
		Date date = new Date(System.currentTimeMillis());
		final EPlan templatePlan = TemporalUtils.createTemporalPlan(templateFile.getName(), uri, date, date);
		TransactionUtils.writing(templatePlan, new Runnable() {				
			@Override
			public void run() {
				templatePlan.setTemplate(true);	
			}
		});
		Resource eResource = templatePlan.eResource();
		eResource.save(null);
	}

}
