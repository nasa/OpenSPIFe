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
package gov.nasa.ensemble.core.jscience.csvxml;

import gov.nasa.ensemble.core.jscience.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

public class XMLCSVProfilesResource extends ResourceImpl {

	public XMLCSVProfilesResource(URI uri) {
		super(uri);
	}

	@Override
	public void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		ProfileDumper profileDumper = new ProfileDumper(outputStream);
		try {
			List<Profile> profiles = new ArrayList<Profile>();
			for (EObject object : getContents()) {
				if (object instanceof Profile) {
					profiles.add((Profile)object);
				}
			}
			profileDumper.writeProfiles(profiles);
		} catch (ParserConfigurationException e) {
			throw new IOException("failed to write profiles", e);
		}
	}

	@Override
	public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		ProfileLoader profileLoader = new ProfileLoader(inputStream);
		try {
			Collection<ProfileWithLazyDatapointsFromCsv<?>> profiles = profileLoader.readProfiles();
			profileLoader.ensureDataLoaded(); // this should be optional, and happen lazily
			getContents().addAll(profiles);
		} catch (ProfileLoadingException e) {
			throw new IOException("failed to load profiles", e);
		}
	}
	
}
