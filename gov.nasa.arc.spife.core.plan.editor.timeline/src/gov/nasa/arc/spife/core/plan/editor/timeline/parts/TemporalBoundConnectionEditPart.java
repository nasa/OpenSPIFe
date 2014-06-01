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
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Date;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;

public class TemporalBoundConnectionEditPart extends ConstraintConnectionEditPart {

	private Listener listener = new Listener();
	
	public PeriodicTemporalConstraint getTemporalBound() {
		EditPart target = getTarget();
		return target == null ? null : ((TemporalBoundEditPart)target).getModel();
	}
	
	@Override
	public void setSource(EditPart newSource) {
		EditPart currentSource = getSource();
		// case 1:
		if(newSource == null && currentSource != null) {
			removeModelListener(currentSource);
		} 
		// case 2:
		else if(newSource != null && currentSource == null) {
			addModelListener(newSource);
		}
		// case 3: ignore if new and old source are the same, otherwise remove & add
		else if(newSource != null && currentSource != null && !newSource.equals(currentSource)) {
			removeModelListener(currentSource);
			addModelListener(newSource);
		}
		super.setSource(newSource);
	}

	private void addModelListener(EditPart newSource) {
		EPlanElement ePlanElement = (EPlanElement) newSource.getModel();
		ePlanElement.getMember(TemporalMember.class).eAdapters().add(listener);
	}

	private void removeModelListener(EditPart currentSource) {
		EPlanElement ePlanElement = (EPlanElement) currentSource.getModel();
		ePlanElement.getMember(TemporalMember.class).eAdapters().remove(listener);
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		final PeriodicTemporalConstraint b = getTemporalBound();
		if (b != null) {
			TemporalNodeEditPart ep = (TemporalNodeEditPart) getSource();
			boolean minMaxConstraintLinesVisible = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_MIN_MAX_CONSTRAINT_LINES_VISIBLE);
			if (ep != null) {
				Runnable runnable = null;				
				if(minMaxConstraintLinesVisible) {
					final Date date = b.getPoint().getDate();
					final IFigure figure = this.getFigure();
					runnable = new Runnable() {
						@Override
						public void run() {
							Date minTime = ConstraintUtils.getPeriodicConstraintEarliestDate(b);
							Date maxTime = ConstraintUtils.getPeriodicConstraintLatestDate(b);
							boolean visible = !CommonUtils.equals(minTime, date) && !CommonUtils.equals(maxTime, date);
							figure.setVisible(visible);
						}
					};
				} else {
					if(figure.isVisible()) {
						runnable = new Runnable() {
							@Override
							public void run() {
								figure.setVisible(false);
							}
						};
					}
				}

				if(runnable != null) {
					WidgetUtils.runInDisplayThread(getViewer().getControl(), runnable);
				}
			}
		}
	}

	private class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(Notification notification) {
			refreshVisuals();
		}
		
	}
		
}
