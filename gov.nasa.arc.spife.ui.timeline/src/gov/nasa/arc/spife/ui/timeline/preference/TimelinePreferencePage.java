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
package gov.nasa.arc.spife.ui.timeline.preference;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.common.ui.preferences.LongFieldEditor;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class TimelinePreferencePage extends AbstractPreferencePage implements TimelineConstants {

	public static final String P_ALTERNATING_COLOR			= "timeline.color.alternating";
	public static final String P_HORIZONTAL_LINES_HIDE		= "timeline.line.horizontal.hide";
	public static final String P_HORIZONTAL_LINES_COLOR		= "timeline.color.horizontal";
	public static final String P_VERTICAL_LINES_COLOR		= "timeline.color.vertical";
	public static final String P_SCALE_TICKMARK_COLOR		= "timeline.color.tickmark";
	public static final String P_ROW_ELEMENT_HEIGHT			= "timeline.row.element.height";
	public static final String P_ROW_ELEMENT_ROUNDED		= "timeline.row.element.rounded";
	public static final String P_ROW_ELEMENT_BORDER			= "timeline.row.element.border";
	public static final String P_ROW_ELEMENT_OVERLAP		= "timeline.row.element.overlapFactor";
	public static final String P_SNAP_TO_ACTIVE				= "timeline.editor.snapTo.active";
	public static final String P_SNAP_TO_ORBIT_ACTIVE		= "timeline.editor.snapToOrbit.active";

	public static final String P_TOOLTIP_SPEED				= "timeline.row.element.tooltip.speed";
	public static final String P_TOOLTIP_TIME   			= "timeline.row.element.tooltip.time";
	
	public static final String P_INFO_FONT_SIZE				= "timeline.info.font.size";
	public static final String P_CONTENT_FONT_SIZE			= "timeline.content.font.size";
	public static final String P_SCALE_FONT_SIZE			= "timeline.scale.font.size";
	
	public static final String P_CURSOR_TIME_ENABLED		= "timeline.marker.cursor.enabled";
	public static final String P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS		= "timeline.row.element.activity.group.noGaps";
	public static final String P_PIN_ICON_VISIBLE			= "timeline.pin.icon.visible";
	
	public static final String P_DECORATOR_TEXT_KEY			= "timeline.decorator.text.key";
	public static final String P_DECORATOR_TEXT_KEY_NONE	= "timeline.decorator.text.key.none";
	
	public static final String P_MIN_MAX_CONSTRAINT_LINES_VISIBLE 		= "timeline.min.max.constraint.lines.visible";	
	
	private Group colorGroup;
	private BooleanFieldEditor hideHorizontalLinesBooleanFieldEditor;
	private ColorFieldEditor horizontalColorFieldEditor;

	
	public static enum Tooltip {
		NORMAL,
		FAST,
		NONE
	}
	
	public TimelinePreferencePage() {
		super(TIMELINE_PREFERENCES);
		setDescription("Timeline Preference Page");
	}

	@Override
	protected void createFieldEditors() {
		colorGroup = createGroup(getFieldEditorParent());
		colorGroup.setText("Color");
		{
			addField(new ColorFieldEditor(
				P_ALTERNATING_COLOR,
				"Alternating Rows:",
				colorGroup));
		}
		addField(hideHorizontalLinesBooleanFieldEditor = 
				new BooleanFieldEditor(P_HORIZONTAL_LINES_HIDE, "Hide horizontal lines", colorGroup));
		{
			addField(horizontalColorFieldEditor = new ColorFieldEditor(
				P_HORIZONTAL_LINES_COLOR,
				"Horizontal Lines:",
				colorGroup));
		}
		{
			addField(new ColorFieldEditor(
				P_VERTICAL_LINES_COLOR,
				"Vertical Lines:",
				colorGroup));
		}
		{
			addField(new ColorFieldEditor(
				P_SCALE_TICKMARK_COLOR,
				"Scale Tickmarks:",
				colorGroup));
		}
		
		{
			IntegerFieldEditor editor = new IntegerFieldEditor(
					P_SCALE_FONT_SIZE,
				    "Scale area font size:",
				    getFieldEditorParent());
			editor.setValidRange(3, 72);
			addField(editor);
		}
		
		{
			IntegerFieldEditor editor = new IntegerFieldEditor(
				P_CONTENT_FONT_SIZE,
			    "Timeline area font size:",
			    getFieldEditorParent());
			editor.setValidRange(3, 72);
			addField(editor);
		}
		
		{
			addField(new BooleanFieldEditor(
				P_CURSOR_TIME_ENABLED,
			    "Show timeline cursor",
			    getFieldEditorParent()));
		}
		
		Group nodeGroup = createGroup(getFieldEditorParent());
		nodeGroup.setText("Row");

		{
			IntegerFieldEditor editor = new IntegerFieldEditor(
					P_ROW_ELEMENT_HEIGHT,
				    "Height (pixels):",
				    nodeGroup);
			editor.setValidRange(3, 100);
			addField(editor);
		}
		
		{
			addField(new StringFieldEditor(
				P_ROW_ELEMENT_OVERLAP,
			    "Activity Vertical Overlap:",
			    nodeGroup) {
					@Override
					protected boolean doCheckState() {
						try {
							float f = Float.parseFloat(getStringValue());
							return (f >= 0 && f <=1);
						} catch (NumberFormatException e) {
							return false;
						}
					}
			});
		}
		
		addField(new BooleanFieldEditor(P_ROW_ELEMENT_ROUNDED, "Round corners", nodeGroup));
		addField(new BooleanFieldEditor(
				P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS,
				"Show Activity Group figures with no duration gaps between its activities",
				nodeGroup));
		addTooltipSpeedEditor(nodeGroup);
		addTooltipTimeEditor(nodeGroup);
		addNodeDecoratorFields(getFieldEditorParent());
	}

	private void addTooltipTimeEditor(Group nodeGroup) {
		LongFieldEditor editor = new LongFieldEditor(
				P_TOOLTIP_TIME,
			    "Tool Tip Time (ms):",
			    nodeGroup);
		editor.setValidRange(500, Long.MAX_VALUE);
		addField(editor);
	}

	private void addTooltipSpeedEditor(Group nodeGroup) {
		String tooltipValues[][] = new String[3][2];
		tooltipValues[0] = new String[] { "Normal",    Tooltip.NORMAL + "" };
		tooltipValues[1] = new String[] { "Fast", 	 Tooltip.FAST + ""   };
		tooltipValues[2] = new String[] { "None",  	 Tooltip.NONE + ""	 };
		ComboFieldEditor tooltipEditor = new ComboFieldEditor(
				P_TOOLTIP_SPEED,
				"Tool Tip Speed: ",
				tooltipValues,
				nodeGroup);
		addField(tooltipEditor);
	}

	private void addNodeDecoratorFields(Composite parent) {
		TextDecoratorFieldProvider p = ClassRegistry.createInstance(TextDecoratorFieldProvider.class);
		if (p != null) {
			Group nodeTextGroup = createGroup(parent);
			nodeTextGroup.setText("Activity Text");
			
			String labelValues[][] = p.getFieldValues();
			addField(new ComboFieldEditor(
					P_DECORATOR_TEXT_KEY,
					"Label parameter: ",
					labelValues,
					nodeTextGroup));
		}
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		horizontalColorFieldEditor.setEnabled(true, colorGroup);
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
	
	private void setGroupEnablement() {
		Boolean isHideHorizontalLines = hideHorizontalLinesBooleanFieldEditor.getBooleanValue();
		horizontalColorFieldEditor.setEnabled(!isHideHorizontalLines, colorGroup);
	}
}
