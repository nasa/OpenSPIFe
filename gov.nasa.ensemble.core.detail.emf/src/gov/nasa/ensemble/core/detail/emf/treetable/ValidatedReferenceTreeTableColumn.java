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
package gov.nasa.ensemble.core.detail.emf.treetable;

import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class ValidatedReferenceTreeTableColumn<T extends EObject> extends EMFTreeTableColumn<T> {

	private DetailProviderParameter parameter;
	private boolean editable;

	public ValidatedReferenceTreeTableColumn(
			DetailProviderParameter parameter,
			String headerName, int defaultWidth, boolean editable) {
		super(parameter.getPropertyDescriptor(), headerName, defaultWidth);
		this.parameter = parameter;
		this.editable = editable;
	}

	@Override
	public boolean canModify(T facet) {
		return editable && super.canModify(facet);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent, T object) {
		CellEditor result = super.createPropertyEditor(parent, object);
		parameter.setTarget(object);
		EMFDetailUtils.bindValidatorDecoration(parameter, result.getControl());
		return result;
	}
}
