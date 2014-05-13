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

import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

public class PlanActionFactory {

    private static final ImageData ADD_GROUP_ICON = IconLoader.getImageData(EditorPlugin.getDefault().getBundle(), "icons/new_activity_group");
    private static final ImageData COLLAPSE_ALL_ICON = IconLoader.getImageData(EditorPlugin.getDefault().getBundle(), "icons/collapseall");
    private static final ImageData EXPAND_ALL_ICON = IconLoader.getImageData(EditorPlugin.getDefault().getBundle(), "icons/expandall");

	public static final ActionFactory ADD_GROUP = new ActionFactory("addGroup") {
		
		@Override
		public IWorkbenchAction create(IWorkbenchWindow window) {
			if (window == null) {
                throw new IllegalArgumentException();
            }
			String string = "Add " + EPlanUtils.getActivityGroupDisplayName();
			String description = "This action will add a new " 
				+ EPlanUtils.getActivityGroupDisplayName() 
				+ " to the plan.";
			RetargetAction action = new RetargetAction(getId(), string); 
			action.setToolTipText(string);
			action.setDescription(description);
			window.getPartService().addPartListener(action);
            action.setActionDefinitionId("gov.nasa.ensemble.core.plan.editor.newActivityGroupAction"); //$NON-NLS-1$
            action.setImageDescriptor(new ImageDescriptor() {
            	@Override
            	public ImageData getImageData() {
            		return ADD_GROUP_ICON;
            	}
            });
            return action;
		}
		
	};

	/**
	 * Retargetable global action handler for 'collapse all'
	 */
	public static final ActionFactory COLLAPSE_ALL = new ActionFactory("collapse all") {
	    @Override
		public IWorkbenchAction create(IWorkbenchWindow window) {
	        if (window == null) {
	            throw new IllegalArgumentException();
	        }
	        RetargetAction action = new RetargetAction(getId(), "Collapse All");
	        action.setToolTipText("collapses all of the items in the current view/editor"); 
	        action.setDescription("This action collapses all of the items in the current view/editor"); 
	        window.getPartService().addPartListener(action);
	        action.setActionDefinitionId("gov.nasa.ensemble.core.plan.editor.collapseAllAction");
	        action.setImageDescriptor(new ImageDescriptor() {
            	@Override
            	public ImageData getImageData() {
            		return COLLAPSE_ALL_ICON;
            	}
            });
            return action;
	    }
	};
	
	/**
	 * Retargetable global action handler for 'expand all'
	 */
	public static final ActionFactory EXPAND_ALL = new ActionFactory("expand all") {
	    @Override
		public IWorkbenchAction create(IWorkbenchWindow window) {
	        if (window == null) {
	            throw new IllegalArgumentException();
	        }
	        RetargetAction action = new RetargetAction(getId(), "Expand All");
	        action.setToolTipText("expands all of the items in the current view/editor"); 
	        action.setDescription("This action expands all of the items in the current view/editor"); 
	        window.getPartService().addPartListener(action);
	        action.setActionDefinitionId("gov.nasa.ensemble.core.plan.editor.expandAllAction");
	        action.setImageDescriptor(new ImageDescriptor() {
            	@Override
            	public ImageData getImageData() {
            		return EXPAND_ALL_ICON;
            	}
            });
            return action;
	    }
	};

}
