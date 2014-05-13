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

import gov.nasa.ensemble.common.io.XMLUtilities;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.measure.unit.Unit;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/** 
 * Writes a collection of profiles (one per column) to an XML file containing CSV.
 *
 * An instance of this class may only be safely used by one thread at a time.
 *
 * Initially requested for LASS.
 * @since SPF-6700
 */
public class ProfileDumper {
	
	private static final char NEWLINE = '\n';
	private static final String CHARACTERS_NEEDING_QUOTE = ",\n\"";
	private static final char DOUBLE_QUOTE = '"';
	private static final String DOUBLE_QUOTE_STRING = String.valueOf(DOUBLE_QUOTE);
	private static final String QUOTED_DOUBLE_QUOTE = String.valueOf(new char[]{DOUBLE_QUOTE,DOUBLE_QUOTE});
	
	private static final DateFormat DATE_FORMAT = new ISO8601DateFormat(TimeZone.getTimeZone("UTC"));
	
	private Document document;

	private File outputFile;
	private OutputStream outputStream;

	/**
	 * When this constructor is used, the output will be written to the file
	 * and the file will be subsequently closed.
	 * 
	 * @param outputFile
	 */
	public ProfileDumper(File outputFile) {
		this.outputFile = outputFile;
	}
	
	/**
	 * When this constructor is used, the output will be written to the stream
	 * and it is the responsibility of the caller to close the stream when appropriate.
	 * 
	 * @param outputStream
	 */
	public ProfileDumper(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public void writeProfiles(Collection<? extends Profile> profiles) throws IOException, ParserConfigurationException {
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element columnList = document.createElement("columns");
		Element dataElement = document.createElement("data");
		columnList.setAttribute("timeFormat", "ISO8601");
		for (Profile profile : profiles) {
			columnList.appendChild(createColumnXMLFromProfile(profile));
		}
		dataElement.appendChild(createCSVFromProfiles(profiles));
		Element root = document.createElement("resources");
   //   root.setAttributeNS doesn't work (with this serializer and output format)
//		root.setAttributeNS("xsi", "noNamespaceSchemaLocation",
		root.setAttribute("xsi:noNamespaceSchemaLocation",
				"../schema/lass_import_format.xsd");
//		root.setAttributeNS("xmlns", "xsi",
		root.setAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		root.appendChild(columnList);
		root.appendChild(dataElement);
		document.appendChild(root);
		serialize();
	}
	
	public void writeProfiles(Profile[] profiles) throws IOException, ParserConfigurationException {
		writeProfiles(Arrays.asList(profiles));		
	}

	private void serialize() throws IOException {
		if (outputFile != null) {
			FileWriter writer = new FileWriter(outputFile);
			try {
				XMLUtilities.print(document, writer);
			} finally {
				writer.close();
			}
		} else if (outputStream != null) {
			XMLUtilities.print(document, outputStream);
		}
	}
	
	private Node makeAttribute(String name, String value) {
		Attr result = document.createAttribute(name);
		result.setNodeValue(value);
		return result;
	}

	private Node createColumnXMLFromProfile(Profile profile) {
		Node columnElement = document.createElement("column");
		NamedNodeMap attributes = columnElement.getAttributes();
		
		attributes.setNamedItem(makeAttribute("id", profile.getId()));
		
		if (!profile.getUnits().isCompatible(Unit.ONE)) {
			attributes.setNamedItem(makeAttribute("units", profile.getUnits().toString()));
		}
		
		attributes.setNamedItem(makeAttribute("interpolation", profile.getInterpolation().getLiteral().toLowerCase()));
	
		if (profile.getName() != null && !profile.getName().equals(profile.getId())) {
			attributes.setNamedItem(makeAttribute("displayName", profile.getName()));
		}
		
		if (profile.getDefaultValue() != null && !profile.getDefaultValue().equals(profile.getId())) {
			attributes.setNamedItem(makeAttribute("defaultValue", formatValueCell(profile.getDefaultValue())));
		}
		
		attributes.setNamedItem(makeAttribute("type", capitalize(profile.getDataType().getInstanceClass().getSimpleName())));
		if (!profile.getAttributes().isEmpty()) {
			for (Object key : profile.getAttributes().keySet()) {
				if (key instanceof String) {
					Object value = profile.getAttributes().get(key);
					if (value instanceof String) {
						Node propertyElement = document.createElement("property");
						propertyElement.getAttributes().setNamedItem(makeAttribute("key", (String)key));
						propertyElement.getAttributes().setNamedItem(makeAttribute("value", (String)value));
						columnElement.appendChild(propertyElement);
					}
				}
			}
		}
		return columnElement;
	}
	
	private String capitalize(String string) {
		if (string.length() < 2) return string.toUpperCase();
		return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
	}

	private CDATASection createCSVFromProfiles(Collection<? extends Profile> profiles) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(NEWLINE);
		generateCSVFromProfiles(profiles, buffer);
		buffer.append(NEWLINE);
		return document.createCDATASection(buffer.toString());
	}

	private SortedMap<Date, String[]> makeOutputRows(Collection<? extends Profile> profiles) {
		SortedMap<Date, String[]> result = new TreeMap<Date, String[]>();
		int nProfiles = profiles.size();
		int columnNumber = 0;
		for (Profile<Object> profile : profiles) {
			for (DataPoint<Object> datapoint : profile.getDataPoints()) {
				Date date = datapoint.getDate();
				if (!result.containsKey(date)) {
					result.put(date, new String[nProfiles]);
				}
				result.get(date)[columnNumber]
						= formatValueCell(datapoint.getValue());
			}
			columnNumber++;
		}
		return result;
	}
	
	private void generateCSVFromProfiles(Collection<? extends Profile> profiles, StringBuffer buffer) {
		generateCSVHeaderRow(profiles, buffer);
		SortedMap<Date, String[]> outputRows = makeOutputRows(profiles);
		for (Date date : outputRows.keySet()) {
			buffer.append(NEWLINE);
			generateCSVRow(date, outputRows.get(date), buffer);
		}
	}
	
	private void generateCSVHeaderRow(Collection<? extends Profile> profiles, StringBuffer buffer) {
		appendCSVCell("Time", buffer, true);
		for (Profile profile : profiles) {
			appendCSVCell(profile.getId(), buffer, false);
		}
	}

	private void generateCSVRow(Date date, String[] serializedData, StringBuffer buffer) {
		appendCSVCell(formatDateCell(date), buffer, true);
		for (String cell : serializedData) {
			appendCSVCell(cell, buffer, false);
		}
	}

	private void appendCSVCell(String contents, StringBuffer buffer, boolean first) {
		if (!first) buffer.append(',');
		if (contents != null) {
			if (!quoteNeeded(contents)) {
				buffer.append(contents);
			} else {
				if (contents.indexOf(DOUBLE_QUOTE) >= 0) {
					contents = contents.replace(DOUBLE_QUOTE_STRING, QUOTED_DOUBLE_QUOTE);
				}
				buffer.append(DOUBLE_QUOTE);
				buffer.append(contents);
				buffer.append(DOUBLE_QUOTE);
			}
		}
	}

	private boolean quoteNeeded(String contents) {
		int length = contents.length();
		for (int i=0; i < length; i++) {
			if (CHARACTERS_NEEDING_QUOTE.indexOf(contents.charAt(i)) >= 0) {
				return true;
			}
		}
		return false;
	}

	private String formatDateCell(Date date) {
		return DATE_FORMAT.format(date);
	}

	private String formatValueCell(Object cellContents) {
		return cellContents.toString();
	}

}
