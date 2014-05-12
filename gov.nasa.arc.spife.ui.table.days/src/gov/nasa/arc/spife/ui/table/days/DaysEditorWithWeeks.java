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
package gov.nasa.arc.spife.ui.table.days;


import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class DaysEditorWithWeeks extends DaysEditor {
	
	private String editorId;
	private Composite weeksScrollComposite;
	private Button scrollLeftButton;
	private Button scrollRightButton;
	private Composite weeksComposite;
	private Button[] weekButtons;
	private int numWeeks = 0;
	private int firstWeekOffset = 0;
	private int baseWeekIndex = 0;
	private int selectedWeek = 0;
	
	public DaysEditorWithWeeks() {
		super();
	}
	
	public DaysEditorWithWeeks(String editorId) {
		super();
		this.editorId = editorId;
	}
	
	@Override
	public String getId() {
		if (editorId != null) {
			return editorId;
		}
		return super.getId();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		populateWeeksComposite();
	}
	
	@Override
	protected void createAdditionalControls(Composite parent) {
		weeksScrollComposite = new Composite(parent, SWT.FLAT | SWT.NO);
		GridData weeksLayoutData = new GridData(GridData.BEGINNING, GridData.FILL, true, false, 1, 1);
		// weeksLayoutData.verticalIndent = 3;
		weeksScrollComposite.setLayoutData(weeksLayoutData);
		GridLayout scrollLayout = new GridLayout();
		scrollLayout.numColumns = 4;
		weeksScrollComposite.setLayout(scrollLayout);
		scrollLeftButton = new Button(weeksScrollComposite, SWT.ARROW | SWT.LEFT | SWT.FLAT);
		scrollLeftButton.setEnabled(false);
		scrollLeftButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				baseWeekIndex--;
				remapWeekButtons();
			}
			
		});
		scrollRightButton = new Button(weeksScrollComposite, SWT.ARROW | SWT.RIGHT | SWT.FLAT);
		scrollRightButton.setEnabled(false);
		scrollRightButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				baseWeekIndex++;
				remapWeekButtons();
			}
			
		});
		Label label = new Label(weeksScrollComposite, SWT.NONE);
		label.setText("Scroll to week:");
		weeksComposite = new Composite(weeksScrollComposite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.wrap = false;
		rowLayout.spacing = 0;
		weeksComposite.setLayout(rowLayout);
		weeksLayoutData = new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1);
		weeksComposite.setLayoutData(weeksLayoutData);
		weeksComposite.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				scrollRightButton.setEnabled(notAllButtonsFit());
			}
			
		});
	}
	
	@Override
	protected void daySelected(int day) {
		int selected = (day + firstWeekOffset) / 7;
		for (int index = 0; index < weekButtons.length; index++) {
			Button button = weekButtons[index];
			if (index == selected) {
				button.setSelection(true);
				selectedWeek = baseWeekIndex + index;
			}
			else {
				button.setSelection(false);
			}
		}
	}
	
	@Override
	public void setFocus() {
		super.setFocus();
		scrollRightButton.setEnabled(notAllButtonsFit());
	}
	
	private boolean notAllButtonsFit() {
		int compositeWidth = weeksComposite.getSize().x;
		int buttonWidth = weekButtons[0].getSize().x;
		return (numWeeks - baseWeekIndex) * buttonWidth > compositeWidth;
	}

	private void populateWeeksComposite() {
		List<Day> days = daysComposite.getDays();
		int nDays = days.size();
		// Sunday is 1, Monday is 2
		int startDayOfWeek = nDays > 0 ? days.get(0).getDayOfWeek() : 2;
		// treat Sunday as last day of week
		if (startDayOfWeek == 1) {
			startDayOfWeek = 8;
		}
		firstWeekOffset = startDayOfWeek - 2;
		numWeeks = (int) Math.ceil( (nDays + firstWeekOffset) / 7.0);
		weekButtons = new Button[numWeeks];
		SelectionAdapter weeksAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selected = (Button)e.getSource();
				for (int index = 0; index < weekButtons.length; index++) {
					Button button = weekButtons[index];
					if (button == selected) {
						selectedWeek = baseWeekIndex + index;
					}
					else {
						button.setSelection(false);
					}
				}
				int day = 0;
				if (selectedWeek == 0) {
					day = 0;
				} else {
					day = (selectedWeek * 7) - firstWeekOffset;
				}
				daysComposite.scrollToDay(day);
			}
		};
		for (int i = 0; i < numWeeks; i++) {
			Button button = new Button(weeksComposite, SWT.FLAT | SWT.TOGGLE);
			button.setText(Integer.toString(i + 1));
			button.setLayoutData(new RowData(25, SWT.DEFAULT));
			button.addSelectionListener(weeksAdapter);
			weekButtons[i] = button;
		}
		Button firstButton = weekButtons[0];
		firstButton.setSelection(true);
	}
	
	
	
	private void remapWeekButtons() {
		for (int index = 0; index < weekButtons.length; index++) {
			Button button = weekButtons[index];
			int newIndex = baseWeekIndex + index;
			button.setText(Integer.toString(newIndex + 1));
			button.setVisible(newIndex < numWeeks);
			button.setSelection(newIndex == selectedWeek);
		}
		scrollLeftButton.setEnabled(baseWeekIndex > 0);
		scrollRightButton.setEnabled(notAllButtonsFit());
	}
	
}
