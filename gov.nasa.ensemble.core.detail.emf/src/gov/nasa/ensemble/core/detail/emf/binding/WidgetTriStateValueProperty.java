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

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

public class WidgetTriStateValueProperty extends WidgetValueProperty {

	WidgetTriStateValueProperty() {
		super(SWT.Selection); //  | SWT.Modify ?
	}

	@Override
	public Object getValueType() {
		return TriState.class;
	}

	@Override
	protected Object doGetValue(Object source) {
		Button triButton = (Button) source;
		if ((triButton.getStyle() & SWT.CHECK) == 0) {
			return false;
		}
		if (triButton.getSelection() && !triButton.getGrayed()) {
	    	return true;
	    }
		if (triButton.getSelection() && triButton.getGrayed()) {
			return null;
		}
	    return false;
	}

	@Override
	protected void doSetValue(Object source, Object value) {			
		Button triButton = (Button) source;
		if (value == null) {
			triButton.setGrayed(true);
			triButton.setSelection(true);
		}
		else if ((Boolean) value) {
			triButton.setGrayed(false);
			triButton.setSelection(true);
		}
		else {
			triButton.setGrayed(false);
			triButton.setSelection(false);
		}
	}
	
	// --------------------------
	
	@Override
	public String toString() {
		return "Button.selection <Fake TriState>"; //$NON-NLS-1$
	}
	
}
