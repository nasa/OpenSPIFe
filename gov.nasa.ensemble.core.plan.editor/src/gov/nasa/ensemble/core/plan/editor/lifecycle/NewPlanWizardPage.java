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

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (plan).
 * 
 * This page will first create and open a project named "plan" (if one does
 * not already exist or is not currently open).  It will use the plan project
 * as the container for all newly created plans.
 */
public class NewPlanWizardPage extends EnsembleWizardPage implements MissionExtendable {
	
	/**
	 * First try to create a mission-specific subclass. If that fails, log the failure and
	 * return an instance of this class.
	 * @return a new instance of this class or a mission-specific subclass thereof
	 */
	public static NewPlanWizardPage createInstance() {
		try {
			return MissionExtender.construct(NewPlanWizardPage.class);
		} catch (ConstructionException e) {
			Logger.getLogger(NewPlanWizardPage.class).error(e.getMessage(), e);
			return new NewPlanWizardPage();
		}
	}
	
	/** The name of the project resource that is to contain the new plan. */
	protected String containerName = "plan";
	
	/** The text box widget for soliciting the name of the plan. */
	protected Text planText;
	
	/**
	 * Constructor for NewPlanWizardPage sets the title and description. This is invoked via
	 * "new" only if there is no mission-specific subclass.
	 */
	public NewPlanWizardPage() {
		super("wizardPage");
		setTitle("Create a New Plan");
		setDescription("Please provide a name for the plan you are creating.  After creation, you may edit this plan and save it to the database.");
	}

	/**
	 * Create the control that contains the label and text box for soliciting the plan name and
	 * then immediately validate the default plan name, which will be invalid.
	 * @param parent the parent Composite that will contain this control
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		buildPlanNameText(container);
		
		setControl(container);
		pageUpdated();
	}
	
	/**
	 * Return a new plan instance with the solicited name.
	 * @param monitor ignored
	 * @return a new plan instance with the solicited name
	 */
	public EPlan createNewPlan(IProgressMonitor monitor) {
		return PlanFactory.getInstance().createPlan(getPlanName());
	}
	
	/**
	 * Create a label and text box for soliciting the plan's name.
	 * @param parent the Composite that contains the label and text box
	 */
	private void buildPlanNameText(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("&Plan name:");

		planText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		planText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		planText.addModifyListener(new EnsembleWizardPage.DefaultModifyListener());
		planText.setText("New Plan");
	}
	
	/**
	 * A plan's name string is valid iff it is non-empty and consists only of alphanumeric
	 * characters.
	 * @param planName the plan's name string to be validated
	 * @return true iff the planName is non-empty and consists only of alphanumeric characters
	 */
	protected boolean validateName(String planName) {
		// only allow alphanumeric characters and the underscore
		clearError(NewPlanWizardPage.class);
		String invalidChars = planName.replaceAll("[a-zA-Z0-9_]", "");
		if (!StringUtils.isEmpty(invalidChars)) {
			setError(NewPlanWizardPage.class, "Invalid characters in the plan name: '" + invalidChars + "'");
			return false;
		}
		if (planName.length() == 0) {
			setError(NewPlanWizardPage.class, "Plan name must be specified");
			return false;
		}
		return true;
	}

	/**
	 * Clear any prior errors for this page and validate the plan name string from the text box.
	 * Ensures that both text fields are set.
	 */
	@Override
	protected void pageUpdated() {
		clearError(NewPlanWizardPage.class);
		String fileName = getPlanName();
		validateName(fileName);
	}

	/**
	 * Return the name of the project resource, which is "plan".
	 * @return the name of the project resource, which is "plan"
	 */
	public String getContainerName() {
		return containerName;
	}

	/**
	 * Return the contents of the text box for soliciting the plan's name.
	 * @return the contents of the text box for soliciting the plan's name
	 */
	public String getPlanName() {
		return planText.getText();
	}
	
}
