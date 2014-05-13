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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.ui.operations.PasteSpecialSettings;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;

public abstract class TransferableExtensionWizardPage extends WizardPage {
	
	public static final PasteSpecialSettings SETTINGS = PasteSpecialSettings.getInstance();

	private IStructureLocation location = null;
	
	public TransferableExtensionWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		SETTINGS.put(PasteSpecialSettings.ACTIVE, true);
	}

	public TransferableExtensionWizardPage(String pageName) {
		super(pageName);
		SETTINGS.put(PasteSpecialSettings.ACTIVE, true);
	}

	protected final IStructureLocation getStructureLocation() {
		return location;
	}

	public void setStructureLocation(IStructureLocation location) {
		this.location = location;
	}

	@Override
	public void dispose() {
		super.dispose();
		SETTINGS.put(PasteSpecialSettings.ACTIVE, false);
	}

}
