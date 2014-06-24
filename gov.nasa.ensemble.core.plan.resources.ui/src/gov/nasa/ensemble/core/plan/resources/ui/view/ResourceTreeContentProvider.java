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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.core.activityDictionary.resources.IndexableResourceDef;
import gov.nasa.ensemble.core.activityDictionary.resources.IndexedResourceDef;
import gov.nasa.ensemble.core.activityDictionary.view.NamedDefinitionContentProvider;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.ArrayList;
import java.util.List;


public class ResourceTreeContentProvider extends NamedDefinitionContentProvider {

	@Override
	protected String getCategory(INamedDefinition def) {
		if (def instanceof ENumericResourceDef) {
			return ((ENumericResourceDef)def).getCategory();
		} else if (def instanceof EStateResourceDef) {
			return ((EStateResourceDef)def).getCategory();
		}
		return null;
	}

	@Override
	protected List<? extends INamedDefinition> getNamedDefinitions() {
		List<INamedDefinition> defs = new ArrayList<INamedDefinition>();
		defs.addAll(AD.getDefinitions(ENumericResourceDef.class));
		defs.addAll(AD.getDefinitions(EStateResourceDef.class));
		return defs;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof IndexableResourceDef) {
			List<IndexedResourceDef> defs = ((IndexableResourceDef)parent).getIndexedResourceDefs();
			if (defs == null)
				return new IndexedResourceDef[0];
			else
				return defs.toArray(new IndexedResourceDef[defs.size()]);
		} // else...
		return super.getChildren(parent);
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IndexedResourceDef) {
			return ((IndexedResourceDef)element).getParentDefinition();
		} // else...
		return super.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IndexableResourceDef) {
			IndexableResourceDef def = (IndexableResourceDef)element;
			List<IndexedResourceDef> indexedResourceDefs = def.getIndexedResourceDefs();
			return indexedResourceDefs != null && indexedResourceDefs.size() > 0;
		} // else...
		return super.hasChildren(element);
	}

}
