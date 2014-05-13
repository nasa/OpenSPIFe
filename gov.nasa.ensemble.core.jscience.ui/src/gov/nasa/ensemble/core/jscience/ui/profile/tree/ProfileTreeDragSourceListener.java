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
import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.common.ui.dnd.AbstractTreeViewerDragSourceListener;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.Activator;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Image;

public class ProfileTreeDragSourceListener extends AbstractTreeViewerDragSourceListener {
	
	protected ProfileTreeDragSourceListener(TreeViewer treeViewer, IEnsembleEditorModel editorModel) {
		super(treeViewer, editorModel);
	}
	
	@Override
	protected ITransferable getTransferable(DragSource source, long time) {
		ISelection selection = viewer.getSelection();
		if (selection instanceof StructuredSelection) {
			List<Profile> profiles = getResourceProfiles(selection);
			if (!profiles.isEmpty()) {
				return new ProfileTransferable(profiles);
			}
		}
		return null;
	}

	private List<Profile> getResourceProfiles(ISelection selection) {
		List<Profile> profiles = new ArrayList<Profile>();
		for (Object object : ((StructuredSelection)selection).toList()) {
			if (!(object instanceof Profile)) {
				continue;
			}
			profiles.add((Profile) object);
		}
		return profiles;
	}
	
	@Override
	protected Image getFeedbackImage() {
		if (CommonUtils.isOSLinux()) {
			// Linux already has a pretty feedback image
			return null;
		}
		return IconLoader.getIcon(Activator.getDefault().getBundle(), 
				"icons/resource_profile_part.gif");
	}
	
}
