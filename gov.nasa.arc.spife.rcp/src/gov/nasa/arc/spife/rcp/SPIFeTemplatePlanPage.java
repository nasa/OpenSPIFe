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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPage;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPageSite;

public class SPIFeTemplatePlanPage extends TemplatePlanPage {

	public static final String TEMPLATES_PROJECT_NAME = "Templates";
	
	/** Prevent excess activity by not searching with every keystroke; pause half a second to calm things down. */
	private static final int SEARCH_DELAY = 500;
	
	private final IEditorPart editorPart;
	private final IFile projectTemplateFile;
	
	private final ISelectionListener selectionListener = new SelectionListener();
	private final IResourceChangeListener workspaceListener = new WorkspaceListener();
	private SPIFeTemplatePlanViewAddNewItemAction addAction = null;
	private IStructuredSelection currentEditorSelection;
	private final List<IFile> updatedTemplatePlanFiles = new ArrayList<IFile>();

	public SPIFeTemplatePlanPage(IEditorPart editorPart, IFile templateFile) {
		super(true, SEARCH_DELAY);
		this.editorPart = editorPart;
		this.projectTemplateFile = templateFile;
		multiplePlans = true;
		readOnly = false;
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(workspaceListener, IResourceChangeEvent.POST_CHANGE);
		IPageSite site = getSite();
	    if (site != null) {
		    IWorkbenchPage page = site.getPage();
		    page.addPostSelectionListener(selectionListener);
	    	ISelectionProvider partSelectionProvider = editorPart.getSite().getSelectionProvider();
		    ISelection selection = partSelectionProvider.getSelection();
		    if ((selection != null) && (selection instanceof IStructuredSelection)) {
	        	try {
	        		updateSelection((IStructuredSelection)selection);
	        	} catch (Exception e) {
	        		LogUtil.error("Error updating selection from editor", e);
	        	}
        	}
	    }
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(workspaceListener);
		IPageSite site = getSite();
	    if (site != null) {
		    IWorkbenchPage page = site.getPage();
		    page.removePostSelectionListener(selectionListener);
	    }
		super.dispose();
	}

	public void setAddAction(SPIFeTemplatePlanViewAddNewItemAction addAction) {
		this.addAction = addAction;
	}

	@Override
	protected AdapterFactoryLabelProvider getViewerLabelProvider(AdapterFactory adapterFactory) {
		return new SPIFeTemplateViewLabelProvider(adapterFactory);
	}
	
	@Override
	protected EPlan doLoadTemplatePlan(IProgressMonitor monitor) throws Exception {
		// Not used
		return null;
	}

	@Override
	protected List<EPlan> doLoadTemplatePlans(IProgressMonitor monitor) {
		List<EPlan> loadedPlans = new ArrayList<EPlan>();
		ResourceSet templatesResourceSet = TransactionUtils.createTransactionResourceSet(false);
		loadTemplatePlan(templatesResourceSet, projectTemplateFile, loadedPlans, monitor);
		addTemplatesProjectPlans(templatesResourceSet, loadedPlans, monitor);
		return loadedPlans;
	}

	private void addTemplatesProjectPlans(ResourceSet templatesResourceSet, List<EPlan> loadedPlans, IProgressMonitor monitor) {
			IProject templatesProject = ResourcesPlugin.getWorkspace().getRoot().getProject(SPIFeTemplatePlanPage.TEMPLATES_PROJECT_NAME);
			try {
				for (IResource resource : templatesProject.members()) {
					if (resource instanceof IFile) {
						IFile file = (IFile)resource;
						if (file.getFileExtension().equals("plan")) {
							loadTemplatePlan(templatesResourceSet, file, loadedPlans, monitor);
						}
					}
				}
			} catch (CoreException e) {
				LogUtil.error(e);
			}
	}

	public void loadTemplatePlan(ResourceSet resourceSet, IFile templateFile, List<EPlan> loadedPlans, IProgressMonitor monitor) {
		URI uri = EMFUtils.getURI(templateFile);
		try {
			EPlan plan = EPlanUtils.loadPlanIntoResourceSetWithErrorChecking(resourceSet, uri, monitor);
			loadedPlans.add(plan);
		} catch (Exception e) {
			LogUtil.error("failed to load template file from: " + uri, e);
		}
	}

	public void openTemplateInEditor(EPlanChild addedElement, Resource eResource, boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	public void updateSelection(IStructuredSelection selection) {
		currentEditorSelection = selection;
		if (addAction != null) {
			addAction.updateEnablement();
		}
	}
	
	public IStructuredSelection getCurrentEditorSelection() {
		return currentEditorSelection;
	}
	
	public void updatedTemplatePlanResource(EPlan plan) {
		Resource resource = plan.eResource();
		if (resource != null) {
			IFile file = EMFUtils.getFile(resource);
			if (file != null) {
				updatedTemplatePlanFiles.add(file);
			}
		}
	}

	private class SelectionListener implements ISelectionListener {
        @Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        	if (part != editorPart) 
        		return;
        	IPageSite site = getSite(); 
        	if ((site != null) && (site.getSelectionProvider() == null)) {
        		ISelectionProvider partSelectionProvider = editorPart.getSite().getSelectionProvider();
        		site.setSelectionProvider(partSelectionProvider);
        	}
        	if ((selection != null) && (selection instanceof IStructuredSelection)) {
	        	try {
	        		updateSelection((IStructuredSelection)selection);
	        	} catch (Exception e) {
	        		LogUtil.error("Error updating selection from editor", e);
	        	}
        	}
        }
        
        
    }
	
	private class WorkspaceListener implements IResourceChangeListener {

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			// we are only interested in POST_CHANGE events
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				IResourceDelta delta = event.getDelta();
				final boolean[] addedOrRemoved = new boolean[] {false};
				final List<IFile> changedFiles = new ArrayList<IFile>();
				try {
					delta.accept(new IResourceDeltaVisitor() {
						@Override
						public boolean visit(IResourceDelta delta) {
							IResource resource = delta.getResource();
							if (resource == null) {
								return false;
							}
							IProject project = resource.getProject();
							if (project == null) {
								// workspace root
								return true;
							}
							if (!project.getName().equals(TEMPLATES_PROJECT_NAME)) {
								return false;
							}
							if (resource.getType() == IResource.FILE) {
								switch (delta.getKind()) {
								case IResourceDelta.ADDED:
								case IResourceDelta.REMOVED:
									addedOrRemoved[0] = true;
									break;
								case IResourceDelta.CHANGED:
									changedFiles.add((IFile)resource);
									break;
								}
							}
							return true;
						}
					});
				} catch (CoreException e) {
					LogUtil.error(e);
				}
				boolean refresh = false;
				if (addedOrRemoved[0]) {
					refresh = true;
				} 
				if (!changedFiles.isEmpty()) {
					for (IFile file : changedFiles) {
						if (updatedTemplatePlanFiles.contains(file)) {
							updatedTemplatePlanFiles.remove(file);
						} else {
							refresh = true;
						}
					}
				}
				if (refresh) {
					refreshContents();
				}
			}
			
		}
		
	}

}
