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

/*
 * 
 * (c) Copyright IBM Corp. 2000, 2001.
 * 
 * All Rights Reserved.
 *  
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

public class SimpleSpinner extends Composite {

	static final int BUTTON_WIDTH = 16;
	Text text;
	Button up, down;
	int minimum, maximum;

	public SimpleSpinner(Composite parent, int style) {
		super(parent, style);
		text = new Text(this, style | SWT.SINGLE | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);
		text.addListener(SWT.Verify, new Listener() {
			@Override
			public void handleEvent(Event e) {
				verify(e);
			}
		});

		text.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event e) {
				doTraverse(e);
			}
		});

		up.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				up();
			}
		});

		down.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				down();
			}
		});

		addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				resize();
			}
		});

		addListener(SWT.FocusIn, new Listener() {
			@Override
			public void handleEvent(Event e) {
				focusIn();
			}
		});

		text.setFont(getFont());
		minimum = 0;
		maximum = 9;
		setSelection(minimum);
	}

	void verify(Event e) {
		try {
			Integer.parseInt(e.text);
		} catch (NumberFormatException ex) {
			e.doit = false;
		}
	}

	void doTraverse(Event e) {
		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_PREVIOUS:
			if (e.keyCode == SWT.ARROW_UP) {
				e.doit = true;
				e.detail = SWT.NULL;
				up();
			}
			break;

		case SWT.TRAVERSE_ARROW_NEXT:
			if (e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
				e.detail = SWT.NULL;
				down();
			}
			break;
		}
	}

	void up() {
		setSelection(getSelection() + 1);
		notifyListeners(SWT.Selection, new Event());
	}

	void down() {
		setSelection(getSelection() - 1);
		notifyListeners(SWT.Selection, new Event());
	}

	void focusIn() {
		text.setFocus();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}

	public void setSelection(int selection) {
		if (selection < minimum) {
			selection = minimum;
		} else if (selection > maximum) {
			selection = maximum;
		}
		text.setText(String.valueOf(selection));
		text.selectAll();
		text.setFocus();
	}

	public int getSelection() {
		return Integer.parseInt(text.getText());
	}

	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMinimum() {
		return minimum;
	}

	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - BUTTON_WIDTH;
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH,
				buttonHeight);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent(String.valueOf(maximum));
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);
		int width = pt.x + BUTTON_WIDTH;
		int height = pt.y;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width, height);
	}

	public void addSelectionListener(SelectionListener listener) {
		if (listener == null)
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		addListener(SWT.Selection, new TypedListener(listener));
	}

	public Text getText() {
		return text;
	}

	public void addTextKeyListener(KeyListener listener) {
		if (listener == null)
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		text.addKeyListener(listener);
	}

}
