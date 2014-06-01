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
/*
 */
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.ui.FontUtils;

import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * [ {date1} | {Type}     | {icon1} | {Name/Description} ... ]
 * [ {date2} | {Duration} | {icon2} | {Name/Details}     ... ]
 * @author Andrew Bachmann
 */
public class InfobarComposite extends Composite implements IPropertyChangeListener, TimelineConstants {	

	private static final Color BACKGROUND_COLOR = ColorConstants.white;
	private static final Color TYPE_COLOR = ColorConstants.darkGreen;
	private static final Color TEXT_COLOR = ColorConstants.black;
	private static final Color TIME_COLOR = ColorConstants.blue;
	
	private static int DATE_COLUMN = 95;
	private static int DURATION_COLUMN = 110;
	private static int ICON_SIZE = 16;
	private static int TEXT_COLUMN = 200;
	
	private Font font; // needs to be dynamically created and disposed
	
	private final Label icon1;
	private final Label name_description;
	private final Label date1;
	private final Label type;
	private final Label icon2;
	private final Label name_details;
	private final Label date2;
	private final Label duration;

	/**
	 * Constructs the composite and all of its fields.
	 * @see setDateFormat for non-default time representations
	 * @param parent
	 */
	public InfobarComposite(Composite parent) {
		super(parent, SWT.NONE);
		updateFont();
		GridLayout layout = new GridLayout(4, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);
		setBackground(BACKGROUND_COLOR);
		date1 = buildDateLabel(this);
		type = buildTypeLabel(this);
		icon1 = buildIconLabel(this);
		name_description = buildTextLabel(this);
		date2 = buildDateLabel(this);
		duration = buildDurationLabel(this);
		icon2 = buildIconLabel(this);
		name_details = buildTextLabel(this);
		TIMELINE_PREFERENCES.addPropertyChangeListener(this);
	}
	
	private void updateFont () {
		if (this.getParent().isDisposed())
			return;
		
		FontData fontData = this.getParent().getFont().getFontData()[0];
		//if (font != null) font.dispose();
		font = FontUtils.getStyledFont(null, fontData.getName(),
				TIMELINE_PREFERENCES.getInt(TimelinePreferencePage.P_INFO_FONT_SIZE),
				SWT.NORMAL);
		if (date1 != null) 				date1.setFont(font);
		if (type != null) 				type.setFont(font);
		if (icon1 != null) 				icon1.setFont(font);
		if (name_description != null) 	name_description.setFont(font);
		if (date2 != null) 				date2.setFont(font);
		if (duration != null) 			duration.setFont(font);
		if (icon2 != null) 				icon2.setFont(font);
	}

	/**
	 * Dispose the resources used:
	 * 1. the font
	 */
	@Override
	public void dispose() {
		super.dispose();
		//font.dispose();
		TIMELINE_PREFERENCES.removePropertyChangeListener(this);
	}
	
	private Label buildIconLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setBackground(BACKGROUND_COLOR);
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gridData.widthHint = ICON_SIZE;
		gridData.heightHint = ICON_SIZE;
		label.setLayoutData(gridData);
		return label;
	}
	
	private Label buildTextLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setBackground(BACKGROUND_COLOR);
		label.setForeground(TEXT_COLOR);
		GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
		gridData.minimumWidth = TEXT_COLUMN;
		label.setLayoutData(gridData);
		return label;
	}
	
	private Label buildDateLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setBackground(BACKGROUND_COLOR);
		label.setForeground(TIME_COLOR);
		GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		gridData.widthHint = DATE_COLUMN;
		label.setLayoutData(gridData);
		return label;
	}

	private Label buildTypeLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setBackground(BACKGROUND_COLOR);
		label.setForeground(TYPE_COLOR);
		GridData gridData = new GridData(SWT.END, SWT.CENTER, false, false);
		gridData.widthHint = DURATION_COLUMN;
		label.setLayoutData(gridData);
		label.setAlignment(SWT.RIGHT);
		return label;
	}
	
	private Label buildDurationLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setFont(font);
		label.setBackground(BACKGROUND_COLOR);
		label.setForeground(TIME_COLOR);
		GridData gridData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
		gridData.widthHint = DURATION_COLUMN;
		label.setLayoutData(gridData);
		return label;
	}
	
	/**
	 * Set the first icon to show the supplied image.
	 * @param icon
	 */
	public void setIcon1(Image icon) {
		icon1.setImage(icon);
	}
	
	/**
	 * Set the text for the name/description field.
	 * @param text
	 */
	public void setNameDescription(String text) {
		name_description.setText(text == null ? "" : text);
	}
	
	/**
	 * Set the text for the type field.
	 * 
	 * @param text
	 */
	public void setType(String text) {
		updateGridLabel(type, text);
	}

	/**
	 * Set the second icon to show the supplied image.
	 * @param icon
	 */
	public void setIcon2(Image icon) {
		icon2.setImage(icon);
	}
	
	/**
	 * Set the text for the name/details field.
	 * @param text
	 */
	public void setNameDetails(String text) {
		name_details.setText(text);
	}
	
	/**
	 * Set the first and second dates correspondingly and show the computed duration.
	 * @param date1
	 * @param date2
	 */
	public void setDates(Date date1, Date date2) {
		updateGridLabel(this.date1, DATE_STRINGIFIER.getDisplayString(date1));
		updateGridLabel(this.date2, DATE_STRINGIFIER.getDisplayString(date2));
		long milliseconds = DateUtils.subtract(date2, date1);
		long seconds = Math.round(milliseconds/1000.0);
		updateGridLabel(duration, DurationFormat.getEnglishDuration(seconds));
	}
	
	public void setDates(Date date1, Date date2, Date selectionStart) {
		setDates(date1, date2);
		if (selectionStart != null) {
			long milliseconds = DateUtils.subtract(selectionStart, date1);
			long seconds = Math.round(Math.abs(milliseconds)/1000.0);
			updateGridLabel(name_details, DurationFormat.getEnglishDuration(seconds) + " from selection");
		}
	}

	/**
	 * Print some text into this field, making the fields preferred width larger if necessary
	 * @param label
	 * @param date
	 */
	private void updateGridLabel(Label label, String text) {
		label.setText(text==null?"":text);
		GridData data = (GridData)label.getLayoutData();
		int drawnWidth = label.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
		if (drawnWidth > data.widthHint) {
			data.widthHint = drawnWidth + 5;
		}
		label.redraw();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (TimelinePreferencePage.P_INFO_FONT_SIZE.equals(event.getProperty())) {
			updateFont();
			this.redraw();
		}
	}
	
	/**
	 * Clear all the fields
	 */
	public void clear() {
		date1.setText("");
		type.setText("");
		icon1.setImage(null);
		name_description.setText("");
		date2.setText("");
		duration.setText("");
		icon2.setImage(null);
		name_details.setText("");
	}
	
}
