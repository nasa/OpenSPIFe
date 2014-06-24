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

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
/**
 * Provides a text field for editing the date as text the registered format.
 * (Works for Earth time, at least.  Mars time to be tested if needed.)
 * Tells the wizard to display a help message and disable Finish while syntax is not valid.
 * @author kanef
 * @since SPF-5492
 */
public class EnsembleTextBasedDateEditor extends AbstractTypeEditor<Date> {

	private final Text textField;
	private final EnsembleWizardPage wizardPage;
	
	private DateStringifier dateStringifier;

	public EnsembleTextBasedDateEditor(EnsembleWizardPage wizardPage, Composite parent, Date initialValue) {
 		this(wizardPage, parent, initialValue, SWT.NONE);
	}
	
	public EnsembleTextBasedDateEditor(EnsembleWizardPage wizardPage, Composite parent, Date initialValue,
			int style) {
		this(wizardPage, parent, initialValue, style, DateFormatRegistry.INSTANCE.getDefaultDateFormat());
	}
 	
	public EnsembleTextBasedDateEditor(EnsembleWizardPage wizardPage, Composite parent, Date initialValue,
		int style, DateFormat dateFormat) {
	  	super(Date.class);
		super.setObject(initialValue);
		textField = new Text(parent, style);
		this.wizardPage = wizardPage;
		dateStringifier = new DateStringifier(dateFormat);
		resetText();
		addListeners();
	}
	
	@Override
	public Control getEditorControl() {
		return textField;
	}
	
	public void setEnabled(boolean enabled) {
		textField.setEnabled(enabled);
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
		textField.setText(dateStringifier.getDisplayString(getObject()));
	}
	
	private void addListeners() {
		UpdateListener listener = new UpdateListener();
		textField.addModifyListener(listener);
		}
	
	private class UpdateListener implements ModifyListener, SelectionListener {

		private void update() {
			try {
				acceptValue(dateStringifier.getJavaObject(textField.getText(), getObject()));
				if (wizardPage != null) {
					wizardPage.clearError(this.getClass());
				} else {
					textField.setForeground(ColorConstants.black);
				}
			} catch (ParseException e) {
				if (wizardPage != null) {
					wizardPage.setError(this.getClass(), e.getMessage());
				} else {
					textField.setForeground(ColorConstants.red);
				}
			}
		}
		
		@Override
		public void modifyText(ModifyEvent e) {
			update();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			update();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			update();
		}

	}

}
