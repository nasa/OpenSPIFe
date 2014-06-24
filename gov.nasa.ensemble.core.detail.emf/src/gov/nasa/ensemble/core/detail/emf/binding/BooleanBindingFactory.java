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

import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.databinding.Binding;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class BooleanBindingFactory extends BindingFactory {
	
	@Override
	public Binding createBinding(DetailProviderParameter p) {
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
		final EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		boolean isEditable = pd.canSetProperty(target);
		final EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		
		String shortDescription = EMFUtils.getAnnotation(feature, EMFDetailUtils.ANNOTATION_SOURCE_DETAIL, EMFDetailUtils.ANNOTATION_DETAIL_SHORT_DESCRIPTION);
		EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		Composite composite = createEditorComposite(toolkit, parent, (shortDescription == null) ? 1 : 2);
		final Button button = toolkit.createButton(composite, "", SWT.CHECK | SWT.FLAT);
		button.setEnabled(isEditable);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		if (shortDescription != null) {
			Label label = new Label(composite, SWT.NONE);
			label.setText(shortDescription);
		}

		toolkit.adapt(button, true, true);
		EMFDetailUtils.bindValidatorDecoration(p, button);
		EMFDetailUtils.bindControlViability(p, new Control[] {button});

		WidgetTriStateValueProperty valueProperty = new WidgetTriStateValueProperty();
		ISWTObservableValue observable = valueProperty.observe(button);	
		return EMFDetailUtils.bindEMFUndoable(p, observable,
				new EMFUpdateValueStrategy(), new EMFUpdateValueStrategy());
	}
	
	/**
	 * Creates an editor composite. This configures the composite to have either a grid width of 2 if the shortDescription is not
	 * null, or a width of 1 if shortDescription is null
	 * 
	 * @param parent
	 *            component to create composite for
	 * @param shortDescription
	 *            allows for configuration of the composite
	 * @return the created composite
	 */
	private Composite createEditorComposite(FormToolkit toolkit, Composite parent, int numColumns) {
		Composite composite = toolkit.createComposite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		GridLayout layout = new GridLayout(numColumns, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		return composite;
	}
	
}
