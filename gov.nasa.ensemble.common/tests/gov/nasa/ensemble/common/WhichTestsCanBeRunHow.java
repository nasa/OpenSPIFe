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

import gov.nasa.ensemble.common.data.test.SharedTestData;
import gov.nasa.ensemble.common.data.test.TestUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;

/**
 * Ensemble JUnit tests tend to fall into several practical categories
 * of how they can be run manually.  All of them can be run by simulating
 * the way Bamboo will run them, but that has an overhead on the order of
 * tens of seconds, which is inconvenient when the test itself takes less
 * than a second to run once started up.  When used for debugging, as
 * with Test-Driven Development, that makes for a slower debugging cycle.
 * No matter how fast our machines get, the software always seems to
 * be slow enough to take much more time to run a test the slow way than
 * it takes to move the mouse to the Run button and click it.
 * <p>
 * At the other extreme, some tests that are literally unit tests and
 * self-contained can be run as JUnit Test rather than having to be run
 * as JUnit Plugin Test.  That's not only the fastest debugging loop
 * of all (one click, and see a green or red result in about a second),
 * but is also much less effort to set up the first time.
 * <p>
 * Some developers I've talked to either run everything my the slowest
 * and surest mechanism or figure out by trial and error which ones
 * mysteriously work with the quicker shortcuts.  I've been doing the
 * latter, but I've noticed some rules of thumb and figured out some
 * of the underlying reasons why some tests pass one way and get NPE's
 * another way.  I thought I'd give examples of each here so that it
 * can help people run individual tests more efficiently, and maybe
 * to develop their tests to be runnable by the faster methods when possible.
 * 
 * @see https://ensemble.jpl.nasa.gov/confluence/x/roKHAw
 * @see https://ensemble.jpl.nasa.gov/confluence/x/pQDxAg
 * 
 * @author Kanef
 *
 */
public class WhichTestsCanBeRunHow extends TestCase {
	
	/** A sufficiently self-contained test, one that really deserves to
	 * be called a "unit" test, can be launched with the Run As (or Debug As
	 * or Profile As, etc.) JUnit Test, without going through the Run Configurations
	 * wizard.
	 * <p>
	 * An example is the date parser/formatter tests.  As long as they're designed to
	 * test the <em>mechanism</em> of parsing and formatting (e.g., for a Mars LST time
	 * or a Mission Elapsed Time, testing "given this hypothetical start time,
	 * it should parse this string as this date), they can be run this way,
	 * since they don't depend on ensemble.properties.
	 *  <p>
	 * That's how I recommend writing such tests anyway, for several other reasons:
	 * <ul>
	 * <li> We don't want to rewrite
	 * all our test cases when the mission start time changes; as long as we can
	 * show that the mechanism will work.
	 * <li> We don't want to look up whether HH:mm or HH:mm:ss time is configured; we want to test <em>all</em>
	 * likely configurations and make sure they <em>all</em> work.
	 * <li> We want to be able to run the same Common test for different products,
     * not have one expected outcome for MER, one for Phoenix, and one for MSL.
	 * </ul>
	 * @author Kanef
	 * 
	 */
	public void testCanRunAsJunitTest() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		System.out.println("Virtual Machine '" + runtime.getName()
				+ "' started "
				+ DurationFormat.getEnglishDuration(runtime.getUptime()/1000)
				+ " ago.");
		assertEquals(DurationFormat.parseDurationFromHumanInput("90m"),
				DurationFormat.parseDurationFromHumanInput("1:30:00"));	
	}
	
	/** 
	 * If a test needs to access a test file that is located within the plugin,
	 * it can still be run as a plain JUnit Test, but only if it uses the TestUtil
	 * method to find it.  A relative pathname will work with Run As JUnit Test
	 * but won't work on Bamboo.  The mechanism that works on Bamboo requires
	 * looking in the plugin's bundle, which will be null when Run As JUnit Test.
	 * The utility function makes it work transparently either way.
	 */
	
	public void testWithFilesCanStillBeRunAsJunitTest() throws FileNotFoundException {
		assertTrue(TestUtil.findTestFile(CommonPlugin.getDefault(), "schema/IStringifier.exsd").exists());
	}
	
	/**
	 * Any test requiring ensemble.properties to be loaded has to be run 
	 * as a JUnit Plugin Test, but can still be run Headless.
	 * If all the code calling {@link EnsembleProperties} provides defaults,
	 * you may be able to design your test to pass, but if you want
	 * to load the actual configuration that comes with the product,
	 * you will have to run it as JUnit Plugin Test.
	 * <p>
	 * Unless the test checks for it or the code uses ADParameterUtils,
	 * it's not uncommon for a test that loads a plan and tries to
	 * access AD-defined activity parameters to get a NullPointerException
	 * because <code>getData</code> returns null for undefined activities.
	 */
	public void testWithPropertiesRequiresPluginEnvironment() {
		System.out.println("activitydictionary.userchange="
				+ EnsembleProperties.getBooleanPropertyValue("activitydictionary.userchange", false));
		assertTrue("This type of launch does not load ensemble.properties", EnsembleProperties.isConfigured());
		System.out.println("Mission time start is configured as "
				+ EnsembleProperties.getDatePropertyValue("mission.time.start"));
		System.out.println("AD is configured as "
				+ EnsembleProperties.getStringPropertyValue("activitydictionary.location", "(none)"));
	}
	
	/**
	 * Some tests are designed with the idea of testing the default Activity Dictionary checked into
	 * a given product, which they depend on already being loaded.  That will only work if one of
	 * three things has loaded that AD:
	 * <ul>
	 * <li> The test suite itself, e.g. in setUp.
	 * <li> Another test suite that ran before it.  (Depending on that can cause confusing failures.)
	 * <li> Something in the GUI workbench, which won't be created if the test is run Headless
	 * (let alone as JUnit Test).
	 * </ul>
	 * Such tests require setting up the Run Configurations Main tab to "Run a product"
	 * and choosing the appropriate gov.nasa.*.rcp product.  They will actually bring up
	 * a GUI RCP window before running the test.
	 * <p>
	 * An alternative to this is to make your test a subclass of TestCaseWithSpecialAD.
	 * <p>
	 * There are really two competing philosophies in our test suites, because the
	 * products have different needs:
	 * <ol>
	 * <li> MSL, Phoenix, and MER have a user-written Activity Dictionary, with lots
	 * of activity definitions, lots of formulas, which is subject to change even after the software is frozen.
	 * The Ensemble products are more data-driven and have fewer dependencies in the
	 * code about choices made in the AD.
	 * <li> Score and Apex have very few activity definitions, but a lot of parameters
	 * that are heavily intertwined with the code, mostly because they're used by
	 * the interchange format importers and exporters.  Running a test on those importers
	 * and exporters requires something close to the latest AD.  Testing the Plan Integration
	 * mechanism does not.
	 * </ol>
	 * 
	 */
	public void testCannotRunHeadless() {
		if (!Platform.isRunning()) {
			fail("This test won't run headless.");
		}
		// Can't demonstrate ActivityDictionary use from a Common plugin:
//		assertFalse("No AD", ActivityDictionary.getInstance().getActivityDefs().isEmpty());
	}
	
	/* TODO:  Tests that call getMember on plan structures are another example
	 * of tests that can't be run headless.  (Something to do with member initialization code
	 * not being not called).  Might be nice to demonstrate that too, although it won't
	 * actually compile in the Common plugin.  Consider making extended examples that
	 * are subclasses of this class and can actually be run.
	 */
	
	/**
	 * In the Run Configurations Plugin tab, either of two choices usually works an
	 * gives the same result:  After choosing to use only plugins listed and press the Deselect
	 * button, either
	 * <ol>
	 * <li> Add Working Set and choose the product you're testing it for, or
	 * <li> Paste in the name of the test plugin from the Main tab, select it,
	 * show all plugins, and Add Required Plugins.
	 * </ol>
	 * 
	 * The second method results in fewer plugins.  Therefore, it will start up faster,
	 * but will not have all the extensions that the actual test run on Bamboo
	 * will have when the test is run as part of a given product.
	 * 
	 * <p>
	 * 
	 * Most tests will give the same result whichever way they are run,
	 * because they have all the plugins that they need.  The exception is
	 * when a plugin adds an extension that affects what the test does.
	 * For example, a Mars product might change the default stringifier
	 * to use Mars time.  If run with all the plugins of the product,
	 * the test may pass or fail when it would give a different result
	 * when run with just the subset of plugins required to make it run.
	 * In that case, be sure to use the slower, higher fidelity method.
	 * But also be sure to think about whether the test will be run on
	 * Bamboo on all products and give a different result for each one.
	 * <p>
	 * This example will pass if run headless or with Required Plugins only,
	 * or with most products' Working Sets.  It will fail if run with
	 * a Mars Time extension added, e.g. if run with the Working Set
	 * of the MSLICE feature.
	 */
	public void testAffectedByWhichPluginsAreIncluded() {
		assertEquals(DateStringifier.class, StringifierRegistry.getStringifier(Date.class).getClass());
	}
	
	/**
	 * When using SharedTestData, your test uses data that is not packaged into the product,
	 * which means it's not part of the feature.  So you have to check it out manually when
	 * you need it, and when setting up a test configuration to run, you have to do
	 * the one last step of checking the test data plugin box (search for "test" to find it).
	 */
	public void testUsesSharedTestData() {
		try {
			SharedTestData.findTestDataURI("gov.nasa.ensemble.ngps", "schedule.plan");
		} catch (IOException e) {
			fail(e.toString());
		}
	}
	
}
