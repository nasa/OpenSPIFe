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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

public class TemporalChainUtils {

	private static final int CHAIN_SIZE_ESTIMATE = 5;

	/**
	 * Make a new chain with the provided plan elements
	 * @param planElements (must contain at least two elements)
	 * @return
	 */
	public static TemporalChain createChain(List<? extends EPlanElement> planElements) {
		if ((planElements == null) || (planElements.size() < 2)) {
			throw new IllegalArgumentException("must chain 2 or more elements");
		}
		TemporalChain chain = ConstraintsFactory.eINSTANCE.createTemporalChain();
		chain.getElements().addAll(planElements);
		return chain;
	}
	
	/**
	 * Returns the set of all chains that any of the plan elements
	 * participate in.  The chains are returned in their order of
	 * first occurrence in the plan element collection.
	 * 
	 * @param planElements
	 * @return the set of chains 
	 */
	public static LinkedHashSet<TemporalChain> getChains(Collection<? extends EPlanElement> planElements, boolean mustExist) {
		LinkedHashSet<TemporalChain> chains = new LinkedHashSet<TemporalChain>();
		for (EPlanElement planElement : planElements) {
			ConstraintsMember constraintsMember = planElement.getMember(ConstraintsMember.class, mustExist);
			if (constraintsMember != null) {
				TemporalChain chain = constraintsMember.getChain();
				if (chain != null) {
					chains.add(chain);
				}
			}
		}
		return chains;
	}
	
	/**
	 * Returns a list of all plan elements that are in the given chains
	 */
	public static EList<EPlanChild> getChainPlanElements(Collection<TemporalChain> chains) {
		EList<EPlanChild> allPlanElements = new BasicEList<EPlanChild>(CHAIN_SIZE_ESTIMATE*chains.size());
		for (TemporalChain chain : chains) {
			allPlanElements.addAll(CommonUtils.castList(EPlanChild.class, chain.getElements()));
		}
		return allPlanElements;
	}
	
	/**
	 * Create a new subchain with a sublist of elements from the supplied chain 
	 * @see List.subList
	 * @param chain
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public static TemporalChain createSubchain(TemporalChain chain, int fromIndex, int toIndex) {
		EList<EPlanElement> newChainElements = new BasicEList<EPlanElement>();
		for (int i = fromIndex ; i <= toIndex ; i++) {
			EPlanElement chainElement = chain.getElements().get(i);
			newChainElements.add(chainElement);
		}
		return createChain(newChainElements);
	}

	/**
	 * Given the chain, add it to the plan by adding it as a member to the plan 
	 * and adding its elements as members of their corresponding plan elements.
	 * @param chain
	 */
	public static void attachChain(TemporalChain chain) {
		for (EPlanElement chainElement : chain.getElements()) {
			ConstraintsMember constraintsMember = chainElement.getMember(ConstraintsMember.class, true);
			if (constraintsMember.getChain() != null) {
//				 throw new IllegalStateException("adding a chain to a plan element that already has one");
			}
			constraintsMember.setChain(chain);
		}
	}

	/**
	 * Given the chain, remove it from the plan by removing it as a member from the plan 
	 * and removing its elements as members from their corresponding plan elements.
	 * @param chain
	 */
	public static void detachChain(TemporalChain chain) {
		for (EPlanElement chainElement : chain.getElements()) {
			ConstraintsMember constraintsMember = chainElement.getMember(ConstraintsMember.class, true);
			if (constraintsMember.getChain() != chain) {
//				 throw new IllegalStateException("removing a chain from an element that belongs to a different chain");
			} else {
				constraintsMember.setChain(null);
			}
		}
	}
	
	/**
	 * Sort in the chaining order:
	 * 1. order by current start times
	 * 2. break ties by using the inherent order of the elements
	 */
	public static final Comparator<EPlanElement> CHAIN_ORDER
		= new Comparator<EPlanElement>() {
		@Override
		public int compare(EPlanElement o1, EPlanElement o2) {
			TemporalMember m1 = o1.getMember(TemporalMember.class);
			TemporalMember m2 = o2.getMember(TemporalMember.class);
			Date d1 = m1.getStartTime();
			Date d2 = m2.getStartTime();
			if ((d1 == null) || (d2 == null)) {
				return PlanUtils.INHERENT_ORDER.compare(o1, o2);
			}
			int result = (int)DateUtils.subtract(d1, d2);
			if (result != 0) {
				return result;
			}
			return PlanUtils.INHERENT_ORDER.compare(o1, o2);
		}
	};

	/**
	 * Return a set of all the chains in the plan
	 * 
	 * @param plan
	 * @return
	 */
	public static Set<TemporalChain> getChains(EPlan plan) {
		final Set<TemporalChain> chains = new LinkedHashSet<TemporalChain>();
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
				if (constraintsMember != null) {
					TemporalChain chain = constraintsMember.getChain();
					if (chain != null) {
						chains.add(chain);
					}
				}
			}
		}.visitAll(plan);
		return chains;
	}

}
