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
package gov.nasa.ensemble.core.activityDictionary.resources;

import java.util.List;

/**
 * Remodeled resources are those that take a considerable amount of time to
 * compute. These are often user initiated computations due to their heavyweight
 * nature.
 */
public class ModeledResourceDef extends NumericResourceDef implements IndexableResourceDef {

	private List<String> indices = null;
	private List<IndexedResourceDef> indexedResourceDefs = null;

	@Override
	public List<String> getIndices() {
		return indices;
	}

	@Override
	public void setIndices(List<String> indices) {
		this.indices = indices;
	}

	@Override
	public List<IndexedResourceDef> getIndexedResourceDefs() {
		return indexedResourceDefs;
	}

	@Override
	public void setIndexedResourceDefs(List<IndexedResourceDef> indexedResourceDefs) {
		this.indexedResourceDefs = indexedResourceDefs;
	}

}
