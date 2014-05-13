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
package gov.nasa.ensemble.core.detail.emf;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class PropertyDescriptorSorter implements MissionExtendable {

	private static final List<String> DETAIL_CATEGORIES_ORDER;
	private static final boolean DETAIL_HIDE_UNSORTED_CATEGORIES = EnsembleProperties.getBooleanPropertyValue("detail.category.hide.unsorted", false);
	
	static {
		DETAIL_CATEGORIES_ORDER = EnsembleProperties.getStringListPropertyValue("detail.category.sort");
		if (DETAIL_CATEGORIES_ORDER != null) {
			DETAIL_CATEGORIES_ORDER.add(0, EMFDetailUtils.UNCATEGORIZED);
		}
	}

	public void sort(EObject object, LinkedHashMap<String, List<IItemPropertyDescriptor>> map) {
		//sort categories
		if (DETAIL_CATEGORIES_ORDER != null) {
			Map<String, List<IItemPropertyDescriptor>> oldMap = (LinkedHashMap<String, List<IItemPropertyDescriptor>>) map.clone();
			map.clear();
			for (String category : DETAIL_CATEGORIES_ORDER) {
				if (oldMap.containsKey(category)) {
					List<IItemPropertyDescriptor> pds = oldMap.remove(category);
					if (pds != null && !pds.isEmpty()) {
						map.put(category, pds);
					}
				}
			}
			/* if flag 'detail.category.append.unsorted' is set append all other categories
				they will be hidden otherwise */
			if (!DETAIL_HIDE_UNSORTED_CATEGORIES) {
				for (Entry<String, List<IItemPropertyDescriptor>> unsortedEntry : oldMap.entrySet()) {
					map.put(unsortedEntry.getKey(), unsortedEntry.getValue());
				}
			}
		}
	}
	
	public String getCategory(EObject object, IItemPropertyDescriptor pd) {
		return pd.getCategory(object);
	}
	
}
