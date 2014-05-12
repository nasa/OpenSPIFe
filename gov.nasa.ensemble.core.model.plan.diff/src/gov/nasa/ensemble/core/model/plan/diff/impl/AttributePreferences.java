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
package gov.nasa.ensemble.core.model.plan.diff.impl;

import gov.nasa.ensemble.common.EnsembleProperties;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EStructuralFeature;

public class AttributePreferences {
	

	private static final String PROPERTY_IGNORED_ATTRIBUTES = "ensemble.plan.diff.ignore";
	
	private static Set<String> ignoredAttributeList = null;

	
	public static boolean isAttributeIgnored(EStructuralFeature parameter) {
		// See:  SPF-4197, SPF-4905.
		// SPF-8666 has problems related to this, but I'm leaving it alone until I have a complete fix.
		return parameter.isDerived() || parameter.isTransient() || isAttributeIgnored(parameter.getName());
	}
	
	public static Set<String> getIgnoredAttributeList() {
		if (ignoredAttributeList==null) {
			ignoredAttributeList = parseIgnoredAttributeList();
		}
		return ignoredAttributeList;
	}
	
	private static boolean isAttributeIgnored(String attributeName) {
		return getIgnoredAttributeList().contains(attributeName);
	}

	private static Set<String> parseIgnoredAttributeList() {
		Set<String> set = new HashSet<String>();
		String propertyString = EnsembleProperties.getProperty(PROPERTY_IGNORED_ATTRIBUTES);
		if (propertyString != null) {
			StringTokenizer tokenizer = new StringTokenizer(propertyString, ",");
			while (tokenizer.hasMoreTokens()) {
				set.add(tokenizer.nextToken());
			}
		}
		return set;
	}


}
