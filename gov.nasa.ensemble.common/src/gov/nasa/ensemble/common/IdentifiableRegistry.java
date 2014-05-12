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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.ref.WeakReference;
import java.rmi.dgc.VMID;
import java.util.HashMap;
import java.util.Map;

public class IdentifiableRegistry<I> {
	
	private Map<String, WeakReference<I>> uniqueIdToIdentifiable = new HashMap<String, WeakReference<I>>();
	private Map<I, String> identifiableToUniqueId = new HashMap<I, String>();
	
	/**
	 * Generates a unique id.  Can be used for any purpose.  Isn't registered, so
	 * items can't be retrieved programmatically.  Uses VMID, and lucky 8.
	 * For compatibility with APGEN, use only letters,digits, and "_".
	 * APGEN also requires the id to end with _ and at least one digit. (so, 8)
	 * @return the unique id, e.g. "ID_eizh2yvu_8egwa_8"
	 */
	public static String generateUniqueId() {
		// MAE-2182 Use VMID to generate the unique ID
		String string = new VMID().toString();
		return "ID_" + string.replace(':','_').replace('-','_') + "_8";
	}

	/**
	 * Generates a unique id and registers the object on this identifiable registry.
	 * @return the unique id, e.g. "ID_eizh2yvu_8egwa_8"
	 */
	public synchronized String generateUniqueId(I object) {
		String uniqueId = generateUniqueId();
	    registerIdentifiable(object, uniqueId);
    	return uniqueId;
	}

	/**
	 * Generates a unique id if necessary, otherwise returns the existing id
	 */
	public synchronized String getOrRegister(I object) {
		String uniqueId = identifiableToUniqueId.get(object);
		if (uniqueId == null) {
			return generateUniqueId(object);
		}
		return uniqueId;
	}
	
	/**
	 * This method is used for registering an identifiable object that
	 * has an unique id already.  A typical case of using this
	 * function is to register an identifiable that has been persisted
	 * to a database or filesystem and then restored later.
	 * 
	 * @param object
	 * @param uniqueId
	 */
	public synchronized void registerIdentifiable(I object, String uniqueId) {
		if (uniqueId == null) {
			LogUtil.warn("null id for register identifiable: generating new id");
			generateUniqueId(object); // will also register it
			return;
		}
		WeakReference<I> reference = uniqueIdToIdentifiable.get(uniqueId);
		if (reference != null) {
			I identifiable = reference.get();
			if (identifiable != null && object != identifiable) {
				LogUtil.warn("registerIdentifiable collision on " + uniqueId);
				return;
			}
		}
		String oldUniqueId = identifiableToUniqueId.get(object);
		if (oldUniqueId != null && !oldUniqueId.equals(uniqueId)) {
			LogUtil.warn("object already registered as " + oldUniqueId);
		}
		uniqueIdToIdentifiable.put(uniqueId, new WeakReference<I>(object));
		identifiableToUniqueId.put(object, uniqueId);
	}

	/**
	 * Return the unique id for this object
	 * 
	 * @param object
	 * @return the unique id for this object
	 */
	public synchronized String getUniqueId(I object) {
		if (object == null) {
			return null;
		}
		return identifiableToUniqueId.get(object);
	}
	
	/**
	 * This method retrieves an identifiable of the given class. The Identifiable should either have been registered explicitly
	 * through registerIdentifiable or implicitly through a call to generateUniqueId.
	 * 
	 * This method returns null if there is no object registered with the uniqueId. If the object registered with the uniqueId is
	 * not of the appropriate type, a ClassCastException will be thrown.
	 * 
	 * @param <T>
	 * @param klass
	 * @param uniqueId
	 */
	public synchronized <T extends I> T getIdentifiable(Class<T> klass, String uniqueId) {
		WeakReference<I> reference = uniqueIdToIdentifiable.get(uniqueId);
		if (reference == null) {
			return null;
		}
		I identifiable = reference.get();
		if (identifiable == null) {
			return null;
		}
		return klass.cast(identifiable);
	}

	/**
	 * This method removes the identifiable object from the registry.
	 * It should be called when the identifiable is no longer needed.
	 * For example, when the document is closed, its identifiables
	 * could be released.  It is safe to call this multiple times
	 * for the same identifiable.
	 * 
	 * @param object
	 */
	public synchronized void releaseIdentifiable(I object) {
		String uniqueId = getUniqueId(object);
		uniqueIdToIdentifiable.remove(uniqueId);
		identifiableToUniqueId.remove(object);
	}
	
	public synchronized void report() {
		LogUtil.debug("uniqueIdToIdentifiable.size() = " + uniqueIdToIdentifiable.size());
		LogUtil.debug("identifiableToUniqueId.size() = " + identifiableToUniqueId.size());
	}
	
}
