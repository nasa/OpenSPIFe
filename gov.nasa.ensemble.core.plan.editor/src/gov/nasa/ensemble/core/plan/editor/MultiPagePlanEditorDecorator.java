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

import gov.nasa.ensemble.common.mission.MissionExtendable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

public class MultiPagePlanEditorDecorator implements MissionExtendable {
	
	private IEditorPart editorPart;
	
	public Composite createPageContainer(Composite parent, PlanEditorModel planEditorModel) {
		return parent;
	}
	
	protected final void setEditorPart(IEditorPart editorPart) { 
		this.editorPart = editorPart; 
	}
	
	protected final IEditorPart getEditorPart() { 
		return this.editorPart; 
	}
	
	public void dispose() { 
		/* do nothing */ 
	}
	
}
