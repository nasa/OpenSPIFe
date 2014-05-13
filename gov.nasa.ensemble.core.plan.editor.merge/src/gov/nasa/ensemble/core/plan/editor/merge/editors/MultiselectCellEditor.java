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
package gov.nasa.ensemble.core.plan.editor.merge.editors;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.multiselect.InPlaceCheckBoxTreeDialog;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.emf.util.EcoreEListStringifier;
import gov.nasa.ensemble.emf.util.TransactionRecordingUndoableOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MultiselectCellEditor extends DialogCellEditor {

	Composite composite;
	Button button;
	EPlanElement element;
	EStructuralFeature feature;
	List<String> choiceOfValues;
	Collection<?> choices;

	public MultiselectCellEditor(Composite parent, EPlanElement element, EStructuralFeature feature) {
		super(parent);
		this.composite = parent;
		this.element = element;
		this.feature = feature;
		this.choiceOfValues = getChoiceOfValues(element, feature);
	}
	
	@Override
	protected Button createButton(Composite parent) {
		button = new Button(parent, SWT.ARROW | SWT.DOWN);
		return button;
	}
	
	@Override
	protected void updateContents(Object value) {
		List<String> list = getList(value);
		String contents = EcoreEListStringifier.formatString(list.toString());
		try {
			super.updateContents(contents);
		} catch (SWTException e) {
			//catch silently
		}
	}
	
	@Override
	public void activate() {
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<String> selected = getList(getValue());			
				final InPlaceCheckBoxTreeDialog dialog = new InPlaceCheckBoxTreeDialog(composite.getShell(), button, selected, new ArrayList(), choiceOfValues, "") {

					@Override
					public void dialogButtonPressOK() {
						List<Object> newValue = getChoiceObjects(getSelectedValues());
						setNewValue(newValue);
						updateContents(newValue);
					}

					@Override
					public void dialogButtonPressDeselect() {
						List<Object> newValue = new ArrayList();
						setNewValue(newValue);
						updateContents(newValue);
					}
					
					private void setNewValue(final List<Object> newValue) {
						EPlan plan = EPlanUtils.getPlan(element);
						TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
						TransactionRecordingUndoableOperation operation = new TransactionRecordingUndoableOperation("change "+feature.getName(), domain, new Runnable() {
							@Override
							public void run() {
								ADParameterUtils.setParameterObjectSafely(element, feature.getName(), newValue);
							}
						});
						IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
						CommonUtils.execute(operation, undoContext);
					}

					@Override
					protected void dispose() {
						// don't do anything
					}		
				};
				dialog.open();
			}

		});
		super.activate();
	}
	
	private String getChoiceText(Object feature, final Object object) {
		String text = null;
		if (feature instanceof EReference) {
			IItemLabelProvider labeler = EMFUtils.adapt(object, IItemLabelProvider.class);
			text = labeler.getText(object);
		} else if(object instanceof EEnumLiteral) {
			text = ((EEnumLiteral) object).getLiteral();
		} else {
			IStringifier stringifier = EMFUtils.getStringifier((EAttribute)feature);
			text = stringifier.getDisplayString(object);
		}
		return text;
	}
	
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		return null;
	}
	
	protected List<Object> getChoiceObjects(List<String> values) {
		List<Object> results = new ArrayList();
		for(Object value : choices) {
			if(values.contains(getChoiceText(feature, value))) {
				results.add(value);
			}
		}
		return results;
	}
		
	protected List<String> getChoiceOfValues(EPlanElement element, EStructuralFeature feature) {
		
		if(choiceOfValues == null) {
			EClassifier eType = feature.getEType();
			if(eType instanceof EEnumImpl) {
				choices = ((EEnumImpl) eType).getELiterals();				
			} else {
				IItemPropertySource itemPropertySource = EMFUtils.adapt(element, IItemPropertySource.class);
				IItemPropertyDescriptor pd = itemPropertySource.getPropertyDescriptor(element, feature);
				choices = pd.getChoiceOfValues(element);
			}
			choiceOfValues = getList(choices);
		}
		
		return choiceOfValues;
	}
	
	private List<String> getList(Object values) {
		List<String> list = new ArrayList();
		if (values instanceof EcoreEList) {
			for (Object o : ((EcoreEList) values).toArray()) {
				list.add(getChoiceText(feature, o));
			}
		} else if (values instanceof List<?>) {
			for (Object literal : ((List) values).toArray()) {
				if (literal instanceof EEnumLiteral) {
					list.add(((EEnumLiteral) literal).getInstance().toString());
				} else if (literal instanceof EObject) {
					list.add(getChoiceText(feature, literal));
				}
			}
		} else if (values instanceof EEnumImpl) {
			for (EEnumLiteral literal : ((EEnumImpl) values).getELiterals()) {
				list.add(literal.getInstance().toString());
			}
		}
		return list;
	}
}
