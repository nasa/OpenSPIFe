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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;

/**
 * Action delegate that adds a layer of support for enablement/disablement
 * of actions that only are allowable when the merge editor is selected.
 */
public abstract class TimelineEditorActionDelegate implements IEditorActionDelegate {

	private MultiPagePlanEditor currentEditor = null;
	private IAction action = null;
	
	private IPropertyListener listener = new IPropertyListener() {
		@Override
		public void propertyChanged(Object source, int propId) {
			action.setEnabled(currentEditor.getCurrentEditor() instanceof TimelineEditorPart);
		}
	};
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (!(targetEditor instanceof MultiPagePlanEditor)) {
			action.setEnabled(false);
			return;
		}
		
		if (currentEditor != null) {
			currentEditor.removePropertyListener(listener);
		}
	
		this.action = action;
		this.currentEditor = (MultiPagePlanEditor) targetEditor;
		this.currentEditor.addPropertyListener(listener);
		
		action.setEnabled(currentEditor.getCurrentEditor() instanceof TimelineEditorPart);
	}

	protected MultiPagePlanEditor getActiveEditor() {
		return currentEditor;
	}
	
}
