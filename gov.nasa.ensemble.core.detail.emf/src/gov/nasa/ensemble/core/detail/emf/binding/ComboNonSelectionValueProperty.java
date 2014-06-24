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

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

public class ComboNonSelectionValueProperty extends WidgetValueProperty {

	ComboNonSelectionValueProperty() {
		super(new int[] { SWT.FocusOut, SWT.DefaultSelection });
	}
	
	@Override
	public Object getValueType() {
		return Integer.TYPE;
	}

	@Override
	protected Object doGetValue(Object source) {
		return new Integer(((Combo) source).getSelectionIndex());
	}

	@Override
	protected void doSetValue(Object source, Object value) {
		int intValue = value == null ? new Integer(-1) : ((Integer) value).intValue();
		if (intValue == -1)
			((Combo) source).deselectAll();
		else
			((Combo) source).select(intValue);
	}

	@Override
	public String toString() {
		return "Combo.selectionIndex <int>"; //$NON-NLS-1$
	}

}
