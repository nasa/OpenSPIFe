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
package gov.nasa.ensemble.core.model.plan.diff.test;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADParameterMemberFactory;
import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * ResourcefulPlanForPlanDiffTest serves the same function for resource-reference
 * plan diff testing as PlanForPlanDiffTest does for regular plan diff testing,
 * but depends on an Activity Dictionary and creates activity instance of a
 * particular ActivityDef defined therein, so that it can add resource references.
 * Unfortunately, I was unable to make it a subclass and make it provide all the same
 * methods, because duplicatePlan (which works by serializing the plan into XML)
 * gets a NullPointerException on the ePackage of the ActivityDef of the activity.
 * After spending hours single-stepping through EMF code and comparing it to
 * tests that are known to work, I finally decided to implement the new test
 * through an approach that doesn't involve duplicating a plan.
 * Instead, it relies on the activity names being unique and serving as the diffId.
 * This allows two plans to be constructed from scratch by the JUnit test
 * and still match each other.
 * 
 * This type of plan, for simplicity, has activities at top level, and no groups.
 * @see ResourcefulPlanForPlanDiffTest(String[]) constructor for notation.
 */

public class ResourcefulPlanForPlanDiffTest {

	/** Caller TestCase should load this AD appropriately. */
	private final ActivityDictionary AD = ActivityDictionary.getInstance();
	private EActivityDef testActivityType;
	
	protected static final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	protected static final Date PLAN_START = new Date(System.currentTimeMillis());
	private static final String PLAN_COPIES_ALL_HAVE_THIS_DIFF_ID = "planCopiesAllHaveThisDiffID";
	protected static final ResourceSet sharedResourceSet = EMFUtils.createResourceSet();
	
	char[] activityNames = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	char[] referenceNames = "123456789".toCharArray();
	
	private EPlan plan;

	private Map<String, EObject> referenceNameToObject = new HashMap<String, EObject>();

	private EStructuralFeature digitalResourceFeature;
	
	/**
	 * @param activityAndResourceContents
	 * Activities are named with single letters (A, B, C...), references with
	 * single digits (1, 2, 3...), and the plan is created with the following
	 * compact notation:
	 * 	  new ResourcefulPlanForPlanDiffTest(new String[] {"A1", "B12", "C2", "D"})
	 *     meaning four activities and three references, with reference 1 used by A and B
	 *     and reference 2 used by A and C.
	*/
	public ResourcefulPlanForPlanDiffTest(String[] activityAndResourceContents) {
		EPlan newPlan = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		newPlan.getMember(TemporalMember.class).setStartTime(PLAN_START);
		newPlan.getMember(CommonMember.class).setDiffID(PLAN_COPIES_ALL_HAVE_THIS_DIFF_ID);
		Resource planResource = new PlanResourceImpl(URI.createURI("http://junit/test.plan"));
		sharedResourceSet.getResources().add(planResource);
		planResource.getContents().add(newPlan);
		this.plan = newPlan;
		init(activityAndResourceContents);
	}

	/** Returns differences in a terse notation.
	 * In case of exception (since a lot can theoretically go wrong),
	 * returns the exception string and does not thrown an exception.
	 * @see TersePlanDiffSummaryNotationForTesting
	 */
	public String diff (ResourcefulPlanForPlanDiffTest otherPlan,  Class<? extends PlanDiffTree> treeType) {
		registerPlan(plan);
		registerPlan(otherPlan.getPlan());
		
		PlanForPlanDiffTest plan1 = new PlanForPlanDiffTest(plan, sharedResourceSet);
		PlanForPlanDiffTest plan2 = new PlanForPlanDiffTest(otherPlan.getPlan(), sharedResourceSet);
		return plan1.diff(plan2, treeType, new AlphabeticalOrder());
	}
	
	private EPlan getPlan() {
		return plan;
	}

	private void init(String[] activityAndResourceContents) {
		// Gather the activity elements to test
		testActivityType = AD.getActivityDefs().get(0);
		digitalResourceFeature = testActivityType.getEStructuralFeatures().get(0);

		initializePlanReferences(plan, createResourceSet());
		
		for (String activityNameAndResources : activityAndResourceContents) {
			String activityName = activityNameAndResources.substring(0, 1);
			EActivity activity = PLAN_FACTORY.createActivity(testActivityType);
			activity.setName(activityName);
			activity.getMember(CommonMember.class).setDiffID(activityName);
			activity.getMember(TemporalMember.class).setStartTime(PLAN_START);
			plan.getChildren().add(activity);

			for (int index = 1; index < activityNameAndResources.length(); index++) {
				String referenceName = activityNameAndResources.substring(index, index+1);
				EObject reference = referenceNameToObject.get(referenceName);
				addResource(activity, reference);
				
			}
		}
		
	}

	private ResourceSet createResourceSet() {
		
		EFactory factory = AD.getEFactoryInstance();
		ResourceSet resourceSet = sharedResourceSet;
		Resource resourceAsInAFile = resourceSet.createResource(URI.createURI("http://junit/foo.resource"));
		
		ObjectDef referenceDefinition = AD.getDefinitions(ObjectDef.class).get(0);

		EStructuralFeature idFeature = getEStructuralFeature(referenceDefinition, "id");
		EStructuralFeature nameFeature = getEStructuralFeature(referenceDefinition, "name");

		{ int id = 0;
		  for (char digitChar : referenceNames) {
			  String name = String.valueOf(digitChar);
			  EObject referenceInstance = factory.create(referenceDefinition);
			  referenceInstance.eSet(idFeature, "Resource id#" + id++);
			  referenceInstance.eSet(nameFeature, name);
			  resourceAsInAFile.getContents().add(referenceInstance);
			  referenceNameToObject.put(name, referenceInstance);
		  }
		}
		return resourceSet;
	}

	@SuppressWarnings("unchecked")
	private void addResource(final EPlanElement activity, final EObject resource) {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EObject data = activity.getData();
				((Collection)data.eGet(digitalResourceFeature)).add(resource);
			}
		});
	}
	
	private EStructuralFeature getEStructuralFeature(EClass eClass, String featureName) {
		EStructuralFeature f = eClass.getEStructuralFeature(featureName);
		if (f==null) {
			throw new NullPointerException("Can't find feature named " + featureName);
		}
		return f;
	}
	
	private EPlan initializePlanReferences(final EPlan plan, final ResourceSet resourceSet) {
		plan.setName("TEST_PLAN");
		plan.setData(ADParameterMemberFactory.FACTORY.createData(PlanPackage.Literals.EPLAN));
		TransactionUtils.writing(resourceSet, new Runnable() {
			@Override
			public void run() {
				Resource resource = resourceSet.createResource(URI.createURI("http://junit/in.plan"));
				resource.getContents().add(plan);
			}
		});
		return plan;
	}
	
	private void registerPlan(EPlan plan) {
			new PlanResourceImpl(URI.createURI("http://junit/FOO.plan")).getContents().add(plan);
	}
}
