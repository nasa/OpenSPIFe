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
package gov.nasa.ensemble.common.ui.preferences.time;

import fj.P1;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.mission.MissionTimeConstants;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.common.ui.preferences.CheckBoxFieldEditor;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

public class MissionTimePreferencePage extends AbstractPreferencePage {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	public static final IPreferenceStore PREFERENCE_STORE = new ScopedPreferenceStoreMirroredToEclipsePreferences(MissionConstants.getScope(),
				 CommonPlugin.ID);
	static {
		try {
			MissionTimePreferenceInitializer initializer = new MissionTimePreferenceInitializer();
			initializer.initializeDefaultPreferences();
		} catch (Exception e) {
			// SPF-8494:  "I shouldn't have to have that property set if I'm running a product which never makes use of it."
		}
	}
	
	private static final Date sampleDateForTestingWhetherChangesWereMadeInThisSession = new Date();
	// SPF-8494 -- make this value lazily initialized
	private static final P1<String> resultOfTestingWhetherChangesWereMadeInThisSession = new P1<String>() {
		@Override
		public String _1() {
			return DATE_STRINGIFIER.getDisplayString(sampleDateForTestingWhetherChangesWereMadeInThisSession);
		}
	}.memo();
	
	private Label concreteSimDescriptionLabel;
	private ISO8601FieldEditor actualMissionStart;
	private ISO8601FieldEditor simMissionStart;
	private CheckBoxFieldEditor simMissionStartActive;

	public MissionTimePreferencePage() {
		super(PREFERENCE_STORE);
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(new GridLayout(1, false));
		
		Label warninglabel = new Label(parent, SWT.CENTER);
		warninglabel.setText("(After making changes, times may be inconsistent until you quit and restart.)");
		warninglabel.setLayoutData(new GridData(GridData.FILL, SWT.NONE, true, false, 2, 1));

		addField(actualMissionStart = createDateEditor(MissionTimeConstants.MISSION_START_TIME,
				"Mission Start Time (UTC):",
				parent));
		
		// Mission Start time of day!
		addField(createIntegerFieldEditor(MissionTimeConstants.MISSION_HOUR_START_OF_DAY, "Mission Hour Start Of Day: ", parent));
		warninglabel = new Label(parent, SWT.CENTER);
		warninglabel.setText("(This is the hour of the day where it breaks each day in the Days Editor.)");
		warninglabel.setLayoutData(new GridData(GridData.FILL, SWT.NONE, true, false, 2, 1));
		
        Group group = createGroup(parent);
        group.setText("Simulation");
        
        //
        // Mission time simulation
    	createCheckboxEnabledTextFieldEditor(
    			simMissionStartActive = 
			new CheckBoxFieldEditor(
					MissionTimeConstants.MISSION_SIMULATION_START_TIME_ACTIVE, "Set clock forward as if the above Start of Mission occurred at the real time of:",
					group),
					simMissionStart = createDateEditor(MissionTimeConstants.MISSION_SIMULATION_START_TIME, "", group)
    	);
	
    	concreteSimDescriptionLabel = new Label(group, SWT.CENTER | SWT.WRAP);
    	concreteSimDescriptionLabel.setLayoutData(new GridData(GridData.FILL, SWT.NONE, true, false, 2, 1));
    	concreteSimDescriptionLabel.setText(getConcreteSimDescription());
    	
    	String dayOrSol = MissionCalendarUtils.getMissionDayName();
		createCheckboxEnabledTextFieldEditor(
    			new CheckBoxFieldEditor(
    					MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY_ACTIVE, "Lock default "
    					+ dayOrSol
    					+ " for New Plan to:",
    					group),
    			new IntegerFieldEditor(
    					MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY,
    					"",
    					group)
    	);
    	Label unwarninglabel = new Label(group, SWT.CENTER);
		unwarninglabel.setText("(The default " + dayOrSol + " can be changed without restarting.)");
		unwarninglabel.setLayoutData(new GridData(GridData.FILL, SWT.NONE, true, false, 2, 1));
    	
	}

	private IntegerFieldEditor createIntegerFieldEditor(String propertyName, String label, Composite parent) {
		IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(propertyName, label, parent, 2);
		integerFieldEditor.setPreferenceStore(PREFERENCE_STORE);
		integerFieldEditor.load();
		integerFieldEditor.setValidRange(0, 23);
		integerFieldEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		return integerFieldEditor;
	}
	
	private ISO8601FieldEditor createDateEditor(String propertyName, String label, Composite parent) {
		ISO8601FieldEditor result = new ISO8601FieldEditor(propertyName, label, parent);
		result.getTextControl().addModifyListener(new DescriptionChangingModListener());
		return result;
	}

	private String getConcreteSimDescription() {
		try {
			Date now = new Date(System.currentTimeMillis());
			Date proposedActual = actualMissionStart.getDateValue();
			Date proposedSimulated = simMissionStart.getDateValue();
			if (proposedSimulated==null) return "";
			long proposedOffset = DateUtils.subtract(proposedActual, proposedSimulated);
			return "(Enabling this will set the clock "
			+ (proposedOffset > 0? "forward" : "backward")
			+ " "
			+ DurationFormat.getEnglishDuration(Math.abs(proposedOffset/1000))
			+ ", making the current time "
			+ formatWithProposedValues(null, now, true)
			+ ".)";
		} catch (Exception e) { return ""; }
	}
	
	private String formatWithProposedValues(DateFormat dateFormat, Date sampleDate, boolean pretendSimTimeEnabled) {
		try {
			if (dateFormat==null) dateFormat = ((DateStringifier)DATE_STRINGIFIER).getDateFormat();
			Date currentActual = MissionConstants.getInstance().getActualMissionStartTime();
			Date currentSimulated = MissionConstants.getInstance().getSimulatedMissionStartTime();
			if (currentSimulated==null) currentSimulated = currentActual;
			Date proposedActual = actualMissionStart.getDateValue();
			Date proposedSimulated = null;
			if (pretendSimTimeEnabled || simMissionStartActive.getBooleanValue()) {
				try {
					proposedSimulated = simMissionStart.getDateValue();
				} catch (NumberFormatException e) { /* stay null */ }
			}
			if (proposedSimulated==null) proposedSimulated=proposedActual;

			long proposedOffset = DateUtils.subtract(proposedActual, proposedSimulated);
			long currentOffset = DateUtils.subtract(currentActual, currentSimulated);
			return dateFormat.format(DateUtils.add(sampleDate, proposedOffset-currentOffset));
		} catch (NumberFormatException e) {
			return "Invalid date";
		}
	}
	

	@Override
	public boolean performOk() {
		// TODO:  The right way is probably to use a PreferenceChangeListener.	
		MissionConstants.getInstance().decacheMissionStartTime();
		if (isRestartRecommended()) {
			 MessageDialog.openWarning(getShell(), "Caution",
					 "You have changed the settings since starting.  Times may be inconsistent until you restart.");
		}
		return super.performOk();
	}
	
	private void updateRestartRecommendation() {
		if (isRestartRecommended()) {
			setMessage("These changes may make times inconsistent until you restart.",
					WARNING);}
		else {
			setMessage(null);
		}
	}

	private void createCheckboxEnabledTextFieldEditor(
		CheckBoxFieldEditor toggleEditor,
		FieldEditor valueEditor) {
		addField(toggleEditor);
		addField(valueEditor);
		Button checkbox = toggleEditor.getCheckbox();
		boolean selected = getPreferenceStore().getBoolean(toggleEditor.getPreferenceName());
		valueEditor.setEnabled(selected, checkbox.getParent());
		checkbox.addSelectionListener(new TextEnablementListener(valueEditor));
	}

	private final class DescriptionChangingModListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			concreteSimDescriptionLabel.setText(getConcreteSimDescription());
			updateRestartRecommendation();
		}
		
	}

	private final class TextEnablementListener implements SelectionListener {
			
		private final FieldEditor editor;

		private TextEnablementListener(FieldEditor text) {
			this.editor = text;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			final Button checkbox = (Button) e.getSource();
			enableDisable(editor, checkbox);
			updateRestartRecommendation();
		}		
	}
	
	private void enableDisable(final FieldEditor editor, Button checkbox) {
		final boolean isChecked = checkbox.getSelection();
		final Composite parent = checkbox.getParent();
		WidgetUtils.runInDisplayThread(checkbox, new Runnable() {
			@Override
			public void run() {
				editor.setEnabled(isChecked, parent);
			}
		});
	}
	
	private boolean isRestartRecommended() {
		String expected = resultOfTestingWhetherChangesWereMadeInThisSession._1();
		DateFormat freshDateFormat;
		try {
			freshDateFormat = ((DateStringifier)DATE_STRINGIFIER).getDateFormat().getClass().newInstance();
		} catch (InstantiationException e) {
			return true;
		} catch (IllegalAccessException e) {
			return true;
		}
		String actual = formatWithProposedValues(
				freshDateFormat,
				sampleDateForTestingWhetherChangesWereMadeInThisSession,
				false);
		return !expected.equals(actual);
	}
	
}
