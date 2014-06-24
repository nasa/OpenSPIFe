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

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ZoomDropdownHandlerContributionItem extends ContributionItem {

	private static final Image IMAGE_ZOOM = Activator.getDefault().getIcon("zoom-tool.png");

	@SuppressWarnings("unchecked")
	@Override
	public void fill(final Menu menu, int index) {
		if (!AbstractTimelineCommandHandler.isTimelineActive()) {
			return;
		}
		final Timeline timeline = TimelineUtils.getTimelineInActivePart();
		if (timeline != null) {
			String zoomAsText = timeline.getZoomManager().getZoomAsText();
			for (final String text : timeline.getZoomManager().getZoomLevelsAsText()) {
				final MenuItem item = new MenuItem(menu, SWT.RADIO);
				item.setText(text);
				item.setImage(IMAGE_ZOOM);
				item.setSelection(text.equals(zoomAsText));

				Listener listener = new Listener() {
					@Override
					public void handleEvent(Event event) {
						switch (event.type) {
						case SWT.Selection:
							if (item.getSelection()) {
								timeline.getZoomManager().setZoomAsText(text);
							}
							break;
						}
					}
				};
				item.addListener(SWT.Selection, listener);
			}
		}
	}
}
