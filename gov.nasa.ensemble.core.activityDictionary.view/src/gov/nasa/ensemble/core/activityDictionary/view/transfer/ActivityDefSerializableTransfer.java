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
package gov.nasa.ensemble.core.activityDictionary.view.transfer;

import gov.nasa.ensemble.common.ui.dnd.SerializableTransfer;

/**
 * A subclass of SerializableTransfer that transfers ActivityDef objects.
 * 
 * @see org.eclipse.swt.dnd.ByteArrayTransfer
 * @see gov.nasa.ensemble.common.ui.dnd.SerializableTransfer
 */
public class ActivityDefSerializableTransfer extends SerializableTransfer {

	private static ActivityDefSerializableTransfer instance;

	/**
	 * Default constructor. Private because this constructor should only be
	 * invoked by the getInstance() method of this class.
	 */
	private ActivityDefSerializableTransfer() {
		super("ACTIVITYDEF_SERIALIZABLE_TRANSFER");
	}

	/**
	 * @return the singleton instance
	 */
	public static ActivityDefSerializableTransfer getInstance() { 
		if (instance == null) instance = new ActivityDefSerializableTransfer();
		return instance;
	}
	
}
