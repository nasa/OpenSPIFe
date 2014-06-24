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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestConstraintUtils extends TestCase {

	public void testTemporalBoundAccess() {
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		plan.setName("plan");
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		plan.getChildren().add(group);
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		group.getChildren().add(activity);
		ConstraintsMember constraintsMember = activity.getMember(ConstraintsMember.class, true);
		List<PeriodicTemporalConstraint> periodicConstraints = constraintsMember.getPeriodicTemporalConstraints();
		assertEquals("TemporalBound Access: ", 0, periodicConstraints.size());
		PeriodicTemporalConstraint startConstraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		startConstraint.getPoint().setEndpoint(Timepoint.START);
		startConstraint.getPoint().setElement(activity);
		periodicConstraints.add(startConstraint);
		PeriodicTemporalConstraint endConstraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		endConstraint.getPoint().setEndpoint(Timepoint.END);
		endConstraint.getPoint().setElement(activity);
		periodicConstraints.add(endConstraint);
		assertEquals("TemporalBound Access: ", 2, periodicConstraints.size());
		assertEquals("TemporalBound Access: ", 1, ConstraintUtils.getPeriodicConstraints(activity, Timepoint.START, true).size());
		assertEquals("TemporalBound Access: ", 1, ConstraintUtils.getPeriodicConstraints(activity, Timepoint.END, true).size());
		List<PeriodicTemporalConstraint> constraints = new ArrayList<PeriodicTemporalConstraint>(ConstraintUtils.getPeriodicConstraints(activity, true));
        int size = constraints.size();
        for (PeriodicTemporalConstraint constraint : constraints) {
            ConstraintUtils.detachConstraint(constraint);
            assertEquals("TemporalBound Access: ", --size, ConstraintUtils.getPeriodicConstraints(activity, true).size());
        }
	}

	public void testTemporalRelationAccess() {
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		plan.setName("plan");
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		plan.getChildren().add(group);
		EActivity a = PlanFactory.eINSTANCE.createEActivity();
		group.getChildren().add(a);
		ConstraintsMember constraintsMemberA = a.getMember(ConstraintsMember.class, true);
		List<BinaryTemporalConstraint> binaryTemporalConstraintsA = constraintsMemberA.getBinaryTemporalConstraints();
		assertEquals("TemporalRelation Access: ", 0, binaryTemporalConstraintsA.size());
		EActivity b = PlanFactory.eINSTANCE.createEActivity();
		group.getChildren().add(b);
		ConstraintsMember constraintsMemberB = b.getMember(ConstraintsMember.class, true);
		List<BinaryTemporalConstraint> binaryTemporalConstraintsB = constraintsMemberB.getBinaryTemporalConstraints();
		assertEquals("TemporalRelation Access: ", 0, binaryTemporalConstraintsB.size());
		createRelations(plan, a, b);
		assertEquals("TemporalRelation Access: ", 4, binaryTemporalConstraintsA.size());
		assertEquals("TemporalRelation Access: ", 4, binaryTemporalConstraintsB.size());
		assertEquals("TemporalRelation Access: ", 2, ConstraintUtils.getBinaryConstraints(a, Timepoint.START, true).size());
		assertEquals("TemporalRelation Access: ", 2, ConstraintUtils.getBinaryConstraints(a, Timepoint.END, true).size());
        List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>(ConstraintUtils.getBinaryConstraints(a, true));
        int size = constraints.size();
        for (BinaryTemporalConstraint constraint : constraints) {
            --size;
            ConstraintUtils.detachConstraint(constraint);
            assertEquals("TemporalRelation Access: ", size, ConstraintUtils.getBinaryConstraints(a, true).size());
            assertEquals("TemporalRelation Access: ", size, ConstraintUtils.getBinaryConstraints(b, true).size());
        }
	}
	
	private void createRelations(EPlan plan, final EActivity a, final EActivity b) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				for (Timepoint aTimepoint : Timepoint.values()) {
					for (Timepoint bTimepoint : Timepoint.values()) {
						BinaryTemporalConstraint r = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
						r.getPointA().setElement(a);
						r.getPointA().setEndpoint(aTimepoint);
						r.getPointB().setElement(b);
						r.getPointB().setEndpoint(bTimepoint);
						ConstraintUtils.attachConstraint(r);
					}
				}
			}
		});
	}
	
}
