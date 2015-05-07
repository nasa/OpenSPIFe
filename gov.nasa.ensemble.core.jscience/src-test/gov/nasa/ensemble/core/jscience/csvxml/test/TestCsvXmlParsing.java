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
package gov.nasa.ensemble.core.jscience.csvxml.test;

import gov.nasa.ensemble.common.data.test.TestUtil;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.Activator;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoader;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoadingException;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileWithLazyDatapointsFromCsv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.junit.Assert;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

public class TestCsvXmlParsing extends Assert {
	private static final String TEST_DIRECTORY = "data-test/csvxml/inputs/";
	private static final String[][] MALFORMED_FILES = {
		{"incorrect-csv-number.xml", "Malformed double: 63W"},
		{"incorrect-csv-iso8601-date.xml", "date format"},
		{"incorrect-csv-stk-date.xml", "date format"},
		{"incorrect-csv-headers.xml", "but CSV headings are"},
		// Missing CDATA is not tolerated.  Matt suggests it should be.
		// Can easily be supported if needed, but for now it's considered
		// part of the SIS.
		{"leave-out-CDATA--values-match-iso8601-dates.xml", "Expected <![CDATA["}
		};
	private static final String[] DATE_TEST_FILES = {
		"values-match-iso8601-dates.xml",
		"values-match-iso8601-dates-out-of-order.xml",
		"values-match-stk-dates.xml",
		"values-match-iso8601-dates--column-order-inconsistent.xml",
		// "leave-out-CDATA--values-match-iso8601-dates.xml"
	};

	private List<ProfileWithLazyDatapointsFromCsv<?>> readProfilesFrom(URI sourceFile) throws ProfileLoadingException, IOException {
		ProfileLoader profileLoader = new ProfileLoader(sourceFile);
		return new ArrayList(profileLoader.readProfiles());
	}
	
	@Test
	public void testXmlContents() throws ProfileLoadingException, IOException {
		List<? extends Profile> profiles = readProfilesFrom(findFile(TEST_DIRECTORY + "nominal-example.xml"));
		Profile rn1 = profiles.get(0);
		Profile rn2 = profiles.get(1);
		Profile rn3 = profiles.get(2);
		assertEquals("RN1", rn1.getId());
		assertEquals("RN2", rn2.getId());
		assertEquals("RN3", rn3.getId());
		assertEquals(SI.WATT, rn1.getUnits());
		assertEquals(Unit.ONE, rn2.getUnits());
		assertEquals(Unit.ONE, rn3.getUnits());
		assertEquals(Double.class, rn1.getDataType().getInstanceClass());
		assertEquals(Boolean.class, rn2.getDataType().getInstanceClass());
		assertEquals(String.class, rn3.getDataType().getInstanceClass());
		assertEquals(INTERPOLATION.LINEAR, rn1.getInterpolation());
		assertEquals(INTERPOLATION.INSTANTANEOUS, rn2.getInterpolation());
		assertEquals(INTERPOLATION.STEP, rn3.getInterpolation());
		assertEquals("Resource Name 1", rn1.getName());
		assertEquals("Resource Name 2", rn2.getName());
		assertEquals("Resource Name 3", rn3.getName());
		assertEquals("one", rn1.getAttributes().get("metadata test"));
		assertEquals("two", rn2.getAttributes().get("metadata test"));
		assertEquals("three", rn3.getAttributes().get("metadata test"));
		assertEquals("red", rn1.getAttributes().get("color"));
		assertEquals("blue", rn2.getAttributes().get("color"));
		assertEquals(null, rn3.getAttributes().get("color"));
		assertEquals("strawberry", rn1.getAttributes().get("flavor"));
		assertEquals("blueberry", rn2.getAttributes().get("flavor"));
		assertEquals(null, rn3.getAttributes().get("flavor"));
		assertRandomAccessWorks(rn1);
		assertRandomAccessWorks(rn2);
		assertRandomAccessWorks(rn3);
	}
	
	@Test
	public void testSequentialValuesWithMissingValues() throws ProfileLoadingException, IOException {
		for (ProfileWithLazyDatapointsFromCsv profile : readProfilesFrom(findFile(TEST_DIRECTORY + "values-are-sequential.xml"))) {
			assertProfileSequential(profile);
			assertRandomAccessWorks(profile);
		}
	}
	
	@Test
	public void testDefaultValues() throws ProfileLoadingException, IOException {
		List<ProfileWithLazyDatapointsFromCsv<?>> profiles = new ArrayList(readProfilesFrom(findFile(TEST_DIRECTORY + "values-are-equal.xml")));
		assertEquals("Expected 3 profiles", 3, profiles.size());
		ProfileWithLazyDatapointsFromCsv<?> first = profiles.get(0);
		ProfileWithLazyDatapointsFromCsv<?> second = profiles.get(1);
		ProfileWithLazyDatapointsFromCsv<?> third = profiles.get(2);
		for (DataPoint point1 : first.getDataPointsLazily()) {
			DataPoint point2 = second.getDataPoint(point1.getDate());
			DataPoint point3 = third.getDataPoint(point1.getDate());
			assertEquals(point1.getValue(), point2.getValue());
			assertEquals(point1.getValue(), point3.getValue());
		}
		for (Profile profile : profiles) {
			assertRandomAccessWorks(profile);
		}
	}
	@Test
	public void testWithDateFields() throws ProfileLoadingException, IOException {
		for (String file : DATE_TEST_FILES) {
			for (ProfileWithLazyDatapointsFromCsv profile : readProfilesFrom(findFile(TEST_DIRECTORY + file))) {
				assertDatapointValuesDescribeDates(profile);
				assertDatesInOrder(profile);
				assertRandomAccessWorks(profile);
			}
		}
	}
	
	private void assertDatesInOrder(ProfileWithLazyDatapointsFromCsv<?> profile) {
		Date prevDate = null;
		for (DataPoint<?> datapoint : profile.getDataPoints()) {
			Date date = datapoint.getDate();
			if (prevDate != null && prevDate.after(date)) {
				fail("Dates are out of order:  " +
						date + " seen after " + prevDate +
						".  Binary search will return wrong values.");
			}
			prevDate = date;
		}
	}

	@Test
	public void testInterpolation() throws ProfileLoadingException, IOException {
		List<ProfileWithLazyDatapointsFromCsv<?>> profiles = readProfilesFrom(findFile(TEST_DIRECTORY + "interpolation.xml"));
		int nCases = INTERPOLATION.values().length;
		assertEquals("Number of profiles", nCases * 2, profiles.size());
		for (int caseNumber=0; caseNumber < nCases; caseNumber++) {
			ProfileWithLazyDatapointsFromCsv<?> interpolatedProfile = profiles.get(caseNumber);
			ProfileWithLazyDatapointsFromCsv<?> expectedProfile = profiles.get(nCases + caseNumber);
			// Check all the dates that are in the file; some profiles will have to interpolate points for them.
			for (DataPoint expectedDatapoint : expectedProfile.getDataPointsLazily()) {
				DataPoint<?> possiblyInterpolatedDatapoint 
				= interpolatedProfile.getDataPoint(expectedDatapoint.getDate());
				assertEquals(interpolatedProfile.getInterpolation().getLiteral(),
						expectedDatapoint.getValue(), possiblyInterpolatedDatapoint.getValue());
			}
			// Now try a date that isn't in the file at all.
			String dateString = interpolatedProfile.getAttributes().get("interpolate_at_date");
			Date interpolatedDate = ISO8601DateFormat.parseISO8601(dateString);
			Double expectedValue = Double.valueOf(interpolatedProfile.getAttributes().get("expected_interpolated_value"));
			assertEquals(interpolatedProfile.getInterpolation().getLiteral() + " at " + dateString,
					expectedValue, interpolatedProfile.getDataPoint(interpolatedDate).getValue());
		}
	}


	@Test
	public void testExceptionHandling() {
		// Getting datapoints from malformed.xml and incorrect-headers.xml should throw an exception,
		// but only when we actually try to get the datapoints.
		List<ProfileWithLazyDatapointsFromCsv<?>> profiles;
		for (String[] filenameAndExpectedError: MALFORMED_FILES) {
			String filename = filenameAndExpectedError[0];
			String expectedError = filenameAndExpectedError[1];
			try {
				profiles = readProfilesFrom(findFile(TEST_DIRECTORY + filename));
				profiles.get(0).getId();
			} catch (Exception e) {
				fail("Exception thrown while loading XML, before asking load the datapoints:  " + e);
				return;
			}
			try {
				profiles.get(0).getDataPointsLazily();
				fail("No exception was thrown on " + filename);
			} catch (ProfileLoadingException e) {
				String message = e.getMessage();
				assertTrue(message, message.contains(expectedError));
			}
		}
		
	}

	private void assertProfileSequential(ProfileWithLazyDatapointsFromCsv<Integer> profile) throws ProfileLoadingException {
		Integer nextExpectedValue = 1;
		List<DataPoint<Integer>> dataPoints = profile.getDataPointsLazily();
		if (dataPoints.isEmpty()) {
			fail("No datapoints read for " + profile.getId() + " column.");
		}
		for (DataPoint<Integer> datapoint : dataPoints) {
			assertEquals(profile.getId() + " is not sequential:  ",
					nextExpectedValue++, datapoint.getValue());
		}
	}

	private void assertDatapointValuesDescribeDates(ProfileWithLazyDatapointsFromCsv profile) throws ProfileLoadingException {
		if (profile.getDataPointsLazily().get(0) instanceof String) {
			assertDatapointValuesDescribeDateStringField(profile); // month name used in test
		} else if (profile.getDataPointsLazily().get(0) instanceof Integer) {
			assertDatapointValuesDescribeDateIntegerField(profile);
		}
	}
	
	private void assertDatapointValuesDescribeDateStringField(ProfileWithLazyDatapointsFromCsv<String> profile) throws ProfileLoadingException {
		String description = profile.getName();
		DateFormat format = new SimpleDateFormat(profile.getId());
		List<DataPoint<String>> dataPoints = profile.getDataPointsLazily();
		if (dataPoints.isEmpty()) {
			fail("No datapoints read for " + profile.getId() + " column.");
		}
		for (DataPoint<String> datapoint : dataPoints) {
			assertDatapointValuesDescribeDateStringField(datapoint, format, description);
		}
	}
	private void assertDatapointValuesDescribeDateIntegerField(ProfileWithLazyDatapointsFromCsv<Integer> profile) throws ProfileLoadingException {
		String description = profile.getName();
		DateFormat format = new SimpleDateFormat(profile.getId());
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		List<DataPoint<Integer>> dataPoints = profile.getDataPointsLazily();
		if (dataPoints.isEmpty()) {
			fail("No datapoints read for " + profile.getId() + " column.");
		}
		for (DataPoint<Integer> datapoint : dataPoints) {
			assertDatapointValuesDescribeDateIntegerField(datapoint, format, description);
		}
	}

	private void assertDatapointValuesDescribeDateStringField(DataPoint<String> datapoint,
			DateFormat format, String columnName) {
		String value = datapoint.getValue();
		assertNotNull("Values should never be null", value);
		assertEquals(columnName, value, format.format(datapoint.getDate()));
	}
	
	private void assertDatapointValuesDescribeDateIntegerField(DataPoint<Integer> datapoint,
			DateFormat format, String columnName) {
		Integer value = datapoint.getValue();
		assertNotNull("Values should never be null", value);
		assertEquals(columnName + " of " + datapoint, (int) value, Integer.parseInt(format.format(datapoint.getDate())));
	}
	
	private void assertRandomAccessWorks(Profile<Object> profile) {
		for (DataPoint<Object> datapoint : profile.getDataPoints()) {
			assertEquals("Random access search failed to find datapoint.",
					datapoint, profile.getDataPoint(datapoint.getDate()));
		}
	}

	private URI findFile(String filePath) throws FileNotFoundException {
		File findTestFile = TestUtil.findTestFile(Activator.getDefault(), filePath);
		URI uri = URI.createFileURI(findTestFile.toString());
		return uri;
	}


}
