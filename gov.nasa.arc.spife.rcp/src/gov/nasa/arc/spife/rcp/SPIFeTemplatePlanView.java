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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanView;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanViewAddAction;

import org.eclipse.ui.IWorkbenchPart;

public class SPIFeTemplatePlanView extends TemplatePlanView {

	/** A unique ID string for this view. */
	public static final String ID = ClassIdRegistry.getUniqueID(SPIFeTemplatePlanView.class);
	
	@Override
	protected String getID() {
		return ID;
	}
	
	@Override
	protected TemplatePlanViewAddAction createAddNewItemAction() {
		SPIFeTemplatePlanViewAddNewItemAction action = new SPIFeTemplatePlanViewAddNewItemAction(this);
		action.setToolTipText("Add New Template");
		return action;
	}
	
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		PageRec pageRec = super.doCreatePage(part);
		if (pageRec.page instanceof SPIFeTemplatePlanPage) {
			SPIFeTemplatePlanPage page = (SPIFeTemplatePlanPage)pageRec.page;
			page.setAddAction((SPIFeTemplatePlanViewAddNewItemAction) addAction);
		}
		return pageRec;
	}

}
