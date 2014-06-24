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

import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeBottomUp;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeByName;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeFlat;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeTopDown;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Comparator;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * By using a shorthand for plan operations (PlanForPlanDiffTest)
 * and a shorthand for diff results, this test collection tersely spells out
 * a bunch of different tests of the Plan Diff match and diff engines
 * and the PlanDiffTreeTopLevel output structure.
 * @see PlanForPlanDiffTest
 * @see TersePlanDiffSummaryNotationForTesting
 */
public class TestPlanDiffSyntheticThorough extends TestCase {
	
	private enum WithID {Only, AndWithout} // Legacy, from when we used EMF Diff's heuristics

	/*
	 * Most of these tests are on small plans, but one is a stress test
	 * and has parameters that can be set here.
	 * 1000 size and and 3 reps makes it take about 2.8 sec
	 * on a 2.93 GHz Intel Mac.  5000 makes it run out of heap space duplicating plan.
	 */
	private static final int SIZE_OF_STRESS_TEST = 1000;
	private static final int REPS_OF_STRESS_TEST = 3;

	protected PlanForPlanDiffTest basePlan;
	private Class<? extends PlanDiffTree> topDown = PlanDiffTreeTopDown.class;
	private Class<? extends PlanDiffTree> bottomUp = PlanDiffTreeBottomUp.class;
	private Class<? extends PlanDiffTree> byName = PlanDiffTreeByName.class;
	private Class<? extends PlanDiffTree> flat = PlanDiffTreeFlat.class;
	private PlanForPlanDiffTest emptyPlan = newPlan();
	private Comparator<PlanDiffNode> inherentOrder = null;
	private Comparator<PlanDiffNode> alphabeticalOrder = new AlphabeticalOrder();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		basePlan = makeBasePlan();
	}
	
	public void testNondestructive () {
		// This is really a test of the test itself.
		PlanForPlanDiffTest alteredPlan = basePlan.add("Z");
		assertEquals("Internal testing error:  One child should have been added to new plan only.",
				basePlan.getPlan().getChildren().size()+1,
				alteredPlan.getPlan().getChildren().size());			
	}

	public void testTrivialChanges () {
		assertDiff(WithID.AndWithout, "Add empty groups", inherentOrder, topDown,
				"+A +B",
				emptyPlan, emptyPlan.add("A").add("B"));
		assertDiff(WithID.AndWithout, "Remove empty groups", inherentOrder, topDown,
				"-A -B",
				emptyPlan.add("A").add("B"), emptyPlan);
		assertDiff(WithID.AndWithout, "Unchanged", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan);
		assertDiff(WithID.AndWithout, "Add empty groups and activities", inherentOrder, topDown,
				"+A +B +c",
				emptyPlan, emptyPlan.add("A").add("B").addOrphanActivity("c"));
	}
	

	public void testEasyChanges () {
		assertDiff(WithID.AndWithout, "From empty", inherentOrder, topDown,
				"+A (+a1 +a2 +a3) +B (+b1 +b2 +b3) +C (+c1 +c2 +c3) +D (+d1 +d2 +d3) +E (+e1 +e2 +e3)",
				emptyPlan, basePlan);
		assertDiff(WithID.AndWithout, "To empty", inherentOrder, topDown,
				"-A (-a1 -a2 -a3) -B (-b1 -b2 -b3) -C (-c1 -c2 -c3) -D (-d1 -d2 -d3) -E (-e1 -e2 -e3)",
				basePlan, emptyPlan);
		assertDiff(WithID.AndWithout, "Add everything", inherentOrder, topDown,
				"+J (+j1) +K (+k1)",
				emptyPlan, emptyPlan.add("J")
									.add("K")
									.addUnder("J", "j1")
									.addUnder("K", "k1"));
		assertDiff(WithID.AndWithout, "Remove everything", inherentOrder, topDown,
				"-A (-a1 -a2 -a3) -B (-b1 -b2 -b3) -C (-c1 -c2 -c3) -D (-d1 -d2 -d3) -E (-e1 -e2 -e3)",
				basePlan,
				basePlan.delete("A").delete("B").delete("C").delete("D").delete("E")
					);
	}
	
	public void testAddsAndRemoves () {
		assertDiff(WithID.AndWithout, "Delete B", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3) -B (-b1 -b2 -b3)",
				basePlan, basePlan.delete("B"));
		assertDiff(WithID.AndWithout, "Delete d1", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) ΔD (=d2 =d3 -d1) =E (=e1 =e2 =e3)",
				basePlan, basePlan.delete("d1"));
		assertDiff(WithID.AndWithout, "Delete D&E", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) -D (-d1 -d2 -d3) -E (-e1 -e2 -e3)",
				basePlan, basePlan.delete("D").delete("E"));
		assertDiff(WithID.AndWithout, "Add b4", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 =b3 +b4) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.addUnder("B", "b4"));
		assertDiff(WithID.Only, "Add b4 and remove b3", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 +b4 -b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.addUnder("B", "b4").delete("b3"));
		assertDiff(WithID.AndWithout, "Add X", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3) +X",
				basePlan, basePlan.add("X"));
	}
	
	public void testParameterChanges () {
		assertDiff(WithID.AndWithout, "Rename b1", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (Δx1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.rename("b1", "x1"));
		assertDiff(WithID.AndWithout, "Rename b3 to b4", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 Δb4) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.rename("b3", "b4"));
		assertDiff(WithID.Only, "Rename E to X", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) ΔX (=e1 =e2 =e3)",
				basePlan, basePlan.rename("E", "X"));
		assertDiff(WithID.Only, "Remove everything and add more", inherentOrder, topDown,
				"+X (+a1 +a2 +a3) +Y (+c1 +c2 +c3) -A (-a1 -a2 -a3) -B (-b1 -b2 -b3) -C (-c1 -c2 -c3) -D (-d1 -d2 -d3) -E (-e1 -e2 -e3)",
				basePlan, basePlan
						.delete("A").delete("B").delete("C").delete("D").delete("E")
						.add("X").add("Y")
						.addUnder("X", "a1").addUnder("X", "a2").addUnder("X", "a3")
						.addUnder("Y", "c1").addUnder("Y", "c2").addUnder("Y", "c3")
				);
		
		assertDiff(WithID.AndWithout, "Rename all groups", inherentOrder, topDown,
				"ΔAA (=a1 =a2 =a3) ΔBB (=b1 =b2 =b3) ΔCC (=c1 =c2 =c3) ΔDD (=d1 =d2 =d3) ΔEE (=e1 =e2 =e3)",
				basePlan, basePlan
				.rename("A","AA")
				.rename("B","BB")
				.rename("C","CC")
				.rename("D","DD")
				.rename("E","EE"));

		assertDiff(WithID.Only, "Rename all activities", inherentOrder, topDown,
				"ΔA (Δa01 Δa02 Δa03) ΔB (Δb01 Δb02 Δb03) ΔC (Δc01 Δc02 Δc03) ΔD (Δd01 Δd02 Δd03) ΔE (Δe01 Δe02 Δe03)",
				basePlan, basePlan
				.rename("a1","a01")
				.rename("a2","a02")
				.rename("a3","a03")
				.rename("b1","b01")
				.rename("b2","b02")
				.rename("b3","b03")
				.rename("c1","c01")
				.rename("c2","c02")
				.rename("c3","c03")
				.rename("d1","d01")
				.rename("d2","d02")
				.rename("d3","d03")
				.rename("e1","e01")
				.rename("e2","e02")
				.rename("e3","e03")
				);
		
		assertDiff(WithID.AndWithout, "Change string in b3", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 Δb3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.changeString("b3"));

		assertDiff(WithID.AndWithout, "Change boolean in b2", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 Δb2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.changeBoolean("b2"));

		assertDiff(WithID.AndWithout, "Delay C", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) ΔC (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.delay("C", 12));
		assertDiff(WithID.AndWithout, "Delay b1", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (Δb1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, basePlan.delay("b1", 12));
		
		assertDiff(WithID.Only, "Rename E to X and delete children", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) ΔX (-e1 -e2 -e3)",
				basePlan, basePlan.rename("E", "X").delete("e1").delete("e2").delete("e3"));
		
		// Test variations of that:
		
		assertDiff(WithID.Only, "Rename E to X", inherentOrder, topDown,
					"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) ΔX (=e1 =e2 =e3)",
				basePlan, basePlan.rename("E", "X"));

		assertDiff(WithID.AndWithout, "Delete children of E", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) ΔE (-e1 -e2 -e3)",
				basePlan, basePlan.delete("e1").delete("e2").delete("e3"));
		
		assertDiff(WithID.Only, "Rename E to X and delete two children", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) ΔX (=e3 -e1 -e2)",
				basePlan, basePlan.rename("E", "X").delete("e1").delete("e2"));
		
		PlanForPlanDiffTest hodgepodge = newPlan();
		for (String groupName : new String[] {"A", "B", "C", "D", "E",
											  "F", "G", "H", "I"}) {
			hodgepodge = hodgepodge.add(groupName);
			for (String activityNumber : new String[] {"1", "2", "3"}) {
				hodgepodge = hodgepodge.addUnder(groupName, groupName.toLowerCase() + activityNumber);
			}			
		}
		
		assertDiff(WithID.Only, "The test that Jack built", inherentOrder, topDown,
		"ΔB (=b1 =b3 -b2) ΔC (=c1 =c2 =c3 +c4) ΔD (=d1 Δd2 =d3)" +
		" ΔE (=e1 Δe002 =e3)" +
		" ΔF (=f1 Δf2 =f3)" + 
		" ΔG (=g1 =g2 =g3 +g4) ΔH (=h1 Δh002 =h3) ΔI (=i1 =i2 =i3 +i4)" +
		" -A (-a1 -a2 -a3)",
		hodgepodge,
		hodgepodge
		.delete("A")
		.delete("b2")
		.addUnder("C", "c4")
		.delay("d2", 1)
		.delay("e2", 1).rename("e2", "e002")
		.changeBoolean("f2")
		.changeBoolean("h2").delay("h2", 24).rename("h2", "h002")
		.addUnder("G", "g4")
		.addUnder("I", "i4")
		);

		
		assertDiff(WithID.AndWithout, "Variant on f2 boolean problem", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)" +
				" ΔF (=f1 Δf2 =f3)" +
				" =G (=g1 =g2 =g3) =H (=h1 =h2 =h3) =I (=i1 =i2 =i3)",
				hodgepodge,
				hodgepodge
				.changeBoolean("f2")
		);
		
		PlanForPlanDiffTest commonBooleanTest = basePlan.delete("C").delete("D").delete("E");
		
		assertDiff(WithID.AndWithout, "Minimal passing test case for SPF-4120", inherentOrder, topDown,
				"=A (=a1 =a2 =a3)" +
				" ΔB (=b1 Δb2 =b3)",
				commonBooleanTest,
				commonBooleanTest
				.changeBoolean("b2")
		);
		
		assertDiff(WithID.AndWithout, "Minimal failing test case for SPF-4120", inherentOrder, topDown,
				"ΔB (=b1 Δb2 =b3)" +
				" -A (-a1 -a2 -a3)",
				commonBooleanTest,
				commonBooleanTest
				.delete("A")
				.changeBoolean("b2")
		);
		
		PlanForPlanDiffTest heterogeneousTest = basePlan
		.addGroupUnderGroup("A", "AA")
		.addGroupUnderGroup("B", "BB")
		.addOrphanActivity("f1")
		.addOrphanActivity("f2")
		.addOrphanActivity("f3");

		assertDiff(WithID.AndWithout, "heterogeneous test", inherentOrder, topDown,
				"ΔA (=a1 =a2 =a3 ΔAA) ΔB (Δb1 =b2 =b3 =BB)" +
				" ΔD (=d1 =d2 =d3 +DD)" +
				" =E (=e1 =e2 =e3)" +
				" =f1 =f3" +
				" -C (-c1 -c2 -c3) -f2",
				heterogeneousTest,
				heterogeneousTest
				.changeBoolean("AA")
				.delete("C")
				.delay("b1", 1)
				.addGroupUnderGroup("D", "DD")
				.delete("f2"));

	}
	
	public void testMove () {
		// Move detection is a SPF-4220 feature.
		
		PlanForPlanDiffTest houseBeforeMove = newPlan()
		.add("LivingRoom")
		.add("Kitchen")
		.add("DiningRoom")
		.addUnder("LivingRoom", "sofa")
		.addUnder("LivingRoom", "stereo")
		.addUnder("Kitchen", "dishwasher")
		.addUnder("Kitchen", "refrigerator")
		.addUnder("Kitchen", "oven")
		.addUnder("DiningRoom", "table")
		.addUnder("DiningRoom", "television")
		.addUnder("DiningRoom", "chairs");
		
		PlanForPlanDiffTest houseAfterMove = houseBeforeMove.move("television", "LivingRoom");
		assertDiff(WithID.AndWithout, "Move furniture", inherentOrder, topDown,
				"ΔLivingRoom (=sofa =stereo ⤷television) " +
				"=Kitchen (=dishwasher =refrigerator =oven) " +
				"ΔDiningRoom (=table =chairs ⤹television)",
				houseBeforeMove, houseAfterMove);
		assertDiff(WithID.AndWithout, "Move furniture", inherentOrder, bottomUp,
				"[⤹] (⤹television) " +
				"[⤷] (⤷television) " +
				"[=] (=sofa =stereo =Kitchen (=dishwasher =refrigerator =oven) =DiningRoom (=table =chairs))",
				houseBeforeMove, houseAfterMove);		
	}
	
	public void testDeleteAndMoveChild () {
		assertDiff(WithID.AndWithout, "Delete C and let B adopt c2", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 =b3 ⤷c2) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3) -C (-c1 ⤹c2 -c3)",
				basePlan, basePlan.move("c2", "B").delete("C"));
	}
	
	public void testChangesToChildlessGroup () {
		PlanForPlanDiffTest base = newPlan()
				.add("X").add("Y").add("Z");
		assertDiff(WithID.Only, "Delete childless X and Y", inherentOrder, topDown,
				"=Z -X -Y",
				base, base.delete("X").delete("Y"));
		assertDiff(WithID.Only, "Delete childless X and Y invisible to flat tree", inherentOrder, flat,
				"",
				base, base.delete("X").delete("Y"));
		assertDiff(WithID.Only, "Rename childless X to XX", inherentOrder, topDown,
				"ΔXX =Y =Z",
				base, base.rename("X", "XX"));
	}

	
	public void testPlanDiffTreeTypes () {
		PlanForPlanDiffTest afterAddAndDelete = basePlan.addUnder("B", "b4").delete("b3");
		PlanForPlanDiffTest afterDelete = basePlan.delete("B");
		PlanForPlanDiffTest afterRename = basePlan.rename("c3", "c3a");
		assertDiff(WithID.Only, "Top down tree", inherentOrder , topDown,
				"=A (=a1 =a2 =a3) ΔB (=b1 =b2 +b4 -b3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3)",
				basePlan, afterAddAndDelete);
		assertDiff(WithID.Only, "Top down tree 2", inherentOrder, topDown,
				"=A (=a1 =a2 =a3) =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3) -B (-b1 -b2 -b3)",
				basePlan, afterDelete);
		assertDiff(WithID.Only, "Flat tree 1", inherentOrder, flat,
				"=a1 =a2 =a3 =b1 =b2 +b4 =c1 =c2 =c3 =d1 =d2 =d3 =e1 =e2 =e3 -b3",
				basePlan, afterAddAndDelete);
		assertDiff(WithID.Only, "Flat tree -- SPF-4681 regression test", inherentOrder, flat,
				// This replicates SPF-4681,
				// "Flat Chronological Plan Diff View Does Not Report Deleted Activity".
				// Note that we don't test the sorting (chronological) part.
				"=a1 =a2 =a3 =c1 =c2 =c3 =d1 =d2 =d3 =e1 =e2 =e3 -b1 -b2 -b3",
				basePlan, afterDelete);
		assertDiff(WithID.Only, "Flat tree -- reverse of SPF-4681", inherentOrder, flat,
				"=a1 =a2 =a3 +b1 +b2 +b3 =c1 =c2 =c3 =d1 =d2 =d3 =e1 =e2 =e3",
				afterDelete, basePlan);
		assertDiff(WithID.Only, "Bottom Up tree 1", inherentOrder, bottomUp,
				"[+] (+b4) " +
				"[-] (-b3) " +
				// Note:  No "[Δ] (ΔB)", per SPF-4772 design clarification.
				"[=] (=A (=a1 =a2 =a3) =b1 =b2 =C (=c1 =c2 =c3) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3))",
				basePlan, afterAddAndDelete);
		assertDiff(WithID.Only, "Bottom Up tree 2", inherentOrder, bottomUp,
				"[Δ] (=name (Δc3a)) " +
				"[=] (=A (=a1 =a2 =a3) =B (=b1 =b2 =b3) =C (=c1 =c2) =D (=d1 =d2 =d3) =E (=e1 =e2 =e3))",
				basePlan, afterRename);
		
		PlanForPlanDiffTest namePlan = newPlan()
		.add("Famous").add("Fictional")
		.addUnder("Fictional", "Fred-Flintstone")
		.addUnder("Fictional", "Barney_Rubble")
		.addUnder("Famous", "Fred Astaire")
		.addUnder("Famous", "Frank Sinatra")
		.addUnder("Fictional", "Frank Poole")
		.addUnder("Fictional", "Barney the Dinosaur");
		
		assertDiff(WithID.Only, "By Name tree", alphabeticalOrder, byName,
				"“Barney” (“Barney the Dinosaur” (=Barney the Dinosaur) “Barney_Rubble” (=Barney_Rubble))" +
				" “Famous” (=Famous)" +
				// Note:  No " “Fictional” (ΔFictional)", per SPF-4772 design clarification.
				" “Frank” (“Frank Poole” (-Frank Poole) “Frank Sinatra” (=Frank Sinatra))" +
				" “Fred” (“Fred Astaire” (=Fred Astaire) “Fred-Flintstone” (=Fred-Flintstone))",
				namePlan, namePlan.delete("Frank Poole"));
	}
	
	
	public void testStressTestPlanDiff () {
		for (int rep = 1; rep <= REPS_OF_STRESS_TEST; rep++) {
			ResourceSet resourceSet  = EMFUtils.createResourceSet();
			PlanForPlanDiffTest bigPlan = newPlan(resourceSet);
			bigPlan.setSuppressUnchanged(true);
			for (int i = 1; i <= SIZE_OF_STRESS_TEST; i++) {
				bigPlan.destructivelyAdd("G" + i);
			}
			assertDiff(WithID.Only, "Stress Test", inherentOrder, topDown,
					"+X -G42",
					bigPlan, bigPlan.delete("G42").add("X"));
		}
	}


	private PlanForPlanDiffTest makeBasePlan() {
		PlanForPlanDiffTest result = newPlan();
		result = result.add("A").add("B").add("C").add("D").add("E");
		result = result.addUnder("A", "a1").addUnder("A", "a2").addUnder("A", "a3");
		result = result.addUnder("B", "b1").addUnder("B", "b2").addUnder("B", "b3");
		result = result.addUnder("C", "c1").addUnder("C", "c2").addUnder("C", "c3");
		result = result.addUnder("D", "d1").addUnder("D", "d2").addUnder("D", "d3");
		result = result.addUnder("E", "e1").addUnder("E", "e2").addUnder("E", "e3");
		return result;
	}
	
	/**
	 * 
	 * @param andMaybeWithout -- No longer supported; new implementation always relies on id.
	 * @param testName -- just used to help identify test failures
	 * @param expectedInNotation -- expected result, written in TersePlanDiffSummaryNotationForTesting™
	 * @param basePlan -- aka the "left" or "before" plan
	 * @param modifiedPlan -- aka the "right" or "after" plan
	 * @param desiredOrder 
	 */
	private void assertDiff(WithID andMaybeWithout, String testName,
			Comparator<PlanDiffNode> desiredOrder,
			Class<? extends PlanDiffTree> treeType,
			String expectedInNotation,
			PlanForPlanDiffTest basePlan,
			PlanForPlanDiffTest modifiedPlan) {
		assertEquals(testName, expectedInNotation, basePlan.diff(modifiedPlan, treeType, desiredOrder));
		sanityCheck(basePlan);
		sanityCheck(modifiedPlan);
// No longer supported
//		if (andMaybeWithout == WithID.AndWithout)
//			assertEquals(testName + " ignoring ID", expectedInNotation, basePlan.diffIgnoringID(modifiedPlan, treeType));
	}
	
	protected void sanityCheck(PlanForPlanDiffTest basePlan2) {
		// Subclass may do a check here.
	}

	protected PlanForPlanDiffTest newPlan() {
		return new PlanForPlanDiffTest();
	}

	protected PlanForPlanDiffTest newPlan(ResourceSet resourcerSet) {
		return new PlanForPlanDiffTest(resourcerSet);
	}

}
