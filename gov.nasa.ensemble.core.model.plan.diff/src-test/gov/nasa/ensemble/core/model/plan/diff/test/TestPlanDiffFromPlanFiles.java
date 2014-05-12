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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.ChronologicalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeBottomUp;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeByName;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeFlat;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeTopDown;
import gov.nasa.ensemble.core.model.plan.diff.trees.SortCombinedPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Comparator;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;


/**
 * File-based test.  Unlike some others, this ignores changes for test purposes,
 * since some of the test cases are so big.
 * @author kanef
 * @see {@link TestPlanDiffSyntheticThorough} and {@link TestPlanDiffSyntheticResourceful}
 */
public class TestPlanDiffFromPlanFiles extends TestCaseWithADAndPlansOutsideOfBuild {
	
//	private ResourceSet sharedResourceSet = new ResourceSetImpl();
	private static ResourceSet toEachItsOwnResourceSet = null; // 2010-08-09:  Arash currently recommends each have its own, but hopes Score will change to be "more like PLATO"

	@Override
	protected URL getADlocation () {
		return findFile("test-dictionary-based-on-MOD.dictionary");
	}
	
	@Test
	public void testLazySundayNoChanges () {
		testTopDown("Lazy-Sunday",
				    "Lazy_Sunday--base",
				    "Lazy_Sunday--no-changes",
				    "");
	}
	
	@Test
	public void testLazySundayNoConstraints () {
		testTopDown("Lazy-Sunday",
				    "Lazy_Sunday--base",
				    "Lazy_Sunday--no-constraints",
				    "");
	}
	
	@Test
	public void testLazySundayDeleteNap () {
		testTopDownAndBottomUp("Lazy-Sunday",
				"Lazy_Sunday--base",
				"Lazy_Sunday--delete-nap",
				"-Nap",
				"[-] (-Nap)");
	}
	
	@Test
	public void testLazySundaySomeoneAlwaysSleeping () {
		testTopDownAndBottomUp("Lazy-Sunday",
				"Lazy_Sunday--base",
				"Lazy_Sunday--FE-6-always-sleeping",
				"ΔSlumber (+FE-6) ΔNap (+FE-6) ΔSnooze (+FE-6) ΔRest (+FE-6) ΔSiesta (+FE-6)"
				+ " ΔDoze (+FE-6) ΔCatnap (+FE-6) ΔShuteye (+FE-6)",
				"[Δ] (ΔFE-6 (+Slumber +Nap +Snooze +Rest +Siesta +Doze +Catnap +Shuteye))");
	}

	@Test
	public void testLazySundaySomeoneNeverSleeps () {
		testTopDownAndBottomUp("Lazy-Sunday",
				"Lazy_Sunday--base",
				"Lazy_Sunday--FE-1-never-sleeps",
				"ΔSlumber (-FE-1) ΔNap (-FE-1) ΔSnooze (-FE-1)",
				"[Δ] (ΔFE-1 (-Slumber -Nap -Snooze))");
	}

	@Test
	public void testLazySundayRename () {
		testTopDown("Lazy-Sunday",
				    "Lazy_Sunday--base",
				    "Lazy_Sunday--rename-Shuteye",
				    "ΔJustRestingMyEyes");
	}

	@Test
	public void testIAmNotLisa () {
		testTopDown("I-am-Not-Lisa",
				"Not-Lisa/schedule",
				"Not-Lisa-either/schedule",
				"ΔSLEEP-FE2 LOG-ENTRY" +
				" (ΔSLEEP-FE2 LOG-ENTRY (+FE-2 -FE-5)) " +
				"ΔEXPRS-RACK-OPERATE (⤷EXPRS2-SYS-PWR/THERM) " + 
				"-EXPRS2-RACK-OPERATE (⤹EXPRS2-SYS-PWR/THERM)"
				);
	}
	
	@Test
	public void testRegressionSpf4884 () {
		// Argument changes to СЭМ-MO-22 were not being detected in rev 78313, fixed in rev 80827.
		testTopDown("STP08_09NOV30--Trimmed-for-SPF-4884-regression-test",
				"STP08_09NOV30_before/schedule",
				"STP08_09NOV30_after/schedule",
				"ΔСЭМ-MO-22 ΔEHS-GC/DMS-START ΔIMMN-BLOOD-OPERATOR (+FE-3 -ISS CDR) -BCAT5-CRYSTAL-PHOTO");
	}

	@Test
	public void testRegressionSpf4889 () {
		testTopDownAndBottomUp("STP08_09NOV30--Trimmed-for-SPF-4889",
				"STP08_09NOV30-before/schedule",
				"STP08_09NOV30-after/schedule",
				"ΔHREP-SCENE-DNLK -СЭМ-MO-22 -BISP-FE4 PILL-INGEST -BISP-FE4 HISD-PHOTO",
				//TODO:  Figure out why delta node has no children
				"[-] (-СЭМ-MO-22 -BISP-FE4 PILL-INGEST -BISP-FE4 HISD-PHOTO) [Δ]");
		testFlatChronological("STP08_09NOV30--Trimmed-for-SPF-4889",
				"STP08_09NOV30-before/schedule",
				"STP08_09NOV30-after/schedule",
				"ΔHREP-SCENE-DNLK -BISP-FE4 PILL-INGEST -СЭМ-MO-22 -BISP-FE4 HISD-PHOTO");				
		testByName("STP08_09NOV30--Trimmed-for-SPF-4889",
				"STP08_09NOV30-before/schedule",
				"STP08_09NOV30-after/schedule",
				"“BISP” (“BISP-FE4 HISD-PHOTO” (-BISP-FE4 HISD-PHOTO)" +
				" “BISP-FE4 PILL-INGEST” (-BISP-FE4 PILL-INGEST))" +
				" “HREP” (“HREP-SCENE-DNLK” (ΔHREP-SCENE-DNLK))" +
				" “СЭМ” (“СЭМ-MO-22” (-СЭМ-MO-22) “СЭМ-MO-22 white”)");				
	}
	

	private void testTopDown(String directory, String planName1, String planName2, String expectedDiffs) {
		testOnePair(directory, planName1, planName2, PlanDiffTreeTopDown.class, expectedDiffs);
	}

	private void testTopDownAndBottomUp(String directory, String planName1, String planName2,
			String expectedDiffsTopDown, String expectedDiffsBottomUp) {
		testOnePair(directory, planName1, planName2, PlanDiffTreeTopDown.class, expectedDiffsTopDown);
		testOnePair(directory, planName1, planName2, PlanDiffTreeBottomUp.class, expectedDiffsBottomUp);
	}
	
	private void testOnePair (String directory, String planName1, String planName2, Class<? extends PlanDiffTree> treeClass,
			String expectedDiffs) {
		EPlan plan1 = getTestCase(directory, planName1);
		EPlan plan2 = getTestCase(directory, planName2);
		AbstractDiffTree tree;
		tree = getDiffTree(plan1, plan2, treeClass);
		WrapperUtils.dispose(plan1);
		WrapperUtils.dispose(plan2);		
		assertEquals("Diffs", expectedDiffs, TersePlanDiffSummaryNotationForTesting.getChangesOnly(tree));
	}

	private AbstractDiffTree getDiffTree (EPlan plan1, EPlan plan2, Class<? extends PlanDiffTree> treeClass) {
		try {
			PlanDiffList differences = PlanDiffEngine.findChanges(plan1, plan2);
			if (differences==null) fail("Failed to find differences");
			Comparator<PlanDiffNode> originalOrder = new SortCombinedPlan(plan1, plan2, differences);
			Constructor<? extends PlanDiffTree> constructor = treeClass.getConstructor(PlanDiffList.class, Comparator.class);
			AbstractDiffTree tree = constructor.newInstance(differences, originalOrder);
			return tree;
		} catch (Exception e) {
			fail(e.toString());
			return null;
		}
	}
	
	private void testFlatChronological(String directory, String planName1, String planName2, String expectedDiffs) {
		testOnePairSorted(directory, planName1, planName2, PlanDiffTreeFlat.class, new ChronologicalOrder(), expectedDiffs);
	}

	private void testByName(String directory, String planName1, String planName2, String expectedDiffs) {
		testOnePairSorted(directory, planName1, planName2, PlanDiffTreeByName.class, new AlphabeticalOrder(), expectedDiffs);
	}

	private void testOnePairSorted (String directory, String planName1, String planName2,
			 Class<? extends PlanDiffTree> treeClass, Comparator<PlanDiffNode> order,
			String expectedDiffs) {
		EPlan plan1 = getTestCase(directory, planName1);
		EPlan plan2 = getTestCase(directory, planName2);
		AbstractDiffTree tree;
		tree = getDiffTreeSorted(plan1, plan2, treeClass, order);
		WrapperUtils.dispose(plan1);
		WrapperUtils.dispose(plan2);		
		assertEquals("Diffs", expectedDiffs, TersePlanDiffSummaryNotationForTesting.getChangesOnly(tree));
	}

	private AbstractDiffTree getDiffTreeSorted (EPlan plan1, EPlan plan2,
			Class<? extends PlanDiffTree> treeClass, Comparator<PlanDiffNode> order) {
		try {
			PlanDiffList differences = PlanDiffEngine.findChanges(plan1, plan2);
			Constructor<? extends PlanDiffTree> constructor = treeClass.getConstructor(PlanDiffList.class, Comparator.class);
			AbstractDiffTree tree = constructor.newInstance(differences, order);
			return tree;
		} catch (Exception e) {
			fail(e.toString());
			return null;
		}
	}
	
	protected EPlan getTestCase (String directory, String baseFilename) {
		return PlanDiffUtils.loadPlanFromFile(findFile(directory + "/" + baseFilename + ".plan"),
				toEachItsOwnResourceSet);
	}

}
