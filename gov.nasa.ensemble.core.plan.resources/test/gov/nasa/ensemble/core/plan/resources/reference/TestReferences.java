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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADParameterMemberFactory;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.resources.AbstractResourceTest;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class TestReferences extends AbstractResourceTest {

	private static final Date CREW_ACTIVITY_START = DateUtils.add(PLAN_START, 60*60*1000);
	private static final Date SLEEP_ACTIVITY_START = DateUtils.add(CREW_ACTIVITY_START, 2*60*60*1000);
	
	private EPlan plan;
	private EActivity crewActivity = null;
	private EActivity sleepActivity = null;

	private EObject plt = null;
	private EObject cdr = null;

	private EEnum LOCATION;
	private EEnumLiteral LOCATION__HERE;
	private EEnumLiteral LOCATION__THERE;
	
	private EStructuralFeature CREW_ACTIVITY__CREW_MEMBERS;
	private EStructuralFeature CREW_ACTIVITY__LOCATION;
	private EStructuralFeature SLEEP_ACTIVITY__CREW_MEMBERS;
	private EStructuralFeature SLEEP_ACTIVITY__LOCATION;

	private EStructuralFeature CREW_MEMBER__ID = null;
	private EStructuralFeature CREW_MEMBER__NAME = null;
	private EStructuralFeature CREW_MEMBER__NUMERIC = null;
	private EStructuralFeature CREW_MEMBER__BOOLEAN_STATE = null;
	private EStructuralFeature CREW_MEMBER__ENUM_STATE = null;
	
	@Override
	public void setUp() {
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestReferences.dictionary", true));
		EFactory factory = AD.getEFactoryInstance();
		//
		// Gather enum
		LOCATION = getEEnum("LOCATION");
		LOCATION__HERE = LOCATION.getEEnumLiteral("HERE");
		assertNotNull(LOCATION__HERE);
		LOCATION__THERE = LOCATION.getEEnumLiteral("THERE");
		assertNotNull(LOCATION__THERE);
		//
		// Gather the crew member elements to test
		EClass crewMemberClass = getEClass("CrewMember");
		CREW_MEMBER__ID = getEStructuralFeature(crewMemberClass, "id");
		CREW_MEMBER__NAME = getEStructuralFeature(crewMemberClass, "name");
		CREW_MEMBER__NUMERIC = getEStructuralFeature(crewMemberClass, "numeric");
		CREW_MEMBER__BOOLEAN_STATE = getEStructuralFeature(crewMemberClass, "booleanState");
		CREW_MEMBER__ENUM_STATE = getEStructuralFeature(crewMemberClass, "enumState");
		//
		// Gather the activity elements to test
		EActivityDef CREW_ACTIVITY = getActivityDef("Crew_Activity");
		CREW_ACTIVITY__CREW_MEMBERS = getEStructuralFeature(CREW_ACTIVITY, "crewMembers");
		assertTrue(CREW_ACTIVITY__CREW_MEMBERS instanceof EReference);
		assertTrue(CREW_ACTIVITY__CREW_MEMBERS.isMany());
		CREW_ACTIVITY__LOCATION = getEStructuralFeature(CREW_ACTIVITY, "location");

		EActivityDef SLEEP_ACTIVITY = getActivityDef("Sleep_Activity");
		SLEEP_ACTIVITY__CREW_MEMBERS = getEStructuralFeature(SLEEP_ACTIVITY, "crewMembers");
		assertTrue(SLEEP_ACTIVITY__CREW_MEMBERS instanceof EReference);
		assertTrue(SLEEP_ACTIVITY__CREW_MEMBERS.isMany());
		SLEEP_ACTIVITY__LOCATION = getEStructuralFeature(SLEEP_ACTIVITY, "location");
		//
		// Set up domain
		TransactionalEditingDomain domain = TransactionUtils.createTransactionEditingDomain(false);
		ResourceSet resourceSet = domain.getResourceSet();
		//
		// Create CDR
		cdr = factory.create(crewMemberClass);
		cdr.eSet(CREW_MEMBER__ID, "ID_CDR");
		cdr.eSet(CREW_MEMBER__NAME, "CDR");
		//
		// Create PLT
		plt = factory.create(crewMemberClass);
		plt.eSet(CREW_MEMBER__ID, "ID_PLT");
		plt.eSet(CREW_MEMBER__NAME, "PLT");
		//
		// Create the set of related resources and add one for the crew members
		Resource crewMemberResource = resourceSet.createResource(URI.createURI("http://junit/crewMembers.resource"));
		crewMemberResource.getContents().add(cdr);
		crewMemberResource.getContents().add(plt);
		//
		// Create plan
		URI uri = URI.createURI("http://junit/in.plan");
		crewActivity = PlanFactory.getInstance().createActivity(CREW_ACTIVITY);
		sleepActivity = PlanFactory.getInstance().createActivity(SLEEP_ACTIVITY);
		plan = createPlanInstance(domain, uri);
		EPlanUtils.contributeProductResources(plan);
		ExtensionPointResourceSetListener.addListener(domain);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);

				plan.getChildren().add(crewActivity);
				crewActivity.getData().eSet(CREW_ACTIVITY__LOCATION, LOCATION__HERE);
				crewActivity.getMember(TemporalMember.class).setStartTime(CREW_ACTIVITY_START);
				crewActivity.getMember(TemporalMember.class).setDuration(Amount.valueOf(1, NonSI.HOUR).to(SI.SECOND));
				
				plan.getChildren().add(sleepActivity);
				sleepActivity.getData().eSet(SLEEP_ACTIVITY__LOCATION, LOCATION__THERE);
				sleepActivity.getMember(TemporalMember.class).setStartTime(SLEEP_ACTIVITY_START);
				sleepActivity.getMember(TemporalMember.class).setDuration(Amount.valueOf(1, NonSI.HOUR).to(SI.SECOND));
			}
		});
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			WrapperUtils.dispose(plan);
		} catch (Exception e) {
			LogUtil.error("diposing plan", e);
		}
	}
	
	public void testAddAndRemove() {
		assertAddAndRemove(cdr, CREW_ACTIVITY__CREW_MEMBERS);
		assertAddAndRemove(plt, CREW_ACTIVITY__CREW_MEMBERS);
	}
	
	public void testNumericEffect() {
		addCrewMember(crewActivity, cdr, CREW_ACTIVITY__CREW_MEMBERS);
		addCrewMember(sleepActivity, cdr, SLEEP_ACTIVITY__CREW_MEMBERS);
		recomputePlan(plan);
		
		assertStructuralFeatureProfileAmountValue(cdr, CREW_MEMBER__NUMERIC, PLAN_START, null);
		assertStructuralFeatureProfileAmountValue(cdr, CREW_MEMBER__NUMERIC, CREW_ACTIVITY_START, Amount.valueOf(1, NonSI.HOUR));
		assertStructuralFeatureProfileAmountValue(cdr, CREW_MEMBER__NUMERIC, SLEEP_ACTIVITY_START, Amount.valueOf(0, NonSI.HOUR));
	}
	
	public void testBooleanEffect() {
		addCrewMember(crewActivity, cdr, CREW_ACTIVITY__CREW_MEMBERS);
		recomputePlan(plan);
		
		assertStructuralFeatureProfileValue(cdr, CREW_MEMBER__BOOLEAN_STATE, PLAN_START, null);
		assertStructuralFeatureProfileValue(cdr, CREW_MEMBER__BOOLEAN_STATE, CREW_ACTIVITY_START, Boolean.TRUE);
	}
	
	public void testStateExpressionEffect() {
		addCrewMember(crewActivity, cdr, CREW_ACTIVITY__CREW_MEMBERS);
		addCrewMember(sleepActivity, cdr, SLEEP_ACTIVITY__CREW_MEMBERS);
		recomputePlan(plan);

		assertStructuralFeatureProfileValue(cdr, CREW_MEMBER__ENUM_STATE, PLAN_START, null);
		assertStructuralFeatureProfileValue(cdr, CREW_MEMBER__ENUM_STATE, CREW_ACTIVITY_START, LOCATION__HERE);
		assertStructuralFeatureProfileValue(cdr, CREW_MEMBER__ENUM_STATE, SLEEP_ACTIVITY_START, LOCATION__THERE);
	}
	
	public void testSavable () {
		try {
			Map<String, Boolean> options = new HashMap<String, Boolean>();
			options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, false);
			ResourceSet resourceSet = EMFUtils.createResourceSet();
			Resource resource = new PlanResourceImpl(URI.createURI("http://junit/in.plan"));
			resourceSet.getResources().add(resource);
			resource.getContents().add(plan);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			resource.save(out, options);
			// System.out.println(new String (out.toByteArray()));
			out.close();
		} catch (Exception exception) {
			fail("Plan is not savable:  " + exception);
		}
	}
	
	private void assertStructuralFeatureProfileAmountValue(EObject object, EStructuralFeature feature, Date time, Amount<Duration> expectedValue) {
		Profile profile = getStructuralFeatureProfile(object, feature);
		assertNotNull(profile);
		Object value = profile.getValue(time);
		if (expectedValue == null) {
			assertNull(value);
		} else {
			assertTrue(value instanceof Amount);
			assertAmountProximity(expectedValue, (Amount) value);
		}
	}
	
	private void assertStructuralFeatureProfileValue(EObject object, EStructuralFeature feature, Date time, Object expectedValue) {
		Profile profile = getStructuralFeatureProfile(object, feature);
		assertNotNull(profile);
		ProfileUtil.debugProfile(profile);
		Object value = profile.getValue(time);
		if (expectedValue == null) {
			assertNull(value);
		} else {
			assertEquals(expectedValue, value);
		}
	}
	
	private Profile getStructuralFeatureProfile(EObject object, EStructuralFeature feature) {
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		for (Profile profile : member.getResourceProfiles()) {
			if (profile instanceof StructuralFeatureProfile
					&& ((StructuralFeatureProfile)profile).getObject() == object
					&& ((StructuralFeatureProfile)profile).getFeature() == feature) {
				return profile;
			}
		}
		return null;
	}
	
	private void assertAddAndRemove(EObject crewMember, EStructuralFeature feature) {
		EActivity a = crewActivity;
		EObject data = a.getData();
		//
		// Test for the non-existence of the CrewMember
		assertEquals(0, ((Collection)data.eGet(feature)).size());
		//
		// Add CrewMember
		addCrewMember(a, crewMember, feature);
		//
		// Test for the existence of the CrewMember
		assertEquals(1, ((Collection)data.eGet(feature)).size());
		//
		// Remove CrewMember
		removeCrewMember(crewMember, a, feature);
		//
		// Test for the non-existence of the CrewMember
		assertEquals(0, ((Collection)data.eGet(feature)).size());
	}

	private void removeCrewMember(final EObject crewMember, final EActivity a, final EStructuralFeature feature) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				((Collection)a.getData().eGet(feature)).remove(crewMember);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void addCrewMember(final EActivity a, final EObject crewMember, final EStructuralFeature feature) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				((Collection)a.getData().eGet(feature)).add(crewMember);
			}
		});
	}
	
	private EEnum getEEnum(String id) {
		EClassifier classifier = AD.getEClassifier(id);
		assertNotNull(classifier);
		assertTrue(classifier instanceof EEnum);
		return (EEnum) classifier;
	}
	
	private EClass getEClass(String className) {
		EClassifier classifier = AD.getEClassifier(className);
		assertNotNull(classifier);
		assertTrue(classifier instanceof EClass);
		return (EClass) classifier;
	}

	private EStructuralFeature getEStructuralFeature(EClass crewMemberClass, String featureName) {
		EStructuralFeature f = crewMemberClass.getEStructuralFeature(featureName);
		assertNotNull(f);
		return f;
	}
	
	public EPlan createPlanInstance(EditingDomain domain, final URI uri) {
		final EPlan plan = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		plan.setName("TEST_PLAN");
		plan.setData(ADParameterMemberFactory.FACTORY.createData(PlanPackage.Literals.EPLAN));
		final ResourceSet resourceSet = domain.getResourceSet();
		TransactionUtils.writing(resourceSet, new Runnable() {
			@Override
			public void run() {
				Resource resource = resourceSet.createResource(uri);
				resource.getContents().add(plan);
				EPlanUtils.contributeProductResources(plan);
			}
		});
		return plan;
	}
	
}
