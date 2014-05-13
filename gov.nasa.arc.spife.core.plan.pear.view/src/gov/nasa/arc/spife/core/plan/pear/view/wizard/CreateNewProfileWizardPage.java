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
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.profile.tree.ProfileTreeView;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

public class CreateNewProfileWizardPage extends EnsembleWizardPage {

	private static final int CATEGORY_FIELD_X_INDENT = 20;
	private static final int CATEGORY_FIELD_HINT_LENGTH = 200;

	private EPlan plan;
	private List<String> existingCategoryValues;
	private List<String> existingProfileNames;
	
	private Text nameText;
	
	private Group categoryGroup;
	private Button existingCategoryButton;
	private Combo existingCombo;
	private Button newCategoryButton;
	private Text newCategoryText;
	
	
	public CreateNewProfileWizardPage() {
		super("wizardPage"); //$NON-NLS-1$
		this.setTitle(Messages.CreateNewProfileWizardPage_Title);
		setMessage(Messages.CreateNewProfileWizardPage_Description_Message);
	}

	public CreateNewProfileWizardPage(EPlan plan) {
		this();
		
		this.plan = plan;
		existingCategoryValues = getExistingCategories();
		
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		existingProfileNames = new ArrayList<String>();
		for (Profile profile : member.getResourceProfiles()) {
			existingProfileNames.add(profile.getName().toLowerCase());
		}
	}
	
	@Override
	public void createControl(Composite parent) {

		 Composite composite = new Composite(parent, SWT.NONE);
		 GridLayoutFactory.fillDefaults().numColumns(2)
		 								 .spacing(0, 10)
		 								 .applyTo(composite);
		 // Name
		 createProfileName(composite);
		 // Category
		 createPofileCategory(composite);
		 
		 // Set tab list
		 composite.setTabList(new Control[]{nameText,categoryGroup});
		 categoryGroup.setTabList(new Control[]{existingCategoryButton,existingCombo,newCategoryButton,newCategoryText});
		 
		 setControl(composite);
		 // Disable next button initially - needs user input values.
		 this.setPageComplete(false);
	}

	private void createProfileName(Composite composite) {
		
		 // Name
		 final Label nameLabel = new Label(composite, SWT.NONE);
		 nameLabel.setText(Messages.CreateNewProfileWizardPage_Name_Label);
		 GridDataFactory.fillDefaults().indent(5, 10)
		 							   .applyTo(nameLabel);
		 
		 nameText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		 nameText.addModifyListener(new DefaultModifyListener());
		 GridDataFactory.fillDefaults().indent(5, 10)
		 							   .applyTo(nameText);
	}
	
	private void createPofileCategory(Composite composite) {
		
		categoryGroup = new Group(composite, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 0).applyTo(categoryGroup);
		GridDataFactory.fillDefaults().grab(true, false)
									  .span(2, 1)
									  .hint(SWT.DEFAULT, 125)
									  .applyTo(categoryGroup);
		categoryGroup.setText(Messages.CreateNewProfileWizardPage_Group_Label);
		
		// Existing Category
		existingCategoryButton = new Button(categoryGroup, SWT.RADIO);
		existingCategoryButton.setText(Messages.CreateNewProfileWizardPage_Existing_Button_Description);
		GridDataFactory.fillDefaults().indent(0, 5)
									  .applyTo(existingCategoryButton);
		existingCategoryButton.setSelection(true);
		existingCategoryButton.addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event){
	    		boolean selected = ((Button)event.widget).getSelection();
	    		existingCombo.setEnabled(selected);
	    		pageUpdated();
	    	}
		});
		
		existingCombo = new Combo(categoryGroup, SWT.READ_ONLY);
		existingCombo.addModifyListener(new DefaultModifyListener());
		GridDataFactory.fillDefaults().indent(CATEGORY_FIELD_X_INDENT, 0)
									  .hint(CATEGORY_FIELD_HINT_LENGTH, SWT.DEFAULT)
		 							  .applyTo(existingCombo);
		// Set the exiting category values
		existingCombo.setItems(existingCategoryValues.toArray(new String[0]));
		
		// New Category
		newCategoryButton = new Button(categoryGroup, SWT.RADIO);
		newCategoryButton.setText(Messages.CreateNewProfileWizardPage_NewCategory_Button_Description);
		newCategoryButton.addSelectionListener(new SelectionAdapter() {
	    	@Override
			public void widgetSelected(SelectionEvent event){
	    		boolean selected = ((Button)event.widget).getSelection();
	    		newCategoryText.setEnabled(selected);
	    		pageUpdated();
	    	}
		});
		GridDataFactory.fillDefaults().applyTo(newCategoryButton);
		newCategoryText = new Text(categoryGroup, SWT.BORDER | SWT.SINGLE);
		newCategoryText.addModifyListener(new DefaultModifyListener());
		newCategoryText.setEnabled(false);
		GridDataFactory.fillDefaults().indent(CATEGORY_FIELD_X_INDENT, 0)
									  .hint(CATEGORY_FIELD_HINT_LENGTH, SWT.DEFAULT)
									  .applyTo(newCategoryText);
	}
	
	/**
	 * Retrieves the existing categories defined in the active plan.
	 * 
	 * @return
	 */
	private List<String> getExistingCategories() {
		
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		List<String> categoryList = new ArrayList<String>(); 
		for (Profile profile : member.getResourceProfiles()) {
			String category = profile.getCategory();
			if(category!=null && !categoryList.contains(category)) {
				categoryList.add(category);
			}
		}
		// Sort it out
		Collections.sort(categoryList);

		return categoryList;
	}

	protected static IViewPart getProfileView() {
		
		IViewReference viewReferences[] = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences();
		for (int i = 0; i < viewReferences.length; i++) {
			if (ProfileTreeView.ID.equals(viewReferences[i].getId())) {
				return viewReferences[i].getView(false);
			}
		}
		return null;
	}
	
	public boolean isExistingCategory() {
		return existingCategoryButton.getSelection();
	}
	
	
	public String getProfileName() {
		return (nameText != null) ? nameText.getText().trim() : ""; //$NON-NLS-1$
	}
	
	public String getCategory() {
		return (isExistingCategory() ? existingCombo.getText() : newCategoryText.getText().trim());
	}
	
	/**
	 * Validate the profile name.
	 * 
	 * @param profileName The profile's name string to be validated.
	 * @return true if the profileName is non-empty and does not already exist.
	 */
	protected final boolean validateName(String profileName) {
		clearError(CreateNewProfileWizardPage.class);

		if (profileName.length() == 0) {
			setError(CreateNewProfileWizardPage.class, Messages.CreateNewProfileWizardPage_NoProfileName_Error_Message);
			return false;
		}
		if(existingProfileNames.contains(profileName.toLowerCase())) {
			setError(CreateNewProfileWizardPage.class,
					NLS.bind(Messages.CreateNewProfileWizardPage_ProfileNameExists_Error_Message, profileName));
			return false;
		}
		return true;
	}
	
	/**
	 * Validate if the category given is valid.
	 * 
	 * @return 
	 */
	private boolean validateCategory() {
		
		String category = getCategory();

		if(isExistingCategory()) {
			// Existing category
			if(category.isEmpty()) {
				setError(CreateNewProfileWizardPage.class, Messages.CreateNewProfileWizardPage_ExistingCategoryNotSet_Error_Message);
				return false;
			}
		}
		else {
			// New Category
			if(category.isEmpty()) {
				// New Category
				setError(CreateNewProfileWizardPage.class, Messages.CreateNewProfileWizardPage_NewCategoryNotSet_Error_Message);
				return false;
			}
			// New Category already exists
			if(containsIgnoreCase(existingCategoryValues, category)) {
				setError(CreateNewProfileWizardPage.class, 
						NLS.bind(Messages.CreateNewProfileWizardPage_NewCategoryExists_Message, category));
				return false;
			}
		}
		return true;
	}
	
	private boolean containsIgnoreCase(List<String> list, String str) {
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			if (iter.next().equalsIgnoreCase(str))
				return true;
		}
		return false;
	}
	
	@Override
	protected void pageUpdated() {
		clearError(CreateNewProfileWizardPage.class);
		String priofileName = getProfileName();
		boolean result = validateName(priofileName) && validateCategory();
		this.setPageComplete(result);
	}
	
	@Override
	public boolean isPageComplete() {
		boolean pageComplete = super.isPageComplete();
		return pageComplete;
	}	
}
