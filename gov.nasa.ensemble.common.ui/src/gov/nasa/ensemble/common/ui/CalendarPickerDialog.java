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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.mission.MissionConstants;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class CalendarPickerDialog extends Dialog {
	private Date initialDate;
	private Object result;
	private final Point location;

	public CalendarPickerDialog(Shell parent, Date date, Point location) {
		super(parent, SWT.CALENDAR | SWT.BORDER);
		this.initialDate = date;
		this.location = location;
	}

	public Object open() {

		// create the modal dialog
		Shell parent = getParent();
		final Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		if (location != null) {
			dialog.setLocation(location);
		}
		dialog.setLayout(new GridLayout(1, false));
		dialog.setText(getText());

		// set the date to the admit date
		final DateTime dateTime = new DateTime(dialog, SWT.CALENDAR);

		DateTimeWidgetHelper.setDate(dateTime, initialDate);
		dateTime.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (e.y > 20) {
					result = DateTimeWidgetHelper.getDate(dateTime);
					dialog.close();
				}
			}
		});
		dateTime.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
					result = DateTimeWidgetHelper.getDate(dateTime);
					dialog.close();
				}
			}
		});
		dateTime.setFocus();

		// open the dialog
		dialog.pack();
		dialog.open();

		// wait for it to close
		Display display = parent.getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	private static class DateTimeWidgetHelper {

		private static Date initialDate;

		public static void setDate(DateTime dt, Date date) {
			if (date == null) {
				initialDate = new Date();
			}
			initialDate = date;
			Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
			calendar.setTime(initialDate);

			dt.setYear(calendar.get(Calendar.YEAR));
			dt.setMonth(calendar.get(Calendar.MONTH));
			dt.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		}

		/**
		 * get the date from the mission calendar
		 * 
		 * @param dt
		 * @return Date
		 */
		public static Date getDate(DateTime dt) {
			Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
			calendar.setTime(initialDate);
			calendar.set(Calendar.YEAR, dt.getYear());
			calendar.set(Calendar.MONTH, dt.getMonth());
			calendar.set(Calendar.DAY_OF_MONTH, dt.getDay());
			return calendar.getTime();
		}
	}
}
