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
package gov.nasa.ensemble.core.plan.constraints.ui;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.constraints.ui.preference.PlanConstraintsPreferences;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanInsertionLocation;
import gov.nasa.ensemble.core.plan.editor.PlanOriginalLocation;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.dnd.TransferData;

public class TemporalChainTransferExtension extends AbstractPlanTransferableExtension {

	private static final String NAMESPACE = TemporalChainTransferExtension.class.getCanonicalName() + ":";
	private static final String REMAINING_ELEMENT_KEY = NAMESPACE + "remainingElementChains";
	private static final String REPLACED_DURING_INSERT_KEY = NAMESPACE + "replacedDuringInsert";
	private static final String RESULT_FROM_INSERT_KEY = NAMESPACE + "resultFromInsert";
	private static final String TRANSFERABLE_KEY = NAMESPACE + "transferable";
	private static final String ORIGINAL_LOCATION_KEY = NAMESPACE + "originalLocationChains";
	private static final Logger trace = Logger.getLogger(TemporalChainTransferExtension.class);
	
	@Override
	public boolean vetoInsertHook(TransferData type, PlanElementState state) {
		if (PlanContainerTransferProvider.transfer.isSupportedType(type)
			&& !ConstraintsPlugin.ALLOW_PULL_GROUP_CONSTRAINTS
			&& PlanConstraintsPreferences.getUseMeetsChains()) {
			// If inserting a group when pull group constraints are not allowed
			// and meets chains are being used should only be allowed if not
			// dropping into a chain.
			EPlanParent parent = state.getParent();
			int index = state.getIndex();
			if (index == 0) {
				// dropped at the beginning, not in chain
				return false;
			}
			EList<EPlanChild> children = parent.getChildren();
			if (index == children.size()) {
				// dropped at the end, not in chain
				return false;
			}
			EPlanChild predecessor = children.get(index - 1);
			EPlanChild successor = children.get(index);
			ConstraintsMember predecessorMember = predecessor.getMember(ConstraintsMember.class, false);
			ConstraintsMember successorMember = successor.getMember(ConstraintsMember.class, false);
			if ((predecessorMember != null) && (successorMember != null)) {
				TemporalChain predecessorChain = predecessorMember.getChain();
				TemporalChain successorChain = successorMember.getChain();
				if ((predecessorChain != null) && (predecessorChain == successorChain)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void postGetLocation(PlanTransferable transferable, PlanOriginalLocation location) {
		Set<EPlanElement> allPlanElements = EPlanUtils.computeContainedElements(transferable.getPlanElements());
		Set<TemporalChain> oldChains = TemporalChainUtils.getChains(allPlanElements, false);
		setOriginalLocationChains(transferable, location, oldChains);
	}
	
	@Override
	public void postGetHook(PlanTransferable transferable) {
		Set<EPlanElement> planElementSet = EPlanUtils.computeContainedElements(transferable.getPlanElements());
		Set<TemporalChain> originalChains = TemporalChainUtils.getChains(planElementSet, false);
		if (originalChains != null) {
			Set<TemporalChain> transferableChains = new HashSet<TemporalChain>();
			for (TemporalChain originalChain : originalChains) {
				List<EPlanElement> planElements = new ArrayList<EPlanElement>();
				List<EPlanElement> originalChainElements = originalChain.getElements();
				for (EPlanElement chainElement : originalChainElements) {
					if (planElementSet.contains(chainElement)) {
						planElements.add(chainElement);
					}
				}
				if (planElements.size() == originalChainElements.size()) {
					transferableChains.add(originalChain);
				} else if (planElements.size() >= 2) {
					TemporalChain newChain = TemporalChainUtils.createChain(planElements);
					transferableChains.add(newChain);
				}
			}
			setTransferableChains(transferable, transferableChains);
		}
	}

	@Override
	public void postCopyHook(PlanTransferable original, PlanTransferable copy) {
		Map<EPlanElement, EPlanElement> oldElementToNewPlanElement = PlanTransferable.createCopyPlanElementMap(original, copy);
		Set<TemporalChain> copyChains = new HashSet<TemporalChain>();
 		Set<TemporalChain> originalChains = getTransferableChains(original);
 		if (originalChains != null) {
 			for (TemporalChain originalChain : originalChains) {
 				List<EPlanElement> elements = new ArrayList<EPlanElement>();
 				for (EPlanElement originalElement : originalChain.getElements()) {
 					EPlanElement newElement = oldElementToNewPlanElement.get(originalElement);
 					if (newElement != null) {
 						elements.add(newElement);
 					}
 				}
 				if (elements.size() >= 2) {
 					TemporalChain newChain = TemporalChainUtils.createChain(elements);
 					copyChains.add(newChain);
 				}
 			}
 			setTransferableChains(copy, copyChains);
		}
	}
	
	@Override
	public void mergeHook(PlanTransferable copy, PlanTransferable mergedCopy) {
		Set<TemporalChain> transferableChains = getTransferableChains(copy);
		Set<TemporalChain> mergedCopyTransferableChains = getTransferableChains(mergedCopy);
		if(mergedCopyTransferableChains == null) {
			mergedCopyTransferableChains = new HashSet<TemporalChain>();
			setTransferableChains(mergedCopy, mergedCopyTransferableChains);
		}		
		mergedCopyTransferableChains.addAll(transferableChains);
	}

	@Override
	public void postUnpackHook(PlanTransferable planTransferable) {
		Set<TemporalChain> chains = getTransferableChains(planTransferable);
		if (chains != null) {
			setTransferableChains(planTransferable, chains);
		}
	}

	@Override
	public void preAddHook(IPlanElementTransferable transferable, IStructureLocation location) {
		prepareTransferableChains(transferable, location);
		if (location instanceof PlanInsertionLocation) {
			createChainResultingFromInsertionIfNecessary(transferable, (PlanInsertionLocation)location);
		}
	}

	@Override
	public void postAddHook(IPlanElementTransferable transferable, IStructureLocation location) {
		if (location instanceof PlanInsertionLocation) {
			PlanInsertionLocation insertionLocation = (PlanInsertionLocation) location;
			// inserting something "new"
			TemporalChain replacedChain = getChainReplacedDuringInsertion(insertionLocation);
			TemporalChain resultingChain = getChainResultingFromInsertion(insertionLocation);
			Set<TemporalChain> transferableChains = getTransferableChains(transferable);
			if (replacedChain != null) {
				TemporalChainUtils.detachChain(replacedChain);
			}
			if (resultingChain != null) {
				TemporalChainUtils.attachChain(resultingChain);
			} else if (transferableChains != null) {
				for (TemporalChain transferableChain : transferableChains) {
					TemporalChainUtils.attachChain(transferableChain);
				}
			}
		}
		if (location instanceof PlanOriginalLocation) {
			// adding back something that was removed
			Set<TemporalChain> locationChains = getOriginalLocationChains(transferable, location);
			if (locationChains != null) {
				for (TemporalChain locationChain : locationChains) {
					TemporalChainUtils.attachChain(locationChain);
				}
			} 
			Set<TemporalChain> addChains = getChainsOfRemainingElements(transferable);
			if (addChains != null) {
				for (TemporalChain addChain : addChains) {
					TemporalChainUtils.detachChain(addChain);
				}
			}
		}
	}

	@Override
	public void preRemoveHook(IPlanElementTransferable transferable, IStructureLocation location) {
		Set<EPlanElement> allPlanElements = EPlanUtils.computeContainedElements(transferable.getPlanElements());
		Set<TemporalChain> removeChains = TemporalChainUtils.getChains(allPlanElements, false);
		TemporalChain replacedChain = (location instanceof PlanInsertionLocation ? getChainReplacedDuringInsertion((PlanInsertionLocation)location) : null);
		if (replacedChain != null) {
			if (removeChains != null) {
				for (TemporalChain removeChain : removeChains) {
					TemporalChainUtils.detachChain(removeChain);
				}
			}
			TemporalChainUtils.attachChain(replacedChain);
		} else {
			Set<TemporalChain> remainingElementChains = getChainsOfRemainingElements(transferable);
			if (remainingElementChains == null) {
				// we need to create the new chains to attach (if any)
				remainingElementChains = new HashSet<TemporalChain>();
				List<? extends EPlanElement> planElements = transferable.getPlanElements();
				for (TemporalChain removeChain : removeChains) {
					TemporalChain newChain = createRemainingElementChain(planElements, removeChain);
					if (newChain != null) {
						remainingElementChains.add(newChain);
					}
				}
				setChainsOfRemainingElements(transferable, remainingElementChains);
			}
			if (removeChains != null) {
				for (TemporalChain removeChain : removeChains) {
					TemporalChainUtils.detachChain(removeChain);
				}
			}
			for (TemporalChain remainingElementChain : remainingElementChains) {
				TemporalChainUtils.attachChain(remainingElementChain);
			}
		}
	}

	private TemporalChain createRemainingElementChain(List<? extends EPlanElement> planElements, TemporalChain chain) {
		List<EPlanElement> newPlanElements = new ArrayList<EPlanElement>();
		for (EPlanElement chainElement : chain.getElements()) {
			if (!planElements.contains(chainElement)) {
				newPlanElements.add(chainElement);
			}
		}
		// if there are enough, leftovers regroup to make a new chain
		if (newPlanElements.size() > 1) {
			return TemporalChainUtils.createChain(newPlanElements);
		}
		return null;
	}
	
	/*
	 * Utility functions
	 */
	
	/**
	 * This method will adapt chains from the clipboard or drag/drop buffer so they are prepared
	 * to be added to the location provided.  It does this by resolving the plan element of each
	 * chain element if necessary (maybe never necessary?) and by setting the plan of the chain
	 * to be equal to the destination plan. 
	 * 
	 * @param transferable
	 * @param location
	 */
	private void prepareTransferableChains(IPlanElementTransferable transferable, IStructureLocation location) {
		Set<TemporalChain> transferableChains = getTransferableChains(transferable);
		if (transferableChains != null) {
			EPlan destinationPlan = null;
			if (location instanceof PlanOriginalLocation) {
				EPlanElement firstElement = transferable.getPlanElements().iterator().next();
				PlanElementState state = ((PlanOriginalLocation)location).getPlanElementState(firstElement);
				destinationPlan = EPlanUtils.getPlan(state.getParent());
			} else if (location instanceof PlanInsertionLocation) {
				destinationPlan = EPlanUtils.getPlan(((PlanInsertionLocation)location).getTarget());
			}
			if ((destinationPlan == null) && !transferableChains.isEmpty()) {
				trace.warn("failed to find destination plan before adding chains.");
			}
		}
	}

	/**
	 * First checks the transferable to ensure that the chain resulting 
	 * from insertion hasn't been created yet.  Then it checks to see
	 * if this insertion point corresponds to an insertion into an
	 * existing chain.  If so, then it creates a new chain to be inserted.
	 * @param transferable
	 * @param location
	 */
	private void createChainResultingFromInsertionIfNecessary(IPlanElementTransferable transferable, PlanInsertionLocation location) {
		if (getChainResultingFromInsertion(location) == null) {
			PlanElementState insertionState = location.getInsertionState();
			EPlanElement parent = insertionState.getParent();
			int index = insertionState.getIndex();
			List<? extends EPlanElement> children = EPlanUtils.getChildren(parent);
			if (index > 0 && index < children.size()) {
				EPlanElement priorPlanElement = children.get(index - 1);
				EPlanElement subsequentPlanElement = children.get(index);
				ConstraintsMember priorMember = priorPlanElement.getMember(ConstraintsMember.class, false);
				ConstraintsMember subsequentMember = subsequentPlanElement.getMember(ConstraintsMember.class, false);
				if ((priorMember != null) && (subsequentMember != null)) {
					TemporalChain priorChain = priorMember.getChain();
					TemporalChain subsequentChain = subsequentMember.getChain();
					if ((priorChain != null) && (subsequentChain != null) && (priorChain == subsequentChain)) {
						TemporalChain newChain = createChainResultingFromInsertion(transferable, priorPlanElement);
						setChainReplacedDuringInsertion(location, priorChain);
						setChainResultingFromInsertion(location, newChain);
					}
				}
			}
		}
	}

	/**
	 * Takes a plan transferable which represents a set of plan elements 
	 * to be inserted into an existing chain, and the chain element from
	 * that chain which is located immediately prior to the insertion point.
	 * Creates a new chain with all the elements of the old chain before
	 * the insertion point, then all the elements of the transferable, and
	 * then the remaining elements from the existing chain. 
	 *  
	 * @param transferable
	 * @param priorChainElement
	 * @return the new chain
	 */
	private TemporalChain createChainResultingFromInsertion(IPlanElementTransferable transferable, EPlanElement priorChainElement) {
		TemporalChain oldChain = priorChainElement.getMember(ConstraintsMember.class, true).getChain();
		List<EPlanElement> newChainPlanElements = new ArrayList<EPlanElement>();
		for (EPlanElement chainElement : oldChain.getElements()) {
			newChainPlanElements.add(chainElement);
			if (chainElement == priorChainElement) {
				// insert the new elements at this point in the chain
				newChainPlanElements.addAll(transferable.getPlanElements());
			}
		}
		return TemporalChainUtils.createChain(newChainPlanElements);
	}

	/**
	 * Store original location chains.
	 * These are chains that used to exist in the plan before some element was removed. 
	 */
	private static void setOriginalLocationChains(
		IPlanElementTransferable transferable, 
		PlanOriginalLocation location, Set<TemporalChain> oldChains
	) {
		location.setData(ORIGINAL_LOCATION_KEY, oldChains);
	}
	
	/**
	 * Retrieve original location chains.
	 * These are chains that used to exist in the plan before some element was removed.
	 * @param transferable
	 * @param location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Set<TemporalChain> getOriginalLocationChains(
		IPlanElementTransferable transferable, 
		IStructureLocation location
	) {
		if (location instanceof PlanOriginalLocation) {
			PlanOriginalLocation originalLocation = (PlanOriginalLocation) location;
			return (Set<TemporalChain>)originalLocation.getData(ORIGINAL_LOCATION_KEY);
		}
		return null;
	}

	/**
	 * Store transferable chains.
	 * These are chains that exist temporarily on the clipboard or in the drag/drop buffer.
	 * @param transferable
	 * @param chains
	 */
	public static void setTransferableChains(IPlanElementTransferable transferable, Set<TemporalChain> chains) {
		transferable.setData(TRANSFERABLE_KEY, chains);
	}

	/**
	 * Retrieve transferable chains.
	 * These are chains that exist temporarily on the clipboard or in the drag/drop buffer.
	 * @param transferable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Set<TemporalChain> getTransferableChains(IPlanElementTransferable transferable) {
		return (Set<TemporalChain>)transferable.getData(TRANSFERABLE_KEY);
	}

	/**
	 * Retrieve the chain resulting from insertion (if any)
	 * If some plan elements are inserted into an existing chain, there will be
	 * a new chain created, which is this chain.
	 * @param location
	 * @return
	 */
	private static TemporalChain getChainResultingFromInsertion(PlanInsertionLocation location) {
		return (TemporalChain)location.getData(RESULT_FROM_INSERT_KEY);
	}

	/**
	 * Set the chain resulting from insertion
	 * If some plan elements are inserted into an existing chain, there will be
	 * a new chain created, which is this chain.
	 * 
	 * @param location
	 * @param chain
	 */
	private static void setChainResultingFromInsertion(PlanInsertionLocation location, TemporalChain chain) {
		location.setData(RESULT_FROM_INSERT_KEY, chain);
	}

	/**
	 * Set the chain that is replaced during insertion
	 * If some plan elements are inserted into an existing chain, that existing
	 * chain will be removed, and this is that existing chain.
	 * @param location
	 * @return
	 */
	private static TemporalChain getChainReplacedDuringInsertion(PlanInsertionLocation location) {
		return (TemporalChain)location.getData(REPLACED_DURING_INSERT_KEY);
	}

	/**
	 * Set the chain that is replaced during insertion
	 * If some plan elements are inserted into an existing chain, that existing
	 * chain will be removed, and this is that existing chain.
	 * @param location
	 * @param chain
	 */
	private static void setChainReplacedDuringInsertion(PlanInsertionLocation location, TemporalChain chain) {
		location.setData(REPLACED_DURING_INSERT_KEY, chain);
	}

	/**
	 * Retrieve the chains of remaining elements
	 * When some plan elements are removed from existing chains but those chains
	 * still have at least two elements left after the removal, new shorter chains 
	 * are created, and these are those chains. 
	 * @param transferable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Set<TemporalChain> getChainsOfRemainingElements(IPlanElementTransferable transferable) {
		return (Set<TemporalChain>)transferable.getData(REMAINING_ELEMENT_KEY);
	}

	/**
	 * Set the chains of remaining elements
	 * When some plan elements are removed from existing chains but those chains
	 * still have at least two elements left after the removal, new shorter chains 
	 * are created, and these are those chains. 
	 * @param transferable
	 * @param chains
	 */
	public static void setChainsOfRemainingElements(IPlanElementTransferable transferable, Set<TemporalChain> chains) {
		transferable.setData(REMAINING_ELEMENT_KEY, chains);
	}

}
