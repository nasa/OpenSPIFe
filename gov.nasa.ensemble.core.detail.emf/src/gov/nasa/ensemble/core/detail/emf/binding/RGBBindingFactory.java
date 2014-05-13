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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RGBBindingFactory extends BindingFactory {

	@Override
	public Binding createBinding(DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (feature == null) {
			return null;
		}
		boolean isEditable = pd.canSetProperty(target);
		EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		Composite composite = createPaddingComposite(toolkit, parent);
		ColorSelector colorSelector = new ColorSelector(composite);
		Button button = colorSelector.getButton();
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, true));
		button.setEnabled(isEditable);
		toolkit.adapt(button, true, true);
		EMFDetailUtils.bindControlViability(p, new Control[] {button});
		return EMFDetailUtils.bindEMFUndoable(p, new RGBSelectorObservableValue(colorSelector, ERGB.class == feature.getEType().getInstanceClass()));
	}

	private Composite createPaddingComposite(FormToolkit toolkit, Composite parent) {
		Composite composite = toolkit.createComposite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		return composite;
	}

}
