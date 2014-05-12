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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.plan.editor.search.PlanIndexer;
import gov.nasa.ensemble.core.plan.editor.search.PlanSearcher;

import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

/**
 * This test confirms the implementation of the lucene search engine.
 * 
 * @author Alonzo Benavides
 */
public class TestPlanSearch extends TestCase {

	private static final String NAME = "name"; // whereas indexAttributesRec uses PlanSearchPage.NAME_STRING;
	private PlanIndexer indexer;
	private EPlan plan;

	@Override
	public void setUp() {		
		plan = createPlan("Zearch_test");

		indexer = new PlanIndexer();
		
		indexer.addAttribute(NAME);
//		indexer.indexAttributesRec(plan);
		for (EPlanChild activity : plan.getChildren()) {
			// This is the one the Template page calls.
			indexer.indexElementByLabel(activity);
		}
		indexer.refresh();
	}
		
	public void testBasicPlanSearch() {
		assertCount(1, "foo_bar_5");
		assertCount(1, "foo-bar-5");
		assertCount(2, "bar");
		assertCount(1, "baz");
		assertCount(3, "foo");
		assertCount(0, "shouldNotBeFound");
		assertCount(1, "bar-5-foo");
		assertCount(7, "_");
		assertCount(7, "");
	}
	
	public void testSpecial() {
		assertCount(1, "huh?");
		assertCount(0, "foo_?ar_5");	// ? should get quoted and not be Lucene syntax
	}
	
	/** SPF-11124 */
	public void testRegression11124() {
		assertCount(1, "mumble");
		assertCount(1, "mumble/");
		assertCount(1, "mumble/f");
		assertCount(1, "mumble/frotz");
		assertCount(2, "W");
		assertCount(2, "S-W"); // also matches WeSt
		assertCount(1, "S/W");
		assertCount(0, "m/f");
		assertCount(2, "/");
	}
	
	public void testSearchById() {
		EPlanChild child = plan.getChildren().get(0);
		String featureName = PlanPackage.Literals.COMMON_MEMBER__DIFF_ID.getName();
		CommonMember member = child.getMember(CommonMember.class);
		if (member != null) {
			String diffID = member.getDiffID();
			if (!CommonUtils.isNullOrEmpty(diffID)) {
				for (EPlanChild activity : plan.getChildren()) {
					indexer.indexAttributes(activity);
				}
				indexer.refresh();
				
				PlanSearcher searcher = new PlanSearcher(indexer.getFileDirectory());
				searcher.addQueryForWordsContaining(featureName, diffID);
				Vector<Integer> resultz = searcher.zearch();
				assertEquals(1, resultz.size());
				List<String> resultIDs = searcher.getResultIDs();
				EPlanElement identifiable = indexer.idRegistry.getIdentifiable(EPlanElement.class, resultIDs.get(0));
				assertEquals(child, identifiable);
			}
		}
	}
	
	private void assertCount(int expected, String target) {
		PlanSearcher searcher = new PlanSearcher(indexer.getFileDirectory());
		searcher.addQueryForWordsContaining(NAME, target);		
		Vector<Integer> resultz = searcher.zearch();
		assertEquals("Wrong number of '"+target + "' ", expected, resultz.size());
	}
	
	public EPlan createPlan(String planName){
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		plan.setName(planName);
		
		for (int i=4; i<6; i++) {
			plan.getChildren().add(createActivity("foo_bar_" + i));
		}
		
		plan.getChildren().add(createActivity("foo_baz"));
		plan.getChildren().add(createActivity("mumble/frotz"));
		plan.getChildren().add(createActivity("S/W"));
		plan.getChildren().add(createActivity("west"));
		plan.getChildren().add(createActivity("huh?"));
		
		return plan;
	}
	
	private EActivity createActivity(String name) {
		EActivity activity = gov.nasa.ensemble.core.plan.PlanFactory.getInstance().createActivityInstance();
		activity.setName(name);
		return activity;
	}
	
}
