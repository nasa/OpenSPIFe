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
import gov.nasa.ensemble.core.detail.emf.EMFDetailFormPart;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.detail.emf.util.LabelProviderWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class MultiSelectBindingFactory extends BindingFactory {

	@Override
	public Binding createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		Composite parent = parameter.getParent();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Collection<?> choiceOfValues = pd.getChoiceOfValues(target);
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		EClassifier eType = feature.getEType();
		FormToolkit toolkit = parameter.getDetailFormToolkit();
		DataBindingContext dataBindingContext = parameter.getDataBindingContext();
		
		boolean valid = true;
		List<Object> choices = new ArrayList<Object>();
		if (choiceOfValues != null) {
			for (Object choice : choiceOfValues) {
				if (choice != null && eType.isInstance(choice)) {
					choices.add(choice);
				}
			}
		}

		if (valid) {
			Object value = pd.getPropertyValue(target);
			if (value instanceof PropertyValueWrapper) { // what is this all about?
				value = ((PropertyValueWrapper) value).getEditableValue(value);
			}
			if (value == null) {
				value = Collections.emptyList();
			}
			LabelProvider editLabelProvider = new LabelProviderWrapper(pd.getLabelProvider(target));
			final FeatureEditorDialog dialog = new FeatureEditorDialog(
					parent.getShell(), 
					editLabelProvider,
					target,
					eType,
					(List<?>) value, 
					pd.getDisplayName(target), 
					choices, 
					false, 
					pd.isSortChoices(target),
					feature.isUnique());

			final IObservableValue targetObservable = new AbstractObservableValue() {

				@Override
				protected void doSetValue(Object value) {
					fireValueChange(Diffs.createValueDiff(null, value));
				}

				@Override
				protected Object doGetValue() {
					return dialog.getResult();
				}

				@Override
				public Object getValueType() {
					return List.class;
				}

			};

	    	EMFDetailUtils.createLabel(parent, toolkit, target, pd);

			Button button = toolkit.createButton(parent, "", SWT.DOWN);
			button.setImage(editLabelProvider.getImage(target));
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					dialog.open();
					targetObservable.setValue(dialog.getResult());
				}

			});
			
			EMFDetailUtils.bindValidatorDecoration(parameter, button);
			EMFDetailUtils.bindControlViability(parameter, new Control[] {button});
			toolkit.adapt(button, true, true);
			return dataBindingContext.bindValue(
					targetObservable, 
					new UndoableObservableValue(target, pd), 
					new EMFReferenceUpdateStrategy(UpdateValueStrategy.POLICY_UPDATE, choices, editLabelProvider), 
					new EMFReferenceUpdateStrategy(UpdateValueStrategy.POLICY_UPDATE, choices, editLabelProvider));
		}

		Logger.getLogger(EMFDetailFormPart.class).error("no multi-select editor created for " + feature.getName());
		return null;
	}
	
}
