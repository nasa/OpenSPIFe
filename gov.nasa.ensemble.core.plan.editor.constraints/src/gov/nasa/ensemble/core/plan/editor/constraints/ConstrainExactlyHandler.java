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
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.CreateTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.DeleteTemporalRelationOperation;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jscience.physics.amount.Amount;

public class ConstrainExactlyHandler extends DrudgerySavingHandler {

	public ConstrainExactlyHandler() {
		super("Constrain Exactly", "Constraining Exactly");
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		if (elements.size() > 1) {
			CompositeOperation op = new CompositeOperation(actionName);
			LinkedHashSet<BinaryTemporalConstraint> commonConstraints = getCommonBinaryTemporalConstraints(elements);
			if (commonConstraints.size() > 0) {
				for (BinaryTemporalConstraint binaryTemporalConstraint : commonConstraints) {
					op.add(new DeleteTemporalRelationOperation(binaryTemporalConstraint));
				}				
			} else {
				ECollections.sort(elements, TemporalChainUtils.CHAIN_ORDER);
				Iterator<? extends EPlanElement> iter = elements.iterator();
				EPlanElement actA = iter.next();
				while (iter.hasNext()) {
					EPlanElement actB = iter.next();
					Date startB = actB.getMember(TemporalMember.class).getStartTime();
					Date endA = actA.getMember(TemporalMember.class).getEndTime();
					Amount<Duration> offset = DateUtils.subtract(startB, endA);
					BinaryTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
					constraint.getPointA().setElement(actA);
					constraint.getPointA().setEndpoint(Timepoint.END);
					constraint.getPointB().setElement(actB);
					constraint.getPointB().setEndpoint(Timepoint.START);
					constraint.setMinimumBminusA(offset);
					constraint.setMaximumBminusA(offset);
					constraint.setRationale(actionName);
					op.add(new CreateTemporalRelationOperation(constraint));
					actA = actB;
				}
			}
			IUndoContext undoContext = getUndoContext();
			CommonUtils.execute(op, undoContext);
		}
		return null;
	}

	@Override
	public boolean isEnabledForSelection(ISelection selection) {
		boolean isEnabled = super.isEnabledForSelection(selection);
		if (isEnabled) {
			List<EPlanElement> elements = new ArrayList(PlanEditorUtil.emfFromSelection(selection));
			if (ConstraintUtils.isNested(elements)) {
				return false;
			}
		}
		return isEnabled;
	}
	
	@Override
	public boolean isCheckedForSelection(ISelection selection) {
		List<EPlanElement> elements = getSelectedTemporalElements(selection);
		for (EPlanElement element : elements) {
			if ((element.getMember(ConstraintsMember.class, false) == null) 
				|| ((element instanceof EActivityGroup) 
					&& !ConstraintsPlugin.ALLOW_PULL_GROUP_CONSTRAINTS)) {
				return false;
			}
		}
		return (getCommonBinaryTemporalConstraints(elements).size() > 0);	
	}
	
	/**
	 * This method is used to determine if every item in the given list of
	 * EPlanElement objects has at least one constraint which is the same for
	 * all items. The constraint which is common to all the items is returned.
	 * @param elements
	 * @return the common constraints.
	 */
	private static LinkedHashSet<BinaryTemporalConstraint> getCommonBinaryTemporalConstraints(List<? extends EPlanElement> elements) {
		LinkedHashSet<BinaryTemporalConstraint> constraints = new LinkedHashSet<BinaryTemporalConstraint>();
		LinkedHashSet<BinaryTemporalConstraint> result = new LinkedHashSet<BinaryTemporalConstraint>();
		if (elements == null || elements.size() == 0) {
			return result;
		}
		// go through each plan element to get a list of all constraints in the selection
		for (EPlanElement element : elements) {
			constraints.addAll(ConstraintUtils.getBinaryConstraints(element, true));					
		}
		/*
		 * now that we have all the constraints involved, see if all of the 
		 * elements involved in a particular constraint are within the given
		 * selection
		 */
		for (BinaryTemporalConstraint binaryTemporalConstraint : constraints) {
			LinkedHashSet<EPlanElement> constrainedElements = ConstraintUtils.getConstrainedEPlanElements(binaryTemporalConstraint);
			if (elements.containsAll(constrainedElements)) {
				result.add(binaryTemporalConstraint);
			}
		}
		return result;
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> sortedElements) {
		return Collections.EMPTY_MAP;
	}
	
	@Override
	public String getCommandId() {
		return CONSTRAINT_EXACTLY_COMMAND_ID;
	}


}
