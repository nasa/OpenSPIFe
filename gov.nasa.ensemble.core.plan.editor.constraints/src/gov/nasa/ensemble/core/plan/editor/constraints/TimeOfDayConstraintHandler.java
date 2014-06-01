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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.operation.CompositeOperation;
import gov.nasa.ensemble.core.jscience.TimeOfDayStringifier;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.plan.PlanPlugin;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalBoundOperation;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jscience.physics.amount.Amount;

public abstract class TimeOfDayConstraintHandler extends AbstractPlanEditorHandler {

	protected static final TimeOfDayStringifier TOD_STRINGIFIER = new TimeOfDayStringifier();
	private IWorkbenchWindow window;
	private MyValidator validator = new MyValidator();
	protected Object data = null;
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	protected abstract String getStartOrEndName();
	
	protected abstract Timepoint getStartOrEndObject();
	
	protected abstract String getEarliestOrLatestName();
	
	protected abstract String getEarlierOrLaterName();
	
	protected abstract Amount<Duration> getRelevantPartOfConstraint(PeriodicTemporalConstraint constraint);
	
	protected abstract void setRelevantPartOfConstraint(PeriodicTemporalConstraint constraint, Amount<Duration> offset);
	
	protected abstract boolean isRelevantConstraint(PeriodicTemporalConstraint constraint);
	
	@Override
	public boolean isCheckedForSelection(ISelection selection) {
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		boolean isChecked = !elements.isEmpty();
		for (EPlanElement element : elements) {
			ConstraintsMember member = element.getMember(ConstraintsMember.class, false);
			if (member == null || getRelevantConstraints(member).isEmpty()) {
				isChecked = false;
				break;
			}
		}
		return isChecked;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Command command = event.getCommand();
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		boolean isChecked = getCommandState(command);
		IUndoContext undoContext = getUndoContext();
		CompositeOperation op = new CompositeOperation(getEarliestOrLatestName());
		if (isChecked) {
			for (EPlanElement element : elements) {
				ConstraintsMember member = element.getMember(ConstraintsMember.class, true);
				Set<PeriodicTemporalConstraint> constraints = getRelevantConstraints(member);
				for (PeriodicTemporalConstraint constraint : constraints) {
					op.add(new DeleteTemporalBoundOperation(constraint));
				}
			}
		} else {
			Object data = showDialog();
			if (data instanceof Amount) {
				Amount<Duration> offset = (Amount<Duration>)data;
				if (!elements.isEmpty()) {
					ECollections.sort(elements, TemporalChainUtils.CHAIN_ORDER);
					for (EPlanElement planElement: elements) {
						op.add(createPeriodicTemporalConstraintOperation(planElement, offset, undoContext));
					}
				}
			}
		}
		CommonUtils.execute(op, undoContext);
		setCommandState(command, !isChecked);
		return null;
	}
	
	private PeriodicTemporalConstraint createPeriodicTemporalConstraint(EPlanElement planElement, Amount<Duration> offset) {
		PeriodicTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		ConstraintPoint point = constraint.getPoint();
		point.setElement(planElement);
		point.setEndpoint(getStartOrEndObject());
		setRelevantPartOfConstraint(constraint, offset);
		return constraint;
	}
	
	protected String getDialogText() {
		return "Constrain to never " +
				getStartOrEndName().toLowerCase() +
				" any " +
				getEarlierOrLaterName().toLowerCase() +
				" than:";
	}

	private class MyValidator implements IInputValidator {
		@Override
		public String isValid(String newText) {
			if (newText==null || newText.trim().length()==0) {
				return "Please enter a time of day.";
			}
			data = null;
			try {
				data = TOD_STRINGIFIER.getStandardSeconds(newText);
			} catch (Exception e) {
					return e.getMessage();
			}
			return null;
		}
	}

	private Object showDialog() {
		if (window == null) {
			window = PlanPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		}
		if (window != null) {
			InputDialog d = new InputDialog(window.getShell(), getEarliestOrLatestName(), getDialogText(), "", validator);
			int code = d.open();
			if (code == Window.OK) {
				return data;
			}
		}
		return null;
	}

	private TemporalBoundEditOperation createPeriodicTemporalConstraintOperation(EPlanElement planElement, Amount<Duration> offset, IUndoContext undoContext) {
		ConstraintsMember facet = planElement.getMember(ConstraintsMember.class, true);
		Set<PeriodicTemporalConstraint> oldConstraints = getRelevantConstraints(facet);
		PeriodicTemporalConstraint newConstraint = null;
		for (PeriodicTemporalConstraint oldConstraint : oldConstraints) {
			Amount<Duration> time = getRelevantPartOfConstraint(oldConstraint);
			if (time.compareTo(offset) == 0) {
				return null; // same as an existing pin
			}
		}			
		newConstraint = createPeriodicTemporalConstraint(planElement, offset);
		TemporalBoundEditOperation operation = new TemporalBoundEditOperation(getEarliestOrLatestName(), planElement, oldConstraints, newConstraint);
		if (undoContext != null) {
			operation.addContext(undoContext);
		}
		operation.addContext(undoContext);
		return operation;
	}

	private Set<PeriodicTemporalConstraint> getRelevantConstraints(ConstraintsMember facet) {
		Set<PeriodicTemporalConstraint> result = new LinkedHashSet<PeriodicTemporalConstraint>();
		for (PeriodicTemporalConstraint constraint : facet.getPeriodicTemporalConstraints()) {
			if (isRelevantConstraint(constraint)) {
				result.add(constraint);
			}
		}
		return result;
	}

}
