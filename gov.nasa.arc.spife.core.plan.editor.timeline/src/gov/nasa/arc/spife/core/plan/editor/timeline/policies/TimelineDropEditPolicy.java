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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.commands.TimelineAddOperation;
import gov.nasa.arc.spife.core.plan.editor.timeline.util.RowHighlightHelper;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.EnsembleRequestConstants;
import gov.nasa.ensemble.common.ui.gef.OperationCommand;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanStructureLocation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanStructureOperationContributor;
import gov.nasa.ensemble.core.plan.temporal.TemporalTransferableExtension;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.IWorkbenchPartSite;

public abstract class TimelineDropEditPolicy extends DropEditPolicy {

	private static final int BOUNDARY_MARGIN = 3;

	private static final String SHOW_HIGLIGHT_PROPERTY = "timeline.row.drop.highlight";
	private static final boolean SHOW_HIGLIGHT = EnsembleProperties.getBooleanPropertyValue(SHOW_HIGLIGHT_PROPERTY, false);

	private final RowHighlightHelper highlighter = new RowHighlightHelper(this);
	
	protected abstract EPlanElement getTargetNode(); 
	
	protected PlanTimelineViewer getCastedViewer() {
		return (PlanTimelineViewer) getHost().getViewer();		
	}
	
	/**
	 * Computes the time that a drop corresponds to by figuring
	 * out the screen offset from the parent node's start position, 
	 * transforming the screen offset into a duration, and adding
	 * that duration to the parent node's start time.
	 * @param dropLocation
	 * @return the time where the drop occurred
	 */
	protected Date getDropTime(Point dropLocation) {
		Page page = getCastedViewer().getPage();
		int node_x = ((GraphicalEditPart)getHost()).getFigure().getBounds().x;
		Date date = page.getTime(dropLocation.x - node_x);
		int msMoveThreshold = (int) page.getZoomOption().getMsMoveThreshold();
		return MissionCalendarUtils.round(date.getTime(), msMoveThreshold);
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.ensemble.common.ui.gef.DropEditPolicy#getDropCommand(org.eclipse.gef.Request)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected Command getDropCommand(Request request) {
		final PlanTimelineViewer viewer = getCastedViewer();
		IUndoContext undoContext = TransactionUtils.getUndoContext(viewer.getPlan());
		EPlanElement targetPlanElement = getTargetNode();
		DropTargetEvent event = getDropTargetEvent(request); 
		Point dropLocation = getDropLocation(request);
		Date startTime = getDropTime(dropLocation);
		ITransferable transferable = TransferRegistry.getInstance().getDroppedObjects(event.data, event.currentDataType);
		final PlanEditorModel model = viewer.getPlanEditorModel();
		InsertionSemantics semantics = InsertionSemantics.ON;
		if (!EnsembleDragAndDropOracle.isDragSourceEditorModel(model)
			&& ((event.operations & DND.DROP_COPY) != 0)) {
			event.detail = DND.DROP_COPY;
		}
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		if (event.detail == DND.DROP_COPY) {
			transferable = modifier.copy(transferable);
		}
		StructuredSelection selection;
		if (targetPlanElement == null) {
			selection = new StructuredSelection();
		} else {
			selection = new StructuredSelection(targetPlanElement); 
		}
		
		IFigure figure = ((GraphicalEditPart)getHost()).getFigure();		
		int y = figure.getBounds().y;
		int height = figure.getBounds().height;
		int lowerBound = y;		
		int upperBound = y + height;
		
		if ((dropLocation.y - lowerBound) < BOUNDARY_MARGIN) {
			semantics = InsertionSemantics.BEFORE;
		} else if ((upperBound - dropLocation.y) < BOUNDARY_MARGIN) {
			semantics = InsertionSemantics.AFTER;
		}
		
		boolean canInsert = false;
		for (TransferData d : getTransfer(request).getSupportedTypes()){
			canInsert = modifier.canInsert(d, selection, InsertionSemantics.ON);
			if (canInsert) break;
		}

		if (!canInsert) {
			semantics = InsertionSemantics.ON;
		}
		if (transferable instanceof IPlanElementTransferable) {
			TemporalTransferableExtension.setTransferableStartTime((IPlanElementTransferable)transferable, startTime);
		}
		IStructureLocation location = modifier.getInsertionLocation(transferable, selection, semantics);
		IUndoableOperation op = new TimelineAddOperation(transferable, modifier, location, viewer.getTimeline());
		op = PlanStructureOperationContributor.addContributorOperations(op, (IPlanElementTransferable)transferable, (PlanStructureLocation)location);
		if (isChangeValueRequest()) {
			ChangeBoundsRequest changeBoundRequest = new ChangeBoundsRequest(TimelineConstants.REQ_CHANGE_VALUE_VIA_DROP);
			if (transferable instanceof IPlanElementTransferable) {
				IPlanElementTransferable planTransferable = (IPlanElementTransferable) transferable;
				changeBoundRequest.setEditParts(planTransferable.getPlanElements());
			}

			changeBoundRequest.setLocation(dropLocation);
			Command command = getHost().getCommand(changeBoundRequest);
			//
			// This is terribly annoying, EMF wrapped in Operation, wrapped in GEF
			if (command instanceof OperationCommand) {
				IUndoableOperation setValueOperation = ((OperationCommand)command).getOperation();
				CompositeOperation compound = new CompositeOperation(op.getLabel());
				compound.add(setValueOperation);
				compound.add(op);
				op = compound;
			}
		}
		IWorkbenchPartSite site = viewer.getSite();
		return new OperationCommand(undoContext, op, event.widget, site);
	}

	private boolean isChangeValueRequest() {
		PlanTimelineViewer viewer = getCastedViewer();
		PlanTimeline timeline = viewer.getTimeline();
		int key = timeline.getModifierKey();
		return SWT.ALT == key;
	}
	
	@Override
	protected boolean isSupportedTransferData(TransferData d) {
		EPlanElement planElement = getTargetNode();
		if (planElement == null) {
			return false;
		}
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		StructuredSelection selection = new StructuredSelection(planElement);
		return modifier.canInsert(d, selection, InsertionSemantics.ON);
	}
	
	@Override
	public void showTargetFeedback(Request request) {
		super.showTargetFeedback(request);
		if (SHOW_HIGLIGHT && request.getType().equals(EnsembleRequestConstants.REQ_DROP)){
			Point dropLocation = (Point)request.getExtendedData().get(DROP_LOCATION);
			highlighter.showHighlight(dropLocation);
		}
			
		final Point pt = (Point)request.getExtendedData().get(DropEditPolicy.DROP_LOCATION);
		if (pt != null) {
			TimelineViewer viewer = getCastedViewer();
			
			final Page page = viewer.getPage();
			EditPart ep = viewer.getTimelineEditPart().getDataEditPart();
			int figureX = ((GraphicalEditPart)ep).getFigure().getBounds().x;
			long currentTime = page.getTime(pt.x - figureX).getTime();
			Timeline timeline = viewer.getTimeline();
			if (!CommonUtils.equals(timeline.getCursorTime(), currentTime)) {
				timeline.setCursorTime(currentTime);
			}
		}
	}
	
	@Override
	public void eraseTargetFeedback(Request request) {
		super.eraseTargetFeedback(request);
		if (SHOW_HIGLIGHT && EnsembleRequestConstants.REQ_DROP.equals(request.getType())) {
			highlighter.eraseHighlight();
		}
	}
	
}
