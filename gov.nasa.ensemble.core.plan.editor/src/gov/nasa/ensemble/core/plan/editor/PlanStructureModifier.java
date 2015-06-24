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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.DiffIdGenerator;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityDefTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityDefTransferable;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.TransferData;

/**
 * Methods of querying and modifying a plan, e.g., by dragging and dropping a plan template into
 * a plan.
 */
public class PlanStructureModifier implements IStructureModifier {

	/** The singleton instance of this class. */
	public static final PlanStructureModifier INSTANCE = new PlanStructureModifier();
	
	private static final List<IPlanTransferableExtension> PLAN_TRANSFERABLE_EXTENSIONS = PlanTransferableExtensionRegistry.getInstance().getExtensions();

	/** The logger object for this class. */
	private Logger trace = Logger.getLogger(getClass());

	/** The zero-argument constructor does nothing. */
	protected PlanStructureModifier() {
		// default constructor
	}
	
	/**
	 * Can anything be transferred to the selection? It must have at least one plan element that
	 * is not a child. The selection's plan must be modifiable. There must be no vetoes.
	 * Note: called frequently during drag and drop.
	 * @param type
	 * @param selection the widget to insert
	 * @param semantics where to insert the selection
	 * @return whether the selection can be transferred
	 */
	@Override
	public boolean canInsert(TransferData type, ISelection selection, InsertionSemantics semantics) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("canInsert: selection: " + selection + " semantics: " + semantics);
		}
		EPlanElement target = getTargetElement(selection);
		if (target == null) {
			return false;
		}
		EPlan plan = EPlanUtils.getPlan(target);
		if (!PlanEditApproverRegistry.getInstance().canModifyStructure(plan)) { 
			return false;
		}
		PlanElementState state = null;
		if (ActivityDefTransferProvider.transfer.isSupportedType(type) ||
			ActivityTransferProvider.transfer.isSupportedType(type)) {
			// an activity def is essentially an activity for these purposes
			state = PlanUtils.getAddLocationForActivities(target, semantics); 
		}
		if (PlanContainerTransferProvider.transfer.isSupportedType(type)) {
			// a plan is essentially a bunch of activity groups for these purposes
			state = PlanUtils.getAddLocationForActivityGroups(target, semantics);
		}
		// only call vetoInsertHook methods if the state is not null
		if (state == null) {
			return false;
		}
		return notInsertionVetoed(type, state);
	}
	
	public boolean notInsertionVetoed(TransferData type, PlanElementState state) {
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			if (extension.vetoInsertHook(type, state)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Create and return a new PlanTransferable for the selection.  If the selection is empty
	 * or the first element is an EPlan the result will be null.  Otherwise, the PlanTransferable's
	 * elements become those of the selection. The plan unique ID becomes that of the elements' plan.
	 * Call the post-get hook after initializing.
	 * 
	 * @param selection the selection for which the PlanTransferable is being created
	 * @return the newly-created PlanTransferable if it could be created; otherwise null
	 */
	@Override
	public PlanTransferable getTransferable(ISelection selection) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("getTransferable: " + selection);
		}
		List<? extends EPlanElement> elements = getTransferableElements(selection);
		if (elements.isEmpty() || (elements.get(0) instanceof EPlan)) {
			return null;
		}
		PlanTransferable transferable = new PlanTransferable();
		transferable.setPlanElements(elements);
		EPlan plan = EPlanUtils.getPlan(elements.iterator().next());
		if (plan != null) {
			transferable.setPlanUniqueId(plan.getRuntimeId());
		}
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postGetHook(transferable);
		}
		return transferable;
	}

	/**
	 * @param t a transferable; it must be a plan-element transferable to have a location
	 * @param selection the selection into which the transferable is being transferred; it must
	 * have a target element for there to be a location
	 * @param semantics where to insert the transferable into the selection relative to the target
	 * element
	 */
	@Override
	public IStructureLocation getInsertionLocation(ITransferable t, ISelection selection, InsertionSemantics semantics) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("getInsertionLocation: " + t);
		}
		if (!(t instanceof IPlanElementTransferable)) {
			return null;
		}
		EPlanElement target = getTargetElement(selection);
		if (target == null) {
			return null; // no target
		}
		IPlanElementTransferable transferable = (IPlanElementTransferable) t;
		if (transferable instanceof ActivityDefTransferable) {
			((ActivityDefTransferable)transferable).instantiateElements(target, semantics);
		}
		List<? extends EPlanElement> insertingElements = transferable.getPlanElements(); 
		if ((insertingElements == null) || (insertingElements.isEmpty())) {
			return null;
		}
		PlanInsertionLocation planInsertionLocation = new PlanInsertionLocation(target, semantics, insertingElements);
		if (planInsertionLocation.getInsertionState() == null) {
			return null; // shouldn't fail here (would've failed in canInsert)
		}
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postGetInsertionHook(transferable, planInsertionLocation);
		}
		return planInsertionLocation;
	}

	/**
	 * If the transferable is a plan element transferable, add it to the location; otherwise
	 * trace the failure. Trace the call if enabled. 
	 * @param transferable a transferable, which might be a plan element transferable
	 * @param location the location to which the transferable is being dragged
	 */
	@Override
	public void add(ITransferable transferable, IStructureLocation location) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("add: " + transferable + " at " + location);
		}
		if (transferable instanceof IPlanElementTransferable) {
			addPlanElementTransferable((IPlanElementTransferable)transferable, location);
		} else {
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("add: transferable wasn't an instance of " + IPlanElementTransferable.class.getSimpleName());
			}
		}
	}

	@Override
	public PlanOriginalLocation getLocation(ITransferable transferable) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("getLocation: " + transferable);
		}
		if (!(transferable instanceof PlanTransferable)) {
			trace.warn("getLocation: transferable wasn't PlanTransferable");
			return null;
		}
		PlanTransferable planTransferable = (PlanTransferable) transferable;
		PlanOriginalLocation location = new PlanOriginalLocation();
		List<? extends EPlanElement> elements = planTransferable.getPlanElements();
		for (EPlanElement element : elements) {
			if (element instanceof EPlan) {
				return null; // plans have no 'location'
			}
			PlanElementState state = PlanUtils.getCurrentState(element);
			if (state == null) {
				return null;
			}
			location.setPlanElementState(element, state);
		}
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postGetLocation((PlanTransferable)transferable, location);
		}
		return location;
	}
	
	/**
	 * Undo an add operation. If the transferable is a plan element transferable, and it has
	 * plan elements, remove those elements from the location in a separate thread.
	 * @param transferable a transferable, which might be a plan element transferable
	 * @param location a structure location from which to remove the plan transferable's elements
	 */
	@Override
	public void remove(ITransferable transferable, final IStructureLocation location) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("remove: " + transferable);
		}
		if (transferable instanceof IPlanElementTransferable) {
			final IPlanElementTransferable peTransferable = (IPlanElementTransferable) transferable;
			List<? extends EPlanElement> elements = peTransferable.getPlanElements();
			if ((elements != null) && !elements.isEmpty()) {
				EPlan plan = EPlanUtils.getPlan(elements.iterator().next());
				if (PlanEditApproverRegistry.getInstance().canModifyStructure(plan)) {
					TransactionUtils.writing(plan, new Runnable() {
						@Override
						public void run() {
							removeElements(location, peTransferable);
						}
					});
				}
			}
		}
	}

	@Override
	public ITransferable copy(ITransferable transferable) {
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("copy: " + transferable);
		}
		if (transferable instanceof ActivityDefTransferable) {
			return transferable;
		}
		if (!(transferable instanceof PlanTransferable)) {
			trace.warn("copy: transferable wasn't PlanTransferable");
			return null;
		}
		PlanTransferable planTransferable = (PlanTransferable) transferable;
		List<? extends EPlanElement> planElements = planTransferable.getPlanElements();
		List<? extends EPlanElement> copiedPlanElements = EPlanUtils.copy(planElements);
		PlanTransferable copiedTransferable = new PlanTransferable();
		copiedTransferable.setPlanUniqueId(planTransferable.getPlanUniqueId());
		copiedTransferable.setPlanElements(copiedPlanElements);
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postCopyHook(planTransferable, copiedTransferable);
		}
		// Generate new diff ids for each copied plan element and its descendant plan elements
		new PlanVisitor(true) {
			@Override
			protected void visit(EPlanElement element) {
				String diffId = DiffIdGenerator.getInstance().generateDiffId(element.eClass());
				element.getMember(CommonMember.class).setDiffID(diffId);
				element.setPersistentID(EcoreUtil.generateUUID());
			}
		}.visitAll(copiedPlanElements);
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postInstantiationHook(planTransferable, copiedTransferable);
		}
		return copiedTransferable;
	}

    /*
     * Utility methods
     */
    
	@SuppressWarnings("unused")
	private void addPlanElementTransferable(IPlanElementTransferable transferable, IStructureLocation location) {
		final List<? extends EPlanElement> elements = transferable.getPlanElements();
		if ((elements == null) || elements.isEmpty()) {
			// nothing to do
			return;
		}
		PlanTransferableExtensionWizard wizard = null;
		if (location instanceof PlanInsertionLocation) {
//			PlanInsertionLocation insertionLocation = (PlanInsertionLocation) location;
//			wizard = checkPlanTransferableExtensionWizard(transferable, insertionLocation);
		} else if (!(location instanceof PlanOriginalLocation)) {
			trace.warn("add: location wasn't PlanOriginalLocation or PlanInsertionLocation");
			return;
		}
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.preAddHook(transferable, location);
		}
		if (location instanceof PlanOriginalLocation) {
			PlanOriginalLocation planLocation = (PlanOriginalLocation)location;
			final Map<EPlanChild, Integer> childToIndex = new HashMap<EPlanChild, Integer>();
			final Map<EPlanParent, Set<EPlanChild>> parentToNewChildren = new HashMap<EPlanParent, Set<EPlanChild>>();
			EPlan plan = null;
			for (EPlanElement element : elements) {
				EPlanChild child = (EPlanChild)element;
				PlanElementState state = planLocation.getPlanElementState(child);
				EPlanParent parent = state.getParent();
				childToIndex.put(child, state.getIndex()); // cache the index before insertion, to support the sort
				Set<EPlanChild> set = parentToNewChildren.get(parent);
				if (set == null) {
					set = new TreeSet<EPlanChild>(new Comparator<EPlanChild>() {
						@Override
						public int compare(EPlanChild o1, EPlanChild o2) {
							return childToIndex.get(o1) - childToIndex.get(o2);
						}
					});
					parentToNewChildren.put(parent, set);
				}
				set.add(child);
				if (plan == null) {
					plan = EPlanUtils.getPlan(parent);
				} else if (plan != EPlanUtils.getPlan(parent)) {
					Logger logger = Logger.getLogger(PlanStructureModifier.class);
					logger.warn("failed sanity check in add");
				}
			}
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					PlanUtils.addElementsAtIndices(parentToNewChildren, childToIndex);
				}
			});
		} else if (location instanceof PlanInsertionLocation) {
			PlanInsertionLocation insertionLocation = (PlanInsertionLocation) location;
			PlanElementState state = insertionLocation.getInsertionState();
			final EPlanParent parent = state.getParent();
			final int index = state.getIndex();
			TransactionUtils.writing(parent, new Runnable() {
				@Override
				public void run() {
					PlanUtils.addElementsHere(parent, index, CommonUtils.castList(EPlanChild.class, elements));
				}
			});
		}
		for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
			extension.postAddHook(transferable, location);
		}
		if (wizard != null) {
			wizard.dispose();
		}
	}

	/**
	 * Remove the plan elements from the plan element transferable. Before doing so, call the
	 * pre-remove hook. After doing so, call the post-remove hook.
	 * @param location passed to the pre-remove and post-remove hooks
	 * @param peTransferable the source of the elements to remove
	 */
	private void removeElements(IStructureLocation location, IPlanElementTransferable peTransferable) {
	    for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
	    	extension.preRemoveHook(peTransferable, location);
	    }
		PlanUtils.removeElements(peTransferable.getPlanElements());
	    for (IPlanTransferableExtension extension : PLAN_TRANSFERABLE_EXTENSIONS) {
	    	extension.postRemoveHook(peTransferable, location);
	    }
    }

	/**
	 * Return the element that is the target element of the selection.
	 * The target is the element that is last in the plan. (not last in time)
	 * 
	 * @see PlanUtils.INHERENT_ORDER
	 * @param selection a selection
	 * @return the target element, or null if none
	 */
	public EPlanElement getTargetElement(ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		EPlanElement target = null;
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		for (Iterator iterator = structuredSelection.iterator() ; iterator.hasNext() ; ) {
			Object object = iterator.next();
			if (object instanceof EPlanElement) {
				EPlanElement element = (EPlanElement) object;
				if (target == null) {
					target = element;
				} else {
					int compare = PlanUtils.INHERENT_ORDER.compare(target, element);
					if (compare < 0) {
						target = element;
					}
				}
			}
		}
		return target;
	}

	/**
	 * Return a list of the plan elements in the selection which don't have parents in the
	 * list and which are not sub-activities, in the order in which they occur in the plan.
	 * @param selection a selection
	 * @return a list of the plan elements in the selection which don't have parents in the
	 * list and which are not sub-activities
	 */
	private List<? extends EPlanElement> getTransferableElements(ISelection selection) {
		Set<EPlanElement> allElements = PlanEditorUtil.emfFromSelection(selection);
		List<EPlanElement> consolidatedElements = EPlanUtils.getConsolidatedPlanElements(allElements);
		List<EPlanElement> editableElements = new ArrayList<EPlanElement>(consolidatedElements.size());
		for (EPlanElement element : consolidatedElements) {
			if (element instanceof EActivity) {
				EActivity activity = (EActivity) element;
				if (activity.isIsSubActivity() && activity.getParent() instanceof EActivity) {
					continue;
				}
			}
			editableElements.add(element);
		}
		Collections.sort(editableElements, PlanUtils.INHERENT_ORDER);
		return editableElements;
	}

//	private void checkPlanTransferableExtensionWizard(IPlanElementTransferable transferable, PlanInsertionLocation location) {
//		if (PlatformUI.isWorkbenchRunning() && !CommonPlugin.isJunitRunning()) { // JUnit
//			PlanTransferableExtensionWizard wizard = new PlanTransferableExtensionWizard(transferable, location);
//			if (PasteSpecialSettings.getInstance().getBoolean(PasteSpecialSettings.ACTIVE)
//				|| !wizard.getWarnings().isEmpty()) {
//				IWorkbench workbench = PlatformUI.getWorkbench();
//				Shell parent = workbench.getActiveWorkbenchWindow().getShell();
//				WizardDialog dialog = new WizardDialog(parent, wizard);
//				int returnCode = dialog.open();
//				switch(returnCode) {
//				case Window.CANCEL:
//					throw new OperationCanceledException();
//				default:
//					// go on
//				}
//			}
//		}
//		return wizard;
//	}
	

}
