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
package gov.nasa.ensemble.core.plan.constraints.ui.operation;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.PlanStructureComparator;
import gov.nasa.ensemble.core.plan.constraints.ui.TemporalChainTransferExtension;
import gov.nasa.ensemble.core.plan.editor.PlanInsertionLocation;
import gov.nasa.ensemble.core.plan.editor.PlanOriginalLocation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class ChainOperation extends AbstractTransactionUndoableOperation {
	
	private static final Logger trace = Logger.getLogger(ChainOperation.class);
	
	private final PlanStructureModifier modifier;
	private final Set<TemporalChain> oldChains;
	private final TemporalChain newChain;
	private final EPlan context;
	private PlanTransferable transferable = null;
	private PlanOriginalLocation origin = null;
	private PlanInsertionLocation destination = null;
	private List<? extends EPlanElement> oldOrder = null;
	private List<? extends EPlanElement> newOrder = null;


	/**
	 * Chain the elements in the specified order.
	 * @param modifier
	 * @param elements
	 * @param shouldReorderByTime
	 */
	public ChainOperation(PlanStructureModifier modifier, List<EPlanChild> elements, boolean shouldReorderByTime) {
		super("chain items");
		this.modifier = modifier;
		trace.debug("creating " + ChainOperation.class.getSimpleName() + ": " + elements);
		if ((elements == null) || (elements.size() < 2)) {
			throw new IllegalArgumentException("must chain 2 or more elements");
		}
		if (!ConstraintUtils.sameParent(elements)) {
			throw new IllegalArgumentException("all chained elements should have the same parent");
		}
		this.oldChains = TemporalChainUtils.getChains(elements, true);
		EList<EPlanChild> allPlanElements = TemporalChainUtils.getChainPlanElements(oldChains);
		for (EPlanChild element : elements) {
			if (!allPlanElements.contains(element)) {
				allPlanElements.add(element);
			}
		}
		if (shouldReorderByTime) {
			ECollections.sort(allPlanElements, TemporalChainUtils.CHAIN_ORDER);
		} else {
			ECollections.sort(allPlanElements, PlanStructureComparator.INSTANCE);
		}
		this.newChain = TemporalChainUtils.createChain(allPlanElements);
		this.context = EPlanUtils.getPlan(allPlanElements.get(0));
		checkOrder(allPlanElements);
	}

	private void checkOrder(List<EPlanChild> allPlanElements) {
		EPlanChild firstElement = allPlanElements.get(0);
		int currentIndex = firstElement.getListPosition();
		int insertionIndex = currentIndex;
		boolean alreadyInOrder = true;
		for (EPlanChild element : allPlanElements) {
			int elementIndex = element.getListPosition();
			if (currentIndex++ != elementIndex) {
				alreadyInOrder = false;
			}
			if (elementIndex < insertionIndex) {
				insertionIndex = elementIndex;
			}
		}
		if (!alreadyInOrder) {
			reorderElements(allPlanElements, firstElement, insertionIndex);
		}
	}

	private void reorderElements(List<EPlanChild> allPlanElements, EPlanElement firstElement, int insertionIndex) {
		this.newOrder = allPlanElements;
		ISelection selection = new StructuredSelection(allPlanElements);
		this.transferable = modifier.getTransferable(selection);
		EList<EPlanChild> oldOrder = new BasicEList<EPlanChild>(allPlanElements);
		ECollections.sort(oldOrder, PlanStructureComparator.INSTANCE);
		this.oldOrder = oldOrder;
		this.origin = modifier.getLocation(transferable);
		TemporalChainTransferExtension.setTransferableChains(transferable, Collections.<TemporalChain>emptySet());
		EPlanElement parent = (EPlanElement)firstElement.eContainer();
		if (insertionIndex == 0) {
			this.destination = new PlanInsertionLocation(parent, InsertionSemantics.ON, allPlanElements);
		} else {
			EPlanElement element = EPlanUtils.getChildren(parent).get(insertionIndex - 1);
			this.destination = new PlanInsertionLocation(element, InsertionSemantics.AFTER, allPlanElements);
		}
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("executing " + ChainOperation.class.getSimpleName() + ": " + this);
				for (TemporalChain oldChain : oldChains) {
					TemporalChainUtils.detachChain(oldChain);
				}
				if (transferable != null) {
					move(origin, destination, newOrder);
				}
				TemporalChainUtils.attachChain(newChain);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				trace.debug("undoing " + ChainOperation.class.getSimpleName() + ": " + this);
				TemporalChainUtils.detachChain(newChain);
				if (transferable != null) {
					move(destination, origin, oldOrder);
				}
				for (TemporalChain oldChain : oldChains) {
					TemporalChainUtils.attachChain(oldChain);
				}
			}
		});
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(ChainOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(newChain));
		if (oldChains.isEmpty()) {
			builder.append(" from ");
			for (TemporalChain oldChain : oldChains) {
				builder.append(oldChain + " ");
			}
		}
		return builder.toString();
	}

	private void move(IStructureLocation from, IStructureLocation to, List<? extends EPlanElement> newOrder) {
		List<? extends EPlanElement> originalOrder = transferable.getPlanElements();
		modifier.remove(transferable, from);
		try {
			transferable.setPlanElements(newOrder);
			modifier.add(transferable, to);
		} catch (RuntimeException e) {
			// don't duplicate elements
			// this handler assumes that we failed to add any part of the transferable
			transferable.setPlanElements(originalOrder);
			modifier.add(transferable, from);
			throw e;
		}
	}

	public static boolean isExecutableForSelection(ISelection selection) { 
		List<EPlanElement> elements = new ArrayList<EPlanElement>(PlanEditorUtil.emfFromSelection(selection));
		if ((elements == null) || (elements.size() < 2)) {
			return false;
		}
		if (!ConstraintUtils.sameParent(elements)) {
			return false;
		}
		return true;
	}

}
