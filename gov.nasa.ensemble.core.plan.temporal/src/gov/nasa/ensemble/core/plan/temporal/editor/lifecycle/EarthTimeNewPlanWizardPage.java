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

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.type.editor.EnsembleTextBasedDateEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 */
public class EarthTimeNewPlanWizardPage extends TemporalNewPlanWizardPage implements MissionExtendable {
	
	protected static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(EarthTimeNewPlanWizardPage.class);
	
	protected EnsembleTextBasedDateEditor planStartEditor;
	protected EnsembleTextBasedDateEditor planEndEditor;
	
	/**
	 * @param selection
	 */
	public EarthTimeNewPlanWizardPage() {
		super();
	}
	
	@Override
	protected Date getDefaultPlanStartDate() {
		Date date = new Date();
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	@Override
	protected Date getDefaultPlanEndDate() {
		Date date = getDefaultPlanStartDate();
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}
	
	@Override
	protected Date getPlanStartDate() {
		return planStartEditor.getObject();
	}
	
	@Override
	protected Date getPlanEndDate() {
		return planEndEditor.getObject();
	}
	
	@Override
	protected void createPlanTimeScale(Composite parent) {
		Group planTimeScaleGroup = new Group(parent, SWT.NONE);
		planTimeScaleGroup.setText("Plan Time Scale");
		
		planTimeScaleGroup.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		planTimeScaleGroup.setLayoutData(gridData);
		
		Composite row = new Composite(planTimeScaleGroup, SWT.NONE);
		
		GridLayout layout = new GridLayout(4, false);
		row.setLayout(layout);
		
		GridData layoutData = new GridData();
		layoutData.horizontalSpan = 2;
		row.setLayoutData(layoutData);
		
		Label planStartLabel = new Label(row, SWT.NONE);
		planStartLabel.setText("Plan Start: ");
		
		planStartEditor = createDateEditor(row, getDefaultPlanStartDate());
		
		Label planEndLabel = new Label(row, SWT.NONE);
		planEndLabel.setText("    Plan End: ");
		
		planEndEditor = createDateEditor(row, getDefaultPlanEndDate());
	}
	
	private EnsembleTextBasedDateEditor createDateEditor(Composite parent, Date date) {
		final EnsembleTextBasedDateEditor editor = new EnsembleTextBasedDateEditor(this, parent, date);
		editor.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				WidgetUtils.runInDisplayThread(editor.getEditorControl(), new Runnable() {
					@Override
					public void run() {
						if (validateTimeFields()) {
							getPlanIdentification().setPlanStart(getPlanStartDate());
							getPlanIdentification().updatePlanName();
						}
					}
				});
			}
		});
		return editor;
	}
	
	private boolean validateTimeFields() {
		String message = null;
		Date startDate = getPlanStartDate();
		Date endDate = getPlanEndDate();
		if (startDate == null) {
			message = "Please specify a start time";
		} else if (endDate == null) {
			message = "Please specify an end time";
		} else if (startDate.compareTo(endDate) >= 0) {
			message = "Start time must be before End time";
		}
		startEndTimeUpdateStatus = message;
		displayPlanValidity();
		return (message == null);
	}
	
}
