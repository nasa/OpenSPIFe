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
package gov.nasa.ensemble.common.ui.preferences.time;

import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.util.Date;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * A field editor for an date/time type preference in ISO8601 format (2006-04-07T05:09:42).
 */
public class ISO8601FieldEditor extends StringFieldEditor {

    private static final int DEFAULT_TEXT_LIMIT = "2006-04-07T05:09:42".length();

    /**
     * Creates a new iso8601-format date/time field editor 
     */
    protected ISO8601FieldEditor() {
		// nothing to do
    }

    /**
     * Creates an iso8601-format date/time field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public ISO8601FieldEditor(String name, String labelText, Composite parent) {
        this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
    }

    /**
     * Creates an iso8601-format date/time field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    public ISO8601FieldEditor(String name, String labelText, Composite parent,
            int textLimit) {
        init(name, labelText);
        setEmptyStringAllowed(false);
        setErrorMessage(JFaceResources.getString("Use 2006-01-02T03:04:05 syntax."));
        createControl(parent);
    }

	/**
	 * Expose protected method
	 */
	@Override
	public Text getTextControl() {
		return super.getTextControl();
	}

    /* (non-Javadoc)
     * Method declared on StringFieldEditor.
     * Checks whether the entered String is a valid iso8601-format date/time or not.
     */
    @Override
	protected boolean checkState() {

        Text text = getTextControl();

        if (text == null)
            return false;

        String string = text.getText();
        try {
            Date date = ISO8601DateFormat.parseISO8601(string);
            if (date != null) {
                clearErrorMessage();
                return true;
            }
			showErrorMessage();
			return false;
        } catch (NumberFormatException e1) {
            showErrorMessage();
        }

        return false;
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doLoad() {
        Text text = getTextControl();
        if (text != null) {
            String stringValue = getPreferenceStore().getString(getPreferenceName());
            Date date = ISO8601DateFormat.parseISO8601(stringValue);
            text.setText(ISO8601DateFormat.formatISO8601(date));
        }

    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doLoadDefault() {
        Text text = getTextControl();
        if (text != null) {
            String value = getPreferenceStore().getString(getPreferenceName());
            text.setText(value);
        }
        valueChanged();
    }

    /* (non-Javadoc)
     * Method declared on FieldEditor.
     */
    @Override
	protected void doStore() {
        Text text = getTextControl();
        if (text != null) {
            getPreferenceStore().setValue(getPreferenceName(), text.getText());
        }
    }

    /**
     * Returns this field editor's current value as a Date object
     *
     * @return the value
     * @exception NumberFormatException if the <code>String</code> does not
     *   contain a parsable iso8601 date/time
     */
    public Date getDateValue() throws NumberFormatException {
    	    Text text = getTextControl();
        if (text != null) {
    		   Date date = ISO8601DateFormat.parseISO8601(text.getText());
    		   if (date != null) return date;
        }
        throw new NumberFormatException("Invalid time syntax:  Use ISO8602, e.g. 2006-01-02T12:45:56");
    }
}
