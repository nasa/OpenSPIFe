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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure.Palette;
import gov.nasa.arc.spife.ui.timeline.figure.RowDataFigureLayout;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataRowEditPart;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.ForbiddenWorkbenchUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.OperationCommand;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyBounds;
import gov.nasa.ensemble.core.plan.constraints.network.ConstrainedPlanModifier;
import gov.nasa.ensemble.core.plan.constraints.network.IPlanConstraintInfo;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModificationTweakerRegistry;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.ControlNotification;
import gov.nasa.ensemble.emf.util.ControlNotificationImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;

import javolution.context.Context;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.jscience.physics.amount.Amount;

public class MoveThread extends Thread {

	private final Object queueLock = new int[0];
	private final Object finishLock = new int[0];
	
	private EPlan plan;
	private PlanTimeline planTimeline;
	private TemporalExtentsCache temporalExtentsCache = new TemporalExtentsCache();
	private Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
	private EditPart originEditPart = null;
	private Set<RowDataFigureLayout> lockedLayouts = new LinkedHashSet<RowDataFigureLayout>();
	private Set<IFigure> movingFigures = new LinkedHashSet<IFigure>();
	private boolean quit = false;
	private long nextDelta;
	private List nextEditParts;
	private long lastTimeDelta = 0;
	private volatile boolean hasWork = false;
	protected boolean paused = false;
	
	public MoveThread(EPlan plan, PlanTimeline planTimeline) {
		super("MoveThread(" + plan.getName() + ")");
		this.plan = plan;
		this.planTimeline = planTimeline;
	}
	
	private void dispose() {
		plan = null;
		planTimeline = null;
		temporalExtentsCache = null;
		changedTimes = null;
		originEditPart = null;
		lockedLayouts = null;
		nextEditParts = null;
		movingFigures = null;
		Context current = Context.getCurrent();
		ReflectionUtils.set(current, "_owner", null);
	}
	
	public EditPart getOriginEditPart() {
		return originEditPart;
	}
	
	public void startMove(EditPart part, IFigure figure) {
		if (movingFigures.add(figure)) {
			RowDataFigureLayout layout = (RowDataFigureLayout) figure.getLayoutManager();
			if (lockedLayouts.add(layout)) {
				layout.setLockVerticalPosition(true);
			}
		}
		if (originEditPart != null) {
			// this method gets called for each edit part in the selection,
			// but there is no need to reinitialize the variables
			return; 
		}
		originEditPart = part;
		temporalExtentsCache.clear();
		temporalExtentsCache.cache(plan);
		changedTimes.clear();
		nextEditParts = null;
		lastTimeDelta = 0;
	}
	
	public Command finishMove() {
		if (originEditPart == null) {
			// This method gets called for each edit part in the selection,
			// but there is no need to recomplete the operation.
			// Also, this is sometimes called at the end of bounding box
			// selection, so we want to ignore that as well.
			return null;
		}
		boolean hadWork;
		synchronized (finishLock) {
			hadWork = hasWork;
		}
		if (hadWork) {
			TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
			TransactionUtils.checkTransaction(domain, true); // this will throw an illegal state exception if we are processing a transaction in this thread
			final Display display = planTimeline.getControl().getDisplay();
			BusyIndicator.showWhile(display, new Runnable() {
				@Override
				public void run() {
					boolean hadWork = true;
					while (hadWork) {
						synchronized (finishLock) {
							try {
								finishLock.wait(100);
							} catch (InterruptedException e) {
								// fall through
							}
							hadWork = hasWork;
						}
						if (hadWork) {
							// we should be holding no locks when we reach here
							ForbiddenWorkbenchUtils.processPendingEvents(display);
						}
					}
				}
			});
		}
		OperationCommand command = null;
		if (!changedTimes.isEmpty()) {
			IUndoContext ctx = TransactionUtils.getUndoContext(plan); 
			SetExtentsOperation op = createPreferredTimesOperation();
			command = new OperationCommand(ctx, op, planTimeline.getControl(), planTimeline.getSite());
		}
		originEditPart = null;
		nextEditParts = null;
		lastTimeDelta = 0;
		for (RowDataFigureLayout layout : lockedLayouts) {
			layout.setLockVerticalPosition(false);
		}
		lockedLayouts.clear();
		for (IFigure figure : movingFigures) {
			figure.revalidate();
		}
		movingFigures.clear();
		updatePalettes();
		temporalExtentsCache.clear();
		changedTimes.clear();
		CompoundCommand cc = new CompoundCommand();
		cc.add(new Command() {

			@Override
			public void execute() {
				TransactionUtils.writing(plan, new Runnable() {
					@Override
					public void run() {
						paused = false;
						plan.eNotify(new ControlNotificationImpl(plan, ControlNotification.RESUME));
					}
				});
			}
			
		});
		cc.add(command);
		return cc;
	}
	
	private SetExtentsOperation createPreferredTimesOperation() {
		@SuppressWarnings("unused")
		Map<EPlanElement, TemporalExtent> timesChangedByMove = this.changedTimes;
		Map<EPlanElement, TemporalExtent> timesChangedBySnapToOrbit = new LinkedHashMap<EPlanElement, TemporalExtent>();
		timesChangedBySnapToOrbit.putAll(this.changedTimes);
		if (planTimeline.getSnapToOrbitEnabled()) {
			PlanModificationTweakerRegistry.getInstance().applyTweaksAfterMove(plan, timesChangedBySnapToOrbit);
		}
		// Snap To Orbit overrides Snap To, but only if an event was being moved.
		if (timesChangedBySnapToOrbit.equals(timesChangedByMove)) {
			Map<EPlanElement, TemporalExtent> timesChangedBySnapTo = new LinkedHashMap<EPlanElement, TemporalExtent>();
			timesChangedBySnapTo.putAll(this.changedTimes);
			int snapPx = planTimeline.getSnapToTolerance();
			long snap = planTimeline.getPage().convertToMilliseconds(snapPx);
			if (snap > 0 && timesChangedBySnapTo.size() <= 2) {
				for (Map.Entry<EPlanElement, TemporalExtent> changedTime : timesChangedBySnapTo.entrySet()) {
					EPlanElement element = changedTime.getKey();
					TemporalExtent extent = changedTime.getValue();
					Date date = extent.getStart();
					if (element instanceof EPlan) continue;
					Date snappedStartTime = computeSnapStartTime(plan, element, date, snap);
					timesChangedBySnapTo.put(element, extent.moveToStart(snappedStartTime));
				}	
			}
			return new SetExtentsOperation("move nodes", plan, timesChangedBySnapTo, temporalExtentsCache.clone());
		} else {
			return new SetExtentsOperation("move nodes", plan, timesChangedBySnapToOrbit, temporalExtentsCache.clone());			
		}
	}
	
	public Date computeSnapStartTime(EPlan plan, EPlanElement element, Date startTime, long tolerance) {
		Date newStartTime = null;
		long duration = element.getMember(TemporalMember.class).getExtent().getDurationMillis();
		List<EPlanElement> siblings = computeSnapSiblings(plan, element, tolerance);
		long delNewStart = Long.MAX_VALUE / 2;
		long delNewEnd = Long.MAX_VALUE / 2;
		for (EPlanElement sibling: siblings) {
			if (sibling == element) 
				continue;
			TemporalMember temporalMember = sibling.getMember(TemporalMember.class);
			TemporalExtent extent = temporalMember.getExtent();
			Date siblingStartTime = extent.getStart();
			long siblingDuration = extent.getDurationMillis();
			Date siblingEndDate = DateUtils.add(siblingStartTime, siblingDuration);
			long delStart = Math.abs(DateUtils.subtract(startTime, siblingEndDate));
			if (delStart <= tolerance && delStart < delNewStart && delStart < delNewEnd) {
				newStartTime = DateUtils.add(siblingStartTime, siblingDuration);
				delNewStart = delStart;
			}
			long delEnd = Math.abs(DateUtils.subtract(DateUtils.add(startTime, duration), siblingStartTime));
			if (delEnd <= tolerance && delEnd < delNewEnd && delEnd < delNewStart) {
				newStartTime = DateUtils.subtract(siblingStartTime, duration);
				delNewStart = delEnd;
			}
		}
		if (newStartTime != null) {
			return newStartTime;
		} // else...
		return startTime;
	}

	private List<EPlanElement> computeSnapSiblings(EPlan plan, EPlanElement element, long tolerance) {
		TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
		List<EPlanElement> siblings = new ArrayList<EPlanElement>();
		if (this.changedTimes.size() == 1) {
			for (Object ep : originEditPart.getChildren()) {
				if (ep instanceof TemporalNodeEditPart && ((TemporalNodeEditPart)ep).getModel() != element) {
					EPlanElement pe = ((TemporalNodeEditPart) ep).getModel();
					TemporalExtent e = pe.getMember(TemporalMember.class).getExtent();
					long d0 = Math.abs(DateUtils.subtract(e.getStart(), extent.getEnd()));
					long d1 = Math.abs(DateUtils.subtract(e.getEnd(), extent.getStart()));
					if (d0 <= tolerance || d1 <= tolerance) {
						siblings.add(pe);
					}
				}
			}
		}
		if (siblings.isEmpty()) {
			if (element instanceof EActivity) {
				siblings.addAll(EPlanUtils.getActivities(plan));
			} else if (element instanceof EActivityGroup) {
				final List<EActivityGroup> groups = new ArrayList<EActivityGroup>();
				new PlanVisitor() {
					@Override
					protected void visit(EPlanElement element) {
						if (element instanceof EActivityGroup) {
							groups.add((EActivityGroup) element);
						}
					}
				}.visitAll(plan);
			}
		}
		return siblings;
	}

	public boolean isDragging() {
		return (originEditPart != null);
	}

	public void quit() {
		quit = true;
		interrupt();
		dispose();
	}
	
	@Override
	public void run() {
		try {
			while (!quit) {
				try {
					synchronized (queueLock) {
						while (nextEditParts == null) {
							try {
								synchronized (finishLock) {
									hasWork = false;
									finishLock.notify();
								}
								queueLock.wait();
							} catch (InterruptedException e) {
								// fall through
							}
							if (quit) {
								return;
							}
						}
					}
					if (nextEditParts != null) {
						TransactionUtils.writing(plan, new Runnable() {
							@Override
							public void run() {
								if (!paused) {
									paused = true;
									plan.eNotify(new ControlNotificationImpl(plan, ControlNotification.PAUSE));
								}
								List parts;
								long delta;
								synchronized (queueLock) {
									parts = nextEditParts;
									delta = nextDelta;
									if ((parts == null) || quit) {
										return;
									}
									nextEditParts = null;
									nextDelta = 0;
								}
								move(parts, delta);
							}
						});
					}
				} catch (Exception e) {
					LogUtil.error("exception in MoveThread", e);
				}
			}
		} finally {
			dispose();
		}
	}

	public void queueMove(ChangeBoundsRequest request) {
		if (!isDragging()) {
			return;
		}
		List editParts = request.getEditParts();
		if (editParts.isEmpty()) {
			return;
		}
		Page page = planTimeline.getPage();
		long timeDelta = (int) page.convertToMilliseconds(request.getMoveDelta().x);
		long tolerance = page.getZoomOption().getMsMoveThreshold();
		tolerance *= MissionConstants.getInstance().getEarthSecondsPerLocalSeconds();
		timeDelta = timeDelta - (timeDelta % tolerance);
		if (Math.abs(timeDelta - lastTimeDelta) < tolerance) {
			return;
		}
		synchronized (queueLock) {
//			if (nextEditParts != null) {
//				System.out.println("skipped");
//			}
			hasWork = true;
			nextEditParts = editParts;
			nextDelta = timeDelta;
			queueLock.notify();
		}
	}

	private void move(final List editParts, final long delta) {
		LinkedHashSet<EPlanElement> editPartElements = new LinkedHashSet<EPlanElement>(editParts.size());
		GraphicalEditPart focusPart = null;
		for (Object object : editParts) {
			if (object instanceof GraphicalEditPart) {
				GraphicalEditPart part = (GraphicalEditPart) object;
				if (TimelineUtils.isMoveable(part)) {
					Object model = part.getModel();
					if (model instanceof EPlanElement) {
						EPlanElement element = (EPlanElement) model;
						editPartElements.add(element);
						if (focusPart == null) {
							EditPart parent = part;
							while (parent != null) {
								if (parent instanceof TreeTimelineDataRowEditPart) {
									focusPart = part;
									break;
								}
								parent = parent.getParent();
							}
						}
					}
				}
			}
		}
		updateCurrentStartTimes(editPartElements, delta);
		if (focusPart != null) {
			final GraphicalEditPart part = focusPart;
			Display display = part.getViewer().getControl().getDisplay();
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					planTimeline.doHorizontalScroll(part);
				}				
			});			
		}
		updatePalettes();
//		WidgetUtils.runInDisplayThread(planTimeline.getControl(), new Runnable() {
//			@Override
//			public void run() {
//				IFigure figure = originFigure;
//				if (figure != null) {
//					originFigure.getUpdateManager().performUpdate();
//				}
//			}
//		});
	}
	
	private void updateCurrentStartTimes(final LinkedHashSet<EPlanElement> editPartElements, final long finalTimeDelta) {
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		List<? extends EPlanElement> copyOfChangedElements = new ArrayList<EPlanElement>(changedTimes.keySet());
		changedTimes.clear();
//		long msMoveThreshold = planTimeline.getPage().getZoomOption().getMsMoveThreshold();
		Amount<Duration> delta = AmountUtils.toAmount(finalTimeDelta, gov.nasa.ensemble.core.jscience.util.DateUtils.MILLISECONDS);
		for (EPlanElement planElement : editPartElements) {
			if (planElement instanceof EActivity && ((EActivity)planElement).isIsSubActivity()) {
				continue;
			}
			Map<EPlanElement, TemporalExtent> newlyChangedTimes = modifier.shiftElement(planElement, delta, temporalExtentsCache);
			List<EPlanElement> unchangedTimes = new ArrayList<EPlanElement>();
			for (Map.Entry<EPlanElement, TemporalExtent> changedTime : newlyChangedTimes.entrySet()) {
				EPlanElement pe = changedTime.getKey();
				TemporalExtent newExtent = changedTime.getValue();
				Date newStart = newExtent.getStart();
//				newStart = MissionCalendarUtils.round(newStart.getTime(), (int) msMoveThreshold);
				newExtent = newExtent.moveToStart(newStart);
				newlyChangedTimes.put(pe, newExtent);
				TemporalExtent originalExtent = temporalExtentsCache.get(pe);
				if (newExtent.equals(originalExtent)) {
					unchangedTimes.add(pe);
				} else {
					TemporalExtent priorMoveExtent = changedTimes.get(pe);
					if ((originalExtent != null) && (priorMoveExtent != null)) {
						Date originalStart = originalExtent.getStart();
						Date priorMoveStart = priorMoveExtent.getStart();
						if (Math.abs(DateUtils.subtract(originalStart, priorMoveStart)) >
							Math.abs(DateUtils.subtract(originalStart, newStart))) {
							unchangedTimes.add(pe);
						} else {
							pe.getMember(TemporalMember.class).setExtent(newExtent);
						}
					} else {
						pe.getMember(TemporalMember.class).setExtent(newExtent);
					}
				}
			}
			newlyChangedTimes.keySet().removeAll(unchangedTimes);
			changedTimes.putAll(newlyChangedTimes);
		}
		// return elements that have dropped out of the move action to their previous start time
		List<EPlanElement> paletteElements = new ArrayList<EPlanElement>();
		for (EPlanElement noLongerMovedElement: copyOfChangedElements) {
			if (!changedTimes.keySet().contains(noLongerMovedElement)) {
				TemporalExtent previousExtent = temporalExtentsCache.get(noLongerMovedElement);
				noLongerMovedElement.getMember(TemporalMember.class).setExtent(previousExtent);
				paletteElements.add(noLongerMovedElement);
			}
		}
		updatePalettes(paletteElements);
		lastTimeDelta = finalTimeDelta;
	}

	/*
	 * Coloring functions
	 */
	
	public void updatePalettes() {
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		if (modifier instanceof ConstrainedPlanModifier) {
			final Set<EPlanElement> planElements = new HashSet<EPlanElement>(changedTimes.keySet());
			updatePalettes(planElements);
		}
	}

	private void updatePalettes(final Iterable<EPlanElement> planElements) {
		WidgetUtils.runInDisplayThread(planTimeline.getControl(), new Runnable() {
			@Override
			public void run() {
				for (EPlanElement pe : planElements) {
					updatePalette(pe);
				}
			}
		}, true);
	}

	private void updatePalette(EPlanElement pe) {
		for (EditPartViewer viewer : planTimeline.getTimelineViewers()) {
			Map editPartRegistry = viewer.getEditPartRegistry();
			List<GraphicalEditPart> editParts = (List<GraphicalEditPart>) editPartRegistry.get(pe);
			if (editParts != null) {
				for (GraphicalEditPart ep : editParts) {
					if (ep instanceof TemporalNodeEditPart) {
						TemporalNodeEditPart tep = (TemporalNodeEditPart) ep;
						BarFigure f = (BarFigure) tep.getFigure();
						f.setPalette(getPalette(pe, tep.getTemporalExtent()));
					}
				}
			}
		}
	}
	
	private Palette getPalette(EPlanElement element, TemporalExtent extent) {
		if (element instanceof EPlan) {
			return BarFigure.Palette.NORMAL;
		}
		if (isDragging()) {
			if (atEdgeOfConstraints(element, extent.getStart(), extent.getEnd())) {
				return BarFigure.Palette.UNMOVING;
			}
			TemporalExtent initExtent = temporalExtentsCache.get(element);
			Date currStart = extent.getStart();
			Date initStart = (initExtent != null ? initExtent.getStart() : currStart);
			if (!CommonUtils.equals(initStart, currStart)) {	
				return BarFigure.Palette.MOVING;
			}
		}
		return BarFigure.Palette.NORMAL;
	}

	private boolean atEdgeOfConstraints(EPlanElement element, Date start, Date end) {
		IPlanModifier m = PlanModifierMember.get(plan).getModifier();
		if (m instanceof ConstrainedPlanModifier) {
			IPlanConstraintInfo info = ((ConstrainedPlanModifier)m).getPlanConstraintInfo();
			ConsistencyBounds simpleBounds = info.getBounds(element);
			return isCloseEnough(simpleBounds.getEarliestStart(), start)
					|| isCloseEnough(simpleBounds.getLatestEnd(), end);
		}
		return false;
	}

	private boolean isCloseEnough(Date date1, Date date2) {
		return (date1 != null) && (date2 != null) && (Math.abs(DateUtils.subtract(date1, date2)) < 1000);
	}

}
