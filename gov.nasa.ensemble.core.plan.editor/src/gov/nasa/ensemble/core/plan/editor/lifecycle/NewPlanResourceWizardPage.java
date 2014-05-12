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

import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.ExtensionPlanFactoryLookup;
import gov.nasa.ensemble.core.model.plan.IPlanFactory;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NewPlanResourceWizardPage extends NewPlanWizardPage implements MissionExtendable, Listener {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	public static final int PROBLEM_NONE = 0;
	public static final int PROBLEM_RESOURCE_EMPTY = 1;
	public static final int PROBLEM_RESOURCE_EXIST = 2;
	public static final int PROBLEM_PATH_INVALID = 4;
	public static final int PROBLEM_NAME_INVALID = 7;

	private Composite outterContainer;

	private String problemMessage = "";//$NON-NLS-1$
	private int problemType = PROBLEM_NONE;

	public static final int DEFAULT_PLAN_START_HOUR = 8;
	public static final int DEFAULT_PLAN_END_HOUR = 8;

	private Text planStartTimeText;
	private Text planEndTimeText;

	private String planIdUpdateStatus;

	private String startEndTimeUpdateStatus;

	private Combo planType;

	private Text resourceNameField;

	private IStructuredSelection currentSelection;

	private String resourceExtension;

	private static final int SIZING_TEXT_FIELD_WIDTH = 200;

	private List<String> actions;
	private ExtensionPlanFactoryLookup lookup;
	private IPlanFactory action;

	public NewPlanResourceWizardPage(IStructuredSelection selection) {
		this.currentSelection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		outterContainer = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		outterContainer.setLayout(layout);

		GridData layoutData = new GridData();
		layoutData.horizontalSpan = 2;
		outterContainer.setLayoutData(layoutData);

		lookup = new ExtensionPlanFactoryLookup();
		actions = lookup.getAvailable();

		final Composite innerTopComposite = new Composite(outterContainer, SWT.NONE);
		{
			GridLayoutFactory.fillDefaults().numColumns(2).margins(30, 10).applyTo(innerTopComposite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(2, 1).applyTo(innerTopComposite);
		}
		Label label = new Label(innerTopComposite, SWT.NONE);
		{
			GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(label);
		}
		label.setText("Plan type:");
		label.setFont(parent.getFont());
		planType = new Combo(innerTopComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		{
			GridDataFactory.fillDefaults().hint(200, SWT.DEFAULT).applyTo(planType);
		}
		planType.setItems(getItems(actions));
		planType.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = planType.getSelectionIndex();
				if (index > -1) {
					selectAction(index);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		final Composite innerBottomComposite = new Composite(outterContainer, SWT.NONE);
		{
			GridLayoutFactory.fillDefaults().numColumns(2).margins(30, 10).applyTo(innerBottomComposite);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(2, 1).applyTo(innerBottomComposite);
		}
		label = new Label(innerBottomComposite, SWT.NONE);
		{
			GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(label);
		}
		label.setText("File name:");
		label.setFont(parent.getFont());

		resourceNameField = new Text(innerBottomComposite, SWT.BORDER);
		{
			GridDataFactory.fillDefaults().hint(SIZING_TEXT_FIELD_WIDTH, SWT.DEFAULT).applyTo(resourceNameField);
		}
		resourceNameField.addListener(SWT.Modify, this);
		resourceNameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				handleResourceNameFocusLostEvent();
			}
		});
		resourceNameField.setEnabled(false);

		Label planStartLabel = new Label(innerBottomComposite, SWT.NONE);
		{
			GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(planStartLabel);
		}
		planStartLabel.setText("Start time:");

		String startDefaultTime = getDefaultPlanStartDateString();
		planStartTimeText = createDateText(innerBottomComposite, startDefaultTime);
		planStartTimeText.setEnabled(false);

		Label planEndLabel = new Label(innerBottomComposite, SWT.NONE);
		{
			GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(planEndLabel);
		}
		planEndLabel.setText("End time:");

		String endDefaultTime = getDefaultPlanEndDateString();
		planEndTimeText = createDateText(innerBottomComposite, endDefaultTime);
		planEndTimeText.setEnabled(false);

		if (planType.getItemCount() == 1) {
			planType.select(0);
			selectAction(0);
		}
		
		validatePage();
		setError(NewPlanResourceWizardPage.class, null);
		setMessage(null);
		setControl(outterContainer);
	}

	private String[] getItems(List<String> actions) {
		String[] result = new String[actions.size()];
		int index = 0;
		for (String str : actions) {
			result[index] = lookup.getActionProperty(str, "label");
			index++;
		}
		return result;
	}

	private Text createDateText(Composite parent, String defaultStartTime) {
		final Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);
		int stringWidth = calculateStringWidth(text, defaultStartTime);
		text.setText(defaultStartTime);
		text.addListener(SWT.Modify, this);
		text.setLayoutData(new GridData(stringWidth, -1));
		return text;
	}

	// Identical to TemporalNewPlanWizardPage.validateTimeFields(). Refactor!
	private boolean validateTimeFields() {
		String message = null;
		try {
			final Date startDate = getPlanStartDate();
			final Date endDate   = getPlanEndDate();
			if (startDate == null) {
				message = "Please specify a start time";
			} else if (endDate == null) {
				message = "Please specify an end time";
			} else if (startDate.compareTo(endDate) >= 0) {
				message = "Start time must be before End time";
			}
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			message = t.getLocalizedMessage();
		}
		startEndTimeUpdateStatus = message;
		displayPlanValidity();
		return (message == null);
	}

	/**
	 * If any of the updateStatus validation checks is not null, call updateStatus(String) with its message to feed it back to the user and prevent them pressing the "Finish" button. If all validation
	 * checks are null, the plan information is valid and the user may press "Finish" to create the plan.
	 */
	private void displayPlanValidity() {
		clearError(NewPlanResourceWizardPage.class);
		if (planIdUpdateStatus != null) {
			setError(NewPlanResourceWizardPage.class, planIdUpdateStatus);
		} else if (startEndTimeUpdateStatus != null) {
			setError(NewPlanResourceWizardPage.class, startEndTimeUpdateStatus);
		}
	}

	private int calculateStringWidth(Text text, String time) {
		// Compute the base size of the string, then add 5 chars.
		// Note, that if a short date is displayed, the size will be shorter
		// if the "size" changes over time: ex 10 becomes 100 then at that time the
		// string will grow. For Bedrest its worse since month, and day do not have
		// a leading zero.
		GC gc = new GC(text);
		gc.setFont(text.getFont());
		FontMetrics fontMetrics = gc.getFontMetrics();
		int stringWidth = fontMetrics.getAverageCharWidth() * (time.length() + 5);
		gc.dispose();
		return stringWidth;
	}

	private Date getDefaultPlanStartDate() {
		int dom = MissionConstants.getInstance().getDefaultPlanStartDay();
		Date date = MissionCalendarUtils.getMissionDate(dom);
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, DEFAULT_PLAN_START_HOUR);
		return cal.getTime();
	}

	private Date getDefaultPlanEndDate() {
		int dom = MissionConstants.getInstance().getDefaultPlanStartDay();
		Date date = MissionCalendarUtils.getMissionDate(dom + MissionConstants.HOW_MANY_DAYS_ARE_USUALLY_PLANNED_AT_ONCE);
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, DEFAULT_PLAN_END_HOUR);
		return cal.getTime();
	}

	private String getDefaultPlanStartDateString() {
		try {
			return DATE_STRINGIFIER.getDisplayString(getDefaultPlanStartDate());
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			return "";
		}
	}

	private String getDefaultPlanEndDateString() {
		try {
			return DATE_STRINGIFIER.getDisplayString(getDefaultPlanEndDate());
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			return "";
		}
	}

	protected Date getPlanStartDate() throws ParseException {
		String startTime = planStartTimeText.getText();
		Date planStartDate = DATE_STRINGIFIER.getJavaObject(startTime, getDefaultPlanStartDate());
		return planStartDate;
	}

	protected Date getPlanEndDate() throws ParseException {
		String endTime = planEndTimeText.getText();
		return DATE_STRINGIFIER.getJavaObject(endTime, getDefaultPlanEndDate());
	}

//	private IPlanFactory getPlanFactoryAction() {
//		return action;
//	}

	public String getResource() {
		String resource = resourceNameField.getText();
		if (useResourceExtension()) {
			return resource + '.' + resourceExtension;
		}
		return resource;
	}

	private boolean useResourceExtension() {
		String resource = resourceNameField.getText();
		if ((resourceExtension != null) && (resourceExtension.length() > 0) && (resource.length() > 0) && (resource.endsWith('.' + resourceExtension) == false)) {
			return true;
		}
		return false;
	}

	public void setResourceExtension(String value) {
		resourceExtension = value;
		validateResource();
	}

	public boolean areAllValuesValid() {
		return problemType == PROBLEM_NONE;
	}

	public int getProblemType() {
		return problemType;
	}

	private boolean validateResource() {
		// don't attempt to validate controls until they have been created
		if (outterContainer == null) {
			return false;
		}
		problemType = PROBLEM_NONE;
		problemMessage = "";//$NON-NLS-1$

		if (!validateResourceName()) {
			return false;
		}

		IPath path = getWorkspaceResourcePath().append(getResource());
		return validateFullResourcePath(path);
	}

	public File getResourceFile() {
		return getWorkspaceResourcePath().append(getResource()).toFile();
	}

	private boolean validateFullResourcePath(IPath resourcePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IStatus result = workspace.validatePath(resourcePath.toString(), IResource.FOLDER);
		if (!result.isOK()) {
			problemType = PROBLEM_PATH_INVALID;
			problemMessage = result.getMessage();
			return false;
		}

		if (workspace.getRoot().getFile(resourcePath).exists()) {
			problemType = PROBLEM_RESOURCE_EXIST;
			problemMessage = NLS.bind("{0} already exits", getResource());
			return false;
		}
		return true;
	}

	private boolean validateResourceName() {
		String resourceName = getResource();

		if (resourceName.length() == 0) {
			problemType = PROBLEM_RESOURCE_EMPTY;
			problemMessage = NLS.bind("File name cannot be empty", "file");
			return false;
		}

		if (!Path.ROOT.isValidPath(resourceName)) {
			problemType = PROBLEM_NAME_INVALID;
			problemMessage = NLS.bind("{0} is an invalid file name", resourceName);
			return false;
		}
		return true;
	}

	private void handleResourceNameFocusLostEvent() {
		if (useResourceExtension()) {
			setResource(resourceNameField.getText() + '.' + resourceExtension);
		}
	}

	public void setResource(String value) {
		resourceNameField.setText(value);
		validateResource();
	}

	public String getProblemMessage() {
		return problemMessage;
	}

	private boolean validatePage() {
		clearError(NewPlanResourceWizardPage.class);
		validateResource();
		if (!areAllValuesValid()) {
			// if blank name then fail silently
			if (getProblemType() == PROBLEM_RESOURCE_EMPTY) {
				setError(NewPlanResourceWizardPage.class, getProblemMessage());
			} else {
				setError(NewPlanResourceWizardPage.class, getProblemMessage());
			}
			return false;
		}

		String resourceName = getResource();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus result = workspace.validateName(resourceName, IResource.FILE);
		if (!result.isOK()) {
			setError(NewPlanResourceWizardPage.class, result.getMessage());
			return false;
		}
		return validateTimeFields();
	}

	@Override
	public void handleEvent(Event event) {
		setPageComplete(validatePage());
	}

	public IPlanFactory getAction() {
		return action;
	}

	public IResource getSelectedResource() {
		IResource selectedResource = null;
		Iterator it = currentSelection.iterator();
		if (it.hasNext()) {
			Object object = it.next();
			if (object instanceof IResource) {
				selectedResource = (IResource) object;
			} else if (object instanceof IAdaptable) {
				selectedResource = (IResource) ((IAdaptable) object).getAdapter(IResource.class);
			}
		}
		return selectedResource;
	}

	public IPath getWorkspaceResourcePath() {
		IResource selectedResource = getSelectedResource();
		if (selectedResource != null) {
			if (selectedResource.getType() == IResource.FILE) {
				return selectedResource.getParent().getFullPath();
			}
			if (selectedResource.isAccessible()) {
				return selectedResource.getFullPath();
			}
		}
		return null;
	}

	public String getPlanType() {
		return planType.getText();
	}

	private void selectAction(int index) {
		action = lookup.getAction(actions.get(index));
		String filename = lookup.getActionProperty(actions.get(index), "filename");
		resourceNameField.setEnabled(true);
		planStartTimeText.setEnabled(true);
		planEndTimeText.setEnabled(true);
		resourceNameField.setText((filename != null) ? filename : "");
		resourceNameField.setFocus();
	}
	
}
