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
package gov.nasa.ensemble.core.model.plan.diff.report.test;

import gov.nasa.ensemble.common.data.test.TestUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.test.TestCaseWithADAndPlansOutsideOfBuild;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;

public abstract class AbstractTestPlanDiffReportFromPlanFiles extends
		TestCaseWithADAndPlansOutsideOfBuild {

	private ResourceSet toEachItsOwnResourceSet = null;

	public AbstractTestPlanDiffReportFromPlanFiles() {
		super();
	}

	@Override
	protected String getPluginIdForTestFolder() {
		return gov.nasa.ensemble.core.model.plan.diff.report.Activator.PLUGIN_ID;
	}

	@Override
	protected URL getADlocation() {
		return findFile("test-dictionary-based-on-MOD.dictionary");
	}

	@Test
	public void testUnexpectedChangesToPlanDiffOutput() throws IOException {
		compareToExpectedFile("STP08_09NOV30--Trimmed",
				"STP08_09NOV30-before/schedule",
				"STP08_09NOV30-after/schedule",
				getContentOfLineToSkipTo(),
				debugging()
				);				
	}

	protected boolean debugging() {
		return !TestUtil.isBamboo();
	}

	protected abstract String getContentOfLineToSkipTo();

	protected void compareToExpectedFile(String directory, String planName1,
			String planName2, String startComparingAtLine, boolean writeActualFileOnFailure) throws IOException {	
				EPlan plan1 = getTestCase(directory, planName1);
				EPlan plan2 = getTestCase(directory, planName2);
				URL expectedFile = getExpectedFile(directory);
				InputStream expectedStream = expectedFile.openStream();
				PlanDiffList differences = PlanDiffEngine.findChanges(plan1, plan2);
				OutputStream actualStream = new ByteArrayOutputStream();
				writeReportToStream(differences, plan1, plan2, actualStream);
				boolean failed = true;
				try {
					compareResults(readAll(new InputStreamReader(expectedStream)), actualStream.toString(), startComparingAtLine);
					failed = false;
				}
				finally {
					if (failed && writeActualFileOnFailure) {
						try {
							File outputFile = getActualFile(expectedFile);
							writeReportToStream(differences, plan1, plan2,
									new PrintStream(outputFile));
							System.out.println("Actual output written to " + outputFile);
						} catch (Exception e) {
							System.err.println("Failed to save actual result:  " + e);
						}
					}
				}
			}

	protected File getActualFile(URL expectedFile) throws IOException {
		if (TestUtil.isBamboo()) {
			return File.createTempFile(getClass().getSimpleName()+"-actual", ".html");
		} else {
			String urlString = FileLocator.resolve(expectedFile).toString().replace("expected", "actual");
			return new File(URI.create(urlString));
		}
	}

	protected abstract URL getExpectedFile(String parent);

	protected abstract void writeReportToStream(PlanDiffList differences, EPlan plan1,
			EPlan plan2, OutputStream stream) throws IOException;

	private String readAll(InputStreamReader expectedStream) throws IOException {
		StringBuffer result = new StringBuffer();
		int nextByte;
		do {
			nextByte = expectedStream.read();
			if (nextByte > 0) result.append((char) nextByte);
		}
		while (nextByte > 0);
		return result.toString();
	}

	private void compareResults(String expectedString, String actualString, String startComparingAtLine) {
			final String newlines = "\r?\n";
			String[] expectedLines = expectedString.split(newlines);
			String[] actualLines = actualString.split(newlines);
			int eIndex = 0;
			int aIndex = 0;
			int nLinesCompared = 0;
			int nMismatchedLines = 0;
			if (startComparingAtLine != null) {
				// First, advance each output to the staring position, skipping all the boilerplate
				// and timestamps at the beginning, which will change.
				while (eIndex < expectedLines.length && aIndex < actualLines.length
						&& !expectedLines[eIndex].trim().equals(startComparingAtLine)) {
					eIndex++;
				}
				while (aIndex < expectedLines.length && aIndex < actualLines.length
						&& !actualLines[aIndex].trim().equals(startComparingAtLine)) {
					aIndex++;
				}
				// Make sure we found the starting point in each before proceeding with test.
				if (eIndex >= expectedLines.length) {
					fail ("No '" + startComparingAtLine + "' line seen in expected output.");
				}
				if (aIndex >= actualLines.length) {
					fail ("No '" + startComparingAtLine + "' line seen in actual output.");
				}
			}
			// Now compare the remainder of the files.
			do {
				if (eIndex >= expectedLines.length) fail("Output is longer than expected.");
				if (aIndex >= actualLines.length) fail("Output is shorter than expected.");
	//			if (actualLines[aIndex].contains("name=\"TopLevelTree\"")) {
	//				System.out.println("Context: " + actualLines[aIndex].trim());
	//			}
				if (!expectedLines[eIndex].trim().equals(actualLines[aIndex].trim())) {
					nMismatchedLines++;
					System.err.println("Expected:  " + expectedLines[eIndex].trim());
					System.err.println("Actual:    " + actualLines[aIndex].trim());
				}
				eIndex++;
				aIndex++;
				nLinesCompared++;
			} while (eIndex < expectedLines.length && 
						aIndex < actualLines.length);
			
			assertTrue(nMismatchedLines + " out of " + nLinesCompared + " are not identical to expected output." +
					"\n ---> If code has legitimately changed, temporarily return true from debugging(), inspect new output, and check it in as " +
					" expected.html.",
					   nMismatchedLines == 0);
			
			// Successful match -- reached end of both with no mismatch
			passIfEnoughLinesCompared(nLinesCompared);
		}

	protected void passIfEnoughLinesCompared(int nLinesCompared) {
		assertTrue("Not enough lines compared", nLinesCompared > 100);
	}

	private EPlan getTestCase(String directory, String baseFilename) {
		EPlan result = PlanDiffUtils.loadPlanFromFile(findFile(directory + File.separator + baseFilename + ".plan"),
				toEachItsOwnResourceSet);
		assertNotNull("Failed to load plan", result);
		return result;
	}

}
