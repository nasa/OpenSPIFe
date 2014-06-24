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
package gov.nasa.ensemble.core.detail.emf.binding;

import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Widget;

/**
 * A modified implementation of the DataBindings IObservableValue interface for the Nebula
 * CDateTime control. Customized to use a listener that responds to FocusOut eventa as well
 * as the default selected (Enter) event
 * 
 * @author pcentgraf (modified by rnado)
 * @since Mar 8, 2007
 */
public class CDateTimeObservableValue extends AbstractObservableValue implements ISWTObservableValue {
	
	/**
	 * The Control being observed here.
	 */
	protected final CDateTime dateTime;
	
	/**
	 * Flag to prevent infinite recursion in {@link #doSetValue(Object)}.
	 */
	protected boolean updating = false;
	
	/**
	 * The "old" selection before a selection event is fired.
	 */
	protected Date currentSelection;

	private SelectionListener selectionListener = new SelectionListener() {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			if (!updating) {
				Date newSelection = CDateTimeObservableValue.this.dateTime.getSelection();
				fireValueChange(Diffs.createValueDiff(currentSelection, newSelection));
				currentSelection = newSelection;
			}
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			// skip
		}
	};
	
	private FocusListener focusListener = new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
			// skip
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (!updating) {
				Date newSelection = CDateTimeObservableValue.this.dateTime.getSelection();
				fireValueChange(Diffs.createValueDiff(currentSelection, newSelection));
				currentSelection = newSelection;
			}
		}
	};
	
	/**
	 * Observe the selection property of the provided CDateTime control.
	 * @param dateTime the control to observe
	 */
	public CDateTimeObservableValue(CDateTime dateTime) {
		this.dateTime = dateTime;
		currentSelection = dateTime.getSelection();
		this.dateTime.addSelectionListener(selectionListener);
		this.dateTime.addFocusListener(focusListener);
	}

	@Override
	public synchronized void dispose() {
		dateTime.removeSelectionListener(selectionListener);
		dateTime.removeFocusListener(focusListener);
		super.dispose();
	}

	@Override
	protected Object doGetValue() {
		if(!dateTime.isDisposed()) {
			return dateTime.getSelection();
		}
		return null;
	}
	
	@Override
	protected void doSetValue(Object value) {
		if(value instanceof Date && !dateTime.isDisposed()) {
			Date oldValue;
			Date newValue;
			try {
				updating = true;
				oldValue = dateTime.getSelection();
				newValue = (Date) value;
				dateTime.setSelection(newValue);
				currentSelection = newValue;
				fireValueChange(Diffs.createValueDiff(oldValue, newValue));
			} finally {
				updating = false;
			}
		}
	}

	@Override
	public Object getValueType() {
		return Date.class;
	}
	
	@Override
	public Widget getWidget() {
		return dateTime;
	}
	
}
