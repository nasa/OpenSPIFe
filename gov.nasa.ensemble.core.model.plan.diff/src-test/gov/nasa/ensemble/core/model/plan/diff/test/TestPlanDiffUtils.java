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
package gov.nasa.ensemble.core.model.plan.diff.test;

import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import junit.framework.TestCase;

public class TestPlanDiffUtils extends TestCase {
	
	public void test_collapseWhitespace() {
		assertEquals("foo", coll("foo"));
		assertEquals("Single spaces left alone.",
				coll("Single spaces left alone."));
		assertEquals("Double spaces should be collapsed.",
				coll("Double  spaces  should  be collapsed."));
		assertEquals("Multiple spaces should be collapsed.",
				coll("Multiple    spaces  should  be       collapsed."));
		assertEquals("Trim beginning and end",
				coll("  Trim beginning and end  "));
		assertEquals("Trim blank lines at beginning and end",
				coll("\n\n\nTrim blank lines at beginning and end\n\n"));
		assertEquals("Single newlines\nleft alone.",
				coll("Single newlines\nleft alone."));
		assertEquals("Multiple newlines\nshould be collapsed.",
				coll("Multiple newlines\n\n\nshould be collapsed."));
		assertEquals("Whitespace at end of line\nshould be removed.",
				coll("Whitespace at end of line \nshould be removed."));
		assertEquals("Whitespace at end of line\nshould be removed.",
				coll("Whitespace at end of line   \nshould be removed."));
		assertEquals("Whitespace at start of line\nshould be removed.",
				coll("Whitespace at start of line\n   should be removed."));
		assertEquals("Trim combination of horizontal and vertical space at beginning and end",
				coll("  \n Trim combination of horizontal and vertical space at beginning and end\n "));
		assertEquals("Trim combination of horizontal and vertical space at beginning and end",
				coll("\n Trim combination of horizontal and vertical space at beginning and end   \n"));
		
	}

	private String coll(String string) {
		return PlanDiffUtils.collapseWhitespace(string);
	}

}
