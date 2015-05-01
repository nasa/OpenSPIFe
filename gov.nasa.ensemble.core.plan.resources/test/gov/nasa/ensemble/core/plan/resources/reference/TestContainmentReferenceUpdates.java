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
package gov.nasa.ensemble.core.plan.resources.reference;

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.resources.AbstractResourceUpdaterTest;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EResourceDef;

import java.util.Date;
import java.util.List;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class TestContainmentReferenceUpdates extends AbstractResourceUpdaterTest {

	private static final String ACTIVE_REFERENCES_COUNT = "activeReferencesCount";
	private EResourceDef activeReferenceCountDef;

	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestContainmentReferenceUpdates.dictionary", true));
		activeReferenceCountDef = AD.getDefinition(EResourceDef.class, ACTIVE_REFERENCES_COUNT);
		super.setUp();
	}
	
	public void testActivityAdd() throws InterruptedException {
		final EActivityDef activityDef = getActivityDef("Activity");
		final EActivity activity = PLAN_FACTORY.createActivity(activityDef);
		final Date activityStart = DateUtils.add(PLAN_START, ONE_HOUR);
		activity.getMember(TemporalMember.class).setStartTime(activityStart);
		activity.getMember(TemporalMember.class).setDuration(ONE_HOUR);
		final Date activityEnd = activity.getMember(TemporalMember.class).getEndTime();
		executeTest(new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
			}
		}, new Runnable() {
			@Override
			public void run() {
				List<EObject> references = eGet(activity.getData(), "reference");
				assertEquals(5, references.size());
				for (EObject object : references) {
					assertEquals(Boolean.valueOf(false), eGet(object, "active"));
				}
				assertProfileValue(plan, activeReferenceCountDef, activityStart, Amount.ZERO);
				assertProfileValue(plan, activeReferenceCountDef, activityEnd, Amount.ZERO);
			}
		});
	}
	
	public void testActivityReferenceAdd() throws InterruptedException {
		testActivityAdd();
		final EActivity activity = (EActivity) plan.getChildren().get(0);
		final Date activityStart = activity.getMember(TemporalMember.class).getStartTime();
		final Date activityEnd = activity.getMember(TemporalMember.class).getEndTime();
		
		executeTest(new Runnable() {
			@Override
			public void run() {
				@SuppressWarnings("unchecked")
				List<EObject> references = eGet(activity.getData(), "reference");
				EClass objectDef = (EClass)AD.getEClassifier("ObjectDef");
				EObject object = AD.getEFactoryInstance().create(objectDef);
				eSet(object, "active", true);
				references.add(object);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertProfileValue(plan, activeReferenceCountDef, activityStart, Amount.ONE);
				assertProfileValue(plan, activeReferenceCountDef, activityEnd, Amount.ZERO);
			}
		});
		assertActivityRemovalEffects(activity);
	}
	
	public void testActivityReferenceEdit() throws InterruptedException {
		testActivityAdd();
		final EActivity activity = (EActivity) plan.getChildren().get(0);
		final Date activityStart = activity.getMember(TemporalMember.class).getStartTime();
		final Date activityEnd = activity.getMember(TemporalMember.class).getEndTime();
		
		executeTest(new Runnable() {
			@Override
			public void run() {
				List<EObject> references = eGet(activity.getData(), "reference");
				eSet(references.get(1), "active", true);
				eSet(references.get(3), "active", true);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertProfileValue(plan, activeReferenceCountDef, activityStart, Amount.valueOf(2, Unit.ONE));
				assertProfileValue(plan, activeReferenceCountDef, activityEnd, Amount.ZERO);
			}
		});
		assertActivityRemovalEffects(activity);
	}

	private void assertActivityRemovalEffects(final EActivity activity) throws InterruptedException {
		executeTest(new Runnable() {
			@Override
			public void run() {
				plan.getChildren().remove(activity);
			}
		}, new Runnable() {
			@Override
			public void run() {
				assertProfileValue(plan, activeReferenceCountDef, activity.getMember(TemporalMember.class).getStartTime(), Amount.ZERO);
				assertProfileValue(plan, activeReferenceCountDef, activity.getMember(TemporalMember.class).getEndTime(), Amount.ZERO);
			}
		});
	}
	
}
