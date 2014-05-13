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
package gov.nasa.arc.spife.ui.timeline.chart.model.provider.detail;

import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.type.editor.TextEditor;
import gov.nasa.ensemble.core.detail.emf.ChildrenBindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.jscience.AmountStringifier;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jscience.physics.amount.Amount;

public class ChartDetailProvider extends DetailProvider {
	
	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Object feature = pd.getFeature(target);
		return ChartPackage.Literals.CHART__PLOTS == feature
				|| ChartPackage.Literals.LINE_CHART__MAXIMUM_LINE == feature
				|| ChartPackage.Literals.LINE_CHART__MINIMUM_LINE == feature;
	}
	
	@Override
	public void createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		Object feature = pd.getFeature(target);
		if (ChartPackage.Literals.CHART__PLOTS == feature) {
			ChildrenBindingFactory.createBinding(parameter);
		} else if (ChartPackage.Literals.LINE_CHART__MAXIMUM_LINE == feature
				|| ChartPackage.Literals.LINE_CHART__MINIMUM_LINE == feature) {
			createAmountLineBinding(parameter);
		}
	}

	private void createAmountLineBinding(DetailProviderParameter parameter) {
		final EObject eObject = parameter.getTarget();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		final EStructuralFeature feature = (EStructuralFeature) pd.getFeature(eObject);
		AmountLine value = (AmountLine) eObject.eGet(feature);
		Composite parent = parameter.getParent();
		FormToolkit toolkit = parameter.getDetailFormToolkit();
		toolkit.createLabel(parent, pd.getDisplayName(eObject)+": ");
		Text text = toolkit.createText(parent, null, SWT.BORDER);
		final TextEditor editor = new TextEditor(text, new AmountStringifier());
		editor.setObject(value == null ? null : value.getAmount());
		final PropertyChangeListener uiToModel = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				Amount amount = (Amount) event.getNewValue();
				AmountLine line = ChartFactory.eINSTANCE.createAmountLine();
				line.setAmount(amount);
				line.setRgb(ColorConstants.red.getRGB());
				EditingDomain domain = EMFUtils.getAnyDomain(eObject);
				Command command = SetCommand.create(domain, eObject, feature, line);
				EMFUtils.executeCommand(domain, command);
			}
		};
		editor.addPropertyChangeListener(uiToModel);
		final AdapterImpl modelToUI = new AdapterImpl() {

			@Override
			public void notifyChanged(Notification msg) {
				AmountLine line = (AmountLine) eObject.eGet(feature);
				Amount<?> value = line == null ? null : line.getAmount();
				if (!CommonUtils.equals(value, editor.getObject())) {
					editor.setObject(value);
				}
			}
			
		};
		eObject.eAdapters().add(modelToUI);
		text.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				editor.removePropertyChangeListener(uiToModel);
				eObject.eAdapters().remove(modelToUI);
			}
		});
	}
	
}
