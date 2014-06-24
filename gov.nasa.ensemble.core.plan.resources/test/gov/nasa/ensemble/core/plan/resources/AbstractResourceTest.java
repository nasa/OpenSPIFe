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

import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public abstract class AbstractResourceTest extends TestCase {

	protected static final ActivityDictionary AD = ActivityDictionary.getInstance();
	protected static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	
	protected static final Amount<Duration> ONE_HOUR = Amount.valueOf(1, NonSI.HOUR);
	protected static final Amount<Duration> ONE_DAY = Amount.valueOf(1, NonSI.DAY);
	
	protected static final Date PLAN_START = new Date();
	
	public void load(URI uri) {
		assertTrue(EMFUtils.exists(uri));
		AD.load(uri);
	}
	
	public EActivityDef getActivityDef(String key) {
		EActivityDef activityDef = AD.getActivityDef(key);
		assertNotNull(activityDef);
		assertNotNull(activityDef.getEPackage());
		return activityDef;
	}
	
	public void assertParameterValue(EActivity activity, String key, Object expectedValue) throws UndefinedParameterException {
		Object object = ADParameterUtils.getParameterObject(activity, key);
		assertEquals(expectedValue, object);
	}
	
	public EPlan createPlan(final EActivity activity) {
		final EPlan plan = createPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(activity);
			}
		});
		return plan;
	}

	protected EPlan createPlan() {
		final EPlan plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		EPlanUtils.contributeProductResources(plan);
		return plan;
	}

	protected EPlan createPlan(URI planUri) throws IOException {
		final EPlan plan = EPlanUtils.loadOnePlan(planUri);
		EPlanUtils.contributeProductResources(plan);
		recomputePlan(plan);
		return plan;
	}

	protected void recomputePlan(EPlan plan) {
		long start = System.currentTimeMillis();
		ResourceUpdater.recompute(plan);
		System.out.println("compute time: "+ (System.currentTimeMillis() - start) + " ms");
	}
	
	protected void assertADEffect(EPlanElement planElement, String resourceKey, Object expectedValue, boolean reformat) {
		EResourceDef resourceDef = AD.getDefinition(EResourceDef.class, resourceKey);
		assertNotNull(resourceDef);
		assertADEffect(planElement, resourceDef, expectedValue, reformat);
	}
	
	protected void assertADEffect(EPlanElement planElement, EResourceDef resourceDef, Object expectedValue) {
		assertADEffect(planElement, resourceDef, expectedValue, false);
	}
	
	@SuppressWarnings("unchecked")
	protected void assertADEffect(EPlanElement planElement, EResourceDef resourceDef, Object expectedValue, boolean reformat) {
		ComputableAmount amount = ADEffectUtils.getEffectAmount(planElement, resourceDef);
		assertNotNull(amount);
		assertEquals("expected computing state to be COMPLETE on "+planElement.getName()+"."+resourceDef, ComputingState.COMPLETE, amount.getComputing());
		assertResourceValue(expectedValue, amount.getAmount(), reformat);
	}

	protected void assertAmountProximity(final Amount expected, Amount actual) {
		assertTrue("Expected '"+"Expected '"+expected+"' but got '"+actual+"'", expected.approximates(actual));
	}
	
	protected void setParameterValue(final EActivity activity, final String key, final Object value) {
		TransactionUtils.writing(activity, new Runnable() {
			@Override
			public void run() {
				try {
					ADParameterUtils.setParameterObject(activity, key, value);
					assertEquals(value, ADParameterUtils.getParameterObject(activity, key));
				} catch (UndefinedParameterException e) {
					fail(e.getMessage());
				}
			}
		});
	}
	
	protected void assertProfileValue(EPlan plan, EResourceDef def, Date date, Object expected) {
		assertProfileValue(plan, def, date, expected, false);
	}
	
	protected void assertProfileValue(EPlan plan, EResourceDef def, Date date, Object expected, boolean reformat) {
		assertProfileValue(plan, def.getName(), date, expected, reformat);
	}

	protected void assertProfileValue(EPlan plan, String profileName, Date date, Object expected) {
		assertProfileValue(plan, profileName, date, expected, false);
	}
	
	protected void assertProfileValue(EPlan plan, String profileName, Date date, Object expectedValue, boolean reformat) {
		Profile<?> profile = getProfile(plan, profileName);
		Object value = profile.getValue(date);
		assertResourceValue(expectedValue, value, reformat);
	}

	private void assertResourceValue(Object expectedValue, Object actualValue, boolean reformat) {
		if (expectedValue instanceof Amount) {
			assertNotNull("Expected '"+expectedValue+"' but got null", actualValue);
			assertTrue("Expacted value of type Amount but got "+actualValue.getClass(), actualValue instanceof Amount);
			if (reformat) {
				String actualValueString = EnsembleAmountFormat.INSTANCE.formatAmount((Amount<?>) actualValue);
				try {
					actualValue = EnsembleAmountFormat.INSTANCE.parseAmount(actualValueString, Unit.ONE);
				} catch (Exception e) {
					fail("cannot parse '"+actualValueString+"': "+e.getMessage());
				}
			}
			assertAmountProximity((Amount)expectedValue, (Amount)actualValue);
		} else if (reformat && expectedValue instanceof String && actualValue instanceof Amount) {
			String actualValueString = EnsembleAmountFormat.INSTANCE.formatAmount((Amount<?>) actualValue);
			assertEquals("Expected '"+expectedValue+"' but got '"+actualValueString+"'", expectedValue, actualValueString);
		} else if (expectedValue instanceof Number) {
			assertNotNull("Expected '"+expectedValue+"' but got null", actualValue);
			assertTrue("Expacted value of type Number but got "+actualValue.getClass(), actualValue instanceof Number);
			assertEquals("Expected '"+expectedValue+"' but got '"+actualValue+"'", ((Number)expectedValue).doubleValue(), ((Number)actualValue).doubleValue());
		} else {
			assertEquals("Expected '"+expectedValue+"' but got '"+actualValue+"'", expectedValue, actualValue);
		}
	}

	protected Profile<?> getProfile(final EPlan plan, String profileKey) {
		Profile<?> profile = ResourceUtils.getProfile(plan, profileKey);
		assertNotNull("no profile with id '"+profileKey+"' found", profile);
		return profile;
	}
	
	protected String createPlanElementCSV(EPlanElement planElement, String resourceKey, String valueString) {
		return new StringBuffer()
			.append("planElement, ")
			.append(planElement.getName())
			.append(",")
			.append(resourceKey)
			.append(",")
			.append(valueString)
			.append("\n")
			.toString();
	}
	
	protected Object createProfileCSV(String profileKey, Date date, Amount<?> amount) {
		return createProfileCSV(profileKey, date, EnsembleAmountFormat.INSTANCE.formatAmount(amount));
	}
	
	protected String createProfileCSV(String profileKey, Date date, String valueString) {
		return new StringBuffer()
			.append("plan, ")
			.append(profileKey)
			.append(",")
			.append(ISO8601DateFormat.formatISO8601(date))
			.append(",")
			.append(valueString)
			.append("\n")
			.toString();
	}
	
}
