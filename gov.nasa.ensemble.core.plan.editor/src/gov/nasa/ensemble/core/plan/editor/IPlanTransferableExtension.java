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

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.plan.PlanElementState;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.dnd.TransferData;

public interface IPlanTransferableExtension extends IAdaptable {
	
	/**
	 * Called from canInsert to allow extensions to veto
	 * insertion to the state.  Return true to veto.
	 * @param type TODO
	 * @param state
	 * 
	 * @return true to veto insertion
	 */
	public boolean vetoInsertHook(TransferData type, PlanElementState state);

	/**
	 * Called from getLocation after a PlanTransferable's
	 * current location has been requested.  Use setData to
	 * add data to the PlanOriginalLocation.
	 * @param transferable
	 * @param location 
	 */
	public void postGetLocation(PlanTransferable transferable, PlanOriginalLocation location);
	
	/**
	 * Called from getTransferable(ISelection selection) after the
	 * base PlanTransferable has been constructed.  Use setData to
	 * add data to the PlanTransferable.
	 * 
	 * @param transferable
	 */
	public void postGetHook(PlanTransferable transferable);

	/**
	 * Called from getTransferable(ISelection selection) after the
	 * base PlanInsertionLocation has been constructed.  Use setData to
	 * add data to the PlanInsertionLocation.
	 * 
	 * @param transferable
	 * @param location
	 */
	public void postGetInsertionHook(ITransferable transferable, PlanInsertionLocation location);

	/**
	 * Called from copy(ITransferable transferable) after the base
	 * copy of PlanTransferable has been constructed.  Use getData
	 * or setData to add data to the PlanTransferable.
	 * 
	 * @param original
	 * @param copy
	 */
	public void postCopyHook(PlanTransferable original, PlanTransferable copy);

	/**
	 * Called from copy after the instantiation after the generation of the unique ID.
	 * @param original
	 * @param copy
	 */
	public void postInstantiationHook(PlanTransferable original, PlanTransferable copy);

	/**
	 * This method is to support making multiple copies.
	 * 
	 * @param copy The copy that is a single copy of a source transferable
	 * @param mergedCopy The result of merging a number of individual copies.
	 * Some information may be added from copy to mergedCopy based on
	 * subclass implementation.
	 */	
	public void mergeHook(PlanTransferable copy, PlanTransferable mergedCopy);
	
	/**
	 * Called from unpackTransferObject after a PlanTransferable
	 * has been reconstituted from the clipboard or a drag and drop
	 * operation.
	 * 
	 * @param planTransferable
	 */
	public void postUnpackHook(PlanTransferable planTransferable);

	/**
	 * Called from add before a PlanTransferable is added to the plan
	 * at the supplied PlanOriginalLocation.
	 * @param transferable
	 * @param location
	 */
	public void preAddHook(IPlanElementTransferable transferable, IStructureLocation location);
	
	/**
	 * Called from add after a PlanTransferable is added to the plan
	 * at the supplied PlanOriginalLocation. 
	 * 
	 * @param transferable
	 * @param location
	 */
	public void postAddHook(IPlanElementTransferable transferable, IStructureLocation location);
	
	/**
	 * Called from remove before a PlanTransferable is removed from the plan.
	 * @param transferable
	 * @param location
	 */
	public void preRemoveHook(IPlanElementTransferable transferable, IStructureLocation location);

	/**
	 * Called from remove after a PlanTransferable is removed from the plan.
	 * @param transferable
	 * @param location
	 */
	public void postRemoveHook(IPlanElementTransferable transferable, IStructureLocation location);

}
