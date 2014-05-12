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
package gov.nasa.ensemble.core.plan.editor.context;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Event;

public class ValueAction extends PlanEditorContextMenuAction {
	
	private Object value;
	private Boolean isChecked = null;
	private String text = null;
	
	public ValueAction(String displayName, List<EPlanElement> elements, EStructuralFeature feature, Object value) {
		super(displayName, elements, feature);
		this.value = value;
	}
	
	@Override
	public int getStyle() {
		if (getFeature().isMany()) {
			return IAction.AS_CHECK_BOX;
		} else {
			return IAction.AS_RADIO_BUTTON;
		}
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ICON_PROVIDER.get(getFeature(), getText());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getText() {
		if (text == null) {
			EStructuralFeature feature = getFeature();
			if(feature instanceof EReference) {
				IItemLabelProvider labeler = EMFUtils.adapt(value, IItemLabelProvider.class);
				if (labeler != null) {
					text = labeler.getText(value);
				}
			} else {
				IStringifier stringifier = EMFUtils.getStringifier((EAttribute)feature);
				if (stringifier != null) {
					text = stringifier.getDisplayString(value);
				}
			}
			if (text == null) {
				text = value.toString();
			}
		}
		return text;
	}
	
	@Override
	public boolean isChecked() {
		if (isChecked == null) {
			Object currentValue = getCommonValue();
			if (getFeature().isMany() && currentValue instanceof Collection) {
				isChecked = ((Collection<?>) currentValue).contains(value);
			} else if (currentValue != null) {
				isChecked = CommonUtils.equals(currentValue, value);
			} else {
				isChecked = false;
			}
		}
		return isChecked;
	}
	
	@Override
	public void runWithEvent(Event event) {
		String label = "Edit " + getDisplayName();
		CompositeOperation op = new CompositeOperation(label);
		for (EObject object : getObjects()) {
			if (object instanceof EPlanElement) {
				IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
				if (source != null) {
					IItemPropertyDescriptor pd = source.getPropertyDescriptor(object, getFeature());
					Object propertyValue = EMFUtils.getPropertyValue(pd, object);
					if (pd.isMany(object)) {
						@SuppressWarnings("unchecked")
						EList<EObject> oldValue = new BasicEList<EObject>((Collection<EObject>) propertyValue);
						EList<EObject> newValue = new BasicEList<EObject>(oldValue);
						if (isChecked()) {
							newValue.remove(value);
						} else if (!newValue.contains(value)) {
							newValue.add((EObject) value);
						}
						op.add(getOperation(object, pd, oldValue, newValue));
					} else {
						op.add(getOperation(object, pd, propertyValue, value));
					}
				}
			}
		}
		IUndoContext undoContext = getUndoContext();
		CommonUtils.execute(op, undoContext);
	}
	
}

