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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.transfers;

import org.eclipse.swt.dnd.Transfer;

/**
 * TransferProvider is an extension point interface.
 * Classes implement this extension point in order to
 * contribute formats to the clipboard or drag and drop.
 * 
 * @author Andrew
 *
 */
public interface ITransferProvider<TRANSFER_TYPE> {

	/**
	 * Returns the transfer that this provider provides.
	 * @return the transfer
	 */
	public Transfer getTransfer();

	/**
	 * Checks the ITransferable to see if it can be packed
	 * into a transfer object.  Equivalent to, but
	 * should be more efficient than:
	 *    (packTransferObject(transferable) != null)
	 * 
	 * @param transferable
	 * @return false if packTransferObject(transferable) would be null 
	 */
	public boolean canPack(ITransferable transferable);
	
	/**
	 * Returns an object of the type expected by the transfer. 
	 * 
	 * @param transferable
	 * @return the object, or null if this transfer can't accept the ITransferable
	 */
	public TRANSFER_TYPE packTransferObject(ITransferable transferable);

	/**
	 * Accepts an object from a transfer and unpacks it into an ITransferable.
	 * 
	 * @param transferObject
	 * @return the array
	 */
	public ITransferable unpackTransferObject(TRANSFER_TYPE transferObject);
	
}
