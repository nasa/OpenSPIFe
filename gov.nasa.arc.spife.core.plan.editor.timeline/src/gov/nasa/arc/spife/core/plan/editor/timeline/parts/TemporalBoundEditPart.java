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

import gov.nasa.arc.spife.core.plan.editor.timeline.TimelinePlugin;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.TemporalBoundLink;
import gov.nasa.arc.spife.ui.timeline.figure.RowDataFigureLayoutData;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Image;
import org.jscience.physics.amount.Amount;

public class TemporalBoundEditPart extends PlanTimelineViewerEditPart<PeriodicTemporalConstraint> implements NodeEditPart {
	
	private static final Image ICON_PINNED = TimelinePlugin.getDefault().getIcon("at_exactly.gif");
	private static final Image ICON_ANYTIME_AFTER = TimelinePlugin.getDefault().getIcon("anytime_after.gif");
	private static final Image ICON_ANYTIME_BEFORE = TimelinePlugin.getDefault().getIcon("anytime_before.gif");
	
	private Image image = null;
	private Date boundedDate = null;
	
	private Listener listener = new Listener();
	
	@Override
	public void setModel(Object model) {
		PeriodicTemporalConstraint bound = (PeriodicTemporalConstraint) model;
		Amount<Duration> earliest = bound.getEarliest();
		Amount<Duration> latest = bound.getLatest();
		if ((earliest == null) && (latest == null)) {
			throw new IllegalArgumentException("TemporalBound visual is required to contain actual constraints");
		} else if (earliest == null) {
			image = ICON_ANYTIME_BEFORE;
			boundedDate = ConstraintUtils.getPeriodicConstraintLatestDate(bound);
		} else if (latest == null) {
			image = ICON_ANYTIME_AFTER;
			boundedDate = ConstraintUtils.getPeriodicConstraintEarliestDate(bound);
		} else {
			image = ICON_PINNED;
			boundedDate = ConstraintUtils.getPeriodicConstraintEarliestDate(bound);
		}
		super.setModel(model);
	}
	
	@Override
	public void activate() {
		super.activate();
		EPlanElement ePlanElement = getModel().getPoint().getElement();
		ePlanElement.getMember(CommonMember.class).eAdapters().add(listener);
		ePlanElement.getMember(TemporalMember.class).eAdapters().add(listener);
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				IFigure parent = figure.getParent();
				if (parent != null) {
					RowDataFigureLayoutData constraint = (RowDataFigureLayoutData) parent.getLayoutManager().getConstraint(figure);
					if (constraint == null) {
						constraint = new RowDataFigureLayoutData();
					}
					constraint.start = getBoundedDate();
					constraint.instantaneous = true;
					if (constraint.start != null) {
						parent.setConstraint(figure, constraint);
					}
				}
				
			}
		});
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		EPlanElement ePlanElement = getModel().getPoint().getElement();
		ePlanElement.getMember(CommonMember.class).eAdapters().remove(listener);
		ePlanElement.getMember(TemporalMember.class).eAdapters().remove(listener);
	}

	@Override
	protected void refreshVisuals() {
		IFigure f = getFigure();
		if (f != null) {
			f.repaint();
		}
	}

	@Override
	protected IFigure createFigure() {
		Label label = new Label() {
			@Override
			protected void paintFigure(Graphics g) {
				EPlanElement element = getModel().getPoint().getElement();
				TemporalMember member = element.getMember(TemporalMember.class);
				boolean scheduled = member.getScheduled() != Boolean.FALSE;
				try {  if (!scheduled) g.setAlpha(128); } catch(Exception e) { /* okay */ }
				super.paintFigure(g);
				try { if (!scheduled) g.setAlpha(128); } catch(Exception e) { /* okay */ }
			}
		};
		label.setIcon(image);
		label.setPreferredSize(image.getBounds().width, image.getBounds().height);
		
		// SPF-4554 & SPF-7331 - just make pin icon visibility configurable
		boolean pinIconVisible = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_PIN_ICON_VISIBLE);
		label.setVisible(pinIconVisible);
		
		return label;
	}
	
	public Date getBoundedDate() {
		return boundedDate;
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
	}

	@Override
	protected List getModelTargetConnections() {
		PeriodicTemporalConstraint b = getModel();
		EPlanElement ePlanElement = b.getPoint().getElement();
		return Collections.singletonList(new TemporalBoundLink(ePlanElement, b));
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		if (connection.getModel() instanceof TemporalBoundLink) {
			return new ChopboxAnchor(getFigure());
		}
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}

	private class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(final Notification notification) {
			if (TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == notification.getFeature()) {
				WidgetUtils.runInDisplayThread(getViewer().getControl(), new Runnable() {
					@Override
					public void run() {
						Boolean b = (Boolean) notification.getNewValue();
						if ((b == null) || b.booleanValue()) {
							figure.setForegroundColor(ColorConstants.black);
						} else {
							figure.setForegroundColor(ColorConstants.lightGray);
						}
					}
				});
			} else if (PlanPackage.Literals.COMMON_MEMBER__VISIBLE == notification.getFeature()) {
				WidgetUtils.runInDisplayThread(getViewer().getControl(), new Runnable() {
					@Override
					public void run() {
						Boolean visible = (Boolean) notification.getNewValue();
						figure.setVisible(visible == null || visible);
					}
				});
			}
		}
	}

}
