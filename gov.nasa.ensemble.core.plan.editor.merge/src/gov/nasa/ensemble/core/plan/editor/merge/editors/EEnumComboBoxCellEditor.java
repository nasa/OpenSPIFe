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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

public final class EEnumComboBoxCellEditor extends ExtendedComboBoxCellEditor {

	public EEnumComboBoxCellEditor(Composite parent, EEnumImpl impl) {
		super(parent, getList(impl), new LabelProvider());
	}

	private static List getList(EEnumImpl impl) {
		List list = new ArrayList();
		EList<EEnumLiteral> literals = impl.getELiterals();
		for (EEnumLiteral literal : literals) {
			list.add(literal.getInstance());
		}
		return list;
	}

	@Override
	public void doSetFocus() {
		final CCombo combo = (CCombo)getControl();
		if (!combo.isDisposed()) {
			String text = combo.getText();
			if (text.length() == 0 && CommonUtils.isWSCocoa()) {
				combo.getDisplay().timerExec(1000, new Runnable() {
					@Override
					public void run() {
						focusIt(combo);
					}
				});
			} else {
				focusIt(combo);
			}
		}	
	}
	
	private void focusIt(final CCombo combo) {
		if (!combo.isDisposed()) {
			combo.setFocus();
			combo.setListVisible(true);
		}
	}
	
}
