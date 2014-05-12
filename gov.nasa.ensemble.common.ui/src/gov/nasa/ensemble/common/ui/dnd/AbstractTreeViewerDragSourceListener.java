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
package gov.nasa.ensemble.common.ui.dnd;

import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public abstract class AbstractTreeViewerDragSourceListener extends EnsembleDragSourceListener {

	/**
	 * Cache of the TreeViewer that the drag contents can be derived from,
	 * typically through the current selection
	 */
	protected final AbstractTreeViewer viewer;

	/**
	 * Cache of the selected objects. Set during dragStart and read (and
	 * cleared) during dragSetData. The cache is required for drag and drop
	 * to work on all platforms -- on the Mac, the selection is cleared by
	 * the time dragSetData is called and therefore the cache is required.
	 */		
	protected ITransferable transferable;
	
	public AbstractTreeViewerDragSourceListener(AbstractTreeViewer viewer, IEnsembleEditorModel editorModel) {
		super(viewer.getControl(), editorModel);
		this.viewer = viewer;
	}
	
	@Override
	protected boolean isDragPossible(DragSource source, long time) {
		transferable = getTransferable(source, time);
		if (transferable == null) {
			return false;
		}
		Transfer transfers[] = TransferRegistry.getInstance().getTransfersFor(transferable);
		source.setTransfer(transfers);
		viewer.cancelEditing();
		return true;
	}

	/**
	 * Returns the transferable from the arguments. If null, the transfer fails to
	 * be performed
	 */
	protected abstract ITransferable getTransferable(DragSource source, long time);

	@Override
	protected void finishDrag(DragSource source, long time, boolean doit, int detail) {
		if (doit) {
			// Nothing to do here since we typically create an undoable operation
			// in the drop listener which actually does all the work.
		}
		transferable = null;
	}

	@Override
	protected Object getDragData(DragSource source, long time, TransferData dataType) {
		return TransferRegistry.getInstance().getDragData(transferable, dataType);
	}
	
}
