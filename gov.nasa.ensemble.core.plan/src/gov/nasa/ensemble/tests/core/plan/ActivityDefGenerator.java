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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ActivityDefGenerator implements Iterator<EActivityDef>, Iterable<EActivityDef> {
	private ActivityDictionary AD = ActivityDictionary.getInstance();
	private List<? extends EActivityDef> defs;
	private int defsSize;
	private int defCount;
	
	public ActivityDefGenerator() {
		defs = AD.getActivityDefs();
		if (defs.isEmpty()) {
			EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef("ActivityDefGeneratorDef", "Test");
			defs = Collections.singletonList(def);
		}				
		defsSize = defs.size();
		defCount = 0;
	}
	
	@Override
	public EActivityDef next() {
		EActivityDef candidate = defs.get(defCount++ % defsSize);
		boolean isSubActivity = DictionaryUtil.isHidden(candidate);
		if (isSubActivity) {
			return next();
		}
		return candidate;
	}

	@Override
	public boolean hasNext() {
		return (defCount < defsSize);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("can not remove an ActivityDef via ActivityDefGenerator");
	}
	
	@Override
	public Iterator<EActivityDef> iterator() {
		return this;
	}
	
}
