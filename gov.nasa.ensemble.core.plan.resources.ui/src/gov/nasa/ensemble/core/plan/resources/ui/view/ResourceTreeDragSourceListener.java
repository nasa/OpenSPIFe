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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.common.ui.dnd.AbstractTreeViewerDragSourceListener;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.activityDictionary.resources.IndexableResourceDef;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.NumericResourceDefTransferable;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.StateResourceDefTransferable;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Image;

public class ResourceTreeDragSourceListener extends AbstractTreeViewerDragSourceListener {

	private static final Image RESOURCE_ICON = IconLoader.getIcon(gov.nasa.ensemble.core.plan.resources.ui.Activator.getDefault().getBundle(), "icons/resource_profile_part.gif");
	
	protected ResourceTreeDragSourceListener(TreeViewer treeViewer, IEnsembleEditorModel editorModel) {
		super(treeViewer, editorModel);
	}

	@Override
	protected ITransferable getTransferable(DragSource source, long time) {
		ISelection selection = viewer.getSelection();
		if (!(selection instanceof StructuredSelection)) {
			return null;
		}

		List<ENumericResourceDef> nrdefs = getNumericResourceDefs(selection);
		
		List<EStateResourceDef> srdefs = getStateResourceDefs(selection);
		if (nrdefs.size() == 0 && srdefs.size() == 0) {
			return null;
		} else
		if (nrdefs.size() != 0 && srdefs.size() != 0) {
			return null;
		}
		
		// one is guranteed to have defs in it
		if (nrdefs.size() > 0) {
			return new NumericResourceDefTransferable(nrdefs);
		} // else...
		return new StateResourceDefTransferable(srdefs);
	}

	private List<ENumericResourceDef> getNumericResourceDefs(ISelection selection) {
		List<ENumericResourceDef> defs = new ArrayList<ENumericResourceDef>();
		for (Object object : ((StructuredSelection )selection).toList()) {
			if (!(object instanceof ENumericResourceDef)) {
				continue;
			}
			
			ENumericResourceDef nrd = (ENumericResourceDef) object;
			if (nrd instanceof IndexableResourceDef) {
				IndexableResourceDef mrd = (IndexableResourceDef) nrd;
				if (mrd.getIndices() != null && mrd.getIndices().size() > 0) {
					continue;
				}
			} // else...
			defs.add(nrd);
		}
		return defs;
	}

	private List<EStateResourceDef> getStateResourceDefs(ISelection selection) {
		List<EStateResourceDef> defs = new ArrayList<EStateResourceDef>();
		for (Object object : ((StructuredSelection )selection).toList()) {
			if (!(object instanceof EStateResourceDef)) {
				continue;
			}
			defs.add((EStateResourceDef) object);
		}
		return defs;
	}

	@Override
	protected Image getFeedbackImage() {
		if (CommonUtils.isOSLinux()
				|| CommonUtils.isOSMac()) {
			// Linux already has a pretty feedback image
			return null;
		}
		return RESOURCE_ICON;
	}
}
