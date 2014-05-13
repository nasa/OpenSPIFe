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
package gov.nasa.arc.spife.europa.preferences;

import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

public class EuropaPreferencePage extends AbstractPreferencePage {

	private static final String P_DOMAIN = "gov.nasa.arc.spife.europa";
	/* package */ static final String P_CONNECTION_TYPE = P_DOMAIN + ".connectiontype";  // JNI vs xml-rpc
	/* package */ static final String P_SERVER_TYPE = P_DOMAIN + ".servertype";  // in-process vs out-of-process server
	/* package */ static final String P_ENGINE_TYPE = P_DOMAIN + ".enginetype";  // normally used to experiment in development, examples are standard/dual/grounded 
	/* package */ static final String P_HOST         = P_DOMAIN + ".host";        // Europa host name
	/* package */ static final String P_PORT         = P_DOMAIN + ".port";        // Europa port number
	/* package */ static final String P_MAX_STEPS    = P_DOMAIN + ".maxsteps";    // Max number of steps
	/* package */ static final String P_STRICT_TYPES = P_DOMAIN + ".stricttypes"; // Whether or not to use strict type checking
	/* package */ static final String P_MODEL_NAME   = P_DOMAIN + ".modelname";   // Name of model to use
	/* package */ static final String P_FIND_TEMPORAL_VIOLATIONS = P_DOMAIN + ".findtemporalviolations"; // Whether or not to find temporal violations
	/* package */ static final String P_FIND_FLIGHT_RULE_VIOLATIONS = P_DOMAIN + ".findflightruleviolations"; // Whether or not to find flight rule violations
	/* package */ static final String P_FIND_SHARABLE_VIOLATIONS = P_DOMAIN + ".findsharableviolations"; // Whether or not to find sharable violations
	/* package */ static final String P_FIND_CLAIMABLE_VIOLATIONS = P_DOMAIN + ".findclaimableviolations"; // Whether or not to find claimable violations
	/* package */ static final String P_FIND_NUMERIC_VIOLATIONS = P_DOMAIN + ".findnumericviolations"; // Whether or not to find numeric violations
	/* package */ static final String P_FIX_VIOLATIONS_PROGRESSIVELY = P_DOMAIN + ".fixviolationsprogressively"; // Whether or not to fix violations progressively
	/* package */ static final String P_USE_EUROPA_VIOLATIONS = P_DOMAIN + ".useeuropaviolations"; // Whether or not to use europa's violation checking and fixing
	/* package */ static final String P_USE_RESOURCE_SOLVING = P_DOMAIN + ".useresourcesolving"; // Whether or not to use resource solving when generating nddl model
	/* package */ static final String P_TRANSLATE_NUMERIC_RESOURCES = P_DOMAIN + ".translatenumericresources"; // Whether or not to trasnlate numeric resources when generating nddl model
	/* package */ public static final String P_DO_MODEL_AUTOEXPORT = P_DOMAIN + ".domodelautoexport";
	
	private Group connectionGroup;
	private Group findingGroup;
	
	private RadioGroupFieldEditor connectionTypeEditor;
	private RadioGroupFieldEditor serverTypeEditor;
	private StringFieldEditor hostEditor;
	private StringFieldEditor portEditor;
	private StringFieldEditor engineTypeEditor;
	
	private BooleanFieldEditor useEuropaViolationsEditor;
	private BooleanFieldEditor temporalEditor;
	private BooleanFieldEditor flightRuleEditor;
	private BooleanFieldEditor sharableEditor;
	private BooleanFieldEditor claimableEditor;
	private BooleanFieldEditor numericEditor;
	
	public EuropaPreferencePage() {
		super(EuropaPlugin.getDefault().getPreferenceStore());
		setDescription("Europa Preference Page");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		
		connectionGroup = createGroup(parent);
		connectionGroup.setText("Connection And Server Type");
		
		connectionTypeEditor = new RadioGroupFieldEditor(
				P_CONNECTION_TYPE,
			    "Connection &Type:",
			    2,
			    new String[][] {
					{"JNI","JNI"},
					{"xml-rpc","xml-rpc"}
				},
			    connectionGroup);
		addField(connectionTypeEditor);

		serverTypeEditor = new RadioGroupFieldEditor(
				P_SERVER_TYPE,
			    "&Server Type:",
			    2,
			    new String[][] {
					{"Embedded","embedded"},
					{"Out-of-process","remote"}
				},
			    connectionGroup);
		addField(serverTypeEditor);
		
		hostEditor = new StringFieldEditor(
				P_HOST,
			    "Fully Qualified &Host Name:",
			    connectionGroup);
		addField(hostEditor);
		
		portEditor = new IntegerFieldEditor(
				P_PORT,
			    "Default &Port Number:",
			    connectionGroup);
		addField(portEditor);
		
		engineTypeEditor = new StringFieldEditor(
				P_ENGINE_TYPE,
			    "Engine Type:",
			    connectionGroup);
		addField(engineTypeEditor);
		
		findingGroup = createGroup(parent);
		findingGroup.setText("Finding violations");
		addField(useEuropaViolationsEditor = new BooleanFieldEditor(
				P_USE_EUROPA_VIOLATIONS,
				"Use &Europa violation checking and fixing",
				findingGroup));
		addField(temporalEditor = new BooleanFieldEditor(
			P_FIND_TEMPORAL_VIOLATIONS,
			"Report temporal violations (re-open plans to take effect)",
			findingGroup));
		indent(temporalEditor, findingGroup);
		addField(flightRuleEditor = new BooleanFieldEditor(
			P_FIND_FLIGHT_RULE_VIOLATIONS,
			"Find and fix state violations (re-open plans to take effect)",
			findingGroup));
		indent(flightRuleEditor, findingGroup);
		addField(sharableEditor = new BooleanFieldEditor(
			P_FIND_SHARABLE_VIOLATIONS,
			"Find and fix sharable violations (re-open plans to take effect)",
			findingGroup));
		indent(sharableEditor, findingGroup);
		addField(claimableEditor = new BooleanFieldEditor(
			P_FIND_CLAIMABLE_VIOLATIONS,
			"Find and fix claimable violations (re-open plans to take effect)",
			findingGroup));
		indent(claimableEditor, findingGroup);
		addField(numericEditor = new BooleanFieldEditor(
			P_FIND_NUMERIC_VIOLATIONS,
			"Find and fix numeric violations (re-open plans to take effect)",
			findingGroup));
		indent(numericEditor, findingGroup);

		Group fixingGroup = createGroup(parent);
		fixingGroup.setText("Fixing violations");
		addField(new BooleanFieldEditor(
				P_FIX_VIOLATIONS_PROGRESSIVELY,
				"Fix violations &progressively:",
				BooleanFieldEditor.SEPARATE_LABEL,
				fixingGroup));
		addField(new IntegerFieldEditor(
				P_MAX_STEPS,
			    "Maximum number of steps to fix &violations:",
			    fixingGroup));
		
		Group adGroup = createGroup(parent);
		adGroup.setText("Activity Dictionary validation / Model Generation");
		addField(new StringFieldEditor(
				P_MODEL_NAME,
				"&Activity Dictionary Target Version:",
				adGroup));
		addField(new BooleanFieldEditor(
				P_STRICT_TYPES,
				"Use strict activity &type checking",
				BooleanFieldEditor.SEPARATE_LABEL,
				adGroup));		
		addField(new BooleanFieldEditor(
				P_DO_MODEL_AUTOEXPORT,
				"Do &Model Auto-export if needed",
				BooleanFieldEditor.SEPARATE_LABEL,
				adGroup));		
		addField(new BooleanFieldEditor(
				P_USE_RESOURCE_SOLVING,
				"Use Resource Solving for State Constraints",
				BooleanFieldEditor.SEPARATE_LABEL,
				adGroup));
		addField(new BooleanFieldEditor(
				P_TRANSLATE_NUMERIC_RESOURCES,
				"Translate Numeric Resources",
				BooleanFieldEditor.SEPARATE_LABEL,
				adGroup));
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		setGroupEnablement();
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		setGroupEnablement();
	}

	protected String getConnectionType() {
		// Hack to keep track of connection type, since unbelievably eclipse doesn't expose an accessor
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=129722
		Composite radioBoxControl = connectionTypeEditor.getRadioBoxControl(connectionGroup);
		for(Control c : radioBoxControl.getChildren()) {
			if (c instanceof Button) {
				Button b = (Button)c;
				if (b.getSelection())
					return b.getText();
			}
		}
		
		return null;
	}
	
	private void setGroupEnablement() {
		String connectionType = getConnectionType();
		boolean usingXmlRpc = ("xml-rpc".equals(connectionType));
		
		// Disable xml-rcp until we fix 64 bit truncation problems for time values
		if (CommonUtils.isOSArch64()) { 
			Composite radioBoxControl = connectionTypeEditor.getRadioBoxControl(connectionGroup);
			for(Control c : radioBoxControl.getChildren()) {
				if (c instanceof Button) {
					Button b = (Button)c;
					if ("xml-rpc".equals(b.getText()))
						b.setEnabled(false);
					if ("JNI".equals(b.getText()))
						b.setSelection(true);
				}
			}			
			usingXmlRpc = false;
		}
		
		if (!usingXmlRpc) {
			Composite radioBoxControl = serverTypeEditor.getRadioBoxControl(connectionGroup);
			for(Control c : radioBoxControl.getChildren()) {
				if (c instanceof Button) {
					Button b = (Button)c;
					if ("Embedded".equals(b.getText()))
						b.setSelection(true);
					else
						b.setSelection(false);
				}			
			}
		}
		
		serverTypeEditor.setEnabled(usingXmlRpc, connectionGroup);
		hostEditor.setEnabled(usingXmlRpc, connectionGroup);
		portEditor.setEnabled(usingXmlRpc, connectionGroup);
			
		Boolean enabled = useEuropaViolationsEditor.getBooleanValue();
		temporalEditor.setEnabled(enabled, findingGroup);
		flightRuleEditor.setEnabled(enabled, findingGroup);
		sharableEditor.setEnabled(enabled, findingGroup);
		claimableEditor.setEnabled(enabled, findingGroup);
		numericEditor.setEnabled(enabled, findingGroup); // not fully implemented on server side yet, but it should work for very simple cases
	}
}
