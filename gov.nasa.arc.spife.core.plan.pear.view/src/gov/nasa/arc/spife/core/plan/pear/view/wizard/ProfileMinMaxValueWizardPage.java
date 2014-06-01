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
package gov.nasa.arc.spife.core.plan.pear.view.wizard;

import gov.nasa.arc.spife.core.plan.pear.view.internal.Messages;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.common.ui.type.editor.StringTypeEditor;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class ProfileMinMaxValueWizardPage extends EnsembleWizardPage {

	private static final int DEFAULT_DIALOG_WIDTH = 500;
	private static final int DESCRIPTION_INDENT = 10;
	private static final int DEFAULT_LABEL_LENGTH = DEFAULT_DIALOG_WIDTH - DESCRIPTION_INDENT;
	private static final int FIELD_INDENT = 20;
	private static final int FIELD_HINT_LENGTH = 100;

	private EPlan plan;
	private EDataType datatype;

	private Text unitText;
	private Text minText;
	private Text maxText;
	
	private Combo datapathCombo;


	private GridDataFactory unitGridDataFactory;
	private GridDataFactory numericGridDataFactory;
	private GridDataFactory timeGridDataFactory;
	private GridDataFactory datapathGridDataFactory;
	private Composite composite;
	private Composite numericComposite;
	private Composite timeComposite;
	private Composite unitComposite;
	
	private StringTypeEditor timeStart;
	private StringTypeEditor timeEnd;
	
	public ProfileMinMaxValueWizardPage(EPlan plan) {
		super("wizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ProfileMinMaxValueWizardPage_Title);
		setMessage(Messages.ProfileMinMaxValueWizardPage_UnitMinMaxDatapathDescription);
		
		this.plan = plan;
	}

	@Override
	public void createControl(Composite parent) {

		composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.applyTo(composite);
		 
		unitComposite = createUnitControl(composite);
		createDescription(composite);
		numericComposite = createNumericControl(composite);
		timeComposite = createTimeControl(composite);
		createDatapathControl(composite);
		
		setControl(composite);
	}
	
	
	private void createDescription(Composite parent) {
		final Label descriptionLabel = new Label(parent, SWT.WRAP);
		descriptionLabel.setText(Messages.ProfileMinMaxValueWizardPage_DescriptionLabel_Description);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 10)
		 							  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							  .grab(true, false)
		 							  .applyTo(descriptionLabel);
	}
	
	private Composite createUnitControl(Composite parent) {

		Composite unitComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.numColumns(3)
										.applyTo(unitComposite);
		unitGridDataFactory = GridDataFactory.fillDefaults();
		unitGridDataFactory.applyTo(unitComposite);
		
		final Label UnitDescriptionLabel = new Label(unitComposite, SWT.WRAP);
		UnitDescriptionLabel.setText(Messages.ProfileMinMaxValueWizardPage_UnitDescription);
		GridDataFactory.fillDefaults().span(3, 1)
									  .indent(DESCRIPTION_INDENT, 10)
		 							  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							  .grab(true, false)
		 							  .applyTo(UnitDescriptionLabel);
		
		// Unit
		final Label unitLabel = new Label(unitComposite, SWT.NONE);
		unitLabel.setText(Messages.ProfileMinMaxValueWizardPage_UnitLabel);
		GridDataFactory.fillDefaults().indent(FIELD_INDENT, 5)
									  .applyTo(unitLabel);

		unitText = new Text(unitComposite, SWT.BORDER | SWT.SINGLE);
		unitText.addModifyListener(new DefaultModifyListener());
		GridDataFactory.swtDefaults().indent(5, 5)
									 .hint(FIELD_HINT_LENGTH, SWT.DEFAULT)
									 .applyTo(unitText);
		
		final Link siLink = new Link(unitComposite, SWT.NONE);
		siLink.setText("<a>" + Messages.ProfileMinMaxValueWizardPage_UnitLinkLabel + "</a>");
		siLink.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				org.eclipse.swt.program.Program.launch(Messages.ProfileMinMaxValueWizardPage_UnitLinkDestination);
			}
		});
		GridDataFactory.fillDefaults().indent(5, 5)
									  .applyTo(siLink);
		
		unitText.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				validateUnit(true);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// Do nothing
			}
		});
		
		return unitComposite;
	}
	
	private Composite createNumericControl(Composite parent) {

		Composite numericComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.numColumns(2)
										.applyTo(numericComposite);
		numericGridDataFactory = GridDataFactory.fillDefaults();
		numericGridDataFactory.applyTo(numericComposite);
		
		// Min
		final Label nameLabel = new Label(numericComposite, SWT.NONE);
		nameLabel.setText(Messages.ProfileMinMaxValueWizardPage_MinLabel);
		GridDataFactory.fillDefaults().indent(FIELD_INDENT, 5)
									  .applyTo(nameLabel);

		minText = new Text(numericComposite, SWT.BORDER | SWT.SINGLE);
		minText.addModifyListener(new DefaultModifyListener());
		GridDataFactory.swtDefaults().indent(5, 5)
									 .hint(FIELD_HINT_LENGTH, SWT.DEFAULT)
									 .applyTo(minText);

		// Max
		final Label maxLabel = new Label(numericComposite, SWT.NONE);
		maxLabel.setText(Messages.ProfileMinMaxValueWizardPage_MaxLabel);
		GridDataFactory.fillDefaults().indent(FIELD_INDENT, SWT.DEFAULT)
									  .applyTo(maxLabel);

		maxText = new Text(numericComposite, SWT.BORDER | SWT.SINGLE);
		maxText.addModifyListener(new DefaultModifyListener());
		GridDataFactory.swtDefaults().indent(5, SWT.DEFAULT)
									 .hint(FIELD_HINT_LENGTH, SWT.DEFAULT)
									 .applyTo(maxText);
		
		return numericComposite;
	}
	
	private Composite createTimeControl(Composite parent) {
		Composite timeComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.applyTo(timeComposite);
		timeGridDataFactory = GridDataFactory.fillDefaults();
		timeGridDataFactory.applyTo(timeComposite);
		
		TemporalExtent extent = PlanEditorUtil.getPlanExportExtentPropertyValue(plan);
		timeStart = createDateTimeEditor(timeComposite, Messages.ProfileMinMaxValueWizardPage_StartTimeLabel, extent.getStart());
		GridDataFactory.swtDefaults().indent(10, 5)
									 .hint(FIELD_HINT_LENGTH, SWT.DEFAULT)
									 .applyTo(timeStart.getEditorControl());
		timeEnd = createDateTimeEditor(timeComposite, Messages.ProfileMinMaxValueWizardPage_EndTimeLabel, extent.getEnd());
		GridDataFactory.swtDefaults().indent(7, 5)
									 .hint(FIELD_HINT_LENGTH, SWT.DEFAULT)
									 .applyTo(timeEnd.getEditorControl());
		
		KeyListener dateSequenceKeyListener = new KeyListener() {

			@Override
			public void keyReleased(KeyEvent ke) {
				try {
					Date start = (Date) timeStart.getObjectRaw();
					Date end = (Date) timeEnd.getObjectRaw();
					clearError(ProfileMinMaxValueWizardPage.class);
					
					if (start == null || end == null) {
						return;
					}
					if (!start.before(end)) {
						setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_EndTimeLaterThanStartTime_Error_Message);
					} else {
						clearError(ProfileMinMaxValueWizardPage.class);
					}
				} catch (ParseException e1) {
					// The format is incorrect
					setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_TimeFormatInvalid_Error_Message);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// inspect on keyReleased event instead.
			}
		};
		
		timeStart.getEditorControl().addKeyListener(dateSequenceKeyListener);
		timeEnd.getEditorControl().addKeyListener(dateSequenceKeyListener);
		
		return timeComposite;
	}
	
	private Composite createDatapathControl(Composite parent) {
		
		final Label DatapathDescriptionLabel = new Label(parent, SWT.WRAP);
		DatapathDescriptionLabel.setText(Messages.ProfileMinMaxValueWizardPage_DatapathDescription);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 10)
		 							  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							  .grab(true, false)
		 							  .applyTo(DatapathDescriptionLabel);
		
		

		Composite datapathComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.numColumns(3)
										.applyTo(datapathComposite);
		datapathGridDataFactory = GridDataFactory.fillDefaults();
		datapathGridDataFactory.applyTo(datapathComposite);
		
		// Datapath:
		final Label datapathLabel = new Label(datapathComposite, SWT.NONE);
		datapathLabel.setText(Messages.ProfileMinMaxValueWizardPage_DatapathLabel);
		GridDataFactory.fillDefaults().indent(FIELD_INDENT, 5)
									  .applyTo(datapathLabel);

		datapathCombo = new Combo(datapathComposite, SWT.NONE);
		GridDataFactory.swtDefaults().indent(5, 5)
									 .applyTo(datapathCombo);
		
		datapathCombo.setItems(fetchDatapathValuesFromAD());

		return datapathComposite;
	}
	
	private String[] fetchDatapathValuesFromAD() {
		ArrayList<String> datapathComboItems = new ArrayList<String>();
		List<EDataType> list = ActivityDictionary.getInstance().getDefinitions(EDataType.class);
		for (EDataType eDataType : list) {
			if (eDataType.getName().equals("DATAPATH")) {
				EList<EObject> eContents = eDataType.eContents();
				for (EObject eObject : eContents) {
					datapathComboItems.add(eObject.toString());
				}
			}
		}
		return datapathComboItems.toArray(new String[datapathComboItems.size()]);
	}
	
	
	private StringTypeEditor createDateTimeEditor(Composite parent, String string, Date initialValue) {
		Composite dateTimeComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 15)
										.numColumns(2)
										.applyTo(dateTimeComposite);
		GridDataFactory.fillDefaults().applyTo(dateTimeComposite);

		IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);

		Label label = new Label(dateTimeComposite, SWT.NONE);
		label.setText(string);
		GridDataFactory.fillDefaults().indent(FIELD_INDENT, 5)
									  .applyTo(label);
		final StringTypeEditor editor = new StringTypeEditor(dateStringifier, dateTimeComposite, initialValue, true);
		return editor;
	}
	
	public String getMin() {
		String min = ""; //$NON-NLS-1$
		if (datatype == EcorePackage.Literals.EINTEGER_OBJECT
				|| datatype == EcorePackage.Literals.EDOUBLE_OBJECT) {
			min = minText.getText().trim();
		}
		else if(datatype == JSciencePackage.Literals.EDURATION) {
			min = minText.getText().trim();
			long l = DurationFormat.parseDurationFromHumanInput(min);
			min = ""+l; //$NON-NLS-1$
		}
		else if(datatype == EcorePackage.Literals.EDATE) { // AKA TIME
			Date date = (Date) timeStart.getObject();
			if(date != null) {
				min = ISO8601DateFormat.formatISO8601(date);
			}
		}

		return (min.isEmpty() ? null : min);
	}
	public String getMax() {
		String max = ""; //$NON-NLS-1$
		if (datatype == EcorePackage.Literals.EINTEGER_OBJECT
				|| datatype == EcorePackage.Literals.EDOUBLE_OBJECT) {
			max = maxText.getText().trim();
		}
		else if(datatype == JSciencePackage.Literals.EDURATION) {
			max = minText.getText().trim();
			long l = DurationFormat.parseDurationFromHumanInput(max);
			max = ""+l; //$NON-NLS-1$
		}
		else if(datatype == EcorePackage.Literals.EDATE) { // AKA TIME
			Date date = (Date) timeEnd.getObject();
			if(date != null) {
				max = ISO8601DateFormat.formatISO8601(date);
			}
		}

		return (max.isEmpty() ? null : max);
	}
	
	public Unit<? extends Quantity> getUnit() {
		String enteredUnit = ""; //$NON-NLS-1$
		if (datatype == EcorePackage.Literals.EINTEGER_OBJECT
				|| datatype == EcorePackage.Literals.EDOUBLE_OBJECT) {
			enteredUnit = unitText.getText().trim();
		} 
	
		if (!enteredUnit.isEmpty()) {
			Unit<? extends Quantity> unit = null;
			try {
				unit = Unit.valueOf(enteredUnit);
			} catch (IllegalArgumentException e) {
				return null;
			}
			return unit;
		}
		return null;
	}
	
	public String getUnitLiteral() {
		return unitText.getText().trim();
	}
	
	public String getDatapath() {
		String datapath = ""; //$NON-NLS-1$
		int selectedIndex = datapathCombo.getSelectionIndex();
		if (selectedIndex == -1) {
			return null;
		}
		datapath = datapathCombo.getItem(selectedIndex);
		return (datapath.isEmpty() ? null : datapath);
	}
	
	public void setDataType(EDataType datatype) {
		
		if(this.datatype == datatype) {
			// No change in datatype
			return;
		}
		
		this.datatype = datatype;
		
		// Hide/Show relevant controls
		if(datatype == EcorePackage.Literals.EDATE) { // AKA TIME
			numericComposite.setVisible(false);
			timeComposite.setVisible(true);
			
			numericGridDataFactory.exclude(true).applyTo(numericComposite);
			timeGridDataFactory.exclude(false).applyTo(timeComposite);
		}
		else {
			// All other datatypes use the generic numeric composite
			numericComposite.setVisible(true);
			timeComposite.setVisible(false);
			
			numericGridDataFactory.exclude(false).applyTo(numericComposite);
			timeGridDataFactory.exclude(true).applyTo(timeComposite);
		}
		
		// Hide/Show unit field
		if(datatype == EcorePackage.Literals.EINTEGER_OBJECT || datatype == EcorePackage.Literals.EDOUBLE_OBJECT) {
			unitComposite.setVisible(true);
			unitGridDataFactory.exclude(false).applyTo(unitComposite);
			setMessage(Messages.ProfileMinMaxValueWizardPage_UnitMinMaxDatapathDescription);
		} else {
			unitComposite.setVisible(false);
			unitGridDataFactory.exclude(true).applyTo(unitComposite);
			setMessage(Messages.ProfileMinMaxValueWizardPage_MinMaxDatapathDescription);
		}
		
		
		// Clears previous data
		clearData();
		//update layout
		composite.layout();
	}
	
	private void clearData() {
		unitText.setText("");

		minText.setText("");
		maxText.setText("");

		timeStart.getEditorControl().setText("");
		timeEnd.getEditorControl().setText("");
		
		datapathCombo.clearSelection();
	}
	
	/**
	 * Checks if the Minimum and Maximum values are valid.
	 * @return
	 */
	private boolean validate() {
		boolean result = true;
		if(datatype == EcorePackage.Literals.EINTEGER_OBJECT) {
			result = validateInteger();
			result &= validateUnit(false);
		}
		else if(datatype == EcorePackage.Literals.EDOUBLE_OBJECT) {
			result = validateDouble();
			result &= validateUnit(false);
		}
		else if(datatype == JSciencePackage.Literals.EDURATION) {
			result = validateDuration();
		}
		return result;
	}
	
	private boolean validateInteger() {
		String min = getMin();
		String max = getMax();
		
		if(min != null) {
			boolean valid = isInteger(min);
			if(!valid) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MinIntegerValueInvalid_Error_Message);
				return false;
			}
		}
		
		if(max != null) {
			boolean valid = isInteger(max);
			if(!valid) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MaxIntegerValueInvalid_Error_Message);
				return false;
			}
		}
		
		if(min!=null && max!=null) {
			int minValue = Integer.parseInt(min);
			int maxValue = Integer.parseInt(max);
			if(minValue > maxValue) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MinGreaterThanMaxValue_Error_Message);
				return false;
			}
		}
		return true;
	}
	
	private boolean validateDouble() {
		String min = getMin();
		String max = getMax();
		
		if(min != null) {
			boolean valid = isDouble(min);
			if(!valid) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MinDoubleInvalid_Error_Message);
				return false;
			}
		}
		
		if(max != null) {
			boolean valid = isDouble(max);
			if(!valid) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MaxDoubleInvalid_Error_Message);
				return false;
			}
		}
		
		if(min!=null && max!=null) {
			double minValue = Double.parseDouble(min);
			double maxValue = Double.parseDouble(max);
			if(minValue > maxValue) {
				setError(ProfileMinMaxValueWizardPage.class, Messages.ProfileMinMaxValueWizardPage_MinGreaterThanMaxValue_Error_Message);
				return false;
			}
		}
		return true;
	}

	/**
	 * Don't update the text entry on every pageUpdated validation attempt, as this will complicate entering, for example, "bit/s"
	 * @param updateText  
	 * @return
	 */
	public boolean validateUnit(boolean updateText) {
		String enteredText = unitText.getText().trim();
		
		try {
			String standardizedText = Unit.valueOf(enteredText).toString();
			if (!standardizedText.equals(enteredText)) {
				// normalize the text field if necessary
				if (updateText) {
					unitText.setText(standardizedText);
				}
			}
			return true;
		} catch (IllegalArgumentException e) {
			setError(ProfileMinMaxValueWizardPage.class, "\"" +enteredText +"\" is not a valid SI unit. Please enter a valid unit (or leave it blank)");
			return false;
		}
	}
	
	private boolean validateDuration() {
		String min = minText.getText().trim();
		String max = maxText.getText().trim();
		
		try {
			if(min!=null) {
				DurationFormat.parseDurationFromHumanInput(min);
			}
		}
		catch(NumberFormatException nfe) {
			setError(ProfileMinMaxValueWizardPage.class, 
					NLS.bind(Messages.ProfileMinMaxValueWizardPage_MinDuration_Error_Message, nfe.getMessage()));
			return false;
		}
		
		try {
			if(max!=null) {
				DurationFormat.parseDurationFromHumanInput(max);
			}
		}
		catch(NumberFormatException nfe) {
			setError(ProfileMinMaxValueWizardPage.class, 
					NLS.bind(Messages.ProfileMinMaxValueWizardPage_MaxDuration_Error_Message, nfe.getMessage()));
			return false;
		}

		return true;
	}
	
	private boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	private boolean isDouble(String d) {
		try {
			Double.parseDouble(d);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	@Override
	protected void pageUpdated() {
		clearError(ProfileMinMaxValueWizardPage.class);
		boolean result = validate();
		this.setPageComplete(result);
	}
}
