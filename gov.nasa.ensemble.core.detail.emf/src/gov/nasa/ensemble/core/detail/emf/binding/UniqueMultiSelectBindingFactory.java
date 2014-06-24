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

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.multi.MultiEObject;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertySource;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;


public class UniqueMultiSelectBindingFactory extends BindingFactory {

	@Override
	public Binding createBinding(final DetailProviderParameter parameter) {
		
		EObject ptarget = parameter.getTarget();
		final FormToolkit toolkit = parameter.getDetailFormToolkit();
		Composite parent = parameter.getParent();
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		
		//create label 
		String shortDescription = pd.getDisplayName(ptarget);
		EMFDetailUtils.createLabel(parent, toolkit, ptarget, pd);

		//create composite
		Composite composite = createEditorComposite(toolkit, parent, (shortDescription == null) ? 1 : 2);
		
		//create multiselect widget
		Map map = getValues(parameter);
		final UniqueMultiselectWidget widget = new UniqueMultiselectWidget(composite, parameter, map);

		//hook up listeners
		if (pd instanceof MultiItemPropertyDescriptor) {
			final MultiItemPropertyDescriptor descriptor = (MultiItemPropertyDescriptor) pd;
			List<EObject> commandOwners = descriptor.getCommandOwners();
			if (!commandOwners.isEmpty()) {
				for (EObject owner : commandOwners) {
					attachListener(owner, parameter, widget);
				}
			}
		} 
		else {
			attachListener(ptarget, parameter, widget);
		}
		
		toolkit.adapt(widget.getControl(), true, true);
		EMFDetailUtils.bindValidatorDecoration(parameter, widget.getControl());
		EMFDetailUtils.bindControlViability(parameter, new Control[] {widget.getControl()});
	
		return null;
	}
	
	public void attachListener(EObject target, final DetailProviderParameter parameter, final UniqueMultiselectWidget widget) {
		
		EStructuralFeature feature = (EStructuralFeature) parameter.getPropertyDescriptor().getFeature(target);
		final IObservableValue modelObservable = EMFObservables.observeValue(target, feature);
		final IValueChangeListener listener = new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				widget.updateWidget(getValues(parameter));
			}

		};
		modelObservable.addValueChangeListener(listener);
		widget.getControl().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				modelObservable.removeValueChangeListener(listener);
			}

		});
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
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		GridLayout layout = new GridLayout(numColumns, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		return composite;
	}
	
	
	//----------------------------------------------
	/** get values map for Multiselect widget */
	
	Map<Object, TriState> getValues(DetailProviderParameter parameter) {
		IItemPropertyDescriptor pd = parameter.getPropertyDescriptor();
		EObject target = parameter.getTarget();
		Map<Object, TriState> map = new LinkedHashMap<Object, TriState>();
		EStructuralFeature feature = (EStructuralFeature) pd.getFeature(target);
		if (target instanceof MultiEObject) {
			MultiEObject multiEObject = (MultiEObject)target;
			Collection<? extends EObject> eObjects = multiEObject.getEObjects();
			if (eObjects.isEmpty()) {
				return map;
			}
			MultiItemPropertySource source = multiEObject.getSource();
			MultiItemPropertyDescriptor multiPropertyDescriptor = (MultiItemPropertyDescriptor) source.getPropertyDescriptor(target, feature);
			Vector<Object> listOfValues = multiPropertyDescriptor.getValues();
			EObject firstObject = eObjects.toArray(new EObject[0])[0];
			for (Object referencedObject : getPossibleValues(firstObject, multiPropertyDescriptor, feature.getEType())) {
				for (Object list : listOfValues) {
					Collection<Object> values = (Collection<Object>) list;
					TriState triState = map.get(referencedObject);
					boolean contains = values != null && values.contains(referencedObject);
					if (triState == null) {
						map.put(referencedObject, contains ? TriState.TRUE : TriState.FALSE);
					} else if ((!contains && TriState.TRUE == triState)
							|| (contains && TriState.FALSE == triState)) {
						// once quasi, you never go back
						map.put(referencedObject, TriState.QUASI);
						break;
					}
				}
			}
			return map;
		} // else...
		Object value = EMFUtils.getPropertyValue(pd, target);
		return mapValues((Collection<Object>) value);
	}

	private Collection<?> getPossibleValues(EObject target, IItemPropertyDescriptor pd, EClassifier type) {
		if (type instanceof EEnum) {
			return ((EEnum)type).getELiterals();
		} else if (type instanceof EDataType) {
			return pd.getChoiceOfValues(target);
		} else {
			return EMFUtils.getReachableObjectsOfType(target, type);
		}
	}

	private Map<Object, TriState> mapValues(Collection<Object> values) {
		Map<Object, TriState> map = new LinkedHashMap<Object, TriState>();
		if (values == null) {
			return map;
		}
		for (Object value : values) {
			map.put(value, TriState.TRUE);
		}
		return map;
	}
	

}
