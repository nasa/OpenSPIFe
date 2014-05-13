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
package gov.nasa.ensemble.common.ui.type.editor;

import gov.nasa.ensemble.common.ui.DatePickerDialog;
import gov.nasa.ensemble.common.ui.EnsembleDateWidgetHelper;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

/**
 * Provides a calendar widget for picking a date.
 * (TODO:  If we're keeping this, it should probably be renamed EarthCalendarEditor for clarity.)
 * @deprecated  Anyone using this?  For the only known user of this, this has been replaced per SPF-5492.
 * @see gov.nasa.ensemble.common.ui.type.editor.EnsembleTextBasedDateEditor
 */

@Deprecated
public class EarthDateTypeEditor extends AbstractTypeEditor<Date> {
	
	// private static final Logger trace = Logger.getLogger(EarthDateTypeEditor.class);
	
	private static final String CALENDAR_ICON="cal_16";
	
	private final Control top;
	private final DateTime dateTime;
	private final Button calButton;

    private boolean edited;

    
	public EarthDateTypeEditor(DateTime dt) {
		super(Date.class);
		this.dateTime = dt;
		
		// Since not "parent" composite, assume DT is top level
		this.top=dateTime;
		
		this.calButton= null;
		addListeners();
	}
	
	public EarthDateTypeEditor(Composite parent) {
		this(parent, null); 
	}
	
    public EarthDateTypeEditor(Composite parent, Object value) {
    	super(Date.class);
		super.setObject(value);
		Composite t = new Composite(parent, SWT.NONE);
		t.setLayout(new GridLayout(2, false));
		
    	this.dateTime = createDateTime(t, value);
    	this.calButton= createCalendarWidget(t);
    	this.top = t;
    	addListeners();
    }

	public Control getEditorControl() {
		return top;
	}

	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		
		final Date dateObj = getObject();
		WidgetUtils.runInDisplayThread(dateTime, new Runnable() {
			public void run() {
				EnsembleDateWidgetHelper.setDate(dateTime, dateObj);
			}
		});
	}
	
	protected Button createCalendarWidget(Composite comp) {
		final Button calB = new Button(comp, SWT.PUSH | SWT.FLAT);
		calB.setImage(MissionUIConstants.getInstance().getIcon(CALENDAR_ICON));
		calB.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				Date d = EnsembleDateWidgetHelper.getDate(dateTime); // always use time from dateTime
				Point display = calB.toDisplay(new Point(0, 0));
				DatePickerDialog datePicker = new DatePickerDialog(calButton.getShell(), d, display);
				Object result = datePicker.open();
				if (result instanceof Date && result != null) {
					Date newDate = (Date) result;
					// update the admit date version
					setObject(newDate);
				}
			}

			public void widgetSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});
		return calB;
	}
	
	protected DateTime createDateTime(Composite parent, Object value) {
		
		final DateTime dt = new DateTime(parent, SWT.MEDIUM);
		EnsembleDateWidgetHelper.setDate(dt, (Date) value);
		return dt;
    }
    
    private void addListeners() {
    	DateTimeListener listener = new DateTimeListener();
    	dateTime.addFocusListener(listener);
    	dateTime.addSelectionListener(listener);
    }

    /**
     * The user has input the supplied value.
     *
     * @param newObject
     */
	protected void acceptValue(Object newObject) {
		super.setObject(newObject);
	}

	/**
     * Sets the text back to the original value
     */
	private void resetText() {
		edited = false;
		EnsembleDateWidgetHelper.setDate(dateTime, getObject());
	}
	
	private class DateTimeListener implements FocusListener, KeyListener, SelectionListener {

		public void focusGained(FocusEvent event) {
			edited = false;
		}
		
		public void focusLost(FocusEvent event) {
			if (edited) {
				Date d = EnsembleDateWidgetHelper.getDate(dateTime);
				acceptValue(d);
			}
		}

		public void keyPressed(KeyEvent event) {
			// special keys handled in release
		}
		
		public void keyReleased(KeyEvent event) {
			if (event.keyCode == SWT.ESC) {
				resetText();
			} 
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// When user makes modifications should trigger this event
			Date d = EnsembleDateWidgetHelper.getDate(dateTime);
			acceptValue(d);
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
	}
    
}
