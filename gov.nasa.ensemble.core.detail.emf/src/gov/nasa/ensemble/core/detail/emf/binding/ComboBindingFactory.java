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

import static java.lang.Boolean.*;
import static java.lang.System.*;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.core.detail.emf.BindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.detail.emf.util.LabelProviderWrapper;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ComboBindingFactory extends BindingFactory {

	private static final boolean updateOnSelection = parseBoolean(getProperty("detail.windows.comboUpdateOnSelection", "true"));
	private final boolean useTypableCombo;
	private FiniteOrderedSetUpdateValueStrategy targetToModelUpdateValueStrategy;
	private FiniteOrderedSetUpdateValueStrategy modelToTargetUpdateValueStrategy;

	public ComboBindingFactory(final boolean useTypableCombo) {
		this.useTypableCombo = useTypableCombo;
	}

	@Override
	public Binding createBinding(DetailProviderParameter p) {
		return createBinding(p, null);
	}
	
	public Binding createBinding(DetailProviderParameter p, List<EStructuralFeature> dependsOn) {
		DataBindingContext dbc = p.getDataBindingContext();
		FormToolkit toolkit = p.getDetailFormToolkit();
		Composite parent = p.getParent();
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		List<Object> choices = getComboChoices(p);
		LabelProvider labeler = getEditLabelProvider(pd, target);
		String items[];
		if (pd.isSortChoices(target)) {
			items = sort(choices, labeler);
		} else {
			items = label(choices, labeler);
		}
		boolean isEditable = pd.canSetProperty(target);
		Label label = EMFDetailUtils.createLabel(parent, toolkit, target, pd);
		final Combo combo = buildCombo(parent);
		combo.setItems(items);
		if (dependsOn != null) {
			setupComboUpdater(combo, label, p, labeler, dependsOn);
		}
		EMFDetailUtils.addDescriptionTooltip(pd, target, combo);
		combo.setLayoutData(Activator.createStandardGridData());
		combo.setEnabled(isEditable);
		toolkit.adapt(combo, true, true);
		EMFDetailUtils.bindValidatorDecoration(p, combo);
		EMFDetailUtils.bindControlViability(p, new Control[] { combo });
		targetToModelUpdateValueStrategy = new FiniteOrderedSetUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE, choices);
		modelToTargetUpdateValueStrategy = new FiniteOrderedSetUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE, choices);
		IObservableValue targetObservableValue = null;
		if (combo instanceof TypableCombo && !updateOnSelection) {
			targetObservableValue = new ComboNonSelectionValueProperty().observe(combo);
		} else {
			targetObservableValue = SWTObservables.observeSingleSelectionIndex(combo);
		}
		return dbc.bindValue(targetObservableValue, new UndoableObservableValue(target, pd), targetToModelUpdateValueStrategy, modelToTargetUpdateValueStrategy);
	}

	public Combo buildCombo(Composite parent) {
		final Combo combo;
		if (useTypableCombo && CommonUtils.isOSWindows()) {
			combo = new TypableCombo(parent, SWT.BORDER | SWT.READ_ONLY
					| SWT.DROP_DOWN);
		} else {
			combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
			combo.addListener(SWT.MouseWheel, new Listener() {
				@Override
				public void handleEvent(Event event) {
					event.doit = false;
				}
			});
		}

		return combo;
	}

	protected List<Object> getComboChoices(DetailProviderParameter p) {
		EObject target = p.getTarget();
		IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		boolean editable = pd.canSetProperty(target);
		if (!editable) {
			Object propertyValue = EMFUtils.getPropertyValue(pd, target);
			if (propertyValue == null) {
				return Collections.emptyList();
			}
			ArrayList list = new ArrayList();
			list.add(propertyValue);
			return list;
		}
		return new ArrayList<Object>(pd.getChoiceOfValues(target));
	}

	/**
	 * Sort the choices by the labels from the label provider, and return the
	 * labels in that order.
	 * 
	 * @param choices
	 * @param labeler
	 * @return
	 */
	protected final String[] sort(List<Object> choices, LabelProvider labeler) {
		final Map<Object, String> objectLabels = new LinkedHashMap<Object, String>();
		for (Object choice : choices) {
			String label = labeler.getText(choice);
			objectLabels.put(choice, label);
		}
		Collections.sort(choices, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				String l1 = objectLabels.get(o1);
				String l2 = objectLabels.get(o2);
				return String.CASE_INSENSITIVE_ORDER.compare(l1, l2);
			}
		});
		String[] items = new String[choices.size()];
		int i = 0;
		for (Object choice : choices) {
			items[i++] = objectLabels.get(choice);
		}
		return items;
	}

	/**
	 * Return a list of the labels for each choice, using the label provider
	 * 
	 * @param choices
	 * @param labeler
	 * @return
	 */
	protected final String[] label(List<Object> choices, LabelProvider labeler) {
		String[] items = new String[choices.size()];
		int i = 0;
		for (Object choice : choices) {
			items[i++] = labeler.getText(choice);
		}
		return items;
	}

	/**
	 * Wraps the IItemLabelProvider in a standardized JFace LabelProvider in
	 * order for certain editors to be properly configured
	 * 
	 * @param pd
	 *            descriptor of the target
	 * @param target
	 *            to edit
	 * @return the JFace label provider
	 */
	protected final LabelProvider getEditLabelProvider(
			IItemPropertyDescriptor pd, Object target) {
		return new LabelProviderWrapper(pd.getLabelProvider(target));
	}
	
	/**
	 * Setup an EMF adapter on the target object that updates the combo control's choices and label when there is a change in the
	 * value(s) of one of the features that the combo choice list depends on
	 * 
	 * @param combo
	 * @param p
	 * @param labeler
	 * @param dependsOn
	 */
	protected void setupComboUpdater(final Combo combo, final Label label, final DetailProviderParameter p, final LabelProvider labeler, final List<EStructuralFeature> dependsOn) {
		final EObject object = p.getTarget();
		final IItemPropertyDescriptor pd = p.getPropertyDescriptor();
		final Adapter comboUpdater = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (dependsOn.contains(msg.getFeature())) {
					WidgetUtils.runInDisplayThread(combo, new Runnable() {
						@Override
						public void run() {
							Object selection = null;
							int index = combo.getSelectionIndex();
							if (index >= 0) {
								selection = combo.getItem(index);
							}
							List<Object> choices = getComboChoices(p);
							// Update the values used in the two FiniteOrderedSetUpdateValueStrategy instances
							targetToModelUpdateValueStrategy.setValues(choices);
							modelToTargetUpdateValueStrategy.setValues(choices);
							String items[];
							if (pd.isSortChoices(object)) {
								items = sort(choices, labeler);
							} else {
								items = label(choices, labeler);
							}
							combo.setItems(items);
							// reselect old selection if it is still amongst the items
							if (selection != null) {
								for (int i=0; i<items.length; i++) {
									if (items[i].equals(selection)) {
										combo.select(i);
										break;
									}
								}
							}
							// the label for the combo may need updating as well
							String displayName = pd.getDisplayName(object);
							if (!label.getText().equals(displayName)) {
								label.setText(displayName);
								label.getParent().layout();
							}
						}
					});
				}
			}
		};
		object.eAdapters().add(comboUpdater);
		combo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				object.eAdapters().remove(comboUpdater);
				combo.removeDisposeListener(this);
			}
		});
	}

}
