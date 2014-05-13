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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.treetable;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.dnd.AbstractTreeViewerDragSourceListener;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.DragSource;

/* package */ 
class TreeTableViewerDragSourceListener extends AbstractTreeViewerDragSourceListener {
	
	public TreeTableViewerDragSourceListener(TreeTableViewer viewer, IEnsembleEditorModel editorModel) {
		super(viewer, editorModel);
	}

	@Override
	protected ITransferable getTransferable(DragSource source, long time) {
		ISelection selection = viewer.getSelection();
		TreeTableContentProvider contentProvider = (TreeTableContentProvider)viewer.getContentProvider();
		IStructureModifier modifier = contentProvider.getStructureModifier();
		return modifier.getTransferable(selection);
	}
	
}
