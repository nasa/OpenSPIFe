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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.type.editor.EnsembleTextBasedDateEditor;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

/**
 * This page prompts for an output filename and start and end boundaries within the plan.
 */
public class EMFPlanSelectionExportWizardPage extends FileSelectionPage {

//	private static final String TOOLTIP_FOR_SELECTION = "Only activities you have highlighted in the table or timeline will be included.";
//	private static final String TOOLTIP_FOR_RANGE = "Only activities in progress during this period will be included." +
//			"  Activities that have already ended before the From time or have not yet started by the To time will be excluded.";
	private Button allButton;
	private Button selectionButton;
	private Button rangeButton;
	private EnsembleTextBasedDateEditor fromField;
	private EnsembleTextBasedDateEditor toField;
	private boolean includeSelectionOption = true;
	
	private EPlan plan;
	
	public EMFPlanSelectionExportWizardPage(int style, EPlan plan) {
		super(style);
		this.plan = plan;
	}
	
	/** Call right after creating to leave out Selection option.
	 * @since SPF-5492
	 */
	public void setIncludeSelectionOption(boolean b) {
		includeSelectionOption = b;
	}


	@Override
	protected void buildControls(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
	    container.setLayout(new GridLayout(2, false));
	  
	    allButton = new Button(container, SWT.RADIO);
		allButton.setText("All");
		allButton.setSelection(true);
		
		if (includeSelectionOption) {
			
			// Empty label for grid
			new Label(container, SWT.NONE);

			selectionButton = new Button(container, SWT.RADIO);
			selectionButton.setText("Selection");
//			selectionButton.setToolTipText(TOOLTIP_FOR_SELECTION);
		}
		
		// Empty label for grid
		new Label(container, SWT.NONE);
		
		rangeButton = new Button(container, SWT.RADIO);
		rangeButton.setText("From:");
//		rangeButton.setToolTipText(TOOLTIP_FOR_RANGE);
		
		fromField = new EnsembleTextBasedDateEditor(this, container, plan.getMember(PlanTemporalMember.class).getStartTime());
//		fromField.getEditorControl().setToolTipText(TOOLTIP_FOR_RANGE);
		
		Label toLabel = new Label(container, SWT.NONE);		
		toLabel.setText("      To:");
//		toLabel.setToolTipText(TOOLTIP_FOR_RANGE);
		
		toField = new EnsembleTextBasedDateEditor(this, container, plan.getMember(PlanTemporalMember.class).getEndTime());
//		toField.getEditorControl().setToolTipText(TOOLTIP_FOR_RANGE);
		
		{DateEditListener listener = new DateEditListener();
		fromField.addPropertyChangeListener(listener);
		toField.addPropertyChangeListener(listener);
		}
		{RadioButtonListener listener = new RadioButtonListener();
		allButton.addSelectionListener(listener);
		if (includeSelectionOption) selectionButton.addSelectionListener(listener);
		rangeButton.addSelectionListener(listener);
		}
	
	}
	
	protected boolean isAllSelected() {
		return allButton != null && allButton.getSelection();
	}

	protected boolean isSelectionSelected() {
		return selectionButton != null && selectionButton.getSelection();
	}

	protected boolean isRangeSelected() {
		return rangeButton != null && rangeButton.getSelection();
	}
	
	protected Date getStartBound() {
		return fromField.getObject();
	}
	
	protected Date getEndBound() {
		return toField.getObject();
	}
	
	@Override
	protected void pageUpdated() {
		clearError(Date.class);
		if (isRangeSelected() && getStartBound().after(getEndBound())) {
			setError(Date.class, "End date is before start date.");
		}
		super.pageUpdated();
	}

	public List<EActivity> getActivitiesToExport() {
		
		if(isSelectionSelected()) {
			List<EActivity> activities = new ArrayList<EActivity>();
			
			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor(); 
            if(editorPart instanceof MultiPagePlanEditor) { 
                 MultiPagePlanEditor multiPagePlanEditor = (MultiPagePlanEditor)editorPart; 
                 ISelectionProvider ensembleSelectionProvider = multiPagePlanEditor.getSite().getSelectionProvider();
                 
                 IStructuredSelection selection = (IStructuredSelection)ensembleSelectionProvider.getSelection();
                 Iterator iterator = selection.iterator();
                 while(iterator.hasNext()) {
                	 Object o = iterator.next();
                	 if(o instanceof EActivity) {
                		 activities.add((EActivity)o);
                	 }
                 }
            }             
            return activities;
		} else if(isRangeSelected()) {
			Date startBound = fromField.getObject();
			Date endBound = toField.getObject();
			List<EActivity> activities = EPlanUtils.getActivities(plan);
			ArrayList<EActivity> activitiesInBounds = new ArrayList<EActivity>();
			
			for(EActivity activity : activities) {
				Date activityStart = activity.getMember(TemporalMember.class).getStartTime();
				Date activityEnd = activity.getMember(TemporalMember.class).getEndTime();

				if((activityEnd.compareTo(startBound) >= 0
						&& activityStart.compareTo(endBound) < 0)) {
					activitiesInBounds.add(activity);
				}
			}
			
			LogUtil.info(activitiesInBounds.size() + " activities are in the selected time range.");

			return activitiesInBounds;
		} else { // all
			return EPlanUtils.getActivities(plan);
		}
	}
	
	
	public class DateEditListener implements PropertyChangeListener, SelectionListener {
		
		private void doit () {
			pageUpdated();
			if (rangeButton != null) rangeButton.setSelection(true);
			if (allButton != null) allButton.setSelection(false);
			if (selectionButton != null) selectionButton.setSelection(false);
//			setMessage(TOOLTIP_FOR_RANGE, INFORMATION);
		}

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			 doit();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			 doit();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			 doit();
		}
	}
	
	public class RadioButtonListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent e) {
			pageUpdated();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			pageUpdated();
		}
	}


	
}
