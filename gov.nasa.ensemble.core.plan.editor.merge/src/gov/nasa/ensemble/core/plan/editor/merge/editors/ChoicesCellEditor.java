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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.editor.merge.editors;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.plan.ParameterSerializerRegistry;
import gov.nasa.ensemble.core.plan.ParameterStringifierUtils;
import gov.nasa.ensemble.core.plan.parameters.IParameterSerializer;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EChoice;

import java.util.List;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ChoicesCellEditor extends ComboBoxCellEditor {
	
	private Object[] values;
	private final EAttributeParameter pDef;
	private List<EChoice> choices;

	public ChoicesCellEditor(Composite parent, EAttributeParameter pDef, List<EChoice> choices) {
		super(parent, getItems(pDef, choices));
		this.pDef = pDef;
		setChoices(choices);
	}
	
	public void setChoices(List<EChoice> choices) {
		IParameterSerializer serializer = ParameterSerializerRegistry.getSerializer(pDef.getEAttributeType());
		
		this.choices = choices;
		int choiceCount = choices.size();
		String items[] = new String[choiceCount];
		values = new Object[choiceCount];
		for (int i=0; i<items.length; i++) {
			EChoice choice = choices.get(i);
			items[i] = choice.getValue();
			values[i] = serializer.getJavaObject(choice.getValue());
		}
		setItems(items);
	}

	@Override
	protected Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		if (control instanceof CCombo) {
			CCombo combo = (CCombo) control;
			combo.setEditable(false);
		}
		return control;
	}

	@Override
	protected void doSetValue(Object value) {
		int i = -1;
		for (Object item : values) {
			i++;
			if (CommonUtils.equals(value, item)) {
				break;
			}
		}
		super.doSetValue(Integer.valueOf(i));
	}

	@Override
	protected Object doGetValue() {
		Integer intValue = (Integer)super.doGetValue();
		EChoice choice = choices.get(intValue.intValue());
		IParameterSerializer serializer = ParameterSerializerRegistry.getSerializer(pDef.getEAttributeType());
		String hibernateString = choice.getValue();
    	return serializer.getJavaObject(hibernateString);
	}
	
	@SuppressWarnings("unchecked")
	private static final String[] getItems(EAttributeParameter pDef, List<EChoice> choices) {
		IParameterSerializer serializer = ParameterSerializerRegistry.getSerializer(pDef.getEAttributeType());
		IStringifier stringifier = ParameterStringifierUtils.getStringifier(pDef);
		String[] items = new String[choices.size()];
		int i = 0;
		for (EChoice choice : choices) {
	    	String hibernateString = choice.getValue();
	    	Object object = serializer.getJavaObject(hibernateString);
			items[i++] = stringifier.getDisplayString(object);
		}
		return items;
	}

}
