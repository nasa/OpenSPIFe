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
package gov.nasa.ensemble.emf.patch;

import gov.nasa.ensemble.emf.model.patch.Patch;

import java.util.LinkedList;
import java.util.ListIterator;

public class PatchQueue {

	private LinkedList<Patch> queue = new LinkedList<Patch>();
	private boolean hasBeenUndone = false;
	
	public boolean add(Patch patch) {
		return queue.add(patch);
	}
	
	public void apply() throws PatchRollbackException {
		ListIterator<Patch> iterator = queue.listIterator();
		while (iterator.hasNext()) {
			Patch patch = iterator.next();
			patch.applyAndReverse();
		}
		hasBeenUndone = false;
	}
	
	public void undo() throws PatchRollbackException {
		if (hasBeenUndone) {
			throw new IllegalStateException("Attempt to undo twice.");
		}
		int size = queue.size();
		if (size > 0) {
			ListIterator<Patch> iterator = queue.listIterator(size-1);
			while (iterator.hasPrevious()) {
				Patch patch = iterator.previous();
				patch.applyAndReverse();
			}
		}
		hasBeenUndone = true;
	}
	
	public void clear() {
		queue.clear();
	}
}
