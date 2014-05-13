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
package gov.nasa.arc.spife.core.plan.pear.view.ui;

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanPageBookView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.Page;

public class ProfileEffectsAndRequirementsView extends PlanPageBookView {
	
	public static final String ID = ClassIdRegistry.getUniqueID(ProfileEffectsAndRequirementsView.class);
	
	public ProfileEffectsAndRequirementsView() {
		super("");
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		String wrapped = getPartName();
		String unwrapped = wrapped.replaceAll("&&", "&");
		setPartName(unwrapped);
		setTitleToolTip(unwrapped);
		super.init(site);
	}

	@Override
	protected Page createPage(IEditorPart editor, PlanEditorModel model) {
		return new ProfileEffectsAndRequirementsPage(getViewSite(), editor, model);
	}

}
