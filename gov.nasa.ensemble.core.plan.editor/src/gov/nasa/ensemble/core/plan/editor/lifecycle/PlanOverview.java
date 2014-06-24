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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.ICompositeListLabel;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.PermissionConstants;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class PlanOverview implements ICompositeListLabel, PropertyChangeListener {

	private final EPlan plan;

	protected Composite  composite;
	protected Label      notesLabel;
	protected Composite  iconRow;

	protected StyledText objectSummaryText;
	protected StyleRange objectSummaryBoldStyleRange;
	protected StyleRange objectSummaryItalicsStyleRange;
	protected StyleRange objectSummaryNormalStyleRange;
			
	/**
	 * Create the GUI elements of the plan overview with the given composite as
	 * its parent.
	 * 
	 * @param parent
	 * @param plan 
	 */
	public PlanOverview(Composite parent, EPlan plan) {
		this.composite = new Composite(parent, SWT.BORDER | SWT.NO_FOCUS);
		this.plan = plan;
	}

	public void layout() {
		composite.setBackground(ColorConstants.white);
		composite.setLayout(new GridLayout());
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		layoutArea();
		
		for (Control child : composite.getChildren()) {
			child.setBackground(ColorConstants.white);
		}
	}
	
	@Override
	public Composite getComposite() {
		return composite;
	}

	public String getName() {
		return plan.getName();
	}

	protected void layoutArea() {
		// build the plan summary section
		buildSummaryArea();
		// build the notes section
		buildNotesArea();
		// build the activity icons and resource summary section
		buildCategoriesArea();
	}

	protected void setCategoryIcons() {
		Image image = MissionUIConstants.getInstance().getContainerIcon("");
		ImageData imageData = image.getImageData();
		iconRow.setLayoutData(new GridData(-1, imageData.height + 2));
		final Set<String> categories = getCategorySet();
		WidgetUtils.runInDisplayThread(composite, new Runnable() {
			@Override
			public void run() {
				for (String category : categories) {
					if (category != null) {
						Image image = MissionUIConstants.getInstance().getIcon(category);
						addCategoryIcon(image);
					}
				}
				composite.layout(true, true);
			}
		});
	}
	
	protected void buildCategoriesArea() {
		iconRow = new Composite(composite, SWT.NO_FOCUS);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginBottom = 0;
		rowLayout.marginTop = 0;
		rowLayout.marginLeft = 0;
		rowLayout.marginRight = 0;
		rowLayout.wrap = true;
		rowLayout.pack = true;
		rowLayout.justify = false;
		iconRow.setLayout(rowLayout);
		iconRow.setBackground(composite.getBackground());
		setCategoryIcons();
	}

	protected void buildNotesArea() {
		notesLabel = new Label(composite, SWT.NO_FOCUS);
		final String notes = getNotes();
		WidgetUtils.runInDisplayThread(composite, new Runnable() {
			@Override
			public void run() {
				setNotesText(notes);
			}
		});
	}
	
	protected void buildSummaryArea() {
		objectSummaryText   = new StyledText(composite, SWT.NO_FOCUS);
		objectSummaryText.setEditable(false);
		objectSummaryText.setEnabled(false);
		objectSummaryBoldStyleRange    = new StyleRange();
		objectSummaryNormalStyleRange  = new StyleRange();
		objectSummaryItalicsStyleRange = new StyleRange();
		
		setSummaryText();
	}
	
	protected final void addCategoryIcon(final Image image) {
		if (image != null) {
			ImageData imageData = image.getImageData();
			Canvas canvas = new Canvas(iconRow, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
			canvas.setLayoutData(new RowData(imageData.width, imageData.height));
			canvas.setBackground(iconRow.getBackground());
		    canvas.addPaintListener(new PaintListener() {
		        @Override
				public void paintControl(PaintEvent e) {
		        	e.gc.drawImage(image,0,0);
		        }
		    });
		}
	}		

	public void updateObjectName(String name) {
		updatePlanName(name);
	}
	
	public void updatePlanName(final String planName) {
		final int activityCount = getActivityCount();
		final int activityGroupCount = getActivityGroupCount();
		final String custodian = getCustodian();
		final String worldPermissions = getWorldPermissions();
		final String group = getGroup();
		final String lastModified = getLastModified();
		WidgetUtils.runInDisplayThread(composite, new Runnable() {
			@Override
			public void run() {
				setPlanSummaryText(
						activityCount,
						activityGroupCount, 
						planName, 
						custodian, 
						worldPermissions, 
						group,  
						lastModified);
			}
		});
	}
	
	public void updateWorldPermissions(final String worldPermissions) {
		final String planName = getName();
		final int activityCount = getActivityCount();
		final int activityGroupCount = getActivityGroupCount();
		final String custodian = getCustodian();
		final String group = getGroup();
		final String lastModified = getLastModified();
		WidgetUtils.runInDisplayThread(composite, new Runnable() {
			@Override
			public void run() {
				setPlanSummaryText(
						activityCount,
						activityGroupCount, 
						planName, 
						custodian, 
						worldPermissions, 
						group,  
						lastModified);
			}
		});
	}
	
	protected void setSummaryText() {
		updatePlanName(getName());		
	}
	
	/**
	 * Set the "plan summary" section of the plan overview. This summary
	 * includes the plan name (bolded) and a brief description of the number of
	 * activity groups and activities (in italics).
	 * @param lastModified 
	 * @param name
	 */
	protected void setPlanSummaryText(
		Integer numActivities, Integer numActivityGroups, 
		String planName, 
		String custodian, String worldPermissions, String group, 
		String lastModified
	) {
		
		// build the plan summary (name plus brief plan description)
		StringBuilder planSummaryBuilder = new StringBuilder(planName);
		
		if (worldPermissions != null) {
			planSummaryBuilder.append(" ("+worldPermissions+")");
		}
		
		if (custodian != null) {
			planSummaryBuilder.append(" [");
			planSummaryBuilder.append(custodian);
			
			if (group != null) {
				planSummaryBuilder.append("/"+group);
			}
			
			planSummaryBuilder.append("]");
		}
		planSummaryBuilder.append("\n");
		if (numActivities != null && numActivityGroups != null) {
			planSummaryBuilder.append("(");
			planSummaryBuilder.append(numActivities);
			planSummaryBuilder.append(" ");
			if (numActivities == 1) {
				planSummaryBuilder.append("activity");
			} else {
				planSummaryBuilder.append("activities");
			}
			planSummaryBuilder.append(" in ");
			planSummaryBuilder.append(numActivityGroups);
			planSummaryBuilder.append(" ");
			if (numActivityGroups == 1) {
				planSummaryBuilder.append("group");
			} else {
				planSummaryBuilder.append("groups");
			}
			planSummaryBuilder.append(") - Modified: ");
			planSummaryBuilder.append(lastModified);
		}
		
		String planSummaryString = planSummaryBuilder.toString();
		objectSummaryText.setText(planSummaryString);
		
		// set the style ranges (bold plan name, and italics plan description)
		int planNameLength = planName.length();
		objectSummaryBoldStyleRange.start      = 0;
		objectSummaryBoldStyleRange.length     = planNameLength;
		objectSummaryBoldStyleRange.fontStyle  = SWT.BOLD;
		objectSummaryText.setStyleRange(objectSummaryBoldStyleRange);
		
		int custodianNameLength = 0;
		if (custodian != null) { 
			custodianNameLength = custodian.length() + 3;
		}
		objectSummaryNormalStyleRange.start      = planNameLength;
		objectSummaryNormalStyleRange.length     = custodianNameLength;
		objectSummaryNormalStyleRange.fontStyle  = SWT.NONE;
		objectSummaryText.setStyleRange(objectSummaryNormalStyleRange);
		
		objectSummaryItalicsStyleRange.start      = custodianNameLength + planNameLength;
		objectSummaryItalicsStyleRange.length     = planSummaryString.length() - planNameLength - custodianNameLength;
		objectSummaryItalicsStyleRange.fontStyle  = SWT.ITALIC;
		objectSummaryText.setStyleRange(objectSummaryItalicsStyleRange);
		
		// tell our parent to re-layout
		composite.layout(true, true);
	}
	
	/**
	 * Set the "notes" section of the plan overview. If the specified notes
	 * argument is > 75 characters, the display will truncate the notes and set
	 * a tool tip with the complete notes value.
	 * 
	 * @param notes
	 */
	public void setNotesText(String notes) {
		if (notes == null) {
			notes = "";
		}
		if (notes.length() > 75) {
			notesLabel.setToolTipText(notes);
			notes = notes.substring(0, 75) + " ...";
		} else
			notesLabel.setToolTipText(null);
		
		notesLabel.setText(notes);
		composite.layout(true, true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if (EditorPlugin.ATTRIBUTE_NOTES.equals(name)) {
			setNotesText((String) evt.getNewValue()); 
		} else  if (WrapperUtils.ATTRIBUTE_NAME.equals(name)) {
			updatePlanName((String) evt.getNewValue());
		} else if (PermissionConstants.WORLD_PERMISSIONS_KEY.equals(name)) {
			updateWorldPermissions((String) evt.getNewValue());
		}
	}
	
	public int getActivityGroupCount() {
		class CountingVisitor extends PlanVisitor {
			int groups = 0;
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivityGroup) {
			    	groups++;
			    }
			}
		}
		CountingVisitor visitor = new CountingVisitor();
		visitor.visitAll(plan);
		return visitor.groups;
	}
	
	public int getActivityCount() {
		class CountingVisitor extends PlanVisitor {
			int activities = 0;
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
			    	activities++;
			    }
			}
		}
		CountingVisitor visitor = new CountingVisitor();
		visitor.visitAll(plan);
		return visitor.activities;
	}
	
	public String getNotes() {
		return plan.getMember(CommonMember.class).getNotes();
	}
	
	public String getCustodian() {
		return WrapperUtils.getAttributeValue(plan, EditorPlugin.ATTRIBUTE_CUSTODIAN);
	}
	
	public String getWorldPermissions() {
		return WrapperUtils.getAttributeValue(plan, PermissionConstants.WORLD_PERMISSIONS_KEY);
	}

	public String getGroup() {
		return WrapperUtils.getAttributeValue(plan, EditorPlugin.ATTRIBUTE_GROUP);
	}
	
	public String getLastModified() {
		EObject adapter = CommonUtils.getAdapter(plan, EObject.class);
		if (adapter != null && adapter.eResource() != null) {
			long time = adapter.eResource().getTimeStamp();
			Date date = new Date(time);
			return StringifierRegistry.getStringifier(Date.class).getDisplayString(date);
		}
		return "Unknown";
	}
	
	public String getPlanState() {
		return WrapperUtils.getAttributeValue(plan, EditorPlugin.ATTRIBUTE_PLAN_STATE);
	}
	
	/**
	 * Performs the following query:
	 * select valueString from parameter param, activity a, activityGroup ag, plan p
	 *  where p.name = 'name'
	 *  and ag.plan = p.id
	 *  and a.activityGroup = ag.id
	 *  and param.planElement = a.id
	 *  and param.name = ATTRIBUTE_TYPE;
	 * @return a list of string representing the categories fo the plan
	 */
	public Set<String> getCategorySet() {
		final Set<String> categories = new LinkedHashSet<String>();
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
					EActivity activity = (EActivity) element;
			    	EActivityDef def = ADParameterUtils.getActivityDef(activity);
			    	if (def == null) {
						LogUtil.warn("PlanOverviewProvider.getCategorySet(): Activity "+activity.getName()+" has null ActivityDef");
					} else {
						categories.add(def.getCategory());
					}
			    }
			}
		}.visitAll(plan);
		return categories;
	}

}
