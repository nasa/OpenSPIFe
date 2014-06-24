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


import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;

/**
 * This selection listener is apparently a wrapper to prevent setSelection from
 * automatically revealing the selected items, as is its default behavior. 
 * 
 * @see setSelection
 */
public final class TreeTableViewerSelectionListener implements IPostSelectionProvider {

	private final TreeTableViewer treeTableViewer;

	public TreeTableViewerSelectionListener(TreeTableViewer mergeEditor) {
		treeTableViewer = mergeEditor;
	}

	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
    	treeTableViewer.addPostSelectionChangedListener(listener);
    }

    @Override
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
    	treeTableViewer.removePostSelectionChangedListener(listener);
    }

    @Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
    	treeTableViewer.addSelectionChangedListener(listener);
    }

    @Override
	public ISelection getSelection() {
    	return treeTableViewer.getSelection();
    }

    @Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
    	treeTableViewer.removeSelectionChangedListener(listener);
    }

    /**
     * Suppress the "reveal" on the tree table viewer.
     */
    @Override
	public void setSelection(ISelection selection) {
    	treeTableViewer.setSelection(selection, false);
    }
}
