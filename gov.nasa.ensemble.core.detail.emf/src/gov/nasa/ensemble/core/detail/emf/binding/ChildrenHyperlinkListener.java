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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;

public class ChildrenHyperlinkListener extends HyperlinkAdapter {
	private final ISelectionProvider selectionProvider;
	private final Object node;

	public ChildrenHyperlinkListener(ISelectionProvider selectionProvider, Object eObject) {
		this.selectionProvider = selectionProvider;
		node = eObject;
	}

	@Override
	public void linkActivated(HyperlinkEvent e) {
		if (node != null) {
			boolean modifySelection = isModifySelectionKeyDown(e);
			selectTemporalNode(node, modifySelection);
		}
	}

	private boolean isModifySelectionKeyDown(HyperlinkEvent e) {
		String os = Platform.getOS();
		int addToSelectionBit = SWT.CONTROL;
		if (Platform.OS_WIN32.equals(os)) {
			addToSelectionBit = SWT.CONTROL;
		} else if (Platform.OS_MACOSX.equals(os)) {
			addToSelectionBit = SWT.COMMAND;
		} else if (Platform.OS_LINUX.equals(os)) {
			addToSelectionBit = SWT.CONTROL;
		}
		int stateMask = e.getStateMask();
		return ((stateMask & addToSelectionBit) != 0);
	}

	private void selectTemporalNode(Object node, boolean modifySelection) {
		List<Object> elements;
		if (modifySelection) {
			elements = getModifiedSelection(node);
		} else {
			elements = Collections.singletonList(node);
		}
		StructuredSelection selection = new StructuredSelection(elements);
		selectionProvider.setSelection(selection);
	}

	private List<Object> getModifiedSelection(Object node) {
		boolean elementFound = false;
		List<Object> elements = new ArrayList<Object>();
		ISelection oldSelection = selectionProvider.getSelection();
		if (oldSelection instanceof StructuredSelection) {
			StructuredSelection selection = (StructuredSelection) oldSelection;
			for (Object object : selection.toList()) {
//				if (object instanceof Object) {
//					Object element = object;
					if (object == node) {
						elementFound = true;
					} else {
						elements.add(object);
					}
//				}
			}
		}
		if (!elementFound) {
			elements.add(node);
		}
		return elements;
	}
}
