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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class CocoaCompatibleTextCellEditor extends TextCellEditor {

	public CocoaCompatibleTextCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected void doSetFocus() {
		if ((text != null) && !text.isDisposed()) {
			final String string = text.getText();
			boolean workaround = (string.length() == 0) && CommonUtils.isWSCocoa();
			if (workaround) {
				// http://bugs.eclipse.org/bugs/show_bug.cgi?id=328864
				text.selectAll();
				ReflectionUtils.invoke(this, "checkSelection");
				ReflectionUtils.invoke(this, "checkDeleteable");
				ReflectionUtils.invoke(this, "checkSelectable");
				Display display = text.getDisplay();
				display.timerExec(700, new Runnable() {
					@Override
					public void run() {
						timerExec(string);
					}
				});
			} else {
				super.doSetFocus();
			}
        }
	}

	protected void timerExec(String string) {
		if ((text != null) && !text.isDisposed()) {
			text.setFocus();
		}
	}

}
