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
package gov.nasa.ensemble.core.plan.editor.merge.action;

import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class ToggleRowHighlightActionBarContributor extends MultiPageEditorActionBarContributor {

	private static final ImageDescriptor ICON = MergePlugin.getImageDescriptor("icons/rainbow.png");
	
	private ToggleRowHighlightAction action = new ToggleRowHighlightAction();
	private MergeRowHighlightDecorator decorator = null;
	private IEditorPart activeEditor;
	
	@Override
	public void setActivePage(IEditorPart editor) {
		if (editor != null) {
			activeEditor = editor;
			decorator = (MergeRowHighlightDecorator) editor.getAdapter(MergeRowHighlightDecorator.class);
			if (decorator != null) {
				action.setEnabled(decorator.isRowColorHighlightingEnabled());
				action.setChecked(decorator.isRowColorHighlightingVisible());
				return;
			}
		} 
		action.setEnabled(false);
		action.setChecked(false);
	}
	
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		super.contributeToMenu(menuManager);
		IMenuManager menu = menuManager.findMenuUsingPath("plan");
		if (menu != null) {
			menu.add(action);
		}
	}
	
	private class ToggleRowHighlightAction extends Action {
		
		private static final String TEXT = "Show Row Highlight";
		
		@Override
		public void run() {
			decorator.toggleRowHighlightVisibility();
			action.setChecked(decorator.isRowColorHighlightingVisible());
			if (activeEditor instanceof AbstractPlanEditorPart) {
				((AbstractPlanEditorPart) activeEditor).refresh();
			}
		}
		
		@Override
		public String getText() {
			return TEXT;
		}
		
		@Override
		public ImageDescriptor getImageDescriptor() {
			return ICON;
		}
		
		@Override
		public int getStyle() {
			return IAction.AS_CHECK_BOX;
		}
		
	}

}
