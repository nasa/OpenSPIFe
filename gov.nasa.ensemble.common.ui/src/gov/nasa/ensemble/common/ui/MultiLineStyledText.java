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

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A multi line styled text widget which automatically grows to some
 * max size as text is entered.
 * 
 * There is currently a bug that screws with layout and sizing
 * when a new line is pressed. 
 * See bug: https://bugs.eclipse.org/bugs/show_bug.cgi?id=140519
 * 
 * If you start to use this class, please email Alex Eiser at 
 * aeiser@mail.arc.nasa.gov
 * 
 * WARN: Do not set the layout style on this composite, it is requried to be
 * set internally
 * 
 * @author alexeiser
 *
 */
public class MultiLineStyledText extends Composite {

	private static final Logger trace = Logger.getLogger(MultiLineStyledText.class);
	/**
	 * The StyledText object which is used as the main text area
	 */
	protected StyledText stext;
	
	/**
	 * The layout data associated with the styledText.
	 */
	protected FormData stextFD;
	
	/**
	 * A boolean which forces a wait until the first pain attempt
	 */
	protected boolean initialPaint = true;
	
	/**
	 * The minimum line height used for the styled text area
	 */
	final public static int minLineHeight = 2;
	
	/**
	 * The maximum number of lines to use for the height, after which a scroll bar is used
	 */
	final public static int maxLineHeight = 25;
	
	// Initial line hieght
	protected int lineHeight = 3;
	
	
	protected int width = 0;
	
	public MultiLineStyledText(Composite parent, int style) {
		super (parent, style);
		
		// Set the layout on this control
		FormLayout layout = new FormLayout();
		this.setLayout(layout);
		
		// Create the styled text object with a vertical scroll.
		stext = new StyledText(this, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		stext.setData("StyledText","comp.stext");
		stext.setWordWrap(true);
		stext.setEditable(true);
		
		
		// Set the initial field height
		int height = stext.getLineHeight() * lineHeight;
		stextFD = new FormData();
		stextFD.top = new FormAttachment(0);
		stextFD.left = new FormAttachment(0);
		stextFD.right = new FormAttachment(100);
		stextFD.bottom = new FormAttachment(0, height + 8);
		
		stext.setLayoutData(stextFD);
		
		// Handle resize events on the parent, to update the width 
		parent.addControlListener(new ControlAdapter() { 
			@Override
			public void controlResized(ControlEvent e) {
//				int parentWidth = getParent().getClientArea().width;
//				int x = getLocation().x;
//				width = (parentWidth - x) - 10; // getClientArea().width;//parentWidth - x - stext.getVerticalBar().getSize().x ;
//				System.out.println("Width " + width);
//				int width = getClientArea().width;
//				stextFD.right = new FormAttachment(0, width);
				
				// Call the relayout code to decide the new size.
//				Control[] ctrl = {stext};
				
				
//				getShell().layout(true, true);
//				redraw();
				
				//System.out.println("Parent Size");
				
//				updateTextSize();
//				
//				layout(true, true);
//				redraw();
				
//				layout(true, true);

//				TableWrapData d = (TableWrapData) getLayoutData();
//				d.maxWidth=width;
				
//				stextFD.width = width;
				
//				redraw();
				
			}
		});
	
		// Handle resize events, so the 
		stext.addControlListener(new ControlAdapter() { 
			@Override
			public void controlResized(ControlEvent e) {
				// Do to some bug I don't understand... the size is not available correctly on first paint... so 
				// wait till an initial update to draw.
//				if (initialPaint) return;
//				
//				// Call the relayout code to decide the new size.
				initialPaint = true;
//				updateTextSize();
//				layout(true, true);
//				redraw();
				
			}
		});
		
		// When users make "modifications" update the text field size.
		stext.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateTextSize();
			}
			
		});
		
		// Work around for an anoying problem which is caused by the view unable
		// to size the text properly on the initial paint. (getting the text size
		// is incorect.)
		stext.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent e) {
				// Painted once, update the display with any textual information.
				if (initialPaint) {
					initialPaint = false;
					updateTextSize();
				}
//				layout(true, true);
//				redraw();
				
			}
			
		});
		// Provide an initial EMPTY string content.
		stext.setText("");
		
	}
	
	
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point p = super.computeSize(wHint, hHint, changed);
		int max = getClientArea().width;
		//System.out.println(max + " " + p.x + " " + changed  );
		if (p.x > max ) {
			p.x = max;
		}
		layout(true, true);
		redraw();
		
		return p;
	}
	
	public StyledText getTextControl() {
		return stext;
	}
	
	/**
	 * Null inputs will be replaced with an empty string.
	 * @param text
	 */
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		if (! isDisposed()) {
			stext.setText(text);
			updateTextSize();
		}
		else {
			trace.debug("SET TEXT DISPOSED");
		}
	}
	
	public String getText() {
		return stext.getText();
	}
	
	public void addModifyListener(ModifyListener modifyListener) {
		stext.addModifyListener(modifyListener);
	}
	
	@Override
	public void addFocusListener(FocusListener focusListener) {
		stext.addFocusListener(focusListener);
	}
	
	@Override
	public void addDisposeListener(DisposeListener dipsoseListener) {
		stext.addDisposeListener(dipsoseListener);
	}
	
	/**
	 * Compute the number of lines that are needed for the view.
	 * Gets the text bounds, and computes the number of lines needed for that.
	 * Assumes that each line takes up the common getLineHeight.
	 * @return
	 */
	private int calculateNeededLineCount() {
		// Initalize the new height to the old height
		int newLineHeight = lineHeight;
		
		if (stext.getText().length() == 0) {
			newLineHeight = 3;
		}
		else {
			String text = stext.getText();
			Rectangle textRect = stext.getTextBounds(0, text.length() -1);
			
			double lineHeight = stext.getLineHeight();

			double lHeigh = textRect.height / lineHeight;
			newLineHeight = (int) Math.ceil(lHeigh);
			
			
			// Because of bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=140519
			// Add a new line for the last return, and if the char before that is also a 
			// return add a nother one.
			if (text.charAt(text.length() -1) == '\n') {
				newLineHeight++;
				if (text.charAt(text.length() -2) == '\n') {
					newLineHeight++;
				}
			}
			
			// bound the height of the box
			if (newLineHeight < minLineHeight) {
				newLineHeight = minLineHeight;
			}
			if (newLineHeight > maxLineHeight) {
				newLineHeight = maxLineHeight;
			}
		}
		return newLineHeight;
	}
	
	private void updateTextSize() {
		// Do to some bug I don't understand... the size is not available correctly on first paint... so 
		// wait till an initial update to draw.
		if (initialPaint) return;
		
		// Calculate the number of lines that should be displayed.
		int newLineHeight = calculateNeededLineCount();
		// Only update the size of the line height is right
		if (newLineHeight != lineHeight) {
			lineHeight = newLineHeight;
		
			// Compute the new height, based on a line and the number of lines to use
			int height = stext.getLineHeight() * lineHeight;
			height += 8; // Using the "Form" layout, there is a difference in how the offsets
			// are handled. This it appears that 8 PX is taken up by the borders... 
			// but this is a guess
			
			// Only increase the height, if its larger then the current value.
			if (stextFD.bottom.offset < height) {
				stextFD.bottom = new FormAttachment(0, height);
			}
			
			getShell().layout(true, true);			
			// Keep the scrolled position the same.
			stext.showSelection();
		}
	}
}
