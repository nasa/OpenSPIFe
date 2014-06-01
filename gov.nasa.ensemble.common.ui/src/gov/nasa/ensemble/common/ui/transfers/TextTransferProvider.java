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

import java.util.Iterator;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * @author Andrew
 *
 */
public class TextTransferProvider implements ITransferProvider<String> {

	@Override
	public Transfer getTransfer() {
		return TextTransfer.getInstance();
	}

	@Override
	public boolean canPack(ITransferable transferable) {
		return true;
	}

	@Override
	public String packTransferObject(ITransferable transferable) {
		if (transferable == null) {
			return "null";
		}
		String string;
		if (transferable instanceof Iterable) {
			Iterable<?> iterable = (Iterable<?>) transferable;
			Iterator<?> iterator = iterable.iterator();
			string = "[ ";
			while (iterator.hasNext()) {
				string += iterator.next();
				if (iterator.hasNext()) {
					string += ", ";
				}
			}
			string += " ]";
		} else {
			string = String.valueOf(transferable);
		}
		return string;
	}
	
	@Override
	public ITransferable unpackTransferObject(String clipboardObject) {
		return null;
	}
	
}
