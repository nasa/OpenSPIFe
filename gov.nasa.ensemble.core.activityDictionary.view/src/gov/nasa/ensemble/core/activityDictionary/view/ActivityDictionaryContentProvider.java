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
package gov.nasa.ensemble.core.activityDictionary.view;

import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.INamedDefinition;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all the internal data structures required for maintaining
 * and displaying an activity dictionary tree.
 */
public class ActivityDictionaryContentProvider extends NamedDefinitionContentProvider {
	
	/**
	 * @return a list of all the activity definitions in the activity dictionary
	 */
	@Override
	protected List<? extends INamedDefinition> getNamedDefinitions() {
		List<EActivityDef> defs = new ArrayList<EActivityDef>();
		for (EActivityDef def : AD.getDefinitions(EActivityDef.class)) {
			if (!DictionaryUtil.isHidden(def)) {
				defs.add(def);
			}
		}
		return defs;
	}

	@Override
	protected String getCategory(INamedDefinition def) {
		return ((EActivityDef)def).getCategory();
	}

}
