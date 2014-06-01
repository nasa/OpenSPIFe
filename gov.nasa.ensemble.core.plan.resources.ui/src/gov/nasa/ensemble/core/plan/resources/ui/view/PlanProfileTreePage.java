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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.UndoRedoUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.profile.tree.ProfileTreePage;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.IPageSite;

public class PlanProfileTreePage extends ProfileTreePage {

	@SuppressWarnings("unused")
	private final PlanEditorModel planEditorModel;
	private final EPlan plan;
	private final IWorkbenchPartSite viewSite;
	private TreeViewer treeViewer;
	
	public PlanProfileTreePage(IEditorPart part, PlanEditorModel planEditorModel) {
		this.planEditorModel = planEditorModel;
		this.plan = planEditorModel.getEPlan();
		this.viewSite = part.getSite();
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		IPageSite site = getSite();
		UndoRedoUtils.setupUndoRedo(site.getActionBars(), viewSite, planEditorModel.getUndoContext());
	}

	@Override
	protected void configureTreeViewer(TreeViewer treeViewer) {
		super.configureTreeViewer(treeViewer);
		this.treeViewer = treeViewer;
		treeViewer.setContentProvider(new PlanProfileTreeContentProvider());
		treeViewer.setInput(plan);
		treeViewer.expandToLevel(2);
	}
	
	public List<Profile> removeRow() {
		List<Profile> removeList = new ArrayList();
		if (treeViewer != null) {
			ISelection selection = treeViewer.getSelection();
			if (selection instanceof StructuredSelection) {
				final List<EObject> list = ((StructuredSelection)selection).toList();
				for (EObject eObject : list) {
					if (eObject instanceof Profile) {
						removeList.add((Profile) eObject);
					}
				}
				TransactionUtils.writing(plan, new Runnable() {
					@Override
					public void run() {
						Set<Resource> resources = new HashSet<Resource>();
						//WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles().remove(list);
						
						for(EObject eObject : list) {
							if(eObject instanceof Profile) {
								Profile profile = (Profile)eObject;
								resources.add(profile.eResource());
								EcoreUtil.delete(profile);
							}
						}
						
						// Save Resources
						for(Resource resource : resources) {
							saveProfileResource(resource);
						}
						
						// Save Plan
						saveProfileResource(plan.eResource());
					}
				});
				
				// Remove from viewer
				treeViewer.remove(list.toArray());
			}
		}
		return removeList;
	}
	
	public List<Profile> getSelectedProfiles() {
		List<Profile> selectedProfiles = new ArrayList<Profile>();
		if (treeViewer != null) {
			ISelection selection = treeViewer.getSelection();
			if (selection instanceof StructuredSelection) {
				for (EObject eObject : (List<EObject>)((StructuredSelection)selection).toList()) {
					if (eObject instanceof Profile) {
						selectedProfiles.add((Profile) eObject);
					}
				}
			}
		}
		return selectedProfiles;
	}
	
	/**
	 * Save profile resources
	 * 
	 * @param resource
	 */
	private void saveProfileResource(final Resource resource) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
					@Override
					protected void execute(IProgressMonitor monitor) {
						try {
							resource.save(null);
						} catch (IOException e) {
							LogUtil.error(e);
						}
					}
				};
				try {
					operation.run(null);
				} catch (InvocationTargetException e) {
					LogUtil.error(e);
				} catch (InterruptedException e) {
					LogUtil.error(e);
				}
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.setName("Profile saver thread");
		thread.start();
	}
}
