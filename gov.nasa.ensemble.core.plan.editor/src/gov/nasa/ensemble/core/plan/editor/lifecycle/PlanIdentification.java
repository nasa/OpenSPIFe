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

import fj.data.Option;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PlanIdentification implements MissionExtendable {
	
	private Text notesText;
	private Text planNameText;
	private String planName = "";
	private String notes = "";
	
	private boolean planNameEditedByUser;
	private Date planStart = null;
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public PlanIdentification() {
		this.planNameEditedByUser = false;
	}

	/**
	 * Create the GUI elements of the plan identification with the given
	 * composite as the parent.
	 * 
	 * @param parent
	 * @return
	 */
	public Composite createPlanIdentification(Composite parent) {
		return createPlanIdentification(parent, canRename());
	}
	
	public Composite createExtraPlanIdentificationFields(Composite composite) {
		return composite;
	}
	
	public Composite createPlanIdentification(Composite parent, boolean canRename) {
		Group group = new Group(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(10, 10).numColumns(2).applyTo(group);
		GridDataFactory.fillDefaults().applyTo(group);
		group.setText("Plan Identification");
		if (canRename) {
			buildPlanNameText(group);
		}
		buildNotes(group);
		return group;
	}
	
	/**
	 * Update the fields in the plan identification using the values stored in
	 * the plan.
	 * 
	 * @param plan
	 */
	public void updateFields(EPlan plan) {
		String v = plan.getMember(CommonMember.class).getNotes();
		if (v != null) {
			notesText.setText(v);
		}
		String name = plan.getName();
		IProject project = EMFUtils.getProject(plan);
		if (project != null) {
			name = project.getName();
		}
		setPlanNameTextQuiet(name);
	}

	public void updatePlan(final EPlan plan) {
		updatePlan(plan, true);
	}
	
	/**
	 * Update the plan based upon the fields in the dialog
	 * @param plan
	 */
	public void updatePlan(final EPlan plan, boolean notify) {
		final CommonMember commonMember = plan.getMember(CommonMember.class);
		final String notes = notesText.getText();
		final String planName = getPlanName();
		TransactionUtils.writing(commonMember, new Runnable() {
			@Override
			public void run() {
				plan.setName(planName);
			    commonMember.setNotes(notes);
			}
		});
	}
	
	/**
	 * Update the auto-generated plan name if the user has not already hand
	 * edited the plan name field.
	 */
	public void updatePlanName() {
		if (!planNameEditedByUser) {
			String generatedPlanName = generatePlanName(planStart);
			setPlanNameTextQuiet(generatedPlanName);
			
			// notify any listeners that the plan name is about to change
			firePropertyChange(WrapperUtils.ATTRIBUTE_NAME, null, generatedPlanName);
		}
	}
	
	/**
	 * Generate a plan name using the given start sol.
	 * 
	 * Implicit parameters used to generate the plan name are the group, and
	 * current plan state.
	 * 
	 * @param planStart
	 * 
	 * @return a plan name with the format "sol_NNN_GROUP_STATE"
	 */
	protected String generatePlanName(Date date) {
		int startSol = MissionCalendarUtils.getDayOfMission(date);
		String missionDayName = MissionConstants.getInstance().getMissionDayName();
		StringBuilder generatedPlanName = new StringBuilder()
		  .append(missionDayName)
		  .append("_")
		  .append(startSol)
		  .append("_");
		return generatedPlanName.toString().replaceAll("[^a-zA-Z0-9_]","_");  // sanitize the plan name
	}
	
	/**
	 * Wrap the actual setting of the plan name with the add/remove listener
	 * calls so as not to confuse this update with an update from the user.
	 * 
	 * @param name
	 */
	protected void setPlanNameTextQuiet(String name) {
		planName = name;
		if (planNameText != null) {
			planNameText.removeModifyListener(planNameModifyListener);
			planNameText.setText(name);
			planNameText.addModifyListener(planNameModifyListener);
		}
	}
	
	/*
	 * UI builder methods
	 */

	/**
	 * Build the GUI element for inputing plan notes.
	 * 
	 * @param parent
	 */
	protected void buildNotes(Composite parent) {
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("Notes: ");
		GridDataFactory.fillDefaults().applyTo(label);
		
		notesText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		notesText.setText("");
		notesText.addModifyListener(new TextModifyListener(EditorPlugin.ATTRIBUTE_NOTES));
		notesText.setData("name", "TemporalPlanIdentification.notesText");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(notesText);
	}

	/**
	 * Build the GUI element for inputing the plan name.
	 * 
	 * @param parent
	 */
	protected void buildPlanNameText(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Plan name:");
		GridDataFactory.fillDefaults().applyTo(label);
		
		planNameText = new Text(parent, SWT.BORDER | SWT.SINGLE);
		planNameText.addModifyListener(planNameModifyListener);
		planNameText.setData("name", "TemporalPlanIdentification.planNameText");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(planNameText);

		if (getPlanNameMessage().isSome()) {
			Label planNameMessageLabel = new Label(parent, SWT.NONE);
			planNameMessageLabel.setText(getPlanNameMessage().some());
			GridDataFactory.fillDefaults().span(2, 1).applyTo(planNameMessageLabel);
		}
	}
	
	public boolean canRename() {
		return true;
	}
	
	protected Option<String> getPlanNameMessage() {
		return Option.none();
	}
	
	/*
	 * Property accessors
	 */
	
	/**
	 * @return the value of the plan name field
	 */
	public String getPlanName() {
		return planName;
	}
	
	public void setPlanStart(Date date) {
		this.planStart = date;
	}
	
	public Text getNotesText() {
		return notesText;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public Text getPlanNameText() {
	    return planNameText;
    }
	
	/*
	 * 
	 * Property change delegates
	 * 
	 */
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	/*
	 * 
	 * Inner class definitions
	 * 
	 */
	
	private ModifyListener planNameModifyListener = new TextModifyListener(WrapperUtils.ATTRIBUTE_NAME) {
		
		@Override
		public void modifyText(ModifyEvent e) {
			planNameEditedByUser = true;
			super.modifyText(e);
		}
	};
	
	private class TextModifyListener implements ModifyListener {

		private final String property;
		
		public TextModifyListener(String property) {
			super();
			this.property = property;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			Text text = (Text) e.getSource();
			if (text == notesText) {
				notes = text.getText();
			}
			else if (text == planNameText) {
				planName = text.getText();				
			}
			firePropertyChange(property, null, text.getText());
		}
		
	}

}
