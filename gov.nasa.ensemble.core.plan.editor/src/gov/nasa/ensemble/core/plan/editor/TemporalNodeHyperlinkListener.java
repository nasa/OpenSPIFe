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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;

public class TemporalNodeHyperlinkListener implements IHyperlinkListener {
	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(TemporalNodeHyperlinkListener.class);

	private final ISelectionProvider selectionProvider;
	private final EPlan plan;
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry;

	public TemporalNodeHyperlinkListener(ISelectionProvider selectionProvider, EPlan plan, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		this.selectionProvider = selectionProvider;
		this.plan = plan;
		this.identifiableRegistry = identifiableRegistry;
	}

	@Override
	public void linkEntered(HyperlinkEvent e) {
		EPlanElement node = getTargetNode(e);
		if (node != null) {
			highlightTemporalNode(node);
		}
	}

	@Override
	public void linkExited(HyperlinkEvent e) {
		EPlanElement node = getTargetNode(e);
		if (node != null) {
			unhighlightTemporalNode(node);
		}
	}

	@Override
	public void linkActivated(HyperlinkEvent e) {
		EPlanElement node = getTargetNode(e);
		if (node != null) {
			boolean modifySelection = isModifySelectionKeyDown(e);
			selectTemporalNode(node, modifySelection);
		}
	}

	private void highlightTemporalNode(EPlanElement node) {
		// TODO: highlight
	}

	private void unhighlightTemporalNode(EPlanElement node) {
		// TODO: unhighlight
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

	private void selectTemporalNode(EPlanElement node, boolean modifySelection) {
		List<EPlanElement> elements;
		if (modifySelection) {
			elements = getModifiedSelection(node);
		} else {
			elements = Collections.singletonList(node);
		}
		StructuredSelection selection = new StructuredSelection(elements);
		selectionProvider.setSelection(selection);
	}

	private List<EPlanElement> getModifiedSelection(EPlanElement node) {
		boolean elementFound = false;
		ISelection oldSelection = selectionProvider.getSelection();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		for (EPlanElement element : PlanEditorUtil.emfFromSelection(oldSelection)) {
			if (element == node) {
				elementFound = true;
			} else {
				elements.add(element);
			}
		}
		if (!elementFound) {
			elements.add(node);
		}
		return elements;
	}

	/**
	 * Utility function to look up the target for a given hyperlink event.
	 * Relies on the unique id for the target being put into the href of
	 * the hyperlink.  As a backup, checks the link label as a print name.
	 * @param e
	 * @return the temporal node targeted by the hyperlink, if any
	 */
	private EPlanElement getTargetNode(HyperlinkEvent e) {
		EPlanElement node = null;
		String href = (String)e.getHref();
		if (href != null) {
			String uniqueId = PlanPrinter.getUniqueIdFromHref(href);
			node = identifiableRegistry.getIdentifiable(EPlanElement.class, uniqueId);
		}
		if (node == null) {
			String printName = e.getLabel();
			node = getNodeByPrintName(plan, printName);
		}
		return node;
	}

	/**
	 * Utility function to look up a node by checking its print name.
	 * Should only be used as a backup, since multiple nodes could have
	 * the same print name.
	 * @param parent
	 * @param printName
	 * @return one of the temporal nodes with the given print name, if any exist.
	 */
	private EPlanElement getNodeByPrintName(EPlanElement parent, String printName) {
		String parentName = PlanPrinter.getPrintName(parent);
		if (CommonUtils.equals(parentName, printName)) {
			return parent;
		}
		for (EPlanElement child : EPlanUtils.getChildren(parent)) {
			EPlanElement node = getNodeByPrintName(child, printName);
			if (node != null) {
				return node;
			}
		}
		return null;
	}

}
