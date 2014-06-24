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
package gov.nasa.ensemble.core.jscience.xml;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.resource.ProgressMonitorInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

public class XMLProfilesResource extends ResourceImpl {
	private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(new String[] {"condition", "conditions", "resources", "resource", "profile", "profiles"});
	private static Logger LOGGER = Logger.getLogger(XMLProfilesResource.class);
	private static Map<Object, Object> DEFAULT_SAVE_OPTIONS = createSaveOptions();

	public XMLProfilesResource() {
		super();
		initialize();
	}

	public XMLProfilesResource(URI uri) {
		super(uri);
		initialize();
	}

	private void initialize() {
		setTrackingModification(true);
		this.isModified = false;
		defaultSaveOptions = DEFAULT_SAVE_OPTIONS;
	}

	private static Map<Object, Object> createSaveOptions() {
		Map<Object,Object> map = new LinkedHashMap<Object,Object>();
		map.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
		return Collections.unmodifiableMap(map);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doLoad(InputStream inputStream, Map<?, ?> options) {
		String extension = getURI().fileExtension();
		if (!SUPPORTED_EXTENSIONS.contains(extension.toLowerCase())) {
			LOGGER.warn("\"" + extension + "\" is not a standard extension for a ProfileResource.");
		}
		ProfilesParser parser = XMLProfileParser.getInstance();
		inputStream = ProgressMonitorInputStream.wrapInputStream(this, inputStream, options);
		StopWatch sw = new StopWatch();
		sw.start();
		try {
			parser.parse(getURI(), inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		sw.stop();
		LOGGER.debug("Loaded profiles from " + getURI() + " in " + sw.toString());
		List<Profile> profiles = parser.getProfiles();
		if (profiles != null) {
			getContents().addAll(profiles);				
		}
		setModified(false);
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		if (isModified()
				|| !URIConverter.INSTANCE.exists(getURI(), null)) {
			super.save(options);
		}
	}
	
	@Override
	public void doSave(OutputStream outputStream, Map<?, ?> options) {
		BufferedOutputStream bos = null;
		OutputStreamWriter osw = null;
		PrintWriter pw = null;
		ProfileWriter writer = null;
		String extension = getURI().fileExtension();
		if (!SUPPORTED_EXTENSIONS.contains(extension.toLowerCase())) {
			LOGGER.warn("\"" + extension + "\" is not a standard extension for a ProfileResource.");
		}
		try {
			bos = new BufferedOutputStream(outputStream);
			osw = new OutputStreamWriter(bos, "UTF-8");
			pw = new PrintWriter(osw);
			writer = new XMLProfileWriter(pw);
			List<Profile> profiles = new ArrayList<Profile>(getContents().size());
			for (EObject obj : getContents()) {
				if (obj instanceof Profile) {
					profiles.add((Profile) obj);
				}
			}
			writer.writeProfiles(profiles, null, null);
			this.isModified = false;
		} catch (UnsupportedEncodingException e) {
			LogUtil.error(e);
		} finally {
			IOUtils.closeQuietly(pw);
			IOUtils.closeQuietly(osw);
			IOUtils.closeQuietly(bos);
		}
	}
	
	public List<Profile> getProfiles() {
		List<Profile> toRet = new ArrayList<Profile>(getContents().size());
		for (EObject obj : getContents()) {
			if (obj instanceof Profile)
				toRet.add((Profile) obj);
		}
		return toRet;
	}
}
