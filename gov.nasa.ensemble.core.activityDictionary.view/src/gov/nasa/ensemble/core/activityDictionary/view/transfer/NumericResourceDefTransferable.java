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
package gov.nasa.ensemble.core.activityDictionary.view.transfer;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.resources.IndexedResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;

import java.util.Collection;

public class NumericResourceDefTransferable extends NamedDefTransferable<ENumericResourceDef> {

	public NumericResourceDefTransferable() {
		super(ENumericResourceDef.class);
	}
	
	public NumericResourceDefTransferable(Collection<ENumericResourceDef> defs) {
		super(ENumericResourceDef.class, defs);
	}

	@Override
	protected ENumericResourceDef getNamedDefinition(String name) {
		ActivityDictionary ad = ActivityDictionary.getInstance();
		ENumericResourceDef nrd = ad.getDefinition(ENumericResourceDef.class, name);
		if (nrd == null) {
			nrd = IndexedResourceDef.decode(name);
		}
		return nrd;
	}
	
}
