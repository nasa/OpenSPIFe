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
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.jscience.Profile;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;

public class XMLProfileParser extends ProfilesParser implements MissionExtendable {
	protected static final String PARSER_NAME = "XML";
	private boolean parsingDataPoints = true;
	
	public static XMLProfileParser getInstance() {
		try {
			return MissionExtender.construct(XMLProfileParser.class);
		} catch (ConstructionException e) {
			LogUtil.error(e);
		}
		return new XMLProfileParser();
	}
	
	public void setParsingDataPoints(boolean parsingDataPoints) {
		this.parsingDataPoints = parsingDataPoints;
	}

	@Override
	public int numberParsable(URI uri, InputStream inputStream) {
		BufferedInputStream bis = null;
		XMLProfileHandler xmlProfileHandler = new XMLProfileHandler();
		try {
			bis = new BufferedInputStream(inputStream);
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			xmlProfileHandler.setTestOnly(true);
			parser.parse(inputStream, xmlProfileHandler);
		} catch (Exception ex) {
			return ProfilesParser.UNPARSABLE;
		} finally {
			IOUtils.closeQuietly(bis);
		}
		return xmlProfileHandler.getProfileCount();
	}

	@Override
	protected void doParse(URI uri, InputStream inputStream) {
		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			XMLProfileHandler xmlProfileHandler = new XMLProfileHandler();
			xmlProfileHandler.setParsingDataPoints(parsingDataPoints);
			parser.parse(inputStream, xmlProfileHandler);
			for(Profile profile : xmlProfileHandler.getProfiles())
				addProfile(uri, profile);
		} catch (Exception e) {
			errorHandler.unhandledException(e);
		}
	}
	
	protected void addProfile(URI uri, Profile profile) {
		profiles.add(profile);
	}
}
