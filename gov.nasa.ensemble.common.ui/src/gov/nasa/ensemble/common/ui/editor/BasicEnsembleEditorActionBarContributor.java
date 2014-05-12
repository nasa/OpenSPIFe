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
package gov.nasa.ensemble.common.ui.editor;


import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * This is a basic editor action bar contributor that can be registered as the contributor class
 * for an editor that implements IEnsembleEditorPart in order to update action bars when the part becomes active
 * 
 * @author rnado
 *
 */
public class BasicEnsembleEditorActionBarContributor extends EditorActionBarContributor {

	/**
	 * Overrides the setActiveEditor method on EditorActionBarContributor to update the action bars
	 * on an IEnsembleEditorPart when it becomes the active editor
	 */
	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		IActionBars bars = getActionBars();
		if (bars != null && targetEditor instanceof IEnsembleEditorPart) {
			((IEnsembleEditorPart)targetEditor).updateActionBars(bars);
		}
	}

}
