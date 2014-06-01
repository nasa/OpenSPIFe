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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ide.IGotoMarker;

public class ViolationClickListener implements IDoubleClickListener, ISelectionChangedListener {

	private final IEditorPart editor;
	private final PlanAdvisorMember planAdvisorMember;

	public ViolationClickListener(IEditorPart editor, PlanAdvisorMember planAdvisorMember) {
		this.editor = editor;
		this.planAdvisorMember = planAdvisorMember;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// aka single click
		select(event.getSelection());
		
	}
	@Override
	public void doubleClick(DoubleClickEvent event) {
		select(event.getSelection());
	}
	
	 private void select(ISelection selection) {
		if (selection instanceof TreeSelection)
		{
			TreeSelection treeSelection = (TreeSelection) selection;
			List<EPlanElement> elements = new ArrayList<EPlanElement>();
			Violation violation = null;
			EPlan plan = planAdvisorMember.getPlan();
			// can only double click on one violation at a time...
			for (Object object : treeSelection.toList()) {
				if (object instanceof ViolationTracker) {
					violation = ((ViolationTracker)object).getViolation();
					for (EPlanElement element : violation.getElements()) {
						if (EPlanUtils.getPlan(element) == plan) {
							elements.add(element);
						}
					}
				}
			}
			if (!elements.isEmpty()) {
				ISelectionProvider selectionProvider = editor.getSite().getSelectionProvider();
				selectionProvider.setSelection(new StructuredSelection(elements));
			}
			IEditorPart editor = this.editor;
			IResource resource = getResource(editor);
			if (editor instanceof MultiPagePlanEditor) {
				editor = ((MultiPagePlanEditor)editor).getCurrentEditor();
			}			
			if (editor instanceof IGotoMarker && violation != null && resource != null) {
				IGotoMarker gotoMarkerEditor = (IGotoMarker)editor;
				try {
					IMarker[] markers = resource.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
					for(IMarker marker : markers) {
						Violation markerViolation = planAdvisorMember.getMarkerViolation(marker);
						if (violation.equals(markerViolation)) {
							gotoMarkerEditor.gotoMarker(marker);
							break;
						}
					}
				} catch (CoreException e) {
					LogUtil.error(e);
				}
			}						
		}
	}	
	
	private IResource getResource(IEditorPart editor) {
		IResource resource = null;
		if (editor instanceof MultiPagePlanEditor) {
			Object adapter = editor.getAdapter(PlanEditorModel.class);
			if(adapter instanceof PlanEditorModel) {
				PlanEditorModel planEditorModel = (PlanEditorModel) adapter;
				adapter = planEditorModel.getEPlan().getAdapter(IResource.class);
				if(adapter instanceof IResource) {
					resource = (IResource)adapter;
				}
			}
    	}	
		return resource;
	}

}
