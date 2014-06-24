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
package gov.nasa.ensemble.common.documentation;

import static fj.P.*;
import static fj.data.List.*;
import static fj.function.Strings.*;
import static gov.nasa.ensemble.common.functional.Predicates.*;
import fj.F;
import fj.data.List;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.EnsembleProperties.IEnsemblePropertyGetListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

public class EnsemblePropertiesSurveyor {
	
	static private Map<String, Object> configuredValues = new LinkedHashMap<String, Object>();
	static private Map<String, Collection<Usage>> callers = new LinkedHashMap<String, Collection<Usage>>();
	
	public static void initialize() {
		CommonPlugin plugin = CommonPlugin.getDefault();
		Properties config = plugin.getEnsembleProperties();
		for (String property : plugin.getEnsemblePropertiesInFile()) {
			configuredValues.put(property, config.get(property));
		}
		EnsembleProperties.addListener(new Listener());
	}
	
	static private class Usage {
		String caller;
		String type;
		Object defaultValue;
	
		public Usage(String caller, String type, Object defaultValue, String fetchedConfiguredValue) {
			this.caller = caller;
			this.type = type;
			this.defaultValue = defaultValue;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Usage) {
				Usage other = (Usage) obj;
				return CommonUtils.equals(type, other.type)
						&& CommonUtils.equals(defaultValue, other.defaultValue);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			if (type==null || defaultValue==null) return 0;
			return type.hashCode() ^ defaultValue.hashCode();
		}
	}

	static private class Listener implements IEnsemblePropertyGetListener {

		@Override
		public void propertyValueFetched(String property, String type, Object defaultValue, String fetchedConfiguredValue) {
			if (!callers.containsKey(property)) {
				callers.put(property, new HashSet());
			}
			callers.get(property).add(new Usage(getCallerClassNameFromStack(),
					type, defaultValue, fetchedConfiguredValue));			
		}

	}
	
	// Report generation

	public static void generateReport() {
		header("Properties configured and used");
		generateReportForThoseWithOneCaller();
		header("Properties for which no callers could be found");
		System.out.println("Either these are unused, or the test run didn't exercise the by displaying or generating anything that needed them.");
		generateReportForThoseWithNoCallers();
		header("Properties used by multiple callers");
		generateReportForThoseWithMultipleCallers();
	}

	private static void header(String string) {
		System.out.println("h3. " + string);
	}

	private static TreeSet<String> getAllProperties() {
		TreeSet<String> result = new TreeSet<String>();
		result.addAll(configuredValues.keySet());
		result.addAll(callers.keySet());
		return result;
	}

	public static void generateReportForThoseWithOneCaller() {
		System.out.println("|| Property || caller || type || {{ensemble.properties}} value || Default if taken out");
		for (String property : getAllProperties()) {
			Collection<Usage> callersOfThisProperty = callers.get(property);
			if (callersOfThisProperty != null && callersOfThisProperty.size()==1) {
				Usage use = callersOfThisProperty.iterator().next();
				Object configuredValue = configuredValues.get(property);
				System.out.print("| " + property + " | {{" + use.caller + "}} |_" + use.type + "_| " + displayValue(configuredValue) + " | ");
				if (use.defaultValue == null) {
					System.out.println("_none_");
				} else if (use.defaultValue.equals(configuredValue)) {
					System.out.println("_same_");				
				} else {
					System.out.println(displayValue(use.defaultValue));
				}
			}
		}
		System.out.println(); // blank line, so as not to make next line part of table
	}

	public static void generateReportForThoseWithNoCallers() {
		for (String property : getAllProperties()) {
			Object configuredValue = configuredValues.get(property);
			Collection<Usage> callersOfThisProperty = callers.get(property);
			if (callersOfThisProperty == null || callersOfThisProperty.size()==0) {
				System.out.println("* " + property + ", which is currently set to " + displayValue(configuredValue));
			}
		}
		System.out.println(); // blank line, so as not to make next line part of list
	}
	
	public static void generateReportForThoseWithMultipleCallers() {
		for (String property : getAllProperties()) {
			Object configuredValue = configuredValues.get(property);
			Collection<Usage> callersOfThisProperty = callers.get(property);
			if (callersOfThisProperty != null && callersOfThisProperty.size() > 1) {
				System.out.println("* " + property + ", currently set to " + displayValue(configuredValue));
				System.out.println("  has multiple callers:");
				for (Usage use : callersOfThisProperty) {
					System.out.print("  ** {{" + 
							use.caller +
							"}} uses it as a _" + use.type + "_; ");
					if (use.defaultValue==null) {
						System.out.println("no default.");
					} else  if (use.defaultValue.equals(configuredValue)){
						System.out.println("default is the same as above.");
					} else {
						System.out.println("if not set, would default to " + displayValue(use.defaultValue));
					}
				}
			}
		}
		System.out.println(); // blank line, so as not to make next line part of table
	}

	private static String displayValue(Object value) {
		if (value==null) return "_none_";
		return "{{" + value +  "}}";
	}

	private static String getCallerClassNameFromStack() {
		return getClassNameFromStack(4);
	}

	private static String getClassNameFromStack(int stackIndex) {
		final F<StackTraceElement, String> toClassName = new F<StackTraceElement, String>() {
			@Override
			public String f(final StackTraceElement element) {
				return element.getClassName();
			}
		};
		final List<String> ignorableFragments = list("java.lang", "clover",
				EnsembleProperties.class.getName(), EnsemblePropertiesSurveyor.class.getName());
		final F<String, Boolean> ignore = or(ignorableFragments.map(contains));
		final List<StackTraceElement> stackTrace = list(Thread.currentThread().getStackTrace());
		return stackTrace.map(toClassName).dropWhile(ignore).orHead(p("NULL"));
	}


}
