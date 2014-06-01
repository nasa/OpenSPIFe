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

import gov.nasa.ensemble.common.thread.ThreadUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;

import java.util.concurrent.ExecutorService;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolTip;

public class TextEditor extends AbstractTypeEditor<Object> {
	
	private static final Color INVALID_INPUT_COLOR = ColorConstants.red;
	
	private Text text;
	private IStringifier stringifier;
    private boolean edited = false;

    private static final ExecutorService pool = ThreadUtils.newCoalescingThreadPool("TextBindingFactory bubble revealer");
    private static ToolTip bubble;
    
	public TextEditor(Text text, IStringifier stringifier) {
		super(Object.class);
		this.stringifier = stringifier;
		this.text = text;
		addListeners();
		bubble = new ToolTip(text.getShell(), SWT.BALLOON | SWT.ICON_ERROR);
	}

	public TextEditor(Composite parent, IStringifier stringifier, boolean editable) {
    	super(Object.class);
		this.stringifier = stringifier;
    	this.text = createText(parent, editable);
		addListeners();
		if (text != null)
			bubble = new ToolTip(text.getShell(), SWT.BALLOON | SWT.ICON_ERROR);
    }

	@Override
	public Control getEditorControl() {
		return text;
	}

	@Override
	public void setObject(Object o) {
		super.setObject(o);
		updateText();
	}
	
	protected void acceptValue(Object object) {
		setObject(object);
	}
	
	protected final void updateText() {
		WidgetUtils.runInDisplayThread(text, new Runnable() {
			@Override
			@SuppressWarnings("unchecked")
			
			public void run() {
				String string = stringifier.getDisplayString(getObject());
				if (!string.equals(text.getText())) {
					// the above check prevents the cursor from going 
					// to the start of the line when pressing return.
					text.setText(string);
					updateValidityColor();
				}
			}
		});
	}

	/**
     * Sets the text back to the original value
     */
	@SuppressWarnings("unchecked")
	private void resetText() {
		edited = false;
		text.setText(stringifier.getDisplayString(getObject()));
		updateValidityColor();
	}

	/**
	 * Sets the validity color according to the current text.
	 */
	private void updateValidityColor() {
		String string = text.getText();
		try {
			@SuppressWarnings({"unchecked", "unused"})
			Object object = stringifier.getJavaObject(string, getObject());
			clearError(text);
		} catch (Exception e) {
			text.setForeground(INVALID_INPUT_COLOR);
			showError(text, e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private Text createText(Composite parent, boolean editable) {
		String string = stringifier.getDisplayString(getObject());
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

	private void addListeners() {
		TextListener listener = new TextListener();
		text.addFocusListener(listener);
		text.addKeyListener(listener);
		text.addModifyListener(listener);
	}
	
	public static void showError(final Text text, final String msg) {
		text.setForeground(INVALID_INPUT_COLOR);
		text.setToolTipText(msg);
		if (bubble != null) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					WidgetUtils.runInDisplayThread(text, new Runnable() {
						@Override
						public void run() {
							Point textLocation = text.getLocation();
							Point textSize = text.getSize();
							textLocation = new Point(textLocation.x, textLocation.y + textSize.y);
							Point bubbleLocation = WidgetUtils.getDisplay().map(text.getParent(), null, textLocation);
							bubble.setText("Invalid Input");
							bubble.setMessage(msg == null ? "" : msg);
							bubble.setLocation(bubbleLocation);
							bubble.setVisible(true);
							bubble.setAutoHide(true);
						}
					}, true);
				}
			});
		}
	}
	
	public static void clearError(Text text) {
		text.setForeground(null);
		if (bubble != null && bubble.isVisible()) {
			bubble.setVisible(false);
		}
	}
	
	private class TextListener implements FocusListener, KeyListener, ModifyListener {

		@Override
		public void modifyText(ModifyEvent event) {
			edited = true;
			updateValidityColor();
		}

		@Override
		public void keyPressed(KeyEvent event) {
			// special keys handled in release
		}
		
		@Override
		public void keyReleased(KeyEvent event) {
			if (event.keyCode == SWT.ESC) {
				resetText();
			} else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
				if (text.getLineCount() == 1) {
					event.doit = false;
					String string = text.getText();
					try {
						@SuppressWarnings("unchecked")
						Object object = stringifier.getJavaObject(string, getObject());
						setObject(object);
					} catch (Exception e) {
						// ignore carriage return on an invalid value
					}
				}
			}
		}

		@Override
		public void focusGained(FocusEvent event) {
			edited = false;
		}
		
		@Override
		public void focusLost(FocusEvent event) {
			if (edited) {
				String string = text.getText();
				try {
					@SuppressWarnings("unchecked")
					Object object = stringifier.getJavaObject(string, getObject());
					setObject(object);
				} catch (Exception e) {
					// reset the text if losing focus on an invalid value
					resetText();
				}
			}
		}
	}
}
