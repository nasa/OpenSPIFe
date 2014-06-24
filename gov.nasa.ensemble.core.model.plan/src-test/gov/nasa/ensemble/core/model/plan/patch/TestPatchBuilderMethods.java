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
package gov.nasa.ensemble.core.model.plan.patch;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.model.patch.Patch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author ideliz
 */
public class TestPatchBuilderMethods extends TestPatchBuilderBaseSetup implements ITestPatchBuilderAllMethods {

	@Test
	public void add__target_feature_newObject() {
		EPlan plan = makePlanWithActivityNames("A", "B", "C");
		PlanPatchBuilder builder = new PlanPatchBuilder();
		EActivity activity = makeActivityNamed("1");
		builder.add(plan, CHILDREN, activity);
		Patch patch = builder.getPatch();
		assertPatchOperation(plan, patch,
				Arrays.asList("A", "B", "C"),
				Arrays.asList("A", "B", "C", "1"));
	}

	@Test
	public void add__target_feature_newObject_index() {
		EActivity activity = makeActivityNamed("1");
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, activity, 0);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C"),
					Arrays.asList("1", "A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, activity, 3);
			assertPatchOperation(plan, builder.getPatch(),
					Arrays.asList("A", "B", "C"),
					Arrays.asList("A", "B", "C", "1"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, activity, 1);
			assertPatchOperation(plan, builder.getPatch(),
					Arrays.asList("A", "B", "C"),
					Arrays.asList("A", "1", "B", "C"));
		}
	}

	@Test 
	@Override
	public void addAll__target_feature_newObjects() {
		EPlan plan = makePlanWithActivityNames("A", "B", "C");
		List<EActivity> newActivities = Arrays.asList(makeActivityNamed("1"), 
				makeActivityNamed("2"), makeActivityNamed("3"));
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.addAll(plan, CHILDREN, newActivities);
		Patch patch = builder.getPatch();
		assertPatchOperation(plan, patch,
				Arrays.asList("A", "B", "C"),
				Arrays.asList("A", "B", "C", "1", "2", "3"));
	}

	@Test 
	public void addAll__target_feature_newObjects_index() {
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			List<EActivity> newActivities = Arrays.asList(makeActivityNamed("1"), 
					makeActivityNamed("2"), makeActivityNamed("3"));
			PlanPatchBuilder builder = new PlanPatchBuilder();
			int index = plan.getChildren().size();
			builder.addAll(plan, CHILDREN, newActivities, index);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C"),
					Arrays.asList("A", "B", "C", "1", "2", "3"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			List<EActivity> newActivities = Arrays.asList(makeActivityNamed("1"), 
					makeActivityNamed("2"), makeActivityNamed("3"));
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.addAll(plan, CHILDREN, newActivities, 0);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C"),
					Arrays.asList( "1", "2", "3", "A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			List<EActivity> newActivities = Arrays.asList(makeActivityNamed("1"), 
					makeActivityNamed("2"), makeActivityNamed("3"));
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.addAll(plan, CHILDREN, newActivities, 1);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C"),
					Arrays.asList("A", "1", "2", "3",  "B", "C"));
		}
	}
	
	@Test
	public void remove__target_feature_object() {
		/* test */ {
			EPlan plan = makePlanWithActivityNames("1", "A", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("1", "A", "B", "C"),
					Arrays.asList("A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "1", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "1", "B", "C"),
					Arrays.asList("A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C", "1");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C", "1"),
					Arrays.asList("A", "B", "C"));
		}
		
	}
	
	@Override
	public void remove__target_feature_object_index() {
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			int index = 2;
			builder.remove(plan, CHILDREN, plan.getChildren().get(index), index);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C"),
					Arrays.asList("A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("1", "A", "B", "C");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			int index = 0;
			builder.remove(plan, CHILDREN, plan.getChildren().get(index), index);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("1", "A", "B", "C"),
					Arrays.asList("A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C", "1");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			int index = 3;
			builder.remove(plan, CHILDREN, plan.getChildren().get(index), index);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "C", "1"),
					Arrays.asList("A", "B", "C"));
		}
	}

	@Test
	public void removeAll__target_feature_objects() {
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			List<EPlanChild> toRemove = new ArrayList<EPlanChild>();
			toRemove.add(getActivityNamed(plan, "1"));
			toRemove.add(getActivityNamed(plan, "2"));
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					Arrays.asList("A", "B", "C"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			List<EPlanChild> toRemove = new ArrayList<EPlanChild>();
			toRemove.addAll(plan.getChildren());
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					new ArrayList<String>());
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			List<EPlanChild> toRemove = new ArrayList<EPlanChild>();
			toRemove.add(getActivityNamed(plan, "C"));
			toRemove.add(getActivityNamed(plan, "A"));
			toRemove.add(getActivityNamed(plan, "B"));
			PlanPatchBuilder builder = new PlanPatchBuilder();
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					Arrays.asList("1", "2"));
		}
	}
	
	
	@Test
	public void removeAll__target_feature_indexes() {
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			List<EPlanElement> toRemove = new ArrayList<EPlanElement>();
			toRemove.add(plan.getChildren().get(2));
			toRemove.add(plan.getChildren().get(4));
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					Arrays.asList("A", "B", "C"));
		}

		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			List<EPlanElement> toRemove = new ArrayList<EPlanElement>();
			toRemove.add(plan.getChildren().get(3));
			toRemove.add(plan.getChildren().get(1));
			toRemove.add(plan.getChildren().get(0));
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					Arrays.asList("1", "2"));
		}
		
		/* test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
			PlanPatchBuilder builder = new PlanPatchBuilder();
			List<EPlanElement> toRemove = new ArrayList<EPlanElement>();
			toRemove.add(plan.getChildren().get(0));
			toRemove.add(plan.getChildren().get(1));
			toRemove.add(plan.getChildren().get(3));
			builder.removeAll(plan, CHILDREN, toRemove);
			Patch patch = builder.getPatch();
			assertPatchOperation(plan, patch,
					Arrays.asList("A", "B", "1", "C", "2"),
					Arrays.asList("1", "2"));
		}
	}
	
	@Test
	@Override
	public void modify__target_feature_newValue() {
		EPlan plan = makePlanWithActivityNames("A", "B", "1", "C", "2");
		List<EPlanElement> activities = new ArrayList<EPlanElement>();
		activities.add(plan.getChildren().get(2));
		activities.add(plan.getChildren().get(4));
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.modify(plan, CHILDREN, activities);
		Patch patch = builder.getPatch();
		assertPatchOperation(plan, patch,
				Arrays.asList("A", "B", "1", "C", "2"),
				Arrays.asList("1", "2"));
	}

	@Override
	public void move__target_feature_oldIndex_newIndex() {
		// TODO Auto-generated method stub
		
	}

	
}
