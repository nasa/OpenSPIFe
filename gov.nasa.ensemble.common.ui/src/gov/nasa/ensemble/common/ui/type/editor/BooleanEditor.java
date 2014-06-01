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
package gov.nasa.ensemble.common.ui.type.editor;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BooleanEditor extends AbstractTypeEditor<Boolean> {

	private final Composite parent;
	private final Button button;

	public BooleanEditor(Composite parent) {
		super(Boolean.class);
		this.parent = parent;
		button = createButton();
	}
	
	@Override
	public Control getEditorControl() {
		return button;
	}
	
	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		WidgetUtils.runInDisplayThread(button, new Runnable() {
			@Override
			public void run() {
				if (object != null) {
					button.setSelection((Boolean)object);
				}
			}
		});			
	}
	
	private Button createButton() {
		final Button button = new Button(parent, SWT.CHECK | SWT.FLAT);
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateEditor();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateEditor();
			}
			private void updateEditor() {
				BooleanEditor.super.setObject(Boolean.valueOf(button.getSelection()));
			}
		});
		return button;
	}
	
}
