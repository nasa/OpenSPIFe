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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.MatchPlanVisitor;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.Unit;

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.osgi.framework.Bundle;

public class TestCsvTests extends AbstractResourceTest {

	private static final String PATH_BASE = "/datafiles/test/csv";
	
	public void testCsvTests() throws URISyntaxException {
		Bundle bundle = ResourcesPlugin.getDefault().getBundle();
		Enumeration<URL> entries = bundle.findEntries(PATH_BASE, "*", false);
		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			URI uri = EMFUtils.createURI(url);
			String folderName = uri.lastSegment();
			if (uri.toString().endsWith("/")) {
				folderName = uri.segment(uri.segmentCount() - 2);
				if (CommonUtils.equals(".svn", folderName)) {
					continue;
				}
			}
			try {
				runTest(folderName);
			} catch (IOException e) {
				AssertionFailedError error = new AssertionFailedError("running test '"+folderName+"': " + e.getMessage());
				error.setStackTrace(e.getStackTrace());
				throw error;
			}
		}
	}

	@SuppressWarnings("null")
	private void runTest(String folderName) throws URISyntaxException, IOException {
		URL adFileUrl = null;
		URL planFileUrl = null;
		URL csvFileUrl = null;
		
		Bundle bundle = ResourcesPlugin.getDefault().getBundle();
		Enumeration<URL> entries = bundle.findEntries(PATH_BASE+"/"+folderName, "*", false);
		while (entries.hasMoreElements()) {
			URL url = entries.nextElement();
			URI uri = EMFUtils.createURI(url);
			String ext = uri.fileExtension();
			if (CommonUtils.equals("dictionary", ext)) {
				adFileUrl = url;
			} else if (CommonUtils.equals("plan", ext)) {
				planFileUrl = url;
			} else if (CommonUtils.equals("csv", ext)) {
				csvFileUrl = url;
			}
		}
		assertNotNull("no dictionary file found in folder "+folderName, adFileUrl);
		assertNotNull("no plan file found in folder "+folderName, planFileUrl);
		assertNotNull("no csv file found in folder "+folderName, csvFileUrl);
		
		load(EMFUtils.createURI(adFileUrl));
		EPlan plan = createPlan(EMFUtils.createURI(planFileUrl));
		InputStream stream = csvFileUrl.openStream();
		try {
			testResources(plan, stream);
		} finally {
			stream.close();
		}
	}
	
	protected void testResources(URI adUri, URI planURI, InputStream stream) throws IOException {
		load(adUri);
		final EPlan plan = createPlan(planURI);
		testResources(plan, stream);
	}
	
	protected void testResources(final EPlan plan, final String csv) {
		testResources(plan, new ByteArrayInputStream(csv.getBytes()));
	}
	
	protected void testResources(final EPlan plan, final InputStream stream) {
		TransactionUtils.reading(plan, new CsvTestRunnable(plan, stream));
	}
	
	private final class CsvTestRunnable implements Runnable {
		
		private final EPlan plan;
		private final InputStream stream;
		private Map<String, EPlanElement> planElementById = new HashMap<String, EPlanElement>();

		private CsvTestRunnable(EPlan plan, InputStream stream) {
			this.plan = plan;
			this.stream = stream;
		}

		@Override
		public void run() {
			try {
				doTestResources(plan, stream);
			} catch (IOException e) {
				fail(e.getMessage());
			} 
		}

		private void doTestResources(final EPlan plan, final InputStream stream) throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line = reader.readLine();
			while (!CommonUtils.isNullOrEmpty(line)) {
				if (line.trim().startsWith("#")) {
					line = reader.readLine();
					continue;
				}
				String[] entries = line.split(",");
				if (entries != null && entries.length > 0) {
					String identifier = entries[0].trim();
					if ("plan".equalsIgnoreCase(identifier)) {
						String dateString = entries[1].trim();
						String profileKey = entries[2].trim();
						String valueString = entries[3].trim();
						Object expectedProfileValue = parseProfileExpectedValue(profileKey, valueString);
						Date date = null;
						try {
							date = DateFormatRegistry.INSTANCE.getDefaultDateFormat().parse(dateString);
						} catch (Exception e1) {
							try {
								date = ISO8601DateFormat.parseISO8601(dateString);
							} catch (Exception e) {
								fail("cannot parse datString '"+dateString+"': "+e1.getMessage());
							}
						}
						try {
							assertProfileValue(plan, profileKey, date, expectedProfileValue, true);
						} catch (AssertionFailedError e) {
							AssertionFailedError x = new AssertionFailedError("failed on line "+line+": "+e.getMessage());
							x.setStackTrace(e.getStackTrace());
							throw x;
						}
					} else if ("planElement".equalsIgnoreCase(identifier)) {
						String elementId = entries[1].trim();
						String resourceKey = entries[2].trim();
						String valueString = entries[3].trim();

						Object epxectedActivityValue = parseProfileExpectedValue(resourceKey, valueString);
						EPlanElement planElement = findElement(plan, elementId);
						assertNotNull("no element with name '"+elementId+"' found", planElement);
						
						assertADEffect(planElement, resourceKey, epxectedActivityValue, true);
					}
				}
				line = reader.readLine();
			}
		}

		private Object parseProfileExpectedValue(String profileKey, String expectedProfileValueString) {
			if (CommonUtils.equals(expectedProfileValueString, "null")) {
				return null;
			}
			Profile profile = getProfile(plan, profileKey);
			Unit units = profile.getUnits();
			if (units != null) {
				try {
					return EnsembleAmountFormat.INSTANCE.parseAmount(expectedProfileValueString, units);
				} catch (Exception e) {
					// all we can do is try
				}
			}
			EDataType eDataType = getEDataType(plan, profileKey);
			if (eDataType != null) {
				return EcoreUtil.createFromString(eDataType, expectedProfileValueString);
			}
			return expectedProfileValueString;
		}

		private EDataType getEDataType(final EPlan plan, String profileKey) {
			Profile profile = getProfile(plan, profileKey);
			EDataType eDataType = profile.getDataType();
			if (eDataType == null) {
				EResourceDef definition = AD.getDefinition(EResourceDef.class, profileKey);
				if (definition != null) {
					eDataType = definition.getEAttributeType();
				}
			}
			return eDataType;
		}

		private EPlanElement findElement(EPlan plan, final String id) {
			EPlanElement planElement = planElementById.get(id);
			if (planElement != null) {
				return planElement;
			}
			MatchPlanVisitor matcher = new MatchPlanVisitor(true) {
				@Override
				protected boolean matches(EPlanElement element) {
					return id.equals(element.getPersistentID())
							|| id.equals(element.getName());
				}
			};
			matcher.visitAll(plan);
			planElement = matcher.getPlanElement();
			if (planElement != null) {
				planElementById.put(id, planElement);
			}
			return planElement;
		}
	}
	
}
