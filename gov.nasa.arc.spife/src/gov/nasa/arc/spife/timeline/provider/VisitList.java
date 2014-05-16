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
package gov.nasa.arc.spife.timeline.provider;

import java.util.ArrayList;

/**
 * Modeled on ChangeNotifier.
 * This allows adding and removing items while iterating over the list, 
 * even in the same thread.
 * 
 * @author abachman
 * @param <T>
 */
public final class VisitList<T> extends ArrayList<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public interface Visitor<T> {
		public void visit(T item);
	}
	
	@SuppressWarnings("unchecked")
	public void visitAll(Visitor<T> visitor) {
		int expectedModCount = modCount;
		T[] array = (T[])toArray();
		for (int i = 0 ; i < array.length ; ++i) {
			T item = array[i];
			if (expectedModCount == modCount || contains(item)) {
				visitor.visit(item);
			}
		}
	}
	
}
