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
package gov.nasa.arc.spife.ui.timeline.action.dialog;

import gov.nasa.arc.spife.PageUtils;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.preference.TimelineDateFormatRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.thread.ThreadUtils;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.type.editor.TextEditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

/**
 * This dialog class is designed to solicit a specific time from the user.
 * Ideally, the user is shown a range and is asked to specify a time within
 * that range in any of the supported formats.
 * 
 * @author Eugene Turkov
 *
 */
public class GoToTimeDialog extends Dialog{

	public static final String P_SHOW_GOTO_TIMEZONES = "timeline.goto.show.timezones";
	
	/**
	 * the time that has been approved and is the time to use
	 */
	private Date selectedTime;	    
	
	/**
	 * contains the actual Text widget to collect input. Uses a stringifier
	 * to update the text color.
	 */
    private TextEditor textEditor;
    
    /**
     * Shell to be shown that will contain information about the text entered.
     * Normally the tool tip will only show if there is an error message to display
     */
    private static ToolTip toolTip;
    
    // copied from TextEditor to support the tool tip behavior
	private static final ExecutorService pool = 
		ThreadUtils.newCoalescingThreadPool("GoToTimeDialog tooltip revealer");
	
	/**
	 * The message to be displayed in the tool tip
	 */
	private static String helpMessage;
    
    /**
     * Ok button widget.
     */
    private Button okButton;
    
    /**
     * Combo for picking different time formats
     */
	private Combo timeFormatCombo;
	
	/**
	 * The last formats chosen.
	 */
	private static String formatID = null;
    
    private IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);

    private Timeline timeline;
    
	private Label msgLabel;
	
	public GoToTimeDialog(Shell parentShell, Timeline timeline) {
		super(parentShell);
		this.timeline = timeline;
	}
    
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Go to time");
	}
	
	/**
	 * This class looks at the current plan to determine the start and end times
	 * of the plan. Using these times, a request message is put together. The
	 * user is shown the message about the text field and the message is of the
	 * form: "Please enter a time between DATE1> and DATE2"
	 * 
	 * @return the request message to be shown in the tool tip.
	 */
	private String calculateRequestMessage() {
		String start;
		try {
			start = dateStringifier.getDisplayString(getMinStartTime());
		} catch (Exception e) {
			// The range of each format is different so just provide text
			start = "start";
		}
		String end;
		try {
			end = dateStringifier.getDisplayString(getMaxEndTime());
		} catch (Exception e) {
			// The range of each format is different so just provide text
			end = "end";
		}
		return "Scroll the timeline to a specified time between "
			+ start + " and " + end;
	}

	/**
	 * The label that is used to display the calculated message to the user.
	 * 
	 * @param parent
	 * @param composite
	 * @param message
	 * @return the newly created label
	 */
	private Label createLabel(Composite parent, Composite composite, String message) {
		Label label = new Label(composite, SWT.WRAP);
        label.setText(message);
        GridData data = new GridData(GridData.GRAB_HORIZONTAL
                | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_CENTER);
        data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants
        		.MINIMUM_MESSAGE_AREA_WIDTH);
        label.setLayoutData(data);
        label.setFont(parent.getFont());
        
        return label;
	}
	
	/**
	 * The start time of the current page (plan)
	 * 
	 * @return the start time
	 */
	private Date getMinStartTime() {
		return timeline.getPage().getStartTime();
	}
	
	/**
	 * The end time of the current page (plan)
	 * 
	 * @return the end time
	 */
	private Date getMaxEndTime() {
		return timeline.getPage().getExtent().getEnd();
	}
	
	/**
	 * Performs the necessary tasks to enable tool tips to show in conjunction
	 * with the user entering text into the Text widget.
	 * 
	 * @param text
	 */
	private void installTooltipFunctionality(final Text text) {
		final Color defaultTextForegroundColor = text.getForeground();
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					Date startTime = getMinStartTime();
					Date endTime = getMaxEndTime();
					selectedTime = dateStringifier.getJavaObject(
							text.getText(), startTime);
	
					if(selectedTime != null && !PageUtils.isPagingEnabled()
							&& (selectedTime.getTime() < startTime.getTime()
							|| selectedTime.getTime() > endTime.getTime())) {
						helpMessage = "This plan does not include the time '"
							+ dateStringifier.getDisplayString(selectedTime) + "'";
					}
					
					else if (selectedTime != null && !text.getText().trim().equals("")) {
						helpMessage = null;
						okButton.setEnabled(true);
					}
				} catch (ParseException e1) {
					helpMessage = e1.getMessage();
					okButton.setEnabled(false);
				}

				Runnable runnable = createTooltipRunnable(text, defaultTextForegroundColor);
				pool.execute(runnable);
			}
		});		
	}
	
	/**
	 * The runnable that provides tool tip support & functionality. This code is
	 * taken from the TextBindingFactory.
	 * 
	 * @return a runnable to run that will show tool tips
	 */
	private Runnable createTooltipRunnable(final Text text, final Color defaultTextForegroundColor) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				WidgetUtils.runInDisplayThread(text, new Runnable() {
					@Override
					public void run() {
						if (tipAvailable() && helpMessage != null) {
							Point textLocation = text.getLocation();
							Point textSize = text.getSize();
							textLocation = new Point(textLocation.x
									, textLocation.y + textSize.y);
							
							Point tipLocation = WidgetUtils.getDisplay()
								.map(text.getParent(), null, textLocation);
							
							toolTip.setText("Invalid Input");
							toolTip.setMessage(helpMessage);
							toolTip.setLocation(tipLocation);
							toolTip.setVisible(true);
							toolTip.setAutoHide(true);
						}
						
						else {
							toolTip.setVisible(false);
							text.setForeground(defaultTextForegroundColor);
						}
					}
				}, true);
			}
		};
		
		return runnable;
	}
	
	/**
	 * Add the correct layout, add tool tip support, and add a dispose listener
	 * for clean-up.
	 */
	private TextEditor createTextEditor(Composite parent) {
		boolean editable = true;
		TextEditor textEditor = new TextEditor(parent, dateStringifier, editable);
		final Text text = (Text)textEditor.getEditorControl();
		text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL));	
		
		text.setText(dateStringifier.getDisplayString(timeline.getPage().getStartTime()));
		installTooltipFunctionality(text);
		
		text.addDisposeListener(new DisposeListener() {			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (tipAvailable())
					toolTip.dispose();
			}
		});	
		return textEditor;
	}
	
	@Override
	protected Control createDialogArea(final Composite parent) {
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);
		
		if (Boolean.parseBoolean(System.getProperty(P_SHOW_GOTO_TIMEZONES, "false"))) {
			createFormatCombo(composite);
		}
        // create message to show about text field
		String message = calculateRequestMessage();
		// the label that will contain the message
        msgLabel = createLabel(parent, composite, message);
		// allow the user to type in text		
		textEditor = createTextEditor(parent);
		
		applyDialogFont(composite);
		
		if (tipAvailable())
			toolTip.dispose();
		toolTip = new ToolTip(parent.getShell(), SWT.BALLOON | SWT.ICON_ERROR);
		
		return composite;
	}

	/**
	 * This creates a drop down for choosing the time format from the list that
	 * is currently being shown by the timeline. Choosing a different format
	 * allows the user to pick how they'll specify their goto time.
	 */
	@SuppressWarnings("unused")
	private void createFormatCombo(final Composite composite) {
		List<DateFormat> formats = TimelineDateFormatRegistry.getDateFormats();
		if (formats.size() == 0) {
			LogUtil.info("Expected non-empty set of formats");
			return;
		}
		final List<String> ids = new ArrayList<String>();
		for (DateFormat df : formats)
			ids.add(DateFormatRegistry.INSTANCE.getDateFormatID(df));
		
		timeFormatCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		timeFormatCombo.setItems(ids.toArray(new String[ids.size()]));
		final int lastFormat;
		if (formatID != null && ids.contains(formatID))
			lastFormat = ids.indexOf(formatID);
		else
			lastFormat = 0;
		timeFormatCombo.select(lastFormat);
		timeFormatCombo.setEnabled(ids.size() > 1);
		timeFormatCombo.addSelectionListener(
			new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					final String id = timeFormatCombo.getText();
					formatID = id;
					DateFormat df = DateFormatRegistry.INSTANCE.lookupDateFormat(id);
					dateStringifier = new DateStringifier(df);
					msgLabel.setText(calculateRequestMessage());
					composite.getParent().pack(true);
				}
			}
		);
	}

	private static boolean tipAvailable() {
		return toolTip != null && !toolTip.isDisposed();
	}

	public Date getSelectedTime() {
		return this.selectedTime;
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
	protected void createButtonsForButtonBar(Composite parent) {
        // create OK and Cancel buttons by default
        okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        okButton.setEnabled(true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        /*
         * do this here because setting the text will set enablement on the ok
         * button
         */        
        textEditor.getEditorControl().setFocus();
    }
}
