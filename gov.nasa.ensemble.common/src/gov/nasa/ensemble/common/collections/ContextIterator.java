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
package gov.nasa.ensemble.common.collections;

import java.util.Iterator;

public class ContextIterator<T> implements Iterator<T> {
	private enum IteratorState {
		FINAL,    /* Current element is final element back.hasNext() was false */
		HAS_NEXT, /* Current element is not final element back.hasNext() was true */
		NORMAL    /* Next element has not yet been retrieved back.hasNext() is authoritative */
	}
	private T prev = null;
	private T current = null;
	private T next = null;
	
	private IteratorState state = IteratorState.NORMAL;
	
	private Iterator<T> back;
	
	public ContextIterator(Iterator<T> back) {
		this.back = back;
	}
	
	public boolean hasPrevious() {
		return prev != null;
	}
	
	@Override
	public boolean hasNext() {
		switch (state) {
		case FINAL:
			return false;
		case HAS_NEXT:
			return true;
		case NORMAL: default:
			return back.hasNext();
		}
	}
	
	public T previous() {
		return prev;
	}
	
	public T current() {
		return current;
	}

	@Override
	public T next() {
		if (state == IteratorState.NORMAL && back.hasNext()) {
			next = back.next();
		}
		prev = current;
		current = next;
		next = null;
		state = IteratorState.NORMAL;
		return current;
	}
	
	public T peek() {
		if (state == IteratorState.NORMAL) {
			if (back.hasNext()) {
				next = back.next();
				state = IteratorState.HAS_NEXT;
			} else {
				next = null;
				state = IteratorState.FINAL;
			}
		}
		return next;
	}

	@Override
	public void remove() {
		if (state != IteratorState.NORMAL) {
			throw new UnsupportedOperationException("Cannot use ContextIterator to remove an item after peeking.");
		}
		back.remove();
	}
	
}
