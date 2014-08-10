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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jscience.physics.amount.Amount;

public class PaginationComposite extends Composite implements TimelineConstants {

	private final Page page;
	private Label currentPageLabel;
	private Button pageLeftButton;
	private Button pageRightButton;
	private Listener listener = new Listener();

	public PaginationComposite(Composite parent, Page page) {
		super(parent, SWT.NONE);
		this.page = page;
		this.page.eAdapters().add(listener);
		setLayout(new GridLayout(3, false));
		createControls();
		updateCurrentPageLabel();
	}

	private void createControls() {
		pageLeftButton = new Button(this, SWT.BORDER);
		pageLeftButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		pageLeftButton.setText("<--");
		pageLeftButton.addSelectionListener(listener);

		currentPageLabel = new Label(this, SWT.NONE);
		currentPageLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		pageRightButton = new Button(this, SWT.BORDER);
		pageRightButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		pageRightButton.setText("-->");
		pageRightButton.addSelectionListener(listener);

		updatePageButtons();
	}

	@SuppressWarnings("null")
	private void updatePageButtons() {
		ZoomOption option = page.getZoomOption();
		Amount<Duration> scrollInterval = option.getScrollInterval();
		boolean hasInterval = scrollInterval != null;
		pageLeftButton.setEnabled(hasInterval);
		pageRightButton.setEnabled(hasInterval);
		if (hasInterval) {
			long s = scrollInterval.longValue(SI.SECOND);
			pageLeftButton.setToolTipText(DurationFormat.getFormattedDuration(s));
			pageRightButton.setToolTipText(DurationFormat.getFormattedDuration(s));
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		this.page.eAdapters().remove(listener);
	}

	private void updateCurrentPageLabel() {
		TemporalExtent extent = page.getExtent();
		String startString = DATE_STRINGIFIER.getDisplayString(extent.getStart());
		final int widthPixels = page.getWidth();
		final long mpp = page.getMilliSecondsPerPixel();
		final long pageEnd = widthPixels * mpp + extent.getStart().getTime();
		final Date end = min(new Date(pageEnd), extent.getEnd());
		String endString = DATE_STRINGIFIER.getDisplayString(end);
		currentPageLabel.setText(startString + " - " + endString);
	}

	private static Date min(Date d1, Date d2) {
		if (d1.getTime() < d2.getTime())
			return d1;
		return d2;
	}

	private class Listener extends AdapterImpl implements SelectionListener {

		@Override
		public void notifyChanged(Notification msg) {
			Object f = msg.getFeature();

			if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f || TimelinePackage.Literals.PAGE__ZOOM_OPTION == f || TimelinePackage.Literals.PAGE__START_TIME == f || TimelinePackage.Literals.PAGE__DURATION == f) {
				updatePageButtons();
				updateCurrentPageLabel();
			}
			super.notifyChanged(msg);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			ZoomOption zoomOption = page.getZoomOption();
			TemporalExtent currentExtent = page.getExtent();
			final Amount<Duration> scrollInterval = zoomOption.getScrollInterval();
			if (scrollInterval == null) {
				return;
			}
			Object source = e.getSource();
			Date newStartTime = null;
			if (pageLeftButton == source) {
				newStartTime = DateUtils.subtract(currentExtent.getStart(), scrollInterval);
			} else if (pageRightButton == source) {
				newStartTime = DateUtils.add(currentExtent.getStart(), scrollInterval);
			}

			if (newStartTime != null) {
				newStartTime = MissionCalendarUtils.round(newStartTime.getTime(), (int) scrollInterval.longValue(DateUtils.MILLISECONDS));
				final Date fNewStartTime = newStartTime;
				gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(page, new Runnable() {
					@Override
					public void run() {
						page.setStartTime(fNewStartTime);
					}
				});
				updateCurrentPageLabel();
			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}

}
