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
package gov.nasa.arc.spife.core.plan.pear.view.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "gov.nasa.arc.score.plan.profile.wizard.messages"; //$NON-NLS-1$
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
	// New Profile Wizard
	public static String NewProfileWizard_NoActivePlan_Error_Msg;
	public static String NewProfileWizard_NoActivePlan_Error_Title;
	// Create New Profile
	public static String CreateNewProfileWizardPage_ProfileNameExists_Error_Message;
	public static String CreateNewProfileWizardPage_Description_Message;
	public static String CreateNewProfileWizardPage_Existing_Button_Description;
	public static String CreateNewProfileWizardPage_ExistingCategoryNotSet_Error_Message;
	public static String CreateNewProfileWizardPage_Group_Label;
	public static String CreateNewProfileWizardPage_Name_Label;
	public static String CreateNewProfileWizardPage_NewCategory_Button_Description;
	public static String CreateNewProfileWizardPage_NewCategoryExists_Message;
	public static String CreateNewProfileWizardPage_NewCategoryNotSet_Error_Message;
	public static String CreateNewProfileWizardPage_NoProfileName_Error_Message;
	public static String CreateNewProfileWizardPage_Title;
	// Profile Source
	public static String ProfileSourceWizardPage_ActiveResourceButton_Label;
	public static String ProfileSourceWizardPage_ActiveResourceLabel_Message;
	public static String ProfileSourceWizardPage_Description;
	public static String ProfileSourceWizardPage_ExternalConditionButton_Label;
	public static String ProfileSourceWizardPage_ExternalConditionLabel_Description;
	public static String ProfileSourceWizardPage_Title;
	// Profile Data Type
	public static String ProfileDataTypeWizardPage_AdvancedTwistie;
	public static String ProfileDataTypeWizardPage_BooleanButton;
	public static String ProfileDataTypeWizardPage_BooleanLable_Message;
	public static String ProfileDataTypeWizardPage_ClaimButton;
	public static String ProfileDataTypeWizardPage_ClaimLabel_Description;
	public static String ProfileDataTypeWizardPage_DefinedEnumerationButton;
	public static String ProfileDataTypeWizardPage_DefinedEnumerationLabel_Description;
	public static String ProfileDataTypeWizardPage_Description;
	public static String ProfileDataTypeWizardPage_DoubleButton;
	public static String ProfileDataTypeWizardPage_DoubleLabel_Description;
	public static String ProfileDataTypeWizardPage_DurationButton;
	public static String ProfileDataTypeWizardPage_DurationButtonLabel_Description;
	public static String ProfileDataTypeWizardPage_IntegerButton;
	public static String ProfileDataTypeWizardPage_IntegerLabel_Description;
	public static String ProfileDataTypeWizardPage_StringButton;
	public static String ProfileDataTypeWizardPage_StringLabel_Description;
	public static String ProfileDataTypeWizardPage_TimeButton;
	public static String ProfileDataTypeWizardPage_TimeLabel_Description;
	public static String ProfileDataTypeWizardPage_Title;
	// Profile Max Min
	public static String ProfileMinMaxValueWizardPage_Title;
	public static String ProfileMinMaxValueWizardPage_UnitMinMaxDatapathDescription;
	public static String ProfileMinMaxValueWizardPage_MinMaxDatapathDescription;
	public static String ProfileMinMaxValueWizardPage_DescriptionLabel_Description;
	public static String ProfileMinMaxValueWizardPage_EndTimeLabel;
	public static String ProfileMinMaxValueWizardPage_EndTimeLaterThanStartTime_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MaxDoubleInvalid_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MaxIntegerValueInvalid_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MaxLabel;
	public static String ProfileMinMaxValueWizardPage_MinDoubleInvalid_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MinGreaterThanMaxValue_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MinIntegerValueInvalid_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MinLabel;
	public static String ProfileMinMaxValueWizardPage_StartTimeLabel;
	public static String ProfileMinMaxValueWizardPage_TimeFormatInvalid_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MinDuration_Error_Message;
	public static String ProfileMinMaxValueWizardPage_MaxDuration_Error_Message;
	public static String ProfileMinMaxValueWizardPage_UnitDescription;
	public static String ProfileMinMaxValueWizardPage_UnitLabel;
	public static String ProfileMinMaxValueWizardPage_UnitLinkLabel;
	public static String ProfileMinMaxValueWizardPage_UnitLinkDestination;
	public static String ProfileMinMaxValueWizardPage_DatapathDescription;
	public static String ProfileMinMaxValueWizardPage_DatapathLabel;



	private Messages() {
		// do nothing
	}
}
