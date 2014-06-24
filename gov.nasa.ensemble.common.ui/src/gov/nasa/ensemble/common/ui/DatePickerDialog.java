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



import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DatePickerDialog extends Dialog {
	Date initialDate;
	Object result;
	private final Point location;
	public DatePickerDialog (Shell parent, Date date, Point location) {
		super(parent, SWT.CALENDAR | SWT.BORDER);
		this.initialDate = date;
		this.location = location;
	}
	public Object open () {
		
		// create the modal dialog
		Shell parent = getParent();
		final Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		if (location != null) {
			dialog.setLocation(location);
		}
		dialog.setLayout (new GridLayout (1, false));
		dialog.setText(getText());

		// set the date to the admit date
		final DateTime dateTime = new DateTime (dialog, SWT.CALENDAR);
		
		EnsembleDateWidgetHelper.setDate(dateTime, initialDate);
		
		// create the buttons
		Composite composite = new Composite(dialog, SWT.NONE);
		composite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Button ok = new Button (composite, SWT.PUSH);
		ok.setText ("OK");
		ok.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				result = EnsembleDateWidgetHelper.getDate(dateTime);
				dialog.close();
			}
		});
		Button cancel = new Button(composite, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addSelectionListener (new SelectionAdapter () {
			@Override
			public void widgetSelected (SelectionEvent e) {
				dialog.close();
			}
		});
		dialog.setDefaultButton (ok);
		
		// open the dialog
		dialog.pack ();
		dialog.open ();

		// wait for it to close
		Display display = parent.getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return result;
	}
}
