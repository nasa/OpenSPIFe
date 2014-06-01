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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.core.jscience.ui.profile.tree.ProfileTreePage;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

public class PlanProfileTreePageAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IEditorPart
				&& ProfileTreePage.class == adapterType) {
			IEditorPart editor = (IEditorPart) adaptableObject;
			IEditorInput input = editor.getEditorInput();
			PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
			if (model != null) {
				return new PlanProfileTreePage(editor, model);
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { ProfileTreePage.class };
	}

}
