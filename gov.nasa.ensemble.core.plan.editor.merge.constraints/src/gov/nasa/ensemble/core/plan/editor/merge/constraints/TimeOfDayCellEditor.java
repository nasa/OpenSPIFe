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
package gov.nasa.ensemble.core.plan.editor.merge.constraints;

import gov.nasa.ensemble.common.ui.type.editor.CocoaCompatibleTextCellEditor;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class TimeOfDayCellEditor extends CocoaCompatibleTextCellEditor {

	private static final TimeOfDayStringifier STRINGIFIER = new TimeOfDayStringifier();
	
	public TimeOfDayCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		Text text = (Text) super.createControl(parent);
		new StringTypeEditor(STRINGIFIER, text);
		return text;
	}
	
}
