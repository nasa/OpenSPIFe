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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.common.extension.DynamicExtensionUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.InputStream;
import java.util.Date;

import org.junit.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ITraversalStrategy;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class TestJSConstraint extends Assert {

	private static final URI AD_LOCATION = URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.formula.js/datafiles/TestJSConstraint.dictionary", true);
	private static final Date PLAN_START = new Date();
	private static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	private static final IBatchValidator batchValidator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);

	static {
		batchValidator.setTraversalStrategy(new ITraversalStrategy.Flat());
	}

	@SuppressWarnings("restriction")
	@BeforeClass
	public static void setupExtensions() throws Exception {
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
		Object key = ((ExtensionRegistry) registry).getTemporaryUserToken();
		Bundle bundle = Activator.getDefault().getBundle();
		IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);
		InputStream inputStream = null;
		try {
			inputStream = FileLocator.openStream(bundle, new Path("datafiles/TestJSConstraintExtension.xml"), false);
			registry.addContribution(inputStream, contributor, false, null, null, key);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		// wait until jobs kicked off by adding the contribution are complete
		DynamicExtensionUtils.waitForJobs();
	}

	private EPlan plan;
	private EActivityDef parameterTestActivityDef;
	private EActivity parameterTestActivity;

	@Before
	public void setUp() throws Exception {
		ActivityDictionary ad = ActivityDictionary.getInstance();
		try {
			ad.load(AD_LOCATION);
		} catch (Exception e) {
			fail("Failed to load AD from " + AD_LOCATION + ": " + e.getMessage());
		}

		parameterTestActivityDef = ad.getActivityDef("ParameterTestActivity");
		parameterTestActivity = PLAN_FACTORY.createActivity(parameterTestActivityDef);
		plan = PLAN_FACTORY.createPlan("TEST_JSCONSTRAINT_PLAN");

		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);

				EActivityGroup group = PLAN_FACTORY.createActivityGroup(plan);
				plan.getChildren().add(group);

				parameterTestActivity.getMember(TemporalMember.class).setStartTime(PLAN_START);
				group.getChildren().add(parameterTestActivity);
			}
		});
	}

	@After
	public void tearDown() throws Exception {
		try {
			WrapperUtils.dispose(plan);
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("disposing plan", e);
		}
		ActivityDictionary.getInstance().restoreDefaultDictionary();
	}

	@Test
	public void testDefaultParameterCondition() {
		assertTrue(validateConstraints());
	}

	@Test
	public void testFailingParameterCondition() {
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(parameterTestActivity, new Runnable() {
			@Override
			public void run() {
				EObject eObject = parameterTestActivity.getData();
				EClass eClass = eObject.eClass();
				EStructuralFeature f = eClass.getEStructuralFeature("Requires_equal_to_90");
				eObject.eSet(f, 91);
			}
		});
		assertFalse(validateConstraints());
	}

	/*
	 * public void testGenericConstraintSucceed() { assertTrue(validateConstraints()); }
	 * 
	 * public void testGenericConstraintFail() { assertFalse(validateConstraints()); }
	 */

	private boolean validateConstraints() {
		final IStatus status = batchValidator.validate(parameterTestActivity);
		return status.isOK();
	}

}
