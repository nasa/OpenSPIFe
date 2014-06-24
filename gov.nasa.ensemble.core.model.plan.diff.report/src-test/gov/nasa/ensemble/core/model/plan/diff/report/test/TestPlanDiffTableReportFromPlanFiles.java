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

import gov.nasa.ensemble.common.time.DurationFormat.DurationType;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.table.PlanDiffOutputAsTableHTML;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestPlanDiffTableReportFromPlanFiles extends
		AbstractTestPlanDiffReportFromPlanFiles {
	
	@Override
	protected void passIfEnoughLinesCompared(int nLinesCompared) {
		assertTrue("No lines compared", nLinesCompared > 0);
	}

	@Override
	protected String getContentOfLineToSkipTo() {
		// We make the publication heading unchanging, so no need.
		// Also, we suppress indentation to avoid whitespace around IND and DEL,
		// so it's all one big line and we can't skip anyway.
		return null;
	}

	@Override
	protected void writeReportToStream(PlanDiffList differences, EPlan plan1,
			EPlan plan2, OutputStream stream) throws IOException {
		new ReproduciblePlanDiffOutputAsTableHTML().writeToStream(differences, plan1, plan2,
				"This is a test of the Plan Diff report." +
				"  If a real caller has a place for the planner to type a summary, it would go here.",
				stream);
	}

	@Override
	protected URL getExpectedFile(String parent) {
		return findFile(parent + File.separator + "expected-table-report.html");
	}
	
	public void testCamelCaseConversion() {
		assertEquals("Foo Bar", PlanDiffUtils.convertFromCamelToPretty("Foo_Bar"));
		assertEquals("Foo Bar", PlanDiffUtils.convertFromCamelToPretty("fooBar"));
		assertEquals("Fee Fie Foe Foo", PlanDiffUtils.convertFromCamelToPretty("feeFieFoeFoo"));
	}
	
	private static final Date CONSTANT_DATE = ISO8601DateFormat.parseISO8601("2004-01-03T04:35:00");
	private static final Object CONSTANT_USER = TestPlanDiffTableReportFromPlanFiles.class.getSimpleName();
	
	private class ReproduciblePlanDiffOutputAsTableHTML extends PlanDiffOutputAsTableHTML {
		
		
		
		public ReproduciblePlanDiffOutputAsTableHTML() {
			super();
			// Make test and expected output self-contained, independent of ensemble.properties config
			durationStringifier = new DurationStringifier(DurationType.D_SLASH_HM);
		}

		@Override
		protected void adjustTextDiffParameters() {
			// Try to make result timing independent.
			textDiffEngine.Diff_Timeout = 0f;
		}

		@Override
		protected String getReportPublicationLine() {
			String formatString = "test output published %s (NOT!) by %s";
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy HH:mm (zzz)");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return String.format(formatString,
					dateFormat.format(CONSTANT_DATE),
					CONSTANT_USER);
		}
		
		@SuppressWarnings("deprecation")
		@Override
		/** Indent test output for easier troubleshooting, even if it does cause extraneous space to be rendered around INT and DEL */
		protected void doSerializationCustomizations() {
			format.setIndenting(true);
			format.setIndent(4);
		}
	}

}
