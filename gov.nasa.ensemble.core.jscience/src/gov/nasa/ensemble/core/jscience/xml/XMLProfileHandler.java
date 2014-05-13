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

import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLProfileHandler extends PathHandler {
	protected static final String DATATYPE_ATTRIBUTE = ProfileUtil.DATATYPE_ATTRIBUTE;
	protected static final String ID_ATTRIBUTE = ProfileUtil.ID_ATTRIBUTE;
	protected static final String NAME_ATTRIBUTE = ProfileUtil.NAME_ATTRIBUTE;
	protected static final String CATEGORY_ATTRIBUTE = ProfileUtil.CATEGORY_ATTRIBUTE;
	protected static final String UNITS_ATTRIBUTE = ProfileUtil.UNITS_ATTRIBUTE;
	protected static final String EXTERNAL_CONDITION_ATTRIBUTE = ProfileUtil.EXTERNAL_CONDITION_ATTRIBUTE;
	
	protected static final String PROFILE = "profile";
	protected static final String PROFILES = "profiles";
	protected static final String CONDITION = "condition";
	protected static final String CHANGE = "change";
	protected static final String START = "start";
	protected static final String END = "end";
	protected static final String VALUE = "value";
	protected static final String DATAPOINT = "datapoint";
	protected static final String DATE = "date";

	private List<String> contentTags = Arrays.asList(new String[] {CONDITION, PROFILE});
	private String rootPrefix;
	private List<Profile> profiles = new ArrayList<Profile>();

	private ProfileHandler handler;
	private EDataType datatype;
	private int profileCount = 0;

	public boolean testOnly = false;
	private boolean parsingDataPoints = true;
	private final ISO8601DateFormat iso8601DateFormat = new ISO8601DateFormat();
	
	public XMLProfileHandler() {
		rootPrefix = "/";	
	}

	public XMLProfileHandler(String rootPrefix) {
		this();
		this.rootPrefix = rootPrefix;	
	}
	

	public void setTestOnly(boolean testOnly) {
		this.testOnly = testOnly;
	}

	public int getProfileCount() {
		return profileCount;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (!getPath().startsWith(rootPrefix))
			return;
		
		if (contentTags.contains(qName)) {
			if (!testOnly) {
				handler = new ProfileHandler();
				for (int i = 0; i < attributes.getLength(); ++i) {
					String xmlAttributeName = attributes.getQName(i).toLowerCase();
					String xmlAttributeValue = attributes.getValue(i);
					if (ID_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setId(xmlAttributeValue);
					} else if (NAME_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setName(xmlAttributeValue);
					} else if (CATEGORY_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setCategory(xmlAttributeValue);
					} else if (UNITS_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setUnits(xmlAttributeValue);
					} else if (DATATYPE_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setDataType(xmlAttributeValue);
					} else if (EXTERNAL_CONDITION_ATTRIBUTE.equals(xmlAttributeName)) {
						handler.setExternalCondition(Boolean.parseBoolean(xmlAttributeValue));
					}
					handler.setAttribute(xmlAttributeName, xmlAttributeValue);
				}
			}
			datatype = EcorePackage.Literals.ESTRING;
			for (int i = 0; i < attributes.getLength(); ++i) {
				if (attributes.getQName(i).toLowerCase().equals(DATATYPE_ATTRIBUTE)) {
					EDataType newDataType = EMFUtils.createEDataTypeFromString(attributes.getValue(i));
					if (newDataType != null)
						datatype = newDataType;
				}
			}
		} else if(isParsingDataPoints()) {
			if (qName.equals(CHANGE)) {
				String startText = attributes.getValue(START);
				String endText = attributes.getValue(END);
				Date start = iso8601DateFormat.parse(startText, new ParsePosition(0));
				Date end = iso8601DateFormat.parse(endText, new ParsePosition(0));
				Object value = null;
				String valueString = attributes.getValue(VALUE);
				if (valueString != null)
					value = ProfileUtil.convertToType(datatype, valueString);

				if (!testOnly) {
					handler.addDataPoint(start, value);
					handler.addDataPoint(end, null);
				}
			} else {
				if (qName.equals(DATAPOINT)) {
					Date time = null;
					String timeText = attributes.getValue(DATE);
					if (timeText != null)
						time = iso8601DateFormat.parse(timeText, new ParsePosition(0));
					Object value = null;
					String valueString = attributes.getValue(VALUE);
					if (valueString != null)
						value = ProfileUtil.convertToType(datatype, valueString);

					if (!testOnly && time != null) {
						handler.addDataPoint(time, value);
					}
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (getPath().startsWith(rootPrefix) && contentTags.contains(qName)) {
			if (!testOnly)
				profiles.add(handler.getProfile());
			++profileCount;
		}
		super.endElement(uri, localName, qName);
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setParsingDataPoints(boolean parsingDataPoints) {
		this.parsingDataPoints = parsingDataPoints;
	}

	public boolean isParsingDataPoints() {
		return parsingDataPoints;
	}
}
