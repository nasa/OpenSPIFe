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

import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanPageBookView;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.Page;

public class InconsView extends PlanPageBookView {

	private static final String DEFAULT_MESSAGE = "This is where incons will be listed, once you create or open a plan.";
	public static final String ID = "gov.nasa.ensemble.core.plan.resources.ui.view.InconsView";
	
	public InconsView() {
		super(DEFAULT_MESSAGE);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
	}

	@Override
	protected Page createPage(IEditorPart editor, PlanEditorModel model) {
		return new InconsPage(editor, model);
	}
	
}
