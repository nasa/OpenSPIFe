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
package gov.nasa.ensemble.core.plan.editor.actions;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.plan.PlanPersister;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.ltk.internal.core.refactoring.resource.RenameResourceProcessor;
import org.eclipse.ltk.internal.ui.refactoring.RefactoringUIMessages;
import org.eclipse.ltk.internal.ui.refactoring.actions.AbstractResourcesHandler;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.ltk.ui.refactoring.resource.RenameResourceWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

@SuppressWarnings("restriction")
public class RenamePlanProjectHandler extends AbstractResourcesHandler {
	
	private static final String RENAME_PROJECT_TITLE = "Rename Plan Project";
	private static final String RENAME_RESOURCE_TITLE = "Rename Resource";

	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof TreeSelection) {
			Object firstElement = ((TreeSelection) selection).getFirstElement();
			IResource resource = (IResource) firstElement;
			String title = null;
			RefactoringWizard wizard = null;
			if (firstElement instanceof IProject) {
				title = RENAME_PROJECT_TITLE;
				wizard = new RenamePlanProjectWizard((IProject)resource);
			} else {
				title = RENAME_RESOURCE_TITLE;
				wizard = new RenameResourceWizard(resource);
			}
			Shell activeShell = HandlerUtil.getActiveShell(event);
			if (!saveBeforeRename(resource, title)) {
				return null;
			}
			RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
			try {
				op.run(activeShell, RENAME_PROJECT_TITLE);
			} catch (InterruptedException e) {
				// do nothing
			}
		}
		return null;
	}
	
	// Uses a simple confirmation dialog when there are unsaved project files
	protected boolean saveBeforeRename(IResource resource, final String title) {
		final List<IResource> dirty = getDirtyResources(resource);
		if (dirty.isEmpty()) {
			return true;
		}
		Display display = WidgetUtils.getDisplay();
		final boolean launch[] = new boolean[] { !PlatformUI.isWorkbenchRunning() };
		if (!launch[0]) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					StringBuilder builder = new StringBuilder("The following files being edited will be saved first:");
					builder.append('\n');
					for (IResource resource : dirty) {
						builder.append('\n');
						builder.append(resource.getName());
					}
					Shell parent = WidgetUtils.getShell();
					launch[0] = MessageDialog.openConfirm(parent, title, builder.toString());
				}
			});
		}
		if (launch[0]) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					IDE.saveAllEditors(dirty.toArray(new IResource[dirty.size()]), false);
				}
			});
		}
		return launch[0];
	}

	private List<IResource> getDirtyResources(IResource resource) {
		IContainer container = null;
		if (resource instanceof IContainer) {
			container = (IContainer)resource;
		}
		List<IResource> dirty = new ArrayList<IResource>();
		for(IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows()) {
			for(IWorkbenchPage page : workbenchWindow.getPages()) {
				if(page != null) {
					for (IEditorPart editor : page.getDirtyEditors()) {
						IResource editorResource = (IResource)editor.getEditorInput().getAdapter(IResource.class);
						if(editorResource != null
								&& (resource.equals(editorResource)
										|| (container != null && containedIn(editorResource, container)))) {
							dirty.add(editorResource);
						}
					}
				}
			}			
		}
		return dirty;
	}
	
	private boolean containedIn(IResource resource, IContainer container) {
		IContainer parent = resource.getParent();
		if (parent == null) {
			return false;
		}
		if (parent.equals(container)) {
			return true;
		}
		return containedIn(parent, container);
	}

	public static void renamePlan(final String newName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(newName);
		renamePlan(project, newName);
	}
	
	public static void renamePlan(IProject project, final String newName) {
		final EPlan plan = loadPlan(project);
		if (plan != null ) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.setName(newName);
				PlanPersister.getInstance().savePlan(plan, new NullProgressMonitor());
			}
		});
		} else {
			LogUtil.warn("Unable to load schedule.plan from " + project.getName());
		}
	}
	
	private static EPlan loadPlan(IProject project) {
		if (project.exists()) {
			try {
				if (!project.isOpen()) {
					project.open(null);
				}
				IFile file = project.getFile("schedule.plan");
				if (file.exists()) {
					URI uri = EMFUtils.getURI(file);
					EditingDomain editingDomain = EMFUtils.createEditingDomain();
					Resource planResource = editingDomain.getResourceSet().getResource(uri, true);
					EObject plan = EMFUtils.getLoadedContent(planResource);
					if (plan instanceof EPlan) {
						return (EPlan) plan;
					}
				}
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}
		return null;
	}
	
	private class RenamePlanProjectWizard extends RefactoringWizard {

		private Date planStartTime;
		private IProject project;
		
		public RenamePlanProjectWizard(IProject project) {
			super(new RenameRefactoring(new RenameResourceProcessor(project)), DIALOG_BASED_USER_INTERFACE | NO_PREVIEW_PAGE);
			this.project = project;
			EPlan plan = loadPlan(project);
			if (plan != null) {
				this.planStartTime = plan.getMember(PlanTemporalMember.class).getStartBoundary();
			}
			setDefaultPageTitle(RENAME_PROJECT_TITLE);
			setWindowTitle(RENAME_PROJECT_TITLE);
		}

		@Override
		protected void addUserInputPages() {
			RenameResourceProcessor processor = (RenameResourceProcessor) getRefactoring().getAdapter(RenameResourceProcessor.class);
			addPage(new RenameResourceRefactoringConfigurationPage(processor));
		}

		private class RenameResourceRefactoringConfigurationPage extends UserInputWizardPage {

			private static final String PROJECT_EXISTS_ERROR_MESSAGE = "A project with this name already exists.";
			private static final String PROJECT_NAME_INVALID_ERROR_MESSAGE = "This is an invalid name for a project.";
			
			private final RenameResourceProcessor fRefactoringProcessor;
			private Text fNameField;
			
			public RenameResourceRefactoringConfigurationPage(RenameResourceProcessor processor) {
				super("RenameResourceRefactoringInputPage"); //$NON-NLS-1$
				fRefactoringProcessor = processor;
			}

			@Override
			public void createControl(Composite parent) {
				Composite composite= new Composite(parent, SWT.NONE);
				composite.setLayout(new GridLayout(2, false));
				composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				composite.setFont(parent.getFont());

				Label label= new Label(composite, SWT.NONE);
				label.setText(RefactoringUIMessages.RenameResourceWizard_name_field_label);
				label.setLayoutData(new GridData());

				fNameField= new Text(composite, SWT.BORDER);
				fNameField.setText(fRefactoringProcessor.getNewResourceName());
				fNameField.setFont(composite.getFont());
				fNameField.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				fNameField.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						validatePage();
					}
				});

				fNameField.selectAll();
				setPageComplete(false);
				setControl(composite);
			}

			@Override
			public void setVisible(boolean visible) {
				if (visible) {
					fNameField.setFocus();
				}
				super.setVisible(visible);
			}

			protected final void validatePage() {
				String text = fNameField.getText();
				RefactoringStatus status = fRefactoringProcessor.validateNewElementName(text);
				if (text == null || text.isEmpty()) {
					status = RefactoringStatus.createFatalErrorStatus(PROJECT_NAME_INVALID_ERROR_MESSAGE);
				} else {
					try {
						if (!status.hasFatalError() && PlanPersister.getInstance().doesPlanExist(text, planStartTime)) {
							status = RefactoringStatus.createFatalErrorStatus(PROJECT_EXISTS_ERROR_MESSAGE);
						}
					} catch (IllegalArgumentException e) {
						status = RefactoringStatus.createFatalErrorStatus(PROJECT_NAME_INVALID_ERROR_MESSAGE);
					}
				}
				setPageComplete(status);
			}

			@Override
			protected boolean performFinish() {
				initializeRefactoring();
				closeProjectEditors();
				String newName = fNameField.getText();
				if (super.performFinish()) {
					renamePlan(newName);
					return true;
				} else {
					return false;
				}
			}

			/**
			 * Close all editors that are editing files in the project
			 * @param project the IProject whose editors should be closed
			 */
			private void closeProjectEditors() {
				IWorkbench workbench = PlatformUI.getWorkbench();
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				IWorkbenchPage activePage = window.getActivePage();
				IEditorReference[] editorReferences = activePage.getEditorReferences();
				for (IEditorReference editorReference : editorReferences) {
					IEditorPart editorPart = editorReference.getEditor(true);
					if (editorPart == null) {
						continue;
					}
					IEditorInput editorInput = editorPart.getEditorInput();
					if (editorInput instanceof IFileEditorInput) {
						IFile file = ((IFileEditorInput)editorInput).getFile();
						if (project.equals(file.getProject())) {
							activePage.closeEditor(editorPart, true);
						}
					}
				}
			}

			@Override
			public IWizardPage getNextPage() {
				initializeRefactoring();
				return super.getNextPage();
			}

			private void initializeRefactoring() {
				fRefactoringProcessor.setNewResourceName(fNameField.getText());
			}
		}
	}

}
