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

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.core.detail.emf.multi.MultiItemPropertyDescriptor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class PropertyDescriptorUpdateOperation extends AbstractEnsembleUndoableOperation {
	
	private IItemPropertyDescriptor pd;
	private EObject model;
	private final Object oldValue;
	private final Object newValue;

	public PropertyDescriptorUpdateOperation(String label, EObject model, IItemPropertyDescriptor pd, Object newValue) {
		super(label);
		this.model = model;
		this.pd = pd;
		if (pd instanceof MultiItemPropertyDescriptor) {
			this.oldValue = ((MultiItemPropertyDescriptor)pd).getValues();
		} else {
			this.oldValue = EMFUtils.getPropertyValue(pd, model);
		}
		this.newValue = newValue;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}

	@Override
	protected void execute() throws Throwable {
		pd.setPropertyValue(model, newValue);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void undo() throws Throwable {
		if (pd instanceof MultiItemPropertyDescriptor) {
			((MultiItemPropertyDescriptor)pd).setValues((Collection<Object>)oldValue);
		} else {
			pd.setPropertyValue(model, oldValue);
		}
	}

	@Override
	public String toString() {
		return "oldValue='" + oldValue + "', newValue='" + newValue + "'";
	}

}
