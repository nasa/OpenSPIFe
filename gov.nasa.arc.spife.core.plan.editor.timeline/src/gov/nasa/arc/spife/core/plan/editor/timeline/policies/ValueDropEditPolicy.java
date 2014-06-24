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

import gov.nasa.arc.spife.core.plan.editor.timeline.util.RowHighlightHelper;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataRowEditPart;
import gov.nasa.ensemble.common.ui.gef.OperationCommand;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.CommandUndoableOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer.Conditional;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class ValueDropEditPolicy extends PlanTimelineViewerEditPolicy {

	public static final String ROLE = ValueDropEditPolicy.class.getName();
	
	private final RowHighlightHelper highlighter = new ValueChangeHighlighter(this);

	@Override
	public Command getCommand(Request request) {
		if (understandsRequest(request)) {
			ChangeBoundsRequest cbr = (ChangeBoundsRequest)request;
			Point location = cbr.getLocation();
			EditPart targetEditPart = getViewer().findObjectAtExcluding(location, Collections.emptyList(), new TreeTimelineDataRowConditional());
			if (targetEditPart != null) {
				Object model = targetEditPart.getModel();
				Set<EPlanElement> elements = new HashSet<EPlanElement>();
				for (Object object : ((ChangeBoundsRequest)request).getEditParts()) {
					EPlanElement pe = null;
					if (object instanceof EPlanElement) {
						pe = (EPlanElement) object;
					} else if (((EditPart)object).getModel() instanceof EPlanElement) {
						pe = (EPlanElement) ((EditPart)object).getModel();
					}
					if (pe != null) {
						elements.add(pe);
					}
				}
				if (!elements.isEmpty()) {
					EditingDomain domain = EMFUtils.getAnyDomain(getTimeline().getTimelineModel());
					if (domain != null) {
						org.eclipse.emf.common.command.Command command = null;
						if (REQ_CHANGE_VALUE_VIA_DROP.equals(request.getType())) {
							command = SetCommand.create(domain, model, null, elements);
						} else if (REQ_ADD_VALUE_VIA_DROP.equals(request.getType())) {
							command = AddCommand.create(domain, model, null, elements);
						}
						if (command != null) {
							IUndoContext undoContext = EMFUtils.getUndoContext(domain);
							CommandUndoableOperation op = new CommandUndoableOperation(domain, command);
							op.addContext(new ObjectUndoContext(domain));
							return new OperationCommand(undoContext, op , getTimeline().getControl(), getViewer().getSite());
						}
					}
				}
			}
		}
		return super.getCommand(request);
	}
	
	@Override
	public void eraseTargetFeedback(Request request) {
		if (understandsRequest(request)) {
			highlighter.eraseHighlight();
		}
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (understandsRequest(request)) {
			Point pt = ((ChangeBoundsRequest)request).getLocation(); 
			highlighter.showHighlight(pt);
		}
		super.showTargetFeedback(request);
	}

	@Override
	public boolean understandsRequest(Request request) {
		Object type = request.getType();
		if (REQ_CHANGE_VALUE_VIA_DROP.equals(type)
				|| REQ_ADD_VALUE_VIA_DROP.equals(type)) {
			return !((ChangeBoundsRequest) request).getEditParts().isEmpty();
		}
		return super.understandsRequest(request);
	}

	private final class ValueChangeHighlighter extends RowHighlightHelper {
		private ValueChangeHighlighter(EditPolicy policy) {
			super(policy);
		}

		@Override
		protected IFigure getHostFigure(Point pt) {
			EditPart targetEditPart = getViewer().findObjectAtExcluding(pt, Collections.emptyList(), new TreeTimelineDataRowConditional());
			if (targetEditPart instanceof GraphicalEditPart) {
				return ((GraphicalEditPart)targetEditPart).getFigure();
			}
			return super.getHostFigure(pt);
		}
	}

	private final class TreeTimelineDataRowConditional implements Conditional {
		@Override
		public boolean evaluate(EditPart editPart) {
			return editPart instanceof TreeTimelineDataRowEditPart;
		}
	}
	
}
