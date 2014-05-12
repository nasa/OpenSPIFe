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
package gov.nasa.ensemble.common.metadata;

import fj.F;
import fj.data.Option;
import gov.nasa.ensemble.common.functional.Lists;
import gov.nasa.ensemble.common.functional.Predicate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*package*/ class MetaMetaData {
	private final List<WeakReference> owners = Collections.synchronizedList(new ArrayList<WeakReference>());
	
	public List<Object> getOwners() {
		synchronized (owners) {
			owners.removeAll(Lists.filter(owners, new Predicate<WeakReference>() {
				@Override
				public boolean apply(WeakReference object) {
					return object.get() == null;
				}
			}));
			return Collections.unmodifiableList(
					Lists.mapOption(owners, new F<WeakReference, Option<Object>>() {
						@Override
						public Option<Object> f(WeakReference ref) {
							return Option.fromNull(ref.get());
						}
					}));
		}
	}
	
	public Object getOwner() {
		List referents = getOwners();
		return referents.isEmpty() ? null : referents.get(0);
	}
	
	void registerOwner(Object owner) {
		synchronized (owners) {
			for (Object existingOwner : getOwners())
				if (owner == existingOwner)
					return;
			owners.add(new WeakReference(owner));
		}
	}
}
