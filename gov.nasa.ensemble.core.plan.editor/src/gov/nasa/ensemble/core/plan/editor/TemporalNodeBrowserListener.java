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

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

public class TemporalNodeBrowserListener implements LocationListener {

	private final ISelectionProvider selectionProvider;
	private final EPlan plan;
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry;
	
	public TemporalNodeBrowserListener(ISelectionProvider selectionProvider, EPlan plan, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		this.selectionProvider = selectionProvider;
		this.plan = plan;
		this.identifiableRegistry = identifiableRegistry;
	}

	@Override
	public void changed(LocationEvent event) {
		// don't care
	}
	
	@Override
	public void changing(LocationEvent event) {
		EPlanElement node = getTargetNode(event);
		if (node != null) {
			selectTemporalNode(node);
		}
		event.doit = false;
	}
	
	private void selectTemporalNode(EPlanElement node) {
		List<EPlanElement> elements = Collections.singletonList(node);
		StructuredSelection selection = new StructuredSelection(elements);
		selectionProvider.setSelection(selection);
	}

	/**
	 * Utility function to look up the target for a given hyperlink event.
	 * Relies on the unique id for the target being put into the href of
	 * the hyperlink.  As a backup, checks the link label as a print name.
	 * @param e
	 * @return the temporal node targeted by the hyperlink, if any
	 */
	private EPlanElement getTargetNode(LocationEvent e) {
		EPlanElement node = null;
		String href = e.location;
		if (href != null) {
			String uniqueId = PlanPrinter.getUniqueIdFromHref(href);
			node = identifiableRegistry.getIdentifiable(EPlanElement.class, uniqueId);
		}
		if (node == null) {
			String printName = e.location;
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
