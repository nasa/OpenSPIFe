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
package gov.nasa.arc.spife.ui.timeline.action;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.type.editor.TextEditor;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.text.ParseException;
import java.util.Date;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TimelineExtentHandler extends AbstractTimelineCommandHandler {

	public final static String ID = "gov.nasa.arc.spife.ui.timeline.set.extent.command";

	@Override
	public Object execute(ExecutionEvent event) {
		final Timeline<?> timeline = TimelineUtils.getTimeline(event);
		if (timeline != null) {
			Shell parentShell = WidgetUtils.getShell();
			final SetExtentDialog extentDialog = new SetExtentDialog(parentShell, timeline);
			extentDialog.setBlockOnOpen(true);
			int result = extentDialog.open();
			if (result == Window.OK) {
				final Date sTime = extentDialog.getStartTime();
				final Date eTime = extentDialog.getEndTime();
				gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(timeline.getPage(), new Runnable() {
					@Override
					public void run() {
						Page page = timeline.getPage();
						page.setStartTime(sTime);
						page.setDuration(DateUtils.subtract(eTime, sTime));
					}
				});

			}
		}
		return null;
	}

	@Override
	public String getCommandId() {
		return ID;
	}

	private static class SetExtentDialog extends Dialog {

		private static final IStringifier<Date> dateStringifier = StringifierRegistry.getStringifier(Date.class);

		private Date startTime;
		private Date endTime;

		private TextEditor startEditor;
		private TextEditor endEditor;

		protected SetExtentDialog(Shell parentShell, Timeline<?> timeline) {
			super(parentShell);
			startTime = timeline.getPage().getStartTime();
			endTime = DateUtils.add(timeline.getPage().getStartTime(), timeline.getPage().getDuration());
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			final Composite composite = new Composite((Composite) super.createDialogArea(parent), SWT.NONE);

			composite.setLayout(new GridLayout(2, false));
			new Label(composite, SWT.NONE).setText("Start Time");
			startEditor = createTextEditor(composite, startTime, dateStringifier);
			new Label(composite, SWT.NONE).setText("End Time");
			endEditor = createTextEditor(composite, endTime, dateStringifier);

			return composite;
		}

		public Date getStartTime() {
			return startTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		private TextEditor createTextEditor(Composite composite, final Date date, final IStringifier<Date> stringifier) {
			final TextEditor editor = new TextEditor(composite, stringifier, true);
			final Text text = (Text) editor.getEditorControl();
			text.setText(stringifier.getDisplayString(date));
			text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
			return editor;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			addModListener(startEditor, startTime, dateStringifier);
			addModListener(endEditor, endTime, dateStringifier);
		}

		private void addModListener(TextEditor editor, final Date date, final IStringifier<Date> stringifier) {
			final Text text = (Text) editor.getEditorControl();
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					final Button button = getButton(IDialogConstants.OK_ID);
					final String txt = text.getText();
					try {
						final Date obj = stringifier.getJavaObject(txt, date);
						if (obj == null) {
							if (button != null)
								button.setEnabled(false);
							return;
						}
						text.setForeground(ColorConstants.black);
						date.setTime(obj.getTime());
						if (button != null)
							button.setEnabled(true);
					} catch (ParseException e1) {
						if (button != null)
							button.setEnabled(false);
					}
				}
			});
		}
	}
}
