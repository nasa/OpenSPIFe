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
package gov.nasa.ensemble.common.ui.transfers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.Transfer;

public abstract class SimpleByteArrayTransferProvider implements ITransferProvider<byte[]> {

	private final Logger trace = Logger.getLogger(getClass());

	protected static Transfer createSimpleByteArrayTransfer(String name) {
		return new SimpleByteArrayTransfer(name);
	}
	
	public SimpleByteArrayTransferProvider() {
		super();
	}

	/**
	 * Converts a transferable map into a byte array 
	 * @param transferable
	 * @return the byte array
	 */
	@Override
	public byte[] packTransferObject(ITransferable transferable) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(transferable);
			oos.flush();
			oos.close();
			bos.close();
			return bos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("packTransferObject", e);
		}
	}

	/**
	 * Converts a byte array into a transferable map
	 * @param byteArray
	 * @return the map
	 */
	@Override
	public ITransferable unpackTransferObject(byte[] byteArray) {
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
			ois = new ObjectInputStream(bis);
			Object object = ois.readObject();
			if (object instanceof ITransferable) {
				return (ITransferable)object;
			}
			trace.error("unpacked a non-transferable?");
		} catch (ClassNotFoundException e) {
			trace.error("may need Eclipse-RegisterBuddy: gov.nasa.ensemble.common.ui", e);
			// see also http://help.eclipse.org/help31/topic/org.eclipse.platform.doc.isv/reference/misc/bundle_manifest.html
		} catch (EOFException eof) {
			trace.error("unpackTransferObject", eof);
		} catch (IOException e) {
			trace.error("unpackTransferObject", e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					trace.error("unpackTransferObject", e);
				}
			}
		}
		return null;
	}

}
