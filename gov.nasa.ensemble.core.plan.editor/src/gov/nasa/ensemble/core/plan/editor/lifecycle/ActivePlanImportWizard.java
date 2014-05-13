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

import gov.nasa.ensemble.common.ui.wizard.EnsembleWizardErrorPage;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IImportWizard;

public abstract class ActivePlanImportWizard extends ActivePlanWizard implements IImportWizard {
	private static final WizardPage ERROR_PAGE = new EnsembleWizardErrorPage("Plan Import Error", "No active plan selected in workspace");
	
	public ActivePlanImportWizard() {
		errorPage = ActivePlanImportWizard.ERROR_PAGE;
	}
	
	@Override
	protected String getWizardExtensionPointID() {
		return EXT_POINT_ID_IMPORT_WIZARDS;
	}
	
	@Override
	public final String getWindowTitlePrefix() {
		return "Import ";
	}
}
