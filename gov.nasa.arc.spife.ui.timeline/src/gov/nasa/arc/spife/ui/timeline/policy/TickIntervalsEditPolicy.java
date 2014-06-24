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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.figure.TickIntervalsFigure;
import gov.nasa.arc.spife.ui.timeline.model.TickManager;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TickIntervalsEditPolicy extends TimelineViewerEditPolicy implements PropertyChangeListener, TimelineConstants {

	public static final String ROLE = TickIntervalsEditPolicy.class.getSimpleName();
	
	@Override
	public void activate() {
		super.activate();
		updateTickIntervals();
		getTickManager().addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getTickManager().removePropertyChangeListener(this);
	}

	private TickManager getTickManager() {
		return getViewer().getTickManager();
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String propertyName = e.getPropertyName();
		if (TickManager.TICK_INTERVALS.equals(propertyName)) {
			GEFUtils.runInDisplayThread(getHost(), new Runnable() {
				@Override
				public void run() {
					updateTickIntervals();
				}
			});
		}
	}

	private void updateTickIntervals() {
		TickIntervalsFigure figure = null;
		if (getHostFigure() instanceof TickIntervalsFigure) {
			figure = (TickIntervalsFigure) getHostFigure();
		} else if (getLayer(LAYER_DATA_PRIMARY_LAYER) instanceof TickIntervalsFigure) {
			figure = (TickIntervalsFigure) getLayer(LAYER_DATA_PRIMARY_LAYER);
		}
		
		if (figure == null) {
			return;
		}
		
		figure.setTickManager(getTickManager());
		figure.repaint();
	}

}
