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
package gov.nasa.ensemble.core.jscience.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.measure.unit.Unit;

import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EDataType;

public class ProfileTemplate {
	private String id;
	private String name;
	private String category;
	private String dataTypeString;
	private Unit units;
	private INTERPOLATION interpolation;
	private Map<String, String> attributes;
	private boolean externalCondition;

	public ProfileTemplate(String id, String name, String category, String dataTypeString, Unit units, INTERPOLATION interpolation, boolean externalCondition) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.dataTypeString = dataTypeString;
		this.units = units;
		this.interpolation = interpolation;
		this.externalCondition = externalCondition;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public EDataType getDataType() {
		return EMFUtils.createEDataTypeFromString(dataTypeString);
	}

	public INTERPOLATION getInterpolation() {
		return interpolation;
	}
	
	public boolean isExternalCondition() {
		return externalCondition;
	}

	public void addAttribute(String key, String value) {
		if (attributes == null)
			attributes = new HashMap<String, String>();
		attributes.put(key, value);
	}
	
	public Map<String, String> getAttributes() {
		if (attributes == null)
			return Collections.EMPTY_MAP;
		return attributes;
	}

	public Profile createInstance() {
		Profile toRet = JScienceFactory.eINSTANCE.createProfile();
		toRet.setId(id);
		toRet.setName(name);
		toRet.setCategory(category);
		toRet.setDataType(getDataType());
		toRet.setInterpolation(interpolation);
		toRet.setUnits(units);
		toRet.setExternalCondition(externalCondition);
		toRet.getAttributes().map().putAll(getAttributes());
		toRet.setValid(true);
		return toRet;
	}
}
