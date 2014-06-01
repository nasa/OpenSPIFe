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
package gov.nasa.ensemble.core.plan.constraints.ui;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class DistanceConstraintTransferExtension extends AbstractPlanTransferableExtension {

	public static final String TEMPORAL_DISTANCE_CONSTRAINTS_KEY = "temporalDistanceConstraints";
	
	@Override
	public void postGetHook(PlanTransferable transferable) {
		Set<BinaryTemporalConstraint> transferableConstraints = new LinkedHashSet<BinaryTemporalConstraint>();
		Set<? extends EPlanElement> elements = EPlanUtils.computeContainedElements(transferable.getPlanElements());
		for (EPlanElement element : elements) {
			transferableConstraints.addAll(ConstraintUtils.getBinaryConstraints(element, false));
		}
		setTemporalDistanceConstraints(transferable, transferableConstraints);
	}

	@Override
	public void postCopyHook(PlanTransferable original, PlanTransferable copy) {
		Map<EPlanElement, EPlanElement> oldElementToNewPlanElement = PlanTransferable.createCopyPlanElementMap(original, copy);
 		Set<BinaryTemporalConstraint> copyConstraints = new LinkedHashSet<BinaryTemporalConstraint>();
 		Collection<BinaryTemporalConstraint> originalConstraints = getTemporalDistanceConstraints(original);
 		if (originalConstraints != null) {
 			for (BinaryTemporalConstraint originalConstraint : originalConstraints) {
 				EPlanElement originalA = originalConstraint.getPointA().getElement();
 				EPlanElement originalB = originalConstraint.getPointB().getElement();
 				EPlanElement copyA = oldElementToNewPlanElement.get(originalA);
 				EPlanElement copyB = oldElementToNewPlanElement.get(originalB);
 				if ((copyA != null) && (copyB != null)) {
 					Timepoint pointA = originalConstraint.getPointA().getEndpoint();
 					String anchorA = originalConstraint.getPointA().getAnchor();
 					Timepoint pointB = originalConstraint.getPointB().getEndpoint();
 					String anchorB = originalConstraint.getPointB().getAnchor();
 					Amount<Duration> min = originalConstraint.getMinimumBminusA();
 					Amount<Duration> max = originalConstraint.getMaximumBminusA();
 					String rationale = originalConstraint.getRationale();
 					BinaryTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
 					constraint.getPointA().setElement(copyA);
 					constraint.getPointA().setEndpoint(pointA);
 					constraint.getPointA().setAnchor(anchorA);
 					constraint.getPointB().setElement(copyB);
 					constraint.getPointB().setEndpoint(pointB);
 					constraint.getPointB().setAnchor(anchorB);
 					constraint.setMinimumBminusA(min);
 					constraint.setMaximumBminusA(max);
 					constraint.setRationale(rationale);
 					copyConstraints.add(constraint);
 				}
 			}
 			setTemporalDistanceConstraints(copy, copyConstraints);
 		}
	}

	@Override
	public void postUnpackHook(PlanTransferable planTransferable) {
		Set<BinaryTemporalConstraint> constraints = getTemporalDistanceConstraints(planTransferable);
		if (constraints != null) {
			setTemporalDistanceConstraints(planTransferable, constraints);
		}
	}
	
	@Override
	public void postAddHook(IPlanElementTransferable transferable, IStructureLocation location) {
		final Collection<BinaryTemporalConstraint> constraints = getTemporalDistanceConstraints(transferable);
		if ((constraints == null) || constraints.isEmpty()) {
			return;
		}
		TransactionUtils.writing(constraints.iterator().next(), new Runnable() {
			@Override
			public void run() {
				for (BinaryTemporalConstraint constraint : constraints) {
					EPlanElement planElementA = constraint.getPointA().getElement();
					EPlanElement planElementB = constraint.getPointB().getElement();
					if ((planElementA != null) && (planElementB != null)
						&& (EPlanUtils.getPlan(planElementA) == EPlanUtils.getPlan(planElementB))
						&& !planElementA.getMember(ConstraintsMember.class, true).getBinaryTemporalConstraints().contains(constraint)
						&& !planElementB.getMember(ConstraintsMember.class, true).getBinaryTemporalConstraints().contains(constraint)) {
						ConstraintUtils.attachConstraint(constraint);
					}
				}
			}
		});
	}

	@Override
	public void preRemoveHook(IPlanElementTransferable transferable, IStructureLocation location) {
		final Collection<BinaryTemporalConstraint> constraints = getTemporalDistanceConstraints(transferable);
		if ((constraints == null) || constraints.isEmpty()) {
			return;
		}
		TransactionUtils.writing(constraints.iterator().next(), new Runnable() {
			@Override
			public void run() {
				for (BinaryTemporalConstraint constraint : constraints) {
					ConstraintUtils.detachConstraint(constraint);
				}
			}
		});		
	}

	/*
	 * Store/restore data
	 */
	
	@SuppressWarnings("unchecked")
	private Set<BinaryTemporalConstraint> getTemporalDistanceConstraints(IPlanElementTransferable transferable) {
		return (Set<BinaryTemporalConstraint>)transferable.getData(TEMPORAL_DISTANCE_CONSTRAINTS_KEY);
	}
	
	private void setTemporalDistanceConstraints(IPlanElementTransferable transferable, Set<BinaryTemporalConstraint> transferableConstraints) {
		transferable.setData(TEMPORAL_DISTANCE_CONSTRAINTS_KEY, transferableConstraints);
	}

}
