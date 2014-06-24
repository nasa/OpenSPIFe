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

import org.eclipse.swt.dnd.ByteArrayTransfer;

public class SimpleByteArrayTransfer extends ByteArrayTransfer {

	private final String[] typeNames;
	private final int[] typeIds;

	public SimpleByteArrayTransfer(String typeName) {
		this.typeNames = new String[] { typeName };
		this.typeIds = new int[] { registerType(typeName) };
	}
	
	@Override
	protected int[] getTypeIds() {
		return typeIds;
	}

	@Override
	protected String[] getTypeNames() {
		return typeNames;
	}
	
	@Override
	public String toString() {
		return "SimpleByteArrayTransfer['" + typeNames[0] + "']";		
	}
	
}
