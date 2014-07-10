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
package gov.nasa.arc.spife.rcp.importer;

import gov.nasa.ensemble.common.io.CSVUtilities;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.util.InterchangeDateFormatFactory;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.ParameterStringifierUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

/**
 * Parse a list of instances of one activity type (in CSV format with some extra headers).
 * 
 * @see SPF-7693
 * @see design https://ensemble.jpl.nasa.gov/confluence/x/WgABAw
 * @author kanef
 * 
 */
public class CsvActivityInstanceParser {

	BufferedReader reader;
	ActivityDictionary dictionary = ActivityDictionary.getInstance();

	public CsvActivityInstanceParser(InputStream stream) {
		super();
		this.reader = new BufferedReader(new InputStreamReader(stream));
	}

	public List<EActivity> parse() throws ParseException, IOException {
		EActivityDef definition = parseActivityType(firstLineAfterComments());
		DateFormat timeFormat = parseDateFormat(reader.readLine());
		return parseRows(definition, timeFormat, parseHeaderRow(definition));
	}

	private List<EActivity> parseRows(EActivityDef definition, DateFormat timeFormat, List<EAttribute> colHeaders) throws IOException, ParseException {
		List<EActivity> result = new LinkedList<EActivity>();
		String line;
		do {
			line = reader.readLine();
			if (line != null && line.startsWith("#")) {
				definition = parseActivityType(firstLineAfterComments());
				colHeaders = parseHeaderRow(definition);
				continue;
			}
			if (line != null && !line.isEmpty())
				result.add(parseRow(definition, timeFormat, colHeaders, line));
		} while (line != null);
		return result;
	}

	private EActivity parseRow(EActivityDef definition, DateFormat timeFormat, List<EAttribute> colHeaders, String line) throws ParseException, IOException {
		EActivity activity = PlanFactory.getInstance().createActivity(definition);
		List<String> values = parseIntoFields(line);
		if (values.size() != colHeaders.size()) {
			throw new ParseException("Rows don't all have " + colHeaders.size() + " columns; " + " at least one has " + values.size(), 0);
		}
		Iterator<String> valuesIter = values.iterator();
		Iterator<EAttribute> parameterIter = colHeaders.iterator();
		while (valuesIter.hasNext() && parameterIter.hasNext()) {
			String valueString = valuesIter.next();
			EAttribute parameter = parameterIter.next();
			if (valueString != null) {
				Object value;
				if (parameter.getEAttributeType().getInstanceClass() == Date.class) {
					value = timeFormat.parse(valueString);
				} else {
					value = ParameterStringifierUtils.getStringifier(parameter).getJavaObject(valueString, null);
				}
				Class<? extends EObject> containerClass = (Class<? extends EObject>) parameter.getContainerClass();
				boolean activitySpecific = containerClass == null;
				boolean isMember = !activitySpecific && EMember.class.isAssignableFrom(containerClass);
				EObject container;
				if (isMember)
					container = activity.getMember((Class<? extends EMember>) containerClass);
				else if (activitySpecific)
					container = activity.getData();
				else
					container = activity;
				container.eSet(parameter, value);
			}
		}
		return activity;
	}

	private List<String> parseIntoFields(String line) throws IOException {
		List<String> allFields = CSVUtilities.parseCSVLine(line);
		List<String> result = new ArrayList(allFields.size());
		for (int index = 0; index < allFields.size(); index++) {
			result.add(getCsvCell(allFields, index));
		}
		return result;
	}

	private List<EAttribute> parseHeaderRow(EActivityDef definition) throws IOException, ParseException {
		List<EAttribute> result = new LinkedList<EAttribute>();
		for (String column : parseIntoFields(reader.readLine())) {
			result.add(parseParameterName(definition, column));
		}
		return result;
	}

	private EAttribute parseParameterName(EActivityDef definition, String name) throws ParseException {
		if (name.equalsIgnoreCase("InstanceName"))
			return PlanPackage.Literals.EPLAN_ELEMENT__NAME;
		if (name.equalsIgnoreCase("StartTime"))
			return TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
		if (name.equalsIgnoreCase("Duration"))
			return TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
		if (name.equalsIgnoreCase("EndTime"))
			return TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME;
		EAttribute result = (EAttribute) definition.getEStructuralFeature(name);
		if (result == null) {
			throw new ParseException("ActivityDef " + definition.getName() + " has no parameter named '" + name + "'.", 0);
		}
		return result;
	}

	private EActivityDef parseActivityType(String line) throws ParseException, IOException {
		List<String> contents = CSVUtilities.parseCSVLine(line);
		if (contents.size() < 2 || !getCsvCell(contents, 0).equalsIgnoreCase("ActivityDef")) {
			throw new ParseException("First line must have 'ActivityDef' in first column and activity type name in second.", 0);
		}
		String typeName = getCsvCell(contents, 1);
		EActivityDef result = dictionary.getActivityDef(typeName);
		if (result == null)
			throw new ParseException("No Activity Definition named '" + typeName + "'.", 0);
		return result;
	}

	private DateFormat parseDateFormat(String line) throws ParseException, IOException {
		List<String> contents = CSVUtilities.parseCSVLine(line);
		if (contents.size() < 2 || !getCsvCell(contents, 0).equalsIgnoreCase("TimeFormat")) {
			throw new ParseException("Second line must have 'TimeFormat' in first column and 'STK' or 'UTC' in second.", 0);
		}
		return InterchangeDateFormatFactory.fromName(getCsvCell(contents, 1));
	}

	private String getCsvCell(List<String> contents, int index) {
		return CSVUtilities.dequote(contents.get(index)).trim();
	}

	private String firstLineAfterComments() throws IOException {
		// The beginning of the file may have several lines of comments, which are designated by starting with #.
		String line = "#";
		while (line.isEmpty() || line.startsWith("#")) {
			line = reader.readLine();
		}
		return line;
	}

}
