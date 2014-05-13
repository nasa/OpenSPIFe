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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.jscience.physics.amount.Amount;

public class ProfileHandler<T> {
	protected List<DataPoint<T>> dataPoints = new ArrayList<DataPoint<T>>();
	protected Map<String, String> attributes = new LinkedHashMap<String, String>();
	private String name;
	private String id;
	private String category;
	private Unit units;
	private EDataType dataType;
	private Boolean externalCondition;
	private boolean allowImplicitProfile = true;

	public ProfileHandler() {
		super();
		Profile toRet = JScienceFactory.eINSTANCE.createProfile();
		toRet.setInterpolation(INTERPOLATION.STEP);
	}

	public void addDataPoint(Date time, Object value) {
		if ((value instanceof Float) || (value instanceof Double)) {
			Number number = (Number) value;
			if (number.longValue() == number.doubleValue()) {
				value = number.longValue();
			}
		}
		DataPoint dp = JScienceFactory.eINSTANCE.createEDataPoint(time, value);
		dataPoints.add(dp);
	}

	public String getAttribute(String name) {
		return attributes.get(name.toLowerCase());
	}

	public String getCategory() {
		return category;
	}
	
	public void setAllowImplicitProfile(boolean allowImplicitProfile) {
		this.allowImplicitProfile = allowImplicitProfile;
	}

	public Profile getProfile() {
		String id = this.id;
		if (id == null) {
			id = ProfileUtil.getId(name);
			//LogUtil.error("ProfileHandler requires an id to construct profiles, and no id was found for " + name + " : " + category);
			//return null;			
		}
		
		Profile toRet = ProfileConfig.INSTANCE.createProfile(id);
		if (toRet == null) {
			if (allowImplicitProfile) {
				toRet = JScienceFactory.eINSTANCE.createProfile();
				toRet.setId(id);
				if (externalCondition == null)
					toRet.setExternalCondition(true);
				toRet.setValid(true);
			} else {
				LogUtil.error("Implcit Profile construction is not allowed, and no definition exists for " + id);
				return null;
			}
		}

		if (toRet.getName() == toRet.getId() && name != null) {
			toRet.setName(name);
		}

		if (toRet.getCategory() == null) {
			toRet.setCategory(category);
		} else if (category != null && !toRet.getCategory().equals(category)) {
			// LogUtil.warn("[" + id + "] Import category not consistent with configuration file. \"" + category + "\" != \"" + toRet.getCategory() + "\"");
		}
		
		if (externalCondition != null)
			toRet.setExternalCondition(externalCondition);

		if (toRet.getUnits() == null || toRet.getUnits().equals(Unit.ONE)) {
			toRet.setUnits(units);
		} else if (units != null && !toRet.getUnits().equals(units)) {
			String importUnits = units == null? null : EnsembleUnitFormat.INSTANCE.format(units);
			String canonUnits = EnsembleUnitFormat.INSTANCE.format(toRet.getUnits());
			LogUtil.warn("[" + id + "] Import units not consistent with configuration file. " + importUnits + " != " + canonUnits);
		}

		if (toRet.getDataType() == null) {
			toRet.setDataType(dataType);
		} else if (dataType != null && !CommonUtils.equals(toRet.getDataType().getInstanceClass(), dataType.getInstanceClass())) {
			String importDataType = dataType == null? null : EMFUtils.convertToString(dataType);
			String canonDataType = EMFUtils.convertToString(toRet.getDataType());
			LogUtil.warn("[" + id + "] Import data type not consistent with configuration file. " + importDataType + " != " + canonDataType);
		}

		toRet.getAttributes().putAll(attributes);

		if (toRet.getDataType() == null) {
			EDataType type = ProfileUtil.getInferredType(dataPoints);
			toRet.setDataType(type);
		}
		if (dataPoints != null && dataPoints.size() != 0) {
			setDataPointsCanonically(toRet);
		}
		return toRet;
	}

	public List<DataPoint<T>> getDataPoints() {
		return dataPoints;
	}

	public EDataType getDataType() {
		return dataType;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Unit getUnits() {
		return units;
	}

	public void setAttribute(String name, String value) {
		attributes.put(name.toLowerCase(), value);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setDataType(EDataType dataType) {
		this.dataType = dataType;
	}

	public void setDataType(String string) {
		this.dataType = EMFUtils.createEDataTypeFromString(string);
	}
	
	public void setExternalCondition(boolean externalCondition) {
		this.externalCondition = externalCondition;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUnits(String units) {
		try {
			this.units = EnsembleUnitFormat.INSTANCE.parse(units);
		}
		catch (ParseException e) {
			LogUtil.errorOnce("Failed to parse \"" + units + "\" as a unit.");
		}
	}

	public void setUnits(Unit units) {
		this.units = units;
	}

	private void setDataPointsCanonically(Profile toRet) {
		Collections.sort(dataPoints, DataPoint.DEFAULT_COMPARATOR);
		Unit units = toRet.getUnits();
		if (units == null) {
			units = ProfileUtil.getInferredUnit(dataPoints);
			if (units != null) {
				toRet.setUnits(units);
			}
		}
		// unifyType(toRet.getDataType(), units);
		dataPoints = ProfileUtil.stripNullTransitions(dataPoints);
		dataPoints = ProfileUtil.stripRedundantMidpoints(dataPoints, false);
		toRet.setDataPoints(dataPoints);
		toRet.setValid(true);
	}

	@SuppressWarnings("unused")
	private void unifyType(EDataType type, Unit profileUnits) {
		Boolean previous = null;
		for (DataPoint dp : dataPoints) {
			Object value = ProfileUtil.convertToType(type, dp.getValue());
			if (value instanceof Amount) {
				Amount amount = (Amount) value;
				Unit amountUnit = amount.getUnit();
				if ((profileUnits != null) && !amountUnit.equals(profileUnits)) {
					Amount profileValue = amount.to(profileUnits);
					dp.setValue(profileValue);
				}
			}
			if (value instanceof Number) {
				Number number = (Number) value;
				if (profileUnits != null)
					value = AmountUtils.valueOf(number, profileUnits);
				else
					value = AmountUtils.valueOf(number, Unit.ONE);
				dp.setValue(value);
			}
			if (type == EcorePackage.Literals.EBOOLEAN_OBJECT) {
				if ((previous == Boolean.TRUE) && (value == null)) {
					value = Boolean.FALSE;
					dp.setValue(value);
				}
				previous = (Boolean) value;
			}
		}
	}

	
}
