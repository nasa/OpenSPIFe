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
package gov.nasa.ensemble.core.plan.resources.ui.profile.editor;

import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.jscience.Profile;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

@SuppressWarnings("unchecked")
public class ProfileDataPointDetailsFactory extends BindingFactory {
	
	@Override
	public Binding createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		if (!(target instanceof Profile)) {
			throw new IllegalArgumentException("ProfileDataPointEditor target must be a Profile.");
		}
		FormToolkit toolkit = parameter.getDetailFormToolkit();
		Composite parent = toolkit.createComposite(parameter.getParent());
		parent.setLayout(new GridLayout(2, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		new ProfileDataPointsEditor(parent, parameter);
		return null;
	}

	
	
	

	
	



	

	
}
