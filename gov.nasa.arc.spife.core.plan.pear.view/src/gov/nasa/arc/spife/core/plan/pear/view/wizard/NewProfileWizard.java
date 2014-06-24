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

import gov.nasa.arc.spife.core.plan.pear.view.Activator;
import gov.nasa.arc.spife.core.plan.pear.view.internal.AddProfileOperation;
import gov.nasa.arc.spife.core.plan.pear.view.internal.Messages;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.wizard.EnsembleBasicNewResourceWizard;
import gov.nasa.ensemble.common.ui.wizard.EnsembleWizardErrorPage;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class NewProfileWizard extends EnsembleBasicNewResourceWizard {

	private ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/full/wizban/new_profile.png");//$NON-NLS-1$
	
	private static final WizardPage ERROR_PAGE = new EnsembleWizardErrorPage(Messages.NewProfileWizard_NoActivePlan_Error_Title, Messages.NewProfileWizard_NoActivePlan_Error_Msg);
	
	private CreateNewProfileWizardPage createNewProfileWizardPage;
	private ProfileSourceWizardPage newProfileSourceWizardPage;
	private ProfileDataTypeWizardPage profileDataTypeWizardPage;
	private ProfileMinMaxValueWizardPage profileMinMaxWizardPage;

	private EPlan plan;
	private PlanEditorModel model;
	
	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}

	@Override
	protected void addContentPages() {
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		model = PlanEditorModelRegistry.getCurrent(workbench);
		if (model != null) {
			this.plan = model.getEPlan();
		}
		
		if(plan != null) {
			// Active Plan and Profiles
			createNewProfileWizardPage = new CreateNewProfileWizardPage(plan);
			newProfileSourceWizardPage = new ProfileSourceWizardPage();
			profileDataTypeWizardPage =  new ProfileDataTypeWizardPage();
			profileMinMaxWizardPage = new ProfileMinMaxValueWizardPage(plan);
			
			// Add New Profile wizard pages
			addPage(createNewProfileWizardPage);
			addPage(newProfileSourceWizardPage);
			addPage(profileDataTypeWizardPage);
			addPage(profileMinMaxWizardPage);
		}
		else {
			// Error no active plan
			addPage(ERROR_PAGE);
		}
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		// Setup the next page : ProfileDataTypeWizardPage
		if (currentPage == newProfileSourceWizardPage) {
			
			boolean isActivityResource = newProfileSourceWizardPage.isActivityResource();
			profileDataTypeWizardPage.setActivityResource(isActivityResource);
		}
		// Setup the next page : ProfileMinMaxWizardPage
		if (currentPage == profileDataTypeWizardPage
				&& profileDataTypeWizardPage.canFlipToNextPage()) {

			EDataType datatype = profileDataTypeWizardPage.getDataType();
			profileMinMaxWizardPage.setDataType(datatype);
		}
		return super.getNextPage(currentPage);
	}
	
	@Override
	public boolean performFinish() {
		// Create and configures the new profile from the wizard pages.
		Profile profile = createProfile();
		ProfileEnvelopeConstraint constraint = null;
		// External Conditions do not have constraints
		if(!newProfileSourceWizardPage.isExternalCondition()) {
			// MIN MAX
			// Min and max values are not applicable since the user cannot effect a
			// condition's value from an activity effect.
			constraint = getConstraint(profile);
		}
		// Set the operation for undo/redo functionality
		final IUndoableOperation op = new AddProfileOperation(plan, profile, constraint);
		CommonUtils.execute(op, model.getUndoContext());
		
		return true;
	}
	
	private Profile createProfile() {
		final Profile profile = JScienceFactory.eINSTANCE.createProfile();
		// Profile Name
		String profileName = createNewProfileWizardPage.getProfileName();
		profile.setName(profileName);
		// Profile ID
		String id = ProfileUtil.getId(profileName);
		profile.setId(id);
		// Profile Category
		final String category = createNewProfileWizardPage.getCategory();
		profile.setCategory(category);
		// Set if Profile is External Condition
		boolean isExternalCondition = newProfileSourceWizardPage.isExternalCondition();
		profile.setExternalCondition(isExternalCondition);
		// Data Type
		EDataType datatype = profileDataTypeWizardPage.getDataType();
		profile.setDataType(datatype);
		/* Profile Attributes */
		EMap attributes = profile.getAttributes();
		attributes.put(ProfileUtil.CATEGORY_ATTRIBUTE, category);
		boolean isClaimable = profileDataTypeWizardPage.isClaimSelected();
		attributes.put(ProfileUtil.CLAIMABLE_ATTRIBUTE, new Boolean(isClaimable).toString());
		attributes.put(ProfileUtil.DATATYPE_ATTRIBUTE, profileDataTypeWizardPage.getDataTypeLiteral());
		attributes.put(ProfileUtil.EXTERNAL_CONDITION_ATTRIBUTE, new Boolean(isExternalCondition).toString());
		attributes.put(ProfileUtil.ID_ATTRIBUTE, id);
		attributes.put(ProfileUtil.NAME_ATTRIBUTE, profileName);
		if (profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EINTEGER_OBJECT ||
				profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EDOUBLE_OBJECT) {
			String unit = profileMinMaxWizardPage.getUnitLiteral();
			if(!unit.isEmpty()) {
				attributes.put(ProfileUtil.UNITS_ATTRIBUTE, unit);
			}
		}
		return profile;
	}
	
	private ProfileEnvelopeConstraint getConstraint(Profile profile) {
		boolean isClaimable = profileDataTypeWizardPage.isClaimSelected();
		ProfileEnvelopeConstraint constraint = null;
		
		// Sets the profile constraint only for Datatypes Claim, Double, Integer, Duration, or Time
		if(profileDataTypeWizardPage.canFlipToNextPage() || isClaimable) {
			final String minString;
			final String maxString;
			
			if(isClaimable) {
				// Claim selection implies min = null and max = 1.
				minString = null;
				maxString = ProfileDataTypeWizardPage.CLAIM_MAX_VALUE; // "1"
			}
			else {
				minString = profileMinMaxWizardPage.getMin();
				maxString = profileMinMaxWizardPage.getMax();
			}
			
			if(minString!=null || maxString!=null) {
				// Create a constraint
				constraint = ProfileFactory.eINSTANCE.createProfileEnvelopeConstraint();
//				constraint.setProfileKey(profile.getId());
				constraint.setMinLiteral(minString);
				constraint.setMaxLiteral(maxString);
				// SPF-9689 Also set minLiteral and maxLiteral attributes in the profile
				profile.setMinLiteral(minString);
				profile.setMaxLiteral(maxString);
			}
			
			// Units
			if (profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EINTEGER_OBJECT ||
					profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EDOUBLE_OBJECT) {
				profile.setUnits(profileMinMaxWizardPage.getUnit());
			}
			
			// Datapath
			if (profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EINTEGER_OBJECT ||
					profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EDOUBLE_OBJECT ||
					profileDataTypeWizardPage.getDataType() == JSciencePackage.Literals.EDURATION ||
					profileDataTypeWizardPage.getDataType() == EcorePackage.Literals.EDATE) {
				String datapath = profileMinMaxWizardPage.getDatapath();
				profile.getAttributes().put("datapath", datapath);
			}
		}
		return constraint;
	}
		
	@Override
	public boolean canFinish() {
		
		IWizardPage currentPage = this.getContainer().getCurrentPage();
		if (currentPage == profileDataTypeWizardPage) {
			return profileDataTypeWizardPage.canFinish();
		}
		if (currentPage == profileMinMaxWizardPage) {
			return profileMinMaxWizardPage.isPageComplete();
		}
		return false;
	}
}
