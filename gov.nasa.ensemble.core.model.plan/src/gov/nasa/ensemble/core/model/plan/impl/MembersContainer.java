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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.core.model.plan.EMember;

import java.util.Collection;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;

public class MembersContainer extends EObjectContainmentWithInverseEList<EMember> {
	
	public MembersContainer(Class<?> dataClass, InternalEObject owner, int featureID, int inverseFeatureID) {
		super(dataClass, owner, featureID, inverseFeatureID);
	}
	
	@Override
	public void addUnique(EMember member) {
		boolean exists = replaceIfExists(member);
		if (!exists) {
			super.addUnique(member);
		}
	}

	@Override
	public void addUnique(int index, EMember member) {
		addUnique(member);
	}
	
	@Override
	public boolean addAllUnique(int index, Collection<? extends EMember> collection) {
		for (EMember member : collection) {
			addUnique(member);
		}
		return true;
	}
	
	private boolean replaceIfExists(EMember newMember) {
		boolean exists = false;
		String key = newMember.getKey();
		for (int i = 0 ; i < this.size() ; i++) {
			EMember existingMember = get(i);
			if (existingMember.getKey() == key) {
				set(i, newMember);
				exists = true;
				break;
			}
		}
		return exists;
	}
	
}
