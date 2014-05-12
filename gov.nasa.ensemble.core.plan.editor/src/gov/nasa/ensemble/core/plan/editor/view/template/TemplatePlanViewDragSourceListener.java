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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.dnd.AbstractTreeViewerDragSourceListener;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSource;

/* package */ 
class TemplatePlanViewDragSourceListener extends AbstractTreeViewerDragSourceListener {
	
	public TemplatePlanViewDragSourceListener(TreeViewer viewer) {
		super(viewer, null);
	}
	
	@Override
	protected ITransferable getTransferable(DragSource source, long time) {
		ISelection selection = viewer.getSelection();
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		return modifier.getTransferable(selection);
	}

	@Override
	protected boolean isDragPossible(DragSource source, long time) {
		return super.isDragPossible(source, time)
				&& !containsIgnorable();
	}

	private boolean containsIgnorable() {
		if (transferable instanceof PlanTransferable) {
			for (EPlanElement element : ((PlanTransferable)transferable).getPlanElements()) {
				if (element instanceof TemplatePlanViewIgnorable) {
					return true;
				}
			}
		}
		return false;
	}

}
