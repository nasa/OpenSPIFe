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
package gov.nasa.ensemble.common.ui.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * A subclass of ByteArrayTransfer that transfers generic serializable objects.
 * 
 * @see org.eclipse.swt.dnd.ByteArrayTransfer
 */
public abstract class SerializableTransfer extends ByteArrayTransfer {
	
	protected static final Logger trace = Logger.getLogger(SerializableTransfer.class);
	
	private String typeName;
	private int typeId;
	
	/**
	 * Construct a SerializableTransfer and register the typeName with the
	 * system as a data type.
	 * 
	 * @param typeName
	 *            the typeName to register as a data type
	 */
	protected SerializableTransfer(String typeName) {
		this.typeName = typeName;
		typeId = registerType(typeName);
	}

	/**
	 * @return the platform specfic names of the data types that can be
	 *         converted using this transfer agent.
	 */
	@Override
	protected String[] getTypeNames() {
		return new String[]{typeName};
	}

	/**
	 * @return the platform specfic ids of the data types that can be converted
	 *         using this transfer agent
	 */
	@Override
	protected int[] getTypeIds(){
		return new int[] {typeId};
	}

	/**
	 * @param types
	 * @return true if any of TransferData types are supported
	 */
	public boolean isSupportedType(TransferData[] types) {
		for (TransferData type: types) {
			if (isSupportedType(type)) return true;
		}
		return false;
	}
	
	/**
	 * This implementation of <code>javaToNative</code> converts a java 
	 * <code>byte[]</code> to a platform specific representation.  For additional
	 * information see <code>Transfer#javaToNative</code>.
	 * 
	 * @see Transfer#javaToNative
	 * 
	 * @param object a java <code>byte[]</code> containing the data to be converted
	 * @param transferData an empty <code>TransferData</code> object; this
	 *  object will be filled in on return with the platform specific format of the data
	 */	
	@Override
	public void javaToNative (Object object, TransferData transferData) {
		if (object == null || !(object instanceof Serializable[])) return;
		
		if (isSupportedType(transferData)) {
			Serializable[] activities = (Serializable[]) object;
			
			// write data to a byte array and then ask super to convert to a
			// platform specific representation
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(activities);
				oos.flush();
				oos.close();
				bos.close();
				super.javaToNative(bos.toByteArray(), transferData);
			}
			catch (IOException ioe) {
				trace.error("Error serializing Serializable for drag and drop.",ioe);
			}
		}
	}
	
	@Override
	@SuppressWarnings("cast")
	public Object nativeToJava(TransferData transferData){	
		if (isSupportedType(transferData)) {
			byte[] bytes = (byte[])super.nativeToJava(transferData);
			if (bytes == null) return null;
			try {
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
				Object obj = ois.readObject();
				if (obj instanceof Serializable[]) {
					return obj;
				}
				trace.error("Expected Serializable[] object in drag and drop, but got a "+obj.getClass());
				return null;
			}
			catch (IOException ioe) {
				trace.error("Error deserializing Serializable[] during drag and drop.",ioe);
				return null;
			}
			catch (ClassNotFoundException cnfe) {
				trace.error("Error casting object to Serializable[] during drag and drop.",cnfe);
				return null;
			}
		}
		
		return null;
	}
	
}
