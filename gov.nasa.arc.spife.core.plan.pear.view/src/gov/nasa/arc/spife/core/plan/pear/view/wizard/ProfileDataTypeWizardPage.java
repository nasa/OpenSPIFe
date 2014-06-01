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
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

public class ProfileDataTypeWizardPage extends EnsembleWizardPage {

	public static final String CLAIM_MAX_VALUE = "1"; //$NON-NLS-1$
	
	private static final int DEFAULT_DIALOG_WIDTH = 500;
	private static final int DESCRIPTION_INDENT = 30;
	private static final int DEFAULT_LABEL_LENGTH = DEFAULT_DIALOG_WIDTH - DESCRIPTION_INDENT;

	private Map<String,EDataType> enumMap;
	private boolean isActivityResource;
	
	private Label claimDescriptionLabel;
	private Button claimButton; // Claim selection implies data type = Integer and max = 1.
	private Button booleanButton;
	private Button doubleButton;
	private Button integerButton;
	private GridDataFactory claimButtonGridDataFactory;
	private GridDataFactory claimDescriptionLabelGridDataFactory;

	private Button stringButton;
	private Button enumerationButton;
	private Combo enumerationCombo;
	private Button durationButton;
	private Button timeButton;
	
	private GridDataFactory advGridDataFactory;
	private Composite composite;
	private Group advancedComposite;
	private ExpandableComposite expComposite;
	private Composite[] radioComposites;
	
	public ProfileDataTypeWizardPage() {
		super("wizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.ProfileDataTypeWizardPage_Title);
		setMessage(Messages.ProfileDataTypeWizardPage_Description);
		
		// Initialize the enum list values for the Defined Enumerations
		List<EDataType> list = ActivityDictionary.getInstance().getDefinitions(EDataType.class);
		enumMap = new HashMap<String, EDataType>();
		for(EDataType eenum: list) {
			enumMap.put(eenum.getName(), eenum);
		}
	}

	@Override
	public void createControl(Composite parent) {
		
		 composite = new Composite(parent, SWT.NONE);
		 GridLayoutFactory.fillDefaults().spacing(0, 10)
		 								 .applyTo(composite);

		 createDataTypes(composite);
		 createAdvancedContents(composite);
		 
		 // Make sure all radio buttons are in the same group and react to each other
		 radioComposites = new Composite[] {composite, advancedComposite};
		 
		 // Set the tab lists
		 composite.setTabList(new Control[]{claimButton,booleanButton,doubleButton,integerButton,expComposite,advancedComposite});
		 advancedComposite.setTabList(new Control[]{stringButton,enumerationButton,enumerationCombo,durationButton,timeButton});
		 
		 setControl(composite);
	}
	
	private void createDataTypes(Composite composite) {
		
		 // Claim
		 claimButton = new Button(composite, SWT.RADIO);
		 claimButton.setText(Messages.ProfileDataTypeWizardPage_ClaimButton);
		 claimButton.addListener(SWT.Selection, radioGroupListener);
		 claimButtonGridDataFactory = GridDataFactory.fillDefaults().indent(0, 5);
		 claimButtonGridDataFactory.applyTo(claimButton);
		 
		 claimDescriptionLabel = new Label(composite, SWT.WRAP);
		 claimDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_ClaimLabel_Description);
		 claimDescriptionLabelGridDataFactory = GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   								.hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   								.grab(true, false);
		 claimDescriptionLabelGridDataFactory.applyTo(claimDescriptionLabel);
		 
		 // Boolean
		 booleanButton = new Button(composite, SWT.RADIO);
		 booleanButton.setText(Messages.ProfileDataTypeWizardPage_BooleanButton);
		 booleanButton.setSelection(true);
		 booleanButton.addListener(SWT.Selection, radioGroupListener);
		 GridDataFactory.fillDefaults().indent(0, 5)
									   .applyTo(booleanButton);
		 
		 final Label booleanDescriptionLabel = new Label(composite, SWT.WRAP);
		 booleanDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_BooleanLable_Message);
		 GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   .grab(true, false)
		 							   .applyTo(booleanDescriptionLabel);
		 
		 // Double
		 doubleButton = new Button(composite, SWT.RADIO);
		 doubleButton.setText(Messages.ProfileDataTypeWizardPage_DoubleButton);
		 doubleButton.addListener(SWT.Selection, radioGroupListener);
		 GridDataFactory.fillDefaults().indent(0, 5)
									   .applyTo(doubleButton);
		 
		 final Label doubleDescriptionLabel = new Label(composite, SWT.WRAP);
		 doubleDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_DoubleLabel_Description);
		 GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   .grab(true, false)
		 							   .applyTo(doubleDescriptionLabel);
		 
		 // Integer
		 integerButton = new Button(composite, SWT.RADIO);
		 integerButton.setText(Messages.ProfileDataTypeWizardPage_IntegerButton);
		 integerButton.addListener(SWT.Selection, radioGroupListener);
		 GridDataFactory.fillDefaults().indent(0, 5)
									   .applyTo(integerButton);
		 
		 final Label integerDescriptionLabel = new Label(composite, SWT.WRAP);
		 integerDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_IntegerLabel_Description);
		 GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
		 							   .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
		 							   .grab(true, false)
		 							   .applyTo(integerDescriptionLabel);
	}

	/**
	 * Upon expanding and hiding the advanced controls will adjust accordingly.
	 * 
	 * @param e
	 */
	private void updateAdvancedArea(ExpansionEvent e) {
				
		// Set the visibility of the advanced section
		advancedComposite.setVisible(e.getState());
		// We want to exclude the advanced composite if it is not visible
		advGridDataFactory.exclude(!e.getState()).applyTo(advancedComposite);
		
		// Set Boolean selection when advanced is hidden
		if(!e.getState() && isAdvancedRadioSelected()) {
			clearSelections();
			booleanButton.setSelection(true);
		}
		
		// Resize and update layout
		refreshLayout();
	}
	
	/**
	 * The Advanced control section.
	 * 
	 * @param composite
	 * @param label
	 * @param nColumns
	 */
	private void createAdvancedContents(Composite composite) {
		
		expComposite = new ExpandableComposite(composite, SWT.NONE, 
								ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT | SWT.NO_FOCUS);
		expComposite.setText(Messages.ProfileDataTypeWizardPage_AdvancedTwistie);
		expComposite.setExpanded(false);
		expComposite.addExpansionListener(new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent event) {

				// Update Advanced Components
				updateAdvancedArea(event);
			}
		});
		GridDataFactory.fillDefaults().grab(true, false)
									  .applyTo(expComposite);
		
		advancedComposite = new Group(composite, SWT.BORDER);
		advancedComposite.setVisible(false);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(5, 5).applyTo(advancedComposite);
		advGridDataFactory = GridDataFactory.fillDefaults()
									  // exclude this because it is hidden by default and will mess up
									  // the wizard sizing.
									  .exclude(true)
									  .indent(SWT.DEFAULT, -10);
		advGridDataFactory.applyTo(advancedComposite);
		
		// String
		stringButton = new Button(advancedComposite, SWT.RADIO);
		stringButton.setText(Messages.ProfileDataTypeWizardPage_StringButton);
		stringButton.addListener(SWT.Selection, radioGroupListener);
		GridDataFactory.fillDefaults().indent(0, 5)
									  .span(2, 1)
									  .applyTo(stringButton);
		
		final Label stringDescriptionLabel = new Label(advancedComposite, SWT.WRAP);
		stringDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_StringLabel_Description);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
									  .span(2, 1)
									  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
									  .grab(true, false)
									  .applyTo(stringDescriptionLabel);
		
		// Defined Enumeration
		enumerationButton = new Button(advancedComposite, SWT.RADIO);
		enumerationButton.setText(Messages.ProfileDataTypeWizardPage_DefinedEnumerationButton);
		enumerationButton.addListener(SWT.Selection, radioGroupListener);
		GridDataFactory.fillDefaults().indent(0, 5)
									  .span(1, 1)
									  .applyTo(enumerationButton);
		
		enumerationCombo = new Combo(advancedComposite, SWT.READ_ONLY);
		enumerationCombo.addModifyListener(new DefaultModifyListener());
		enumerationCombo.setEnabled(false);
		GridDataFactory.swtDefaults().indent(15, 5)
									 .hint(250, SWT.DEFAULT)
		 							 .applyTo(enumerationCombo);
		String[] enums = enumMap.keySet().toArray(new String[0]);
		Arrays.sort(enums);
		enumerationCombo.setItems(enums);
		enumerationCombo.select(0);

		final Label definedEnumerationLabel = new Label(advancedComposite, SWT.WRAP);
		definedEnumerationLabel.setText(Messages.ProfileDataTypeWizardPage_DefinedEnumerationLabel_Description);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
									  .span(2, 1)
									  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
									  .grab(true, false)
									  .applyTo(definedEnumerationLabel);
		
		// Duration
		durationButton = new Button(advancedComposite, SWT.RADIO);
		durationButton.setText(Messages.ProfileDataTypeWizardPage_DurationButton);
		durationButton.addListener(SWT.Selection, radioGroupListener);
		GridDataFactory.fillDefaults().indent(0, 5)
									  .span(2, 1)
									  .applyTo(durationButton);
		
		final Label durationDescriptionLabel = new Label(advancedComposite, SWT.WRAP);
		durationDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_DurationButtonLabel_Description);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
									  .span(2, 1)
									  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
									  .grab(true, false)
									  .applyTo(durationDescriptionLabel);
		
		// Time
		timeButton = new Button(advancedComposite, SWT.RADIO);
		timeButton.setText(Messages.ProfileDataTypeWizardPage_TimeButton);
		timeButton.addListener(SWT.Selection, radioGroupListener);
		GridDataFactory.fillDefaults().indent(0, 5)
									  .span(2, 1)
									  .applyTo(stringButton);
		
		final Label timeDescriptionLabel = new Label(advancedComposite, SWT.WRAP);
		timeDescriptionLabel.setText(Messages.ProfileDataTypeWizardPage_TimeLabel_Description);
		GridDataFactory.fillDefaults().indent(DESCRIPTION_INDENT, 0)
									  .span(2, 1)
									  .hint(DEFAULT_LABEL_LENGTH, SWT.DEFAULT)
									  .grab(true, false)
									  .applyTo(timeDescriptionLabel);
	}
	
	
	
	protected boolean isAdvancedRadioSelected() {
		return stringButton.getSelection() || enumerationButton.getSelection()
				|| durationButton.getSelection() || timeButton.getSelection();
	}

	
	public boolean isClaimSelected() {
		return claimButton.getSelection();
	}
	
	public boolean isExternalCondition() {
		return booleanButton.getSelection();
	}
	
	/**
	 * This listener makes sure that all radios on the wizard page are grouped
	 * and reacts together, even though there are in separate Composites.
	 */
	private Listener radioGroupListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			// clear previous selections
			clearSelections();
			Button button = (Button) event.widget;
			button.setSelection(true);

			// Enable Enumeration combo if the radio button is selected
			enumerationCombo.setEnabled(button == enumerationButton);
			
			getWizard().getContainer().updateButtons();
		}
	};
	
	/**
	 * Clears any radio selection
	 */
	private void clearSelections() {
		for (int i = 0; i < radioComposites.length; i++) {
			Composite composite = radioComposites[i];
			Control[] children = composite.getChildren();
			for (int j = 0; j < children.length; j++) {
				Control child = children[j];
				if (child instanceof Button) {
					Button button = (Button) child;
					if ((button.getStyle() & SWT.RADIO) != 0)
						button.setSelection(false);
				}
			}
		}
	}

	/**
	 * Show the Claim option only if the Profile is an Activity Resource.
	 * 
	 * @param isActivityResource
	 */
	public void setActivityResource(boolean isActivityResource) {
		
		this.isActivityResource = isActivityResource;
		
		if(claimButton != null) {
			claimButtonGridDataFactory.exclude(!isActivityResource).applyTo(claimButton);
			claimButton.setVisible(isActivityResource);
		}
		if(claimDescriptionLabel != null) {
			claimDescriptionLabelGridDataFactory.exclude(!isActivityResource).applyTo(claimDescriptionLabel);
			claimDescriptionLabel.setVisible(isActivityResource);
		}

		// If the claim button WAS selected and the claim option is NOW hidden
		// (since the user went back and set the profile to an External Condition data type),
		// we want to set the default selection to Boolean.
		if (!isActivityResource && claimButton.getSelection()) {
			claimButton.setSelection(false);
			// Set new default to Boolean
			booleanButton.setSelection(true);
		}
		
		// Resize and update layout
		refreshLayout();
	}
	
	/**
	 * Refresh and adjust the dialog and the controls.
	 */
	private void refreshLayout() {
		
		// Ask the receiver to lay out its controls
		composite.layout();
		
		// Adjust the wizard page to fit the visible controls
		Shell shell = getShell();
		shell.setSize(shell.computeSize(shell.getSize().x, SWT.DEFAULT));
	}
	
	
	public EDataType getDataType() {
		if(booleanButton.getSelection()) {
			return EcorePackage.Literals.EBOOLEAN_OBJECT;
		}
		else if(doubleButton.getSelection()) {
			return EcorePackage.Literals.EDOUBLE_OBJECT;
		}
		else if(integerButton.getSelection() || claimButton.getSelection()) {
			return EcorePackage.Literals.EINTEGER_OBJECT;
		}
		else if(stringButton.getSelection()) {
			return EcorePackage.Literals.ESTRING;
		}
		else if(enumerationButton.getSelection()) {
			String enumName = enumerationCombo.getText();
			return enumMap.get(enumName);
		}
		else if(durationButton.getSelection()) {
			return JSciencePackage.Literals.EDURATION;
		}
		else if(timeButton.getSelection()) {
			return EcorePackage.Literals.EDATE;
		}
		return null;
	}
	
	public String getDataTypeLiteral() {
		if(booleanButton.getSelection()) {
			return "boolean";
		}
		else if(doubleButton.getSelection()) {
			return "double";
		}
		else if(integerButton.getSelection() || claimButton.getSelection()) {
			return "integer";
		}
		else if(stringButton.getSelection()) {
			return "string";
		}
		else if(enumerationButton.getSelection()) {
			String enumName = enumerationCombo.getText();
			return enumName;
		}
		else if(durationButton.getSelection()) {
			return "duration";
		}
		else if(timeButton.getSelection()) {
			return "date";
		}
		return "";
	}
	
	public boolean canFinish() {
		return !isActivityResource
				|| (!doubleButton.getSelection() && !integerButton.getSelection()
						&& !durationButton.getSelection() && !timeButton.getSelection());
	}
	
	@Override
	public boolean canFlipToNextPage() {
		// Allows to flip to Max/Min page if the Profile is an Activity Resource
		// with datatype of Double/Integer/Duration/Time.
		return isActivityResource
				&& (doubleButton.getSelection() || integerButton.getSelection()
						|| durationButton.getSelection() || timeButton.getSelection());
	}
}
