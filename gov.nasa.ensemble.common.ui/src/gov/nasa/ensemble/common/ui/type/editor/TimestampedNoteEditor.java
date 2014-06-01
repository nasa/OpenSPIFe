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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;


public class TimestampedNoteEditor extends AbstractTypeEditor<TimestampedNote> {

	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(TimestampedNoteEditor.class);
	protected static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	private final Composite parent;
	private final Composite noteComposite;
	private Text noteField;
	private Text creationTimestamp;
	private Text modificationTimestamp;
	FormToolkit toolkit;
	
	TimestampedNote storedObject = new TimestampedNote();
	
	public TimestampedNoteEditor(Composite parent) {
		super(TimestampedNote.class);
		this.parent = parent;
		toolkit = new FormToolkit(parent.getDisplay());
		this.noteComposite = createNoteComposite();
	}
	
	@Override
	public Control getEditorControl() {
		return noteComposite;
	}
	
	@Override
	public TimestampedNote getObject() {
		return storedObject;
	}

	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		// Set the local cache, then force a redisplay of the fields.
		if (object != null) {
			storedObject = (TimestampedNote) object;
		}
		WidgetUtils.runInDisplayThread(parent, new Runnable() {
			@Override
			public void run() {
				if (noteField != null) {
					noteField.setText(storedObject.value==null? "" : storedObject.value);
				}
				if (creationTimestamp != null) {
					setDateTextInField(creationTimestamp, storedObject.created);
				}
				if (modificationTimestamp != null) {
					setDateTextInField(modificationTimestamp, storedObject.modified);
				}
			}
		});
		}
		
	private Composite createNoteComposite() {
		final Composite noteComposite = new Composite(parent, SWT.NONE);
		
		@SuppressWarnings("unused")
		int fontSize = 12;
		try {noteComposite.getFont().getFontData()[0].getHeight();}
		catch (Exception e) {fontSize = 12;}

		GridLayout gl = new GridLayout(2, false);
		gl.marginBottom=0;
		gl.marginLeft =0;
		gl.marginRight =0;
		gl.marginTop =0;
		noteComposite.setLayout(gl);
		{GridData layoutData = new GridData();
		layoutData.verticalAlignment = SWT.TOP;
		layoutData.grabExcessHorizontalSpace = true;
		noteComposite.setLayoutData(layoutData);
		}
		
		noteField = toolkit.createText(noteComposite, storedObject.value);

		{GridData layoutData = new GridData();
		layoutData.horizontalSpan = 2;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		noteField.setLayoutData(layoutData);
		}
		
		{GridData layoutData = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL);
//		layoutData.widthHint=fontSize*"12/31/2999 24:00:00".length();
		toolkit.createLabel(noteComposite, "Created");
		creationTimestamp = new Text(noteComposite, SWT.READ_ONLY);
		creationTimestamp.setLayoutData(layoutData);
		toolkit.createLabel(noteComposite, "Last Modified");
		modificationTimestamp = new Text(noteComposite, SWT.READ_ONLY);
		modificationTimestamp.setLayoutData(layoutData);
		setDateTextInField(creationTimestamp, storedObject.created);
		setDateTextInField(modificationTimestamp, storedObject.modified);
		}
		
		noteField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent arg0) {
				// No-op				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				{
					final Date now = new Date();
					final String string = noteField.getText();
					if (string != null) {
						if (storedObject.value==null || !storedObject.value.equals(string)) {
							TimestampedNote oldStoredObject = storedObject;
							storedObject = new TimestampedNote();
							storedObject.value = string;
							storedObject.created = oldStoredObject.created;
							if (oldStoredObject.created==null && creationTimestamp != null) {
								storedObject.created = now;
							}
							if (modificationTimestamp != null) {
								storedObject.modified = now;
							}
							firePropertyChange(oldStoredObject, storedObject);
							WidgetUtils.runInDisplayThread(parent, new Runnable() {
								@Override
								public void run() {
									setDateTextInField(creationTimestamp, storedObject.created);
									setDateTextInField(modificationTimestamp, storedObject.modified);		
								}
							});
						}
					}
				}				
			}
		});

		GridDataFactory.createFrom(new GridData(SWT.TOP)).hint(25, SWT.DEFAULT).grab(false,false);
		
		return noteComposite;
	}
	
	private void setDateTextInField(Text field, Date date) {
		if (date==null) {
			field.setText("");
		} else {
			field.setText(DateFormatUtils.format(date, "MM/dd/yyyy hh:mm:ss"));
		}
	}

}
