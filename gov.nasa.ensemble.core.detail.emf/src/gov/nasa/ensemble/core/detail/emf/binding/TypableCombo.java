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
/**
 * 
 */
package gov.nasa.ensemble.core.detail.emf.binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class TypableCombo extends Combo {

	private String[] items;
	private int defaultIndex;

	public TypableCombo(Composite parent, int style) {
		super(parent, style);
		new FilteringAdapter(this);
		// MAE-4628
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				setFocus();
			}
		});
		
		addListener(SWT.Selection, new Listener() {
	        @Override
	        public void handleEvent(Event arg0) {
	            resetIndexes();
	        }
	    });
	}

	@Override
	protected void checkSubclass() {
		// suppress error
	}

	@Override
	public void setItems(String[] items) {
		this.items = items;
		super.setItems(items);
	}

	@Override
	public void select(int index) {
		this.defaultIndex = index;
		filter = new StringBuffer();
		if (getItemCount() != items.length) {
			showItems(items);
		}
		super.select(index);
	}

	public void showItems(String[] items) {
		super.setItems(items);
	}
	
	public void filteredSelect(int index) {
		super.select(index);
		Event event = new Event();
		event.type = SWT.Selection;
		event.item = this;
		event.widget = this;
		event.text = getItem(index);
		event.index = Arrays.asList(items).indexOf(event.text);
		for (Listener listener : getListeners(SWT.Selection)) {
			listener.handleEvent(event);
		}
	}

	public void refreshItems(String filter) {
		if (!isDisposed()) {
			if (filter.length() == 0) {
				showItems(items);
				super.select(defaultIndex);
			} else {
				int index = getSelectionIndex();
				String selectedItem = null;
				if (index != -1) {
					selectedItem = getItem(index);
				}
				List<String> newItems = new ArrayList<String>(items.length);
				for (String item : items) {
					if (item.regionMatches(true, 0, filter, 0, filter.length())) {
						newItems.add(item);
					}
				}
				int matchCount = newItems.size();
				showItems(newItems.toArray(new String[matchCount]));
				if (matchCount > 0) {
					index = newItems.indexOf(selectedItem);
					if (index == -1) {
						index = 0;
					}
//					super.select(index); // this keeps the current item if it matches the filter
					super.select(0);     // this selects the first matching item
				}
			}
		}
	}

	private StringBuffer filter = new StringBuffer();
	
	public class FilteringAdapter implements KeyListener, FocusListener {

		public FilteringAdapter(Control control) {
			control.addKeyListener(this);
			control.addFocusListener(this);
		}

		private void refreshItems() {
			TypableCombo.this.refreshItems(filter.toString());
		}

		@Override
		public void keyPressed(KeyEvent e) {
			e.doit = false;
			if (e.keyCode == SWT.DEL || e.keyCode == SWT.BS) {
				if (filter.length() > 0) {
					filter = new StringBuffer(filter.substring(0, filter.length() - 1));
				}
			} else if (e.keyCode == SWT.SHIFT || e.keyCode == SWT.ALT || e.keyCode == SWT.CONTROL || e.keyCode == SWT.COMMAND ||
					e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
			} else if (e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
				e.doit = true;
			} else if (e.keyCode == SWT.ESC) {
				filter = new StringBuffer();
			} else if (e.character != '\0') {
				filter.append(e.character);
			}
			if (!e.doit) {
				refreshItems();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// Do nothing
		}

		@Override
		public void focusGained(FocusEvent e) {
			filter = new StringBuffer();
		}

		@Override
		public void focusLost(FocusEvent e) {
			resetIndexes();
		}

	}
	
	private void resetIndexes() {
		filter = new StringBuffer();
		int itemCount = getItemCount();
		if (items == null || itemCount == items.length) {
			return;
		}
		String item = null;
		int index = (itemCount != 0 ? getSelectionIndex() : -1);
		if (index != -1) {
			item = getItem(index);
		}
//		System.err.println(" index : " + index + " & item : " + item);
		showItems(items);
		int newIndex = defaultIndex;
		if (index != -1) {
			newIndex = Arrays.asList(items).indexOf(item);
			defaultIndex = newIndex;
		}
//		System.err.println(" newindex : " + newIndex);
		filteredSelect(newIndex);
	}

}
