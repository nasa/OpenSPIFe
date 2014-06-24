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
package gov.nasa.arc.spife.europa.export.model;

import gov.nasa.arc.spife.europa.export.model.wizards.EuropaModelExportWizard;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.unit.NonSI;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class TestEuropa extends TestCase {

	protected final ActivityDictionary AD = ActivityDictionary.getInstance();
	
	protected final Date START_TIME = new Date(System.currentTimeMillis());
	
	private EPlan plan = null;
	//private EuropaMember member;
	private EActivity activity0;
	private EActivity activity1;
	
	private EObject object0;
	private EObject object1;

	private EStructuralFeature singletonClaim;

	private EStructuralFeature multipleClaims;

	private boolean modeluploaded = false;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		//
		// Load the AD
		AD.load(URI.createPlatformPluginURI("/gov.nasa.arc.spife.europa.export.model/datafiles/test/TestEuropa.dictionary", true));
		if (!modeluploaded) {
			// Upload the model
			EuropaModelExportWizard.uploadModel(AD, true, new NullProgressMonitor());
			modeluploaded = true;
		}
		
		plan = PlanFactory.getInstance().createPlan("EUROPA_TEST_PLAN");
		final EActivityGroup group = PlanFactory.getInstance().createActivityGroupInstance();
		
		EClassifier testObjectDef = AD.getEClassifier("TestObjectDef");
		assertTrue(testObjectDef instanceof EClass);
		
		EClass testObjectDefEClass = (EClass) testObjectDef;
		EStructuralFeature f = testObjectDefEClass.getEStructuralFeature("name");
		assertNotNull(f);
		
		object0 = AD.getEFactoryInstance().create(testObjectDefEClass);
		object0.eSet(f, "object0");
		object1 = AD.getEFactoryInstance().create(testObjectDefEClass);
		object1.eSet(f, "object1");
		
		EActivityDef activityThatClaimsDef = AD.getActivityDef("ActivityThatClaims");
		assertNotNull(activityThatClaimsDef);
		
		singletonClaim = activityThatClaimsDef.getEStructuralFeature("singletonClaim");
		assertNotNull(singletonClaim);
		
		multipleClaims = activityThatClaimsDef.getEStructuralFeature("multipleClaims");
		assertNotNull(multipleClaims);
		
		activity0 = PlanFactory.getInstance().createActivity(activityThatClaimsDef);
		activity1 = PlanFactory.getInstance().createActivity(activityThatClaimsDef);
		
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.eResource().getContents().add(object0);
				plan.eResource().getContents().add(object1);
				
				group.getChildren().add(activity0);
				group.getChildren().add(activity1);
				plan.getChildren().add(group);
				
				plan.getMember(TemporalMember.class).setStartTime(START_TIME);
				group.getMember(TemporalMember.class).setStartTime(START_TIME);
				activity0.getMember(TemporalMember.class).setStartTime(START_TIME);
				activity0.getMember(TemporalMember.class).setDuration(Amount.valueOf(1, NonSI.HOUR));
				activity1.getMember(TemporalMember.class).setStartTime(START_TIME);
				activity1.getMember(TemporalMember.class).setDuration(Amount.valueOf(1, NonSI.HOUR));
			}
		});		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		WrapperUtils.dispose(plan);
	}
}
