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

/**
 * An interface for callbacks related to retrieving objects over a network. Because network activity should never take place in a
 * display thread, callbacks implementing this interface may be used to handle the eventual retrieval of an object.
 * 
 * @param <T>
 *            the type of the object being retrieved.
 */
public interface IRetrievalCallback<T> {
	/**
	 * Invoked when an object has been successfully retrieved.
	 * 
	 * @param object
	 *            the object that was retrieved.
	 */
	public void retrieved(T object);
}
