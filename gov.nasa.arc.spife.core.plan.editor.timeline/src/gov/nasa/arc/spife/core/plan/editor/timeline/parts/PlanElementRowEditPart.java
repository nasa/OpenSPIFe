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

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.TimelinePlugin;
import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.core.plan.parameters.VisibleOperation;
import gov.nasa.ensemble.core.plan.temporal.ScheduledOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.SimpleRaisedBorder;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.swt.graphics.Image;

public class PlanElementRowEditPart extends PlanElementRowHeaderEditPart {

	private static Logger trace = Logger.getLogger(PlanElementRowEditPart.class);

	private static final Image visibleImage 	   				= TimelinePlugin.getDefault().getIcon("layer_visible.gif");
	private static final Image invisibleImage 	   				= TimelinePlugin.getDefault().getIcon("layer_visible_invisible.gif");
	private static final Image scheduledImage 	   				= TimelinePlugin.getDefault().getIcon("scheduled.gif");
	private static final Image unscheduledImage    				= TimelinePlugin.getDefault().getIcon("scheduled_unscheduled.gif");
	private static final Image quasiScheduledImage 				= TimelinePlugin.getDefault().getIcon("scheduled_mixed.gif");
	
	private Label visibleLabel 									= null;
	private Label scheduledLabel 								= null;
	
	private Listener listener 									= new Listener();
	
	@Override
	protected void addTitle(IFigure figure) {
		addVisibleLabel(figure);
		addScheduledLabel(figure);
		super.addTitle(figure);
	}

	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		refreshVisuals();
	}

	private void addVisibleLabel(IFigure figure) {
		visibleLabel = new Label();
		visibleLabel.setBorder(new SimpleRaisedBorder());
		visibleLabel.addMouseListener(new MouseListener.Stub() {
			@Override
			public void mousePressed(MouseEvent me) {
				EPlanElement node = getModel();
				TriState oldValue = SpifePlanUtils.getVisible(node);
				if (!PlanEditApproverRegistry.getInstance().canModify(node)) {
					return;
				}
				try {
					VisibleOperation op = new VisibleOperation(node, oldValue == TriState.FALSE);
					op.addContext(TransactionUtils.getUndoContext(node));
					IOperationHistory history = OperationHistoryFactory.getOperationHistory();
					history.execute(op, null, null);
				} catch (Exception e) {
					trace.error(e.getMessage(), e);
				}
			}
		});
		updateVisibleVisual();
		figure.add(visibleLabel);
	}

	private void addScheduledLabel(IFigure figure) {
		scheduledLabel = new Label();
		scheduledLabel.setBorder(new SimpleRaisedBorder());
		scheduledLabel.addMouseListener(new MouseListener.Stub() {
			@Override
			public void mousePressed(MouseEvent me) {
				EPlanElement node = getModel();
				ScheduledOperation.toggleScheduledness(node);
			}
		});
		updateScheduledVisual();
		figure.add(scheduledLabel);
	}
	
	/**
	 * Convenience method to retrieve the casted viewer.
	 */
	@Override
	public PlanTimelineViewer getViewer() {
		return (PlanTimelineViewer) super.getViewer();
	}
	
	@Override
	public void activate() {
		super.activate();
		getModel().getMember(TemporalMember.class).eAdapters().add(listener);
		getModel().getMember(CommonMember.class).eAdapters().add(listener);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		getModel().getMember(TemporalMember.class).eAdapters().remove(listener);
		getModel().getMember(CommonMember.class).eAdapters().remove(listener);
	}

	@Override
	public void refreshVisuals() {
		updateVisibleVisual();
		updateScheduledVisual();
		super.refreshVisuals();
	}

	private void updateVisibleVisual() {
		EPlanElement pe = getModel();
		if (pe.getMember(CommonMember.class).isVisible()) {
			visibleLabel.setIcon(visibleImage);
		} else {
			visibleLabel.setIcon(invisibleImage);
		}
	}
	
	private void updateScheduledVisual() {
		Boolean scheduled = getModel().getMember(TemporalMember.class).getScheduled();
		if (scheduled == null) {
			scheduledLabel.setIcon(quasiScheduledImage);
		} else if (scheduled) {
			scheduledLabel.setIcon(scheduledImage);
		} else {
			scheduledLabel.setIcon(unscheduledImage);
		}
	}

	private class Listener extends AdapterImpl {
	
		@Override
		public void notifyChanged(Notification notification) {
			Object f = notification.getFeature();
			if (TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == f) {
				GEFUtils.runInDisplayThread(PlanElementRowEditPart.this, new Runnable() {
					@Override
					public void run() {
						updateScheduledVisual();
					}
				});
			} else if (PlanPackage.Literals.COMMON_MEMBER__VISIBLE == f) {
				GEFUtils.runInDisplayThread(PlanElementRowEditPart.this, new Runnable() {
					@Override
					public void run() {
						updateVisibleVisual();
					}
				});
			}
		}
		
	}

}
