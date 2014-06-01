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

import gov.nasa.arc.spife.core.plan.editor.timeline.commands.NudgeAction;
import gov.nasa.arc.spife.core.plan.editor.timeline.commands.TimelineKeyHandler;
import gov.nasa.arc.spife.ui.timeline.part.AbstractTimelineEditPart;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.KeyStroke;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class PlanTimelineEditPart extends AbstractTimelineEditPart<EPlan> {
	
	private static final int FAST_NUDGE_FACTOR = 10;
	
	@Override
	public GraphicalEditPart createHeaderEditPart(EPlan model) {
		GraphicalEditPart editPart = new PlanTimelineHeaderEditPart();
		editPart.setModel(model);
		return editPart;
	}

	@Override
	public GraphicalEditPart createDataEditPart(EPlan model) {
		PlanTimelineDataEditPart editPart = new PlanTimelineDataEditPart();
		editPart.setModel(model);
		return editPart;
	}
	
	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
	@Override
	public void activate() {
		if( isActive() ) 
			return;
		
		TransactionUtils.reading(getModel(), new Runnable() {
			@Override
			public void run() {
				PlanTimelineEditPart.super.activate();
			}
		});
		
		Action action = null;
		KeyStroke key = null;
		TimelineKeyHandler keyHandler = new TimelineKeyHandler();
		
		action = new NudgeAction("Nudge Right", 1, this);
		key = KeyStroke.getPressed(SWT.ARROW_RIGHT, 0);
		keyHandler.put(key, action);
		
		action = new NudgeAction("Fast Nudge Right", FAST_NUDGE_FACTOR, this);
		key = KeyStroke.getPressed(SWT.ARROW_RIGHT, SWT.SHIFT);
		keyHandler.put(key, action);
		
		action = new NudgeAction("Nudge Left", -1, this);
		key = KeyStroke.getPressed(SWT.ARROW_LEFT, 0);
		keyHandler.put(key, action);
		
		action = new NudgeAction("Fast Nudge Left", -1*FAST_NUDGE_FACTOR, this);
		key = KeyStroke.getPressed(SWT.ARROW_LEFT, SWT.SHIFT);
		keyHandler.put(key, action);
		
//		action = new NeighborSelectAction("Select Next Right", this, Direction.RIGHT);
//		key = KeyStroke.getPressed(SWT.ARROW_RIGHT, SWT.CONTROL);
//		keyHandler.put(key, action);
//		
//		action = new NeighborSelectAction("Select Next Left", this, Direction.LEFT);
//		key = KeyStroke.getPressed(SWT.ARROW_LEFT, SWT.CONTROL);
//		keyHandler.put(key, action);
//		
//		action = new NeighborSelectAction("Select Next (Down) Vertical Category", this, Direction.DOWN);
//		key = KeyStroke.getPressed(SWT.ARROW_DOWN, SWT.CONTROL);
//		keyHandler.put(key, action);
//		
//		action = new NeighborSelectAction("Select Previous (Up) Vertical Category", this, Direction.UP);
//		key = KeyStroke.getPressed(SWT.ARROW_UP, SWT.CONTROL);
//		keyHandler.put(key, action);
		
		TimelineKeyHandler handler = (TimelineKeyHandler) getViewer().getKeyHandler();
		if(handler == null) {
			handler = new TimelineKeyHandler();
			getViewer().setKeyHandler(handler);
		}
		keyHandler.setParent(handler);
		getViewer().setKeyHandler(keyHandler);
	}
	
}
