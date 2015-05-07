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

import gov.nasa.ensemble.common.TestDataManipulator;
import gov.nasa.ensemble.common.data.test.TestUtil;
import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.Activator;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileDumper;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoader;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoadingException;
import gov.nasa.ensemble.core.jscience.impl.JScienceFactoryImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

public class TestCsvXmlWriting extends Assert {

	/** checked-in baseline results */
	private static final String EXPECTED_OUTPUTS_DIRECTORY = "data-test/csvxml/expected-outputs/";
	/** temporary area, used in place of temp files if running in IDE */
	private static final String ACTUAL_OUTPUTS_DIRECTORY = "data-test/csvxml/actual-outputs/";
	private JScienceFactory FACTORY = new JScienceFactoryImpl();
	private TestDataManipulator helper = new TestDataManipulator(Activator.getDefault());

	private void writeProfilesTo(Profile[] profiles, File outputFile) throws ParserConfigurationException {
		try {
			ProfileDumper dumper = new ProfileDumper(outputFile);
			dumper.writeProfiles(profiles);
		} catch (IOException e) {
			fail("Could not write to file " + outputFile);
		}
	}
	
	@Test
	public void testFileWriter() throws IOException {
		String testCaseName = "hello-world.txt";
		File outputFile = FileUtilities.createTempFile(testCaseName, ""); // createOutputFile(testCaseName);
		outputFile.createNewFile();
		FileWriter writer = new FileWriter(outputFile);
		writer.write("Hello, world.");
		writer.close();
		assertFilesIdentical(testCaseName, outputFile);
		}


	@Test
	public void testXmlContents() throws IOException, ParserConfigurationException {
		String testCaseName = "simple.xml";
		Profile resource1 = FACTORY.createProfile();
		Profile resource2 = FACTORY.createProfile();
		Profile resource3 = FACTORY.createProfile();
		resource1.setId("P1");
		resource2.setId("P2");
		resource3.setId("P3");
		resource1.setUnits(SI.WATT);
		resource2.setUnits(Unit.ONE);
		// Leave resource3 set to default.
		resource1.setDataType(EcorePackage.Literals.EDOUBLE_OBJECT);
		resource2.setDataType(EcorePackage.Literals.EBOOLEAN_OBJECT);
		resource3.setDataType(EcorePackage.Literals.ESTRING);
		resource1.setInterpolation(INTERPOLATION.LINEAR);
		resource2.setInterpolation(INTERPOLATION.INSTANTANEOUS);
		resource3.setInterpolation(INTERPOLATION.STEP);
		resource2.setDefaultValue(true);
		resource1.setName("Profile Name 1");
		// Leave resource2 set to default.
		resource3.setName("Profile Name 3");
		resource1.getAttributes().put("color", "red");
		resource1.getAttributes().put("flavor", "cherry");
		resource2.getAttributes().put("color", "green");
		resource2.getAttributes().put("flavor", "spearmint");
		Profile[] profiles = new Profile[] {resource1, resource2, resource3};
		File outputFile = createOutputFile(testCaseName);
		writeProfilesTo(profiles, outputFile);
		assertFilesIdentical(testCaseName, outputFile);
		assertFileMatchesProfiles(testCaseName, outputFile, profiles);
	}
	
	@Test
	public void testCsvContents() throws IOException, ParserConfigurationException {
		String testCaseName = "month-of-sundays.xml";
		Profile<String> monthName = FACTORY.createProfile();
		Profile<String> day = FACTORY.createProfile();
		Profile<String> year = FACTORY.createProfile();
		monthName.setId("MMMM");
		day.setId("d");
		year.setId("yyyy");
		monthName.setDataType(EcorePackage.Literals.ESTRING);
		day.setDataType(EcorePackage.Literals.ESTRING);
		year.setDataType(EcorePackage.Literals.ESTRING);
		monthName.setName("Month");
		day.setName("Day");
		year.setName("Year");

		Profile<String>[] profiles = new Profile[] {monthName, day, year};
		Date[] dates = getMonthOfSundays(31);
		for (Profile<String> profile : profiles) {
			DateFormat fieldFormat = new SimpleDateFormat(profile.getId());
			DataPoint<String>[] datapoints = new DataPoint[dates.length];
			int i=0;
			for (Date date : dates) {
				String value = fieldFormat.format(date);
				datapoints[i++] = FACTORY.createEDataPoint(date, value);
			}
			profile.setDataPointsArray(datapoints);
		}
		File outputFile = createOutputFile(testCaseName);
		writeProfilesTo(profiles, outputFile);
		assertFilesIdentical(testCaseName, outputFile);
		assertFileMatchesProfiles(testCaseName, outputFile, profiles);
	}
	
	@Test
	public void testQuotedContents() throws IOException, ParserConfigurationException {
		String testCaseName = "quoted.xml";
		Profile profile = FACTORY.createProfile();
		profile.setId("myid");
		profile.setDataType(EcorePackage.Literals.ESTRING);
		Date[] dates = getMonthOfSundays(3);
		profile.setDataPointsArray(new DataPoint[] {
				FACTORY.createEDataPoint(dates[0], "not quoted"),
				FACTORY.createEDataPoint(dates[1], "yes, quoted"),
				FACTORY.createEDataPoint(dates[2], "and when I say \"quoted\"...")
		});

		File outputFile = createOutputFile(testCaseName);
		Profile[] profiles = new Profile[] {profile};
		writeProfilesTo(profiles, outputFile);
		assertFilesIdentical(testCaseName, outputFile);
		assertFileMatchesProfiles(testCaseName, outputFile, profiles);
	}
	
	@Test
	public void testSparseContents() throws IOException, ParserConfigurationException {
		String testCaseName = "sparse.xml";
		int n = 30;
		Date[] dates = getMonthOfSundays(n);
		Profile[] profiles = new Profile[n];
		for (int i=0; i < n; i++) {
			profiles[i] = FACTORY.createProfile();
			profiles[i].setDataType(EcorePackage.Literals.EINTEGER_OBJECT);
			profiles[i].setId("col_" + (i+1));
			profiles[i].setDataPoints(Collections.singletonList(
					FACTORY.createEDataPoint(dates[i], i+1)));
		}

		File outputFile = createOutputFile(testCaseName);
		writeProfilesTo(profiles, outputFile);
		assertFilesIdentical(testCaseName, outputFile);
		assertFileMatchesProfiles(testCaseName, outputFile, profiles);
	}

	private void assertFileMatchesProfiles(String testCaseName,
			File fileToReadBack, Profile[] expectedProfiles) throws IOException {
		URI uri = URI.createFileURI(fileToReadBack.toString());
		ProfileLoader loader = new ProfileLoader(uri);
		int i = 0;
		Collection<? extends Profile> actualProfiles;
		try {
			actualProfiles = loader.readProfiles();
		} catch (ProfileLoadingException e) {
			fail("Error parsing " + fileToReadBack + ": " + e.getMessage());
			return;
			}
		for (Profile<Object> actual : actualProfiles) {
			Profile<Object> expected = expectedProfiles[i++];
			assertEquals("Round trip produced a different id",
					expected.getId(), actual.getId());
			assertEquals("Round trip produced a different name",
					expected.getName(), actual.getName());
			assertEquals("Round trip produced a different DataType",
					expected.getDataType(), actual.getDataType());
			assertEquals("Round trip produced a different Interpolation",
					expected.getInterpolation(), actual.getInterpolation());
			assertEquals("Round trip produced different Units",
					expected.getUnits(), actual.getUnits());
			
			EMap<String, String> expectedAttributes = expected.getAttributes();
			EMap<String, String> actualAttributes = actual.getAttributes();
			assertEquals("Round trip produced different numbers of properties",
					expectedAttributes.size(),
					actualAttributes.size());
			for (String key : expectedAttributes.keySet()) {
				assertEquals(key, expectedAttributes.get(key), actualAttributes.get(key));
			}
			
			EList<DataPoint<Object>> expectedDatapoints = expected.getDataPoints();
			EList<DataPoint<Object>> actualDatapoints = actual.getDataPoints();
			assertEquals("Round trip produced different number of DataPoints",
					expectedDatapoints.size(), actualDatapoints.size());
			int j=0;
			for (DataPoint<Object> actualDatapoint : actualDatapoints) {
				DataPoint<Object> expectedDatapoint = expectedDatapoints.get(j++);
				assertEquals("Round trip produced different DataPoints",
						expectedDatapoint, actualDatapoint);
			}
		}
		
	}

	private URL findExistingFile(String filePath) throws FileNotFoundException {
		return TestUtil.findTestData(Activator.getDefault(), filePath);
	}
	
	private void assertFilesIdentical(String testCaseName, File acualFile) {
		try {
			helper.assertSameContent(
					findExistingFile(EXPECTED_OUTPUTS_DIRECTORY + testCaseName),
					acualFile);
		} catch (IOException e) {
			fail("Unable to compare files: " + e);
		}
	}
	
	private File createOutputFile(String filename) throws IOException  {
		return TestUtil.createTestOutputFile(ACTUAL_OUTPUTS_DIRECTORY, filename, "");
	}

	private Date[] getMonthOfSundays(int nSundays) {
		Date start;
		Date[] result = new Date[nSundays];
		try {
			start = new SimpleDateFormat("M/d/yyyy Z").parse("4/3/2011 PDT");
		} catch (ParseException e) {
			start = new Date();
		}
		final long ONE_WEEK = 7*24*60*60*1000;		
		for (int weekNumber= 0;
				weekNumber < nSundays;
				weekNumber++) {
			result[weekNumber] = DateUtils.add(start, ONE_WEEK*weekNumber);
		}
		return result;
	}

}
