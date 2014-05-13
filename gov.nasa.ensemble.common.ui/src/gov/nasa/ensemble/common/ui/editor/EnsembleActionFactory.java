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

import org.eclipse.ui.actions.ActionFactory;

public abstract class EnsembleActionFactory extends ActionFactory {

	private EnsembleActionFactory(String actionId) {
		super(actionId);
	}
	
//	/**
//     * Workbench action (id "paste"): Paste. This action is a
//     * {@link RetargetAction} with id "paste". This action maintains
//     * its enablement state.
//     */
//    public static final ActionFactory PASTE_SPECIAL = new ActionFactory("pasteSpecial") {//$NON-NLS-1$
//        
//        /* (non-Javadoc)
//         * @see org.eclipse.ui.actions.ActionFactory#create(org.eclipse.ui.IWorkbenchWindow)
//         */
//        @Override
//		public IWorkbenchAction create(IWorkbenchWindow window) {
//            if (window == null) {
//                throw new IllegalArgumentException();
//            }
//            RetargetAction action = new RetargetAction(getId(), "Paste special...");
//            action.setToolTipText("Paste special"); 
//            window.getPartService().addPartListener(action);
//            action.setActionDefinitionId("org.eclipse.ui.edit.pastespecial"); //$NON-NLS-1$
//            ISharedImages sharedImages = window.getWorkbench().getSharedImages();
//            action.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
//            action.setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
//            return action;
//        }
//    };
	
}
