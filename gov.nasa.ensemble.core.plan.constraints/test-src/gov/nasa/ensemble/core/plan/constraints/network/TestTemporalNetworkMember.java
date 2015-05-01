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
package gov.nasa.ensemble.core.plan.constraints.network;

import static org.junit.Assert.*;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;

import java.util.Date;
import java.util.List;

import javax.measure.unit.NonSI;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTemporalNetworkMember {

	private EPlan plan;
	private TemporalNetworkMember networkMember;
	
	@Before
	public void setUp() {
		ActivityDictionary.getInstance().restoreDefaultDictionary();
		final PlanFactory planFactory = PlanFactory.getInstance();
		plan = planFactory.createPlan(getClass().getName());
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		ExtensionPointResourceSetListener.addListener(domain, true);
		final Date planStartDate = new Date();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(PlanTemporalMember.class).setStartBoundary(planStartDate);
			}
		});
		final List<EActivityDef> defs = ActivityDictionary.getInstance().getActivityDefs();
		final EList<EPlanChild> children = plan.getChildren();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				Date date = planStartDate;
				for (EActivityDef def : defs) {
					final EActivity activity = planFactory.createActivity(def);
					activity.getMember(TemporalMember.class).setStartTime(date);
					children.add(activity);
					date = DateUtils.add(date, AmountUtils.toAmount(1, NonSI.MINUTE));
				}
			}
		});
		assertEquals(children.size(), defs.size());
		if (children.size() < 2) {
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					for (int i = children.size() ; i < 2 ; i++) {
						EActivity genericActivity = planFactory.createActivityInstance();
						children.add(genericActivity);
					}
				}
			});
		}
		final TemporalChain chain = TemporalChainUtils.createChain(children);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				TemporalChainUtils.attachChain(chain);
			}
		});
		networkMember = WrapperUtils.getMember(plan, TemporalNetworkMember.class);
	}
	
	@After
	public void tearDown() {
		if (plan != null) {
			WrapperUtils.dispose(plan);
		}
	}

	@Test
	public void testConsistency() {
		assertTrue(networkMember.isConsistent());
		checkProperties(plan);
		EList<EPlanChild> children = plan.getChildren();
		for (EPlanChild child : children) {
			checkProperties(child);
		}
	}

	private void checkProperties(EPlanElement element) {
		ConsistencyProperties properties = networkMember.getProperties(element);
		assertNotNull(properties);
		ConsistencyBounds propertyBounds = properties.getBounds();
		assertNotNull(propertyBounds);
		ConsistencyBounds bounds = networkMember.getBounds(element);
		assertNotNull(bounds);
		assertEquals(propertyBounds.getEarliestStart(), bounds.getEarliestStart());
		assertEquals(propertyBounds.getEarliestEnd(), bounds.getEarliestEnd());
		assertEquals(propertyBounds.getEarliestStart(), bounds.getLatestStart());
		assertEquals(propertyBounds.getEarliestEnd(), bounds.getLatestEnd());
	}
	
}
