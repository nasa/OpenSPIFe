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
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.List;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;

public class BooleanAction extends PlanEditorContextMenuAction {
	
	public BooleanAction(String displayName, List<? extends EObject> elements, EStructuralFeature feature) {
		super(displayName, elements, feature);
	}
	
	@Override
	public int getStyle() {
		 return IAction.AS_CHECK_BOX;
	}
	
	@Override
	public boolean isChecked() {
		Object currentValue = getCommonValue();
		if (CommonUtils.equals(Boolean.TRUE, currentValue)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void runWithEvent(Event event) {
		MenuItem item = (MenuItem) event.widget;
		boolean selected = item.getSelection();
		String label = "Edit " + getDisplayName();
		CompositeOperation op = new CompositeOperation(label);
		for (EObject object : getObjects()) {
			if (object instanceof EPlanElement) {
				IItemPropertySource source = EMFUtils.adapt(object, IItemPropertySource.class);
				if (source != null) {
					IItemPropertyDescriptor pd = source.getPropertyDescriptor(object, getFeature());
					op.add(getOperation(object, pd, selected, !selected));
				}
			}
		}
		IUndoContext undoContext = getUndoContext();
		CommonUtils.execute(op, undoContext);
	}
	
	
}
