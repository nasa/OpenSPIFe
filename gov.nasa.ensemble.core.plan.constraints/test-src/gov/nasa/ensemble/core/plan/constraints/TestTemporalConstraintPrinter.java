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

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.constraints.TemporalConstraintPrinter;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.MessageConstraintViolationPrinter;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import javax.measure.quantity.Duration;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcorePackage;
import org.jscience.physics.amount.Amount;

public class TestTemporalConstraintPrinter extends TestCase {
	
	public void testAnchorPointPrinting() {
		final EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef();
		def.setName("TestConstraintPrinterActivity");
		
		final String timeAttributeName = "timeAttribute";
		EAttribute timeAttribute = DictionaryFactory.eINSTANCE.createEAttributeParameter();
		EMFUtils.addAnnotation(timeAttribute, ParameterDescriptor.ANNOTATION_SOURCE, ParameterDescriptor.ANNOTATION_DETAIL_DISPLAY_NAME, "Time Attribute");
		timeAttribute.setEType(EcorePackage.Literals.EDATE);
		timeAttribute.setName(timeAttributeName);
		def.getEStructuralFeatures().add(timeAttribute);
		
		ActivityDictionary.getInstance().getEClassifiers().add(def);
		
		final EPlan plan = PlanFactory.getInstance().createPlan("TestConstraintPrinterTestPlan");
		
		TransactionUtils.writing(plan, new Runnable() {

			@Override
			public void run() {
				EActivity elementA = PlanFactory.getInstance().createActivity(def);
				plan.getChildren().add(elementA);
				
				EActivity elementB = PlanFactory.getInstance().createActivity(def);
				plan.getChildren().add(elementB);
				
				Timepoint timepoint = Timepoint.START;
				String anchor = timeAttributeName;
				
				System.out.println();
				debugConstraint(elementA, anchor, 	null, 		elementB, anchor, 	null, 		DateUtils.ZERO_DURATION, 	DateUtils.ZERO_DURATION);
				debugConstraint(elementA, anchor, 	null, 		elementB, anchor, 	null, 		null, 						DateUtils.ZERO_DURATION);
				debugConstraint(elementA, anchor, 	null, 		elementB, anchor, 	null, 		DateUtils.ZERO_DURATION, 	null);
				
				System.out.println();
				debugConstraint(elementA, anchor, 	null, 		elementB, null, 	timepoint, 	DateUtils.ZERO_DURATION, 	DateUtils.ZERO_DURATION);
				debugConstraint(elementA, anchor, 	null, 		elementB, null, 	timepoint, 	null, 						DateUtils.ZERO_DURATION);
				debugConstraint(elementA, anchor, 	null, 		elementB, null, 	timepoint, 	DateUtils.ZERO_DURATION, 	null);
				
				System.out.println();
				debugConstraint(elementA, null, 	timepoint, 	elementB, anchor, 	null, 		DateUtils.ZERO_DURATION, 	DateUtils.ZERO_DURATION);
				debugConstraint(elementA, null, 	timepoint, 	elementB, anchor, 	null, 		null, 						DateUtils.ZERO_DURATION);
				debugConstraint(elementA, null, 	timepoint, 	elementB, anchor, 	null, 		DateUtils.ZERO_DURATION, 	null);
			}

			private void debugConstraint(EActivity elementA, String anchorA, Timepoint timepointA, EActivity elementB, String anchorB, Timepoint timepointB, Amount<Duration> minBminusA, Amount<Duration> maxBminusA) {
				BinaryTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
				constraint.getPointA().setElement(elementA);
				constraint.getPointA().setAnchor(anchorA);
				constraint.getPointA().setEndpoint(timepointA);
				constraint.getPointB().setElement(elementB);
				constraint.getPointB().setAnchor(anchorB);
				constraint.getPointB().setEndpoint(timepointB);
				constraint.setMinimumBminusA(minBminusA);
				constraint.setMaximumBminusA(maxBminusA);
				constraint.setRationale(null);
				
				TemporalConstraintPrinter printer = new TemporalConstraintPrinter(new IdentifiableRegistry<EPlanElement>());
				String constraintText = printer.getText(constraint, elementA);
				System.out.println(constraintText);
				
				String violationText = new MessageConstraintViolationPrinter().getDistanceText(constraint);
				System.out.println(violationText);
			}
		});
		
		ActivityDictionary.getInstance().restoreDefaultDictionary();
	}
	
}
