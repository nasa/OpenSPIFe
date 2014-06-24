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
package gov.nasa.ensemble.core.jscience.ui.profile.tree;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.ui.view.page.DefaultPageBookView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;

public class ProfileTreeView extends DefaultPageBookView {

	public static final String ID = ClassIdRegistry.getUniqueID(ProfileTreeView.class);
	
	@Override
	protected Page createPageForEditor(IEditorPart editor) {
		return CommonUtils.getAdapter(editor, ProfileTreePage.class);
	}

	@Override
	protected Page createPageForView(IViewPart view) {
		return null;
	}

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return part instanceof IEditorPart;
	}
}
