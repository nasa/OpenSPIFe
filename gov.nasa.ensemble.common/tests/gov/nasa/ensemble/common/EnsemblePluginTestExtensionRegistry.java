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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.extension.BasicExtensionRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.reflection.ReflectionUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import junit.framework.JUnit4TestAdapter;
import junit.framework.JUnit4TestCaseFacade;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * Run this class as a junit plugin test to run all of the junit plugin tests that have been registered using this extension point.
 * 
 * For an example of implementing this extension point, see the plugin.xml for this plugin.
 * 
 * @author Andrew
 * @see IEnsemblePluginTest
 */
public class EnsemblePluginTestExtensionRegistry extends BasicExtensionRegistry<IEnsemblePluginTest> {

	private static final String ID = "gov.nasa.ensemble.common.IEnsemblePluginTest";
	private static EnsemblePluginTestExtensionRegistry instance = new EnsemblePluginTestExtensionRegistry();

	private EnsemblePluginTestExtensionRegistry() {
		super(IEnsemblePluginTest.class, ID);
	}

	public static EnsemblePluginTestExtensionRegistry getInstance() {
		return instance;
	}

	public static Test suite() {
		TestSuite testSuite = testSuite();
		return testSuite;
	}

	public static TestSuite testSuite() {
		List<ITestSuiteFilter> noFilters = Collections.emptyList();
		return testSuite(noFilters);
	}

	public static TestSuite testSuite(List<ITestSuiteFilter> filters) {
		EPTERTestSuite suite = new EPTERTestSuite("EnsemblePluginTestExtensionRegistry");
		List<IEnsemblePluginTest> tests = getInstance().getInstances();
		for (final IEnsemblePluginTest test : tests) {
			try {
				boolean accept = filters.isEmpty();
				for (ITestSuiteFilter filter : filters) {
					accept = accept || filter.acceptTestSuite(test);
				}
				if (accept) {
					test.addTests(suite);
				}
			} catch (final Throwable t) {
				if (t instanceof ThreadDeath) {
					throw (ThreadDeath) t;
				}
				LogUtil.error("couldn't load test: " + test, t);
				suite.addTest(new Test() {
					@Override
					public int countTestCases() {
						return 1;
					}

					@Override
					public void run(TestResult result) {
						result.addError(this, t);
					}

					@Override
					public String toString() {
						return t.getMessage();
					}
				});
			}
		}
		suite.sort();
		return suite;
	}

	private static String getLabel(Test test) {
		String string = test.toString();
		// int start = string.indexOf('(');
		// int end = string.indexOf(')', start);
		// String label = string.substring(start + 1, end);
		return string;
	}

	private static final class EPTERTestSuite extends TestSuite {

		private static final String TESTS_FILENAME = "tests.txt";

		private EPTERTestSuite(String name) {
			super(name);
		}

		@Override
		public void run(TestResult result) {
			super.run(result);
			File tests = new File(TESTS_FILENAME);
			try {
				FileWriter fileWriter = new FileWriter(tests);
				BufferedWriter writer = new BufferedWriter(fileWriter);
				writeClassNames(result.errors(), writer);
				writeClassNames(result.failures(), writer);
				writer.close();
			} catch (IOException e) {
				if (!(e instanceof FileNotFoundException)) {
					LogUtil.error(e);
				}
			}
		}

		private void writeClassNames(Enumeration<TestFailure> failures, Writer writer) throws IOException {
			while (failures.hasMoreElements()) {
				TestFailure failure = failures.nextElement();
				Test test = failure.failedTest();
				writeTest(writer, test);
			}
		}

		private void writeTest(Writer writer, Test test) throws IOException {
			if (test instanceof TestSuite) {
				TestSuite suite = (TestSuite) test;
				Enumeration<Test> tests = suite.tests();
				while (tests.hasMoreElements()) {
					Test t = tests.nextElement();
					writeTest(writer, t);
				}
			} else {
				String string = getLabel(test);
				writer.write(string);
				writer.write("\n");
			}
		}

		public void sort() {
			File tests = new File(TESTS_FILENAME);
			try {
				FileReader fileReader = new FileReader(tests);
				BufferedReader reader = new BufferedReader(fileReader);
				List<String> classNames = new ArrayList<String>();
				while (true) {
					String line = reader.readLine();
					if (line == null) {
						break;
					}
					classNames.add(line);
				}
				reader.close();
				Vector<Test> fTests = (Vector<Test>) ReflectionUtils.get(this, "fTests");
				FailingPriorityComparator comparator = new FailingPriorityComparator(classNames);
				Collections.sort(fTests, comparator);
			} catch (IOException e) {
				if (!(e instanceof FileNotFoundException)) {
					LogUtil.error(e);
				}
			}
		}

	}

	public static class FailingPriorityComparator implements Comparator<Test> {

		private final List<String> classNames;

		public FailingPriorityComparator(List<String> classNames) {
			this.classNames = classNames;
		}

		@Override
		public int compare(Test test1, Test test2) {
			int i1 = getIndex(test1);
			i1 = (i1 == -1 ? classNames.size() * 2 : i1);
			int i2 = getIndex(test2);
			i2 = (i2 == -1 ? classNames.size() * 2 : i2);
			return i1 - i2;
		}

		public int getIndex(Test test) {
			if (test instanceof TestCase) {
				String label = getLabel(test);
				return classNames.indexOf(label);
			}
			if (test instanceof JUnit4TestCaseFacade) {
				String label = getLabel(test);
				return classNames.indexOf(label);
			}
			if (test instanceof TestSuite) {
				TestSuite suite = (TestSuite) test;
				int index = -1;
				Enumeration<Test> tests = suite.tests();
				while (tests.hasMoreElements()) {
					Test t = tests.nextElement();
					index = Math.max(index, getIndex(t));
				}
				return index;
			}
			if (test instanceof JUnit4TestAdapter) {
				JUnit4TestAdapter adapter = (JUnit4TestAdapter) test;
				int index = -1;
				List<Test> tests = adapter.getTests();
				for (Test t : tests) {
					index = Math.max(index, getIndex(t));
				}
				return index;
			}
			return -1;
		}

	}

}
