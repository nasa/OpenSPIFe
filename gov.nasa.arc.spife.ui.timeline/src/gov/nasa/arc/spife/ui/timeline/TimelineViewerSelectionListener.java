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

import gov.nasa.arc.spife.ui.timeline.Timeline.SELECTION_MODE;
import gov.nasa.arc.spife.ui.timeline.part.AbstractTimelineEditPart;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * This class manages the selection from the internal viewer and maps them to the external world
 */
public class TimelineViewerSelectionListener implements ISelectionChangedListener {
	
	private final Timeline timeline;
	
	public TimelineViewerSelectionListener(Timeline timeline) {
		this.timeline = timeline;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void selectionChanged(SelectionChangedEvent event) {
		if (timeline.isProcessingSelection()) {
			return;
		}
		
		boolean isHeaderSelection = false;
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		HashSet<Object> models = new HashSet<Object>();
		for (Object o : selection.toList()) {
			EditPart ep = (EditPart)o;
			Object m = ep.getModel();
			if (m != null && (!(m instanceof EObject) || ((EObject)m).eResource() != null)) {
				models.add(m);
			}
			
			TimelineViewer viewer = (TimelineViewer) ep.getViewer();
			if (viewer == null) {
				continue;
			}
			AbstractTimelineEditPart timelineEditPart = viewer.getTimelineEditPart();
			EditPart parent = ep;
			while (parent != null) {
				if (parent == timelineEditPart.getHeaderEditPart()) {
					isHeaderSelection = true;
					break;
				}
				parent = parent.getParent();
			}
		}
		
		timeline.setProcessingSelection(true);
		for (Object o : timeline.getTimelineViewers()) {
			final TimelineViewer v = (TimelineViewer) o;
			if (v != event.getSource()) {
				if (isHeaderSelection) {
					timeline.setSelectionMode(SELECTION_MODE.SCROLL_TO_LEFT_JUSTIFY);
				} else {
					timeline.setSelectionMode(SELECTION_MODE.NONE);
				}
				v.selectModels(models);
			}
		}
		
		Object source = event.getSource();
		if (source instanceof TimelineViewer) {
			final TimelineViewer v = (TimelineViewer) source;
			for (Object e : selection.toList()) {
				final GraphicalEditPart ep = (GraphicalEditPart)e;
				if (EditPart.SELECTED_PRIMARY == ep.getSelected()) {
					WidgetUtils.runInDisplayThread(timeline.getControl(), new Runnable() {
						@Override
						public void run() {
							timeline.doVerticalScroll(v, ep);
						}
					});
					break;
				}
			}
		}
		timeline.setProcessingSelection(false);
		timeline.getSite().getSelectionProvider().setSelection(new StructuredSelection(new ArrayList(models)));
		timeline.setSelectionMode(SELECTION_MODE.SCROLL_TO_CENTER); // set back to false at TimelineSelectionListener (after all viewers have been notified)
	}

}
