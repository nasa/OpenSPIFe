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
package gov.nasa.ensemble.core.plan.temporal.editor.lifecycle;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.mission.MissionTimeConstants;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.common.ui.preferences.time.MissionTimePreferencePage;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.lifecycle.NewPlanWizardPage;
import gov.nasa.ensemble.core.plan.editor.lifecycle.PlanIdentification;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 */
public class TemporalNewPlanWizardPage extends NewPlanWizardPage implements MissionExtendable {
	
	protected static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	protected Text planStartTimeText;
	protected Text planEndTimeText;
	protected PlanIdentification planIdentification;
	protected String startEndTimeUpdateStatus;
	
	private String planIdUpdateStatus;
	
	/**
	 * @param selection
	 */
	public TemporalNewPlanWizardPage() {
		setTitle("Create a New Plan");
		setDescription("Enter the initial plan parameters.  You may change this information once you create the plan.");
	}
	
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		GridLayoutFactory.fillDefaults().applyTo(container);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
		
		createPlanTimeScale(container);
		createPlanIdentification(container);
		
		planIdentification.updatePlanName();
		setControl(container);
	}
	
	@Override
	public EPlan createNewPlan(IProgressMonitor monitor) {
		final Date startDate;
		try {
			startDate = getPlanStartDate();
		} catch (ParseException e) {
			LogUtil.error("Bad plan start date", e);
			return null;
		}
		final Date endDate;
		try {
			endDate = getPlanEndDate();
		} catch (ParseException e) {
			LogUtil.error("Bad plan end date", e);
			return null;
		}
		EPlan plan = PlanFactory.getInstance().createPlanInstance(getPlanName());
		PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
		planTemporalMember.setStartBoundary(startDate);
		planTemporalMember.setEndBoundary(endDate);
		planTemporalMember.setStartTime(startDate);
		planTemporalMember.setDuration(DateUtils.subtract(endDate, startDate));
		planIdentification.updatePlan(plan);
		return plan;
	}
	
	/**
	 * this is really a custom version of PlanFactory.createPlan(..)
	 * @param file
	 * @param progressMonitor
	 * @return
	 */
	public EPlan createNewPlan(IFile file, IProgressMonitor progressMonitor) {
		final Date startDate;
		try {
			startDate = getPlanStartDate();
		} catch (ParseException e) {
			LogUtil.error("Bad plan start date", e);
			return null;
		}
		final Date endDate;
		try {
			endDate = getPlanEndDate();
		} catch (ParseException e) {
			LogUtil.error("Bad plan end date", e);
			return null;
		}
		URI uri = EMFUtils.getURI(file);
		final EPlan plan = TemporalUtils.createTemporalPlan(getPlanName(), uri, startDate, endDate);
		return plan;
	}

	public void updateSchedulePlan(final EPlan plan) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				planIdentification.updatePlan(plan);
			}
		});
	}
	
	@Override
	public boolean isPageComplete() {
		boolean pageComplete = super.isPageComplete() && this.isCurrentPage();
		return pageComplete;
	}	
	
	public PlanIdentification getPlanIdentification() {
		return planIdentification;
	}

	@Override
	public String getPlanName() {
		return planIdentification.getPlanName();
	}
	
	/**
	 * If any of the updateStatus validation checks is not null, call updateStatus(String)
	 * with its message to feed it back to the user and prevent them pressing the "Finish"
	 * button.  If all validation checks are null, the plan information is valid and the 
	 * user may press "Finish" to create the plan.
	 */
	protected void displayPlanValidity() {
	  clearError(TemporalNewPlanWizardPage.class);
		if (planIdUpdateStatus != null) {
			setError(TemporalNewPlanWizardPage.class, planIdUpdateStatus);
		}
		else if (startEndTimeUpdateStatus != null) {
		  setError(TemporalNewPlanWizardPage.class, startEndTimeUpdateStatus);
		}
	}
	
	protected Date getDefaultPlanStartDate() {
		int dom = MissionConstants.getInstance().getDefaultPlanStartDay();
		Date date = MissionCalendarUtils.getMissionDate(dom);
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		Integer hour = MissionTimePreferencePage.PREFERENCE_STORE.getInt(MissionTimeConstants.MISSION_HOUR_START_OF_DAY);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
	
	protected Date getDefaultPlanEndDate() {
		int dom = MissionConstants.getInstance().getDefaultPlanStartDay();
		Date date = MissionCalendarUtils.getMissionDate(dom + MissionConstants.HOW_MANY_DAYS_ARE_USUALLY_PLANNED_AT_ONCE);
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		Integer hour = MissionTimePreferencePage.PREFERENCE_STORE.getInt(MissionTimeConstants.MISSION_HOUR_START_OF_DAY);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}
	
	protected String getDefaultPlanStartDateString() {
		try {
			return DATE_STRINGIFIER.getDisplayString(getDefaultPlanStartDate());
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			return "";
		}
	}
	
	protected String getDefaultPlanEndDateString() {
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
		Date planStartDate =  DATE_STRINGIFIER.getJavaObject(startTime, getDefaultPlanStartDate());
		
		/* SPF-3786 - Can't create plans in the past
		PreferenceMissionConstants PreferenceMissionConstants = PreferenceMissionConstants.getInstance();
		Date earliestPlanStartTime = PreferenceMissionConstants.getEarliestPlanStartTime();
		if (earliestPlanStartTime.after(planStartDate)) {
			throw new ParseException("Plan start date must be after " 
					+ DATE_STRINGIFIER.getDisplayString(earliestPlanStartTime), 0);
		}
		*/
		return planStartDate;
	}
	
	protected Date getPlanEndDate() throws ParseException {
		String endTime = planEndTimeText.getText();
		return DATE_STRINGIFIER.getJavaObject(endTime, getDefaultPlanEndDate());
	}
	
	protected void createPlanTimeScale(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText("Plan Time Scale");
		GridLayoutFactory.fillDefaults().numColumns(4).margins(10, 10).equalWidth(false).applyTo(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		
		//Plan Start Time
		Label starTimeLabel = new Label(group, SWT.NONE);
		starTimeLabel.setText("Plan Start ");
		planStartTimeText = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(planStartTimeText);
		planStartTimeText.setText(getDefaultPlanStartDateString());
		planStartTimeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				WidgetUtils.runInDisplayThread(planStartTimeText, new Runnable() {
					@Override
					public void run() {
						if (validateTimeFields()) {
							planIdentification.setPlanStart(getPlanStart());
							planIdentification.updatePlanName();
						}
					}
				});
			}
		});
		planStartTimeText.addModifyListener(new DefaultModifyListener());
		
		//Plan End Time
		Label endTimeLabel = new Label(group, SWT.NONE);
		endTimeLabel.setText("\tPlan End ");
		planEndTimeText = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(planEndTimeText);
		planEndTimeText.setText(getDefaultPlanEndDateString());
		planEndTimeText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				WidgetUtils.runInDisplayThread(planEndTimeText, new Runnable() {
					@Override
					public void run() {
						// Unlike start, it's not used to identify the plan.
						// But we do need to validate it.
						validateTimeFields();
					}
				});
			}
		});
		planEndTimeText.addModifyListener(new DefaultModifyListener());
		group.pack();
	}
	
	private void createPlanIdentification(Composite parent) {
		try {
			planIdentification = MissionExtender.construct(PlanIdentification.class);
			planIdentification.setPlanStart(getPlanStart());
			Composite composite = planIdentification.createPlanIdentification(parent);
			planIdentification.createExtraPlanIdentificationFields(composite);
			Text planNameText = planIdentification.getPlanNameText();
			if (planNameText != null) {
				planNameText.addModifyListener(new EnsembleWizardPage.DefaultModifyListener());
				planIdentification.getPlanNameText().addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						if (validateName(planIdentification.getPlanName())) {
							clearError(TemporalNewPlanWizardPage.class);
						}
					}
				});
			}
		} catch (ConstructionException e) {
			LogUtil.error("Could not construct TemporalPlanIdentification",e);
		}
	}

	@Override
	protected boolean validateName(String planName) {
		if (!validateTimeFields()) {
			return false;
		}
		return super.validateName(planName);
	}

	// Identical to NewPlanResourceWizardPage.validateTimeFields(). Refactor!
	private boolean validateTimeFields() {
		String message = null;
		try {
			Date startDate = getPlanStartDate();
			Date endDate   = getPlanEndDate();
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
	
	private Date getPlanStart() {
		Date date = null;
		try {
			date = getPlanStartDate();
		} catch (ParseException e) {
			LogUtil.error("couldn't get start sol", e);
		}
		return date;
	}
	
}
