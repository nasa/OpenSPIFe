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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.data.test.TestUtil;

import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestUDict extends TestCase {

	private static final Map<String, String> udictToJScience = new HashMap<String, String>();
	private static final UnitFormat TEST_FORMAT = TestUnitFormatCreator.newTestFormat();
	
	static {
		udictToJScience.put("m2", "m^2");
		udictToJScience.put("m/sec2", "m/s^2");
		udictToJScience.put("kgm2", "kg*(m^2)");
		udictToJScience.put("N-m/radian", "Nm/radian");

		udictToJScience.put("kg2m4", "(kg^2)*(m^4)");
		udictToJScience.put("kg2m4/sec", "(kg^2)*(m^4)/s");
		
		udictToJScience.put("rad2", "rad^2");
		udictToJScience.put("rad2/sec", "(rad^2)/sec");
		udictToJScience.put("rad/sec2", "rad/(sec^2)");
		udictToJScience.put("rad2/sec2", "(rad^2)/(sec^2)");
		udictToJScience.put("sec2/rad", "(sec^2)/rad");
		udictToJScience.put("sec2/rad2", "(sec^2)/(rad^2)");
		udictToJScience.put("sec2/rad3", "(sec^2)/(rad^3)");
		udictToJScience.put("rad2/sec3", "(sec^2)/(sec^3)");
		udictToJScience.put("in2", "in^2");
		udictToJScience.put("sec/g2", "sec/(g^2)");
		udictToJScience.put("sec/g3", "sec/(g^3)");
		udictToJScience.put("A-hrs", "A*h");
		udictToJScience.put("Amp-hrs", "A*h");
		udictToJScience.put("Amp-hrs/sec", "A*h/s");
		udictToJScience.put("A-hrs2", "A*(h^2)");
		udictToJScience.put("A-hrs3", "A*(h^3)");
		udictToJScience.put("amps/degC2", "A/(C^2)");
		udictToJScience.put("amps/degC4", "A/(C^4)");
		udictToJScience.put("amps/Nm/degC2", "A/Nm/(C^2)");
		udictToJScience.put("amps/Nm/degC4", "A/Nm/(C^4)");
		udictToJScience.put("log(millivolts)", "log(mV)");
	}

	@Test
	public void testUdict() throws Exception {
		URL url = TestUtil.findTestData(Activator.getDefault(), "files/udict.xml");
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
		NodeList list = document.getElementsByTagName("UnitCategory");
		for (int i=0; i<list.getLength(); i++) {
			Element element = (Element) list.item(i);
			String categoryName = element.getAttribute("Name");
			
			NodeList referenceUnitList = element.getElementsByTagName("RefUnit");
			assertEquals("Expected 1 reference unit for category '"+categoryName+"'", 1, referenceUnitList.getLength());

			Element refUnitElement = (Element)referenceUnitList.item(0);
			System.out.print(categoryName + " - ");
			debugUnit(refUnitElement);
	
			if (categoryName.equals("cpu_interval")
				|| categoryName.equals("gst")
				|| categoryName.equals("abs_time")
			) {
				continue;
			}
			
			Unit unit = parseUnitElement(refUnitElement);
			assertNotNull("Unit category '"+categoryName+"' base unit not parsed", unit);
			
			NodeList unitList = element.getElementsByTagName("Unit");
			for (int j=0; j<unitList.getLength(); j++) {
				Element subUnitElement = (Element) unitList.item(j);

				Element factorElement = (Element)((Element)subUnitElement.getElementsByTagName("Conversion").item(0)).getElementsByTagName("Factor").item(0);
				
				String invertString = factorElement.getAttribute("Invert");
				boolean invert = invertString != null && Boolean.parseBoolean(invertString);
				
				String factorString = (factorElement).getAttribute("Value");
				double factor = Double.parseDouble(factorString);
				if (invert) {
					factor = 1/factor;
				}
				
				Unit subUnit = parseUnitElement(subUnitElement);
				if (subUnit != null) {
					// TODO: Equivalence check regarding the factor
					continue;
				}
				
				String subUnitAbbreviation = subUnitElement.getAttribute("Abbrev");
				
				if (factor == 1.0) {
					TEST_FORMAT.alias(unit, subUnitAbbreviation);
				} else {
					TEST_FORMAT.alias(unit.times(factor), subUnitAbbreviation);
				}
				
				System.out.print("\t");
				debugUnit(subUnitElement);

				subUnit = parseUnitElement(subUnitElement);
				assertNotNull("Unit category '"+categoryName+"' sub unit not parsed", subUnit);
			}
		}
		
		list = document.getElementsByTagName("SoloUnitCategory");
		for (int i=0; i<list.getLength(); i++) {
			Element element = (Element) list.item(i);
			Unit unit = parseUnitElement(element);
			if (unit == null) {
				System.out.print("SoloUnit: ");
				debugUnit(element);
			}
		}
		
		testGenerateConfluenceOutput();
	}

	public void testGenerateConfluenceOutput() throws Exception {
		URL url = TestUtil.findTestData(Activator.getDefault(), "files/udict.xml");
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
		NodeList list = document.getElementsByTagName("UnitCategory");
		
		System.out.println("||udict NAME ||udict ABBREV ||MSLICE||");
		for (int i=0; i<list.getLength(); i++) {
			Element element = (Element) list.item(i);
			
			NodeList referenceUnitList = element.getElementsByTagName("RefUnit");
			assertEquals("Expected 1 reference unit for category '"+element.getAttribute("Name")+"'", 1, referenceUnitList.getLength());

			Element refUnitElement = (Element)referenceUnitList.item(0);
			printConfluenceTableRow(refUnitElement);
			
			NodeList unitList = element.getElementsByTagName("Unit");
			for (int j=0; j<unitList.getLength(); j++) {
				Element subUnitElement = (Element) unitList.item(j);
				printConfluenceTableRow(subUnitElement);
			}
		}
		
		list = document.getElementsByTagName("SoloUnitCategory");
		for (int i=0; i<list.getLength(); i++) {
			Element element = (Element) list.item(i);
			printConfluenceTableRow(element);
		}
	}

	private void printConfluenceTableRow(Element refUnitElement) {
		Unit unit = parseUnitElement(refUnitElement);
		
		String unitString = null;
		if (unit == null) {
			unitString = "(x)";
		} else if (unit == Unit.ONE) {
			unitString = "Dimensionless";
		} else {
			unitString = UnitFormat.getUCUMInstance().format(unit);
		}
		System.out.println("| *"+ refUnitElement.getAttribute("Name")+"* | "+refUnitElement.getAttribute("Abbrev")+" | "+unitString+" |");
	}
	
	private void debugUnit(Element unitElement) {
		Unit unit = parseUnitElement(unitElement);

		String unitName = unitElement.getAttribute("Name");
		String unitAbbreviation = unitElement.getAttribute("Abbrev");
		System.out.print(unitName+" ("+unitAbbreviation+")");
		if (unit == null) {
			System.out.println(" - unresolved");
		} else {
			System.out.println();
		}
	}

	private Unit parseUnitElement(Element unitElement) {
		String unitName = unitElement.getAttribute("Name");
		String unitAbbreviation = unitElement.getAttribute("Abbrev");
		
		Unit unit = null;
		try {
			
			unit = (Unit) TEST_FORMAT.parseObject(convert(unitName));
		} catch (ParseException e) {
			// null indicates error state
		}
		try {
			unit = (Unit) TEST_FORMAT.parseObject(convert(unitAbbreviation));
		} catch (ParseException e) {
			// null indicates error state
		}
		return unit;
	}
	
	private String convert(String uName) {
		String u = udictToJScience.get(uName);
		return u == null ? uName : u;
	}
	
}
