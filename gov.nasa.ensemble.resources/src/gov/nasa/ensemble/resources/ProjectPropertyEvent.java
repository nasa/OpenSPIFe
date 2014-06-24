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

import org.eclipse.core.resources.IResource;

import fj.data.Option;

public class ProjectPropertyEvent {
	public final IResource resource;
	public final String key;
	public final Option<String> value;

	public ProjectPropertyEvent(final IResource resource, final String key, final Option<String> value) {
		this.resource = resource;
		this.key = key;
		this.value = value;
	}
}
