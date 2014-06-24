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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class UnchainOperation extends AbstractTransactionUndoableOperation {
	
	private static final Logger trace = Logger.getLogger(UnchainOperation.class);
	
	private final Set<TemporalChain> oldChains;
	private final Set<TemporalChain> newChains;
	private final EPlan context;

	/**
	 * Unchain the supplied elements (possibly breaking existing chains
	 * into multiple chains)
	 * 
	 * There must be at least one element supplied.
	 * 
	 * @param planElements
	 */
	public UnchainOperation(List<? extends EPlanElement> planElements) {
		super("unchain items");
		trace.debug("creating " + UnchainOperation.class.getSimpleName() + ": " + planElements);
		if ((planElements == null) || planElements.isEmpty()) {
			throw new IllegalArgumentException("must unchain at least one element");
		}
		oldChains = TemporalChainUtils.getChains(planElements, true);
		newChains = new HashSet<TemporalChain>();
		for (TemporalChain oldChain : oldChains) {
			List<EPlanElement> newChainElements = new ArrayList<EPlanElement>();
			for (EPlanElement planElement : oldChain.getElements()) {
				if (!planElements.contains(planElement)) {
					newChainElements.add(planElement);
				} else {
					if (newChainElements.size() > 1) {
						// existing chain was broken, so make a new chain up to here
						TemporalChain chain = ConstraintsFactory.eINSTANCE.createTemporalChain();
						chain.getElements().addAll(newChainElements);
						newChains.add(chain);
					}
					// start accumulating elements anew
					newChainElements = new ArrayList<EPlanElement>();
				}
			}
			if (newChainElements.size() > 1) {
				// if there are enough, leftovers make the final chain
				TemporalChain chain = ConstraintsFactory.eINSTANCE.createTemporalChain();
				chain.getElements().addAll(newChainElements);
				newChains.add(chain);
			}
		}
		context = EPlanUtils.getPlan(planElements.get(0));
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}
	
	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("executing " + UnchainOperation.class.getSimpleName() + ": " + this);
				for (TemporalChain oldChain : oldChains) {
					TemporalChainUtils.detachChain(oldChain);
				}
				for (TemporalChain newChain : newChains) {
					TemporalChainUtils.attachChain(newChain);
				}
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("undoing " + UnchainOperation.class.getSimpleName() + ": " + this);
				for (TemporalChain newChain : newChains) {
					TemporalChainUtils.detachChain(newChain);
				}
				for (TemporalChain oldChain : oldChains) {
					TemporalChainUtils.attachChain(oldChain);
				}
			}
		});
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(UnchainOperation.class.getSimpleName());
		builder.append(":");
		for (TemporalChain oldChain : oldChains) {
			builder.append(oldChain + " ");
		}
		if (!newChains.isEmpty()) {
			builder.append("yields ");
			for (TemporalChain newChain : newChains) {
				builder.append(newChain + " ");
			}
		}
		return builder.toString();
	}

}
