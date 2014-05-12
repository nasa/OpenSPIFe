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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.ui.transfers.ITransferable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.dnd.TransferData;

public interface IStructureModifier {

	/**
	 * Determine if data of the given type can be inserted with the given semantics and selection. Note: this should be fairly fast
	 * since it can be called repeatedly during a drag and drop.
	 * 
	 * @param type
	 * @param selection
	 * @param semantics
	 * 
	 * @return boolean
	 */
	public boolean canInsert(TransferData type, ISelection selection, InsertionSemantics semantics);

	/**
	 * Returns the transferable in the selection. If there is no selection, or no transferable, return null.
	 * 
	 * @param selection
	 * 
	 * @return the transferable in the selection
	 */
	public ITransferable getTransferable(ISelection selection);

	/**
	 * Compute the landing site of a paste operation in the given document for an object of the given type when the selection in
	 * that document is the given selection.
	 * 
	 * Returns null if there is no viable location for an object of the given type.
	 * 
	 * @param transferable
	 * @param selection
	 * @param insertionSemantics
	 * 
	 * @return IStructureLocation
	 */
	public IStructureLocation getInsertionLocation(ITransferable transferable, ISelection selection,
			InsertionSemantics insertionSemantics);

	/**
	 * Puts the transferable in the location.
	 * 
	 * @param transferable
	 * @param location
	 */
	public void add(ITransferable transferable, IStructureLocation location);

	/**
	 * Returns where the transferable is.
	 * 
	 * @param transferable
	 * @return IStructureLocation
	 */
	public IStructureLocation getLocation(ITransferable transferable);

	/**
	 * Removes the transferable which should be at the given location.
	 * 
	 * @param transferable
	 */
	public void remove(ITransferable transferable, IStructureLocation location);

	/**
	 * Copy the transferable.
	 * 
	 * @param transferable
	 * @return ITransferable
	 */
	public ITransferable copy(ITransferable transferable);

}
