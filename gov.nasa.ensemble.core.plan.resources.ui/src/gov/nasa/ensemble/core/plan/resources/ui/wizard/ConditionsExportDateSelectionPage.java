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
package gov.nasa.ensemble.core.plan.resources.ui.wizard;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;

import java.text.ParseException;
import java.util.Date;

import javax.measure.quantity.Duration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jscience.physics.amount.Amount;

public class ConditionsExportDateSelectionPage extends FileSelectionPage {
	
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	private final EPlan plan;
	private Date date;

	public ConditionsExportDateSelectionPage(EPlan plan, int style) {
		super(style);
		this.plan = plan;
		this.date = getDefaultDate();
		setTitle("Export FINCON");
		setMessage("Select a file and time to export the resource conditions.");
	}

	public Date getDate() {
		return date;
	}
	
	@Override
	protected Composite buildFileChooser(Composite parent) {
		Composite composite = super.buildFileChooser(parent);
		Label dateLabel = new Label(composite, SWT.NONE);
		dateLabel.setText("Time:");
		final Date defaultDate = getDefaultDate();
		final Text text = new Text(composite, SWT.BORDER | SWT.SINGLE);
		text.setText(DATE_STRINGIFIER.getDisplayString(defaultDate));
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				WidgetUtils.runInDisplayThread(text, new Runnable() {
					@Override
					public void run() {
						parseTime(defaultDate, text.getText());
					}
				});
			}
		});
		return composite;
	}
	
	private Date getDefaultDate() {
		TemporalMember temporalMember = plan.getMember(TemporalMember.class);
		Date startDate = temporalMember.getStartTime();
		Amount<Duration> duration = temporalMember.getDuration();
		Date defaultDate = new Date();
		if (startDate != null && duration != null) {
			defaultDate = DateUtils.add(startDate, duration);
		}
		return defaultDate;
	}

	private void parseTime(final Date defaultDate, String string) {
	  clearError(ConditionsExportDateSelectionPage.class);
		try {
			date = DATE_STRINGIFIER.getJavaObject(string, defaultDate);
		} catch (ParseException e) {
			setError(ConditionsExportDateSelectionPage.class, e.getMessage());
		}
	}
	
}
