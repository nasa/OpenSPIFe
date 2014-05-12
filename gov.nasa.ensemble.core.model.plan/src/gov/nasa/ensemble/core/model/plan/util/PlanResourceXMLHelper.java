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
/**
 * 
 */
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;

public class PlanResourceXMLHelper extends XMIHelperImpl {

	private final ISO8601DateFormat ISO8601_DATE_FORMAT = new ISO8601DateFormat();
	
	private static ThreadLocal<Boolean> setFromXML = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	public static boolean isSetFromXML() {
		return setFromXML.get();
	}
	
	public PlanResourceXMLHelper(XMLResource resource) {
		super(resource);
		ISO8601_DATE_FORMAT.setMillisFormatMode(true);
	}

	@Override
	public void setValue(EObject object, EStructuralFeature feature, Object value, int position) {
		if (PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS == feature) {
			EPlanElement pe = (EPlanElement) object;
			EMember newMember = (EMember) value;
			List<EMember> members = pe.getMembers();
			EMember oldMember = pe.getMember(newMember.getKey());
			if (oldMember != null) {
				int index = members.indexOf(oldMember);
				members.set(index, newMember);
			}
			return;
		}
		if (feature instanceof EReference) {
			EReference reference = (EReference) feature;
			Object defaultValue = reference.getDefaultValue();
			String defaultValueLiteral = reference.getDefaultValueLiteral();
			if ((defaultValueLiteral == null) && (defaultValue instanceof List)) {
				setReferenceByIndex(object, value, reference);
				return;
			}
		}
		try {
			setFromXML.set(true);
			super.setValue(object, feature, value, position);
		} finally {
			setFromXML.set(false);
		}
	}

	private EObject currentObject = null;
	private Map<EReference, Integer> referenceIndices = new LinkedHashMap<EReference, Integer>();
	
	@SuppressWarnings("unchecked")
	private void setReferenceByIndex(EObject object, Object value,
			EReference reference) {
		if (currentObject != object) {
			referenceIndices.clear();
			currentObject = object;
		}
		int index = (referenceIndices.containsKey(reference) ? referenceIndices.get(reference) : 0);
		List currentValue = (List) object.eGet(reference);
		if(index >= reference.getUpperBound()) {
			List subList = currentValue.subList(index, currentValue.size());
			currentValue.removeAll(subList);
			return;
		}
		if (index >= currentValue.size()) {
			currentValue.add(value);
		} else {
			currentValue.set(index, value);
		}
		referenceIndices.put(reference, index + 1);
	}
	
	@Override
	public String convertToString(EFactory factory, EDataType dataType, Object value) {
		if (EcorePackage.Literals.EDATE == dataType) {
			return ISO8601_DATE_FORMAT.format((Date) value);
		}
		return super.convertToString(factory, dataType, value);
	}

	@Override
	protected Object createFromString(EFactory factory, EDataType dataType, String value) {
		if (EcorePackage.Literals.EDATE == dataType) {
			try {
				if (value == null || value.trim().length() == 0) {
					return null;
				}
				return ISO8601_DATE_FORMAT.parse(value);
			} catch (ParseException e) {
				// legacy .plan support, ignore and fall through
			}
		}
		return super.createFromString(factory, dataType, value);
	}
	
}
