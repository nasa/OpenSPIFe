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
package gov.nasa.ensemble.core.plan.editor.merge.columns;


import java.util.Collection;

import org.eclipse.jface.resource.ImageDescriptor;

public class StringPicklistWizardPage extends AbstractPicklistWizardPage {

	private Collection<String> choices;

	public StringPicklistWizardPage(Collection<String> choices, String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.choices = choices;
	}

	public StringPicklistWizardPage(Collection<String> choices, String pageName, String title) {
		this(choices, pageName, title, null);
	}

	public StringPicklistWizardPage(Collection<String> choices, String pageName) {
		this(choices, pageName, pageName, null);
	}

	@Override
	public Collection<String> computeAvailableChoices() {
		return choices;
	}

}
