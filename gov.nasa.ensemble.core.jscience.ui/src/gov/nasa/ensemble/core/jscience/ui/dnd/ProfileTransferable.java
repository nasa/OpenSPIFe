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
package gov.nasa.ensemble.core.jscience.ui.dnd;

import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.jscience.Profile;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class ProfileTransferable implements ITransferable, Externalizable {

	private Collection<Profile> profiles;
	private final List<String> uriFragments = new ArrayList<String>();
	
	public ProfileTransferable(Profile profile) {
		this(Collections.singletonList(profile));
	}
	
	public ProfileTransferable(Collection<Profile> profiles) {
		this.profiles = profiles;
		for (Profile profile : profiles) {
			URI uri = EcoreUtil.getURI(profile);
			uriFragments.add(uri.toString());
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		if (profiles == null) {
			out.writeInt(0);
		} else {
			out.writeInt(profiles.size());
			for (Profile profile : profiles) {
				URI uri = EcoreUtil.getURI(profile);
				out.writeObject(uri.toString());
			}
		}
	}
	
	public List<String> getUriFragments() {
		return uriFragments;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		uriFragments.clear();
		int numOfProfiles = in.readInt();
		Collection<Profile> nameDefs = new ArrayList<Profile>();
		for (int i = 0; i < numOfProfiles; i++) {
			uriFragments.add((String) in.readObject());
		}
		this.profiles = nameDefs;
	}

	@Override
	public void dispose() {
		if (profiles != null) {
			profiles.clear();
		}
		if (uriFragments != null) {
			uriFragments.clear();
		}
	}

}
