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

import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.report.html.trees.PlanDiffOutputAsTreeHTML;
import gov.nasa.ensemble.core.model.plan.diff.test.TestPlanDiffSyntheticThorough;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.ChronologicalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeBottomUp;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeByName;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeFlat;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeTopDown;
import gov.nasa.ensemble.core.model.plan.diff.trees.SortCombinedPlan;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;



/**
 * File-based test.  Unlike some others, this ignores changes for test purposes,
 * since some of the test cases are so big.
 * @author kanef
 * @see {@link TestPlanDiffSyntheticThorough}
 */
public class TestPlanDiffTreeHtmlFromPlanFiles extends AbstractTestPlanDiffReportFromPlanFiles {
	
	private static final SimpleDateFormat DATE_FORMAT_THAT_WAS_USED_TO_GENERATE_EXPECTED_REPORT
		= new SimpleDateFormat("EEEE 'at' HH:mm '(in format used for JUnit test purposes)'");
	{
	DATE_FORMAT_THAT_WAS_USED_TO_GENERATE_EXPECTED_REPORT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	@Override
	protected String getContentOfLineToSkipTo() {
		return "<HR>";
	}
	
	@Override
	protected void writeReportToStream(PlanDiffList differences, EPlan plan1, EPlan plan2, OutputStream actualStream) throws IOException {
		PlanDiffOutputAsTreeHTML reportGenerator = new PlanDiffOutputAsTreeHTML();
		reportGenerator.setActivityGroupName("sequences"); // make test independent of ensemble.properties config changes
		reportGenerator.setDateStringifier(new DateStringifier(DATE_FORMAT_THAT_WAS_USED_TO_GENERATE_EXPECTED_REPORT));
		List<AbstractDiffTree> diffTrees = getDiffTrees(differences, plan1, plan2);
		reportGenerator.writeToStream(
				diffTrees, differences, plan1, plan2,
				new PrintStream(actualStream));
	}

	/**
	 * Must match what real caller of PlanDiffOutputAsHTML uses.
	 * @param plan1 
	 * @param plan2 
	 */
	private List<AbstractDiffTree> getDiffTrees (PlanDiffList differences, EPlan plan1, EPlan plan2) {
		List<AbstractDiffTree> trees = new ArrayList<AbstractDiffTree>(2);
		Comparator<PlanDiffNode> originalOrder = new SortCombinedPlan(plan1, plan2, differences);
		Comparator<PlanDiffNode> alphabeticalOrder = new AlphabeticalOrder();
		Comparator<PlanDiffNode> chronologicalOrder = new ChronologicalOrder();
		trees.add(new PlanDiffTreeFlat(differences, chronologicalOrder));
		trees.add(new PlanDiffTreeTopDown(differences, originalOrder));
		trees.add(new PlanDiffTreeBottomUp(differences, originalOrder));
		trees.add(new PlanDiffTreeByName(differences, alphabeticalOrder));
		return trees;
	}
	
	@Override
	protected URL getExpectedFile(String parent) {
		return findFile(parent + File.separator + "expected-tree-report.html");
	}

}
