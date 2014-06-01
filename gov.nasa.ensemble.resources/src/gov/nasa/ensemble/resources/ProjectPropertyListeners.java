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
package gov.nasa.ensemble.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProjectPropertyListeners {

	private final ProjectProperties props;
	private final Set<String> keys;

	public ProjectPropertyListeners(final ProjectProperties props, final String... keys) {
		this.props = props;
		this.keys = new HashSet<String>(Arrays.asList(keys));
	}

	public void add(final IProjectPropertyListener listener) {
		props.addListener(new ListenerWrapper(listener));
	}

	public void remove(final IProjectPropertyListener listener) {
		props.removeListener(new ListenerWrapper(listener));
	}
	
	private class ListenerWrapper implements IProjectPropertyListener {
		private IProjectPropertyListener listener;

		private ListenerWrapper(final IProjectPropertyListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void propertyChanged(final ProjectPropertyEvent event) {
			if (keys.contains(event.key))
				listener.propertyChanged(event);
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof ListenerWrapper && 
				listener.equals(((ListenerWrapper)obj).listener);
		}
		
		@Override
		public int hashCode() {
			return listener.hashCode();
		}
	}
}
