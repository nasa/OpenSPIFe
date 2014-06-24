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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.internal.resources.ProjectInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.undo.CopyProjectOperation;

@SuppressWarnings("restriction")
public class SaveAsWizard extends Wizard implements MissionExtendable {

	protected MultiPagePlanEditor editor;
	protected EPlan plan;
	protected PlanOverview planOverview;
	protected SaveAsWizardPage page;

	protected PlanIdentification planIdentification;

	public static SaveAsWizard createSaveAsWizard(MultiPagePlanEditor editor) {
		try {
			return MissionExtender.construct(SaveAsWizard.class, new Class[] { MultiPagePlanEditor.class }, editor);
		} catch (ConstructionException e) {
			// Do nothing and just return an instance of this class.
		}
		return new SaveAsWizard(editor);
	}

	public SaveAsWizard(MultiPagePlanEditor editor) {
		super();
		this.editor = editor;
		this.plan = editor.getPlan();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		IProject project = EMFUtils.getProject(plan);
		if (project != null && project.exists()) {
			return performSaveAs();
		}
		return true;
	}

	protected boolean performSaveAs() {
		final String newPlanName = getNewPlanName();
		if (ResourcesPlugin.getWorkspace().getRoot().getProject(newPlanName).exists()) {
			String message = "Project '" + newPlanName + "' already exists.";
			Status status = new Status(IStatus.ERROR, EditorPlugin.ID, message);
			ErrorDialog.openError(getShell(), "Invalid input", null, status);
			return false;
		}
		WorkspaceModifyOperation operation = createSaveAsOperation(newPlanName);
		return executeSaveAsOperation(operation, false, true);
	}
	
	protected boolean executeSaveAsOperation(WorkspaceModifyOperation operation, boolean fork, boolean cancelable) {
		try {
			getContainer().run(fork, cancelable, operation);
		} catch (InvocationTargetException e) {
			LogUtil.error(e);
			return false;
		} catch (InterruptedException e) {
			LogUtil.error(e);
			return false;
		}
		return true;
	}

	private WorkspaceModifyOperation createSaveAsOperation(final String newPlanName) {
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) {
				//
				// Stop plan services
				editor.getPlanEditorModel().deactivatePlanServices();
				//
				// Copy the project
				final IProject project = EMFUtils.getProject(plan);
				CopyProjectOperation op = createCopyProjectOperation(project, newPlanName);
				try {
					IOperationHistory history = OperationHistoryFactory.getOperationHistory();
					history.execute(op, monitor, null);
				} catch (Exception e) {
					LogUtil.error("executing " + op.getLabel(), e);
					return;
				}
				
				final IProject destinationProject = ResourcesPlugin.getWorkspace().getRoot().getProject(newPlanName);
				// SPF-11242 Avoid using the old project's content type matcher (Eclipse copy project bug??)
				refreshContentTypeMatcher(destinationProject);

				// Update the URI's of the loaded resources
				final EditingDomain editingDomain = EMFUtils.getAnyDomain(plan);
				TransactionUtils.reading(editingDomain, new Runnable() {
					@Override
					public void run() {
						ResourceSet resourceSet = editingDomain.getResourceSet();
						for (Resource resource : resourceSet.getResources()) {
							URI uri = resource.getURI();
							IFile file = EMFUtils.getFile(uri);
							if (file != null) {
								IFile destinationFile = destinationProject.getFile(file.getProjectRelativePath());
								resource.setURI(EMFUtils.getURI(destinationFile));
							}
						}
						resourceSet.setURIConverter(new ProjectURIConverter(destinationProject));
					}
				});
				//
				// Update plan details
				TransactionUtils.writing(plan, new Runnable() {
					@Override
					public void run() {
						planIdentification.updatePlan(plan, false);
					}
				});
				//
				// Save, close and reopen the new plan via MultiPagePlanEditor calls
				editor.savePlan(plan, monitor);
				final IFile file = EMFUtils.getFile(plan.eResource().getURI());
				WidgetUtils.runInDisplayThread(getShell(), new Runnable() {
					@Override
					public void run() {
						editor.closePlan(plan);
						MultiPagePlanEditor.openEditorOnPlanFile(file);
					}
				});
			}
		};
		return operation;
	}

	protected CopyProjectOperation createCopyProjectOperation(IProject project, String newPlanName) {
		return new CopyProjectOperation(project, newPlanName, null, "Plan Save-as");
	}
	
	private void refreshContentTypeMatcher(IProject project) {
		ProjectInfo info = (ProjectInfo) ((Project) project).getResourceInfo(false, true);
		info.setMatcher(null);
	}

	@Override
	public void addPages() {
		page = createSaveAsWizardPage();
		addPage(page);
	}

	/**
	 * @return the new plan name.
	 */
	protected final String getNewPlanName() {
		if (planIdentification == null) {
			return plan.getName();
		}
		return planIdentification.getPlanName();
	}

	protected final SaveAsWizardPage createSaveAsWizardPage() {
		return new SaveAsWizardPage();
	}

	protected class SaveAsWizardPage extends WizardPage implements PropertyChangeListener {

		protected SaveAsWizardPage() {
			super("wizardPage");
			setTitle("Assign a unique name to a plan and save it");
			setDescription("This wizard will check the plan name against open plans and plans on the database to assure uniqueness.");
		}

		@Override
		public void createControl(Composite parent) {
			Composite planSaveDialogContainer = new Composite(parent, SWT.NONE);
			planSaveDialogContainer.setLayout(new GridLayout());
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			planSaveDialogContainer.setLayoutData(gridData);

			createPlanOverview(planSaveDialogContainer);
			createPlanIdentification(planSaveDialogContainer);

			setControl(planSaveDialogContainer);
			dialogChanged();
		}

		@Override
		public void dispose() {
			planIdentification.removePropertyChangeListener(this);
			super.dispose();
		}

		/**
		 * Create the GUI elements for the plan overview.
		 * 
		 * The default implementation of this method creates the mission independent plan overview. There should be no need for subclasses to override this method. Instead, override the mission
		 * extenders used by the DatabaseObjectOverview class.
		 * 
		 * @param parent
		 */
		private void createPlanOverview(Composite parent) {
			Group planOverviewGroup = new Group(parent, SWT.NONE);
			planOverviewGroup.setText("Plan Overview");
			planOverviewGroup.setLayout(new GridLayout());
			GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
			planOverviewGroup.setLayoutData(gridData);

			planOverview = new PlanOverview(parent, plan);
			planOverview.layout();
		}

		/**
		 * Create the GUI elements for the plan identification.
		 * 
		 * The default implementation of this method creates a plan identification that contains only the plan name. Subclasses may override this method to provide a more elaborate plan
		 * identification.
		 * 
		 * @param parent
		 */
		/**
		 * Create the plan identification GUI elements.
		 */
		protected Composite createPlanIdentification(Composite parent) {
			try {
				planIdentification = MissionExtender.construct(PlanIdentification.class);
				planIdentification.setPlanStart(getPlanStart());
				Composite identificationComposite = planIdentification.createPlanIdentification(parent, true);

				// initialize the plan identification's values using the plan parameters
				planIdentification.updateFields(plan);

				// listen for changes so the plan overview can by kept in sync
				planIdentification.addPropertyChangeListener(this);
				planIdentification.addPropertyChangeListener(planOverview);
				return identificationComposite;
			} catch (ConstructionException e) {
				LogUtil.error("Could not construct TemporalPlanIdentification", e);
				return null;
			}
		}

		/**
		 * @return the current day of mission (as a String) using the start time parameter of the plan or empty string if unable to determine the start time
		 */
		private Date getPlanStart() {
			return plan.getMember(TemporalMember.class).getStartTime();
		}

		protected boolean validateName(String planName) {
			// only allow alphanumeric characters and the underscore
			String invalidChars = planName.replaceAll("[a-zA-Z0-9_-]", "");
			if (!StringUtils.isEmpty(invalidChars)) {
				updateStatus("Invalid characters in the plan name: '" + invalidChars + "'");
				setPageComplete(false);
				return false;
			}

			if (planName.length() == 0) {
				updateStatus("Plan name must be specified");
				setPageComplete(false);
				return false;
			}

			return true;
		}

		/**
		 * Ensures that both text fields are set.
		 */
		public void dialogChanged() {
			final String planName = getNewPlanName();
			if (StringUtils.isEmpty(planName)) {
				updateStatus("Plan name must be specified");
				return;
			}

			if (!validateName(planName)) {
				return;
			}

			setErrorMessage(null);
			setPageComplete(false);

			final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(planName);
			if (project != null && project.exists()) {
				updateStatus("The project " + project.getName()+ " already exists in the workspace.", ERROR);
			} else {
				updateStatus(null, 0);
			}
		}

		protected void updateStatus(String message) {
			updateStatus(message, IStatus.ERROR);
		}

		protected void updateStatus(String message, int severity) {
			if (severity == WARNING) {
				setMessage(message, WARNING);
			} else {
				setErrorMessage(message);
			}
			setPageComplete(severity != ERROR);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(WrapperUtils.ATTRIBUTE_NAME)) {
				page.dialogChanged();
			} else if (evt.getPropertyName().equals(EditorPlugin.ATTRIBUTE_PLAN_STATE)) {
				dialogChanged();
			}
		}
	}
}
