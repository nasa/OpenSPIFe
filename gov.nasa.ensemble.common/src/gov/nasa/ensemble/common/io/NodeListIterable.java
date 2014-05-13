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
package gov.nasa.ensemble.common.io;

import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A great class
 * @author rocks
 *
 */
public class NodeListIterable implements Iterable<Node> {
	private final NodeList nodeList;
	private final int length;
	public NodeListIterable(final NodeList nodeList) {
		this.nodeList = nodeList;
		this.length = nodeList.getLength();
	}
	@Override
	public Iterator<Node> iterator() {
		return new NodeListIterator();
	}
	private class NodeListIterator implements Iterator<Node> {
		private int index = 0;
		@Override
		public boolean hasNext() {
			return (index < length);
		}
		@Override
		public Node next() {
			return nodeList.item(index++);
		}
		@Override
		public void remove() {
			throw new UnsupportedOperationException("nice try");
		}
	}
}
