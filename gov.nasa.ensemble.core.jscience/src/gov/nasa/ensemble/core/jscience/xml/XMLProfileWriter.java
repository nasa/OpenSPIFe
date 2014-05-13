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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.io.XMLUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jscience.physics.amount.Amount;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLProfileWriter implements ProfileWriter {
	private Writer writer;
	private Document document;
	private Element rootElement;

	public XMLProfileWriter(PrintWriter pw) {
		writer = pw;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			LogUtil.error(e);
		}
	}

	public void writeProfile(Profile profile, Date start, Date end) {
		writeProfile(document, rootElement, profile, start, end);
	}
	
	public static void writeProfile(Document document, Element parent, Profile profile, Date start, Date end) {
		Map<String, String> attributes = profile.getAttributes().map(); 

		Element profileElement = document.createElement(XMLProfileHandler.PROFILE);
		parent.appendChild(profileElement);
		
		Unit units = profile.getUnits();
		if (units != null  && units != Unit.ONE) {
			String formattedUnits = EnsembleUnitFormat.INSTANCE.format(units);
			profileElement.setAttribute(XMLProfileHandler.UNITS_ATTRIBUTE, formattedUnits);
		}
		profileElement.setAttribute(XMLProfileHandler.NAME_ATTRIBUTE, profile.getName());
		profileElement.setAttribute(XMLProfileHandler.ID_ATTRIBUTE, profile.getId());
		profileElement.setAttribute(XMLProfileHandler.CATEGORY_ATTRIBUTE, profile.getCategory());
		profileElement.setAttribute(XMLProfileHandler.EXTERNAL_CONDITION_ATTRIBUTE, String.valueOf(profile.isExternalCondition()));
		EDataType dataType = profile.getDataType();
		if (dataType != null) {
			profileElement.setAttribute(XMLProfileHandler.DATATYPE_ATTRIBUTE, EMFUtils.convertToString(dataType));
		}
		List<String> keys = new ArrayList(attributes.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			String value = attributes.get(key);
			if (value != null) {
				profileElement.setAttribute(key.toLowerCase().replaceAll("[^a-z0-9_-]", "-"), value);
			}
		}
		EList dataPoints = profile.getDataPoints();
		if (!dataPoints.isEmpty() && dataType == null) {
			LogUtil.error("no data type defined for data points in profile with id: " + profile.getId());
		}
		for (Object o : dataPoints) {
			DataPoint dp = (DataPoint) o;
			Date date = dp.getDate();
			if (date == null || (start != null && date.before(start)) || (end != null && date.after(end)))
				continue;
			Element datapointElement = document.createElement(XMLProfileHandler.DATAPOINT);
			profileElement.appendChild(datapointElement);
			datapointElement.setAttribute(XMLProfileHandler.DATE, ISO8601DateFormat.formatISO8601(date));
			Object value = dp.getValue();
			if (value instanceof Amount) {
				Amount amount = (Amount) value;
				Unit amountUnits = amount.getUnit();
				if (amountUnits == Unit.ONE || CommonUtils.equals(amountUnits, units)) {
					value = AmountUtils.getNumericValue(amount);
				} 
			}
			if (value != null) {
				String valueString;
				if (dataType != null) {
					valueString = EcoreUtil.convertToString(dataType, value);
				} else {
					valueString = value.toString();
				}
				datapointElement.setAttribute(XMLProfileHandler.VALUE, valueString);
			}
		}
	}

	@Override
	public void writeProfiles(Iterable<? extends Profile> profiles, Date start, Date end) {
		rootElement = document.createElement(XMLProfileHandler.PROFILES);
		document.appendChild(rootElement);
		for (Profile profile : profiles) {
			writeProfile(profile, start, end);
		}
		XMLUtilities.print(document, writer);
		rootElement = null;
	}
	
}
