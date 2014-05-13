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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.Unit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ProfileConfig {
	private static final String DEFAULT_CONFIG_URL = "platform:/plugin/gov.nasa.ensemble.ngps/datafiles/profiles.xml";
	public static final ProfileConfig INSTANCE = new ProfileConfig();

	private static final String ATTR_CATEGORY = "category";
	private static final String ATTR_DATATYPE = "datatype";
	private static final String ATTR_ID = "id";
	private static final String ATTR_INTERPOLATION = "interpolation";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_UNITS = "units";
	private static final String ATTR_VALUE = "value";
	private static final String ATTR_EXTERNAL = "externalCondition";
	
	private Map<String, ProfileTemplate> profileTemplates = new HashMap<String, ProfileTemplate>();
	
	protected ProfileConfig() {
		String configFilePlatformPath = EnsembleProperties.getProperty("profiles.config", DEFAULT_CONFIG_URL);
		File toLoad = null;
		try {
			URL configFileURL = FileLocator.toFileURL(new URL(configFilePlatformPath));
			toLoad = new File(configFileURL.getFile());
		} catch (Exception e) {
			LogUtil.error(e);
		}

		if (toLoad != null) {
			loadConfig(toLoad);
		}
	}
	
	protected void loadConfig(File configFile) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document configDoc = docBuilder.parse(configFile);
			Element root = configDoc.getDocumentElement();

			NodeList profileElements = root.getElementsByTagName("profile");
			for (int i = 0; i < profileElements.getLength(); ++i) {
				Element profileElement = (Element) profileElements.item(i);

				String id = profileElement.getAttribute(ATTR_ID); 
				String name = profileElement.getAttribute(ATTR_NAME);
				String category = profileElement.getAttribute(ATTR_CATEGORY);
				String dataTypeString = EMFUtils.convertToString(EcorePackage.Literals.EBOOLEAN_OBJECT);
				if (profileElement.hasAttribute(ATTR_DATATYPE)) {
					dataTypeString = profileElement.getAttribute(ATTR_DATATYPE);
				}
				Unit units = Unit.ONE;
				if (profileElement.hasAttribute(ATTR_UNITS)) {
					units = EnsembleUnitFormat.INSTANCE.parse(profileElement.getAttribute(ATTR_UNITS));
				}
				
				INTERPOLATION interpolation = INTERPOLATION.STEP;
				if (profileElement.hasAttribute(ATTR_INTERPOLATION)) {
					interpolation = INTERPOLATION.get(profileElement.getAttribute(ATTR_INTERPOLATION));
				}
				
				boolean externalCondition = true;
				if (profileElement.hasAttribute(ATTR_EXTERNAL)) {
					externalCondition = Boolean.parseBoolean(profileElement.getAttribute(ATTR_EXTERNAL));
				}
				
				ProfileTemplate profileTemplate = new ProfileTemplate(id, name, category, dataTypeString, units, interpolation, externalCondition);

				NodeList attributeElements = profileElement.getElementsByTagName("attribute");
				for (int j = 0; j < attributeElements.getLength(); ++j) {
					Element attributeElement = (Element) attributeElements.item(j);
					String attributeName = attributeElement.getAttribute(ATTR_NAME);
					String attributeValue = attributeElement.getAttribute(ATTR_VALUE);
					profileTemplate.addAttribute(attributeName, attributeValue);
				}

				profileTemplates.put(id, profileTemplate);
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}

	public String getId(String profileName) {
		if (profileName == null || profileName.equals(""))
			throw new NullPointerException("Cannot convert null or empty string into profile ID.");
		String source = profileName.toUpperCase();
		StringBuffer result = new StringBuffer(source.length());
		boolean underscoreLast = false;
		for (int i = 0; i < source.length(); ++i) {
			char c = source.charAt(i);
			if (Character.isLetterOrDigit(c)) {
				if (Character.getNumericValue(c) >= 0) {
					result.append(c);
				} else {
					result.append("x" + Integer.toHexString(c));
				}
				underscoreLast = false;
			}
			else if (!underscoreLast) {
				result.append("_");
				underscoreLast = true;
			}
		}
		if (result.length() == 1 && result.charAt(0) == '_')
			return "_";
		int start = result.charAt(0) == '_' ? 1 : 0;
		int end = result.charAt(result.length() - 1) == '_' ? result.length() - 1 : result.length();
		return result.substring(start, end);
	}
	
	public EDataType getDataType(String profileId) {
		ProfileTemplate template = profileTemplates.get(profileId);
		if (template != null)
			return template.getDataType();
		return null;
	}
	
	public ProfileTemplate getTemplate(String profileId) {
		return profileTemplates.get(profileId);
	}
	
	public Profile createProfile(String profileId) {
		if (profileTemplates.containsKey(profileId)) {
			return profileTemplates.get(profileId).createInstance();
		}
		return null;
	}
}
