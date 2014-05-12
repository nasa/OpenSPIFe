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

import gov.nasa.ensemble.common.io.CSVUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.impl.DataPointImpl;
import gov.nasa.ensemble.core.jscience.util.InterchangeDateFormatFactory;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** 
 * Loads a collection of profiles (one per column) from an XML file containing CSV.
 * 
 * The data is lazily loaded from the CSV in the CDATA section at the end,
 * at the time the datapoints for any of the profiles are requested with getDataPoints(),
 * at which time the affected profile asks the loader to load datapoints for all profiles.
 * 
 * Initially requested for LASS.
 * @since SPF-6700
 */
public class ProfileLoader {
		
	private static final char[] BEGINNING_OF_DATA_ELEMENT = "<data>".toCharArray();
	private static final String BEGINNING_OF_CDATA_SECTION = "<![CDATA[";
	private static final char[] BOILERPLATE_CLOSING = "</data></resources>".toCharArray();
	private static final String END_OF_CSV_LINE = "]]>"; // end of CDATA section
	private static final int ONE_FOR_STARTTIME = 1;

	private enum Datatype {Double, Integer, String, Boolean}

	private List<ProfileAndMetadata> profileAndMetadataInXmlOrder;
	private ProfileAndMetadata[] profileAndMetadataInCsvOrder;
	
	private int nCharactersPrecedingData;	
	private boolean dataLoaded = false;

	private DateFormat specifiedDateFormat;

	private URI sourceURI;
	private InputStream sourceStream;

	public ProfileLoader(URI uri) {
		sourceURI = uri;
	}
	
	/**
	 * When using this constructor, the stream must be parsed first
	 * with readProfiles(), and then optionally ensureDataLoaded().
	 * When finished loading, the caller should close the input stream.
	 * 
	 * @param stream
	 */
	public ProfileLoader(InputStream stream) {
		this.sourceStream = stream;
	}
	
	/** Loads profile objects.  Their datapoints are read later, when they're accessed.
	 * @return Profiles.
	 * @throws ProfileLoadingException
	 * @throws IOException
	 */
	public Collection<ProfileWithLazyDatapointsFromCsv<?>> readProfiles() throws ProfileLoadingException, IOException {
		return readProfiles(false);
	}
	
	/**
	 * Loads profile objects.
	 * @param loadDataImmediately -- set to true if the stream can't be reopened and repositioned later on demand when the datapoints are needed.
	 * @return Profiles.
	 * @throws ProfileLoadingException
	 * @throws IOException
	 */
	public Collection<ProfileWithLazyDatapointsFromCsv<?>> readProfiles(boolean loadDataImmediately) throws ProfileLoadingException, IOException {
		InputStream urlStreamWeJustOpened = null;
		try {
			final InputStream streamWeAreReading;
			if (sourceStream != null) {
				streamWeAreReading = sourceStream;
			} else {
				ExtensibleURIConverterImpl converter = new ExtensibleURIConverterImpl();
				urlStreamWeJustOpened = converter.createInputStream(sourceURI);
				streamWeAreReading = urlStreamWeJustOpened;
			}
			Document document = loadColumnDefinitions(streamWeAreReading);
			Node topLevel = document.getElementsByTagName("columns").item(0);
			String timeFormatName = topLevel.getAttributes().getNamedItem("timeFormat").getNodeValue();
			specifiedDateFormat = InterchangeDateFormatFactory.fromName(timeFormatName);
			NodeList columnElements = document.getElementsByTagName("column");
			int nProfiles = columnElements.getLength();
			profileAndMetadataInXmlOrder = new ArrayList(nProfiles);
			for (int i=0; i < nProfiles; i++) {
				Node item = columnElements.item(i);
				ProfileWithLazyDatapointsFromCsv<?> profile = createLazyProfile(item);
				profile.setValid(true); // see SPF-8162
				String datatypeName = item.getAttributes().getNamedItem("type").getTextContent();
				Datatype datatype = Datatype.valueOf(datatypeName);
				Node defaultValueNode = item.getAttributes().getNamedItem("defaultValue");
				Object defaultValue = null;
				if (defaultValueNode != null) {
					defaultValue = parseValueCell(defaultValueNode.getTextContent(), datatype);
				}
				profileAndMetadataInXmlOrder.add(new ProfileAndMetadata(profile, datatype, defaultValue));
			}
			List<ProfileWithLazyDatapointsFromCsv<?>> profiles = new ArrayList<ProfileWithLazyDatapointsFromCsv<?>>();
			for (ProfileAndMetadata profileEtc : profileAndMetadataInXmlOrder) {
				profiles.add(profileEtc.getProfile());
			}
			if (loadDataImmediately) {
				loadDataFromThisPointInStream(streamWeAreReading);
			}
			return profiles;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new ProfileLoadingException(e.getClass() + ":  " + e.getMessage());
		} finally {
			if (urlStreamWeJustOpened != null) {
				IOUtils.closeQuietly(urlStreamWeJustOpened);
			}
		}
	}

	private ProfileWithLazyDatapointsFromCsv<?> createLazyProfile(Node columnElement) throws ProfileLoadingException {
		ProfileWithLazyDatapointsFromCsv result = new ProfileWithLazyDatapointsFromCsv(this);
		NamedNodeMap attributes = columnElement.getAttributes();
		result.setId(attributes.getNamedItem("id").getTextContent());
		Node unitAttribute = attributes.getNamedItem("units");
		if (unitAttribute == null) {
			result.setUnits(Unit.ONE);
		} else {
			String unitName = unitAttribute.getTextContent();
			Unit unit = null;
			try {
				unit = EnsembleUnitFormat.INSTANCE.parse(unitName);
			} catch (Exception e) {/* leave null */}
			if (unit==null) LogUtil.warn("Undefined unit name: " + unitName);
			result.setUnits(unit==null? new BaseUnit(unitName + " (unrecognized units)") : unit);
		}

		Node displayNameAttribute = attributes.getNamedItem("displayName");
		if (displayNameAttribute == null) {
			result.setName(null);
		} else {
			result.setName(displayNameAttribute.getTextContent());
		}
		
		Node interpolationAttribute = attributes.getNamedItem("interpolation");
		String interpolationString = interpolationAttribute.getTextContent();
		result.setInterpolation(INTERPOLATION.valueOf(interpolationString.toUpperCase()));
		
		String typeAttribute = attributes.getNamedItem("type").getTextContent();
		result.setDataType(EMFUtils.createEDataTypeFromString(typeAttribute));
		
		Node defaultValueAttribute = attributes.getNamedItem("defaultValue");
		if (defaultValueAttribute != null) {
			String defaultValueString = defaultValueAttribute.getTextContent();
			result.setDefaultValue(parseValueCell(defaultValueString, Datatype.valueOf(typeAttribute)));
		}
		
		if (columnElement.hasChildNodes()) {
			NodeList properties = columnElement.getChildNodes();
			for (int i=0; i < properties.getLength(); i++) {
				Node property = properties.item(i);
				if (property.getNodeType()==Node.ELEMENT_NODE) {
					NamedNodeMap propertyAttributes = property.getAttributes();
					result.getAttributes().put(
							propertyAttributes.getNamedItem("key").getTextContent(),
							propertyAttributes.getNamedItem("value").getTextContent());
				}
			}
		}
		
		return result;
	}

	private Document loadColumnDefinitions(InputStream stream) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		docBuilderFactory.setIgnoringComments(true);
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		CensoringInputStream fileContentsWithoutCsv = new CensoringInputStream(stream);
		Document document = docBuilder.parse(new InputSource(fileContentsWithoutCsv));
		nCharactersPrecedingData = fileContentsWithoutCsv.getNCharactersPrecedingData();
		return document;
	}

	public synchronized void ensureDataLoaded() throws ProfileLoadingException {
		if (!dataLoaded) {
			loadData();
			dataLoaded = true;
		}
	}

	private void loadData() throws ProfileLoadingException {
		if (sourceStream != null) {
			loadDataFromThisPointInStream(sourceStream);
			dataLoaded = true;
		} else {
			try {
				ExtensibleURIConverterImpl converter = new ExtensibleURIConverterImpl();
				InputStream stream = converter.createInputStream(sourceURI);
				stream.skip(nCharactersPrecedingData);
				loadDataFromThisPointInStream(stream);
				dataLoaded = true;
			} catch (IOException e) {
				throw new ProfileLoadingException("Error opening " + sourceURI
						+ " to position " + nCharactersPrecedingData + ": " + e);
			}
		}
	}

	private void loadDataFromThisPointInStream(InputStream stream) throws ProfileLoadingException {
		
			try {
			List<DataPoint>[] dataPointsForEachProfile = new ArrayList[profileAndMetadataInXmlOrder.size()];
			for (int i = 0; i < dataPointsForEachProfile.length; i++) {
				dataPointsForEachProfile[i] = new ArrayList<DataPoint>();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			skipCDATA(reader);
			List<String> headers = CSVUtilities.parseCSVLine(reader, true);
			validateHeaderRow(headers);
			profileAndMetadataInCsvOrder = reorderProfilesAndAddMetadata(headers);
			List<String> currentRow = headers;
			Date prevDate = null;
			boolean rowsWereOutOfTimeOrder = false;
			while (true) { // loop over rows, adding to add profiles (except where values are omitted in a row).
				currentRow = CSVUtilities.parseCSVLine(reader, true);
				if (currentRow.size() > 0 && currentRow.get(0).trim().startsWith(END_OF_CSV_LINE)) {
					break; // end of data
				}
				if (currentRow.size() != profileAndMetadataInXmlOrder.size()+1) {
					throw new ProfileLoadingException("Row has " + currentRow.size() + " elements instead of " +
							(profileAndMetadataInXmlOrder.size()+1) + ": " + currentRow);
				}
				Date date = parseDateCell(currentRow.get(0));
				if (prevDate != null && prevDate.after(date)) {
					rowsWereOutOfTimeOrder = true;
				}
				prevDate = date;
				for (int i = 0, column = 1; i < dataPointsForEachProfile.length; i++, column++) {
					ProfileAndMetadata profileAndMetadata = profileAndMetadataInCsvOrder[i];
					Datatype datatype = profileAndMetadata.getDatatype();
					Object defaultValue = profileAndMetadata.getDefaultValue();
					String cellContents = currentRow.get(column);
					if (!isEmpty(cellContents, datatype)) {
						dataPointsForEachProfile[i].add(
								new DataPointImpl(date,
										parseValueCell(cellContents, datatype)));
					} else if (defaultValue != null) {
						dataPointsForEachProfile[i].add(new DataPointImpl(date, defaultValue));
					}
				}
			}
			int i = 0;
			for (ProfileAndMetadata profileEtc : profileAndMetadataInCsvOrder) {
				List<DataPoint> dataPoints = dataPointsForEachProfile[i++];
				if (rowsWereOutOfTimeOrder) {
					Collections.sort(dataPoints, DataPoint.DEFAULT_COMPARATOR);
				}
				profileEtc.getProfile().setDataPoints(dataPoints);
			}
		} catch (IOException e) {
			throw new ProfileLoadingException("I/O error: " + e);
		} finally {
			if (sourceStream == null) {
				IOUtils.closeQuietly(stream);
			}
		}
	}

	private ProfileAndMetadata[] reorderProfilesAndAddMetadata(List<String> headers) throws ProfileLoadingException {
		ProfileAndMetadata[] result = new ProfileAndMetadata[headers.size()-ONE_FOR_STARTTIME];
		for (ProfileAndMetadata profileEtc : profileAndMetadataInXmlOrder) {
			String id = profileEtc.getProfile().getId();
			int index = headers.indexOf(id);
			if (index == -1) {
				// already validated, but just in case:
				throw new ProfileLoadingException("No profile id " + id + " listed in CSV headers.");
			} else {
				int i = index-ONE_FOR_STARTTIME; // first is always StartTime
				result[i] = profileEtc;
			}
		}
		return result;
	}

	private void skipCDATA(BufferedReader reader) throws IOException, ProfileLoadingException {
		// Skips over the line that says "<!CDATA[" and the blank line before it.
		String line = "";
		while (line.isEmpty()) {
			line = reader.readLine();
			if (line==null) throw new EOFException("End of file where " + BEGINNING_OF_CDATA_SECTION + " was expected.");
		}
		if (!line.contains(BEGINNING_OF_CDATA_SECTION)) {
			throw new ProfileLoadingException("Expected " + BEGINNING_OF_CDATA_SECTION);
		}
	}

	private void validateHeaderRow(Collection<String> csvHeaders) throws ProfileLoadingException {
		Set<String> actualCsvHeaders = new HashSet<String>();
		Set<String> expectedHeadersFromXml = new HashSet<String>();
		int i=0;
		for (String header : csvHeaders) {
			if (i++ >= ONE_FOR_STARTTIME) {
				actualCsvHeaders.add(header.toLowerCase());
			}
		}
		for (ProfileAndMetadata profileEtc : profileAndMetadataInXmlOrder) {
			expectedHeadersFromXml.add(profileEtc.getProfile().getId().toLowerCase());
		}
		if (!actualCsvHeaders.equals(expectedHeadersFromXml)) {
			throw new ProfileLoadingException("XML declares column names as '" + expectedHeadersFromXml
					+ "' but CSV headings are '" + actualCsvHeaders);
		}
	}

	private Date parseDateCell(String string) throws ProfileLoadingException {
		string = string.trim(); // tolerate indentation
		try {
			return specifiedDateFormat.parse(string);
		} catch (ParseException e) {
			throw new ProfileLoadingException("Invalid date format " + string);
		}
	}

	private Object parseValueCell(String cellContents, Datatype datatype) throws ProfileLoadingException {
		try {
		switch (datatype) {
			case Integer: return Integer.parseInt(cellContents.trim());
			case Boolean: return Boolean.parseBoolean(cellContents.trim());
			case Double:  return Double.parseDouble(cellContents.trim());
			// Don't call trim except for numbers:
			// "Spaces are considered part of a field and should not be ignored."
			// -- RFC 4180
			case String:  return cellContents;
		}
		// Should not fall through, since all switch statements above unconditionally return.
		return null;
		} catch (NumberFormatException e) {
			throw new ProfileLoadingException("Malformed " + datatype.name().toLowerCase() + ": " + cellContents);
		}
	}

	private boolean isEmpty(String cellContents, Datatype datatype) {
		switch (datatype) {
		// Don't call trim except for numbers:
		// "Spaces are considered part of a field and should not be ignored."
		// -- RFC 4180
		case String:  return cellContents.isEmpty();
		default:      return cellContents.isEmpty() || cellContents.trim().isEmpty();
		}
	}


	/** This reads an XML file normally until it gets to the opening tag of the data element,
	 * and then pretends that it instead saw the closing tag at the end of the file.
	 * We'll read the data element's contents later on demand.
	 * At this point of loading, we need balanced XML. 
	 */
	private static class CensoringInputStream extends InputStream {
		
		private InputStream underlyingStream;
		private int nCharactersPrecedingData = 0;
	
		private int nCharactersOfDataTagSeen = 0;
		private int nCharactersOfClosingTagsReturned = 0;
		
		public CensoringInputStream(InputStream underlyingStream) {
			super();
			this.underlyingStream = underlyingStream;
		}

		@Override
		public int read() throws IOException {
			if (nCharactersOfDataTagSeen < BEGINNING_OF_DATA_ELEMENT.length) { // normal case
				int nextByte = underlyingStream.read();
				if (nextByte==BEGINNING_OF_DATA_ELEMENT[nCharactersOfDataTagSeen]) {
					nCharactersOfDataTagSeen++;
				} else {
					// If we see "<datx", for example, we reset it from 4 back to 0 and start looking again.
					nCharactersOfDataTagSeen = 0;
				}
				nCharactersPrecedingData++;
				return nextByte;				
			}
			// If we've seen the '>' in "<data>", we start streaming the final closing tags
			else if (nCharactersOfClosingTagsReturned >= BOILERPLATE_CLOSING.length) { // until done.
				return -1; // end of stream
			} else { // otherwise, we're currently returning the last bytes of the stream, one by one.
				int i = nCharactersOfClosingTagsReturned;
				nCharactersOfClosingTagsReturned++;
				return BOILERPLATE_CLOSING[i];
			}
		}
		
		public int getNCharactersPrecedingData() {
			return nCharactersPrecedingData;
		}

		@Override
		public int available() throws IOException {
			return underlyingStream.available();
		}

		@Override
		public void close() {
			// underlying stream should be closed separately when appropriate
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public void reset() throws IOException {
			underlyingStream.reset();
			nCharactersOfDataTagSeen = 0;
		}

		@Override
		public long skip(long n) throws IOException {
			nCharactersOfDataTagSeen = 0;
			return underlyingStream.skip(n);
		}
			
	}

	private class ProfileAndMetadata {
		ProfileWithLazyDatapointsFromCsv profile;
		Datatype datatype;
		Object defaultValue;
		
		public ProfileAndMetadata(ProfileWithLazyDatapointsFromCsv profile, Datatype datatype,
				Object defaultValue) {
			super();
			this.profile = profile;
			this.datatype = datatype;
			this.defaultValue = defaultValue;
		}

		public ProfileWithLazyDatapointsFromCsv getProfile() {
			return profile;
		}

		public Datatype getDatatype() {
			return datatype;
		}

		public Object getDefaultValue() {
			return defaultValue;
		}
	}

}
