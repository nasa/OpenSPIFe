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
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.date.defaulting.DefaultDateUtil;

import java.text.ParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class StringTypeEditor<T> extends AbstractTypeEditor<Object> {

	public static final Color INVALID_INPUT_COLOR = ColorConstants.red;
	public static final Color VALID_COLOR = ColorConstants.black;

	private final Text text;
	private final IStringifier<Object> stringifier;

	private ToolTip tip = null;

	private boolean edited;

	public StringTypeEditor(IStringifier<Object> stringifier, Text text) {
		super(Object.class);
		this.stringifier = stringifier;
		this.text = text;
		addTextListeners();

		tip = new ToolTip(text.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
	}

	public StringTypeEditor(IStringifier<Object> stringifier, Composite parent, Object value, boolean editable) {
		super(Object.class);
		this.stringifier = stringifier;
		super.setObject(value);
		this.text = createText(parent, value, editable);
		addTextListeners();
		tip = new ToolTip(text.getShell(), SWT.BALLOON | SWT.ICON_ERROR);

		// For some reason, applying a color at creation, does not get it to update correctly
		// so simply do it next in the queue.
		parent.getDisplay().asyncExec(new Runnable() {
			public void run() {
				updateValidityColor(false); // make sure the color is set right in the case where you ALLOW errors.
			}
		});
	}

	public Text getEditorControl() {
		return text;
	}

	@Override
	public void setObject(final Object object) {
		super.setObject(object);
		WidgetUtils.runInDisplayThread(text, new Runnable() {
			public void run() {
				String string = stringifier.getDisplayString(object);
				if (string == null) {
					string = "";
				}
				Point selection = text.getSelection();
				text.setText(string);
				// If canonicalized, assume extra characters were added to end,
				// and select them in case user is typing without looking.
				// E.g., "http:" may turn into "http://"
				int oldInsertionPoint = selection.x;
				int endOfNewText = string.length();
				text.setSelection(Math.min(oldInsertionPoint, endOfNewText), endOfNewText);
				updateValidityColor();
			}
		});
	}

	/**
	 * Inspect the object *without* validation. Purpose: This version does *not* call resetText() in the event of a validation error.
	 */
	public Object getObjectRaw() throws ParseException {
		if (edited) {
			String string = text.getText();
			Object object = stringifier.getJavaObject(string, StringTypeEditor.super.getObject());
			acceptValue(object);
		}
		return super.getObject();
	}

	/**
	 * Force validation prior to return.
	 */
	@Override
	public Object getObject() {
		if (edited) {
			String string = text.getText();
			try {
				Object object = stringifier.getJavaObject(string, StringTypeEditor.super.getObject());
				acceptValue(object);
			} catch (SoftOutOfBoundsException e) {
				acceptValue(e.getJavaObject());
				if (tip.isVisible()) {
					tip.setVisible(false);
				}
			} catch (OutOfBoundsException e) {
				resetText();
				if (tip.isVisible()) {
					tip.setVisible(false);
				}
			} catch (Exception e) {
				// reset the text if losing focus on an invalid value
				resetText();
			}
		}
		return super.getObject();
	}

	protected Text createText(Composite parent, Object value, boolean editable) {
		String string = stringifier.getDisplayString(value);
		int multiLineFlags = ((string.length() > 50) ? (SWT.MULTI | SWT.WRAP) : 0);
		int editableFlags = (editable ? SWT.BORDER : SWT.READ_ONLY);
		int flags = editableFlags | multiLineFlags;
		if (((flags & SWT.WRAP) != 0) && (string.indexOf(' ') == -1)) {
			// workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=111704
			flags &= ~SWT.WRAP;
		}
		final Text text = new Text(parent, flags);
		text.setText(string);
		return text;
	}

	private void addTextListeners() {
		TextListener listener = new TextListener();
		text.addFocusListener(listener);
		text.addKeyListener(listener);
		text.addModifyListener(listener);
	}

	/**
	 * The user has input the supplied value.
	 * 
	 * @param newObject
	 */
	protected void acceptValue(Object newObject) {
		super.setObject(newObject);
		edited = false;
	}

	/**
	 * Sets the text back to the original value
	 */
	private void resetText() {
		edited = false;
		String str = stringifier.getDisplayString(super.getObject());
		text.setText(str == null ? "" : str);
		updateValidityColor();
	}

	private void updateValidityColor() {
		updateValidityColor(true);
	}

	/**
	 * Sets the validity color according to the current text.
	 * 
	 * ShowTip, determines if a "tip" about the error should be displayed
	 */
	private void updateValidityColor(boolean showTip) {
		if (!text.isDisposed()) { // No point updating color, if widget is disposed
			String string = text.getText();
			try {
				@SuppressWarnings({ "unused" })
				Object object = stringifier.getJavaObject(string, getDefaultObject(null));
				if (text.getForeground() == null || !text.getForeground().equals(VALID_COLOR)) {
					text.setForeground(VALID_COLOR);
				}
				if (tip.isVisible()) {
					tip.setVisible(false);
				}
			} catch (Exception e) {
				text.setForeground(INVALID_INPUT_COLOR);

				Point textLocation = text.getLocation();
				Point textSize = text.getSize();
				textLocation = new Point(textLocation.x, textLocation.y + textSize.y);

				Point tipLocation = WidgetUtils.getDisplay().map(text.getParent(), null, textLocation);

				if (showTip) {
					tip.setText("Invalid Input");
					String msg = e.getMessage();
					if (msg == null) {
						msg = "";
					}
					tip.setMessage(msg);
					tip.setLocation(tipLocation);
					tip.setVisible(true);
				}
			}
		}
	}

	private Object getDefaultObject(Object target) {
		Object normalDefault = super.getObject();
		if (normalDefault != null)
			return normalDefault;
		else
			return DefaultDateUtil.tryHarderToFindDefaultDateIfApplicable(target, stringifier);
	}

	private class TextListener implements FocusListener, KeyListener, ModifyListener {

		public void modifyText(ModifyEvent event) {
			edited = true;
			updateValidityColor();
		}

		public void keyPressed(KeyEvent event) {
			// special keys handled in release
		}

		public void keyReleased(KeyEvent event) {
			if (event.keyCode == SWT.ESC) {
				resetText();
			} else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
				if (text.getLineCount() == 1) {
					event.doit = false;
					String string = text.getText();
					try {
						Object object = stringifier.getJavaObject(string, StringTypeEditor.super.getObject());
						acceptValue(object);
					} catch (Exception e) {
						// ignore carriage return on an invalid value
					}
				}
			}
		}

		public void focusGained(FocusEvent event) {
			edited = false;
		}

		public void focusLost(FocusEvent event) {
			if (edited && !text.isDisposed()) {
				String string = text.getText();
				try {
					Object object = stringifier.getJavaObject(string, StringTypeEditor.super.getObject());
					acceptValue(object);
				} catch (SoftOutOfBoundsException e) {
					acceptValue(e.getJavaObject());
				} catch (Exception e) {
					// reset the text if losing focus on an invalid value
					resetText();
				}
			}
			if (!tip.isDisposed() && tip.isVisible()) {
				tip.setVisible(false);
			}
		}
	}

}
