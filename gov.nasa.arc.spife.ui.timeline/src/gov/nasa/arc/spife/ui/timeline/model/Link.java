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
package gov.nasa.arc.spife.ui.timeline.model;

public class Link {

	private final Object source;
	private final Object target;
	
	public Link(Object source, Object target) {
		super();
		this.source = source;
		this.target = target;
	}

	public Object getSource() {
		return source;
	}

	public Object getTarget() {
		return target;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Link)) return false;
		
		Link l = (Link) obj;
		return this.getSource().equals(l.getSource())
				&& this.getTarget().equals(l.getTarget());
	}

	@Override
	public int hashCode() {
		return getSource().hashCode() ^ getTarget().hashCode();
	}
	
}
