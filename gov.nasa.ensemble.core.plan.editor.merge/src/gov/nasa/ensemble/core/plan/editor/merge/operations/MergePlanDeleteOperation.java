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
package gov.nasa.ensemble.core.plan.editor.merge.operations;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.plan.editor.PlanDeleteOperation;
import gov.nasa.ensemble.core.plan.editor.merge.MergeEditor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public final class MergePlanDeleteOperation extends PlanDeleteOperation {

	private final ISelectionProvider provider;
    private ISelection oldSelection = null;
    private ISelection newSelection = null;
	private final Tree tree;

	public MergePlanDeleteOperation(ITransferable transferable, IStructureModifier modifier, ISelectionProvider provider, Tree tree) {
	    super(transferable, modifier);
		this.provider = provider;
		this.tree = tree;
		this.oldSelection = (provider != null ? provider.getSelection() : null);
    }

    @Override
    protected void dispose(UndoableState state) {
    	oldSelection = null;
    	newSelection = null;
    }

    @Override
    protected void execute() {
    	WidgetUtils.runInDisplayThread(tree, new Runnable() {
    		@Override
			public void run() {
    			TreeItem newSelectedItem = MergeEditor.getNewlySelectedItem(tree);
    			if (newSelectedItem != null) {
    				newSelection = new StructuredSelection(newSelectedItem.getData());
    			} else {
    				newSelection = StructuredSelection.EMPTY;
    			}
    		}
    	}, true);
    	super.execute();
    	WidgetUtils.runInDisplayThread(tree, new Runnable() {
    		@Override
			public void run() {
		    	if ((provider != null) && (newSelection != null)) {
		    		provider.setSelection(newSelection);
		    	}
    		}
    	}, true);
    }

    @Override
    protected void undo() {
    	super.undo();
    	WidgetUtils.runInDisplayThread(tree, new Runnable() {
    		@Override
			public void run() {
		    	if ((provider != null) && (oldSelection != null)) {
		    		provider.setSelection(oldSelection);
		    	}
    		}
    	}, true);
    }
}
