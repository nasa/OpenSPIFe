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
package gov.nasa.ensemble.core.plan.editor.transfers;

import gov.nasa.ensemble.common.ui.transfers.ITransferProvider;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;

import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.Transfer;

/**
 * @author Andrew
 *
 */
public class HTMLTransferProvider implements ITransferProvider<String> {

	@Override
	public Transfer getTransfer() {
		return HTMLTransfer.getInstance(); 
	}

	@Override
	public boolean canPack(ITransferable transferable) {
		// This transferable is disabled for now because it adds an unnecessary expense
		// to each copy.  The file is not deleted because it is a good example of how
		// to do an HTML transferable.
		// return transferable instanceof IPlanElementTransferable;
		return false;
	}

	@Override
	public String packTransferObject(ITransferable transferable) {
		if ((transferable == null) || !(transferable instanceof IPlanElementTransferable)) {
			return "null";
		}
		IPlanElementTransferable transferable2 = (IPlanElementTransferable) transferable;
		StringBuilder string = new StringBuilder("<p>");
		for (EPlanElement element : transferable2.getPlanElements()) {
			String className = element.getClass().getSimpleName();
			String elementName = element.getName();
			string.append(className + " " + elementName + "<br>");
//			for (Parameter parameter : element.getParameters()) {
//				String valueString = parameter.getValueString();
//				if (valueString != null) {
//					string.append("&nbsp;&nbsp;" + parameter.getName() + " = " + valueString + "<br>");
//				}
//			}
			string.append("<br>");
		}
		string.append("</p>");
		return string.toString();
	}

	@Override
	public ITransferable unpackTransferObject(String clipboardObject) {
		return null;
	}
	
}
