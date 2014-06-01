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
import gov.nasa.ensemble.common.ui.editor.SelectionUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.services.IDisposable;

/**
 * This class manages the selection from the external world into the viewers
 */
public class TimelineSelectionListener implements ISelectionChangedListener, IDisposable {
	
	private static final Logger trace = Logger.getLogger(TimelineSelectionListener.class);

	private Timeline timeline;

	public TimelineSelectionListener(Timeline timeline) {
		super();
		this.timeline = timeline;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void selectionChanged(SelectionChangedEvent event) {
		if (timeline == null || timeline.isProcessingSelection()) {
			return;
		}
		
		boolean scrolled = false;
		ISelection selection = event.getSelection();
		if (event != null && selection != null && selection instanceof IStructuredSelection) {
			SelectionUtils.logSelection(trace, "ExternalSelectionChanged" + "Listener.selectionChanged()", selection);
			timeline.setProcessingSelection(true);
			Set<Object> models = new LinkedHashSet<Object>(((StructuredSelection)selection).toList());
			if (!models.isEmpty()) {
				for (Object o : timeline.getTimelineViewers()) {
					TimelineViewer v = (TimelineViewer) o;
					v.selectModels(models);
					if (!scrolled) {
						EditPart editPartToScroll = getSelectedEditPart(v, models, EditPart.SELECTED_PRIMARY);
						if (editPartToScroll == null) {
							editPartToScroll = getSelectedEditPart(v, models, EditPart.SELECTED);
						}
						if (editPartToScroll != null) {
							TimelineViewer viewer = (TimelineViewer) editPartToScroll.getViewer();

							// don't center since the request is coming from the timeline itself.
							viewer.scrollToEditPart(editPartToScroll);
							scrolled = true;
						}
					}
				}
			}
			timeline.setProcessingSelection(false);
			timeline.setSelectionMode(SELECTION_MODE.SCROLL_TO_CENTER);
		}
	}

	private EditPart getSelectedEditPart(TimelineViewer viewer, Set<Object> models, int selectionState) {
		for (Object model : models) {
			Set<EditPart> eps = viewer.getEditParts(model);
			for (EditPart ep : eps) {
				if (ep.getSelected() == selectionState) {
					return ep;
				}
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		timeline = null;
	}
	
}
