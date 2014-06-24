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
package gov.nasa.ensemble.common.ui.treetable;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.TransferData;

public abstract class TreeTableContentProvider implements ITreeContentProvider {

	protected TreeTableViewer viewer = null;

	public TreeTableContentProvider() {
		super();
	}

	protected final TreeTableViewer getViewer() {
	    return viewer;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeTableViewer) viewer;
	}
	
	public boolean acceptsDrop(TransferData sourceType, ISelection selection, InsertionSemantics semantics) {
		IStructureModifier structureModifier = getStructureModifier();
		return structureModifier.canInsert(sourceType, selection, semantics);
	}

	public IUndoableOperation createDropOperation(Object targetElement, InsertionSemantics semantics, TransferData currentDataType, int detail, Object data) {
		return null;
	}

	public IStructureModifier getStructureModifier() {
		return null;
	}

	@Override
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length != 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}
	
	public boolean isValidDrop(ISelection sourceSelection, Object target) {
		return true;
	}

}
