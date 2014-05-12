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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyMaintenanceSystemConsistencyListener;
import gov.nasa.ensemble.core.plan.temporal.OffsetsConsistencyMaintenanceListener;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Arrays;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public abstract class AbstractResourceUpdaterTest extends AbstractResourceTest {
	
	protected static final ActivityDictionary AD = ActivityDictionary.getInstance();
	protected static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	protected EPlan plan;
	protected ResourceUpdater resourceUpdater;
	
	@Override
	protected EPlan createPlan() {
		ResourceUpdaterFactory.setTestingResourceUpdater(true);
		final EPlan plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
			}
		});
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		EPlanUtils.contributeProductResources(plan);
		ExtensionPointResourceSetListener.addListener(domain, 
			Arrays.asList(new DependencyMaintenanceSystemConsistencyListener(), new OffsetsConsistencyMaintenanceListener()), 
			Arrays.asList(new ResourceUpdaterModelChangeListener()));
		return plan;
	}

	@Override
	protected void setUp() throws Exception {
		plan = createPlan();
		resourceUpdater = WrapperUtils.getMember(plan, ResourceUpdater.class);
		assertNotNull(resourceUpdater);
	}
	
	@Override
	protected void tearDown() throws Exception {
		ResourceUpdaterFactory.setTestingResourceUpdater(false);
		WrapperUtils.dispose(plan);
	}

	protected void executeTest(Runnable editRunnable, Runnable assertRunnable) throws InterruptedException {
		TransactionUtils.writing(plan, editRunnable);
		resourceUpdater.waitUntilFinished();
		assertRunnable.run();
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T eGet(EObject object, String featureName) {
		return (T) object.eGet(object.eClass().getEStructuralFeature(featureName));
	}

	protected void eSet(EObject object, String featureName, Object value) {
		object.eSet(object.eClass().getEStructuralFeature(featureName), value);
	}
	
}
