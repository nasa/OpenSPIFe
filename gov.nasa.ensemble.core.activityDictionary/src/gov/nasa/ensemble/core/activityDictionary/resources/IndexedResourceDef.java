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

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;

import java.util.List;

import org.apache.log4j.Logger;

public class IndexedResourceDef extends NumericResourceDef {

	private int index;
	private List<String> list;
	private IndexableResourceDef inner;

	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger(IndexedResourceDef.class);
	
	public IndexedResourceDef(IndexableResourceDef def, List<String> list, int index) {
		this.inner = def;
		this.list = list;
		this.index = index;
	}

	public IndexableResourceDef getParentDefinition() {
		return inner;
	}

	@Override
	public String getName() {
		String resourceName = inner.getName();
		String indexName = list.get(index);
		return resourceName+"[\""+indexName+"\"]";
	}
	
	public static IndexedResourceDef decode(String name) {
		int index = name.indexOf("[");
		if (index != -1) {
			ActivityDictionary ad = ActivityDictionary.getInstance();
			String resourceName = name.substring(0, index);
			String indexName = name.substring(index+2, name.length()-2);
			IndexableResourceDef def = ad.getDefinition(IndexableResourceDef.class, resourceName);
			if (def != null) {
				List<String> list = def.getIndices();
				if (list != null) {
					int count = list.size();
					for(int i=0; i<count; i++) {
						if (indexName.equals(list.get(i))) {
							return new IndexedResourceDef(def, list, i);
						}
					}
				}
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

}
